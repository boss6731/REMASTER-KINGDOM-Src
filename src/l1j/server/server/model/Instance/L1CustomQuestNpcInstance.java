package l1j.server.server.model.Instance;

import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.datatables.ServerCustomQuestTable;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.templates.CustomQuest;
import l1j.server.server.templates.CustomQuestUser;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.eCustomQuestPerformType;
import l1j.server.server.templates.eCustomQuestType;

public class L1CustomQuestNpcInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;

	public L1CustomQuestNpcInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		if (player == null || this == null)
			return;
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());

		if(talking != null)
			player.sendPackets(new S_NPCTalkReturn(getId(), talking.getNormalAction()));
		
	}

	@Override
	public void onFinalAction(L1PcInstance pc, String action) {
		if (this == null || pc == null)
			return;

		if (action.startsWith("reward")) {
			// 보상받기
			String quest_id = action.replace("reward_quest_", "");

			int questid = Integer.valueOf(quest_id);
		
			CustomQuest cq = ServerCustomQuestTable.getInstance().getCustomQuest(questid);
			if (cq != null) {
				CustomQuestUser cqu = pc.getCustomQuestUser(questid);
				if (cqu != null) {
					if (cqu.getQuestState() == 3) {
						pc.sendPackets("\fW[任務通知] 已經完成的任務。");
					} else {
						if (cq.getQuestPerformType() == eCustomQuestPerformType.KILL_NPC) {
							if (cq.getSuccessCount() == cqu.getSuccessCount() && cqu.getQuestState() == 2) {
								// 發放獎勵
								L1ItemInstance tem = pc.getInventory().storeItem(cq.getRewardItemId(), cq.getRewardItemCount());
								pc.sendPackets(String.format("\fW[任務獎勵] 獲得了 %s 的獎勵 %s(%s)。", cq.getQuestName(), tem.getName(), cq.getRewardItemCount()));
								pc.sendPackets(new S_NPCTalkReturn(getId(), ""));
								pc.send_effect(19255, false);
								cqu.setQuestState(3);
							} else {
								pc.sendPackets("\fW[任務通知] 任務完成條件未滿足。");
							}
						} else if (cq.getQuestPerformType() == eCustomQuestPerformType.COLLECT_ITEM) {
								// 檢查並刪除背包中的物品後發放獎勵
							if (pc.getInventory().consumeItem(cq.getCollectItemId(), cq.getSuccessCount())) {
								L1ItemInstance tem = pc.getInventory().storeItem(cq.getRewardItemId(), cq.getRewardItemCount());
								pc.sendPackets(String.format("\fW[任務獎勵] 獲得了 %s 的獎勵 %s(%s)。", cq.getQuestName(), tem.getName(), cq.getRewardItemCount()));
								pc.sendPackets(new S_NPCTalkReturn(getId(), ""));
								pc.send_effect(19255, false);
								cqu.setQuestState(3);
							} else {
								pc.sendPackets("\fW[任務通知] 完成任務所需的物品不足。");
							}
						}

						if (cq.getQuestType() == eCustomQuestType.REPEAT) {
							pc.removeCustomQuest(questid);
						}
						// 發放獎勵後根據任務類型
						// 重複任務刪除玩家任務，刪除資料庫記錄
						// 每日任務將狀態設為3，每天早上9點重置
						// 每週任務將狀態設為3，每週三早上9點重置

					}
				} else {
					pc.sendPackets("\fW[任務通知] 找不到進行中的任務。");
					pc.sendPackets(new S_NPCTalkReturn(getId(), ""));
				}
			} else {
				pc.sendPackets("\f3[任務通知] 尚未準備好的任務。");
				pc.sendPackets(new S_NPCTalkReturn(getId(), ""));
			}

		} else {
			// 퀘스트 받기
			String quest_id = action.replace("quest_", "");

			int questid = Integer.valueOf(quest_id);

			CustomQuest cq = ServerCustomQuestTable.getInstance().getCustomQuest(questid);
			if (cq != null) {
				CustomQuestUser cqu = pc.getCustomQuestUser(questid);
				if (cqu != null) {
					if (cqu.getQuestState() == 3) {
						pc.sendPackets("\fW[任務通知] 已經完成的任務。");
						pc.sendPackets(new S_NPCTalkReturn(getId(), ""));
					} else {
						pc.sendPackets("\fW[任務通知] 任務已在進行中。");
						pc.sendPackets(new S_NPCTalkReturn(getId(), ""));
					}
				} else {
					pc.setCustomQuestNpcObjId(getId());
					pc.setCustomQuestId(cq.getQuestId());
					pc.sendPackets(new S_Message_YN(6008, cq.getQuestName() + " 任務接受嗎？"));
				}
			} else {
				pc.sendPackets("\f3[任務通知] 尚未準備好的任務。");
				pc.sendPackets(new S_NPCTalkReturn(getId(), ""));
			}
		}

	}
}