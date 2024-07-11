package l1j.server.MJKDASystem;

import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
import l1j.server.Config;
/**********************************
 * 
 * MJ Kill Death Assist Object.
 * made by mjsoft, 2017.
 *  
 **********************************/
import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJTemplate.Chain.KillChain.MJCharacterKillChain;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.server.GMCommands;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ShowCmd;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.MJBytesOutputStream;

public class MJKDA {
	private static final int KDAM_INIT = 0;
	private static final int KDAM_WP_INC = 1;

	private static final int ASSASSINATION_LEVEL_1 = 1;
	private static final int ASSASSINATION_LEVEL_2 = 2;

	private static ServerBasePacket[] _kdaMessages = new ServerBasePacket[] {
		new S_SystemMessage("您的殺死比已經重置。"),
		new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "您的攻城點數增加了1點。(PK 勝利)"),
		new S_SystemMessage("由於當前處於保護模式，懲罰不會被應用。請放心狩獵。"),

	public int objid;
	public String name;
	public int kill;
	public int death;
	public long lastDeathMs;
	public long lastKillMs;
	public int lastKillId;
	public boolean isBot;
	public boolean isChartView;
	public int assassination_level;

	public void onInit(L1PcInstance owner) {
		kill = 0;
		death = 0;
		owner.sendPackets(_kdaMessages[KDAM_INIT], false);
		for (int i = assassination_level - 1; i >= 0; --i) {
			int skill_id = L1SkillId.ASSASSNATIONS[i];
			stop_assassination(owner, skill_id);
		}
		assassination_level = 0;
	}

	public void onProtection(L1PcInstance pc) {
		pc.sendPackets(_kdaMessages[KDAM_INIT], false);
	}

	public void onKill(L1PcInstance killer, L1PcInstance victim) {
		if (killer.getZoneType() != 0)
			return;

		if (GMCommands.IS_PROTECTION) {
			onProtection(victim);
			return;
		}

		MJBotAI ai = killer.getAI();
		if (ai == null) {
			if (killer.getClanid() != 0) {
				L1Clan clan = killer.getClan();
				if (clan != null) {
					clan.incWarPoint();
					killer.sendPackets(_kdaMessages[KDAM_WP_INC], false);
				}
			}
		} else {
			if (ai.getBotType() == MJBotType.REDKNIGHT || ai.getBotType() == MJBotType.PROTECTOR)
				return;
		}

		killer.sendPackets(new S_PacketBox(S_PacketBox.배틀샷, victim.getId()));
		MJKDA tKda = victim.getKDA();
		MJKDA oKda = killer.getKDA();
		if (oKda == null || tKda == null)
			return;

		MJCharacterKillChain.getInstance().on_kill(killer, victim);
		long cur = System.currentTimeMillis();
		try {
			long diff = cur - tKda.lastDeathMs;
			if (diff < MJKDALoadManager.KDA_DEATH_DELAY_MS) {
				killer.sendPackets(new S_SystemMessage(String.format("[%s] 角色在短時間內死亡次數過多，不給予點數。", victim.getName())));
				return;
			} else if (tKda.objid == oKda.lastKillId && diff < MJKDALoadManager.KDA_KILL_DUPL_DELAY_MS) {
				killer.sendPackets(new S_SystemMessage(String.format("[%s] 角色在短時間內被過多擊殺，不給予點數。", victim.getName())));
				return;
			}

			tKda.death++;
			oKda.kill++;
			oKda.do_assassination(killer);
			MJKDALoadManager.KDA_TOTAL_PVP++;
			/*
			 * onProvideHuntPrice(killer, victim); notifyKill(killer, victim);
			 */
			if (!victim.isInWarAreaAndWarTime(victim, killer))
				setLawful(killer, victim);

			if (killer.getMapId() == 1708 || killer.getMapId() == 1709) {
				notifyKillForGotten(killer, victim);
				setLawful(killer, victim);
			} else {
				onProvideHuntPrice(killer, victim);
				// 使用TODO PVP訊息傳輸時取消註釋
				notifyKill(killer, victim);
				if (!victim.isInWarAreaAndWarTime(victim, killer))
					setLawful(killer, victim);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tKda.lastDeathMs = cur;
			oKda.lastKillMs = cur;
			oKda.lastKillId = tKda.objid;
		}
	}

	public boolean is_assassination_level2() {
		return (assassination_level & ASSASSINATION_LEVEL_2) > 0;
	}

	public boolean is_assassination_level1() {
		return (assassination_level & ASSASSINATION_LEVEL_1) > 0;
	}

	public boolean is_assassination_level0() {
		return assassination_level == 0;
	}

	public void do_assassination(L1PcInstance owner) {
		if (is_assassination_level2())
			return;

		// TODO 防止無節制的 PVP
		if (kill == Config.ServerAdSetting.KillCount1) {
			owner.start_teleport(32787, 32846, 666, owner.getHeading(), 18339, true, false);
			owner.sendPackets("\f3由於無節制的 PVP，您將被困在地獄中 [60 分鐘]。");
		} else if (kill == Config.ServerAdSetting.KillCount2) {
			owner.start_teleport(32787, 32846, 666, owner.getHeading(), 18339, true, false);
			owner.sendPackets("\f3由於無節制的 PVP，您將被困在地獄中 [60 分鐘]。");
		}

		// TODO 暗殺系統
		if (kill >= Config.ServerAdSetting.AssassinationLevel1KillCount) {
			if (is_assassination_level1())
				stop_assassination(owner, L1SkillId.ASSASSINATION_LEVEL_1);
			send_assassination(owner, L1SkillId.ASSASSINATION_LEVEL_2);
			assassination_level = ASSASSINATION_LEVEL_2;
		} else if (kill >= Config.ServerAdSetting.AssassinationLevel1KillCount) {
			if (is_assassination_level1())
				return;

			send_assassination(owner, L1SkillId.ASSASSINATION_LEVEL_1);
			assassination_level = ASSASSINATION_LEVEL_1;
		}
	}

	// TODO 求購 金幣 系統回饋零件
	private void onProvideHuntPrice(L1PcInstance killer, L1PcInstance victim) {
		int price1 = Config.ServerAdSetting.WANTED_ADENA_CONSUME[0] * 8 / 10;
		int price2 = Config.ServerAdSetting.WANTED_ADENA_CONSUME[1] * 8 / 10;
		int price3 = Config.ServerAdSetting.WANTED_ADENA_CONSUME[2] * 8 / 10;
		if (victim.getClanid() == killer.getClanid()) {
			return;
		}
		if (victim.hasSkillEffect(L1SkillId.USER_WANTED1)) {
			killer.getInventory().storeItem(L1ItemId.ADENA, price1);
			victim.removeSkillEffect(L1SkillId.USER_WANTED1);
			victim.doWanted(false, false);
			victim.set_Wanted_Level(0);
		} else if (victim.hasSkillEffect(L1SkillId.USER_WANTED2)) {
			killer.getInventory().storeItem(L1ItemId.ADENA, price2);
			victim.removeSkillEffect(L1SkillId.USER_WANTED2);
			victim.doWanted(false, false);
			victim.set_Wanted_Level(0);
		} else if (victim.hasSkillEffect(L1SkillId.USER_WANTED3)) {
			killer.getInventory().storeItem(L1ItemId.ADENA, price3);
			victim.removeSkillEffect(L1SkillId.USER_WANTED3);
			victim.doWanted(false, false);
			victim.set_Wanted_Level(0);
		}
	}

	public void notifyKill(L1PcInstance killer, L1PcInstance victim) {
		MJShiftBattleCharacterInfo killer_info = killer.get_battle_info();
		MJShiftBattleCharacterInfo victim_info = victim.get_battle_info();
		String killer_name = killer.is_shift_battle() ? "未知人" : killer.getName();
		String victim_name = victim.is_shift_battle() ? "未知人" : victim.getName();
		if (killer_info != null) {
			killer_name = killer_info.to_name_pair();
		}
		if (victim_info != null) {
			victim_name = victim_info.to_name_pair();
		}

		ServerBasePacket[] pcks = new ServerBasePacket[] {
				new S_SystemMessage(String.format("PVP: [%s]的攻擊使得 [%s] 死亡。", killer_name, victim_name)),
				new S_ChatPacket(String.format("[PVP位置] %s", MapsTable.getInstance().getMapName(killer.getMapId())),
						Opcodes.S_MESSAGE),
		};
		L1World.getInstance().broadcastPacketToAll(pcks, true);
	}

	public void notifyKillForGotten(L1PcInstance killer, L1PcInstance victim) {
		MJShiftBattleCharacterInfo killer_info = killer.get_battle_info();
		MJShiftBattleCharacterInfo victim_info = victim.get_battle_info();
		String killer_name = killer.is_shift_battle() ? "未知人" : killer.getName();
		String victim_name = victim.is_shift_battle() ? "未知人" : victim.getName();
		if (killer_info != null) {
			killer_name = killer_info.to_name_pair();
		}
		if (victim_info != null) {
			victim_name = victim_info.to_name_pair();
		}

		int cid = victim.getClanid();
		if (cid == 0) {
			ServerBasePacket nonEqualsPck = S_ShowCmd.getPkMessageAtBattleServer(killer_name, victim_name);
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null)
					continue;
				pc.sendPackets(nonEqualsPck, false);
			}
			return;
		}

		ServerBasePacket equalsPck = S_ShowCmd.getPkMessageAtBattleServer(String.format("\\aG%s", killer_name),
				victim_name);
		ServerBasePacket nonEqualsPck = S_ShowCmd.getPkMessageAtBattleServer(killer_name, victim_name);
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null)
				continue;
			if (pc.getClanid() == cid)
				pc.sendPackets(equalsPck, false);
			else
				pc.sendPackets(nonEqualsPck, false);
		}
	}

	private void setLawful(L1PcInstance killer, L1PcInstance victim) {
		int kLaw = killer.getLawful();
		int vLaw = victim.getLawful();
		if (vLaw >= 0 && !victim.isPinkName()) {
			if (kLaw < 30000) {
				killer.set_PKcount(killer.get_PKcount() + 1);
				killer.setLastPk();
			}

			int nLaw = calculate_lawful(killer);
			killer.setLawful(nLaw);
			killer.send_lawful();
		} else
			victim.setPinkName(false);
	}

	public static int calculate_lawful(L1PcInstance killer) { // 23-03-21 改變
		int nLaw = 0;
		nLaw = killer.getLawful();
		/*
		 * if (killer.getLevel() < 50)
		 * nLaw = -1 * (int) ((Math.pow(killer.getLevel(), 2) * 4));
		 * else
		 * nLaw = -1 * (int) ((Math.pow(killer.getLevel(), 3) * 0.08));
		 * 
		 * return Math.max(nLaw, -32768);
		 */
		return nLaw - 30000 < -32767 ? -32767 : nLaw - 32767;
	}

	public byte[] serialize() throws Exception {
		MJBytesOutputStream bos = new MJBytesOutputStream(32);
		bos.write(0x0A);
		bos.writeS2(name);
		bos.write(0x10);
		bos.writeBit(kill);
		byte[] data = bos.toArray();
		bos.close();
		bos.dispose();
		bos = null;
		return data;
	}

	public static void stop_assassination(L1PcInstance pc, int skill_id) {
		if (pc == null)
			return;

		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.END);
		noti.set_spell_id(skill_id);
		noti.set_off_icon_id(skill_id);
		noti.set_end_str_id(0);
		noti.set_is_good(true);
		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
	}

	public static void send_assassination(L1PcInstance pc, int skill_id) {
		if (pc == null)
			return;

		int str_id = skill_id == L1SkillId.ASSASSINATION_LEVEL_1 ? 5192 : 5193;
		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_noti_type(eNotiType.RESTAT);
		noti.set_spell_id(skill_id);
		noti.set_duration_show_type(eDurationShowType.TYPE_EFF_UNLIMIT);
		noti.set_on_icon_id(skill_id);
		noti.set_icon_priority(10);
		noti.set_tooltip_str_id(str_id);
		noti.set_new_str_id(str_id);
		noti.set_is_good(true);
		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
	}
}
