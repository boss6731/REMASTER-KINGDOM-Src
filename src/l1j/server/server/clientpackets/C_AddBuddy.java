 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.BuddyTable;
 import l1j.server.server.datatables.CharacterTable;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Buddy;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1CharName;

 public class C_AddBuddy extends ClientBasePacket {
     private static final String C_ADD_BUDDY = "[C] C_AddBuddy";

     public C_AddBuddy(byte[] decrypt, GameClient client) {
         super(decrypt);
         L1PcInstance pc = client.getActiveChar(); // 註解: 獲取當前活動的玩家角色
         if (pc == null) // 註解: 如果沒有活動角色，返回
             return;
         String charName = readS(); // 註解: 讀取玩家輸入的角色名稱
         L1Buddy buddyList = BuddyTable.getInstance().getBuddy(pc.getId(), charName); // 註解: 獲取玩家的好友列表

         if (charName.equalsIgnoreCase(pc.getName())) { // 註解: 如果輸入的名稱是玩家自己的名稱
             pc.sendPackets("不能將自己添加為好友。"); // 註解: 發送消息通知玩家不能添加自己為好友
             return; // 註解: 返回，停止後續執行
         }
         if (buddyList != null) { // 註解: 如果好友列表中已經存在該名稱
             pc.sendPackets((ServerBasePacket)new S_ServerMessage(1052, charName)); // 註解: 發送服務器消息通知玩家該名稱已在好友列表中
             return; // 註解: 返回，停止後續執行
         }

                // 遍歷角色名稱列表，檢查並添加好友
         for (L1CharName cn : CharacterTable.getInstance().getCharNameList()) {
             if (charName.equalsIgnoreCase(cn.getName())) {
                 String name = cn.getName();
                 if (!cn.getName().equalsIgnoreCase("卡西歐佩亞") &&
                         !cn.getName().equalsIgnoreCase("運營者") &&
                         !cn.getName().equalsIgnoreCase("梅提斯") &&
                         !cn.getName().equalsIgnoreCase("米索菲亞")) {

                     BuddyTable.getInstance().addAndSetBuddy(pc.getId(), name, "");
                     return;
                 }
             }
         }
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(109, charName)); // 註解: 發送服務器消息，通知玩家名稱無效
     }

     public String getType() {
         return "[C] C_AddBuddy"; // 註解: 返回類型標識符
     }
 }


