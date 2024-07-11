 package l1j.server.server.model.item.collection.favor.bean;

 import java.util.ArrayList;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.server.model.item.collection.favor.construct.L1FavorBookListType;





 public class L1FavorBookObject
 {
   private L1FavorBookListType listType;
   private L1FavorBookTypeObject type;
   private int index;
   private ConcurrentHashMap<Integer, L1FavorBookRegistObject> register;
   private ArrayList<Integer> itemIds;

   public L1FavorBookObject(L1FavorBookListType listType, L1FavorBookTypeObject type, int index, ConcurrentHashMap<Integer, L1FavorBookRegistObject> register, ArrayList<Integer> itemIds) {
     this.listType = listType;
     this.type = type;
     this.index = index;
     this.register = register;
     this.itemIds = itemIds;
   }

   public L1FavorBookListType getListType() {
     return this.listType;
   }
   public L1FavorBookTypeObject getType() {
     return this.type;
   }
   public int getIndex() {
     return this.index;
   }
   public ConcurrentHashMap<Integer, L1FavorBookRegistObject> getRegister() {
     return this.register;
   }
   public L1FavorBookRegistObject getRegister(int descId, int bless) {
     if (this.register == null) {
       return null;
     }
     switch (this.type.getType()) {
       case 1:
         return this.register.get(Integer.valueOf(bless));
     }
     return this.register.get(Integer.valueOf(descId));
   }

   public ArrayList<Integer> getItemIds() {
     return this.itemIds;
   }


   public String toString() {
     StringBuilder sb = new StringBuilder();
     sb.append("listType : ").append(this.listType.getName()).append("\r\n");
     sb.append("type : ").append(this.type.getType()).append("\r\n");
     sb.append("index : ").append(this.index).append("\r\n");
     sb.append("register size : ").append((this.register == null) ? 0 : this.register.size()).append("\r\n");
     sb.append("itemIds size : ").append((this.itemIds == null) ? 0 : this.itemIds.size()).append("\r\n");
     return sb.toString();
   }
 }


