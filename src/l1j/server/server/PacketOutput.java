package l1j.server.server;

import l1j.server.server.serverpackets.ServerBasePacket;

public interface PacketOutput {
  void sendPacket(ServerBasePacket paramServerBasePacket);
}


