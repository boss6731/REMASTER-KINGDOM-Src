 package l1j.server.server.model.item.collection.time.construct;



 public enum L1TimeCollectionStatus
 {
   private int status;
   START(1),
   DELETE(2),
   CLOSE(3),
   CHANGE(4);

   L1TimeCollectionStatus(int status) {
     this.status = status;
   }
   public int getStstus() {
     return this.status;
   }
 }


