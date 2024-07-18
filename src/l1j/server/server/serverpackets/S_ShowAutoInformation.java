package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ShowAutoInformation extends ServerBasePacket {
	private static final String S_ShowAutoInformation = "[S] S_SHOW_AUTOINFORMATION";

	// 構造函數，接受角色實例
	public S_ShowAutoInformation(L1PcInstance pc) {
		buildPacket(pc); // 構建封包
	}

	// 對指定角色構建封包
	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_BOARD_READ); // 寫入操作碼，表示讀取看板
		writeD(0); // 寫入整數0，可能表示空白或無數據
		writeS("自動狩獵"); // 寫入自動狩獵標題
		writeS("使用者手冊"); // 寫入使用說明書標題
		writeS(""); // 寫入空白行

		// 定義不同職業的物品說明
		StringBuffer type1 = new StringBuffer();
		String type1Potion = "藥水:快速體力恢復藥水(500個)\r\n");
		String type2Potion = "藥水:快速強力體力恢復藥水(500個)\r\n");
		String etcItem = "卷軸:瞬間移動卷軸(300張)\r\n");
		String etcItem ="卷軸:奇岩村回程卷軸(5張)\r\n");
		String etcItem = "卷軸 :變身卷軸(20張)\r\n");

		// 根據角色職業附加不同的物品說明
		if (pc.isCrown()) {
			type1.append("王族：強力卷軸(5個)\r\n");
			type1.append("勇氣：惡魔之血(30個)\r\n");
		} else if (pc.isKnight()) {
			type1.append("騎士：強力卷軸(5個)\r\n");
			type1.append("勇氣：濃縮勇氣(5個)\r\n");
		} else if (pc.isWarrior()) {
			type1.append("戰士：強力卷軸(5個)\r\n");
			type1.append("勇氣：濃縮勇氣(5個)\r\n");
			type1.append("勇氣：結晶體(2000個)\r\n");
		} else if (pc.isDragonKnight()) {
					type1.append("龍騎士：強力卷軸(5個)\r\n");
					type1.append("材料：刻印的骨片(100個)\r\n");
		} else if (pc.isElf()) {
			if (pc.getWeapon() == null || pc.getWeapon().getItem().getType1() != 20) {
				type1.append("精靈：強力卷軸(5個)\r\n");
				type1.append("材料：精靈玉(100個)\r\n");
				type1.append("材料：精靈餅(30個)\r\n");
			} else {
				type1.append("妖精：強力卷軸(5個)\r\n");
				type1.append("勇氣：濃縮集中(5個)\r\n");
				type1.append("箭矢：銀箭(3000支)\r\n");
			}
		} else if (pc.isDarkElf()) {
			type1.append("黑暗妖精：強力卷軸(5個)\r\n");
					type1.append("材料：黑曜石(100個)\r\n");
		} else if (pc.isWizard()) {
			type1.append("法師：強力卷軸(5個)\r\n");
			type1.append("輔助：濃縮智慧(3個)\r\n");
			type1.append("輔助：濃縮魔力(3個)\r\n");
			type1.append("材料：魔力石(100個)\r\n");
		} else if (pc.isBlackWizard()) {
			type1.append("黑魔導：強力卷軸(5個)\r\n");
			type1.append("輔助：濃縮智慧(3個)\r\n");
			type1.append("材料：尤格德拉(300個)\r\n");
		} else if (pc.isFencer()) { // 新增劍士
			type1.append("劍士：強力卷軸(5個)\r\n");
			type1.append("材料：下級魔晶石(50個)\r\n");
			type1.append("材料：中級魔晶石(30個)\r\n");
		} else if (pc.isGoldenKnight()) { // 新增黃金槍騎
			type1.append("黃金槍騎：強力卷軸(5個)\r\n");
			type1.append("輔助：黃金的恩典(10個)\r\n");
			type1.append("材料：黃金硬幣(100個)\r\n");
		}

		// 寫入物品說明
		writeS("類型 1.\r\n" + type1Potion + type1.toString() + etcItem +
				"\r\n "
				+"類型 2.\r\n" + type2Potion + type1.toString() + etcItem); // 內容
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_ShowAutoInformation;
	}
}


