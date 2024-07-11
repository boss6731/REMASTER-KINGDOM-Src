package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_GOODS_INVEN_NOTI;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.templates.L1Item;

public class MJMyTradeShopModel {
	static MJMyTradeShopModel newModel(L1PcInstance pc, L1ItemInstance item, String address, String displayPart, String subject, int quantity, int price, int pricePerUnit){
		MJMyTradeShopModel model = new MJMyTradeShopModel();
		model.tradeNo = MJMyTradeShopProvider.provider().tradeNo();
		model.tradeType = MJMyShopTradeType.SELL;
		model.displayPart = displayPart;
		model.subject = subject;
		model.registeredModel = MJMyTradeShopUserModel.newModel(pc, address);
		model.itemModel = MJMyTradeShopItemModel.newModel(item, price, pricePerUnit, quantity);
		return model;
	}
	
	static MJMyTradeShopModel newModel(ResultSet rs) throws SQLException{
		MJMyTradeShopModel model = new MJMyTradeShopModel();
		model.tradeNo = rs.getInt("trade_no");
		model.completed = MJMyTradeShopCompleteCode.fromResultSet(rs);
		model.subject = rs.getString("subject");
		model.displayPart = rs.getString("displayPart");
		model.tradeType = MJMyShopTradeType.fromName(rs.getString("trade_type"));
		model.commission = rs.getDouble("commission");
		model.registeredModel = MJMyTradeShopUserModel.newModel(rs, "reg");
		model.itemModel = MJMyTradeShopItemModel.newModel(rs);
		model.consumerModel = MJMyTradeShopUserModel.newModel(rs, "cons");		
		return model;
	}
	
	int tradeNo;
	MJMyTradeShopCompleteCode completed;
	double commission;
	String subject;
	String displayPart;
	MJMyShopTradeType tradeType;
	MJMyTradeShopUserModel registeredModel;
	MJMyTradeShopItemModel itemModel;
	MJMyTradeShopUserModel consumerModel;
	MJMyTradeShopModel(){
	}
	
	public int tradeNo(){
		return tradeNo;
	}
	
	public MJMyTradeShopCompleteCode completed(){
		return completed;
	}
	
	public String subject(){
		return subject;
	}
	
	public String displayPart(){
		return displayPart;
	}
	
	public double commission(){
		return commission;
	}
	
	public MJMyShopTradeType tradeType(){
		return tradeType;
	}
	
	public boolean hasRegistered(){
		return registeredModel != null;
	}
	
	public MJMyTradeShopUserModel registeredModel(){
		return registeredModel;
	}
	
	public boolean hasItem(){
		return itemModel != null;
	}
	
	public MJMyTradeShopItemModel itemModel(){
		return itemModel;
	}

	public boolean hasConsumer(){
		return consumerModel != null;
	}
	
	public MJMyTradeShopUserModel consumerModel(){
		return consumerModel;
	}
	
	public boolean onCancel(final String account){
		completed = MJMyTradeShopCompleteCode.CANCELED;
		return MJMyTradeShopProvider.provider().onCancelTradeModel(tradeNo, account);
	}
	
	public boolean onBuy(L1PcInstance pc, String address, double commission){
		this.commission = commission;
		this.consumerModel = MJMyTradeShopUserModel.newModel(pc, address);
		return MJMyTradeShopProvider.provider().onBuyTradeModel(this);
	}
	
	public void toItem(L1PcInstance pc){
		L1ItemInstance item = ItemTable.getInstance().createItem(itemModel.itemId(), itemModel.identified());
		item.setEnchantLevel(itemModel.enchant());
		item.setAttrEnchantLevel(itemModel.elementalEnchant());
		item.set_Doll_Bonus_Level(itemModel.dollbonuslevel());
		item.set_Doll_Bonus_Value(itemModel.dollbonusvalue());
		item.setBlessTypeValue(itemModel.BlessTypeValue());
		
		if(itemModel.bless() != 1){
			item.setBless(itemModel.bless());
		}
		if (itemModel.blesslevel() != 0) {
			item.set_bless_level(itemModel.blesslevel());
			item.setBless(0);// √‡∫π¿∏∑Œ πŸ≤„¡‹ ¿ÃπÃ¡ˆ
			pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS_LEVEL);
			pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS_LEVEL);
		}
		if (itemModel.BlessType() != 0) {
			item.setBlessType(itemModel.BlessType());
			item.setBless(0);// √‡∫π¿∏∑Œ πŸ≤„¡‹ ¿ÃπÃ¡ˆ
		}
		
		item.setCount(itemModel.quantity());
		SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
		pwh.storeTradeItem(item);
		SC_GOODS_INVEN_NOTI.do_send(pc);
	}
	
	public static class MJMyTradeShopUserModel{
		static MJMyTradeShopUserModel newModel(L1PcInstance pc, String address){
			MJMyTradeShopUserModel model = new MJMyTradeShopUserModel();
			model.actionMillis = System.currentTimeMillis();
			model.address = address;
			model.characterId = pc.getId();
			model.characterName = pc.getName();
			model.accountName = pc.getAccountName();
			return model;
		}
		
		static MJMyTradeShopUserModel newModel(ResultSet rs, String prefix) throws SQLException{
			MJMyTradeShopUserModel model = new MJMyTradeShopUserModel();
			Timestamp ts = rs.getTimestamp(MJString.joinAsOnce("_", prefix, "date"));
			if(ts == null){
				return null;
			}
			
			model.actionMillis = ts.getTime();
			model.address = rs.getString(MJString.joinAsOnce("_", prefix, "addr"));
			model.characterId = rs.getInt(MJString.joinAsOnce("_", prefix, "char_id"));
			model.characterName = rs.getString(MJString.joinAsOnce("_", prefix, "char_name"));
			model.accountName = rs.getString(MJString.joinAsOnce("_", prefix, "account"));
			return model;
		}

		long actionMillis;
		String address;
		int characterId;
		String characterName;
		String accountName;
		MJMyTradeShopUserModel(){
			actionMillis = 0;
			address = MJString.EmptyString;
			characterId = 0;
			characterName = MJString.EmptyString;
			accountName = MJString.EmptyString;
		}
		
		public long actionMillis(){
			return actionMillis;
		}
		
		public String address(){
			return address;
		}
		
		public int characterId(){
			return characterId;
		}
		
		public String characterName(){
			return characterName;
		}
		
		public String accountName(){
			return accountName;
		}
	}
	
	public static class MJMyTradeShopItemModel{
		static MJMyTradeShopItemModel newModel(L1ItemInstance item, int price, int pricePerUnit, int quantity){
			MJMyTradeShopItemModel model = new MJMyTradeShopItemModel();
			model.itemId = item.getItemId();
			model.itemName = item.getName();
			
			L1Item template = item.getItem();
			model.iconId = template.getGfxId();
			model.price = price;
			model.pricePerUnit = pricePerUnit;
			model.quantity = quantity;
			model.enchant = item.getEnchantLevel();
			model.bless = template.getBless();
			model.identified = item.isIdentified();
			model.elementalEnchant = item.getAttrEnchantLevel();
			
			model.dollbonuslevel = item.get_Doll_Bonus_Level();
			model.dollbonusvalue = item.get_Doll_Bonus_Value();
			model.blesslevel = item.get_bless_level();
			
			model.BlessType = item.getBlessType();
			model.BlessTypeValue = item.getBlessTypeValue();
			return model;
		}
		
		static MJMyTradeShopItemModel newModel(ResultSet rs) throws SQLException{
			MJMyTradeShopItemModel model = new MJMyTradeShopItemModel();
			model.itemId = rs.getInt("item_id");
			model.itemName = rs.getString("item_name");
			model.iconId = rs.getInt("icon_id");
			model.price = rs.getInt("price");
			model.pricePerUnit = rs.getInt("pricePerUnit");
			model.quantity = rs.getInt("quantity");
			model.enchant = rs.getInt("enchant");
			model.bless = rs.getInt("bless");
			model.identified = rs.getBoolean("identified");
			model.elementalEnchant = rs.getInt("elemental_enchant");
			
			model.dollbonuslevel = rs.getInt("doll_bonus_level");
			model.dollbonusvalue = rs.getInt("doll_bonus_value");
			model.blesslevel = rs.getInt("bless_level");

			model.BlessType = rs.getInt("bless_type");
			model.BlessTypeValue = rs.getInt("bless_type_value");
			return model;
		}
		
		int itemId;
		String itemName;
		int iconId;
		int price;
		int pricePerUnit;
		int quantity;
		int enchant;
		boolean identified;
		int bless;
		int elementalEnchant;
		int dollbonuslevel;
		int dollbonusvalue;
		int blesslevel;
		int BlessType;
		int BlessTypeValue;
		
		public int itemId(){
			return itemId;
		}

		public String itemName(){
			return itemName;
		}
		
		public int iconId(){
			return iconId;
		}
		
		public int pricePerUnit(){
			return pricePerUnit;
		}
		
		public int price(){
			return price;
		}

		public int quantity(){
			return quantity;
		}

		public int enchant(){
			return enchant;
		}

		public boolean identified(){
			return identified;
		}

		public int bless(){
			return bless;
		}

		public int elementalEnchant(){
			return elementalEnchant;
		}
		
		public int dollbonuslevel(){
			return dollbonuslevel;
		}
		
		public int dollbonusvalue(){
			return dollbonusvalue;
		}
		
		public int blesslevel(){
			return blesslevel;
		}
		
		public int BlessType(){
			return BlessType;
		}
		
		public int BlessTypeValue(){
			return BlessTypeValue;
		}
		
	}
}
