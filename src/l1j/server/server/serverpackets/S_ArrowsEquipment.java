package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

public class S_ArrowsEquipment extends ServerBasePacket {
	public S_ArrowsEquipment(L1ItemInstance item) {
		writeC(Opcodes.S_EVENT);
		writeC(0xcd);
		writeD(item.getId());
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}


