package l1j.server.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import MJShiftObject.Battle.MJShiftBattlePlayManager;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.BuyLimitSystem.BuyLimitSystem;
import l1j.server.BuyLimitSystem.BuyLimitSystemAccount;
import l1j.server.BuyLimitSystem.BuyLimitSystemAccountTable;
import l1j.server.BuyLimitSystem.BuyLimitSystemCharacter;
import l1j.server.BuyLimitSystem.BuyLimitSystemCharacterTable;
import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
import l1j.server.MJDShopSystem.MJDShopItem;
import l1j.server.MJItemExChangeSystem.S_ItemExSelectPacket;
import l1j.server.MJTemplate.MJProto.MainServer_Client_InterRacing.SC_INTER_RACING_TICKET_SELL_LIST_NOTI.SellItemT;
import l1j.server.PowerBall.PowerBallController;
import l1j.server.TJ.TJCouponProvider;

import l1j.server.server.IdFactory;
import l1j.server.server.server.Controller.BugRaceController;
import l1j.server.server.server.datatables.ItemTable;
import l1j.server.server.server.datatables.NoShopAndWare;
import l1j.server.server.server.datatables.NpcShopTable;
import l1j.server.server.server.datatables.NpcShopTable2;
import l1j.server.server.datatables.NpcShopTable3;
import l1j.server.server.server.datatables.ShopTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Warehouse.ClanWarehouse;
import l1j.server.server.model.Warehouse.ElfWarehouse;
import l1j.server.server.model.Warehouse.PrivateWarehouse;
import l1j.server.server.model.Warehouse.SupplementaryService;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.shop.L1AssessedItem;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.model.shop.L1ShopBuyOrderList;
import l1j.server.server.model.shop.L1ShopSellOrderList;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.server.monitor.Logger.WarehouseType;
import l1j.server.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.SQLUtil;

public class C_Result extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final HashMap<Integer, String> afterImpls;
	static {
		afterImpls = new HashMap<>();
		afterImpls.put(0, "L1Merchant"); //클랜 상점
		afterImpls.put(7320121, "L1Merchant");
		afterImpls.put(2020562, "L1Merchant");
		afterImpls.put(2020563, "L1Merchant");
		afterImpls.put(2020564, "L1Merchant");
		afterImpls.put(2020565, "L1Merchant");
		afterImpls.put(2020566, "L1Merchant");
		afterImpls.put(2020567, "L1Merchant");
		afterImpls.put(2020568, "L1Merchant");
		afterImpls.put(2020569, "L1Merchant");
		afterImpls.put(2020700, "L1Merchant");
		afterImpls.put(2020701, "L1Merchant");
		afterImpls.put(2020702, "L1Merchant");
		afterImpls.put(2020703, "L1Merchant");
		afterImpls.put(2020704, "L1Merchant");
		afterImpls.put(2020705, "L1Merchant");
		afterImpls.put(2020706, "L1Merchant");
		afterImpls.put(2020707, "L1Merchant");
		afterImpls.put(2020708, "L1Merchant");
		afterImpls.put(2020570, "L1Merchant");
		afterImpls.put(2020571, "L1Merchant");

		afterImpls.put(7320085, "L1Merchant"); // 基本
		afterImpls.put(73201211, "L1Merchant"); // 王族
		afterImpls.put(73201212, "L1Merchant"); // 法師
		afterImpls.put(73201213, "L1Merchant"); // 妖精
		afterImpls.put(73201214, "L1Merchant"); // 黑暗妖精
		afterImpls.put(73201215, "L1Merchant"); // 龍騎士
		afterImpls.put(73201216, "L1Merchant"); // 幻術師
		afterImpls.put(73201217, "L1Merchant"); // 戰士
		afterImpls.put(73201218, "L1Merchant"); // 騎士
		afterImpls.put(73201219, "L1Merchant"); // 劍士
		afterImpls.put(73201220, "L1Merchant"); // 黃金槍騎
	}
	
	private static final String DWARF		= "L1Dwarf";
	private static final String NPC_SHOP	= "L1NpcShop";
	private static final String MERCHANT	= "L1Merchant";
	private static final String SELECTOR	= "L1Selector";
	private static final String TJCOUPON	= "L1TjCoupon";
	private static final String ITEMCHANGE	= "L1ItemChange";
	
	private L1PcInstance pc ;
	private int npcObjectId;
	private int resultType;
	private int size;
	private L1Object findObject;
	private String npcImpl;
	private String npcName;
	private int npcId;
	private int level;
	private boolean tradable;
	private boolean isPrivateShop;
	private boolean isPrivateNpcShop;
	
	private int bugCount;
	public C_Result(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		pc = clientthread.getActiveChar();
		if (pc == null)
			return;
		if (pc.getOnlineStatus() == 0) {
			clientthread.kick();
			return;
		}
		for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
			if (player.getAccountName().equalsIgnoreCase(clientthread.getAccountName()) && !player.isPrivateShop()) {
				bugCount++;
			}
		}
		if (bugCount > 1) {
			clientthread.kick();
			return;
		}
		// 패킷 읽어옴..
		npcObjectId = readD();
		resultType = readC();
		size = readC();
//		@SuppressWarnings("unused")
		readP(1); //unknown
//		int unknown = readC();

		if (size < 0 || size>L1PcInventory.MAX_AMOUNT) {
			return;
		}
		
		
		isPrivateShop = false;
		isPrivateNpcShop = false;

		level = pc.getLevel();
		npcId = 0;
		npcImpl = "";
		tradable = true;
		findObject = L1World.getInstance().findObject(npcObjectId);
		npcName = "";

		if (findObject != null) { // 3셀
			int diffLocX = Math.abs(pc.getX() - findObject.getX());
			int diffLocY = Math.abs(pc.getY() - findObject.getY());
			if (resultType == 18 && pc.isGm() && findObject instanceof L1PcInstance && npcObjectId != pc.getId()) {
				L1PcInstance target = (L1PcInstance) findObject;
				L1ItemInstance item = null;

				if (target.getInventory().getItems() == null)
					return;

				ArrayList<L1ItemInstance> _list = new ArrayList<L1ItemInstance>(target.getInventory().getItems());
				ArrayList<Integer> _list_count = new ArrayList<Integer>();

				for (int i = 0; i < size; i++) {
					int index = readD();
					int count = readD();
					item = _list.get(index);

					if (pc.isTwoLogin(pc)) {
						return;
					}

					if (item == null) {
						return;
					}

					if (!item.isStackable() && count != 1) {
						pc.sendPackets(new S_Disconnect());
						return;
					}
					if (count <= 0 || item.getCount() <= 0) {
						pc.sendPackets(new S_Disconnect());
						return;
					}
					if (count > item.getCount()) {
						count = item.getCount();
					}
					_list.add(item);
					_list_count.add(Integer.valueOf(count));

					L1ItemInstance tr = null;
					tr = (L1ItemInstance) _list.get(index);
					int item_count = ((Integer) _list_count.get(i)).intValue();
					if (item.isEquipped()) {
						target.getInventory().setEquipped(item, false, false, true, false);
						target.start_teleport(target.getX(), target.getY(), target.getMapId(), target.getHeading(), 18339, false);
					}
					target.getInventory().tradeItem(tr, item_count, pc.getInventory());
					pc.sendPackets(String.format("物品回收: %s的 %s (%d) 回收。", target.getName(), tr.getName(), item_count));
				}

				_list.clear();
				_list_count.clear();
				return;
			} else if (resultType == 20 && findObject instanceof L1PcInstance && npcObjectId == pc.getId()) {
				LinkedList<ItemCountInfo> items = new LinkedList<>();
				for (int i = 0; i < size; i++) {
					int index = readD();
					int count = readD();
					items.add(new ItemCountInfo(index, count));
				}
				TJCouponProvider.provider().onChoiceItem(pc, items);
				return;
			}


			if (findObject instanceof L1NpcInstance) {
				L1NpcInstance targetNpc = (L1NpcInstance) findObject;
				if (!targetNpc.is_sub_npc()  && targetNpc.getNpcId() != Config.ServerAdSetting.PC_MASTER_SHOP_ID) {
					if (targetNpc.getNpcId() != Config.ServerAdSetting.TIME_COLLECTION_NPC_IDS[0]) {
//						System.out.println("封包確認4");
			if (diffLocX > 18 || diffLocY > 18) {
			    if (!(findObject instanceof L1NpcInstance)) {
			        System.out.println(String.format("檢測到不明的店鋪接近。%d %s (%d, %d, %d)", findObject.getId(), String.valueOf(findObject), findObject.getX(), findObject.getY(), findObject.getMapId()));
			        return;
			    }
			    if (resultType == 5 && size == 0 && targetNpc.getNpcTemplate().getImpl().equalsIgnoreCase("L1Dwarf")) {
			        L1Clan clan = pc.getClan();
			        if (clan == null)
			            return;

			        clan.deleteClanRetrieveUser(pc.getId());
			        pc.sendPackets("倉庫距離過遠，無法找到物品。");
			    } else {
			        pc.sendPackets("與販售/購買 NPC 距離過遠，交易已取消。");
			    }
			    return;
			}
			}
				}

				npcId = targetNpc.getNpcTemplate().get_npcId();
				npcImpl = targetNpc.getNpcTemplate().getImpl();
				npcName = targetNpc.getName();

				// npcshop add
				if (npcImpl.equals("L1NpcShop"))
					isPrivateNpcShop = true;

			} else if (findObject instanceof L1PcInstance) {
				if (npcObjectId == pc.getId() && resultType == 9) {
					S_ItemExSelectPacket select_packet = pc.get_select_item();
					if (select_packet != null) {
						int select_index = readD();
						int select_count = readD();
						if (select_count <= 0)
							return;

						select_packet.do_select(pc, select_index);
						select_packet.dispose();
						return;
					}
					pc.sendPackets("時間已超過。");
					return;
				}

				L1PcInstance gm = (L1PcInstance) findObject;
				if (gm.isGm() && resultType == 3) {
					int objectId = readD();
					int count = readD();
					L1Object tmp = L1World.getInstance().findObject(objectId);
					if (tmp != null && tmp instanceof L1PcInstance) {
						gm.start_teleport(tmp.getX(), tmp.getY(), tmp.getMapId(), gm.getHeading(), 18339, false, false);
					} else
						return;
				}

				if (findObject.getId() == pc.getId() && pc.getMapId() == 38 && resultType == 0) {
					for (int i = 0; i < size; i++) {
						int line = readD();
						int count = readD();

						if (count <= 0) {
							return;
						}

						if (count <= 0 || count >= 10000) {
							return;
						}

						try {
							// TODO 當購買傳送卷軸時
							if (pc.getInventory().checkItem(40308, 500 * (int) count)) {
								pc.getInventory().consumeItem(40308, 500 * (int) count);
								pc.getInventory().storeItem(4100086, (int) count, true);
							} else {
								pc.sendPackets("您的金幣不足。");
							}
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
					}
					return;
				}
				isPrivateShop = true;
			}
		}
		
		

		if (npcObjectId == 7626) {
			npcId = 5;
			npcImpl = "L1Merchant";
		}
		if (afterImpls.containsKey(npcObjectId)) {
			npcId = npcObjectId;
			npcImpl = afterImpls.get(npcObjectId);
		}
		typeSwitch();
	}
	private void typeSwitch(){
		if(resultType == 0 && npcImpl.equalsIgnoreCase(MERCHANT))                                        nomalShopBuy(); // 購買物品
		else if(resultType == 1 && npcImpl.equalsIgnoreCase(MERCHANT))                                    nomalShopSell(); // 賣出物品
		else if(resultType == 2 && npcImpl.equalsIgnoreCase(DWARF))                                        nomalWarehouseIn(); // 存入個人倉庫
		else if(resultType == 3 && npcImpl.equalsIgnoreCase(DWARF))                                        nomalWarehouseOut(); // 提取個人倉庫
		else if(resultType == 4 && npcImpl.equalsIgnoreCase(DWARF))                                        clanWarehouseIn(); // 存入血盟倉庫
		else if(resultType == 5 && npcImpl.equalsIgnoreCase(DWARF))                                        clanWarehouseOut(); // 提取血盟倉庫
		else if(resultType == 5 && npcImpl.equalsIgnoreCase(DWARF))                                        clanWarehouseCancel(); // 從血盟倉庫取消提取，或按下 ESC 鍵
		else if(resultType == 8 && npcImpl.equalsIgnoreCase(DWARF) && pc.isElf())                        elfWarehouseIn(); // 存入妖精倉庫
		else if(resultType == 9 && npcImpl.equalsIgnoreCase(DWARF) && pc.isElf())                        elfWarehouseOut(); // 提取妖精倉庫
		else if(resultType == 21)                                                                        packageWarehousOut(); // 提取套裝倉庫
/*      else if(resultType == 17 && npcImpl.equalsIgnoreCase(DWARF) && pc.getSpecialWareHouseSize() > 0)specialWarehouseIn(); // 存入特殊倉庫
/		else if(resultType == 18 && npcImpl.equalsIgnoreCase(DWARF))                                    specialWarehouseOut(); // 提取特殊倉庫
*/      else if(resultType == 0 && isPrivateNpcShop)                                                    npcPrivateShopBuy(); // NPC 私人商店購買
		else if(resultType == 1 && isPrivateNpcShop)                                                    npcPrivateShopSell(); // NPC 私人商店出售
		else if(resultType == 0 && isPrivateShop)                                                        privateShopBuy(); // 私人商店購買物品
		else if(resultType == 1 && isPrivateShop)                                                        privateShopSell(); // 私人商店出售
/*		else if(resultType == 3 && npcImpl.equalsIgnoreCase(SELECTOR))									itemSelector();
		else if(resultType == 3 && npcImpl.equalsIgnoreCase(TJCOUPON))									tjCouponSelect();
		else if(resultType == 3 && npcImpl.equalsIgnoreCase(ITEMCHANGE))								itemChange();*/
	}

	/********************************************************************************************************
	 ****************************************** 購買物品 ***********************************************
	 *********************************************************************************************************/
	private void nomalShopBuy() {

		if ((pc.getLevel() >= Config.ServerAdSetting.USERNOSHOPLEVEL && pc.getClanid() <= 0) && !pc.isGm()) {
			pc.sendPackets("\fY等級 " + Config.ServerAdSetting.USERNOSHOPLEVEL + " 以上的無血盟角色無法使用商店");
			pc.sendPackets("\fY至少加入血盟是為了服務器上順暢遊戲的系統");
			return;
		}

		// 購買物品
		L1Shop shop = ShopTable.getInstance().get(npcId);
		if (shop == null) {
			System.out.println(String.format("[商店]%d NPC無法找到。", npcId));
			return;
		}
		L1ShopBuyOrderList orderList = shop.newBuyOrderList();
		int itemNumber = 0;
		long itemcount = 0;
		/*
		 * if (npcId == 7200002) { pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.TAM, pc.getAccount().getTamPoint())); }
		 */

		if (pc.isServerDown == true) {
			if (npcId == 70035 || npcId == 70041 || npcId == 70042 || npcId == 170041 || npcId == 370041 || npcId == BugRaceController.RACE_SELLER_NPCID) {
				pc.sendPackets(String.valueOf(new S_SystemMessage("伺服器即將關閉期間無法進行購買。")));
				return;
			}
		}

		if (shop.getSellingItems().size() < size) {
			if (npcId == 70035 || npcId == 70041 || npcId == 70042 || npcId == BugRaceController.RACE_SELLER_NPCID) {
				pc.sendPackets("比賽已經開始了。");
				return;
			}
			try {
				System.out.println("■[錯誤防護]■: " + pc.getName() + " 試圖購買超過商店出售的物品數量(" + shop.getSellingItems().size() + ")。試圖購買的數量: " + size);
				pc.getNetConnection().kick();
				pc.getNetConnection().close();
	//			pc.getNetConnection().close();
				return;
			}catch (Exception e) {
				e.getStackTrace();
			}
		}
		for (int i = 0; i < size; i++) {
			itemNumber = readD();

			itemcount = readD();
			if (itemcount <= 0) {
				return;
			}
			if (npcId >= 6100000 && npcId <= 6100035) {
				if (itemcount > 1) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("只能一次購買一個。")));
					return;
				}
			}

			if (npcId == 370041) {
				// if (itemcount <= 0 || itemcount >= 99999) {
				if (itemcount <= 0 || itemcount >= 10000) {
					pc.sendPackets("只能購買1到9999個。");
					return;
				}
			} else {
				if (itemcount <= 0 || itemcount >= 10000) {
					pc.sendPackets("只能購買1到9999個。");
					return;
				}
			}

			if (Config.ServerAdSetting.GAHO_DBAN) {
				int inv_itemid = 4100121;
				int inv_count = pc.getInventory().checkItemCount(inv_itemid);

				if (shop.getSellingItems().get(itemNumber).getItemId() == inv_itemid) {
					if (!pc.getSafetyZone()) {
						pc.sendPackets("\f3高級不滅祝福只能在城鎮內購買。");
						return;
					}
				}

				if (shop.getSellingItems().get(itemNumber).getItemId() == inv_itemid) {
					if (3 < itemcount) {
						pc.sendPackets(String.format("\f3高級不滅的祝福無法購買超過3個。"));
						return;
					}
					if (pc.getInventory().checkItem(inv_itemid)) {
						if (inv_count == 1 && itemcount >= 3) {
							pc.sendPackets(String.format("\f3高級不滅的祝福無法購買超過3個。"));
							continue;
						} else if (inv_count >= 2 && itemcount >= 2) {
							pc.sendPackets(String.format("\f3高級不滅的祝福無法購買超過3個。"));
							continue;
						} else if (inv_count >= 3 && itemcount >= 1) {
							pc.sendPackets(String.format("\f3高級不滅的祝福無法購買超過3個。"));
							continue;
						}
					}
				}
			}

			if (npcId == 0) {
				if (shop.getSellingItems().get(itemNumber).getClanShopType() > 0) {
					switch (shop.getSellingItems().get(itemNumber).getClanShopType()) {
/*                    case 8: // 盟主/副盟主/守護/精英/普通
						if (pc.getClanRank() == L1Clan.TRAINEE) { // 修練
						pc.sendPackets("普通等級以上可購買。");
						return;
						}
						break;*/
						case 9: // 盟主/副盟主/守護
							if (/*pc.getClanRank() == L1Clan.TRAINEE ||*/ pc.getClanRank() == L1Clan.NORMAL || pc.getClanRank() == L1Clan.ELITE) { // 普通, 精英
								pc.sendPackets("守護等級以上可購買。");
								return;
							}
							break;
						case 10: // 盟主
							if (pc.getClanRank() != L1Clan.PRINCE) { // 盟主
								pc.sendPackets("僅盟主可購買。");
								return;
							}
							break;
					}
				}
			}

/*                if (npcId == 19) { // N幣充值請聯繫網站客服中心
					if (itemNumber == 0) {
					pc.sendPackets(String.format(Config.Message.NCOIN_MESSAGE, pc.getAccount().Ncoin_point));
//                        pc.sendPackets(String.format("\aQ(N幣充值通過應用中心(CTRL+Z)進行，並可通過Boss Raid獲得) (Ncoin:%,d)", pc.getAccount().Ncoin_point));
					return;
					}
					}*/

			if (BuyLimitSystem(pc, shop.getSellingItems().get(itemNumber).getItem().getItemId(), (int) itemcount, shop.getSellingItems().get(itemNumber).getPrice()))
				continue;

			try {
				orderList.add(itemNumber, (int) itemcount, pc);
				if (shop.getSellingItems().size() > itemNumber) {
					LoggerInstance.getInstance().addShop(shop.getSellingItems().get(itemNumber).getItem().getName(), (int) itemcount,
							(long) shop.getSellingItems().get(itemNumber).getPrice() * itemcount, npcName, pc.getName());
					if (orderList.BugOk() != 0) {
						for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
							if (player.isGm() || pc == player) {
								player.sendPackets(String.valueOf(new S_SystemMessage(pc.getName() + "，您已超過商店的最大購買數量 (" + itemcount + ")。")));
							}
						}
					}
				} else {
					System.out.println(String.format("[商店購買例外資訊] 請檢查 %s 的背包。", pc.getName()));
				}
			} catch (Exception e) {
				System.out.println(String.format("[商店購買例外資訊] 角色名: %s, itemNumber: %d, itemcount: %d", pc.getName(), itemNumber, itemcount));
				e.printStackTrace();
				return;
			}
		}

		if (orderList.getList().size() == 0)
			return;

		int bugok = orderList.BugOk();
		if (bugok == 0) {
			shop.sellItems(pc, orderList);
			pc.saveInventory();
		}
		
	}
	/**
	 * 賣出物品
	 **/
	private void nomalShopSell() {

		/*
		 * if(Config.Login.StandbyServer){ pc.sendPackets("在開放等待狀態下，此操作不可行。"); return; }
		 */
		if ((pc.getLevel() >= Config.ServerAdSetting.USERNOSHOPLEVEL && pc.getClanid() <= 0) && !pc.isGm()) {
			pc.sendPackets("\fY等級 " + Config.ServerAdSetting.USERNOSHOPLEVEL + " 以上的無血盟角色無法使用商店");
			pc.sendPackets("\fY至少加入一個血盟是為了伺服器的順暢遊戲體驗");
			return;
		}



		// 賣出物品
		L1Shop shop = ShopTable.getInstance().get(npcId);
		L1ShopSellOrderList orderList = shop.newSellOrderList(pc);
		int itemNumber;
		long itemcount;

		for (int i = 0; i < size; i++) {
			itemNumber = readD();
			itemcount = readD();
			if (itemcount <= 0) {
				return;
			}
			if (npcId >= 6100000 && npcId <= 6100035 && !pc.getInventory().getItem(itemNumber).isPackage()) {
				pc.sendPackets(new S_SystemMessage("包含未從封包商店購買的物品。"));
				return;
			}


			// TODO 如果金額超過18億，將其每1億轉換為支票。
			int over_count = (int) (itemcount + pc.getInventory().countItems(L1ItemId.ADENA));
			if (over_count >= Config.ServerAdSetting.ADEN_OVER_COUNT) {
				if (pc.getInventory().consumeItem(Config.ServerAdSetting.ADEN_ITEMID, Config.ServerAdSetting.ADEN_COUNT)) {
					pc.getInventory().storeItem(Config.ServerAdSetting.REWARD_ITEMID, Config.ServerAdSetting.REWARD_COUNT);
				}
			}
			
			orderList.add(itemNumber, (int) itemcount, pc);
			L1ItemInstance item = pc.getInventory().getItem(itemNumber);
			if (item != null) {
				L1AssessedItem assessedItem = shop.assessItem(item);
				// TODO 當玩家在商店中出售物品時，如果總金額超過配置設定的18億，則返回處理
				if ((long) assessedItem.getAssessedPrice() * itemcount >= Config.ServerAdSetting.ADEN_OVER_ADEN) {
					pc.sendPackets(Config.ServerAdSetting.ADEN_OVER_ADEN + "金幣以上無法出售。");
					return;
				}
				LoggerInstance.getInstance().addShopSell(item.getItem().getName(), (int) itemcount, (long) assessedItem.getAssessedPrice() * itemcount, npcName, pc.getName());
			}
		}
	
		int bugok = orderList.BugOk();
		if (bugok == 0) {
			shop.buyItems(orderList);
			// 防止回溯複製防止數量性Bug
			pc.saveInventory();
			// 防止回溯複製防止數量性Bug
		}

	/**
	 * 存入倉庫(個人)
	 **/
	}

	private void nomalWarehouseIn() {
		if (pc.getLevel() < 20) {
			pc.sendPackets("倉庫使用等級: 20");
			return;
		}
		if (MJShiftBattlePlayManager.is_shift_battle(pc))
			return;

		int objectId, count;
		L1Object object = null;
		L1ItemInstance item = null;
		Map<Integer, Integer> map = new HashMap<>();
//			System.out.println(size);
		for (int i = 0; i < size; i++) {
			tradable = true;
			objectId = readD();
			count = readD();
			item = pc.getInventory().getItem(objectId);
			
			if (map.containsKey(objectId)) {
				System.out.println("(複製Bug) 角色名:" + pc.getName() + " 物品物件ID:" + objectId);
				continue;
			}
			map.put(objectId, objectId);

/*			object = pc.getInventory().getItem(objectId);
			item = (L1ItemInstance) object;*/
			if (item == null) {
				break;
			}
			// 與資料庫連動的無法存入倉庫物品 NoShopAndWare
			int itemId = item.getItem().getItemId();
			if (!pc.isGm() && (NoShopAndWare.getInstance().isNoShopAndWare(itemId) || item.getEndTime() != null)) {//
				pc.sendPackets(String.format("%s無法使用於倉庫。", item.getLogName()));
			//	pc.sendPackets(new S_SystemMessage("該物品無法使用於倉庫。"));
				break;
			}
			long nowtime = System.currentTimeMillis();
			if (item.getItemdelay3() >= nowtime) {
				break;
			}
			if (objectId != item.getId()) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (!item.isStackable() && count != 1) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (count <= 0 || item.getCount() <= 0) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (count > item.getCount()) {
				count = item.getCount();
			}
			if (item.getCount() > 2000000000) {
				break;
			}
			if (count > 2000000000) {
				break;
			}
			/** 창고 맡기기 부분 버그 방지 **/

			if (item.getItem().getWareHouseLimitType().toInt() == 0) {
				tradable = false;
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
			} else if (item.getItem().getWareHouseLimitType().toInt() == 2) {
				if (item.getItem().getWareHouseLimitLevel() != 0) {
					if (item.getItem().getWareHouseLimitLevel() > item.getEnchantLevel()) {
						tradable = false;
						pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
					}
				}
			}

			/*
			 * if (!item.getItem().isTradable()) { tradable = false; // 1%0 無法丟棄或轉移給他人。 pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
			 *
			 * }
			 */

			/*
			 * if (item.get_Carving() != 0) { tradable = false; pc.sendPackets("刻印的物品無法使用於倉庫。"); }
			 */

			if (!MJCompanionInstanceCache.is_companion_oblivion(item.getId())) {
				tradable = false;
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
				break;
			}

			Object[] petlist = pc.getPetList().values().toArray();
			for (Object petObject : petlist) {
				if (petObject instanceof L1PetInstance) {
					L1PetInstance pet = (L1PetInstance) petObject;
					if (item.getId() == pet.getItemObjId()) {
						tradable = false;
						// 1%0 無法丟棄或轉移給他人。
						pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
						break;
					}
				}
			}

			L1DollInstance doll = pc.getMagicDoll();
			if (doll != null) {
				if (item.getId() == doll.getItemObjId()) {
					// 1%0 無法丟棄或轉移給他人。
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
					break;
				}
			}

			PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
			if (warehouse == null)
				break;

			if (warehouse.checkAddItemToWarehouse(item, count) == L1Inventory.SIZE_OVER) {
				pc.sendPackets(new S_ServerMessage(75));
				break;
			}

			if (item.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
				break;
			}

			if (tradable) {
				pc.getInventory().tradeItem(objectId, count, warehouse);
				pc.getLight().turnOnOffLight();
				/** 紀錄檔案保存 **/
				LoggerInstance.getInstance().addWarehouse(WarehouseType.Private, true, pc, item, count);
				// [存入倉庫:一般] 立方體 : 短劍(1)
   				/*if (count >= 500) {
   				} else {
   				}*/
			}
		}

		/**
		 * 取出倉庫物品(個人)
		 **/
	}
	private void nomalWarehouseOut() {
		if (pc.getLevel() < 20) {
			pc.sendPackets("倉庫使用等級: 20");
			return;
		}
		if (MJShiftBattlePlayManager.is_shift_battle(pc))
			return;
		if (!pc.getInventory().checkItem(L1ItemId.ADENA, 100 * size)) {
			pc.sendPackets("\f1金幣不足。");
			return;
		}
		
		PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
		if (warehouse == null) {
			return;
		}
		int objectId, count;
		L1ItemInstance item = null;
		Map<Integer, Integer> map = new HashMap<>();
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		ArrayList<Integer> list_count = new ArrayList<Integer>();
		
		for (int i = 0; i < size; i++) {
			objectId = readD();
			count = readD();
			item = warehouse.getItem(objectId);
			if (map.containsKey(objectId)) {
				System.out.println("(複製Bug) 角色名:" + pc.getName() + " 物品物件ID:" + objectId);
				continue;
			}
			map.put(objectId, objectId);

			/** 防止取出倉庫物品過程中的Bug **/
			if (item == null) {
				break;
			}
			if (objectId != item.getId()) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (!item.isStackable() && count != 1) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (count <= 0 || item.getCount() <= 0) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (count > item.getCount()) {
				count = item.getCount();
			}
			if (pc.getInventory().checkAddItem(item, count) != L1Inventory.OK) {
				pc.sendPackets("攜帶的物品太重，無法進行交易。");
				break;
			}
			itemList.add(item);
			list_count.add(count);
		}
		
		
		
		L1ItemInstance item1 = null;
		/** 防止取出倉庫物品過程中的Bug **/
		for (int i = 0; i < itemList.size(); i++) {
			item1 = itemList.get(i);
			int item_count = list_count.get(i);
			if (pc.getInventory().checkAddItem(item1, item_count) == L1Inventory.OK) {
				if (pc.getInventory().consumeItem(L1ItemId.ADENA, 100)) {
					warehouse.tradeItem(item1, item_count, pc.getInventory());
					/** 紀錄檔案保存 **/
					LoggerInstance.getInstance().addWarehouse(WarehouseType.Private, false, pc, item1, item_count);
				} else {
					pc.sendPackets(new S_ServerMessage(189)); // 金幣不足。
					break;
				}
			} else {
				pc.sendPackets(new S_ServerMessage(270)); // 攜帶的物品太重，無法進行交易。
			}
		}
		itemList.clear();
		list_count.clear();
	}


	/**
	 * 存入倉庫(血盟)
	 **/
	private void clanWarehouseIn() {
		if (pc.getClanid() <= 0) {
			pc.sendPackets(new S_ServerMessage(208));
			return;
		}
		if (pc.getLevel() < 20) {
			pc.sendPackets("倉庫使用等級: 20");
			return;
		}
		if (MJShiftBattlePlayManager.is_shift_battle(pc))
			return;
		int objectId, count;
		L1Object object = null;
		L1ItemInstance item = null;
		L1Clan clan = null;
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < size; i++) {
			tradable = true;
			objectId = readD();
			count = readD();
			item		= pc.getInventory().getItem(objectId);
			if (item == null) {
				break;
			}

			if (map.containsKey(objectId)) {
				System.out.println("(複製Bug) 角色名:" + pc.getName() + " 物品物件ID:" + objectId);
				continue;
			}

			map.put(objectId, objectId);
			clan = L1World.getInstance().getClan(pc.getClanid());

			// 無法存入倉庫的物品與資料庫連動 NoShopAndWare
			int itemId = item.getItem().getItemId();
			if (!pc.isGm() && (NoShopAndWare.getInstance().isNoShopAndWare(itemId) || item.getEndTime() != null)) {
				pc.sendPackets(String.format("%s無法使用倉庫。", item.getLogName()));
			//	pc.sendPackets(new S_SystemMessage("該物品無法使用倉庫。"));
				break;
			}
			long nowtime = System.currentTimeMillis();
			if (item.getItemdelay3() >= nowtime) {
				break;
			}

			if (objectId != item.getId()) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (!item.isStackable() && count != 1) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (count <= 0 || item.getCount() <= 0) {
				pc.sendPackets(new S_Disconnect());
				break;
			}

			if (item.getCount() > 2000000000) {
				break;
			}
			if (count > 2000000000) {
				break;
			}

			if (count > item.getCount()) {
				count = item.getCount();
			}
			/** 창고 맡기기 부분 버그 방지 **/

			if (item.getBless() >= 128) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
				break;
			}
			if (!MJCompanionInstanceCache.is_companion_oblivion(item.getId())) {
				tradable = false;
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
				break;
			}

			L1DollInstance doll = pc.getMagicDoll();
			if (doll != null) {
				if (item.getId() == doll.getItemObjId()) {
					// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
					break;
				}
			}

			if (clan != null) {

				if (!item.getItem().isTradable()) {
					tradable = false;
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
				}

				if (item.get_Carving() != 0) {
					tradable = false;
					pc.sendPackets("刻印的物品無法使用血盟倉庫。");
				}

				Object[] petlist = pc.getPetList().values().toArray();
				for (Object petObject : petlist) {
					if (petObject instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) petObject;
						if (item.getId() == pet.getItemObjId()) {
							tradable = false;
							// 1%0無法丟棄或交易給他人。
							pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
							break;
						}
					}
				}

				ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());

				if (clanWarehouse.checkAddItemToWarehouse(item, count) == L1Inventory.SIZE_OVER) {
					pc.sendPackets(new S_ServerMessage(75));
					break;
				}

				if (tradable) {
					pc.getInventory().tradeItem(objectId, count, clanWarehouse);
					pc.getLight().turnOnOffLight();
					history(pc, item, count, 1);
					/** 로그파일저장 **/
					LoggerInstance.getInstance().addWarehouse(WarehouseType.Clan, true, pc, item, count);
				}
			}
		}
		
	}

	/**
	 * 取出倉庫(血盟)
	 **/

	private void clanWarehouseOut() {
		System.out.println("血盟倉庫取出");
		if (pc.getLevel() < 20) {
			pc.sendPackets("倉庫使用等級: 20");
			return;
		}
		if (MJShiftBattlePlayManager.is_shift_battle(pc))
			return;

		// ** 倉庫使用等級修正為5級**//
		if (pc.getInventory().checkEnchantItem(40308, 0, 71)) {
			pc.sendPackets(new S_ServerMessage(189));
			return;
		}
		int objectId, count;

		L1ItemInstance item;

		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan == null) {
			return;
		}
		if (clan != null) {
			clan.deleteClanRetrieveUser(pc.getId());
		}
		ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		ArrayList<Integer> list_count = new ArrayList<Integer>();
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < size; i++) {
			objectId = readD();
			count = readD();
			if (map.containsKey(objectId)) {
				System.out.println("(複製Bug) 角色名:" + pc.getName() + " 物品物件ID:" + objectId);
				continue;
			}
			map.put(objectId, objectId);

			item = clanWarehouse.getItem(objectId);
			// ** 血盟倉庫取出部分防護 **//
			if (item == null) {
				break;
			}
			if (objectId != item.getId()) {
				pc.sendPackets(new S_Disconnect());
				break;
			}

			if (!item.isStackable() && count != 1) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (count <= 0 || item.getCount() <= 0 || item.getCount() > 2000000000) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (count >= item.getCount()) {
				count = item.getCount();
			}
			itemList.add(item);
			list_count.add(count);
			
		}
			
		L1ItemInstance item1 = null;
		for (int i = 0; i < itemList.size(); i++) {
			item1 = itemList.get(i);
			int item_count = list_count.get(i);
			// ** 血盟倉庫取出部分防護 **//
			if (pc.getInventory().checkAddItem(item1, item_count) == L1Inventory.OK) { // 容量
				if (pc.getInventory().consumeItem(L1ItemId.ADENA, 100 * size)) {
					clanWarehouse.tradeItem(item1, item_count, pc.getInventory());
					history(pc, item1, item_count, 2);
					//** 紀錄檔案保存 **/
					LoggerInstance.getInstance().addWarehouse(WarehouseType.Clan, false, pc, item1, item_count);
				} else {
					pc.sendPackets(new S_ServerMessage(189)); // 金幣不足。
					break;
				}
			} else {
				// 1 攜帶的物品太重，無法進行交易。
				pc.sendPackets(new S_ServerMessage(270));
				break;
			}
		}
		itemList.clear();
		list_count.clear();
		clanWarehouse.setWarehouseUsingChar(0, 0);
	}


	/**
	 * 從血盟倉庫取消操作，按下Cancel或ESC鍵
	 */
	private void clanWarehouseCancel(){
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan == null) {
			return;
		}
		if (MJShiftBattlePlayManager.is_shift_battle(pc)) {
			return;
		}
		if (pc.hasSkillEffect(L1SkillId.SetBuff)) {
			return;
		}
		clan.deleteClanRetrieveUser(pc.getId());
	}
	/**
	 * 個人妖精倉庫存入
	 **/
	private void elfWarehouseIn(){
		if (pc.getLevel() < 20) {
			pc.sendPackets("倉庫使用等級: 20");
			return;
		}
		if (MJShiftBattlePlayManager.is_shift_battle(pc))
			return;

		int objectId, count;
		L1Object object = null;
		L1ItemInstance item = null;
		for (int i = 0; i < size; i++) {
			tradable = true;
			objectId = readD();
			count = readD();
			item = pc.getInventory().getItem(objectId);
			if (item == null) {
				break;
			}
			// 창고불가아이템 디비연동 NoShopAndWare
			int itemId = item.getItem().getItemId();
			if (!pc.isGm() && NoShopAndWare.getInstance().isNoShopAndWare(itemId)) {//
				pc.sendPackets(new S_ServerMessage(189));
				break;
			}
			if (objectId != item.getId()) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (!item.isStackable() && count != 1) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (count <= 0 || item.getCount() <= 0) {
				pc.sendPackets(new S_Disconnect());
				break;
			}
			if (count > item.getCount()) {
				count = item.getCount();
			}
			if (item.getCount() > 2000000000) {
				break;
			}
			if (count > 2000000000) {
				break;
			}
			//** 倉庫存入部分防止Bug **/

			if (item.getItem().getWareHouseLimitType().toInt() == 0) {
				tradable = false;
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
			}

			/*
			 * if (!item.getItem().isTradable()) { tradable = false; pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); }
			 */

			/*
			 * if (item.get_Carving() != 0) { tradable = false; pc.sendPackets("刻印的物品無法使用倉庫。"); }
			 */

			if (!MJCompanionInstanceCache.is_companion_oblivion(item.getId())) {
				tradable = false;
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
				break;
			}

			Object[] petlist = pc.getPetList().values().toArray();
			for (Object petObject : petlist) {
				if (petObject instanceof L1PetInstance) {
					L1PetInstance pet = (L1PetInstance) petObject;
					if (item.getId() == pet.getItemObjId()) {
						tradable = false;
						// 1%0無法丟棄或交易給他人。
						pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
						break;
					}
				}
			}
			ElfWarehouse elfwarehouse = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName());
			if (elfwarehouse.checkAddItemToWarehouse(item, count) == L1Inventory.SIZE_OVER) {
				pc.sendPackets(new S_ServerMessage(75)); // 1對方攜帶的物品太多，無法進行交易。
				break;
			}
			if (tradable) {
				pc.getInventory().tradeItem(objectId, count, elfwarehouse);
				pc.getLight().turnOnOffLight();

				/** 紀錄檔案保存 **/
				LoggerInstance.getInstance().addWarehouse(WarehouseType.Elf, true, pc, item, count);
			}
		}
	}
	/**
	 * 個人妖精倉庫取出
	 **/
	private void elfWarehouseOut(){
		if (pc.getLevel() < 20) {
			pc.sendPackets("倉庫使用等級: 20");
			return;
		}
		if (MJShiftBattlePlayManager.is_shift_battle(pc))
			return;

		if (!pc.getInventory().checkItem(40494, 4 * size)) { // 米斯里爾
			pc.sendPackets("米斯里爾不足。");
			return;
		}
		ElfWarehouse elfwarehouse = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName());
		if (elfwarehouse == null) {
			return;
		}
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		ArrayList<Integer> list_count = new ArrayList<Integer>();
		int objectId, count;
		L1ItemInstance item = null;
		L1ItemInstance item1 = null;
		for (int i = 0; i < size; i++) {
			objectId = readD();
			count = readD();
			item = elfwarehouse.getItem(objectId);

			/** 창고 찾기 부분 버그 방지 **/
			if (item == null) {
				return;
			}
			if (objectId != item.getId()) {
				pc.sendPackets(new S_Disconnect());
				return;
			}
			if (!item.isStackable() && count != 1) {
				pc.sendPackets(new S_Disconnect());
				return;
			}
			if (count <= 0 || item.getCount() <= 0) {
				pc.sendPackets(new S_Disconnect());
				return;
			}
			if (count > item.getCount()) {
				count = item.getCount();
			}
			itemList.add(item);
			list_count.add(count);
		}
		
		
		for (int i = 0; i < itemList.size() ; i ++) {
			/** 창고 찾기 부분 버그 방지 **/
			item1 = itemList.get(i);
			int item_count = list_count.get(i);
			if (pc.getInventory().checkAddItem(item1, item_count) == L1Inventory.OK) {
				if (pc.getInventory().consumeItem(40494, 2)) {
					elfwarehouse.tradeItem(item1, item_count, pc.getInventory());
					/** 로그파일저장 **/
					LoggerInstance.getInstance().addWarehouse(WarehouseType.Elf, false, pc, item1, item_count);
				} else {
					pc.sendPackets(new S_ServerMessage(337, "$767"));
					break;
				}
			} else {
				pc.sendPackets(new S_ServerMessage(270));
				break;
			}
		}
	} 
	
	private void unk() {
	
	
	if (resultType == 10 && size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf")) {
		if (MJShiftBattlePlayManager.is_shift_battle(pc))
				return;

			int objectId, count;
			L1ItemInstance item = null;
			for (int i = 0; i < size; i++) {
				objectId = readD();
				count = readD();
				item = pc.getDwarfForPackageInventory().getItem(objectId);

				/** 창고 찾기 부분 버그 방지 **/
				if (item == null) {
					return;
				}

				if (objectId != item.getId()) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (!item.isStackable() && count != 1) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count <= 0 || item.getCount() <= 0) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count > item.getCount()) {
					count = item.getCount();
				}
				/** 창고 찾기 부분 버그 방지 **/

				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
					pc.getDwarfForPackageInventory().tradeItem(item, count, pc.getInventory());
				} else {
					pc.sendPackets(new S_ServerMessage(270));
					break;
				}
			}

		/**
		 * NPC 商店購買物品
		 **/
	}
	}

	private void npcPrivateShopBuy() {
		/*
		 * if(Config.Login.StandbyServer){ pc.sendPackets("開放等待狀態下無法進行此操作。"); return; }
		 */
		if ((pc.getLevel() >= Config.ServerAdSetting.USERNOSHOPLEVEL && pc.getClanid() <= 0) && !pc.isGm()) {
			pc.sendPackets("\fY等級 " + Config.ServerAdSetting.USERNOSHOPLEVEL + " 以上非血盟成員的角色無法使用商店");
			pc.sendPackets("\fY至少加入血盟是為了更順利的遊戲環境");
			return;
		}

		L1Shop shop = NpcShopTable.getInstance().get(npcId);
		if (shop == null)
			shop = NpcShopTable2.getInstance().get(npcId);
		if (shop == null)
			shop = NpcShopTable3.getInstance().get(npcId);

		L1ShopBuyOrderList orderList = shop.newBuyOrderList();
		int itemNumber;
		long itemcount;

		for (int i = 0; i < size; i++) {
			itemNumber = readD();
			itemcount = readD();
			if (itemcount <= 0) {
				return;
			}
			if (size >= 2) { // 無法同時購買不同物品，如果選擇了2個以上，
				pc.sendPackets(new S_SystemMessage("無法一次購買不同的物品。"));
				return;
			}
			if (pc.getMapId() == 800) {
				if (itemcount > 10000) {
					pc.sendPackets(new S_SystemMessage("最大購買數量：雜物(10000) / 裝備(1)"));
					return;
				}
			}
			orderList.add(itemNumber, (int) itemcount, pc);
			if (shop.getSellingItems().size() <= itemNumber) {
				pc.sendPackets("目前無法購買。");
				return;
			}

			if (BuyLimitSystem(pc, shop.getSellingItems().get(itemNumber).getItem().getItemId(), (int) itemcount, shop.getSellingItems().get(itemNumber).getPrice()))
				continue;

			if (shop.getSellingItems().get(itemNumber).get_count() == (int) itemcount)
				delete_NpcShopInfo(npcObjectId, itemNumber, resultType);

			if (shop.getSellingItems().get(itemNumber).get_count() > 1)
				update_NpcShopInfo((int) itemcount, npcObjectId, itemNumber, resultType);

			L1ShopItem shopItem = shop.getSellingItems().get(itemNumber);
			LoggerInstance.getInstance().addShop(String.format("+%d %s", shopItem.getEnchant(), shopItem.getItem().getName()), (int) itemcount, (long) shopItem.getPrice() * itemcount, npcName,
					pc.getName());
		}

		if (orderList.getList().size() == 0)
			return;

		int bugok = orderList.BugOk();
		if (bugok == 0) {
			shop.sellItems(pc, orderList);
			// 防止回溯複製和數量性Bug
			pc.saveInventory();
			// 防止回溯複製和數量性Bug
		}

		/**
		 * 個人商店購買物品
		 **/
	}

	private void privateShopBuy(){
		/*
		 * if(Config.Login.StandbyServer){ pc.sendPackets("開放等待狀態下無法進行此操作。"); return; }
		 */
		if ((pc.getLevel() >= Config.ServerAdSetting.USERNOSHOPLEVEL && pc.getClanid() <= 0) && !pc.isGm()) {
			pc.sendPackets("\fY等級 " + Config.ServerAdSetting.USERNOSHOPLEVEL + " 以上非血盟成員的角色無法使用商店");
			pc.sendPackets("\fY至少加入血盟是為了更順利的遊戲環境");
			return;
		}

		int order;
		int count;
		int price;
		int itemObjectId;
		int sellPrice;
		int sellCount;
		L1ItemInstance item;

		L1PcInstance targetPc = null;
		if (findObject instanceof L1PcInstance) {
			targetPc = (L1PcInstance) findObject;
		}
		if (targetPc == null) {
			return;
		}

		/** 2016.11.24 MJ 應用中心 市價 **/
		ArrayList<MJDShopItem> sells = targetPc.getSellings();
		MJDShopItem ditem = null;
		synchronized (sells) {
			// 因為售罄，瀏覽中的物品數量與列表數量不一致
			if (pc.getPartnersPrivateShopItemCount() != sells.size()) {
				return;
			}

			int[] orders = new int[size];
			int[] counts = new int[size];
			MJDShopItem[] dItems = new MJDShopItem[size];
			for (int i = 0; i < size; i++) {
				orders[i] = readD();
				counts[i] = readD();
				dItems[i] = sells.get(orders[i]);
			}

			for (int i = 0; i < size; i++) { // 預計購買的商品
				order = orders[i];
				count = counts[i];
				ditem = dItems[i];
				// order = readD();
				// count = readD();
				// ditem = sells.get(order);
				itemObjectId = ditem.objId;
				sellPrice = ditem.price;
				sellCount = ditem.count;
				item = targetPc.getInventory().getItem(itemObjectId);
				if (item == null)
					continue;

				long nowtime = System.currentTimeMillis();
				if (item.getItemdelay3() >= nowtime)
					break;

				if (count > sellCount)
					count = sellCount;

				if (count <= 0)
					continue;

				if (item.isEquipped()) {
					pc.sendPackets(new S_ServerMessage(905, ""));
					continue;
				}

				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
					for (int j = 0; j < count; j++) { // 오버플로우를 체크
						if (sellPrice * j > 2000000000 || sellPrice * j < 0) {
							pc.sendPackets(new S_ServerMessage(904, "2000000000"));
							return;
						}
					}
					price = count * sellPrice;

					/** 개인상점 버그방지 **/

					if (itemObjectId != item.getId()) {
						pc.sendPackets(new S_Disconnect());
						targetPc.sendPackets(new S_Disconnect());
						return;
					}
					if (!item.isStackable() && count != 1) {
						pc.sendPackets(new S_Disconnect());
						targetPc.sendPackets(new S_Disconnect());
						return;
					}
					if (count <= 0 || item.getCount() <= 0 || item.getCount() < count) {
						pc.sendPackets(new S_Disconnect());
						targetPc.sendPackets(new S_Disconnect());
						return;
					}
					if (count >= item.getCount()) {
						count = item.getCount();
					}

					if (item.isEquipped()) {
						pc.sendPackets(new S_SystemMessage("對方正在穿戴中的物品。"));
						return;
					}
					if (price <= 0 || price > 2000000000)
						return;
					/** 防止個人商店Bug **/

					if (pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
						try {
							L1ItemInstance adena = pc.getInventory().findItemId(L1ItemId.ADENA);
							if (targetPc != null && adena != null) {
								if (targetPc.getInventory().tradeItem(item, count, pc.getInventory()) == null) {
									return;
								}
								pc.getInventory().tradeItem(adena, price, targetPc.getInventory());

								//** 2016.11.24 MJ 應用中心 市價 **/
								targetPc.updateSellings(itemObjectId, count);
								//** 2016.11.24 MJ 應用中心 市價 **/

								String message = item.getItem().getName() + " (" + String.valueOf(count) + ")";
								targetPc.sendPackets(new S_ServerMessage(877, pc.getName(), message));
								// 你已經在 %0 售出 %1%o。

								writeLogbuyPrivateShop(pc, targetPc, item, count, price);
								try {
									pc.saveInventory();
									targetPc.saveInventory();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						pc.sendPackets(new S_ServerMessage(189)); // \f1아데나가 부족합니다.
						break;
					}
				} else {
					pc.sendPackets(new S_ServerMessage(270));
					break;
				}
			}
		}

		/**
		 * NPC 個人商店出售
		 **/
	}

	private void npcPrivateShopSell(){
		/*
		 * if(Config.Login.StandbyServer){ pc.sendPackets("開放等待狀態下無法進行此操作。"); return; }
		 */
		if ((pc.getLevel() >= Config.ServerAdSetting.USERNOSHOPLEVEL && pc.getClanid() <= 0) && !pc.isGm()) {
			pc.sendPackets("\fY等級 " + Config.ServerAdSetting.USERNOSHOPLEVEL + " 以上非血盟成員的角色無法使用商店");
			pc.sendPackets("\fY至少加入血盟是為了更順利的遊戲環境");
			return;
		}

			L1Shop shop = NpcShopTable.getInstance().get(npcId);
			if (shop == null)
				shop = NpcShopTable2.getInstance().get(npcId);
			if (shop == null)
				shop = NpcShopTable3.getInstance().get(npcId);
			L1ShopSellOrderList orderList = shop.newSellOrderList(pc);
			int itemNumber;
			long itemcount;

			for (int i = 0; i < size; i++) {
				itemNumber = readD();
				itemcount = readD();
				if (itemcount <= 0) {
					return;
				}
				orderList.add(itemNumber, (int) itemcount, pc);

				if (shop.getBuyingItems().get(itemNumber).get_count() == (int) itemcount)
					delete_NpcShopInfo(npcObjectId, itemNumber, resultType);

				if (shop.getBuyingItems().get(itemNumber).get_count() > 1)
					update_NpcShopInfo((int) itemcount, npcObjectId, itemNumber, resultType);

			}
			int bugok = orderList.BugOk();
			if (bugok == 0) {
				shop.buyItems(orderList);
				// 防止回溯複製和數量性Bug
				pc.saveInventory();
				// 防止回溯複製和數量性Bug
			}
	}
	/**
	 * 個人商店出售
	 **/
	private void privateShopSell(){
		/*
		 * if(Config.Login.StandbyServer){ pc.sendPackets("開放等待狀態下無法進行此操作。"); return; }
		 */
		if ((pc.getLevel() >= Config.ServerAdSetting.USERNOSHOPLEVEL && pc.getClanid() <= 0) && !pc.isGm()) {
			pc.sendPackets("\fY等級 " + Config.ServerAdSetting.USERNOSHOPLEVEL + " 以上非血盟成員的角色無法使用商店");
			pc.sendPackets("\fY至少加入血盟是為了更順利的遊戲環境");
			return;
		}
	
		int count;
		int order;
		int itemObjectId;
		L1ItemInstance item = null;
		int buyPrice;
		int buyCount;
	
		L1PcInstance targetPc = null;
		if (findObject instanceof L1PcInstance) {
			targetPc = (L1PcInstance) findObject;
		}
		if (targetPc == null) {
			return;
		}
	
		/** 2016.11.24 MJ 앱센터 시세 **/
		ArrayList<MJDShopItem> purs = targetPc.getPurchasings();
		MJDShopItem ditem = null;
		synchronized (purs) {
			for (int i = 0; i < size; i++) {
				itemObjectId = readD();
				count = readCH();
				order = readC();
				item = pc.getInventory().getItem(itemObjectId);
				if (item == null) {
					continue;
				}
	
				ditem = purs.get(order);
				buyPrice = ditem.price;
				buyCount = ditem.count;
				if (count > buyCount) {
					count = buyCount;
				}
				
				if (item.isEquipped()) {
					pc.sendPackets(new S_ServerMessage(905)); 
					continue;
				}
	
				if (targetPc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
					for (int j = 0; j < count; j++) { // 오버플로우를 체크
						if (buyPrice * j > 2000000000 || buyPrice * j < 0) {
							targetPc.sendPackets(new S_ServerMessage(904, "2000000000"));
							return;
						}
					}
					/** 버그 방지 **/
					if (itemObjectId != item.getId()) {
						pc.sendPackets(new S_Disconnect());
						targetPc.sendPackets(new S_Disconnect());
						return;
					}
	
					if (count >= item.getCount()) {
						count = item.getCount();
					}
	
					if (!item.isStackable() && count != 1)
						return;
					if (item.getCount() <= 0 || count <= 0)
						return;
					if (buyPrice * count <= 0 || buyPrice * count > 2000000000)
						return;
					// ** 개인상점 부분 비셔스 방어 **//
	
					if (targetPc.getInventory().checkItem(L1ItemId.ADENA, count * buyPrice)) {
						L1ItemInstance adena = targetPc.getInventory().findItemId(L1ItemId.ADENA);
						if (adena != null) {
							targetPc.getInventory().tradeItem(adena, count * buyPrice, pc.getInventory());
							pc.getInventory().tradeItem(item, count, targetPc.getInventory());
							/** 2016.11.24 MJ 앱센터 시세 **/
							targetPc.updatePurchasings(ditem.objId, count);
							/** 2016.11.24 MJ 앱센터 시세 **/
							
							try {
								pc.saveInventory();
								targetPc.saveInventory();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						targetPc.sendPackets(new S_ServerMessage(189)); // \f1金幣不足。
						break;
					}
				} else {
					pc.sendPackets(new S_ServerMessage(271)); // \f1對方持有的物品過多，無法進行交易。
					break;
				}
			}
		}
	}

// TODO 附加物品倉庫
		
	
	private void packageWarehousOut(){
			int objectId, count;
			L1ItemInstance item = null;
			SupplementaryService warehouse = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
			if (warehouse == null)
				return;

			if (size > 100)
				return;

			for (int i = 0; i < size; i++) {
				objectId = readD();
				count = readD();
				int invalid = readD();
				item = warehouse.getItem(objectId);

				if (item == null) {
					return;
				}
				if (invalid != 1) {
					System.out.println("附加物品倉庫 invalid:" + invalid + ", itemName:" + item.getName());
				}

				if (objectId != item.getId()) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (!item.isStackable() && count != 1) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count <= 0 || item.getCount() <= 0) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count > item.getCount()) {
					count = item.getCount();
				}

				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
					//** 項目時間標記 **/
					if (item.getItem().getItemId() >= 758 && item.getItem().getItemId() <= 761) {
						SetDeleteTime(item, 4320); // 3天
					} else if (item.getItem().getItemId() == 41922 || item.getItem().getItemId() == 41923 || item.getItem().getItemId() == 41924
							|| item.getItem().getItemId() == 41925 || item.getItem().getItemId() == 41925 || item.getItem().getItemId() == 900077 || item.getItem().getItemId() == 772
							|| item.getItem().getItemId() == 773 || item.getItem().getItemId() == 774 || item.getItem().getItemId() == 775) {
						SetDeleteTime(item, 1440); // 1天
					} else if (item.getItem().getItemId() == 210095 || (item.getItem().getItemId() == 2100950)) {
						SetDeleteTime(item, 180); // 3小時
					}
					warehouse.tradeItem(item, count, pc.getInventory());
					pc.saveInventory();
					//** 日誌文件保存 **/
					LoggerInstance.getInstance().addWarehouse(WarehouseType.Private, false, pc, item, count);
					// [倉庫查找:一般] 方盒 : 短劍(1)
					if (count >= 500) {
					} else {
					}
				} else {
					pc.sendPackets(new S_ServerMessage(270)); // \f1 가지고 있는 것이
					break;
				}
			}
	}

	@Override
	public String getType() {
		return "[C] C_Result";
	}

	private void writeLogbuyPrivateShop(L1PcInstance pc, L1PcInstance targetPc, L1ItemInstance item, int count, int price) {
		LoggerInstance.getInstance().개인상점구매(true, pc, targetPc, item, item.getCount());
	}

	private void history(L1PcInstance pc, L1ItemInstance item, int count, int i) {
		StringBuilder itemname = new StringBuilder();
		Connection con = null;
		PreparedStatement pstm = null;
		int clanid = pc.getClanid();
		String char_name = pc.getName();
		int item_enchant = item.getEnchantLevel();
		int elapsed_time = (int) (System.currentTimeMillis() / 1000);
		String type = null;
		if (i == 1) {
			type = "已存放。";
		} else {
			type = "已取出。";
		}
		if (item.getItem().getType2() != 0) {
			if (item_enchant >= 0) {
				itemname.append("+" + item_enchant + " ");
			} else {
				itemname.append(item_enchant + " ");
			}
		}
		itemname.append(item.getName());
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_warehousehistory SET id =?, clan_id = ?, char_name = ?, item_name = ?, item_count = ?, elapsed_time = ?, item_getorput = ?");
			pstm.setInt(1, IdFactory.getInstance().nextId());
			pstm.setInt(2, clanid);
			pstm.setString(3, char_name);
			pstm.setString(4, itemname.toString());
			pstm.setInt(5, count);
			pstm.setInt(6, elapsed_time);
			pstm.setString(7, type);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void SetDeleteTime(L1ItemInstance item, int minute) {
		Timestamp deleteTime = null;
		deleteTime = new Timestamp(System.currentTimeMillis() + (60000 * minute));
		item.setEndTime(deleteTime);
	}

	/**
	 * 比較世界上所有角色的帳戶，如果存在相同的帳戶，則返回 true，否則返回 false
	 *
	 * @return 如果存在相同帳戶，則返回 true
	 */


	public static class ItemCountInfo {
		public int itemObjectId;
		public int itemCount;

		ItemCountInfo(int itemObjectId, int itemCount) {
			this.itemObjectId = itemObjectId;
			this.itemCount = itemCount;
		}
	}

	public static void update_NpcShopInfo(int count, int npcObjID, int itemObjId, int type) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE character_shop SET count=count-" + count + " WHERE objid=? AND item_objid=? AND type=?");
			pstm.setInt(1, npcObjID);
			pstm.setInt(2, itemObjId);
			pstm.setInt(3, type);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	public static void delete_NpcShopInfo(int npcObjID, int itemObjId, int type) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("delete from character_shop where objid=? AND item_objid=? AND type=?");
			pstm.setInt(1, npcObjID);
			pstm.setInt(2, itemObjId);
			pstm.setInt(3, type);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean BuyLimitSystem(L1PcInstance pc, int itemId, int count, int price) {
		L1ItemInstance item = null;
		item = ItemTable.getInstance().createItem(itemId);
		BuyLimitSystem.L1BuyLimitItems limit_item = BuyLimitSystem.getInstance().getLimitBuyType(itemId);
		if (limit_item != null) {
			int limit_type = limit_item.isLimitType();
			int current_time = (int) (System.currentTimeMillis() / 1000);
			if (limit_type == 1 || limit_type == 2) {
				Timestamp start_time = new Timestamp(System.currentTimeMillis());
				start_time.setHours(limit_item.getStartTime());
				start_time.setMinutes(0);
				start_time.setSeconds(0);
				start_time.setNanos(0);

				Timestamp limit_time = new Timestamp(System.currentTimeMillis());
				limit_time.setHours(limit_item.getEndTime());
				limit_time.setMinutes(0);
				limit_time.setSeconds(0);
				limit_time.setNanos(0);

				if (current_time < start_time.getTime() / 1000 || current_time > limit_time.getTime() / 1000) {
					pc.sendPackets(5436);
					return true;
				}
			}

			if (limit_type == 1 || limit_type == 3 || limit_type == 5) {
				BuyLimitSystemAccount limit_account = BuyLimitSystemAccountTable.getInstance().getLimitTable(pc.getAccountName(), itemId);
				if (limit_account != null) {
					/*
					 * if (pc.getAccount().Ncoin_point < price) { pc.sendPackets("N幣不足。"); return true; }
					 */
					if (limit_account.getCount() <= 0) {
						pc.sendPackets(3460);
						return true;
					}
					if (count > limit_account.getCount()) {
						pc.sendPackets(3460);
						return true;
					}
					limit_account.setCount((int) (limit_account.getCount() - count));
					limit_account.setBuyTime(new Timestamp(System.currentTimeMillis()));
					if (limit_type == 1)
						BuyLimitSystemAccountTable.getInstance().updateLimitItem(pc.getAccountName(), item, limit_account.getCount(), false);
					else
						BuyLimitSystemAccountTable.getInstance().updateLimitItem(pc.getAccountName(), item, limit_account.getCount(), true);
				}
			} else if (limit_type == 2 || limit_type == 4 || limit_type == 6) {
				BuyLimitSystemCharacter limit_char = BuyLimitSystemCharacterTable.getInstance().getLimitTable(pc, itemId);
				if (limit_char != null) {
					/*
					 * if (pc.getAccount().Ncoin_point < price) { pc.sendPackets("N幣不足"); return true; }
					 */
					if (limit_char.getCount() <= 0) {
						pc.sendPackets(3460);
						return true;
					}
					if (count > limit_char.getCount()) {
						pc.sendPackets(3460);
						return true;
					}
					limit_char.setCount((int) (limit_char.getCount() - count));
					limit_char.setBuyTime(new Timestamp(System.currentTimeMillis()));
					if (limit_type == 2)
						BuyLimitSystemCharacterTable.getInstance().updateLimitItem(pc, item, limit_char.getCount(), false);
					else
						BuyLimitSystemCharacterTable.getInstance().updateLimitItem(pc, item, limit_char.getCount(), true);
				}
			}
		}
		return false;
	}
}