 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;

 public final class DropItemTable
 {
   private class dropItemData {
     public double dropRate = 1.0D; private dropItemData() {}
     public double dropAmount = 1.0D;
   }

   private static Logger _log = Logger.getLogger(DropItemTable.class.getName());

   private static DropItemTable _instance;

   private final Map<Integer, dropItemData> _dropItem = new HashMap<>();

   public static DropItemTable getInstance() {
     if (_instance == null) {
       _instance = new DropItemTable();
     }
     return _instance;
   }

   private DropItemTable() {
     loadMapsFromDatabase();
   }

   public static void reload() {
     DropItemTable oldInstance = _instance;
     _instance = new DropItemTable();
     oldInstance._dropItem.clear();
   }

   private void loadMapsFromDatabase() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM drop_item");
       dropItemData data = null;
       for (rs = pstm.executeQuery(); rs.next(); ) {
         data = new dropItemData();
         int itemId = rs.getInt("item_id");
         data.dropRate = rs.getDouble("drop_rate");
         data.dropAmount = rs.getDouble("drop_amount");

         this._dropItem.put(new Integer(itemId), data);
       }

       _log.config("drop_item " + this._dropItem.size());
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public double getDropRate(int itemId) {
     dropItemData data = this._dropItem.get(Integer.valueOf(itemId));
     if (data == null) {
       return 1.0D;
     }
     return data.dropRate;
   }

   public double getDropAmount(int itemId) {
     dropItemData data = this._dropItem.get(Integer.valueOf(itemId));
     if (data == null) {
       return 1.0D;
     }
     return data.dropAmount;
   }
 }


