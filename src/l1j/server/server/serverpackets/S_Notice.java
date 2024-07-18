package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.server.Opcodes;

public class S_Notice extends ServerBasePacket{
	public S_Notice() {
		writeC(Opcodes.S_NOTICE);
		writeC(0xB5);
		writeC(0x01);
	}
	
	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}


