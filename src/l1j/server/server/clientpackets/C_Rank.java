package l1j.server.server.server.clientpackets;

import java.util.Collection;

import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_ALLY_LIST;
import l1j.server.MJWarSystem.MJCastleWarBusiness;

import l1j.server.server.server.datatables.CharacterTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.server.serverpackets.S_SurvivalCry;
import l1j.server.server.serverpackets.S_SystemMessage;

public class C_Rank extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_RANK = "[C] C_Rank";

	private L1ItemInstance weapon;

	public C_Rank(byte abyte0[], GameClient clientthread) throws Exception {
		super(abyte0);

		int type = readC();

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		int castle_id = MJCastleWarBusiness.getInstance().NowCastleWarState();
		boolean is_war = MJCastleWarBusiness.getInstance().isNowWar(castle_id);
		//TODO 加入空指針
		if(clan == null) {
			return;
		}
		switch (type) {
			case 1: // 階級
				int rank = readC();
				String name = readS();
				L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
				if ((!pc.isCrown()) && (pc.getClanRank() != L1Clan.GUARDIAN) && (pc.getClanRank() != L1Clan.SUBMONARCH)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("階級授予失敗：沒有授予階級的權限。")));
					return;
				}
			
			if (targetPc != null) {
				if (pc.getClanid() == targetPc.getClanid()) {
					try {
						if ((pc.getClanRank() != L1Clan.MONARCH) && (pc.getClanRank() != L1Clan.GUARDIAN) && (pc.getClanRank() != L1Clan.SUBMONARCH)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("階級授予失敗：沒有授予階級的權限。")));
							return;
						}
						if ((targetPc.isCrown()) && (targetPc.getId() == targetPc.getClan().getLeaderId())) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("階級授予失敗：目標是血盟的君主")));
							return;
						}
						if ((pc.getClanRank() == L1Clan.SUBMONARCH) && (rank == 3)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("階級授予失敗：授予的階級等於或高於您的階級")));
							return;
						}
						if ((pc.getClanRank() == L1Clan.GUARDIAN) && (rank == 9)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("階級授予失敗：授予的階級等於或高於您的階級")));
							return;
						}
						if ((pc.getClanRank() == L1Clan.GUARDIAN)
								&& ((targetPc.getClanRank() == L1Clan.MONARCH) || (targetPc.getClanRank() == L1Clan.GUARDIAN) || (targetPc.getClanRank() == L1Clan.SUBMONARCH))) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("階級授予失敗：目標目前的階級等於或高於您的階級")));
							return;
						}
						targetPc.setClanRank(rank);
						targetPc.save(); // 將角色信息寫入數據庫
						pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, rank, name)); // 給君主發送
						clan.UpdataClanMember(targetPc.getName(), targetPc.getClanRank());
						String rankString = "一般";
						if (rank == 7) {
							rankString = "訓練";
						} else if (rank == 8) {
							rankString = "一般";
						} else if (rank == 9) {
							rankString = "守護騎士";
						} else if (rank == 13) {
							rankString = "精銳";
						} else if (rank == 14) {
							rankString = "副君主";
						}
						targetPc.sendPackets(String.valueOf(new S_SystemMessage("階級: " + rankString + " (으)로階級任命")));
						targetPc.sendPackets(new S_ACTION_UI2(targetPc, S_ACTION_UI2.CLAN_RANK));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					pc.sendPackets(String.valueOf(new S_SystemMessage("不是同一血盟成員。")));
					return;
				}
			} else {
				L1PcInstance restorePc = CharacterTable.getInstance().restoreCharacter(name);
				if ((restorePc != null) && (restorePc.getClanid() == pc.getClanid())) {
					try {
						if ((restorePc.isCrown()) && (restorePc.getId() == restorePc.getClan().getLeaderId())) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("階級授予失敗：目標是血盟的君主。")));
							return;
						}
						if ((pc.getClanRank() != L1Clan.MONARCH) && (pc.getClanRank() != L1Clan.GUARDIAN) && (pc.getClanRank() != L1Clan.SUBMONARCH)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("階級授予失敗：沒有授予階級的權限。")));
							return;
						}
						if ((pc.getClanRank() == L1Clan.SUBMONARCH) && (rank == 3)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("階級授予失敗：授予的階級等於或高於您的階級")));
							return;
						}
						if ((pc.getClanRank() == L1Clan.GUARDIAN) && (rank == 9)) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("階級授予失敗：授予的階級等於或高於您的階級")));
							return;
						}
						if ((pc.getClanRank() == L1Clan.GUARDIAN) && ((restorePc.getClanRank() == L1Clan.MONARCH) || (restorePc.getClanRank() == L1Clan.GUARDIAN)
								|| (restorePc.getClanRank() == L1Clan.SUBMONARCH))) {
							pc.sendPackets(String.valueOf(new S_SystemMessage("階級授予失敗：目標目前的階級等於或高於您的階級")));
							return;
						}
						restorePc.setClanRank(rank);
						restorePc.save(); // 將角色信息寫入數據庫
						restorePc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, rank, name));
						pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, rank, name));
						clan.UpdataClanMember(restorePc.getName(), restorePc.getClanRank());
						String rankString = "一般";
						if (rank == 7)
							rankString = "訓練";
						else if (rank == 3)
							rankString = "副君主";
						else if (rank == 8)
							rankString = "一般";
						else if (rank == 9)
							rankString = "守護騎士";
						else if (rank == 13)
							rankString = "精銳";
						for (L1PcInstance mem : clan.getOnlineClanMember()) {
							mem.sendPackets(String.valueOf(new S_SystemMessage(restorePc.getName() + " 的階級已變更為 " + rankString + "。")));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					pc.sendPackets(2069);
					return;
				}
				restorePc = null;
			}
			break;
			case 2: // 清單
				try {
					if (clan.AllianceSize() > 0) {
						SC_BLOOD_PLEDGE_ALLY_LIST.ally_list_send(pc);
					}
				} catch (Exception e) {
				} // 無錯誤
				break;
			case 3: // 加入同盟
				String target_clan_name = readS();
				L1Clan target_clan = L1World.getInstance().findClan(target_clan_name);
				if (target_clan == null) {
					pc.sendPackets("不存在的血盟。");
					return;
				}
			L1PcInstance allianceLeader = L1World.getInstance().findpc(target_clan.getLeaderName());
			if (allianceLeader == null) {
				pc.sendPackets(218, target_clan.getClanName());
				return;
			}
			
			if (is_war) {
				pc.sendPackets(1234);
			}

				if (pc.getLevel() < 25 || !pc.isCrown()) {
					pc.sendPackets(1206); // 只有25級以上的血盟君主才可以申請同盟
					return;
				}
				if (pc.getClan().AllianceSize() != 0) {
					pc.sendPackets(1202); // 已經加入同盟的狀態。
					return;
				}
				if (clan.AllianceSize() > 4) {
					pc.sendPackets("同盟最多只能有4個血盟。");
					return;
				}

				if (clan.getCurrentWar() != null) {
					pc.sendPackets(1234); // 戰爭中無法加入同盟
					return;
				}

			L1Clan leader_clan = allianceLeader.getClan();
			if (allianceLeader != null) {
				if (!leader_clan.isAlliance_leader() && allianceLeader.isCrown() && leader_clan.AllianceSize() > 0) {
					pc.sendPackets("只能加入同盟主血盟。");
					return;
				}

				if (leader_clan.AllianceSize() > 4 && allianceLeader.isCrown()) {
					pc.sendPackets("對方血盟的同盟已經有4個以上的血盟。");
					return;
				}

				if (allianceLeader.getLevel() > 24 && allianceLeader.isCrown()) {
					allianceLeader.setTempID(pc.getId());
					allianceLeader.sendPackets(String.valueOf(new S_Message_YN(223, pc.getName())));
				} else {
					pc.sendPackets(1201); // 無法加入同盟。
				}
			}
				break;
			case 4: // 退出同盟
				if (is_war) {
					pc.sendPackets("同盟：攻城戰期間無法退出同盟。");
				}

				if (clan.getCurrentWar() != null) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1203))); // 戰爭中無法退出同盟。
					return;
				}

				if (clan.AllianceSize() > 0) {
					pc.sendPackets(String.valueOf(new S_Message_YN(1210, ""))); // 真的要退出同盟嗎？
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1233))); // 沒有同盟。
				}
			break;
			case 5: // 生存的吶喊 (CTRL + E)
//            if (pc.getWeapon() == null) {
//                pc.sendPackets(new S_ServerMessage(1973));
//                // 必須裝備武器才能使用。
//                return;
//            }
			if (pc.get_food() >= 225) {
				int addHp = 0;
				int gfxId1 = 0;
				int gfxId2 = 0;
				long curTime = System.currentTimeMillis() / 1000;
				int fullTime = (int) ((curTime - pc.getCryOfSurvivalTime()) / 60);
				if (fullTime < 180) {//60->100
					long time = (pc.getCryOfSurvivalTime() + (60 * 180)) - curTime;//60->100
					// 生存的吶喊：等待中
					pc.sendPackets(String.valueOf(new S_SystemMessage("生存的吶喊：等待時間 (" + (time / 60) + "分鐘 " + (time % 60) + "秒)")));
					return;
				}

				if (pc.getLevel() >= 1 && pc.getLevel() <= 87) {
					gfxId1 = 19286;
					gfxId2 = 8910;
					addHp = 800;
				} else if (pc.getLevel() >= 88 && pc.getLevel() <= 89) {
					gfxId1 = 19288;
					gfxId2 = 8908;
					addHp = 1000;
				} else if (pc.getLevel() >= 90 && pc.getLevel() <= 92) {
					gfxId1 = 19290;
					gfxId2 = 8908;
					addHp = 1200;
				} else if (pc.getLevel() >= 93 && pc.getLevel() <= 94) {
					gfxId1 = 19292;
					gfxId2 = 8908;
					addHp = 1300;
				} else if (pc.getLevel() >= 95 && pc.getLevel() <= 127) {
					gfxId1 = 19294;
					gfxId2 = 8908;
					addHp = 1500;
				}
				
				S_SkillSound sound = new S_SkillSound(pc.getId(), gfxId1);
				pc.sendPackets(sound);
				Broadcaster.broadcastPacket(pc, sound);
				sound = new S_SkillSound(pc.getId(), gfxId2);
				pc.sendPackets(sound);
				Broadcaster.broadcastPacket(pc, sound);
				pc.setCryOfSurvivalTime();
				pc.set_food(0);
				pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, 0));
				pc.setCurrentHp(pc.getCurrentHp() + addHp);
				pc.sendPackets(new S_SurvivalCry(60 * 180));//100분
			} else {
				pc.sendPackets(String.valueOf(new S_ServerMessage(3461)));
			}
			break;
			case 6: // 武器炫耀 Alt + 0 (數字)
			if (pc.getWeapon() == null) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(1973)));
				return;
			}
			int gfx3 = 0;
			weapon = pc.getWeapon();
			int EnchantLevel2 = weapon.getEnchantLevel();
			if (EnchantLevel2 < 0) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(79)));
				return;
			} else if (EnchantLevel2 >= 0 && EnchantLevel2 <= 6) {
				gfx3 = 8684;
			} else if (EnchantLevel2 >= 7 && EnchantLevel2 <= 8) {
				gfx3 = 8685;
			} else if (EnchantLevel2 >= 9 && EnchantLevel2 <= 10) {
				gfx3 = 8773;
			} else if (EnchantLevel2 >= 11) {
				gfx3 = 8686;
			}
			pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), gfx3)));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), gfx3));
			break;
		case 8:
			send_dungeon_remains(pc, pc.get_account_progresses());
			send_dungeon_remains(pc, pc.get_character_progresses());
			break;
			case 9: // 同盟驅逐
				int kick_clanId = readD();
				if (is_war) {
					pc.sendPackets("同盟：攻城戰期間無法驅逐同盟。");
				}

				if (clan.getCurrentWar() != null) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1235))); // 戰爭中無法退出同盟。
					return;
				}
			if (clan.AllianceSize() > 0) {
				L1Clan kick_clan = L1World.getInstance().getClan(kick_clanId);
				if (kick_clan != null) {
					pc.setTempID(kick_clanId);
					pc.sendPackets(String.valueOf(new S_Message_YN(C_Attr.MSGCODE_6008_CLAN_ALLIANCE_KICK, 6008, String.format("是否要將 %s 血盟從同盟中驅逐?", kick_clan.getClanName()))));
				} else {
					pc.sendPackets("同盟：要驅逐的血盟不存在。");
				}
			} else {
				pc.sendPackets(String.valueOf(new S_ServerMessage(1233))); // 沒有同盟。
			}
				break;
			case 10: // 解散同盟
				if (is_war) {
					pc.sendPackets("同盟：攻城戰期間無法解散同盟。");
				}

				if (clan.getCurrentWar() != null) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1235))); // 戰爭中無法退出同盟。
					return;
				}

				if (clan.AllianceSize() > 0) {
					pc.sendPackets(String.valueOf(new S_Message_YN(5460, ""))); // 真的要解散同盟嗎？
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(1233))); // 沒有同盟。
				}
			break;
		default:
			break;
		}
	}

	private void send_dungeon_remains(L1PcInstance pc, Collection<DungeonTimeProgress<?>> progresses) {
		DungeonTimeInformationLoader loader = DungeonTimeInformationLoader.getInstance();
		for (DungeonTimeProgress<?> progress : progresses) {
			DungeonTimeInformation dtInfo = loader.from_timer_id(progress.get_timer_id());
			pc.sendPackets(new S_ServerMessage(2535, dtInfo.get_description(), String.valueOf((progress.get_remain_seconds() / 60))));
		}
	}

	@Override
	public String getType() {
		return C_RANK;
	}
}
