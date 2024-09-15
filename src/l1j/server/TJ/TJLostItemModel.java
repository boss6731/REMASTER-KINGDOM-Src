package l1j.server.TJ;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;

public class TJLostItemModel {
	static TJLostItemModel newModel(ResultSet rs) throws SQLException {
		TJLostItemModel model = new TJLostItemModel();
		model.characterId = rs.getInt("character_id");
		model.characterName = rs.getString("character_name");
		model.itemObjectId = rs.getInt("item_object_id");
		model.itemId = rs.getInt("item_id");
		model.itemName = rs.getString("item_name");
		model.enchant = rs.getInt("enchant");
		model.elementalEnchant = rs.getInt("elemental_enchant");
		model.instanceBless = rs.getInt("instance_bless");
		model.instanceCustomBless = rs.getInt("instance_custom_bless");
		model.lostTime = rs.getLong("lost_time");
		model.recoveryCount = rs.getInt("recovery_count");
		return model;
	}
	
	int characterId;
	String characterName;
	int itemObjectId;
	int itemId;
	String itemName;
	int enchant;
	int elementalEnchant;
	int instanceBless;
	int instanceCustomBless;
	long lostTime;
	int recoveryCount;
	TJLostItemModel(){
	}

	public int characterId(){
		return characterId;
	}

	public String characterName(){
		return characterName;
	}

	public int itemObjectId(){
		return itemObjectId;
	}

	public int itemId(){
		return itemId;
	}

	public String itemName(){
		return itemName;
	}

	public int enchant(){
		return enchant;
	}

	public int elementalEnchant(){
		return elementalEnchant;
	}

	public int instanceBless(){
		return instanceBless;
	}
	
	public int instanceCustomBless() {
		return instanceCustomBless;
	}

	public long lostTime(){
		return lostTime;
	}

	public int recoveryCount(){
		return recoveryCount;
	}

	L1ItemInstance itemInstance() {
		L1ItemInstance item = ItemTable.getInstance().createItem(itemId());
		if(item == null) {
			return null;
		}
		
		item.setEnchantLevel(enchant());
		item.setBless(instanceBless());
		item.set_bless_level(instanceCustomBless());
		item.setAttrEnchantLevel(elementalEnchant());
		item.setCount(1);
		item.setIdentified(true);
		return item;
	}
}
