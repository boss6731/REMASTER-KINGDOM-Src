package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ManagerInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.ClanWarehouse;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.utils.SQLUtil;

public class ClanTable {
	private static Logger _log = Logger.getLogger(ClanTable.class.getName());

	private static ClanTable _instance;

	private final HashMap<Integer, L1Clan> _clans = new HashMap<Integer, L1Clan>();
	private final HashMap<Integer, L1Clan> _redknights = new HashMap<Integer, L1Clan>(6);

	public static final String CLAN_TUTORIAL_ANN = "自動退出等級: "+ Config.ServerAdSetting.NEWPLAYERLEVELPURGE +"\r\n\r\n濫用時將嚴格處罰。\r\n\r\n髒話者將被永久驅逐。";
	public static final String CLAN_TUTORIAL_INTRO = "這是一個新血盟。";
	public static final int CLAN_TUTORIAL_EMB = Config.ServerAdSetting.CLANEMBPROTECTION;
	public int TempleantiqueclanId = 0;
	private static boolean loadedWarehouse = false;
	private static void onLoadWarehouse(){
		if (loadedWarehouse) {
			return;
		}
		loadedWarehouse = true;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
			clanWarehouse.loadItems();
		}
	}

	public static ClanTable getInstance() {
		if (_instance == null) {
			_instance = new ClanTable();
		}
		return _instance;
	}

	private static L1Clan createRedKnight(int castleId){
		L1Clan clan = new L1Clan();
		clan.setClanId(IdFactory.getInstance().nextId());
		clan.setClanName("紅色騎士團");
		clan.setCastleId(castleId);
		clan.setRedKnight(true);
		L1World.getInstance().storeClan(clan);
		return clan;
	}
	
	private ClanTable() {
		for(int i=1; i<=8; ++i){
			L1Clan clan = createRedKnight(i);
			_redknights.put(i, clan);
		}
		
		{
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;

			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT * FROM clan_data ORDER BY clan_id");

				rs = pstm.executeQuery();
				L1Clan clan = null;
				while (rs.next()) {					
					clan = new L1Clan();
					int clan_id = rs.getInt(1);
					int castle_id = rs.getInt("hascastle");
					clan.setClanId(clan_id);
					clan.setClanName(rs.getString("clan_name"));
					clan.setLeaderId(rs.getInt("leader_id"));
					clan.setLeaderName(rs.getString("leader_name"));
					clan.setCastleId(castle_id);
					clan.setHouseId(rs.getInt("hashouse"));
					clan.setAllianceList(rs.getString("alliance"));
					clan.setLoad_Alliance_leader(rs.getString("alliance_leader"));
					clan.setClanBirthDay(rs.getTimestamp("clan_birthday"));
					/** 血盟自動加入 */
					clan.setBot(rs.getString("bot").equalsIgnoreCase("true"));
					clan.setBotStyle(rs.getInt("bot_style"));
					clan.setBotLevel(rs.getInt("bot_level"));
					/** 血盟自動加入 */
					clan.setOnlineMaxUser(rs.getInt("max_online_user"));
					clan.setAnnouncement(rs.getString("announcement"));
					clan.setIntroduction(rs.getString("introduction"));
					clan.setEmblemId(rs.getInt("emblem_id"));
					clan.setEmblemStatus(rs.getInt("emblem_status"));
					//	clan.setClanExp(rs.getInt(16));
					clan.setBless(rs.getInt("bless"));
					clan.setBlessCount(rs.getInt("bless_count"));
					clan.setBuffTime(rs.getInt("attack"), rs.getInt("defence"), rs.getInt("pvpattack"), rs.getInt("pvpdefence"));

					/** 2016.11.25 MJ 應用中心 血盟 **/
					clan.setJoinSetting(rs.getInt("join_setting"));
					clan.setJoinType(rs.getInt("join_type"));
					clan.setJoinPassword(rs.getString("join_password"));
					clan.setWarPoint(rs.getInt("War_point"));
					/** 2016.11.25 MJ 應用中心 血盟 **/

					// TODO 血盟祝福消耗
					clan.setEinhasadBlessBuff(rs.getInt("EinhasadBlessBuff"));
					clan.setBuffFirst(rs.getInt("Buff_List1"));
					clan.setBuffSecond(rs.getInt("Buff_List2"));
					clan.setBuffThird(rs.getInt("Buff_List3"));
					clan.setCastleDate(rs.getTimestamp("Castle_hasdate"));
					clan.setContribution(rs.getInt("contribution"));
					
					clan.setEntranceNotice(rs.getString("noticement"));
					L1World.getInstance().storeClan(clan);
					
					_clans.put(clan_id, clan);
				}

			} catch (Exception e) {
				_log.log(Level.SEVERE, "ClanTable[]Error", e);
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
		}

		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;

			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT char_name, ClanRank, level, notes, objid, Type FROM characters WHERE ClanID = ?");
				pstm.setInt(1, clan.getClanId());
				rs = pstm.executeQuery();

				while (rs.next()) {
					String name = rs.getString("char_name");
					int rank = rs.getInt("ClanRank");
					int level = rs.getInt("level");
					String notes = rs.getString("notes");
					int memberId = rs.getInt("objid");
					int type = rs.getInt("Type");
					clan.addClanMember(name, rank, level, notes, memberId, type, 0, null);
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, "ClanTable[]Error1", e);
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
		}
		onLoadWarehouse();
	}

	public L1Clan createTutorialClan(){ 
		L1ManagerInstance manager = L1ManagerInstance.getInstance();
		
		if(!Config.ServerAdSetting.TUTORIALCLAN)
			return null;
		
		if(manager.getClanid() != 0){
			System.out.println("已經有新建立的血盟。");
			return null;
		}
		
		L1Clan clan = L1World.getInstance().findClan(Config.ServerAdSetting.NEWCLANNAME);
		if(clan != null){
			manager.setClanid(clan.getClanId());
			manager.setClanname(clan.getClanName());
			manager.setClanRank(L1Clan.Lord);
			clan.addClanMember(manager.getName(), manager.getClanRank(), manager.getLevel(), "", manager.getId(), manager.getType(), manager.getOnlineStatus(), manager);
			return clan;
		}
		
		clan = new L1Clan();
		clan.setClanId(IdFactory.getInstance().nextId());
		clan.setClanName(Config.ServerAdSetting.NEWCLANNAME);
		clan.setLeaderId(manager.getId());
		clan.setLeaderName("Metis");
		clan.setCastleId(0);
		clan.setHouseId(0);
		clan.setAllianceList(null);
		clan.setAlliance_leader(false);
		clan.setClanBirthDay(new Timestamp(System.currentTimeMillis()));
		clan.setAnnouncement(CLAN_TUTORIAL_ANN);
		clan.setIntroduction(CLAN_TUTORIAL_INTRO);
		clan.setEmblemId(CLAN_TUTORIAL_EMB);
		clan.setEmblemStatus(1);
		clan.setBless(0);
		clan.setBlessCount(0);
		clan.setBuffTime(0, 0, 0, 0);
		clan.setJoinSetting(1);
		clan.setJoinType(0);
		clan.setBot(false);
		clan.setContribution(0);
		
		Connection con = null;
		PreparedStatement pstm = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_data SET clan_id=?, clan_name=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, alliance=?, alliance_leader=?, clan_birthday=?, max_online_user=?, announcement=?, introduction=?, emblem_id=?, emblem_status=?,clan_exp=?,bless=?,bless_count=?,attack=?,defence=?,pvpattack=?,pvpdefence=?,join_setting=?,join_type=?,EinhasadBlessBuff=?,Buff_List1=?,Buff_List2=?,Buff_List3=?,contribution=?");
			pstm.setInt(1, clan.getClanId());
			pstm.setString(2, clan.getClanName());
			pstm.setInt(3, clan.getLeaderId());
			pstm.setString(4, clan.getLeaderName());
			pstm.setInt(5, clan.getCastleId());
			pstm.setInt(6, clan.getHouseId());
			pstm.setString(7, clan.getAllianceList()!=null ? clan.getAllianceList().toString() : "");
			pstm.setString(8, clan.isAlliance_leader() ? "true" : "false");
			pstm.setTimestamp(9, clan.getClanBirthDay());
			pstm.setInt(10, clan.getOnlineMaxUser());
			pstm.setString(11, "");
			pstm.setString(12, "");
			pstm.setInt(13, 0);
			pstm.setInt(14, 0);
			pstm.setInt(15, 0);
			pstm.setInt(16, 0);
			pstm.setInt(17, 0);
			pstm.setInt(18, 0);
			pstm.setInt(19, 0);
			pstm.setInt(20, 0);
			pstm.setInt(21, 0);
			pstm.setInt(22, clan.getJoinSetting());
			pstm.setInt(23, clan.getJoinType());
			pstm.setInt(24, clan.getEinhasadBlessBuff());
			pstm.setInt(25, clan.getBuffFirst());
			pstm.setInt(26, clan.getBuffSecond());
			pstm.setInt(27, clan.getBuffThird());
			pstm.setInt(28, 0);
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, "ClanTable[]Error2", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		L1World.getInstance().storeClan(clan);
		_clans.put(clan.getClanId(), clan);

		manager.setClanid(clan.getClanId());
		manager.setClanname(clan.getClanName());
		manager.setClanRank(L1Clan.Prince);
		clan.addClanMember(manager.getName(), manager.getClanRank(), manager.getLevel(), "", manager.getId(), manager.getType(), manager.getOnlineStatus(), manager);
		return clan;
	}
	
	public L1Clan createClan(L1PcInstance player, String clan_name, boolean isBot) {
		for (L1Clan oldClans : L1World.getInstance().getAllClans()) {
			if (oldClans.getClanName().equalsIgnoreCase(clan_name)) {
				return null;
			}
		}
		Timestamp time = new Timestamp(System.currentTimeMillis());
		L1Clan clan = new L1Clan();
		clan.setClanId(IdFactory.getInstance().nextId());
		clan.setClanName(clan_name);
		clan.setLeaderId(player.getId());
		clan.setLeaderName(player.getName());
		clan.setCastleId(0);
		clan.setHouseId(0);
		clan.setAllianceList(null);
		clan.setAlliance_leader(false);
		clan.setClanBirthDay(time);
		clan.setAnnouncement("");
		clan.setIntroduction("");
		clan.setEmblemId(0);
		clan.setEmblemStatus(0);
		clan.setBless(0);
		clan.setBlessCount(0);
		clan.setBuffTime(0, 0, 0, 0);
		clan.setJoinSetting(1);
		clan.setJoinType(1);
		clan.setBot(isBot);
		clan.setContribution(0);
		clan.setEntranceNotice("");
		
		Connection con = null;
		PreparedStatement pstm = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_data SET clan_id=?, clan_name=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, alliance=?, alliance_leader=?, clan_birthday=?, max_online_user=?, announcement=?, introduction=?, emblem_id=?, emblem_status=?,clan_exp=?,bless=?,bless_count=?,attack=?,defence=?,pvpattack=?,pvpdefence=?,join_setting=?,join_type=?,bot=?,EinhasadBlessBuff=?,Buff_List1=?,Buff_List2=?,Buff_List3=?,contribution=?");
			pstm.setInt(1, clan.getClanId());
			pstm.setString(2, clan.getClanName());
			pstm.setInt(3, clan.getLeaderId());
			pstm.setString(4, clan.getLeaderName());
			pstm.setInt(5, clan.getCastleId());
			pstm.setInt(6, clan.getHouseId());
			pstm.setString(7, clan.getAllianceList()!=null ? clan.getAllianceList().toString() : "");
			pstm.setString(8, clan.isAlliance_leader() ? "true" : "false");
			pstm.setTimestamp(9, clan.getClanBirthDay());
			pstm.setInt(10, clan.getOnlineMaxUser());
			pstm.setString(11, "");
			pstm.setString(12, "");
			pstm.setInt(13, 0);
			pstm.setInt(14, 0);
			pstm.setInt(15, 0);
			pstm.setInt(16, 0);
			pstm.setInt(17, 0);
			pstm.setInt(18, 0);
			pstm.setInt(19, 0);
			pstm.setInt(20, 0);
			pstm.setInt(21, 0);
			pstm.setInt(22, clan.getJoinSetting());
			pstm.setInt(23, clan.getJoinType());
			pstm.setBoolean(24, clan.isBot());
			pstm.setInt(25, clan.getEinhasadBlessBuff());
			pstm.setInt(26, clan.getBuffFirst());
			pstm.setInt(27, clan.getBuffSecond());
			pstm.setInt(28, clan.getBuffThird());
			pstm.setInt(29, 0);
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, "ClanTable[]Error2", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		L1World.getInstance().storeClan(clan);
		_clans.put(clan.getClanId(), clan);

		player.setClanid(clan.getClanId());
		player.setClanname(clan.getClanName());
		player.setClanRank(L1Clan.Prince);
		clan.addClanMember(player.getName(), player.getClanRank(), player.getLevel(), "", player.getId(), player.getType(), player.getOnlineStatus(), player);
		try {
			player.save();
		} catch (Exception e) {
			_log.log(Level.SEVERE, "ClanTable[]Error3", e);
		}
		return clan;
	}
	
	public L1Clan createClan(L1PcInstance player, String clan_name) {
		for (L1Clan oldClans : L1World.getInstance().getAllClans()) {
			if (oldClans.getClanName().equalsIgnoreCase(clan_name)) {
				return null;
			}
		}
		Timestamp time = new Timestamp(System.currentTimeMillis());
		L1Clan clan = new L1Clan();
		clan.setClanId(IdFactory.getInstance().nextId());
		clan.setClanName(clan_name);
		clan.setLeaderId(player.getId());
		clan.setLeaderName(player.getName());
		clan.setCastleId(0);
		clan.setHouseId(0);
		clan.setAllianceList(null);
		clan.setAlliance_leader(false);
		clan.setClanBirthDay(time);
		clan.setAnnouncement("");
		clan.setIntroduction("");
		clan.setEmblemId(0);
		clan.setEmblemStatus(0);
		clan.setBless(0);
		clan.setBlessCount(0);
		clan.setBuffTime(0, 0, 0, 0);
		clan.setJoinSetting(1);
		clan.setJoinType(1);
		clan.setContribution(0);
		clan.setEntranceNotice("");
		
		Connection con = null;
		PreparedStatement pstm = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_data SET clan_id=?, clan_name=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, alliance=?, alliance_leader=?, clan_birthday=?, max_online_user=?, announcement=?, introduction=?, noticement=?, emblem_id=?, emblem_status=?,clan_exp=?,bless=?,bless_count=?,attack=?,defence=?,pvpattack=?,pvpdefence=?,join_setting=?,join_type=?,EinhasadBlessBuff=?,Buff_List1=?,Buff_List2=?,Buff_List3=?,contribution=?");
			int idx = 0;
			pstm.setInt(++idx, clan.getClanId());
			pstm.setString(++idx, clan.getClanName());
			pstm.setInt(++idx, clan.getLeaderId());
			pstm.setString(++idx, clan.getLeaderName());
			pstm.setInt(++idx, clan.getCastleId());
			pstm.setInt(++idx, clan.getHouseId());
			pstm.setString(++idx, clan.getAllianceList()!=null ? clan.getAllianceList().toString() : "");
			pstm.setString(++idx, clan.isAlliance_leader() ? "true" : "false");
			pstm.setTimestamp(++idx, clan.getClanBirthDay());
			pstm.setInt(++idx, clan.getOnlineMaxUser());
			pstm.setString(++idx, "");
			pstm.setString(++idx, "");
			pstm.setString(++idx, "");
			pstm.setInt(++idx, 0);
			pstm.setInt(++idx, 0);
			pstm.setInt(++idx, 0);
			pstm.setInt(++idx, 0);
			pstm.setInt(++idx, 0);
			pstm.setInt(++idx, 0);
			pstm.setInt(++idx, 0);
			pstm.setInt(++idx, 0);
			pstm.setInt(++idx, 0);
			pstm.setInt(++idx, clan.getJoinSetting());
			pstm.setInt(++idx, clan.getJoinType());
			pstm.setInt(++idx, clan.getEinhasadBlessBuff());
			pstm.setInt(++idx, clan.getBuffFirst());
			pstm.setInt(++idx, clan.getBuffSecond());
			pstm.setInt(++idx, clan.getBuffThird());
			pstm.setInt(++idx, 0);
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, "ClanTable[]Error2", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		L1World.getInstance().storeClan(clan);
		_clans.put(clan.getClanId(), clan);

		player.setClanid(clan.getClanId());
		player.setClanname(clan.getClanName());
		player.setClanRank(L1Clan.Monarch);
		clan.addClanMember(player.getName(), player.getClanRank(), player.getLevel(), "", player.getId(), player.getType(), player.getOnlineStatus(), player);
		try {
			player.save();
		} catch (Exception e) {
			_log.log(Level.SEVERE, "ClanTable[]Error3", e);
		}
		return clan;
	}

	public static void updateWarPoint(L1Clan clan){
		Connection con = null;
		PreparedStatement pstm = null;
		try{
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("update clan_data set War_point=? WHERE clan_name=?");
			pstm.setInt(1, clan.getWarPoint());
			pstm.setString(2, clan.getClanName());
			pstm.execute();
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public static void updateOnlineUser(L1Clan clan){
		Connection 			con 	= null;
		PreparedStatement 	pstm 	= null;
		try{
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("update clan_data set total_m=?, current_m=? WHERE clan_name=?");
			pstm.setInt(1, clan.getClanMemberList().size());
			pstm.setInt(2, clan.getCurrentOnlineMemebers());
			pstm.setString(3, clan.getClanName());
			pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm, con);
		}
	}
	
	public void updateClan(L1Clan clan) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET clan_id=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, alliance=?, alliance_leader=?, clan_birthday=?, bot_style=?, bot_level=?, max_online_user=?, announcement=?, introduction=?, noticement=?, emblem_id=?, emblem_status=?, join_setting=?, join_type=?, total_m=?, current_m=?, War_point=?,EinhasadBlessBuff=?,Buff_List1=?,Buff_List2=?,Buff_List3=?,Castle_hasdate=?,contribution=? WHERE clan_name=?");
			int idx = 0;
			pstm.setInt(++idx, clan.getClanId());
			pstm.setInt(++idx, clan.getLeaderId());
			pstm.setString(++idx, clan.getLeaderName());
			pstm.setInt(++idx, clan.getCastleId());
			pstm.setInt(++idx, clan.getHouseId());
			pstm.setString(++idx, clan.getAllianceList()!=null ? clan.getAllianceList().toString() : "");
			pstm.setString(++idx, clan.isAlliance_leader() ? "true" : "false");
			pstm.setTimestamp(++idx, clan.getClanBirthDay());
			/** 血盟自動加入 */
			pstm.setInt(++idx, clan.getBotStyle());
			pstm.setInt(++idx, clan.getBotLevel());
			/** 血盟自動加入 */
			pstm.setInt(++idx, clan.getOnlineMaxUser());
			pstm.setString(++idx, clan.getAnnouncement());
			pstm.setString(++idx, clan.getIntroduction());
			pstm.setString(++idx, clan.getEntranceNotice());
//			System.out.println(clan.getEntranceNotice());
			
			
			pstm.setInt(++idx, clan.getEmblemId());
			pstm.setInt(++idx, clan.getEmblemStatus());
			pstm.setInt(++idx, clan.getJoinSetting());
			pstm.setInt(++idx, clan.getJoinType());
			pstm.setInt(++idx, clan.getClanMemberList().size());
			pstm.setInt(++idx, clan.getCurrentOnlineMemebers());
			pstm.setInt(++idx, clan.getWarPoint());
			pstm.setInt(++idx, clan.getEinhasadBlessBuff());
			pstm.setInt(++idx, clan.getBuffFirst());
			pstm.setInt(++idx, clan.getBuffSecond());
			pstm.setInt(++idx, clan.getBuffThird());
			pstm.setTimestamp(++idx, clan.getCastleDate());
			pstm.setInt(++idx, clan.getContribution());
			pstm.setString(++idx, clan.getClanName());
			
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, "ClanTable[]Error4", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 ** 血盟自動加入*
	 *
	 * @param player 玩家
	 * @param clan_name 血盟名稱
	 * @param style 風格
	 * @return 返回值
	 */
	public void createClanBot(L1PcInstance player, String clan_name, int style) {
		for (L1Clan oldClans : L1World.getInstance().getAllClans()) {
			if (oldClans.getClanName().equalsIgnoreCase(clan_name))
				return;
		}

		L1Clan clan = new L1Clan();
		clan.setClanId(IdFactory.getInstance().nextId());
		clan.setClanName(clan_name);
		clan.setLeaderId(player.getId());
		clan.setLeaderName(player.getName());
		clan.setCastleId(0);
		clan.setHouseId(0);
		clan.setBot(true);
		clan.setBotStyle(style);

		player.setClanid(clan.getClanId());
		player.setClanname(clan.getClanName());

		Connection con = null;
		PreparedStatement pstm = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("INSERT INTO clan_data SET clan_id=?, clan_name=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, bot=?, bot_style=?");
			pstm.setInt(1, clan.getClanId());
			pstm.setString(2, clan.getClanName());
			pstm.setInt(3, clan.getLeaderId());
			pstm.setString(4, clan.getLeaderName());
			pstm.setInt(5, clan.getCastleId());
			pstm.setInt(6, clan.getHouseId());
			pstm.setString(7, "true");
			pstm.setInt(8, style);
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, "ClanTable[]Error5", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		/** 血盟自動加入 */
		L1World.getInstance().storeClan(clan);
		_clans.put(clan.getClanId(), clan);
		/** 血盟自動加入 */
	}

	public void deleteClan(L1Clan clan){
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_data WHERE clan_name=?");
			pstm.setString(1, clan.getClanName());
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, "ClanTable[]Error6", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
		clanWarehouse.clearItems();
		clanWarehouse.deleteAllItems();

		L1World.getInstance().removeClan(clan);
		_clans.remove(clan.getClanId());
	}
	
	public void deleteClan(String clan_name) {
		L1Clan clan = L1World.getInstance().findClan(clan_name);
		if (clan == null) {
			return;
		}
		
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_data WHERE clan_name=?");
			pstm.setString(1, clan_name);
			pstm.execute();
		} catch (Exception e) {
			_log.log(Level.SEVERE, "ClanTable[]Error6", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
		clanWarehouse.clearItems();
		clanWarehouse.deleteAllItems();

		L1World.getInstance().removeClan(clan);
		_clans.remove(clan.getClanId());
	}

	public L1Clan getTemplate(int clan_id) {
		return _clans.get(clan_id);
	}
	
	public L1Clan getRedKnight(int castleId){
		return _redknights.get(castleId);
	}

	/** 血盟自動加入 */
	public static void reload() {
		L1World.getInstance().clearClan();
		ClanTable oldInstance = _instance;
		_instance = new ClanTable();
		if (oldInstance != null) {
			oldInstance._clans.clear();		
			oldInstance._redknights.clear();
		}
	}

	public L1Clan find(String clan_name) {
		for (L1Clan clan : _clans.values()) {
			if (clan.getClanName().equalsIgnoreCase(clan_name))
				return clan;
		}
		return null;
	}
	
	public L1Clan findCastleClan(int castleId){
		for(L1Clan clan : _clans.values()){
			if(clan == null)
				continue;
			
			if(clan.getCastleId() == castleId)
				return clan;
		}
		
		return _redknights.get(castleId);
	}

	public void updateUnderDungeon(int clanid, int type) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection(); PreparedStatement pstm = con.prepareStatement("UPDATE clan_data SET under_dungeon=? WHERE clan_id=?")) {
			pstm.setInt(1, type);
			pstm.setInt(2, clanid);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateRankDate(int clanid, Timestamp time) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection(); PreparedStatement pstm = con.prepareStatement("UPDATE clan_data SET rankdate=? WHERE clan_id=?")) {
			pstm.setTimestamp(1, time);
			pstm.setInt(2, clanid);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateRankTime(int clanid, int time) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection(); PreparedStatement pstm = con.prepareStatement("UPDATE clan_data SET ranktime=? WHERE clan_id=?")) {
			pstm.setInt(1, time);
			pstm.setInt(2, clanid);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 血盟增益 **/
	public void updateBlessCount(int clanid, int count) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection();
				PreparedStatement pstm = con.prepareStatement("UPDATE clan_data SET bless_count=? WHERE clan_id=?")) {
			pstm.setInt(1, count);
			pstm.setInt(2, clanid);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateBless(int clanid, int bless) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection(); PreparedStatement pstm = con.prepareStatement("UPDATE clan_data SET bless=? WHERE clan_id=?")) {
			pstm.setInt(1, bless);
			pstm.setInt(2, clanid);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateBuffTime(int a, int b, int c, int d, int clanid) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection();
				PreparedStatement pstm = con.prepareStatement("UPDATE clan_data SET attack=?,defence=?,pvpattack=?,pvpdefence=? WHERE clan_id=?")) {
			pstm.setInt(1, a);
			pstm.setInt(2, b);
			pstm.setInt(3, c);
			pstm.setInt(4, d);
			pstm.setInt(5, clanid);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 2016.11.25 MJ 應用中心 血盟 **/
	public void updateClanPassword(L1Clan clan) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("UPDATE clan_data SET join_password=? WHERE clan_name=?");
			pstm.setString(1, clan.getJoinPassword());
			pstm.setString(2, clan.getClanName());
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	/** 2016.11.25 MJ 應用中心 血盟 **/
	
	public void updateContribution(int clanid, int value) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection();
				PreparedStatement pstm = con.prepareStatement("UPDATE clan_data SET contribution=? WHERE clan_id=?")) {
			pstm.setInt(1, value);
			pstm.setInt(2, clanid);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

