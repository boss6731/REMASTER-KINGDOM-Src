package l1j.server.server.server.command.executor;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.server.datatables.ItemTable;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;

public class L1DescIdArmor implements L1CommandExecutor {
	private L1DescIdArmor() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1DescIdArmor();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int descid = Integer.parseInt(st.nextToken(), 10);
			int count = Integer.parseInt(st.nextToken(), 10);
			int enchant = 0;
			if (st.hasMoreTokens()) {
				enchant = Integer.parseInt(st.nextToken());
			}
			int countA = -1;
			L1ItemInstance item = null;
			for (int i = 0; i < count; i++) {
				if (pc.getInventory().getSize() > 255) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("\\aG道具欄可保管的最大項目數量為256個。")));
					break;
				}
				countA++;
				item = ItemTable.getInstance().createItem(20456);
				item.getItem().setItemDescId(descid + i);
				item.getItem().setGfxId(1945);
				item.setCount(1);
				item.setEnchantLevel(enchant);
				item.getItem().setName(String.valueOf(descid + i));
				item.getItem().setNameId(String.valueOf(descid + i));
				item.setIdentified(true);
				pc.getInventory().storeItem(item);
			}
			pc.sendPackets(String.valueOf(new S_SystemMessage("\\aH防具 " + descid + "~" + String.valueOf(descid + countA) + " 已創建。")));
		} catch (Exception exception) {
			int count = 0;
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				if (item.getItemId() == 20456) {
					pc.getInventory().deleteItem(item);
					count++;
				}
			}
			if (count > 0) {
				pc.sendPackets(String.valueOf(new S_SystemMessage("\\aA防具確認用物品(" + count + ")已被刪除。")));
			} else {
				pc.sendPackets(String.valueOf(new S_SystemMessage("請輸入 " + cmdName + " [id] [出現的數量] [強化]。")));
			}
		}
	}
}
