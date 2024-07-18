package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;

public class S_AttackPacket extends ServerBasePacket {
	private static final String _S__1F_ATTACKPACKET = "[S] S_AttackPacket";

	public S_AttackPacket(L1Character cha, int objid, int type) {
		buildpacket(cha, objid, type);
	}
	
	public S_AttackPacket(L1Character cha, int objid, int type, int attacktype) {
		buildpacket(cha, objid, type, attacktype);
	}

	private void buildpacket(L1Character cha, int objid, int type) {
		writeC(Opcodes.S_ATTACK);
		writeC(type);
		writeD(objid);
		writeD(cha.getId());
		// writeD(_objid);
		writeC(0x4E);
		writeC(0);
		writeC(cha.getHeading());
		writeD(0x00000000);
		writeC(0x00);
	}

	private void buildpacket(L1Character cha, int objid, int type, int attacktype) {
		writeC(Opcodes.S_ATTACK); // 攻擊操作碼
		writeC(type); // 攻擊類型
		writeD(cha.getId()); // 寫入角色ID
		writeD(objid); // 寫入目標ID
		writeH(0x02); // 傷害值（固定為 2）
		writeC(cha.getHeading()); // 寫入角色面向方向
		writeH(0x0000); // 目標X座標（固定為 0x0000）
		writeH(0x0000); // 目標Y座標（固定為 0x0000）
		writeC(attacktype); // 攻擊類型（0: 無, 2: 爪, 4: 雙刀, 0x08: 反射鏡）
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return _S__1F_ATTACKPACKET;
	}
}


