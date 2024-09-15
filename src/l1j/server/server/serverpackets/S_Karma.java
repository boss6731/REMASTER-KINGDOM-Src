package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Karma extends ServerBasePacket {
	private static final String _TYPE = "[S] S_Karma";
	
	public S_Karma(L1PcInstance pc)
	{
		writeC(Opcodes.S_EVENT);
	    writeC(0x57);
		// + 欲望陣營, - 神聖陣營
		// 必須擊敗神聖陣營以提升欲望等級..
	    writeD(pc.getKarma());
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _TYPE;
	}
}
