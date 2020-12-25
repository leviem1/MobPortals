package com.snowypeaksystems.mobactions.player;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.snowypeaksystems.mobactions.data.CommandData;
import com.snowypeaksystems.mobactions.data.ICommandData;
import com.snowypeaksystems.mobactions.mock.FakePlayer;
import org.junit.jupiter.api.Test;

/**
 * Tests for MobActionsPlayer.
 * @author Copyright (c) Levi Muniz. All Rights Reserved.
 */
class MobActionsPlayerTest {
  @Test
  void canUseWarp() {
    FakePlayer fake = new FakePlayer();
    MobActionsUser player = new MobActionsPlayer(fake);

    assertFalse(player.canUseWarp("test1"));

    fake.setPermission("mobactions.warp.test1", true);
    assertTrue(player.canUseWarp("test1"));
    fake.setPermission("mobactions.warp.test1", false);

    fake.setPermission("mobactions.warp.*", true);
    assertTrue(player.canUseWarp("test1"));
    fake.setPermission("mobactions.warp.*", false);
  }

  @Test
  void canRunCommand() {
    FakePlayer fake = new FakePlayer();
    MobActionsUser player = new MobActionsPlayer(fake);
    ICommandData command = new CommandData("test1", "", "");

    assertFalse(player.canRunCommand(command.getAlias()));

    fake.setPermission("mobactions.command.test1", true);
    assertTrue(player.canRunCommand(command.getAlias()));
    fake.setPermission("mobactions.command.test1", false);

    fake.setPermission("mobactions.command.*", true);
    assertTrue(player.canRunCommand(command.getAlias()));
    fake.setPermission("mobactions.command.*", false);
  }

  @Test
  void canCreate() {
    FakePlayer fake = new FakePlayer();
    MobActionsUser player = new MobActionsPlayer(fake);

    assertFalse(player.canCreate());

    fake.setPermission("mobactions.admin.create", true);
    assertTrue(player.canCreate());
    fake.setPermission("mobactions.admin.create", false);
  }

  @Test
  void canRemove() {
    FakePlayer fake = new FakePlayer();
    MobActionsUser player = new MobActionsPlayer(fake);

    assertFalse(player.canRemove());

    fake.setPermission("mobactions.admin.remove", true);
    assertTrue(player.canRemove());
    fake.setPermission("mobactions.admin.remove", false);
  }

  @Test
  void canUseWarpCommand() {
    FakePlayer fake = new FakePlayer();
    MobActionsUser player = new MobActionsPlayer(fake);

    assertFalse(player.canUseWarpCommand());

    fake.setPermission("mobactions.warp", true);
    assertTrue(player.canUseWarpCommand());
    fake.setPermission("mobactions.warp", false);
  }

  @Test
  void canSetWarp() {
    FakePlayer fake = new FakePlayer();
    MobActionsUser player = new MobActionsPlayer(fake);

    assertFalse(player.canSetWarp());

    fake.setPermission("mobactions.admin.warps.set", true);
    assertTrue(player.canSetWarp());
    fake.setPermission("mobactions.admin.warps.set", false);
  }

  @Test
  void canRemoveWarp() {
    FakePlayer fake = new FakePlayer();
    MobActionsUser player = new MobActionsPlayer(fake);

    assertFalse(player.canRemoveWarp());

    fake.setPermission("mobactions.admin.warps.remove", true);
    assertTrue(player.canRemoveWarp());
    fake.setPermission("mobactions.admin.warps.remove", false);
  }

  @Test
  void canReload() {
    FakePlayer fake = new FakePlayer();
    MobActionsUser player = new MobActionsPlayer(fake);

    assertFalse(player.canReload());

    fake.setPermission("mobactions.admin.reload", true);
    assertTrue(player.canReload());
    fake.setPermission("mobactions.admin.reload", false);
  }

  @Test
  void canUseWarpsCommand() {
    FakePlayer fake = new FakePlayer();
    MobActionsUser player = new MobActionsPlayer(fake);

    assertFalse(player.canReload());

    fake.setPermission("mobactions.warp", true);
    assertTrue(player.canUseWarpsCommand());
    fake.setPermission("mobactions.warp", false);
  }
}