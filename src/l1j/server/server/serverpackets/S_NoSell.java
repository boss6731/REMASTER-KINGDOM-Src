package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;

public class S_NoSell extends ServerBasePacket {
	private static final String _S__25_NoSell = "[S] _S__25_NoSell";

	public S_NoSell(L1NpcInstance npc) {
		buildPacket(npc);
	}

	private void buildPacket(L1NpcInstance npc) {
		writeC(Opcodes.S_HYPERTEXT);
		writeD(npc.getId());
//		writeS("nosell");
		writeS("incence");//動作值
		writeC(1);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return _S__25_NoSell;
	}
}
