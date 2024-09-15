package l1j.server.server.model.shop;

import javolution.util.FastTable;
import l1j.server.server.datatables.AdenShopTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1AdenShopItem;
import l1j.server.server.templates.L1Item;

public class L1AdenShop {

	private FastTable<Item> buylist = new FastTable<Item>();
	private boolean bugok = false;

	public L1AdenShop() {
	}

	private int _totalPrice = 0;

	public void add(L1PcInstance pc, int id, int count) {
		try {
			if (_totalPrice < 0)
				return;
			L1AdenShopItem item = AdenShopTable.getInstance().get(id);
			if (item == null)
				return;
			int price = item.getPrice();
			if (price == 0)
				return;
			_totalPrice += price * count;
			if (_totalPrice < 0 || _totalPrice >= 1000000000) {
				bugok = true;
				return;
			}
			if (count <= 0 || count > 50) {
				bugok = true;
				return;
			}
			Item listitem = new Item();
			listitem.itemid = id;
			listitem.count = count * (item.getPackCount() > 0 ? item.getPackCount() : 1);
			buylist.add(listitem);
			
			pc.sendPackets(3513);
		} catch (Exception e) {

		}
	}

	public boolean BugOk() {
		// TODO 自動生成的方法存根
		return bugok;
	}

	public boolean commit(L1PcInstance pc) {
		// TODO 自動生成的方法存根

		if (pc.getNcoin() < _totalPrice) {
			pc.sendPackets(new S_SystemMessage("N幣不足。"));
			return false;
		}
		
		pc.addNcoin(-_totalPrice);
		SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
		
		if (pwh == null)
			return false;
		for (Item listitem : buylist) {
			if (listitem.itemid == 0 || listitem.count == 0)
				continue;
			L1Item tempItem = ItemTable.getInstance().getTemplate(listitem.itemid);
			if (tempItem.isStackable()) {
				L1ItemInstance item = ItemTable.getInstance().createItem(listitem.itemid);
				item.setIdentified(true);
				item.setEnchantLevel(0);
				item.setCount(listitem.count);
				item.setChargeCount(tempItem.getMaxChargeCount());
				pwh.storeTradeItem(item);
			} else {
				L1ItemInstance item = null;
				int createCount;
				for (createCount = 0; createCount < listitem.count; createCount++) {
					item = ItemTable.getInstance().createItem(listitem.itemid);
					item.setIdentified(true);
					item.setEnchantLevel(0);
					item.setChargeCount(tempItem.getMaxChargeCount());
					pwh.storeTradeItem(item);
				}
			}
		}
		return true;
	}

	class Item {
		public int itemid = 0;
		public int count = 0;
	}

}
