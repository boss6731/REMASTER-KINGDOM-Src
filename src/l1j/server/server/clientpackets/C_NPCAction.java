package l1j.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.STATUS_CURSE_BARLOG;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CURSE_YAHEE;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import MJShiftObject.MJShiftObjectManager;
import MJShiftObject.Battle.DomTower.MJDomTowerNpcActionInfo;
import l1j.server.Config;
import l1j.server.EventSystem.EventSystemInfo;
import l1j.server.EventSystem.EventSystemLoader;
import l1j.server.GameSystem.Colosseum.ColosseumTable;
import l1j.server.GameSystem.Colosseum.L1Colosseum;
import l1j.server.IndunSystem.Hadin.HadinSystem;
import l1j.server.IndunSystem.Ice.Demon.DemonSystem;
import l1j.server.IndunSystem.Ice.Queen.QueenSystem;
import l1j.server.IndunSystem.IceDungeon.IceDungeonSystem;
import l1j.server.IndunSystem.Luun_Secret.Luun_Secret_System;
import l1j.server.IndunSystem.Whale_Room.WhaleBossRoomSystem;
import l1j.server.IndunSystem.Whale_Room.WhaleTreasureRoomSystem;
import l1j.server.InfinityBattle.InfinityBattle;
import l1j.server.MJActionListener.ActionListener;
import l1j.server.MJActionListener.ActionListenerLoader;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJINNSystem.Loader.MJINNHelperLoader;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Builder.MJServerPacketBuilder;
import l1j.server.MJTemplate.MJClassesType.MJEClassesType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client_ArenaCo.SC_ARENA_GAME_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_PERSONAL_SHOP_ITEM_LIST_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_PERSONAL_SHOP_ITEM_LIST_NOTI.ePersonalShopType;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_WAREHOUSE_ITEM_LIST_NOTI;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventProvider;
import l1j.server.MJWarSystem.MJCastleWar;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.NpcShopCash.NpcShopCashTable;
import l1j.server.server.GameClient;
import l1j.server.server.Opcodes;
import l1j.server.server.Controller.BugRaceController;
import l1j.server.server.datatables.ClanStorageTable;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcActionTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetTypeTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.datatables.TownTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1HousekeeperInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtml;
import l1j.server.server.model.npc.action.L1NpcAction;
import l1j.server.server.model.shop.L1AssessedItem;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ApplyAuction;
import l1j.server.server.serverpackets.S_AuctionBoardRead;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ClanWareHouseHistory;
import l1j.server.server.serverpackets.S_ClassShop;
import l1j.server.server.serverpackets.S_CloseList;
import l1j.server.server.serverpackets.S_Deposit;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_Drawal;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_HouseMap;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_Karma;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NoSell;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_PetCtrlMenu;
import l1j.server.server.serverpackets.S_PetList;
import l1j.server.server.serverpackets.S_PremiumShopSellList;
import l1j.server.server.serverpackets.S_PsyUml;
import l1j.server.server.serverpackets.S_RetrieveElfList;
import l1j.server.server.serverpackets.S_RetrievePackageList;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SelectTarget;
import l1j.server.server.serverpackets.S_SellHouse;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_ShopBuyList;
import l1j.server.server.serverpackets.S_ShopSellList;
import l1j.server.server.serverpackets.S_ShopSellList.MinigameSellListException;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TaxRate;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1PetType;
import l1j.server.server.templates.L1Town;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

public class C_NPCAction extends ClientBasePacket {

	private static final String C_NPC_ACTION = "[C] C_NPCAction";
	private static Random _random = new Random(System.nanoTime());

	public C_NPCAction(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		int objid = readD();
		String s = readS();
		String s2 = null;
		if (s.equalsIgnoreCase("deadTrans") || s.equalsIgnoreCase("pvpSet") || s.equalsIgnoreCase("ShowHPMPRecovery") || s.equalsIgnoreCase("showDisableEffectIcon") || s.equalsIgnoreCase("showDungeonTimeLimit")) {
			return;
		}
		if (s.equalsIgnoreCase("select") || s.equalsIgnoreCase("map") || s.equalsIgnoreCase("apply")) {
			s2 = readS();
		} else if (s.equalsIgnoreCase("ent")) {
			L1Object obj = L1World.getInstance().findObject(objid);
			if (obj != null && obj instanceof L1NpcInstance) {
				final int PET_MATCH_MANAGER = 80088;
				if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == PET_MATCH_MANAGER) {
					s2 = readS();
				}
			}
		}
		int[] materials = null;
		int[] counts = null;
		int[] createitem = null;
		int[] createcount = null;

		String htmlid = null;
		String success_htmlid = null;
		String failure_htmlid = null;
		String[] htmldata = null;

		L1PcInstance pc = client.getActiveChar();
		if (pc == null || pc.isGhost() || pc.isPrivateShop() || pc.is無人商店())
			return;



		L1PcInstance target;
		L1Object obj = L1World.getInstance().findObject(objid);
		if (pc.isGm()) {
			pc.sendPackets("\\f3(isGm)Action_ID : " + s);
//			System.out.println("(isGm)Action_ID : " + s);
		}

		if (obj != null) {
			if (obj instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				int difflocx = Math.abs(pc.getX() - npc.getX());
				int difflocy = Math.abs(pc.getY() - npc.getY());
				if (!(obj instanceof L1PetInstance) && !(obj instanceof L1SummonInstance)) {
					if (!npc.is_sub_npc()) {
						if (npc.getNpcTemplate().get_npcId() != Config.ServerAdSetting.TIME_COLLECTION_NPC_IDS[0]) {
							if (difflocx > 18 || difflocy > 18) {
								return;
							}
						}
					}
				}
				npc.onFinalAction(pc, s);
			} else if (obj instanceof L1PcInstance) {
				target = (L1PcInstance) obj;
				if (s.matches("[0-9]+")) {
					summonMonster(target, s);
				} else {
					if (target.isMagicItem()) { // 轉型法術書
						L1PolyMorph.MagicBookPoly(target, s, 1200);
						target.setMagicItem(false);
					} else {
						L1PolyMorph.handleCommands(target, s);
					}
				}
				return;
			}
		} else {
			return;
		}

		// TODO NPC點擊時延遲開始
		Long latestClickMillis = pc.attribute().getNotExistsNew(L1PcInstance.latestNpcClickMillis).get();
		long currentMillis = System.currentTimeMillis();
		if (latestClickMillis != null) {
			long diff = currentMillis - latestClickMillis.longValue();
			if (diff < 500) { // 0.5秒
				return;
			}
		}
		pc.attribute().get(L1PcInstance.latestNpcClickMillis).set(currentMillis);
		// TODO NPC點擊時延遲結束

		if (obj.instanceOf(MJL1Type.L1TYPE_NPC)) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			MJObjectEventProvider.provider().npcEventFactory().fireNpcTalked(pc, npc, s);
			if (MJINNHelperLoader.getInstance().onAction(npc, pc, s))
				return;

			EventSystemInfo EventInfo = EventSystemLoader.getInstance().getEventSystemInfo();
			if (EventInfo != null) {
				if (EventInfo.get_npc_id() == npc.getNpcId()) {
					if (s.equalsIgnoreCase(EventInfo.get_action_name())) {
						if (EventInfo.is_event()) {
							int x = EventInfo.get_teleport_x();
							int y = EventInfo.get_teleport_y();
							int mapid = EventInfo.get_event_map_id();
							pc.start_teleport(x, y, mapid, pc.getHeading(), 18339, true);
							return;
						} else {
							pc.sendPackets("當前沒有進行中的活動。");
							return;
						}
					}
				}
			}
		}
	}

	L1NpcAction action = NpcActionTable.getInstance().get(s, pc, obj);
		if (action != null) {
		L1NpcHtml result = action.execute(s, pc, obj, readByte());
		if (pc.isGm()){
//				System.out.println(result);
		}
		return;
	}

if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 8502072) {
		if (pc.getLevel() >= 60) {
			if (s.equalsIgnoreCase("32")) {
				boolean check = MJCastleWarBusiness.getInstance().isNowWar(4);
				if (check) {
					int newX = 32595 + 3;
					int newY = 33165 + 3;
					short mapId = 4;
					pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, false);
				} else {
					pc.sendPackets("沒有進行中的攻城戰。");
				}
			} else {
				pc.sendPackets("沒有進行中的攻城戰。");
			}
		} else {
			pc.sendPackets(4900);
		}
	}
// 檢查NPC是否為指定ID（8502072），並處理玩家等級大於60且對應指令（"32"）的情況

if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 130004) {
		if (s.equalsIgnoreCase("expel")) {
			if (pc.getInventory().checkItem(30001881)) {
				for (L1PcInstance player : L1World.getInstance().getVisiblePlayers(pc.getMapId())) {
					if (!player.getInventory().checkItem(30001881)) {
						player.start_teleport(33445, 32796, 4, player.getHeading(), 18339, true);
					}
				}
			}
		}
// 檢查NPC是否為指定ID（130004），並處理指令（"expel"）的情況

		if (s.equalsIgnoreCase("name")) {
			pc.sendPackets("此宅邸無法轉讓。");
			return;
		}
// 處理指令（"name"）的情況，告知玩家此宅邸無法轉讓
	}


		if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 73201280) {
		if (pc.getLevel() >= 85) {
			if (s.equalsIgnoreCase("a")) {
				if (InfinityBattle.getInstance().isReady()) {
					InfinityBattle.getInstance().addTeamMembers(pc);
				} else {
					htmlid = "sungun1";
				}
			}
		} else {
			htmlid = "sungun1";
		}
	}
		if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 21024) {
		if (s.equalsIgnoreCase("a")) {
			pc.start_teleport(33440, 32805, 4, pc.getHeading(), 18339, true);
		}
	}


		if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50000264){
		if (s.equalsIgnoreCase("d")){
			pc.getInventory().consumeItem(40308, 25000);
			pc.start_teleport(32803, 32870, 63, pc.getHeading(), 18339, true);
			return;
		}
	}

		if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7800244){
		if (s.equalsIgnoreCase("a")){
			((L1NpcInstance)obj).sendPackets(new S_DoActionGFX(((L1NpcInstance)obj).getId(), 9), true);
			Broadcaster.broadcastPacket((L1NpcInstance)obj, new S_DoActionGFX(((L1NpcInstance)obj).getId(), 9));
			whaleRoom(pc);
			return;
		}
	}

	// TODO 세금 확인 액션
	/** 켄트 **/
		if (s.equalsIgnoreCase("andyn3")) {
		MJServerPacketBuilder b = new MJServerPacketBuilder(16);
		pc.sendPackets(b.addC(Opcodes.S_HYPERTEXT).addD(obj.getId()).addS("andyn3").addH(0x01).addH(1).addS(Integer.toString(MJCastleWarBusiness.getInstance().getTaxRate(1))).build(true));
		b.close();
		b.dispose();
		/** 우드벡 **/
	} else if (s.equalsIgnoreCase("vola3")) {
		MJServerPacketBuilder b = new MJServerPacketBuilder(16);
		pc.sendPackets(b.addC(Opcodes.S_HYPERTEXT).addD(obj.getId()).addS("vola3").addH(0x01).addH(1).addS(Integer.toString(MJCastleWarBusiness.getInstance().getTaxRate(3))).build(true));
		b.close();
		b.dispose();
		/** 기란 **/
	} else if (s.equalsIgnoreCase("s_merchant3")) {
		MJServerPacketBuilder b = new MJServerPacketBuilder(16);
		pc.sendPackets(b.addC(Opcodes.S_HYPERTEXT).addD(obj.getId()).addS("s_merchant3").addH(0x01).addH(1).addS(Integer.toString(MJCastleWarBusiness.getInstance().getTaxRate(4))).build(true));
		b.close();
		b.dispose();
	} else if (s.equalsIgnoreCase("vergil3")) {
		MJServerPacketBuilder b = new MJServerPacketBuilder(16);
		pc.sendPackets(b.addC(Opcodes.S_HYPERTEXT).addD(obj.getId()).addS("vergil3").addH(0x01).addH(1).addS(Integer.toString(MJCastleWarBusiness.getInstance().getTaxRate(4))).build(true));
		b.close();
		b.dispose();
		/** 하이네 **/
	} else if (s.equalsIgnoreCase("shivan3")) {
		MJServerPacketBuilder b = new MJServerPacketBuilder(16);
		pc.sendPackets(b.addC(Opcodes.S_HYPERTEXT).addD(obj.getId()).addS("shivan3").addH(0x01).addH(1).addS(Integer.toString(MJCastleWarBusiness.getInstance().getTaxRate(5))).build(true));
		b.close();
		b.dispose();
		/** 웰던 **/
	} else if (s.equalsIgnoreCase("ralf6")) {
		MJServerPacketBuilder b = new MJServerPacketBuilder(16);
		pc.sendPackets(b.addC(Opcodes.S_HYPERTEXT).addD(obj.getId()).addS("ralf6").addH(0x01).addH(1).addS(Integer.toString(MJCastleWarBusiness.getInstance().getTaxRate(6))).build(true));
		b.close();
		b.dispose();
		/** 아덴 **/
	} else if (s.equalsIgnoreCase("defman3")) {
		MJServerPacketBuilder b = new MJServerPacketBuilder(16);
		pc.sendPackets(b.addC(Opcodes.S_HYPERTEXT).addD(obj.getId()).addS("defman3").addH(0x01).addH(1).addS(Integer.toString(MJCastleWarBusiness.getInstance().getTaxRate(6))).build(true));
		b.close();
		b.dispose();
	} else if (s.equalsIgnoreCase("fega3")) {
		MJServerPacketBuilder b = new MJServerPacketBuilder(16);
		pc.sendPackets(b.addC(Opcodes.S_HYPERTEXT).addD(obj.getId()).addS("fega3").addH(0x01).addH(1).addS(Integer.toString(MJCastleWarBusiness.getInstance().getTaxRate(6))).build(true));
		b.close();
		b.dispose();
	}

		if (s.equalsIgnoreCase("mayer3")) {
		htmlid = "mayer3";
	}
	/**악섬 게렝**/

		if (s.equalsIgnoreCase("buy")) {
		int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();

		if (pc.getInventory().getWeight100() > 90 && !pc.is_top_ranker()) {
			pc.sendPackets(new S_ServerMessage(3561));
			return;
		}
		if (npcid == 20000){//바르가
			MJObjectEventProvider.provider().pcEventFactory().fireBarga(pc);
		}

		L1NpcInstance npc = (L1NpcInstance) obj;
		if (isNpcSellOnly(npc)) {
			return;
		}



		NpcShopCashTable.L1CashType pshop = NpcShopCashTable.getInstance().getNpcCashType(npcid);
		if (pshop != null) {
			if (pshop.isShopType().equalsIgnoreCase("NormalShop")) {
				pc.sendPackets(new S_ShopSellList(objid, pc));
				return;
			} else if (pshop.isShopType().equalsIgnoreCase("PrivateShop")) {
				SC_PERSONAL_SHOP_ITEM_LIST_NOTI.do_send_for_npc(pc, objid, ePersonalShopType.TRADE_BUY);
				return;
			} else if (pshop.isShopType().equalsIgnoreCase("PremiumShop")) {
				pc.sendPackets(new S_PremiumShopSellList(objid, pc));
				return;
			}
		}

		if (npcid == 80104) {
			if (!pc.isCrown()) {
				htmlid = "incence";
				pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
				return;
			}
		} else if (npcid == 8500151) {
			htmlid = "incence";
			pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
			return;
		}
		if (npcid == 7320121 || npcid == 7320085 || npcid == 2020561 || npcid == 8502049) {
			pc.sendPackets(new S_ClassShop(pc, objid));
			return;
		}
		try {
			pc.sendPackets(new S_ShopSellList(objid, pc));
		} catch (MinigameSellListException e) {
			// e.printStackTrace();
		}
	} else if (s.equalsIgnoreCase("sell")) {
		int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
		if (npcid == 70523 || npcid == 70805) {
			htmlid = "ladar2";
		} else if (npcid == 70674) {
			htmlid = "incence";
		} else if (npcid == 70537 || npcid == 70807) {
			htmlid = "farlin2";
		} else if (npcid == 70525 || npcid == 70804) {
			htmlid = "lien2";
		} else if (npcid == 50527 || npcid == 50505 || npcid == 50519 || npcid == 50545 || npcid == 50531 || npcid == 50529 || npcid == 50516 || npcid == 50538 || npcid == 50518 || npcid == 50509 || npcid == 50536 || npcid == 50520 || npcid == 50543
				|| npcid == 50526 || npcid == 50512 || npcid == 50510 || npcid == 50504 || npcid == 50525 || npcid == 50534 || npcid == 50540 || npcid == 50515 || npcid == 50513 || npcid == 50528 || npcid == 50533 || npcid == 50542 || npcid == 50511
				|| npcid == 50501 || npcid == 50503 || npcid == 50508 || npcid == 50514 || npcid == 50532 || npcid == 50544 || npcid == 50524 || npcid == 50535 || npcid == 50521 || npcid == 50517 || npcid == 50537 || npcid == 50539 || npcid == 50507
				|| npcid == 50530 || npcid == 50502 || npcid == 50506 || npcid == 50522 || npcid == 50541 || npcid == 50523 || npcid == 50620 || npcid == 50623 || npcid == 50619 || npcid == 50621 || npcid == 50622 || npcid == 50624 || npcid == 50617
				|| npcid == 50614 || npcid == 50618 || npcid == 50616 || npcid == 50615 || npcid == 50626 || npcid == 50627 || npcid == 50628 || npcid == 50629 || npcid == 50630 || npcid == 50631) {
			String sellHouseMessage = sellHouse(pc, objid, npcid);
			if (sellHouseMessage != null) {
				htmlid = sellHouseMessage;
			}
		} else {
			if (npcid == BugRaceController.RACE_SELLER_NPCID) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (BugRaceController.getInstance().sellings()) {
					try {
						pc.sendPackets(new S_ShopBuyList(objid, pc));
					} catch (Exception e) {
					}
				} else {
					pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "maeno3"));
				}
				return;
			}



			//TODO CMD視窗無物品出售時的錯誤輸出及異常處理 NPC銷售清單可能會出現Bug，所以單獨將某些NPC添加到列表中（NPC購買） 其他訊息僅輸出，可能是一個bug，所以一定要檢查一次，如果沒有問題就添加！
			if (/*npcid == 170041 ||*/ npcid == 370041 || npcid == 70064 || npcid == 7200002 || npcid == 7200047) {
				if (obj instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1Shop shop = ShopTable.getInstance().get(npc.getNpcId());
					if (shop == null) {
						pc.sendPackets(new S_NoSell(npc));
						return;
					}
					List<L1AssessedItem> assessedItems = shop.assessItems(pc.getInventory());
					if (assessedItems.isEmpty()) {
						pc.sendPackets(new S_NoSell(npc));
						return;
					} else {
						pc.sendPackets(new S_ShopBuyList(objid, pc));
					}
				}
				return;
			}
			try {
				pc.sendPackets(new S_ShopBuyList(objid, pc));
			} catch (IllegalArgumentException e) {
			}
		}
	} else if (s.equalsIgnoreCase("history")) {
		if (pc.getClanid() > 0)
			pc.sendPackets(new S_ClanWareHouseHistory(pc));
		else
			pc.sendPackets(new S_SystemMessage("請加入血盟後再使用。"));
		// 如果玩家尚未加入血盟，則發送系統消息通知玩家加入血盟

	} else if (s.equalsIgnoreCase("retrieve")) {

		if (pc.getAccount().getGamePassword() != 0) {
			pc.sendPackets(new S_ServerMessage());
			return;
		}
		// 如果玩家帳戶設置了遊戲密碼，則發送服務器消息並返回

		if (pc.getInventory().getSize() > Config.ServerAdSetting.Inventory_Count) {
			pc.sendPackets("\f2當你的物品欄中的物品數量少於 " + Config.ServerAdSetting.Inventory_Count + " 個時，才可以使用。當前: (\aG" + pc.getInventory().getSize() + "/\aA" + Config.ServerAdSetting.Inventory_Count + ")\f2 持有中。");
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "當你的物品欄中的物品數量少於 " + Config.ServerAdSetting.Inventory_Count + " 個時，才可以使用。當前: (\aG" + pc.getInventory().getSize() + "/\aA" + Config.ServerAdSetting.Inventory_Count + ")\f2 持有中。"));
			return;
		}
		// 如果玩家的物品欄數量超過了配置的最大值，則發送提示消息並返回

		if (pc.getLevel() >= 5) {
			SC_WAREHOUSE_ITEM_LIST_NOTI.send_user_warehouse_items(pc, objid);
			return;
		}
		// 如果玩家等級大於或等於5，則發送倉庫物品列表

	} else if (s.equalsIgnoreCase("retrieve-elven")) {
		if (pc.getInventory().getSize() > Config.ServerAdSetting.Inventory_Count) {
			pc.sendPackets("\f2當你的物品欄中的物品數量少於 " + Config.ServerAdSetting.Inventory_Count + " 個時，才可以使用。當前: (\aG" + pc.getInventory().getSize() + "/\aA" + Config.ServerAdSetting.Inventory_Count + ")\f2 持有中。");
			pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "當你的物品欄中的物品數量少於 " + Config.ServerAdSetting.Inventory_Count + " 個時，才可以使用。當前: (\aG" + pc.getInventory().getSize() + "/\aA" + Config.ServerAdSetting.Inventory_Count + ")\f2 持有中。"));
			return;
		}
		// 如果玩家的物品欄數量超過了配置的最大值，則發送提示消息並返回
	}
			if (pc.getLevel() >= 5 && pc.isElf()) {
		S_RetrieveElfList rpl = new S_RetrieveElfList(objid, pc);
		if (rpl.NonValue)
			htmlid = "noitemret";
		else
			pc.sendPackets(rpl);
		rpl = null;
	}
} else if (s.equalsIgnoreCase("retrieve-aib")) {
		S_RetrievePackageList rpl = new S_RetrievePackageList(objid, pc);
		if (rpl.NonValue)
		htmlid = "noitemret";
		else
		pc.sendPackets(rpl);
		rpl = null;
		} else if (s.equalsIgnoreCase("retrieve-char")) { // 專用倉庫
		if (pc.getInventory().getSize() > Config.ServerAdSetting.Inventory_Count) {
		pc.sendPackets("\f2當你的物品欄中的物品數量少於 " + Config.ServerAdSetting.Inventory_Count + " 個時，才可以使用。當前: (\aG" + pc.getInventory().getSize() + "/\aA" + Config.ServerAdSetting.Inventory_Count + ")\f2 持有中。");
		pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "當你的物品欄中的物品數量少於 " + Config.ServerAdSetting.Inventory_Count + " 個時，才可以使用。當前: (\aG" + pc.getInventory().getSize() + "/\aA" + Config.ServerAdSetting.Inventory_Count + ")\f2 持有中。"));
		return;
		}
		// 如果玩家的物品欄數量超過了配置的最大值，則發送提示消息並返回

		if (pc.get_SpecialSize() == 0) {
		pc.sendPackets(new S_ServerMessage(1623));
		return;
		}
		// 如果玩家的特殊空間大小為0，則發送服務器消息（消息代碼1623）並返回

		} else if (s.equalsIgnoreCase("retrieve-pledge")) {
		if (pc.getInventory().getSize() > Config.ServerAdSetting.Inventory_Count) {
		pc.sendPackets("\f2當你的物品欄中的物品數量少於 " + Config.ServerAdSetting.Inventory_Count + " 個時，才可以使用。當前: (\aG" + pc.getInventory().getSize() + "/\aA" + Config.ServerAdSetting.Inventory_Count + ")\f2 持有中。");
		pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "當你的物品欄中的物品數量少於 " + Config.ServerAdSetting.Inventory_Count + " 個時，才可以使用。當前: (\aG" + pc.getInventory().getSize() + "/\aA" + Config.ServerAdSetting.Inventory_Count + ")\f2 持有中。"));
		return;
		}
		// 如果玩家的物品欄數量超過了配置的最大值，則發送提示消息並返回

		if (pc.getLevel() >= 5) {
		if (pc.getClanid() == 0) {
		pc.sendPackets(new S_SystemMessage("你必須加入血盟才能使用血盟倉庫。"));
		return;
		}
		// 如果玩家等級大於或等於5，且沒有加入血盟，則發送系統消息通知玩家必須加入血盟才能使用血盟倉庫

		if (!ClanStorageTable.getInstance().is_ClanStorageUse(pc, pc.getName())) {
		pc.sendPackets(new S_ServerMessage(728));
		return;
		}
		// 如果玩家沒有權限使用血盟倉庫，則發送服務器消息（消息代碼728）並返回
		}

/*				if (pc.getClanRank() == L1Clan.수련) {
					pc.sendPackets(new S_ServerMessage(728));
					return;
				}*/
		SC_WAREHOUSE_ITEM_LIST_NOTI.send_user_pledge_warehouse_items(pc, objid);
		return;
		}
		} else if (s.equalsIgnoreCase("retrieve-pledge")) {
		if (!ClanStorageTable.getInstance().is_ClanStorageUse(pc, pc.getName())) {
		pc.sendPackets(new S_ServerMessage(728));
		return;
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7210041) {
//			System.out.println(s+"+"+"패킷확인");
		if (s.startsWith("a_soul") || s.startsWith("a_soul_center")) {
		if (!pc.isPcBuff()) {
		pc.getInventory().consumeItem(41921, 3);
		}
		} else {
		if (!pc.isPcBuff()) {
		pc.getInventory().consumeItem(41921, 4);
		}
		}
		if (s.startsWith("a_giant")) {
//				System.out.println("패킷확인");
		pc.start_teleport(32767, 32991, 624, pc.getHeading(), 18339, true);
		} else if (s.startsWith("a_soul")) {
		pc.start_teleport(32902, 32811, 430, pc.getHeading(), 18339, true);
		} else if (s.startsWith("a_soul_center")) {
		pc.start_teleport(32869, 32876, 430, pc.getHeading(), 18339, true);
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 200007) {// 오류
		if (s.equalsIgnoreCase("1")) {
		htmlid = "camus5";
		} else if (s.equalsIgnoreCase("2")) {
		if (pc.getInventory().consumeItem(L1ItemId.ADENA, 10000)) {
		int[] prismIds = { 210106, 210107, 210108, 210109 };
		int idx = _random.nextInt(prismIds.length);
		pc.getInventory().storeItem(prismIds[idx], 1);
		L1ItemInstance item = ItemTable.getInstance().createItem(prismIds[idx]);
		item.setCount(1);
		pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
		htmlid = "camus2";
		} else {
		htmlid = "camus3";
		}
		}
		// 결혼반지 충전
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70702) {
		if (s.equalsIgnoreCase("chg")) {
		L1ItemInstance ring = null;
		if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 1000)) {
		pc.sendPackets(189);
		return;
		}
		if (pc.getPartnerId() != 0) {
		ring = pc.getInventory().findItemId(40906);

		if (ring == null) {
		ring = pc.getInventory().findItemId(40905);
		}
		if (ring == null) {
		ring = pc.getInventory().findItemId(40904);
		}
		if (ring == null) {
		ring = pc.getInventory().findItemId(40903);
		}

		if (ring != null) {
		ring.setChargeCount(ring.getItem().getMaxChargeCount());
		pc.getInventory().updateItem(ring, L1PcInventory.COL_CHARGE_COUNT);
		pc.sendPackets(791);
		}
		}
		pc.sendPackets(457);
		}

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 900106) {// 달나라
		if (s.equalsIgnoreCase("0")) {
		if (pc.getLevel() > 30) {
		if (!pc.getInventory().checkItem(22253, 1)) {
		if (pc.getInventory().checkItem(40308, 5000)) {
		pc.getInventory().consumeItem(40308, 5000);
		pc.getInventory().storeItem(22253, 1);
		htmlid = "rabbita5";
		} else {
		htmlid = "rabbita4";
		}
		} else {
		htmlid = "rabbita3";
		}
		} else {
		htmlid = "rabbita2";
		}
		}
		} else if (s.equalsIgnoreCase("get")) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70099 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70796) {
		L1ItemInstance item = pc.getInventory().storeItem(20081, 1);
		String npcName = npc.getNpcTemplate().get_name();
		String itemName = item.getItem().getName();
		pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
		pc.getQuest().set_end(L1Quest.QUEST_OILSKINMANT);
		htmlid = "";
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70528 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70546 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70567
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70594 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70654 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70748
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70774 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70799 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70815
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70860) {

		if (pc.getHomeTownId() > 0) {

		} else {

		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 8500147 && s.equalsIgnoreCase("B")) {
		MJCompanionInstance companion = pc.get_companion();
		if (companion == null) {
		pc.sendPackets("請在召喚寵物後再嘗試。");
		return;
		}
		// 獲取玩家的寵物對象，如果寵物為空（即沒有召喚寵物），則發送提示消息並返回

		companion.do_traning(false);
		return;
		// 執行寵物訓練方法並返回

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 8500150) {
		MJCompanionInstance companion = pc.get_companion();
		if (companion == null) {
		pc.sendPackets("請在召喚寵物後再嘗試。");
		return;
		}
		// 如果NPC的ID為8500150，則同樣檢查並獲取玩家的寵物對象，如果寵物為空，則發送提示消息並返回
		}
		if (s.equalsIgnoreCase("a")) {
		companion.do_restore_keep_exp(false);
		return;
		} else if (s.equalsIgnoreCase("b")) {
		companion.do_oblivion(false);
		}
		} else if (s.equalsIgnoreCase("fix")) {

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70012 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70019 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70031
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70054 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70065 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70070
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70075 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70084 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70096) {
		if (s.equalsIgnoreCase("room")) {
		if (pc.getInventory().checkItem(40312))
		htmlid = "inn5";
		else {
		L1ItemInstance item = pc.getInventory().findItemId(40308);
		if (item != null && item.getCount() >= 300) {
		materials = (new int[] { 40308 });
		counts = (new int[] { 300 });
		createitem = (new int[] { 40312 });
		createcount = (new int[] { 1 });
		htmlid = "inn4";
		} else
		htmlid = "inn3";
		}
		} else if (s.equalsIgnoreCase("room")) {
		if (pc.getInventory().checkItem(40312))
		htmlid = "inn5";
		else {
		L1ItemInstance item = pc.getInventory().findItemId(40308);
		if (item != null && item.getCount() >= 600) {
		materials = (new int[] { 40308 });
		counts = (new int[] { 600 });
		createitem = (new int[] { 40312 });
		createcount = (new int[] { 1 });
		htmlid = "inn4";
		} else
		htmlid = "inn3";
		}
		} else if (s.equalsIgnoreCase("room")) {
		int k1 = 0;
		try {
		k1 = pc.getLawful();
		} catch (Exception exception) {
		}
		if (k1 >= 0) {
		htmlid = "inn2";
		htmldata = (new String[] { "旅館老闆", "300" });
		} else {
		htmlid = "inn1";
		}
		} else if (s.equalsIgnoreCase("hall")) {
		int k1 = 0;
		int c1 = 0;
		try {
		k1 = pc.getLawful();
		c1 = pc.getClassId();
		} catch (Exception exception1) {
		}
		if (k1 >= 0) {
		if (c1 == 0 || c1 == 1) {
		htmlid = "inn4";
		htmldata = (new String[] { "旅館老闆", "600" });
		} else {
		htmlid = "inn10";
		}
		} else {
		htmlid = "inn11";
		}
		} else if (s.equalsIgnoreCase("return")) {
		if (pc.getInventory().checkItem(40312)) {
		int ct = pc.getInventory().findItemId(40312).getCount();
		int cash = ct * 60;
		materials = (new int[] { 40312 });
		counts = (new int[] { ct });
		createitem = (new int[] { 40308 });
		createcount = (new int[] { cash });
		htmlid = "inn20";
		String count = Integer.toString(cash);
		htmldata = (new String[] { "旅館老闆", count });
		} else if (pc.getInventory().checkItem(40312)) {
		int ct = pc.getInventory().findItemId(40312).getCount();
		int cash = ct * 120;
		materials = (new int[] { 49016 });
		counts = (new int[] { ct });
		createitem = (new int[] { 40308 });
		createcount = (new int[] { cash });
		htmlid = "inn20";
		String count = Integer.toString(cash);
		htmldata = (new String[] { "旅館老闆", count });
		} else {
		pc.sendPackets(new S_SystemMessage("沒有房間或大廳出租."));
		}
		} else if (s.equalsIgnoreCase("enter")) {
		int nowX = pc.getX();
		int nowY = pc.getY();
		short map = pc.getMapId();
		if (pc.getInventory().checkItem(40312)) {
		if (map == 0)
		pc.start_teleport(32746, 32803, 16384, 5, 18339, false, false);
		else if (map > 0)
		if (nowX < 32641 && nowX > 32621 && nowY < 32760 && nowY > 32740)
		pc.start_teleport(32744, 32803, 17408, 5, 18339, false, false);
		else if (nowX < 32638 && nowX > 32618 && nowY < 33177 && nowY > 33157)
		pc.start_teleport(32745, 32803, 20480, 5, 18339, false, false);
		else if (nowX < 33995 && nowX > 33975 && nowY < 33322 && nowY > 33302)
		pc.start_teleport(32745, 32803, 24576, 5, 18339, false, false);
		else if (nowX < 33447 && nowX > 33427 && nowY < 32799 && nowY > 32779)
		pc.start_teleport(32740, 32803, 18432, 5, 18339, false, false);
		else if (nowX < 33615 && nowX > 33595 && nowY < 33285 && nowY > 33265)
		pc.start_teleport(32745, 32803, 22528, 5, 18339, false, false);
		else if (nowX < 33126 && nowX > 33106 && nowY < 33389 && nowY > 33369)
		pc.start_teleport(32745, 32803, 21504, 5, 18339, false, false);
		else if (nowX < 34078 && nowX > 34058 && nowY < 32264 && nowY > 32244)
		pc.start_teleport(32745, 32803, 19456, 5, 18339, false, false);
		} else if (pc.getInventory().checkItem(40312)) {
		if (map == 0)
		pc.start_teleport(32744, 32808, 16896, 5, 18339, false, false);
		else if (map > 0)
		if (nowX < 32641 && nowX > 32621 && nowY < 32760 && nowY > 32740)
		pc.start_teleport(32745, 32807, 18944, 5, 18339, false, false);
		else if (nowX < 32638 && nowX > 32618 && nowY < 33177 && nowY > 33157)
		pc.start_teleport(32745, 32807, 19968, 5, 18339, false, false);
		else if (nowX < 33995 && nowX > 33975 && nowY < 33322 && nowY > 33302)
		pc.start_teleport(32745, 32807, 20992, 5, 18339, false, false);
		else if (nowX < 33447 && nowX > 33427 && nowY < 32799 && nowY > 32779)
		pc.start_teleport(32745, 32807, 22016, 5, 18339, false, false);
		else if (nowX < 33615 && nowX > 33595 && nowY < 33285 && nowY > 33265)
		pc.start_teleport(32745, 32807, 23040, 5, 18339, false, false);
		else if (nowX < 33126 && nowX > 33106 && nowY < 33389 && nowY > 33369)
		pc.start_teleport(32745, 32807, 24064, 5, 18339, false, false);
		else if (nowX < 34078 && nowX > 34058 && nowY < 32264 && nowY > 32244)
		pc.start_teleport(32745, 32807, 25088, 5, 18339, false, false);
		} else {
		pc.sendPackets(new S_SystemMessage("沒有房間或大廳出租."));
		}
		}
		} else if (s.equalsIgnoreCase("hall") && obj instanceof L1MerchantInstance) {

		} else if (s.equalsIgnoreCase("return")) {

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 900136) {// 말섬 인던 유키
		if (s.equalsIgnoreCase("enter")) {
		if (pc.isInParty()) {
		if (pc.getParty().isLeader(pc)) {
		if (pc.getParty().getNumOfMembers() >= 1) {
		boolean ck = true;
		for (L1PcInstance Ppc : pc.getParty().getMembers()) {
		if (pc.getMapId() != Ppc.getMapId()) {
		pc.sendPackets(new S_SystemMessage("隊友尚未全部聚集."));
		ck = false;
		break;
		}
		}
		if (ck) {
		if (pc.getMapId() == 9100)
		htmlid = "";
		else
		HadinSystem.getInstance().startHadin(pc);
		L1PolyMorph.undoPoly(pc);
		L1World.getInstance().broadcastServerMessage("\\aD" + pc.getName() + " 您和同伴一起前往過去旅行 .");
		}
		htmlid = "";
		} else if (pc.getMapId() == 9100) {
		htmlid = "id1_1";
		} else {
		htmlid = "id0_1";
		}
		} else if (pc.getMapId() == 9100)
		htmlid = "id1_2";
		else {
		htmlid = "id0_2";
		}
		} else if (pc.getMapId() == 9100)
		htmlid = "id1_2";
		else {
		htmlid = "id0_2";
		}
		}
		} else if (s.equalsIgnoreCase("openigate")) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		openCloseGate(pc, npc.getNpcTemplate().get_npcId(), true);
		htmlid = "";
		} else if (s.equalsIgnoreCase("closeigate")) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		openCloseGate(pc, npc.getNpcTemplate().get_npcId(), false);
		htmlid = "";
		} else if (s.equalsIgnoreCase("askwartime")) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (npc.getNpcTemplate().get_npcId() == 60514) {
		htmldata = makeWarTimeStrings(L1CastleLocation.KENT_CASTLE_ID);
		htmlid = "ktguard7";
		} else if (npc.getNpcTemplate().get_npcId() == 60560) {
		htmldata = makeWarTimeStrings(L1CastleLocation.OT_CASTLE_ID);
		htmlid = "orcguard7";
		} else if (npc.getNpcTemplate().get_npcId() == 60552 || npc.getNpcTemplate().get_npcId() == 5155) {
		htmldata = makeWarTimeStrings(L1CastleLocation.WW_CASTLE_ID);
		htmlid = "wdguard7";
		} else if (npc.getNpcTemplate().get_npcId() == 60524 || npc.getNpcTemplate().get_npcId() == 60525 || npc.getNpcTemplate().get_npcId() == 60529 || npc.getNpcTemplate().get_npcId() == 7320232) {
		htmldata = makeWarTimeStrings(L1CastleLocation.GIRAN_CASTLE_ID);
		htmlid = "grguard7";
		} else if (npc.getNpcTemplate().get_npcId() == 70857) {
		htmldata = makeWarTimeStrings(L1CastleLocation.HEINE_CASTLE_ID);
		htmlid = "heguard7";
		} else if (npc.getNpcTemplate().get_npcId() == 60530 || npc.getNpcTemplate().get_npcId() == 60531) {
		htmldata = makeWarTimeStrings(L1CastleLocation.DOWA_CASTLE_ID);
		htmlid = "dcguard7";
		} else if (npc.getNpcTemplate().get_npcId() == 60533 || npc.getNpcTemplate().get_npcId() == 60534) {
		htmldata = makeWarTimeStrings(L1CastleLocation.ADEN_CASTLE_ID);
		htmlid = "adguard7";
		} else if (npc.getNpcTemplate().get_npcId() == 81156) {
		htmldata = makeWarTimeStrings(L1CastleLocation.DIAD_CASTLE_ID);
		htmlid = "dfguard3";
		}
		} else if (s.equalsIgnoreCase("inex")) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
		int castle_id = clan.getCastleId();
		if (castle_id != 0) {
		MJCastleWar war = MJCastleWarBusiness.getInstance().get(castle_id);
		pc.sendPackets(new S_ServerMessage(309, war.getCastleName(), String.valueOf(war.getPublicMoney())));
		htmlid = "";
		}
		}
		} else if (s.equalsIgnoreCase("tax")) {
		pc.sendPackets(new S_TaxRate(pc.getId(), MJCastleWarBusiness.getInstance().getTaxRate(pc.getClan().getCastleId())));
		} else if (s.equalsIgnoreCase("withdrawal")) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
		int castle_id = clan.getCastleId();
		if (castle_id != 0) {
		pc.sendPackets(new S_Drawal(pc.getId(), MJCastleWarBusiness.getInstance().getPublicMoney(castle_id)));
		}
		}
		} else if (s.equalsIgnoreCase("cdeposit")) {
		pc.sendPackets(new S_Deposit(pc.getId()));
		} else if (s.equalsIgnoreCase("employ")) {
		if ((pc.getClanRank() != L1Clan.盟主) && (pc.getClanRank() != L1Clan.守護騎士) && (pc.getClanRank() != L1Clan.副盟主) && (pc.getClanRank() != L1Clan.CLAN_RANK_LEAGUE_GUARDIAN)) {
		pc.sendPackets(new S_SystemMessage("僅限守護騎士及以上等級可以購買傭兵。"));
		return;
		}
		// 如果玩家的血盟等級不是君主、守護、副君主或聯盟守護者，則發送提示消息並返回

		if (pc.getPetList().size() > 0) {
		pc.sendPackets(new S_SystemMessage("已經有寵物或召喚物時無法使用。"));
		return;
		}
		// 如果玩家已經有寵物或召喚物，則發送提示消息並返回

		if (!pc.getInventory().consumeItem(40308, 300000)) {
		pc.sendPackets(new S_SystemMessage("購買傭兵需要30萬 金幣。"));
		return;
		}
		// 如果玩家的背包中沒有消耗30萬阿達納，則發送提示消息並返回

		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea((L1NpcInstance) obj);
		if (castleId > 0) {
		isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
		}
		// 檢查當前區域是否屬於城堡範圍，並且該城堡是否正在進行攻城戰

		if (isNowWar) {
		pc.sendPackets(new S_SystemMessage("在該城堡的攻城戰進行期間無法使用。"));
		return;
		}
		// 如果該城堡正在進行攻城戰，則發送提示消息並返回
		L1Npc npcTemp = NpcTable.getInstance().getTemplate(7320000);
		L1SummonInstance summon = new L1SummonInstance(npcTemp, pc);
		summon.setPetcost(6);
		pc.sendPackets(new S_CloseList(pc.getId()));
		pc.sendPackets(new S_ChatPacket(pc, "傭兵已經被雇用，可以使用1小時。", 1));
		// 創建召喚傭兵實例，設置寵物花費，並發送相關消息

		} else if (s.equalsIgnoreCase("arrange")) {
		pc.sendPackets(new S_SystemMessage("目前正在更新中，很快就可以使用。"));
		// 處理 "arrange" 指令，發送系統消息通知玩家功能正在更新

		} else if (s.equalsIgnoreCase("castlegate")) {
		repairGate(pc);
		htmlid = "";
		// 處理 "castlegate" 指令，修復城門，並清空htmlid

		} else if (s.equalsIgnoreCase("encw")) {
		if (pc.getWeapon() == null) {
		pc.sendPackets(new S_ServerMessage(79));
		// 如果玩家沒有裝備武器，發送通用錯誤消息（消息代碼79）

		} else {
		L1SkillUse l1skilluse = null;
		if (pc.getInventory().checkItem(40308, 100)) {
		for (L1ItemInstance item : pc.getInventory().getItems()) {
		if (pc.getWeapon().equals(item)) {
		l1skilluse = new L1SkillUse();
		l1skilluse.handleCommands(pc, L1SkillId.ENCHANT_WEAPON, item.getId(), 0, 0, null, 0, L1SkillUse.TYPE_SPELLSC);
		pc.getInventory().consumeItem(40308, 100);
		break;
		}
		}
		// 檢查玩家是否有足夠的阿達納（100），並對裝備中的武器進行強化，消耗100阿達納
		} else {
		pc.sendPackets(189);
		}
		}
		htmlid = "";
		} else if (s.equalsIgnoreCase("demand")) {
		if ((pc.getClanRank() != L1Clan.盟主) && (pc.getClanRank() != L1Clan.守護騎士) && (pc.getClanRank() != L1Clan.副盟主) && (pc.getClanRank() != L1Clan.CLAN_RANK_LEAGUE_GUARDIAN)) {
		pc.sendPackets(new S_SystemMessage("僅限守護騎士及以上等級可以購買傭兵。"));
		return;
		}
		// 如果玩家的血盟等級不是盟主、守護騎士、副盟主或聯盟守護者，則發送提示消息並返回

		if (pc.getPetList().size() > 0) {
		pc.sendPackets(new S_SystemMessage("已經有寵物或召喚物時無法使用。"));
		return;
		}
		// 如果玩家已經有寵物或召喚物，則發送提示消息並返回

		if (!pc.getInventory().consumeItem(40308, 300000)) {
		pc.sendPackets(new S_SystemMessage("購買傭兵需要30萬 金幣。"));
		return;
		}
		// 如果玩家的背包中沒有消耗30萬阿達納，則發送提示消息並返回

		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea((L1NpcInstance) obj);
		if (castleId > 0) {
		isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
		}
		// 檢查當前區域是否屬於城堡範圍，並且該城堡是否正在進行攻城戰

		if (isNowWar) {
		pc.sendPackets(new S_SystemMessage("在該城堡的攻城戰進行期間無法使用。"));
		return;
		}
		// 如果該城堡正在進行攻城戰，則發送提示消息並返回

		L1Npc npcTemp = NpcTable.getInstance().getTemplate(7320031);
		L1SummonInstance summon = new L1SummonInstance(npcTemp, pc);
		summon.setPetcost(6);
		pc.sendPackets(new S_CloseList(pc.getId()));
		pc.sendPackets(new S_ChatPacket(pc, "肯特城傭兵已被雇用，可以使用1小時。", 1));
		// 創建召喚傭兵實例，設置寵物花費，並發送相關消息
		} else if (s.equalsIgnoreCase("enca")) {
		L1ItemInstance item = pc.getInventory().getItemEquipped(2, 2);
		if (pc.getInventory().checkItem(40308, 100)) {
		if (item != null) {
		L1SkillUse l1skilluse = new L1SkillUse();
		l1skilluse.handleCommands(pc, L1SkillId.BLESSED_ARMOR, item.getId(), 0, 0, null, 0, L1SkillUse.TYPE_SPELLSC);
		pc.getInventory().consumeItem(40308, 100);
		} else {
		pc.sendPackets(new S_ServerMessage(79));
		}
		} else {
		pc.sendPackets(189);
		}
		htmlid = "";
		} else if (s.equalsIgnoreCase("depositnpc")) {
		Object[] petList = pc.getPetList().values().toArray();
		L1PetInstance pet = null;
		for (Object petObject : petList) {
		if (petObject instanceof L1PetInstance) {
		pet = (L1PetInstance) petObject;
		if (pet.getArmor() != null) {
		pet.removePetArmor(pet.getArmor());
		}
		if (pet.getWeapon() != null) {
		pet.removePetWeapon(pet.getWeapon());
		}
		/** ui6 관련 펫파티,컨트롤 **/
		pc.sendPackets(new S_PetCtrlMenu(pc, pet, false));

		pet.collect();
		pet.deleteMe();
		pc.getPetList().remove(Integer.valueOf(pet.getId()));
		}
		}
		htmlid = "";
		// 重置 htmlid 變量

		} else if (s.equalsIgnoreCase("dismissnpc")) {
		pc.sendPackets(new S_SystemMessage("暫時無法轉讓。"));
		// 處理 "dismissnpc" 指令，發送系統消息通知玩家暫時無法轉讓

		} else if (s.equalsIgnoreCase("withdrawnpc")) {
		List<L1ItemInstance> amuletList = new ArrayList<L1ItemInstance>();
		L1ItemInstance item = null;
		// 處理 "withdrawnpc" 指令，創建一個空的護身符列表

		for (Object itemObject : pc.getInventory().getItems()) {
		item = (L1ItemInstance) itemObject;
		if (item.getItem().getItemId() == 40314 || item.getItem().getItemId() == 40316) {
		amuletList.add(item);
		}
		// 遍歷玩家背包中的物品，將符合條件的護身符（itemId 為 40314 或 40316）添加到列表中
		}

		if (amuletList.size() <= 0) {
		pc.sendPackets(new S_SystemMessage("沒有持有的寵物。"));
		// 如果列表中沒有護身符，發送系統消息通知玩家沒有持有的寵物

		} else {
		pc.sendPackets(new S_PetList(objid, pc));
		// 否則，發送寵物列表給玩家
		}

		} else if (s.equalsIgnoreCase("changename")) {
		pc.setTempID(objid);
		pc.sendPackets(new S_Message_YN(325, ""));
		} else if (s.equalsIgnoreCase("attackchr")) {
		if (pc.getZoneType() != 1) {
		if (obj instanceof L1Character) {
		L1Character cha = (L1Character) obj;
		pc.sendPackets(new S_SelectTarget(cha.getId()));
		}
		} else {
		if (obj instanceof L1Character) {
		L1Character cha = (L1Character) obj;
		pc.sendPackets(new S_SelectTarget(cha.getId()));
		}
		}
		} else if (s.equalsIgnoreCase("select")) {
		pc.sendPackets(new S_AuctionBoardRead(objid, s2));
		} else if (s.equalsIgnoreCase("map")) {
		pc.sendPackets(new S_HouseMap(objid, s2));
		} else if (s.equalsIgnoreCase("apply")) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
		if (pc.isCrown() && pc.getId() == clan.getLeaderId()) {
		if (pc.getLevel() >= 15) {
		if (clan.getHouseId() == 0) {
		pc.sendPackets(new S_ApplyAuction(objid, s2));
		} else {
		pc.sendPackets(new S_ServerMessage(521));
		htmlid = "";
		}
		} else {
		pc.sendPackets(new S_ServerMessage(519));
		htmlid = "";
		}
		} else {
		pc.sendPackets(new S_ServerMessage(518));
		htmlid = "";
		}
		} else {
		pc.sendPackets(new S_ServerMessage(518));
		htmlid = "";
		}
		} else if (s.equalsIgnoreCase("open") || s.equalsIgnoreCase("close")) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		openCloseDoor(pc, npc, s);
		htmlid = "";
		} else if (s.equalsIgnoreCase("expel")) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		expelOtherClan(pc, npc.getNpcTemplate().get_npcId());
		htmlid = "";
		} else if (s.equalsIgnoreCase("pay")) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		htmldata = makeHouseTaxStrings(pc, npc);
		htmlid = "agpay";
		} else if (s.equalsIgnoreCase("payfee")) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		payFee(pc, npc);
		htmlid = "";
		} else if (s.equalsIgnoreCase("name")) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
		int houseId = clan.getHouseId();
		if (houseId != 0) {
		L1House house = HouseTable.getInstance().getHouseTable(houseId);
		int keeperId = house.getKeeperId();
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (npc.getNpcTemplate().get_npcId() == keeperId) {
		pc.setTempID(houseId);
		pc.sendPackets(new S_Message_YN(512, ""));
		}
		}
		}
		htmlid = "";
		} else if (s.equalsIgnoreCase("rem")) {
		} else if (s.equalsIgnoreCase("tel0") || s.equalsIgnoreCase("tel1") || s.equalsIgnoreCase("tel2") || s.equalsIgnoreCase("tel3")) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
		int houseId = clan.getHouseId();
		if (houseId != 0) {
		L1House house = HouseTable.getInstance().getHouseTable(houseId);
		int keeperId = house.getKeeperId();
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (npc.getNpcTemplate().get_npcId() == keeperId) {
		int[] loc = new int[3];
		if (s.equalsIgnoreCase("tel0")) {
		loc = L1HouseLocation.getHouseTeleportLoc(houseId, 0);
		} else if (s.equalsIgnoreCase("tel1")) {
		loc = L1HouseLocation.getHouseTeleportLoc(houseId, 1);
		} else if (s.equalsIgnoreCase("tel2")) {
		loc = L1HouseLocation.getHouseTeleportLoc(houseId, 2);
		} else if (s.equalsIgnoreCase("tel3")) {
		loc = L1HouseLocation.getHouseTeleportLoc(houseId, 3);
		}
		pc.start_teleport(loc[0], loc[1], loc[2], 5, 18339, true, false);
		}
		}
		}
		htmlid = "";
		} else if (s.equalsIgnoreCase("upgrade")) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
		int houseId = clan.getHouseId();
		if (houseId != 0) {
		L1House house = HouseTable.getInstance().getHouseTable(houseId);
		int keeperId = house.getKeeperId();
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (npc.getNpcTemplate().get_npcId() == keeperId) {
		if (pc.isCrown() && pc.getId() == clan.getLeaderId()) {
		if (house.isPurchaseBasement()) {
		pc.sendPackets(new S_ServerMessage(1135));
		} else {
		if (pc.getInventory().consumeItem(L1ItemId.ADENA, 5000000)) {
		house.setPurchaseBasement(true);
		HouseTable.getInstance().updateHouse(house);
		pc.sendPackets(new S_ServerMessage(1099));
		} else {
		pc.sendPackets(new S_ServerMessage(189));
		}
		}
		} else {
		pc.sendPackets(new S_ServerMessage(518));
		}
		}
		}
		}
		htmlid = "";
		} else if (s.equalsIgnoreCase("hall") && obj instanceof L1HousekeeperInstance) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
		int houseId = clan.getHouseId();
		if (houseId != 0) {
		L1House house = HouseTable.getInstance().getHouseTable(houseId);
		int keeperId = house.getKeeperId();
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (npc.getNpcTemplate().get_npcId() == keeperId) {
		if (house.isPurchaseBasement()) {
		int[] loc = new int[3];
		loc = L1HouseLocation.getBasementLoc(houseId);
		pc.start_teleport(loc[0], loc[1], loc[2], 5, 18339, true, false);
		} else {
		pc.sendPackets(new S_ServerMessage(1098));
		}
		}
		}
		}
		htmlid = "";
		}
/**阿克蘇姆格倫**/
		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7800100) {
		// 如果與玩家互動的NPC ID是7800100（阿克蘇姆格倫）
		if (pc.getInventory().checkItem(420100) || pc.getInventory().checkItem(420101) || pc.getInventory().checkItem(420102)
		|| pc.getInventory().checkItem(420103) || pc.getInventory().checkItem(420104) || pc.getInventory().checkItem(420105)
		|| pc.getInventory().checkItem(420106) || pc.getInventory().checkItem(420107) || pc.getInventory().checkItem(420108)
		|| pc.getInventory().checkItem(420109) || pc.getInventory().checkItem(420110) || pc.getInventory().checkItem(420111)) {
		htmlid = "indun_losus_gerengf";
		return;
		// 如果玩家背包中包含以上任意一種道具，則顯示對應的HTML頁面並返回
		} else {
		if (s.equalsIgnoreCase("a")) { // 選擇火焰的力量
		pc.getInventory().storeItem(420100, 1); // 獲得盒子的火焰
		} else if (s.equalsIgnoreCase("b")) { // 選擇閃光的力量
		pc.getInventory().storeItem(420102, 1); // 獲得閃光的盒子
		} else if (s.equalsIgnoreCase("c")) { // 選擇酷寒的力量
		pc.getInventory().storeItem(420101, 1); // 獲得酷寒的盒子
		}
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 9291 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70760
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 460000151) {
		// 如果與玩家互動的NPC ID是460000151

		if (s.equalsIgnoreCase("fire")) {
		// 處理玩家選擇 "fire" 的情況

		if (pc.isElf()) {
		// 檢查玩家是否是精靈

		if (pc.getElfAttr() != 0) {
		// 檢查玩家是否已經有精靈屬性

		if (pc.getElfAttr() == 2) {
		pc.sendPackets(new S_SystemMessage("請先移除水屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有水屬性，發送系統消息並重置 htmlid
		} else if (pc.getElfAttr() == 4) {
		pc.sendPackets(new S_SystemMessage("請先移除風屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有風屬性，發送系統消息並重置 htmlid
		} else if (pc.getElfAttr() == 8) {
		pc.sendPackets(new S_SystemMessage("請先移除地屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有地屬性，發送系統消息並重置 htmlid
		} else if (pc.getElfAttr() == 1) {
		pc.sendPackets(new S_SystemMessage("不能選擇相同屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有火屬性，發送系統消息並重置 htmlid
		}
		return;
		}
		pc.sendPackets("火精靈的屬性已經滲入。");
		pc.setElfAttr(1);
		pc.sendPackets(new S_SkillIconGFX(15, 1));
		// SkillCheck.getInstance().sendAllSkillList(pc);
		htmlid = "";
		// 設置玩家的精靈屬性為火屬性，並發送相應的視覺效果，重置 htmlid

		} else {
		pc.sendPackets(new S_SystemMessage("該菜單無法被當前職業使用。"));
		htmlid = "";
		// 如果玩家不是精靈，則發送系統消息通知玩家無法使用該菜單，並重置 htmlid

		} else if (s.equalsIgnoreCase("water")) {
		if (pc.isElf()) {
		// 處理玩家選擇 "water" 的情況，檢查玩家是否是精靈

		if (pc.getElfAttr() != 0) {
		// 檢查玩家是否已經有精靈屬性

		if (pc.getElfAttr() == 1) {
		pc.sendPackets(new S_SystemMessage("請先移除火屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有火屬性，發送系統消息並重置 htmlid
		} else if (pc.getElfAttr() == 4) {
		pc.sendPackets(new S_SystemMessage("請先移除風屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有風屬性，發送系統消息並重置 htmlid
		} else if (pc.getElfAttr() == 8) {
		pc.sendPackets(new S_SystemMessage("請先移除地屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有地屬性，發送系統消息並重置 htmlid
		} else if (pc.getElfAttr() == 2) {
		pc.sendPackets(new S_SystemMessage("不能選擇相同屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有水屬性，發送系統消息並重置 htmlid
		}
		return;
		}
		pc.sendPackets("水精靈的屬性已經滲入。");
		pc.setElfAttr(2);
		pc.sendPackets(new S_SkillIconGFX(15, 2));
		// SkillCheck.getInstance().sendAllSkillList(pc);
		htmlid = "";
		// 設置玩家的精靈屬性為水屬性，並發送相應的視覺效果，重置 htmlid

		} else {
		pc.sendPackets(new S_SystemMessage("該菜單無法被當前職業使用。"));
		htmlid = "";
		// 如果玩家不是精靈，則發送系統消息通知玩家無法使用該菜單，並重置 htmlid

		} else if (s.equalsIgnoreCase("air")) {
		if (pc.isElf()) {
		// 處理玩家選擇 "air" 的情況，檢查玩家是否是精靈

		if (pc.getElfAttr() != 0) {
		// 檢查玩家是否已經有精靈屬性

		if (pc.getElfAttr() == 1) {
		pc.sendPackets(new S_SystemMessage("請先移除火屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有火屬性，發送系統消息並重置 htmlid
		} else if (pc.getElfAttr() == 2) {
		pc.sendPackets(new S_SystemMessage("請先移除水屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有水屬性，發送系統消息並重置 htmlid
		} else if (pc.getElfAttr() == 8) {
		pc.sendPackets(new S_SystemMessage("請先移除地屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有地屬性，發送系統消息並重置 htmlid
		} else if (pc.getElfAttr() == 4) {
		pc.sendPackets(new S_SystemMessage("不能選擇相同屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有風屬性，發送系統消息並重置 htmlid
		}
		return;
		}
		pc.sendPackets("風精靈的屬性已經滲入。");
		pc.setElfAttr(4);
		pc.sendPackets(new S_SkillIconGFX(15, 4));
		// SkillCheck.getInstance().sendAllSkillList(pc);
		htmlid = "";
		// 設置玩家的精靈屬性為風屬性，並發送相應的視覺效果，重置 htmlid

		} else {
		pc.sendPackets(new S_SystemMessage("該菜單無法被當前職業使用。"));
		htmlid = "";
		// 如果玩家不是精靈，則發送系統消息通知玩家無法使用該菜單，並重置 htmlid
		}
		} else if (s.equalsIgnoreCase("earth")) {
		if (pc.isElf()) {
		// 處理玩家選擇 "earth" 的情況，檢查玩家是否是精靈

		if (pc.getElfAttr() != 0) {
		// 檢查玩家是否已經有精靈屬性

		if (pc.getElfAttr() == 1) {
		pc.sendPackets(new S_SystemMessage("請先移除火屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有火屬性，發送系統消息並重置 htmlid
		} else if (pc.getElfAttr() == 2) {
		pc.sendPackets(new S_SystemMessage("請先移除水屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有水屬性，發送系統消息並重置 htmlid
		} else if (pc.getElfAttr() == 4) {
		pc.sendPackets(new S_SystemMessage("請先移除風屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有風屬性，發送系統消息並重置 htmlid
		} else if (pc.getElfAttr() == 8) {
		pc.sendPackets(new S_SystemMessage("不能選擇相同屬性的精靈力。"));
		htmlid = "";
		// 如果玩家已經有地屬性，發送系統消息並重置 htmlid
		}
		return;
		}
		pc.sendPackets("地精靈的屬性已經滲入。");
		pc.setElfAttr(8);
		pc.sendPackets(new S_SkillIconGFX(15, 8));
		// SkillCheck.getInstance().sendAllSkillList(pc);
		htmlid = "";
		// 設置玩家的精靈屬性為地屬性，並發送相應的視覺效果，重置 htmlid

		} else {
		pc.sendPackets(new S_SystemMessage("該菜單無法被當前職業使用。"));
		htmlid = "";
		// 如果玩家不是精靈，則發送系統消息通知玩家無法使用該菜單，並重置 htmlid
		}

		} else if (s.equalsIgnoreCase("init")) {
		if (pc.isElf()) {
		// 處理玩家選擇 "init" 的情況，檢查玩家是否是精靈

		if (pc.getElfAttr() == 0) {
		pc.sendPackets("沒有賦予的精靈力量。");
		htmlid = "";
		return;
		// 如果玩家沒有任何精靈屬性，發送消息通知玩家 "沒有賦予的精靈力量。" 並重置 htmlid
		}
		htmlid = "ellyonne11";
		} else {
		htmlid = "ellyonne15";
		}
		} else if (s.equalsIgnoreCase("count")) {
		htmlid = "ellyonne12";
		int aden_count = 500000;
		if (pc.getElfAttrInitCount() > 1) {
		aden_count = 500000 * pc.getElfAttrInitCount();
		}
		DecimalFormat df = new DecimalFormat("#,##0");
		htmldata = (new String[] { String.valueOf(df.format(aden_count)) });
		} else if (s.equalsIgnoreCase("money")) {
		if (pc.isElf()) {
		int aden_count = 500000;
		if (pc.getElfAttrInitCount() > 1) {
		aden_count = 500000 * pc.getElfAttrInitCount();
		}
		if (pc.getInventory().consumeItem(40308, aden_count)) {
		if (pc.hasSkillEffect(L1SkillId.ELEMENTAL_PROTECTION)) {
		pc.removeSkillEffect(L1SkillId.ELEMENTAL_PROTECTION);
		}
		pc.sendPackets("精靈屬性已經移除。");
		pc.setElfAttr(0);
		pc.setGlory_Earth_Attr(0);
		pc.addElfAttrInitCount(1);
		pc.sendPackets(new S_SkillIconGFX(15, 0));
		// SkillCheck.getInstance().sendAllSkillList(pc);
		htmlid = "";
		// 移除玩家的精靈屬性，並發送相應的視覺效果，重置 htmlid

		} else {
		pc.sendPackets(new S_ServerMessage(189));
		htmlid = "ellyonne13";
		// 如果玩家不是精靈，則發送系統消息並設置 htmlid

		} else {
		htmlid = "ellyonne15";
		// 如果條件不符合，設置 htmlid

		} else if (s.equalsIgnoreCase("bm")) { // 用兩個回憶的蠟燭來移除精靈力
		if (pc.isElf()) {
		if (pc.getInventory().consumeItem(200000, 2)) {
		if (pc.hasSkillEffect(L1SkillId.ELEMENTAL_PROTECTION)) {
		pc.removeSkillEffect(L1SkillId.ELEMENTAL_PROTECTION);
		}
		pc.sendPackets("精靈屬性已經移除。");
		pc.setElfAttr(0);
		pc.setGlory_Earth_Attr(0);
		pc.sendPackets(new S_SkillIconGFX(15, 0));
		htmlid = "";
		// 移除玩家的精靈屬性，並發送相應的視覺效果，重置 htmlid

		} else {
		pc.sendPackets(new S_ServerMessage(4540));
		htmlid = "ellyonne13";
		// 如果玩家沒有足夠的物品，則發送系統消息並設置 htmlid

		} else {
		htmlid = "ellyonne15";
		// 如果玩家不是精靈，設置 htmlid

		} else if (s.equalsIgnoreCase("gfw")) { // 火+水
		if (pc.isElf()) {
		if (pc.getElfAttr() != 0 && pc.getGlory_Earth_Attr() != 0) {
		pc.sendPackets("已經選擇了其它屬性。");
		htmlid = "";
		return;
		// 如果玩家已經有其他屬性，發送系統消息並重置 htmlid
		}
		pc.sendPackets("火+水精靈的屬性已經滲入。");
		pc.setElfAttr(1);
		pc.setGlory_Earth_Attr(2);
		pc.sendPackets(new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()));
		// SkillCheck.getInstance().sendAllSkillList(pc);
		htmlid = "";
		// 設置玩家的精靈屬性為火屬性，榮耀地屬性為水屬性，並發送相應的視覺效果，重置 htmlid

		} else {
		pc.sendPackets(new S_SystemMessage("該菜單無法被當前職業使用。"));
		htmlid = "";
		// 如果玩家不是精靈，則發送系統消息並重置 htmlid

		} else if (s.equalsIgnoreCase("gfa")) { // 火+風
		if (pc.isElf()) {
		if (pc.getElfAttr() != 0 && pc.getGlory_Earth_Attr() != 0) {
		pc.sendPackets("已經選擇了其它屬性。");
		htmlid = "";
		return;
		// 如果玩家已經有其他屬性，發送系統消息並重置 htmlid
		}
		pc.sendPackets("火+風精靈的屬性已經滲入。");
		pc.setElfAttr(1);
		pc.setGlory_Earth_Attr(4);
		pc.sendPackets(new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()));
		// SkillCheck.getInstance().sendAllSkillList(pc);
		htmlid = "";
		// 設置玩家的精靈屬性為火屬性，榮耀地屬性為風屬性，並發送相應的視覺效果，重置 htmlid

		} else {
		pc.sendPackets(new S_SystemMessage("該菜單無法被當前職業使用。"));
		htmlid = "";
		// 如果玩家不是精靈，則發送系統消息並重置 htmlid

		} else if (s.equalsIgnoreCase("gfe")) { // 火+地
		if (pc.isElf()) {
		if (pc.getElfAttr() != 0 && pc.getGlory_Earth_Attr() != 0) {
		pc.sendPackets("已經選擇了其它屬性。");
		htmlid = "";
		return;
		// 如果玩家已經有其他屬性，發送系統消息並重置 htmlid
		}
		pc.sendPackets("火+地精靈的屬性已經滲入。");
		pc.setElfAttr(1);
		pc.setGlory_Earth_Attr(8);
		pc.sendPackets(new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()));
		// SkillCheck.getInstance().sendAllSkillList(pc);
		htmlid = "";
		// 設置玩家的精靈屬性為火屬性，榮耀地屬性為地屬性，並發送相應的視覺效果，重置 htmlid

		} else {
		pc.sendPackets(new S_SystemMessage("該菜單無法被當前職業使用。"));
		htmlid = "";
		// 如果玩家不是精靈，則發送系統消息並重置 htmlid

		} else if (s.equalsIgnoreCase("gwa")) { // 水+風
		if (pc.isElf()) {
		if (pc.getElfAttr() != 0 && pc.getGlory_Earth_Attr() != 0) {
		pc.sendPackets("已經選擇了其它屬性。");
		htmlid = "";
		return;
		// 如果玩家已經有其他屬性，發送系統消息並重置 htmlid
		}
		pc.sendPackets("水+風精靈的屬性已經滲入。");
		pc.setElfAttr(2);
		pc.setGlory_Earth_Attr(4);
		pc.sendPackets(new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()));
		// SkillCheck.getInstance().sendAllSkillList(pc);
		htmlid = "";
		// 設置玩家的精靈屬性為水屬性，榮耀地屬性為風屬性，並發送相應的視覺效果，重置 htmlid

		} else {
		pc.sendPackets(new S_SystemMessage("該菜單無法被當前職業使用。"));
		htmlid = "";
		// 如果玩家不是精靈，則發送系統消息並重置 htmlid

		} else if (s.equalsIgnoreCase("gwe")) { // 水+地
		if (pc.isElf()) {
		if (pc.getElfAttr() != 0 && pc.getGlory_Earth_Attr() != 0) {
		pc.sendPackets("已經選擇了其它屬性。");
		htmlid = "";
		return;
		// 如果玩家已經有其他屬性，發送系統消息並重置 htmlid
		}
		pc.sendPackets("水+地精靈的屬性已經滲入。");
		pc.setElfAttr(2);
		pc.setGlory_Earth_Attr(8);
		pc.sendPackets(new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()));
		// SkillCheck.getInstance().sendAllSkillList(pc);
		htmlid = "";
		// 設置玩家的精靈屬性為水屬性，榮耀地屬性為地屬性，並發送相應的視覺效果，重置 htmlid

		} else {
		pc.sendPackets(new S_SystemMessage("該菜單無法被當前職業使用。"));
		htmlid = "";
		// 如果玩家不是精靈，則發送系統消息並重置 htmlid

		} else if (s.equalsIgnoreCase("gae")) { // 風+地
		if (pc.isElf()) {
		if (pc.getElfAttr() != 0 && pc.getGlory_Earth_Attr() != 0) {
		pc.sendPackets("已經選擇了其它屬性。");
		htmlid = "";
		return;
		// 如果玩家已經有其他屬性，發送系統消息並重置 htmlid
		}
		pc.sendPackets("風+地精靈的屬性已經滲入。");
		pc.setElfAttr(4);
		pc.setGlory_Earth_Attr(8);
		pc.sendPackets(new S_SkillIconGFX(15, pc.getElfAttr() + pc.getGlory_Earth_Attr()));

		// SkillCheck.getInstance().sendAllSkillList(pc);
		htmlid = "";

		// 設置玩家的精靈屬性為風屬性，榮耀地屬性為地屬性，並發送相應的視覺效果，重置 htmlid

		} else {
		pc.sendPackets(new S_SystemMessage("該菜單無法被當前職業使用。"));
		htmlid = "";

		// 如果玩家不是精靈，則發送系統消息並重置 htmlid
		}
		} else if (s.equalsIgnoreCase("exp")) {// 신녀 아가타
		if (pc.get_exp_res() == 1) {
		int cost = 0;
		int level = pc.getLevel();
		int lawful = pc.getLawful();
		if (level < 45) {
		cost = level * level * 50;
		} else {
		cost = level * level * 150;
		}
		if (lawful >= 0) {
		cost = (int) (cost * 0.7);
		}
		pc.sendPackets(new S_Message_YN(738, String.valueOf(cost)));
		} else {
		pc.sendPackets(new S_ServerMessage(739));
		htmlid = "";
		}
		htmlid = "";
		} else if (s.equalsIgnoreCase("material")) { // 救護憑證
		if (pc.getInventory().checkItem(3000049, 1) || pc.getInventory().checkItem(4100694, 1)) {

		// 檢查玩家背包中是否有 3000049 或 4100694 其中之一個物品

		if (pc.get_exp_res() == 1) {
		pc.sendPackets(new S_Message_YN(2551, ""));

		// 如果玩家的經驗重置狀態為 1，則發送選擇消息 2551
		} else {
		pc.sendPackets(new S_ServerMessage(739));
		htmlid = "";

		// 如果玩家的經驗重置狀態不是 1，則發送系統消息 739 並重置 htmlid
		}
		} else {
		pc.sendPackets("救護憑證不足。");

		// pc.sendPackets(new S_NpcChatPacket(npc, "需要救護憑證。", 20));
		htmlid = "";

		// 如果玩家沒有足夠的救護憑證，則發送消息 "救護憑證不足。" 並重置 htmlid
		}
		if (pc.getLevel() > 54) {

		// 檢查玩家等級是否大於 54

		if (pc.getInventory().checkItem(200000)) {

		// 檢查玩家背包中是否有物品編號 200000

		pc.getInventory().consumeItem(200000, 1);

		// 消耗一個物品編號 200000 的物品

		pc.start_teleport(32723 + CommonUtil.random(-10, 10), 32815 + CommonUtil.random(-10, 10), 5166, 5, 18339, true, false);

		// 傳送玩家到指定地圖上的隨機座標

		pc.屬性重置();

		// 調用玩家屬性重置方法

		StatInitialize(pc);

		// 調用屬性初始化方法

		htmlid = "";

		// 重置 htmlid 變量

		} else {
		pc.sendPackets(new S_ServerMessage(1290));

		// 如果玩家沒有足夠的物品，則發送系統消息 1290
		}
		} else {
		pc.sendPackets(new S_SystemMessage("屬性重置僅限於等級 55 及以上玩家。"));

		// 如果玩家等級不夠，則發送系統消息通知 "屬性重置僅限於等級 55 及以上玩家。"
		}

		} else if (npcId == 50038 || npcId == 50042 || npcId == 50029 || npcId == 50019 || npcId == 50062) {
		pc.sendPackets(new S_ChatPacket(pc, "無限競技場觀戰模式已禁用。"));

		// 如果 NPC ID 符合某些值，則發送聊天消息通知玩家 "無限競技場觀戰模式已禁用。"
		} else {
		htmlid = enterUb(pc, npcId);

		// 調用 enterUb 方法，並將返回值賦給 htmlid

		} else if (s.equalsIgnoreCase("par")) {

		// 如果字符串 s 等於 "par"

		pc.sendPackets("無法使用。");

		// 發送消息通知玩家 "無法使用。"
		}
//		} else if (s.equalsIgnoreCase("info")) {
//			int npcId = ((L1NpcInstance) obj).getNpcId();
//			if (npcId == 80085 || npcId == 80086 || npcId == 80087) {
//
//			} else {
//				htmlid = "colos2";
//			}
		} else if (s.equalsIgnoreCase("sco")) {
		htmldata = new String[10];
		htmlid = "colos3";
		// TODO 헤이스트 사
		} else if (s.equalsIgnoreCase("haste")) {
		if (pc.hasSkillEffect(L1SkillId.HASTE))
		pc.removeSkillEffect(L1SkillId.HASTE);
		new L1SkillUse().handleCommands(pc, L1SkillId.HASTE, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);

		// TODO 變身術士
		} else if (s.equalsIgnoreCase("Src1")) { // 變身為真·死亡騎士
		if (pc.getLevel() > 82) {
		pc.sendPackets("無法為等級83以上的勇士變身！");
		return;

		// 如果玩家等級超過 82，則發送消息 "無法為等級83以上的勇士變身！" 並返回
		}
		if (pc.getInventory().checkItem(40308, 100)) {
		poly(client, 13152);
		pc.getInventory().consumeItem(40308, 100);

		// 如果玩家有 100 個物品編號 40308，則變身為 13152 並消耗這些物品
		} else {
		pc.sendPackets(189);

		// 如果玩家沒有足夠的物品，則發送系統消息 189
		}
		htmlid = "";

		// 重置 htmlid 變量
		} else if (s.equalsIgnoreCase("Src2")) { // 變身為真·暗影
		if (pc.getLevel() > 82) {
		pc.sendPackets("無法為等級83以上的勇士變身！");
		return;

		// 如果玩家等級超過 82，則發送消息 "無法為等級83以上的勇士變身！" 並返回
		}
		if (pc.getInventory().checkItem(40308, 100)) {
		poly(client, 15868);
		pc.getInventory().consumeItem(40308, 100);

		// 如果玩家有 100 個物品編號 40308，則變身為 15868 並消耗這些物品
		} else {
		pc.sendPackets(189);

		// 如果玩家沒有足夠的物品，則發送系統消息 189
		}
		htmlid = "";

		// 重置 htmlid 變量
		} else if (s.equalsIgnoreCase("Src3")) { // 變身為真·長矛手
		if (pc.getLevel() > 82) {
		pc.sendPackets("無法為等級83以上的勇士變身！");
		return;

		// 如果玩家等級超過 82，則發送消息 "無法為等級83以上的勇士變身！" 並返回
		}
		if (pc.getInventory().checkItem(40308, 100)) {
		poly(client, 15539);
		pc.getInventory().consumeItem(40308, 100);
		} else {
		pc.sendPackets(189);
		}
		htmlid = "";

		// 重置 htmlid 變量

		} else if (s.equalsIgnoreCase("Src4")) { // 變身為真·歐文
		if (pc.getLevel() > 82) {
		pc.sendPackets("無法為等級83以上的勇士變身！");
		return;

		// 如果玩家等級超過 82，則發送消息 "無法為等級83以上的勇士變身！" 並返回
		}
		if (pc.getInventory().checkItem(40308, 100)) {
		poly(client, 13635);
		pc.getInventory().consumeItem(40308, 100);

		// 如果玩家有 100 個物品編號 40308，則變身為 13635 並消耗這些物品
		} else {
		pc.sendPackets(189);

		// 如果玩家沒有足夠的物品，則發送系統消息 189
		}
		htmlid = "";

		// 重置 htmlid 變量

		} else if (s.equalsIgnoreCase("Src5")) { // 變身為真·巴風特
		if (pc.getLevel() > 82) {
		pc.sendPackets("無法為等級83以上的勇士變身！");
		return;

		// 如果玩家等級超過 82，則發送消息 "無法為等級83以上的勇士變身！" 並返回
		}
		if (pc.getInventory().checkItem(40308, 100)) {
		poly(client, 15550);
		pc.getInventory().consumeItem(40308, 100);
		} else {
		pc.sendPackets(189);
		}
		htmlid = "";
		} else if (s.equalsIgnoreCase("contract1")) {
		pc.getQuest().set_step(L1Quest.QUEST_LYRA, 1);
		htmlid = "lyraev2";
		} else if (s.equalsIgnoreCase("contract1yes") || s.equalsIgnoreCase("contract1no")) {

		if (s.equalsIgnoreCase("contract1yes")) {
		htmlid = "lyraev5";
		} else if (s.equalsIgnoreCase("contract1no")) {
		pc.getQuest().set_step(L1Quest.QUEST_LYRA, 0);
		htmlid = "lyraev4";
		}
		int totem = 0;
		if (pc.getInventory().checkItem(40131)) {
		totem++;
		}
		if (pc.getInventory().checkItem(40132)) {
		totem++;
		}
		if (pc.getInventory().checkItem(40133)) {
		totem++;
		}
		if (pc.getInventory().checkItem(40134)) {
		totem++;
		}
		if (pc.getInventory().checkItem(40135)) {
		totem++;
		}
		if (totem != 0) {
		materials = new int[totem];
		counts = new int[totem];
		createitem = new int[totem];
		createcount = new int[totem];

		totem = 0;
		if (pc.getInventory().checkItem(40131)) {
		L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40131);
		int i1 = l1iteminstance.getCount();
		materials[totem] = 40131;
		counts[totem] = i1;
		createitem[totem] = L1ItemId.ADENA;
		createcount[totem] = i1 * 50;
		totem++;
		}
		if (pc.getInventory().checkItem(40132)) {
		L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40132);
		int i1 = l1iteminstance.getCount();
		materials[totem] = 40132;
		counts[totem] = i1;
		createitem[totem] = L1ItemId.ADENA;
		createcount[totem] = i1 * 100;
		totem++;
		}
		if (pc.getInventory().checkItem(40133)) {
		L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40133);
		int i1 = l1iteminstance.getCount();
		materials[totem] = 40133;
		counts[totem] = i1;
		createitem[totem] = L1ItemId.ADENA;
		createcount[totem] = i1 * 50;
		totem++;
		}
		if (pc.getInventory().checkItem(40134)) {
		L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40134);
		int i1 = l1iteminstance.getCount();
		materials[totem] = 40134;
		counts[totem] = i1;
		createitem[totem] = L1ItemId.ADENA;
		createcount[totem] = i1 * 30;
		totem++;
		}
		if (pc.getInventory().checkItem(40135)) {
		L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40135);
		int i1 = l1iteminstance.getCount();
		materials[totem] = 40135;
		counts[totem] = i1;
		createitem[totem] = L1ItemId.ADENA;
		createcount[totem] = i1 * 200;
		totem++;
		}
		}
		} else if (s.equalsIgnoreCase("pandora6") || s.equalsIgnoreCase("cold6") || s.equalsIgnoreCase("balsim3") || s.equalsIgnoreCase("mellin3") || s.equalsIgnoreCase("glen3")) {
		htmlid = s;
		int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
		int taxRatesCastle = L1CastleLocation.getCastleTaxRateByNpcId(npcid);
		htmldata = new String[] { String.valueOf(taxRatesCastle) };
		} else if (s.equalsIgnoreCase("set")) {
		if (obj instanceof L1NpcInstance) {
		int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
		int town_id = L1TownLocation.getTownIdByNpcid(npcid);

		if (town_id >= 1 && town_id <= 10) {
		if (pc.getHomeTownId() == -1) {
		pc.sendPackets(new S_ServerMessage(759));
		htmlid = "";
		} else if (pc.getHomeTownId() > 0) {
		if (pc.getHomeTownId() != town_id) {
		L1Town town = TownTable.getInstance().getTownTable(pc.getHomeTownId());
		if (town != null) {
		pc.sendPackets(new S_ServerMessage(758, town.get_name()));
		}
		htmlid = "";
		} else {
		htmlid = "";
		}
		} else if (pc.getHomeTownId() == 0) {
		if (pc.getLevel() < 10) {
		pc.sendPackets(new S_ServerMessage(757));
		htmlid = "";
		} else {
		int level = pc.getLevel();
		int cost = level * level * 10;
		if (pc.getInventory().consumeItem(L1ItemId.ADENA, cost)) {
		pc.setHomeTownId(town_id);
		pc.setContribution(0);
		pc.save();
		} else {
		pc.sendPackets(new S_ServerMessage(337, "$4"));
		}
		htmlid = "";
		}
		}
		}
		}
		} else if (s.equalsIgnoreCase("clear")) {
		if (obj instanceof L1NpcInstance) {
		int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
		int town_id = L1TownLocation.getTownIdByNpcid(npcid);
		if (town_id > 0) {
		if (pc.getHomeTownId() > 0) {
		if (pc.getHomeTownId() == town_id) {
		pc.setHomeTownId(-1);
		pc.setContribution(0);
		} else {
		pc.sendPackets(new S_ServerMessage(756));
		}
		}
		htmlid = "";
		}
		}
		} else if (s.equalsIgnoreCase("ask")) {
		if (obj instanceof L1NpcInstance) {
		int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
		int town_id = L1TownLocation.getTownIdByNpcid(npcid);

		if (town_id >= 1 && town_id <= 10) {
		L1Town town = TownTable.getInstance().getTownTable(town_id);
		String leader = town.get_leader_name();
		if (leader != null && leader.length() != 0) {
		htmlid = "owner";
		htmldata = new String[] { leader };
		} else {
		htmlid = "noowner";
		}
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71038) {
		if (s.equalsIgnoreCase("A")) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		L1ItemInstance item = pc.getInventory().storeItem(41060, 1);
		String itemName = item.getItem().getName();
		String npcName = npc.getNpcTemplate().get_name();
		pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
		htmlid = "orcfnoname9";
		} else if (s.equalsIgnoreCase("Z")) {
		if (pc.getInventory().consumeItem(41060, 1)) {
		htmlid = "orcfnoname11";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71039) {
		if (s.equalsIgnoreCase("teleportURL")) {
		htmlid = "orcfbuwoo2";
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71040) {
		if (s.equalsIgnoreCase("A")) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		L1ItemInstance item = pc.getInventory().storeItem(41065, 1);
		String itemName = item.getItem().getName();
		String npcName = npc.getNpcTemplate().get_name();
		pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
		htmlid = "orcfnoa4";
		} else if (s.equalsIgnoreCase("Z")) {
		if (pc.getInventory().consumeItem(41065, 1)) {
		htmlid = "orcfnoa7";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71041) {
		if (s.equalsIgnoreCase("A")) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		L1ItemInstance item = pc.getInventory().storeItem(41064, 1);
		String itemName = item.getItem().getName();
		String npcName = npc.getNpcTemplate().get_name();
		pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
		htmlid = "orcfhuwoomo4";
		} else if (s.equalsIgnoreCase("Z")) {
		if (pc.getInventory().consumeItem(41064, 1)) {
		htmlid = "orcfhuwoomo6";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71042) {
		if (s.equalsIgnoreCase("A")) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		L1ItemInstance item = pc.getInventory().storeItem(41062, 1);
		String itemName = item.getItem().getName();
		String npcName = npc.getNpcTemplate().get_name();
		pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
		htmlid = "orcfbakumo4";
		} else if (s.equalsIgnoreCase("Z")) {
		if (pc.getInventory().consumeItem(41062, 1)) {
		htmlid = "orcfbakumo6";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71043) {
		if (s.equalsIgnoreCase("A")) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		L1ItemInstance item = pc.getInventory().storeItem(41063, 1);
		String itemName = item.getItem().getName();
		String npcName = npc.getNpcTemplate().get_name();
		pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
		htmlid = "orcfbuka4";
		} else if (s.equalsIgnoreCase("Z")) {
		if (pc.getInventory().consumeItem(41063, 1)) {
		htmlid = "orcfbuka6";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71044) {
		if (s.equalsIgnoreCase("A")) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		L1ItemInstance item = pc.getInventory().storeItem(41061, 1);
		String itemName = item.getItem().getName();
		String npcName = npc.getNpcTemplate().get_name();
		pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
		htmlid = "orcfkame4";
		} else if (s.equalsIgnoreCase("Z")) {
		if (pc.getInventory().consumeItem(41061, 1)) {
		htmlid = "orcfkame6";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71078) {
		if (s.equalsIgnoreCase("teleportURL")) {
		htmlid = "usender2";
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71080) {

		// 如果 NPC 的模板 ID 為 71080

		if (s.equalsIgnoreCase("teleportURL")) {

		// 如果字符串 s 等於 "teleportURL"

		pc.sendPackets(new S_SystemMessage("海音城堡地下城已經關閉。"));

		// 發送系統消息通知玩家 "海音城堡地下城已經關閉。"

		// htmlid = "amisoo2";

		// 註釋掉的代碼，本來應該設置 htmlid 為 "amisoo2"
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80049) {
		if (s.equalsIgnoreCase("1")) {
		if (pc.getKarma() <= -10000000) {
		pc.setKarma(1000000);
		pc.sendPackets(new S_ServerMessage(1078));
		pc.sendPackets(new S_Karma(pc));
		htmlid = "betray13";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80050) {
		if (s.equalsIgnoreCase("1")) {
		htmlid = "meet105";
		} else if (s.equalsIgnoreCase("2")) {
		if (pc.getInventory().checkItem(40718)) {
		htmlid = "meet106";
		} else {
		htmlid = "meet110";
		}
		} else if (s.equalsIgnoreCase("a")) {
		if (pc.getInventory().consumeItem(40718, 1)) {
		pc.addKarma((int) (-100 * Config.ServerRates.RateKarma));
		pc.sendPackets(new S_ServerMessage(1079));
		pc.sendPackets(new S_Karma(pc));
		htmlid = "meet107";
		} else {
		htmlid = "meet104";
		}
		} else if (s.equalsIgnoreCase("b")) {
		if (pc.getInventory().consumeItem(40718, 10)) {
		pc.addKarma((int) (-1000 * Config.ServerRates.RateKarma));
		pc.sendPackets(new S_ServerMessage(1079));
		pc.sendPackets(new S_Karma(pc));
		htmlid = "meet108";
		} else {
		htmlid = "meet104";
		}
		} else if (s.equalsIgnoreCase("c")) {
		if (pc.getInventory().consumeItem(40718, 100)) {
		pc.addKarma((int) (-10000 * Config.ServerRates.RateKarma));
		pc.sendPackets(new S_ServerMessage(1079));
		pc.sendPackets(new S_Karma(pc));
		htmlid = "meet109";
		} else {
		htmlid = "meet104";
		}
		} else if (s.equalsIgnoreCase("d")) {
		if (pc.getInventory().checkItem(40066)) {
		htmlid = "";
		} else {
		if (pc.getKarmaLevel() < -1) {
		pc.start_teleport(32683, 32895, 608, 5, 18339, true, false);
		}
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80052) {
		if (s.equalsIgnoreCase("a")) {

		// 如果字符串 s 等於 "a"

		if (pc.hasSkillEffect(STATUS_CURSE_YAHEE)) {

		// 如果玩家目前有「雅希的詛咒」效果

		pc.sendPackets(new S_ServerMessage(79));

		// 則發送系統消息 79（不能使用的情況）

		} else {

		// 如果玩家沒有「雅希的詛咒」效果

		pc.setSkillEffect(STATUS_CURSE_BARLOG, 1020 * 1000); // 1020 秒

		// 給予玩家「巴洛克的詛咒」效果，持續時間為 1020 秒

		pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA, 1, 1020));

		// 發送包裝盒，設置 1 號光環圖標，持續時間 1020 秒

		pc.sendPackets(new S_SkillSound(pc.getId(), 750));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 750));

		// 給予玩家技能音效 750 並廣播給其他玩家


		// pc.sendPackets(new S_ServerMessage(1127));

		// 註釋掉的代碼，可能原先用來發送系統消息 1127

		pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "雅希的軍隊已經被部署在各個地方。"));

		// 發送綠色訊息框，內容是「雅希的軍隊已經被部署在各個地方。」

		pc.sendPackets("授予權限：可攻擊巨大的魔族(巴洛克)");

		// 發送消息給玩家，內容是「授予權限：可攻擊巨大的魔族(巴洛克)」
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80053) {
		int karmaLevel = pc.getKarmaLevel();
		if (s.equalsIgnoreCase("a")) {
		int aliceMaterialId = 0;
		int[] aliceMaterialIdList = { 40991, 196, 197, 198, 199, 200, 201, 202, 203 };
		for (int id : aliceMaterialIdList) {
		if (pc.getInventory().checkItem(id)) {
		aliceMaterialId = id;
		break;
		}
		}
		if (aliceMaterialId == 0) {
		htmlid = "alice_no";
		} else if (aliceMaterialId == 40991) {
		if (karmaLevel <= -1) {
		materials = new int[] { 40995, 40718, 40991 };
		counts = new int[] { 100, 100, 1 };
		createitem = new int[] { 196 };
		createcount = new int[] { 1 };
		success_htmlid = "alice_1";
		failure_htmlid = "alice_no";
		} else {
		htmlid = "aliceyet";
		}
		} else if (aliceMaterialId == 196) {
		if (karmaLevel <= -2) {
		materials = new int[] { 40997, 40718, 196 };
		counts = new int[] { 100, 100, 1 };
		createitem = new int[] { 197 };
		createcount = new int[] { 1 };
		success_htmlid = "alice_2";
		failure_htmlid = "alice_no";
		} else {
		htmlid = "alice_1";
		}
		} else if (aliceMaterialId == 197) {
		if (karmaLevel <= -3) {
		materials = new int[] { 40990, 40718, 197 };
		counts = new int[] { 100, 100, 1 };
		createitem = new int[] { 198 };
		createcount = new int[] { 1 };
		success_htmlid = "alice_3";
		failure_htmlid = "alice_no";
		} else {
		htmlid = "alice_2";
		}
		} else if (aliceMaterialId == 198) {
		if (karmaLevel <= -4) {
		materials = new int[] { 40994, 40718, 198 };
		counts = new int[] { 50, 100, 1 };
		createitem = new int[] { 199 };
		createcount = new int[] { 1 };
		success_htmlid = "alice_4";
		failure_htmlid = "alice_no";
		} else {
		htmlid = "alice_3";
		}
		} else if (aliceMaterialId == 199) {
		if (karmaLevel <= -5) {
		materials = new int[] { 40993, 40718, 199 };
		counts = new int[] { 50, 100, 1 };
		createitem = new int[] { 200 };
		createcount = new int[] { 1 };
		success_htmlid = "alice_5";
		failure_htmlid = "alice_no";
		} else {
		htmlid = "alice_4";
		}
		} else if (aliceMaterialId == 200) {
		if (karmaLevel <= -6) {
		materials = new int[] { 40998, 40718, 200 };
		counts = new int[] { 50, 100, 1 };
		createitem = new int[] { 201 };
		createcount = new int[] { 1 };
		success_htmlid = "alice_6";
		failure_htmlid = "alice_no";
		} else {
		htmlid = "alice_5";
		}
		} else if (aliceMaterialId == 201) {
		if (karmaLevel <= -7) {
		materials = new int[] { 40996, 40718, 201 };
		counts = new int[] { 10, 100, 1 };
		createitem = new int[] { 202 };
		createcount = new int[] { 1 };
		success_htmlid = "alice_7";
		failure_htmlid = "alice_no";
		} else {
		htmlid = "alice_6";
		}
		} else if (aliceMaterialId == 202) {
		if (karmaLevel <= -8) {
		materials = new int[] { 40992, 40718, 202 };
		counts = new int[] { 10, 100, 1 };
		createitem = new int[] { 203 };
		createcount = new int[] { 1 };
		success_htmlid = "alice_8";
		failure_htmlid = "alice_no";
		} else {
		htmlid = "alice_7";
		}
		} else if (aliceMaterialId == 203) {
		htmlid = "alice_8";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50035) {
		if (s.equalsIgnoreCase("teleport i giranc-gate1")) { // 기란성 중문 안쪽
		pc.send_effect(12261, true);
		SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, 33633, 32698, 15482, SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_TEST);
		} else if (s.equalsIgnoreCase("teleport i giranc-gate2")) { // 기란성 외성문 안쪽
		pc.send_effect(12261, true);
		SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, 33633, 32721, 15482, SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_TEST);
		} else if (s.equalsIgnoreCase("teleport i giranc-tower")) { // 기란성 수호탑
		pc.send_effect(12261, true);
		SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, 33629, 32678, 15482, SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_TEST);
		} else if (s.equalsIgnoreCase("teleport i giranc-village")) { // 기란성 마을
		pc.send_effect(12261, true);
		SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, 33621, 32780, 15482, SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_TEST);
		} else {
		htmlid = "teleport i giranc-gate";
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80056) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (pc.getKarma() <= -10000000) {
		getBloodCrystalByKarma(pc, npc, s);
		}
		htmlid = "";
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80063) {
		if (s.equalsIgnoreCase("a")) {
		if (pc.getInventory().checkItem(40921)) {
		pc.getInventory().consumeItem(40921, 1);
		pc.start_teleport(32674, 32832, 603, 2, 18339, true, false);
		} else {
		htmlid = "gpass02";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80064) {
		if (s.equalsIgnoreCase("1")) {
		htmlid = "meet005";
		} else if (s.equalsIgnoreCase("2")) {
		if (pc.getInventory().checkItem(40678)) {
		htmlid = "meet006";
		} else {
		htmlid = "meet010";
		}
		} else if (s.equalsIgnoreCase("a")) {
		if (pc.getInventory().consumeItem(40678, 1)) {
		pc.addKarma((int) (100 * Config.ServerRates.RateKarma));
		pc.sendPackets(new S_ServerMessage(1078));
		pc.sendPackets(new S_Karma(pc));
		htmlid = "meet007";
		} else {
		htmlid = "meet004";
		}
		} else if (s.equalsIgnoreCase("b")) {
		if (pc.getInventory().consumeItem(40678, 10)) {
		pc.addKarma((int) (1000 * Config.ServerRates.RateKarma));
		pc.sendPackets(new S_ServerMessage(1078));
		pc.sendPackets(new S_Karma(pc));
		htmlid = "meet008";
		} else {
		htmlid = "meet004";
		}
		} else if (s.equalsIgnoreCase("c")) {
		if (pc.getInventory().consumeItem(40678, 100)) {
		pc.addKarma((int) (10000 * Config.ServerRates.RateKarma));
		pc.sendPackets(new S_ServerMessage(1078));
		pc.sendPackets(new S_Karma(pc));
		htmlid = "meet009";
		} else {
		htmlid = "meet004";
		}
		} else if (s.equalsIgnoreCase("d")) {
		if (pc.getInventory().checkItem(40066)) {
		htmlid = "";
		} else {
		if (pc.getKarmaLevel() > 1) {
		pc.start_teleport(32674, 32832, 602, 2, 18339, true, false);
		}
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80066) {
		if (s.equalsIgnoreCase("1")) {
		if (pc.getKarma() >= 10000000) {
		pc.setKarma(-1000000);
		pc.sendPackets(new S_ServerMessage(1079));
		pc.sendPackets(new S_Karma(pc));
		htmlid = "betray03";
		}
		}
		}

		else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80073) {

		// 如果 NPC 的模板 ID 為 80073

		if (s.equalsIgnoreCase("a")) {

		// 如果字符串 s 等於 "a"

		if (pc.hasSkillEffect(STATUS_CURSE_BARLOG)) {

		// 如果玩家目前有「巴洛克的詛咒」效果

		pc.sendPackets(new S_ServerMessage(79));

		// 則發送系統消息 79（不能使用的情況）

		} else {

		// 如果玩家沒有「巴洛克的詛咒」效果

		pc.setSkillEffect(STATUS_CURSE_YAHEE, 1020 * 1000); // 1020 秒

		// 給予玩家「雅希的詛咒」效果，持續時間為 1020 秒

		pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA, 2, 1020));

		// 發送包裝盒，設置 2 號光環圖標，持續時間 1020 秒

		pc.sendPackets(new S_SkillSound(pc.getId(), 750));
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 750));

		// 給予玩家技能音效 750 並廣播給其他玩家


		// pc.sendPackets(new S_ServerMessage(1127));

		// 註釋掉的代碼，可能原先用來發送系統消息 1127

		pc.sendPackets("授予權限：可攻擊巨大的魔族(混亂-死亡-墮落-雅希)");

		// 發送消息給玩家，內容是「授予權限：可攻擊巨大的魔族(混亂-死亡-墮落-雅希)」
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80072) {
		int karmaLevel = pc.getKarmaLevel();
		if (s.equalsIgnoreCase("0")) {
		htmlid = "lsmitha";
		} else if (s.equalsIgnoreCase("1")) {
		htmlid = "lsmithb";
		} else if (s.equalsIgnoreCase("2")) {
		htmlid = "lsmithc";
		} else if (s.equalsIgnoreCase("3")) {
		htmlid = "lsmithd";
		} else if (s.equalsIgnoreCase("4")) {
		htmlid = "lsmithe";
		} else if (s.equalsIgnoreCase("5")) {
		htmlid = "lsmithf";
		} else if (s.equalsIgnoreCase("6")) {
		htmlid = "";
		} else if (s.equalsIgnoreCase("7")) {
		htmlid = "lsmithg";
		} else if (s.equalsIgnoreCase("8")) {
		htmlid = "lsmithh";
		} else if (s.equalsIgnoreCase("a") && karmaLevel >= 1) {
		materials = new int[] { 20158, 40669, 40678 };
		counts = new int[] { 1, 50, 100 };
		createitem = new int[] { 20083 };
		createcount = new int[] { 1 };
		success_htmlid = "";
		failure_htmlid = "lsmithaa";
		} else if (s.equalsIgnoreCase("b") && karmaLevel >= 2) {
		materials = new int[] { 20144, 40672, 40678 };
		counts = new int[] { 1, 50, 100 };
		createitem = new int[] { 20131 };
		createcount = new int[] { 1 };
		success_htmlid = "";
		failure_htmlid = "lsmithbb";
		} else if (s.equalsIgnoreCase("c") && karmaLevel >= 3) {
		materials = new int[] { 20075, 40671, 40678 };
		counts = new int[] { 1, 50, 100 };
		createitem = new int[] { 20069 };
		createcount = new int[] { 1 };
		success_htmlid = "";
		failure_htmlid = "lsmithcc";
		} else if (s.equalsIgnoreCase("d") && karmaLevel >= 4) {
		materials = new int[] { 20183, 40674, 40678 };
		counts = new int[] { 1, 20, 100 };
		createitem = new int[] { 20179 };
		createcount = new int[] { 1 };
		success_htmlid = "";
		failure_htmlid = "lsmithdd";
		} else if (s.equalsIgnoreCase("e") && karmaLevel >= 5) {
		materials = new int[] { 20190, 40674, 40678 };
		counts = new int[] { 1, 40, 100 };
		createitem = new int[] { 20209 };
		createcount = new int[] { 1 };
		success_htmlid = "";
		failure_htmlid = "lsmithee";
		} else if (s.equalsIgnoreCase("f") && karmaLevel >= 6) {
		materials = new int[] { 20078, 40674, 40678 };
		counts = new int[] { 1, 5, 100 };
		createitem = new int[] { 20290 };
		createcount = new int[] { 1 };
		success_htmlid = "";
		failure_htmlid = "lsmithff";
		} else if (s.equalsIgnoreCase("g") && karmaLevel >= 7) {
		materials = new int[] { 20078, 40670, 40678 };
		counts = new int[] { 1, 1, 100 };
		createitem = new int[] { 20261 };
		createcount = new int[] { 1 };
		success_htmlid = "";
		failure_htmlid = "lsmithgg";
		} else if (s.equalsIgnoreCase("h") && karmaLevel >= 8) {
		materials = new int[] { 40719, 40673, 40678 };
		counts = new int[] { 1, 1, 100 };
		createitem = new int[] { 20031 };
		createcount = new int[] { 1 };
		success_htmlid = "";
		failure_htmlid = "lsmithhh";
		}
		// 라폰스시작
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80074) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (pc.getKarma() >= 10000000) {
		getSoulCrystalByKarma(pc, npc, s);
		}
		htmlid = "";
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50007) { // 에스메랄다
		int[][] ghostloc = { { 32642, 34249, 32878, 33114, 32734, 32736, 32737, 32769, 33931, 33051, 32870, 33972, 33427, 33594, 33446, 32741 },
		{ 32956, 33453, 32653, 32939, 32831, 32814, 32684, 32800, 33347, 32340, 33254, 33363, 32814, 33244, 32757, 32284, }, { 0, 4, 4, 4, 278, 62, 63, 77, 4, 4, 4, 4, 4, 4, 4, 4, } };
		if (s.equalsIgnoreCase("journey")) {
		pc.ghosttime = System.currentTimeMillis() + 8000;
		pc.beginGhost();
		pc.start_teleport(ghostloc[0][pc._ghostCount], ghostloc[1][pc._ghostCount], ghostloc[2][pc._ghostCount], 5, 18339, true, false);
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80057) {
		htmlid = karmaLevelToHtmlId(pc.getKarmaLevel());
		htmldata = new String[] { String.valueOf(pc.getKarmaPercent()) };
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80059 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80060 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80061
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80062) {
		htmlid = talkToDimensionDoor(pc, (L1NpcInstance) obj, s);
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 81124) {
		if (s.equalsIgnoreCase("1")) {
		poly(client, 4002);
		htmlid = "";
		} else if (s.equalsIgnoreCase("2")) {
		poly(client, 4004);
		htmlid = "";
		} else if (s.equalsIgnoreCase("3")) {
		poly(client, 4950);
		htmlid = "";
		}
		} else if (s.equalsIgnoreCase("contract1")) {
		pc.getQuest().set_step(L1Quest.QUEST_LYRA, 1);
		htmlid = "lyraev2";
		} else if (s.equalsIgnoreCase("contract1yes") || s.equalsIgnoreCase("contract1no")) {

		if (s.equalsIgnoreCase("contract1yes")) {
		htmlid = "lyraev5";
		} else if (s.equalsIgnoreCase("contract1no")) {
		pc.getQuest().set_step(L1Quest.QUEST_LYRA, 0);
		htmlid = "lyraev4";
		}
		int totem = 0;
		if (pc.getInventory().checkItem(40131)) {
		totem++;
		}
		if (pc.getInventory().checkItem(40132)) {
		totem++;
		}
		if (pc.getInventory().checkItem(40133)) {
		totem++;
		}
		if (pc.getInventory().checkItem(40134)) {
		totem++;
		}
		if (pc.getInventory().checkItem(40135)) {
		totem++;
		}
		if (totem != 0) {
		materials = new int[totem];
		counts = new int[totem];
		createitem = new int[totem];
		createcount = new int[totem];

		totem = 0;
		if (pc.getInventory().checkItem(40131)) {
		L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40131);
		int i1 = l1iteminstance.getCount();
		materials[totem] = 40131;
		counts[totem] = i1;
		createitem[totem] = L1ItemId.ADENA;
		createcount[totem] = i1 * 50;
		totem++;
		}
		if (pc.getInventory().checkItem(40132)) {
		L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40132);
		int i1 = l1iteminstance.getCount();
		materials[totem] = 40132;
		counts[totem] = i1;
		createitem[totem] = L1ItemId.ADENA;
		createcount[totem] = i1 * 100;
		totem++;
		}
		if (pc.getInventory().checkItem(40133)) {
		L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40133);
		int i1 = l1iteminstance.getCount();
		materials[totem] = 40133;
		counts[totem] = i1;
		createitem[totem] = L1ItemId.ADENA;
		createcount[totem] = i1 * 50;
		totem++;
		}
		if (pc.getInventory().checkItem(40134)) {
		L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40134);
		int i1 = l1iteminstance.getCount();
		materials[totem] = 40134;
		counts[totem] = i1;
		createitem[totem] = L1ItemId.ADENA;
		createcount[totem] = i1 * 30;
		totem++;
		}
		if (pc.getInventory().checkItem(40135)) {
		L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40135);
		int i1 = l1iteminstance.getCount();
		materials[totem] = 40135;
		counts[totem] = i1;
		createitem[totem] = L1ItemId.ADENA;
		createcount[totem] = i1 * 200;
		totem++;
		}
		}
		} else if (s.equalsIgnoreCase("pandora6") || s.equalsIgnoreCase("cold6") || s.equalsIgnoreCase("balsim3") || s.equalsIgnoreCase("mellin3") || s.equalsIgnoreCase("glen3")) {
		htmlid = s;
		int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
		int taxRatesCastle = L1CastleLocation.getCastleTaxRateByNpcId(npcid);
		htmldata = new String[] { String.valueOf(taxRatesCastle) };
		} else if (s.equalsIgnoreCase("set")) {
		if (obj instanceof L1NpcInstance) {
		int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
		int town_id = L1TownLocation.getTownIdByNpcid(npcid);

		if (town_id >= 1 && town_id <= 10) {
		if (pc.getHomeTownId() == -1) {
		pc.sendPackets(new S_ServerMessage(759));
		htmlid = "";
		} else if (pc.getHomeTownId() > 0) {
		if (pc.getHomeTownId() != town_id) {
		L1Town town = TownTable.getInstance().getTownTable(pc.getHomeTownId());
		if (town != null) {
		pc.sendPackets(new S_ServerMessage(758, town.get_name()));
		}
		htmlid = "";
		} else {
		htmlid = "";
		}
		} else if (pc.getHomeTownId() == 0) {
		if (pc.getLevel() < 10) {
		pc.sendPackets(new S_ServerMessage(757));
		htmlid = "";
		} else {
		int level = pc.getLevel();
		int cost = level * level * 10;
		if (pc.getInventory().consumeItem(L1ItemId.ADENA, cost)) {
		pc.setHomeTownId(town_id);
		pc.setContribution(0);
		pc.save();
		} else {
		pc.sendPackets(new S_ServerMessage(337, "$4"));
		}
		htmlid = "";
		}
		}
		}
		}
		} else if (s.equalsIgnoreCase("clear")) {
		if (obj instanceof L1NpcInstance) {
		int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
		int town_id = L1TownLocation.getTownIdByNpcid(npcid);
		if (town_id > 0) {
		if (pc.getHomeTownId() > 0) {
		if (pc.getHomeTownId() == town_id) {
		pc.setHomeTownId(-1);
		pc.setContribution(0);
		} else {
		pc.sendPackets(new S_ServerMessage(756));
		}
		}
		htmlid = "";
		}
		}
		} else if (s.equalsIgnoreCase("ask")) {
		if (obj instanceof L1NpcInstance) {
		int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
		int town_id = L1TownLocation.getTownIdByNpcid(npcid);

		if (town_id >= 1 && town_id <= 10) {
		L1Town town = TownTable.getInstance().getTownTable(town_id);
		String leader = town.get_leader_name();
		if (leader != null && leader.length() != 0) {
		htmlid = "owner";
		htmldata = new String[] { leader };
		} else {
		htmlid = "noowner";
		}
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70534 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70556 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70572
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70631 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70663 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70761
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70788 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70806 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70830
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70876) {
		if (s.equalsIgnoreCase("r")) {
		if (obj instanceof L1NpcInstance) {
		}
		} else if (s.equalsIgnoreCase("t")) {

		} else if (s.equalsIgnoreCase("c")) {

		}

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71005) {
		if (s.equalsIgnoreCase("0")) {
		if (!pc.getInventory().checkItem(41209)) {
final int[] item_ids = { 41209, };
final int[] item_amounts = { 1, };
		for (int i = 0; i < item_ids.length; i++) {
		L1ItemInstance item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		}
		}
		htmlid = "";
		} else if (s.equalsIgnoreCase("1")) {
		if (pc.getInventory().consumeItem(41213, 1)) {
final int[] item_ids = { 40029, };
final int[] item_amounts = { 20, };
		L1ItemInstance item = null;
		for (int i = 0; i < item_ids.length; i++) {
		item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName() + " (" + item_amounts[i] + ")"));
		}
		htmlid = "";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71006) {
		if (s.equalsIgnoreCase("0")) {
		if (pc.getLevel() > 25) {
		htmlid = "jpe0057";
		} else if (pc.getInventory().checkItem(41213)) {
		htmlid = "jpe0056";
		} else if (pc.getInventory().checkItem(41210) || pc.getInventory().checkItem(41211)) {
		htmlid = "jpe0055";
		} else if (pc.getInventory().checkItem(41209)) {
		htmlid = "jpe0054";
		} else if (pc.getInventory().checkItem(41212)) {
		htmlid = "jpe0056";
		materials = new int[] { 41212 };
		counts = new int[] { 1 };
		createitem = new int[] { 41213 };
		createcount = new int[] { 1 };
		} else {
		htmlid = "jpe0057";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70512) {
		if (s.equalsIgnoreCase("0") || s.equalsIgnoreCase("fullheal")) {
		int hp = _random.nextInt(21) + 70;
		pc.setCurrentHp(pc.getCurrentHp() + hp);
		pc.sendPackets(new S_ServerMessage(77));
		pc.sendPackets(new S_SkillSound(pc.getId(), 830));
		pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
		htmlid = "";
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71025) {
		if (s.equalsIgnoreCase("0")) {
final int[] item_ids = { 41225, };
final int[] item_amounts = { 1, };
		for (int i = 0; i < item_ids.length; i++) {
		L1ItemInstance item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		}
		htmlid = "jpe0083";
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71055) {
		if (s.equalsIgnoreCase("0")) {
final int[] item_ids = { 40701, };
final int[] item_amounts = { 1, };
		L1ItemInstance item = null;
		for (int i = 0; i < item_ids.length; i++) {
		item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		}
		pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 1);
		htmlid = "lukein8";
		}
		if (s.equalsIgnoreCase("1")) {
		pc.getQuest().set_end(L1Quest.QUEST_TBOX3);
		materials = new int[] { 40716 }; // 할아버지의 보물
		counts = new int[] { 1 };
		createitem = new int[] { 20269 }; // 해골목걸이
		createcount = new int[] { 1 };
		htmlid = "lukein0";
		} else if (s.equalsIgnoreCase("2")) {
		htmlid = "lukein12";
		pc.getQuest().set_step(L1Quest.QUEST_RESTA, 3);
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71063) {
		if (s.equalsIgnoreCase("0")) {
		materials = new int[] { 40701 };
		counts = new int[] { 1 };
		createitem = new int[] { 40702 };
		createcount = new int[] { 1 };
		htmlid = "maptbox1";
		pc.getQuest().set_end(L1Quest.QUEST_TBOX1);
		int[] nextbox = { 1, 2, 3 };
		int pid = _random.nextInt(nextbox.length);
		int nb = nextbox[pid];
		if (nb == 1) {
		pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 2);
		} else if (nb == 2) {
		pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 3);
		} else if (nb == 3) {
		pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 4);
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71064 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71065 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71066) {
		if (s.equalsIgnoreCase("0")) {
		materials = new int[] { 40701 };
		counts = new int[] { 1 };
		createitem = new int[] { 40702 };
		createcount = new int[] { 1 };
		htmlid = "maptbox1";
		pc.getQuest().set_end(L1Quest.QUEST_TBOX2);
		int[] nextbox2 = { 1, 2, 3, 4, 5, 6 };
		int pid = _random.nextInt(nextbox2.length);
		int nb2 = nextbox2[pid];
		if (nb2 == 1) {
		pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 5);
		} else if (nb2 == 2) {
		pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 6);
		} else if (nb2 == 3) {
		pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 7);
		} else if (nb2 == 4) {
		pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 8);
		} else if (nb2 == 5) {
		pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 9);
		} else if (nb2 == 6) {
		pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 10);
		}
		}

		// 작은 상자-3번째
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71067 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71068 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71069
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71070 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71071 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71072) {
		if (s.equalsIgnoreCase("0")) {
		htmlid = "maptboxi";
		materials = new int[] { 40701 }; // 작은 보물의 지도
		counts = new int[] { 1 };
		createitem = new int[] { 40716 }; // 할아버지의 보물
		createcount = new int[] { 1 };
		pc.getQuest().set_end(L1Quest.QUEST_TBOX3);
		pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 11);
		}

		// 시미즈(해적섬)
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71056) {
		// 아들을 찾는다
		if (s.equalsIgnoreCase("a")) {
		pc.getQuest().set_step(L1Quest.QUEST_SIMIZZ, 1);
		htmlid = "SIMIZZ7";
		} else if (s.equalsIgnoreCase("b")) {
		if (pc.getInventory().checkItem(40661) && pc.getInventory().checkItem(40662) && pc.getInventory().checkItem(40663)) {
		htmlid = "SIMIZZ8";
		pc.getQuest().set_step(L1Quest.QUEST_SIMIZZ, 2);
		materials = new int[] { 40661, 40662, 40663 };
		counts = new int[] { 1, 1, 1 };
		createitem = new int[] { 20044 };
		createcount = new int[] { 1 };
		} else {
		htmlid = "SIMIZZ9";
		}
		} else if (s.equalsIgnoreCase("d")) {
		htmlid = "SIMIZZ12";
		pc.getQuest().set_step(L1Quest.QUEST_SIMIZZ, L1Quest.QUEST_END);
		}

		// 도일(해적섬)
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71057) {
		// 러쉬에 대해 듣는다
		if (s.equalsIgnoreCase("3")) {
		htmlid = "doil4";
		} else if (s.equalsIgnoreCase("6")) {
		htmlid = "doil6";
		} else if (s.equalsIgnoreCase("1")) {
		if (pc.getInventory().checkItem(40714)) {
		htmlid = "doil8";
		materials = new int[] { 40714 };
		counts = new int[] { 1 };
		createitem = new int[] { 40647 };
		createcount = new int[] { 1 };
		pc.getQuest().set_step(L1Quest.QUEST_DOIL, L1Quest.QUEST_END);
		} else {
		htmlid = "doil7";
		}
		}

		// 루디 안(해적섬)
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71059) {
		// 루디 안의 부탁을 받아들인다
		if (s.equalsIgnoreCase("A")) {
		htmlid = "rudian6";
final int[] item_ids = { 40700 };
final int[] item_amounts = { 1 };
		L1ItemInstance item = null;
		for (int i = 0; i < item_ids.length; i++) {
		item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		}
		pc.getQuest().set_step(L1Quest.QUEST_RUDIAN, 1);
		} else if (s.equalsIgnoreCase("B")) {
		if (pc.getInventory().checkItem(40710)) {
		htmlid = "rudian8";
		materials = new int[] { 40700, 40710 };
		counts = new int[] { 1, 1 };
		createitem = new int[] { 40647 };
		createcount = new int[] { 1 };
		pc.getQuest().set_step(L1Quest.QUEST_RUDIAN, L1Quest.QUEST_END);
		} else {
		htmlid = "rudian9";
		}
		}

		// 레스타(해적섬)
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71060) {
		// 동료들에 대해
		if (s.equalsIgnoreCase("A")) {
		if (pc.getQuest().get_step(L1Quest.QUEST_RUDIAN) == L1Quest.QUEST_END) {
		htmlid = "resta6";
		} else {
		htmlid = "resta4";
		}
		} else if (s.equalsIgnoreCase("B")) {
		htmlid = "resta10";
		pc.getQuest().set_step(L1Quest.QUEST_RESTA, 2);
		}

		// 카좀스(해적섬)
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71061) {
		// 지도를 조합해 주세요
		if (s.equalsIgnoreCase("A")) {
		if (pc.getInventory().checkItem(40647, 3)) {
		htmlid = "cadmus6";
		pc.getInventory().consumeItem(40647, 3);
		pc.getQuest().set_step(L1Quest.QUEST_CADMUS, 2);
		} else {
		htmlid = "cadmus5";
		pc.getQuest().set_step(L1Quest.QUEST_CADMUS, 1);
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71036) {
		if (s.equalsIgnoreCase("a")) {
		htmlid = "kamyla7";
		pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 1);
		} else if (s.equalsIgnoreCase("c")) {
		htmlid = "kamyla10";
		pc.getInventory().consumeItem(40644, 1);
		pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 3);
		} else if (s.equalsIgnoreCase("e")) {
		htmlid = "kamyla13";
		pc.getInventory().consumeItem(40630, 1);
		pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 4);
		} else if (s.equalsIgnoreCase("i")) {
		htmlid = "kamyla25";
		} else if (s.equalsIgnoreCase("b")) { // 카 미라(흐랑코의 미궁)
		if (pc.getQuest().get_step(L1Quest.QUEST_KAMYLA) == 1) {
		pc.start_teleport(32679, 32742, 482, 5, 18339, true, false);
		}
		} else if (s.equalsIgnoreCase("d")) { // 카 미라(디에고가 닫힌 뇌)
		if (pc.getQuest().get_step(L1Quest.QUEST_KAMYLA) == 3) {
		pc.start_teleport(32736, 32800, 483, 5, 18339, true, false);
		}
		} else if (s.equalsIgnoreCase("f")) { // 카 미라(호세 지하소굴)
		if (pc.getQuest().get_step(L1Quest.QUEST_KAMYLA) == 4) {
		pc.start_teleport(32746, 32807, 484, 5, 18339, true, false);
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71089) {
		if (s.equalsIgnoreCase("a")) {
		htmlid = "francu10";
final int[] item_ids = { 40644 };
final int[] item_amounts = { 1 };
		L1ItemInstance item = null;
		for (int i = 0; i < item_ids.length; i++) {
		item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 2);
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71090) {
		if (s.equalsIgnoreCase("a")) {
		htmlid = "";
final int[] item_ids = { 246, 247, 248, 249, 40660 };
final int[] item_amounts = { 1, 1, 1, 1, 5 };
		L1ItemInstance item = null;
		for (int i = 0; i < item_ids.length; i++) {
		item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, 1);
		}
		} else if (s.equalsIgnoreCase("b")) {
		if (pc.getInventory().checkEquipped(246) || pc.getInventory().checkEquipped(247) || pc.getInventory().checkEquipped(248) || pc.getInventory().checkEquipped(249)) {
		htmlid = "jcrystal5";
		} else if (pc.getInventory().checkItem(40660)) {
		htmlid = "jcrystal4";
		} else {
		pc.getInventory().consumeItem(246, 1);
		pc.getInventory().consumeItem(247, 1);
		pc.getInventory().consumeItem(248, 1);
		pc.getInventory().consumeItem(249, 1);
		pc.getInventory().consumeItem(40620, 1);
		pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, 2);
		pc.start_teleport(32801, 32895, 483, 5, 18339, true, false);
		}
		} else if (s.equalsIgnoreCase("c")) {
		if (pc.getInventory().checkEquipped(246) || pc.getInventory().checkEquipped(247) || pc.getInventory().checkEquipped(248) || pc.getInventory().checkEquipped(249)) {
		htmlid = "jcrystal5";
		} else {
		pc.getInventory().checkItem(40660);
		L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40660);
		int sc = l1iteminstance.getCount();
		if (sc > 0) {
		pc.getInventory().consumeItem(40660, sc);
		} else {
		}
		pc.getInventory().consumeItem(246, 1);
		pc.getInventory().consumeItem(247, 1);
		pc.getInventory().consumeItem(248, 1);
		pc.getInventory().consumeItem(249, 1);
		pc.getInventory().consumeItem(40620, 1);
		pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, 0);
		pc.start_teleport(32736, 32800, 483, 5, 18339, true, false);
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71091) {
		if (s.equalsIgnoreCase("a")) {
		htmlid = "";
		pc.getInventory().consumeItem(40654, 1);
		pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, L1Quest.QUEST_END);
		pc.start_teleport(32744, 32927, 483, 5, 18339, true, false);
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71074) {
		if (s.equalsIgnoreCase("A")) {
		htmlid = "lelder5";
		pc.getQuest().set_step(L1Quest.QUEST_LIZARD, 1);
		} else if (s.equalsIgnoreCase("B")) {
		htmlid = "lelder10";
		pc.getInventory().consumeItem(40633, 1);
		pc.getQuest().set_step(L1Quest.QUEST_LIZARD, 3);
		} else if (s.equalsIgnoreCase("C")) {
		htmlid = "lelder13";
		materials = new int[] { 40634 };
		counts = new int[] { 1 };
		createitem = new int[] { 20167 }; // 리자드망로브
		createcount = new int[] { 1 };
		pc.getQuest().set_step(L1Quest.QUEST_LIZARD, L1Quest.QUEST_END);
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71198) {
		if (s.equalsIgnoreCase("A")) {
		if (pc.getQuest().get_step(71198) != 0 || pc.getInventory().checkItem(21059, 1)) {
		return;
		}
		if (pc.getInventory().consumeItem(41339, 5)) {
		L1ItemInstance item = ItemTable.getInstance().createItem(41340);
		if (item != null) {
		if (pc.getInventory().checkAddItem(item, 1) == 0) {
		pc.getInventory().storeItem(item);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		}
		}
		pc.getQuest().set_step(71198, 1);
		htmlid = "tion4";
		} else {
		htmlid = "tion9";
		}
		} else if (s.equalsIgnoreCase("B")) {
		if (pc.getQuest().get_step(71198) != 1 || pc.getInventory().checkItem(21059, 1)) {
		return;
		}
		if (pc.getInventory().consumeItem(41341, 1)) {
		pc.getQuest().set_step(71198, 2);
		htmlid = "tion5";
		} else {
		htmlid = "tion10";
		}
		} else if (s.equalsIgnoreCase("C")) {
		if (pc.getQuest().get_step(71198) != 2 || pc.getInventory().checkItem(21059, 1)) {
		return;
		}
		if (pc.getInventory().consumeItem(41343, 1)) {
		L1ItemInstance item = ItemTable.getInstance().createItem(21057);
		if (item != null) {
		if (pc.getInventory().checkAddItem(item, 1) == 0) {
		pc.getInventory().storeItem(item);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		}
		}
		pc.getQuest().set_step(71198, 3);
		htmlid = "tion6";
		} else {
		htmlid = "tion12";
		}
		} else if (s.equalsIgnoreCase("D")) {
		if (pc.getQuest().get_step(71198) != 3 || pc.getInventory().checkItem(21059, 1)) {
		return;
		}
		if (pc.getInventory().consumeItem(41344, 1)) {
		L1ItemInstance item = ItemTable.getInstance().createItem(21058);
		if (item != null) {
		pc.getInventory().consumeItem(21057, 1);
		if (pc.getInventory().checkAddItem(item, 1) == 0) {
		pc.getInventory().storeItem(item);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		}
		}
		pc.getQuest().set_step(71198, 4);
		htmlid = "tion7";
		} else {
		htmlid = "tion13";
		}
		} else if (s.equalsIgnoreCase("E")) {
		if (pc.getQuest().get_step(71198) != 4 || pc.getInventory().checkItem(21059, 1)) {
		return;
		}
		if (pc.getInventory().consumeItem(41345, 1)) {
		L1ItemInstance item = ItemTable.getInstance().createItem(21059);
		if (item != null) {
		pc.getInventory().consumeItem(21058, 1);
		if (pc.getInventory().checkAddItem(item, 1) == 0) {
		pc.getInventory().storeItem(item);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		}
		}
		pc.getQuest().set_step(71198, 0);
		pc.getQuest().set_step(71199, 0);
		htmlid = "tion8";
		} else {
		htmlid = "tion15";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71199) {
		if (s.equalsIgnoreCase("A")) {
		if (pc.getQuest().get_step(71199) != 0 || pc.getInventory().checkItem(21059, 1)) {
		return;
		}
		if (pc.getInventory().checkItem(41340, 1)) {
		pc.getQuest().set_step(71199, 1);
		htmlid = "jeron2";
		} else {
		htmlid = "jeron10";
		}
		} else if (s.equalsIgnoreCase("B")) {
		if (pc.getQuest().get_step(71199) != 1 || pc.getInventory().checkItem(21059, 1)) {
		return;
		}
		if (pc.getInventory().consumeItem(40308, 1000000)) {
		L1ItemInstance item = ItemTable.getInstance().createItem(41341);
		if (item != null) {
		if (pc.getInventory().checkAddItem(item, 1) == 0) {
		pc.getInventory().storeItem(item);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		}
		}
		pc.getInventory().consumeItem(41340, 1);
		pc.getQuest().set_step(71199, 255);
		htmlid = "jeron6";
		} else {
		htmlid = "jeron8";
		}
		} else if (s.equalsIgnoreCase("C")) {
		if (pc.getQuest().get_step(71199) != 1 || pc.getInventory().checkItem(21059, 1)) {
		return;
		}
		if (pc.getInventory().consumeItem(41342, 1)) {
		L1ItemInstance item = ItemTable.getInstance().createItem(41341);
		if (item != null) {
		if (pc.getInventory().checkAddItem(item, 1) == 0) {
		pc.getInventory().storeItem(item);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		}
		}
		pc.getInventory().consumeItem(41340, 1);
		pc.getQuest().set_step(71199, 255);
		htmlid = "jeron5";
		} else {
		htmlid = "jeron9";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80079) {
		if (s.equalsIgnoreCase("0")) {
		if (!pc.getInventory().checkItem(41312)) {
		L1ItemInstance item = pc.getInventory().storeItem(41312, 1);
		if (item != null) {
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		pc.getQuest().set_step(L1Quest.QUEST_KEPLISHA, L1Quest.QUEST_END);
		}
		htmlid = "keplisha7";
		}
		} else if (s.equalsIgnoreCase("1")) {
		if (!pc.getInventory().checkItem(41314)) {
		if (pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
		materials = new int[] { L1ItemId.ADENA, 41313 };
		counts = new int[] { 1000, 1 };
		createitem = new int[] { 41314 };
		createcount = new int[] { 1 };
		int htmlA = _random.nextInt(3) + 1;
		int htmlB = _random.nextInt(100) + 1;
		switch (htmlA) {
		case 1:
		htmlid = "horosa" + htmlB; // horosa1 ~ horosa100
		break;
		case 2:
		htmlid = "horosb" + htmlB; // horosb1 ~ horosb100
		break;
		case 3:
		htmlid = "horosc" + htmlB; // horosc1 ~ horosc100
		break;
default:
		break;
		}
		} else {
		htmlid = "keplisha8";
		}
		}
		} else if (s.equalsIgnoreCase("2")) {
		if (pc.getCurrentSpriteId() != pc.getClassId()) {
		htmlid = "keplisha9";
		} else {
		if (pc.getInventory().checkItem(41314)) {
		pc.getInventory().consumeItem(41314, 1);
		int html = _random.nextInt(9) + 1;
		int PolyId = 6180 + _random.nextInt(40);
		polyByKeplisha(client, PolyId);
		switch (html) {
		case 1:
		htmlid = "horomon11";
		break;
		case 2:
		htmlid = "horomon12";
		break;
		case 3:
		htmlid = "horomon13";
		break;
		case 4:
		htmlid = "horomon21";
		break;
		case 5:
		htmlid = "horomon22";
		break;
		case 6:
		htmlid = "horomon23";
		break;
		case 7:
		htmlid = "horomon31";
		break;
		case 8:
		htmlid = "horomon32";
		break;
		case 9:
		htmlid = "horomon33";
		break;
default:
		break;
		}
		}
		}
		} else if (s.equalsIgnoreCase("3")) {
		if (pc.getInventory().checkItem(41312)) {
		pc.getInventory().consumeItem(41312, 1);
		htmlid = "";
		}
		if (pc.getInventory().checkItem(41313)) {
		pc.getInventory().consumeItem(41313, 1);
		htmlid = "";
		}
		if (pc.getInventory().checkItem(41314)) {
		pc.getInventory().consumeItem(41314, 1);
		htmlid = "";
		}
		}

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71181) { // 에마이
		if (s.equalsIgnoreCase("A")) { // 곰인형
		if (pc.getInventory().consumeItem(41093, 1)) {
		pc.getInventory().storeItem(41097, 1);
		htmlid = "my5";
		} else {
		htmlid = "my4";
		}
		} else if (s.equalsIgnoreCase("B")) { // 향수
		if (pc.getInventory().consumeItem(41094, 1)) {
		pc.getInventory().storeItem(41918, 1);
		htmlid = "my6";
		} else {
		htmlid = "my4";
		}
		} else if (s.equalsIgnoreCase("C")) { // 드레스
		if (pc.getInventory().consumeItem(41095, 1)) {
		pc.getInventory().storeItem(41919, 1);
		htmlid = "my7";
		} else {
		htmlid = "my4";
		}
		} else if (s.equalsIgnoreCase("D")) { // 반지
		if (pc.getInventory().consumeItem(41096, 1)) {
		pc.getInventory().storeItem(41920, 1);
		htmlid = "my8";
		} else {
		htmlid = "my4";
		}
		}

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80084) {
		if (s.equalsIgnoreCase("q")) {
		if (pc.getInventory().checkItem(41356, 1)) {
		htmlid = "rparum4";
		} else {
		L1ItemInstance item = pc.getInventory().storeItem(41356, 1);
		if (item != null) {
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		}
		htmlid = "rparum3";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80105) {
		if (s.equalsIgnoreCase("c")) {
		if (pc.isCrown()) {
		if (pc.getInventory().checkItem(20383, 1)) {
		if (pc.getInventory().checkItem(L1ItemId.ADENA, 100000)) {
		L1ItemInstance item = pc.getInventory().findItemId(20383);
		if (item != null && item.getChargeCount() != 50) {
		item.setChargeCount(50);
		pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
		pc.getInventory().consumeItem(L1ItemId.ADENA, 100000);
		htmlid = "";
		}
		} else {
		pc.sendPackets(new S_ServerMessage(337, "$4"));
		}
		}
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4202000) { // 용기사
		// 피에나
		if (s.equalsIgnoreCase("teleportURL") && pc.isDragonknight()) {
		htmlid = "feaena3";
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4201000) { // 환술사
		// 아샤
		if (s.equalsIgnoreCase("teleportURL") && pc.isBlackwizard()) {
		htmlid = "asha3";
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 200015) {
		if (s.equalsIgnoreCase("0")) {
		if (pc.getInventory().checkItem(200000)) {
		htmlid = "candleg3";
		} else {
		pc.getInventory().storeItem(200000, 1);
		htmlid = "candleg2";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 3000003) {
		if (s.equalsIgnoreCase("a")) {
		htmlid = "prokel3";
		pc.getInventory().storeItem(210087, 1);
		pc.getQuest().set_step(L1Quest.QUEST_LEVEL15, 1);
		} else if (s.equalsIgnoreCase("b")) {
		if (pc.getInventory().checkItem(210088) || pc.getInventory().checkItem(210089) || pc.getInventory().checkItem(210090)) {
		htmlid = "prokel5";
		pc.getInventory().consumeItem(210088, 1);
		pc.getInventory().consumeItem(210089, 1);
		pc.getInventory().consumeItem(210090, 1);
		pc.getInventory().storeItem(502, 1);
		pc.getInventory().storeItem(210020, 1);
		pc.getQuest().set_step(L1Quest.QUEST_LEVEL15, 255);
		} else {
		htmlid = "prokel6";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 3100004) {
		if (s.equalsIgnoreCase("a")) {
		htmlid = "silrein4";
		pc.getInventory().storeItem(210092, 5);
		pc.getInventory().storeItem(210093, 1);
		pc.getQuest().set_step(L1Quest.QUEST_LEVEL15, 1);
		} else if (s.equalsIgnoreCase("b")) {
		if (pc.getInventory().checkItem(210091, 10) || pc.getInventory().checkItem(40510) || pc.getInventory().checkItem(40511) || pc.getInventory().checkItem(40512) || pc.getInventory().checkItem(41080)) {
		htmlid = "silrein7";
		pc.getInventory().consumeItem(210091, 10);
		pc.getInventory().consumeItem(40510, 1);
		pc.getInventory().consumeItem(40511, 1);
		pc.getInventory().consumeItem(40512, 1);
		pc.getInventory().consumeItem(41080, 1);
		pc.getInventory().storeItem(505, 1);
		pc.getInventory().storeItem(210004, 1);
		pc.getQuest().set_step(L1Quest.QUEST_LEVEL15, 255);
		} else {
		htmlid = "silrein8";
		}
		}

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 3200014) {
		if (s.equalsIgnoreCase("buy 5")) {
		petbuy(client, 46044, 41159, 1000);
		htmlid = "subsusp3";
		} else if (s.equalsIgnoreCase("buy 6")) {
		petbuy(client, 46042, 41159, 1000);
		htmlid = "subsusp4";
		}

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 200075) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		int count = 1;
		int itemid = 0;
		if (pc.getLevel() >= 30 && pc.getLevel() < 40) {
		itemid = 210097;
		} else if (pc.getLevel() >= 40 && pc.getLevel() < 52) {
		itemid = 210098;
		} else if (pc.getLevel() >= 52 && pc.getLevel() < 55) {
		itemid = 210099;
		} else if (pc.getLevel() >= 55 && pc.getLevel() < 60) {
		itemid = 210100;
		} else if (pc.getLevel() >= 60 && pc.getLevel() < 65) {
		itemid = 210101;
		} else if (pc.getLevel() >= 65 && pc.getLevel() < 70) {
		itemid = 210102;
		} else if (pc.getLevel() >= 70 && pc.getLevel() < 75) {
		itemid = 210103;
		} else if (pc.getLevel() >= 75 && pc.getLevel() < 80) {
		itemid = 210116;
		} else if (pc.getLevel() >= 80) {
		itemid = 210117;
		}
		if (s.equalsIgnoreCase("0"))
		count = 1;
		else if (s.equalsIgnoreCase("1"))
		count = 2;
		else if (s.equalsIgnoreCase("2"))
		count = 3;
		else if (s.equalsIgnoreCase("3"))
		count = 4;
		else if (s.equalsIgnoreCase("4"))
		count = 5;
		else if (s.equalsIgnoreCase("5"))
		count = 6;
		else if (s.equalsIgnoreCase("6"))
		count = 7;
		else if (s.equalsIgnoreCase("7"))
		count = 8;
		else if (s.equalsIgnoreCase("8"))
		count = 9;
		else if (s.equalsIgnoreCase("9"))
		count = 10;
		else if (s.equalsIgnoreCase(":"))
		count = 11;
		else if (s.equalsIgnoreCase(";"))
		count = 12;
		else if (s.equalsIgnoreCase("<"))
		count = 13;
		else if (s.equalsIgnoreCase("="))
		count = 14;
		else if (s.equalsIgnoreCase(">"))
		count = 15;
		else if (s.equalsIgnoreCase("?"))
		count = 16;
		else if (s.equalsIgnoreCase("@"))
		count = 17;
		else if (s.equalsIgnoreCase("A"))
		count = 18;
		else if (s.equalsIgnoreCase("B"))
		count = 19;
		else if (s.equalsIgnoreCase("C"))
		count = 20;
		else
		count = 1;
		int adena = count * 2500;
		if (pc.getInventory().checkItem(40308, adena)) {
		pc.getInventory().consumeItem(40308, adena);
		L1ItemInstance item = pc.getInventory().storeItem(itemid, count);
		if (item != null) {
		String itemName = item.getItem().getName();
		String npcName = npc.getNpcTemplate().get_name();
		pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
		}
		htmlid = "sharna3";
		} else {
		htmlid = "sharna5";
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71126) {
		if (s.equalsIgnoreCase("B")) {
		if (pc.getInventory().checkItem(41007, 1)) {
		htmlid = "eris10";
		} else {
		L1NpcInstance npc = (L1NpcInstance) obj;
		String npcName = npc.getNpcTemplate().get_name();
		L1ItemInstance item = pc.getInventory().storeItem(41007, 1);
		String itemName = item.getItem().getName();
		pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
		htmlid = "eris6";
		}
		} else if (s.equalsIgnoreCase("C")) {
		if (pc.getInventory().checkItem(41009, 1)) {
		htmlid = "eris10";
		} else {
		L1NpcInstance npc = (L1NpcInstance) obj;
		L1ItemInstance item = pc.getInventory().storeItem(41009, 1);
		String itemName = item.getItem().getName();
		String npcName = npc.getNpcTemplate().get_name();
		pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
		htmlid = "eris8";
		}
		} else if (s.equalsIgnoreCase("A")) {
		if (pc.getInventory().checkItem(41007, 1)) {
		if (pc.getInventory().checkItem(40969, 20)) {
		htmlid = "eris18";
		materials = new int[] { 40969, 41007 };
		counts = new int[] { 20, 1 };
		createitem = new int[] { 41008 };
		createcount = new int[] { 1 };
		} else {
		htmlid = "eris5";
		}
		} else {
		htmlid = "eris2";
		}
		} else if (s.equalsIgnoreCase("E")) {
		if (pc.getInventory().checkItem(41010, 1)) {
		htmlid = "eris19";
		} else {
		htmlid = "eris7";
		}
		} else if (s.equalsIgnoreCase("D")) {
		if (pc.getInventory().checkItem(41010, 1)) {
		htmlid = "eris19";
		} else {
		if (pc.getInventory().checkItem(41009, 1)) {
		if (pc.getInventory().checkItem(40959, 1)) {
		htmlid = "eris17";
		materials = new int[] { 40959, 41009 };
		counts = new int[] { 1, 1 };
		createitem = new int[] { 41010 };
		createcount = new int[] { 1 };
		} else if (pc.getInventory().checkItem(40960, 1)) {
		htmlid = "eris16";
		materials = new int[] { 40960, 41009 };
		counts = new int[] { 1, 1 };
		createitem = new int[] { 41010 };
		createcount = new int[] { 1 };
		} else if (pc.getInventory().checkItem(40961, 1)) {
		htmlid = "eris15";
		materials = new int[] { 40961, 41009 };
		counts = new int[] { 1, 1 };
		createitem = new int[] { 41010 };
		createcount = new int[] { 1 };
		} else if (pc.getInventory().checkItem(40962, 1)) {
		htmlid = "eris14";
		materials = new int[] { 40962, 41009 };
		counts = new int[] { 1, 1 };
		createitem = new int[] { 41010 };
		createcount = new int[] { 1 };
		} else if (pc.getInventory().checkItem(40635, 10)) {
		htmlid = "eris12";
		materials = new int[] { 40635, 41009 };
		counts = new int[] { 10, 1 };
		createitem = new int[] { 41010 };
		createcount = new int[] { 1 };
		} else if (pc.getInventory().checkItem(40638, 10)) {
		htmlid = "eris11";
		materials = new int[] { 40638, 41009 };
		counts = new int[] { 10, 1 };
		createitem = new int[] { 41010 };
		createcount = new int[] { 1 };
		} else if (pc.getInventory().checkItem(40642, 10)) {
		htmlid = "eris13";
		materials = new int[] { 40642, 41009 };
		counts = new int[] { 10, 1 };
		createitem = new int[] { 41010 };
		createcount = new int[] { 1 };
		} else if (pc.getInventory().checkItem(40667, 10)) {
		htmlid = "eris13";
		materials = new int[] { 40667, 41009 };
		counts = new int[] { 10, 1 };
		createitem = new int[] { 41010 };
		createcount = new int[] { 1 };
		} else {
		htmlid = "eris8";
		}
		} else {
		htmlid = "eris7";
		}
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80076) {
		if (s.equalsIgnoreCase("A")) {
		int[] diaryno = { 49082, 49083 };
		int pid = _random.nextInt(diaryno.length);
		int di = diaryno[pid];
		if (di == 49082) { // 홀수 페이지 뽑아라
		htmlid = "voyager6a";
		L1NpcInstance npc = (L1NpcInstance) obj;
		L1ItemInstance item = pc.getInventory().storeItem(di, 1);
		String itemName = item.getItem().getName();
		String npcName = npc.getNpcTemplate().get_name();
		pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
		} else if (di == 49083) { // 짝수 페이지 뽑아라
		htmlid = "voyager6b";
		L1NpcInstance npc = (L1NpcInstance) obj;
		L1ItemInstance item = pc.getInventory().storeItem(di, 1);
		String itemName = item.getItem().getName();
		String npcName = npc.getNpcTemplate().get_name();
		pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
		}
		}
		// 연금 술사 페리타
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71128) {
		if (s.equals("A")) {
		if (pc.getInventory().checkItem(41010, 1)) { // 이리스의 추천서
		htmlid = "perita2";
		} else {
		htmlid = "perita3";
		}
		} else if (s.equals("p")) {
		// 判斷是否擁有受詛咒的黑色耳環
		if (pc.getInventory().checkItem(40987, 1) // 法師職業
		&& pc.getInventory().checkItem(40988, 1) // 騎士職業
		&& pc.getInventory().checkItem(40989, 1)) { // 戰士職業
		htmlid = "perita43";

		// 如果玩家擁有 1 個物品編號 40987（法師耳環）、40988（騎士耳環）和 40989（戰士耳環），設置 htmlid 為 "perita43"

		} else if (pc.getInventory().checkItem(40987, 1) // 法師職業
		&& pc.getInventory().checkItem(40989, 1)) { // 戰士職業
		htmlid = "perita44";

		// 如果玩家擁有 1 個物品編號 40987（法師耳環）和 40989（戰士耳環），設置 htmlid 為 "perita44"

		} else if (pc.getInventory().checkItem(40987, 1) // 法師職業
		&& pc.getInventory().checkItem(40988, 1)) { // 騎士職業
		htmlid = "perita45";

		// 如果玩家擁有 1 個物品編號 40987（法師耳環）和 40988（騎士耳環），設置 htmlid 為 "perita45"

		} else if (pc.getInventory().checkItem(40988, 1) // 騎士職業
		&& pc.getInventory().checkItem(40989, 1)) { // 戰士職業
		htmlid = "perita47";

		// 如果玩家擁有 1 個物品編號 40988（騎士耳環）和 40989（戰士耳環），設置 htmlid 為 "perita47"

		} else if (pc.getInventory().checkItem(40987, 1)) { // 法師職業
		htmlid = "perita46";

		// 如果玩家只擁有 1 個物品編號 40987（法師耳環），設置 htmlid 為 "perita46"

		} else if (pc.getInventory().checkItem(40988, 1)) { // 騎士職業
		htmlid = "perita49";

		// 如果玩家只擁有 1 個物品編號 40988（騎士耳環），設置 htmlid 為 "perita49"

		} else if (pc.getInventory().checkItem(40989, 1)) { // 戰士職業
		htmlid = "perita48";

		// 如果玩家只擁有 1 個物品編號 40989（戰士耳環），設置 htmlid 為 "perita48"

		} else {
		htmlid = "perita50";

		// 如果玩家沒有上述任何一個物品，設置 htmlid 為 "perita50"
		}
		} else if (s.equals("q")) {

		} else if (s.equals("s")) {// 判斷是否擁有黑色耳環
		if (pc.getInventory().checkItem(41173, 1) // 法師職業
		&& pc.getInventory().checkItem(41174, 1) // 騎士職業
		&& pc.getInventory().checkItem(41175, 1)) { // 戰士職業
		htmlid = "perita54";

		// 如果玩家擁有 1 個物品編號 41173（法師耳環）、41174（騎士耳環）和 41175（戰士耳環），設置 htmlid 為 "perita54"

		} else if (pc.getInventory().checkItem(41173, 1) // 法師職業
		&& pc.getInventory().checkItem(41175, 1)) { // 戰士職業
		htmlid = "perita55";

		// 如果玩家擁有 1 個物品編號 41173（法師耳環）和 41175（戰士耳環），設置 htmlid 為 "perita55"

		} else if (pc.getInventory().checkItem(41173, 1) // 法師職業
		&& pc.getInventory().checkItem(41174, 1)) { // 騎士職業
		htmlid = "perita56";

		// 如果玩家擁有 1 個物品編號 41173（法師耳環）和 41174（騎士耳環），設置 htmlid 為 "perita56"

		} else if (pc.getInventory().checkItem(41174, 1) // 騎士職業
		&& pc.getInventory().checkItem(41175, 1)) { // 戰士職業
		htmlid = "perita58";

		// 如果玩家擁有 1 個物品編號 41174（騎士耳環）和 41175（戰士耳環），設置 htmlid 為 "perita58"

		} else if (pc.getInventory().checkItem(41174, 1)) { // 騎士職業
		htmlid = "perita57";

		// 如果玩家只擁有 1 個物品編號 41174（騎士耳環），設置 htmlid 為 "perita57"

		} else if (pc.getInventory().checkItem(41175, 1)) { // 戰士職業
		htmlid = "perita60";

		// 如果玩家只擁有 1 個物品編號 41175（戰士耳環），設置 htmlid 為 "perita60"

		} else if (pc.getInventory().checkItem(41176, 1)) { // 法師職業
		htmlid = "perita59";

		// 如果玩家只擁有 1 個物品編號 41176（法師耳環），設置 htmlid 為 "perita59"

		} else {
		htmlid = "perita61";

		// 如果玩家沒有上述任何一個物品，設置 htmlid 為 "perita61"
		}
		// 神秘黑色耳環判別
		if (pc.getInventory().checkItem(41161, 1) // 法師職業
		&& pc.getInventory().checkItem(41162, 1) // 騎士職業
		&& pc.getInventory().checkItem(41163, 1)) { // 戰士職業
		htmlid = "perita62";

		// 如果玩家擁有 1 個物品編號 41161（法師耳環）、41162（騎士耳環）和 41163（戰士耳環），設置 htmlid 為 "perita62"

		} else if (pc.getInventory().checkItem(41161, 1) // 法師職業
		&& pc.getInventory().checkItem(41163, 1)) { // 戰士職業
		htmlid = "perita63";

		// 如果玩家擁有 1 個物品編號 41161（法師耳環）和 41163（戰士耳環），設置 htmlid 為 "perita63"

		} else if (pc.getInventory().checkItem(41161, 1) // 法師職業
		&& pc.getInventory().checkItem(41162, 1)) { // 騎士職業
		htmlid = "perita64";

		// 如果玩家擁有 1 個物品編號 41161（法師耳環）和 41162（騎士耳環），設置 htmlid 為 "perita64"

		} else if (pc.getInventory().checkItem(41162, 1) // 騎士職業
		&& pc.getInventory().checkItem(41163, 1)) { // 戰士職業
		htmlid = "perita66";

		// 如果玩家擁有 1 個物品編號 41162（騎士耳環）和 41163（戰士耳環），設置 htmlid 為 "perita66"

		} else if (pc.getInventory().checkItem(41161, 1)) { // 法師職業
		htmlid = "perita65";

		// 如果玩家只擁有 1 個物品編號 41161（法師耳環），設置 htmlid 為 "perita65"

		} else if (pc.getInventory().checkItem(41162, 1)) { // 騎士職業
		htmlid = "perita68";

		// 如果玩家只擁有 1 個物品編號 41162（騎士耳環），設置 htmlid 為 "perita68"

		} else if (pc.getInventory().checkItem(41163, 1)) { // 戰士職業
		htmlid = "perita67";

		// 如果玩家只擁有 1 個物品編號 41163（戰士耳環），設置 htmlid 為 "perita67"

		} else {
		htmlid = "perita69";

		// 如果玩家沒有上述任何一個物品，設置 htmlid 為 "perita69"
		}
		} else if (s.equals("B")) {
		// 淨化的一部分
		if (pc.getInventory().checkItem(40651, 10) // 火焰的氣息
		&& pc.getInventory().checkItem(40643, 10) // 水的氣息
		&& pc.getInventory().checkItem(40618, 10) // 大地的氣息
		&& pc.getInventory().checkItem(40645, 10) // 狂風的氣息
		&& pc.getInventory().checkItem(40676, 10) // 黑暗的氣息
		&& pc.getInventory().checkItem(40442, 5) // 普羅布的液體
		&& pc.getInventory().checkItem(40051, 1)) { // 高級祖母綠
		htmlid = "perita7";

		// 如果玩家擁有上述所有物品，設置 htmlid 為 "perita7"

		materials = new int[] { 40651, 40643, 40618, 40645, 40676, 40442, 40051 };
		counts = new int[] { 10, 10, 10, 10, 10, 5, 1 };
		createitem = new int[] { 40925 }; // 淨化的一部分
		createcount = new int[] { 1 };

		// 配置材料和生成物品的信息

		} else {
		htmlid = "perita8";

		// 如果玩家沒有上述所有物品，設置 htmlid 為 "perita8"
		}


		// 神秘的一部分：1階段
		} else if (s.equals("G") || s.equals("h") || s.equals("i")) {

		// 如果玩家選擇 "G"、"h" 或 "i"

		if (pc.getInventory().checkItem(40651, 5) // 火焰的氣息
		&& pc.getInventory().checkItem(40643, 5) // 水的氣息
		&& pc.getInventory().checkItem(40618, 5) // 大地的氣息
		&& pc.getInventory().checkItem(40645, 5) // 狂風的氣息
		&& pc.getInventory().checkItem(40676, 5) // 黑暗的氣息
		&& pc.getInventory().checkItem(40675, 5) // 黑暗的礦石
		&& pc.getInventory().checkItem(40049, 3) // 高級紅寶石
		&& pc.getInventory().checkItem(40051, 1)) { // 高級祖母綠
		htmlid = "perita27";

		// 如果玩家擁有上述所有物品，設置 htmlid 為 "perita27"

		materials = new int[] { 40651, 40643, 40618, 40645, 40676, 40675, 40049, 40051 };
		counts = new int[] { 5, 5, 5, 5, 5, 5, 3, 1 };
		createitem = new int[] { 40926 }; // 神秘的一部分：1階段
		createcount = new int[] { 1 };

		// 配置材料和生成物品的信息
		}
		} else {
		htmlid = "perita28";
		}
		} else if (s.equals("H") || s.equals("j") || s.equals("k")) {
		// 神秘的一部分：2階段
		if (pc.getInventory().checkItem(40651, 10) // 火焰的氣息
		&& pc.getInventory().checkItem(40643, 10) // 水的氣息
		&& pc.getInventory().checkItem(40618, 10) // 大地的氣息
		&& pc.getInventory().checkItem(40645, 10) // 狂風的氣息
		&& pc.getInventory().checkItem(40676, 20) // 黑暗的氣息
		&& pc.getInventory().checkItem(40675, 10) // 黑暗的礦石
		&& pc.getInventory().checkItem(40048, 3) // 高級鑽石
		&& pc.getInventory().checkItem(40051, 1)) { // 高級祖母綠
		htmlid = "perita29";

		// 如果玩家擁有上述所有物品，設置 htmlid 為 "perita29"

		materials = new int[] { 40651, 40643, 40618, 40645, 40676, 40675, 40048, 40051 };
		counts = new int[] { 10, 10, 10, 10, 20, 10, 3, 1 };
		createitem = new int[] { 40927 }; // 神秘的一部分：2階段
		createcount = new int[] { 1 };

		// 配置材料和生成物品的信息

		} else {
		htmlid = "perita30";

		// 如果玩家沒有上述所有物品，設置 htmlid 為 "perita30"
		}


		// 神秘的一部分：3階段
		} else if (s.equals("I") || s.equals("l") || s.equals("m")) {

		// 如果玩家選擇 "I"、"l" 或 "m"

		if (pc.getInventory().checkItem(40651, 20) // 火焰的氣息
		&& pc.getInventory().checkItem(40643, 20) // 水的氣息
		&& pc.getInventory().checkItem(40618, 20) // 大地的氣息
		&& pc.getInventory().checkItem(40645, 20) // 狂風的氣息
		&& pc.getInventory().checkItem(40676, 30) // 黑暗的氣息
		&& pc.getInventory().checkItem(40675, 10) // 黑暗的礦石
		&& pc.getInventory().checkItem(40050, 3) // 高級藍寶石
		&& pc.getInventory().checkItem(40051, 1)) { // 高級祖母綠
		htmlid = "perita31";

		// 如果玩家擁有上述所有物品，設置 htmlid 為 "perita31"
		}
		materials = new int[] { 40651, 40643, 40618, 40645, 40676, 40675, 40050, 40051 };
		counts = new int[] { 20, 20, 20, 20, 30, 10, 3, 1 };
		createitem = new int[] { 40928 }; // 신비적인 일부：3 단계
		createcount = new int[] { 1 };
		} else {
		htmlid = "perita32";
		}
		} else if (s.equals("J") || s.equals("n") || s.equals("o")) {
		// 神秘的一部分：4階段
		if (pc.getInventory().checkItem(40651, 30) // 火焰的氣息
		&& pc.getInventory().checkItem(40643, 30) // 水的氣息
		&& pc.getInventory().checkItem(40618, 30) // 大地的氣息
		&& pc.getInventory().checkItem(40645, 30) // 狂風的氣息
		&& pc.getInventory().checkItem(40676, 30) // 黑暗的氣息
		&& pc.getInventory().checkItem(40675, 20) // 黑暗的礦石
		&& pc.getInventory().checkItem(40052, 1) // 最頂級鑽石
		&& pc.getInventory().checkItem(40051, 1)) { // 高級祖母綠
		htmlid = "perita33";

		// 如果玩家擁有上述所有物品，設置 htmlid 為 "perita33"

		materials = new int[] { 40651, 40643, 40618, 40645, 40676, 40675, 40052, 40051 };
		counts = new int[] { 30, 30, 30, 30, 30, 20, 1, 1 };
		createitem = new int[] { 40928 }; // 神秘的一部分：4階段
		createcount = new int[] { 1 };

		// 配置材料和生成物品的信息

		} else {
		htmlid = "perita34";

		// 如果玩家沒有上述所有物品，設置 htmlid 為 "perita34"
		}


		// 1階段耳環（靈魂耳環）
		} else if (s.equals("K")) {

		// 如果玩家選擇 "K"

		int earinga = 0;
		int earingb = 0;

		if (pc.getInventory().checkEquipped(21014) || pc.getInventory().checkEquipped(21006) || pc.getInventory().checkEquipped(21007)) {
		htmlid = "perita36";

		// 如果玩家裝備了任何一種耳環，設置 htmlid 為 "perita36"

		} else if (pc.getInventory().checkItem(21014, 1)) { // 法師職業
		earinga = 21014;
		earingb = 41176;

		// 如果玩家擁有物品編號 21014（法師耳環），將 earinga 設置為 21014，earingb 設置為 41176

		} else if (pc.getInventory().checkItem(21006, 1)) { // 騎士職業
		earinga = 21006;
		earingb = 41177;

		// 如果玩家擁有物品編號 21006（騎士耳環），將 earinga 設置為 21006，earingb 設置為 41177

		} else if (pc.getInventory().checkItem(21007, 1)) { // 戰士職業
		earinga = 21007;
		earingb = 41178;

		// 如果玩家擁有物品編號 21007（戰士耳環），將 earinga 設置為 21007，earingb 設置為 41178

		} else {
		htmlid = "perita36";

		// 如果玩家沒有任何一種耳環，設置 htmlid 為 "perita36"
		}
		}
		if (earinga > 0) {
		materials = new int[] { earinga };
		counts = new int[] { 1 };
		createitem = new int[] { earingb };
		createcount = new int[] { 1 };

		// 如果 earinga 大於0，配置材料和生成物品的信息
		}

		} else if (s.equals("L")) { // 2階段耳環（智慧耳環）
		if (pc.getInventory().checkEquipped(21015)) {
		htmlid = "perita22";

		// 如果玩家已裝備耳環 21015，設置 htmlid 為 "perita22"

		} else if (pc.getInventory().checkItem(21015, 1)) {
		materials = new int[] { 21015 };
		counts = new int[] { 1 };
		createitem = new int[] { 41179 };
		createcount = new int[] { 1 };

		// 如果玩家擁有耳環 21015，配置材料和生成物品的信息

		} else {
		htmlid = "perita22";

		// 如果玩家沒有耳環 21015，設置 htmlid 為 "perita22"
		}

		} else if (s.equals("M")) { // 3階段耳環（真實耳環）
		if (pc.getInventory().checkEquipped(21016)) {
		htmlid = "perita26";

		// 如果玩家已裝備耳環 21016，設置 htmlid 為 "perita26"

		} else if (pc.getInventory().checkItem(21016, 1)) {
		materials = new int[] { 21016 };
		counts = new int[] { 1 };
		createitem = new int[] { 41182 };
		createcount = new int[] { 1 };

		// 如果玩家擁有耳環 21016，配置材料和生成物品的信息

		} else {
		htmlid = "perita26";

		// 如果玩家沒有耳環 21016，設置 htmlid 為 "perita26"
		}

		} else if (s.equals("b")) { // 2階段耳環（熱情耳環）
		if (pc.getInventory().checkEquipped(21009)) {
		htmlid = "perita39";

		// 如果玩家已裝備耳環 21009，設置 htmlid 為 "perita39"

		} else if (pc.getInventory().checkItem(21009, 1)) {
		materials = new int[] { 21009 };
		counts = new int[] { 1 };
		createitem = new int[] { 41180 };
		createcount = new int[] { 1 };

		// 如果玩家擁有耳環 21009，配置材料和生成物品的信息

		} else {
		htmlid = "perita39";

		// 如果玩家沒有耳環 21009，設置 htmlid 為 "perita39"
		}
		} else if (s.equals("d")) { // 3階段耳環（榮譽耳環）
		if (pc.getInventory().checkEquipped(21012)) {
		htmlid = "perita41";

		// 如果玩家已裝備耳環 21012，設置 htmlid 為 "perita41"

		} else if (pc.getInventory().checkItem(21012, 1)) {
		materials = new int[] { 21012 };
		counts = new int[] { 1 };
		createitem = new int[] { 41183 };
		createcount = new int[] { 1 };

		// 如果玩家擁有耳環 21012，配置材料和生成物品的信息

		} else {
		htmlid = "perita41";

		// 如果玩家沒有耳環 21012，設置 htmlid 為 "perita41"
		}

		} else if (s.equals("a")) { // 2階段耳環（憤怒耳環）
		if (pc.getInventory().checkEquipped(21008)) {
		htmlid = "perita38";

		// 如果玩家已裝備耳環 21008，設置 htmlid 為 "perita38"

		} else if (pc.getInventory().checkItem(21008, 1)) {
		materials = new int[] { 21008 };
		counts = new int[] { 1 };
		createitem = new int[] { 41181 };
		createcount = new int[] { 1 };

		// 如果玩家擁有耳環 21008，配置材料和生成物品的信息

		} else {
		htmlid = "perita38";

		// 如果玩家沒有耳環 21008，設置 htmlid 為 "perita38"
		}

		} else if (s.equals("c")) { // 3階段耳環（勇猛耳環）
		if (pc.getInventory().checkEquipped(21010)) {
		htmlid = "perita40";

		// 如果玩家已裝備耳環 21010，設置 htmlid 為 "perita40"

		} else if (pc.getInventory().checkItem(21010, 1)) {
		materials = new int[] { 21010 };
		counts = new int[] { 1 };
		createitem = new int[] { 41184 };
		createcount = new int[] { 1 };

		// 如果玩家擁有耳環 21010，配置材料和生成物品的信息

		} else {
		htmlid = "perita40";

		// 如果玩家沒有耳環 21010，設置 htmlid 為 "perita40"
		}

		// 寶石工匠Rumtis
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71129) {

		// 如果 NPC 的模板 ID 是 71129（寶石工匠Rumtis）

		if (s.equals("Z")) {
		htmlid = "rumtis2";

		// 如果玩家選擇 "Z"，設置 htmlid 為 "rumtis2"

		} else if (s.equals("Y")) {
		if (pc.getInventory().checkItem(41010, 1)) { // 伊利斯的推薦書
		htmlid = "rumtis3";

		// 如果玩家擁有物品編號 41010（伊利斯的推薦書），設置 htmlid 為 "rumtis3"

		} else {
		htmlid = "rumtis4";

		// 如果玩家沒有物品編號 41010，設置 htmlid 為 "rumtis4"
		}

		} else if (s.equals("q")) {
		htmlid = "rumtis92";

		// 如果玩家選擇 "q"，設置 htmlid 為 "rumtis92"

		} else if (s.equals("A")) {
		if (pc.getInventory().checkItem(41161, 1)) {

		// 神秘的黑色耳環
		htmlid = "rumtis6";

		// 如果玩家擁有物品編號 41161（神秘的黑色耳環），設置 htmlid 為 "rumtis6"

		} else {
		htmlid = "rumtis101";

		// 如果玩家沒有物品編號 41161，設置 htmlid 為 "rumtis101"
		}

		} else if (s.equals("B")) {
		if (pc.getInventory().checkItem(41164, 1)) {

		// 神秘的法師耳環
		htmlid = "rumtis7";

		// 如果玩家擁有物品編號 41164（神秘的法師耳環），設置 htmlid 為 "rumtis7"

		} else {
		htmlid = "rumtis101";

		// 如果玩家沒有物品編號 41164，設置 htmlid 為 "rumtis101"
		}

		} else if (s.equals("C")) {
		if (pc.getInventory().checkItem(41167, 1)) {

		// 神秘的灰色法師耳環
		htmlid = "rumtis8";

		// 如果玩家擁有物品編號 41167（神秘的灰色法師耳環），設置 htmlid 為 "rumtis8"

		} else {
		htmlid = "rumtis101";

		// 如果玩家沒有物品編號 41167，設置 htmlid 為 "rumtis101"
		}

		} else if (s.equals("T")) {
		if (pc.getInventory().checkItem(41167, 1)) {

		// 神秘的白色法師耳環
		htmlid = "rumtis9";

		// 如果玩家擁有物品編號 41168（神秘的白色法師耳環），設置 htmlid 為 "rumtis9"

		} else {
		htmlid = "rumtis101";

		// 如果玩家沒有物品編號 41167，設置 htmlid 為 "rumtis101"
		}

		} else if (s.equals("w")) {
		if (pc.getInventory().checkItem(41162, 1)) {

		// 神秘的黑色耳環
		htmlid = "rumtis14";

		// 如果玩家擁有物品編號 41162（神秘的黑色耳環），設置 htmlid 為 "rumtis14"

		} else {
		htmlid = "rumtis101";

		// 如果玩家沒有物品編號 41162，設置 htmlid 為 "rumtis101"
		}
		} else if (s.equals("x")) {
		if (pc.getInventory().checkItem(41165, 1)) {

		// 神秘的騎士耳環
		htmlid = "rumtis15";
		} else {
		htmlid = "rumtis101";
		}

		} else if (s.equals("y")) {
		if (pc.getInventory().checkItem(41168, 1)) {

		// 神秘的灰色騎士耳環
		htmlid = "rumtis16";
		} else {
		htmlid = "rumtis101";
		}

		} else if (s.equals("z")) {
		if (pc.getInventory().checkItem(41171, 1)) {

		// 神秘的白色騎士耳環
		htmlid = "rumtis17";
		} else {
		htmlid = "rumtis101";
		}

		} else if (s.equals("U")) {
		if (pc.getInventory().checkItem(41163, 1)) {

		// 神秘的黑色耳環
		htmlid = "rumtis10";
		} else {
		htmlid = "rumtis101";
		}

		} else if (s.equals("V")) {
		if (pc.getInventory().checkItem(41166, 1)) {

		// 神秘的戰士耳環
		htmlid = "rumtis11";
		} else {
		htmlid = "rumtis101";
		}

		} else if (s.equals("W")) {
		if (pc.getInventory().checkItem(41169, 1)) {

		// 神秘的灰色戰士耳環
		htmlid = "rumtis12";
		} else {
		htmlid = "rumtis101";
		}

		} else if (s.equals("X")) {
		if (pc.getInventory().checkItem(41172, 1)) {

		// 神秘的白色戰士耳環
		htmlid = "rumtis13";
		} else {
		htmlid = "rumtis101";
		}
		} else if (s.equals("D") || s.equals("E") || s.equals("F") || s.equals("G")) {
		int insn = 0;
		int bacn = 0;
		int me = 0;
		int mr = 0;
		int mj = 0;
		int an = 0;
		int men = 0;
		int mrn = 0;
		int mjn = 0;
		int ann = 0;
		if (pc.getInventory().checkItem(40959, 1) // 명법군왕의 인장
		&& pc.getInventory().checkItem(40960, 1) // 마령군왕의 인장
		&& pc.getInventory().checkItem(40961, 1) // 마수군왕의 인장
		&& pc.getInventory().checkItem(40962, 1)) { // 암살군왕의 인장
		insn = 1;
		me = 40959;
		mr = 40960;
		mj = 40961;
		an = 40962;
		men = 1;
		mrn = 1;
		mjn = 1;
		ann = 1;
		} else if (pc.getInventory().checkItem(40642, 10) // 명법군의 배지
		&& pc.getInventory().checkItem(40635, 10) // 마령군의 배지
		&& pc.getInventory().checkItem(40638, 10) // 마수군의 배지
		&& pc.getInventory().checkItem(40667, 10)) { // 암살군의 배지
		bacn = 1;
		me = 40642;
		mr = 40635;
		mj = 40638;
		an = 40667;
		men = 10;
		mrn = 10;
		mjn = 10;
		ann = 10;
		}
		if (pc.getInventory().checkItem(40046, 1) // 사파이어
		&& pc.getInventory().checkItem(40618, 5) // 대지의 숨결
		&& pc.getInventory().checkItem(40643, 5) // 수의 숨결
		&& pc.getInventory().checkItem(40645, 5) // 돌풍이 심함 취
		&& pc.getInventory().checkItem(40651, 5) // 불의 숨결
		&& pc.getInventory().checkItem(40676, 5)) { // 어둠의 숨결
		if (insn == 1 || bacn == 1) {
		htmlid = "rumtis60";
		materials = new int[] { me, mr, mj, an, 40046, 40618, 40643, 40651, 40676 };
		counts = new int[] { men, mrn, mjn, ann, 1, 5, 5, 5, 5, 5 };
		createitem = new int[] { 40926 }; // 가공된 사파이어：1 단계
		createcount = new int[] { 1 };
		} else {
		htmlid = "rumtis18";
		}
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70644 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70538 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70560
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70667 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70725 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70790
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70884) {
		if (s.equalsIgnoreCase("request blood of evil")) {

		// 如果字符串 s 等於 "request blood of evil"（請求邪惡之血）

		pc.sendPackets(new S_SystemMessage("現在還不是合適的時機。"));

		// 向玩家發送一條系統消息，通知玩家「現在還不是合適的時機。」

		htmlid = "";

		// 重置 htmlid 變量為空字符串


		// return;

		// 註釋掉的代碼，原先可能用來立即返回結束方法
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71119) {
		if (s.equalsIgnoreCase("request las history book")) {
		materials = new int[] { 41019, 41020, 41021, 41022, 41023, 41024, 41025, 41026 };
		counts = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		createitem = new int[] { 41027 };
		createcount = new int[] { 1 };
		htmlid = "";
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71170) {
		if (s.equalsIgnoreCase("request las weapon manual")) {
		materials = new int[] { 41027 };
		counts = new int[] { 1 };
		createitem = new int[] { 40965 };
		createcount = new int[] { 1 };
		htmlid = "";
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 6000015) {
		if (s.equalsIgnoreCase("1")) {
		pc.start_teleport(33929, 33348, 4, 5, 18339, true, false);
		} else if (s.equalsIgnoreCase("a") && pc.getInventory().checkItem(41158, 10)) {
		pc.start_teleport(32800, 32800, 110, 5, 18339, true, false);
		pc.getInventory().consumeItem(41158, 10);
		} else if (s.equalsIgnoreCase("b") && pc.getInventory().checkItem(41158, 20)) {
		pc.start_teleport(32800, 32800, 120, 5, 18339, true, false);
		pc.getInventory().consumeItem(41158, 20);
		} else if (s.equalsIgnoreCase("c") && pc.getInventory().checkItem(41158, 30)) {
		pc.start_teleport(32800, 32800, 130, 5, 18339, true, false);
		pc.getInventory().consumeItem(41158, 30);
		} else if (s.equalsIgnoreCase("d") && pc.getInventory().checkItem(41158, 40)) {
		pc.start_teleport(32800, 32800, 140, 5, 18339, true, false);
		pc.getInventory().consumeItem(41158, 40);
		} else if (s.equalsIgnoreCase("e") && pc.getInventory().checkItem(41158, 50)) {
		pc.start_teleport(32796, 32796, 150, 5, 18339, true, false);
		pc.getInventory().consumeItem(41158, 50);
		} else if (s.equalsIgnoreCase("f") && pc.getInventory().checkItem(41158, 60)) {
		pc.start_teleport(32720, 32821, 160, 5, 18339, true, false);
		pc.getInventory().consumeItem(41158, 60);
		} else if (s.equalsIgnoreCase("g") && pc.getInventory().checkItem(41158, 70)) {
		pc.start_teleport(32720, 32821, 170, 5, 18339, true, false);
		pc.getInventory().consumeItem(41158, 70);
		} else if (s.equalsIgnoreCase("h") && pc.getInventory().checkItem(41158, 80)) {
		pc.start_teleport(32724, 32822, 180, 5, 18339, true, false);
		pc.getInventory().consumeItem(41158, 80);
		} else if (s.equalsIgnoreCase("i") && pc.getInventory().checkItem(41158, 90)) {
		pc.start_teleport(32722, 32827, 190, 5, 18339, true, false);
		pc.getInventory().consumeItem(41158, 90);
		} else if (s.equalsIgnoreCase("j") && pc.getInventory().checkItem(41158, 100)) {
		pc.start_teleport(32731, 32856, 200, 5, 18339, true, false);
		pc.getInventory().consumeItem(41158, 100);
		}
		// 첩보원(욕망의 동굴측)
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80067) {
		if (s.equalsIgnoreCase("n")) {
		htmlid = "";
		poly(client, 6034);
final int[] item_ids = { 41132, 41133, 41134 };
final int[] item_amounts = { 1, 1, 1 };
		L1ItemInstance item = null;
		for (int i = 0; i < item_ids.length; i++) {
		item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		pc.getQuest().set_step(L1Quest.QUEST_DESIRE, 1);
		}
		// 「그런 임무는 그만둔다」
		} else if (s.equalsIgnoreCase("d")) {
		htmlid = "minicod09";
		pc.getInventory().consumeItem(41130, 1);
		pc.getInventory().consumeItem(41131, 1);

		// 「초기화한다」
		} else if (s.equalsIgnoreCase("k")) {
		htmlid = "";
		pc.getInventory().consumeItem(41132, 1); // 被玷污的粉末
		pc.getInventory().consumeItem(41133, 1); // 沒有力量的粉末
		pc.getInventory().consumeItem(41134, 1); // 自私的粉末
		pc.getInventory().consumeItem(41135, 1); // 被玷污的精髓
		pc.getInventory().consumeItem(41136, 1); // 沒有力量的精髓
		pc.getInventory().consumeItem(41137, 1); // 自私的精髓
		pc.getInventory().consumeItem(41138, 1); // 精髓
		pc.getQuest().set_step(L1Quest.QUEST_DESIRE, 0); // 重置任務步驟
		} else if (s.equalsIgnoreCase("e")) {
		if (pc.getQuest().get_step(L1Quest.QUEST_DESIRE) == L1Quest.QUEST_END || pc.getKarmaLevel() >= 1) {
		htmlid = "";
		} else {
		if (pc.getInventory().checkItem(41138)) {
		htmlid = "";
		pc.addKarma((int) (1600 * Config.ServerRates.RateKarma));
		pc.sendPackets(new S_Karma(pc));
		pc.getInventory().consumeItem(41130, 1); // 血跡的契約書
		pc.getInventory().consumeItem(41131, 1); // 血跡的指令書
		pc.getInventory().consumeItem(41138, 1); // 精髓
		pc.getQuest().set_step(L1Quest.QUEST_DESIRE, L1Quest.QUEST_END);
		} else {
		htmlid = "minicod04";
		}
		}
		// 선물을 받는다
		} else if (s.equalsIgnoreCase("g")) {
		htmlid = "";
final int[] item_ids = { 41130 }; // 핏자국의 계약서
final int[] item_amounts = { 1 };
		L1ItemInstance item = null;
		for (int i = 0; i < item_ids.length; i++) {
		item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		}
		}
		/** 드루가 리뉴얼 **/
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 900005) {// 드루가의
		L1ItemInstance item = null;
		if (s.equalsIgnoreCase("a")) {
		if (pc.getInventory().checkItem(L1ItemId.DRUGA_POKET)) { // 이미
		htmlid = "veil3";
		} else if (pc.getInventory().checkItem(L1ItemId.ADENA, 10000000)) {// 1000만
		pc.getInventory().consumeItem(L1ItemId.ADENA, 10000000);
		item = pc.getInventory().storeItem(14000000, 1);
		pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
		htmlid = "veil8";
		} else {
		htmlid = "veil4";
		}
		}
		if (s.equalsIgnoreCase("b")) {
		if (pc.getInventory().checkItem(L1ItemId.DRUGA_POKET)) { // 이미
		htmlid = "veil3";
		} else if (pc.getInventory().checkItem(L1ItemId.ADENA, 10000000)) {// 1000만
		pc.getInventory().consumeItem(L1ItemId.ADENA, 10000000);
		item = pc.getInventory().storeItem(14000001, 1);
		pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
		htmlid = "veil8";
		} else {
		htmlid = "veil4";
		}
		}
		if (s.equalsIgnoreCase("c")) {
		if (pc.getInventory().checkItem(L1ItemId.DRUGA_POKET)) { // 이미
		htmlid = "veil3";
		} else if (pc.getInventory().checkItem(L1ItemId.ADENA, 10000000)) {// 1000만
		pc.getInventory().consumeItem(L1ItemId.ADENA, 10000000);
		item = pc.getInventory().storeItem(14000002, 1);
		pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
		htmlid = "veil8";
		} else {
		htmlid = "veil4";
		}
		}
		if (s.equalsIgnoreCase("d")) {
		if (pc.getInventory().checkItem(L1ItemId.DRUGA_POKET)) { // 이미
		htmlid = "veil3";
		} else if (pc.getInventory().checkItem(L1ItemId.ADENA, 10000000)) {// 1000만
		pc.getInventory().consumeItem(L1ItemId.ADENA, 10000000);
		item = pc.getInventory().storeItem(14000003, 1);
		pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
		htmlid = "veil8";
		} else {
		htmlid = "veil4";
		}
		}

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 900017) {// 반쿠
		if (s.equalsIgnoreCase("buy 7")) {
		petbuy(client, 900043, 410045, 1);// 녹색 해츨링 7
		} else if (s.equalsIgnoreCase("buy 8")) {
		petbuy(client, 900044, 410046, 1);// 황색 해츨링 8
		}
		htmlid = "";
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 900048) {// 수상한 요리사
		if (s.equalsIgnoreCase("0")) {
		if (pc.getInventory().checkItem(41921, 45)) {
		pc.getInventory().consumeItem(41921, 45);
		pc.getInventory().storeItem(410055, 1);
		pc.sendPackets(new S_ServerMessage(403, "$8538"));
		} else {
		htmlid = "suschef5";
		}
		} else if (s.equalsIgnoreCase("1")) {
		if (pc.getInventory().checkItem(410057, 1)) {// A
		if (pc.hasSkillEffect(L1SkillId.FEATHER_BUFF_B))
		pc.removeSkillEffect(L1SkillId.FEATHER_BUFF_B);
		if (pc.hasSkillEffect(L1SkillId.FEATHER_BUFF_C))
		pc.removeSkillEffect(L1SkillId.FEATHER_BUFF_C);
		if (pc.hasSkillEffect(L1SkillId.FEATHER_BUFF_D))
		pc.removeSkillEffect(L1SkillId.FEATHER_BUFF_D);
		pc.getInventory().consumeItem(410057, 1);
		new L1SkillUse().handleCommands(pc, L1SkillId.FEATHER_BUFF_A, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 7947));
		htmlid = "";
		} else if (pc.getInventory().checkItem(410058, 1)) {// B
		if (pc.hasSkillEffect(L1SkillId.FEATHER_BUFF_A))
		pc.removeSkillEffect(L1SkillId.FEATHER_BUFF_A);
		if (pc.hasSkillEffect(L1SkillId.FEATHER_BUFF_C))
		pc.removeSkillEffect(L1SkillId.FEATHER_BUFF_C);
		if (pc.hasSkillEffect(L1SkillId.FEATHER_BUFF_D))
		pc.removeSkillEffect(L1SkillId.FEATHER_BUFF_D);
		pc.getInventory().consumeItem(410058, 1);
		new L1SkillUse().handleCommands(pc, L1SkillId.FEATHER_BUFF_B, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 7948));
		htmlid = "";
		} else if (pc.getInventory().checkItem(410059, 1)) {// C
		if (pc.hasSkillEffect(L1SkillId.FEATHER_BUFF_B))
		pc.removeSkillEffect(L1SkillId.FEATHER_BUFF_B);
		if (pc.hasSkillEffect(L1SkillId.FEATHER_BUFF_A))
		pc.removeSkillEffect(L1SkillId.FEATHER_BUFF_A);
		if (pc.hasSkillEffect(L1SkillId.FEATHER_BUFF_D))
		pc.removeSkillEffect(L1SkillId.FEATHER_BUFF_D);
		pc.getInventory().consumeItem(410059, 1);
		new L1SkillUse().handleCommands(pc, L1SkillId.FEATHER_BUFF_C, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 7949));
		htmlid = "";
		} else if (pc.getInventory().checkItem(410060, 1)) {// D
		if (pc.hasSkillEffect(L1SkillId.FEATHER_BUFF_B))
		pc.removeSkillEffect(L1SkillId.FEATHER_BUFF_B);
		if (pc.hasSkillEffect(L1SkillId.FEATHER_BUFF_C))
		pc.removeSkillEffect(L1SkillId.FEATHER_BUFF_C);
		if (pc.hasSkillEffect(L1SkillId.FEATHER_BUFF_A))
		pc.removeSkillEffect(L1SkillId.FEATHER_BUFF_A);
		pc.getInventory().consumeItem(410060, 1);
		new L1SkillUse().handleCommands(pc, L1SkillId.FEATHER_BUFF_D, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 7950));
		htmlid = "";
		} else {
		htmlid = "suschef4";
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() >= 900090 && ((L1NpcInstance) obj).getNpcTemplate().get_npcId() <= 900098) {
		int locx = 0, locy = 0, checkitem = 0;
		if (s.equalsIgnoreCase("A")) {
		switch (((L1NpcInstance) obj).getNpcTemplate().get_npcId()) {
		case 900090:// 16층 -> 20층
		locx = 32754;
		locy = 32747;
		checkitem = 40104;
		break;
		case 900091:// 26층 -> 30층
		locx = 32754;
		locy = 32747;
		checkitem = 40105;
		break;
		case 900092:// 36층 -> 40층
		locx = 32754;
		locy = 32747;
		checkitem = 40106;
		break;
		case 900093:// 46층 -> 50층
		locx = 32754;
		locy = 32747;
		checkitem = 40107;
		break;
		case 900094:// 56층 -> 60층
		locx = 32635;
		locy = 32790;
		checkitem = 40108;
		break;
		case 900095:// 66층 -> 70층
		locx = 32637;
		locy = 32791;
		checkitem = 40109;
		break;
		case 900096:// 76층 -> 80층
		locx = 32637;
		locy = 32791;
		checkitem = 40110;
		break;
		case 900097:// 86층 -> 90층
		locx = 32637;
		locy = 32791;
		checkitem = 40111;
		break;
		case 900098:// 96층 -> 100층
		locx = 32732;
		locy = 32857;
		checkitem = 40112;
		break;
default:
		break;
		}
		if (pc.getInventory().checkItem(40308, 300) && pc.getInventory().checkItem(checkitem, 2)) {
		pc.getInventory().consumeItem(40308, 300);
		pc.getInventory().consumeItem(checkitem, 2);
		pc.start_teleport(locx, locy, pc.getMapId() + 4, pc.getHeading(), 18339, true, false);
		} else {
		htmlid = "omantel2";
		}
		}
		// 클라우디아 수정요청 //엔피시 번호확인하세요
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 900146) { // 말섬 인던 휴그린트
		if (s.equalsIgnoreCase("0")) {
		if (!pc.getInventory().checkItem(L1ItemId.MAGIC_BREATH, 1)) {
		if (pc.getInventory().checkItem(40308, 1000)) {
		pc.getInventory().consumeItem(40308, 1000);
		pc.getInventory().storeItem(L1ItemId.MAGIC_BREATH, 1); // 마력의 숨결
		htmlid = "hugrint2";
		} else {
		htmlid = "hugrint3"; // 아덴부족
		}
		} else {
		htmlid = "hugrint4"; // 숨결이미 가지고있음
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70842) { // 마르바
		htmlid = 마르바(pc, obj, s);
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70840) { // 로빈후드
		htmlid = 로빈후드(pc, s);
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 600005) {// 지브릴
		htmlid = 지브릴(pc, s);
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71179) { // 디에츠(빛고목제작)
		if (s.equalsIgnoreCase("A")) {// 복원된 고대의 목걸이
		Random random = new Random();
		if (pc.getInventory().checkItem(49028, 1) && pc.getInventory().checkItem(49029, 1) && pc.getInventory().checkItem(49030, 1) && pc.getInventory().checkItem(41139, 1)) { // 보석과
		// 볼품없는
		// 목걸이 확인
		if (random.nextInt(10) > 6) {
		materials = new int[] { 49028, 49029, 49030, 41139 };
		counts = new int[] { 1, 1, 1, 1 };
		createitem = new int[] { 41140 }; // 복원된 고대의 목걸이
		createcount = new int[] { 1 };
		htmlid = "dh8";
		} else { // 실패의 경우 아이템만 사라짐
		materials = new int[] { 49028, 49029, 49030, 41139 };
		counts = new int[] { 1, 1, 1, 1 };
		createitem = new int[] { 410027 }; // 보석 가루
		createcount = new int[] { 5 };
		htmlid = "dh7";
		}
		} else { // 재료가 부족한 경우
		htmlid = "dh6";
		}
		} else if (s.equalsIgnoreCase("B")) {// 빛나는 고대의 목걸이 제작을 부탁한다.
		Random random = new Random();
		if (pc.getInventory().checkItem(49027, 1) && pc.getInventory().checkItem(41140, 1)) { // 다이아몬드와
		// 복원된 목걸이
		if (random.nextInt(10) > 7) {
		materials = new int[] { 49027, 41140 };
		counts = new int[] { 1, 1 };
		createitem = new int[] { 20422 }; // 빛나는 고대 목걸이
		createcount = new int[] { 1 };
		htmlid = "dh9";
		} else {
		materials = new int[] { 49027, 41140 };
		counts = new int[] { 1, 1 };
		createitem = new int[] { 410027 }; // 보석가루
		createcount = new int[] { 5 };
		htmlid = "dh7";
		}
		} else { // 재료가 부족한 경우
		htmlid = "dh6";
		}
		}
		// 첩보원(그림자의 신전측)
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 81202) {
		// 「화가 나지만 승낙한다」
		if (s.equalsIgnoreCase("n")) {
		htmlid = "";
		poly(client, 6035);
final int[] item_ids = { 41123, 41124, 41125 };
final int[] item_amounts = { 1, 1, 1 };
		L1ItemInstance item = null;
		for (int i = 0; i < item_ids.length; i++) {
		item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		pc.getQuest().set_step(L1Quest.QUEST_SHADOWS, 1);
		}
		// 「그런 임무는 그만둔다」
		} else if (s.equalsIgnoreCase("d")) {
		htmlid = "minitos09";
		pc.getInventory().consumeItem(41121, 1);
		pc.getInventory().consumeItem(41122, 1);
		// 「초기화한다」
		} else if (s.equalsIgnoreCase("k")) {
		htmlid = "";
		pc.getInventory().consumeItem(41123, 1); // 카헬의 타락 한 가루
		pc.getInventory().consumeItem(41124, 1); // 카헬의 무력 한 가루
		pc.getInventory().consumeItem(41125, 1); // 카헬의 아집 한 가루
		pc.getInventory().consumeItem(41126, 1); // 핏자국의 타락 한 정수
		pc.getInventory().consumeItem(41127, 1); // 핏자국의 무력 한 정수
		pc.getInventory().consumeItem(41128, 1); // 핏자국의 아집 한 정수
		pc.getInventory().consumeItem(41129, 1); // 핏자국의 정수
		pc.getQuest().set_step(L1Quest.QUEST_SHADOWS, 0);
		// 정수를 건네준다
		} else if (s.equalsIgnoreCase("e")) {
		if (pc.getQuest().get_step(L1Quest.QUEST_SHADOWS) == L1Quest.QUEST_END || pc.getKarmaLevel() >= 1) {
		htmlid = "";
		} else {
		if (pc.getInventory().checkItem(41129)) {
		htmlid = "";
		pc.addKarma((int) (-1600 * Config.ServerRates.RateKarma));
		pc.sendPackets(new S_Karma(pc));
		pc.getInventory().consumeItem(41121, 1); // 카헬의 계약서
		pc.getInventory().consumeItem(41122, 1); // 카헬의 지령서
		pc.getInventory().consumeItem(41129, 1); // 핏자국의 정수
		pc.getQuest().set_step(L1Quest.QUEST_SHADOWS, L1Quest.QUEST_END);
		} else {
		htmlid = "minitos04";
		}
		}
		// 재빠르게 받는다
		} else if (s.equalsIgnoreCase("g")) {
		htmlid = "";
final int[] item_ids = { 41121 }; // 카헬의 계약서
final int[] item_amounts = { 1 };
		L1ItemInstance item = null;
		for (int i = 0; i < item_ids.length; i++) {
		item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
		pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == BugRaceController.RACE_SELLER_NPCID) {
		if (s.equals("status")) {
		if (pc.isGm())
		pc.sendPackets(S_PsyUml.bugRaceUml(true));
		else
		pc.sendPackets(S_PsyUml.bugRaceUml(false));
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 370041) {
		if (s.equals("status")) {
		pc.sendPackets(S_PsyUml.powerBallUml());
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 777849) { // 킬톤 (호랑이)

		// 如果 NPC 的模板 ID 是 777849（킬톤，老虎）

		if (s.equalsIgnoreCase("0")) { // 킬톤的契約書

		// 如果字符串 s 等於 "0"（請求 킬톤的契約書）

		if (pc.getInventory().checkItem(40308, 500000)) {

		// 如果玩家擁有 500,000 個編號為 40308 的物品（Adena）

		pc.getInventory().consumeItem(40308, 500000);

		// 消耗玩家的 500,000 個 Adena

		pc.getInventory().storeItem(87050, 1);

		// 玩家獲得一個編號為 87050 的物品（킬톤的契約書）

		pc.sendPackets(new S_SystemMessage("虎頓給了您虎頓的契約書."));

		// 向玩家發送系統消息通知他們 "킬톤給了您 킬톤的契約書。"

		htmlid = "";

		// 重置 htmlid 變量為空字符串

		} else {
		// 如果玩家沒有足夠的 Adena

		htmlid = "killton3"; // 아데나 부족시

		// 設置 htmlid 為 "killton3"（Adena 不足時顯示的畫面）
		}
		}

		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 777848) { // 메린(진돗개)

		// 如果 NPC 的模板 ID 是 777848（메린，珍島犬）

		if (s.equalsIgnoreCase("0")) { // 메린的契約書

		// 如果字符串 s 等於 "0"（請求 메린的契約書）

		if (pc.getInventory().checkItem(40308, 500000)) {

		// 如果玩家擁有 500,000 個編號為 40308 的物品（Adena）

		pc.getInventory().consumeItem(40308, 500000);

		// 消耗玩家的 500,000 個 Adena

		pc.getInventory().storeItem(87051, 1);

		// 玩家獲得一個編號為 87051 的物品（메린的契約書）

		pc.sendPackets(new S_SystemMessage("美琳給了您美琳的契約書."));

		// 向玩家發送系統消息通知他們 "메린給了您 메린的契約書。"

		htmlid = "";

		// 重置 htmlid 變量為空字符串

		} else {

		// 如果玩家沒有足夠的 Adena

		htmlid = "merin3"; // 아데나 부족시

		// 設置 htmlid 為 "merin3"（Adena 不足時顯示的畫面）
		}
		}
		// 마빈
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70000) {
		if (s.equalsIgnoreCase("b")) {
		if (pc.getLevel() >= 70) {// 이게 2가지 액션으로 사용하는데 뭐지..
		if (!pc.getInventory().checkItem(700012)) {
		pc.getInventory().storeItem(700012, 1);
		htmlid = "marbinquest2";
		}
		}
		}
		if (s.equalsIgnoreCase("a")) {
		if (pc.getInventory().checkItem(700013, 1) && pc.getInventory().checkItem(700016, 1) && pc.getInventory().checkItem(700015, 100)) {
		pc.getInventory().consumeItem(700013, 1);
		pc.getInventory().consumeItem(700016, 1);
		pc.getInventory().consumeItem(700015, 100);
		pc.getInventory().storeItem(700017, 5);
		addQuestExp(pc, 2);
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 3944));
		pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
		htmlid = "marbinquest4";
		} else {
		htmlid = "marbinquest9";
		}
		} else if (s.equalsIgnoreCase("b")) {
		if (pc.getInventory().checkItem(700014, 1) && pc.getInventory().checkItem(1000004, 1) && pc.getInventory().checkItem(700015, 100)) {
		pc.getInventory().consumeItem(700014, 1);
		pc.getInventory().consumeItem(1000004, 1);
		pc.getInventory().consumeItem(700015, 100);
		pc.getInventory().storeItem(700017, 15);
		addQuestExp(pc, 3);
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 3944));
		pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
		htmlid = "marbinquest2";
		} else {
		htmlid = "marbinquest8";
		}
		} else if (s.equalsIgnoreCase("d")) {
		if (pc.getInventory().checkItem(700014, 1) && pc.getInventory().checkItem(1000004, 1) && pc.getInventory().checkItem(700015, 100)) {
		pc.getInventory().consumeItem(700014, 1);
		pc.getInventory().consumeItem(1000004, 1);
		pc.getInventory().consumeItem(700015, 100);
		pc.getInventory().storeItem(700017, 15);
		addQuestExp(pc, 9);
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 3944));
		pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
		htmlid = "marbinquest6";
		} else {
		htmlid = "marbinquest7";
		}
		} else if (s.equalsIgnoreCase("c")) {
		if (pc.getInventory().checkItem(700014, 1) && pc.getInventory().checkItem(700016, 1) && pc.getInventory().checkItem(700015, 100)) {
		pc.getInventory().consumeItem(700014, 1);
		pc.getInventory().consumeItem(700016, 1);
		pc.getInventory().consumeItem(700015, 100);
		pc.getInventory().storeItem(700017, 15);
		addQuestExp(pc, 6);
		pc.broadcastPacket(new S_SkillSound(pc.getId(), 3944));
		pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
		htmlid = "marbinquest6";
		} else {
		htmlid = "marbinquest7";
		}
		}
		// 피어스
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70908) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		String npcName = npc.getNpcTemplate().get_name();

		if (s.equals("a")) { // 어둠의 힘을 원합니다.
		int[] list = new int[2];
		for (L1ItemInstance item : pc.getInventory().getItems()) {
		switch (item.getItemId()) {
		case 13:
		case 81:
		case 162:
		case 177:
		case 194:
		if (item.getEnchantLevel() == 8 && pc.getInventory().checkItem(40308, 5000000)) {
		list[0] = 1;
		} else if (item.getEnchantLevel() == 9 && pc.getInventory().checkItem(40308, 10000000)) {
		list[1] = 2;
		}
		break;
default:
		break;
		}
		}
		int count = list[0] + list[1];
		switch (count) {
		case 1:
		htmlid = "piers03";
		break;
		case 2:
		htmlid = "piers02";
		break;
		case 3:
		htmlid = "piers01";
		break;
default:
		htmlid = "piers04";
		break;
		}
		} else if (s.equals("0")) {
		PiersItem(pc, objid, npcName, pc.PiersItemId[0], pc.PiersEnchant[0], pc.PiersItemId[18]);
		} else if (s.equals("1")) {
		PiersItem(pc, objid, npcName, pc.PiersItemId[1], pc.PiersEnchant[1], pc.PiersItemId[18]);
		} else if (s.equals("2")) {
		PiersItem(pc, objid, npcName, pc.PiersItemId[2], pc.PiersEnchant[2], pc.PiersItemId[18]);
		} else if (s.equals("3")) {
		PiersItem(pc, objid, npcName, pc.PiersItemId[3], pc.PiersEnchant[3], pc.PiersItemId[18]);
		} else if (s.equals("4")) {
		PiersItem(pc, objid, npcName, pc.PiersItemId[4], pc.PiersEnchant[4], pc.PiersItemId[18]);
		} else if (s.equals("5")) {
		PiersItem(pc, objid, npcName, pc.PiersItemId[5], pc.PiersEnchant[5], pc.PiersItemId[18]);
		} else if (s.equals("6")) {
		PiersItem(pc, objid, npcName, pc.PiersItemId[6], pc.PiersEnchant[6], pc.PiersItemId[18]);
		} else if (s.equals("7")) {
		PiersItem(pc, objid, npcName, pc.PiersItemId[7], pc.PiersEnchant[7], pc.PiersItemId[18]);
		} else if (s.equals("8")) {
		PiersItem(pc, objid, npcName, pc.PiersItemId[8], pc.PiersEnchant[8], pc.PiersItemId[18]);
		} else if (s.equals("9")) {
		PiersItem(pc, objid, npcName, pc.PiersItemId[9], pc.PiersEnchant[9], pc.PiersItemId[18]);
		} else if (s.equals("A")) { // +7 파괴의 크로우
		PiersCheckItem(pc, objid, 8, 5000000, 30018, 7);
		} else if (s.equals("B")) { // +7 파괴의 이도류
		PiersCheckItem(pc, objid, 8, 5000000, 30019, 7);
		} else if (s.equals("C")) { // +8 파괴의 크로우
		PiersCheckItem(pc, objid, 9, 10000000, 30020, 8);
		} else if (s.equals("D")) { // +8 파괴의 이도류
		PiersCheckItem(pc, objid, 9, 10000000, 30021, 8);
		}
		// 시장 경비병/시장 중심지 이동
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50025 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50032 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50048
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50058 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5067 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5070) {
		if (s.equals("teleport giran-trade-zone-giran") || s.equals("teleport Mobjtele2")) {
		int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
		pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 18339, true, false);
		} else if (s.equals("EnterSeller")) {
		s2 = readS();
		L1PcInstance targetShop = L1World.getInstance().getPlayer(s2);
		if (targetShop != null && targetShop.isPrivateShop() && pc.getMapId() == targetShop.getMapId()) {
		pc.setFindMerchantId(targetShop.getId());
		pc.start_teleport(targetShop.getX() - 1, targetShop.getY() + 1, targetShop.getMapId(), 1, 18339, true, false);
		} else {
		L1NpcShopInstance nn = L1World.getInstance().getShopNpc(s2);
		if (nn != null && nn.getMapId() == 800 && nn.getState() == 1) {
		pc.setFindMerchantId(nn.getId());
		pc.start_teleport(nn.getX() + CommonUtil.random(3) - 1, nn.getY() + CommonUtil.random(3) - 1, nn.getMapId(), 0, 18339, false, false);
		}
		}
		} else if (s.equals("teleport Mobjtele1")) {
		pc.start_teleport(32800, 32927, 800, pc.getHeading(), 18339, true, false);
		}
		// 적대적인 얼음 여왕 근위병
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5079) {
		if (s.equalsIgnoreCase("enter")) {
		QueenSystem.getInstance().startQueen(pc);
		}
		// 우호적인 얼음 여왕 근위병
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5078) {
		if (s.equalsIgnoreCase("enter")) {
		DemonSystem.getInstance().startDemon(pc);
		}
		// 상아탑 첩보원
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5086) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		String npcName = npc.getNpcTemplate().get_name();
		if (s.equalsIgnoreCase("a")) {
		if (pc.getInventory().checkItem(30055, 1)) { // 화염의 막대
		htmlid = "icqwand4";
		} else {
		createNewItem(pc, npcName, 30055, 120, 0);
		htmlid = "icqwand2";
		}
		} else if (s.equalsIgnoreCase("b")) {
		if (pc.getInventory().checkItem(30056, 1)) { // 신비한 회복 물약
		htmlid = "icqwand4";
		} else {
		createNewItem(pc, npcName, 30056, 100, 0);
		htmlid = "icqwand3";
		}
		}
		// 베테르랑
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5111) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		String npcName = npc.getNpcTemplate().get_name();
		int[] weapon = new int[] { 1126, 1127, 1128, 1129, 1130, 1131, 1132, 1133 };
		int[] armor = new int[] { 22328, 22329, 22330, 22331, 22332, 22333, 22334, 22335 };
		int result = 0;
		if (s.equals("a")) { // 1. 베테르랑 단검
		if (pc.getInventory().checkItem(30065, 1)) {
		if (pc.isCrown() || pc.isKnight() || pc.isWizard() || pc.isElf() || pc.isDarkelf()) {
		createNewItem(pc, npcName, 1126, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE6";
		}
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("b")) { // 2. 베테르랑 한손검
		if (pc.getInventory().checkItem(30065, 1)) {
		if (pc.isCrown() || pc.isKnight() || pc.isElf() || pc.isDragonknight()) {
		createNewItem(pc, npcName, 1127, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE6";
		}
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("c")) { // 3. 베테르랑 양손검
		if (pc.getInventory().checkItem(30065, 1)) {
		if (pc.isCrown() || pc.isKnight() || pc.isDragonknight()) {
		createNewItem(pc, npcName, 1128, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE6";
		}
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("d")) { // 4. 베테르랑 보우건
		if (pc.getInventory().checkItem(30065, 1)) {
		if (pc.isElf() || pc.isDarkelf() || pc.isBlackwizard()) {
		createNewItem(pc, npcName, 1129, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE6";
		}
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("e")) { // 5. 베테르랑 지팡이
		if (pc.getInventory().checkItem(30065, 1)) {
		if (pc.isWizard() || pc.isBlackwizard()) {
		createNewItem(pc, npcName, 1130, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE6";
		}
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("f")) { // 6. 베테르랑 크로우
		if (pc.getInventory().checkItem(30065, 1)) {
		if (pc.isDarkelf()) {
		createNewItem(pc, npcName, 1131, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE6";
		}
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("g")) { // 7. 베테르랑 체인소드
		if (pc.getInventory().checkItem(30065, 1)) {
		if (pc.isDragonknight()) {
		createNewItem(pc, npcName, 1132, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE6";
		}
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("h")) { // 8. 베테르랑 키링크
		if (pc.getInventory().checkItem(30065, 1)) {
		if (pc.isBlackwizard()) {
		createNewItem(pc, npcName, 1133, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE6";
		}
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("i")) { // 1. 베테르랑 판금 갑옷
		if (pc.getInventory().checkItem(30065, 1)) {
		if (pc.isKnight()) {
		createNewItem(pc, npcName, 22328, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE6";
		}
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("j")) { // 2. 베테르랑 가죽 갑옷
		if (pc.getInventory().checkItem(30065, 1)) {
		if (pc.isCrown() || pc.isElf() || pc.isDarkelf() || pc.isDragonknight() || pc.isBlackwizard()) {
		createNewItem(pc, npcName, 22329, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE6";
		}
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("k")) { // 3. 베테르랑 로브
		if (pc.getInventory().checkItem(30065, 1)) {
		if (pc.isWizard() || pc.isBlackwizard()) {
		createNewItem(pc, npcName, 22330, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE6";
		}
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("l")) { // 4. 베테르랑 방패
		if (pc.getInventory().checkItem(30065, 1)) {
		if (pc.isKnight() | pc.isDragonknight()) {
		createNewItem(pc, npcName, 22331, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE6";
		}
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("m")) { // 5. 베테르랑 티셔츠
		if (pc.getInventory().checkItem(30065, 1)) {
		createNewItem(pc, npcName, 22332, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("n")) { // 6. 베테르랑 장화
		if (pc.getInventory().checkItem(30065, 1)) {
		createNewItem(pc, npcName, 22333, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("o")) { // 7. 베테르랑 해골 투구
		if (pc.getInventory().checkItem(30065, 1)) {
		if (pc.isCrown() || pc.isKnight() || pc.isDragonknight()) {
		createNewItem(pc, npcName, 22334, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE6";
		}
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("p")) { // 8. 베테르랑 마법 망토
		if (pc.getInventory().checkItem(30065, 1)) {
		if (pc.isElf() || pc.isDarkelf() || pc.isBlackwizard()) {
		createNewItem(pc, npcName, 22335, 1, 0);
		pc.getInventory().consumeItem(30065, 1);
		htmlid = "veteranE2";
		} else {
		htmlid = "veteranE6";
		}
		} else {
		htmlid = "veteranE1";
		}
		} else if (s.equals("1")) { // 1. 체력 물약(포도주스) 무기</font>를 아이템으로
		for (int i = 0; i < weapon.length; i++) {
		if (pc.getInventory().checkItem(weapon[i], 1)) {
		pc.getInventory().consumeItem(weapon[i], 1);
		createNewItem(pc, npcName, 60029, 50, 0);
		result = 1;
		break;
		}
		}
		if (result == 1) {
		htmlid = "veteranE3";
		} else {
		htmlid = "veteranE4";
		}
		} else if (s.equals("2")) { // 2. 용사의 무기 마법 주문서
		for (int i = 0; i < weapon.length; i++) {
		if (pc.getInventory().checkItem(weapon[i], 1)) {
		pc.getInventory().consumeItem(weapon[i], 1);
		createNewItem(pc, npcName, 30068, 1, 0);
		result = 1;
		break;
		}
		}
		if (result == 1) {
		htmlid = "veteranE3";
		} else {
		htmlid = "veteranE4";
		}
		} else if (s.equals("3")) { // 3. 용사의 갑옷 마법 주문서
		for (int i = 0; i < weapon.length; i++) {
		if (pc.getInventory().checkItem(weapon[i], 1)) {
		pc.getInventory().consumeItem(weapon[i], 1);
		createNewItem(pc, npcName, 30069, 1, 0);
		result = 1;
		break;
		}
		}
		if (result == 1) {
		htmlid = "veteranE3";
		} else {
		htmlid = "veteranE4";
		}
		} else if (s.equals("4")) { // 1. 체력 물약(포도주스) 방어구</font>를 아이템으로
		for (int i = 0; i < armor.length; i++) {
		if (pc.getInventory().checkItem(armor[i], 1)) {
		pc.getInventory().consumeItem(armor[i], 1);
		createNewItem(pc, npcName, 60029, 50, 0);
		result = 1;
		break;
		}
		}
		if (result == 1) {
		htmlid = "veteranE3";
		} else {
		htmlid = "veteranE4";
		}
		} else if (s.equals("5")) { // 2. 용사의 무기 마법 주문서
		for (int i = 0; i < armor.length; i++) {
		if (pc.getInventory().checkItem(armor[i], 1)) {
		pc.getInventory().consumeItem(armor[i], 1);
		createNewItem(pc, npcName, 30068, 1, 0);
		result = 1;
		break;
		}
		}
		if (result == 1) {
		htmlid = "veteranE3";
		} else {
		htmlid = "veteranE4";
		}
		} else if (s.equals("6")) { // 3. 용사의 갑옷 마법 주문서
		for (int i = 0; i < armor.length; i++) {
		if (pc.getInventory().checkItem(armor[i], 1)) {
		pc.getInventory().consumeItem(armor[i], 1);
		createNewItem(pc, npcName, 30069, 1, 0);
		result = 1;
		break;
		}
		}
		if (result == 1) {
		htmlid = "veteranE3";
		} else {
		htmlid = "veteranE4";
		}
		}
		// 角色支援團邏輯
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5112) {

		// 如果 NPC 的模板 ID 是 5112

		L1NpcInstance npc = (L1NpcInstance) obj;
		String npcName = npc.getNpcTemplate().get_name();

		if (s.equals("a")) { // 1.等級提升（最高75級）

		// 如果玩家選擇 "a"（請求等級提升）

		if (pc.getLevel() < 51) {

		// 如果玩家等級小於51
		pc.set_exp(ExpTable.getExpByLevel(51));

		// 將玩家經驗值設置為51級所需的經驗值

		} else if (pc.getLevel() >= 51 && pc.getLevel() < 75) {

		// 如果玩家等級在51到75之間
		pc.set_exp(ExpTable.getExpByLevel(pc.getLevel() + 1));

		// 將玩家經驗值設置為當前等級的下一級所需的經驗值

		} else if (pc.getLevel() >= 75) {

		// 如果玩家等級大於等於75
		htmlid = "lind_sm1";

		// 設置 htmlid 為 "lind_sm1"
		}

		} else if (s.equals("b")) { // 2.支援物品發放

		// 如果玩家選擇 "b"（請求支援物品）

		if (pc.getLevel() < 75) {

		// 如果玩家等級小於75
		htmlid = "lind_sm9";

		// 設置 htmlid 為 "lind_sm9"

		} else if (pc.getInventory().checkItem(30070, 1)) { // 如果玩家擁有支援確認書

		// 如果玩家背包中有物品編號30070（支援確認書）
		htmlid = "lind_sm4";

		// 設置 htmlid 為 "lind_sm4"

		} else { // 發放 [ 支援確認書, 支援箱, 回想的蠟燭 ]

		// 如果玩家沒有支援確認書

		createNewItem(pc, npcName, 30071, 1, 0); // 發放亞丁王國支援箱
		createNewItem(pc, npcName, 30070, 1, 0); // 發放亞丁王國支援確認書
		createNewItem(pc, npcName, 200000, 1, 0); // 發放回想的蠟燭
		htmlid = "lind_sm2";

		// 設置 htmlid 為 "lind_sm2"
		}

		} else if (s.equals("c")) { // 3.回憶的蠟燭支援

		// 如果玩家選擇 "c"（請求回憶的蠟燭）

		if (pc.getInventory().checkItem(200000, 1)) { // 如果玩家背包中有回想的蠟燭

		// 如果玩家擁有物品編號200000（回憶的蠟燭）
		htmlid = "lind_sm5";

		// 設置 htmlid 為 "lind_sm5"

		} else {

		// 如果玩家沒有回憶的蠟燭

		createNewItem(pc, npcName, 200000, 1, 0);

		// 發放回憶的蠟燭
		htmlid = "lind_sm6";

		// 設置 htmlid 為 "lind_sm6"
		} else if (s.equals("d")) { // 1.드래곤의 혈흔(안타라스) 받기
		pc.sendPackets(new S_SkillSound(pc.getId(), 7783));
		pc.setSkillEffect(L1SkillId.ANTA_BUFF, 7200 * 1000);
		pc.sendPackets(new S_OwnCharAttrDef(pc));
		pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 82, 7200 / 60));
		htmlid = "lind_sm8";
		pc.sendPackets(new S_NPCTalkReturn(objid, "lind_sm8"));
		} else if (s.equals("e")) { // 2.드래곤의 혈흔(파푸리온) 받기.
		pc.sendPackets(new S_SkillSound(pc.getId(), 7783));
		pc.setSkillEffect(L1SkillId.FAFU_BUFF, 7200 * 1000);
		pc.sendPackets(new S_OwnCharAttrDef(pc));
		pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 85, 7200 / 60));
		htmlid = "lind_sm8";
		}
		// 반지개방
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5162) {
		if (s.equals("A")) {
		if (pc.getQuest().isEnd(L1Quest.QUEST_RING_LEFT_SLOT60)) {
		pc.sendPackets(new S_ServerMessage(3255));// 해당 슬롯은 이미 확장되었습니다.
		} else {
		if (pc.getInventory().checkItem(3000400, 1) /* && pc.getInventory().checkItem(40308, 10000000) */ && pc.getLevel() >= 60) {
		pc.getInventory().consumeItem(3000400, 1);
		// pc.getInventory().consumeItem(40308, 10000000);
		pc.getQuest().set_end(L1Quest.QUEST_RING_LEFT_SLOT60);
		pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_LRING));
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
		pc.sendPackets(new S_SkillSound(pc.getId(), 12003));
		} else {
		if (pc.getInventory().checkItem(40308, 20000000) && pc.getLevel() >= 60) {
		pc.getInventory().consumeItem(40308, 20000000);
		pc.getQuest().set_end(L1Quest.QUEST_RING_LEFT_SLOT60);
		pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_LRING));
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
		pc.sendPackets(new S_SkillSound(pc.getId(), 12003));
		} else
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot6"));
		}
		}
		} else if (s.equals("B")) { // 81부터
		if (pc.getQuest().isEnd(L1Quest.QUEST_RING_RIGHT_SLOT60)) {
		pc.sendPackets(new S_ServerMessage(3255));// 해당 슬롯은 이미 확장되었습니다.
		} else {
		if (pc.getInventory().checkItem(3000401, 1) /* && pc.getInventory().checkItem(40308, 10000000) */ && pc.getLevel() >= 60) {
		pc.getInventory().consumeItem(3000401, 1);
		// pc.getInventory().consumeItem(40308, 10000000);
		pc.getQuest().set_end(L1Quest.QUEST_RING_RIGHT_SLOT60);
		pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_RRING));
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
		pc.sendPackets(new S_SkillSound(pc.getId(), 12003));
		} else {
		if (pc.getInventory().checkItem(40308, 20000000) && pc.getLevel() >= 60) {
		pc.getInventory().consumeItem(40308, 20000000);
		pc.getQuest().set_end(L1Quest.QUEST_RING_RIGHT_SLOT60);
		pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_RRING));
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
		pc.sendPackets(new S_SkillSound(pc.getId(), 12003));
		} else
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot6"));
		}
		}
		} else if (s.equals("D") || s.equals("C")) {
		if (pc.getQuest().isEnd(L1Quest.QUEST_EARRING_SLOT60)) {
		pc.sendPackets(new S_ServerMessage(3255));// 해당 슬롯은 이미 확장되었습니다.
		} else {
		if (pc.getInventory().checkItem(3000399, 1) /* && pc.getInventory().checkItem(40308, 1000000) */ && pc.getLevel() >= 60) {
		pc.getInventory().consumeItem(3000399, 1);
		// pc.getInventory().consumeItem(40308, 1000000);
		pc.getQuest().set_end(L1Quest.QUEST_EARRING_SLOT60);
		pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_EARRING));
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
		pc.sendPackets(new S_SkillSound(pc.getId(), 12004));
		} else {
		if (pc.getInventory().checkItem(40308, 2000000) && pc.getLevel() >= 60) {
		pc.getInventory().consumeItem(40308, 2000000);
		pc.getQuest().set_end(L1Quest.QUEST_EARRING_SLOT60);
		pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_EARRING));
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
		pc.sendPackets(new S_SkillSound(pc.getId(), 12004));
		} else
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot6"));
		}
		}
		} else if (s.equalsIgnoreCase("E")) { // 견갑
		if (pc.getQuest().isEnd(L1Quest.QUEST_SLOT_SHOULD)) {
		pc.sendPackets(new S_ServerMessage(3255));// 해당 슬롯은 이미 확장되었습니다.
		} else {
		if (pc.getInventory().checkItem(3000402, 1) && pc.getInventory().checkItem(40308, 15000000) && pc.getLevel() >= 60) {
		pc.getInventory().consumeItem(3000402, 1);
		pc.getInventory().consumeItem(40308, 15000000);
		pc.getInventory().storeItem(9996, 1);
		pc.getQuest().set_end(L1Quest.QUEST_SLOT_SHOULD);
		pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_SHOULD));
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
		} else {
		if (pc.getInventory().checkItem(40308, 30000000) && pc.getLevel() >= 60) {
		pc.getInventory().consumeItem(40308, 30000000);
		pc.getInventory().storeItem(9996, 1);
		pc.getQuest().set_end(L1Quest.QUEST_SLOT_SHOULD);
		pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_SHOULD));
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
		} else
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot6"));
		}
		}
		} else if (s.equalsIgnoreCase("F")) { // 휘장
		if (pc.getQuest().isEnd(L1Quest.QUEST_SLOT_BADGE)) {
		pc.sendPackets(new S_ServerMessage(3255));// 해당 슬롯은 이미 확장되었습니다.
		} else {
		if (pc.getInventory().checkItem(3000403, 1) /* && pc.getInventory().checkItem(40308, 1000000) */ && pc.getLevel() >= 60) {
		pc.getInventory().consumeItem(3000403, 1);
		// pc.getInventory().consumeItem(40308, 1000000);
		pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_BADGE));
		pc.getQuest().set_end(L1Quest.QUEST_SLOT_BADGE);
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
		pc.sendPackets(189);
		} else {
		if (pc.getInventory().checkItem(40308, 2000000) && pc.getLevel() >= 60) {
		pc.getInventory().consumeItem(40308, 2000000);
		pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, S_ReturnedStat.OPEN_SLOT_BADGE));
		pc.getQuest().set_end(L1Quest.QUEST_SLOT_BADGE);
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
		} else
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot10"));
		}
		}
		}
		// 안톤
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70614) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		String npcName = npc.getNpcTemplate().get_name();

		int enchant = 0;
		int itemId = 0;
		int oldArmor = 0;
		if (s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D") // 판금
		|| s.equals("E") || s.equals("F") || s.equals("G") || s.equals("H") // 비늘
		|| s.equals("I") || s.equals("J") || s.equals("K") || s.equals("L") // 가죽
		|| s.equals("M") || s.equals("N") || s.equals("O") || s.equals("P")) { // 로브
		if (s.equals("A") || s.equals("B") || s.equals("C") || s.equals("D")) {
		if (s.equals("A")) {
		enchant = 7;
		} else if (s.equals("B")) {
		enchant = 8;
		} else if (s.equals("C")) {
		enchant = 9;
		} else if (s.equals("D")) {
		enchant = 10;
		}
		oldArmor = 20095;
		itemId = 222300;
		} else if (s.equals("E") || s.equals("F") || s.equals("G") || s.equals("H")) {
		if (s.equals("E")) {
		enchant = 7;
		} else if (s.equals("F")) {
		enchant = 8;
		} else if (s.equals("G")) {
		enchant = 9;
		} else if (s.equals("H")) {
		enchant = 10;
		}
		oldArmor = 20094;
		itemId = 222301;
		} else if (s.equals("I") || s.equals("J") || s.equals("K") || s.equals("L")) {
		if (s.equals("I")) {
		enchant = 7;
		} else if (s.equals("J")) {
		enchant = 8;
		} else if (s.equals("K")) {
		enchant = 9;
		} else if (s.equals("L")) {
		enchant = 10;
		}
		oldArmor = 20092;
		itemId = 222302;
		} else if (s.equals("M") || s.equals("N") || s.equals("O") || s.equals("P")) {
		if (s.equals("M")) {
		enchant = 7;
		} else if (s.equals("N")) {
		enchant = 8;
		} else if (s.equals("O")) {
		enchant = 9;
		} else if (s.equals("P")) {
		enchant = 10;
		}
		oldArmor = 20093;
		itemId = 222303;
		}
		if (pc.getInventory().checkEnchantItem(20110, enchant, 1) && pc.getInventory().checkItem(41246, 100000) && pc.getInventory().checkItem(oldArmor, 1)) {
		pc.getInventory().consumeEnchantItem(20110, enchant, 1);
		pc.getInventory().consumeItem(41246, 100000); // 용해제
		pc.getInventory().consumeItem(oldArmor, 1); // 고대의
		createNewItem(pc, npcName, itemId, 1, enchant - 7);
		htmlid = "";
		} else {
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "anton9"));
		}
		} else if (s.equals("1") || s.equals("2") || s.equals("3") || s.equals("4")) {
		if (s.equals("1")) {
		oldArmor = 20095;
		itemId = 222300;
		} else if (s.equals("2")) {
		oldArmor = 20094;
		itemId = 222301;
		} else if (s.equals("3")) {
		oldArmor = 20092;
		itemId = 222302;
		} else if (s.equals("4")) {
		oldArmor = 20093;
		itemId = 222303;
		}
		if (pc.getInventory().checkItem(49015, 1) && pc.getInventory().checkItem(oldArmor, 1)) {
		pc.getInventory().consumeItem(49015, 1); // 블랙미스릴용액
		pc.getInventory().consumeItem(oldArmor, 1); // 고대의 시리즈
		createNewItem(pc, npcName, itemId, 1, enchant);
		htmlid = "";
		} else {
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "anton9"));
		}
		}

		// 카너스
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5125) {
		htmlid = 카너스(pc, s);
		// 다크엘프 생존자
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5131) {
		if (s.equals("exitghost")) {
		pc.makeReadyEndGhost();
		}
		// 수호병사 OUT
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5128) {
		if (s.equals("a")) {
		// L1Teleport.teleport(pc, 32614, 33195, (short) 4,
		// pc.getHeading(), true);
		pc.start_teleport(32614, 33195, 4, pc.getHeading(), 18339, true, false);
		htmlid = "";
		}

		// 수호병사 IN
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5130) {
		if (s.equals("a")) {
		pc.start_teleport(32617, 33215, 4, pc.getHeading(), 18339, true, false);
		htmlid = "";
		}
		// 감시자의 눈
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5133) {
		if (s.equals("exitghost")) {
		pc.makeReadyEndGhost();
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5010) {
		if (s.equals("b")) { // 接受魔法增益
//                if (pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
		if (pc.isPcBuff()) { // 判斷玩家是否能接受增益
		if (pc.getInventory().checkItem(41921, 2)) { // 檢查玩家是否擁有2個物品編號41921
		pc.getInventory().consumeItem(41921, 2); // 消耗2個物品編號41921

		int[] allBuffSkill = { 26, 37, 42, 48 }; // 定義所有的增益技能
		pc.setBuffnoch(1); // 設置增益狀態
		L1SkillUse l1skilluse = new L1SkillUse();
		for (int i = 0; i < allBuffSkill.length; i++) {
		l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF); // 應用增益技能
		}
		pc.setBuffnoch(0); // 重置增益狀態

		} else if (pc.getInventory().checkItem(41921, 3)) { // 檢查玩家是否擁有3個物品編號41921
		pc.getInventory().consumeItem(41921, 3); // 消耗3個物品編號41921

		int[] allBuffSkill = { 26, 37, 42, 48 }; // 定義所有的增益技能
		pc.setBuffnoch(1); // 設置增益狀態
		L1SkillUse l1skilluse = new L1SkillUse();
		for (int i = 0; i < allBuffSkill.length; i++) {
		l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF); // 應用增益技能
		}
		pc.setBuffnoch(0); // 重置增益狀態

		} else {
		pc.sendPackets(new S_SystemMessage("缺少金色精靈羽毛。")); // 如果玩家沒有足夠的金色精靈羽毛，發送系統訊息
		}
		}
		}
		} else if (s.equals("c")) { // 成長的增益
//                if (pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
		if (pc.isPcBuff()) { // 判斷玩家是否能接受增益
		if (pc.getInventory().checkItem(41921, 2)) { // 檢查玩家是否擁有2個物品編號41921
		pc.getInventory().consumeItem(41921, 2); // 消耗2個物品編號41921
		int time = 3600; // 增益持續時間為3600秒
		pc.setSkillEffect(L1SkillId.EXP_POTION, time * 1000); // 設置經驗增益效果
		S_SkillSound es = new S_SkillSound(pc.getId(), 13249); // 設置技能效果音效
		pc.sendPackets(es, false); // 發送效果音效
		pc.broadcastPacket(es); // 廣播效果音效
		pc.sendPackets(new S_ServerMessage(1313)); // 發送系統消息
		} else if (pc.getInventory().checkItem(41921, 3)) { // 檢查玩家是否擁有3個物品編號41921
		pc.getInventory().consumeItem(41921, 3); // 消耗3個物品編號41921
		int time = 3600; // 增益持續時間為3600秒
		pc.setSkillEffect(L1SkillId.EXP_POTION, time * 1000); // 設置經驗增益效果
		S_SkillSound es = new S_SkillSound(pc.getId(), 13249); // 設置技能效果音效
		pc.sendPackets(es, false); // 發送效果音效
		pc.broadcastPacket(es); // 廣播效果音效
		pc.sendPackets(new S_ServerMessage(1313)); // 發送系統消息
		} else {
		pc.sendPackets(new S_SystemMessage("沒有足夠的金色精靈羽毛。")); // 如果玩家沒有足夠的金色精靈羽毛，發送系統訊息
		}
		}
		}
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5161) {
		htmlid = 비자야(pc, obj, s);
		/** 마을 강화 버프사 통합 **/
		} else if ( ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 21025 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 900088 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7310100 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7132
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 73201237 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 73201245 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 120837
		|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 899999 || ((L1NpcInstance) obj).getNpcTemplate().get_npcId() >= 5000 && ((L1NpcInstance) obj).getNpcTemplate().get_npcId() <= 5012) {
		htmldata = 生成HTML數據(s, pc); // 生成 HTML 數據
		htmlid = ZenDoor(s, pc, objid); // 獲取 ZenDoor 的 HTML ID
		} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 120834) {
		htmlid = HeyShop(s, pc, objid); // 獲取 HeyShop 的 HTML ID
		} else {
		int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId(); // 獲取 NPC ID
		if (Config.Login.UseShiftServer) { // 如果使用 Shift 伺服器
		if (MJShiftObjectManager.getInstance().is_battle_server_thebes()) { // 如果是 Thebes 戰鬥伺服器
		if (npcid == 7320089 && s.equalsIgnoreCase("a")) {
		MJShiftObjectManager.getInstance().do_send_battle_server(pc, MJString.EmptyString); // 發送到戰鬥伺服器
		return;
		}
// TODO 個人互聯伺服器使用時
		} else if (MJShiftObjectManager.getInstance().is_battle_server_fisland()) { // 如果是 FI 島戰鬥伺服器
		if (npcid == 70086 && s.equalsIgnoreCase("a")) {
		MJShiftObjectManager.getInstance().do_send_battle_server(pc, MJString.EmptyString); // 發送到戰鬥伺服器
		return;
		}
		if (npcid == 7320004 && MJShiftObjectManager.getInstance().is_battle_server_ready()) {
		pc.sendPackets("在等待時間中無法進入。"); // 發送等待時間內無法進入的訊息給玩家
		return;
		}
		if (npcid == 505011 && s.equalsIgnoreCase("teleportURL")) {
		MJShiftObjectManager.getInstance().do_returner(pc); // 執行返回操作
		return;
		}

		} else if (MJShiftObjectManager.getInstance().is_battle_server_domtower()) { // 如果是 Dom Tower 戰鬥伺服器
		MJDomTowerNpcActionInfo aInfo = MJDomTowerNpcActionInfo.get_action_info(npcid); // 獲取 Dom Tower NPC 動作信息
		if (aInfo != null) {
		int map_id = aInfo.get_select_mapid(s); // 根據選項獲取地圖 ID
		if (map_id > 0) {
		MJShiftObjectManager.getInstance().do_send_battle_server(pc, String.valueOf(map_id)); // 發送到戰鬥伺服器
		return;
		}
		}
		if (npcid == 8000011 && s.equalsIgnoreCase("a")) { // 如果 NPC ID 是 8000011 且選項是 "a"
		MJShiftObjectManager.getInstance().do_returner(pc); // 執行返回操作
		return;
		}
		if (npcid >= 8000000 && npcid <= 8000010 && MJShiftObjectManager.getInstance().is_battle_server_ready()) { // 如果 NPC ID 在 8000000 到 8000010 範圍內且伺服器準備就緒
		pc.sendPackets("在等待時間中無法進入。"); // 發送等待時間內無法進入的訊息給玩家
		return;
		}
		}
		}

		ActionListener listener = null;

		/** 如果在動作監聽器中達到最大等級 **/
		if ((npcid == 7320204 || npcid == 7310085 || npcid == 50000041) && pc.getLevel() >= Config.ServerAdSetting.NewCha1) {
		pc.sendPackets(String.format("%d等級以上無法進入。", Config.ServerAdSetting.NewCha1)); // 發送消息通知玩家等級限制
		return;
		}

// TODO 在動作監聽器中缺少的條件在下面添加
		if (npcid == 4902001 && pc.getMapId() == 666) {
		pc.sendPackets(538); // 發送 ID 為 538 的封包
		return;
		}

		if (npcid == 70751 && s.equalsIgnoreCase("a")) {
		listener = ActionListenerLoader.getInstance().findListener(npcid, String.format("a%d", MJEClassesType.fromGfx(pc.getClassId()).toInt())); // 獲取相應的動作監聽器
		} else if (npcid == 70751 && s.equalsIgnoreCase("c")) {
		if (pc.getInventory().checkItem(4100460)) { // 檢查玩家是否擁有物品編號4100460
		pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "擁有 '冰凍的心臟' 時無法進入。")); // 發送消息通知玩家無法進入
		return;
		}
		if (!pc.getInventory().checkItem(700019)) { // 檢查玩家是否擁有物品編號700019
		pc.sendPackets(new S_SystemMessage("沒有火焰的氣息。"), true); // 發送消息通知玩家缺少火焰的氣息
		return;
		}
		}
		pc.getInventory().consumeItem(700019, 1);
		IceDungeonSystem.getInstance().startDungeon(pc);
		}

		// TODO 사용안함
		if (npcid == 70752111 && s.equalsIgnoreCase("a")) {
		listener = ActionListenerLoader.getInstance().findListener(npcid, String.format("a%d", MJEClassesType.fromGfx(pc.getClassId()).toInt()));
		} else {
		listener = ActionListenerLoader.getInstance().findListener(npcid, s);
		}

		if (listener == null) {
		htmlid = C_NPCAction2.getInstance().NpcAction(pc, obj, s, htmlid);
		} else {
//				System.out.println(listener);
		htmlid = listener.to_action(pc, obj);
		}
		}
		if (htmlid != null && htmlid.equalsIgnoreCase("colos2")) {
		htmldata = makeUbInfoStrings(((L1NpcInstance) obj).getNpcTemplate().get_npcId());
		}
		if (createitem != null) {
		boolean isCreate = true;
		for (int j = 0; j < materials.length; j++) {
		if (!pc.getInventory().checkItemNotEquipped(materials[j], counts[j])) {
		L1Item temp = ItemTable.getInstance().getTemplate(materials[j]);
		pc.sendPackets(new S_ServerMessage(337, temp.getName()));
		isCreate = false;
		}
		}

		if (isCreate) {
		int create_count = 0;
		int create_weight = 0;
		L1Item temp = null;
		for (int k = 0; k < createitem.length; k++) {
		temp = ItemTable.getInstance().getTemplate(createitem[k]);
		if (temp.isStackable()) {
		if (!pc.getInventory().checkItem(createitem[k])) {
		create_count += 1;
		}
		} else {
		create_count += createcount[k];
		}
		create_weight += temp.getWeight() * createcount[k] / 1000;
		}
		if (pc.getInventory().getSize() + create_count > Config.ServerAdSetting.Inventory_Count) {
		pc.sendPackets(new S_ServerMessage(263));
		return;
		}
		if (pc.getMaxWeight() < pc.getInventory().getWeight() + create_weight) {
		pc.sendPackets(new S_ServerMessage(82));
		return;
		}

		for (int j = 0; j < materials.length; j++) {
		pc.getInventory().consumeItem(materials[j], counts[j]);
		}
		L1ItemInstance item = null;
		for (int k = 0; k < createitem.length; k++) {
		item = pc.getInventory().storeItem(createitem[k], createcount[k]);
		if (item != null) {
		String itemName = ItemTable.getInstance().getTemplate(createitem[k]).getName();
		String createrName = "";
		if (obj instanceof L1NpcInstance) {
		createrName = ((L1NpcInstance) obj).getNpcTemplate().get_name();
		}
		if (createcount[k] > 1) {
		pc.sendPackets(new S_ServerMessage(143, createrName, itemName + " (" + createcount[k] + ")"));
		} else {
		pc.sendPackets(new S_ServerMessage(143, createrName, itemName));
		}
		}
		}
		if (success_htmlid != null) {
		pc.sendPackets(new S_NPCTalkReturn(objid, success_htmlid, htmldata));
		}
		} else {
		if (failure_htmlid != null) {
		pc.sendPackets(new S_NPCTalkReturn(objid, failure_htmlid, htmldata));
		}
		}
		}

		if (htmlid != null) {
		pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
		// System.out.println("Npc액션값: " + s);
		// } else {
		// System.out.println("Npc액션값: " + s);
		}
		}
private String 吉布里爾(L1PcInstance pc, String s) {String htmlid = "";if (s.equals("A")) {
		if (pc.getInventory().checkItem(41348)) {
		pc.getInventory().consumeItem(41348, 1);
		pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 3);
		htmlid = "zybril12";
		} else {
		htmlid = "zybril11";
		}
		} else if (s.equals("B")) {if (pc.getInventory().checkItem(40048, 10) && pc.getInventory().checkItem(40049, 10) && pc.getInventory().checkItem(40050, 10) && pc.getInventory().checkItem(40051, 10)) {pc.getInventory().consumeItem(40048, 10); // 消耗 10 個物品 ID 為 40048 的物品
		pc.getInventory().consumeItem(40049, 10); // 消耗 10 個物品 ID 為 40049 的物品
		pc.getInventory().consumeItem(40050, 10); // 消耗 10 個物品 ID 為 40050 的物品
		pc.getInventory().consumeItem(40051, 10); // 消耗 10 個物品 ID 為 40051 的物品

final int[] item_ids = { 41353 }; // 定義獲得的物品 ID
final int[] item_amounts = { 1 }; // 定義獲得物品的數量
		for (int i = 0; i < item_ids.length; i++) {pc.getInventory().storeItem(item_ids[i], item_amounts[i]); // 存入獲得的物品
		pc.sendPackets(new S_SystemMessage("獲得了 伊娃的匕首。")); // 發送消息通知玩家獲得物品
		}
		pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 4); // 設置任務步驟為 4
		htmlid = "zybril13"; // 設置 HTML ID 為 "zybril13"
		} else {htmlid = ""; // 如果條件不滿足，設置 HTML ID 為空
		}
		} else if (s.equals("C")) {if (pc.getInventory().checkItem(40514, 10) && pc.getInventory().checkItem(41353, 1)) {pc.getInventory().consumeItem(40514, 10); // 消耗10個物品ID為40514的物品
		pc.getInventory().consumeItem(41353, 1); // 消耗1個物品ID為41353的物品
final int[] item_ids = { 41354 }; // 定義獲得的物品ID
final int[] item_amounts = { 1 }; // 定義獲得物品的數量
		for (int i = 0; i < item_ids.length; i++) {pc.getInventory().storeItem(item_ids[i], item_amounts[i]); // 存入獲得的物品
		pc.sendPackets(new S_SystemMessage("獲得了神聖的伊娃之水。")); // 發送消息通知玩家獲得物品
		}
		pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 5); // 設置任務步驟為5
		htmlid = "zybril9"; // 設置HTML ID為"zybril9"
		} else {htmlid = "zybril13"; // 如果條件不滿足，設置HTML ID為"zybril13"
		}
		} else if (s.equals("D")) {if (pc.getInventory().checkItem(41349)) {pc.getInventory().consumeItem(41349, 1); // 消耗1個物品ID為41349的物品
final int[] item_ids = { 41351 }; // 定義獲得的物品ID
final int[] item_amounts = { 1 }; // 定義獲得物品的數量
		for (int i = 0; i < item_ids.length; i++) {pc.getInventory().storeItem(item_ids[i], item_amounts[i]); // 存入獲得的物品
		pc.sendPackets(new S_SystemMessage("獲得了月光的精髓。")); // 發送消息通知玩家獲得物品
		}
		pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 6); // 設置任務步驟為6
		htmlid = "zybril10"; // 設置HTML ID為"zybril10"
		} else {htmlid = "zybril14"; // 如果條件不滿足，設置HTML ID為"zybril14"
		}
		}
		return htmlid;}private String 羅賓漢(L1PcInstance pc, String s) {String htmlid = "";if (s.equals("A")) { /* robinhood1~7 */if (pc.getInventory().checkItem(40068)) { /* 檢查精靈薄餅 */pc.getInventory().consumeItem(40068, 1); /* 消耗精靈薄餅 */
		pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 1); /* 任務步驟設置為1 */
		htmlid = "robinhood4"; // 設置HTML ID為"robinhood4"
		} else {htmlid = "robinhood19"; // 如果條件不滿足，設置HTML ID為"robinhood19"
		}
		} else if (s.equals("B")) { /* robinhood8 */
final int[] item_ids = { 41346, 41348 }; // 定義獲得的物品ID
final int[] item_amounts = { 1, 1 }; // 定義獲得物品的數量
		for (int i = 0; i < item_ids.length; i++) {
		pc.getInventory().storeItem(item_ids[i], item_amounts[i]); // 存入獲得的物品
		pc.sendPackets(new S_SystemMessage("獲得了羅賓漢的便條和介紹信。")); // 發送消息通知玩家獲得物品
		}
		pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 2); /* 任務步驟設置為2 */
		htmlid = "robinhood13"; // 設置HTML ID為"robinhood13"
		}
		} else if (s.equals("C")) { /* robinhood9 */
		if (pc.getInventory().checkItem(41346) && pc.getInventory().checkItem(41351) && pc.getInventory().checkItem(41352, 4) && pc.getInventory().checkItem(40618, 30) && pc.getInventory().checkItem(40643, 30) && pc.getInventory().checkItem(40645, 30)
		&& pc.getInventory().checkItem(40651, 30) && pc.getInventory().checkItem(40676, 30)) {
		pc.getInventory().consumeItem(41346, 1); /* 消耗物品：便條 */
		pc.getInventory().consumeItem(41351, 1); /* 消耗物品：月光的精髓 */
		pc.getInventory().consumeItem(41352, 4); /* 消耗物品：某物品4個 */
		pc.getInventory().consumeItem(40651, 30); /* 消耗物品：某物品30個 */
		pc.getInventory().consumeItem(40643, 30); /* 消耗物品：火30個 */
		pc.getInventory().consumeItem(40645, 30); /* 消耗物品：水30個 */
		pc.getInventory().consumeItem(40618, 30); /* 消耗物品：風30個 */
		pc.getInventory().consumeItem(40676, 30); /* 消耗物品：大地30個 */
final int[] item_ids = { 41350, 41347 }; // 定義獲得的物品ID
final int[] item_amounts = { 1, 1 }; // 定義獲得物品的數量
		for (int i = 0; i < item_ids.length; i++) {
		pc.getInventory().storeItem(item_ids[i], item_amounts[i]); // 存入獲得的物品
		pc.sendPackets(new S_SystemMessage("獲得了羅賓漢的戒指和便條。")); // 發送消息通知玩家獲得物品
		}
		pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 7); /* 任務步驟設置為7 */
		htmlid = "robinhood10"; /* 提示玩家尋找其他材料 */
		} else {
		htmlid = "robinhood15"; /* 提示玩家帶來月光精髓和某物品 */
		}
		} else if (s.equals("E")) { /* robinhood11 */
		if (pc.getInventory().checkItem(41350) && pc.getInventory().checkItem(41347) && pc.getInventory().checkItem(40491, 30) && pc.getInventory().checkItem(40495, 40) && pc.getInventory().checkItem(100) && pc.getInventory().checkItem(40509, 12)
		&& pc.getInventory().checkItem(40052) && pc.getInventory().checkItem(40053) && pc.getInventory().checkItem(40054) && pc.getInventory().checkItem(40055)) {
		pc.getInventory().consumeItem(41350, 1); /* 消耗物品：戒指 */
		pc.getInventory().consumeItem(41347, 1); /* 消耗物品：便條 */
		pc.getInventory().consumeItem(40491, 30); /* 消耗物品：獅鷲獸羽毛30個 */
		pc.getInventory().consumeItem(40495, 40); /* 消耗物品：秘銀絲40個 */
		pc.getInventory().consumeItem(100, 1); /* 消耗物品：某物品1個 */
		pc.getInventory().consumeItem(40509, 12); /* 消耗物品：某物品12個 */
		pc.getInventory().consumeItem(40052, 1); /* 消耗物品：最高級寶石1 */
		pc.getInventory().consumeItem(40053, 1); /* 消耗物品：最高級寶石2 */
		pc.getInventory().consumeItem(40054, 1); /* 消耗物品：最高級寶石3 */
		pc.getInventory().consumeItem(40055, 1); /* 消耗物品：最高級寶石4 */
final int[] item_ids = { 205 }; // 定義獲得的物品ID
final int[] item_amounts = { 1 }; // 定義獲得物品的數量
		for (int i = 0; i < item_ids.length; i++) {
		pc.getInventory().storeItem(item_ids[i], item_amounts[i]); // 存入獲得的物品
		pc.sendPackets(new S_SystemMessage("獲得了月之長弓。")); // 發送消息通知玩家獲得物品
		}
		pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 0); /* 任務步驟重置 */
		htmlid = "robinhood12"; /* 完成 */
		} else {
		htmlid = "robinhood17"; /* 材料不足 */
		}
		}
		return htmlid; // 返回 htmlid

private String 馬爾巴(L1PcInstance pc, L1Object obj, String s) {
		String htmlid = "";
		L1NpcInstance npc = (L1NpcInstance) obj;
		if (pc.getInventory().checkItem(40665)) {
		htmlid = "marba17";
		if (s.equalsIgnoreCase("B")) {
		htmlid = "marba7";
		if (pc.getInventory().checkItem(214) && pc.getInventory().checkItem(20389) && pc.getInventory().checkItem(20393) && pc.getInventory().checkItem(20401) && pc.getInventory().checkItem(20406) && pc.getInventory().checkItem(20409)) {
		htmlid = "marba15";
		}
		}
		} else if (s.equalsIgnoreCase("A")) {
		if (pc.getInventory().checkItem(40637)) {
		htmlid = "marba20";
		} else {
		L1ItemInstance item = pc.getInventory().storeItem(40637, 1);
		String itemName = item.getItem().getName();
		String npcName = npc.getNpcTemplate().get_name();
		pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
		htmlid = "marba6";
		}
		}
		return htmlid;
		}

private String 卡納斯(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equals("a")) { // 移動到吉爾塔斯前方
		pc.start_teleport(32855, 32862, 537, pc.getHeading(), 18339, true, false);
		} else if (s.equals("b")) { // 移動到前哨基地
		pc.start_teleport(32804, 32864, 537, pc.getHeading(), 18339, true, false);
		} else if (s.equals("d")) { // 確認戰鬥區域（需要5000亞丁那）
		if (pc.getInventory().checkItem(40308, 5000)) {pc.getInventory().consumeItem(40308, 5000); // 消耗5000亞丁那
		pc.sendPackets(new S_SystemMessage("感謝您的亞丁那，但現在無法使用。"));
		htmlid = ""; // 不設置HTML ID
		} else {htmlid = "canus3"; // 設置HTML ID為"canus3"
		}
		}
		return htmlid;
		}
private String 毗濕耶耶(L1PcInstance pc, Object obj, String s) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		String npcName = npc.getNpcTemplate().get_name();
		String htmlid = "";
		int itemId = 0, enchant = 0, adena = 0;
		int[] sealitemId = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 30119, 30120 };

		if (s.equals("A")) { // +7 共鳴的奇靈克 [ +8 藍寶石奇靈克 ]
		itemId = 503;
		enchant = 8;
		adena = 5000000;
		} else if (s.equals("B")) { // +8 共鳴的奇靈克 [ +9 藍寶石奇靈克 ]
		itemId = 503;
		enchant = 9;
		adena = 10000000;
		} else if (s.equals("C")) { // +7 共鳴的奇靈克 [ +8 黑曜石奇靈克 ]
		itemId = 504;
		enchant = 8;
		adena = 5000000;
		} else if (s.equals("D")) { // +8 共鳴的奇靈克 [ +9 黑曜石奇靈克 ]
		itemId = 504;
		enchant = 9;
		adena = 10000000;
		}

		if (pc.getInventory().checkEnchant(itemId, enchant) && pc.getInventory().checkItem(40308, adena)) {
		pc.getInventory().DeleteEnchant(itemId, enchant);
		pc.getInventory().consumeItem(40308, adena);
		createNewItem(pc, npcName, sealitemId[enchant], 1, 0);
		htmlid = "vjaya05";
		} else {
		htmlid = "vjaya04";
		}
		return htmlid;
		}

// 피어스
private void PiersItem(L1PcInstance pc, int objid, String npcName, int checkItem, int checkEnchant, int adena) {
		if (pc.getInventory().checkEnchant(checkItem, checkEnchant) && pc.getInventory().checkItem(40308, adena)) {
		pc.getInventory().DeleteEnchant(checkItem, checkEnchant);
		pc.getInventory().consumeItem(40308, adena);
		createNewItem(pc, npcName, pc.PiersItemId[16], 1, pc.PiersItemId[17]);
		pc.sendPackets(new S_NPCTalkReturn(objid, ""));
		for (int i = 0; i < 20; i++) {
		pc.PiersItemId[i] = 0;
		pc.PiersEnchant[i] = 0;
		}
		} else {
		pc.sendPackets(new S_NPCTalkReturn(objid, "piers04"));
		}
		}

// 피어스 아이템 체크
private void PiersCheckItem(L1PcInstance pc, int objid, int enchant, int adena, int newItem, int newItemEnchant) {
		int listcount = 0;
		String[] list = new String[10];
		for (int i = 0; i < 10; i++) {
		list[i] = " ";
		}
		for (L1ItemInstance item : pc.getInventory().getItems()) {
		switch (item.getItemId()) {
		case 13:
		case 81:
		case 162:
		case 177:
		case 194:
		if (item.getEnchantLevel() == enchant && pc.getInventory().checkItem(40308, adena)) {
		list[listcount] = "+" + item.getEnchantLevel() + " " + item.getName();
		pc.PiersItemId[listcount] = item.getItemId();
		pc.PiersEnchant[listcount] = enchant;
		listcount++;
		}
		break;
default:
		break;
		}
		}
		if (listcount == 0 || !pc.getInventory().checkItem(40308, adena)) {
		pc.sendPackets(new S_NPCTalkReturn(objid, "piers04"));
		} else {
		pc.PiersItemId[16] = newItem;
		pc.PiersItemId[17] = newItemEnchant;
		pc.PiersItemId[18] = adena;
		pc.sendPackets(new S_NPCTalkReturn(objid, "piers00", list));
		}
		}

private boolean createNewItem(L1PcInstance pc, String npcName, int item_id, int count, int enchant) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
		item.setCount(count);
		item.setEnchantLevel(enchant);
		if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
		pc.getInventory().storeItem(item);
		} else {
		L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
		}
		pc.sendPackets(new S_ServerMessage(143, npcName, item.getLogName()));
		return true;
		} else {
		return false;
		}
		}

private void petbuy(GameClient client, int npcid, int paytype, int paycount) {
		L1PcInstance pc = client.getActiveChar();
		L1PcInventory inv = pc.getInventory();
		int charisma = pc.getAbility().getTotalCha();
		int petcost = 0;
		Object[] petlist = pc.getPetList().values().toArray();
		for (Object pet : petlist) {
		if (pet instanceof L1PetInstance) {
		petcost += ((L1NpcInstance) pet).getPetcost();
		}
		}
		if (pc.isCrown()) { // CROWN
		charisma += 6;
		} else if (pc.isElf()) { // ELF
		charisma += 12;
		} else if (pc.isWizard()) { // WIZ
		charisma += 6;
		} else if (pc.isDarkelf()) { // DE
		charisma += 6;
		} else if (pc.isDragonknight()) { // 용기사
		charisma += 6;
		} else if (pc.isBlackwizard()) { // 환술사
		charisma += 6;
		}
		charisma -= petcost;
		int petCount = charisma / 6;
		if (petCount <= 0) {
		pc.sendPackets(new S_ServerMessage(489)); // 물러가려고 하는 애완동물이 너무 많습니다.
		return;
		}
		if (pc.getInventory().checkItem(paytype, paycount)) {
		pc.getInventory().consumeItem(paytype, paycount);
		L1SpawnUtil.spawn(pc, npcid, 0, 0);
		L1MonsterInstance targetpet = null;
		L1ItemInstance petamu = null;
		L1PetType petType = null;
		for (L1Object object : L1World.getInstance().getVisibleObjects(pc, 3)) {
		if (object instanceof L1MonsterInstance) {
		targetpet = (L1MonsterInstance) object;
		petType = PetTypeTable.getInstance().get(targetpet.getNpcTemplate().get_npcId());
		if (petType == null || targetpet.isDead()) {
		return;
		}
		if (charisma >= 6 && inv.getSize() < Config.ServerAdSetting.Inventory_Count) {
		petamu = inv.storeItem(40314, 1); // 펫의 아뮤렛트
		if (petamu != null) {
		new L1PetInstance(targetpet, pc, petamu.getId());
		pc.sendPackets(new S_ItemName(petamu));
		}
		}
		}
		}
		}
		}

private String karmaLevelToHtmlId(int level) {
		if (level == 0 || level < -7 || 7 < level) {
		return "";
		}
		String htmlid = "";
		if (0 < level) {
		htmlid = "vbk" + level;
		} else if (level < 0) {
		htmlid = "vyk" + Math.abs(level);
		}
		return htmlid;
		}

private String enterUb(L1PcInstance pc, int npcId) {
		L1Colosseum ub = ColosseumTable.getInstance().getUbForNpcId(npcId);
		if (!Config.ServerAdSetting.COLOSSEUMOPEN || !ub.canPcEnter(pc)) {
		return "colos2";
		}
		if (ub.isNowUb()) {
		return "colos1";
		}
		if (ub.getMembersCount() >= ub.getMaxPlayer()) {
		return "colos4";
		}
		ub.addMember(pc);
		L1Location loc = ub.getLocation().randomLocation(10, false);
		pc.start_teleport(loc.getX(), loc.getY(), ub.getMapId(), pc.getHeading(), 18339, true);
		return "";
		}

private static final String[] summonstr_list = new String[] { "7", "263", "519", "8", "264", "520", "9", "265", "521", "10", "266", "522", "11", "267", "523", "12", "268", "524", "13", "269", "525", "14", "270", "526", "15", "271", "527", "16", "17",
		"18", "274" };

private static final int[] summonid_list = new int[] { 810820, 810821, 810822, // 28
		810823, 810824, 810825, // 32
		810826, 810827, 810828, // 36
		810829, 810830, 810831, // 40
		810832, 810833, 810834, // 44
		810835, 810836, 810837, // 48
		810839, 810838, 810840, // 52
		810841, 810842, 810843, // 56
		810844, 810845, 810846, // 60
		810847, // 64
		810848, // 68
		810850, 810849 // 72 //-- 이부분 임.

		}; // 소환몹 정하는 구문....

private static final int[] summonlvl_list = new int[] { 28, 28, 28, 32, 32, 32, 36, 36, 36, 40, 40, 40, 44, 44, 44, 48, 48, 48, 52, 52, 52, 56, 56, 56, 60, 60, 60, 64, 68, 72, 72 };// 술자 레벨제한
private static final int[] summoncha_list = new int[] { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 14, 36, 36, 50 };// 카리

private void summonMonster(L1PcInstance pc, String s) {
		int summonid = 0;
		int levelrange = 0;
		int summoncost = 0;

		// 소비구문
		for (int loop = 0; loop < summonstr_list.length; loop++) {
		if (s.equalsIgnoreCase(summonstr_list[loop])) {
		summonid = summonid_list[loop];
		levelrange = summonlvl_list[loop];
		summoncost = summoncha_list[loop];
		break;
		}
		}
		if (pc.getLevel() < levelrange) {
		pc.sendPackets(new S_ServerMessage(743));
		return;
		}

		int petcost = 0;
		Object[] petlist = pc.getPetList().values().toArray();
		for (Object pet : petlist) {
		petcost += ((L1NpcInstance) pet).getPetcost();
		}
		if ((summonid == 810850 || summonid == 810849 || summonid == 810848) && petcost != 0) {
		pc.sendPackets(new S_CloseList(pc.getId()));
		return;
		}
		int charisma = pc.getAbility().getTotalCha() + 6 - petcost;
		int summoncount = 0;
		if (levelrange <= 52) {
		summoncount = charisma / summoncost;
		} else if (levelrange == 56) {
		summoncount = charisma / (summoncost + 2);
		} else if (levelrange == 60) {
		summoncount = charisma / (summoncost + 4);
		} else if (levelrange == 64) {
		summoncount = charisma / (summoncost + 6);
		} else {
		summoncount = charisma / summoncost;
		}

		if (levelrange <= 52 && summoncount > 5) {
		summoncount = 5;
		} else if (levelrange == 56 && summoncount > 4) {
		summoncount = 4;
		} else if (levelrange == 60 && summoncount > 3) {
		summoncount = 3;
		} else if (levelrange == 64 && summoncount > 2) {
		summoncount = 2;
		}

		L1Npc npcTemp = NpcTable.getInstance().getTemplate(summonid);
		L1SummonInstance summon = null;
		for (int cnt = 0; cnt < summoncount; cnt++) {
		summon = new L1SummonInstance(npcTemp, pc);
		if (summonid == 810850 || summonid == 810849 || summonid == 810848) {
		summon.setPetcost(pc.getAbility().getTotalCha() + 7);
		} else {
		if (levelrange <= 52) {
		summon.setPetcost(summoncost);
		} else if (levelrange == 56) {
		summon.setPetcost(summoncost + 2);
		} else if (levelrange == 60) {
		summon.setPetcost(summoncost + 4);
		} else if (levelrange == 64) {
		summon.setPetcost(summoncost + 6);
		} else {
		summoncount = charisma / summoncost;
		}
		}
		}
		pc.sendPackets(new S_CloseList(pc.getId()));
		}

private void poly(GameClient clientthread, int polyId) {
		L1PcInstance pc = clientthread.getActiveChar();

		if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
		pc.getInventory().consumeItem(L1ItemId.ADENA, 100);

		L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_NPC, false, false);
		} else {
		pc.sendPackets(new S_ServerMessage(337, "$4"));
		}
		}

private void polyByKeplisha(GameClient clientthread, int polyId) {
		L1PcInstance pc = clientthread.getActiveChar();

		if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
		pc.getInventory().consumeItem(L1ItemId.ADENA, 100);

		L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_KEPLISHA, false, false);
		} else {
		pc.sendPackets(new S_ServerMessage(337, "$4"));
		}
		}

private String sellHouse(L1PcInstance pc, int objectId, int npcId) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan == null) {
		return "";
		}
		int houseId = clan.getHouseId();
		if (houseId == 0) {
		return "";
		}
		L1House house = HouseTable.getInstance().getHouseTable(houseId);
		int keeperId = house.getKeeperId();
		if (npcId != keeperId) {
		return "";
		}
		if (!pc.isCrown()) {
		pc.sendPackets(new S_ServerMessage(518));
		return "";
		}
		if (pc.getId() != clan.getLeaderId()) {
		pc.sendPackets(new S_ServerMessage(518));
		return "";
		}
		if (house.isOnSale()) {
		return "agonsale";
		}

		pc.sendPackets(new S_SellHouse(objectId, String.valueOf(houseId)));
		return null;
		}

private void openCloseDoor(L1PcInstance pc, L1NpcInstance npc, String s) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
		int houseId = clan.getHouseId();
		if (houseId != 0) {
		L1House house = HouseTable.getInstance().getHouseTable(houseId);
		int keeperId = house.getKeeperId();
		if (npc.getNpcTemplate().get_npcId() == keeperId) {
		L1DoorInstance door1 = null;
		L1DoorInstance door2 = null;
		L1DoorInstance door3 = null;
		L1DoorInstance door4 = null;
		ArrayList<L1DoorInstance> doors = DoorSpawnTable.getInstance().getDoorList();
		for (L1DoorInstance door : doors) {
		if (door.getKeeperId() == keeperId) {
		if (door1 == null) {
		door1 = door;
		continue;
		}
		if (door2 == null) {
		door2 = door;
		continue;
		}
		if (door3 == null) {
		door3 = door;
		continue;
		}
		if (door4 == null) {
		door4 = door;
		break;
		}
		}
		}
		doors = null;
		if (door1 != null) {
		if (s.equalsIgnoreCase("open")) {
		door1.open();
		} else if (s.equalsIgnoreCase("close")) {
		door1.close();
		}
		}
		if (door2 != null) {
		if (s.equalsIgnoreCase("open")) {
		door2.open();
		} else if (s.equalsIgnoreCase("close")) {
		door2.close();
		}
		}
		if (door3 != null) {
		if (s.equalsIgnoreCase("open")) {
		door3.open();
		} else if (s.equalsIgnoreCase("close")) {
		door3.close();
		}
		}
		if (door4 != null) {
		if (s.equalsIgnoreCase("open")) {
		door4.open();
		} else if (s.equalsIgnoreCase("close")) {
		door4.close();
		}
		}
		}
		}
		}
		}

		private void openCloseGate(L1PcInstance pc, int keeperId, boolean isOpen) {
		boolean isNowWar = false;
		int pcCastleId = 0;
		if (pc.getClanid() != 0) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
		pcCastleId = clan.getCastleId();
		}
		}
		if (pcCastleId == 0) { // KKK 혈맹이 없거나, 성혈이 아닌경우
		return;
		}
		if (keeperId == 70656 || keeperId == 70549 || keeperId == 70985) {
		if (isExistDefenseClan(L1CastleLocation.KENT_CASTLE_ID)) {
		if (pcCastleId != L1CastleLocation.KENT_CASTLE_ID) {
		return;
		}
		}
		isNowWar = MJCastleWarBusiness.getInstance().isNowWar(L1CastleLocation.KENT_CASTLE_ID);
		} else if (keeperId == 70600) { // OT
		if (isExistDefenseClan(L1CastleLocation.OT_CASTLE_ID)) {
		if (pcCastleId != L1CastleLocation.OT_CASTLE_ID) {
		return;
		}
		}
		isNowWar = MJCastleWarBusiness.getInstance().isNowWar(L1CastleLocation.OT_CASTLE_ID);
		} else if (keeperId == 70778 || keeperId == 70987 || keeperId == 70687) {
		if (isExistDefenseClan(L1CastleLocation.WW_CASTLE_ID)) {
		if (pcCastleId != L1CastleLocation.WW_CASTLE_ID) {
		return;
		}
		}
		isNowWar = MJCastleWarBusiness.getInstance().isNowWar(L1CastleLocation.WW_CASTLE_ID);
		} else if (keeperId == 70817 || keeperId == 70800 || keeperId == 70988 || keeperId == 70990 || keeperId == 70989 || keeperId == 70991) {
		if (isExistDefenseClan(L1CastleLocation.GIRAN_CASTLE_ID)) {
		if (pcCastleId != L1CastleLocation.GIRAN_CASTLE_ID) {
		return;
		}
		}
		isNowWar = MJCastleWarBusiness.getInstance().isNowWar(L1CastleLocation.GIRAN_CASTLE_ID);

		} else if (keeperId == 70863 || keeperId == 70992 || keeperId == 70862) {
		if (isExistDefenseClan(L1CastleLocation.HEINE_CASTLE_ID)) {
		if (pcCastleId != L1CastleLocation.HEINE_CASTLE_ID) {
		return;
		}
		}
		isNowWar = MJCastleWarBusiness.getInstance().isNowWar(L1CastleLocation.HEINE_CASTLE_ID);
		} else if (keeperId == 70995 || keeperId == 70994 || keeperId == 70993) {
		if (isExistDefenseClan(L1CastleLocation.DOWA_CASTLE_ID)) {
		if (pcCastleId != L1CastleLocation.DOWA_CASTLE_ID) {
		return;
		}
		}
		isNowWar = MJCastleWarBusiness.getInstance().isNowWar(L1CastleLocation.DOWA_CASTLE_ID);
		} else if (keeperId == 70996) {
		if (isExistDefenseClan(L1CastleLocation.ADEN_CASTLE_ID)) {
		if (pcCastleId != L1CastleLocation.ADEN_CASTLE_ID) {
		return;
		}
		}
		isNowWar = MJCastleWarBusiness.getInstance().isNowWar(L1CastleLocation.ADEN_CASTLE_ID);
		}
		ArrayList<L1DoorInstance> doors = DoorSpawnTable.getInstance().getDoorList();
		for (L1DoorInstance door : doors) {
		if (door.getKeeperId() == keeperId) {
		if (isNowWar && door.getMaxHp() > 1) {
		} else {
		if (isOpen) {
		door.open();
		} else {
		door.close();
		}
		}
		}
		}
		doors = null;
		}

		private void whaleRoom(L1PcInstance pc){
		L1SkillUse.off_icons(pc, L1SkillId.먹음직스러운복어);
		pc.removeSkillEffect(L1SkillId.먹음직스러운복어);
		int _mapnum;
		int rand = CommonUtil.random(100);
		if (rand >= 50){
		_mapnum = WhaleBossRoomSystem.getInstance().blankMapId();
		WhaleBossRoomSystem.getInstance().WhaleBossRoomStart(pc, _mapnum);
		pc.start_teleport(32808, 32807, _mapnum, pc.getHeading(), 18339, true); //보스방
		} else {
		_mapnum = WhaleTreasureRoomSystem.getInstance().blankMapId();
		WhaleTreasureRoomSystem.getInstance().WhaleTreasureRoomStart(pc, _mapnum);
		pc.start_teleport(32808, 32807, _mapnum, pc.getHeading(), 18339, true); //보물상자 방
		}
		}

		private boolean isExistDefenseClan(int castleId) {
		boolean isExistDefenseClan = false;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
		if (castleId == clan.getCastleId()) {
		isExistDefenseClan = true;
		break;
		}
		}
		return isExistDefenseClan;
		}

		private void expelOtherClan(L1PcInstance clanPc, int keeperId) {
		int houseId = 0;
		for (L1House house : HouseTable.getInstance().getHouseTableList()) {
		if (house.getKeeperId() == keeperId) {
		houseId = house.getHouseId();
		}
		}
		if (houseId == 0) {
		return;
		}

		int[] loc = new int[3];
		L1PcInstance pc = null;
		for (L1Object object : L1World.getInstance().getObject()) {
		if (object instanceof L1PcInstance) {
		pc = (L1PcInstance) object;
		if (L1HouseLocation.isInHouseLoc(houseId, pc.getX(), pc.getY(), pc.getMapId()) && clanPc.getClanid() != pc.getClanid()) {
		loc = L1HouseLocation.getHouseTeleportLoc(houseId, 0);
		if (pc != null) {
		pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 18339, true, false);
		}
		}
		}
		}
		}

		private void repairGate(L1PcInstance pc) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
		int castleId = clan.getCastleId();
		if (castleId != 0) {
		if (!MJCastleWarBusiness.getInstance().isNowWar(castleId)) {
		ArrayList<L1DoorInstance> doors = DoorSpawnTable.getInstance().getDoorList();
		for (L1DoorInstance door : doors) {
		if (L1CastleLocation.checkInWarArea(castleId, door)) {
		door.repairGate();
		}
		}
		doors = null;
		pc.sendPackets(new S_ServerMessage(990));
		} else {
		pc.sendPackets(new S_ServerMessage(991));
		}
		}
		}
		}

		private void payFee(L1PcInstance pc, L1NpcInstance npc) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
		int houseId = clan.getHouseId();
		if (houseId != 0) {
		L1House house = HouseTable.getInstance().getHouseTable(houseId);
		int keeperId = house.getKeeperId();
		if (npc.getNpcTemplate().get_npcId() == keeperId) {

		TimeZone tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
		Calendar checkCal = Calendar.getInstance(tz);
		checkCal.add(Calendar.DATE, 3);
		checkCal.set(Calendar.MINUTE, 0);
		checkCal.set(Calendar.SECOND, 0);

		if (house.getTaxDeadline().after(checkCal)) {
		pc.sendPackets(new S_SystemMessage("沒有更多的稅款需要支付。"));
		} else if (pc.getInventory().checkItem(L1ItemId.ADENA, 2000)) {
		pc.getInventory().consumeItem(L1ItemId.ADENA, 2000);
		Calendar cal = Calendar.getInstance(tz);
		cal.add(Calendar.DATE, Config.ServerAdSetting.HOUSETAXINTERVAL);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		house.setTaxDeadline(cal);
		HouseTable.getInstance().updateHouse(house);
		} else {
		pc.sendPackets(new S_ServerMessage(189));
		}
		}
		}
		}
		}

		private String[] makeHouseTaxStrings(L1PcInstance pc, L1NpcInstance npc) {
		String name = npc.getNpcTemplate().get_name();
		String[] result;
		result = new String[] { name, "2000", "1", "1", "00" };
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
		int houseId = clan.getHouseId();
		if (houseId != 0) {
		L1House house = HouseTable.getInstance().getHouseTable(houseId);
		int keeperId = house.getKeeperId();
		if (npc.getNpcTemplate().get_npcId() == keeperId) {
		Calendar cal = house.getTaxDeadline();
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		result = new String[] { name, "2000", String.valueOf(month), String.valueOf(day), String.valueOf(hour) };
		}
		}
		}
		return result;
		}

		private String[] makeWarTimeStrings(int castleId) {
		Calendar warTime = MJCastleWarBusiness.getInstance().get(castleId).nextCal();
		int year = warTime.get(Calendar.YEAR);
		int month = warTime.get(Calendar.MONTH) + 1;
		int day = warTime.get(Calendar.DATE);
		int hour = warTime.get(Calendar.HOUR_OF_DAY);
		int minute = warTime.get(Calendar.MINUTE);
		String[] result;
		if (castleId == L1CastleLocation.OT_CASTLE_ID) {
		result = new String[] { String.valueOf(year), String.valueOf(month), String.valueOf(day), String.valueOf(hour), String.valueOf(minute) };
		} else {
		result = new String[] { "", String.valueOf(year), String.valueOf(month), String.valueOf(day), String.valueOf(hour), String.valueOf(minute) };
		}
		return result;
		}

		private String[] makeUbInfoStrings(int npcId) {
		L1Colosseum ub = ColosseumTable.getInstance().getUbForNpcId(npcId);
		return ub.makeUbInfoStrings();
		}

		private String talkToDimensionDoor(L1PcInstance pc, L1NpcInstance npc, String s) {
		String htmlid = "";
		int protectionId = 0;
		int sealId = 0;
		int locX = 0;
		int locY = 0;
		short mapId = 0;
		if (npc.getNpcTemplate().get_npcId() == 80059) {
		protectionId = 40909;
		sealId = 40913;
		locX = 32773;
		locY = 32835;
		mapId = 607;
		} else if (npc.getNpcTemplate().get_npcId() == 80060) {
		protectionId = 40912;
		sealId = 40916;
		locX = 32757;
		locY = 32842;
		mapId = 606;
		} else if (npc.getNpcTemplate().get_npcId() == 80061) {
		protectionId = 40910;
		sealId = 40914;
		locX = 32830;
		locY = 32822;
		mapId = 604;
		} else if (npc.getNpcTemplate().get_npcId() == 80062) {
		protectionId = 40911;
		sealId = 40915;
		locX = 32835;
		locY = 32822;
		mapId = 605;
		}

		if (s.equalsIgnoreCase("a")) {
		// L1Teleport.teleport(pc, locX, locY, mapId, 5, true);
		pc.start_teleport(locX, locY, mapId, pc.getHeading(), 18339, true, false);
		htmlid = "";
		} else if (s.equalsIgnoreCase("b")) {
		L1ItemInstance item = pc.getInventory().storeItem(protectionId, 1);
		if (item != null) {
		pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
		}
		htmlid = "";
		} else if (s.equalsIgnoreCase("c")) {
		htmlid = "wpass07";
		} else if (s.equalsIgnoreCase("d")) {
		if (pc.getInventory().checkItem(sealId)) {
		L1ItemInstance item = pc.getInventory().findItemId(sealId);
		pc.getInventory().consumeItem(sealId, item.getCount());
		}
		} else if (s.equalsIgnoreCase("e")) {
		htmlid = "";
		} else if (s.equalsIgnoreCase("f")) {
		if (pc.getInventory().checkItem(protectionId)) {
		pc.getInventory().consumeItem(protectionId, 1);
		}
		if (pc.getInventory().checkItem(sealId)) {
		L1ItemInstance item = pc.getInventory().findItemId(sealId);
		pc.getInventory().consumeItem(sealId, item.getCount());
		}
		htmlid = "";
		}
		return htmlid;
		}

		private boolean isNpcSellOnly(L1NpcInstance npc) {
		int npcId = npc.getNpcTemplate().get_npcId();
		String npcName = npc.getNpcTemplate().get_name();
		if (npcId == 70027 || "伊甸商團".equals(npcName)) {
		return true;
		}
		return false;
		}

		private void getBloodCrystalByKarma(L1PcInstance pc, L1NpcInstance npc, String s) {
		L1ItemInstance item = null;

		if (s.equalsIgnoreCase("1")) {
		pc.addKarma((int) (500 * Config.ServerRates.RateKarma));
		pc.sendPackets(new S_Karma(pc));
		item = pc.getInventory().storeItem(40718, 1);
		if (item != null) {
		pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
		}
		pc.sendPackets(new S_ServerMessage(1081));
		} else if (s.equalsIgnoreCase("2")) {
		pc.addKarma((int) (5000 * Config.ServerRates.RateKarma));
		pc.sendPackets(new S_Karma(pc));
		item = pc.getInventory().storeItem(40718, 10);
		if (item != null) {
		pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
		}
		pc.sendPackets(new S_ServerMessage(1081));
		} else if (s.equalsIgnoreCase("3")) {
		pc.addKarma((int) (50000 * Config.ServerRates.RateKarma));
		pc.sendPackets(new S_Karma(pc));
		item = pc.getInventory().storeItem(40718, 100);
		if (item != null) {
		pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
		}
		pc.sendPackets(new S_ServerMessage(1081));
		}
		}

		private void getSoulCrystalByKarma(L1PcInstance pc, L1NpcInstance npc, String s) {
		L1ItemInstance item = null;

		if (s.equalsIgnoreCase("1")) {
		pc.addKarma((int) (-500 * Config.ServerRates.RateKarma));
		pc.sendPackets(new S_Karma(pc));
		item = pc.getInventory().storeItem(40678, 1);
		if (item != null) {
		pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
		}
		pc.sendPackets(new S_ServerMessage(1080));
		} else if (s.equalsIgnoreCase("2")) {
		pc.addKarma((int) (-5000 * Config.ServerRates.RateKarma));
		pc.sendPackets(new S_Karma(pc));
		item = pc.getInventory().storeItem(40678, 10);
		if (item != null) {
		pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
		}
		pc.sendPackets(new S_ServerMessage(1080));
		} else if (s.equalsIgnoreCase("3")) {
		pc.addKarma((int) (-50000 * Config.ServerRates.RateKarma));
		pc.sendPackets(new S_Karma(pc));
		item = pc.getInventory().storeItem(40678, 100);
		if (item != null) {
		pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
		}
		pc.sendPackets(new S_ServerMessage(1080));
		}
		}

		private void StatInitialize(L1PcInstance pc) {
		L1SkillUse l1skilluse = new L1SkillUse();
		l1skilluse.handleCommands(pc, L1SkillId.CANCELLATION, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_LOGIN);

		if (pc.getWeapon() != null) {
		pc.getInventory().setEquipped(pc.getWeapon(), false, false, false, false);
		}

		pc.sendPackets(new S_CharVisualUpdate(pc));
		pc.sendPackets(new S_OwnCharStatus2(pc));

		for (L1ItemInstance armor : pc.getInventory().getItems()) {
		if (armor != null && armor.isEquipped()) {
		pc.getInventory().setEquipped(armor, false, false, false, false);
		}
		}
		pc.setReturnStat(pc.get_exp());
		pc.sendPackets(new S_SPMR(pc));
		pc.sendPackets(new S_OwnCharAttrDef(pc));
		pc.sendPackets(new S_OwnCharStatus2(pc));
		pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
		try {
		pc.save();
		} catch (Exception e) {
		e.printStackTrace();
		}
		}

		/** 경험치 지급 */
		public void addQuestExp(L1PcInstance pc, double exp) {
		double bonusexp = (exp * Config.ServerRates.RateXp) / 100;
		long needExp = ExpTable.getNeedExpNextLevel(52);
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		long totalexp = (long) (needExp * bonusexp * exppenalty);
		pc.add_exp(totalexp);
		}

		private String[] 빈줌제작(String s, L1PcInstance pc) {
		String[] result = new String[6];
		int x = 0;
		int x1 = 0;
		int x2 = 0;
		int x3 = 0;
		int x4 = 0;
		int x5 = 0;
		if (s.equalsIgnoreCase("1")) {
		x = 1;
		pc._x = 1;
		} else if (s.equalsIgnoreCase("2")) {
		x = 5;
		pc._x = 5;
		} else if (s.equalsIgnoreCase("3")) {
		x = 10;
		pc._x = 10;
		} else if (s.equalsIgnoreCase("4")) {
		x = 100;
		pc._x = 100;
		} else if (s.equalsIgnoreCase("5")) {
		x = 500;
		pc._x = 500;
		} else {
		return result;
		}
		x1 = 50 * x;
		x2 = 100 * x;
		x3 = 100 * x;
		x4 = 200 * x;
		x5 = 200 * x;
		result[0] = String.valueOf(x1);
		result[1] = String.valueOf(x2);
		result[2] = String.valueOf(x3);
		result[3] = String.valueOf(x4);
		result[4] = String.valueOf(x5);
		result[5] = String.valueOf(x);
		return result;
		}

		private String 任務(String s, L1PcInstance pc, int objid) {
		String htmlid = null;
		if ((s.equalsIgnoreCase("1")) || (s.equalsIgnoreCase("2")) || (s.equalsIgnoreCase("3")) || (s.equalsIgnoreCase("4")) || (s.equalsIgnoreCase("5"))) {
		int i = 0;
		if (s.equalsIgnoreCase("1"))
		i = 1;
		else if (s.equalsIgnoreCase("2"))
		i = 5;
		else if (s.equalsIgnoreCase("3"))
		i = 10;
		else if (s.equalsIgnoreCase("4"))
		i = 100;
		else if (s.equalsIgnoreCase("5"))
		i = 500;
		String[] htmldata = new String[] { String.valueOf(50 * i), String.valueOf(100 * i), String.valueOf(100 * i), String.valueOf(200 * i), String.valueOf(200 * i), String.valueOf(i) };
		pc.sendPackets(new S_NPCTalkReturn(objid, "bs_m4", htmldata));
		return htmlid;
		}
		if (pc._x == 0) {
		L1Object obj = L1World.getInstance().findObject(objid);
		int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
		if (npcid == 5000) {
		if (pc.getLevel() > 79) {
		pc.sendPackets("只有79級以下的玩家才能使用。");
		return null;
		}
		if (s.equals("a")) { // 施放魔法
		if (pc.getInventory().checkItem(4100463, 1000)) {
		pc.getInventory().consumeItem(4100463, 1000);
		int[] allBuffSkill = { 26, 37, 42, 48 };
		pc.setBuffnoch(1);
		L1SkillUse l1skilluse = new L1SkillUse();
		for (int i = 0; i < allBuffSkill.length; i++) {
		l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
		}
		pc.setBuffnoch(0);
		htmlid = "";
		} else {
		pc.sendPackets("騎士團硬幣不足。");
		}
		}
		}
		}
		} else if (s.equals("a")) { // 마법을 받는다.
		if (pc.getInventory().checkItem(40308, 1000)) {
		pc.getInventory().consumeItem(40308, 1000);
		int[] allBuffSkill = { 26, 37, 42, 48 };
		pc.setBuffnoch(1);
		L1SkillUse l1skilluse = new L1SkillUse();
		for (int i = 0; i < allBuffSkill.length; i++) {
		l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
		}
		pc.setBuffnoch(0);
		htmlid = "";
		} else {
		pc.sendPackets(new S_ServerMessage(189));
		}
		}
		} else {
		if (s.equalsIgnoreCase("A")) {
		if ((pc.getInventory().checkItem(40090, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 50))) {
		pc.getInventory().consumeItem(40090, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 50);
		pc.getInventory().storeItem(40860, pc._x);
		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("B")) {
		if ((pc.getInventory().checkItem(40090, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 50))) {
		pc.getInventory().consumeItem(40090, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 50);
		pc.getInventory().storeItem(40861, pc._x);
		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("C")) {
		if ((pc.getInventory().checkItem(40090, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 50))) {
		pc.getInventory().consumeItem(40090, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 50);
		pc.getInventory().storeItem(40862, pc._x);
		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("D")) {
		if ((pc.getInventory().checkItem(40090, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 50))) {
		pc.getInventory().consumeItem(40090, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 50);
		pc.getInventory().storeItem(40866, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("E")) {
		if ((pc.getInventory().checkItem(40090, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 50))) {
		pc.getInventory().consumeItem(40090, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 50);
		pc.getInventory().storeItem(40859, pc._x);
		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}

		}

		if (s.equalsIgnoreCase("F")) {
		if ((pc.getInventory().checkItem(40091, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 100))) {
		pc.getInventory().consumeItem(40091, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 100);
		pc.getInventory().storeItem(40872, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("G")) {
		if ((pc.getInventory().checkItem(40091, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 100))) {
		pc.getInventory().consumeItem(40091, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 100);
		pc.getInventory().storeItem(40871, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("H")) {
		if ((pc.getInventory().checkItem(40091, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 100))) {
		pc.getInventory().consumeItem(40091, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 100);
		pc.getInventory().storeItem(40870, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("I")) {
		if ((pc.getInventory().checkItem(40091, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 100))) {
		pc.getInventory().consumeItem(40091, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 100);
		pc.getInventory().storeItem(40867, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("J")) {
		if ((pc.getInventory().checkItem(40091, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 100))) {
		pc.getInventory().consumeItem(40091, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 100);
		pc.getInventory().storeItem(40873, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}

		}

		if (s.equalsIgnoreCase("K")) {
		if ((pc.getInventory().checkItem(40092, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 100))) {
		pc.getInventory().consumeItem(40092, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 100);
		pc.getInventory().storeItem(40875, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("L")) {
		if ((pc.getInventory().checkItem(40092, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 100))) {
		pc.getInventory().consumeItem(40092, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 100);
		pc.getInventory().storeItem(40879, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("M")) {
		if ((pc.getInventory().checkItem(40092, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 100))) {
		pc.getInventory().consumeItem(40092, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 100);
		pc.getInventory().storeItem(40877, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("N")) {
		if ((pc.getInventory().checkItem(40092, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 100))) {
		pc.getInventory().consumeItem(40092, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 100);
		pc.getInventory().storeItem(40880, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("O")) {
		if ((pc.getInventory().checkItem(40092, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 100))) {
		pc.getInventory().consumeItem(40092, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 100);
		pc.getInventory().storeItem(40876, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}

		}

		if (s.equalsIgnoreCase("P")) {
		if ((pc.getInventory().checkItem(40093, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 200))) {
		pc.getInventory().consumeItem(40093, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 200);
		pc.getInventory().storeItem(40890, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("Q")) {
		if ((pc.getInventory().checkItem(40093, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 200))) {
		pc.getInventory().consumeItem(40093, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 200);
		pc.getInventory().storeItem(40883, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("R")) {
		if ((pc.getInventory().checkItem(40093, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 200))) {
		pc.getInventory().consumeItem(40093, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 200);
		pc.getInventory().storeItem(40884, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("S")) {
		if ((pc.getInventory().checkItem(40093, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 200)) && (pc.getInventory().checkItem(40318, pc._x))) {
		pc.getInventory().consumeItem(40318, pc._x);
		pc.getInventory().consumeItem(40093, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 200);
		pc.getInventory().storeItem(40889, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("T")) {
		if ((pc.getInventory().checkItem(40093, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 200)) && (pc.getInventory().checkItem(40318, pc._x))) {
		pc.getInventory().consumeItem(40093, pc._x);
		pc.getInventory().consumeItem(40318, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 200);
		pc.getInventory().storeItem(40887, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}

		if (s.equalsIgnoreCase("U")) {
		if ((pc.getInventory().checkItem(40094, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 200))) {
		pc.getInventory().consumeItem(40094, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 200);
		pc.getInventory().storeItem(40893, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("V")) {
		if ((pc.getInventory().checkItem(40094, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 200))) {
		pc.getInventory().consumeItem(40094, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 200);
		pc.getInventory().storeItem(40895, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("W")) {
		if ((pc.getInventory().checkItem(40094, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 200))) {
		pc.getInventory().consumeItem(40094, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 200);
		pc.getInventory().storeItem(40897, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("X")) {
		if ((pc.getInventory().checkItem(40094, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 200))) {
		pc.getInventory().consumeItem(40094, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 200);
		pc.getInventory().storeItem(40896, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		if (s.equalsIgnoreCase("Y")) {
		if ((pc.getInventory().checkItem(40094, pc._x)) && (pc.getInventory().checkItem(40308, pc._x * 200))) {
		pc.getInventory().consumeItem(40094, pc._x);
		pc.getInventory().consumeItem(40308, pc._x * 200);
		pc.getInventory().storeItem(40892, pc._x);

		htmlid = "bs_m1";
		} else {
		htmlid = "bs_m2";
		}
		}
		pc._x = 0;
		}
		return htmlid;
		}

		private String 嘿商店(String s, L1PcInstance pc, int objid) {
		String htmlid = null;
		if (pc._x == 0) {
		L1Object obj = L1World.getInstance().findObject(objid);
		int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
		if (npcid == 120834 && s.equals("A")) { // 施放魔法
		if (pc.getInventory().checkItem(40308, 220)) {
		pc.getInventory().consumeItem(40308, 220);
		int[] allBuffSkill = { 26, 37, 42, 48 };
		pc.setBuffnoch(1);
		L1SkillUse l1skilluse = new L1SkillUse();
		for (int i = 0; i < allBuffSkill.length; i++) {
		l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
		}
		pc.setBuffnoch(0);
		Collection<L1MerchantInstance> npc = L1World.getInstance().getAllHastActionobjet();
		for (L1MerchantInstance actionnpc : npc) {
		if (actionnpc == null) continue;

		Broadcaster.broadcastPacket(actionnpc, new S_DoActionGFX(actionnpc.getId(), 19));
		}
		htmlid = "";
		} else {
		pc.sendPackets(new S_ServerMessage(189));
		}
		}
		}
		return htmlid;
		}
		} else {
		pc._x = 0;
		}
		return htmlid;
		}

		@Override
		public String getType() {
		return C_NPC_ACTION;
		}
		}
