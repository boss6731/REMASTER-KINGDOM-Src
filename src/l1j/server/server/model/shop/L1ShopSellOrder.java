 package l1j.server.server.model.shop;






 class L1ShopSellOrder
 {
   private final L1AssessedItem _item;
   private final int _count;

   public L1ShopSellOrder(L1AssessedItem item, int count) {
     this._item = item;
     this._count = count;
   }

   public L1AssessedItem getItem() {
     return this._item;
   }

   public int getCount() {
     return this._count;
   }
 }


