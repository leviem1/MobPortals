package com.snowypeaksystems.mobportals;

import io.papermc.lib.PaperLib;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class which handles initialization of the plugin. Additionally,
 * provides many methods to be used throughout the codebase.
 * @author Copyright (c) Levi Muniz. All Rights Reserved.
 */
public class MobPortals extends JavaPlugin {
  Messages messages;
  private Warps warpStorage;
  private NamespacedKey warpKey;
  private HashMap<Player, String> creators;
  private HashSet<Player> removers;

  @Override
  public void onEnable() {
    PaperLib.suggestPaper(this);

    saveDefaultConfig();

    PluginCommand cmd = getCommand("mp");
    CommandListener cl = new CommandListener(this);
    cmd.setExecutor(cl);
    cmd.setTabCompleter(cl);

    warpKey = new NamespacedKey(this, "portalDest");
    messages = Messages.getMessages(getConfig());
    warpStorage = new Warps(new File(getDataFolder(), "warps"), getServer());
    creators = new HashMap<>();
    removers = new HashSet<>();

    getServer().getPluginManager().registerEvents(new EventListener(this), this);

    greeting();
  }

  /**
   * Creates a warp with the specified name at the specified location.
   * @param name name to associate with the warp
   * @param location location of the warp
   * @return Returns true on success, or false if a warp with that name already exists
   */
  public boolean setWarp(String name, Location location) {
    if (warpExists(name)) {
      return false;
    }

    warpStorage.put(name, location);

    return true;
  }

  /**
   * Removes the warp with the specified name at the specified location.
   * @param name name to dissociate with the warp
   * @return Returns true on success, or false if a warp with that name does not exist
   */
  public boolean delWarp(String name) {
    if (!warpExists(name)) {
      return false;
    }

    warpStorage.remove(name);

    return true;
  }

  /** Returns true if the warp exists, false otherwise. */
  public boolean warpExists(String name) {
    return warpStorage.containsKey(name);
  }

  /** Returns the location of the warp associated with that name, or null if absent. */
  public Location getWarpLocation(String name) {
    return warpStorage.get(name);
  }

  /**
   * Teleports the player to a warp with the designated name.
   * @param player player to teleport
   * @param warp name of warp to teleport to
   * @throws PermissionException if the player does not have the required permission
   */
  public void warpPlayer(Player player, String warp) throws PermissionException {
    if (!player.hasPermission("mobportals.use." + warp)
        && !player.hasPermission("mobportals.use.*")) {
      throw new PermissionException("mobportals.use." + warp);
    }

    if (warpExists(warp)) {
      String message = messages.warpSuccess.replaceAll(messages.warpToken, warp);

      CompletableFuture<Boolean> future = new CompletableFuture<>();
      future.thenAccept(success -> {
        if (success) {
          player.sendMessage(message);
        }
      });

      warpPlayer(player, getWarpLocation(warp), future);
    } else {
      player.sendMessage(messages.warpNotFound.replace(messages.warpToken, warp));
    }
  }

  private void warpPlayer(Player player, Location location, CompletableFuture<Boolean> future) {
    PaperLib.getChunkAtAsync(location).thenAccept(chunk -> {
      player.teleport(location);
      future.complete(true);
    });
  }

  /** Returns true if player is creating a portal, false otherwise. */
  public boolean isCreating(Player player) {
    return creators.containsKey(player);
  }

  /** Marks the player as currently creating a portal.
   * @param player player to update the state of
   * @param warp name of the warp that is being created
   * @throws PermissionException if the player does not have the required permission
   */
  public void setCreating(Player player, String warp) throws PermissionException {
    if (!player.hasPermission("mobportals.create")) {
      throw new PermissionException("mobportals.create");
    }

    creators.put(player, warp);
  }

  /** Gets the name of the warp the player is creating a portal for. */
  public String getCreation(Player player) {
    return creators.get(player);
  }

  /** Un-marks the player as creating. */
  public void stopCreating(Player player) {
    creators.remove(player);
  }

  /** Returns true if player is removing a portal, false otherwise. */
  public boolean isRemoving(Player player) {
    return removers.contains(player);
  }

  /** Marks the player as currently removing a portal. */
  public void setRemoving(Player player) throws PermissionException {
    if (!player.hasPermission("mobportals.remove")) {
      throw new PermissionException("mobportals.remove");
    }

    removers.add(player);
  }

  /** Un-marks the player as removing a portal. */
  public void stopRemoving(Player player) {
    removers.remove(player);
  }

  /** Returns true if entity is a portal, false otherwise. */
  public boolean isPortal(LivingEntity entity) {
    return getPortalDestination(entity) != null;
  }

  /** Returns the name of the warp associated with the entity, or null if absent. */
  public String getPortalDestination(LivingEntity entity) {
    return entity.getPersistentDataContainer().get(warpKey, PersistentDataType.STRING);
  }

  /**
   * Creates a portal from the given non-player entity to the specified warp. Will
   * overwrite previous portals.
   * @param entity non-player entity to associate the warp to
   * @param warp warp to associate
   * @throws IllegalArgumentException if the entity specified is an instance of Player
   */
  public boolean createPortal(LivingEntity entity, String warp) {
    if (entity instanceof Player) {
      throw new IllegalArgumentException("Portals cannot be set to players!");
    }
    if (warpExists(warp)) {
      return false;
    }

    entity.getPersistentDataContainer().set(warpKey, PersistentDataType.STRING, warp);
    entity.setCustomName(messages.mobNameText.replace(messages.warpToken, warp));
    entity.setCustomNameVisible(true);
    entity.setInvulnerable(true);

    return true;
  }

  /** Deletes the warp associated with the entity. */
  public boolean removePortal(LivingEntity entity) {
    if (!isPortal(entity)) {
      return false;
    }

    entity.getPersistentDataContainer().remove(warpKey);
    entity.setCustomName(null);
    entity.setCustomNameVisible(false);
    entity.setInvulnerable(false);

    return true;
  }

  /** Reloads the configuration. */
  public void reload() {
    reloadConfig();
    messages.regenerateMessages(getConfig());
    warpStorage.loadAll();
  }

  private void greeting() {
    getServer().getConsoleSender().sendMessage(
        ChatColor.GOLD + "Rise and shine, MobPortals is ready to go!");
    getServer().getConsoleSender().sendMessage(
        ChatColor.YELLOW + "Please consider donating at https://github.com/sponsors/leviem1/");
  }
}