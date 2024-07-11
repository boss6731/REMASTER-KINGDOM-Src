 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.map.L1WorldMap;
 import l1j.server.server.utils.SQLUtil;


 public class FishingZoneTable
 {
   private static FishingZoneTable _instance;
   private final ArrayList<String> _Lockey;

   private FishingZoneTable() {
     this._Lockey = new ArrayList<>();
     load();
   }

   public static FishingZoneTable getInstance() {
     if (_instance == null) {
       _instance = new FishingZoneTable();
     }
     return _instance;
   }

   public boolean isLockey(String key) {
     return this._Lockey.contains(key);
   }

   private void load() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM fishing_zone");

       rs = pstm.executeQuery();

       while (rs.next()) {
         int srcX = rs.getInt("loc_X");
         int srcY = rs.getInt("loc_Y");
         int srcMapId = rs.getInt("loc_mapId");

         String key = srcMapId + srcX + srcY;
         this._Lockey.add(key);
         L1WorldMap.getInstance().getMap((short)srcMapId)
           .setPassable(srcX, srcY, false);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   public static void reload() {
     FishingZoneTable oldInstance = _instance;
     _instance = new FishingZoneTable();
     oldInstance._Lockey.clear();
   }
 }


