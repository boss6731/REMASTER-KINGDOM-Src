package l1j.server.MJWebServer.Dispatcher.Template.Item.POJO;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.templates.L1Item;

public class MJPowerBookItem {
	public String enchant;
	public String name;
	public String image;
	public String goodsId;
	
	public MJPowerBookItem(){}
	public MJPowerBookItem(int item_id) {
		L1Item item = ItemTable.getInstance().getTemplate(item_id);
		enchant = item.get_safeenchant() == -1 ? "不可能" : "可能";
		name = item.getName();
		image = String.format("%s.png", item.getGfxId());
		goodsId = String.valueOf(item_id);
	}


