 package l1j.server.server.clientpackets;

 import MJShiftObject.Battle.MJShiftBattlePlayManager;
 import java.util.Calendar;
 import l1j.server.Config;
 import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.datatables.NoDropItem;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.model.L1Inventory;
 import l1j.server.server.model.L1World;
 import l1j.server.server.monitor.Logger;
 import l1j.server.server.monitor.LoggerInstance;
 import l1j.server.server.serverpackets.S_Disconnect;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_DropItem
   extends ClientBasePacket
 {
   private static final String C_DROP_ITEM = "[C] C_DropItem";
   Calendar rightNow = Calendar.getInstance();
   int day = this.rightNow.get(5);
   int hour = this.rightNow.get(10);
   int min = this.rightNow.get(12);
   int year = this.rightNow.get(1);
   int month = this.rightNow.get(2) + 1;
   String totime = "[" + this.year + ":" + this.month + ":" + this.day + ":" + this.hour + ":" + this.min + "]";

   public C_DropItem(byte[] decrypt, GameClient client) throws Exception {
     super(decrypt);
     int length = 1;
     length = readD();
     for (int i = 0; i < length; i++) {
       int x = readH();
       int y = readH();
       int objectId = readD();
       int count = readD();

       L1PcInstance pc = client.getActiveChar();

       if (pc == null || pc.isGhost()) {
         return;
       }

       if (MJShiftBattlePlayManager.is_shift_battle(pc)) {
         return;
       }
       if (pc.getOnlineStatus() != 1) {
         pc.sendPackets((ServerBasePacket)new S_Disconnect());

         return;
       }
       L1ItemInstance item = pc.getInventory().getItem(objectId);
       if (item != null) {
         long nowtime = System.currentTimeMillis();
         if (item.getItemdelay3() >= nowtime) {
           return;
         }
         if ((!pc.isGm() && !item.getItem().isTradable()) || item.getItemId() == 100002 || item
           .getItemId() == 100003) {

           pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

           return;
         }

         int itemType = item.getItem().getType2();
         int itemId = item.getItem().getItemId();

         if ((itemType == 1 && count != 1) || (itemType == 2 && count != 1)) {
           pc.sendPackets((ServerBasePacket)new S_Disconnect());
           return;
         }
         if (item.getCount() <= 0) {
           pc.sendPackets((ServerBasePacket)new S_Disconnect());
           return;
         }
         if (!item.isStackable() && count != 1) {
           pc.sendPackets((ServerBasePacket)new S_Disconnect());
           return;
         }
         if (item.getCount() < count || count <= 0 || count > 2000000000) {
           pc.sendPackets((ServerBasePacket)new S_Disconnect());
           return;
         }
         if (count > item.getCount()) {
           count = item.getCount();
         }


           // 檢查玩家是否能夠丟棄某個物品的邏輯
           if (!pc.isGm() && NoDropItem.getInstance().isNoDropItem(itemId)) {
               String itemName = ItemTable.getInstance().findItemIdByName(itemId);
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("[" + itemName + "] 物品不能被丟棄。"));

               return;
           }

                // 檢查玩家等級是否足夠丟棄物品
           if (!pc.isGm() && pc.getLevel() < Config.ServerAdSetting.ALTDROPLEVELLIMIT) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("等級 " + Config.ServerAdSetting.ALTDROPLEVELLIMIT + " 以上才能丟棄物品。"));
               return;
           }

                // 檢查地圖ID，某些地圖不允許丟棄物品
           if (item.getId() >= 0 && (pc.getMapId() == 350 || pc.getMapId() == 340 || pc.getMapId() == 370 || pc.getMapId() == 800)) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("在市場上不能丟棄物品。"));

               return;
           }

                // 檢查其他特定地圖ID，某些地圖不允許丟棄物品
           if ((item.getId() >= 0 && pc.getMapId() == 38) || pc.getMapId() == 34) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("在這個地區不能丟棄物品。"));

               return;
           }

                // 檢查物品是否有期限
           if (!pc.isGm() && item.getEndTime() != null) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("限期物品不能被丟棄。"), true);

               return;
           }

                // 檢查物品是否被刻印
           if (!pc.isGm() && item.get_Carving() != 0) {
               pc.sendPackets((ServerBasePacket)new S_SystemMessage("刻印的物品不能被丟棄。"), true);

               return;
           }

         if (!pc.isGm() && item.getBless() >= 128) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

           return;
         }
         if (!MJCompanionInstanceCache.is_companion_oblivion(item.getId())) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

           return;
         }
         Object[] petlist = pc.getPetList().values().toArray();
         L1PetInstance pet = null;
         for (Object petObject : petlist) {
           if (petObject instanceof L1PetInstance) {
             pet = (L1PetInstance)petObject;
             if (item.getId() == pet.getItemObjId()) {

               pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

               return;
             }
           }
         }
         L1DollInstance doll = pc.getMagicDoll();
         if (doll != null &&
           item.getId() == doll.getItemObjId()) {

           pc.sendPackets((ServerBasePacket)new S_ServerMessage(210, item.getItem().getName()));

           return;
         }

         if (item.isEquipped()) {

           pc.sendPackets((ServerBasePacket)new S_ServerMessage(125));
           return;
         }
         if (x > pc.getX() + 1 || x < pc.getX() - 1 || y > pc.getY() + 1 || y < pc.getY() - 1) {
           return;
         }
         int delay_time = 2000;
         if (item != null &&
           item.isStackable() &&
           item.getItemdelay3() <= nowtime) {
           item.setItemdelay3(nowtime + delay_time);
         }



         item.setGiveItem(true);
         pc.getInventory().tradeItem(item, count, (L1Inventory)L1World.getInstance().getInventory(x, y, pc.getMapId()));
         pc.getLight().turnOnOffLight();

         LoggerInstance.getInstance().addItemAction(Logger.ItemActionType.Drop, pc, item, count);
       }
     }
   }


   public String getType() {
     return "[C] C_DropItem";
   }
 }


