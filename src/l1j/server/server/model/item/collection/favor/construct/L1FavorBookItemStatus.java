 package l1j.server.server.model.item.collection.favor.construct;

 import java.util.concurrent.ConcurrentHashMap;


 public enum L1FavorBookItemStatus
 {
   private int status;
   private static final ConcurrentHashMap<Integer, L1FavorBookItemStatus> DATA;
   CRAFT(0),
   ENCHANT(1),
   BLESS(2),
   CHANGE(3);

   L1FavorBookItemStatus(int status) {
     this.status = status;
   }
   public int getStatus() {
     return this.status;
   }


   static {
     DATA = new ConcurrentHashMap<>();
     for (L1FavorBookItemStatus obj : values()) {
       DATA.put(Integer.valueOf(obj.status), obj);
     }
   }

   public static L1FavorBookItemStatus getStatus(int value) {
     return DATA.get(Integer.valueOf(value));
   }
 }


