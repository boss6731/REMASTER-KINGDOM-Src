package l1j.server.Beginner.Model;

import l1j.server.Beginner.Model.MJBeginnerCollectData.MJBeginnerCollectDataFromMonsters;
import l1j.server.Beginner.Model.MJBeginnerCollectData.MJBeginnerCollectDataFromNpcTalk;
import l1j.server.Beginner.Model.MJBeginnerQuestData.MJBeginnerObjectiveListData.MJBeginnerObjective;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Attribute.MJAttrKey;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.QuestProgress;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_FINISH_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_PROGRESS_UPDATE_NOTI;
import l1j.server.MJTemplate.ObjectEvent.MJInventoryEventFactory;
import l1j.server.MJTemplate.ObjectEvent.MJInventoryEventFactory.MJInventoryItemChangedArgs;
import l1j.server.MJTemplate.ObjectEvent.MJInventoryEventFactory.MJInventoryItemUsedArgs;
import l1j.server.MJTemplate.ObjectEvent.MJMonsterEventFactory;
import l1j.server.MJTemplate.ObjectEvent.MJMonsterEventFactory.MJMonsterKillEventArgs;
import l1j.server.MJTemplate.ObjectEvent.MJNpcEventFactory;
import l1j.server.MJTemplate.ObjectEvent.MJNpcEventFactory.MJNpcTalkEventArgs;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventArgs;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventComposite;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventListener;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventProvider;
import l1j.server.MJTemplate.ObjectEvent.MJPcEventFactory;
import l1j.server.MJTemplate.ObjectEvent.MJPcEventFactory.MJPcLevelChangedArgs;
import l1j.server.MJTemplate.ObjectEvent.MJPcEventFactory.MJPcPledgeChangedArgs;
import l1j.server.MJTemplate.ObjectEvent.MJPcEventFactory.MJPcPssStartedArgs;
import l1j.server.MJTemplate.ObjectEvent.MJPcEventFactory.MJPcQuestFinishedArgs;
import l1j.server.MJTemplate.ObjectEvent.MJPcEventFactory.MJpcBargaKeyArgs;
import l1j.server.MJTemplate.ObjectEvent.MJPcEventFactory.MJpcCPMWBQAddedKeyArgs;
import l1j.server.MJTemplate.ObjectEvent.MJPcEventFactory.MJpcCraftOpenKeyArgs;
//import l1j.server.MJTemplate.ObjectEvent.MJPcEventFactory.MJpcEinhasadKeyArgs;
import l1j.server.MJTemplate.ObjectEvent.MJPcEventFactory.MJpcHiddenDungeonKeyArgs;
import l1j.server.MJTemplate.ObjectEvent.MJPcEventFactory.MJpcIndunKeyArgs;
import l1j.server.MJTemplate.ObjectEvent.MJPcEventFactory.MJpcMagicDollKeyArgs;
import l1j.server.MJTemplate.ObjectEvent.MJPcEventFactory.MJpcMoriaKeyArgs;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Item;

// MJBeginnerEventProvider 類，負責處理新手事件通知
class MJBeginnerEventProvider {

	// 單例對象
	private static final MJBeginnerEventProvider provider = new MJBeginnerEventProvider();

	// 獲取單例對象的方法
	public static MJBeginnerEventProvider provider() {
		return provider;
	}

	// 計算增加的步驟數
	private static int calculateAddedStepCount(int questId, int addedQuantity, int requiredQuantity) {
		return MJBeginnerModelProvider.provider()
				.settingModel()
				.model()
				.modeHandler()
				.calculateAddedStepCount(questId, addedQuantity, requiredQuantity);
	}

	// 通知更新（方法重載）
	static void invokeUpdateNoti(L1PcInstance pc, MJBeginnerUserProgress progress, MJBeginnerUserProgressObjective objective, MJBeginnerObjective objectiveData) {
		invokeUpdateNoti(pc, progress.convertClientModel());
	}

	// 通知更新
	static void invokeUpdateNoti(L1PcInstance pc, QuestProgress questProgress) {
		SC_QUEST_PROGRESS_UPDATE_NOTI noti = SC_QUEST_PROGRESS_UPDATE_NOTI.newInstance();
		noti.set_quest(questProgress);
		// 發送新的任務進度更新通知
		pc.sendPackets(noti, MJEProtoMessages.SC_QUEST_PROGRESS_UPDATE_NOTI);
	}

	// 通知更新消息
	static void invokeUpdateMessageNoti(L1PcInstance pc, String updateName, int current, int total) {
		SC_NOTIFICATION_MESSAGE noti = SC_NOTIFICATION_MESSAGE.newInstance(
				String.format("%s (%d/%d)", updateName, current, total),
				MJSimpleRgb.fromRgb(230, 230, 180),
				3
		);
		pc.sendPackets(noti, MJEProtoMessages.SC_NOTIFICATION_MESSAGE);
	}

	// 通知任務完成
	static void invokeFinishedNoti(L1PcInstance pc, int questId) {
		SC_QUEST_FINISH_NOTI noti = SC_QUEST_FINISH_NOTI.newInstance();
		noti.set_id(questId);
		// 發送新的任務完成通知
		pc.sendPackets(noti, MJEProtoMessages.SC_QUEST_FINISH_NOTI);
	}

	// 私有構造函數，防止實例化
	private MJBeginnerEventProvider() {
	}


	// 處理玩家的特定事件註冊
	// 處理玩家的特定事件註冊
	void registeredEvent(L1PcInstance pc, MJBeginnerUserProgress progress, MJBeginnerUserProgressObjective objective) {
		// 從目標中獲取相應的數據
		MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
		if (objectiveData == null) {
			// 如果沒有相應的數據，則直接返回
			return;
		}
		if (objective.quantity() >= objectiveData.requiredQuantity()) {
			// 如果目標數量已達到或超過所需數量，則不進行後續處理
			return;
		}
			// 根據目標類型，執行相應的事件註冊處理
		switch (objectiveData.objectiveType()) {
			case KILL_NPC:
				// 註冊殺死NPC事件
				registeredKillNpc(pc, progress, objective);
				break;
			case COLLECT_ITEM:
				// 註冊收集物品事件
				registeredCollectItem(pc, progress, objective);
				break;
			case REACH_LEVEL:
				// 註冊達到等級事件
				registeredReachLevel(pc, progress, objective);
				break;
			case TUTORIAL_BLOOD_PLEDGE_JOIN:
				// 註冊加入血盟教學事件
				registeredBloodPledgeJoin(pc, progress, objective);
				break;
			case TUTORIAL_BLOOD_PLEDGE_CREATE:
				// 註冊創建血盟教學事件
				registeredBloodPledgeCreated(pc, progress, objective);
				break;
			case TUTORIAL_USE_ITEM:
				// 註冊使用物品教學事件
				registeredUseItem(pc, progress, objective);
				break;
			case START_PSS:
				// 註冊啟動PSS事件
				registeredPss(pc, progress, objective);
				break;
			case VIEW_DIALOGUE:
				// 註冊查看對話事件
				registeredViewDialog(pc, progress, objective);
				break;
			case TURTORIAL_OPEN_UI:
				// 註冊打開用戶界面教學事件
				registeredTuto(pc, progress, objective);
				break;
			case QUEST_REVEAL:
			case TUTORIAL_ENCHANT_MAX:
			case DESTROY_NOVICE_SIEGE_DOOR:
			case DESTROY_NOVICE_SIEGE_TOWER:
				System.out.println(String.format("無效的目標類型：%s（任務編號：%d）", objectiveData.objectiveType().name(), progress.questId()));
				break;
			default:
				System.out.println(String.format("無效的目標類型：%s（任務編號：%d）", String.valueOf(objectiveData.objectiveType()), progress.questId()));
		}
		// 更新通知
		invokeUpdateNoti(pc, progress, objective, objectiveData);
	}

	// 處理增加的任務進度
	private boolean onAdded(L1PcInstance pc, MJBeginnerUserProgress progress, MJBeginnerUserProgressObjective objective, MJBeginnerObjective objectiveData, int added) {
		// 更新當前數量
		int currentQuantity = objective.addedQuantity(added);
		// 發送更新通知
	}
	// 更新任務進度並檢查是否完成
	private boolean onCount(L1PcInstance pc, MJBeginnerUserProgress progress, MJBeginnerUserProgressObjective objective, MJBeginnerObjective objectiveData, int count) {
		// 設置當前數量
		objective.quantity(count);
		int currentQuantity = count;
		// 發送更新通知
		invokeUpdateNoti(pc, progress, objective, objectiveData);
		// 更新數據庫中的目標數據
		objective.updateDatabase(pc.getId());
		// 檢查是否達到所需數量
		if (currentQuantity >= objectiveData.requiredQuantity()) {
			// 檢查任務是否完成
			if (progress.completed()) {
			// 發送完成通知（目前註釋掉）
			// invokeFinishedNoti(pc, progress.questId());
			}
			return true;
		}
		return false;
	}

	// 註冊內部事件
	private <T extends MJObjectEventArgs> void registeredEventInternal(final L1PcInstance pc, final int questId, final MJAttrKey<MJObjectEventComposite<T>> key, final MJObjectEventListener<T> listener) {
		// 添加任務完成事件的監聽器
		pc.eventHandler().addListener(MJObjectEventProvider.provider().pcEventFactory().pcQuestFinishedKey(), new MJObjectEventListener<MJPcEventFactory.MJPcQuestFinishedArgs>() {
			@override
			public void onEvent(MJPcQuestFinishedArgs args) {
				if (args.questId == questId) {
					// 移除監聽器
					pc.eventHandler().removeListener(key, listener);
					args.remove();
				}
			}
		});
			// 為指定的事件鍵添加監聽器
		pc.eventHandler().addListener(key, listener);
	}



	// 註冊擊殺 NPC 事件
	private void registeredKillNpc(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 註冊內部事件，監聽擊殺 NPC 事件
		registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().monsterEventFactory().monsterKillKey(), new MJObjectEventListener<MJMonsterEventFactory.MJMonsterKillEventArgs>() {
			@override
			public void onEvent(MJMonsterKillEventArgs args) {
				// 獲取被擊殺的怪物實例
				L1MonsterInstance monster = args.monster;
				// 獲取目標數據
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				// 檢查擊殺的怪物是否符合任務要求
				if (monster.getNpcClassId() != objectiveData.assetId()) {
					return;
				}
				// 計算增加的步驟數
				int added = calculateAddedStepCount(progress.questId(), 1, objectiveData.requiredQuantity());
				// 更新目標進度並檢查是否完成
				if (onAdded(pc, progress, objective, objectiveData, added)) {
					// 如果任務完成，移除事件
					args.remove();
				}
				// 發送更新消息通知（目前註釋掉）
				//invokeUpdateMessageNoti(pc, monster.getNameId(), objective.quantity(), objectiveData.requiredQuantity());
			}
		});
	}

	// 註冊收集物品事件
	private void registeredCollectItem(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 為任務完成事件添加監聽器
		pc.eventHandler().addListener(MJObjectEventProvider.provider().pcEventFactory().pcQuestFinishedKey(), new MJObjectEventListener<MJPcEventFactory.MJPcQuestFinishedArgs>() {
			@override
			public void onEvent(MJPcQuestFinishedArgs args) {
				if (args.questId == progress.questId()) {
					// 獲取目標數據
					MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
					// 查找目標物品模板
					L1Item itemTemplate = ItemTable.getInstance().findDescCachedItemsAtOnce(objectiveData.assetId());
					if (itemTemplate == null) {
						// 韓文：無法找到對應的物品
						System.out.println(String.format("（無法找到對應的物品 %d(desc).", objectiveData.assetId()));
						return;
					}
					// 消耗物品
					pc.getInventory().consumeItem(itemTemplate.getItemId());
					// 移除事件
					args.remove();
				}
			}
		});
		// 檢查是否已經收集到指定數量的物品
		if (!alreadyCollectItem(pc, progress, objective)) {
			// 如果未收集足夠數量的物品，註冊內部事件
			registeredCollectItemInternal(pc, progress, objective);
		}
	}

	// 檢查是否已經收集到指定數量的物品
	private boolean alreadyCollectItem(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 獲取目標數據
		MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
		// 查找玩家背包中是否已經有對應描述的物品
		L1ItemInstance item = pc.getInventory().findItemDescId(objectiveData.assetId());
		if (item == null || item.getCount() <= 0) {
			// 如果沒有對應物品或數量不足，返回 false
			return false;
		}
		// 更新任務進度
		return onCount(pc, progress, objective, objectiveData, item.getCount());
	}

	// 註冊通過擊殺怪物收集物品的事件
	private void registeredCollectItemFromMonsters(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 註冊內部事件，監聽怪物擊殺事件
		registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().monsterEventFactory().monsterKillKey(), new MJObjectEventListener<MJMonsterEventFactory.MJMonsterKillEventArgs>() {
			@override
			public void onEvent(MJMonsterKillEventArgs args) {
				// 獲取目標數據
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				// 獲取來自擊殺怪物的收集數據
				MJBeginnerCollectDataFromMonsters collectData = MJBeginnerModelProvider.provider().collectDataModel().collectDataFromMonsters(objectiveData.assetId());
				if (collectData == null) {
					System.out.println(String.format("無法找到對應的怪物擊殺收集數據 %d(desc).", objectiveData.assetId())); // "無法找到對應的怪物擊殺收集數據 %d(desc)"
					return;
				}
				// 檢查怪物 ID 是否匹配
				if (!collectData.matches(args.monster.getNpcId())) {
					return;
				}
				// 查找目標物品模板
				L1Item itemTemplate = ItemTable.getInstance().findDescCachedItemsAtOnce(objectiveData.assetId());
				if (itemTemplate == null) {
					System.out.println(String.format("無法找到對應的物品 %d(desc(monster kill)).", objectiveData.assetId())); // "無法找到對應的物品 %d(desc(monster kill))"
					return;
				}
				// 檢查收集物品的概率
				if (!collectData.selectProbability()) {
					return;
				}
				// 獲取收集物品的數量
				int collectCount = collectData.collectItemsCount();
				if (collectCount <= 0) {
					return;
				}
				// 計算增加的步驟數
				int added = calculateAddedStepCount(progress.questId(), collectCount, objectiveData.requiredQuantity());
				// 將物品存入玩家背包
				L1ItemInstance item = pc.getInventory().storeItem(itemTemplate.getItemId(), added);
				// 發送信息給玩家
				pc.sendPackets(new S_ServerMessage(143, args.monster.getName(), String.format("%s (%,d)", item.getName(), added)));
					// 更新目標進度並檢查是否完成
				if (onCount(pc, progress, objective, objectiveData, item.getCount())) {
					// 如果任務完成，移除事件
					args.remove();
				}
				// 發送更新消息通知（目前註釋掉）

				//   invokeUpdateMessageNoti(pc, itemTemplate.getNameId(), objective.quantity(), objectiveData.requiredQuantity());
			}
		});
	}

	// 註冊達到指定等級的事件
	private void registeredReachLevel(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 獲取目標數據
		MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
		// 如果玩家當前等級未達到目標等級
		if (!onCount(pc, progress, objective, objectiveData, pc.getLevel())) {
			// 註冊內部達到等級事件
			registeredReachLevelInternal(pc, progress, objective);
		}
	}

	// 註冊創建血盟的事件
	private void registeredBloodPledgeCreated(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 註冊內部事件，監聽玩家血盟變化事件
		registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcPledgeChangedKey(), new MJObjectEventListener<MJPcEventFactory.MJPcPledgeChangedArgs>() {
			@override
			public void onEvent(MJPcPledgeChangedArgs args) {
				// 檢查當前血盟是否為空或者未變化
				if (MJString.isNullOrEmpty(args.currentPledge) || args.currentPledge == args.previousPledge) {
					return;
				}
				// 獲取玩家當前的血盟
				L1Clan clan = pc.getClan();
				// 檢查玩家是否是血盟的領袖
				if (clan == null || clan.getLeaderId() != pc.getId()) {
					return;
				}

				// 獲取目標數據
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				// 更新目標進度並檢查是否完成
				if (onAdded(pc, progress, objective, objectiveData, 1)) {
					// 如果任務完成，移除事件
					args.remove();
				}
				// 發送更新消息通知（目前註釋掉）
				//            invokeUpdateMessageNoti(pc, "創建部落", objective.quantity(), objectiveData.requiredQuantity());
			}
		});
	}

	// 註冊使用物品的事件
	private void registeredUseItem(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 註冊內部事件，監聽背包物品使用事件
		registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().inventoryEventFactory().inventoryItemUsedKey(), new MJObjectEventListener<MJInventoryEventFactory.MJInventoryItemUsedArgs>() {
			@override
			public void onEvent(MJInventoryItemUsedArgs args) {
				// 獲取使用的物品實例
				L1ItemInstance item = args.item;
				// 獲取目標數據
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				// 檢查使用的物品是否是目標物品
				if (item.getItem().getItemDescId() != objectiveData.assetId()) {
					return;
				}

				// 計算增加的步驟數
				int added = calculateAddedStepCount(progress.questId(), 1, objectiveData.requiredQuantity());
				// 更新目標進度並檢查是否完成
				if (onAdded(pc, progress, objective, objectiveData, added)) {
					// 如果任務完成，移除事件
					args.remove();
				}
				// 發送更新消息通知（目前註釋掉）
				//   invokeUpdateMessageNoti(pc, String.format("使用%s", item.getItem().getNameId()), objective.quantity(), objectiveData.requiredQuantity());
			}
		});
	}

	// 註冊新手教程的事件
	private void registeredTuto(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		//        System.out.println("퀘스트 번호 : " + objective.questId());
		//        objective.toString() // 顯示目標的詳細信息

			// 如果目標任務 ID 是 371（隱藏地下城指南）
		if (objective.questId() == 371) {
			// 註冊內部事件，監聽隱藏地下城鍵事件
			registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcHiddenDungeonKey(), new MJObjectEventListener<MJPcEventFactory.MJpcHiddenDungeonKeyArgs>() {
				@override
				public void onEvent(MJPcEventFactory.MJpcHiddenDungeonKeyArgs args) {
					// 獲取目標數據
					MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
					// 更新目標進度並檢查是否完成
					if (onAdded(pc, progress, objective, objectiveData, 1)) {
						// 如果任務完成，移除事件
						args.remove();
					}
					// 發送更新消息通知（目前註釋掉）
					//            invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());
				}
			});
		} else if (objective.questId() == 372) { // 통합제작（綜合製作）
			// 註冊內部事件，監聽製作打開事件
			registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcCraftOpenKey(), new MJObjectEventListener<MJPcEventFactory.MJpcCraftOpenKeyArgs>() {
				@override
				public void onEvent(MJPcEventFactory.MJpcCraftOpenKeyArgs args) {
					// 獲取目標數據
					MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
					// 更新目標進度並檢查是否完成
					if (onAdded(pc, progress, objective, objectiveData, 1)) {
						// 如果任務完成，移除事件
						args.remove();
					}
					// 發送更新消息通知（目前註釋掉）
					//            invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());
				}
			});
		} else if (objective.questId() == 373) { // 인던안내（地下城指南）
// 註冊內部事件，監聽地下城鍵事件
/*registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcIndunKey(), new MJObjectEventListener<MJPcEventFactory.MJpcIndunKeyArgs>() {
@override
public void onEvent(MJPcEventFactory.MJpcIndunKeyArgs args) {
MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
[02:23]
if (onAdded(pc, progress, objective, objectiveData, 1)) {
args.remove();
}
// 發送更新消息通知（目前註釋掉）
// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());
}
});/
} else if (objective.questId() == 374) { // 아인하사드 포인트（阿因哈薩德點數）
/registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcEinhasadKey(), new MJObjectEventListener<MJPcEventFactory.MJpcEinhasadKeyArgs>() {
@override
public void onEvent(MJPcEventFactory.MJpcEinhasadKeyArgs args) {
MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
if (onAdded(pc, progress, objective, objectiveData, 1)) {
args.remove();
}
// 發送更新消息通知（目前註釋掉）
// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());
}
});*/
		} else if (objective.questId() == 377) { // 몬스터 도감 등록（怪物圖鑑登錄）
			// 註冊內部事件，監聽怪物圖鑑登錄事件
			registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcCPMWBQAddedKey(), new MJObjectEventListener<MJPcEventFactory.MJpcCPMWBQAddedKeyArgs>() {
				@override
				public void onEvent(MJPcEventFactory.MJpcCPMWBQAddedKeyArgs args) {
					// 獲取當前怪物圖鑑的大小
					int currentSize = pc.attribute().getNotExistsNew(L1PcInstance.pcbookquestinfo).get().size();
					if (currentSize == 0) {
						return;
					}
						// 獲取目標數據
					MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
					// 更新目標進度並檢查是否完成
					if (onAdded(pc, progress, objective, objectiveData, 1)) {
						// 如果任務完成，移除事件
						args.remove();
					}
					// 發送更新消息通知（目前註釋掉）
					// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());
				}
			});
		} else if (objective.questId() == 379) { // 잠재력 강화（潛力強化）
			// 註冊內部事件，監聽魔法娃娃事件
			registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcMagicDollKey(), new MJObjectEventListener<MJPcEventFactory.MJpcMagicDollKeyArgs>() {
				@override
				public void onEvent(MJPcEventFactory.MJpcMagicDollKeyArgs args) {
					// 獲取目標數據

					MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
					// 更新目標進度並檢查是否完成
					if (onAdded(pc, progress, objective, objectiveData, 1)) {
						// 如果任務完成，移除事件
						args.remove();
					}
					// 發送更新消息通知（目前註釋掉）
					// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());
				}
			});
		} else if (objective.questId() == 380) { // 경험치 성장（經驗值成長）
			// 註冊內部事件，監聽莫里亞事件
			registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcMoriaKey(), new MJObjectEventListener<MJPcEventFactory.MJpcMoriaKeyArgs>() {
				@override
				public void onEvent(MJPcEventFactory.MJpcMoriaKeyArgs args) {
					// 獲取目標數據
					MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
					// 更新目標進度並檢查是否完成
					if (onAdded(pc, progress, objective, objectiveData, 1)) {
						// 如果任務完成，移除事件
						args.remove();
					}
					// 發送更新消息通知（目前註釋掉）
					// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());
				}
			});
		} else if (objective.questId() == 381) { // 용맹의 메달 사용처（勇猛勳章的使用地點）
			// 註冊內部事件，監聽勇猛勳章事件
			registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcBargaKey(), new MJObjectEventListener<MJPcEventFactory.MJpcBargaKeyArgs>() {
				@override
				public void onEvent(MJPcEventFactory.MJpcBargaKeyArgs args) {
					// 獲取目標數據
					MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
					// 更新目標進度並檢查是否完成
					if (onAdded(pc, progress, objective, objectiveData, 1)) {
						// 如果任務完成，移除事件
						args.remove();
					}
					// 發送更新消息通知（目前註釋掉）
					// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());
				}
			});
		}
	}

	// 註冊PSS（可能是某種活動或系統）的事件監聽器
	private void registeredPss(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcPssStartedKey(), new MJObjectEventListener<MJPcEventFactory.MJPcPssStartedArgs>() {
			@override
			public void onEvent(MJPcPssStartedArgs args) {
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				int added = calculateAddedStepCount(progress.questId(), 1, objectiveData.requiredQuantity());
				if (onAdded(pc, progress, objective, objectiveData, added)) {
					args.remove();
				}
					// 發送更新消息通知（目前註釋掉）
					// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());
			}
		});
	}

	// 註冊查看對話框的事件監聽器
	private void registeredViewDialog(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@override
			public void run() {
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				onCount(pc, progress, objective, objectiveData, objectiveData.requiredQuantity());
			}
		}, 3000L); // 延遲時間為 3000 毫秒（即 3 秒）
	}
}
