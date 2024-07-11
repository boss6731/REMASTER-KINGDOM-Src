 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Map;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;



















 public class ArmorEnchantList
 {
   public class ArmorEnchant
   {
     int Chance = 0;
   }

   private static Logger _log = Logger.getLogger(ArmorEnchantList.class.getName());

   private static ArmorEnchantList _instance;

   private final Map<Integer, ArmorEnchant> _idlist = new HashMap<>();

   public static ArmorEnchantList getInstance() {
     if (_instance == null) {
       _instance = new ArmorEnchantList();
     }
     return _instance;
   }








   public void armorEnchantList() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("select item_id, chance from armor_enchant_list");
       rs = pstm.executeQuery();

       ArmorEnchant armorenchant = null;
       while (rs.next()) {
         armorenchant = new ArmorEnchant();

         armorenchant.Chance = rs.getInt("chance");

         this._idlist.put(Integer.valueOf(rs.getInt("item_id")), armorenchant);
       }

     } catch (Exception e) {
       _log.log(Level.SEVERE, "ArmorEnchantList[]Error", e);
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public static void reload() {
     ArmorEnchantList oldInstance = _instance;
     _instance = new ArmorEnchantList();
     if (oldInstance != null)
       oldInstance._idlist.clear();
   }

   public int getArmorEnchant(int itemId) {
     ArmorEnchant armorenchant = this._idlist.get(Integer.valueOf(itemId));

     if (armorenchant == null) {
       return 0;
     }

     return armorenchant.Chance;
   }
 }


