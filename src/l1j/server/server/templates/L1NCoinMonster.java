 package l1j.server.server.templates;

 public class L1NCoinMonster
 {
   private int _npcid;
   private String _npcName;
   private int _ncoin;
   private int _effid;
   private boolean alleffect;
   private boolean _isment;
   private boolean _isItem;
   private int _itemid;
   private int _itemcount;

   public boolean isGiveItem() {
     return this._isItem;
   }
   public void setGiveItem(boolean flag) {
     this._isItem = flag;
   }

   public int getItemId() {
     return this._itemid;
   }
   public void setItemId(int id) {
     this._itemid = id;
   }

   public int getItemCount() {
     return this._itemcount;
   }
   public void setItemCount(int i) {
     this._itemcount = i;
   }

   public int getNpcId() {
     return this._npcid;
   }
   public void setNpcId(int id) {
     this._npcid = id;
   }

   public String getNpcName() {
     return this._npcName;
   }
   public void setNpcName(String name) {
     this._npcName = name;
   }

   public int getNCoin() {
     return this._ncoin;
   }
   public void setNCoin(int coin) {
     this._ncoin = coin;
   }

   public int getEffectNum() {
     return this._effid;
   }
   public void setEffectNum(int num) {
     this._effid = num;
   }

   public boolean isAllEffect() {
     return this.alleffect;
   }
   public void setAllEffect(boolean flag) {
     this.alleffect = flag;
   }

   public boolean isMent() {
     return this._isment;
   }

   public void setMent(boolean flag) {
     this._isment = flag;
   }
 }


