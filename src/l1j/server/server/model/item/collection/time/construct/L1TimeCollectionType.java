 package l1j.server.server.model.item.collection.time.construct;

 import java.util.concurrent.ConcurrentHashMap;

 public enum L1TimeCollectionType
 {
   private int _type;
   private String _name;
   private static final ConcurrentHashMap<Integer, L1TimeCollectionType> TYPE_DATA;
   NORMAL(1, "NORMAL"),
   SPECIAL(2, "SPECIAL");
   private static final ConcurrentHashMap<String, L1TimeCollectionType> NAME_DATA;

   L1TimeCollectionType(int type, String name) {
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
     for (L1TimeCollectionType type : values()) {
       TYPE_DATA.put(Integer.valueOf(type._type), type);
       NAME_DATA.put(type._name, type);
     }
   }

   public static L1TimeCollectionType getType(int type) {
     return TYPE_DATA.get(Integer.valueOf(type));
   }

   public static L1TimeCollectionType getType(String name) {
     return NAME_DATA.get(name);
   }
 }


