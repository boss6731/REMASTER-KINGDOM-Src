     package l1j.server.server.serverpackets;
     
     import java.io.UnsupportedEncodingException;
     import java.sql.Timestamp;
     import java.util.ArrayList;
     import java.util.Arrays;
     import l1j.server.MJTemplate.MJEncoding;
     import l1j.server.MJTemplate.MJString;
     import l1j.server.server.datatables.CharacterTable;
     import l1j.server.server.datatables.ClanTable;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1Clan;
     import l1j.server.server.storage.mysql.MySqlCharacterStorage;
     
     
     
     public class S_Pledge
       extends ServerBasePacket
     {
       private static final String _S_Pledge = "[S] _S_Pledge";
       private static final byte[] emptyMemo = new byte[470];
       
       public S_Pledge(int ClanId) {
         L1Clan clan = ClanTable.getInstance().getTemplate(ClanId);
         writeC(108);
         writeC(167);
         writeS(clan.getClanName());
         writeS(clan.getLeaderName());
         writeD(clan.getEmblemId());
         writeC((clan.getHouseId() != 0) ? 1 : 0);
         writeC((clan.getCastleId() != 0) ? 1 : 0);
         writeC(0);
         writeD(clan.getClanBirthDay().getTime() / 1000L);
     

         writeD(0);
         writeD(0);
         if (!MJString.isNullOrEmpty(clan.getAnnouncement())) {
           
           writeByte(clan.getAnnouncement().getBytes(MJEncoding.EUCKR));
         } else {
           writeByte(emptyMemo);
         } 
         writeH(0);
       }
       
       public S_Pledge(int page, int current_page, ArrayList<String> list) {
         writeC(108);
         writeC(170);
         writeC(page);
         writeC(current_page);
         writeC(list.size());
         for (String name : list) {
           if (name == null)
             continue;  try {
             L1PcInstance clanMember = CharacterTable.getInstance().restoreCharacter(name);
             if (clanMember != null) {
               writeS(clanMember.getName());
               if (clanMember.getAccountName().equals("MJBOT") && clanMember.getClanRank() == 0) {
                 writeC(7);
               } else {
                 writeC(clanMember.getClanRank());
               } 
               
               writeC(clanMember.getLevel());
               
               byte[] text = new byte[62];
               Arrays.fill(text, (byte)0);
               
               if (clanMember.getClanMemberNotes().length() != 0) {
                 int i = 0;
                 for (byte b : clanMember.getClanMemberNotes().getBytes("UTF-8")) {
                   text[i++] = b;
                 }
               } 
               writeByte(text);
               writeD(clanMember.getClanMemberId());
               writeC(clanMember.getType());
               Long jointime = Long.valueOf(Timestamp.valueOf(MySqlCharacterStorage.getClanJoinTime(name).toString()).getTime());
               writeD(jointime.longValue() / 1000L);
               
               write8x(clanMember.getClanContribution(), 2);
     
     
     
               
               Long logouttime = Long.valueOf(Timestamp.valueOf(clanMember.getLastLogoutTime().toString()).getTime());
     
               
               writeD(logouttime.longValue() / 30000000L);
     
     
               
               continue;
             }


                 if (name.equalsIgnoreCase("管理員")) {
                     // 檢查名字是否是 "매니저" （忽略大小寫）
                     writeS("馬提斯"); // 寫入名稱 "매티스"
                     writeC(4); // 寫入常量 4
                     writeC(0); // 寫入常量 0

                     // 初始化字節數組，長度為 62
                     byte[] text = new byte[62];
                     Arrays.fill(text, (byte) 0); // 將數組填充為 0

                     // 將 "신규혈맹" 的 UTF-8 字節填入數組中
                     int i = 0;
                     for (byte b : "新血盟".getBytes("UTF-8")) {
                         text[i++] = b;
                     }
                     writeByte(text); // 寫入字節數組
                     writeD(0); // 寫入整數 0
                     writeC(0); // 寫入常量 0
                     writeD(System.currentTimeMillis() / 1000L); // 寫入當前時間（秒）
                     write8x(0L, 2); // 寫入長整數 0 兩次
                     writeD(0); // 寫入整數 0
                 } catch (Exception exception) {
                     // 捕獲並忽略任何異常
                 }

                 writeH(0); // 寫入常量 0
     

       public S_Pledge(String name, String notes) {
         writeC(108);
         writeC(169);
         writeS(name);
         
         byte[] text = new byte[62];
         Arrays.fill(text, (byte)0);
         
         if (notes.length() != 0) {
           int i = 0;
           try {
             for (byte b : notes.getBytes("UTF-8")) {
               text[i++] = b;
             }
           } catch (UnsupportedEncodingException e) {
             e.printStackTrace();
           } 
         } 
         writeByte(text);
         writeH(0);
       }
       
       public S_Pledge(L1Clan clan, int bless) {
         writeC(19);
         writeH(138);
         writeC(8);
         write7B((clan.getBlessCount() / 10000));
         writeC(16);
         write7B(40000L);
         writeC(24);
         write7B(30000L);
         writeC(32);
         write7B(1000L);
         for (int i = 0; i < 4; i++) {
           int time = clan.getBuffTime()[i];
           if (time == 0)
             time = 172800; 
           writeC(42);
           write7B((27 + bitlengh(time)));
           writeC(10);
           writeC(bitlengh(time) + 6);
           writeC(8);
           write7B((2724 + i));
           writeC(16);
           write7B(time);
           writeC(24);
           writeC((clan.getBuffTime()[i] == 0) ? 1 : ((bless == i + 1) ? 2 : 3));
           writeC(18);
           writeS2("$" + Integer.toString(22503 + i));
           writeC(26);
           writeS2("$" + Integer.toString(22508 + i));
           writeC(32);
           write7B((7233 + i * 2));
         } 
         writeH(0);
       }
     
       
       public byte[] getContent() {
         return getBytes();
       }
     
       
       public String getType() {
         return "[S] _S_Pledge";
       }
     }


