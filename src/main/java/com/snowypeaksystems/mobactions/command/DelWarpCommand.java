package com.snowypeaksystems.mobactions.command;

import static com.snowypeaksystems.mobactions.util.Messages.gm;

import com.snowypeaksystems.mobactions.player.MobActionsUser;
import com.snowypeaksystems.mobactions.player.PermissionException;
import com.snowypeaksystems.mobactions.player.PlayerException;
import com.snowypeaksystems.mobactions.util.DebugLogger;
import com.snowypeaksystems.mobactions.warp.IWarpManager;
import com.snowypeaksystems.mobactions.warp.WarpNotFoundException;
import org.bukkit.Bukkit;

public class DelWarpCommand implements IDelWarpCommand {
  private final String name;
  private final IWarpManager warpManager;

  /** Creates a DelWarpCommand object. Can be performed by the console. */
  public DelWarpCommand(String name, IWarpManager warpManager) {
    this.name = name;
    this.warpManager = warpManager;
  }

  @Override
  public void run(MobActionsUser player) throws PlayerException {
    DebugLogger.getLogger().log("Deleting warp");
    if (!player.canRemoveWarp()) {
      DebugLogger.getLogger().log("Permission error");
      throw new PermissionException();
    }

    if (!warpManager.exists(name)) {
      DebugLogger.getLogger().log("Warp not found");
      throw new WarpNotFoundException(name);
    }

    warpManager.unregister(name);
    player.sendMessage(gm("warp-delete-success", name));
    DebugLogger.getLogger().log("Warp deleted");
  }
}