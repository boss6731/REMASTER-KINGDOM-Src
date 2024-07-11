 package l1j.server.server.clientpackets;

 import java.io.File;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ClanAttention;
 import l1j.server.server.serverpackets.S_Message_YN;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;




 public class C_ClanAttention
   extends ClientBasePacket
 {
   private static final String C_PledgeRecommendation = "[C] C_PledgeRecommendation";

   public C_ClanAttention(byte[] decrypt, GameClient client) {
     super(decrypt); String pcClanName, targetClanName; int i; File file; L1PcInstance target;
     String targetClanName2;
     L1PcInstance pc = client.getActiveChar();
     if (pc == null) {
       return;
     }

     int data = readC();



     L1Clan targetClan = null;
     L1Clan clan = null;
     switch (data) {






         case 0: // 註解: 處理操作碼為0的情況
             pcClanName = pc.getClanname(); // 註解: 獲取玩家的血盟名稱
             targetClanName = readS(); // 註解: 讀取一個字符串，表示目標血盟名稱
             clan = L1World.getInstance().findClan(pcClanName); // 註解: 根據玩家的血盟名稱在世界中查找血盟
             if (clan == null) { // 註解: 如果未找到血盟
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("\aG請先創建一個血盟。")); // 註解: 向玩家發送消息，通知請先創建一個血盟
                 return; // 註解: 返回，停止後續執行
             }
             if (pcClanName.toLowerCase().equals(targetClanName.toLowerCase())) { // 註解: 如果玩家的血盟名稱與目標血盟名稱相同（忽略大小寫）
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("\aG不能向自己的血盟請求紋章守護。")); // 註解: 向玩家發送消息，通知不能向自己的血盟請求紋章守護
                 return; // 註解: 返回，停止後續執行
             }
             for (i = 0; i < clan.getGazeList().size(); i++) { // 註解: 遍歷當前血盟的紋章守護列表
                 if (((String)clan.getGazeList().get(i)).toLowerCase().equals(targetClanName.toLowerCase())) { // 註解: 如果目標血盟名稱已在紋章守護列表中（忽略大小寫）
                     pc.sendPackets((ServerBasePacket)new S_SystemMessage("\aG已經與對方血盟進行紋章守護。")); // 註解: 向玩家發送消息，通知已經與對方血盟進行紋章守護
                     return; // 註解: 返回，停止後續執行
                 }
             }
             if (clan.getGazeList().size() >= 5) { // 註解: 如果紋章守護列表已達到最大數量
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("\aG最多只能向5個血盟請求紋章守護。")); // 註解: 向玩家發送消息，通知最多只能向5個血盟請求紋章守護
                 return; // 註解: 返回，停止後續執行
             }

             for (L1Clan checkClan : L1World.getInstance().getAllClans()) { // 註解: 遍歷世界中的所有血盟
                 if (checkClan.getClanName().toLowerCase().equals(targetClanName.toLowerCase())) { // 註解: 如果找到名稱與目標血盟名稱相同的血盟（忽略大小寫）
                     targetClan = checkClan; // 註解: 設置目標血盟為找到的血盟
                     break; // 註解: 跳出循環
                 }
             }
           if (targetClan == null) { // 註解: 如果目標血盟為空（未找到）
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("\aG對方血盟不存在。")); // 註解: 向玩家發送消息，通知對方血盟不存在
               return; // 註解: 返回，停止後續執行
           }
           file = new File(System.getProperty("user.dir") + "/emblem/" + clan.getEmblemId()); // 註解: 創建一個表示當前血盟紋章文件的File對象

           if (!file.exists()) { // 註解: 如果當前血盟的紋章文件不存在
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("沒有血盟標誌，無法請求紋章守護。")); // 註解: 向玩家發送消息，通知沒有血盟標誌無法請求紋章守護
               return; // 註解: 返回，停止後續執行
           }
           file = new File(System.getProperty("user.dir") + "/emblem/" + targetClan.getEmblemId()); // 註解: 創建一個表示目標血盟紋章文件的File對象
           if (!file.exists()) { // 註解: 如果目標血盟的紋章文件不存在
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("對方血盟沒有血盟標誌。")); // 註解: 向玩家發送消息，通知對方血盟沒有血盟標誌
               return; // 註解: 返回，停止後續執行
           }
           target = L1World.getInstance().getPlayer(targetClan.getLeaderName()); // 註解: 根據目標血盟的領袖名稱在世界中查找玩家對象
           if (target != null) { // 註解: 如果找到目標血盟的領袖
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("血盟守護: 請求中，請稍候。")); // 註解: 向玩家發送消息，通知正在請求血盟守護
               target.setTempID(pc.getId()); // 註解: 設置目標玩家的臨時ID為當前玩家的ID
               target.sendPackets((ServerBasePacket)new S_Message_YN(3348, pc.getClanname())); // 註解: 向目標玩家發送請求確認消息
               break; // 註解: 跳出循環
           }
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(3349)); // 註解: 向玩家發送消息，通知對方血盟領袖不在線
           break; // 註解: 跳出循環



         case 1: // 註解: 處理操作碼為1的情況
             targetClanName2 = readS(); // 註解: 讀取一個字符串，表示目標血盟名稱
             if (!pc.isCrown()) { // 註解: 如果玩家不是君主
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("\aG只有王族才能解除紋章守護。")); // 註解: 向玩家發送消息，通知只有君主才能解除紋章守護
                 return; // 註解: 返回，停止後續執行
             }
             clan = L1World.getInstance().findClan(pc.getClanname()); // 註解: 根據玩家的血盟名稱在世界中查找血盟
             if (clan == null) { // 註解: 如果未找到血盟
                 return; // 註解: 返回，停止後續執行
             }

             for (L1Clan checkClan : L1World.getInstance().getAllClans()) { // 註解: 遍歷世界中的所有血盟
                 if (checkClan.getClanName().toLowerCase().equals(targetClanName2.toLowerCase())) { // 註解: 如果找到名稱與目標血盟名稱相同的血盟（忽略大小寫）
                     targetClan = checkClan; // 註解: 設置目標血盟為找到的血盟
                     break; // 註解: 跳出循環
                 }
             }
             if (targetClan == null) { // 註解: 如果未找到目標血盟
                 pc.sendPackets((ServerBasePacket)new S_SystemMessage("\aG對方血盟不存在。")); // 註解: 向玩家發送消息，通知對方血盟不存在
                 return; // 註解: 返回，停止後續執行
             }

         clan.removeGazelist(targetClan.getClanName());
         targetClan.removeGazelist(clan.getClanName());


         for (L1PcInstance member : clan.getOnlineClanMember()) {
           member.sendPackets((ServerBasePacket)new S_ClanAttention(clan.getGazeSize(), clan.getGazeList()));
         }

         for (L1PcInstance member : targetClan.getOnlineClanMember()) {
           member.sendPackets((ServerBasePacket)new S_ClanAttention(targetClan.getGazeSize(), targetClan.getGazeList()));
         }
         break;
     }
   }


   public String getType() {
     return "[C] C_PledgeRecommendation";
   }
 }


