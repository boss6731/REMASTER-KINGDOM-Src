package l1j.server.server.server.command.executor;

import l1j.server.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

import java.util.StringTokenizer;

public class L1CreateItem implements L1CommandExecutor {

	private L1CreateItem() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1CreateItem();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
//			if (st.hasMoreTokens()) {  //判斷是否還有更多的 token 可供分割
			String nameid = st.nextToken();
			int count = 1;
			if (st.hasMoreTokens()) {
				count = Integer.parseInt(st.nextToken());
			}
			int enchant = 0;
			if (st.hasMoreTokens()) {
				enchant = Integer.parseInt(st.nextToken());
			}
			 int attrenchant = 0;
			   if (st.hasMoreTokens()) {
			    attrenchant = Integer.parseInt(st.nextToken());
			   }
			   
			int isId = 0;
			if (st.hasMoreTokens()) {
				isId = Integer.parseInt(st.nextToken());
			}

			int itemid = 0;
			try {
				itemid = Integer.parseInt(nameid);
			} catch (NumberFormatException e) {
				itemid = ItemTable.getInstance(). findItemIdByNameWithoutSpace(
						nameid);
				if (itemid == 0) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("未找到該物品。")));
					return;
				}
			}
			L1Item temp = ItemTable.getInstance(). getTemplate(itemid);
			if (temp != null) {
				if (temp.isStackable()) {
					L1ItemInstance item = ItemTable.getInstance(). createItem(itemid);
					item.setEnchantLevel(0);
					item.setCount(count);
					if (isId == 1) {
						item.setIdentified(true);
					}
					if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
						pc.getInventory().storeItem(item);
						pc.sendPackets(String.valueOf(new S_SystemMessage("\fY物品:[\\aA" + item.getLogName() + "\fY] 物品ID:[\\aA" + itemid + "\fY] 附魔:[\\aA" + enchant + "\fY] \\aG已創建")));
					}
				} else {
					L1ItemInstance item = null;
					int createCount;
					for (createCount = 0; createCount < count; createCount++) {
						item = ItemTable.getInstance(). createItem(itemid);
						item.setEnchantLevel(enchant);
						item.setAttrEnchantLevel(attrenchant);
						if (isId == 1) {
							item.setIdentified(true);
						}
						if (pc.getInventory(). checkAddItem(item, 1) == L1Inventory.OK) {
							pc.getInventory(). storeItem(item);
						} else {
							break;
						}
					}
					if (createCount > 0) {
						pc.sendPackets(String.valueOf(new S_SystemMessage("\fY物品:[\\aA" + item.getLogName() + "\fY] 物品ID:[\\aA" + itemid + "\fY] 附魔:[\\aA" + enchant + "\fY] \\aG創建")));
					}
				}
			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage("指定 ID 的物品不存在")));
			}
//			}
		} catch (Exception e) {
		//	e.printStackTrace();
			pc.sendPackets(String.valueOf(new S_SystemMessage(".物品 [名稱] [數量] [附魔] [屬性1~20] [確認0~1]")));
		}
	}
}
