package me.kvdpxne.hc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

/**
 * Manages the configuration settings for the HideCommands plugin.
 *
 * <p>This class handles loading, saving, and managing default values
 * for the plugin's configuration. It ensures the settings file exists
 * and provides utility methods to access specific configuration options.</p>
 *
 * @since 0.1.0
 */
public final class Settings {

  /**
   * Manages the file paths for plugin storage.
   *
   * @since 0.1.0
   */
  private IoDestination destination;

  /**
   * Indicates whether to use the default unknown command response.
   *
   * @since 0.1.0
   */
  private boolean useDefaultUnknownCommand;

  /**
   * Stores the custom unknown command response.
   *
   * @since 0.1.0
   */
  private String unknownCommand;

  /**
   * Determines if a warning should be shown for non-Spigot environments.
   *
   * @since 0.1.0
   */
  private boolean showNonSpigotWarning;

  /**
   * Determines if a warning should be shown for unsupported versions.
   *
   * @since 0.1.0
   */
  private boolean showNonSupportedNewestVersionWarning;

  /**
   * Constructs a new Settings instance with default IoDestination.
   *
   * @since 0.1.0
   */
  Settings() {
    this.destination = new IoDestination();
  }

  /**
   * Initializes default settings.
   *
   * @since 0.1.0
   */
  void initializeDefault() {
    this.useDefaultUnknownCommand = false;
    this.unknownCommand = "Unknown command. Type \"/help\" for help.";
    this.showNonSpigotWarning = true;
    this.showNonSupportedNewestVersionWarning = true;
  }

  /**
   * Checks if the settings file is present.
   *
   * @return {@code true} if the settings file exists; {@code false} otherwise.
   * @since 0.1.0
   */
  public boolean present() {
    return Files.exists(this.destination.settingsFilePath());
  }

  /**
   * Ensures the settings file exists, copying the default if necessary.
   *
   * @since 0.1.0
   */
  public void relocate() {
    IoHelper.createRootDirectoryIfNeeded(this.destination.rootDirectoryPath());

    final Path settingsPath = this.destination.settingsFilePath();
    IoHelper.createSettingsFileIfNeeded(settingsPath);

    try (final InputStream input =
           IoHelper.newSettingsInputStream()
    ) {
      if (null != input) {
        Files.copy(input, settingsPath, StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (
      final IOException exception
    ) {
      HideCommands.logger().severe(
        "Failed to copy settings file to " + settingsPath +
          "; reason: " + exception.getMessage()
      );
    }
  }

  /**
   * Retrieves a string property with a default fallback.
   *
   * @param properties   the loaded properties.
   * @param key          the property key.
   * @param defaultValue the default value if the property is not found.
   * @return the property value or the default value.
   * @since 0.1.0
   */
  private String zString(
    final Properties properties,
    final String key,
    final String defaultValue
  ) {
    final String property = properties.getProperty(key);
    return null != property
      ? property
      : defaultValue;
  }

  /**
   * Retrieves a boolean property with a default fallback.
   *
   * @param properties   the loaded properties.
   * @param key          the property key.
   * @param defaultValue the default value if the property is not found.
   * @return the property value or the default value.
   * @since 0.1.0
   */
  private boolean zBoolean(
    final Properties properties,
    final String key,
    final boolean defaultValue
  ) {
    final String property = properties.getProperty(key);
    return null != property
      ? Boolean.parseBoolean(property)
      : defaultValue;
  }

  /**
   * Loads settings from the configuration file.
   *
   * @since 0.1.0
   */
  public void load() {
    final Properties properties = new Properties();
    try (final InputStream input =
           Files.newInputStream(this.destination.settingsFilePath())
    ) {
      properties.load(input);
    } catch (
      final IOException exception
    ) {
      HideCommands.logger().severe(
        "Failed to load data from settings file; reason: " +
          exception.getMessage()
      );
    }

    this.useDefaultUnknownCommand = this.zBoolean(
      properties, "use_default_unknown_command", false);

    this.showNonSpigotWarning = this.zBoolean(
      properties, "show_non_spigot_warning", true);

    this.showNonSupportedNewestVersionWarning = this.zBoolean(
      properties, "show_non_supported_new_version_warning", true);

    if (this.useDefaultUnknownCommand) {
      this.unknownCommand =
        YamlDefaultPropertyLoader.loadDefaultUnknownCommandResponse();
    } else {
      this.unknownCommand = this.zString(
        properties,
        "unknown_command",
        "Unknown command. Type \"/help\" for help."
      );
    }

    // Translate colors
    this.unknownCommand = ChatColors.colourise(this.unknownCommand);
  }

  /**
   * Reloads settings from the configuration file.
   *
   * @since 0.1.0
   */
  public void reload() {
    if (!this.present()) {
      this.relocate();
    }
    this.load();
  }

  /**
   * Retrieves the unknown command response.
   *
   * @return the unknown command response string.
   * @since 0.1.0
   */
  public String unknownCommand() {
    return this.unknownCommand;
  }

  /**
   * Checks if non-Spigot warnings are enabled.
   *
   * @return {@code true} if enabled; {@code false} otherwise.
   * @since 0.1.0
   */
  public boolean showNonSpigotWarning() {
    return showNonSpigotWarning;
  }

  /**
   * Checks if warnings for unsupported versions are enabled.
   *
   * @return {@code true} if enabled; {@code false} otherwise.
   * @since 0.1.0
   */
  public boolean showNonSupportedNewestVersionWarning() {
    return showNonSupportedNewestVersionWarning;
  }

  /**
   * Destroys the settings instance, releasing all resources.
   *
   * @since 0.1.0
   */
  void destroy() {
    this.useDefaultUnknownCommand = false;
    this.unknownCommand = null;
    this.showNonSpigotWarning = false;
    this.showNonSupportedNewestVersionWarning = false;

    this.destination = null;
  }
}
