 package l1j.server.server.model.shop;

 import javolution.util.FastTable;
 import l1j.server.server.datatables.AdenShopTable;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Warehouse.SupplementaryService;
 import l1j.server.server.model.Warehouse.WarehouseManager;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1AdenShopItem;
 import l1j.server.server.templates.L1Item;

 public class L1AdenShop {
   private FastTable<Item> buylist = new FastTable();

   private boolean bugok = false;
   private int _totalPrice;

   public L1AdenShop() {
     this._totalPrice = 0;
   }
   public void add(L1PcInstance pc, int id, int count) {
     try {
       if (this._totalPrice < 0)
         return;
       L1AdenShopItem item = AdenShopTable.getInstance().get(id);
       if (item == null)
         return;
       int price = item.getPrice();
       if (price == 0)
         return;
       this._totalPrice += price * count;
       if (this._totalPrice < 0 || this._totalPrice >= 1000000000) {
         this.bugok = true;
         return;
       }
       if (count <= 0 || count > 50) {
         this.bugok = true;
         return;
       }
       Item listitem = new Item();
       listitem.itemid = id;
       listitem.count = count * ((item.getPackCount() > 0) ? item.getPackCount() : 1);
       this.buylist.add(listitem);

       pc.sendPackets(3513);
     } catch (Exception exception) {}
   }




   public boolean BugOk() {
     return this.bugok;
   }



     public boolean commit(L1PcInstance pc) {
            // 檢查玩家的 N 幣是否足夠支付總價格
         if (pc.getNcoin() < this._totalPrice) {
                // 向玩家發送消息，通知 N 幣不足
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("N幣不足。"));
             return false;
         }

     pc.addNcoin(-this._totalPrice);
     SupplementaryService pwh = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());

     if (pwh == null)
       return false;
     for (Item listitem : this.buylist) {
       if (listitem.itemid == 0 || listitem.count == 0)
         continue;
       L1Item tempItem = ItemTable.getInstance().getTemplate(listitem.itemid);
       if (tempItem.isStackable()) {
         L1ItemInstance l1ItemInstance = ItemTable.getInstance().createItem(listitem.itemid);
         l1ItemInstance.setIdentified(true);
         l1ItemInstance.setEnchantLevel(0);
         l1ItemInstance.setCount(listitem.count);
         l1ItemInstance.setChargeCount(tempItem.getMaxChargeCount());
         pwh.storeTradeItem(l1ItemInstance); continue;
       }
       L1ItemInstance item = null;

       for (int createCount = 0; createCount < listitem.count; createCount++) {
         item = ItemTable.getInstance().createItem(listitem.itemid);
         item.setIdentified(true);
         item.setEnchantLevel(0);
         item.setChargeCount(tempItem.getMaxChargeCount());
         pwh.storeTradeItem(item);
       }
     }

     return true;
   }

   class Item {
     public int itemid = 0;
     public int count = 0;
   }
 }


