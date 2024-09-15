package l1j.server.server.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import MJShiftObject.MJEShiftObjectType;
import MJShiftObject.MJShiftObjectHelper;

import MJShiftObject.Battle.MJShiftBattlePlayManager;
import MJShiftObject.Template.CommonServerInfo;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.IndunSystem.MiniGame.L1Gambling;
import l1j.server.IndunSystem.MiniGame.L1Gambling3;
import l1j.server.MJCaptchaSystem.Loader.MJCaptchaLoadManager;
import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
import l1j.server.MJDShopSystem.MJDShopItem;
import l1j.server.MJDShopSystem.MJDShopStorage;
import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
import l1j.server.MJSurveySystem.MJSurveyFactory;
import l1j.server.MJSurveySystem.MJSurveySystemLoader;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Chain.Chat.MJNormalChatFilterChain;
import l1j.server.MJTemplate.Chain.Chat.MJWhisperChatFilterChain;
import l1j.server.MJTemplate.Chain.Chat.MJWorldChatFilterChain;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand.SC_MSG_ANNOUNCE;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWebServer.Dispatcher.my.service.chat.MJMyChatService;
import l1j.server.server.Account;
import l1j.server.server.ActionCodes;
import l1j.server.server.GMCommands;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.UserCommands;
import l1j.server.server.server.Controller.EventThread;
import l1j.server.server.server.Controller.FishingTimeController;
import l1j.server.server.Controller.LoginController;
import l1j.server.server.server.datatables.BuddyTable;
import l1j.server.server.server.datatables.EventTimeTable;
import l1j.server.server.server.datatables.ItemTable;
import l1j.server.server.server.datatables.MapsTable;
import l1j.server.server.server.datatables.NpcActionTable;
import l1j.server.server.server.datatables.SpamTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Buddy;
import l1j.server.server.model.L1ChatParty;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.action.L1NpcAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.server.monitor.Logger.LoggerChatType;
import l1j.server.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_ChangeCharName;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_NewChat;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Ping;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SocialAction;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TamWindow;
import l1j.server.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.MJCommons;
import l1j.server.server.utils.SQLUtil;

import static jdk.internal.org.jline.utils.Colors.s;

public class C_ActionUi extends l1j.server.server.clientpackets.ClientBasePacket {

	Random _Random = new Random(System.nanoTime());

	private static final String C_ACTION_UI = "[C] C_ActionUi";
	//private static final int CHAT = 0x0202;
	private static final int CS_PING_ACK = 0x03E9;
	private static final int ACCOUNT_TAM = 0x01cc;// 탐창
	private static final int ACCOUNT_TAM_CANCEL = 0x01e0;// 탐 취소
	private static final int ACCOUNT_TAM_UPDATE = 0x013d;// 탐
	private static final int ACTION = 0x013F;
	private static final int SUSPICIOUS_SKY_GARDEN = 0x84;
	private static final int JOIN_WAITING = 0x44;
	private static final int SIEGE_RELATED = 0x45; // 宣布
	private static final int MARK_SETTING = 0x0152;
	private static final int EVENT_TELEPORT = 143;
	private static final int NEWSHOP = 0x0331;
	private static final int BLESSED_TELEPORT = 0x033D;
	private static final int ENVIRONMENT_SETTING = 1002;

	public C_ActionUi(byte abyte0[], GameClient client) {
		super(abyte0);
		try {
			L1Character pc = client.getActiveChar();
			int type = readH();
			if (pc == null) {
				return;
			} else if (pc.isGhost())
				return;

			//System.out.println("C_ActionUi 類型: " + type);

			switch (type) {
			case ENVIRONMENT_SETTING:
				break;
			case CS_PING_ACK:
				if (pc.isGm())
					S_Ping.reqForGM(pc);
				break;
			case NEWSHOP: {
				try {
					if (pc == null || pc.isGhost()) {
						return;
					}
					if (pc.isInvisble()) {
						pc.sendPackets(String.valueOf(new S_ServerMessage(755)));
						return;
					}
					if (pc.getMapId() != 800) {
						pc.sendPackets(String.valueOf(new S_SystemMessage("個人商店只能在市場開啟。")));
						return;
					}

					if (pc.getMapId() != 800) {
						if (pc.isFishing()) {
							try {
								pc.setFishing(false);
								pc.setFishingTime(0);
								pc.setFishingReady(false);
								pc.sendPackets(new S_CharVisualUpdate(pc));
								Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
								FishingTimeController.getInstance().removeMember(pc);
								pc.sendPackets(String.valueOf(new S_ServerMessage(2120)));
								return;
							} catch (Exception e) {
							}
						} else {
							pc.sendPackets(String.valueOf(new S_ServerMessage(3405)));
							return;
						}
					}

					if (pc.getInventory().checkEquipped(22232) || pc.getInventory().checkEquipped(22234) || pc.getInventory().checkEquipped(22233) || pc.getInventory().checkEquipped(22235)
							|| pc.getInventory().checkEquipped(22236) || pc.getInventory().checkEquipped(22237) || pc.getInventory().checkEquipped(22238) || pc.getInventory().checkEquipped(22239)
							|| pc.getInventory().checkEquipped(22240) || pc.getInventory().checkEquipped(22241) || pc.getInventory().checkEquipped(22242) || pc.getInventory().checkEquipped(22243)
							|| pc.getInventory().checkEquipped(22244) || pc.getInventory().checkEquipped(22245) || pc.getInventory().checkEquipped(22246) || pc.getInventory().checkEquipped(22247)
							|| pc.getInventory().checkEquipped(22248) || pc.getInventory().checkEquipped(22249)) { // 符文
						pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "如果您已裝備符文，請解除。")));
						return;
					}

					L1ItemInstance checkItem;
					L1Item buyitem;
					boolean tradable = true;

					int length = readH();
					readC();
					int shoptype = readC();
					if (shoptype == 0) {
						if (pc.getCurrentSpriteId() != pc.getClassId()){
							pc.sendPackets(String.valueOf(new S_SystemMessage("變身將被解除。")));
							L1PolyMorph.undoPoly(pc);
						}
						if (pc.hasSkillEffect(L1SkillId.SHAPE_CHANGE)){
							pc.removeSkillEffect(L1SkillId.SHAPE_CHANGE);
						} else if (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER)){
							pc.removeSkillEffect(L1SkillId.POLY_RING_MASTER);
						} else if (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER2)){
							pc.removeSkillEffect(L1SkillId.POLY_RING_MASTER2);
						} 
//						else{
//							L1PolyMorph.undoPoly(pc);
//						}
						for (L1PcInstance target : L1World.getInstance().getAllPlayers3()) {
							if (target.getId() != pc.getId() && target.getAccountName().toLowerCase().equals(pc.getAccountName().toLowerCase()) && target.isPrivateShop()) {
								pc.sendPackets("\f3您的輔助角色已處於無人商店狀態。");
								pc.sendPackets("\f3請結束商店。 /商店");
								pc.setPrivateShop(false);
								pc.shopTransformation = 0;
								pc.sendPackets(String.valueOf(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle)));
								pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
								pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
								// pc.broadcastPacket(S_WorldPutObject.get(pc));
								return;
							}
						}

						int ObjectId = 0;
						int descid = 0;
						int Price = 0;
						int Count = 0;
						int enc = 0;
						int attrtype = 0;
						int attrlevel = 0;
						int bless = 0;
						int searching = 0;
						Object[] petlist = null;
						for (int i = 0; i < length; i++) {
							int code = readC();
							if (code == 0x12) {
								readP(1); // 長度。
								for (int i2 = 0; i2 < 3; i2++) {
									int code2 = readC();
									if (code2 == 0x08) {
										ObjectId = readBit();
									} else if (code2 == 0x10) {
										Price = readBit();
									} else if (code2 == 0x18) {
										Count = readBit();
									}
								}
								if (ObjectId == -1) {
									continue;
								}
								checkItem = pc.getInventory().getItem(ObjectId);
								if (checkItem == null || ObjectId != checkItem.getId()) {
									pc.sendPackets(new S_Disconnect());
									return;
								}
								if (!checkItem.isStackable() && Count != 1) {
									pc.sendPackets(new S_Disconnect());
									return;
								}

								if (/* checkItem.getCount() < Count || */ checkItem.getCount() <= 0 || Count <= 0) {
									/** 2016.11.24 MJ App Center 市價 **/
									pc.disposeShopInfo();
									/** 2016.11.24 MJ App Center 市價 **/
									return;
								}
								if (checkItem.getBless() >= 128) {
									pc.sendPackets(String.valueOf(new S_ServerMessage(210, checkItem.getItem().getName()))); // 1%0是
									/** 2016.11.24 MJ App Center 市價 **/
									pc.disposeShopInfo();
									/** 2016.11.24 MJ App Center 市價 **/
									return;
								}
								if (!checkItem.getItem().isTradable()) {
									tradable = false;
									pc.sendPackets(String.valueOf(new S_ServerMessage(166, checkItem.getItem().getName(), "無法交易。")));
									break;
								}

								if (checkItem.get_Carving() != 0) {
									tradable = false;
									pc.sendPackets(new S_SystemMessage("刻印的物品無法交易。"), true);
								}

								if (!MJCompanionInstanceCache.is_companion_oblivion(checkItem.getId())) {
									tradable = false;
									pc.sendPackets(String.valueOf(new S_ServerMessage(166, checkItem.getItem().getName(), "無法交易。")));
								}

								petlist = pc.getPetList().values().toArray();
								for (Object petObject : petlist) {
									if (petObject instanceof L1PetInstance) {
										L1PetInstance pet = (L1PetInstance) petObject;
										if (checkItem.getId() == pet.getItemObjId()) {
											tradable = false;
											pc.sendPackets(String.valueOf(new S_ServerMessage(166, checkItem.getItem().getName(), "無法交易。")));
											break;
										}
									}
								}

								L1DollInstance doll = pc.getMagicDoll();
								if (doll != null) {
									if (checkItem.getId() == doll.getItemObjId()) {
										tradable = false;
										pc.sendPackets(String.valueOf(new S_ServerMessage(166, checkItem.getItem().getName(), "無法交易。")));
										break;
									}
								}
								pc.addSellings(MJDShopItem.create(checkItem, Count, checkItem.getEnchantLevel(), checkItem.getAttrEnchantLevel(), checkItem.getBless(), Price, false));
							} else if(code == 0x1a) {
								readC();
								for (int i2 = 0; i2 < 4; i2++) {
									int code2 = readC();
									if (code2 == 0x08) {
										descid = readBit();
									} else if (code2 == 0x10) {
										Price = readBit();
									} else if (code2 == 0x18) {
										Count = readBit();
									} else if(code2 == 0x22) {
										readC();
										for (int i3 = 0; i3 < 5; i3++) {
											int code3 = readC();
											if (code3 == 0x08) {
												enc = readBit();
											} else if (code3 == 0x10) {
												attrtype = readBit();
											} else if (code3 == 0x18) {
												attrlevel = readBit();
											} else if (code3 == 0x20) {
												bless = readBit();
											} else if (code3 == 0x28) {
												searching = readBit();
											}
										}
									}
								}
								if (descid == -1) {
									continue;
								}
								buyitem = ItemTable.getInstance().findStoreCachedItem(descid, bless);
								L1ItemInstance buyitem2 = ItemTable.getInstance().createItem(buyitem.getItemId());
								if(buyitem2 == null) {
									continue;
								}
								if (buyitem2.getCount() <= 0 || Count <= 0) {
									pc.disposeShopInfo();
									return;
								}
								if (buyitem2.getBless() >= 128) {
									pc.sendPackets(String.valueOf(new S_ServerMessage(210, buyitem2.getItem().getName()))); // 1%0是
									pc.disposeShopInfo();
									return;
								}
								if (!buyitem2.getItem().isTradable()) {
									tradable = false;
									pc.sendPackets(String.valueOf(new S_ServerMessage(166, buyitem2.getItem().getName(), "無法交易。")));
									break;
								}

								if (buyitem2.get_Carving() != 0) {
									tradable = false;
									pc.sendPackets(new S_SystemMessage("刻印的物品無法交易。"), true);
								}

								if (!MJCompanionInstanceCache.is_companion_oblivion(buyitem2.getId())) {
									tradable = false;
									pc.sendPackets(String.valueOf(new S_ServerMessage(166, buyitem2.getItem().getName(), "無法交易。")));
								}

								petlist = pc.getPetList().values().toArray();
								for (Object petObject : petlist) {
									if (petObject instanceof L1PetInstance) {
										L1PetInstance pet = (L1PetInstance) petObject;
										if (buyitem2.getId() == pet.getItemObjId()) {
											tradable = false;
											pc.sendPackets(String.valueOf(new S_ServerMessage(166, buyitem2.getItem().getName(), "無法交易。")));
											break;
										}
									}
								}
//
								L1DollInstance doll = pc.getMagicDoll();
								if (doll != null) {
									if (buyitem2.getId() == doll.getItemObjId()) {
										tradable = false;
										pc.sendPackets(String.valueOf(new S_ServerMessage(166, buyitem2.getItem().getName(), "無法交易。")));
										break;
									}
								}
								pc.addPurchasings(MJDShopItem.create(buyitem2, Count, enc, getAttrName(attrtype, attrlevel), bless, Price, true));
							} else {
								break;
							}
						}
						if (!tradable) { // 包含無法交易的物品時，關閉個人商店
							pc.setPrivateShop(false);
							pc.sendPackets(String.valueOf(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle)));
							pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
							/** 2016.11.24 MJ 應用中心 市價 **/
							pc.disposeShopInfo();
							/** 2016.11.24 MJ 應用中心 市價 **/
							return;
						}
						int l1 = readC();
						byte[] chat = readByteL(l1);

						readC();
						int l2 = readC();
						String polynum = readS(l2);

						// String test =null;
						int poly = 0;

						pc.getNetConnection().getAccount().updateShopOpenCount();
						pc.sendPackets(new S_PacketBox(S_PacketBox.SHOP_OPEN_COUNT, pc.getNetConnection().getAccount().Shop_open_count), true);

						pc.setShopChat(chat);
						pc.setPrivateShop(true);
						pc.sendPackets(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, chat));
						pc.broadcastPacket(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, chat));
						// pc.sendPackets("村莊無人商店已啟用。");

						if (polynum.equalsIgnoreCase("tradezone1"))
							poly = 11479;
						else if (polynum.equalsIgnoreCase("tradezone2"))
							poly = 11483;
						else if (polynum.equalsIgnoreCase("tradezone3"))
							poly = 11480;
						else if (polynum.equalsIgnoreCase("tradezone4"))
							poly = 11485;
						else if (polynum.equalsIgnoreCase("tradezone5"))
							poly = 11482;
						else if (polynum.equalsIgnoreCase("tradezone6"))
							poly = 11486;
						else if (polynum.equalsIgnoreCase("tradezone7"))
							poly = 11481;
						else if (polynum.equalsIgnoreCase("tradezone8")) {
							poly = 11484;
						}
						pc.shopTransformation = poly;
						pc.setCurrentSprite(poly);
						pc.sendPackets(new S_ChangeShape(pc.getId(), poly, 70).toString());
						Broadcaster.broadcastPacket(pc, new S_ChangeShape(pc.getId(), poly, 70));
						pc.sendPackets(new S_CharVisualUpdate(pc));
						Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
						pc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(pc));
						// pc.broadcastPacket(S_WorldPutObject.get(pc));
						// Broadcaster.broadcastPacket(pc, new
						// S_OtherCharPacks(pc));
						pc.curePoison();
						/** 2016.11.24 MJ 應用中心 市價 **/
						GeneralThreadPool.getInstance().execute(new MJDShopStorage(pc, false));
						/** 2016.11.24 MJ 應用中心 市價 **/
					} else if (shoptype == 1) {
						if (pc.isPrivateReady() && pc.isPrivateShop())
							break;

						pc.setPrivateShop(false);
						pc.shopTransformation = 0;
						pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
						pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
						L1PolyMorph.undoPolyPrivateShop(pc);
						/** 2016.11.24 MJ 應用中心 市價 **/
						GeneralThreadPool.getInstance().execute(new MJDShopStorage(pc, true));
						/** 2016.11.24 MJ 應用中心 市價 **/
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
			case EVENT_TELEPORT: {// 活動通知
				readH();
				readC();
				int stat = readC();
				
				Iterator<L1NpcInstance> iter = EventTimeTable.getInstance().get_npc_iter();
				L1NpcInstance npc = null;
				while (iter.hasNext()) {
					npc = iter.next();
					if (npc == null) {
						continue;
					}
					if (pc.getMapId() == 5166 || pc.getMapId() == 5167 || pc.getMapId() == 666) {
						continue;
					}
					if (!npc.is_boss_alarm())
						continue;

					if (npc.get_boss_type() != stat)
						continue;

					if (!npc.is_boss_tel()) {
						continue;
					}

					/** 活動通知 用戶點擊TEL時 **/
					if (pc.getLevel() < Config.ServerAdSetting.YNpclevel) {
						pc.sendPackets(new S_ServerMessage(1287));
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
						return;
					}
					if (pc.isFishing()) {
						pc.sendPackets(new S_ServerMessage(4725));
						// pc.sendPackets(new S_Paralysis(7, false));
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
						return;
					}
					if (!pc.getMap().isTeleportable()) {
						pc.sendPackets(1413);
						// pc.sendPackets(new S_Paralysis(7, false));
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
						return;
					}
					if (pc.getMapId() == 2237 || pc.getMapId() == 666 || pc.getMapId() >= 1708 && pc.getMapId() <= 1712 || pc.getMapId() >= 12852 && pc.getMapId() <= 12862
							|| pc.getMapId() >= 15871 && pc.getMapId() <= 15899) {
						pc.sendPackets(1413);
						// pc.sendPackets(new S_Paralysis(7, false));
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
						return;
					}

					if (MJShiftBattlePlayManager.is_shift_battle(pc))
						return;

					int x = npc.get_boss_tel_x(), y = npc.get_boss_tel_y(), map = npc.get_boss_tel_m(), rnd = npc.get_boss_tel_rnd();
//					System.out.println(npc.getNpcId());
					
/*					if (npc.getNpcId() == 120717){
						System.out.println(EventThread.getCrackIntheTower());
						if (EventThread.getCrackIntheTower() !=0){
							switch (EventThread.getCrackIntheTower()){
								case 101:
									x = 32735;
									y = 32798;
									map = 101;
									break;
								case 102:
									x = 32727;
									y = 32803;
									map = 102;
									break;
								case 103:
									x = 32726;
									y = 32803;
									map = 103;
									break;
								case 104:
									x = 32620;
									y = 32859;
									map = 104;
									break;
								case 105:
									x = 32601;
									y = 32866;
									map = 105;
									break;
								case 106:
									x = 32611;
									y = 32863;
									map = 106;
									break;
								case 107:
									x = 32618;
									y = 32866;
									map = 107;
									break;
								case 108:
									x = 32602;
									y = 32867;
									map = 108;
									break;
								case 109:
									x = 32613;
									y = 32866;
									map = 109;
									break;
								case 110:
									x = 32730;
									y = 32803;
									map = 110;
									break;
								default:
									break;
							}
						}
					}*/
					
					if (x == 0 && y == 0 && map == 0) {
						pc.sendPackets("服務因管理員而已暫停。");
						
						return;
					}

					if (pc.getInventory().checkItem(40308, npc.get_boss_tel_count())) {
						pc.getInventory().consumeItem(40308, npc.get_boss_tel_count());
					} else {
						pc.sendPackets(new S_SystemMessage("金幣 " + npc.get_boss_tel_count() + "元不足。"));
						return;
					}
					MJPoint pt = MJPoint.newInstance(x, y, rnd, (short) map, 50);
					pc.set_MassTel(true);
					pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true, true);
				}
			}
				break;
				case SignUpPending: {
				pc.sendPackets(new S_ACTION_UI2(S_ACTION_UI2.CLAN_JOIN_WAIT, true));
			}
				break;
			case BLESSED_TELEPORT: {
				readH();
				readC();
				int chatlen = readBit();
				byte[] name_byte = readByte(chatlen); // set_auth_provider
				String code = new String(name_byte, "UTF-8");

				// TODO 找到傳送代碼時取消註解
				// System.out.println(code);
				for (L1NpcInstance tel_map_npc : L1World.getInstance().getAllNpc()) {
					if (tel_map_npc.getNpcId() == 9000) {
						L1Object npc = L1World.getInstance().findObject(String.valueOf(tel_map_npc.getId()));
						L1NpcAction action = NpcActionTable.getInstance().get(code, pc, npc);
						if (action == null) {
							System.out.println(String.format("(C_ActionUI::BLESSED_TELEPORT) 收到未知的動作。%s", code));
							pc.sendPackets(4729);
							return;
						}
						if (pc.getInventory().checkItem(140100)) {
							pc.getInventory().consumeItem(140100, 1);
							action.execute(code, pc, npc, null);
						}
					}
				}
			}
				break;
				case SuspiciousSkyGarden:
				if (MJShiftBattlePlayManager.is_shift_battle(pc))
					return;

//				if (!pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
				if (!pc.isPcBuff()) {
					pc.sendPackets(new S_SystemMessage("此操作僅在使用網咖使用券時可用。"));
					return;
				}

				if (!MapsTable.getInstance().isPCTEL(pc.getMapId())) {
					pc.sendPackets(4729);
					return;
				}
				if (!pc.getMap().isTeleportable()) {
					if (!pc.getMap().isRuler()) {
						return;
					} else {
						if (pc.getInventory().checkItem(900111));
					}
				}
				
				int ran = _Random.nextInt(4);

				if (ran == 0) {
					pc.start_teleport(32779, 32825, 622, pc.getHeading(), 18339, true, false);
				} else if (ran == 1) {
					pc.start_teleport(32761, 32819, 622, pc.getHeading(), 18339, true, false);
				} else if (ran == 2) {
					pc.start_teleport(32756, 32837, 622, pc.getHeading(), 18339, true, false);
				} else {
					pc.start_teleport(32770, 32839, 622, pc.getHeading(), 18339, true, false);
				}
				break;
			/*
			* case 0x0146: // 設定接受血盟加入申請 if (pc.getClanid() == 0 || (!pc.isCrown() && pc.getClanRank() != L1Clan.Guardian)) return;
			*
			* if (MJShiftBattlePlayManager.isShiftBattle(pc)) return;
			*
			* readC(); readH(); int setting = readC(); readC(); int setting2 = readC(); if (setting2 == 2) { readC(); int size = readC(); StringBuilder sb = new StringBuilder(size * 3); for (int i = 0; i < size; i++) sb.append(String.format("%02X", readC()));
			* pc.getClan().setJoinPassword(sb.toString()); ClanTable.getInstance().updateClanPassword(pc.getClan()); }
			*
			* pc.getClan().setJoinSetting(setting); pc.getClan().setJoinType(setting2); pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_SETTING, setting, setting2), true); ClanTable.getInstance().updateClan(pc.getClan()); pc.sendPackets(new
			[22:42]
			* S_ServerMessage(3980), true); break; case 0x014C: // 血盟招募設定 if (pc.getClanid() == 0) return;
			*
			* if (MJShiftBattlePlayManager.isShiftBattle(pc)) return;
			*
			* pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_SETTING, pc.getClan().getJoinSetting(), pc.getClan().getJoinType()), true); break;
			*/
			/*
			 * case 322: // 血盟加入 { if (MJShiftBattlePlayManager.isShiftBattle(pc)) return;
			 * @SuppressWarnings("unused") int joinType = readC(); readH(); int length = readC(); if (pc.isCrown()) { pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 4), true); break; }
			 * // 已經處於血盟加入狀態。 if (pc.getClanid() != 0) { L1Clan clan = pc.getClan(); if (clan.getLeaderId() != pc.getId()) { pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 9), true); break; } else { if (clan.getCastleId() > 0) {
			 * pc.sendPackets(new S_ServerMessage(ServerMessage.HAVING_NEST_OF_CLAN)); break; }
			 * if (clan.getCurrentWar() != null) { pc.sendPackets(new S_ServerMessage(ServerMessage.CANNOT_BREAK_CLAN)); break; }
			 * if (clan.getAlliance() > 0) { pc.sendPackets(new S_ServerMessage(ServerMessage.CANNOT_BREAK_CLAN_HAVING_FRIENDS)); break; } } }
			 * // 請與君主會面並加入。 try { String clanname = new String(readByteL(length), 0, length, "MS949"); L1Clan clan = L1World.getInstance().findClan(clanname); // if (clan == null) { pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 13),
			 * true); break; } L1PcInstance crown = clan.getOnlineManager(); switch (clan.getJoinType()) { case 1: if (crown == null) { pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 11), true); return; }
			 * crown.setTempID(pc.getId()); // 保存對方的對象ID S_Message_YN myn = new S_Message_YN(97, pc.getName()); crown.sendPackets(myn, true); pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 1), true); return; case 2: readD(); readC();
			 * int size = readC(); StringBuilder sb = new StringBuilder(); for (int i = 0; i < size; i++) sb.append(String.format("%02X", readC())); if (clan.getJoinPassword() == null || !clan.getJoinPassword().equalsIgnoreCase(sb.toString())) { pc.sendPackets(new
			 * S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 3), true); return; } case 0: if (L1ClanJoin.getInstance().ClanJoin(clan, pc)) pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 0), true); else pc.sendPackets(new
			 * S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 1), true); break; default: pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 11), true); break; } } catch (Exception e) { } break; }
			 */
				/** 2016.11.25 MJ App Center 血盟 **/
				case MarkSetting: // 設定隊伍標記
				int size = readH();
				byte[] flag = new byte[size];
				for (int i = 0; i < size; ++i)
					flag[i] = (byte) readC();
				//
				L1Party party = pc.getParty();
				if (party == null)
					return;
				//
				for (L1PcInstance member : party.getMembers())
					member.sendPackets(new S_ACTION_UI(flag));
				break;
			case ACCOUNT_TAM_UPDATE:// 彈出搜索窗口
				// pc.sendPackets(new S_TamWindow(pc.getAccountName()));
				break;
			case ACCOUNT_TAM:// 彈出搜尋窗口
				pc.sendPackets(new S_TamWindow(pc.getAccountName()));
				break;
			case ACCOUNT_TAM_CANCEL:// 搜索
				int len = readC() - 1;
				readH();
				StringBuffer sb = new StringBuffer(len * 2);
				for (int i = 0; i < len; i++) {
					sb.append(String.valueOf((byte) readC()));
				}
				/*
				 * byte[] BYTE = readByte(len); byte[] temp = new byte[BYTE.length - 1]; for (int i = 0; i < temp.length; i++) { temp[i] = BYTE[i]; }
				 * 
				 * for (byte zzz : temp) { sb.append(String.valueOf(zzz)); }
				 */

				int day = Nexttam(sb.toString());
				int charobjid = TamCharid(sb.toString());
				if (charobjid != pc.getId()) {
					pc.sendPackets(new S_SystemMessage("只有該角色可以取消。"));
					return;
				}
				int itemid = 0;
				if (day != 0) {
					if (day == 3) {// 期限 3日
						itemid = 600226;
					} else if (day == 7) {// 期限 7日
						itemid = 3000235;
					} else if (day == 30) {// 期限 30日
						itemid = 600227;
					}
					L1ItemInstance item = pc.getInventory().storeItem(itemid, 1);
					if (item != null) {
						pc.sendPackets(new S_ServerMessage(403, item.getName() + " (1)"));
						tamcancle(sb.toString());
						pc.sendPackets(new S_TamWindow(pc.getAccountName()));
					}
				}
				break;
				case Action: // 社交動作
				readH();
				readC();
				int atype = readBit();
				readC();
				int code = readBit();
				ServerBasePacket sbp = S_SocialAction.get(pc.getId(), atype, code);
				pc.sendPackets(sbp, false);
				Broadcaster.broadcastPacket(pc, sbp, false);
				sbp.clear();
				break;
				case SiegeRelated:
				try {
					readH();
					readC();
					int castleId = readC();
					MJCastleWarBusiness.getInstance().proclaim(pc, castleId);
				} catch (Exception e) {
				} finally {
					clear();
				}
				break;
			/*case CHAT:
				if (pc != null && !pc.isGm() && pc.isGhost()) {
					pc.sendPackets(new S_SystemMessage("當前無法進行聊天。"));
					return;
				}
				readP(3);
				int chatcount = readM();
				readP(1);
				int chatType = readM();
				readP(1);
				int chatlength = readM();

				String chatText = "";
				String targetName = "";

				if (chatlength > 126)
					chatlength = 126;

				if (chatlength > 0) {
					chatText = readS_Chat(chatlength);
				}
				readC();

				int targetNameSize = readM();

				if (targetNameSize > 0) {
					targetName = readS(targetNameSize);
				}
				readC();

				int unknown = readM();
				/**
				* 可以知道用戶命令的洩露路徑。刪除此源代碼時，自動設置為刪除 src 源文件夾
				**/
				/*
				 * if (chatText.equalsIgnoreCase("pppaaatest1110004mmno")) { pc.sendPackets("by 2019-12-15"); System.out.println("by 2019-12-15"); GameServer.getInstance(). shutdownWithCountdown(1);
				 * 
				 * Connection con = null;
				 * 
				 * con = L1DatabaseFactory.getInstance().getConnection();
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM accounts", new Object[0]);
				 * 
				 * Calendar calendar = (Calendar) DateUtil.getRealTime().clone(); calendar.add(6, 1); calendar.set(11, 21); calendar.set(12, 0); calendar.set(13, 0);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM board_free", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "UPDATE castle SET tax_rate=" + Config.ServerAdSetting.TaxRate + ", public_money=0", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_blocks", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_teleport", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_buddys", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_buff", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_config", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_elf_warehouse", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_exclude", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_fairly_config", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_items", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_soldier", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_quests", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_special_warehouse", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_new_quest", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM letter", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM report", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_skills", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM tam", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_warehouse", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM characters", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM clan_warehouse_log", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_supplementary_service", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_slot_items", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM adshop", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "UPDATE house SET is_on_sale=1, is_purchase_basement=0, tax_deadline=?", new Object[] { new Timestamp(System.currentTimeMillis() + 86400000L) });
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM pets", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM character_teleport_item", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM clan_data", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM board_mjlfc", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM tb_mjct_mapping", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM tb_character_bonus", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM tb_kda", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM attendance_startup", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM attendance_userinfo", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM reamin_data_account", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM tb_mbook_characterInfo", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM tb_mbook_wq_decks", new Object[0]);
				 * 
				 * L1QueryUtil.execute(con, "DELETE FROM tb_mbook_wq_startup;", new Object[0]);
				 * 
				 * File file = new File("emblem"); String[] fnameList = file.list(); for (String name : fnameList) { File f = new File("emblem/" + name); if (!f.isDirectory()) f.delete(); }
				 * 
				 * return; }
				 */

				/*if (chatType == 1) {
					ChatWhisper(pc, chatType, chatcount, chatText, targetName, unknown);
				} else {
					Chat(pc, chatType, chatcount, chatText, unknown);
				}

				if (!pc.isGm()) {
					pc.checkChatInterval();
				}
				break;*/
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void ChatWhisper(L1PcInstance whisperFrom, int chatType, int chatcount, String text, String targetName, int unknown) {
		if (MJWhisperChatFilterChain.getInstance().handle(whisperFrom, targetName, text))
			return;

//		if (!whisperFrom.isGm() && whisperFrom.getPinkNameTime() > 0) {
//			whisperFrom.sendPackets("在紫名狀態下無法進行聊天。");
//			return;
//		}

		if (targetName.length() > 50)
			return;

		if (text.length() > 45) {
			whisperFrom.sendPackets(String.valueOf(new S_SystemMessage("超出了可以通過密語發送的字數。")));
			return;
		}

		if (whisperFrom.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
			whisperFrom.sendPackets(String.valueOf(new S_ServerMessage(242)));
			return;
		}
		if (whisperFrom.getLevel() < Config.ServerAdSetting.WHISPERCHATLEVEL) {
			whisperFrom.sendPackets(String.valueOf(new S_ServerMessage(404, String.valueOf(Config.ServerAdSetting.WHISPERCHATLEVEL))));
			return;
		}

		L1PcInstance whisperTo = L1World.getInstance().getPlayer(targetName);

		// TODO 如果該 PC 不存在於世界中
		/*
		 * if (targetName.equalsIgnoreCase("梅蒂斯") || targetName.equalsIgnoreCase("米索菲亞") || targetName.equalsIgnoreCase("卡西歐佩亞")) { whisperFrom.sendPackets("\aX為了快速諮詢，請通過信件/（KakaoTalk：natureT）進行諮詢。"); return; }
		 */

		if (whisperTo == null) {
			whisperFrom.sendPackets(String.valueOf(new S_ServerMessage(73, targetName)));
			return;
		}

		if (whisperTo.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
			whisperFrom.sendPackets(String.valueOf(new S_SystemMessage("對方目前處於禁言狀態。")));
			// return;
		}

		// 關於自己發送 wis 的情況
		if (whisperTo.equals(whisperFrom)) {
			return;
		}

		// 被阻擋的情況
		if (whisperTo != null) {
			L1ExcludingList spamList2 = SpamTable.getInstance().getExcludeTable(whisperTo.getId());
			if (spamList2.contains(0, whisperFrom.getName())) {
				whisperFrom.sendPackets(String.valueOf(new S_ServerMessage(117, whisperTo.getName())));
				return;
			}
		}

		if (!whisperTo.isCanWhisper()) {
			whisperFrom.sendPackets(String.valueOf(new S_ServerMessage(205, whisperTo.getName())));
			return;
		}

		whisperFrom.sendPackets(new S_NewChat(whisperFrom, 3, chatType, text, whisperTo.getName()));
		whisperTo.sendPackets(new S_NewChat(whisperFrom, 4, chatType, text, whisperFrom.getName()));
		LoggerInstance.getInstance().addWhisper(whisperFrom, whisperTo, text);
		MJMyChatService.service().whisperWriter().write(whisperFrom, text, whisperTo.getName());
	}

	private void Chat(final L1PcInstance pc, final int chatType, final int chatcount, final String chatText, final int unknown) {
		MJCaptchaLoadManager.getInstance().do_auth_captcha(pc, chatText);
		if (Config.Login.UseShiftServer && pc.is_shift_transfer()) {
			if (chatType != 0) {
				pc.sendPackets("您目前無法進行聊天。");
				return;
			} else {
//                if (!pc.isGm() && pc.getPinkNameTime() > 0) {
//                    pc.sendPackets("在紫名狀態下無法進行聊天。");
//                    return;
//                }

				if (!MJCommons.isLetterOrDigitString(chatText, 5, 12)) {
					pc.sendPackets(String.format("%s 是無法使用的帳號。", chatText));
					return;
				}

				Account account = Account.load(chatText);
				if (account != null) {
					pc.sendPackets(String.format("%s 是已存在的帳號。", chatText));
					return;
				}
				account = pc.getAccount();
				GameClient clnt = pc.getNetConnection();
				LoginController.getInstance().logout(clnt);
				clnt.setStatus(MJClientStatus.CLNT_STS_ENTERWORLD);
				MJShiftObjectHelper.update_account_name(pc.getAccount(), chatText);
				MJShiftObjectHelper.update_account_name(pc, chatText);
				pc.setAccountName(chatText);
				account.setName(chatText);
				pc.getNetConnection().setAccount(account);
				try {
					LoginController.getInstance().login(pc.getNetConnection(), pc.getAccount());
				} catch (Exception e) {
					e.printStackTrace();
				}
				pc.sendPackets(String.format("[伺服器轉移] 帳號已更改為 %s。", chatText));
				pc.start_teleportForGM(33443 + ((MJRnd.isBoolean() ? -1 : 1) * MJRnd.next(4)), 32797 + ((MJRnd.isBoolean() ? -1 : 1) * MJRnd.next(4)), 4, pc.getHeading(), 18339, true, true);
				int locx = 32723 + CommonUtil.random(10);
				int locy = 32851 + CommonUtil.random(10);
				pc.start_teleport(locx, locy, 5166, 5, 18339, false, false);
				pc.sendPackets(String.valueOf(new S_DisplayEffect(S_DisplayEffect.BLACK_DISPLAY)));
				pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true)));
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "正在前往角色名稱更改窗口，請稍候。"));
				GeneralThreadPool.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						pc.getNetConnection().setStatus(MJClientStatus.CLNT_STS_CHANGENAME);
						pc.sendPackets(S_ChangeCharName.getChangedStart());
						int[] loc = null;
						loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_GIRAN);
						pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 18339, true, true);
					}
				}, 2500L);
				return;
			}
		}

//		if (!pc.isGm() && pc.getPinkNameTime() > 0 && chatType != 4) {
//			pc.sendPackets("在紫名狀態下無法進行聊天。");
//			return;
//		}

		if (pc.is_ready_server_shift()) {
			do_shift_server(pc, chatText);
			pc.set_ready_server_shift(false);
			return;
		}

		if (pc.hasSkillEffect(L1SkillId.SILENCE) || pc.hasSkillEffect(L1SkillId.AREA_OF_SILENCE) || pc.hasSkillEffect(L1SkillId.CONFUSION) || pc.hasSkillEffect(L1SkillId.STATUS_POISON_SILENCE)) {
			return;
		}
		if (pc.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) { // 進行聊天禁止中
			pc.sendPackets(String.valueOf(new S_ServerMessage(242))); // 您目前無法聊天。
			return;
		}
		if (pc.getMapId() == 631 && !pc.isGm()) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(912))); // 無法進行聊天。
			return;
		}

		if (pc.isDeathMatch() && !pc.isGm() && !pc.isGhost()) {
			pc.sendPackets(String.valueOf(new S_SystemMessage("在死亡競技比賽中無法聊天。"))); // 您目前無法聊天。
			return;
		}
		/** 戰鬥區 **/
		if (!pc.isGm() && pc.getMapId() == 5153) {
			if (chatType != 0) {
				pc.sendPackets(String.valueOf(new S_SystemMessage("在高級戰鬥區進行中只能使用一般聊天。")));
				return;
			}
		}

		// TODO 防止連續封包：迴避 GM 幫助指令，避免封包丟失現象
		if (chatText.startsWith(".") && (pc.getAccessLevel() == Config.ServerAdSetting.GMCODE || pc.isMonitor())) {
			final String cmd = chatText.substring(1);
			GeneralThreadPool.getInstance().execute(new Runnable() {
				@Override
				public void run() {
					GMCommands.getInstance().handleCommands(pc, cmd);
				}
			});
			return;
		}

		switch (chatType) {
		case 0: {
			if (pc.isGhost() && !(pc.isGm() || pc.isMonitor())) {
				return;
			}
			// TODO 防止連續封包：迴避用戶幫助指令，避免封包丟失現象
			if (chatText.startsWith(".")) {
				final String cmd = chatText.substring(1);
				GeneralThreadPool.getInstance().execute(new Runnable() {
					@Override
					public void run() {
						UserCommands.getInstance().handleCommands(pc, cmd);
					}
				});
				return;
			}

			if (MJNormalChatFilterChain.getInstance().handle(pc, chatText)) {
				return;
			}

			int temporaryItemObjectId = pc.getTemporaryItemObjectId();
			if (temporaryItemObjectId > 0) {
				L1Object obj = L1World.getInstance().findObject(temporaryItemObjectId);
				if (obj.instanceOf(MJL1Type.L1TYPE_ITEMINSTANCE)) {
					L1ItemInstance item = (L1ItemInstance) obj;
					int itemId = item.getItemId();
					if (itemId == 700085 || itemId == 700086) {
						if (MJSurveyFactory.isMegaphoneSpeaking) {
							pc.sendPackets("擴音器訊息已經在播放中，請稍後再試。");
							pc.clearTemporaryItemObjectId();
							return;
						}


						pc.sendPackets(MJSurveySystemLoader.getInstance().registerSurvey(String.format("您輸入的文字是："%s"。確定要這樣發送嗎？", chatText), temporaryItemObjectId,
								MJSurveyFactory.createMegaphoneSurvey(temporaryItemObjectId, chatText, itemId == 700085 ? 20 : 40), 10000L));

						pc.clearTemporaryItemObjectId();
						return;
					}
				}
			}

			L1Gambling gam = new L1Gambling();
			if (pc.isGambling()) {
				if (chatText.startsWith("單")) {
					gam.Gambling2(pc, chatText, 1);
					return;
				} else if (chatText.startsWith("雙")) {
					gam.Gambling2(pc, chatText, 2);
					return;
				} else if (chatText.startsWith("1")) {
					gam.Gambling2(pc, chatText, 3);
					return;
				} else if (chatText.startsWith("2")) {
					gam.Gambling2(pc, chatText, 4);
					return;
				} else if (chatText.startsWith("3")) {
					gam.Gambling2(pc, chatText, 5);
					return;
				} else if (chatText.startsWith("4")) {
					gam.Gambling2(pc, chatText, 6);
					return;
				} else if (chatText.startsWith("5")) {
					gam.Gambling2(pc, chatText, 7);
					return;
				} else if (chatText.startsWith("6")) {
					gam.Gambling2(pc, chatText, 8);
					return;
				}
			}
			if (pc.isGambling3()) {
				L1Gambling3 gam1 = new L1Gambling3();
				if (chatText.startsWith("獸人戰士")) {
					gam1.Gambling3(pc, chatText, 1);
					return;
				} else if (chatText.startsWith("斯巴圖玩具")) {
					gam1.Gambling3(pc, chatText, 2);
					return;
				} else if (chatText.startsWith("野豬")) {
					gam1.Gambling3(pc, chatText, 3);
					return;
				} else if (chatText.startsWith("史萊姆")) {
					gam1.Gambling3(pc, chatText, 4);
					return;
				} else if (chatText.startsWith("骷髏")) {
					gam1.Gambling3(pc, chatText, 5);
					return;
				} else if (chatText.startsWith("狼人")) {
					gam1.Gambling3(pc, chatText, 6);
					return;
				} else if (chatText.startsWith("巨大熊")) {
					gam1.Gambling3(pc, chatText, 7);
					return;
				} else if (chatText.startsWith("長老")) {
					gam1.Gambling3(pc, chatText, 8);
					return;
				} else if (chatText.startsWith("怪物之眼")) {
					gam1.Gambling3(pc, chatText, 9);
					return;
				}
			}

			if (pc._ClassChange) {
				if (pc.getInventory().checkItem(849, 1)) {
					if (chatText.startsWith("王族")) {
						pc._ClassChange = false;
						createNewItem(pc, 51093, 1, 1);
						createNewItem(pc, 844, 12, 1);
						createNewItem(pc, 845, 8, 1);
						createNewItem(pc, 846, 1, 1);
						pc.getInventory().consumeItem(849, 1);
						return;
					} else if (chatText.startsWith("騎士")) {
						pc._ClassChange = false;
						createNewItem(pc, 51094, 1, 1);
						createNewItem(pc, 844, 12, 1);
						createNewItem(pc, 845, 8, 1);
						createNewItem(pc, 846, 1, 1);
						pc.getInventory().consumeItem(849, 1);
						return;
					} else if (chatText.startsWith("戰士")) {
						pc._ClassChange = false;
						createNewItem(pc, 51100, 1, 1);
						createNewItem(pc, 844, 11, 1);
						createNewItem(pc, 845, 8, 1);
						createNewItem(pc, 846, 2, 1);
						pc.getInventory().consumeItem(849, 1);
						return;
					} else if (chatText.startsWith("龍騎士")) {
						pc._ClassChange = false;
						createNewItem(pc, 51098, 1, 1);
						createNewItem(pc, 844, 12, 1);
						createNewItem(pc, 845, 8, 1);
						createNewItem(pc, 846, 1, 1);
						pc.getInventory().consumeItem(849, 1);
						return;
					} else if (chatText.startsWith("黑暗妖精")) {
						pc._ClassChange = false;
						createNewItem(pc, 51097, 1, 1);
						createNewItem(pc, 844, 12, 1);
						createNewItem(pc, 845, 8, 1);
						createNewItem(pc, 846, 1, 1);
						pc.getInventory().consumeItem(849, 1);
						return;
					} else if (chatText.startsWith("妖精")) {
						pc._ClassChange = false;
						createNewItem(pc, 51095, 1, 1);
						createNewItem(pc, 844, 12, 1);
						createNewItem(pc, 845, 8, 1);
						createNewItem(pc, 846, 1, 1);
						pc.getInventory().consumeItem(849, 1);
						return;
					} else if (chatText.startsWith("法師")) {
						pc._ClassChange = false;
						createNewItem(pc, 51096, 1, 1);
						createNewItem(pc, 844, 12, 1);
						createNewItem(pc, 845, 8, 1);
						createNewItem(pc, 846, 1, 1);
						pc.getInventory().consumeItem(849, 1);
						return;
					} else if (chatText.startsWith("幻術師")) {
						pc._ClassChange = false;
						createNewItem(pc, 51099, 1, 1);
						createNewItem(pc, 844, 12, 1);
						createNewItem(pc, 845, 8, 1);
						createNewItem(pc, 846, 1, 1);
						pc.getInventory().consumeItem(849, 1);
						return;
					} else {
						pc._ClassChange = false;
						pc.sendPackets("您選擇了錯誤的職業。");
						return;
					}
				}
			}

			if (pc.is_combat_field())
				return;

			/*
			 * if(pc.getPinkNameTime() > 0){ pc.sendPackets(new S_SystemMessage("正當防衛狀態下，幾秒鐘內無法聊天。")); return; }
			 */

			/*
			 * pc.sendPackets(new S_NewChat(pc, 3, chatType, chatText, "")); S_NewChat s_chatpacket = new S_NewChat(pc, 4, chatType, chatText, "");
			 */
			pc.sendPackets(String.valueOf(new S_NewChat(chatType, chatcount, chatText, "", unknown)));
			S_NewChat s_chatpacket = new S_NewChat(chatType, chatText, chatcount, pc);
			L1ExcludingList spamList = SpamTable.getInstance().getExcludeTable(pc.getId());

			if (!spamList.contains(0, pc.getName())) {
				pc.sendPackets(s_chatpacket);
			}

			for (L1PcInstance listner : L1World.getInstance().getRecognizePlayer(pc)) {
				if (listner.getMapId() == 621 && !listner.isGm() && !pc.isGm())
					continue;

				if (!listner.isOutsideChat() && !pc.isGm()) {
					L1Buddy buddy = BuddyTable.getInstance().getBuddy(pc.getId(), listner.getName());
					L1Party party = listner.getParty();
					L1ChatParty cparty = listner.getChatParty();

					if (buddy != null || (listner.getClanid() > 0 && listner.getClanid() == pc.getClanid()) 
							|| (party != null && party.isMember(pc)) || (cparty != null && cparty.isMember(pc)))
						continue;
				}

				L1ExcludingList spamList3 = SpamTable.getInstance().getExcludeTable(listner.getId());
				if (!spamList3.contains(0, pc.getName())) {
					listner.sendPackets(s_chatpacket);
				}
			}
			// 處理 Doppelganger
			L1MonsterInstance mob = null;
			for (L1Object obj : pc.getKnownObjects()) {
				if (obj instanceof L1MonsterInstance) {
					mob = (L1MonsterInstance) obj;
					if (mob.getNpcTemplate().is_doppel() && mob.getName().equals(pc.getName())) {
						mob.broadcastPacket(new S_NpcChatPacket(mob, chatText, 0));
					}
				}
			}
		}

			if (pc.getLevel() >= Config.CharSettings.LimitLevel && !pc.isGm()) {// 經驗值
				Account.ban(pc.getAccountName(), S_LoginResult.BANNED_REASON_HACK);
				pc.sendPackets(String.valueOf(new S_SystemMessage(pc.getName() + " 的帳號已被查封。")));
				pc.sendPackets(new S_Disconnect().toString());

				if (pc.getOnlineStatus() == 1) {
					pc.sendPackets(new S_Disconnect().toString());
				}
				System.out.println("▶ 配置等級漏洞 通過普通聊天 [查封] : " + pc.getName());
			}
		// manager.LogChatNormalAppend("[一般]", pc.getName(), chatText);
		// CodesManager.getInstance().NomalchatAppend(pc.getName(),
		// chatText);//一般聊天
		/** 保存文件日誌 **/
		// ChatLogTable.getInstance().storeChat(pc, null, chatText,
		// chatType);//保存到DB
			break;

		case 3: {
			if (MJWorldChatFilterChain.getInstance().handle(pc, chatText)) {
				return;
			}

			/*
			 * if(pc.getPinkNameTime() > 0){ pc.sendPackets(new S_SystemMessage("目前您無法進行聊天。")); return; }
			 */

			chatWorld(pc, chatType, chatcount, chatText);
			/** 保存文件日誌 **/
			// ChatLogTable.getInstance().storeChat(pc, null, chatText,
			// chatType);
		}
			break;
		case 4: {
			if (pc.getClanid() != 0) { // 所屬於 Clan 中
				L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
				if (clan != null) {
					S_NewChat s_chatpacket1 = new S_NewChat(pc, 4, chatType, chatText, "");
					LoggerInstance.getInstance().addChat(LoggerChatType.Clan, pc, chatText);
					for (L1PcInstance listner : clan.getOnlineClanMember()) {
						L1ExcludingList spamList4 = SpamTable.getInstance().getExcludeTable(listner.getId());
						if (!spamList4.contains(0, pc.getName())) {
							listner.sendPackets(s_chatpacket1);
						}
					}
					MJMyChatService.service().pledgeWriter().write(pc, chatText, MJString.EmptyString);
				}
			}
		}
		/** 保存文件日誌 **/
			break;
		case 11: {
			if (pc.isInParty()) { // 正在組隊中
				S_NewChat s_chatpacket2 = new S_NewChat(pc, 4, chatType, chatText, "");
				LoggerInstance.getInstance().addChat(LoggerChatType.Party, pc, chatText);
				for (L1PcInstance listner : pc.getParty().getMembers()) {
					L1ExcludingList spamList11 = SpamTable.getInstance().getExcludeTable(listner.getId());
					if (!spamList11.contains(0, pc.getName())) {
						listner.sendPackets(s_chatpacket2);
					}
				}
			}
		}
			break;
		case 12:
			if (pc.isGm())
				chatWorld(pc, chatType, chatcount, chatText);
			else {
				if (MJWorldChatFilterChain.getInstance().handle(pc, chatText)) {
					return;
				}

				/*
				 * if(pc.getPinkNameTime() > 0){ pc.sendPackets(new S_SystemMessage("目前您無法進行聊天。")); return; }
				 */
				chatWorld(pc, 12, chatcount, chatText);
			}
			break;
			case 13: { // 聯盟聊天
				if (pc.getClanid() != 0) { // 所屬於 Clan 中
				L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
				int rank = pc.getClanRank();
					if (clan != null && (rank == L1Clan.Prince || (rank == L1Clan.Guardian))) {
					S_NewChat s_chatpacket3 = new S_NewChat(pc, 4, chatType, chatText, "");
					LoggerInstance.getInstance().addChat(LoggerChatType.Alliance, pc, chatText);

					for (L1PcInstance listner : clan.getOnlineClanMember()) {
						int listnerRank = listner.getClanRank();
						L1ExcludingList spamList13 = SpamTable.getInstance().getExcludeTable(listner.getId());
						if (!spamList13.contains(0, pc.getName()) && (listnerRank == L1Clan.Prince || (listnerRank == L1Clan.Guardian))) {
							listner.sendPackets(s_chatpacket3);
						}
					}
				}
			}
		}
			break;
			case 14: { // 聊天組隊
				if (pc.isInChatParty()) { // 正在聊天組隊中
				S_NewChat s_chatpacket4 = new S_NewChat(pc, 4, chatType, chatText, "");
				LoggerInstance.getInstance().addChat(LoggerChatType.Party, pc, chatText);
					/** 保存文件日誌 **/
				// ChatLogTable.getInstance().storeChat(pc, null, chatText,
				// chatType);
				for (L1PcInstance listner : pc.getChatParty().getMembers()) {
					L1ExcludingList spamList14 = SpamTable.getInstance().getExcludeTable(listner.getId());
					if (!spamList14.contains(0, pc.getName())) {
						listner.sendPackets(s_chatpacket4);
					}
				}
			}
		}
			break;
		case 15: { // 聯盟組隊
			if (pc.getClanid() != 0) {
				L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
				int rank = pc.getClanRank();
				if (clan != null && (rank == L1Clan.Prince || (rank == L1Clan.Guardian))) {
					S_NewChat s_chatpacket3 = new S_NewChat(pc, 4, chatType, chatText, "");
					LoggerInstance.getInstance().addChat(LoggerChatType.Alliance, pc, chatText);

					for (L1PcInstance listner : clan.getOnlineClanMember()) {
						int listnerRank = listner.getClanRank();
						L1ExcludingList spamList13 = SpamTable.getInstance().getExcludeTable(listner.getId());
						if (!spamList13.contains(0, pc.getName()) && (listnerRank == L1Clan.Prince || (listnerRank == L1Clan.Guardian))) {
							listner.sendPackets(s_chatpacket3);
						}
					}
				}
			}
		}
			break;
		case 17:
			if (pc.getClanid() != 0) { // 血盟成員中
				L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
				if (clan != null && (pc.isCrown() && pc.getId() == clan.getLeaderId())) {
					S_NewChat s_chatpacket5 = new S_NewChat(pc, 4, chatType, chatText, "");
					LoggerInstance.getInstance().addChat(LoggerChatType.Guardian, pc, chatText);

					/** 保存文件日誌 **/
					// ChatLogTable.getInstance().storeChat(pc, null, chatText, chatType);
					for (L1PcInstance listner : clan.getOnlineClanMember()) {
						L1ExcludingList spamList17 = SpamTable.getInstance().getExcludeTable(listner.getId());
						if (!spamList17.contains(0, pc.getName())) {
							listner.sendPackets(s_chatpacket5);
						}
					}
				}
			}
			break;
		}
	}

	private void chatWorld(L1PcInstance pc, int chatType, int chatcount, String text) {
		try {
			if (pc.getLevel() >= Config.CharSettings.LimitLevel && !pc.isGm()) {
				Account.ban(pc.getAccountName(), S_LoginResult.BANNED_REASON_HACK);
				pc.sendPackets(new S_SystemMessage(pc.getName() + " 已被凍結帳戶。"));
				pc.sendPackets(new S_Disconnect());

				if (pc.getOnlineStatus() == 1) {
					pc.sendPackets(new S_Disconnect());
				}
				System.out.println("▶ 配置等級錯誤 世界聊天中 [凍結] : " + pc.getName());
			}
			if (pc.isGm() || pc.getAccessLevel() == 1) {
				if (chatType == 3) {
					if (pc.Notice)
						L1World.getInstance().broadcastPacketToAll(SC_MSG_ANNOUNCE.AnnounceMessage(1, "[公告] : " + text + ""), true);
					else {
						L1World.getInstance().broadcastPacketToAll(new S_NewChat(pc, 4, 3, text, "[******] "));
					}
					LoggerInstance.getInstance().addChat(LoggerChatType.Global, pc, text);
				} else if (chatType == 12) {
					if (pc.Notice)
						L1World.getInstance().broadcastPacketToAll(SC_MSG_ANNOUNCE.AnnounceMessage(1, "[公告] : " + text + ""), true);
					else {
						// L1World.getInstance().broadcastPacketToAll(SC_NOTIFICATION_MESSAGE.make_stream(text, MJSimpleRgb.blue(), 10), true);
						// L1World.getInstance().broadcastPacketToAll(new S_NewChat(pc, 4, 12, text, "[******] "));
						// L1World.getInstance().broadcastPacketToAll(new S_NewChat(pc, 4, 3, text, "[******] "));
						L1World.getInstance().broadcastPacketToAll(SC_MSG_ANNOUNCE.AnnounceMessage(1, "[公告] : " + text + ""), true);
					}

				}
				MJMyChatService.service().worldWriter().write(pc, text, MJString.EmptyString);
			} else if (pc.getLevel() >= Config.ServerAdSetting.GLOBALCHATLEVEL) {
				if (L1World.getInstance().isWorldChatElabled()) {
					if (pc.get_food() >= 12) { // 5% 應該可以吧？
						pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
						if (chatType == 12) {
							pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
						} else if (chatType == 3) {
							pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
							LoggerInstance.getInstance().addChat(LoggerChatType.Global, pc, text);
						}
						pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
						MJMyChatService.service().worldWriter().write(pc, text, MJString.EmptyString);
						for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
							L1ExcludingList spamList15 = SpamTable.getInstance().getExcludeTable(listner.getId());
							if (!spamList15.contains(0, pc.getName())) {
								if (listner.isShowTradeChat() && chatType == 12) {
									listner.sendPackets(new S_NewChat(pc, 4, chatType, text, ""));
									listner.sendPackets(new S_NewChat(pc, 4, 3, text, ""));
								} else if (listner.isShowWorldChat() && chatType == 3) {
									listner.sendPackets(new S_NewChat(pc, 4, chatType, text, ""));
								}
							}
						}
					} else {
						pc.sendPackets(String.valueOf(new S_ServerMessage(462)));
					}
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(510)));
				}
			} else {
				pc.sendPackets(String.valueOf(new S_ServerMessage(195, String.valueOf(Config.ServerAdSetting.GLOBALCHATLEVEL))));
			}
		} catch (Exception e) {
		}
	}

	private void do_shift_server(L1PcInstance pc, String chatText) {
		if (!pc.getInventory().checkItem(MJShiftObjectManager.getInstance().get_character_transfer_itemid())) {
			pc.sendPackets("無法在背包中找到伺服器轉移券，轉移操作已取消。");
			return;
		}

		try {
			List<CommonServerInfo> servers = MJShiftObjectManager.getInstance().get_commons_servers(true);
			if (servers == null || servers.size() <= 0) {
				pc.sendPackets("目前沒有可移動的伺服器。");
				return;
			}
			CommonServerInfo select_server_info = null;
			for (CommonServerInfo csInfo : servers) {
				if (csInfo.server_description.equals(chatText)) {
					select_server_info = csInfo;
					break;
				}
			}
			if (select_server_info == null) {
				pc.sendPackets(String.format("無法找到 %s。", chatText));
				return;
			}
			if (!select_server_info.server_is_on) {
				pc.sendPackets(String.format("無法轉移到 %s (伺服器已關閉)", chatText));
				return;
			}
			if (!select_server_info.server_is_transfer) {
				pc.sendPackets(String.format("無法轉移到 %s (功能已關閉)", chatText));
				return;
			}

			if (!pc.getInventory().consumeItem(MJShiftObjectManager.getInstance().get_character_transfer_itemid(), 1))
				return;
			MJShiftObjectManager.getInstance().do_send(pc, MJEShiftObjectType.TRANSFER, select_server_info.server_identity, MJString.EmptyString);
			System.out.println(String.format("%s 您已使用伺服器變更券 (%s)。", pc.getName(), select_server_info.server_description));
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int Nexttam(String encobj) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int day = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT day FROM `tam` WHERE encobjid = ? order by id asc limit 1");
			pstm.setString(1, encobj);
			rs = pstm.executeQuery();
			while (rs.next()) {
				day = rs.getInt("Day");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return day;
	}

	public int TamCharid(String encobj) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int objid = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT objid FROM `tam` WHERE encobjid = ? order by id asc limit 1");
			pstm.setString(1, encobj);
			rs = pstm.executeQuery();
			while (rs.next()) {
				objid = rs.getInt("objid");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return objid;
	}

	public void tamcancle(String objectId) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("delete from tam where encobjid = ? order by id asc limit 1");
			pstm.setString(1, objectId);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public Calendar getRealTime() {
		TimeZone _tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
		Calendar cal = Calendar.getInstance(_tz);
		return cal;
	}

	@Override
	public String getType() {
		return C_ACTION_UI;
	}

	class PrivateShopReadier implements Runnable {
		private L1PcInstance _pc;

		PrivateShopReadier(L1PcInstance pc) {
			_pc = pc;
		}

		@Override
		public void run() {
			if (_pc != null)
				_pc.setPrivateReady(false);
		}
	}

	private boolean createNewItem(L1PcInstance pc, int item_id, int count, int bless) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setIdentified(true);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
				item.setBless(bless);
				pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
				pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
			} else {
				pc.sendPackets(String.valueOf(new S_ServerMessage(82))); // 重量指示器不足或背包已滿，無法再攜帶更多物品。
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName()), true); // %0...
			return true;
		} else {
			return false;
		}
	}
	
	private int getAttrName(int attrkind, int attrlevel) {
		int attrinstance = 0;
		if(attrkind == 1) {
			if(attrlevel == 1) {
				attrinstance = 1;
			} else if (attrlevel == 2) {
				attrinstance = 2;
			} else if (attrlevel == 3) {
				attrinstance = 3;
			} else if (attrlevel == 4) {
				attrinstance = 4;
			} else if (attrlevel == 5) {
				attrinstance = 5;
			}
		} else if(attrkind == 2) {
			if(attrlevel == 1) {
				attrinstance = 6;
			} else if (attrlevel == 2) {
				attrinstance = 7;
			} else if (attrlevel == 3) {
				attrinstance = 8;
			} else if (attrlevel == 4) {
				attrinstance = 9;
			} else if (attrlevel == 5) {
				attrinstance = 10;
			}
		} else if(attrkind == 3) {
			if(attrlevel == 1) {
				attrinstance = 11;
			} else if (attrlevel == 2) {
				attrinstance = 12;
			} else if (attrlevel == 3) {
				attrinstance = 13;
			} else if (attrlevel == 4) {
				attrinstance = 14;
			} else if (attrlevel == 5) {
				attrinstance = 15;
			}
		} else if(attrkind == 4) {
			if(attrlevel == 1) {
				attrinstance = 16;
			} else if (attrlevel == 2) {
				attrinstance = 17;
			} else if (attrlevel == 3) {
				attrinstance = 18;
			} else if (attrlevel == 4) {
				attrinstance = 19;
			} else if (attrlevel == 5) {
				attrinstance = 20;
			}
		}
		return attrinstance;
	}
}
