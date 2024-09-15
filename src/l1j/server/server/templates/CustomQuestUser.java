package l1j.server.server.templates;

import l1j.server.server.templates.eCustomQuestType;

public class CustomQuestUser {
	private int successCount;
	private int questState; // 1-進行中，2-完成，3-無法進行。
	private int questId;
	private eCustomQuestType questType;

	public CustomQuestUser(int id, eCustomQuestType type) {
		questId = id;
		successCount = 0;
		questState = 1;
		questType = type;
	}
	
	public int getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	public void addSuccessCount(int i) {
		this.successCount += i;
	}
	public int getQuestId() {
		return questId;
	}
	public void setQuestId(int questId) {
		this.questId = questId;
	}
	public int getQuestState() {
		return questState;
	}
	public void setQuestState(int questState) {
		this.questState = questState;
	}

	public eCustomQuestType getQuestType() {
		return questType;
	}

	public void setQuestType(eCustomQuestType questType) {
		this.questType = questType;
	}
}
