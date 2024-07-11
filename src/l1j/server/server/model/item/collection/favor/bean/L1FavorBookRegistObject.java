 package l1j.server.server.model.item.collection.favor.bean;

 import l1j.server.server.model.item.collection.favor.construct.L1FavorBookItemStatus;

 public class L1FavorBookRegistObject
 {
   private int descId;
   private int craftId;
   private L1FavorBookItemStatus status;

   public L1FavorBookRegistObject(String[] array) {
     this(
         Integer.parseInt(array[0].trim()),
         Integer.parseInt(array[1].trim()),
         L1FavorBookItemStatus.getStatus(Integer.parseInt(array[2].trim())));
   }


   public L1FavorBookRegistObject(int descId, int craftId, L1FavorBookItemStatus status) {
     this.descId = descId;
     this.craftId = craftId;
     this.status = status;
   }

   public int getDescId() {
     return this.descId;
   }
   public int getCraftId() {
     return this.craftId;
   }
   public L1FavorBookItemStatus getStatus() {
     return this.status;
   }


   public String toString() {
     StringBuilder sb = new StringBuilder();
     sb.append("descId: ").append(this.descId).append("\r\n");
     sb.append("craftId: ").append(this.craftId).append("\r\n");
     sb.append("status: ").append(this.status.name()).append("\r\n");
     return sb.toString();
   }
 }


