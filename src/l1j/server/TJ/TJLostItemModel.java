package l1j.server.TJ;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TJLostItemModel {
	// 建立新的 TJLostItemModel 實例，根據給定的 ResultSet
	static TJLostItemModel newModel(ResultSet rs) throws SQLException {
		TJLostItemModel model = new TJLostItemModel();
		model.characterId = rs.getInt("character_id"); // 角色 ID
		model.characterName = rs.getString("character_name"); // 角色名稱
		model.itemObjectId = rs.getInt("item_object_id"); // 物品物件 ID
		model.itemId = rs.getInt("item_id"); // 物品 ID
		model.itemName = rs.getString("item_name"); // 物品名稱
		model.enchant = rs.getInt("enchant"); // 物品強化等級
		model.elementalEnchant = rs.getInt("elemental_enchant"); // 元素強化等級
		model.instanceBless = rs.getInt("instance_bless"); // 實例祝福狀態
		model.instanceCustomBless = rs.getInt("instance_custom_bless"); // 實例自訂祝福狀態
		model.lostTime = rs.getLong("lost_time"); // 遺失時間
		model.recoveryCount = rs.getInt("recovery_count"); // 恢復次數
		return model;
	}
	
	int characterId; // 角色 ID
	String characterName; // 角色名稱
	int itemObjectId; // 物品物件 ID
	int itemId; // 物品 ID
	String itemName; // 物品名稱
	int enchant; // 物品強化等級
	int elementalEnchant; // 元素強化等級
	int instanceBless; // 實例祝福狀態
	int instanceCustomBless; // 實例自訂祝福狀態
	long lostTime; // 遺失時間
	int recoveryCount; // 恢復次數
	
	// 無參數建構函式
	TJLostItemModel(){
	}

	// 返回角色 ID
	public int characterId(){
		return characterId;
	}

	// 返回角色名稱
	public String characterName(){
		return characterName;
	}

	// 返回物品物件 ID
	public int itemObjectId(){
		return itemObjectId;
	}

	// 返回物品 ID
	public int itemId(){
		return itemId;
	}

	// 返回物品名稱
	public String itemName(){
		return itemName;
	}

	// 返回物品強化等級
	public int enchant(){
		return enchant;
	}

	// 返回元素強化等級
	public int elementalEnchant(){
		return elementalEnchant;
	}

	// 返回實例祝福狀態
	public int instanceBless(){
		return instanceBless;
	}
	
	// 返回實例自訂祝福狀態
	public int instanceCustomBless() {
		return instanceCustomBless;
	}

	// 返回遺失時間
	public long lostTime(){
		return lostTime;
	}

	// 返回恢復次數
	public int recoveryCount(){
		return recoveryCount;
	}

	// 創建並返回 L1ItemInstance 物品實例
	L1ItemInstance itemInstance() {
		L1ItemInstance item = ItemTable.getInstance().createItem(itemId()); // 創建物品實例
		if(item == null) {
			return null;
		}
		
		// 設置物品屬性
		item.setEnchantLevel(enchant());
		item.setBless(instanceBless());
		item.set_bless_level(instanceCustomBless());
		item.setAttrEnchantLevel(elementalEnchant());
		item.setCount(1);
		item.setIdentified(true);
		return item;
	}
}
