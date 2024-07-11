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
	private static final MJBeginnerEventProvider provider = new MJBeginnerEventProvider();

	public static MJBeginnerEventProvider provider() {
		return provider;
	}

	private static int calculateAddedStepCount(int questId, int addedQuantity, int requiredQuantity) {
		return MJBeginnerModelProvider.provider()
				.settingModel()
				.model()
				.modeHandler()
				.calculateAddedStepCount(questId, addedQuantity, requiredQuantity);
	}

	static void invokeUpdateNoti(L1PcInstance pc, MJBeginnerUserProgress progress,
			MJBeginnerUserProgressObjective objective, MJBeginnerObjective objectiveData) {
		invokeUpdateNoti(pc, progress.convertClientModel());
	}

	static void invokeUpdateNoti(L1PcInstance pc, QuestProgress questProgress) {
		SC_QUEST_PROGRESS_UPDATE_NOTI noti = SC_QUEST_PROGRESS_UPDATE_NOTI.newInstance();
		noti.set_quest(questProgress);
		// 新任務註釋
		pc.sendPackets(noti, MJEProtoMessages.SC_QUEST_PROGRESS_UPDATE_NOTI);
	}

	static void invokeUpdateMessageNoti(L1PcInstance pc, String updateName, int current, int total) {
		SC_NOTIFICATION_MESSAGE noti = SC_NOTIFICATION_MESSAGE.newInstance(
				String.format("%s (%d/%d)", updateName, current, total), MJSimpleRgb.fromRgb(230, 230, 180), 3);
		pc.sendPackets(noti, MJEProtoMessages.SC_NOTIFICATION_MESSAGE);
	}

	static void invokeFinishedNoti(L1PcInstance pc, int questId) {
		SC_QUEST_FINISH_NOTI noti = SC_QUEST_FINISH_NOTI.newInstance();
		noti.set_id(questId);
		// 新任務註釋
		pc.sendPackets(noti, MJEProtoMessages.SC_QUEST_FINISH_NOTI);
	}

	private MJBeginnerEventProvider() {
	}

	void registeredEvent(L1PcInstance pc, MJBeginnerUserProgress progress, MJBeginnerUserProgressObjective objective) {
		MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
		if (objectiveData == null) {
			return;
		}
		if (objective.quantity() >= objectiveData.requiredQuantity()) {
			return;
		}
		switch (objectiveData.objectiveType()) {
			case KILL_NPC:
				registeredKillNpc(pc, progress, objective);
				break;
			case COLLECT_ITEM:
				registeredCollectItem(pc, progress, objective);
				break;
			case REACH_LEVEL:
				registeredReachLevel(pc, progress, objective);
				break;
			case TUTORIAL_BLOOD_PLEDGE_JOIN:
				registeredBloodPledgeJoin(pc, progress, objective);
				break;
			case TUTORIAL_BLOOD_PLEDGE_CREATE:
				registeredBloodPledgeCreated(pc, progress, objective);
				break;
			case TUTORIAL_USE_ITEM:
				registeredUseItem(pc, progress, objective);
				break;
			case START_PSS:
				registeredPss(pc, progress, objective);
				break;
			case VIEW_DIALOGUE:
				registeredViewDialog(pc, progress, objective);
				break;
			case TURTORIAL_OPEN_UI:
				registeredTuto(pc, progress, objective);
				break;
			case QUEST_REVEAL:
			case TUTORIAL_ENCHANT_MAX:
			case DESTROY_NOVICE_SIEGE_DOOR:
			case DESTROY_NOVICE_SIEGE_TOWER:
				System.out.println(
						String.format("無效的目標類型：%s(任務ID：%d)", objectiveData.objectiveType().name(), progress.uestId()));
				break;
			default:
				System.out.println(
						String.format("無效的目標類型：%s(任務ID：%d)", String.valueOf(objectiveData.objectiveType()),
								progress.questId()));
		}
		invokeUpdateNoti(pc, progress, objective, objectiveData);
	}

	private boolean onAdded(L1PcInstance pc, MJBeginnerUserProgress progress, MJBeginnerUserProgressObjective objective,
			MJBeginnerObjective objectiveData, int added) {
		int currentQuantity = objective.addedQuantity(added);
		invokeUpdateNoti(pc, progress, objective, objectiveData);
		objective.updateDatabase(pc.getId());
		if (currentQuantity >= objectiveData.requiredQuantity()) {
			if (progress.completed()) {
				// invokeFinishedNoti(pc, progress.questId());
			}
			return true;
		}
		return false;
	}

	private boolean onCount(L1PcInstance pc, MJBeginnerUserProgress progress, MJBeginnerUserProgressObjective objective,
			MJBeginnerObjective objectiveData, int count) {
		objective.quantity(count);
		int currentQuantity = count;
		invokeUpdateNoti(pc, progress, objective, objectiveData);
		objective.updateDatabase(pc.getId());
		if (currentQuantity >= objectiveData.requiredQuantity()) {
			if (progress.completed()) {
				// invokeFinishedNoti(pc, progress.questId());
			}
			return true;
		}
		return false;
	}

	private <T extends MJObjectEventArgs> void registeredEventInternal(final L1PcInstance pc, final int questId,
			final MJAttrKey<MJObjectEventComposite<T>> key, final MJObjectEventListener<T> listener) {
		pc.eventHandler().addListener(MJObjectEventProvider.provider().pcEventFactory().pcQuestFinishedKey(),
				new MJObjectEventListener<MJPcQuestFinishedArgs>() {
					@Override
					public void onEvent(MJPcQuestFinishedArgs args) {
						if (args.questId == questId) {
							pc.eventHandler().removeListener(key, listener);
							args.remove();
						}
					}
				});
		pc.eventHandler().addListener(key, listener);
	}

	private void registeredKillNpc(final L1PcInstance pc, final MJBeginnerUserProgress progress,
			final MJBeginnerUserProgressObjective objective) {
		registeredEventInternal(pc, progress.questId(),
				MJObjectEventProvider.provider().monsterEventFactory().monsterKillKey(),
				new MJObjectEventListener<MJMonsterKillEventArgs>() {
					@Override
					public void onEvent(MJMonsterKillEventArgs args) {
						L1MonsterInstance monster = args.monster;
						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
						if (monster.getNpcClassId() != objectiveData.assetId()) {
							return;
						}
						int added = calculateAddedStepCount(progress.questId(), 1, objectiveData.requiredQuantity());
						if (onAdded(pc, progress, objective, objectiveData, added)) {
							args.remove();
						}
						// invokeUpdateMessageNoti(pc, monster.getNameId(), objective.quantity(),
						// objectiveData.requiredQuantity());
					}
				});
	}

	private void registeredCollectItem(final L1PcInstance pc, final MJBeginnerUserProgress progress,
			final MJBeginnerUserProgressObjective objective) {
		pc.eventHandler().addListener(MJObjectEventProvider.provider().pcEventFactory().pcQuestFinishedKey(),
				new MJObjectEventListener<MJPcQuestFinishedArgs>() {
					@Override
					public void onEvent(MJPcQuestFinishedArgs args) {
						if (args.questId == progress.questId()) {
							MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
							L1Item itemTemplate = ItemTable.getInstance()
									.findDescCachedItemsAtOnce(objectiveData.assetId());
							if (itemTemplate == null) {
								System.out.println(
										String.format("找不到對應於道具 %d(desc) 的道具。", objectiveData.assetId()));
								return;
							}
							pc.getInventory().consumeItem(itemTemplate.getItemId());
							args.remove();
						}
					}
				});
		if (!alreadyCollectItem(pc, progress, objective)) {
			registeredCollectItemInternal(pc, progress, objective);
		}
	}

	private boolean alreadyCollectItem(final L1PcInstance pc, final MJBeginnerUserProgress progress,
			final MJBeginnerUserProgressObjective objective) {
		MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
		L1ItemInstance item = pc.getInventory().findItemDescId(objectiveData.assetId());
		if (item == null || item.getCount() <= 0) {
			return false;
		}
		return onCount(pc, progress, objective, objectiveData, item.getCount());
	}

	private void registeredCollectItemInternal(final L1PcInstance pc, final MJBeginnerUserProgress progress,
			final MJBeginnerUserProgressObjective objective) {
		MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
		MJBeginnerCollectModel collectModel = MJBeginnerModelProvider.provider().collectDataModel();
		if (collectModel.collectDataFromNpcTalk(objectiveData.assetId()) != null) {
			registeredCollectItemFromNpcTalk(pc, progress, objective);
		} else if (collectModel.collectDataFromMonsters(objectiveData.assetId()) != null) {
			registeredCollectItemFromMonsters(pc, progress, objective);
		} else {
			registeredCollectItemFromOthers(pc, progress, objective);
		}
	}

	private void registeredCollectItemFromNpcTalk(final L1PcInstance pc, final MJBeginnerUserProgress progress,
			final MJBeginnerUserProgressObjective objective) {
		registeredEventInternal(pc, progress.questId(), MJObjectEventProvider.provider().npcEventFactory().npcTalkKey(),
				new MJObjectEventListener<MJNpcTalkEventArgs>() {
					@Override
					public void onEvent(MJNpcTalkEventArgs args) {
						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
						MJBeginnerCollectDataFromNpcTalk collectData = MJBeginnerModelProvider.provider()
								.collectDataModel().collectDataFromNpcTalk(objectiveData.assetId());
						if (collectData == null) {
							Object[] objecti = new Object[0];
							System.out.println(
									String.format("找不到對應於道具 %d(desc) 的 collectData(npc)。", objecti,
											eData.assetId());
							return;
						}
						if (!collectData.matches(args.npc.getNpcId())) {
							return;
						}
						L1Item itemTemplate = ItemTable.getInstance()
								.findDescCachedItemsAtOnce(objectiveData.assetId());
						if (itemTemplate == null) {
							System.out.println(
									String.format("找不到對應於道具 %d(desc(npc)) 的道具。", objectiveData.
											ssetId()));
							return;
						}
						int added = calculateAddedStepCount(progress.questId(), collectData.suppliesCount(),
								objectiveData.requiredQuantity());
						L1ItemInstance item = pc.getInventory().storeItem(itemTemplate.getItemId(), added);
						pc.sendPackets(new S_ServerMessage(143, args.npc.getName(),
								String.format("%s (%,d)", item.getName(), added)));
						if (onCount(pc, progress, objective, objectiveData, item.getCount())) {
							args.remove();
						}
						// invokeUpdateMessageNoti(pc, itemTemplate.getNameId(), objective.quantity(),
						// objectiveData.requiredQuantity());
					}
				});
	}

	private void registeredCollectItemFromMonsters(final L1PcInstance pc, final MJBeginnerUserProgress progress,
			final MJBeginnerUserProgressObjective objective) {
		registeredEventInternal(pc, progress.questId(),
				MJObjectEventProvider.provider().monsterEventFactory().monsterKillKey(),
				new MJObjectEventListener<MJMonsterKillEventArgs>() {
					@Override
					public void onEvent(MJMonsterKillEventArgs args) {
						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
						MJBeginnerCollectDataFromMonsters collectData = MJBeginnerModelProvider.provider()
								.collectDataModel().collectDataFromMonsters(objectiveData.assetId());
						if (collectData == null) {
							System.out
									.println(String.format("找不到對應於道具 %d(desc) 的 collectData(monster kill)。",
											objectiveData.assetId()));
							return;
						}
						if (!collectData.matches(args.monster.getNpcId())) {
							return;
						}
						L1Item itemTemplate = ItemTable.getInstance()
								.findDescCachedItemsAtOnce(objectiveData.assetId());
						if (itemTemplate == null) {
							System.out.println(String.format("找不到對應於道具 %d(desc(monster kill)) 的道具。",
									objectiveData.assetId()));
							return;
						}
						if (!collectData.selectProbability()) {
							return;
						}
						int collectCount = collectData.collectItemsCount();
						if (collectCount <= 0) {
							return;
						}
						int added = calculateAddedStepCount(progress.questId(), collectCount,
								objectiveData.requiredQuantity());
						L1ItemInstance item = pc.getInventory().storeItem(itemTemplate.getItemId(), added);
						pc.sendPackets(new S_ServerMessage(143, args.monster.getName(),
								String.format("%s (%,d)", item.getName(), added)));
						if (onCount(pc, progress, objective, objectiveData, item.getCount())) {
							args.remove();
						}
						// invokeUpdateMessageNoti(pc, itemTemplate.getNameId(), objective.quantity(),
						// objectiveData.requiredQuantity());
					}
				});
	}

	private void registeredCollectItemFromOthers(final L1PcInstance pc, final MJBeginnerUserProgress progress,
			final MJBeginnerUserProgressObjective objective) {
		registeredEventInternal(pc, progress.questId(),
				MJObjectEventProvider.provider().inventoryEventFactory().inventoryItemChangedKey(),
				new MJObjectEventListener<MJInventoryItemChangedArgs>() {
					@Override
					public void onEvent(MJInventoryItemChangedArgs args) {
						L1ItemInstance item = args.item;
						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
						if (item.getItem().getItemDescId() != objectiveData.assetId()) {
							return;
						}
						int added = calculateAddedStepCount(progress.questId(), item.getCount(),
								objectiveData.requiredQuantity());
						if (onCount(pc, progress, objective, objectiveData, added)) {
							args.remove();
						}
						// invokeUpdateMessageNoti(pc, item.getItem().getNameId(), objective.quantity(),
						// objectiveData.requiredQuantity());
					}
				});
	}

	private void registeredReachLevel(final L1PcInstance pc, final MJBeginnerUserProgress progress,
			final MJBeginnerUserProgressObjective objective) {
		MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
		if (!onCount(pc, progress, objective, objectiveData, pc.getLevel())) {
			registeredReachLevelInternal(pc, progress, objective);
		}
	}

	private void registeredReachLevelInternal(final L1PcInstance pc, final MJBeginnerUserProgress progress,
			final MJBeginnerUserProgressObjective objective) {
		registeredEventInternal(pc, progress.questId(),
				MJObjectEventProvider.provider().pcEventFactory().pcLevelChangedKey(),
				new MJObjectEventListener<MJPcLevelChangedArgs>() {
					@Override
					public void onEvent(MJPcLevelChangedArgs args) {
						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
						if (onCount(pc, progress, objective, objectiveData, pc.getLevel())) {
							args.remove();
						}
						// invokeUpdateMessageNoti(pc, "레벨 달성", objective.quantity(),
						// objectiveData.requiredQuantity());
					}
				});
	}

	private void registeredBloodPledgeJoin(final L1PcInstance pc, final MJBeginnerUserProgress progress,
			final MJBeginnerUserProgressObjective objective) {
		registeredEventInternal(pc, progress.questId(),
				MJObjectEventProvider.provider().pcEventFactory().pcPledgeChangedKey(),
				new MJObjectEventListener<MJPcPledgeChangedArgs>() {
					@Override
					public void onEvent(MJPcPledgeChangedArgs args) {
						if (MJString.isNullOrEmpty(args.currentPledge) || args.currentPledge == args.previousPledge) {
							return;
						}
						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
						if (onAdded(pc, progress, objective, objectiveData, 1)) {
							args.remove();
						}
						// invokeUpdateMessageNoti(pc, "혈맹 가입", objective.quantity(),
						// objectiveData.requiredQuantity());
					}
				});
	}

	private void registeredBloodPledgeCreated(final L1PcInstance pc, final MJBeginnerUserProgress progress,
			final MJBeginnerUserProgressObjective objective) {
		registeredEventInternal(pc, progress.questId(),
				MJObjectEventProvider.provider().pcEventFactory().pcPledgeChangedKey(),
				new MJObjectEventListener<MJPcPledgeChangedArgs>() {
					@Override
					public void onEvent(MJPcPledgeChangedArgs args) {
						if (MJString.isNullOrEmpty(args.currentPledge) || args.currentPledge == args.previousPledge) {
							return;
						}
						L1Clan clan = pc.getClan();
						if (clan == null || clan.getLeaderId() != pc.getId()) {
							return;
						}

						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
						if (onAdded(pc, progress, objective, objectiveData, 1)) {
							args.remove();
						}
						// invokeUpdateMessageNoti(pc, "혈맹 생성", objective.quantity(),
						// objectiveData.requiredQuantity());
					}
				});
	}

	private void registeredUseItem(final L1PcInstance pc, final MJBeginnerUserProgress progress,
			final MJBeginnerUserProgressObjective objective) {
		registeredEventInternal(pc, progress.questId(),
				MJObjectEventProvider.provider().inventoryEventFactory().inventoryItemUsedKey(),
				new MJObjectEventListener<MJInventoryItemUsedArgs>() {
					@Override
					public void onEvent(MJInventoryItemUsedArgs args) {
						L1ItemInstance item = args.item;
						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
						if (item.getItem().getItemDescId() != objectiveData.assetId()) {
							return;
						}

						int added = calculateAddedStepCount(progress.questId(), 1, objectiveData.requiredQuantity());
						if (onAdded(pc, progress, objective, objectiveData, added)) {
							args.remove();
						}
						// invokeUpdateMessageNoti(pc, String.format("%s 사용",
						// item.getItem().getNameId()), objective.quantity(),
						// objectiveData.requiredQuantity());
					}
				});
	}

	private void registeredTuto(final L1PcInstance pc, final MJBeginnerUserProgress progress,
			final MJBeginnerUserProgressObjective objective) {
		// System.out.println("퀘스트 번호 : "+objective.questId());
		// objective.toString()
		if (objective.questId() == 371) {// 蘇姆薩資訊0x0322
			registeredEventInternal(pc, progress.questId(),
					MJObjectEventProvider.provider().pcEventFactory().pcHiddenDungeonKey(),
					new MJObjectEventListener<MJpcHiddenDungeonKeyArgs>() {
						@Override
						public void onEvent(MJpcHiddenDungeonKeyArgs args) {
							MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
							if (onAdded(pc, progress, objective, objectiveData, 1)) {
								args.remove();
							}
							// invokeUpdateMessageNoti(pc, "자동사냥", objective.quantity(),
							// objectiveData.requiredQuantity());
						}
					});

		} else if (objective.questId() == 372) {// 一體化生產
			registeredEventInternal(pc, progress.questId(),
					MJObjectEventProvider.provider().pcEventFactory().pcCraftOpenKey(),
					new MJObjectEventListener<MJpcCraftOpenKeyArgs>() {
						@Override
						public void onEvent(MJpcCraftOpenKeyArgs args) {
							MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
							if (onAdded(pc, progress, objective, objectiveData, 1)) {
								args.remove();
							}
							// invokeUpdateMessageNoti(pc, "자동사냥", objective.quantity(),
							// objectiveData.requiredQuantity());
						}
					});
		} else if (objective.questId() == 373 || objective.questId() == 374) {// 地下城指南 //Einhasad Point 03FD
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
					onCount(pc, progress, objective, objectiveData, objectiveData.requiredQuantity());
				}
			}, 3000L);

			/*
			 * registeredEventInternal(pc, progress.questId(),
			 * MJObjectEventProvider.provider().pcEventFactory().pcIndunKey(), new
			 * MJObjectEventListener<MJPcEventFactory.MJpcIndunKeyArgs>(){
			 * 
			 * @Override
			 * public void onEvent(MJpcIndunKeyArgs args) {
			 * MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
			 * if(onAdded(pc, progress, objective, objectiveData, 1)){
			 * args.remove();
			 * }
			 * // invokeUpdateMessageNoti(pc, "자동사냥", objective.quantity(),
			 * objectiveData.requiredQuantity());
			 * }
			 * });
			 */
		} /*
			 * else if (objective.questId() == 374){//아인하사드 포인트 03FD
			 * registeredEventInternal(pc, progress.questId(),
			 * MJObjectEventProvider.provider().pcEventFactory().pcEinhasadKey(), new
			 * MJObjectEventListener<MJPcEventFactory.MJpcEinhasadKeyArgs>(){
			 * 
			 * @Override
			 * public void onEvent(MJpcEinhasadKeyArgs args) {
			 * MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
			 * if(onAdded(pc, progress, objective, objectiveData, 1)){
			 * args.remove();
			 * }
			 * // invokeUpdateMessageNoti(pc, "자동사냥", objective.quantity(),
			 * objectiveData.requiredQuantity());
			 * }
			 * });
			 * }
			 */else if (objective.questId() == 377) {// 몬스터 圖鑑 등록
			registeredEventInternal(pc, progress.questId(),
					MJObjectEventProvider.provider().pcEventFactory().pcCPMWBQAddedKey(),
					new MJObjectEventListener<MJpcCPMWBQAddedKeyArgs>() {
						@Override
						public void onEvent(MJpcCPMWBQAddedKeyArgs args) {
							int currentSize = pc.attribute().getNotExistsNew(L1PcInstance.pcbookquestinfo).get().size();
							if (currentSize == 0) {
								return;
							}
							MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
							if (onAdded(pc, progress, objective, objectiveData, 1)) {
								args.remove();
							}
							// invokeUpdateMessageNoti(pc, "자동사냥", objective.quantity(),
							// objectiveData.requiredQuantity());
						}
					});
		} else if (objective.questId() == 379) { // 잠재력 강화
			registeredEventInternal(pc, progress.questId(),
					MJObjectEventProvider.provider().pcEventFactory().pcMagicDollKey(),
					new MJObjectEventListener<MJpcMagicDollKeyArgs>() {
						@Override
						public void onEvent(MJpcMagicDollKeyArgs args) {
							MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
							if (onAdded(pc, progress, objective, objectiveData, 1)) {
								args.remove();
							}
							// invokeUpdateMessageNoti(pc, "자동사냥", objective.quantity(),
							// objectiveData.requiredQuantity());
						}
					});
		} else if (objective.questId() == 380) {// 경험치 성장 0038
			registeredEventInternal(pc, progress.questId(),
					MJObjectEventProvider.provider().pcEventFactory().pcMoriaKey(),
					new MJObjectEventListener<MJpcMoriaKeyArgs>() {
						@Override
						public void onEvent(MJpcMoriaKeyArgs args) {
							MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
							if (onAdded(pc, progress, objective, objectiveData, 1)) {
								args.remove();
							}
							// invokeUpdateMessageNoti(pc, "자동사냥", objective.quantity(),
							// objectiveData.requiredQuantity());
						}
					});

		} else if (objective.questId() == 381) {// 용맹의 메달 사용처
			registeredEventInternal(pc, progress.questId(),
					MJObjectEventProvider.provider().pcEventFactory().pcBargaKey(),
					new MJObjectEventListener<MJpcBargaKeyArgs>() {
						@Override
						public void onEvent(MJpcBargaKeyArgs args) {
							MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
							if (onAdded(pc, progress, objective, objectiveData, 1)) {
								args.remove();
							}
							// invokeUpdateMessageNoti(pc, "자동사냥", objective.quantity(),
							// objectiveData.requiredQuantity());
						}
					});
		}

	}

	private void registeredPss(final L1PcInstance pc, final MJBeginnerUserProgress progress,
			final MJBeginnerUserProgressObjective objective) {
		registeredEventInternal(pc, progress.questId(),
				MJObjectEventProvider.provider().pcEventFactory().pcPssStartedKey(),
				new MJObjectEventListener<MJPcPssStartedArgs>() {
					@Override
					public void onEvent(MJPcPssStartedArgs args) {
						MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
						int added = calculateAddedStepCount(progress.questId(), 1, objectiveData.requiredQuantity());
						if (onAdded(pc, progress, objective, objectiveData, added)) {
							args.remove();
						}
						// invokeUpdateMessageNoti(pc, "자동사냥", objective.quantity(),
						// objectiveData.requiredQuantity());
					}
				});
	}

	private void registeredViewDialog(final L1PcInstance pc, final MJBeginnerUserProgress progress,
			final MJBeginnerUserProgressObjective objective) {
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				onCount(pc, progress, objective, objectiveData, objectiveData.requiredQuantity());
			}
		}, 3000L);
	}
}
