 package l1j.server.server.clientpackets;

 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_Message_YN;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.types.Point;



 public class C_CreateParty
   extends ClientBasePacket
 {
     // C_CreateParty 類的構造函數
     public C_CreateParty(byte[] data, GameClient client) throws Exception {
         super(data);

            // 如果客戶端為空，則返回
         if (client == null) {
             return;
         }

            // 獲取當前活動角色
         L1PcInstance pc = client.getActiveChar();

            // 如果角色為空，則返回
         if (pc == null) {
             return;
         }

            // 檢查角色所在地圖是否為621或角色是否不在世界中
         if (pc.getMapId() == 621 || !pc.is_world()) {
                // 如果條件滿足，發送系統消息“在該地圖無法使用隊伍功能”
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("在該地圖無法使用隊伍功能。"));
             return;
         }
     }
     int type = readC();
     if (type == 0 || type == 1 || type == 4 || type == 5) {
       int targetId = 0;
       L1Object temp = null;
       if (type == 4 || type == 5) {
         String name = readS();

         if (name == null) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(109));
           return;
         }
         L1PcInstance tar = L1World.getInstance().getPlayer(name);
         if (tar != null) {
           L1PcInstance l1PcInstance = tar;
           targetId = tar.getId();
         } else {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(109, name));
           return;
         }
       } else {
         targetId = readD();
         temp = L1World.getInstance().findObject(targetId);
       }
       if (temp instanceof L1PcInstance) {
         L1PcInstance targetPc = (L1PcInstance)temp;
         if (pc.getId() == targetPc.getId()) {
           return;
         }
         if (targetPc.isInParty()) {

           pc.sendPackets((ServerBasePacket)new S_ServerMessage(415));
           return;
         }
         if (pc.isInParty()) {
           if (pc.getParty().isLeader(pc)) {
             targetPc.setPartyID(pc.getId());

             targetPc.sendPackets((ServerBasePacket)new S_Message_YN(953, pc.getName()));
           } else {

             pc.sendPackets((ServerBasePacket)new S_ServerMessage(416));
           }
         } else {
           targetPc.setPartyID(pc.getId());
           switch (type) {
             case 0:
             case 4:
               pc.setPartyType(0);

               targetPc.sendPackets((ServerBasePacket)new S_Message_YN(953, pc.getName()));
               break;
             case 1:
             case 5:
               pc.setPartyType(1);

               targetPc.sendPackets((ServerBasePacket)new S_Message_YN(954, pc.getName()));
               break;
           }
         }
       }
     } else if (type == 2) {
       String name = readS();
       L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
       if (targetPc == null) {

         pc.sendPackets((ServerBasePacket)new S_ServerMessage(109));
         return;
       }
       if (pc.getId() == targetPc.getId()) {
         return;
       }
       if (targetPc.isInChatParty()) {

         pc.sendPackets((ServerBasePacket)new S_ServerMessage(415));



         return;
       }


       if (pc.isInChatParty()) {
         if (pc.getChatParty().isLeader(pc)) {
           targetPc.setPartyID(pc.getId());

           targetPc.sendPackets((ServerBasePacket)new S_Message_YN(951, pc.getName()));
         } else {

           pc.sendPackets((ServerBasePacket)new S_ServerMessage(416));
         }
       } else {
         targetPc.setPartyID(pc.getId());

         targetPc.sendPackets((ServerBasePacket)new S_Message_YN(951, pc.getName()));
       }
     } else if (type == 3) {
       int targetId = readD();
       L1Object temp = L1World.getInstance().findObject(targetId);
       if (temp instanceof L1PcInstance) {
         L1PcInstance targetPc = (L1PcInstance)temp;
         if (pc.getId() == targetPc.getId()) {
           return;
         }

         if (pc.isInParty()) {
           if (targetPc.isInParty()) {
             if (pc.getParty().isLeader(pc)) {
               if (pc.getLocation().getTileLineDistance((Point)targetPc.getLocation()) < 16) {
                 pc.getParty().passLeader(targetPc);
               } else {

                 pc.sendPackets((ServerBasePacket)new S_ServerMessage(1695));
               }
             } else {

               pc.sendPackets((ServerBasePacket)new S_ServerMessage(1697));
             }
           } else {

             pc.sendPackets((ServerBasePacket)new S_ServerMessage(1696));
           }
         }
       }
     }
   }


   public String getType() {
     return "[C] C_CreateParty";
   }
 }


