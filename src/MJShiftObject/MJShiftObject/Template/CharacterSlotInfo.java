package MJShiftObject.Template;

/**
 * 表示角色的裝備槽信息。
 */
public class CharacterSlotInfo {
	// 裝備槽中的物品ID
	public int source_item_id;

	// 裝備槽編號
	public int slot_number;

	/**
	 * 帶參數的構造函數，用於初始化所有字段。
	 *
	 * @param source_item_id 裝備槽中的物品ID
	 * @param slot_number 裝備槽編號
	 */
	public CharacterSlotInfo(int source_item_id, int slot_number) {
		this.source_item_id = source_item_id;
		this.slot_number = slot_number;
	}

	/**
	 * 無參數的默認構造函數。
	 */
	public CharacterSlotInfo() {
	}
}


