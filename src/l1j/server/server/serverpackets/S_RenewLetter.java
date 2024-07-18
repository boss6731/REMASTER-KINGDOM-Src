package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;


public class S_RenewLetter extends ServerBasePacket {
	private static final String S_RENEWLETTER = "[S] S_RenewLetter";

	public S_RenewLetter(L1PcInstance pc, int type, int id) {
		buildPacket(pc, type, id);
	}

	private void buildPacket(L1PcInstance pc, int type, int id) {
		writeC(Opcodes.S_MAIL_INFO);
		writeC(type); // 0: 郵箱 1: 血盟郵箱 2: 保管箱
		writeD(id);  // 帖子編號
		writeC(1);
	}
}
	@Override
	public byte[] getContent() {
		return getBytes();
	}
	
	public String getType() {
		return S_RENEWLETTER;
	}
}


