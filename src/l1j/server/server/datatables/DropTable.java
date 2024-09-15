package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.BonusDropSystem.BonusDropSystemInfo;
import l1j.server.BonusDropSystem.BonusDropSystemLoader;
import l1j.server.BonusItem.BonusItemInfo;
import l1j.server.BonusItem.BonusItemLoader;
import l1j.server.BonusMaps.BonusMapInfo;
import l1j.server.BonusMaps.BonusMapTable;
import l1j.server.EventSystem.EventSystemInfo;
import l1j.server.EventSystem.EventSystemLoader;
import l1j.server.ItemDropLimit.ItemDropLimitLoader;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Chain.Action.MJPickupChain;
import l1j.server.MJTemplate.Chain.Etc.MJAdenaPickupChain;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_OBTAINED_ITEM_INFO;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.datatables.RaceTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.item.L1ItemId;

import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

import l1j.server.server.templates.*;
import l1j.server.server.templates.*;
import l1j.server.server.templates.L1Drop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ItemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;

public class DropTable {

	private static Logger _log = Logger.getLogger(DropTable.class.getName());

	private static DropTable _instance;

	private static final Random _random = new Random();

	private final HashMap<Integer, ArrayList<L1Drop>> _droplists; // 每個怪物的掉落清單
	private final HashMap<Integer, L1Drop> _adenalists; // 每個怪物的亞丁
	private static final int RATE = 1000000;

	public static DropTable getInstance() {
		if (_instance == null) {
			_instance = new DropTable();
		}
		return _instance;
	}

	private DropTable() {
		_droplists = allDropList();
		_adenalists = createAdenaDropList();
	}

	public static void reload() {
		DropTable oldInstance = _instance;
		_instance = new DropTable();
		oldInstance._droplists.clear();
		oldInstance._adenalists.clear();
	}

	public ArrayList<L1Drop> getDropList(int monid) {
		return _droplists.get(monid);
	}

	public boolean isDropListItem(int monid, int itemid) {
		ArrayList<L1Drop> drop = getDropList(monid);
		for (L1Drop d : drop) {
			if (d.getItemid() == itemid) {
				return true;
			}
		}
		return false;
	}

	public L1Drop getDrop(int monid, int itemid) {
		ArrayList<L1Drop> drop = getDropList(monid);
		for (L1Drop d : drop) {
			if (d.getItemid() == itemid) {
				return d;
			}
		}
		return null;
	}

	private HashMap<Integer, L1Drop> createAdenaDropList() {
		HashMap<Integer, L1Drop> map = new HashMap<Integer, L1Drop>();
		Selector.exec("select * from droplist_adena", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					int mobId = rs.getInt("mobId");
					int itemId = rs.getInt("itemId");
					int min = rs.getInt("min");
					int max = rs.getInt("max");
					int chance = rs.getInt("chance");
					int range = rs.getInt("range");

					L1Drop drop = new L1Drop(mobId, itemId, min, max, chance, range);
					L1Npc template = l1j.server.server.datatables.NpcTable.getInstance().getTemplate(mobId);
					L1Item items = l1j.server.server.datatables.ItemTable.getInstance().getTemplate(itemId);

					if (itemId != 40308) {
						System.out.println(String.format("[droplist_adena]: 該表中的 npcid: %s 存在除亞丁之外的其他 itemid: %s。", mobId, itemId));
						continue;
					}
					if (items == null) {
						System.out.println(String.format("[droplist_adena]: item_id: %s 是資料庫中不存在的物品。", itemId));
						continue;
					}
					if (template == null) {
						System.out.println(String.format("[droplist_adena]: 在 NPC 表中，npcid: %s 是不存在的怪物。", mobId));
						continue;
					}
					if (min == 0 || max == 0) {
						System.out.println(String.format("[droplist_adena]: npcid: %s 的亞丁掉落數量為 0。", mobId));
						continue;
					}

					map.put(drop.getMobid(), drop);
				}
			}
		});
		return map;
	}

	private HashMap<Integer, ArrayList<L1Drop>> allDropList() {
		HashMap<Integer, ArrayList<L1Drop>> droplistMap = new HashMap<Integer, ArrayList<L1Drop>>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from droplist");
			rs = pstm.executeQuery();
			L1Drop drop = null;
			while (rs.next()) {
				int mobId = rs.getInt("mobId");
				int itemId = rs.getInt("itemId");
				int min = rs.getInt("min");
				int max = rs.getInt("max");
				int chance = rs.getInt("chance");
				int range = rs.getInt("range");

				drop = new L1Drop(mobId, itemId, min, max, chance, range);

				l1j.server.server.datatables.RaceTable NpcTable = null;
				l1j.server.server.templates.L1Racer template = NpcTable.getInstance().getTemplate(mobId);
				if(template == null) {
					System.out.println(String.format("[droplist]: 在 NPC 表中，npcid: %s 是不存在的怪物。", mobId));
					continue;
				}

				L1Item items = l1j.server.server.datatables.ItemTable.getInstance().getTemplate(itemId);
				if (items == null) {
					System.out.println(String.format("[droplist]: item_id: %s 是資料庫中不存在的物品。", itemId));
					continue;
				}

				ArrayList<L1Drop> dropList = droplistMap.get(drop.getMobid());
				if (dropList == null) {
					dropList = new ArrayList<L1Drop>(8);
					droplistMap.put(new Integer(drop.getMobid()), dropList);
				}
				dropList.add(drop);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return droplistMap;
	}

	/**
     * TODO 亞丁相關
     */
	private void append_drop_adena(L1PcInstance last_attacker, L1NpcInstance monster) {
		if (Config.Login.StandbyServer) {
			return;
		}
		L1Drop drop = _adenalists.get(monster.getNpcId());
		if (drop == null || monster.isResurrect())
			return;

		if (Config.ServerRates.RateDropAdena <= 0 || Config.ServerRates.RateDropItems <= 0)
			return;

		int randomChance = (int) CommonUtil.random(RATE);
		double droprate = Config.ServerRates.RateDropAdena;
		double rateOfMapId = l1j.server.server.datatables.MapsTable.getInstance().getDropRate(monster.getMap().getBaseMapId());
		double rateOfItem = DropItemTable.getInstance().getDropRate(L1ItemId.ADENA);
		double rateOfbless = Config.ServerAdSetting.AdenaRateOfUnBlessing;
		double ainhasad = SC_REST_EXP_INFO_NOTI.selectInventoryItemsRateFromEinhasad(last_attacker, last_attacker.getAccount().get_ein_level());
		int dropChance = drop.getChance();
		if (last_attacker.getInventory().checkItem(4100121)) {
			rateOfbless = Config.ServerAdSetting.AdenaRateOfBlessing;
		} else if (last_attacker.getInventory().checkItem(4100529) && last_attacker.getLevel() <= Config.ServerAdSetting.NEWPLAYERLEVELPROTECTION) {
			rateOfbless = Config.ServerAdSetting.AdenaRateOfBlessing;
		}
		
		double rateRandom = (dropChance * droprate * rateOfMapId * rateOfItem * rateOfbless * ainhasad * selectSpecialStatAdenaropBonus(last_attacker.get_companion()) * selectAutoAdenRate(last_attacker));
		
		if (droprate == 0 || rateRandom < randomChance) {
			return;
		}

		double amount = DropItemTable.getInstance().getDropAmount(L1ItemId.ADENA);
		int min = (int) (drop.getMin() * amount * Config.ServerRates.RateDropAdena);
		int max = (int) (drop.getMax() * amount * Config.ServerRates.RateDropAdena);
		int itemCount = min;
		int addCount = max - min + 1;

		if (addCount > 1)
			itemCount += MJRnd.next(addCount);
		if (itemCount < 0)
			return;

		itemCount = Math.min(itemCount, 2000000000);

		L1ItemInstance item = l1j.server.server.datatables.ItemTable.getInstance().createItem(L1ItemId.ADENA, false);
		if (last_attacker.hasClientAutoItemRatePenalty()) {
			itemCount = (int) (itemCount * Config.ServerAdSetting.PLAYSUPPORTARDEN);
		}
		
		item.setCount(itemCount);
		
		if (item != null) { // 我添加 200705
			monster.getInventory().storeItem(item);
		}
	}

	/**
     * TODO 物品相關
     */
	public void setDrop(L1PcInstance pc, L1NpcInstance npc, L1Inventory inventory) {
		try {
			if (Config.Login.StandbyServer) {
				return;
			}

			// 獲取掉落清單
			int mobId = npc.getNpcTemplate().get_npcId();
			ArrayList<L1Drop> dropList = _droplists.get(mobId);
			if (dropList == null) {
				return;
			}

			// 獲取機率
			double droprate = Config.ServerRates.RateDropItems;
			if (droprate <= 0) {
				droprate = 0;
			}

			if (droprate <= 0) {
				return;
			}

			int itemId;
			int itemCount;
			int addCount;
			int randomChance;
			L1ItemInstance item;
			Random random = new Random(System.nanoTime());

			for (L1Drop drop : dropList) {
				itemId = drop.getItemid();
				RaceTable ItemTable = null;
				l1j.server.server.templates.L1Racer template = ItemTable.getInstance().getTemplate(itemId);
				if (template == null) {
					System.out.println("[掉落清單載入中]不存在的物品: " + itemId);
					continue;
				}

				// 判定掉落機率
				randomChance = (int) CommonUtil.random(RATE);
				double rateOfMapId = l1j.server.server.datatables.MapsTable.getInstance().getDropRate(npc.getMap().getBaseMapId());
				double rateOfItem = DropItemTable.getInstance().getDropRate(itemId);
				double rateBonus = selectInventoryItemsRate(pc) * selectSpecialStatItemDropBonus(pc) * selectItemBonus(pc);
				double ainhasad = SC_REST_EXP_INFO_NOTI.selectInventoryItemsRateFromEinhasad(pc, pc.getAccount().get_ein_level());
				double rateRandom = (drop.getChance() * droprate * rateOfMapId * rateOfItem * rateBonus * ainhasad * selectAutoItemsRate(pc));	
				
				if (droprate == 0 || rateRandom < randomChance) {
					continue;
				}

				if (template.isStackable()) {
					double amount = DropItemTable.getInstance().getDropAmount(itemId);
					int min = (int) (drop.getMin() * amount);
					int max = (int) (drop.getMax() * amount);

					itemCount = min;
					addCount = max - min + 1;

					if (addCount > 1) {
						itemCount += random.nextInt(addCount);
					}
					if (itemCount < 0) {
						itemCount = 0;
					}
					if (itemCount > 2000000000) {
						itemCount = 2000000000;
					}
				} else {
					itemCount = 1;
				}

				if (l1j.server.server.datatables.ItemTable.getInstance().getTemplate(itemId) != null) {
					item = l1j.server.server.datatables.ItemTable.getInstance().createItem(itemId, false);
					if (item == null)
						continue;
					item.setCount(itemCount);
					item.setRange(drop.getRange());
					inventory.storeItem(item);
				} else {
					_log.info("[掉落清單載入中]不存在的物品: " + itemId);
					System.out.println("[掉落清單載入中]不存在的物品: " + itemId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     * 分配掉落
     */
	public void dropShare(L1NpcInstance npc, ArrayList<?> acquisitorList, ArrayList<?> hateList, L1PcInstance pc) {
		if (Config.Login.StandbyServer) {
			return;
		}

		append_drop_adena(pc, npc);
		setDrop(pc, npc, npc.getInventory());
		
		L1Inventory inventory = npc.getInventory();
		int mobId = npc.getNpcTemplate().get_npcId();

		switch (mobId) {
		case 5100:
		case 900013:
		case 900040: // 設定龍的掉落
			if (npc.getMapId() >= 1005 && npc.getMapId() <= 1022 || npc.getMapId() > 6000 && npc.getMapId() < 6499 || npc.getMapId() > 6501 && npc.getMapId() < 6999) {
				return;
			}
			break;
		}

		if (mobId == 7320052 || mobId == 45021 || mobId == 45022 || mobId == 45040 || mobId == 45048) {
			huntEventDrop(npc);
			return;
		}

		if (inventory == null) {
			System.out.println(String.format("[掉落清單(無庫存)] : %s", inventory));
			return;
		}
		if (inventory.getItems() == null || inventory.getItems().size() <= 0) {
			return;
		}
		if (acquisitorList.size() != hateList.size()) {
			return;
		}
		if (pc.getAI() != null) {
			return;
		}
		// TODO 愛因哈薩德訂閱制高級掉落相關
		if (SC_REST_EXP_INFO_NOTI.selectInventoryItemsRateFromEinhasad(pc, pc.getAccount().get_ein_level()) <= 0) {
			return;
		}

		int totalHate = 0;
		L1Character acquisitor;
		for (int i = hateList.size() - 1; i >= 0; i--) {
			acquisitor = (L1Character) acquisitorList.get(i);
			if (Config.ServerAdSetting.AUTOLOOTINGSETTING == 0 && acquisitor instanceof MJCompanionInstance) {
				acquisitorList.remove(i);
				hateList.remove(i);
			} else if (acquisitor != null && acquisitor.getMapId() == npc.getMapId() && acquisitor.getLocation().getTileLineDistance(npc.getLocation()) <= Config.ServerAdSetting.AUTOLOOTINGRANGE) {
				totalHate += (Integer) hateList.get(i);
			} else {
				acquisitorList.remove(i);
				hateList.remove(i);
			}
		}
		
		L1Inventory targetInventory = null;
		L1PcInstance player = null;
//		L1PcInstance player;
		Random random = new Random();
		int randomInt;
		int chanceHate;
		L1ItemInstance item = null;
		
		for (int i = inventory.getSize() - 1; i >= 0; i--) {
			try {
				item = (L1ItemInstance) inventory.getItems().get(i);
				if (item == null) {
					continue;
				}
			} catch (Exception e) {
				new Throwable().printStackTrace();
				e.printStackTrace();
				System.out.println(String.format("[掉落物品庫存錯誤(重複發放)(請回收)]: [NPC ID]:%d, [NPC名稱]:%s, [列表編號]:%d, [物品ID]:%d/%d, [物品名稱]:%s, [數量]: %d, [用戶名稱]:%s, [坐標]❌%d y:%d 地圖ID:%d",
						npc.getNpcId(), npc.getName(), i, item.getId(), item.getItemId(), item.getName(), item.getCount(), pc.getName(), pc.getX(), pc.getY(), pc.getMapId()));
			}

			int itemId = item.getItem().getItemId();
			int count = item.getCount();

			if (ItemDropLimitLoader.getInstance().item_drop_limit(itemId, count)) {
				continue;
			}

			if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) {
				item.setNowLighting(false);
			}
			
			if (item.getItemId() == L1ItemId.ADENA) {
				count *= BonusMapAdenaPercent(pc);
			}

			if ((Config.ServerAdSetting.AUTOLOOTINGSETTING != 0 || AutoLoot.getInstance().isAutoLoot(itemId)) && totalHate > 0) {
				randomInt = random.nextInt(totalHate);
				chanceHate = 0;
				for (int j = hateList.size() - 1; j >= 0; j--) {
					chanceHate += (Integer) hateList.get(j);
					if (chanceHate > randomInt) {
						acquisitor = (L1Character) acquisitorList.get(j);

						if (acquisitor.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
							targetInventory = acquisitor.getInventory();
							if (acquisitor instanceof L1PcInstance) {
								player = (L1PcInstance) acquisitor;
								L1ItemInstance l1iteminstance = player.getInventory().findItemId(L1ItemId.ADENA);
								if (l1iteminstance != null && l1iteminstance.getCount() > 2000000000) {
									MJPoint pt = MJPoint.newInstance(acquisitor.getX(), acquisitor.getY(), item.getRange(), (short) acquisitor.getMapId(), 20);
									targetInventory = L1World.getInstance().getInventory(pt.x, pt.y, pt.mapId);
									player.sendPackets(String.valueOf(new S_SystemMessage("持有的金幣超過20億。(請存入倉庫後再取回)")));
								} else {
									if (player.isInParty()) {
										L1PcInstance[] partyMember = player.getParty().getMembers();
										if (item != null && item.getItemId() != L1ItemId.ADENA && Config.ServerAdSetting.AUTOLOOTINGSETTING == 0) {
											int Who = _random.nextInt(partyMember.length);
											L1PcInstance pc1 = partyMember[Who];
											if (player.getLocation().getTileLineDistance(pc1.getLocation()) < 18) {
												targetInventory = pc1.getInventory();
											}
										} else if (item != null && item.getItemId() == L1ItemId.ADENA) {
											List<L1PcInstance> _membersList = new ArrayList<L1PcInstance>(partyMember.length);
											boolean list_on = false;
											for (int a = 0; a < partyMember.length; a++) {
												if (partyMember[a].getLocation().getTileLineDistance(pc.getLocation()) < 18) {
													_membersList.add(partyMember[a]);
												}
												list_on = true;
											}

											if (list_on) {
												int member_size = _membersList.size();
												if (_membersList.size() != 0 && member_size > 0) {
													int 아데나 = count / member_size;
													try {
														for (int A = 0; A < _membersList.size(); A++) {
															int adena = Math.max(MJAdenaPickupChain.getInstance().pickup(_membersList.get(A), item, 아데나), 1);
															MJPickupChain.getInstance().handle(_membersList.get(A), item, Math.max(1, adena));
															_membersList.get(A).getInventory().storeItem(L1ItemId.ADENA, Math.max(1, adena));
															_membersList.get(A).sendPackets(String.valueOf(new S_ServerMessage(950, String.format("金幣 (%,d)", adena), _membersList.get(A).getName())));
														}
														inventory.deleteItem(item);
													} catch (Exception e) {
														e.printStackTrace();
														System.out.println(String.format("[隊伍分配(錯誤)]: 預設數量 : %d, 金幣數量 : %d, 成員數量 : %d, 角色名稱 : %s", count, adena, member_size, char_name));
																_membersList.size(), player.getName()));
														new Throwable().printStackTrace();
													}
												} else {
													pc.sendPackets(String.valueOf(new S_ServerMessage(950, String.format("金幣 (%,d)", item.getCount()), pc.getName())));
                                                    /* TODO 請確認後刪除下面的日誌 */
													LoggerInstance.getInstance().addItemAction(ItemActionType.Pickup, pc, item, (int) item.getCount());
												}
											} else {
												pc.sendPackets(String.valueOf(new S_ServerMessage(950, String.format("金幣 (%,d)", item.getCount()), pc.getName())));
												// TODO 請確認後刪除下面的日誌
												LoggerInstance.getInstance().addItemAction(ItemActionType.Pickup, pc, item, (int) item.getCount());
											}
										}
									} else { // 單人情況
										if (item != null && item.getItemId() == L1ItemId.ADENA) {
											int adenaCount = count;
											int adena = MJAdenaPickupChain.getInstance().pickup(pc, item, adenaCount);
											item.setCount(adena);
											if (player.RootMent) {
												player.sendPackets(String.valueOf(new S_ServerMessage(143, npc.getName(), String.format("%s (%,d)", item.getName(), adena))));
											}
										}
									}
								}
							}
						} else {
							MJPoint pt = MJPoint.newInstance(acquisitor.getX(), acquisitor.getY(), item.getRange(), (short) acquisitor.getMapId(), 20);
							targetInventory = L1World.getInstance().getInventory(pt.x, pt.y, pt.mapId);
						}
						break;
					}
				}
				SC_OBTAINED_ITEM_INFO.send_obtained(pc, item, item.getCount());
			} else {// 非自動拾取
					/** 惡靈之種強制自動拾取 **/
				if (itemId == 810008) {
					return;
				}
				switch (itemId) {
				case 830012:
				case 830013:
				case 830014:
				case 830015:
				case 830016:
				case 830017:
				case 830018:
				case 830019:
				case 830020:
				case 830021:
				case 830022:
				case 830023:
				case 830024:
				case 830025:
				case 830026:
				case 830027:
				case 830028:
				case 830029:
				case 830030:
				case 830031:
					System.out.println(String.format("%s 怪物掉落護身符(護身符:%s[%d])", npc.getName(), item.getName(), itemId));
					break;
				}

				item.setDropMobId(mobId);

				int maxHatePc = -1;
				int maxHate = -1;

				for (int j = hateList.size() - 1; j >= 0; j--) {
					if (maxHate < (Integer) hateList.get(j)) {
						maxHatePc = j;
						maxHate = (Integer) hateList.get(j);
					}
				}

				if (maxHatePc != -1 && acquisitorList.get(maxHatePc) instanceof L1PcInstance) {
					if (item.getItemId() == L1ItemId.ADENA) {
						if (acquisitorList.get(maxHatePc) instanceof L1PcInstance) {
							L1PcInstance maxPc = (L1PcInstance) acquisitorList.get(maxHatePc);
							int adena_count = count;
							int adena = MJAdenaPickupChain.getInstance().pickup(maxPc, item, adena_count);
							item.setCount(adena);
						}
					}
					item.startItemOwnerTimer((L1PcInstance) acquisitorList.get(maxHatePc));
				} else {
					item.startItemOwnerTimer(pc);
				}
				List<Integer> dirList = new ArrayList<Integer>();
				for (int j = 0; j < 9; j++) {
					dirList.add(j);
				}
				int x = 0;
				int y = 0;
				int dir = 0;
				do {
					if (dirList.size() == 0) {
						x = 0;
						y = 0;
						break;
					}
					randomInt = random.nextInt(dirList.size());
					dir = dirList.get(randomInt);
					dirList.remove(randomInt);
					switch (dir) {
					case 0:
						x = 0;
						y = -1;
						break;
					case 1:
						x = 1;
						y = -1;
						break;
					case 2:
						x = 1;
						y = 0;
						break;
					case 3:
						x = 1;
						y = 1;
						break;
					case 4:
						x = 0;
						y = 1;
						break;
					case 5:
						x = -1;
						y = 1;
						break;
					case 6:
						x = -1;
						y = 0;
						break;
					case 7:
						x = -1;
						y = -1;
						break;
					case 8:
						x = 0;
						y = 0;
						break;
					}
				} while (!MJPoint.isValidPosition(npc.getMap(), npc.getX() + x, npc.getY() + y));
				MJPoint pt = MJPoint.newInstance(npc.getX(), npc.getY(), item.getRange(), (short) npc.getMapId(), 20);
				targetInventory = L1World.getInstance().getInventory(pt.x, pt.y, pt.mapId);
			}
			if (targetInventory != null && targetInventory instanceof L1PcInventory) {
				L1PcInstance owner = ((L1PcInventory) targetInventory).getOwner();
				if (owner != null) {
					MJPickupChain.getInstance().handle(owner, item, Math.max(1, item.getCount()));
					if (item.getItemId() != L1ItemId.ADENA) {
						if (owner.isInParty()) {
							String message = String.format("%s (%,d)", item.getName(), item.getCount());
							S_ServerMessage pck = new S_ServerMessage(950, message, owner.getName());
							owner.getParty().broadcast(pck);
						} else {
							owner.sendPackets(String.valueOf(new S_ServerMessage(143, npc.getName(), String.format("%s (%,d)", item.getName(), item.getCount()))));
						}
					}
				}
			}
			
			inventory.tradeItem(item, item.getCount(), targetInventory);
		}

		Bonus_Drop_System(pc, npc);
		
//		pc.add_dead_count(1);
		pc.addMonsterKill(1);
		pc.sendPackets(new S_OwnCharStatus(pc));
		
		if (npc.getLight() != null) {
			npc.getLight().turnOnOffLight();
		}
	}

	private void huntEventDrop(L1NpcInstance npc) {
		if (Config.ServerAdSetting.HuntEvent) {
			int[] weapons = Config.ServerAdSetting.WEAPONS_ITEMID;
			int[] armors = Config.ServerAdSetting.ARMORS_ITEMID;
			int chance = _random.nextInt(100) + 1;
			int rnd = _random.nextInt(Config.ServerAdSetting.ITEMS); // 掉落的隨機機率（數值高表示什麼都不掉）
			int[] items = new int[Config.ServerAdSetting.RND_RKQT];   // 物品總數
			int eventIemId;
			switch (rnd) {
			case 0:
				items = weapons;
				break;
			case 1:
				items = armors;
				break;
			}
			if (chance >= Config.ServerAdSetting.CHANCE_ITEMS) {
				eventIemId = items[0];
			} else if (chance >= Config.ServerAdSetting.CHANCE_ITEMS1) {
				eventIemId = items[1];
			} else if (chance >= Config.ServerAdSetting.CHANCE_ITEMS2) {
				eventIemId = items[2];
			} else {
				eventIemId = items[3];
			}
			L1Inventory inventory = npc.getInventory();
			L1ItemInstance item = ItemTable.getInstance().createItem(eventIemId, false);
			item.setCount(1);
			inventory.storeItem(item);
			L1Character acquisitor;
			L1Inventory targetInventory = null;

			for (int i = inventory.getSize(); i > 0; i--) {
				item = inventory.getItems().get(0);
				if (item == null) {
					continue;
				}

				acquisitor = npc;
				if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) {
					item.setNowLighting(false);
				}
				MJPoint pt = MJPoint.newInstance(acquisitor.getX(), acquisitor.getY(), item.getRange(), (short) acquisitor.getMapId(), 20);
				targetInventory = L1World.getInstance().getInventory(pt.x, pt.y, pt.mapId);
				inventory.tradeItem(item, item.getCount(), targetInventory);
			}
			npc.getLight().turnOnOffLight();
		}
	}

	/**
     * 事件及獎勵額外掉落設定方法。
     */
	private void Bonus_Drop_System(L1PcInstance pc, L1NpcInstance npc) {
		try {
			if (pc.getParty() == null) {
				BonusItemInfo bonusInfo = calcBonusItem(pc, npc);
				if (bonusInfo != null) {
					tradeBonusItem(pc, npc, bonusInfo);
				}

				EventSystemInfo EventItemInfo = calcEventSytem_Item(pc);
				if (EventItemInfo != null) {
					tradeEventSystem_Item(pc, npc, EventItemInfo);
				}
			} else {
				BonusItemInfo minBonusInfo = null;
				boolean isAllOwner = true;
				L1PcInstance[] partyPlayer = pc.getParty().getMembers();
				for (int k = 0; k < partyPlayer.length; k++) {
					BonusItemInfo bonusInfo = calcBonusItem(partyPlayer[k], npc);
					if (bonusInfo == null) {
						isAllOwner = false;
						break;
					} else {
						if (minBonusInfo == null)
							minBonusInfo = bonusInfo;
						else {
							if (bonusInfo.get_item_enchant() < minBonusInfo.get_item_enchant()) {
								minBonusInfo = bonusInfo;
							}
						}
					}
				}

				if (isAllOwner) {
					tradeBonusItem(pc, npc, minBonusInfo);
				}

				EventSystemInfo minEventInfo = null;
				boolean isAllEventOwner = true;
				L1PcInstance[] partyEventPlayer = pc.getParty().getMembers();
				for (int k = 0; k < partyEventPlayer.length; k++) {
					EventSystemInfo eventbonusInfo = calcEventSytem_Item(partyEventPlayer[k]);
					if (eventbonusInfo == null) {
						isAllEventOwner = false;
						break;
					} else {
						if (minEventInfo == null)
							minEventInfo = eventbonusInfo;
						else {
							minEventInfo = eventbonusInfo;
						}
					}
				}

				if (isAllEventOwner) {
					tradeEventSystem_Item(pc, npc, minEventInfo);
				}
			}
			
			BonusMapDropItems(pc, npc);
			BonusDropItem(pc, npc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BonusItemInfo calcBonusItem(L1PcInstance pc, L1NpcInstance npc) {
		BonusItemInfo result = null;
		for (L1ItemInstance bitem : pc.getInventory().getItems()) {
			int enchant = bitem.getEnchantLevel();
			BonusItemInfo bInfo = BonusItemLoader.getInstance().getBonusItemInfo(bitem.getItemId(), enchant);
			if (bInfo != null) {
				if (bInfo.get_mapids() == null)
					continue;
				if (bInfo.isEquip()) {
					if (!bitem.isEquipped())
						continue;
				}
				if (bInfo.isMap(pc)) {
					result = bInfo;
					break;
				}
			}
		}
		return result;
	}

	private EventSystemInfo calcEventSytem_Item(L1PcInstance pc) {
		EventSystemInfo result = null;
		EventSystemInfo bInfo = EventSystemLoader.getInstance().getEventSystemInfo(pc);
		if (bInfo != null) {
			if (bInfo.get_event_map_id() == 0)
				return null;
			if (bInfo.isMap(pc)) {
				result = bInfo;
			}
		}
		return result;
	}

	private void tradeBonusItem(L1PcInstance pc, L1NpcInstance npc, BonusItemInfo bonusInfo) {
		L1ItemInstance bonusitem = null;
		if (npc != null && pc != null && pc.getInventory() != null) {// 新增
			if (!npc.isResurrect() && pc.getInventory().checkItem(bonusInfo.get_item_id())) {
				if (MJRnd.isWinning(1000000, bonusInfo.calc_probability(pc))) {
					bonusitem = ItemTable.getInstance().createItem(bonusInfo.get_bonus_id());
					bonusitem.setCount(bonusInfo.calc_count(pc));
					pc.getInventory().storeItem(bonusitem);
					pc.sendPackets(String.valueOf(new S_ServerMessage(143, npc.getName(), String.format("%s (%,d)", bonusitem.getName(), bonusitem.getCount()))));
					SC_OBTAINED_ITEM_INFO.send_obtained(pc, bonusitem, bonusitem.getCount());
					if (Config.Login.UseShiftServer) {
						MJPickupChain.getInstance().handle(pc, bonusitem, bonusitem.getCount());
					} else {
						DropMent(pc, bonusitem);
					}
				}
			}
		}
	}

	private void tradeEventSystem_Item(L1PcInstance pc, L1NpcInstance npc, EventSystemInfo bonusInfo) {
		L1ItemInstance bonusitem = null;
		String[] eventbonusitem = null;
		if (bonusInfo.get_drop_item() != null) {
			eventbonusitem = (String[]) MJArrangeParser.parsing(bonusInfo.get_drop_item(), ",", MJArrangeParseeFactory.createStringArrange()).result();
		}
		if (!npc.isResurrect() && pc.getMapId() == bonusInfo.get_event_map_id() && eventbonusitem != null) {
			for (int i = 0; i < eventbonusitem.length; i++) {
				if (MJRnd.isWinning(1000000, bonusInfo.calc_probability(i, pc)) && bonusInfo.is_event()) {
					bonusitem = l1j.server.server.datatables.ItemTable.getInstance().createItem(Integer.parseInt(eventbonusitem[i]));
					bonusitem.setCount(bonusInfo.calc_count(i, pc));
					pc.getInventory().storeItem(bonusitem);
					pc.sendPackets(String.valueOf(new S_ServerMessage(143, npc.getName(), String.format("%s (%,d) (活動)", bonusitem.getName(), bonusitem.getCount()))));
					if (Config.Login.UseShiftServer) {
						MJPickupChain.getInstance().handle(pc, bonusitem, bonusitem.getCount());
					} else {
						DropMent(pc, bonusitem);
					}
				}
			}
		}
	}

	/**
     * 根據地圖的獎勵掉落
     */
	private void BonusMapDropItems(L1PcInstance pc, L1NpcInstance npc) {
		L1ItemInstance dropitem = null;
		int mapId = npc.getMapId();
		BonusMapInfo bInfo = BonusMapTable.getInstance().getBonusMapInfo(mapId);
		if (bInfo != null) {
			if (npc.isDead()) {
				String[] itemid = (String[]) MJArrangeParser.parsing(bInfo.get_item_id(), ",", MJArrangeParseeFactory.createStringArrange()).result();
				for (int i = 0; i < itemid.length; i++) {
					if (MJRnd.isWinning(1000000, bInfo.calc_probability(i))) {
						dropitem = l1j.server.server.datatables.ItemTable.getInstance().createItem(Integer.parseInt(itemid[i]));
						if (dropitem != null) {
							dropitem.setCount(bInfo.calc_count(i));
							pc.getInventory().storeItem(dropitem);
							pc.sendPackets(new S_ServerMessage(143, npc.getName(), dropitem.getLogName()));
							SC_OBTAINED_ITEM_INFO.send_obtained(pc, dropitem, dropitem.getCount());
							if (Config.Login.UseShiftServer) {
								MJPickupChain.getInstance().handle(pc, dropitem, dropitem.getCount());
							} else {
								DropMent(pc, dropitem);
							}
						}
					}
				}
			}
		}
	}

	private void BonusDropItem(L1PcInstance player, L1NpcInstance npc) {
		L1ItemInstance bonusitem = null;
		BonusDropSystemInfo bInfo = BonusDropSystemLoader.getInstance().getBonusDropSystemInfo(npc.getNpcId());
		if (bInfo != null) {
			if (npc.isDead()) {
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc, 30)) {
					if (pc.getAI() != null)
						continue;
					if (pc.getBonusDropNpc() != npc.getId())
						continue;
					String[] itemid = (String[]) MJArrangeParser.parsing(bInfo.get_item_id(), ",", MJArrangeParseeFactory.createStringArrange()).result();
					String[] enchant = (String[]) MJArrangeParser.parsing(bInfo.get_item_enchant(), ",", MJArrangeParseeFactory.createStringArrange()).result();
					for (int i = 0; i < itemid.length; i++) {
						if (MJRnd.isWinning(1000000, bInfo.calc_probability(pc, i))) {
							if (bInfo.get_Effect()) {
								pc.send_effect(bInfo.get_EffectId(), true);
							}
							if (itemid[i].equalsIgnoreCase("ncoin")) {
								pc.addNcoin(bInfo.calc_count(pc, i));
								pc.getNetConnection().getAccount().updateNcoin();
								String s = String.format("\\aGN幣 (%,d)元已充值。", bInfo.calc_count(pc, i));
								pc.sendPackets(s);
							} else {
								bonusitem = l1j.server.server.datatables.ItemTable.getInstance().createItem(Integer.parseInt(itemid[i]));
								if (bonusitem != null) {
									bonusitem.setCount(bInfo.calc_count(pc, i));
									bonusitem.setEnchantLevel(Integer.parseInt(enchant[i]));
									pc.getInventory().storeItem(bonusitem);
									pc.sendPackets(new S_ServerMessage(143, npc.getName(), bonusitem.getLogName()));
									SC_OBTAINED_ITEM_INFO.send_obtained(pc, bonusitem, bonusitem.getCount());
									if (Config.Login.UseShiftServer) {
										MJPickupChain.getInstance().handle(pc, bonusitem, bonusitem.getCount());
									} else {
										DropMent(pc, bonusitem);
									}
								}
							}
						}
					}

					if (bInfo.get_isMent()) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, bInfo.get_isMentMess()));
						pc.sendPackets(bInfo.get_isMentMess());
					}

					pc.setBonusDropNpc(0);
				}
			}
		}
	}

	/**
     * 根據地圖的亞丁幣獲得倍率增加
     */
	private double BonusMapAdenaPercent(L1Character character) {
		if (character == null || !(character instanceof L1PcInstance)) {
			return 1.0D;
		}

		BonusMapInfo Info = BonusMapTable.getInstance().getBonusMapInfo(character.getMapId());
		double adena_point = 0;
		if (Info != null) {
			adena_point += Info.get_andena_percent() * 0.01;
		}

		if (adena_point <= 0)
			adena_point = 1.0;

		return adena_point;
	}

	/**
     * 根據艾因哈薩德的祝福獲得的機率
     */
	private double selectInventoryItemsRate(L1Character character) {
		if (character == null) {
			return 0.00;
		}
		if (character instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) character;
			if (pc.getAccount() != null) {
				return SC_REST_EXP_INFO_NOTI.selectInventoryItemsRateFromEinhasad(pc, pc.getAccount().get_ein_level());
			}
		}

		if (character instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) character;
			if (npc.getMaster() != null && npc.getMaster() instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) npc.getMaster();
				return SC_REST_EXP_INFO_NOTI.selectInventoryItemsRateFromEinhasad(pc, pc.getAccount().get_ein_level());
			}
			if (npc instanceof MJCompanionInstance) {
				L1PcInstance pc = ((MJCompanionInstance) npc).get_master();
				if (pc != null) {
					return SC_REST_EXP_INFO_NOTI.selectInventoryItemsRateFromEinhasad(pc, pc.getAccount().get_ein_level());
				}
			}
		}
		return 0.00D;
	}

	/**
     * 自動狩獵時的物品獲得機率
     */
	private double selectAutoItemsRate(L1Character character) {
		if (character == null || !(character instanceof L1PcInstance)) {
			return 1.0D;
		}

		L1PcInstance pc = (L1PcInstance) character;
		return pc.hasClientAutoItemRatePenalty() ? Config.ServerAdSetting.PLAYSUPPORTITEM : 1D;
	}

	/**
     * 自動狩獵時的金幣獲得機率
     */
	private double selectAutoAdenRate(L1Character character) {
		if (character == null || !(character instanceof L1PcInstance)) {
			return 1.0D;
		}

		L1PcInstance pc = (L1PcInstance) character;
		return pc.hasClientAutoItemRatePenalty() ? Config.ServerAdSetting.PLAYSUPPORTARDEN : 1D;
	}

	/**
	 * 根據超級屬性額外獲得的機率（物品）
	 */
	private double selectSpecialStatItemDropBonus(L1Character character) {
		if (character == null || !(character instanceof L1PcInstance)) {
			return 1.0D;
		}

		L1PcInstance pc = (L1PcInstance) character;
		AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
		double special_point = 1.0;
		if (Info != null) {
			special_point += CalcStat.calcAinhasadStatFirst(Info.get_lucky()) * 0.01;
		}
//		System.out.println("아이템 : " + special_point);
		return special_point;
	}

	/**
	 * 根據超級屬性額外獲得的機率（金幣）
	 */
	private double selectSpecialStatAdenaropBonus(L1Character character) {
		if (character == null || !(character instanceof L1PcInstance)) {
			return 1.0D;
		}

		L1PcInstance pc = (L1PcInstance) character;
		AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
		double special_point = 1.0;
		if (Info != null) {
			special_point += CalcStat.calcAinhasadStatSecond(Info.get_lucky()) * 0.01;
		}
		return special_point;
	}

	/**
	 * 根據物品選項的額外獲得機率
	 */
	private double selectItemBonus(L1Character character) {
		if (character == null || !(character instanceof L1PcInstance)) {
			return 1.0D;
		}

		L1PcInstance pc = (L1PcInstance) character;
		double bonus_point = 1.0;
		if (pc.getItemBonus() > 0) {
			bonus_point += pc.getItemBonus() * 0.01;
		}
		return bonus_point;
	}
	
	private void DropMent(L1PcInstance pc, L1ItemInstance item) {
		if (ItemMessageTable.getInstance().isPickUpMessage(item.getItemId())) {
			L1ItemMessage temp = ItemMessageTable.getInstance().getPickUpMessage(item.getItemId());
			String men = "";
			if (temp != null) {
				if (temp.getType() == 1) {
					if (temp.isMentuse()) {
						if (temp.getOption() == 1)
							men = "" + pc.getName() + " 您";
						else
							men = "某人";

						if (temp.getMent() != null) {
							L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(temp.getMent()));
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, temp.getMent()));
						} else {
							String locationName = l1j.server.server.datatables.MapsTable.getInstance().getMapName(pc.getMapId());
							String itemName = item.getViewName();
							if (itemName == null)
								itemName = item.getName();
							String message = String.format("" + men + " %s \fH在" + locationName + "獲得了。", itemName);
							String message2 = String.format("" + men + " %s \fH在" + locationName + "獲得了。", itemName);
							L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[] { new S_SystemMessage(message), new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message2) });
						}
					}
				}
			}
		}
	}

}