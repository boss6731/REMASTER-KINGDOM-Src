package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_OwnCharStatus2 extends ServerBasePacket {


	public S_OwnCharStatus2(L1PcInstance pc) {
		if (pc == null) {
			return;
		}
		cha = pc;
		writeC(Opcodes.S_ABILITY_SCORES);
		writeH(cha.getAbility().getTotalStr());
		writeH(cha.getAbility().getTotalInt());
		writeH(cha.getAbility().getTotalWis());
		writeH(cha.getAbility().getTotalDex());
		writeH(cha.getAbility().getTotalCon());
		writeH(cha.getAbility().getTotalCha());
		writeC(cha.getInventory().getWeight100());
	}
	
	public S_OwnCharStatus2(L1PcInstance pc, boolean check) {
		if (pc == null) {
			return;
		}
		cha = pc;
		writeC(Opcodes.S_ABILITY_SCORES);
		writeH(cha.getAbility().getTotalStr());
		writeH(cha.getAbility().getTotalInt());
		writeH(cha.getAbility().getTotalWis());
		writeH(cha.getAbility().getTotalDex());
		writeH(cha.getAbility().getTotalCon());
		writeH(cha.getAbility().getTotalCha());
		if (check)
			writeC(0);
	}



	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _S__4F_S_OwnChraStatus2;
	}

	private static final String _S__4F_S_OwnChraStatus2 = "[C] S_OwnCharStatus2";
	private L1PcInstance cha = null;
}


