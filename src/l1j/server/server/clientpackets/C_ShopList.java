 package l1j.server.server.clientpackets;

 import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_PERSONAL_SHOP_ITEM_LIST_NOTI;
 import l1j.server.server.GameClient;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;



 public class C_ShopList
   extends ClientBasePacket
 {
   private static final String C_SHOP_LIST = "[C] C_ShopList";

   public C_ShopList(byte[] abyte0, GameClient clientthread) {
     super(abyte0);
     int type = readC();
     int objectId = readD();

     L1PcInstance pc = clientthread.getActiveChar();
     if (pc == null || pc.isGhost()) {
       return;
     }

       // 從世界實例中查找指定的物件
       L1Object shopPc = L1World.getInstance().findObject(objectId);

// 檢查查找到的物件是否為玩家角色實例
       if (shopPc instanceof L1PcInstance) {
           L1PcInstance cha = (L1PcInstance)shopPc;

           // 檢查當前玩家與目標玩家是否屬於同一帳號
           if (pc.getAccountName().equalsIgnoreCase(cha.getAccountName())) {
               pc.sendPackets("同一帳號下無法使用無人商店。");
               return;
           }

           // 發送個人商店物品列表通知
           SC_PERSONAL_SHOP_ITEM_LIST_NOTI.do_send(pc, objectId, SC_PERSONAL_SHOP_ITEM_LIST_NOTI.ePersonalShopType.fromInt(type));
       } else if (shopPc instanceof l1j.server.server.model.Instance.L1NpcShopInstance) {
           // 如果物件為 NPC 商店實例，發送 NPC 商店的物品列表通知
           SC_PERSONAL_SHOP_ITEM_LIST_NOTI.do_send_for_npc(pc, objectId, SC_PERSONAL_SHOP_ITEM_LIST_NOTI.ePersonalShopType.fromInt(type));
       } else {
           // 檢查其他類型的商店物件
           if (shopPc != null && shopPc.instanceOf(2048)) {
               pc.sendPackets("如果您想加入血盟，請進行切割。");
           } else {
               pc.sendPackets("未找到商店物件。");
           }
           return;
       }


   public String getType() {
     return "[C] C_ShopList";
   }
 }


