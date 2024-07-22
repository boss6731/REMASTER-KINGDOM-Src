package MJShiftObject.Template;

/**
 * 表示角色的任務信息。
 */
public class CharacterQuestInfo {
	// 任務ID
	public int quest_id;

	// 任務進度
	public int quest_step;

	/**
	 * 帶參數的構造函數，用於初始化所有字段。
	 *
	 * @param quest_id 任務ID
	 * @param quest_step 任務進度
	 */
	public CharacterQuestInfo(int quest_id, int quest_step) {
		this.quest_id = quest_id;
		this.quest_step = quest_step;
	}

	/**
	 * 無參數的默認構造函數。
	 */
	public CharacterQuestInfo() {
	}
}


