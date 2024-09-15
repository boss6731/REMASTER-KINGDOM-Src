package l1j.server.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_SPMR extends l1j.server.server.serverpackets.ServerBasePacket {

	private static final String S_SPMR = "[S] S_S_SPMR";

	public S_SPMR(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_MAGIC_STATUS);
		writeH(pc.getAbility().getSp() - pc.getAbility().getTrueSp());// 220906 變更
		if (pc.getResistance() == null) {
			writeH(0);
		} else {
			writeH(pc.getResistance().getMr() - pc.getResistance().getBaseMr() - pc.getResistance().getLevelMr());
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_SPMR;
	}
}
