package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_TestEffect extends ServerBasePacket {

	private static final String S_TESTEFFECT = "[S] S_TestEffect";

	public S_TestEffect(int targetId, boolean active) {
		buildPacket(targetId, active);
	}

	private void buildPacket(int targetId, boolean active) {
		writeC(Opcodes.S_EVENT);
		writeC(0xc2); // 這是一個預定義的操作碼
		writeD(targetId); // 對象ID
		writeD(13135); // 圖像編號
		writeD(active ? 0x01 : 0x00); // 刪除或添加
		writeH(0);
	}
}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_TESTEFFECT;
	}

}
