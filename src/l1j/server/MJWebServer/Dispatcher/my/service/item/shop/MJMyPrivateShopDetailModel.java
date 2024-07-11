package l1j.server.MJWebServer.Dispatcher.my.service.item.shop;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJDShopSystem.MJDShopStorage;
import l1j.server.server.model.Instance.L1ItemInstance;

public class MJMyPrivateShopDetailModel {
	static MJMyPrivateShopDetailModel newShop(ResultSet rs) throws SQLException{
		MJMyPrivateShopDetailModel model = new MJMyPrivateShopDetailModel();
		model.enchant = rs.getInt("enchant");
		model.quantity = rs.getInt("count");
		model.price = rs.getInt("price");
		
		int iden = MJDShopStorage.getAppIden2PackIden(rs.getInt("iden"));
		model.identified = iden != -1;
		model.bless = iden;
		
		int elemental = rs.getInt("attr");
		model.elementalType = L1ItemInstance.pureAttrEnchantLevel(elemental);
		model.elementalValue = L1ItemInstance.attrEnchantToElementalType(elemental);
		model.ownerName = rs.getString("char_name");
		model.locationName = "Áß¾Ó±¸¿ª";
		return model;
	}
	
	int enchant;
	int quantity;
	int price;
	boolean identified;
	int bless;
	int elementalType;
	int elementalValue;
	String ownerName;
	String locationName;
	MJMyPrivateShopDetailModel(){
		
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
	
	public String locationName(){
		return locationName;
	}
}
