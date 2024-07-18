package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Karma extends ServerBasePacket {
	private static final String _TYPE = "[S] S_Karma";

	public S_Karma(L1PcInstance pc) {
		writeC(Opcodes.S_EVENT);
		writeC(0x57);
		// + 對應欲望陣營，- 對應那神的陣營
		// 必須殺死那神才能提升欲望...
		writeD(pc.getKarma());
	}

	@override
	public byte[] getContent() {
		return getBytes();
	}

	@override
	public String getType() {
		return _TYPE;
	}
}


