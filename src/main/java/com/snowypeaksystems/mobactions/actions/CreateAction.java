package com.snowypeaksystems.mobactions.actions;

import static com.snowypeaksystems.mobactions.util.Messages.gm;

import com.snowypeaksystems.mobactions.IInteractiveMob;
import com.snowypeaksystems.mobactions.InteractiveMobAlreadyExistsException;
import com.snowypeaksystems.mobactions.data.MobData;
import com.snowypeaksystems.mobactions.event.CreateIInteractiveMobEvent;
import com.snowypeaksystems.mobactions.player.IStatus;
import com.snowypeaksystems.mobactions.player.MobActionsUser;
import com.snowypeaksystems.mobactions.player.PermissionException;
import com.snowypeaksystems.mobactions.player.PlayerException;
import com.snowypeaksystems.mobactions.util.DebugLogger;
import org.bukkit.Bukkit;

public class CreateAction implements ICreateAction {
  private final IInteractiveMob mob;

  /** Create an action to run when a player creates a MobAction mob with the given data. */
  public CreateAction(IInteractiveMob mob) {
    this.mob = mob;
  }

  @Override
  public void run(MobActionsUser player) throws PlayerException {
    DebugLogger.getLogger().log("Creating mob");
    final MobData data = player.getStatus().getMobData();
    player.getStatus().setMode(IStatus.Mode.NONE);

    if (!player.canCreate()) {
      DebugLogger.getLogger().log("Permission error");
      throw new PermissionException();
    }

    if (mob.exists()) {
      DebugLogger.getLogger().log("Found existing mob");
      throw new InteractiveMobAlreadyExistsException();
    }

    CreateIInteractiveMobEvent event = new CreateIInteractiveMobEvent(player, mob, data);
    Bukkit.getPluginManager().callEvent(event);
    if (!event.isCancelled()) {
      mob.setData(data);
      mob.store();

      player.sendMessage(gm("action-create-success"));
      DebugLogger.getLogger().log("Mob created");
    } else {
      DebugLogger.getLogger().log("Event cancelled");
    }
  }
}
