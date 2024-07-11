package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import l1j.server.MJTemplate.MJTimesFormatter;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyTradeShopModel.MJMyTradeShopItemModel;
import l1j.server.MJWebServer.Dispatcher.my.service.item.shop.MJMyTradeShopModel.MJMyTradeShopUserModel;
import l1j.server.server.model.Instance.L1ItemInstance;

public class MJMyTradeShopDetailModel {
	static MJMyTradeShopDetailModel newShop(MJMyTradeShopModel shopModel){
		MJMyTradeShopDetailModel model = new MJMyTradeShopDetailModel();
		MJMyTradeShopItemModel itemModel = shopModel.itemModel();
		MJMyTradeShopUserModel ownerModel = shopModel.registeredModel();
		
		model.tradeNo = shopModel.tradeNo();
		model.itemName = itemModel.itemName();
		model.enchant = itemModel.enchant();
		model.quantity = itemModel.quantity();
		model.price = itemModel.price();
		model.identified = itemModel.identified();
		model.bless = itemModel.bless();
		
		int elemental = itemModel.elementalEnchant();
		model.elementalType = L1ItemInstance.pureAttrEnchantLevel(elemental);
		model.elementalValue = L1ItemInstance.attrEnchantToElementalType(elemental);
		model.subject = shopModel.subject();
		model.ownerId = ownerModel.characterId();
		model.ownerName = ownerModel.characterName();
		model.displayPart = shopModel.displayPart();
		model.dollbonuslevel = itemModel.dollbonuslevel();
		model.dollbonusvalue = itemModel.dollbonusvalue();
		model.blesslevel = itemModel.blesslevel();

		model.BlessType = itemModel.BlessType();
		model.BlessTypeValue = itemModel.BlessTypeValue();
		
		model.regDate = MJTimesFormatter.BASIC.toString(ownerModel.actionMillis());
		return model;
	}
	
	int tradeNo;
	String itemName;
	int enchant;
	int quantity;
	int price;
	boolean identified;
	int bless;
	int elementalType;
	int elementalValue;
	int elementalEnchant;
	int ownerId;
	String subject;
	String ownerName;
	String displayPart;
	String regDate;
	int dollbonuslevel;
	int dollbonusvalue;
	int blesslevel;
	int BlessType;
	int BlessTypeValue;
	
	public int tradeNo(){
		return tradeNo;
	}
	
	public int enchant(){
		return enchant;
	}
	
	public int quantity(){
		return quantity;
	}

	public int price(){
		return price;
	}
	
	public boolean identified(){
		return identified;
	}
	
	public int bless(){
		return bless;
	}
	
	public int elementalType(){
		return elementalType;
	}
	
	public int elementalValue(){
		return elementalValue;
	}
	
	public String ownerName(){
		return ownerName;
	}
	
	public int ownerId(){
		return ownerId;
	}
}
