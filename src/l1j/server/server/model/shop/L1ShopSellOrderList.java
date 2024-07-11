 package l1j.server.server.model.shop;

 import java.util.ArrayList;
 import java.util.List;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;




















 public class L1ShopSellOrderList
 {
   private final L1Shop _shop;
   private final L1PcInstance _pc;
   private final List<L1ShopSellOrder> _list = new ArrayList<>();
   private int bugok = 0;

   L1ShopSellOrderList(L1Shop shop, L1PcInstance pc) {
     this._shop = shop;
     this._pc = pc;
   }









   public void add(int itemObjectId, int count, L1PcInstance pc) {
     L1ItemInstance item = pc.getInventory().getItem(itemObjectId);


     if (item == null || item.getItem() == null) {
       return;
     }

     if (item.getCount() < count) {
       this.bugok = 1;

       return;
     }
     if (!item.isStackable() && count != 1) {
       this.bugok = 1;

       return;
     }
     if (item.getCount() <= 0 || count <= 0) {
       this.bugok = 1;
       return;
     }
     if (count <= 500 || item.getItemId() != 41246);




     if (item.isDollOn()) {
       return;
     }

     if (item.getBless() >= 128) {
       return;
     }

     L1AssessedItem assessedItem = this._shop.assessItem(this._pc.getInventory().getItem(itemObjectId));

     if (assessedItem == null)
     {


       throw new IllegalArgumentException();
     }

     this._list.add(new L1ShopSellOrder(assessedItem, count));
   }


   public int BugOk() {
     return this.bugok;
   }



   L1PcInstance getPc() {
     return this._pc;
   }

   public List<L1ShopSellOrder> getList() {
     return this._list;
   }
 }


