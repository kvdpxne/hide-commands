package me.kvdpxne.hc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.entity.Player;

/**
 * @since 0.1.0
 */
final class ListenerPacketOutChat
  extends PacketAdapter {

  /**
   * @since 0.1.0
   */
  ListenerPacketOutChat() {
    super(
      PacketAdapter.params()
        .plugin(HideCommands.instance())
        .types(PacketType.Play.Server.CHAT)
        .serverSide()
        .listenerPriority(ListenerPriority.MONITOR)
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

    if (PacketType.Play.Server.CHAT != event.getPacketType()) {
      return;
    }

    final Player player = event.getPlayer();
    if (!HideCommands.instance().playerIdentifierManager().remove(player)) {
      return;
    }

    if (player.isOp()) {
      return;
    }

    event.getPacket().getChatComponents().write(
      0,
      WrappedChatComponent.fromText(
        HideCommands.instance()
          .settings()
          .unknownCommand()
      )
    );
  }
}
