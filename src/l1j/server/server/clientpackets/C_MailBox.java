 package l1j.server.server.clientpackets;

 import MJFX.UIAdapter.MJUIAdapter;
 import java.sql.Timestamp;
 import java.util.concurrent.ConcurrentHashMap;
 import java.util.concurrent.CopyOnWriteArrayList;
 import l1j.server.Config;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
 import l1j.server.MJTemplate.MJSimpleRgb;
 import l1j.server.server.GMCommands;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.LetterTable;
 import l1j.server.server.datatables.SpamTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1ExcludingList;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_LetterList;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;











 public class C_MailBox
   extends ClientBasePacket
 {
   public static final int TYPE_PRIVATE_MAIL = 0;
   public static final int TYPE_BLOODPLEDGE_MAIL = 1;
   public static final int TYPE_KEPT_MAIL = 2;
   public static final int READ_PRIVATE_MAIL = 16;
   public static final int READ_BLOODPLEDGE_MAIL = 17;
   public static final int READ_KEPT_MAIL_ = 18;
   public static final int WRITE_PRIVATE_MAIL = 32;
   public static final int WRITE_BLOODPLEDGE_MAIL = 33;
   public static final int DEL_PRIVATE_MAIL = 48;
   public static final int DEL_BLOODPLEDGE_MAIL = 49;
   public static final int DEL_KEPT_MAIL = 50;
   public static final int TO_KEEP_MAIL = 64;
   public static final int PRICE_PRIVATEMAIL = 50;
   public static final int DEL_PRIVATE_LIST_MAIL = 96;
   public static final int DEL_BLOODPLEDGE_LIST_MAIL = 97;
   public static final int DEL_KEEP_LIST = 98;
   public static final int PRICE_BLOODPLEDGEMAIL = 1000;
   public static final int SIZE_PRIVATE_MAILBOX = 40;
   public static final int SIZE_BLOODPLEDGE_MAILBOX = 80;
   public static final int SIZE_KEPTMAIL_MAILBOX = 10;
   private static final String C_MailBox = "[C] C_MailBox";

   public C_MailBox(byte[] abyte0, GameClient client) {
     super(abyte0);
     int type = readC();

     if (client == null) {
       return;
     }

     L1PcInstance pc = client.getActiveChar();

     if (pc == null) {
       return;
     }
     switch (type) {

       case 16:
         ReadLetter(pc, 16, 0);
         break;
       case 17:
         ReadLetter(pc, 17, 0);
         break;
       case 18:
         ReadLetter(pc, 18, 0);
         break;
       case 32:
         WritePrivateMail(pc);
         break;
       case 33:
         WriteBloodPledgeMail(pc);
         break;
       case 48:
         DeleteLetter(pc, 48, 0);
         break;
       case 49:
         DeleteLetter(pc, 49, 1);
         break;
       case 50:
         DeleteLetter(pc, 50, 2);
         break;
       case 64:
         SaveLetter(pc, 64, 2);
         break;
       case 96:
         DeleteLetter_List(pc, 48, 40);
         break;
       case 97:
         DeleteLetter_List(pc, 49, 80);
         break;
       case 98:
         DeleteLetter_List(pc, 50, 10);
         break;
     }
   }



   private void DeleteLetter_List(L1PcInstance pc, int deletetype, int type) {
     int delete_num = readD();
     for (int i = 0; i < delete_num; i++) {
       int id = readD();
       LetterTable.getInstance().deleteLetter(id);
       pc.sendPackets((ServerBasePacket)new S_LetterList(pc, deletetype, id, true));
     }
   }

   private boolean payMailCost(L1PcInstance RECEIVER, int PRICE) {
     int AdenaCnt = RECEIVER.getInventory().countItems(40308);
     if (AdenaCnt < PRICE) {
       RECEIVER.sendPackets((ServerBasePacket)new S_ServerMessage(189, ""));
       return false;
     }

     RECEIVER.getInventory().consumeItem(40308, PRICE);
     return true;
   }

   private static final ConcurrentHashMap<Integer, Integer> letterPendingCount = new ConcurrentHashMap<>();


     private void WritePrivateMail(L1PcInstance sender, String receiver, String subject, String content, int paper) {
         long postdelaytime = System.currentTimeMillis() / 1000L;
         if (!sender.isGm() && sender.getPostDelay() + 1L > postdelaytime) {
             long time = sender.getPostDelay() + 1L - postdelaytime;
             sender.sendPackets(time + "秒後才能發送。(連續發送將導致客戶端關閉)");
             System.out.println(sender.getName() + " 嘗試發送大量郵件。");
             int count = ((Integer)letterPendingCount.getOrDefault(Integer.valueOf(sender.getId()), Integer.valueOf(0))).intValue();
             if (count > 10) {
                 try {
                     letterPendingCount.remove(Integer.valueOf(sender.getId()));
                     sender.sendPackets((ServerBasePacket)new S_PacketBox(84, "因為瞬間發送大量郵件，客戶端將被關閉。"));
                     sender.getNetConnection().close();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             } else {
                 letterPendingCount.put(Integer.valueOf(sender.getId()), Integer.valueOf(count + 1));
             }
             return;
         }
                // 檢查發送郵件的延遲時間，如果發送間隔不足1秒，則進行相應處理

         letterPendingCount.remove(Integer.valueOf(sender.getId()));

         if (sender.isGm() && receiver.equalsIgnoreCase("公告")) {
             L1World.getInstance().broadcastPacketToAll(SC_NOTIFICATION_MESSAGE.make_stream(content, MJSimpleRgb.green(), Integer.parseInt(subject)));
             return;
         }
            // 如果發送者是GM且接收者是"공지"（公告），則向所有玩家廣播消息

         if (sender.getLevel() < Config.ServerAdSetting.LETTERLEVEL) {
             sender.sendPackets((ServerBasePacket)new S_SystemMessage("等級" + Config.ServerAdSetting.LETTERLEVEL + "以下無法發送郵件。"));
             return;
         }
            // 如果發送者等級低於配置的信件發送等級，則發送失敗消息

         if (subject.length() > Config.ServerAdSetting.LETTER_SUBJECT) {
             sender.sendPackets("郵件標題過長，發送失敗。請在內容中輸入。");
             return;
         }
            // 如果郵件標題超過配置的最大長度，則發送失敗消息

         if (content.length() > Config.ServerAdSetting.LETTER_CONTENT) {
             sender.sendPackets("郵件內容過長，發送失敗。");
             return;
         }
            // 如果郵件內容的長度超過配置的最大長度，則發送失敗消息並返回

         if (!payMailCost(sender, 50)) {
             sender.sendPackets("金幣不足。");
             return;
         }
            // 如果支付郵件費用失敗（即發送者的阿德納不足），則發送失敗消息並返回

     Timestamp dTime = new Timestamp(System.currentTimeMillis());
     if (!checkCountMail(sender, receiver, 0, 40)) {
       return;
     }


     L1PcInstance target = L1World.getInstance().getPlayer(receiver);
     if (target != null) {
       L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(target.getId());
       if (exList.contains(1, sender.getName())) {
         sender.sendPackets((ServerBasePacket)new S_ServerMessage(3082));
         return;
       }
     }
     int id = LetterTable.getInstance().writeLetter(paper, dTime, sender.getName(), receiver, 0, subject, content);
     MJUIAdapter.on_receive_letter(id, sender.getName(), subject, content, dTime, receiver);
     if (target != null && target.getOnlineStatus() != 0) {
       target.sendPackets((ServerBasePacket)new S_LetterList(80, id, 0, sender.getName(), subject));

       if (target.isGm() &&
         GMCommands._sleepingMessage != null && !GMCommands._sleepingMessage.equalsIgnoreCase("")) {
         WritePrivateMail(target, sender.getName(), GMCommands._sleepingTitle, GMCommands._sleepingMessage, paper);
       }
     }
     sender.sendPackets((ServerBasePacket)new S_LetterList(80, id, 1, sender.getName(), subject));
     sender.sendPackets((ServerBasePacket)new S_LetterList(32, true));
     sender.setPostDelay(postdelaytime + 5L);
     sender.sendPackets((ServerBasePacket)new S_LetterList(sender, 48, id, true));
   }

   private void WritePrivateMail(L1PcInstance sender) {
     int paper = readH();
     String receiverName = readS();
     String subject = readSS();
     String content = readSS();

     WritePrivateMail(sender, receiverName, subject, content, paper);
   }

     private void WriteBloodPledgeMail(L1PcInstance sender) {
         if (!payMailCost(sender, 1000)) {
             return;
         }
            // 如果支付郵件費用失敗（即發送者的阿德納不足），則返回，停止後續處理

         if (sender == null) {
             return;
         }
            // 如果發送者為空（null），則返回，停止後續處理

         long postdelaytime = System.currentTimeMillis() / 1000L;
         if (!sender.isGm() && sender.getPostDelay() + 1L > postdelaytime) {
             long time = sender.getPostDelay() + 1L - postdelaytime;
             sender.sendPackets(time + "秒後才能發送。");

             return;
         }
            // 獲取當前時間（秒）並檢查發送郵件的延遲時間，如果發送間隔不足1秒，則發送延遲消息並返回，停止後續處理

     int paper = readH();

     Timestamp dTime = new Timestamp(System.currentTimeMillis());
     String receiverName = readS();
     String subject = readSS();
     String content = readSS();

     L1Clan targetClan = null;
     for (L1Clan clan : L1World.getInstance().getAllClans()) {
       if (clan.getClanName().toLowerCase().equals(receiverName.toLowerCase())) {
         targetClan = clan;

         break;
       }
     }
     if (targetClan == null) {
       return;
     }


     L1PcInstance target = null;
     CopyOnWriteArrayList<L1Clan.ClanMember> clanMemberList = targetClan.getClanMemberList();
     int id = 0;
     try {
       for (int i = 0, a = clanMemberList.size(); i < a; i++) {
         String name = ((L1Clan.ClanMember)clanMemberList.get(i)).name;
         target = L1World.getInstance().getPlayer(name);
         if (checkCountMail(sender, name, 1, 80))
         {
           if (!name.equalsIgnoreCase(sender.getName())) {

             id = LetterTable.getInstance().writeLetter(paper, dTime, sender.getName(), name, 1, subject, content);

             if (target != null && target.getOnlineStatus() != 0)
               target.sendPackets((ServerBasePacket)new S_LetterList(81, id, 0, sender.getName(), subject));
           }  }
       }
     } catch (Exception e) {
       e.printStackTrace();
     }

     sender.sendPackets((ServerBasePacket)new S_LetterList(81, id, 1, sender.getName(), subject));
     sender.sendPackets((ServerBasePacket)new S_LetterList(33, true));
     sender.setPostDelay(postdelaytime + 5L);
   }

   private void DeleteLetter(L1PcInstance pc, int type, int letterType) {
     int id = readD();
     LetterTable.getInstance().deleteLetter(id);
     if (type == 48)
       MJUIAdapter.on_delete_letter(id);
     pc.sendPackets((ServerBasePacket)new S_LetterList(pc, type, id, true));
   }

   private void ReadLetter(L1PcInstance pc, int type, int read) {
     int id = readD();
     LetterTable.getInstance().CheckLetter(id);
     if (type == 16)
       MJUIAdapter.on_check_letter(id);
     pc.sendPackets((ServerBasePacket)new S_LetterList(pc, type, id, read));
   }

   private void LetterList(L1PcInstance pc, int type, int count) {
     pc.sendPackets((ServerBasePacket)new S_LetterList(pc, type, count));
   }

   private void SaveLetter(L1PcInstance pc, int type, int letterType) {
     int id = readD();
     LetterTable.getInstance().SaveLetter(id, letterType);
     pc.sendPackets((ServerBasePacket)new S_LetterList(pc, type, id, true));
   }

     private boolean checkCountMail(L1PcInstance from, String to, int type, int max) {
         int cntMailInMailBox = LetterTable.getInstance().getLetterCount(to, type);
            // 獲取接收者的郵件箱中指定類型的郵件數量

         if (cntMailInMailBox >= max) {
             from.sendPackets((ServerBasePacket)new S_SystemMessage(to + "的郵件箱已滿，無法發送新郵件。"));
             // 如果郵件數量已達到最大值，則發送系統消息通知發送者郵件箱已滿，無法發送新郵件

             return false;
         }
         return true;
        // 如果郵件數量未達到最大值，則返回true，表示可以發送新郵件
     }


   public String getType() {
     return "[C] C_MailBox";
   }
 }


