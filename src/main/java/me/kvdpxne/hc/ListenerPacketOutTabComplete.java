package me.kvdpxne.hc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import java.util.SortedSet;
import java.util.TreeSet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;

/**
 * @since 0.1.0
 */
final class ListenerPacketOutTabComplete
  extends PacketAdapter {

  /**
   * @since 0.1.0
   */
  ListenerPacketOutTabComplete() {
    super(
      PacketAdapter.params()
        .plugin(HideCommands.instance())
        .types(PacketType.Play.Server.TAB_COMPLETE)
        .serverSide()
        .listenerPriority(ListenerPriority.HIGH)
    );
  }

  /**
   * @since 0.1.0
   */
  @Override
  public void onPacketSending(
    final PacketEvent event
  ) {
    if (event.isCancelled()) {
      return;
    }

    if (PacketType.Play.Server.TAB_COMPLETE != event.getPacketType()) {
      return;
    }

    final PacketContainer packet = event.getPacket();
    final String[] context = packet.getStringArrays().readSafely(0);

    if (null == context || 0 == context.length) {
      return;
    }

    final Player player = event.getPlayer();
    // If the player is an administrator then he has privileges to all
    // commands anyway, so further code execution is unnecessary.
    if (player.isOp()) {
      return;
    }

    final SortedSet<String> suggestions = new TreeSet<>();

    final CommandMap commandMap = HideCommands.instance()
      .commandMapHolder()
      .commandMap();

    for (final String next : context) {
      if (suggestions.contains(next)) {
        continue;
      }

      final Command command = commandMap.getCommand(next);
      if (null == command) {
        continue;
      }

      // The text equivalent of the privilege to execute the command.
      final String privilege = command.getPermission();

      // If the privilege to execute the command is null or is empty then a
      // player with no privileges has the privilege to use this command.
      if (null == privilege || privilege.isEmpty() ||
        player.hasPermission(privilege)
      ) {
        final StringBuilder builder = new StringBuilder(next);
        suggestions.add(next);

        final boolean hasSlash = next.contains("/");
        final String name = command.getName();
        final int start = (hasSlash ? 1 : 0);
        final int end = name.length() + start;

        builder.replace(start, end, name);
        builder.delete(end, builder.length());
        suggestions.add(builder.toString());
      }
    }

    if (!suggestions.isEmpty()) {
      packet.getStringArrays().write(0, suggestions.toArray(new String[0]));
    }
  }
}
