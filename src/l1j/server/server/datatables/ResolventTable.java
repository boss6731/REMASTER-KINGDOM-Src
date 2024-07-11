 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;

 public final class ResolventTable
 {
   private static Logger _log = Logger.getLogger(ResolventTable.class.getName());

   private static ResolventTable _instance;

   private final Map<Integer, Integer> _resolvent = new HashMap<>();

   public static ResolventTable getInstance() {
     if (_instance == null) {
       _instance = new ResolventTable();
     }
     return _instance;
   }

   private ResolventTable() {
     loadMapsFromDatabase();
   }

   public static void reload() {
     ResolventTable oldInstance = _instance;
     _instance = new ResolventTable();
     oldInstance._resolvent.clear();
   }

   private void loadMapsFromDatabase() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM resolvent");

       for (rs = pstm.executeQuery(); rs.next(); ) {
         int itemId = rs.getInt("item_id");
         int crystalCount = rs.getInt("crystal_count");

         this._resolvent.put(new Integer(itemId), Integer.valueOf(crystalCount));
       }

       _log.config("resolvent " + this._resolvent.size());
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public int getCrystalCount(int itemId) {
     int crystalCount = 0;
     if (this._resolvent.containsKey(Integer.valueOf(itemId))) {
       crystalCount = ((Integer)this._resolvent.get(Integer.valueOf(itemId))).intValue();
     }
     return crystalCount;
   }
 }


