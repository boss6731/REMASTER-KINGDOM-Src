package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Drawal extends ServerBasePacket {

	public S_Drawal(int objectId, int count) {
		writeC(Opcodes.S_WITHDRAW);
		writeD(objectId);
		writeD(count);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _S__37_DRAWAL;
	}

	private static final String _S__37_DRAWAL = "[S] S_Drawal";
}
