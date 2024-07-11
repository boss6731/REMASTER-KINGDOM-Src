package l1j.server.MJDShopSystem;

import java.sql.ResultSet;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;

/**
 *MJD 商店商品
 *動態商店物品。
 *由 mjsoft 製作，2016 年。
 ***/
/** 包含商店物品的資訊。 **/
public class MJDShopItem {
	public int objId; // 物件 ID
	public int itemId; // 物品 ID
	public String name;
	public int invId; // 背包 ID
	public int count; // 數量
	public int price; // 價格
	public int iden; // 物品的狀態
	public int enchant; // 附魔數值
	public int attr; // 屬性
	public boolean isPurchase; // 銷售(false)，收購(true)

	public static MJDShopItem create(L1ShopItem sitem, int i, boolean isPurchase) {
		L1Item item = ItemTable.getInstance().getTemplate(sitem.getItemId());
		MJDShopItem ditem = new MJDShopItem();
		ditem.objId = i;
		ditem.itemId = sitem.getItemId();
		ditem.name = item.getName();
		ditem.invId = item.getGfxId();
		ditem.count = sitem.get_count();
		ditem.price = sitem.getPrice();
		ditem.iden = item.getBless();
		ditem.enchant = sitem.getEnchant();
		ditem.attr = 0;
		ditem.isPurchase = isPurchase;
		return ditem;
	}

	public static <L1ItemInstance> MJDShopItem create(L1ItemInstance item, int count, int enchant, int attrenc,
			int bless, int price,
			boolean isPurchase) {
		MJDShopItem ditem = new MJDShopItem();
		ditem.objId = ((Object) item).getId();
		ditem.itemId = item.getItemId();
		ditem.name = item.getName();
		ditem.invId = item.get_gfxid();
		ditem.count = count;
		ditem.price = price;
		if (!item.isIdentified())
			ditem.iden = -1;
		else
			ditem.iden = bless;
		ditem.enchant = enchant;
		ditem.attr = attrenc;
		ditem.isPurchase = isPurchase;
		return ditem;
	}

	public static MJDShopItem create(ResultSet rs) throws Exception {
		MJDShopItem ditem = new MJDShopItem();
		ditem.objId = rs.getInt("item_objid");
		ditem.itemId = rs.getInt("item_id");
		ditem.name = rs.getString("Item_name");
		ditem.invId = rs.getInt("invgfx");
		ditem.count = rs.getInt("count");
		ditem.price = rs.getInt("price");
		ditem.iden = MJDShopStorage.getAppIden2PackIden(rs.getInt("iden"));
		ditem.enchant = rs.getInt("enchant");
		ditem.attr = rs.getInt("attr");
		ditem.isPurchase = rs.getBoolean("type");
		return ditem;
	}
}
