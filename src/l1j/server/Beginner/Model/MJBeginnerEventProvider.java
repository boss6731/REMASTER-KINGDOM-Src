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

class MJBeginnerEventProvider {
	private static final MJBeginnerEventProvider provider = new MJBeginnerEventProvider(); // 單例模式實例

	public static MJBeginnerEventProvider provider() { // 獲取單例實例的方法
		return provider;
	}

	private static int calculateAddedStepCount(int questId, int addedQuantity, int requiredQuantity) { // 計算新增步數的方法
		return MJBeginnerModelProvider.provider()
				.settingModel()
				.model()
				.modeHandler()
				.calculateAddedStepCount(questId, addedQuantity, requiredQuantity);
	}
}

	static void invokeUpdateNoti(L1PcInstance pc, MJBeginnerUserProgress progress, MJBeginnerUserProgressObjective objective, MJBeginnerObjective objectiveData) {
		invokeUpdateNoti(pc, progress.convertClientModel());
	}

	static void invokeUpdateNoti(L1PcInstance pc, QuestProgress questProgress) {
		SC_QUEST_PROGRESS_UPDATE_NOTI noti = SC_QUEST_PROGRESS_UPDATE_NOTI.newInstance();
		noti.set_quest(questProgress);
		// 發送任務進度更新通知
		pc.sendPackets(noti, MJEProtoMessages.SC_QUEST_PROGRESS_UPDATE_NOTI);
	}

	static void invokeUpdateMessageNoti(L1PcInstance pc, String updateName, int current, int total) {
		SC_NOTIFICATION_MESSAGE noti = SC_NOTIFICATION_MESSAGE.newInstance(
				String.format("%s (%d/%d)", updateName, current, total),
				MJSimpleRgb.fromRgb(230, 230, 180),
				3
		);
		// 發送通知消息
		pc.sendPackets(noti, MJEProtoMessages.SC_NOTIFICATION_MESSAGE);
	}

static void invokeFinishedNoti(L1PcInstance pc, int questId) {
		SC_QUEST_FINISH_NOTI noti = SC_QUEST_FINISH_NOTI.newInstance();
		noti.set_id(questId);
		// 發送任務完成通知
		pc.sendPackets(noti, MJEProtoMessages.SC_QUEST_FINISH_NOTI);
		}

// 私有構造方法，避免實例化
private MJBeginnerEventProvider() {
		}

	void registeredEvent(L1PcInstance pc, MJBeginnerUserProgress progress, MJBeginnerUserProgressObjective objective) {
		MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
		if (objectiveData == null) {
			return; // 如果目標數據為空，則返回
		}
		if (objective.quantity() >= objectiveData.requiredQuantity()) {
			return; // 如果已達到目標所需數量，則返回
		}
		switch (objectiveData.objectiveType()) {
			case KILL_NPC:
				registeredKillNpc(pc, progress, objective); // 註冊殺死 NPC 事件的監聽器
				break;
			case COLLECT_ITEM:
				registeredCollectItem(pc, progress, objective); // 註冊收集物品事件的監聽器
				break;
			case REACH_LEVEL:
				registeredReachLevel(pc, progress, objective); // 註冊達到等級事件的監聽器
				break;
			case TUTORIAL_BLOOD_PLEDGE_JOIN:
				registeredBloodPledgeJoin(pc, progress, objective); // 註冊加入血盟事件的監聽器
				break;
			case TUTORIAL_BLOOD_PLEDGE_CREATE:
				registeredBloodPledgeCreated(pc, progress, objective); // 註冊創建血盟事件的監聽器
				break;
			case TUTORIAL_USE_ITEM:
				registeredUseItem(pc, progress, objective); // 註冊使用物品事件的監聽器
				break;
			case START_PSS:
				registeredPss(pc, progress, objective); // 註冊開始 PSS 事件的監聽器
				break;
			case VIEW_DIALOGUE:
				registeredViewDialog(pc, progress, objective); // 註冊查看對話事件的監聽器
				break;
			case TURTORIAL_OPEN_UI:
				registeredTuto(pc, progress, objective); // 註冊打開 UI 事件的監聽器
				break;
			case QUEST_REVEAL:
			case TUTORIAL_ENCHANT_MAX:
			case DESTROY_NOVICE_SIEGE_DOOR:
			case DESTROY_NOVICE_SIEGE_TOWER:
				System.out.println(String.format("無效的目標類型 : %s(任務ID : %d)", objectiveData.objectiveType().name(), progress.questId()));
				break;
			default:
				System.out.println(String.format("無效的目標類型 : %s(任務ID : %d)", String.valueOf(objectiveData.objectiveType()), progress.questId()));
		}
		invokeUpdateNoti(pc, progress, objective, objectiveData); // 調用更新通知
	}

	private boolean onAdded(L1PcInstance pc, MJBeginnerUserProgress progress, MJBeginnerUserProgressObjective objective, MJBeginnerObjective objectiveData, int added) {
		int currentQuantity = objective.addedQuantity(added); // 增加當前數量
		invokeUpdateNoti(pc, progress, objective, objectiveData); // 調用更新通知
		objective.updateDatabase(pc.getId()); // 更新資料庫
		if (currentQuantity >= objectiveData.requiredQuantity()) { // 檢查是否達到所需數量
			if (progress.completed()) { // 檢查任務是否完成
				// invokeFinishedNoti(pc, progress.questId()); // 任務完成通知（註釋掉）
			}
			return true;
		}
		return false;
	}

	private boolean onCount(L1PcInstance pc, MJBeginnerUserProgress progress, MJBeginnerUserProgressObjective objective, MJBeginnerObjective objectiveData, int count) {
		objective.quantity(count); // 設定當前數量
		int currentQuantity = count; // 更新當前數量
		invokeUpdateNoti(pc, progress, objective, objectiveData); // 調用更新通知
		objective.updateDatabase(pc.getId()); // 更新資料庫
		if (currentQuantity >= objectiveData.requiredQuantity()) { // 檢查是否達到所需數量
			if (progress.completed()) { // 檢查任務是否完成
				// invokeFinishedNoti(pc, progress.questId()); // 任務完成通知（註釋掉）
			}
			return true;
		}
		return false;
	}

	private <T extends MJObjectEventArgs> void registeredEventInternal(final L1PcInstance pc, final int questId, final MJAttrKey<MJObjectEventComposite<T>> key, final MJObjectEventListener<T> listener) {
		// 註冊任務完成事件的監聽器
		pc.eventHandler().addListener(
				MJObjectEventProvider.provider().pcEventFactory().pcQuestFinishedKey(),
				new MJObjectEventListener<MJPcEventFactory.MJPcQuestFinishedArgs>() {
					@override
					public void onEvent(MJPcQuestFinishedArgs args) {
						if (args.questId == questId) { // 檢查是否為目標任務
							pc.eventHandler().removeListener(key, listener); // 移除特定監聽器
							args.remove(); // 清理事件參數
						}
					}
				}
		);
		// 註冊傳入的監聽器
		pc.eventHandler().addListener(key, listener);
	}

	private void registeredKillNpc(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 註冊 NPC 殺死事件的監聽器
		registeredEventInternal(
				pc,
				progress.questId(),
				MJObjectEventProvider.provider().monsterEventFactory().monsterKillKey(),
				new MJObjectEventListener<MJMonsterEventFactory.MJMonsterKillEventArgs>() {
					@override
					public void onEvent(MJMonsterKillEventArgs args) {
						L1MonsterInstance monster = args.monster;
						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();

						// 檢查被殺死的 NPC 是否為目標 NPC
						if (monster.getNpcClassId() != objectiveData.assetId()) {
							return;
						}

						// 計算增加的步數
						int added = calculateAddedStepCount(progress.questId(), 1, objectiveData.requiredQuantity());

						// 更新進度並檢查是否達到目標
						if (onAdded(pc, progress, objective, objectiveData, added)) {
							args.remove(); // 清理事件參數
						}

						// 更新消息通知 (註釋掉)
						// invokeUpdateMessageNoti(pc, monster.getNameId(), objective.quantity(), objectiveData.requiredQuantity());
					}
				}
		);
	}

	private void registeredCollectItem(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 添加任務完成監聽器
		pc.eventHandler().addListener(
				MJObjectEventProvider.provider().pcEventFactory().pcQuestFinishedKey(),
				new MJObjectEventListener<MJPcEventFactory.MJPcQuestFinishedArgs>() {
					@override
					public void onEvent(MJPcQuestFinishedArgs args) {
						// 檢查當前任務是否是目標任務
						if (args.questId == progress.questId()) {
							MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
							// 查找目標物品
							L1Item itemTemplate = ItemTable.getInstance().findDescCachedItemsAtOnce(objectiveData.assetId());
							if (itemTemplate == null) {
								// 未找到物品時輸出錯誤信息
								System.out.println(String.format("無法找到對應於 ID %d 的物品描述。", objectiveData.assetId()));
								return;
							}
							// 消耗目標物品
							pc.getInventory().consumeItem(itemTemplate.getItemId());
							// 移除事件參數
							args.remove();
						}
					}
				}
		);

		// 檢查是否已經收集了物品，如果未收集則註冊收集事件監聽器
		if (!alreadyCollectItem(pc, progress, objective)) {
			registeredCollectItemInternal(pc, progress, objective);
		}
	}

	private boolean alreadyCollectItem(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 獲取目標數據
		MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
		// 查找玩家背包中對應的物品
		L1ItemInstance item = pc.getInventory().findItemDescId(objectiveData.assetId());
		if (item == null || item.getCount() <= 0) {
			// 如果未找到物品或數量不足，返回 false
			return false;
		}
		// 更新物品數量並返回
		return onCount(pc, progress, objective, objectiveData, item.getCount());
	}

	// 檢查玩家是否已經收集到所需的物品
	private boolean alreadyCollectItem(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 獲取目標數據
		MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
		// 從玩家的背包中查找目標物品
		L1ItemInstance item = pc.getInventory().findItemDescId(objectiveData.assetId());
		// 如果未找到物品或物品數量不足，返回 false
		if (item == null || item.getCount() <= 0) {
			return false;
		}
		// 調用 onCount 方法來更新物品數量並返回結果
		return onCount(pc, progress, objective, objectiveData, item.getCount());
	}

	// 註冊收集物品事件的內部方法
	private void registeredCollectItemInternal(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 獲取目標數據
		MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
		// 獲取收集數據模型
		MJBeginnerCollectModel collectModel = MJBeginnerModelProvider.provider().collectDataModel();
		// 檢查目標物品是否來自 NPC 對話，並註冊相應的收集事件
		if (collectModel.collectDataFromNpcTalk(objectiveData.assetId()) != null) {
			registeredCollectItemFromNpcTalk(pc, progress, objective);
			// 檢查目標物品是否來自怪物掉落，並註冊相應的收集事件
		} else if (collectModel.collectDataFromMonsters(objectiveData.assetId()) != null) {
			registeredCollectItemFromMonsters(pc, progress, objective);
			// 如果以上兩者皆否，註冊其他方式的收集事件
		} else {
			registeredCollectItemFromOthers(pc, progress, objective);
		}
	}

	// 註冊從 NPC 對話中收集物品的事件
	private void registeredCollectItemFromNpcTalk(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().npcEventFactory().npcTalkKey(), new MJObjectEventListener<MJNpcEventFactory.MJNpcTalkEventArgs>() {
			@override
			public void onEvent(MJNpcTalkEventArgs args) {
				// 獲取目標數據
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				// 從收集數據模型中獲取 NPC 對話收集數據
				MJBeginnerCollectDataFromNpcTalk collectData = MJBeginnerModelProvider.provider().collectDataModel().collectDataFromNpcTalk(objectiveData.assetId());
				// 如果未找到對應的收集數據，打印錯誤信息並返回
				if (collectData == null) {
					System.out.println(String.format("找不到對應於項目 %d(desc) 的 collectData(npc)。", objectiveData.assetId()));
					return;
				}
					// 如果 NPC ID 不匹配，返回
				if (!collectData.matches(args.npc.getNpcId())) {
					return;
				}

				// 從緩存中查找一次性描述的物品模板

				L1Item itemTemplate = ItemTable.getInstance().findDescCachedItemsAtOnce(objectiveData.assetId());
				if (itemTemplate == null) {

					// 如果找不到對應的物品模板，打印錯誤信息並返回

					System.out.println(String.format("找不到對應於項目 %d(desc(npc)) 的物品。", objectiveData.assetId()));

					return;

				}

				// 計算新增步數

				int added = calculateAddedStepCount(progress.questId(), collectData.suppliesCount(), objectiveData.requiredQuantity());

				// 將物品存儲到玩家的背包中

				L1ItemInstance item = pc.getInventory().storeItem(itemTemplate.getItemId(), added);

				// 發送消息給玩家，通知其從 NPC 獲得了物品

				pc.sendPackets(new S_ServerMessage(143, args.npc.getName(), String.format("%s (%,d)", item.getName(), added)));

				// 如果物品數量達到要求，移除相應的事件

				if (onCount(pc, progress, objective, objectiveData, item.getCount())) {

					args.remove();

				}

				// 更新通知消息（此行被註釋掉了）

				// invokeUpdateMessageNoti(pc, itemTemplate.getNameId(), objective.quantity(), objectiveData.requiredQuantity());

			}

		});
	}

	// 註冊從怪物收集物品的事件
	// 註冊從怪物收集物品的事件
	private void registeredCollectItemFromMonsters(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 註冊內部事件，當怪物被殺死時觸發
		registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().monsterEventFactory().monsterKillKey(), new MJObjectEventListener<MJMonsterEventFactory.MJMonsterKillEventArgs>() {
			@override
			public void onEvent(MJMonsterKillEventArgs args) {
				// 獲取目標數據
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				// 獲取怪物收集數據
				MJBeginnerCollectDataFromMonsters collectData = MJBeginnerModelProvider.provider().collectDataModel().collectDataFromMonsters(objectiveData.assetId());
				if (collectData == null) {
					// 如果找不到收集數據，打印錯誤信息並返回
					System.out.println(String.format("找不到對應物品 %d(desc) 的 collectData(monster kill)。", objectiveData.assetId()));
					return;
				}
					// 如果怪物的 NPC ID 不匹配，返回
				if (!collectData.matches(args.monster.getNpcId())) {
					return;
				}
					// 從緩存中查找一次性描述的物品模板
				L1Item itemTemplate = ItemTable.getInstance().findDescCachedItemsAtOnce(objectiveData.assetId());
				if (itemTemplate == null) {
					// 如果找不到物品模板，打印錯誤信息並返回
					System.out.println(String.format("找不到對應物品 %d(desc(monster kill)) 的物品。", objectiveData.assetId()));
					return;
				}
					// 如果不符合收集概率，返回
				if (!collectData.selectProbability()) {
					return;
				}
					// 獲取收集物品的數量
				int collectCount = collectData.collectItemsCount();
				if (collectCount <= 0) {
					// 如果收集的物品數量小於等於 0，則返回
					return;
				}
				// 計算新增步數
				int added = calculateAddedStepCount(progress.questId(), collectCount, objectiveData.requiredQuantity());
				// 將物品存儲到玩家的背包中
				L1ItemInstance item = pc.getInventory().storeItem(itemTemplate.getItemId(), added);
				// 發送消息給玩家，通知其從怪物獲得了物品
				pc.sendPackets(new S_ServerMessage(143, args.monster.getName(), String.format("%s (%,d)", item.getName(), added)));
				// 如果物品數量達到要求，移除相應的事件
				if (onCount(pc, progress, objective, objectiveData, item.getCount())) {
					args.remove();
				}
				// 更新通知消息（此行被註釋掉了）
				// invokeUpdateMessageNoti(pc, itemTemplate.getNameId(), objective.quantity(), objectiveData.requiredQuantity());
			}
		});
	}

	// 註冊從其他來源收集物品的事件
	private void registeredCollectItemFromOthers(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 註冊內部事件，當背包中的物品發生變化時觸發
		registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().inventoryEventFactory().inventoryItemChangedKey(), new MJObjectEventListener<MJInventoryEventFactory.MJInventoryItemChangedArgs>() {
			@override
			public void onEvent(MJInventoryItemChangedArgs args) {
				// 獲取變化的物品
				L1ItemInstance item = args.item;
				// 獲取目標數據
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				// 如果物品描述 ID 不匹配，返回
				if (item.getItem().getItemDescId() != objectiveData.assetId()) {
					return;
				}
				// 計算新增步數
				int added = calculateAddedStepCount(progress.questId(), item.getCount(), objectiveData.requiredQuantity());
				// 如果物品數量達到要求，移除相應的事件
				if (onCount(pc, progress, objective, objectiveData, added)) {
					args.remove();
				}
				// 更新通知消息（此行被註釋掉了）
				// invokeUpdateMessageNoti(pc, item.getItem().getNameId(), objective.quantity(), objectiveData.requiredQuantity());
			}
		});
	}

	// 註冊玩家達到目標級別的事件
	private void registeredReachLevel(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 獲取目標數據
		MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
		// 如果玩家當前級別未達到目標要求，註冊相應的內部事件
		if (!onCount(pc, progress, objective, objectiveData, pc.getLevel())) {
			registeredReachLevelInternal(pc, progress, objective);
		}
	}

	// 註冊當玩家達到特定等級的事件
	private void registeredReachLevelInternal(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 註冊內部事件，當玩家等級發生變化時觸發
		registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcLevelChangedKey(), new MJObjectEventListener<MJPcEventFactory.MJPcLevelChangedArgs>() {
			@override
			public void onEvent(MJPcLevelChangedArgs args) {
				// 獲取目標數據
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				// 如果玩家等級達到要求，移除相應的事件
				if (onCount(pc, progress, objective, objectiveData, pc.getLevel())) {
					args.remove();
				}
				// 更新通知消息（此行被註釋掉了）
				// invokeUpdateMessageNoti(pc, "達到等級", objective.quantity(), objectiveData.requiredQuantity());
			}
		});
	}

	// 註冊當玩家加入血盟的事件
	private void registeredBloodPledgeJoin(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 註冊內部事件，當玩家的血盟狀態發生變化時觸發
		registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcPledgeChangedKey(), new MJObjectEventListener<MJPcEventFactory.MJPcPledgeChangedArgs>() {
			@override
			public void onEvent(MJPcPledgeChangedArgs args) {
				// 如果當前血盟為空或者沒有變化，返回
				if (MJString.isNullOrEmpty(args.currentPledge) || args.currentPledge == args.previousPledge) {
					return;
				}
				// 獲取目標數據
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				// 如果加入血盟的數量達到要求，移除相應的事件
				if (onAdded(pc, progress, objective, objectiveData, 1)) {
					args.remove();
				}
				// 更新通知消息（此行被註釋掉了）
				// invokeUpdateMessageNoti(pc, "加入血盟", objective.quantity(), objectiveData.requiredQuantity());
			}
		});
	}

	// 註冊當玩家創建血盟的事件
	private void registeredBloodPledgeCreated(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 註冊內部事件，當玩家的血盟狀態發生變化時觸發
		registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcPledgeChangedKey(), new MJObjectEventListener<MJPcEventFactory.MJPcPledgeChangedArgs>() {
			@override
			public void onEvent(MJPcPledgeChangedArgs args) {
				// 如果當前血盟為空或者沒有變化，返回
				if (MJString.isNullOrEmpty(args.currentPledge) || args.currentPledge == args.previousPledge) {
					return;
				}
				// 獲取玩家的血盟信息
				L1Clan clan = pc.getClan();
				// 如果玩家無血盟或不是血盟領導者，返回
				if (clan == null || clan.getLeaderId() != pc.getId()) {
					return;
				}

				// 獲取目標數據
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				// 如果創建血盟的目標達成，移除相應的事件
				if (onAdded(pc, progress, objective, objectiveData, 1)) {
					args.remove();
				}
				// 更新通知消息（此行被註釋掉了）
				// invokeUpdateMessageNoti(pc, "創建血盟", objective.quantity(), objectiveData.requiredQuantity());
			}
		});
	}

	// 註冊當玩家使用物品的事件
	private void registeredUseItem(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 註冊內部事件，當玩家使用物品時觸發
		registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().inventoryEventFactory().inventoryItemUsedKey(), new MJObjectEventListener<MJInventoryEventFactory.MJInventoryItemUsedArgs>() {
			@override
			public void onEvent(MJInventoryItemUsedArgs args) {
				// 獲取使用的物品
				L1ItemInstance item = args.item;
				// 獲取目標數據
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				// 如果物品描述 ID 不匹配，返回
				if (item.getItem().getItemDescId() != objectiveData.assetId()) {
					return;
				}

				// 計算新增步數
				int added = calculateAddedStepCount(progress.questId(), 1, objectiveData.requiredQuantity());
				// 如果使用物品的目標達成，移除相應的事件
				if (onAdded(pc, progress, objective, objectiveData, added)) {
					args.remove();
				}
				// 更新通知消息（此行被註釋掉了）
				// invokeUpdateMessageNoti(pc, String.format("%s 使用", item.getItem().getNameId()), objective.quantity(), objectiveData.requiredQuantity());
			}
		});
	}

	// 註冊指導任務相關事件
	private void registeredTuto(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		//        System.out.println("任務編號 : " + objective.questId());

		//        objective.toString()
		if (objective.questId() == 371) { // 숨사 안내0x0322 (隱藏地牢指引)
			// 註冊內部事件，當玩家進入隱藏地牢時觸發
			registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcHiddenDungeonKey(), new MJObjectEventListener<MJPcEventFactory.MJpcHiddenDungeonKeyArgs>() {
				@override
				public void onEvent(MJpcHiddenDungeonKeyArgs args) {
					// 獲取目標數據
					MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
					// 如果目標達成，移除相應的事件
					if (onAdded(pc, progress, objective, objectiveData, 1)) {
						args.remove();
					}
					// 更新通知消息（此行被註釋掉了）
					// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());
				}
			});
		} else if (objective.questId() == 372) { // 통합제작 (綜合製作)
			// 註冊內部事件，當玩家開啟製作介面時觸發
			registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcCraftOpenKey(), new MJObjectEventListener<MJPcEventFactory.MJpcCraftOpenKeyArgs>() {
				@override
				public void onEvent(MJpcCraftOpenKeyArgs args) {
					// 獲取目標數據
					MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
						// 如果目標達成，移除相應的事件
					if (onAdded(pc, progress, objective, objectiveData, 1)) {
						args.remove();
					}
					// 更新通知消息（此行被註釋掉了）
					// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());
				}
			});
		} else if (objective.questId() == 373 || objective.questId() == 374) { // 인던안내 (地下城指引) // 아인하사드 포인트 (伊恩哈薩德點數) 03FD
			// 使用線程池延遲執行任務，模擬玩家完成任務
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@override
				public void run() {
					// 獲取目標數據
					MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
					// 更新玩家進度
					onCount(pc, progress, objective, objectiveData, objectiveData.requiredQuantity());
				}
			}, 3000L); // 3秒後執行
		}
	}



		// 註冊指導任務相關事件

	private void registeredTuto(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {

			//        System.out.println("任務編號 : " + objective.questId());

			//        objective.toString()

		if (objective.questId() == 371) { // 숨사 안내0x0322 (隱藏地牢指引)

			// 註冊內部事件，當玩家進入隱藏地牢時觸發



			registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcHiddenDungeonKey(), new MJObjectEventListener<MJPcEventFactory.MJpcHiddenDungeonKeyArgs>() {

				@override

				public void onEvent(MJpcHiddenDungeonKeyArgs args) {

					// 獲取目標數據

					MJBeginnerObjective objectiveData = objective.mappedObjectiveData();

					// 如果目標達成，移除相應的事件

					if (onAdded(pc, progress, objective, objectiveData, 1)) {

						args.remove();

					}

					// 更新通知消息（此行被註釋掉了）

					// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());

				}

			});

		} else if (objective.questId() == 372) { // 통합제작 (綜合製作)

			// 註冊內部事件，當玩家開啟製作介面時觸發

			registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcCraftOpenKey(), new MJObjectEventListener<MJPcEventFactory.MJpcCraftOpenKeyArgs>() {

				@override

				public void onEvent(MJpcCraftOpenKeyArgs args) {

					// 獲取目標數據

					MJBeginnerObjective objectiveData = objective.mappedObjectiveData();

					// 如果目標達成，移除相應的事件

					if (onAdded(pc, progress, objective, objectiveData, 1)) {

						args.remove();

					}

					// 更新通知消息（此行被註釋掉了）

					// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());

				}

			});

		} else if (objective.questId() == 373 || objective.questId() == 374) { // 인던안내 (地下城指引) // 아인하사드 포인트 (伊恩哈薩德點數) 03FD

			// 使用線程池延遲執行任務，模擬玩家完成任務

			GeneralThreadPool.getInstance().schedule(new Runnable() {

				@override

				public void run() {

					// 獲取目標數據

					MJBeginnerObjective objectiveData = objective.mappedObjectiveData();

					// 更新玩家進度

					onCount(pc, progress, objective, objectiveData, objectiveData.requiredQuantity());
				}
			}
			}, 3000L); // 3秒後執行
						/* 註冊內部事件，當玩家進入地下城時觸發
						registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcIndunKey(), new MJObjectEventListener<MJPcEventFactory.MJpcIndunKeyArgs>() {
						@override
						public void onEvent(MJpcIndunKeyArgs args) {
						// 獲取目標數據
						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
						// 如果目標達成，移除相應的事件
						if (onAdded(pc, progress, objective, objectiveData, 1)) {
						[04:01]
						args.remove();
						}
						// 更新通知消息（此行被註釋掉了）
						// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());
						}
						});/
						} /else if (objective.questId() == 374){ // 아인하사드 포인트 (伊恩哈薩德點數) 03FD
						// 註冊內部事件，當玩家獲得伊恩哈薩德點數時觸發
						registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcEinhasadKey(), new MJObjectEventListener<MJPcEventFactory.MJpcEinhasadKeyArgs>() {
						@override
						public void onEvent(MJpcEinhasadKeyArgs args) {
						// 獲取目標數據
						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
						// 如果目標達成，移除相應的事件
						if (onAdded(pc, progress, objective, objectiveData, 1)) {
						args.remove();
						}
						// 更新通知消息（此行被註釋掉了）
						// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());
						}
						});
						} */
		else if (objective.questId() == 377) { // 몬스터 도감 등록 (怪物圖鑑登錄)

				// 註冊內部事件，當玩家登錄怪物圖鑑時觸發

			registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcCPMWBQAddedKey(), new MJObjectEventListener<MJPcEventFactory.MJpcCPMWBQAddedKeyArgs>() {

					@override

					public void onEvent(MJpcCPMWBQAddedKeyArgs args) {

						// 獲取當前圖鑑信息的大小

						int currentSize = pc.attribute().getNotExistsNew(L1PcInstance.pcbookquestinfo).get().size();

						if (currentSize == 0) {

							return;

						}

						// 獲取目標數據

						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();

						// 如果目標達成，移除相應的事件

						if (onAdded(pc, progress, objective, objectiveData, 1)) {

							args.remove();

						}

						// 更新通知消息（此行被註釋掉了）

						// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());

					}
			});
		} else if (objective.questId() == 379) { // 잠재력 강화 (潛能強化)

				// 註冊內部事件，當玩家強化潛能時觸發

			registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcMagicDollKey(), new MJObjectEventListener<MJPcEventFactory.MJpcMagicDollKeyArgs>() {



					@override

					public void onEvent(MJpcMagicDollKeyArgs args) {

						// 獲取目標數據

						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();

						// 如果目標達成，移除相應的事件



						if (onAdded(pc, progress, objective, objectiveData, 1)) {

							args.remove();

						}

						// 更新通知消息（此行被註釋掉了）

						// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());

					}

			});
		} else if (objective.questId() == 380) { // 경험치 성장 0038 (經驗值成長 0038)

				// 註冊內部事件，當玩家經驗值成長時觸發

			registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcMoriaKey(), new MJObjectEventListener<MJPcEventFactory.MJpcMoriaKeyArgs>() {

					@override

					public void onEvent(MJpcMoriaKeyArgs args) {

						// 獲取目標數據

						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();

						// 如果目標達成，移除相應的事件

						if (onAdded(pc, progress, objective, objectiveData, 1)) {

							args.remove();

						}

						// 更新通知消息（此行被註釋掉了）

						// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());

					}

			});
		} else if (objective.questId() == 381) { // 용맹의 메달 사용처 (勇猛勳章使用處)

				// 註冊內部事件，當玩家使用勇猛勳章時觸發

			registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcBargaKey(), new MJObjectEventListener<MJPcEventFactory.MJpcBargaKeyArgs>() {

					@override

					public void onEvent(MJpcBargaKeyArgs args) {

						// 獲取目標數據

						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();

						// 如果目標達成，移除相應的事件

						if (onAdded(pc, progress, objective, objectiveData, 1)) {

							args.remove();

						}

						// 更新通知消息（此行被註釋掉了）

						// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());

					}

			});

		}

	}


	// 註冊PSS相關事件
	private void registeredPss(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 註冊內部事件，當PSS啟動時觸發
		registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().pcEventFactory().pcPssStartedKey(), new MJObjectEventListener<MJPcEventFactory.MJPcPssStartedArgs>() {
			@override
			public void onEvent(MJPcPssStartedArgs args) {
				// 獲取任務目標數據
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				// 計算新增的步數
				int added = calculateAddedStepCount(progress.questId(), 1, objectiveData.requiredQuantity());
				// 如果達到目標，觸發onAdded方法並移除事件
				if (onAdded(pc, progress, objective, objectiveData, added)) {
					args.remove();
				}
				// 註釋掉的行：更新通知消息，用於通知玩家已達到目標
				// invokeUpdateMessageNoti(pc, "自動狩獵", objective.quantity(), objectiveData.requiredQuantity());
			}
		});
	}




	// 註冊查看對話框相關事件
	private void registeredViewDialog(final L1PcInstance pc, final MJBeginnerUserProgress progress, final MJBeginnerUserProgressObjective objective) {
		// 使用線程池延遲3秒執行任務，模擬玩家查看對話框
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@override
			public void run() {
				// 獲取任務目標數據
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				// 更新玩家進度
				onCount(pc, progress, objective, objectiveData, objectiveData.requiredQuantity());
			}
		}, 3000L); // 3秒後執行
	}
}


