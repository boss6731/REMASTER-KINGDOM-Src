package l1j.server.MJWebServer.Dispatcher.Template.Item.POJO;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.templates.L1Item;

public class MJItemResource {
	public int id;
	public MJNameCodeInfo category;
	public String image;
	public String name;
	public String goodsId;
	public boolean eventItem;
	public boolean cashItem;
	public boolean crashable;
	public boolean _public;
	public boolean oldEnchantRuleApplicable;
	public boolean hasAttribute;
	public String itemType;
	public boolean enchantable;
	public boolean tradable;
	
	public MJItemResource(){}
	public MJItemResource(int itemId){
		L1Item template = ItemTable.getInstance().getTemplate(itemId);
		id = itemId;
		category = MJNameCodeInfo.CATEGORY.select_category(template);
		image = String.format("%s.png", template.getGfxId());
		name =  template.getName();
		goodsId = String.valueOf(itemId);
		eventItem = false;
		cashItem = false;
		crashable = false;
		oldEnchantRuleApplicable = false;
		hasAttribute = false;
		itemType = category.code == MJNameCodeInfo.CATEGORY.ETC.code ? "ETC" : "EQUIP";
		enchantable = template.get_safeenchant() >= 0;
		tradable = template.isTradable();
		_public = true;
	}
}
