 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.Map;
 import java.util.logging.Logger;
 import javolution.util.FastMap;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;

 public final class ResolventTable1
 {
   private static Logger _log = Logger.getLogger(ResolventTable.class.getName());

   private static ResolventTable1 _instance;

   private final Map<Integer, Integer> _resolvent1 = (Map<Integer, Integer>)new FastMap();

   public static ResolventTable1 getInstance() {
     if (_instance == null) {
       _instance = new ResolventTable1();
     }
     return _instance;
   }

   private ResolventTable1() {
     loadMapsFromDatabase();
   }

   public static void reload() {
     ResolventTable1 oldInstance = _instance;
     _instance = new ResolventTable1();
     oldInstance._resolvent1.clear();
   }

   private void loadMapsFromDatabase() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM resolvent1");

       for (rs = pstm.executeQuery(); rs.next(); ) {
         int itemId = rs.getInt("item_id");
         int crystalCount = rs.getInt("crystal_count");

         this._resolvent1.put(new Integer(itemId), Integer.valueOf(crystalCount));
       }

       _log.config("resolvent " + this._resolvent1.size());
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
     if (this._resolvent1.containsKey(Integer.valueOf(itemId))) {
       crystalCount = ((Integer)this._resolvent1.get(Integer.valueOf(itemId))).intValue();
     }
     return crystalCount;
   }
 }


