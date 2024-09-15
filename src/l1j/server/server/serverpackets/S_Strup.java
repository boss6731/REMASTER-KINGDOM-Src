package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Strup extends ServerBasePacket {

	public S_Strup(L1PcInstance pc, int type, int time) {
		writeC(Opcodes.S_MAGE_STRENGTH);
		writeH(time);
		writeC(pc.getAbility().getTotalStr());
		writeC(pc.getInventory().getWeight100());
		writeC(0);
		writeD(type);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return _S__25_S_Strup;
	}

	private static final String _S__25_S_Strup = "[S] S_Strup";
}
