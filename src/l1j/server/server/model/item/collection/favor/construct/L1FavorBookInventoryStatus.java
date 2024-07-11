 package l1j.server.server.model.item.collection.favor.construct;



 public enum L1FavorBookInventoryStatus
 {
   private int status;
   STORE(0),
   LIST(1);

   L1FavorBookInventoryStatus(int status) {
     this.status = status;
   }
   public int getStatus() {
     return this.status;
   }
 }


