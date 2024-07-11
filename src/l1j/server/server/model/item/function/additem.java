 package l1j.server.server.model.item.function;

 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.utils.CommonUtil;



 public class additem
 {
   public static void clickItem(L1PcInstance pc, int itemId, L1ItemInstance l1iteminstance) {
     switch (itemId) {
       case 4100164:
         if (pc.getInventory().checkItem(4100164, 1)) {
           pc.getInventory().consumeItem(4100164, 1);

           if (pc.is전사()) {
             beginnerItem(pc, 4100160, 1, 0, 129, 0, true);
           }
           if (pc.isKnight()) {
             beginnerItem(pc, 4100160, 1, 0, 129, 0, true);
           }
           if (pc.isDragonknight()) {
             beginnerItem(pc, 4100160, 1, 0, 129, 0, true);
           }
           if (pc.isCrown()) {
             beginnerItem(pc, 4100160, 1, 0, 129, 0, true);
           }
           if (pc.isWizard()) {
             beginnerItem(pc, 4100160, 1, 0, 129, 0, true);
           }
           if (pc.isBlackwizard()) {
             beginnerItem(pc, 4100160, 1, 0, 129, 0, true);
           }
           if (pc.isElf()) {
             beginnerItem(pc, 4100162, 1, 0, 129, 0, true);
           }
           if (pc.isDarkelf()) {
             beginnerItem(pc, 4100160, 1, 0, 129, 0, true);
           }
         }
         break;
       case 4100165:
         if (pc.getInventory().checkItem(4100165, 1)) {
           pc.getInventory().consumeItem(4100165, 1);

           if (pc.is전사()) {
             beginnerItem(pc, 4100161, 1, 0, 129, 0, true);
           }
           if (pc.isKnight()) {
             beginnerItem(pc, 4100161, 1, 0, 129, 0, true);
           }
           if (pc.isDragonknight()) {
             beginnerItem(pc, 4100161, 1, 0, 129, 0, true);
           }
           if (pc.isCrown()) {
             beginnerItem(pc, 4100161, 1, 0, 129, 0, true);
           }
           if (pc.isWizard()) {
             beginnerItem(pc, 4100161, 1, 0, 129, 0, true);
           }
           if (pc.isBlackwizard()) {
             beginnerItem(pc, 4100161, 1, 0, 129, 0, true);
           }



           if (pc.isDarkelf()) {
             beginnerItem(pc, 4100161, 1, 0, 129, 0, true);
           }
         }
         break;
     }
   }


   public static boolean beginnerItem(L1PcInstance pc, int item_id, int count, int EnchantLevel, int Bless, int attr, boolean identi) {
     L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
     if (item != null) {
       item.setCount(count);
       item.setIdentified(identi);
       item.setEnchantLevel(EnchantLevel);
       item.setAttrEnchantLevel(attr);
       item.setIdentified(true);
       if (!item.isStackable())
         CommonUtil.SetTodayDeleteTime(item, 4320);
       if (pc.getInventory().checkAddItem(item, count) == 0) {

         pc.getInventory().storeItem(item);
         item.setBless(Bless);
         pc.getInventory().updateItem(item, 512);

         pc.getInventory().saveItem(item, 512);
       } else {

         pc.sendPackets((ServerBasePacket)new S_ServerMessage(82));
         return false;
       }
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(403, item.getLogName()));
       return true;
     }
     return false;
   }
 }


