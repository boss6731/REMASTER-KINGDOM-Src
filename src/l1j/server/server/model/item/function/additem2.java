package l1j.server.server.model.item.function;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

public class additem2 {

	public static void clickItem(L1PcInstance pc, int itemId, L1ItemInstance l1iteminstance) {

		switch (itemId) {
			case 3000508: // 裝備箱（初學者）
				if (pc.getInventory().checkItem(3000508, 1)) { // 檢查物品及數量
					pc.getInventory().consumeItem(3000508, 1); // 刪除物品及數量

					if (pc.isWarrior()) { // 檢查職業是否是戰士
					beginnerItem1(pc, 3000573, 1, 0, 129, 0, true);
				}
				if (pc.isKnight()) {
					beginnerItem1(pc, 3000572, 1, 0, 129, 0, true);
				}
				if (pc.isDragonknight()) {
					beginnerItem1(pc, 3000578, 1, 0, 129, 0, true);
				}
				if (pc.isCrown()) {
					beginnerItem1(pc, 3000579, 1, 0, 129, 0, true);
				}
				if (pc.isWizard()) {
					beginnerItem1(pc, 3000575, 1, 0, 129, 0, true);
				}
				if (pc.isBlackwizard()) {
					beginnerItem1(pc, 3000576, 1, 0, 129, 0, true);
				}
				if (pc.isElf()) {
					beginnerItem1(pc, 3000574, 1, 0, 129, 0, true);
				}
				if (pc.isDarkelf()) {
					beginnerItem1(pc, 3000577, 1, 0, 129, 0, true);
				}
			}
			break;
			case 717: // Lv.88 達成禮物箱
				if (pc.getInventory().checkItem(717, 1)) { // 檢查物品及數量
					pc.getInventory().consumeItem(717, 1); // 刪除物品及數量
					if (pc.isWarrior()) { // 檢查是否是戰士
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
					beginnerItem1(pc, 4100164, 1, 0, 129, 0, true); // 전설 변신권 상자
				}
				if (pc.isKnight()) {
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
					beginnerItem1(pc, 4100164, 1, 0, 129, 0, true); // 전설 변신권 상자
				}
				if (pc.isDragonknight()) {
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
					beginnerItem1(pc, 4100164, 1, 0, 129, 0, true); // 전설 변신권 상자
				}
				if (pc.isCrown()) {
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
					beginnerItem1(pc, 4100164, 1, 0, 129, 0, true); // 전설 변신권 상자
				}
				if (pc.isWizard()) {
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
					beginnerItem1(pc, 4100164, 1, 0, 129, 0, true); // 전설 변신권 상자
				}
				if (pc.isBlackwizard()) {
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
					beginnerItem1(pc, 4100164, 1, 0, 129, 0, true); // 전설 변신권 상자
				}
				if (pc.isElf()) {
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
					beginnerItem1(pc, 4100164, 1, 0, 129, 0, true); // 전설 변신권 상자
				}
				if (pc.isDarkelf()) {
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
					beginnerItem1(pc, 4100164, 1, 0, 129, 0, true); // 전설 변신권 상자
				}
			}
			break;
			case 721: // Lv.89 達成禮物箱
				if (pc.getInventory().checkItem(721, 1)) { // 檢查物品及數量
					pc.getInventory().consumeItem(721, 1); // 刪除物品及數量

					if (pc.isWarrior()) { // 檢查是否是戰士
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
				}
				if (pc.isKnight()) {
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
				}
				if (pc.isDragonknight()) {
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
				}
				if (pc.isCrown()) {
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
				}
				if (pc.isWizard()) {
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
				}
				if (pc.isBlackwizard()) {
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
				}
				if (pc.isElf()) {
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
				}
				if (pc.isDarkelf()) {
					beginnerItem1(pc, 400254, 10, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100046, 1, 0, 129, 0, true); // 신비한 마법인형 상자(특수)
				}
			}
			break;
			case 722: // Lv.90 達成禮物箱
				if (pc.getInventory().checkItem(722, 1)) { // 檢查物品及數量
					pc.getInventory().consumeItem(722, 1); // 刪除物品及數量

					if (pc.isWarrior()) { // 檢查是否是戰士
					beginnerItem1(pc, 400254, 30, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100165, 1, 0, 129, 0, true); // 신화 변신권 상자
				}
				if (pc.isKnight()) {
					beginnerItem1(pc, 400254, 30, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100165, 1, 0, 129, 0, true); // 신화 변신권 상자
				}
				if (pc.isDragonknight()) {
					beginnerItem1(pc, 400254, 30, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100165, 1, 0, 129, 0, true); // 신화 변신권 상자
				}
				if (pc.isCrown()) {
					beginnerItem1(pc, 400254, 30, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100165, 1, 0, 129, 0, true); // 신화 변신권 상자
				}
				if (pc.isWizard()) {
					beginnerItem1(pc, 400254, 30, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100165, 1, 0, 129, 0, true); // 신화 변신권 상자
				}
				if (pc.isBlackwizard()) {
					beginnerItem1(pc, 400254, 30, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100165, 1, 0, 129, 0, true); // 신화 변신권 상자
				}
				if (pc.isElf()) {
					beginnerItem1(pc, 400254, 30, 0, 129, 0, true); // 아데나 수표(1억)
//					beginnerItem1(pc, 4100165, 1, 0, 129, 0, true); // 신화 변신권 상자
				}
				if (pc.isDarkelf()) {
					beginnerItem1(pc, 400254, 30, 0, 129, 0, true); // 아데나 수표(1억)
					beginnerItem1(pc, 4100165, 1, 0, 129, 0, true); // 신화 변신권 상자
				}
			}
			break;
			case 4100080: // 飾品包裝箱
				if (pc.getInventory().checkItem(4100080, 1)) { // 檢查物品及數量
					pc.getInventory().consumeItem(4100080, 1); // 刪除物品及數量

					if (pc.isWarrior()) { // 檢查是否是戰士
					beginnerItem1(pc, 4100081, 1, 1, 1, 0, true); // 악세패키지(격수)
				}
				if (pc.isKnight()) {
					beginnerItem1(pc, 4100081, 1, 1, 1, 0, true); // 악세패키지(격수)
				}
				if (pc.isDragonknight()) {
					beginnerItem1(pc, 4100081, 1, 1, 1, 0, true); // 악세패키지(격수)
				}
				if (pc.isCrown()) {
					beginnerItem1(pc, 4100081, 1, 1, 1, 0, true); // 악세패키지(격수)
				}
				if (pc.isWizard()) {
					beginnerItem1(pc, 4100083, 1, 1, 1, 0, true); // 악세패키지(환,마)
				}
				if (pc.isBlackwizard()) {
					beginnerItem1(pc, 4100083, 1, 1, 1, 0, true); // 악세패키지(환,마)
				}
				if (pc.isElf()) {
					beginnerItem1(pc, 4100082, 1, 1, 1, 0, true); // 악세패키지(요정)
				}
				if (pc.isDarkelf()) {
					beginnerItem1(pc, 4100081, 1, 1, 1, 0, true); // 악세패키지(격수)
				}
			}
			break;
		}
	}

	private static boolean beginnerItem1(L1PcInstance pc, int item_id, int count, int EnchantLevel, int Bless, int attr,
			boolean identi) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setIdentified(identi);
			item.setEnchantLevel(EnchantLevel);
			item.setAttrEnchantLevel(attr);
			item.setIdentified(true);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item, Bless);
				pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
				pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
			} else { // 가질 수 없는 경우는 지면에 떨어뜨리는 처리의 캔슬은 하지 않는다(부정 방지)
				pc.sendPackets(new S_ServerMessage(82));
				// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName())); //
			return true;
		} else {
			return false;
		}
	}
	
}