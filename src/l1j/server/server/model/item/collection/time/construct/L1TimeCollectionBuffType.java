 package l1j.server.server.model.item.collection.time.construct;

 import java.util.concurrent.ConcurrentHashMap;

 public enum L1TimeCollectionBuffType
 {
   private int _type;
   private String _name;
   private static final ConcurrentHashMap<Integer, L1TimeCollectionBuffType> TYPE_DATA;
   SHORT(1, "SHORT"),
   LONG(2, "LONG"),
   MAGIC(3, "MAGIC");
   private static final ConcurrentHashMap<String, L1TimeCollectionBuffType> NAME_DATA;

   L1TimeCollectionBuffType(int type, String name) {
     this._type = type;
     this._name = name;
   }
   public int getType() {
     return this._type;
   }
   public String getName() {
     return this._name;
   }



   static {
     TYPE_DATA = new ConcurrentHashMap<>();
     NAME_DATA = new ConcurrentHashMap<>();
     for (L1TimeCollectionBuffType type : values()) {
       TYPE_DATA.put(Integer.valueOf(type._type), type);
       NAME_DATA.put(type._name, type);
     }
   }

   public static L1TimeCollectionBuffType getType(int type) {
     return TYPE_DATA.get(Integer.valueOf(type));
   }

   public static L1TimeCollectionBuffType getType(String name) {
     return NAME_DATA.get(name);
   }
 }


