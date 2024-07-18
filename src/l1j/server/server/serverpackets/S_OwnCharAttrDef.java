package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Resistance;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_OwnCharAttrDef extends ServerBasePacket {

	private static final String S_OWNCHARATTRDEF = "[S] S_OwnCharAttrDef";

	public S_OwnCharAttrDef(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_AC);
		writeD(pc.getAC().getAc());
		Resistance resistance = pc.getResistance();
		if (resistance != null) {
			writeH(resistance.getFire());
			writeH(resistance.getWater());
			writeH(resistance.getWind());
			writeH(resistance.getEarth());
		} else {
			writeH(0);
			writeH(0);
			writeH(0);
			writeH(0);
		}
		writeC(pc.getDg());
		writeC(pc.getTotalER());
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_OWNCHARATTRDEF;
	}
}


