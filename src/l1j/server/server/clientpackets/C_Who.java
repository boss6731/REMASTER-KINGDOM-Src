 package l1j.server.server.clientpackets;

 import l1j.server.Config;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WHOUSER_NOTI_PACKET;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.MapsTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_Who
   extends ClientBasePacket {
   private void onViewDetail(L1PcInstance gm, String name) {
     L1PcInstance pc = L1World.getInstance().getPlayer(name);
     SC_WHOUSER_NOTI_PACKET noti = SC_WHOUSER_NOTI_PACKET.newInstance();
     noti.set_currentusercount(L1World.getInstance().getAllPlayersSize());
     if (pc != null) {
       SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO uInfo = SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO.newInstance();
       SC_WHOUSER_NOTI_PACKET.ACCOUNT_INFO aInfo = SC_WHOUSER_NOTI_PACKET.ACCOUNT_INFO.newInstance();
       aInfo.set_accountname(pc.getAccountName());
       if (pc.getNetConnection() != null) {
         aInfo.set_ip(pc.getNetConnection().getIp());
         aInfo.set_ipkind(pc.getNetConnection().getIpBigEndian());
       } else {
         aInfo.set_ip("0.0.0.0");
         aInfo.set_ipkind(0);
       }
       aInfo.set_location(pc.getLongLocationReverse());
       aInfo.set_worldnumber(pc.getMapId());
       uInfo.set_accountinfo(aInfo);
       uInfo.set_alignstr((pc.getLawful() > 0) ? "Lawful" : ((pc.getLawful() < 0) ? "Chaotic" : "Natural"));
       String clanname = pc.getClanname();
       uInfo.set_pledge(MJString.isNullOrEmpty(clanname) ? "" : clanname);
       uInfo.set_serverno(0);
       uInfo.set_title(pc.getTitle());
       uInfo.set_username(pc.getName());
       noti.add_whouserinfo(uInfo);

       SC_WHOUSER_NOTI_PACKET.HuntingMapInfo mInfo = SC_WHOUSER_NOTI_PACKET.HuntingMapInfo.newInstance();

       mInfo.set_map_desc(445);
       mInfo.set_user_count(L1World.getInstance().getVisiblePlayers(pc.getMapId()).size());
       noti.add_hunting_map_info(mInfo);
     }
     gm.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_WHOUSER_NOTI_PACKET);
   }
   private static final String C_WHO = "[C] C_Who";
   public C_Who(byte[] decrypt, GameClient client) {
     super(decrypt);
     String s = readS();

     L1PcInstance pc = client.getActiveChar();
     if (pc == null) {
       return;
     }
     if (pc.isGm() && !s.equalsIgnoreCase("")) {
       onViewDetail(pc, s);

       return;
     }
       // 從 L1World 中獲取玩家實例
       L1PcInstance find = L1World.getInstance().getPlayer(s);

       float win = 0.0F;
       float lose = 0.0F;
       float total = 0.0F;
       float winner = 0.0F;
       String NameGm = "梅蒂斯";         // GM 名稱 1
       String NameGm1 = "米斯皮";      // GM 名稱 2
       String NameGm2 = "仙后座";    // GM 名稱 3

// 如果傳入的名稱與 NameGm 相同
       if (s.contentEquals(NameGm)) {
           // 向玩家發送訊息包，顯示 GM 名稱和其他資訊
           pc.sendPackets("\aA" + NameGm + " \fW(正義) \aHKakaoTalk\:[" + Config.Message.GMKAKAO + "]請給我發訊息! \\n\\r\f2Kill: 1 / \aGDeath: 0 / \aI[100.00%]");
           return;
       }

// 如果傳入的名稱與 NameGm1 相同
       if (s.contentEquals(NameGm1)) {
           // 向玩家發送訊息包，顯示 GM 名稱和其他資訊
           pc.sendPackets("\aA" + NameGm1 + " \fW(正義) \aHKakaoTalk\:[" + Config.Message.GMKAKAO + "]請給我發訊息! \\n\\r\f2Kill: 1 / \aGDeath: 0 / \aI[100.00%]");
           return;
       }

// 如果傳入的名稱與 NameGm2 相同
       if (s.contentEquals(NameGm2)) {
           // 向玩家發送訊息包，顯示 GM 名稱和其他資訊
           pc.sendPackets("\aA" + NameGm2 + " \fW(正義) \aHKakaoTalk\:[" + Config.Message.GMKAKAO + "]請給我發訊息! \\n\\r\f2Kill: 1 / \aGDeath: 0 / \aI[100.00%]");
           return;
       }

     String locName = MapsTable.getInstance().getMapName(pc.getMapId());
     if (locName == null) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(563));

       return;
     }
     if (find != null) {
       int i = pc.getMapId();
       int countPlayer = 0;
       for (L1Object each1 : L1World.getInstance().getVisibleObjects(i).values()) {
         if (each1 instanceof L1PcInstance) {
           countPlayer++;
         }
       }

       String title = "";
       if (!find.getTitle().equalsIgnoreCase("")) {
         title = find.getTitle() + " ";
       }

       if (find.getKDA() != null) {
         win = (find.getKDA()).kill;
         lose = (find.getKDA()).death;
       }

         // 獲取血盟名稱
         String clanname = find.getClanname();
// 初始化合法性描述
         String lawful = "";
         total = win + lose;
         if (total > 0.0F) {
             // 計算勝率
             winner = win * 100.0F / total;
         }

// 如果對象有血盟
         if (find.getClan() != null) {
             if (find.getLawful() == 0) {
                 lawful = "\fG(Nomal)(普通)";
             } else if (find.getLawful() > 0) {
                 lawful = "\fB(Lawful)(正義)";
             } else {
                 lawful = "\fG(Chaotic)(混沌)";
             }
             // 發送訊息包給玩家，顯示名稱、合法性、血盟名稱、殺敵數、死亡數和勝率
             pc.sendPackets(String.format("\aA%s%s \aE[%s]\f2Kill: %d / \aGDeath: %d / \aI[%.2f%%]",
             find.getName(), lawful, clanname,
                     Integer.valueOf((int)win), Integer.valueOf((int)lose), Float.valueOf(winner)));
         } else {
             if (find.getLawful() == 0) {
                 lawful = "\fG(Nomal)(普通)";
             } else if (find.getLawful() > 0) {
                 lawful = "\fB(Lawful)(正義)";
             } else {
                 lawful = "\aG(Chaotic)(混沌)";
             }
             // 發送訊息包給玩家，顯示名稱、合法性、頭銜、殺敵數、死亡數和勝率
             pc.sendPackets(String.format("\aA%s%s \aD%s \f2Kill: %d / \aGDeath: %d / \aI[%.2f%%]",find.getName(), lawful, title,
                     Integer.valueOf((int)win), Integer.valueOf((int)lose), Float.valueOf(winner)));
         }

// 檢查玩家的地圖ID和位置名稱
         if (pc.getMapId() == 0 || pc.getMapId() == 4 || locName == null) {
             return;
         }

// 發送訊息包告知當前位置的使用者數量
         pc.sendPackets(String.format("%s 使用者 : %d名稱", locName, Integer.valueOf(countPlayer * Config.ServerAdSetting.USERGHOSTCOUNT)));

// 如果玩家的地圖ID為0或4，或者位置名稱為null，結束方法
     } else {
         int i = pc.getMapId();
         int countPlayer = 0;
         for (L1Object each1 : L1World.getInstance().getVisibleObjects(i).values()) {
             // 計算當前地圖上玩家的數量
             if (each1 instanceof L1PcInstance) {
                 countPlayer++;
             }
         }

         // 如果玩家的地圖ID是0或4，或者位置名稱為空
         if (pc.getMapId() == 0 || pc.getMapId() == 4 || locName == null) {
             // 結束此方法
             return;
         }

// 向玩家發送訊息包，告知當前位置的使用者數量
         pc.sendPackets(String.format("%s 使用者 : %d名稱", new Object[] { locName, Integer.valueOf(countPlayer * Config.ServerAdSetting.USERGHOSTCOUNT) }));
     }









   public String getType() {
     return "[C] C_Who";
   }
 }


