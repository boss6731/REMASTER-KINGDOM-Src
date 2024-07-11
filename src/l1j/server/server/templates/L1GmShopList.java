 package l1j.server.server.templates;

 import l1j.server.server.model.Instance.L1ItemInstance;


 public class L1GmShopList
 {
   private L1ItemInstance _item;
   private int _Count;
   private int _Price;

   public L1GmShopList(L1ItemInstance i, int c, int p) {
     this._item = i;
     this._Count = c;
     this._Price = p;
   }

   public L1ItemInstance getItem() {
     return this._item;
   }

   public void setCount(int i) {
     this._Count = i;
   }

   public int getCount() {
     return this._Count;
   }

   public int getPrice() {
     return this._Price;
   }
 }


