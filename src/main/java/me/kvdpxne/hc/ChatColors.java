package me.kvdpxne.hc;

import org.bukkit.ChatColor;

/**
 * Utility class for handling chat color operations in Bukkit plugins.
 * <p>
 * This class provides static methods for colorizing text with Minecraft color
 * codes. It is not intended to be instantiated.
 * </p>
 *
 * @since 0.1.0
 */
final class ChatColors {

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
  private ChatColors() {
    throw new AssertionError("Cannot be instantiated.");
  }

  /**
   * Translates alternate color codes in a given text to Minecraft color codes.
   * <p>
   * The method processes the input string and replaces occurrences of the
   * {@code &} character followed by a valid color code with the corresponding
   * Minecraft chat color.
   * </p>
   *
   * @param text the input string containing color codes prefixed by {@code &}.
   * @return a string with {@code &} color codes translated to Minecraft chat
   * colors, or an empty string if the input is {@code null} or empty.
   * @since 0.1.0
   */
  static String colourise(
    final String text
  ) {
    if (null == text || text.isEmpty()) {
      return "";
    }

    return ChatColor.translateAlternateColorCodes('&', text);
  }
}
