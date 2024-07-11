 package l1j.server.server.templates;

 public class L1ItemMessage {
   private int _itemid;
   private String _itemName;
   private int _option;
   private int _type;
   private boolean _isment;
   private String _ment;

   public int getItemId() {
     return this._itemid;
   }

   public void setItemId(int id) {
     this._itemid = id;
   }

   public String getItemName() {
     return this._itemName;
   }
   public void setItemName(String name) {
     this._itemName = name;
   }

   public int getOption() {
     return this._option;
   }
   public void setOption(int i) {
     this._option = i;
   }

   public int getType() {
     return this._type;
   }
   public void setType(int i) {
     this._type = i;
   }

   public boolean isMentuse() {
     return this._isment;
   }

   public L1ItemMessage setMentuse(boolean flag) {
     this._isment = flag;
     return this;
   }

   public String getMent() {
     return this._ment;
   }

   public void setMent(String ment) {
     this._ment = ment;
   }
 }


