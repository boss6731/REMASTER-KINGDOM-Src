package l1j.server.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.BLESS_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.COMA_B;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_A;
import static l1j.server.server.model.skill.L1SkillId.God_buff;
import static l1j.server.server.model.skill.L1SkillId.IRON_SKIN;
import static l1j.server.server.model.skill.L1SkillId.LIFE_MAAN;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_DEX;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_STR;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import MJShiftObject.Battle.MJShiftBattlePlayManager;
import l1j.server.Config;
import l1j.server.EventSystem.EventSystemInfo;
import l1j.server.EventSystem.EventSystemLoader;
import l1j.server.MJDeathPenalty.MJDeathPenaltyProvider;
import l1j.server.MJDeathPenalty.Exp.MJDeathPenaltyExpModel;
import l1j.server.MJDeathPenalty.Exp.MJDeathPenaltyexpDatabaseLoader;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJInstanceSystem.MJLFC.Creator.MJLFCCreator;
import l1j.server.MJRankSystem.Loader.MJRankUserLoader;
import l1j.server.MJSurveySystem.MJSurveySystemLoader;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Indun.SC_ARENACO_ENTER_INDUN_ROOM_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE;
import l1j.server.MJWarSystem.MJCastleWarBusiness;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.MJWarSystem.MJWarFactory;
import l1j.server.server.Account;
import l1j.server.server.GMCommands;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.server.datatables.BossMonsterSpawnList;
import l1j.server.server.server.datatables.CharacterCustomQuestTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.server.datatables.EventTimeTable;
import l1j.server.server.server.datatables.ExpTable;
import l1j.server.server.server.datatables.HouseTable;
import l1j.server.server.server.datatables.NpcTable;
import l1j.server.server.server.datatables.PetTable;
import l1j.server.server.server.datatables.PolyTable;
import l1j.server.server.server.datatables.ServerCustomQuestTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ChatParty;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1ClanJoin;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Question;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ClanAttention;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_Resurrection;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_Trade;
import l1j.server.server.server.templates.CustomQuest;
import l1j.server.server.templates.L1Boss;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;

public class C_Attr extends ClientBasePacket {

	private static final String C_ATTR = "[C] C_Attr";
	
	// message idx.
	public static final int MSGCODE_6008_BOSS 				= 1;
	public static final int MSGCODE_6008_LFC				= 2;
	public static final int MSGCODE_6008_KDINIT				= 3;	// kd is kill death..
	public static final int MSGCODE_6008_Name				= 6;
	public static final int MSGCODE_6008_EVENT_BOSS 		= 12;
	public static final int MSGCODE_6008_QUESTION 			= 24;
	public static final int MSGCODE_6008_INDUN_INVITE 		= 48;
	public static final int MSGCODE_6008_CONSOLE 			= 96;
	public static final int MSGCODE_6008_CLAN_ALLIANCE_KICK = 192;
	public static final int MSGCODE_6008_QUEST				 = 288;
	public static final int MSGCODE_6008_RESET				 = 70000;
	public static final int MSGCODE_6008_RESET_CONFIRM		 = 70001;
	
	public static final int MSGCODE_NO 						= 0;
	public static final int MSGCODE_YES 					= 1;
	
	public static final int CLAN_ALLIANCE_CLEAR				= 5460;
	public static final int CLAN_ALLIANCE_JOIN				= 223;
	public static final int CLAN_ALLIANCE_WITHDRAWAL		= 1210;

	public C_Attr(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);
		int i = readH();
		int attrcode;
		int c;
		int msgIdx = 0;
		String name;
		if (i == 479) {
			attrcode = i;
		} else {
			msgIdx = readD();
			attrcode = readH();
		}

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null)
			return;
		
		if (attrcode == 6008) {
			switch (msgIdx) {
			case MSGCODE_6008_CLAN_ALLIANCE_KICK:
				c = readC();
				if (c == MSGCODE_YES) {
					L1Clan kick_clan = L1World.getInstance().getClan(pc.getTempID());
					if (kick_clan.isAlliance_leader()) {
						return;
					}
					if (kick_clan != null && pc.getClan().AllianceSize() > 0) {
						for (int clanid : pc.getClan().Alliance()) {
							if (clanid == 0)
								continue;
							L1Clan clan = L1World.getInstance().getClan(clanid);
							if (clan == null)
								continue;
							clan.removeAlliance(kick_clan.getClanId());
							if (pc.getClan().AllianceSize() == 1) {
								pc.getClan().AllianceDelete();
								pc.getClan().setAlliance_leader(false);
								for (L1PcInstance tempPc : pc.getClan().getOnlineClanMember()) {
									tempPc.sendPackets(String.valueOf(new S_ServerMessage(1204, kick_clan.getClanName())));
									SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, pc.getClanid(), false, true);
								}
							} else {
								for (L1PcInstance tempPc : clan.getOnlineClanMember()) {
									tempPc.sendPackets(String.valueOf(new S_ServerMessage(1204, kick_clan.getClanName())));
									SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, clan.getClanId(), false, true);
								}
								for (L1PcInstance tempPc : kick_clan.getOnlineClanMember()) {
									tempPc.sendPackets(String.valueOf(new S_ServerMessage(1204, kick_clan.getClanName())));
									SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, pc.getClanid(), false, true);
								}
							}
							kick_clan.AllianceDelete();
							kick_clan.setAlliance_leader(false);
							ClanTable.getInstance().updateClan(clan);
						}
					}
					pc.setTempID(0);
					ClanTable.getInstance().updateClan(pc.getClan());
				} else if (c == MSGCODE_NO) {
					pc.setTempID(0);
				}
				break;
			case MSGCODE_6008_CONSOLE:
				c = readC();
				if (c == MSGCODE_NO) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("您拒絕了移動參加。")));
					pc.setConsole_type(0);
				} else if (c == MSGCODE_YES) {
					if (pc.getLevel() < Config.ServerAdSetting.YNpclevel) {
						pc.sendPackets(String.valueOf(new S_SystemMessage("從等級 " + Config.ServerAdSetting.YNpclevel + " 開始可以使用。")));
						return;
					}
					
					EventSystemInfo EventInfo = EventSystemLoader.getInstance().getEventSystemInfo2(pc.getConsole_type());
					if (EventInfo != null) {
						if (EventInfo.get_npc_id() == pc.getConsole_type()) {
							L1Map m = L1WorldMap.getInstance().getMap((short) EventInfo.get_event_map_id());
							int x = EventInfo.get_teleport_x();
							int y = EventInfo.get_teleport_y();
							int cx = 0;
							int cy = 0;
							int current_try = 0;
							int limit_try = 100;
							do {
								cx = x + (MJRnd.isBoolean() ? MJRnd.next(5) : -MJRnd.next(5));
								cy = y + (MJRnd.isBoolean() ? MJRnd.next(5) : -MJRnd.next(5));
							} while (++current_try < limit_try && !MJPoint.isValidPosition(m, cx, cy));
							if (current_try >= limit_try) {
								cx = x;
								cy = y;
							}
							pc.start_teleport(cx, cy, (short) EventInfo.get_event_map_id(), pc.getHeading(), 169, true, false);
//							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "已傳送到該Boss區域。"));
						}
					}
				}
				break;
			case MSGCODE_6008_INDUN_INVITE:
				c = readC();
				if (c == MSGCODE_YES) {
					SC_ARENACO_ENTER_INDUN_ROOM_ACK pck = SC_ARENACO_ENTER_INDUN_ROOM_ACK.newInstance();
					pck.set_room_id(pc.get_indun_room_num());
					pck.set_result(SC_ARENACO_ENTER_INDUN_ROOM_ACK.eResult.SUCCESS);
					pc.sendPackets(pck, MJEProtoMessages.SC_ARENACO_ENTER_INDUN_ROOM_ACK);
				} else if (c == MSGCODE_NO) {
					pc.set_indun_room_num(-1);
					pc.sendPackets("您已拒絕邀請。");
				}
				break;
			case MSGCODE_6008_QUESTION:
				c = readC();
				if (c == MSGCODE_YES) {
					L1Question.good += 1;
					pc.sendPackets("感謝您參與調查。");
				} else if (c == MSGCODE_NO) {
					L1Question.bad += 1;
					pc.sendPackets("感謝您參與調查。");
				}
				break;
			case MSGCODE_6008_EVENT_BOSS: {
				c = readC();
				if (c == MSGCODE_NO) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("您未參加Boss突襲移動。")));

				} else if (c == MSGCODE_YES) {
					Iterator<L1NpcInstance> iter = EventTimeTable.getInstance().get_npc_iter();
					L1NpcInstance npc = null;
					
					while (iter.hasNext()) {
						npc = iter.next();
						if (npc == null) {
							continue;
						}
						
						if (!npc.is_boss_alarm())
							continue;

						if (npc.getNpcId() != pc.getBossNpc()) {
							continue;
						}

						/** 活動提醒用戶點擊TEL時 **/
						if (pc.getLevel() < Config.ServerAdSetting.YNpclevel) {
							pc.sendPackets(String.valueOf(new S_ServerMessage(1287)));
							pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
							return;
						}
						if (pc.isFishing()) {
							pc.sendPackets(String.valueOf(new S_ServerMessage(4725)));
							pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
							return;
						}
						if (!pc.getMap().isTeleportable()) {
							pc.sendPackets(1413);
							pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
							return;
						}
						if (pc.getMapId() == 2237 || pc.getMapId() >= 1708 && pc.getMapId() <= 1712 || pc.getMapId() >= 12852 && pc.getMapId() <= 12862
								|| pc.getMapId() >= 15871 && pc.getMapId() <= 15899) {
							pc.sendPackets(1413);
							pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
							return;
						}
						
						if (MJShiftBattlePlayManager.is_shift_battle(pc))
							return;
						
						if (pc.getInventory().checkItem(40308, npc.get_boss_tel_count())) {
							pc.getInventory().consumeItem(40308, npc.get_boss_tel_count());
						} else {
							pc.sendPackets(String.valueOf(new S_SystemMessage("金幣 " + npc.get_boss_tel_count() + " 元不足。")));
							return;
						}
						
						int x = 0, y = 0, map = 0;
						map = npc.getMapId();
						x = npc.getHomeX();
						y = npc.getHomeY();

						if (x == 0 && y == 0 && map == 0) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("可移動時間已過。")));
//							pc.sendPackets(new S_Paralysis(7, false));
							pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false)));
							return;
						}
						
						MJPoint pt = MJPoint.newInstance(x, y, npc.getHomeRnd(), (short) map, 50);
						pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true);
					}
					return;
				}
				break;
			}
			
			case MSGCODE_6008_KDINIT:
				c = readC();
				if(c == MSGCODE_YES){
					L1ItemInstance item = pc.getKillDeathInitializeItem();
					if(item != null){
						pc.getKDA().onInit(pc);
						pc.getInventory().removeItem(item, 1);
					}
				}
				pc.setKillDeathInitializeItem(null);
				return;
			case MSGCODE_6008_Name:
				c = readC();
				if (c == MSGCODE_YES) {
					L1ItemInstance item = pc.getNameInstance();
					if (item != null) {
						int[] MALE_LIST = new int[] { 0, 20553, 138, 20278, 2786, 6658, 6671, 20567, 18520, 19296 };
						int[] FEMALE_LIST = new int[] { 1, 48, 37, 20279, 2796, 6661, 6650, 20577, 18499, 19299 };
						if (pc.get_sex() == 0) {
							pc.set_sex(1);
							pc.setClassId(FEMALE_LIST[pc.getType()]);
						} else {
							pc.set_sex(0);
							pc.setClassId(MALE_LIST[pc.getType()]);
						}
						pc.setCurrentSprite(pc.getClassId());
						pc.sendShape(pc.getClassId());
						pc.getInventory().removeItem(item, 1);
						pc.sendPackets(String.valueOf(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true)));
					}

					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							GameClient clnt = pc.getNetConnection();
							l1j.server.server.clientpackets.C_NewCharSelect.restartProcess(pc);
							Account acc = clnt.getAccount();
							clnt.sendPacket(new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
							if (acc.countCharacters() > 0)
								l1j.server.server.clientpackets.C_CommonClick.sendCharPacks(clnt);
						}
					}, 500L);
				}
				pc.setNameInstance(null);
				return;
				/** 2016.11.26 MJ 應用中心 LFC **/
			case MSGCODE_6008_LFC:{
				c = readC();
				if(c == MSGCODE_NO){
					MJLFCCreator.setInstStatus(pc, InstStatus.INST_USERSTATUS_NONE);
				}else if(c == MSGCODE_YES){
					if(pc.getInstStatus() == InstStatus.INST_USERSTATUS_LFCREADY)
						MJLFCCreator.setInstStatus(pc, InstStatus.INST_USERSTATUS_LFCINREADY);
				}
				return;
			}
			case MSGCODE_6008_BOSS: {
				c = readC();
				if (c == MSGCODE_NO) {
					if (pc.getLevel() < Config.ServerAdSetting.YNpclevel) {
						pc.sendPackets(String.valueOf(new S_SystemMessage("等級 " + Config.ServerAdSetting.YNpclevel + " 以上可使用。")));
						return;
					}
					pc.sendPackets(String.valueOf(new S_SystemMessage("您已拒絕參加Boss移動。")));
					pc.setBossYN(0);
				} else if (c == MSGCODE_YES) {
					if (pc.getLevel() < Config.ServerAdSetting.YNpclevel) {
						pc.sendPackets(String.valueOf(new S_SystemMessage("等級 " + Config.ServerAdSetting.YNpclevel + " 以上可使用。")));
						return;
					}

					L1Boss boss = BossMonsterSpawnList.find(pc.getBossYN());
					if (boss == null)
						pc.sendPackets("由於時間超過，傳送失敗。(10秒)");
					else {

						L1Map m = L1WorldMap.getInstance().getMap((short) boss.getMap());
						int x = boss.getX();
						int y = boss.getY();
						int cx = 0;
						int cy = 0;
						int current_try = 0;
						int limit_try = 100;
						do {
							cx = x + (MJRnd.isBoolean() ? MJRnd.next(5) : -MJRnd.next(5));
							cy = y + (MJRnd.isBoolean() ? MJRnd.next(5) : -MJRnd.next(5));
						} while (++current_try < limit_try && !MJPoint.isValidPosition(m, cx, cy));
						if (current_try >= limit_try) {
							cx = x;
							cy = y;
						}
						pc.start_teleport(cx, cy, (short) boss.getMap(), pc.getHeading(), 18339, true, false);
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "已移動至該Boss區域。"));
					}
				}
				break;
			}
			case MSGCODE_6008_RESET:
				if (readC() == MSGCODE_YES) {
					//GMCommands.clear_DB(pc);
					pc.sendPackets(String.valueOf(new S_Message_YN(C_Attr.MSGCODE_6008_RESET_CONFIRM, 6008, "是否確定要進行初始化？")));
				} else if (readC() == MSGCODE_NO) {
					pc.sendPackets("您已取消資料庫初始化。");
				}
				return;
			case MSGCODE_6008_RESET_CONFIRM:
				if (readC() == MSGCODE_YES) {
					GMCommands.clear_DB(pc);
//					GMCommands.test(pc);
					pc.sendPackets("資料庫初始化正在進行中。");
				} else if (readC() == MSGCODE_NO) {
					pc.sendPackets("您已取消資料庫初始化。");
				}
				return;
			}

			if (MJSurveySystemLoader.getInstance().isSurvey(msgIdx)) {
				MJSurveySystemLoader.getInstance().submitSurvey(pc, msgIdx, readC() == 1);
				return;
			}
			
			
			
		}
		switch (attrcode) {
		case CLAN_ALLIANCE_CLEAR:
			c = readC();
			if (c == 1) { // Yes
				if (pc.getClan() != null && pc.getClan().AllianceSize() > 0) {
					for (int clanid : pc.getClan().Alliance()) {
						if (clanid == 0)
							continue;
						L1Clan clan = L1World.getInstance().getClan(clanid);
						if (clan == null)
							continue;
						clan.AllianceDelete();
						clan.removeAlliance(pc.getClanid());
						ClanTable.getInstance().updateClan(clan);
						for (L1PcInstance tempPc : clan.getOnlineClanMember()) {
							tempPc.sendPackets(new S_ServerMessage(1204, pc.getClan().getClanName()));
							SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, clan.getClanId(), false, true);
						}
						for (L1PcInstance tempPc : pc.getClan().getOnlineClanMember()) {
							SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, pc.getClanid(), false, true);
						}
					}
				}
				pc.getClan().AllianceDelete();
				pc.getClan().setAlliance_leader(false);
				ClanTable.getInstance().updateClan(pc.getClan());
			}
			break;
		case CLAN_ALLIANCE_JOIN:
			c = readC();
			L1PcInstance allianceLeader = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
			if (allianceLeader == null) {
				return;
			}
			if (pc.getLevel() <= Config.ServerAdSetting.CROWNBLOOD_ALLIANCE_LEVEL) {
				pc.sendPackets("等級 " + Config.ServerAdSetting.CROWNBLOOD_ALLIANCE_LEVEL + " 以上可加入同盟。");
				return;
			}

			pc.setTempID(0);
			int PcClanId = pc.getClanid();
			int TargetClanId = allianceLeader.getClanid();
			String PcClanName = pc.getClanname();
			String TargetClanName = allianceLeader.getClanname();
			L1Clan PcClan = L1World.getInstance().getClan(PcClanId);
			L1Clan TargetClan = L1World.getInstance().getClan(TargetClanId);
			if (c == 1) { // Yes
				PcClan.setAlliance_leader(true);
				PcClan.addAlliance(TargetClanId);
				TargetClan.addAlliance(PcClanId);
				if (PcClan.AllianceSize() > 1) {
					for (int clanid : PcClan.Alliance()) {
						L1Clan AllianceClan = L1World.getInstance().getClan(clanid);
						AllianceClan.setAllianceList(PcClan.getAllianceList().toString());
						for (L1PcInstance tempPc : AllianceClan.getOnlineClanMember()) {
							SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, TargetClanId, true, false);
						}
						ClanTable.getInstance().updateClan(AllianceClan);
					}
				}
				pc.sendPackets(String.valueOf(new S_ServerMessage(1200, TargetClanName)));
				allianceLeader.sendPackets(String.valueOf(new S_ServerMessage(224, PcClanName, TargetClanName)));
			} else if (c == 0) { // No
				allianceLeader.sendPackets(1198);
			}
			break;
		case CLAN_ALLIANCE_WITHDRAWAL:
			if (readC() == 1) {
				L1Clan leave_clan = L1World.getInstance().getClan(pc.getClanid());
				if (leave_clan.isAlliance_leader()) {
					return;
				}
				if (pc.getClan() != null && pc.getClan().AllianceSize() > 0) {
					for (int clanid : pc.getClan().Alliance()) {
						if (clanid == 0)
							continue;
						L1Clan clan = L1World.getInstance().getClan(clanid);
						if (clan == null)
							continue;
						
						clan.removeAlliance(pc.getClanid());
						if (clan.AllianceSize() == 1) {
							clan.AllianceDelete();
							clan.setAlliance_leader(false);
							for (L1PcInstance tempPc : clan.getOnlineClanMember()) {
								tempPc.sendPackets(String.valueOf(new S_ServerMessage(1204, leave_clan.getClanName())));
								SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, clan.getClanId(), false, true);
							}
						} else {
							for (L1PcInstance tempPc : clan.getOnlineClanMember()) {
								tempPc.sendPackets(new S_ServerMessage(1204, pc.getClan().getClanName()));
								SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, clan.getClanId(), false, true);
							}
							for (L1PcInstance tempPc : pc.getClan().getOnlineClanMember()) {
								SC_BLOOD_PLEDGE_ALLY_LIST_CHANGE.ally_list_change_send(tempPc, pc.getClanid(), false, true);
							}
						}
						ClanTable.getInstance().updateClan(clan);
					}
				}
				pc.sendPackets(new S_ServerMessage(1204, pc.getClan().getClanName()));
				pc.getClan().AllianceDelete();
				leave_clan.setAlliance_leader(false);
				ClanTable.getInstance().updateClan(pc.getClan());
			}
			break;
		case 178:
			System.out.println("使用技能");
			break;
		case 180:
			readC();
			name = readS();
			boolean maple = false;
			boolean ring = false;
			boolean ring2 = false;
			if (name.startsWith("maple")) {
				String aa = name;
				String bb = aa.replace("maple ", "");
				name = bb;
				maple = true;
			}

			if (name.equalsIgnoreCase("ranking class polymorph")) {
				if (!MJRankUserLoader.getInstance().isRankPoly(pc)) {
					return;
				}
				if (pc.getRankLevel() < 3)
					return;
				switch (pc.getType()) {
				case 0:
					if (pc.get_sex() == 0)
						name = "rangking prince male";
					else
						name = "rangking prince female";
					break;
				case 1:
					if (pc.get_sex() == 0)
						name = "rangking knight male";
					else
						name = "rangking knight female";
					break;
				case 2:
					if (pc.get_sex() == 0)
						name = "rangking elf male";
					else
						name = "rangking elf female";
					break;
				case 3:
					if (pc.get_sex() == 0)
						name = "rangking wizard male";
					else
						name = "rangking wizard female";
					break;
				case 4:
					if (pc.get_sex() == 0)
						name = "rangking darkelf male";
					else
						name = "rangking darkelf female";
					break;
				case 5:
					if (pc.get_sex() == 0)
						name = "rangking dragonknight male";
					else
						name = "rangking dragonknight female";
					break;
				case 6:
					if (pc.get_sex() == 0)
						name = "rangking illusionist male";
					else
						name = "rangking illusionist female";
					break;
				case 7:
					if (pc.get_sex() == 0)
						name = "rangking warrior male";
					else
						name = "rangking warrior female";
					break;
				case 8:
					if (pc.get_sex() == 0)
						name = "rangking fencer male";
					else
						name = "rangking fencer female";
					break;
				}
			}

			if (name != null && name.length() > 0) {
				L1PolyMorph poly = PolyTable.getInstance().getTemplate(name);
				if (poly != null || name.equals("")) {
					if (name.equals("")) {
						int spriteId = pc.getCurrentSpriteId();
						if (spriteId == 6034 || spriteId == 6035) {
						} else {
							if (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER)) {
								pc.removeSkillEffect(L1SkillId.POLY_RING_MASTER);
							} else if (pc.hasSkillEffect(L1SkillId.POLY_RING_MASTER2)) {
								pc.removeSkillEffect(L1SkillId.POLY_RING_MASTER2);
							} 
							pc.removeSkillEffect(L1SkillId.SHAPE_CHANGE);
						}
					} else if (poly.getMinLevel() <= pc.getLevel() || pc.isGm()) {
						if(pc.isPolyRingMaster2() && maple) {
							ring = false;
							ring2 = true;
						}else if(pc.isPolyRingMaster() && maple) {
							ring = true;
							ring2 = false;
						}
						if (ring && ring2){
							ring = false;
							ring2 = true;
						}
						L1PolyMorph.doPoly(pc, poly.getPolyId(), pc.getId() == msgIdx ? 7200 : 3600, L1PolyMorph.MORPH_BY_ITEMMAGIC, ring, ring2);
					} else if (Config.ServerAdSetting.PolyEvent2) {
						int minlevel = 0;
						switch (poly.getPolyId()) {
						case 17541:
						case 17531:
						case 17545:
						case 17515:
						case 17535:
						case 17549:
						case 16014:
						case 16284:
						case 15986:
						case 16053:
						case 16008:
						case 16056:
						case 16002:
						case 16074:
						case 16027:
						case 16040:
							minlevel = 80;
							break;
						case 13152:
						case 13153:
						case 12681:
						case 15868:
						case 11389:
						case 15866:
						case 15539:
						case 15537:
						case 15534:
						case 13635:
						case 13631:
						case 15814:
						case 15550:
						case 15548:
						case 15545:
						case 15831:
						case 15833:
						case 15830:
						case 15832:
						case 15528:
						case 15531:
							minlevel = 65;
							break;
						case 12702:
						case 15850:
						case 11385:
						case 15847:
						case 12240:
						case 15599:
						case 13346:
						case 15848:
						case 15865:
						case 15849:
							minlevel = 60;
							break;
						default:
							minlevel = poly.getMinLevel();
							break;
						}	
						if (minlevel <= pc.getLevel()) {
							if(pc.isPolyRingMaster2() && maple) {
								ring = false;
								ring2 = true;
							}else if(pc.isPolyRingMaster() && maple) {
								ring = true;
								ring2 = false;
							}
							if (ring && ring2){
								ring = false;
								ring2 = true;
							}
							L1PolyMorph.doPoly(pc, poly.getPolyId(), pc.getId() == msgIdx ? 7200 : 3600, L1PolyMorph.MORPH_BY_ITEMMAGIC, ring, ring2);
							return;
						} else {
							pc.sendPackets("現在無法進行該變身。");
						}
					} else {
						pc.sendPackets("現在無法進行該變身。");
					}
				}
			}
			// 添加 msgIdx 或 pc.polyTrigger 檢查邏輯。
			if(msgIdx == pc.getId()) {
				if(pc.polyTrigger() != null) {
					pc.polyTrigger().onWork();
				}
			}
			break;
		case 97: // %0 已申請加入血盟。您是否同意？ (Y/N)
			c = readH();
			L1PcInstance joinPc = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
			pc.setTempID(0);
			if (joinPc != null) {
				if (c == 0) { // No
					joinPc.sendPackets(String.valueOf(new S_ServerMessage(96, pc.getName()))); // 1%0 已拒絕您的請求。
				} else if (c == 1) { // Yes
					L1ClanJoin.getInstance().ClanJoin(pc.getClan(), joinPc);
				}
			}
			break;
		case 3348: // 注意符號
			c = readC();
			if (c == 0) {
			} else if (c == 1) { // yes
				L1PcInstance GazePc = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID()); // 請求用戶
				pc.setTempID(0);
				if(GazePc == null)
					return;
				L1Clan targetClan = L1World.getInstance().findClan(GazePc.getClanname());// 請求用戶血盟
				if (targetClan == null) {
					return;
				}

				L1Clan pcClan = L1World.getInstance().findClan(pc.getClanname());
				if (pcClan == null) {
					return;
				}

				targetClan.addGazelist(pcClan.getClanName());
				pcClan.addGazelist(targetClan.getClanName());

				for (L1PcInstance member : pcClan.getOnlineClanMember()) {
					member.sendPackets(String.valueOf(new S_ClanAttention(true, targetClan.getClanName())));
					member.sendPackets(String.valueOf(new S_ClanAttention(pcClan.getGazeSize(), pcClan.getGazeList())));
				}

				for (L1PcInstance member : targetClan.getOnlineClanMember()) {
					member.sendPackets(String.valueOf(new S_ClanAttention(true, pcClan.getClanName())));
					member.sendPackets(String.valueOf(new S_ClanAttention(targetClan.getGazeSize(), targetClan.getGazeList())));
				}
			}
			break;
			case 217: // %0血盟的%1希望與您的血盟開戰。您是否接受戰爭？ (Y/N)
			case 221: // %0血盟希望投降。您是否接受？ (Y/N)
			case 222: // %0血盟希望結束戰爭。您是否結束戰爭？ (Y/N)
			c = readC();
			L1PcInstance enemyLeader = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
			if (enemyLeader == null) {
				return;
			}
			pc.setTempID(0);
			String clanName = pc.getClanname();
			String enemyClanName = enemyLeader.getClanname();
			if (c == 0) { // No
				if (i == 217) {
					enemyLeader.sendPackets(String.valueOf(new S_ServerMessage(236, clanName))); // %0 血盟已拒絕與您的血盟開戰。
				} else if (i == 221 || i == 222) {
					enemyLeader.sendPackets(String.valueOf(new S_ServerMessage(237, clanName))); // %0 血盟已拒絕您的提案。
				}
			} else if (c == 1) { // Yes
				L1Clan defense = L1World.getInstance().findClan(clanName);
				L1Clan offense = L1World.getInstance().findClan(enemyClanName);
				if(defense == null || offense == null)
					return;
				
				if (i == 217) {
					MJWar war = MJWarFactory.createNormalWar(defense);
					war.register(offense);
				} else{
					MJWar war = offense.getCurrentWar();
					if(war == null)
						return;
					
					war.notifySurrender(defense, offense);
					war.dispose();
				}
			}
			break;
		case 252: // %0%s 希望與您進行物品交易。您是否進行交易？ (Y/N)
			c = readC();
			L1PcInstance trading_partner = (L1PcInstance) L1World.getInstance().findObject(pc.getTradeID());
			L1Npc npc = NpcTable.getInstance().getTemplate(400064);
			L1Npc npc3 = NpcTable.getInstance().getTemplate(300027);
			if (trading_partner != null) {
				if (c == 0) { // No
					trading_partner.sendPackets(String.valueOf(new S_ServerMessage(253, pc.getName())));
					// %0%d 未同意與您的交易。
					pc.setTradeID(0);
					trading_partner.setTradeID(0);
				} else if (c == 1) { // Yes
					pc.sendPackets(String.valueOf(new S_Trade(trading_partner.getName())));
					trading_partner.sendPackets(String.valueOf(new S_Trade(pc.getName())));
				}
				/** 小遊戲 **/
			} else {
				if (c == 0) { // No
					pc.setTradeID(0);
				} else if (c == 1) { // Yes
					if (pc.getX() == 33507 && pc.getY() == 32851 && pc.getMapId() == 4) {
						pc.sendPackets(String.valueOf(new S_Trade(npc.get_name())));
					} else if (pc.getX() == 33515 && pc.getY() == 32851 && pc.getMapId() == 4) {
						pc.sendPackets(String.valueOf(new S_Trade(npc3.get_name())));
					}
				}
			}
			break;
		case 321: // 是否再次復活？ (Y/N)
			c = readC();
			L1PcInstance resusepc1 = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
			pc.setTempID(0);
			if (resusepc1 != null) { // 復活卷軸
				if (c == 0) { // No
					;
				} else if (c == 1) { // Yes
					pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), '\346')));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), '\346'));
					// pc.resurrect(pc.getLevel());
					// pc.setCurrentHp(pc.getLevel());
					pc.resurrect(pc.getMaxHp() / 2);
					pc.setCurrentHp(pc.getMaxHp() / 2);
					// 重生註釋
					//pc.startHpMpRegeneration();
					// pc.startMpRegeneration();
					pc.sendPackets(new S_Resurrection(pc, resusepc1, 0));
					pc.broadcastPacket(new S_Resurrection(pc, resusepc1, 0));
					pc.sendPackets(new S_CharVisualUpdate(pc));
					pc.broadcastPacket(new S_CharVisualUpdate(pc));
//					if(pc.getParty() != null) {
//						pc.getParty().onAuraMember(pc);
//					}
				}
			}
			break;
		case 322: // 是否再次復活？ (Y/N)
			c = readC();
			L1PcInstance resusepc2 = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
			pc.setTempID(0);
			if (resusepc2 != null) { // 祝福復活卷軸, Resurrection, Greater Resurrection
				if (c == 0) { // No
				} else if (c == 1) { // Yes
					pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), '\346')));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), '\346'));
					pc.resurrect(pc.getMaxHp());
					pc.setCurrentHp(pc.getMaxHp());
					pc.sendPackets(new S_Resurrection(pc, resusepc2, 0));
					pc.broadcastPacket(new S_Resurrection(pc, resusepc2, 0));
					pc.sendPackets(new S_CharVisualUpdate(pc));
					pc.broadcastPacket(new S_CharVisualUpdate(pc));
					// EXP 損失中, 能施放 G-RES, EXP 損失的死亡
					// 只有滿足所有條件時才會恢復 EXP
					if (pc.get_exp_res() == 1 && pc.isGres() && pc.isGresValid()) {
						pc.resExp();
						pc.set_exp_res(0);
						pc.setGres(false);
						int index = 0;
						List<MJDeathPenaltyExpModel> deathpenalityexp = pc.get_deathpenalty_exp();
						if (deathpenalityexp != null) {
							for (int j = 0; j < deathpenalityexp.size(); j++) {
								int indexid = deathpenalityexp.get(i).getId();
								if (index < indexid) {
									index = indexid;
								}
							}
							pc.delete_deathpenalty_exp(index);
							MJDeathPenaltyexpDatabaseLoader.getInstance().do_Select(pc);
						}
					}
			
					// if(pc.getParty() != null) {
					// pc.getParty().onAuraMember(pc);
					// }
				}
			}
			break;
		case 325: // 請決定動物的名字：
			c = readC(); // ?
			name = readS();
			int len = name.length();
			if(len > 6 || len < 1){
				pc.sendPackets("請檢查寵物名字的長度。");
				pc.setTempID(0);
				break;
			}
			
			L1PetInstance pet = (L1PetInstance) L1World.getInstance().findObject(pc.getTempID());
			pc.setTempID(0);
			renamePet(pet, name);
			break;

		case 512: // 您的名字是？
			c = readC(); // ?
			name = readS();
			int houseId = pc.getTempID();
			pc.setTempID(0);
			if (name.length() <= 16) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				house.setHouseName(name);
				HouseTable.getInstance().updateHouse(house); // 寫入資料庫
			} else {
				pc.sendPackets(String.valueOf(new S_ServerMessage(513))); // 您的名字太長了。
			}
			break;
			/*
			 * if (c == 0) { pc.sendPackets(new S_SystemMessage("取消了使用回憶的蠟燭"
			 * )); } else if (c == 1) { if
			 * (!pc.getMap().isSafetyZone(pc.getLocation())) { pc.sendPackets(new
			 * S_ChatPacket(pc, "只能在安全區域使用。")); return; } if
			 * (pc.getInventory().checkItem(200000, 1)) { if (pc.getLevel() !=
			 * pc.getHighLevel()) { pc.sendPackets(new S_SystemMessage(
			 * "角色等級已降低，請升級後再使用。")); return; } if (pc.getLevel() > 54) {
			 * pc.getInventory().consumeItem(200000, 1); Random random = new
			 * Random(System.nanoTime()); int locx = 32723 + random.nextInt(10); int
			 * locy = 32851 + random.nextInt(10); L1Teleport.teleport(pc, locx,
			 * locy, (short) 5166, 5, true); 重置屬性點(pc); } else { pc.sendPackets(new
			 * S_SystemMessage("重置屬性點僅限於55級以上。")); } } else {
			 * pc.sendPackets(new S_SystemMessage("沒有回憶的蠟燭。")); return; } }
			 */
		case 6008:
			c = readC();
			if (c == 0) { // No
				if (pc.isSpecialBuff()) {
					pc.setSpecialBuff(false);
				} else if (pc.getCustomQuestId() != 0) {
					pc.setCustomQuestId(0);
					pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getCustomQuestNpcObjId(), "")));
					pc.setCustomQuestNpcObjId(0);
				} else {
					pc.sendPackets(String.valueOf(new S_SystemMessage("輸入時間已超時。")));
				}
			} else if (c == 1) { // Yes
				if (pc.getRaidGame()) {
					pc.setRaidGame(false);
				} else if (pc.isSpecialBuff()) {
					pc.setSpecialBuff(false);
					GeneralThreadPool.getInstance().execute(new IntegratedBuff(pc));
				}
				if (c == 1) {
					if (pc.getCustomQuestId() != 0) {
						CustomQuest cq = ServerCustomQuestTable.getInstance().getCustomQuest(pc.getCustomQuestId());
						if (cq != null) {
							if (pc.getLevel() < cq.getMinLevel() || pc.getLevel() > cq.getMaxLevel()) {
								pc.sendPackets("\fW[任務通知] 該任務僅限於 Lv." + cq.getMinLevel() + " 至 Lv." + cq.getMaxLevel() + " 的玩家進行。");
							} else {
								pc.addCustomQuest(cq.getQuestId(), cq.getQuestType());
								pc.sendPackets("\fW[任務通知] 已接受 " + cq.getQuestName() + " 任務。");
								pc.sendPackets(String.valueOf(new S_Sound(3450)));
								pc.sendPackets(String.valueOf(new S_NPCTalkReturn(pc.getCustomQuestNpcObjId(), "")));
								CharacterCustomQuestTable.save(pc); // 新增
							}
						}
						pc.setCustomQuestId(0);
						pc.setCustomQuestNpcObjId(0);
					}
				} else {
					pc.sendPackets("錯誤的請求。");
				}
			}
			break;
		case 630:
			c = readC();
			L1PcInstance fightPc = (L1PcInstance) L1World.getInstance().findObject(pc.getFightId());
			if (c == 0) {
				pc.setFightId(0);
				fightPc.setFightId(0);
				fightPc.sendPackets(String.valueOf(new S_ServerMessage(631, pc.getName())));
			} else if (c == 1) {
				fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, fightPc.getFightId(), fightPc.getId()));
				pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, pc.getFightId(), pc.getId()));
			}
			break;
			case 653: // 離婚後，戒指將消失。您想要離婚嗎？ (Y/N)
				c = readC();
				if (c == 0) { // No
					;
				} else if (c == 1) { // Yes
					pc.setPartnerId(0);
					pc.save(); // 將角色資訊寫入資料庫
				}
				break;
			case 654: // %0%s想要和你結婚。你要和%0結婚嗎？ (Y/N)
			c = readC();
			L1PcInstance partner = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
			pc.setTempID(0);
			if (partner != null) {
				if (c == 0) { // No
					partner.sendPackets(String.valueOf(new S_ServerMessage( // %0%s拒絕了與您的結婚。
							656, pc.getName())));
				} else if (c == 1) { // Yes
					pc.setPartnerId(partner.getId());
					pc.save();
					pc.sendPackets(String.valueOf(new S_ServerMessage( // 在大家的祝福中，兩位結婚了。
							790)));
					pc.sendPackets(String.valueOf(new S_ServerMessage( // 恭喜！您與%0結婚了。
							655, partner.getName())));

					partner.setPartnerId(pc.getId());
					partner.save();
					partner.sendPackets(String.valueOf(new S_ServerMessage( // 在大家的祝福中，兩位結婚了。
							790)));
					partner.sendPackets(String.valueOf(new S_ServerMessage( // 恭喜！您與%0結婚了。
							655, pc.getName())));
				}
			}
			break;

			// Call Clan
			case 729: // 血盟成員正在嘗試傳送您。是否同意？ (Y/N)
			c = readC();
			if (c == 0) { // No
				;
			} else if (c == 1) { // Yes
				callClan(pc);
			}
			break;
			case 738: // 恢復經驗值需要 %0 的金幣。是否恢復經驗值？
			c = readC();
			if (c == 0) { // No
				;
			} else if (c == 1 && pc.get_exp_res() == 1) { // Yes
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
				if (pc.getInventory().consumeItem(L1ItemId.ADENA, cost)) {
					pc.resExpToTemple();
//					pc.set_exp_res(0);
					
//					MJDeathPenaltyExpModel model = pc.get_deathpenalty_exp().get(req.get_index());

/*					pc.delete_deathpenalty_exp(model.getId());
					MJDeathPenaltyexpDatabaseLoader.getInstance().do_Select(pc);
					MJDeathPenaltyProvider.provider().sendexpinfo(pc);*/
					
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(189)));// 1金幣不足.
				}
			}
			break;
		case 2551: // 恢復經驗值將消耗救濟證書。是否恢復經驗值？ (Y/N)
			c = readC();
			if (c == 0) {
			} else if (c == 1 && pc.get_exp_res() == 1) {
				if (!pc.getInventory().checkItem(3000049, 1) && !pc.getInventory().checkItem(4100694, 1)) {
					pc.sendPackets("救濟證書不足。");
					return;
				}
				pc.getInventory().consumeItem(3000049, 1);
				pc.getInventory().consumeItem(4100694, 1);
				long needExp = ExpTable.getNeedExpNextLevel(pc.getLevel());
				double PobyExp = needExp * 0.05;
				pc.add_exp((long) PobyExp);
				pc.set_exp_res(0);
			}
			break;
		case 951: // 允許聊天派對邀請嗎？ (Y/N)
			c = readC();
			L1PcInstance chatPc = (L1PcInstance) L1World.getInstance().findObject(pc.getPartyID());
			if (chatPc != null) {
				if (c == 0) { // No
					chatPc.sendPackets(String.valueOf(new S_ServerMessage(423, pc.getName()))); // %0 已
					pc.setPartyID(0);
				} else if (c == 1) { // Yes
					if (chatPc.isInChatParty()) {
						if (chatPc.getChatParty().isVacancy() || chatPc.isGm()) {
							chatPc.getChatParty().addMember(pc);
						} else {
							chatPc.sendPackets(String.valueOf(new S_ServerMessage(417))); // 更
						}
					} else {
						L1ChatParty chatParty = new L1ChatParty();
						chatParty.addMember(chatPc);
						chatParty.addMember(pc);
						
						//chatPc.sendPackets(new S_ServerMessage(424, pc.getName())); // %0 已
					}
				}
			}
			break;
		case 953: // 允許隊伍邀請嗎？ (Y/N)
		case 954:
			c = readC();
			L1PcInstance target = (L1PcInstance) L1World.getInstance().findObject(pc.getPartyID());
			if (target != null) {
				if(target.getMapId() == 621){
					target.sendPackets(String.valueOf(new S_SystemMessage("在該地圖中無法使用隊伍功能。")));
					return;
				}
				
				if (c == 0) { // No
					target.sendPackets(String.valueOf(new S_ServerMessage(423, pc.getName()))); // %0 已
					pc.setPartyID(0);
				} else if (c == 1) { // Yes
					/** 戰鬥區 **/
					if (target.getMapId() == 5153 || target.getMapId() == 5001 || pc.getMapId() == 5153
							|| pc.getMapId() == 5001) {
						target.sendPackets(String.valueOf(new S_ServerMessage(423, pc.getName()))); // %0 已拒絕邀請。
						return;
					}

					if (target.isInParty()) { // 邀請者在隊伍中
						if (target.getParty().isVacancy() || target.isGm()) { // 隊伍中有空位或邀請者是GM
							target.getParty().addMember(pc);
						} else { // 隊伍中無空位
							target.sendPackets(String.valueOf(new S_ServerMessage(417))); // 更多
						}
					} else { // 邀請者不在隊伍中
						L1Party party = new L1Party();
						party.addMember(target);
						party.addMember(pc);
						target.sendPackets(String.valueOf(new S_ServerMessage(424, pc.getName()))); // %0 已
					}
				}
			}
			break;
		case 2923: // 攻略
			c = readC();
			if (c == 0) {
				pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "龍之傳送門入口已取消。")));
			} else if (c == 1) {
				if (pc.DragonPortalLoc[0] != 0) {
					Collection<L1PcInstance> templist = L1World.getInstance().getAllPlayers();
					L1PcInstance[] list = templist.toArray(new L1PcInstance[templist.size()]);
					int count = 0;
					for (L1PcInstance player : list) {
						if (player == null)
							continue;
						if (player.getMapId() == pc.DragonPortalLoc[2]) {
							count += 1;
						}
					}
					if (count >= 32) {
						pc.sendPackets(String.valueOf(new S_ServerMessage(1536)));// 人數已滿，無法再
						return;
					}
					pc.start_teleport(pc.DragonPortalLoc[0], pc.DragonPortalLoc[1], pc.DragonPortalLoc[2], 5, 18339, true,
							false);
				}
			}
			pc.DragonPortalLoc[0] = 0;
			pc.DragonPortalLoc[1] = 0;
			pc.DragonPortalLoc[2] = 0;
			break;

		case 479: // 您想提升哪項屬性？(str, dex, int, con, wis, cha)
			if (readC() == 1) {
				String s = readS();
				try{
					pc.onStat(s);
				}catch(Exception e){
					System.out.println(s);
					e.printStackTrace();
					System.out.println(String.format("%s 嘗試超過屬性點數上限。", pc.getName()));
				}
			}
			break;

		default:
			break;
		}
	}

	private class UnifiedBuff implements Runnable {
		private L1PcInstance pc;

		public UnifiedBuff(L1PcInstance pc) {
			this.pc = pc;
		}

		private int[] allBuffSkill = { PHYSICAL_ENCHANT_DEX, PHYSICAL_ENCHANT_STR, BLESS_WEAPON, IRON_SKIN,
				FEATHER_BUFF_A, LIFE_MAAN, God_buff, COMA_B };

		@Override
		public void run() {
			for (int i = 0; i < allBuffSkill.length; i++) {
				new L1SkillUse().handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
			}
			pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), 4856)));
			pc.sendPackets(String.valueOf(new S_ChatPacket(String.valueOf(pc), "您已收到充滿希望與愛的增益效果。", 1)));
		}
	}

	private static void renamePet(L1PetInstance pet, String name) {
		if (pet == null || name == null) {
			throw new NullPointerException();
		}

		int petItemObjId = pet.getItemObjId();
		L1Pet petTemplate = PetTable.getInstance().getTemplate(petItemObjId);
		if (petTemplate == null) {
			throw new NullPointerException();
		}

		L1PcInstance pc = (L1PcInstance) pet.getMaster();
		if (PetTable.isNameExists(name)) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(327))); // 相同的名稱已經存在。
			return;
		}
		L1Npc l1npc = NpcTable.getInstance().getTemplate(pet.getNpcId());
		if (!(pet.getName().equalsIgnoreCase(l1npc.get_name()))) {
			L1ItemInstance removedItem = pc.getInventory().findItemId(410016);
			if(removedItem == null){
				pc.sendPackets(String.valueOf(new S_ServerMessage(326)));
				return;
			}else{
				pc.getInventory().removeItem(removedItem, 1);
			}
		}
		
		pet.setName(name);
		petTemplate.set_name(name);
		PetTable.getInstance().storePet(petTemplate); // 記錄到資料庫中
		L1ItemInstance item = pc.getInventory().getItem(pet.getItemObjId());
		pc.getInventory().updateItem(item);

		
		pc.sendPackets(new S_RemoveObject(pet));
		pc.broadcastPacket(new S_RemoveObject(pet));

		SC_WORLD_PUT_OBJECT_NOTI noti = SC_WORLD_PUT_OBJECT_NOTI.newInstance(pet);
		pc.broadcastPacket(noti, MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true, true);
		//pc.sendPackets(new S_PetPack(pet, pc));
		//pc.broadcastPacket(new S_PetPack(pet, pc));

		// pc.sendPackets(new S_ChangeName(pet.getId(), name));
		// pc.broadcastPacket(new S_ChangeName(pet.getId(), name));
	}

	private void callClan(L1PcInstance pc) {
		L1PcInstance callClanPc = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
		pc.setTempID(0);
		if (callClanPc == null) {
			return;
		}
		if (!pc.getMap().isEscapable() && !pc.isGm()) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(647)));
			pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, false, false);
			return;
		}
		if (pc.getId() != callClanPc.getCallClanId()) {
			return;
		}

		boolean isInWarArea = false;
		int castleId = L1CastleLocation.getCastleIdByArea(callClanPc);
		if (castleId != 0) {
			isInWarArea = true;
			if (MJCastleWarBusiness.getInstance().isNowWar(castleId)) {
				isInWarArea = false;
			}
		}
		short mapId = callClanPc.getMapId();
		if (mapId != 0 && mapId != 4 && mapId != 304 || isInWarArea) {
//			pc.sendPackets(new S_ServerMessage(547));
			pc.sendPackets("王族目前位於無法召喚的地點，法術失敗。");
			callClanPc.sendPackets("在王族的位置無法召喚成員。");
			return;
		}

		L1Map map = callClanPc.getMap();
		int callCalnX = callClanPc.getX();
		int callCalnY = callClanPc.getY();
		int locX = 0;
		int locY = 0;
		int heading = 0;
		switch (callClanPc.getCallClanHeading()) {
		case 0:
			locX = callCalnX;
			locY = callCalnY - 1;
			heading = 4;
			break;
		case 1:
			locX = callCalnX + 1;
			locY = callCalnY - 1;
			heading = 5;
			break;
		case 2:
			locX = callCalnX + 1;
			locY = callCalnY;
			heading = 6;
			break;
		case 3:
			locX = callCalnX + 1;
			locY = callCalnY + 1;
			heading = 7;
			break;
		case 4:
			locX = callCalnX;
			locY = callCalnY + 1;
			heading = 0;
			break;
		case 5:
			locX = callCalnX - 1;
			locY = callCalnY + 1;
			heading = 1;
			break;
		case 6:
			locX = callCalnX - 1;
			locY = callCalnY;
			heading = 2;
			break;
		case 7:
			locX = callCalnX - 1;
			locY = callCalnY - 1;
			heading = 3;
			break;
		default:
			break;
		}

		boolean isExistCharacter = false;
		L1Character cha = null;
		for (L1Object object : L1World.getInstance().getVisibleObjects(callClanPc, 1)) {
			if (object instanceof L1Character) {
				cha = (L1Character) object;
				if (cha.getX() == locX && cha.getY() == locY && cha.getMapId() == mapId) {
					isExistCharacter = true;
					break;
				}
			}
		}

		if (locX == 0 && locY == 0 || !map.isPassable(locX, locY) || isExistCharacter) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(627)));
			return;
		}
		pc.start_teleport(locX, locY, mapId, heading, 2235, true, false);
	}

	@Override
	public String getType() {
		return C_ATTR;
	}
}