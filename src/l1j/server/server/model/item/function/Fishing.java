 package l1j.server.server.model.item.function;

 import l1j.server.server.Controller.FishingTimeController;
 import l1j.server.server.datatables.FishingZoneTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_Fishing;
 import l1j.server.server.serverpackets.S_FishingTime;
 import l1j.server.server.serverpackets.S_InventoryIcon;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class Fishing
   extends L1ItemInstance
 {
    public static void clickItem(L1PcInstance pc, L1ItemInstance item, int fishX, int fishY) {
     if (pc.isFishing()) {
       pc.sendPackets((ServerBasePacket)new S_SystemMessage("釣魚: 進行中"), true);
     } else {
       startFishing(pc, item, fishX, fishY);
     }
   }
   private static void startFishing(L1PcInstance pc, L1ItemInstance item, int fishX, int fishY) {
     int itemId = item.getItemId();
     int chargeCount = item.getChargeCount();
     if (pc.getMapId() != 5490 || fishX <= 32704 || fishX >= 32831 || fishY <= 32768 || fishY >= 32895) {

       pc.sendPackets((ServerBasePacket)new S_ServerMessage(1138));

       return;
     }
     if ((itemId == 41294 || itemId == 41305 || itemId == 41306 || itemId == 600229 || itemId == 9991 || itemId == 87058 || itemId == 87059 || itemId == 4100293) && chargeCount <= 0) {
       return;
     }


       // 檢查物品欄是否超重，如果超過82%
       if (pc.getInventory().getWeight100() > 82) {
           // 發送系統消息通知玩家物品過重無法進行釣魚
           pc.sendPackets((ServerBasePacket)new S_SystemMessage("由於物品過重，無法進行釣魚。"));

           return; // 結束方法，不進行釣魚操作
       }
     if (pc.getInventory().getSize() >= 200) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(263));

       return;
     }
     if (isFishingeZone(fishX, fishY, pc.getMapId())) {
       L1ItemInstance useItem = pc.getInventory().getItem(item.getId());
       if (useItem != null) {
         pc._fishingRod = useItem;
       } else {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(1137));
         return;
       }
       if (pc._fishingRod.getItemId() == 600229 || pc._fishingRod.getItemId() == 87058 || pc._fishingRod
         .getItemId() == 87059 || pc._fishingRod.getItemId() == 4100293 || pc
         .getInventory().consumeItem(41295, 1)) {
         pc._fishingX = fishX;
         pc._fishingY = fishY;
         pc.sendPackets((ServerBasePacket)new S_Fishing(pc.getId(), 71, fishX, fishY));
         pc.broadcastPacket((ServerBasePacket)new S_Fishing(pc.getId(), 71, fishX, fishY));
         pc.setFishing(true);
         boolean ck = false;
         int Time = 0;
         if (pc._fishingRod.getItemId() == 600229) {
           ck = true;
           Time = 30;
           item.setChargeCount(item.getChargeCount() - 1);
           pc.getInventory().updateItem(item, 128);
           pc.setFishingTime(System.currentTimeMillis() + 30000L);
         } else if (pc._fishingRod.getItemId() == 4100293) {
           ck = true;
           Time = 30;
           item.setChargeCount(item.getChargeCount() - 1);
           pc.getInventory().updateItem(item, 128);
           pc.setFishingTime(System.currentTimeMillis() + 30000L);
         } else if (pc._fishingRod.getItemId() == 87058) {
           ck = true;
           Time = 90;
           item.setChargeCount(item.getChargeCount() - 1);
           pc.getInventory().updateItem(item, 128);
           pc.setFishingTime(System.currentTimeMillis() + 90000L);
         } else if (pc._fishingRod.getItemId() == 87059) {
           ck = true;
           Time = 90;
           item.setChargeCount(item.getChargeCount() - 1);
           pc.getInventory().updateItem(item, 128);
           pc.setFishingTime(System.currentTimeMillis() + 90000L);
         } else if (pc._fishingRod.getItemId() == 41293) {
           Time = 240;
           pc.setFishingTime(System.currentTimeMillis() + 240000L);
           pc.sendPackets((ServerBasePacket)new S_FishingTime(63, 1, ck, 240), true);
         } else {
           Time = 80;
           item.setChargeCount(item.getChargeCount() - 1);
           pc.getInventory().updateItem(item, 128);
           pc.setFishingTime(System.currentTimeMillis() + 80000L);
         }
         FishingTimeController.getInstance().addMember(pc);
         pc.sendPackets((ServerBasePacket)S_InventoryIcon.iconNewUnLimit(6834, 4497, true));
         pc.sendPackets((ServerBasePacket)new S_FishingTime(63, 1, ck, Time), true);
       } else {

         pc.sendPackets((ServerBasePacket)new S_ServerMessage(1137), true);
       }
     } else {

       pc.sendPackets((ServerBasePacket)new S_ServerMessage(1138), true);
     }
   }

   public static boolean isFishingeZone(int locX, int locY, int mapId) {
     String key = mapId + locX + locY;
     if (FishingZoneTable.getInstance().isLockey(key)) {
       return true;
     }
     return false;
   }
 }


