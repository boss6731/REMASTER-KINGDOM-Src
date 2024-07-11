 package l1j.server.server.model.item.collection.favor.bean;

 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.item.collection.favor.construct.L1FavorBookListType;

 public class L1FavorBookUserObject
 {
   private L1FavorBookListType listType;
   private L1FavorBookTypeObject type;
   private int index;
   private L1ItemInstance currentItem;
   private L1FavorBookObject obj;

   public L1FavorBookUserObject(L1FavorBookListType listType, L1FavorBookTypeObject type, int index, L1FavorBookObject obj) {
     this(listType, type, index, null, obj);
   }

   public L1FavorBookUserObject(L1FavorBookListType listType, L1FavorBookTypeObject type, int index, L1ItemInstance currentItem, L1FavorBookObject obj) {
     this.listType = listType;
     this.type = type;
     this.index = index;
     this.currentItem = currentItem;
     this.obj = obj;
   }

   public L1FavorBookListType getListType() {
     return this.listType;
   }
   public void setListType(L1FavorBookListType listType) {
     this.listType = listType;
   }

   public L1FavorBookTypeObject getType() {
     return this.type;
   }
   public void setType(L1FavorBookTypeObject type) {
     this.type = type;
   }

   public int getIndex() {
     return this.index;
   }
   public void setIndex(int index) {
     this.index = index;
   }

   public L1ItemInstance getCurrentItem() {
     return this.currentItem;
   }
   public void setCurrentItem(L1ItemInstance currentItem) {
     this.currentItem = currentItem;
   }

   public L1FavorBookObject getObj() {
     return this.obj;
   }
   public void setObj(L1FavorBookObject obj) {
     this.obj = obj;
   }


   public String toString() {
     StringBuilder sb = new StringBuilder();
     sb.append("listType: ").append(this.listType.getName()).append("\r\n");
     sb.append("type: ").append(this.type.getType()).append("\r\n");
     sb.append("index: ").append(this.index).append("\r\n");
     sb.append("itemObjId: ").append((this.currentItem == null) ? 0 : this.currentItem.getId()).append("\r\n");
     sb.append("itemId: ").append((this.currentItem == null) ? 0 : this.currentItem.getItemId()).append("\r\n");
     return sb.toString();
   }
 }


