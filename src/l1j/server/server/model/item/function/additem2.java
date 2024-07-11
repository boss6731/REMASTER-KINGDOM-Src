 package l1j.server.server.model.item.function;

 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;



 public class additem2
 {
   public static void clickItem(L1PcInstance pc, int itemId, L1ItemInstance l1iteminstance) {
     switch (itemId) {
       case 3000508:
         if (pc.getInventory().checkItem(3000508, 1)) {
           pc.getInventory().consumeItem(3000508, 1);

           if (pc.is전사()) {
             beginnerItem1(pc, 3000573, 1, 0, 129, 0, true);
           }
           if (pc.isKnight()) {
             beginnerItem1(pc, 3000572, 1, 0, 129, 0, true);
           }
           if (pc.isDragonknight()) {
             beginnerItem1(pc, 3000578, 1, 0, 129, 0, true);
           }
           if (pc.isCrown()) {
             beginnerItem1(pc, 3000579, 1, 0, 129, 0, true);
           }
           if (pc.isWizard()) {
             beginnerItem1(pc, 3000575, 1, 0, 129, 0, true);
           }
           if (pc.isBlackwizard()) {
             beginnerItem1(pc, 3000576, 1, 0, 129, 0, true);
           }
           if (pc.isElf()) {
             beginnerItem1(pc, 3000574, 1, 0, 129, 0, true);
           }
           if (pc.isDarkelf()) {
             beginnerItem1(pc, 3000577, 1, 0, 129, 0, true);
           }
         }
         break;
       case 717:
         if (pc.getInventory().checkItem(717, 1)) {
           pc.getInventory().consumeItem(717, 1);

           if (pc.is전사()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
             beginnerItem1(pc, 4100164, 1, 0, 129, 0, true);
           }
           if (pc.isKnight()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
             beginnerItem1(pc, 4100164, 1, 0, 129, 0, true);
           }
           if (pc.isDragonknight()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
             beginnerItem1(pc, 4100164, 1, 0, 129, 0, true);
           }
           if (pc.isCrown()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
             beginnerItem1(pc, 4100164, 1, 0, 129, 0, true);
           }
           if (pc.isWizard()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
             beginnerItem1(pc, 4100164, 1, 0, 129, 0, true);
           }
           if (pc.isBlackwizard()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
             beginnerItem1(pc, 4100164, 1, 0, 129, 0, true);
           }
           if (pc.isElf()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
             beginnerItem1(pc, 4100164, 1, 0, 129, 0, true);
           }
           if (pc.isDarkelf()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
             beginnerItem1(pc, 4100164, 1, 0, 129, 0, true);
           }
         }
         break;
       case 721:
         if (pc.getInventory().checkItem(721, 1)) {
           pc.getInventory().consumeItem(721, 1);

           if (pc.is전사()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
           }
           if (pc.isKnight()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
           }
           if (pc.isDragonknight()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
           }
           if (pc.isCrown()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
           }
           if (pc.isWizard()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
           }
           if (pc.isBlackwizard()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
           }
           if (pc.isElf()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
           }
           if (pc.isDarkelf()) {
             beginnerItem1(pc, 400254, 10, 0, 129, 0, true);
             beginnerItem1(pc, 4100046, 1, 0, 129, 0, true);
           }
         }
         break;
       case 722:
         if (pc.getInventory().checkItem(722, 1)) {
           pc.getInventory().consumeItem(722, 1);

           if (pc.is전사()) {
             beginnerItem1(pc, 400254, 30, 0, 129, 0, true);
             beginnerItem1(pc, 4100165, 1, 0, 129, 0, true);
           }
           if (pc.isKnight()) {
             beginnerItem1(pc, 400254, 30, 0, 129, 0, true);
             beginnerItem1(pc, 4100165, 1, 0, 129, 0, true);
           }
           if (pc.isDragonknight()) {
             beginnerItem1(pc, 400254, 30, 0, 129, 0, true);
             beginnerItem1(pc, 4100165, 1, 0, 129, 0, true);
           }
           if (pc.isCrown()) {
             beginnerItem1(pc, 400254, 30, 0, 129, 0, true);
             beginnerItem1(pc, 4100165, 1, 0, 129, 0, true);
           }
           if (pc.isWizard()) {
             beginnerItem1(pc, 400254, 30, 0, 129, 0, true);
             beginnerItem1(pc, 4100165, 1, 0, 129, 0, true);
           }
           if (pc.isBlackwizard()) {
             beginnerItem1(pc, 400254, 30, 0, 129, 0, true);
             beginnerItem1(pc, 4100165, 1, 0, 129, 0, true);
           }
           if (pc.isElf()) {
             beginnerItem1(pc, 400254, 30, 0, 129, 0, true);
           }

           if (pc.isDarkelf()) {
             beginnerItem1(pc, 400254, 30, 0, 129, 0, true);
             beginnerItem1(pc, 4100165, 1, 0, 129, 0, true);
           }
         }
         break;
       case 4100080:
         if (pc.getInventory().checkItem(4100080, 1)) {
           pc.getInventory().consumeItem(4100080, 1);

           if (pc.is전사()) {
             beginnerItem1(pc, 4100081, 1, 1, 1, 0, true);
           }
           if (pc.isKnight()) {
             beginnerItem1(pc, 4100081, 1, 1, 1, 0, true);
           }
           if (pc.isDragonknight()) {
             beginnerItem1(pc, 4100081, 1, 1, 1, 0, true);
           }
           if (pc.isCrown()) {
             beginnerItem1(pc, 4100081, 1, 1, 1, 0, true);
           }
           if (pc.isWizard()) {
             beginnerItem1(pc, 4100083, 1, 1, 1, 0, true);
           }
           if (pc.isBlackwizard()) {
             beginnerItem1(pc, 4100083, 1, 1, 1, 0, true);
           }
           if (pc.isElf()) {
             beginnerItem1(pc, 4100082, 1, 1, 1, 0, true);
           }
           if (pc.isDarkelf()) {
             beginnerItem1(pc, 4100081, 1, 1, 1, 0, true);
           }
         }
         break;
     }
   }


   private static boolean beginnerItem1(L1PcInstance pc, int item_id, int count, int EnchantLevel, int Bless, int attr, boolean identi) {
     L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
     if (item != null) {
       item.setCount(count);
       item.setIdentified(identi);
       item.setEnchantLevel(EnchantLevel);
       item.setAttrEnchantLevel(attr);
       item.setIdentified(true);
       if (pc.getInventory().checkAddItem(item, count) == 0) {
         pc.getInventory().storeItem(item, Bless);
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


