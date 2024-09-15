package l1j.server.server.server.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.stream.Stream;


import l1j.server.Config;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJWarSystem.MJWar;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.ClanWarehouse;
import l1j.server.server.model.Warehouse.WarehouseManager;
import l1j.server.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.IntRange;

public class L1Clan {
	public static final int GENERAL = 1;
	public static final int PRINCE = 1;
	public static l1j.server.server.model.L1Clan clan;

    static public class ClanMember {
		public String name;
		public int rank;
		public int level;
		public String notes;
		public int memberId;
		public int type;
		public boolean online;
		public L1PcInstance player;

		public ClanMember(String name, int rank, int level, String notes, int memberId, int type, boolean online,
						  L1PcInstance pc) {
			this.name = name;
			this.rank = rank;
			this.level = level;
			this.notes = notes;
			this.memberId = memberId;
			this.type = type;
			this.online = online;
			this.player = pc;
		}
	}

	public static final int CLAN_RANK_LEAGUE_PUBLIC = 2;
	public static final int CLAN_RANK_LEAGUE_PRINCE = 4;
	public static final int CLAN_RANK_LEAGUE_PROBATION = 5;
	public static final int CLAN_RANK_LEAGUE_GUARDIAN = 6;

	public static final int VICE_KING = 14;
	// public static final int TRAINING = 7;
	public static final int NORMAL = 8;
	public static final int GUARDIAN = 9;
	public static final int KING = 10;
	public static final int ELITE = 13;

	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(L1Clan.class.getName());

	private int _clanId;

	private String _clanName;

	private int _leaderId;

	private String _leaderName;

	private int _castleId;

	private int _inCastleId = -1;

	private int _houseId;

	private Timestamp _clanBirthday;

	private int _maxuser;
	private int _curuser;

	private int _emblemId = 0;

	private int _emblemStatus = 0;

	// private int _clan_exp; // 公會經驗值

	// 設定公會加入
	private int _join_setting;
	private int _join_type;

	public String getAnnouncement() {
		if (_clanName.equalsIgnoreCase(Config.ServerAdSetting.NEWCLANNAME))
			_announcement = ClanTable.CLAN_TUTORIAL_ANN;
		return _announcement;
	}

	public void setAnnouncement(String announcement) {
		this._announcement = announcement;
	}

	private String _announcement;

	public String getIntroduction() {
		if (_clanName.equalsIgnoreCase(Config.ServerAdSetting.NEWCLANNAME))
			_introduction = ClanTable.CLAN_TUTORIAL_INTRO;
		return _introduction;
	}

	public void setIntroduction(String introduction) {
		this._introduction = introduction;
	}

	private String _introduction;

	public int getEmblemId() {
		if (_clanName.equalsIgnoreCase(Config.ServerAdSetting.NEWCLANNAME))
			_emblemId = ClanTable.CLAN_TUTORIAL_EMB;
		return _emblemId;
	}

	public void setEmblemId(int emblemId) {
		this._emblemId = emblemId;
	}

	public int getEmblemStatus() {
		if (_clanName.equalsIgnoreCase(Config.ServerAdSetting.NEWCLANNAME))
			_emblemStatus = 1;
		return _emblemStatus;
	}

	public void setEmblemStatus(int emblemStatus) {
		this._emblemStatus = emblemStatus;
	}

	/** 公會自動加入 */
	private boolean _bot;
	private int _bot_style;
	private int _bot_level;
	/** 公會自動加入 */
	private CopyOnWriteArrayList<ClanMember> clanMemberList = new CopyOnWriteArrayList<ClanMember>();

	public CopyOnWriteArrayList<ClanMember> getClanMemberList() {
		return clanMemberList;
	}

	public void addClanMember(String name, int rank, int level, String notes, int memberid, int type, int online,
							  L1PcInstance pc) {
		clanMemberList.add(new ClanMember(name, rank, level, notes, memberid, type, online == 1, online == 1 ? pc : null));
		ClanTable.updateOnlineUser(this);
	}

	public void addClanMember(ClanMember cm) {
		clanMemberList.add(cm);
		ClanTable.updateOnlineUser(this);
	}

	public void removeClanMember(String name) {
		for (int i = 0; i < clanMemberList.size(); i++) {
			if (clanMemberList.get(i).name.equals(name)) {
				deleteClanRetrieveUser(clanMemberList.get(i).memberId);
				clanMemberList.remove(i);
				break;
			}
		}
		ClanTable.updateOnlineUser(this);
	}

	/////////// 公會更新 //////////
	public void setClanRank(String name, int data) {
		for (int i = 0; i < clanMemberList.size(); i++) {
			if (clanMemberList.get(i).name.equals(name)) {
				clanMemberList.get(i).rank = data;
				break;
			}
		}
	}

	/////////// 公會更新 //////////
	public int getOnlineMaxUser() {
		return _maxuser;
	}

	public void setOnlineMaxUser(int i) {
		_maxuser = i;
	}

	public int getCurrentUser() {
		return _curuser;
	}

	public void setCurrentUser(int i) {
		_curuser = i;
	}

	// 실시간 변경
	public void UpdataClanMember(String name, int rank) {
		for (int i = 0; i < clanMemberList.size(); i++) {
			if (clanMemberList.get(i).name.equals(name)) {
				clanMemberList.get(i).rank = rank;
				break;
			}
		}
	}

	public void updateClanMemberOnline(L1PcInstance pc) {
		for (ClanMember clan : clanMemberList) {
			if (clan.memberId != pc.getId())
				continue;

			clan.online = pc.getOnlineStatus() == 1;
			clan.player = pc;
			break;
		}
		ClanTable.updateOnlineUser(this);
	}

	public int getCurrentOnlineMemebers() {
		int cnt = 0;
		for (ClanMember clan : clanMemberList) {
			if (clan.player == null)
				clan.online = false;
			else
				clan.online = clan.player.getOnlineStatus() == 1;
			if (clan.online)
				cnt++;
		}
		return cnt;
	}

	public String[] getAllMembersName() {
		ArrayList<String> members = new ArrayList<String>();
		ClanMember member;
		for (int i = 0; i < clanMemberList.size(); i++) {
			member = clanMemberList.get(i);
			if (!members.contains(member.name)) {
				members.add(member.name);
			}
		}
		return members.toArray(new String[members.size()]);
	}

	public Timestamp getClanBirthDay() {
		return _clanBirthday;
	}

	public void setClanBirthDay(Timestamp t) {
		_clanBirthday = t;
	}

	public int getClanId() {
		return _clanId;
	}

	public void setClanId(int clan_id) {
		_clanId = clan_id;
	}

	public String getClanName() {
		return _clanName;
	}

	public void setClanName(String clan_name) {
		_clanName = clan_name;
	}

	public int getLeaderId() {
		return _leaderId;
	}

	public void setLeaderId(int leader_id) {
		_leaderId = leader_id;
	}

	public String getLeaderName() {
		return _leaderName;
	}

	public void setLeaderName(String leader_name) {
		_leaderName = leader_name;
	}

	public int getCastleId() {
		return _castleId;
	}

	public void setCastleId(int hasCastle) {
		_castleId = hasCastle;
	}

	public int getInCastleId() {
		return _inCastleId;
	}

	public void setInCastleId(int i) {
		_inCastleId = i;
	}

	public int getHouseId() {
		return _houseId;
	}

	public void setHouseId(int hasHideout) {
		_houseId = hasHideout;
	}

	// 在線的公會成員數
	public int getOnlineMemberCount() {
		int count = 0;
		for (int i = 0; i < clanMemberList.size(); i++) {
			if (L1World.getInstance().getPlayer(clanMemberList.get(i).name) != null) {
				count++;
			}
		}
		return count;
	}

	public L1PcInstance[] getOnlineClanMember() {
		ArrayList<L1PcInstance> onlineMembers = new ArrayList<L1PcInstance>(clanMemberList.size());
		L1PcInstance pc = null;
		for (int i = 0; i < clanMemberList.size(); i++) {
			pc = L1World.getInstance().getPlayer(clanMemberList.get(i).name);
			if (pc != null && !onlineMembers.contains(pc)) {
				onlineMembers.add(pc);
			}
		}
		return onlineMembers.toArray(new L1PcInstance[onlineMembers.size()]);
	}

	// 全體公會成員姓名列表
	public String getAllMembersFP() {
		String result = "";
		String rank = "";
		for (int i = 0; i < clanMemberList.size(); i++) {
			result = result + clanMemberList.get(i).name + rank + " ";
		}
		return result;
	}

	// 在線的公會成員姓名列表
	public String getOnlineMembersFP() {
		String result = "";
		String rank = "";
		L1PcInstance pc = null;
		for (int i = 0; i < clanMemberList.size(); i++) {
			pc = L1World.getInstance().getPlayer(clanMemberList.get(i).name);
			if (pc != null) {
				result = result + clanMemberList.get(i).name + rank + " ";
			}
		}
		return result;
	}

	/** 公會自動加入 */
	public boolean isBot() {
		return _bot;
	}

	public void setBot(boolean _bot) {
		this._bot = _bot;
	}

	public int getBotStyle() {
		return _bot_style;
	}

	public void setBotStyle(int _bot_style) {
		this._bot_style = _bot_style;
	}

	public int getBotLevel() {
		return _bot_level;
	}

	public void setBotLevel(int _bot_level) {
		this._bot_level = _bot_level;
	}

	/** 公會自動加入 */
	// 句子注視目錄
	private FastTable<String> GazeList = new FastTable<String>();

	// 添加句子注視
	public void addGazelist(String name) {
		if (GazeList.contains(name)) {
			return;
		}
		GazeList.add(name);
	}

	// 刪除句子注視
	public void removeGazelist(String name) {
		if (!GazeList.contains(name)) {
			return;
		}
		GazeList.remove(name);
	}

	// 句子注視大小
	public int getGazeSize() {
		return GazeList.size();
	}

	// 返回注視列表
	public FastTable<String> getGazeList() {
		return GazeList;
	}

	public L1PcInstance getOnlineExecutive() {
		L1PcInstance pc = null;
		L1PcInstance topPc = null;
		int highestRank = 0;

		for (int i = 0; i < clanMemberList.size(); i++) {
			if (clanMemberList.get(i) == null)
				continue;
			if (!clanMemberList.get(i).online || clanMemberList.get(i).player == null)
				continue;

			pc = clanMemberList.get(i).player;

			if (pc.getClanRank() >= L1Clan.GUARDIAN) {
				if (highestRank < pc.getClanRank()) {
					highestRank = pc.getClanRank();
					topPc = pc;
				}
			}
		}
		return topPc;
	}

	public int getJoinSetting() {
		return _join_setting;
	}

	public void setJoinSetting(int i) {
		_join_setting = i;
	}

	public int getJoinType() {
		return _join_type;
	}

	public void setJoinType(int i) {
		_join_type = i;
	}

	/** 公會增益點數 **/
	private int _bless = 0;
	private int _blesscount = 0;
	private int _attack = 0;
	private int _defence = 0;
	private int _pvpattack = 0;
	private int _pvpdefence = 0;
	public int[] getBuffTime = new int[] { _attack, _defence, _pvpattack, _pvpdefence };

	public int[] getBuffTime() {
		return getBuffTime;
	}

	public void setBuffTime(int i, int j) {
		getBuffTime[i] = IntRange.ensure(j, 0, 172800);
	}

	public void setBuffTime(int a, int b, int c, int d) {
		getBuffTime = new int[] { a, b, c, d };
	}

	public int getBlessCount() {
		return _blesscount;
	}

	public void setBlessCount(int i) {
		_blesscount = IntRange.ensure(i, 0, 400000000);
	}

	public void addBlessCount(int i) {
		_blesscount += i;
		if (_blesscount > 400000000)
			_blesscount = 400000000;
		else if (_blesscount < 0)
			_blesscount = 0;
	}

	public int getBless() {
		return _bless;
	}

	public void setBless(int i) {
		_bless = i;
	}

	/** 2016.11.25 MJ 應用中心 公會 **/
	private String _joinPassword;

	public String getJoinPassword() {
		return _joinPassword;
	}

	public void setJoinPassword(String s) {
		_joinPassword = s;
	}

	/** 2016.11.25 MJ 應用中心 血盟 **/

	private int _warPoint;

	public int getWarPoint() {
		return _warPoint;
	}

	public void setWarPoint(int i) {
		_warPoint = i;
	}

	public void incWarPoint() {
		_warPoint++;
		ClanTable.updateWarPoint(this);
	}

	public boolean decWarPoint() {
		if (_warPoint <= 0)
			return false;
		_warPoint--;
		ClanTable.updateWarPoint(this);
		return true;
	}

	public Stream<ClanMember> createMembersStream() {
		return clanMemberList.size() > 100 ? clanMemberList.parallelStream() : clanMemberList.stream();
	}

	public Stream<ClanMember> createOnlineMembers() {
		return createMembersStream().filter((ClanMember m) -> m.online && m.player != null);
	}

	public void broadcast(ServerBasePacket pck) {
		broadcast(pck, true);
	}

	public void broadcast(ServerBasePacket pck, boolean isClear) {
		Stream<ClanMember> stream = createOnlineMembers();
		if (stream != null) {
			stream.forEach((ClanMember m) -> {
				if (m.player != null)
					m.player.sendPackets(pck, false);
			});
		}
		if (isClear)
			pck.clear();
	}

	public void broadcast(ProtoOutputStream output) {
		broadcast(output, true);
	}

	public void broadcast(ProtoOutputStream output, boolean isClear) {
		Stream<ClanMember> stream = createOnlineMembers();
		if (stream != null) {
			stream.forEach((ClanMember m) -> {
				if (m.player != null)
					m.player.sendPackets(output, false);
			});
		}
		if (isClear)
			output.dispose();
	}

	private MJWar _currentWar;

	public void setCurrentWar(MJWar war) {
		_currentWar = war;
	}

	public MJWar getCurrentWar() {
		return _currentWar;
	}

	private boolean _isRedKnight = false;

	public boolean isRedKnight() {
		return _isRedKnight;
	}

	public void setRedKnight(boolean b) {
		_isRedKnight = b;
	}

	public void outOfWarArea(int castleId) {
		final int[] loc = L1CastleLocation.getGetBackLoc(castleId);
		createMembersStream().filter((ClanMember member) -> {
			return member.player != null && L1CastleLocation.checkInWarArea(castleId, member.player);
		}).forEach((ClanMember member) -> {
			member.player.start_teleport(loc[0], loc[1], (short) loc[2], 5, 18339, true);
		});
	}

	//TODO 公會增益重組 2017-11-12
	private int _BuffFirst = 0;
	private int _BuffSecond = 0;
	private int _BuffThird = 0;
	private int _EinhasadBlessBuff = 0;

	public int getBuffFirst() {
		return _BuffFirst;
	}

	public void setBuffFirst(int i) {
		_BuffFirst = i;
	}

	public int getBuffSecond() {
		return _BuffSecond;
	}

	public void setBuffSecond(int i) {
		_BuffSecond = i;
	}

	public int getBuffThird() {
		return _BuffThird;
	}

	public void setBuffThird(int i) {
		_BuffThird = i;
	}

	public int getEinhasadBlessBuff() {
		return _EinhasadBlessBuff;
	}

	public void setEinhasadBlessBuff(int i) {
		_EinhasadBlessBuff = i;
	}

	public void deleteClanRetrieveUser(int targetObjectId){
		ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(getClanName());
		if(clanWarehouse != null && clanWarehouse.getWarehouseUsingChar() == targetObjectId)
			clanWarehouse.setWarehouseUsingChar(0, 0);
	}

	private Timestamp _castleDate;

	public Timestamp getCastleDate() {
		return _castleDate;
	}

	public void setCastleDate(Timestamp castleDate) {
		_castleDate = castleDate;
	}

	private ArrayList<Integer> allianceList = new ArrayList<Integer>();

	public ArrayList<Integer> getAllianceList() {
		if (allianceList.size() > 0) {
			return allianceList;
		}
		return null;
	}

	public L1Clan getAlliance(int i) {
		if (allianceList.size() > 0) {
			for (int id : allianceList) {
				if (id == i) {
					return L1World.getInstance().getClan(i);
				}
			}
		}
		return null;
	}

	public void setAllianceList(String clanlist) {
		if (clanlist == null)
			return;
		int first_idx = clanlist.indexOf("[") + 1;
		int last_idx = clanlist.lastIndexOf("]");
		if (first_idx > -1 && last_idx > -1) {
			String claninfo = clanlist.substring(first_idx, last_idx);
			String[] clan_id = (String[]) MJArrangeParser.parsing(claninfo, ", ", MJArrangeParseeFactory.createStringArrange()).result();
			for (int i = 0; i < clan_id.length; i++) {
				if (!allianceList.contains(Integer.parseInt(clan_id[i])))
					allianceList.add(Integer.parseInt(clan_id[i]));
			}
		}
	}

	public void addAlliance(int i) {
		if (i == 0)
			return;

		if (!allianceList.contains((Integer) this.getClanId()))
			allianceList.add((Integer) this.getClanId());

		if (!allianceList.contains((Integer) i))
			allianceList.add((Integer) i);
	}

	public void removeAlliance(int i) {
		if (i == 0)
			return;

		if (allianceList.contains((Integer) i))
			allianceList.remove((Integer) i);
	}

	public Integer[] Alliance() {
		Integer[] i = (Integer[]) allianceList.toArray(new Integer[allianceList.size()]);
		return i;
	}

	public int AllianceSize() {
		return allianceList.size();
	}

	public void AllianceDelete() {
		if (allianceList.size() > 0)
			allianceList.clear();
	}

	private boolean _isAlliance_leader = false;

	public boolean isAlliance_leader() {
		return _isAlliance_leader;
	}

	public void setAlliance_leader(boolean b) {
		_isAlliance_leader = b;
	}

	public void setLoad_Alliance_leader(String b) {
		_isAlliance_leader = b.equalsIgnoreCase("true") ? true : false;
	}

	private int _contribution = 0;

	public int getContribution() {
		return _contribution;
	}

	public void setContribution(int i) {
		_contribution = i;
	}

	public void addContribution(int i) {
		_contribution += i * Config.ServerAdSetting.CLAN_CONTRIBUTION;
	}

	public void addClanShopContribution(int i) {
		_contribution -= i;
	}

	private String _entrance_notice;
	public void setEntranceNotice(String ment) {
		_entrance_notice = ment;
	}
	public String getEntranceNotice() {
		return _entrance_notice;
	}
}
