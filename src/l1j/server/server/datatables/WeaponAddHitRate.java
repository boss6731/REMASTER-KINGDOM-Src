 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.Map;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import javolution.util.FastMap;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;

 public class WeaponAddHitRate
 {
   public class WeaponHitrate {
     int Hit = 0;
   }

   private static Logger _log = Logger.getLogger(WeaponAddHitRate.class.getName());

   private static WeaponAddHitRate _instance;

   private final Map<Integer, WeaponHitrate> _idlist = (Map<Integer, WeaponHitrate>)new FastMap();

   public static WeaponAddHitRate getInstance() {
     if (_instance == null) {
       _instance = new WeaponAddHitRate();
     }
     return _instance;
   }

   private WeaponAddHitRate() {
     weaponAddHitRate();
   }

   public void weaponAddHitRate() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("select item_id, addHitRate from weapon_damege");
       rs = pstm.executeQuery();
       WeaponHitrate weaponhit = null;
       while (rs.next()) {
         weaponhit = new WeaponHitrate();
         weaponhit.Hit = rs.getInt("addHitRate");
         this._idlist.put(Integer.valueOf(rs.getInt("item_id")), weaponhit);
       }

     } catch (Exception e) {
       _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public static void reload() {
     WeaponAddHitRate oldInstance = _instance;
     _instance = new WeaponAddHitRate();
     if (oldInstance != null)
       oldInstance._idlist.clear();
   }

   public double getWeaponAddHitRate(int itemId) {
     WeaponHitrate weaponhit = this._idlist.get(Integer.valueOf(itemId));

     if (weaponhit == null) {
       return 0.0D;
     }

     return weaponhit.Hit;
   }
 }


