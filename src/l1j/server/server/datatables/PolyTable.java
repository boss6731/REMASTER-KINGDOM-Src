 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.HashMap;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.L1PolyMorph;
 import l1j.server.server.utils.SQLUtil;




















 public class PolyTable
 {
   private static Logger _log = Logger.getLogger(PolyTable.class.getName());

   private static PolyTable _instance;

   private final HashMap<String, L1PolyMorph> _polymorphs = new HashMap<>();
   private final HashMap<Integer, L1PolyMorph> _polyIdIndex = new HashMap<>();
   private final HashMap<Integer, L1PolyMorph> _polyspeed = new HashMap<>();
   private final HashMap<Integer, Integer> _rank_polys = new HashMap<>();
   public static PolyTable getInstance() {
     if (_instance == null) {
       _instance = new PolyTable();
     }
     return _instance;
   }
   private boolean _polyEvent;
   private PolyTable() {
     loadPolymorphs();
   }

   public static void reload() {
     PolyTable oldInstance = _instance;
     _instance = new PolyTable();
     oldInstance._polymorphs.clear();
     oldInstance._polyIdIndex.clear();
     oldInstance._rank_polys.clear();
   }

   private void loadPolymorphs() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM polymorphs");
       rs = pstm.executeQuery();
       fillPolyTable(rs);
     } catch (Exception e) {
       _log.log(Level.SEVERE, "error while creating polymorph table", e);
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

     private void fillPolyTable(ResultSet rs) throws SQLException {
         L1PolyMorph poly = null;
         while (rs.next()) {
             int id = rs.getInt("id");
             String name = rs.getString("name");
             int polyId = rs.getInt("polyid");
             int minLevel = rs.getInt("minlevel");
             int weaponEquipFlg = rs.getInt("weaponequip");
             int armorEquipFlg = rs.getInt("armorequip");
             boolean canUseSkill = rs.getBoolean("isSkillUse");
             int causeFlg = rs.getInt("cause");

             int option = 0;
             if (rs.getString("option") != null) {
                 option = PolyOption(rs.getString("option"));
             }
             boolean spearGfx = rs.getString("spearGfx").equalsIgnoreCase("true");

             poly = new L1PolyMorph(id, name, polyId, minLevel, weaponEquipFlg, armorEquipFlg, canUseSkill, causeFlg, option, spearGfx);

             this._polymorphs.put(name, poly);
             this._polyIdIndex.put(Integer.valueOf(polyId), poly);
             if (name.startsWith("rangking"))
                 this._rank_polys.put(Integer.valueOf(polyId), Integer.valueOf(polyId));
         }
         _log.config("加載變身列表 " + this._polymorphs.size() + "個變身");
         // 註解: 加載變身列表，共加載了 this._polymorphs.size() 個變身
     }

   public L1PolyMorph getTemplate(String name) {
     return this._polymorphs.get(name);
   }

   public L1PolyMorph getTemplate(int polyId) {
     return this._polyIdIndex.get(Integer.valueOf(polyId));
   }



   public boolean isPolyEvent() {
     return this._polyEvent;
   }

   public void setPolyEvent(boolean i) {
     this._polyEvent = i;
   }

   public L1PolyMorph find(int polyId) {
     L1PolyMorph poly = null;
     for (L1PolyMorph each : this._polymorphs.values()) {
       if (each.getPolyId() == polyId) {
         poly = each;

         break;
       }
     }
     return poly;
   }


   private int PolyOption(String type) {
     if (type.equalsIgnoreCase("Poly_Scroll")) {
       return 1;
     }
     if (type.equalsIgnoreCase("Poly_RingMaster")) {
       return 2;
     }
     if (type.equalsIgnoreCase("Poly_Event")) {
       return 3;
     }
     if (type.equalsIgnoreCase("Poly_Class")) {
       return 4;
     }
     if (type.equalsIgnoreCase("Poly_EV")) {
       return 5;
     }
     return 0;
   }

   public boolean isRankingPoly(int sprId) {
     return this._rank_polys.containsKey(Integer.valueOf(sprId));
   }

   public boolean getSpeed(int polyId) {
     return (this._polyspeed.get(Integer.valueOf(polyId)) != null);
   }
 }


/* Location:              D:\天堂R\REMASTER KINGDOM\TheDay.jar!\l1j\server\server\datatables\PolyTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */