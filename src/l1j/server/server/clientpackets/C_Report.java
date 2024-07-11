 package l1j.server.server.clientpackets;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.Timestamp;
 import java.util.ArrayList;
 import java.util.Random;
 import l1j.server.Config;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJNetSafeSystem.Distribution.MJClientStatus;
 import l1j.server.server.Account;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.ClanTable;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.datatables.ReportTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1NpcShopInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ChangeCharName;
 import l1j.server.server.serverpackets.S_CharAmount;
 import l1j.server.server.serverpackets.S_CharPass;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.S_케릭터생성;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1BookMark;
 import l1j.server.server.templates.L1ItemBookMark;
 import l1j.server.server.utils.CommonUtil;
 import l1j.server.server.utils.SQLUtil;














 public class C_Report
   extends ClientBasePacket
 {
   private static final String C_REPORT = "[C] C_Report";
   public static final int DragonMenu = 6;
   public static final int MINI_MAP_SEND = 11;
   public static final int MonsterKill = 44;
   public static final int BOOKMARK_SAVE = 34;
   public static final int BOOKMARK_COLOR = 39;
   public static final int BOOKMARK_LOADING_SAVE = 40;
   public static final int EMBLEM = 46;
   public static final int TELPORT = 48;
   public static final int 케릭터생성 = 43;
   public static final int 파워북검색 = 19;
   public static final int 상인찾기 = 49;
   public static final int 상점개설횟수 = 57;
   public static final int 자동신고 = 0;
   public static final int 시세검색 = 255;
   public static final int 케릭터비번생성 = 14;
   public static final int 케릭터비번변경 = 16;
   public static final int 케릭터비번인증 = 17;
   public static final int CHARNAME_CHANGED = 26;

   public C_Report(byte[] abyte0, GameClient client) throws Exception {
     super(abyte0); String password; int objid; String itemname, modPassword; int failure_count; L1Object obj; int shopitemid; L1PcInstance target; String name; int sizeColor; ReportTable rt; Timestamp date; Connection con; PreparedStatement pstm; int size, i, totalCount, emblemStatus, itemId; L1Clan clan; L1ItemInstance useItem; int mapIndex, point, locx, locy; String targetName; int mapid, x, y, Mid; L1PcInstance l1PcInstance1; String sourceName, destinationName; ServerBasePacket packet; Account acc;
     int type = readC();
     L1PcInstance pc = client.getActiveChar();



     switch (type) {
       case 14:
         if (!Config.Login.CharPassword)
           return;
         password = readSecondPassword();


         if (password == null) {
           System.out.println("kick1");
           client.kick();

           return;
         }

         if (client.getAccount().getCPW() != null && !client.getAccount().getCPW().equalsIgnoreCase("")) {
           System.out.println("kick2");
           client.kick();
           return;
         }
         client.getAccount().setCPW(password);
         client.getAccount().UpdateCharPassword(password);
         client.sendPacket((ServerBasePacket)new S_CharPass(17));
         break;


       case 16:
         if (!Config.Login.CharPassword) {
           return;
         }
         password = readSecondPassword();
         readC();
         modPassword = readSecondPassword();


         if (password == null || modPassword == null) {
           client.kick();

           return;
         }
         if (client.getAccount().getCPW() == null && client.getAccount().getCPW().equalsIgnoreCase("")) {
           return;
         }
         if (client.getAccount().getCPW().equals(password)) {
           client.getAccount().setCPW(modPassword);
           client.getAccount().UpdateCharPassword(modPassword);
           client.sendPacket((ServerBasePacket)new S_CharPass(19, true)); break;
         }
         client.sendPacket((ServerBasePacket)new S_CharPass(19, false));
         break;



       case 17:
         if (!Config.Login.CharPassword) {
           return;
         }
         password = readSecondPassword();

         if (password == null) {
           client.kick();

           return;
         }
         if (client.getAccount().getCPW() == null && client.getAccount().getCPW().equalsIgnoreCase("")) {
           return;
         }

         if (client.getAccount().getCPW().equals(password)) {
           client.reset_second_password_failure_count();
           if (client.getAccount().getwaitpacket() != null) {
             int op = client.getAccount().getwaitpacket()[0] & 0xFF;
             if (op == 168) {
               new C_LoginToServer(client.getAccount().getwaitpacket(), client); break;
             }  if (op == 81) {
               new C_DeleteChar(client.getAccount().getwaitpacket(), client); break;
             }
             System.out.println("無效的等待資料包： " + op);
           }  break;
         }
         failure_count = client.inc_second_password_failure_count();
         if (failure_count >= Config.Login.CharPasswordMaximumFailureCount) {
           client.kick();
           client.close();
           return;
         }
         client.sendPacket((ServerBasePacket)S_CharPass.do_fail_password(failure_count, Config.Login.CharPasswordMaximumFailureCount));
         break;


       case 43:
         if (Config.Login.CharPassword && (client
           .getAccount().getCPW() == null || client.getAccount().getCPW().equalsIgnoreCase(""))) {
           client.sendPacket((ServerBasePacket)new S_CharPass(23)); break;
         }
         client.sendPacket((ServerBasePacket)new S_케릭터생성());
         break;



         case 0:
             objid = readD(); // 讀取目標對象ID
             obj = L1World.getInstance().findObject(objid); // 根據ID查找目標對象

             if (!(obj instanceof L1PcInstance)) {
                 return; // 如果目標對象不是玩家實例，結束程式
             }

             target = (L1PcInstance) obj; // 將目標對象轉換為玩家實例

             if (target == null || target.isGm()) {
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("舉報目標不存在。")); // 如果目標不存在或是GM，發送系統消息
                 return;
             }
         if (!pc.isReport()) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(1021));
           return;
         }
         rt = ReportTable.getInstance();
         if (rt.isReport(target.getName())) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(1020));
           return;
         }
         date = new Timestamp(System.currentTimeMillis());
         rt.reportUpdate(target.getName(), pc.getName(), date);
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(1019));
         pc.startReportDeley();
         break;

         case 255:
             itemname = readS(); // 讀取物品名稱
             shopitemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(itemname); // 根據物品名稱查找物品ID

             if (shopitemid == 0) {
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("無法搜索到物品名稱。請重新輸入搜索詞。")); // 如果物品ID為0，發送系統消息
                 return;
             }
         break;


         case 49:
             name = readS(); // 讀取名稱
             if (name == null)
                 return; // 如果名稱為空，結束程式

             try {
                 String[] rep = name.split("-"); // 以 "-" 拆分名稱
                 if (rep.length >= 2 && rep[0].equalsIgnoreCase("LFC")) {
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage("請使用 .決鬥 指令。")); // 如果名稱的第一部分為 "LFC"，發送系統消息
                     break;
                 }
           if (pc.getMapId() == 800) {
             Random rnd = new Random(System.nanoTime());
             L1PcInstance pn = L1World.getInstance().getPlayer(name);
             if (pn != null && pn.getMapId() == 800 && pn.isPrivateShop()) {

               pc.setFindMerchantId(pn.getId());
               pc.start_teleport(pn.getX() + CommonUtil.random(3) - 1, pn.getY() + CommonUtil.random(3) - 1, pn
                   .getMapId(), 0, 18339, false, false);
             } else {
                 L1NpcShopInstance nn = L1World.getInstance().getShopNpc(name); // 根據名稱獲取NPC商店實例

                 if (nn != null && nn.getMapId() == 800 && nn.getState() == 1) { // 如果NPC存在，且位於地圖800，狀態為1
                     pc.setFindMerchantId(nn.getId()); // 設置玩家查找的商人ID
                     pc.start_teleport(
                             nn.getX() + CommonUtil.random(3) - 1, // 獲取NPC的X座標，加上隨機偏移
                             nn.getY() + CommonUtil.random(3) - 1, // 獲取NPC的Y座標，加上隨機偏移
                             nn.getMapId(), // 獲取NPC的地圖ID
                             0, 18339, // 傳送參數
                             false, false // 傳送標誌
                     );
                 } else {
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage("尋找商人：找不到您要找的商人。"), true); // 如果找不到NPC，發送系統消息
                 }
             rnd = null;
           }
         } catch (Exception exception) {}
         break;


       case 57:
         if (pc.getNetConnection() == null || pc.getNetConnection().getAccount() == null)
           return;
         pc.sendPackets((ServerBasePacket)new S_PacketBox(198, (pc.getNetConnection().getAccount()).Shop_open_count), true);
         break;

       case 39:
         sizeColor = readD();


         con = null;
         pstm = null;
         try {
           if (sizeColor != 0) {
             con = L1DatabaseFactory.getInstance().getConnection();
           }
           for (int j = 0; j < sizeColor; j++) {
             int Numid = readD();
             int id = 0;
             for (L1BookMark book : pc.getBookMarkArray()) {
               if (book.getNumId() == Numid) {
                 id = book.getId();
               }
             }
             String str = readS();
             str = str.replace("\\", "\\\\");
             pstm = con.prepareStatement("UPDATE character_teleport SET name='" + str + "' WHERE id='" + id + "'");
             pstm.execute();
           }
         } catch (Exception e) {
           e.printStackTrace();
         } finally {
           SQLUtil.close(pstm);
           SQLUtil.close(con);
         }
         break;
       case 34:
         if (pc == null) {
           return;
         }
         readC();

         size = pc._bookmarks.size();
         for (i = 0; i < size; i++) {
           int num = readC();
           ((L1BookMark)pc._bookmarks.get(i)).setTemp_id(num);
         }
           pc._speedbookmarks.clear(); // 清空玩家的快速書籤
           for (int i = 0; i < 5; i++) { // 循環5次
               int num = readC(); // 讀取一個數字
               if (num == 255)
                   return; // 如果讀取的數字是255，結束程式
               if (pc._bookmarks.size() - 1 < num) { // 如果書籤數量小於讀取的數字
                   System.out.println("書籤大小錯誤 " + pc.getName() + " num= " + num + " size= " + pc._bookmarks.size()); // 輸出錯誤信息
                   return; // 結束程式
               }
           }
           ((L1BookMark)pc._bookmarks.get(num)).setSpeed_id(i);
           pc._speedbookmarks.add(pc._bookmarks.get(num));
         }
         break;
       case 40:
         if (pc.getBookMarkSize() <= 0) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(2963));
           return;
         }
           totalCount = pc.getInventory().getSize(); // 獲取玩家背包的物品總數

           if (pc.getInventory().getWeight100() > 82 || totalCount >= 200) { // 如果玩家背包重量超過82%或物品總數大於等於200
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("背包已滿，無法創建記憶之珠。")); // 發送系統消息通知玩家背包已滿，無法創建記憶之珠
               return; // 結束程式
           }
         itemId = readD();
         useItem = pc.getInventory().getItem(itemId);
         기억저장구슬(pc, useItem);
         break;




       case 44:
         pc.setMonsterkill(0);
         break;



       case 46:
         if (pc.getClanRank() != 4 && pc.getClanRank() != 10) {
           return;
         }
         emblemStatus = readC();
         clan = pc.getClan();
         clan.setEmblemStatus(emblemStatus);
         ClanTable.getInstance().updateClan(clan);

         for (L1PcInstance member : clan.getOnlineClanMember()) {
           member.sendPackets((ServerBasePacket)new S_PacketBox(173, emblemStatus));
         }
         break;
       case 48:
         mapIndex = readH();
         point = readH();
         locx = 0;
         locy = 0;
         if (mapIndex == 1) {
           if (point == 0) {
             locx = 34079 + (int)(Math.random() * 12.0D);
             locy = 33136 + (int)(Math.random() * 15.0D);
           } else if (point == 1) {
             locx = 33970 + (int)(Math.random() * 10.0D);
             locy = 33243 + (int)(Math.random() * 14.0D);
           } else if (point == 2) {
             locx = 33925 + (int)(Math.random() * 14.0D);
             locy = 33351 + (int)(Math.random() * 9.0D);
           }
         } else if (mapIndex == 2) {
           if (point == 0) {
             locx = 32615 + (int)(Math.random() * 11.0D);
             locy = 32719 + (int)(Math.random() * 7.0D);
           } else if (point == 1) {
             locx = 32621 + (int)(Math.random() * 9.0D);
             locy = 32788 + (int)(Math.random() * 13.0D);
           }
         } else if (mapIndex == 3) {
           if (point == 0) {
             locx = 33501 + (int)(Math.random() * 11.0D);
             locy = 32765 + (int)(Math.random() * 9.0D);
           } else if (point == 1) {
             locx = 33440 + (int)(Math.random() * 11.0D);
             locy = 32784 + (int)(Math.random() * 11.0D);
           }
         } else if (mapIndex == 4) {
           if (point == 0) {
             locx = 32844 + (int)(Math.random() * 2.0D);
             locy = 32883 + (int)(Math.random() * 2.0D);
           } else if (point == 1) {
             locx = 32801 + (int)(Math.random() * 2.0D);
             locy = 32882 + (int)(Math.random() * 2.0D);
           } else if (point == 2) {
             locx = 32756 + (int)(Math.random() * 2.0D);
             locy = 32882 + (int)(Math.random() * 2.0D);
           } else if (point == 3) {
             locx = 32743 + (int)(Math.random() * 2.0D);
             locy = 32927 + (int)(Math.random() * 2.0D);
           } else if (point == 4) {
             locx = 32740 + (int)(Math.random() * 2.0D);
             locy = 32972 + (int)(Math.random() * 2.0D);
           } else if (point == 5) {
             locx = 32800 + (int)(Math.random() * 2.0D);
             locy = 32971 + (int)(Math.random() * 2.0D);
           } else if (point == 6) {
             locx = 32844 + (int)(Math.random() * 2.0D);
             locy = 32971 + (int)(Math.random() * 2.0D);
           } else if (point == 7) {
             locx = 32846 + (int)(Math.random() * 2.0D);
             locy = 32928 + (int)(Math.random() * 2.0D);
           } else if (point == 8) {
             locx = 32797 + (int)(Math.random() * 2.0D);
             locy = 32927 + (int)(Math.random() * 2.0D);
           }
         } else if (mapIndex == 5) {
           if (point == 0) {
             locx = 32577 + (int)(Math.random() * 11.0D);
             locy = 32933 + (int)(Math.random() * 7.0D);
           } else if (point == 1) {
             locx = 32629 + (int)(Math.random() * 9.0D);
             locy = 32957 + (int)(Math.random() * 13.0D);
           }
         }
         pc.start_teleport(locx, locy, pc.getMapId(), pc.getHeading(), 18339, true, false);
         pc.sendPackets((ServerBasePacket)new S_PacketBox(176, pc));
         break;



       case 11:
         targetName = null;
         mapid = 0; x = 0; y = 0; Mid = 0;
         try {
           targetName = readS();
           mapid = readH();
           x = readH();
           y = readH();
           Mid = readH();
         } catch (Exception e) {
           return;
         }
         l1PcInstance1 = L1World.getInstance().getPlayer(targetName);
         if (l1PcInstance1 == null) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(1782)); break;
         }  if (pc == l1PcInstance1) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(1785)); break;
         }
         l1PcInstance1.sendPackets((ServerBasePacket)new S_ServerMessage(1784, pc.getName()));
         l1PcInstance1.sendPackets((ServerBasePacket)new S_PacketBox(111, pc.getName(), mapid, x, y, Mid));
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(1783, l1PcInstance1.getName()));
         break;

       case 26:
         client.setStatus(MJClientStatus.CLNT_STS_CHANGENAME);
         sourceName = readS();
         destinationName = readS();
         if (!client.is_shift_transfer() &&
           client
           .getStatus().toInt() != MJClientStatus.CLNT_STS_CHANGENAME.toInt()) {
           client.close();

           return;
         }
         packet = S_ChangeCharName.doChangeCharName(client, sourceName, destinationName, true);
         if (packet != null)
           client.sendPacket(packet);
         acc = client.getAccount();
         client.sendPacket((ServerBasePacket)new S_CharAmount(acc.countCharacters(), acc.getCharSlot()));
         if (acc.countCharacters() > 0) {
           C_CommonClick.sendCharPacks(client);
         }
         client.setStatus(MJClientStatus.CLNT_STS_AUTHLOGIN);
         break;
     }
   }



   private void 기억저장구슬(L1PcInstance pc, L1ItemInstance useItem) {
     ArrayList<L1BookMark> books = pc._bookmarks;
     L1ItemInstance item = ItemTable.getInstance().createItem(700023);
     for (int i = 0; i < books.size(); i++) {
       L1ItemBookMark.addBookmark(pc, item, books.get(i));
     }
     pc.getInventory().storeItem(item);
     pc.getInventory().removeItem(useItem, 1);
   }

   private String readSecondPassword() {
     StringBuilder sb = new StringBuilder();
     int size = readC();
     if (size > 8)
       size = 8;
     int num = 0;
     for (int i = 0; i < size; i++) {
       num = readC();
       if (num < 0 || num > 9) {
         return null;
       }
       sb.append(String.valueOf(num));
     }
     return sb.toString();
   }


   public String getType() {
     return "[C] C_Report";
   }
 }


