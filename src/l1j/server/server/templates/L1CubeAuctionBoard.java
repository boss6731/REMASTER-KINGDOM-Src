 package l1j.server.server.templates;

 import java.util.Calendar;







 public class L1CubeAuctionBoard
 {
   private int _Id;
   private int _objectid;
   private int _itemid;
   private String _itemName;
   private int _itemType;
   private int _itemCount;
   private int _itemEnchant;
   private int _itemAttrEnchant;
   private int _itemIdentity;
   private int _itemBless;
   private int _itemprice;
   private String _oldOwner;
   private int _oldOwnerId;
   private String _newOwner;
   private int _newOwnerId;
   private Calendar _deadline;

   public int getId() {
     return this._Id;
   }

   public void setId(int i) {
     this._Id = i;
   }



   public int getObjectId() {
     return this._objectid;
   }

   public void setObjectId(int i) {
     this._objectid = i;
   }



   public int getItemId() {
     return this._itemid;
   }

   public void setItemId(int i) {
     this._itemid = i;
   }



   public String getItemName() {
     return this._itemName;
   }

   public void setItemName(String s) {
     this._itemName = s;
   }



   public int getItemType() {
     return this._itemType;
   }

   public void setItemType(int s) {
     this._itemType = s;
   }



   public int getItemCount() {
     return this._itemCount;
   }

   public void setItemCount(int i) {
     this._itemCount = i;
   }



   public int getItemEnchant() {
     return this._itemEnchant;
   }

   public void setItemEnchant(int i) {
     this._itemEnchant = i;
   }



   public int getItemAttrEnchant() {
     return this._itemAttrEnchant;
   }

   public void setItemAttrEnchant(int i) {
     this._itemAttrEnchant = i;
   }



   public int getItemIdentity() {
     return this._itemIdentity;
   }

   public void setItemIdentity(int i) {
     this._itemIdentity = i;
   }



   public int getItemBless() {
     return this._itemBless;
   }

   public void setItemBless(int i) {
     this._itemBless = i;
   }



   public int getItemPrice() {
     return this._itemprice;
   }

   public void setItemPrice(int i) {
     this._itemprice = i;
   }



   public String getOldOwner() {
     return this._oldOwner;
   }

   public void setOldOwner(String s) {
     this._oldOwner = s;
   }



   public int getOldOwnerId() {
     return this._oldOwnerId;
   }

   public void setOldOwnerId(int i) {
     this._oldOwnerId = i;
   }



   public String getNewOwner() {
     return this._newOwner;
   }

   public void setNewOwner(String s) {
     this._newOwner = s;
   }



   public int getNewOwnerId() {
     return this._newOwnerId;
   }

   public void setNewOwnerId(int i) {
     this._newOwnerId = i;
   }



   public Calendar getDeadline() {
     return this._deadline;
   }

   public void setDeadline(Calendar i) {
     this._deadline = i;
   }
 }


