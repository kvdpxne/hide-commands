package me.kvdpxne.hc;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Manages file system paths used by the HideCommands plugin.
 * <p>
 * This class provides access to commonly used paths, such as the plugin's root
 * directory and settings file. Paths are lazily initialized and cached using
 * weak references to prevent memory leaks while allowing garbage collection.
 * </p>
 *
 * @since 0.1.0
 */
final class IoDestination {

  /**
   * Weak reference to the root directory path.
   * <p>
   * This allows the root directory path to be garbage-collected if it is no
   * longer in use.
   * </p>
   *
   * @since 0.1.0
   */
  private Reference<Path> rootDirectoryPathReference;

  /**
   * Weak reference to the settings file path.
   * <p>
   * This allows the settings file path to be garbage-collected if it is no
   * longer in use.
   * </p>
   *
   * @since 0.1.0
   */
  private Reference<Path> settingsFilePathReference;

  /**
   * Constructs a new {@code IoDestination} instance.
   * <p>
   * The file paths are not initialized upon construction and will be lazily
   * loaded when their respective methods are called.
   * </p>
   *
   * @since 0.1.0
   */
  IoDestination() {
  }

  /**
   * Retrieves the root directory path for the plugin.
   * <p>
   * If the path is not already cached, it is initialized to
   * "./plugins/{@link Constants#NAME}". The result is cached as a weak
   * reference for future calls.
   * </p>
   *
   * @return the root directory path.
   * @since 0.1.0
   */
  Path rootDirectoryPath() {
    Path rootDirectoryPath;
    if (null != this.rootDirectoryPathReference) {
      rootDirectoryPath = this.rootDirectoryPathReference.get();
      if (null != rootDirectoryPath) {
        return rootDirectoryPath;
      }
    }

    rootDirectoryPath = Paths.get("./plugins", Constants.NAME);
    this.rootDirectoryPathReference = new WeakReference<>(rootDirectoryPath);
    return rootDirectoryPath;
  }

  /**
   * Retrieves the settings file path for the plugin.
   * <p>
   * If the path is not already cached, it is initialized to
   * "./plugins/{@link Constants#NAME}/{@link Constants#SETTINGS_NAME}". The
   * result is cached as a weak reference for future calls.
   * </p>
   *
   * @return the settings file path.
   * @since 0.1.0
   */
  Path settingsFilePath() {
    Path settingsFilePath;
    if (null != this.settingsFilePathReference) {
      settingsFilePath = this.settingsFilePathReference.get();
      if (null != settingsFilePath) {
        return settingsFilePath;
      }
    }

    settingsFilePath = Paths.get(
      "./plugins", Constants.NAME, Constants.SETTINGS_NAME);
    this.settingsFilePathReference = new WeakReference<>(settingsFilePath);
    return settingsFilePath;
  }
}
