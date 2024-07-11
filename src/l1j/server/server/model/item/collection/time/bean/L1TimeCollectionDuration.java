 package l1j.server.server.model.item.collection.time.bean;

 import java.sql.ResultSet;
 import java.sql.SQLException;

 public class L1TimeCollectionDuration
 {
   private int slotIndex;
   private int enchant;
   private int hour;

   public L1TimeCollectionDuration(ResultSet rs) throws SQLException {
     this(rs
         .getInt("slotIndex"), rs
         .getInt("enchant"), rs
         .getInt("hour"));
   }

   public L1TimeCollectionDuration(int slotIndex, int enchant, int hour) {
     this.slotIndex = slotIndex;
     this.enchant = enchant;
     this.hour = hour;
   }

   public int getSlotIndex() {
     return this.slotIndex;
   }
   public int getEnchant() {
     return this.enchant;
   }
   public int getHour() {
     return this.hour;
   }
 }


