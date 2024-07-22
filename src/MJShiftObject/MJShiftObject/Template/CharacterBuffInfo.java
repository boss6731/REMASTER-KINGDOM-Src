package MJShiftObject.Template;


/**
 * 表示角色的增益狀態信息。
 */
public class CharacterBuffInfo {
	// 技能ID
	public int skill_id;

	// 時間（以秒為單位）
	public int time_sec;

	// 變身ID
	public int poly_id;

	/**
	 * 帶參數的構造函數，用於初始化所有字段。
	 *
	 * @param skill_id 技能ID
	 * @param time_sec 持續時間（以秒為單位）
	 * @param poly_id  變身ID
	 */
	public CharacterBuffInfo(int skill_id, int time_sec, int poly_id) {
		this.skill_id = skill_id;
		this.time_sec = time_sec;
		this.poly_id = poly_id;
	}

	/**
	 * 無參數的默認構造函數。
	 */
	public CharacterBuffInfo() {
	}
}


