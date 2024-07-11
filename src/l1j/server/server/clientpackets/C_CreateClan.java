 package l1j.server.server.clientpackets;

 import java.io.UnsupportedEncodingException;
 import l1j.server.Config;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOODPLEDGE_USER_INFO_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_BLOOD_PLEDGE_JOIN_ACK;
 import l1j.server.server.BadNamesList;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.ClanBuffTable;
 import l1j.server.server.datatables.ClanTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_EinhasadClanBuff;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_Pledge;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;




 public class C_CreateClan
   extends ClientBasePacket
 {
   private static final String C_CREATE_CLAN = "[C] C_CreateClan";

   private static boolean isAlphaNumeric(String s) {
     boolean flag = true;
     char[] ac = s.toCharArray();
     int i = 0;

     while (i < ac.length) {


       if (!Character.isLetterOrDigit(ac[i])) {
         flag = false;
         break;
       }
       i++;
     }
     return flag;
   }

   private static boolean isInvalidName(String name) {
     for (int i = 0; i < name.length(); i++) {
       if (name.charAt(i) == 'ㄱ' || name.charAt(i) == 'ㄲ' || name.charAt(i) == 'ㄴ' || name.charAt(i) == 'ㄷ' || name
         .charAt(i) == 'ㄸ' || name.charAt(i) == 'ㄹ' || name.charAt(i) == 'ㅁ' || name.charAt(i) == 'ㅂ' || name
         .charAt(i) == 'ㅃ' || name.charAt(i) == 'ㅅ' || name.charAt(i) == 'ㅆ' || name.charAt(i) == 'ㅇ' || name
         .charAt(i) == 'ㅈ' || name.charAt(i) == 'ㅉ' || name.charAt(i) == 'ㅊ' || name.charAt(i) == 'ㅋ' || name
         .charAt(i) == 'ㅌ' || name.charAt(i) == 'ㅍ' || name.charAt(i) == 'ㅎ' || name.charAt(i) == 'ㅛ' || name
         .charAt(i) == 'ㅕ' || name.charAt(i) == 'ㅑ' || name.charAt(i) == 'ㅐ' || name.charAt(i) == 'ㅔ' || name
         .charAt(i) == 'ㅗ' || name.charAt(i) == 'ㅓ' || name.charAt(i) == 'ㅏ' || name.charAt(i) == 'ㅣ' || name
         .charAt(i) == 'ㅠ' || name.charAt(i) == 'ㅜ' || name.charAt(i) == 'ㅡ' || name.charAt(i) == 'ㅒ' || name
         .charAt(i) == 'ㅖ' || name.charAt(i) == 'ㅢ' || name.charAt(i) == 'ㅟ' || name.charAt(i) == 'ㅝ' || name
         .charAt(i) == 'ㅞ' || name.charAt(i) == 'ㅙ' || name.charAt(i) == 'ㅚ' || name.charAt(i) == 'ㅘ' || name
         .charAt(i) == '씹' || name.charAt(i) == '좃' || name.charAt(i) == '좆' || name.charAt(i) == 'ㅤ') {
         return false;
       }
     }

            // 檢查角色名稱是否無效的函數
       private static boolean isInvalidName(String name) {
                // 如果名稱長度為0，返回false表示名稱無效
           if (name.length() == 0) {
               return false;
           }

           int numOfNameBytes = 0;
           try {
                // 獲取名稱的字節數（使用MS949編碼）
               numOfNameBytes = name.getBytes("MS949").length;
           } catch (UnsupportedEncodingException e) {
                // 處理不支持的編碼異常
               e.printStackTrace();
               return false;
           }

                // 如果名稱是字母數字組成，返回false表示名稱有效
           if (isAlphaNumeric(name)) {
               return false;
           }

        // 接下來還可能有更多的檢查邏輯

       }



     if (5 < numOfNameBytes - name.length() || 12 < numOfNameBytes) {
       return false;
     }

     if (BadNamesList.getInstance().isBadName(name)) {
       return false;
     }
     return true;
   }

     // C_CreateClan 類的構造函數
     public C_CreateClan(byte[] abyte0, GameClient clientthread) throws Exception {
         super(abyte0);

         // 讀取並轉換血盟名稱為小寫
         String s = readS().toLowerCase();

         // 獲取當前活動角色
         L1PcInstance l1pcinstance = clientthread.getActiveChar();

         // 如果角色為空，則返回
         if (l1pcinstance == null)
             return;

         // 檢查血盟名稱是否無效
         if (isInvalidName(s)) {
             // 如果名稱無效，發送系統消息並返回
             l1pcinstance.sendPackets((ServerBasePacket)new S_SystemMessage("錯誤的血盟名稱。"));
             return;
         }
     }
     if (l1pcinstance.isCrown()) {
       if (l1pcinstance.getClanid() == 0 && l1pcinstance.getLevel() >= Config.ServerAdSetting.CROWNBLOODLEVEL) {
         if (!l1pcinstance.getInventory().checkItem(40308, 10000)) {
           l1pcinstance.sendPackets((ServerBasePacket)new S_ServerMessage(337, "$4"));
           return;
         }
         for (L1Clan l1Clan : L1World.getInstance().getAllClans()) {
           if (l1Clan.getClanName().toLowerCase().equals(s.toLowerCase())) {
             l1pcinstance.sendPackets((ServerBasePacket)new S_ServerMessage(99));
             return;
           }
         }
         L1Clan clan = ClanTable.getInstance().createClan(l1pcinstance, s);
         l1pcinstance.getInventory().consumeItem(40308, 10000);
         if (clan != null) {
           l1pcinstance.sendPackets((ServerBasePacket)new S_ServerMessage(84, s));
           l1pcinstance.sendPackets(SC_BLOODPLEDGE_USER_INFO_NOTI.sendClanInfo(clan.getClanName(), l1pcinstance.getClanRank(), l1pcinstance));
           l1pcinstance.sendPackets((ServerBasePacket)new S_PacketBox(173, l1pcinstance.getClan().getEmblemStatus()));
           l1pcinstance.sendPackets((ServerBasePacket)new S_Pledge(l1pcinstance.getClanid()));
           l1pcinstance.sendPackets(SC_BLOOD_PLEDGE_JOIN_ACK.sendClan(l1pcinstance, clan.getClanName(), 0, 0));
           l1pcinstance.start_teleport(l1pcinstance.getX(), l1pcinstance.getY(), l1pcinstance.getMapId(), l1pcinstance.getHeading(), 18339, false);
         }


           // 註解: 將血盟的愛因哈薩德祝福緩衝設置為0
           clan.setEinhasadBlessBuff(0);

            // 註解: 隨機設置血盟的第一個緩衝buff
           clan.setBuffFirst(ClanBuffTable.getRandomBuff(clan));

            // 註解: 隨機設置血盟的第二個緩衝buff
           clan.setBuffSecond(ClanBuffTable.getRandomBuff(clan));

           // 註解: 隨機設置血盟的第三個緩衝buff
           clan.setBuffThird(ClanBuffTable.getRandomBuff(clan));

            // 註解: 发送愛因哈薩德血盟buff封包給指定角色
           l1pcinstance.sendPackets((ServerBasePacket)new S_EinhasadClanBuff(l1pcinstance), true);

            // 註解: 更新血盟信息到資料庫
           ClanTable.getInstance().updateClan(clan);
       } else {
            // 註解: 如果角色已經有血盟或等級低於設定的創建等級，發送對應的系統消息
           l1pcinstance.sendPackets((ServerBasePacket)new S_SystemMessage("已經有血盟或只有達到 " + Config.ServerAdSetting.CROWNBLOODLEVEL + " 級才能創建血盟。"));
       }
     } else {
            // 註解: 如果其他條件不滿足，發送錯誤消息
         l1pcinstance.sendPackets((ServerBasePacket)new S_ServerMessage(85));
     }
   }


   public String getType() {
     return "[C] C_CreateClan";
   }
 }


