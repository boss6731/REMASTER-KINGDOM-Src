package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Liquor extends ServerBasePacket {

	public S_Liquor(int objecId, int type) {
		writeC(Opcodes.S_DRUNKEN);
		writeD(objecId);
		writeC(type);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _S__19_LIQUOR;
	}

	private static final String _S__19_LIQUOR = "[S] S_Liquor";
}
