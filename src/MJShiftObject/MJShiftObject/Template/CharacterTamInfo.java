package MJShiftObject.Template;

import java.sql.Timestamp;

/**
 * 表示角色的 TAM 信息。
 */
public class CharacterTamInfo {
	// 帳號ID
	public int account_id;

	// 角色ID
	public int character_id;

	// 角色名稱
	public String character_name;

	// 技能ID
	public int skill_id;

	// 到期時間
	public Timestamp expiration_time;

	// 保留字段
	public int reserve;

	/**
	 * 帶參數的構造函數，用於初始化所有字段。
	 *
	 * @param account_id 帳號ID
	 * @param character_id 角色ID
	 * @param character_name 角色名稱
	 * @param skill_id 技能ID
	 * @param expiration_time 到期時間
	 * @param reserve 保留字段
	 */
	public CharacterTamInfo(int account_id, int character_id, String character_name, int skill_id, Timestamp expiration_time, int reserve) {
		this.account_id = account_id;
		this.character_id = character_id;
		this.character_name = character_name;
		this.skill_id = skill_id;
		this.expiration_time = expiration_time;
		this.reserve = reserve;
	}

	/**
	 * 無參數的默認構造函數。
	 */
	public CharacterTamInfo() {
	}
}


