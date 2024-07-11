 package l1j.server.server.model.item.collection.favor.construct;

 import java.util.concurrent.ConcurrentHashMap;

 public enum L1FavorBookListType
 {
   private int _type;
   private String _name;
   private static final ConcurrentHashMap<Integer, L1FavorBookListType> NUMBER_DATA;
   ALL(0, "ALL"),
   RELIC(1, "RELIC"),
   EVENT(2, "EVENT");
   private static final ConcurrentHashMap<String, L1FavorBookListType> NAME_DATA;

   L1FavorBookListType(int type, String name) {
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
     NUMBER_DATA = new ConcurrentHashMap<>();
     NAME_DATA = new ConcurrentHashMap<>();
     for (L1FavorBookListType type : values()) {
       NUMBER_DATA.put(Integer.valueOf(type._type), type);
       NAME_DATA.put(type._name, type);
     }
   }
   public static L1FavorBookListType getListType(int type) {
     return NUMBER_DATA.get(Integer.valueOf(type));
   }
   public static L1FavorBookListType getListType(String str) {
     return NAME_DATA.get(str);
   }
 }


