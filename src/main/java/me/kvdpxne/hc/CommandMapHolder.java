package me.kvdpxne.hc;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;

/**
 * Manages access to the Bukkit {@link CommandMap} instance.
 * <p>
 * This class provides a mechanism to lazily retrieve and cache a reference to
 * the server's {@link CommandMap} using reflection. The cached reference is
 * stored as a {@link WeakReference} to avoid memory leaks.
 * </p>
 *
 * @since 0.1.0
 */
public final class CommandMapHolder {

  /**
   * Weak reference to the {@link CommandMap} instance.
   * <p>
   * This allows the {@link CommandMap} to be garbage-collected if it is no
   * longer in use.
   * </p>
   *
   * @since 0.1.0
   */
  private Reference<CommandMap> commandMapReference;

  /**
   * Constructs a new {@link CommandMapHolder}.
   * <p>
   * The {@link CommandMap} reference is not initialized upon construction and
   * will be lazily loaded when {@link #commandMap()} is called.
   * </p>
   *
   * @since 0.1.0
   */
  CommandMapHolder() {
  }

  /**
   * Retrieves the {@link CommandMap} instance.
   * <p>
   * If the {@link CommandMap} is not already cached, this method uses
   * reflection to obtain it from the Bukkit {@link Server}.
   * </p>
   *
   * @return the {@link CommandMap} instance, or {@code null} if it cannot be
   * retrieved.
   * @since 0.1.0
   */
  public CommandMap commandMap() {
    CommandMap commandMap = null;
    if (null != this.commandMapReference) {
      commandMap = this.commandMapReference.get();
      if (null != commandMap) {
        return commandMap;
      }
    }

    final Server server = Bukkit.getServer();
    final Class<? extends Server> serverClass = server.getClass();

    try {
      final Method getter = serverClass.getMethod("getCommandMap");
      commandMap = (CommandMap) getter.invoke(server);
    } catch (
      final NoSuchMethodException |
            SecurityException |
            IllegalAccessException |
            InvocationTargetException exception
    ) {
      HideCommands.logger().severe(
        String.format(
          "Failed to get %s instance; reason: %s",
          CommandMap.class,
          exception.getMessage()
        )
      );
    }

    this.commandMapReference = new WeakReference<>(commandMap);
    return commandMap;
  }

  /**
   * Destroys the cached {@link CommandMap} reference.
   * <p>
   * This method clears the weak reference to the {@link CommandMap}, allowing
   * it to be garbage-collected if no other references exist.
   * </p>
   *
   * @since 0.1.0
   */
  void destroy() {
    this.commandMapReference = null;
  }
}
