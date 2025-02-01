package me.kvdpxne.hc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;

/**
 * @since 0.1.0
 */
final class ListenerPacketInChat
  extends PacketAdapter {

  /**
   * @since 0.1.0
   */
  ListenerPacketInChat() {
    super(
      PacketAdapter.params()
        .plugin(HideCommands.instance())
        .types(PacketType.Play.Client.CHAT)
        .clientSide()
        .listenerPriority(ListenerPriority.MONITOR)
    );
  }

  /**
   * @since 0.1.0
   */
  @Override
  public void onPacketReceiving(
    final PacketEvent event
  ) {
    if (event.isCancelled()) {
      return;
    }

    if (PacketType.Play.Client.CHAT != event.getPacketType()) {
      return;
    }

    final PacketContainer packet = event.getPacket();
    final String context = packet.getStrings().readSafely(0);

    if (null == context || !context.startsWith("/")) {
      return;
    }

    final CommandMap commandMap = HideCommands.instance()
      .commandMapHolder()
      .commandMap();

    final String name;
    final int indexOfWhitespace = context.indexOf(' ');

    if (-1 == indexOfWhitespace) {
      name = context.substring(1);
    } else {
      name = context.substring(1, indexOfWhitespace);
    }

    final Command command = commandMap.getCommand(name);
    final Player player = event.getPlayer();

    if (null == command) {
      HideCommands.instance()
        .playerIdentifierManager()
        .add(player);
      return;
    }

    if (player.isOp()) {
      return;
    }

    final String privilege = command.getPermission();
    if (null == privilege || privilege.isEmpty()) {
      return;
    }

    if (!player.hasPermission(privilege)) {
      HideCommands.instance()
        .playerIdentifierManager()
        .add(player);
    }
  }
}
