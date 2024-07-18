package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SellHouse extends ServerBasePacket {

	private static final String S_SELLHOUSE = "[S] S_SellHouse";

	public S_SellHouse(int objectId, String houseNumber) {
		buildPacket(objectId, houseNumber);
	}

	private void buildPacket(int objectId, String houseNumber) {
		writeC(Opcodes.S_HYPERTEXT_INPUT);
		writeD(objectId);
		writeD(0); // ?
		writeD(100000); // Spin 控件的初始價格
		writeD(100000); // 價格的下限
		writeD(2000000000); // 價格的上限
		writeH(0); // ?
		writeS("agsell");
		writeS("agsell " + houseNumber);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_SELLHOUSE;
	}
}


