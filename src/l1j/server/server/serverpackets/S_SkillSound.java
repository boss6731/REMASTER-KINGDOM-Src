package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_SkillSound extends ServerBasePacket {
	private static final String S_SKILL_SOUND = "[S] S_SkillSound";

	// 靜態方法：廣播技能音效
	public static void broadcast(L1Character cha, int id){
		S_SkillSound sound = new S_SkillSound(cha.getId(), id);
		if(cha instanceof L1PcInstance){
			L1PcInstance pc = (L1PcInstance)cha;
			pc.sendPackets(sound, false);
		}
		cha.broadcastPacket(sound);
	}

	// 構造函數重載：用於對象ID, GFX ID 和 AID
	public S_SkillSound(int objid, int gfxid, int aid) {
		buildPacket(objid, gfxid, aid);
	}

	// 構造函數重載：用於對象ID, GFX ID, AID 以及坐標
	public S_SkillSound(int objid, int gfxid, int aid, int x, int y) {
		buildPacket(objid, gfxid, aid, x, y);
	}

	// 構造函數重載：用於僅對象ID和GFX ID
	public S_SkillSound(int objid, int gfxid) {
		buildPacket(objid, gfxid, 0);
	}

	// 私有方法：構建封包，不含坐標
	private void buildPacket(int objid, int gfxid, int aid) {
		// TODO: SPR錯誤輸出測試
		if(gfxid == 1100) {
			new Throwable().printStackTrace();
		}
		// aid 未使用
		writeC(Opcodes.S_EFFECT); // 寫入操作碼
		writeD(objid);            // 寫入對象ID
		writeH(gfxid);            // 寫入GFX ID
		writeH(0);                // 寫入0，可能表示無額外數據
		writeD(0x00000000);       // 寫入0，可能表示無額外數據
	}

	// 私有方法：構建封包，包含坐標
	private void buildPacket(int objid, int gfxid, int aid, int x, int y) {
		// TODO: SPR錯誤輸出測試
		if(gfxid == 1100) {
			new Throwable().printStackTrace();
		}
		// aid 未使用
		writeC(Opcodes.S_EFFECT); // 寫入操作碼
		writeD(objid);            // 寫入對象ID
		writeH(gfxid);            // 寫入GFX ID
		writeH(0);                // 寫入0，可能表示無額外數據
		writeD(0x00000000);       // 寫入0，可能表示無額外數據

		writeH(x);                // 寫入x坐標
		writeH(y);                // 寫入y坐標
	}

	@override
	public byte[] getContent() {
		return getBytes();
	}

	@override
	public String getType() {
		return S_SKILL_SOUND;
	}
}


