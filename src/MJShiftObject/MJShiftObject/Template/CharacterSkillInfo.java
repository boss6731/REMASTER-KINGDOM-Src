package MJShiftObject.Template;

/**
 * 表示角色的技能信息。
 */
public class CharacterSkillInfo {
	// 技能ID
	public int skill_id;

	// 技能名稱
	public String skill_name;

	// 是否為主動技能
	public boolean is_active;

	// 主動技能剩餘時間
	public int active_time_left;

	/**
	 * 帶參數的構造函數，用於初始化所有字段。
	 *
	 * @param skill_id 技能ID
	 * @param skill_name 技能名稱
	 * @param is_active 是否為主動技能
	 * @param active_time_left 主動技能剩餘時間
	 */
	public CharacterSkillInfo(int skill_id, String skill_name, boolean is_active, int active_time_left) {
		this.skill_id = skill_id;
		this.skill_name = skill_name;
		this.is_active = is_active;
		this.active_time_left = active_time_left;
	}

	/**
	 * 無參數的默認構造函數。
	 */
	public CharacterSkillInfo() {
	}
}


