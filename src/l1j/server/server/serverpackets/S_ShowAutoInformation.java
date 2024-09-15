package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ShowAutoInformation extends ServerBasePacket {
	private static final String S_ShowAutoInformation = "[S] S_SHOW_AUTOINFORMATION";
	public S_ShowAutoInformation(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(0);
		writeS("自動狩獵");
		writeS("使用說明書");
		writeS("");

		StringBuffer type1 = new StringBuffer();
		String type1Potion = " 藥水 : 迅速體力恢復劑(500個)\r\n";
		String type2Potion = " 藥水 : 迅速強力體力恢復劑(500個)\r\n";
		String etcItem = " 卷軸 : 瞬間移動 (300張)\r\n" +"  卷軸 : 奇岩村莊返回(5張)\r\n" +" 卷軸 : 變身卷軸(20張)";

		if(pc.isCrown()) {
			type1.append(" 輔助 : 強化卷(5個)\r\n");
			type1.append(" 藥水 : 惡魔的血(30個)\r\n");
		}else if(pc.isKnight()) {
			type1.append(" 輔助 : 強化卷(5個)\r\n");
			type1.append(" 藥水 : 濃縮勇氣(5個)\r\n");
		} else if (pc.isWarrior()) {
			type1.append(" 輔助 : 強化卷(5個)\r\n");
			type1.append(" 藥水 : 濃縮勇氣(5個)\r\n");
			type1.append(" 材料 : 結晶體(2000個)\r\n");
		}else if(pc.isDragonknight()) {
			type1.append("輔助 : 強化卷(5個)\r\n");
			type1.append("材料 : 雕刻的骨頭碎片(100個)\r\n");
		}else if(pc.isElf()) {
			if(pc.getWeapon() == null || pc.getWeapon().getItem().getType1() != 20) {
				type1.append(" 輔助 : 強化卷(5個)\r\n");
				type1.append(" 材料 : 精靈之玉(100個)\r\n");
				type1.append(" 材料 : 精靈餅乾(30個)\r\n");
			}else {
				type1.append(" 輔助 : 強化卷(5個)\r\n");
				type1.append(" 藥水 : 濃縮集中(5個)\r\n");
				type1.append(" 箭 : 銀箭(3000支)\r\n");
			} 
		}else if(pc.isDarkelf()) {
			type1.append(" 輔助 : 強化卷(5個)\r\n");
			type1.append(" 材料 : 黑魔石(100個)\r\n");
		}else if(pc.isWizard()) {
			type1.append(" 藥水 : 濃縮智力(3個)\r\n");
			type1.append(" 藥水 : 濃縮魔力(3個)\r\n");
			type1.append(" 材料 : 魔法寶石(100個)\r\n");
		}else if(pc.isBlackwizard()) {
			type1.append(" 輔助 : 強化卷(5個)\r\n");
			type1.append(" 藥水 : 濃縮智力(3個)\r\n");
			type1.append(" 材料 : 世界樹之葉(300個)\r\n");
		}

		writeS("類型 1.\r\n" +type1Potion +type1.toString()  +etcItem + "\r\n" +
				"類型 2.\r\n" +type2Potion +type1.toString() +etcItem);//內容
		}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_ShowAutoInformation;
	}
}