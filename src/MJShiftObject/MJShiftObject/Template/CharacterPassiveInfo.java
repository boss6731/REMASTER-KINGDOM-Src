package MJShiftObject.Template;

/**
 * 表示角色的被動技能信息。
 */
public class CharacterPassiveInfo {
	// 被動技能ID
	public int passive_id;

	// 被動技能名稱
	public String passive_name;

	/**
	 * 帶參數的構造函數，用於初始化所有字段。
	 *
	 * @param passive_id   被動技能ID
	 * @param passive_name 被動技能名稱
	 */
	public CharacterPassiveInfo(int passive_id, String passive_name) {
		this.passive_id = passive_id;
		this.passive_name = passive_name;
	}

	/**
	 * 無參數的默認構造函數。
	 */
	public CharacterPassiveInfo() {
	}
}


