 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Map;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;

















 public class WeaponEnchantList
 {
   private static WeaponEnchantList _instance;

   public class WeaponEnchant
   {
     int Chance = 0;
   }



   private final Map<Integer, WeaponEnchant> _idlist = new HashMap<>();

   public static WeaponEnchantList getInstance() {
     if (_instance == null) {
       _instance = new WeaponEnchantList();
     }
     return _instance;
   }








   public void weaponEnchantList() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("select item_id, chance from weapon_enchant_list");
       rs = pstm.executeQuery();

       WeaponEnchant weaponenchant = null;
       while (rs.next()) {
         weaponenchant = new WeaponEnchant();

         weaponenchant.Chance = rs.getInt("chance");

         this._idlist.put(Integer.valueOf(rs.getInt("item_id")), weaponenchant);
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
     WeaponEnchantList oldInstance = _instance;
     _instance = new WeaponEnchantList();
     if (oldInstance != null)
       oldInstance._idlist.clear();
   }

   public int getWeaponEnchant(int itemId) {
     WeaponEnchant weaponenchant = this._idlist.get(Integer.valueOf(itemId));

     if (weaponenchant == null) {
       return 0;
     }

     return weaponenchant.Chance;
   }
 }


