package l1j.server.server.serverpackets;

import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_SkillIconGFX extends ServerBasePacket {

	public S_SkillIconGFX(int i, int j) {
		writeC(Opcodes.S_EVENT);
		writeC(i);
		writeH(j);
		writeH(0);// 追加
	}
	public S_SkillIconGFX(int i, int j, int gfxid, int objid) {
		writeC(Opcodes.S_EVENT);
		writeC(i);
		writeH(j);
//        writeBit(gfxid);// 註解下面追加
		writeH(gfxid); // 娃娃雙擊顯示 ON
		writeD(objid);
	}
	public S_SkillIconGFX(int i) {
		writeC(Opcodes.S_EVENT);
		writeC(0xa0);
		writeC(1);
		writeH(0);
		writeC(2);
		writeH(i);
	}
	public S_SkillIconGFX(int i, int j, boolean on) {
		writeC(Opcodes.S_EVENT);
		writeC(i);
		writeH(j);
		if (on){
			writeC(1);
		}
		else
			writeC(0);
	}
	
	public S_SkillIconGFX(int i, int j, boolean on, boolean on2) {
		writeC(Opcodes.S_EVENT);
		writeC(i);
		writeH(j);
		int a;
		if (on){
			a = 1;
		} else if (on2){
			a = 2;
		} else
			a = 0;
		writeC(a);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
