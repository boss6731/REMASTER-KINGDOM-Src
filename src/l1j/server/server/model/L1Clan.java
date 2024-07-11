 package l1j.server.server.model;
 import java.sql.Timestamp;
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.concurrent.CopyOnWriteArrayList;
 import java.util.logging.Logger;
 import java.util.stream.Stream;
 import javolution.util.FastTable;
 import l1j.server.Config;
 import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
 import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
 import l1j.server.MJWarSystem.MJWar;
 import l1j.server.server.datatables.ClanTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Warehouse.ClanWarehouse;
 import l1j.server.server.model.Warehouse.WarehouseManager;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.IntRange;

 public class L1Clan {
   public static final int CLAN_RANK_LEAGUE_PUBLIC = 2;
   public static final int CLAN_RANK_LEAGUE_PRINCE = 4;
   public static final int CLAN_RANK_LEAGUE_PROBATION = 5;
   public static final int CLAN_RANK_LEAGUE_GUARDIAN = 6;
   public static final int 부군주 = 14;
   public static final int 일반 = 8;
   public static final int 수호 = 9;
   public static final int 군주 = 10;
   public static final int 정예 = 13;

   public static class ClanMember {
     public String name;

     public ClanMember(String name, int rank, int level, String notes, int memberId, int type, boolean online, L1PcInstance pc) {
       this.name = name;
       this.rank = rank;
       this.level = level;
       this.notes = notes;
       this.memberId = memberId;
       this.type = type;
       this.online = online;
       this.player = pc;
     }


     public int rank;

     public int level;

     public String notes;

     public int memberId;

     public int type;

     public boolean online;
     public L1PcInstance player;
   }
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

   private int _emblemStatus = 0; private int _join_setting;
   private int _join_type;
   private String _announcement;
   private String _introduction;
   private boolean _bot;
   private int _bot_style;
   private int _bot_level;

   public String getAnnouncement() {
     if (this._clanName.equalsIgnoreCase(Config.ServerAdSetting.NEWCLANNAME))
       this._announcement = ClanTable.CLAN_TUTORIAL_ANN;
     return this._announcement;
   }

   public void setAnnouncement(String announcement) {
     this._announcement = announcement;
   }



     public String getIntroduction() {
         // 如果_clanName等於Config.ServerAdSetting.NEWCLANNAME，則設置介紹信息為"신규 혈맹 입니다."
         if (this._clanName.equalsIgnoreCase(Config.ServerAdSetting.NEWCLANNAME))
             this._introduction = "新的血盟。"; //  신규 혈맹 입니다
         return this._introduction;
     }

   public void setIntroduction(String introduction) {
     this._introduction = introduction;
   }



   public int getEmblemId() {
     if (this._clanName.equalsIgnoreCase(Config.ServerAdSetting.NEWCLANNAME))
       this._emblemId = ClanTable.CLAN_TUTORIAL_EMB;
     return this._emblemId;
   }

   public void setEmblemId(int emblemId) {
     this._emblemId = emblemId;
   }

   public int getEmblemStatus() {
     if (this._clanName.equalsIgnoreCase(Config.ServerAdSetting.NEWCLANNAME))
       this._emblemStatus = 1;
     return this._emblemStatus;
   }

   public void setEmblemStatus(int emblemStatus) {
     this._emblemStatus = emblemStatus;
   }



   private CopyOnWriteArrayList<ClanMember> clanMemberList = new CopyOnWriteArrayList<>();

   public CopyOnWriteArrayList<ClanMember> getClanMemberList() {
     return this.clanMemberList;
   }


   public void addClanMember(String name, int rank, int level, String notes, int memberid, int type, int online, L1PcInstance pc) {
     this.clanMemberList.add(new ClanMember(name, rank, level, notes, memberid, type, (online == 1), (online == 1) ? pc : null));
     ClanTable.updateOnlineUser(this);
   }

   public void addClanMember(ClanMember cm) {
     this.clanMemberList.add(cm);
     ClanTable.updateOnlineUser(this);
   }

   public void removeClanMember(String name) {
     for (int i = 0; i < this.clanMemberList.size(); i++) {
       if (((ClanMember)this.clanMemberList.get(i)).name.equals(name)) {
         deleteClanRetrieveUser(((ClanMember)this.clanMemberList.get(i)).memberId);
         this.clanMemberList.remove(i);
         break;
       }
     }
     ClanTable.updateOnlineUser(this);
   }


   public void setClanRank(String name, int data) {
     for (int i = 0; i < this.clanMemberList.size(); i++) {
       if (((ClanMember)this.clanMemberList.get(i)).name.equals(name)) {
         ((ClanMember)this.clanMemberList.get(i)).rank = data;
         break;
       }
     }
   }


   public int getOnlineMaxUser() {
     return this._maxuser;
   }

   public void setOnlineMaxUser(int i) {
     this._maxuser = i;
   }

   public int getCurrentUser() {
     return this._curuser;
   }

   public void setCurrentUser(int i) {
     this._curuser = i;
   }


   public void UpdataClanMember(String name, int rank) {
     for (int i = 0; i < this.clanMemberList.size(); i++) {
       if (((ClanMember)this.clanMemberList.get(i)).name.equals(name)) {
         ((ClanMember)this.clanMemberList.get(i)).rank = rank;
         break;
       }
     }
   }

   public void updateClanMemberOnline(L1PcInstance pc) {
     for (ClanMember clan : this.clanMemberList) {
       if (clan.memberId != pc.getId()) {
         continue;
       }
       clan.online = (pc.getOnlineStatus() == 1);
       clan.player = pc;
     }

     ClanTable.updateOnlineUser(this);
   }

   public int getCurrentOnlineMemebers() {
     int cnt = 0;
     for (ClanMember clan : this.clanMemberList) {
       if (clan.player == null) {
         clan.online = false;
       } else {
         clan.online = (clan.player.getOnlineStatus() == 1);
       }  if (clan.online)
         cnt++;
     }
     return cnt;
   }

   public String[] getAllMembersName() {
     ArrayList<String> members = new ArrayList<>();

     for (int i = 0; i < this.clanMemberList.size(); i++) {
       ClanMember member = this.clanMemberList.get(i);
       if (!members.contains(member.name)) {
         members.add(member.name);
       }
     }
     return members.<String>toArray(new String[members.size()]);
   }

   public Timestamp getClanBirthDay() {
     return this._clanBirthday;
   }

   public void setClanBirthDay(Timestamp t) {
     this._clanBirthday = t;
   }

   public int getClanId() {
     return this._clanId;
   }

   public void setClanId(int clan_id) {
     this._clanId = clan_id;
   }

   public String getClanName() {
     return this._clanName;
   }

   public void setClanName(String clan_name) {
     this._clanName = clan_name;
   }

   public int getLeaderId() {
     return this._leaderId;
   }

   public void setLeaderId(int leader_id) {
     this._leaderId = leader_id;
   }

   public String getLeaderName() {
     return this._leaderName;
   }

   public void setLeaderName(String leader_name) {
     this._leaderName = leader_name;
   }

   public int getCastleId() {
     return this._castleId;
   }

   public void setCastleId(int hasCastle) {
     this._castleId = hasCastle;
   }

   public int getInCastleId() {
     return this._inCastleId;
   }

   public void setInCastleId(int i) {
     this._inCastleId = i;
   }

   public int getHouseId() {
     return this._houseId;
   }

   public void setHouseId(int hasHideout) {
     this._houseId = hasHideout;
   }


   public int getOnlineMemberCount() {
     int count = 0;
     for (int i = 0; i < this.clanMemberList.size(); i++) {
       if (L1World.getInstance().getPlayer(((ClanMember)this.clanMemberList.get(i)).name) != null) {
         count++;
       }
     }
     return count;
   }

   public L1PcInstance[] getOnlineClanMember() {
     ArrayList<L1PcInstance> onlineMembers = new ArrayList<>(this.clanMemberList.size());
     L1PcInstance pc = null;
     for (int i = 0; i < this.clanMemberList.size(); i++) {
       pc = L1World.getInstance().getPlayer(((ClanMember)this.clanMemberList.get(i)).name);
       if (pc != null && !onlineMembers.contains(pc)) {
         onlineMembers.add(pc);
       }
     }
     return onlineMembers.<L1PcInstance>toArray(new L1PcInstance[onlineMembers.size()]);
   }


   public String getAllMembersFP() {
     String result = "";
     String rank = "";
     for (int i = 0; i < this.clanMemberList.size(); i++) {
       result = result + ((ClanMember)this.clanMemberList.get(i)).name + rank + " ";
     }
     return result;
   }


   public String getOnlineMembersFP() {
     String result = "";
     String rank = "";
     L1PcInstance pc = null;
     for (int i = 0; i < this.clanMemberList.size(); i++) {
       pc = L1World.getInstance().getPlayer(((ClanMember)this.clanMemberList.get(i)).name);
       if (pc != null) {
         result = result + ((ClanMember)this.clanMemberList.get(i)).name + rank + " ";
       }
     }
     return result;
   }


   public boolean isBot() {
     return this._bot;
   }

   public void setBot(boolean _bot) {
     this._bot = _bot;
   }

   public int getBotStyle() {
     return this._bot_style;
   }

   public void setBotStyle(int _bot_style) {
     this._bot_style = _bot_style;
   }

   public int getBotLevel() {
     return this._bot_level;
   }

   public void setBotLevel(int _bot_level) {
     this._bot_level = _bot_level;
   }



   private FastTable<String> GazeList = new FastTable();


   public void addGazelist(String name) {
     if (this.GazeList.contains(name)) {
       return;
     }
     this.GazeList.add(name);
   }


   public void removeGazelist(String name) {
     if (!this.GazeList.contains(name)) {
       return;
     }
     this.GazeList.remove(name);
   }


   public int getGazeSize() {
     return this.GazeList.size();
   }


   public FastTable<String> getGazeList() {
     return this.GazeList;
   }

   public L1PcInstance getonline간부() {
     L1PcInstance pc = null;
     L1PcInstance no1pc = null;
     int oldrank = 0;
     for (int i = 0; i < this.clanMemberList.size(); i++) {
       if (this.clanMemberList.get(i) != null)
       {
         if (((ClanMember)this.clanMemberList.get(i)).online && ((ClanMember)this.clanMemberList.get(i)).player != null) {

           pc = ((ClanMember)this.clanMemberList.get(i)).player;
           if (pc.getClanRank() >= 9 &&
             oldrank < pc.getClanRank()) {
             oldrank = pc.getClanRank();
             no1pc = pc;
           }
         }  }
     }
     return no1pc;
   }

   public int getJoinSetting() {
     return this._join_setting;
   }

   public void setJoinSetting(int i) {
     this._join_setting = i;
   }

   public int getJoinType() {
     return this._join_type;
   }

   public void setJoinType(int i) {
     this._join_type = i;
   }


   private int _bless = 0;
   private int _blesscount = 0;
   private int _attack = 0;
   private int _defence = 0;
   private int _pvpattack = 0;
   private int _pvpdefence = 0;
   public int[] getBuffTime = new int[] { this._attack, this._defence, this._pvpattack, this._pvpdefence }; private String _joinPassword;

   public int[] getBuffTime() {
     return this.getBuffTime;
   }
   private int _warPoint; private MJWar _currentWar;
   public void setBuffTime(int i, int j) {
     this.getBuffTime[i] = IntRange.ensure(j, 0, 172800);
   }

   public void setBuffTime(int a, int b, int c, int d) {
     this.getBuffTime = new int[] { a, b, c, d };
   }

   public int getBlessCount() {
     return this._blesscount;
   }

   public void setBlessCount(int i) {
     this._blesscount = IntRange.ensure(i, 0, 400000000);
   }

   public void addBlessCount(int i) {
     this._blesscount += i;
     if (this._blesscount > 400000000) {
       this._blesscount = 400000000;
     } else if (this._blesscount < 0) {
       this._blesscount = 0;
     }
   }
   public int getBless() {
     return this._bless;
   }

   public void setBless(int i) {
     this._bless = i;
   }




   public String getJoinPassword() {
     return this._joinPassword;
   }

   public void setJoinPassword(String s) {
     this._joinPassword = s;
   }





   public int getWarPoint() {
     return this._warPoint;
   }

   public void setWarPoint(int i) {
     this._warPoint = i;
   }

   public void incWarPoint() {
     this._warPoint++;
     ClanTable.updateWarPoint(this);
   }

   public boolean decWarPoint() {
     if (this._warPoint <= 0)
       return false;
     this._warPoint--;
     ClanTable.updateWarPoint(this);
     return true;
   }

   public Stream<ClanMember> createMembersStream() {
     return (this.clanMemberList.size() > 100) ? this.clanMemberList.parallelStream() : this.clanMemberList.stream();
   }

   public Stream<ClanMember> createOnlineMembers() {
     return createMembersStream().filter(m -> (m.online && m.player != null));
   }

   public void broadcast(ServerBasePacket pck) {
     broadcast(pck, true);
   }

   public void broadcast(ServerBasePacket pck, boolean isClear) {
     Stream<ClanMember> stream = createOnlineMembers();
     if (stream != null)
       stream.forEach(m -> {
             if (m.player != null) {
               m.player.sendPackets(pck, false);
             }
           });
     if (isClear)
       pck.clear();
   }

   public void broadcast(ProtoOutputStream output) {
     broadcast(output, true);
   }

   public void broadcast(ProtoOutputStream output, boolean isClear) {
     Stream<ClanMember> stream = createOnlineMembers();
     if (stream != null)
       stream.forEach(m -> {
             if (m.player != null) {
               m.player.sendPackets(output, false);
             }
           });
     if (isClear) {
       output.dispose();
     }
   }


   public void setCurrentWar(MJWar war) {
     this._currentWar = war;
   }

   public MJWar getCurrentWar() {
     return this._currentWar;
   }

   private boolean _isRedKnight = false;

   public boolean isRedKnight() {
     return this._isRedKnight;
   }

   public void setRedKnight(boolean b) {
     this._isRedKnight = b;
   }

   public void outOfWarArea(int castleId) {
     int[] loc = L1CastleLocation.getGetBackLoc(castleId);
     createMembersStream().filter(member ->
         (member.player != null && L1CastleLocation.checkInWarArea(castleId, (L1Character)member.player)))
       .forEach(member -> member.player.start_teleport(loc[0], loc[1], (short)loc[2], 5, 18339, true));
   }




   private int _BuffFirst = 0;
   private int _BuffSecond = 0;
   private int _BuffThird = 0;
   private int _EinhasadBlessBuff = 0; private Timestamp _castleDate;

   public int getBuffFirst() {
     return this._BuffFirst;
   }

   public void setBuffFirst(int i) {
     this._BuffFirst = i;
   }

   public int getBuffSecond() {
     return this._BuffSecond;
   }

   public void setBuffSecond(int i) {
     this._BuffSecond = i;
   }

   public int getBuffThird() {
     return this._BuffThird;
   }

   public void setBuffThird(int i) {
     this._BuffThird = i;
   }

   public int getEinhasadBlessBuff() {
     return this._EinhasadBlessBuff;
   }

   public void setEinhasadBlessBuff(int i) {
     this._EinhasadBlessBuff = i;
   }

   public void deleteClanRetrieveUser(int targetObjectId) {
     ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(getClanName());
     if (clanWarehouse != null && clanWarehouse.getWarehouseUsingChar() == targetObjectId) {
       clanWarehouse.setWarehouseUsingChar(0, 0);
     }
   }


   public Timestamp getCastleDate() {
     return this._castleDate;
   }

   public void setCastleDate(Timestamp castleDate) {
     this._castleDate = castleDate;
   }

   private ArrayList<Integer> allianceList = new ArrayList<>();

   public ArrayList<Integer> getAllianceList() {
     if (this.allianceList.size() > 0) {
       return this.allianceList;
     }
     return null;
   }

   public L1Clan getAlliance(int i) {
     if (this.allianceList.size() > 0) {
       for (Iterator<Integer> iterator = this.allianceList.iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
         if (id == i) {
           return L1World.getInstance().getClan(i);
         } }

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
       String[] clan_id = (String[])MJArrangeParser.parsing(claninfo, ", ", MJArrangeParseeFactory.createStringArrange()).result();
       for (int i = 0; i < clan_id.length; i++) {
         if (!this.allianceList.contains(Integer.valueOf(Integer.parseInt(clan_id[i]))))
           this.allianceList.add(Integer.valueOf(Integer.parseInt(clan_id[i])));
       }
     }
   }

   public void addAlliance(int i) {
     if (i == 0) {
       return;
     }
     if (!this.allianceList.contains(Integer.valueOf(getClanId()))) {
       this.allianceList.add(Integer.valueOf(getClanId()));
     }
     if (!this.allianceList.contains(Integer.valueOf(i)))
       this.allianceList.add(Integer.valueOf(i));
   }

   public void removeAlliance(int i) {
     if (i == 0) {
       return;
     }
     if (this.allianceList.contains(Integer.valueOf(i)))
       this.allianceList.remove(Integer.valueOf(i));
   }

   public Integer[] Alliance() {
     Integer[] i = this.allianceList.<Integer>toArray(new Integer[this.allianceList.size()]);
     return i;
   }

   public int AllianceSize() {
     return this.allianceList.size();
   }

   public void AllianceDelete() {
     if (this.allianceList.size() > 0)
       this.allianceList.clear();
   }

   private boolean _isAlliance_leader = false;

   public boolean isAlliance_leader() {
     return this._isAlliance_leader;
   }

   public void setAlliance_leader(boolean b) {
     this._isAlliance_leader = b;
   }

   public void setLoad_Alliance_leader(String b) {
     this._isAlliance_leader = b.equalsIgnoreCase("true");
   }

   private int _contribution = 0; private String _entrance_notice;

   public int getContribution() {
     return this._contribution;
   }

   public void setContribution(int i) {
     this._contribution = i;
   }

   public void addContribution(int i) {
     this._contribution += i * Config.ServerAdSetting.CLAN_CONTRIBUTION;
   }

   public void addClanShopContribution(int i) {
     this._contribution -= i;
   }


   public void setEntranceNotice(String ment) {
     this._entrance_notice = ment;
   }
   public String getEntranceNotice() {
     return this._entrance_notice;
   }
 }


