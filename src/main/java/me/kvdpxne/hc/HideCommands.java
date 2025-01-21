package me.kvdpxne.hc;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketListener;
import java.util.logging.Logger;
import me.kvdpxne.notchity.MinecraftVersionCreator;
import me.kvdpxne.notchity.Version;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @since 0.1.0
 */
public final class HideCommands
  extends JavaPlugin {

  /**
   * @since 0.1.0
   */
  static HideCommands instance;

  /**
   * @since 0.1.0
   */
  private CommandMapHolder commandMapHolder;

  /**
   * @since 0.1.0
   */
  private PlayerIdentifierManager playerIdentifierManager;

  /**
   * @since 0.1.0
   */
  private Settings settings;

  /**
   * @since 0.1.0
   */
  private boolean forceDisable = false;

  /**
   * @since 0.1.0
   */
  private boolean disabled = true;

  /**
   * @since 0.1.0
   */
  public HideCommands() {
  }

  /**
   * @since 0.1.0
   */
  public static HideCommands instance() {
    if (null == HideCommands.instance || HideCommands.instance.disabled) {
      throw new IllegalStateException(
        "The HideCommands plugin has not been initialized yet."
      );
    }
    return HideCommands.instance;
  }

  /**
   * @since 0.1.0
   */
  static ClassLoader classLoader() {
    return HideCommands.instance.getClassLoader();
  }

  /**
   * @since 0.1.0
   */
  static Logger logger() {
    return HideCommands.instance.getLogger();
  }

  /**
   * @since 0.1.0
   */
  private boolean canUseSpigot() {
    try {
      Class.forName("org.bukkit.Server.Spigot");
      return true;
    } catch (
      final ClassNotFoundException ignored
    ) {
      return false;
    }
  }

  /**
   * @since 0.1.0
   */
  private boolean canUseProtocolLibrary() {
    try {
      Class.forName("com.comphenix.protocol.ProtocolLibrary");
      return true;
    } catch (
      final ClassNotFoundException ignored
    ) {
      return false;
    }
  }


  /**
   * @since 0.1.0
   */
  private void registerListenerPacket(
    final PacketListener... listeners
  ) {
    if (null == listeners || 0 == listeners.length) {
      return;
    }

    final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    for (final PacketListener listener : listeners) {
      manager.addPacketListener(listener);
    }
  }

  /**
   * @since 0.1.0
   */
  public void shutdown() {
    super.getServer().getPluginManager().disablePlugin(this);
  }

  /**
   * @since 0.1.0
   */
  @Override
  public void onLoad() {
    final Logger logger = super.getLogger();
    final Version version = MinecraftVersionCreator.getMinecraftVersion();

    //
    //
    if (version.isOlderThan(1_007_000)) {
      logger.severe("");
      logger.severe("");
      logger.severe("");
      logger.severe("");
      logger.severe("");

      this.forceDisable = true;
      super.setEnabled(false);
    }

    //
    HideCommands.instance = this;

    try {
      this.settings = new Settings();

      if (this.settings.present()) {
        this.settings.load();
      } else {
        this.settings.initializeDefault();
        this.settings.relocate();
      }
    } catch (final Exception ignored) {
      this.forceDisable = true;
      super.setEnabled(false);
      return;
    }

    // If the platform on which the plugin was run is not a fork of the
    // Spigot platform and receiving a warning about this fact is enabled,
    // then an appropriate message will be printed to the console.
    if (!this.canUseSpigot() && this.settings.showNonSpigotWarning()) {
      logger.warning("Detected that the plugin was not run on a platform");
      logger.warning("that is a fork of the Spigot platform, this is");
      logger.warning("fine but you will not be able to the full");
      logger.warning("functionality of the plugin.");
    }

    //
    if (version.isLaterThan(1_021_000) &&
      this.settings.showNonSupportedNewestVersionWarning()
    ) {
      logger.warning("");
      logger.warning("");
    }

    this.commandMapHolder = new CommandMapHolder();
    this.playerIdentifierManager = new PlayerIdentifierManager();

    this.disabled = false;
  }

  /**
   * @since 0.1.0
   */
  @Override
  public void onEnable() {
    if (this.forceDisable) {
      this.shutdown();
      return;
    }

    if (this.canUseProtocolLibrary()) {
      this.registerListenerPacket(
        new ListenerPacketInChat(),
        new ListenerPacketOutChat(),
        new ListenerPacketOutTabComplete()
      );
    }
  }

  /**
   * @since 0.1.0
   */
  @Override
  public void onDisable() {
    if (!this.forceDisable) {
      if (this.canUseProtocolLibrary()) {
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
      }

      this.commandMapHolder.destroy();
      this.playerIdentifierManager.destroy();
      this.settings.destroy();
    }

    this.commandMapHolder = null;
    this.playerIdentifierManager = null;
    this.settings = null;

    this.disabled = true;
    HideCommands.instance = null;
  }

  /**
   * @since 0.1.0
   */
  @Override
  public void saveConfig() {
    if (null != this.settings && !this.settings.present()) {
      this.settings.relocate();
    }
  }

  /**
   * @since 0.1.0
   */
  @Override
  public void saveDefaultConfig() {
    this.saveConfig();
  }

  /**
   * @since 0.1.0
   */
  @Override
  public void reloadConfig() {
    if (null != this.settings) {
      this.settings.reload();
    }
  }

  /**
   * @since 0.1.0
   */
  public CommandMapHolder commandMapHolder() {
    return this.commandMapHolder;
  }

  /**
   * @since 0.1.0
   */
  public PlayerIdentifierManager playerIdentifierManager() {
    return this.playerIdentifierManager;
  }

  /**
   * @since 0.1.0
   */
  public Settings settings() {
    return this.settings;
  }
}
