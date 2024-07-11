 package l1j.server.server.model.item.collection.time.bean;

 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.Iterator;
 import l1j.server.server.model.Instance.L1ItemInstance;


 public class L1TimeCollectionMaterial
 {
   private int flag;
   private int slotIndex;
   private ArrayList<Integer> descIds;
   private int enchant;

   public L1TimeCollectionMaterial(ResultSet rs) throws SQLException {
     this(rs.getInt("flag"), rs.getInt("slotIndex"), new ArrayList<>(), rs.getInt("enchant"));
     String[] array = rs.getString("descIds").split(",");
     for (String id : array) {
       this.descIds.add(Integer.valueOf(Integer.parseInt(id.trim())));
     }
   }

   public L1TimeCollectionMaterial(int flag, int slotIndex, ArrayList<Integer> descIds, int enchant) {
     this.flag = flag;
     this.slotIndex = slotIndex;
     this.descIds = descIds;
     this.enchant = enchant;
   }

   public int getFlag() {
     return this.flag;
   }
   public int getSlotIndex() {
     return this.slotIndex;
   }
   public ArrayList<Integer> getDescIds() {
     return this.descIds;
   }
   public int getEnchant() {
     return this.enchant;
   }

   public boolean isMaterial(L1ItemInstance item) {
     if (item.getEnchantLevel() < this.enchant) {
       return false;
     }
     return this.descIds.contains(Integer.valueOf(item.getItem().getItemDescId()));
   }


   public String toString() {
     StringBuilder sb = new StringBuilder();
     sb.append("flag: ").append(this.flag).append("\r\n");
     sb.append("slotIndex: ").append(this.slotIndex).append("\r\n");
     sb.append("enchant: ").append(this.enchant).append("\r\n");
     sb.append("descIds: ").append(this.descIds).append("\r\n");

     for (Iterator<Integer> iterator = this.descIds.iterator(); iterator.hasNext(); ) { int descid = ((Integer)iterator.next()).intValue();
       sb.append(descid).append("\r\n"); }

     sb.append("---------------\r\n");
     return sb.toString();
   }
 }


