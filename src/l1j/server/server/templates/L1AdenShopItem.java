 package l1j.server.server.templates;

 import l1j.server.server.datatables.ItemTable;

























 public class L1AdenShopItem
   implements Comparable<L1AdenShopItem>
 {
   private int _id;
   private final int _itemId;
   private final L1Item _item;
   private int _icon_id;
   private int _price;
   private final int _packCount;
   private int _count;
   private String _html;
   private int _status;
   private int _type;

   public L1AdenShopItem(int id, int itemId, int icon_id, int price, int packCount, String html, int status, int type) {
     this._id = id;
     this._itemId = itemId;
     this._icon_id = icon_id;
     this._item = ItemTable.getInstance().getTemplate(itemId);
     this._price = price;
     this._packCount = packCount;
     this._html = html;
     this._count = 1;
     this._status = status;
     this._type = type;
   }

   public int get_icon_id() {
     return this._icon_id;
   }

   public int getItemId() {
     return this._itemId;
   }

   public L1Item getItem() {
     return this._item;
   }

   public void setPrice(int i) {
     this._price = i;
   }

   public int getPrice() {
     return this._price;
   }

   public int getPackCount() {
     return this._packCount;
   }

   public int getCount() {
     return this._count;
   }

   public void setCount(int i) {
     this._count = i;
   }

   public String getHtml() {
     return this._html;
   }

   public int getStatus() {
     return this._status;
   }

   public int getType() {
     return this._type;
   }


   public int compareTo(L1AdenShopItem o) {
     if (this._id > o._id) return 1;
     if (this._id < o._id) return -1;
     return 0;
   }
 }


