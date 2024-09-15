package l1j.server.server.server.templates;

import java.util.ArrayList;
import java.util.List;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.CommonUtil;

public class CustomQuest {
	private int questId;
	private String questName;
	private List<Integer> questApplyMapIds = new ArrayList<Integer>();
	private l1j.server.server.templates.eCustomQuestType questType;
	private l1j.server.server.templates.eCustomQuestPerformType questPerformType;
	private int collectItemId;
	private int collectItemDropProb;
	private int successCount;
	private int rewardItemId;
	private int rewardItemCount;
	private int minLevel;
	private int maxLevel;

	public List<Integer> getQuestApplyMapIds() {
		return questApplyMapIds;
	}

	public l1j.server.server.templates.eCustomQuestType getQuestType() {
		return questType;
	}

	public l1j.server.server.templates.eCustomQuestPerformType getQuestPerformType() {
		return questPerformType;
	}

	public int getCollectItemId() {
		return collectItemId;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public int getRewardItemId() {
		return rewardItemId;
	}

	public int getRewardItemCount() {
		return rewardItemCount;
	}

	public void setQuestApplyMapIds(List<Integer> questApplyMapIds) {
		this.questApplyMapIds = questApplyMapIds;
	}

	public void setQuestType(l1j.server.server.templates.eCustomQuestType questType) {
		this.questType = questType;
	}

	public void setQuestPerformType(l1j.server.server.templates.eCustomQuestPerformType questPerformType) {
		this.questPerformType = questPerformType;
	}

	public void setCollectItemId(int collectItemId) {
		this.collectItemId = collectItemId;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public void setRewardItemId(int rewardItemId) {
		this.rewardItemId = rewardItemId;
	}

	public void setRewardItemCount(int rewardItemCount) {
		this.rewardItemCount = rewardItemCount;
	}

	public int getQuestId() {
		return questId;
	}

	public void setQuestId(int questId) {
		this.questId = questId;
	}

	public int getCollectItemDropProb() {
		return collectItemDropProb;
	}

	public void setCollectItemDropProb(int collectItemDropProb) {
		this.collectItemDropProb = collectItemDropProb;
	}

	public String getQuestName() {
		return questName;
	}

	public void setQuestName(String questName) {
		this.questName = questName;
	}

	public void result(L1PcInstance pc) {
		CustomQuestUser cqu = pc.getCustomQuestUser(this.questId);
		if (cqu != null) {
			if (cqu.getQuestState() == 1) {
				int success_count = 0;
				if (this.getQuestPerformType() == eCustomQuestPerformType.KILL_NPC) {
					if (this.getSuccessCount() > cqu.getSuccessCount()) {
						cqu.addSuccessCount(1);
						success_count = cqu.getSuccessCount();
					}
				} else if (this.getQuestPerformType() == eCustomQuestPerformType.COLLECT_ITEM) {
					if (this.getCollectItemId() != 0) {
						L1ItemInstance collectItem = pc.getInventory().findItemId(this.getCollectItemId());
						if(collectItem == null) {
							if (CommonUtil.random(1000000) < this.getCollectItemDropProb()) {
								pc.getInventory().storeItem(this.getCollectItemId(), 1);
								success_count = 1;
							}
						} else {
							if(this.getSuccessCount() < collectItem.getCount()) {
								if (CommonUtil.random(1000000) < this.getCollectItemDropProb()) {
									pc.getInventory().storeItem(this.getCollectItemId(), 1);
									success_count = (collectItem.getCount() + 1);
								}
							}
						}
					}
				}

				pc.sendPackets(String.format("\fW[任務狀況] (%s) (%d) (%d)", this.getQuestName(), success_count, this.getSuccessCount()));
				if(success_count >= this.getSuccessCount()) {
					pc.sendPackets(String.format("\fW[任務完成通知] 已完成 %s 的任務。", this.getQuestName()));
					cqu.setQuestState(2);
					pc.send_effect(18420, false);
				}
			}
		}
	}

	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
}
