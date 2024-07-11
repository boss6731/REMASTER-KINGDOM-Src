 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import l1j.server.Config;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;

 public class WeaponEnchantInformationTable {
   private static WeaponEnchantInformationTable _instance;
   private final Map<Integer, HashMap<Integer, Integer>> _list = new HashMap<>();

   public static WeaponEnchantInformationTable getInstance() {
     if (_instance == null) {
       _instance = new WeaponEnchantInformationTable();
     }
     return _instance;
   }
   private WeaponEnchantInformationTable() {
     loadInformation();
   }
   public static void reload() {
     WeaponEnchantInformationTable oldInstance = _instance;
     _instance = new WeaponEnchantInformationTable();
     oldInstance._list.clear();
   }
   private void loadInformation() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM weapon_enchant_list WHERE item_id = ?");
       for (Iterator<Integer> iterator = enumItemIds().iterator(); iterator.hasNext(); ) { int items = ((Integer)iterator.next()).intValue();
         HashMap<Integer, Integer> _optionMap = new HashMap<>();
         pstm.setInt(1, items);
         rs = pstm.executeQuery();
         while (rs.next()) {
           _optionMap.put(Integer.valueOf(rs.getInt("enchant_lvl")), Integer.valueOf(rs.getInt("chance")));
         }
         this._list.put(Integer.valueOf(items), _optionMap);
         rs.close(); }

     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   private ArrayList<Integer> enumItemIds() {
     ArrayList<Integer> ids = new ArrayList<>();

     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT DISTINCT item_id FROM weapon_enchant_list");
       rs = pstm.executeQuery();
       while (rs.next()) {
         ids.add(Integer.valueOf(rs.getInt("item_id")));
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
     return ids;
   }

   public int getChance(int itemId, int enchant) {
     if (this._list.get(Integer.valueOf(itemId)) == null || ((HashMap)this._list.get(Integer.valueOf(itemId))).get(Integer.valueOf(enchant)) == null || ((HashMap)this._list.get(Integer.valueOf(itemId))).isEmpty()) {
       return Config.ServerRates.EnchantChanceWeapon;
     }
     return ((Integer)((HashMap)this._list.get(Integer.valueOf(itemId))).get(Integer.valueOf(enchant))).intValue();
   }
 }


