package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Dexup extends ServerBasePacket {

	public S_Dexup(L1PcInstance pc, int type, int time) {
		writeC(Opcodes.S_MAGE_DEXTERITY);
		writeH(time);
		writeC(pc.getAbility().getTotalDex());
		writeC(0);
		writeD(type);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	
	@Override
	public String getType() {
		return _S__25_S_Dexup;
	}

	private static final String _S__25_S_Dexup = "[S] S_Dexup";
}
