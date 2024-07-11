 package l1j.server.server.clientpackets;

 import java.util.Calendar;
 import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.datatables.ResolventTable1;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.L1PetInstance;
 import l1j.server.server.monitor.Logger;
 import l1j.server.server.monitor.LoggerInstance;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class C_DeleteInventoryItem
   extends ClientBasePacket {
   Calendar rightNow = Calendar.getInstance();
   int day = this.rightNow.get(5);
   int hour = this.rightNow.get(10);
   int min = this.rightNow.get(12);
   int year = this.rightNow.get(1);
   int month = this.rightNow.get(2) + 1;
   String totime = "[" + this.year + ":" + this.month + ":" + this.day + ":" + this.hour + ":" + this.min + "]";
   private static final String C_DELETE_INVENTORY_ITEM = "[C] C_DeleteInventoryItem";

   public C_DeleteInventoryItem(byte[] decrypt, GameClient client) {
     super(decrypt);

     L1PcInstance pc = client.getActiveChar();
     if (pc == null) {
       return;
     }
     int length = readD();
     for (int i = 0; i < length; i++) {
       int itemObjectId = readD();
       int count = readD();

       L1ItemInstance item = pc.getInventory().getItem(itemObjectId);


       if (item == null) {
         return;
       }

       if (!pc.isGm() && item.getItem().isCantDelete()) {
         pc.sendPackets((ServerBasePacket)new S_ServerMessage(125));

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
       int crystalCount = ResolventTable1.getInstance().getCrystalCount(item.getItem().getItemId());
       L1ItemInstance crystal = ItemTable.getInstance().createItem(40308);

       // 檢查水晶數量是否不為零
       if (crystalCount != 0) {
         // 檢查計數是否不為零
         if (count != 0) {
           // 設置水晶的數量為水晶數量乘以計數
           crystal.setCount(crystalCount * count);
           // 將水晶存儲到角色的物品欄中
           pc.getInventory().storeItem(crystal);
           // 發送系統消息通知玩家物品已被刪除並獲得相應的水晶
           pc.sendPackets((ServerBasePacket)new S_SystemMessage("\aB" + item.getName() + "(" + count + ")個被刪除: 獲得 " + crystal.getName() + "(" + crystal.getCount() + ")個水晶。"));
         } else {
           // 設置水晶的數量為水晶數量乘以物品的數量
           crystal.setCount(crystalCount * item.getCount());
           // 將水晶存儲到角色的物品欄中
           pc.getInventory().storeItem(crystal);
           // 發送系統消息通知玩家物品已被刪除並獲得相應的水晶
           pc.sendPackets((ServerBasePacket)new S_SystemMessage("\aB" + item.getName() + "(" + item.getCount() + ")個被刪除: 獲得 " + crystal.getName() + "(" + crystal.getCount() + ")個水晶。"));
         }
       }


       LoggerInstance.getInstance().addItemAction(Logger.ItemActionType.Delete, pc, item, count);
       if (count == 0)
         count = item.getCount();
       pc.getInventory().removeItem(item, count);
       pc.getLight().turnOnOffLight();
     }
   }


   public String getType() {
     return "[C] C_DeleteInventoryItem";
   }
 }


