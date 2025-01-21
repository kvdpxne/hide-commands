package me.kvdpxne.hc;

/**
 * Holds constant values used across the HideCommands plugin.
 * <p>
 * This class provides centralized definitions for frequently used constants
 * such as plugin name and settings file name. It is not intended to be
 * instantiated.
 * </p>
 *
 * @since 0.1.0
 */
public final class Constants {

  /**
   * The name of the plugin.
   * <p>
   * Used for logging, identification, and other purposes where the plugin's
   * name is required.
   * </p>
   *
   * @since 0.1.0
   */
  public static final String NAME = "hide-commands";

  /**
   * The name of the settings file.
   * <p>
   * This file is used to store configuration properties for the plugin.
   * </p>
   *
   * @since 0.1.0
   */
  public static final String SETTINGS_NAME = "settings.properties";

  /**
   * Private constructor to prevent instantiation.
   * <p>
   * This utility class is not meant to be instantiated. Attempting to do so
   * will result in an {@link AssertionError}.
   * </p>
   *
   * @throws AssertionError if this constructor is called.
   * @since 0.1.0
   */
  private Constants() {
    throw new AssertionError("Cannot be instantiated.");
  }
}
