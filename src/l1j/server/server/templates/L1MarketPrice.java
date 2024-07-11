 package l1j.server.server.templates;public class L1MarketPrice { private int order; private int npcid; private int itemobjid; private String itemname; private String charname;
   private int item_id;
   private int count;
   private int enchant;

   public int getOrder() {
     return this.order;
   }
   private int price; private int locx; private int locy; private int locm; private int iden; private int attr; private int invGfx; private boolean _isPurchase;
   public void setOrder(int i) {
     this.order = i;
   }



   public int getNpcId() {
     return this.npcid;
   }

   public void setNpcId(int id) {
     this.npcid = id;
   }


   public int getItemObjId() {
     return this.itemobjid;
   }
   public void setItemObjId(int id) {
     this.itemobjid = id;
   }



   public String getItemName() {
     return this.itemname;
   }

   public void setItemName(String name) {
     this.itemname = name;
   }



   public String getCharName() {
     return this.charname;
   }

   public void setCharName(String name) {
     this.charname = name;
   }



   public int getItemId() {
     return this.item_id;
   }

   public void setItemId(int i) {
     this.item_id = i;
   }



   public int getCount() {
     return this.count;
   }

   public void setCount(int i) {
     this.count = i;
   }



   public int getEnchant() {
     return this.enchant;
   }

   public void setEnchant(int i) {
     this.enchant = i;
   }



   public int getPrice() {
     return this.price;
   }

   public void setPrice(int i) {
     this.price = i;
   }



   public int getLocX() {
     return this.locx;
   }

   public void setLocX(int i) {
     this.locx = i;
   }



   public int getLocY() {
     return this.locy;
   }

   public void setLocY(int i) {
     this.locy = i;
   }



   public int getLocM() {
     return this.locm;
   }

   public void setLocM(int i) {
     this.locm = i;
   }



   public int getIden() {
     return this.iden;
   }

   public void setIden(int flag) {
     this.iden = flag;
   }



   public int getAttr() {
     return this.attr;
   }

   public void setAttr(int i) {
     this.attr = i;
   }



   public int getInvGfx() {
     return this.invGfx;
   }

   public void setInvGfx(int i) {
     this.invGfx = i;
   }


   public boolean isPurchase() {
     return this._isPurchase;
   }
   public void setPurchase(boolean b) {
     this._isPurchase = b;
   } }


