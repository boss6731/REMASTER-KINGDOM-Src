 package l1j.server.server.templates;

 import l1j.server.server.datatables.ItemTable;















 public class L1ShopItem
 {
   private final int _itemId;
   private final L1Item _item;
   private final int _price;
   private final int _packCount;
   private int _enchant;
   private int _attrenchant;
   private int _count;
   private boolean _timeLimit;
   private int _endTime;
   private boolean _carving;
   private int _bless;
   private int _buylevel;
   private int _classType;
   private int _clan_shop_type;
   private int npcid;

   public L1ShopItem(int itemId, int price, int packCount) {
     this(itemId, price, packCount, 0, false, 0, false, 1, 0, 0, 10, 0);
   }


   public L1ShopItem(int itemId, int price, int packCount, int enchant, boolean timeLimit, int minute, boolean carving, int bless, int attrenchant, int buylevel, int classType, int clan_shop_type) {
     this._itemId = itemId;
     this._item = ItemTable.getInstance().getTemplate(itemId);
     this._price = price;
     this._packCount = packCount;
     this._enchant = enchant;
     this._attrenchant = attrenchant;
     this._count = 1;
     this._timeLimit = timeLimit;
     this._endTime = minute;
     this._carving = carving;
     this._bless = bless;
     this._buylevel = buylevel;
     this._clan_shop_type = clan_shop_type;
     set_classType(classType);
   }


   public L1ShopItem(int itemId, int price, int packCount, int enchant, int bless) {
     this._itemId = itemId;
     this._item = ItemTable.getInstance().getTemplate(itemId);
     this._price = price;
     this._packCount = packCount;
     this._enchant = enchant;
     this._count = 1;
     this._bless = bless;
   }

   public int get_count() {
     return this._count;
   }

   public void set_count(int _count) {
     this._count = _count;
   }

   public int getItemId() {
     return this._itemId;
   }

   public L1Item getItem() {
     return this._item;
   }

   public int getPrice() {
     return this._price;
   }

   public int getPackCount() {
     return this._packCount;
   }

   public int getEnchant() {
     return this._enchant;
   }

   public void setEnchant(int i) {
     this._enchant = i;
   }

   public int getCount() {
     return this._count;
   }

   public void setCount(int i) {
     this._count = i;
   }

   public boolean isTimeLimit() {
     return this._timeLimit;
   }

   public void setTimeLimit(boolean timeLimit) {
     this._timeLimit = timeLimit;
   }

   public int getEndTime() {
     return this._endTime;
   }

   public void setEndTime(int t) {
     this._endTime = t;
   }

   public boolean isCarving() {
     return this._carving;
   }

   public void setCarving(boolean flag) {
     this._carving = flag;
   }

   public int getBless() {
     return this._bless;
   }

   public void setBless(int flag) {
     this._bless = flag;
   }

   public int getAttrEnchant() {
     return this._attrenchant;
   }

   public void setAttrEnchant(int i) {
     this._attrenchant = i;
   }

   public int getBuyLevel() {
     return this._buylevel;
   }

   public void setBuyLevel(int i) {
     this._buylevel = i;
   }

   public void setNpcId(int i) {
     this.npcid = i;
   }

   public int getNpcId() {
     return this.npcid;
   }

   public int get_classType() {
     return this._classType;
   }

   public void set_classType(int _classType) {
     this._classType = _classType;
   }

   public int getClanShopType() {
     return this._clan_shop_type;
   }

   public void setClanShopType(int i) {
     this._clan_shop_type = i;
   }
 }


