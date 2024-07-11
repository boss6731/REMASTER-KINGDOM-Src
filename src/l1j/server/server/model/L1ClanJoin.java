 package l1j.server.server.model;
 import java.io.File;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.Calendar;
 import java.util.TimeZone;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.ClanBuffList.ClanBuffListLoader;
 import l1j.server.Config;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_JOIN_ACK;
 import l1j.server.server.datatables.AuctionBoardTable;
 import l1j.server.server.datatables.CharacterTable;
 import l1j.server.server.datatables.ClanTable;
 import l1j.server.server.datatables.HouseTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_CharTitle;
 import l1j.server.server.serverpackets.S_ClanName;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_Pledge;
 import l1j.server.server.serverpackets.S_ReturnedStat;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1AuctionBoard;
 import l1j.server.server.templates.L1House;
 import l1j.server.server.utils.SQLUtil;

 public class L1ClanJoin {
   private static Logger _log = Logger.getLogger(L1ClanJoin.class.getName()); private static L1ClanJoin _instance;

   public static L1ClanJoin getInstance() {
     if (_instance == null) _instance = new L1ClanJoin();
     return _instance;
   }


     public void tutorialJoin(L1Clan clan, L1PcInstance pc) {
         // 創建一個伺服器消息，告訴其他成員新玩家加入
         S_ServerMessage msg = new S_ServerMessage(94, pc.getName());

         // 向每個在線的血盟成員發送消息
         for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
             clanMembers.sendPackets((ServerBasePacket)msg, false);
         }

         // 清除消息內容
         msg.clear();

         // 設置玩家的血盟ID
         pc.setClanid(clan.getClanId());

         // 設置玩家的血盟名稱
         pc.setClanname(clan.getClanName());

         // 設置玩家在血盟中的等級
         pc.setClanRank(8);

         // 清空玩家的血盟成員註釋
         pc.setClanMemberNotes("");

         // 設置玩家的稱號
         pc.setTitle(Config.Message.GameServerName);

         // 發送系統消息告訴玩家新手保護血盟會有PK傷害限制
         pc.sendPackets((ServerBasePacket)new S_SystemMessage("新手保護血盟會有PK傷害限制。"));

         // 創建並發送玩家稱號的封包
         S_CharTitle title = new S_CharTitle(pc.getId(), pc.getTitle());

         pc.sendPackets((ServerBasePacket)title, false);
         Broadcaster.broadcastPacket((L1Character)pc, (ServerBasePacket)title);

         try {
             // 保存玩家狀態
             pc.save();
         } catch (Exception e) {
             // 捕捉並打印異常
             e.printStackTrace();
         }
     }

     clan.addClanMember(pc.getName(), pc.getClanRank(), pc.getLevel(), "", pc.getId(), pc.getType(), pc.getOnlineStatus(), pc);
     pc.sendPackets((ServerBasePacket)new S_PacketBox(27, 8, pc.getName()));
     pc.sendPackets((ServerBasePacket)new S_ClanName(pc, clan.getEmblemId(), 8));

     pc.sendPackets((ServerBasePacket)new S_ServerMessage(95, pc.getClanname()));
     pc.sendPackets((ServerBasePacket)new S_Pledge(pc.getClanid()));
     pc.sendPackets((ServerBasePacket)new S_ReturnedStat(pc.getId(), clan.getClanId()));
     pc.sendPackets((ServerBasePacket)new S_PacketBox(173, clan.getEmblemStatus()));
     for (L1PcInstance player : clan.getOnlineClanMember()) {

       player.sendPackets((ServerBasePacket)new S_ReturnedStat(pc.getId(), pc.getClan().getEmblemId()));
       player.broadcastPacket((ServerBasePacket)new S_ReturnedStat(player.getId(), pc.getClan().getEmblemId()));
     }

     pc.sendPackets(SC_BLOOD_PLEDGE_JOIN_ACK.sendClan(pc, clan.getClanName(), 0, 0));
   }


     // 方法：將玩家從一個血盟移動到另一個血盟
     private boolean move_clan(L1PcInstance join_pc, L1Clan move_clan) throws Exception {
         // 獲取玩家當前所在的血盟
         L1Clan clan = join_pc.getClan();

         // 如果玩家不屬於任何血盟，則返回true
         if (clan == null) {
             return true;
         }

         // 如果玩家不是血盟的領袖，則發送信息並返回false
         if (clan.getLeaderId() != join_pc.getId()) {
             join_pc.sendPackets("只有血盟的盟主才能進行血盟轉移。");
             return false;
         }

         // 移除玩家在當前血盟中的房屋
         remove_clan_house(join_pc, clan);

         // 刪除玩家在當前血盟中的信息並將其移動到新血盟
         delete_clan(join_pc, clan, move_clan);

         // 成功移動返回true
         return true;
     }

   private void delete_clan(L1PcInstance leader, L1Clan clan, L1Clan move_clan) throws Exception {
     L1PcInstance pc = null;
     int clan_id = clan.getClanId();
     String leader_name = leader.getName();
     String clan_name = clan.getClanName();
     S_ServerMessage msg = new S_ServerMessage(269, leader_name, clan_name);
     for (int i = 0; i < clan.getClanMemberList().size(); i++) {

       pc = L1World.getInstance().getPlayer(((L1Clan.ClanMember)clan.getClanMemberList().get(i)).name);
       if (pc == null) {

         pc = CharacterTable.getInstance().restoreCharacter(((L1Clan.ClanMember)clan.getClanMemberList().get(i)).name);
         pc.movePlayerClanData(move_clan);
       }
       else {

         pc.sendPackets((ServerBasePacket)msg, false);
         pc.ClearPlayerClanData(clan);
         ClanJoin(move_clan, pc);
       }
     }
     msg.clear();
     String emblem_file = String.valueOf(clan_id);
     File file = new File("emblem/" + emblem_file);
     file.delete();
     ClanTable.getInstance().deleteClan(clan_name);
   }

   private void remove_clan_house(L1PcInstance leader, L1Clan clan) {
     int clan_house_id = clan.getHouseId();
     if (clan_house_id <= 0) {
       return;
     }
     AuctionBoardTable boardTable = new AuctionBoardTable();
     L1AuctionBoard board = new L1AuctionBoard();
     L1House house = HouseTable.getInstance().getHouseTable(clan_house_id);
     if (house == null || clan_house_id != house.getHouseId()) {
       return;
     }
     board.setHouseId(clan_house_id);
     board.setHouseName(house.getHouseName());
     board.setHouseArea(house.getHouseArea());
     TimeZone tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
     Calendar cal = Calendar.getInstance(tz);
     cal.add(5, 1);
     cal.set(12, 0);
     cal.set(13, 0);
     board.setDeadline(cal);
     board.setPrice(1000000);
     board.setLocation(house.getLocation());
     board.setOldOwner(leader.getName());
     board.setOldOwnerId(leader.getId());
     board.setBidder("");
     board.setBidderId(0);
     boardTable.insertAuctionBoard(board);
     house.setOnSale(true);
     house.setPurchaseBasement(true);
     HouseTable.getInstance().updateHouse(house);
   }



   public boolean ClanJoin(L1Clan clan, L1PcInstance joinPc) throws Exception {
     int clan_id = clan.getClanId();
     String clanName = clan.getClanName();




     int maxMember = 0;

     if (Config.ServerAdSetting.CLANMAXUSERCOUNT > 0) {
       maxMember = Config.ServerAdSetting.CLANMAXUSERCOUNT;
     }

     if (maxMember <= clan.getClanMemberList().size()) {
       joinPc.sendPackets((ServerBasePacket)new S_ServerMessage(188, clan.getLeaderName()));
       return false;
     }

     if (!move_clan(joinPc, clan)) {
       return false;
     }
     move_clan(joinPc, clan);
     for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
       clanMembers.sendPackets((ServerBasePacket)new S_ServerMessage(94, joinPc.getName()));
     }
     joinPc.setClanid(clan_id);
     joinPc.setClanname(clanName);
     joinPc.setClanRank(8);
     joinPc.setClanMemberNotes("");
     joinPc.setTitle("");
     joinPc.sendPackets((ServerBasePacket)new S_CharTitle(joinPc.getId(), ""));
     Broadcaster.broadcastPacket((L1Character)joinPc, (ServerBasePacket)new S_CharTitle(joinPc.getId(), ""));
     try {
       joinPc.save();
     } catch (Exception exception) {}


     clan.addClanMember(joinPc.getName(), joinPc.getClanRank(), joinPc.getLevel(), "", joinPc.getId(), joinPc.getType(), joinPc.getOnlineStatus(), joinPc);
     joinPc.sendPackets((ServerBasePacket)new S_ClanName(joinPc, clan.getEmblemId(), 8));
     for (L1PcInstance player : clan.getOnlineClanMember()) {
       player.sendPackets((ServerBasePacket)new S_ReturnedStat(joinPc.getId(), joinPc.getClan().getEmblemId()));
       player.broadcastPacket((ServerBasePacket)new S_ReturnedStat(player.getId(), joinPc.getClan().getEmblemId()));
     }
     ClanBuffListLoader.getInstance().load_clan_buff(joinPc);
     joinPc.sendPackets((ServerBasePacket)new S_PacketBox(173, clan.getEmblemStatus()));
     joinPc.sendPackets(SC_BLOOD_PLEDGE_JOIN_ACK.sendClan(joinPc, clanName, 0, 0));
     joinPc.start_teleport(joinPc.getX(), joinPc.getY(), joinPc.getMapId(), joinPc.getHeading(), 18339, false, false);









     return true;
   }

   public boolean ClanJoin(L1PcInstance pc, L1PcInstance joinPc) {
     int clan_id = pc.getClanid();
     String clanName = pc.getClanname();
     L1Clan clan = L1World.getInstance().getClan(clan_id);
     if (clan != null) {
       int maxMember = 0;

       int charisma = 0;
       if (pc.getId() != clan.getLeaderId()) {
         charisma = pc.getAbility().getTotalCha();
       } else {
         charisma = getOfflineClanLeaderCha(clan.getLeaderId());
       }
       boolean lv45quest = false;
       if (pc.getQuest().isEnd(3)) {
         lv45quest = true;
       }
       if (pc.getLevel() >= 50) {
         if (lv45quest == true) {
           maxMember = charisma * 9;
         } else {
           maxMember = charisma * 3;
         }

       } else if (lv45quest == true) {
         maxMember = charisma * 6;
       } else {
         maxMember = charisma * 2;
       }

       if (Config.ServerAdSetting.CLANMAXUSERCOUNT > 0) {
         maxMember = Config.ServerAdSetting.CLANMAXUSERCOUNT;
       }

       if (joinPc.getClanid() == 0) {
         if (maxMember <= clan.getClanMemberList().size()) {
           joinPc.sendPackets((ServerBasePacket)new S_ServerMessage(188, pc.getName()));
           return false;
         }
         for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
           clanMembers.sendPackets((ServerBasePacket)new S_ServerMessage(94, joinPc.getName()));
         }
         joinPc.setClanid(clan_id);
         joinPc.setClanname(clanName);
         joinPc.setClanRank(8);
         joinPc.setClanMemberNotes("");
         joinPc.setTitle("");
         joinPc.sendPackets((ServerBasePacket)new S_CharTitle(joinPc.getId(), ""));
         Broadcaster.broadcastPacket((L1Character)joinPc, (ServerBasePacket)new S_CharTitle(joinPc.getId(), ""));
         try {
           joinPc.save();
         } catch (Exception exception) {}
         ClanBuffListLoader.getInstance().load_clan_buff(joinPc);
         clan.addClanMember(joinPc.getName(), joinPc.getClanRank(), joinPc.getLevel(), "", joinPc.getId(), joinPc.getType(), joinPc.getOnlineStatus(), joinPc);
         joinPc.sendPackets((ServerBasePacket)new S_PacketBox(27, 8, joinPc.getName()));
         joinPc.sendPackets((ServerBasePacket)new S_ServerMessage(95, clanName));
         joinPc.sendPackets((ServerBasePacket)new S_Pledge(joinPc.getClanid()));
         joinPc.sendPackets((ServerBasePacket)new S_ReturnedStat(joinPc.getId(), clan.getClanId()));
         joinPc.sendPackets((ServerBasePacket)new S_PacketBox(173, pc.getClan().getEmblemStatus()));
         for (L1PcInstance player : clan.getOnlineClanMember()) {
           player.sendPackets((ServerBasePacket)new S_ReturnedStat(joinPc.getId(), joinPc.getClan().getEmblemId()));
           player.broadcastPacket((ServerBasePacket)new S_ReturnedStat(player.getId(), joinPc.getClan().getEmblemId()));

         }


       }
       else if (Config.ServerAdSetting.CLANALLIANCE) {
         changeClan(pc, joinPc, maxMember);
       } else {
         joinPc.sendPackets((ServerBasePacket)new S_ServerMessage(89));
       }

     } else {

       return false;
     }
     return true;
   }

   private void changeClan(L1PcInstance pc, L1PcInstance joinPc, int maxMember) {
     int clanId = pc.getClanid();
     String clanName = pc.getClanname();
     L1Clan clan = L1World.getInstance().getClan(clanId);
     int clanNum = clan.getClanMemberList().size();

     int oldClanId = joinPc.getClanid();
     String oldClanName = joinPc.getClanname();
     L1Clan oldClan = L1World.getInstance().getClan(oldClanId);
     int oldClanNum = oldClan.getClanMemberList().size();
     if (clan != null && oldClan != null && joinPc.isCrown() && joinPc
       .getId() == oldClan.getLeaderId()) {
       if (maxMember < clanNum + oldClanNum) {
         joinPc.sendPackets((ServerBasePacket)new S_ServerMessage(188, pc.getName()));

         return;
       }
       L1PcInstance[] clanMember = clan.getOnlineClanMember();
       for (int cnt = 0; cnt < clanMember.length; cnt++) {
         clanMember[cnt].sendPackets((ServerBasePacket)new S_ServerMessage(94, joinPc.getName()));
       }

       pc.setClanRank(8);
       pc.sendPackets((ServerBasePacket)new S_PacketBox(27, 8, pc.getName()));
       try {
         pc.save();
       } catch (Exception e1) {
         e1.printStackTrace();
       }

       for (int i = 0; i < oldClan.getClanMemberList().size(); i++) {
         L1PcInstance oldClanMember = L1World.getInstance().getPlayer(((L1Clan.ClanMember)oldClan.getClanMemberList().get(i)).name);
         if (oldClanMember != null) {
           oldClanMember.setClanid(clanId);
           oldClanMember.setClanname(clanName);
           oldClanMember.setClanRank(8);

           try {
             oldClanMember.save();
           } catch (Exception e) {
             _log.log(Level.SEVERE, "C_Attr[changeClan]Error", e);
           }
           clan.addClanMember(oldClanMember.getName(), oldClanMember.getClanRank(), oldClanMember.getLevel(), oldClanMember
               .getClanMemberNotes(), oldClanMember.getId(), oldClanMember.getType(), oldClanMember.getOnlineStatus(), oldClanMember);

           oldClanMember.sendPackets((ServerBasePacket)new S_PacketBox(27, 8, oldClanMember.getName()));
           oldClanMember.sendPackets((ServerBasePacket)new S_ServerMessage(95, clanName));
           oldClanMember.sendPackets((ServerBasePacket)new S_Pledge(oldClanMember.getClanid()));
           oldClanMember.sendPackets((ServerBasePacket)new S_ReturnedStat(oldClanMember.getId(), clan.getClanId()));
           oldClanMember.sendPackets((ServerBasePacket)new S_PacketBox(173, pc.getClan().getEmblemStatus()));
           for (L1PcInstance player : clan.getOnlineClanMember()) {
             player.sendPackets((ServerBasePacket)new S_ReturnedStat(oldClanMember.getId(), oldClanMember.getClan().getEmblemId()));
             player.broadcastPacket((ServerBasePacket)new S_ReturnedStat(player.getId(), oldClanMember.getClan().getEmblemId()));
           }
         } else {
           try {
             L1PcInstance offClanMember = CharacterTable.getInstance().restoreCharacter(((L1Clan.ClanMember)oldClan.getClanMemberList().get(i)).name);
             offClanMember.setClanid(clanId);
             offClanMember.setClanname(clanName);
             offClanMember.setClanRank(8);
             offClanMember.save();
             clan.addClanMember(offClanMember.getName(), offClanMember.getClanRank(), offClanMember.getLevel(), offClanMember
                 .getClanMemberNotes(), offClanMember.getId(), offClanMember.getType(), offClanMember.getOnlineStatus(), offClanMember);
           } catch (Exception e) {
             _log.log(Level.SEVERE, "C_Attr[changeClan]Error", e);
           }
         }
       }

       String emblem_file = String.valueOf(oldClanId);
       File file = new File("emblem/" + emblem_file);
       file.delete();
       ClanTable.getInstance().deleteClan(oldClanName);
     }
   }


   public int getOfflineClanLeaderCha(int member) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT Cha FROM characters WHERE objid=?");
       pstm.setInt(1, member);
       rs = pstm.executeQuery();
       if (!rs.next()) return 0;

       return rs.getInt("Cha");
     }
     catch (Exception e) {
       _log.warning("could not check existing charname:" + e.getMessage());
       System.out.println("could not check existing charname:" + e.getMessage());
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
     return 0;
   }


     public LeaderInfo getOfflineClanLeaderInfo(int objid) {

     Connection con = null;

     PreparedStatement pstm = null;

     ResultSet rs = null;

     try {

         // 獲取數據庫連接

         con = L1DatabaseFactory.getInstance().getConnection();


         // 準備查詢語句來獲取角色信息

         pstm = con.prepareStatement("SELECT char_name, level, Cha FROM characters where objid=?");

         pstm.setInt(1, objid);

         rs = pstm.executeQuery();


         // 如果查詢沒有結果，返回null

         if (!rs.next()) {

             return null;

         }

         // 創建LeaderInfo對象並設置其屬性

         LeaderInfo info = new LeaderInfo();

         info.name = rs.getString("char_name");

         info.cha = rs.getInt("Cha");

         info.lvl = rs.getInt("level");

         // 關閉結果集和語句

         SQLUtil.close(rs);

         SQLUtil.close(pstm);

         // 準備查詢語句來檢查角色的任務狀態

         pstm = con.prepareStatement("SELECT * FROM character_quests where char_id=? and quest_id=?");


         pstm.setInt(1, objid);

         pstm.setInt(2, 3);

         rs = pstm.executeQuery();

         // 設置45級任務狀態

         if (!rs.next()) {

             info.is45Quest = false;

         } else {

             info.is45Quest = (rs.getInt("quest_step") == 255);

         }

         // 返回LeaderInfo對象

         return info;

     } catch (Exception e) {
         // 捕捉異常並打印警告信息

         _log.warning("無法檢查現有的角色名:" + e.getMessage());

         System.out.println("無法檢查現有的角色名:" + e.getMessage());

     } finally {
         // 確保結果集、語句和連接在最終塊中被關閉

         SQLUtil.close(rs);

         SQLUtil.close(pstm);

         SQLUtil.close(con);

     }
     // 如果發生異常，返回null
         return null;


     class LeaderInfo {
         // 成員變數：姓名
         public String name;
         // 成員變數：等級
         public int lvl;
         // 成員變數：魅力值
         public int cha;
         // 成員變數：是否完成45級任務
         public boolean is45Quest;

         // 覆寫toString方法，返回LeaderInfo的字符串表示
         public String toString() {
             StringBuilder sb = new StringBuilder(128);
             // 添加姓名信息
             sb.append("角色名稱 : ").append(this.name).append("\n");
             // 添加等級信息
             sb.append("等 級 : ").append(this.lvl).append("\n ");
             // 添加魅力值信息
             sb.append("魅力值 : ").append(this.cha).append("\n");
             // 添加任務狀態信息
             sb.append("任務狀態 : ").append(this.is45Quest).append("\n");
             // 返回字符串表示
             return sb.toString();
         }
     }

