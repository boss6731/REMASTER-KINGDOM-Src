 package l1j.server.server.templates;











 public class L1TimeMap
 {
   private int id;
   private int time;
   private int DoorId;

   public L1TimeMap(int id, int time) {
     this.id = id;
     this.time = time;
   }






   public L1TimeMap(int id, int time, int DoorId) {
     this.id = id;
     this.time = time;
     this.DoorId = DoorId;
   }




   public int getId() {
     return this.id;
   }




   public int getTime() {
     return this.time;
   }




   public int getDoor() {
     return this.DoorId;
   }




   public boolean count() {
     return (this.time-- <= 0);
   }
 }


