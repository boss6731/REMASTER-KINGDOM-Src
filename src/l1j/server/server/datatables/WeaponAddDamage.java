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

 public class WeaponAddDamage
 {
   public class WeaponDamage {
     int Damage = 0;
   }

   private static Logger _log = Logger.getLogger(WeaponAddDamage.class.getName());

   private static WeaponAddDamage _instance;

   private final Map<Integer, WeaponDamage> _idlist = (Map<Integer, WeaponDamage>)new FastMap();

   public static WeaponAddDamage getInstance() {
     if (_instance == null) {
       _instance = new WeaponAddDamage();
     }
     return _instance;
   }

   private WeaponAddDamage() {
     weaponAddDamage();
   }

   public void weaponAddDamage() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("select item_id, addDamege from weapon_damege");
       rs = pstm.executeQuery();

       WeaponDamage weapondamage = null;
       while (rs.next()) {
         weapondamage = new WeaponDamage();
         weapondamage.Damage = rs.getInt("addDamege");
         this._idlist.put(Integer.valueOf(rs.getInt("item_id")), weapondamage);
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
     WeaponAddDamage oldInstance = _instance;
     _instance = new WeaponAddDamage();
     if (oldInstance != null)
       oldInstance._idlist.clear();
   }

   public double getWeaponAddDamage(int itemId) {
     WeaponDamage weapondamage = this._idlist.get(Integer.valueOf(itemId));

     if (weapondamage == null) {
       return 0.0D;
     }

     return weapondamage.Damage;
   }
 }


