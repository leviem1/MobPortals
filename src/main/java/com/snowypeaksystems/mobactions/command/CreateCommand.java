package com.snowypeaksystems.mobactions.command;

import static com.snowypeaksystems.mobactions.util.Messages.gm;

import com.snowypeaksystems.mobactions.data.MobData;
import com.snowypeaksystems.mobactions.player.IStatus;
import com.snowypeaksystems.mobactions.player.MobActionsUser;
import com.snowypeaksystems.mobactions.player.PermissionException;
import com.snowypeaksystems.mobactions.player.PlayerException;
import com.snowypeaksystems.mobactions.util.DebugLogger;

public class CreateCommand implements ICreateCommand {
  private final MobData data;

  public CreateCommand(MobData data) {
    this.data = data;
  }

  @Override
  public void run(MobActionsUser player) throws PlayerException {
    DebugLogger.getLogger().log("Setting create mode");
    if (!player.canCreate()) {
      DebugLogger.getLogger().log("Permission error");
      throw new PermissionException();
    }

    player.getStatus().setMode(IStatus.Mode.CREATING);
    player.getStatus().setMobData(data);
    player.sendMessage(gm("create-command"), gm("edit-cancel"));
    DebugLogger.getLogger().log("Create mode set");
  }
}
