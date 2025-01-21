package me.kvdpxne.hc;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Utility class for handling input/output operations for the HideCommands
 * plugin.
 * <p>
 * This class includes methods for creating necessary directories and files, as
 * well as retrieving plugin resources. It is not intended to be instantiated
 * .</p>
 *
 * @since 0.1.0
 */
final class IoHelper {

  /**
   * Private constructor to prevent instantiation.
   * <p>
   * This class is a utility class and cannot be instantiated.
   * </p>
   *
   * @throws AssertionError if this constructor is called.
   * @since 0.1.0
   */
  private IoHelper() {
    throw new AssertionError("Cannot be instantiated.");
  }

  /**
   * Retrieves an input stream for the plugin's settings file.
   * <p>
   * This method locates the settings file resource using the class loader and
   * returns an input stream for reading its contents. If the resource cannot be
   * found or opened, {@code null} is returned.
   * </p>
   *
   * @return an {@link InputStream} for the settings file, or {@code null} if
   * unavailable.
   * @since 0.1.0
   */
  static InputStream newSettingsInputStream() {
    final URL url =
      HideCommands.classLoader().getResource(Constants.SETTINGS_NAME);

    if (null == url) {
      return null;
    }

    try {
      final URLConnection connection = url.openConnection();
      connection.setUseCaches(false);
      return connection.getInputStream();
    } catch (
      final IOException exception
    ) {
      final Logger logger = HideCommands.logger();

      logger.severe(
        String.format(
          "Failed to open resource '%s'.",
          Constants.SETTINGS_NAME)
      );
      logger.severe(exception.getMessage());

      return null;
    }
  }

  /**
   * Ensures the root directory exists, creating it if necessary.
   * <p>
   * If the path exists but is not a directory, it is deleted and replaced with
   * a directory. If the path does not exist, a directory is created.
   * </p>
   *
   * @param path the path to the root directory.
   * @since 0.1.0
   */
  static void createRootDirectoryIfNeeded(
    final Path path
  ) {
    try {
      if (Files.exists(path)) {
        if (!Files.isDirectory(path)) {
          Files.delete(path);
          Files.createDirectory(path);
        }
        return;
      }
      Files.createDirectory(path);
    } catch (
      final IOException exception
    ) {
      HideCommands.logger().severe(
        "Failed to create root directory; reason: " + exception.getMessage()
      );
    }
  }

  /**
   * Ensures the settings file exists, creating it if necessary.
   * <p>
   * If the path exists but is not a regular file, it is deleted and replaced
   * with a new file. If the path does not exist, a new file is created.
   * </p>
   *
   * @param path the path to the settings file.
   * @since 0.1.0
   */
  static void createSettingsFileIfNeeded(
    final Path path
  ) {
    try {
      if (Files.exists(path)) {
        if (!Files.isRegularFile(path)) {
          Files.delete(path);
          Files.createFile(path);
        }
        return;
      }
      Files.createFile(path);
    } catch (
      final IOException exception
    ) {
      HideCommands.logger().severe(
        "Failed to create settings file; reason: " + exception.getMessage()
      );
    }
  }
}
