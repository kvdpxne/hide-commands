package me.kvdpxne.hc;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;

/**
 * Manages player identifiers for the HideCommands plugin.
 * <p>
 * This class maintains a set of UUIDs to track players and provides methods to
 * add, remove, and check player identifiers. The identifiers are stored in a
 * thread-safe manner using a concurrent set.
 * </p>
 *
 * @since 0.1.0
 */
public final class PlayerIdentifierManager {

  /**
   * Set of unique player identifiers.
   * <p>
   * This set is initialized on demand and stores the UUIDs of players in a
   * thread-safe manner.
   * </p>
   *
   * @since 0.1.0
   */
  private Set<UUID> identifiers;

  /**
   * Constructs a new {@link PlayerIdentifierManager}.
   * <p>
   * The identifiers set is not initialized upon creation and will be lazily
   * initialized when needed.
   * </p>
   *
   * @since 0.1.0
   */
  PlayerIdentifierManager() {
  }

  /**
   * Initializes the player identifiers set.
   * <p>
   * This method is called internally to lazily initialize the identifiers set
   * if it has not already been initialized.
   * </p>
   *
   * @since 0.1.0
   */
  private void initialize() {
    this.identifiers = ConcurrentHashMap.newKeySet(12);
    HideCommands.logger().fine("PlayerIdentifierManager has been initialized.");
  }

  /**
   * Adds a player to the identifiers set.
   *
   * @param player the player to add.
   * @return {@code true} if the player's UUID was successfully added;
   * {@code false} if the UUID was already present.
   * @throws IllegalStateException if the HideCommands plugin instance is not
   *                               initialized.
   * @since 0.1.0
   */
  public boolean add(
    final Player player
  ) {
    if (null == this.identifiers) {
      if (null == HideCommands.instance) {
        throw new IllegalStateException(
          "The plugin HideCommands instance is not initialized."
        );
      }
      this.initialize();
    }

    return this.identifiers.add(player.getUniqueId());
  }

  /**
   * Removes a player from the identifiers set.
   *
   * @param player the player to remove.
   * @return {@code true} if the player's UUID was successfully removed;
   * {@code false} if the UUID was not present or the set was not initialized.
   * @since 0.1.0
   */
  public boolean remove(
    final Player player
  ) {
    return null != this.identifiers &&
      this.identifiers.remove(player.getUniqueId());
  }

  /**
   * Checks if a player is present in the identifiers set.
   *
   * @param player the player to check.
   * @return {@code true} if the player's UUID is present; {@code false}
   * otherwise or if the set is not initialized.
   * @since 0.1.0
   */
  public boolean has(
    final Player player
  ) {
    return null != this.identifiers &&
      this.identifiers.contains(player.getUniqueId());
  }

  /**
   * Destroys the player identifiers set.
   * <p>
   * This method clears the identifiers set and sets it to {@code null},
   * effectively releasing all resources associated with it.
   * </p>
   *
   * @since 0.1.0
   */
  void destroy() {
    this.identifiers = null;
  }
}
