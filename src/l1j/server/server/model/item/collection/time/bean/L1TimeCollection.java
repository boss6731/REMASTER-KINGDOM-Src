 package l1j.server.server.model.item.collection.time.bean;

 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionBuffType;
 import l1j.server.server.model.item.collection.time.construct.L1TimeCollectionType;






 public class L1TimeCollection
 {
   private int flag;
   private L1TimeCollectionType type;
   private int collectionIndex;
   private HashMap<Integer, L1TimeCollectionMaterial> material;
   private HashMap<Integer, ConcurrentHashMap<Integer, L1TimeCollectionDuration>> duration;
   private HashMap<Integer, ConcurrentHashMap<L1TimeCollectionBuffType, L1TimeCollectionAblity>> ablity;
   private HashMap<Integer, L1TimeCollectionDuration> lastDuration;
   private int lastAblitySum;

   public L1TimeCollection(ResultSet rs) throws SQLException {
     this(rs.getInt("flag"), L1TimeCollectionType.getType(rs.getString("type")), rs.getInt("collectionIndex"));
   }

   public L1TimeCollection(int flag, L1TimeCollectionType type, int collectionIndex) {
     this.flag = flag;
     this.type = type;
     this.collectionIndex = collectionIndex;
     this.material = new HashMap<>();
     this.duration = new HashMap<>();
     this.ablity = new HashMap<>();
   }

   public int getFlag() {
     return this.flag;
   }

   public L1TimeCollectionType getType() {
     return this.type;
   }

   public int getCollectionIndex() {
     return this.collectionIndex;
   }

   public L1TimeCollectionMaterial getMaterial(int slotIndex) {
     return this.material.get(Integer.valueOf(slotIndex));
   }

   public void putMaterial(L1TimeCollectionMaterial obj) {
     this.material.put(Integer.valueOf(obj.getSlotIndex()), obj);
   }

   public int getSlotSize() {
     return this.material.size();
   }

   public int getDuration(int slotIndex, int enchant) {
     L1TimeCollectionDuration last = this.lastDuration.get(Integer.valueOf(slotIndex));
     if (last != null && enchant > last.getEnchant()) {
       return last.getHour();
     }
     ConcurrentHashMap<Integer, L1TimeCollectionDuration> map = this.duration.get(Integer.valueOf(slotIndex));
     if (map == null) {
       return 0;
     }
     L1TimeCollectionDuration obj = map.get(Integer.valueOf(enchant));
     if (obj == null) {
       return 0;
     }
     return obj.getHour();
   }

   public void putDuration(L1TimeCollectionDuration obj) {
     ConcurrentHashMap<Integer, L1TimeCollectionDuration> map = this.duration.get(Integer.valueOf(obj.getSlotIndex()));
     if (map == null) {
       map = new ConcurrentHashMap<>();
       this.duration.put(Integer.valueOf(obj.getSlotIndex()), map);
     }
     map.put(Integer.valueOf(obj.getEnchant()), obj);
   }

   public L1TimeCollectionAblity getAblity(int sum, L1TimeCollectionBuffType buffType) {
     ConcurrentHashMap<L1TimeCollectionBuffType, L1TimeCollectionAblity> map = this.ablity.get(Integer.valueOf(sum));
     if (map == null &&
       sum > this.lastAblitySum) {
       map = this.ablity.get(Integer.valueOf(this.lastAblitySum));
     }

     if (map == null || map.isEmpty()) {
       return null;
     }
     return map.get(buffType);
   }

   public void putAblity(L1TimeCollectionAblity obj) {
     ConcurrentHashMap<L1TimeCollectionBuffType, L1TimeCollectionAblity> map = this.ablity.get(Integer.valueOf(obj.getSum()));
     if (map == null) {
       map = new ConcurrentHashMap<>();
       this.ablity.put(Integer.valueOf(obj.getSum()), map);
     }
     map.put(obj.getBuffType(), obj);
   }

   public void setLastValue() {
     this.lastDuration = calcLastDuration();
     this.lastAblitySum = calcLastAblity();
   }

   private int calcLastAblity() {
     Iterator<Integer> it = this.ablity.keySet().iterator();
     int last = 0;
     while (it.hasNext()) {
       int current = ((Integer)it.next()).intValue();
       if (current > last) {
         last = current;
       }
     }
     return last;
   }

   private HashMap<Integer, L1TimeCollectionDuration> calcLastDuration() {
     HashMap<Integer, L1TimeCollectionDuration> result = new HashMap<>();

     for (Map.Entry<Integer, ConcurrentHashMap<Integer, L1TimeCollectionDuration>> entry : this.duration.entrySet()) {
       int lastEnchant = 0;
       L1TimeCollectionDuration lastObj = null;
       for (Map.Entry<Integer, L1TimeCollectionDuration> entry2 : (Iterable<Map.Entry<Integer, L1TimeCollectionDuration>>)((ConcurrentHashMap)entry.getValue()).entrySet()) {
         int enchant = ((Integer)entry2.getKey()).intValue();
         if (enchant > lastEnchant) {
           lastEnchant = enchant;
           lastObj = entry2.getValue();
         }
       }
       if (lastEnchant > 0 && lastObj != null) {
         result.put(entry.getKey(), lastObj);
       }
     }

     return result;
   }


   public String toString() {
     StringBuilder sb = new StringBuilder();
     sb.append("flag: ").append(this.flag).append("\r\n");
     sb.append("type: ").append(this.type.getName()).append("\r\n");
     sb.append("collectionIndex: ").append(this.collectionIndex).append("\r\n");
     sb.append("material size: ").append(this.material.size()).append("\r\n");
     sb.append("duration size: ").append(this.duration.size()).append("\r\n");
     sb.append("ablity size: ").append(this.ablity.size()).append("\r\n");
     return sb.toString();
   }
 }


