package me.kvdpxne.hc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

/**
 * Utility class for loading default properties from YAML configuration files.
 *
 * <p>This class is specifically designed for retrieving configuration values
 * for the HideCommands plugin, such as default messages. It is not intended
 * to be instantiated.</p>
 *
 * @since 0.1.0
 */
final class YamlDefaultPropertyLoader {

  /**
   * Private constructor to prevent instantiation.
   *
   * <p>This class is a utility class and cannot be instantiated.</p>
   *
   * @throws AssertionError if this constructor is called.
   * @since 0.1.0
   */
  private YamlDefaultPropertyLoader() {
    throw new AssertionError("Cannot be instantiated.");
  }

  /**
   * Loads the default response for unknown commands from the YAML configuration file.
   *
   * <p>This method reads the "unknown-command" message from the "messages"
   * section of the Spigot YAML configuration file.</p>
   *
   * @return the default unknown command response as a {@link String}.
   * @throws RuntimeException if an I/O error occurs while reading the file.
   * @since 0.1.0
   */
  static String loadDefaultUnknownCommandResponse() {
    final Yaml yaml = new Yaml();

    final Path path = Paths.get("./spigot.yml");
    final String response;

    try (final InputStream input = Files.newInputStream(path)) {
      // noinspection unchecked
      final Map<String, Object> values = (Map<String, Object>) yaml.load(input);
      response = (String) values.get("messages.unknown-command");
    } catch (
      final IOException exception
    ) {
      throw new RuntimeException(exception);
    }

    return response;
  }
}
