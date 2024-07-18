package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_TrueTargetNew extends ServerBasePacket {

	private static final String S_TRUETARGETNEW = "[S] S_TrueTargetNew";

	public S_TrueTargetNew(int targetId, boolean active) {
		buildPacket(targetId, active);
	}

	private void buildPacket(int targetId, boolean active) {
		writeC(Opcodes.S_EVENT);
		writeC(0xc2);
		writeD(targetId); // 對象ID
		writeD(13135); // 圖像編號
		writeD(active ? 0x01 : 0x00); // 刪除或添加
		writeH(0);
	}

	@override
	public byte[] getContent() {
		return getBytes();
	}

	@override
	public String getType() {
		return S_TRUETARGETNEW;
	}

}
