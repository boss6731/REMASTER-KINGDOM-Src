package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_Invis extends ServerBasePacket {

	public S_Invis(int objid, int type) {
		buildPacket(objid, type);
	}

	private void buildPacket(int objid, int type) {
		writeC(Opcodes.S_INVISIBLE);
		writeD(objid);
		writeC(type);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_INVIS;
	}

	private static final String S_INVIS = "[S] S_Invis";
}
