 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Set;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1SpecialMap;
 import l1j.server.server.utils.SQLUtil;



 public class SpecialMapTable
 {
   public static SpecialMapTable _instance;
   public Map<Integer, L1SpecialMap> _list = new HashMap<>();

   public static SpecialMapTable getInstance() {
     if (_instance == null) {
       _instance = new SpecialMapTable();
     }
     return _instance;
   }

   public static void reload() {
     SpecialMapTable oldInstance = _instance;
     _instance = new SpecialMapTable();
     oldInstance._list.clear();
   }

   private SpecialMapTable() {
     loadSpecialMap();
   }

     private void loadSpecialMap() {
         Connection con = null;
         PreparedStatement pstm = null;
         ResultSet rs = null;
         try {
             if (this._list.size() > 0) {
                 this._list.clear(); // 註解: 清空列表
             }
             con = L1DatabaseFactory.getInstance().getConnection();
             pstm = con.prepareStatement("SELECT * FROM Bonus_map");
             rs = pstm.executeQuery();
             while (rs.next()) {
                 L1SpecialMap SM = new L1SpecialMap();
                 int mapId = rs.getInt("地圖編號"); // 註解: 地圖編號
                 SM.setName(rs.getString("地圖名稱")); // 註解: 地圖名稱
                 double expRate = rs.getInt("額外經驗值倍率") * 0.01D; // 註解: 額外經驗值倍率
                 double dmgRate = rs.getInt("額外傷害倍率") * 0.01D; // 註解: 額外傷害倍率
                 SM.setExpRate(expRate); // 註解: 設置經驗值倍率
                 SM.setDmgRate(dmgRate); // 註解: 設置傷害倍率
                 SM.setDmgReduction(rs.getInt("傷害減免")); // 註解: 設置傷害減免
                 SM.setMdmgReduction(rs.getInt("魔法傷害減少")); // 註解: 設置魔法傷害減少
                 this._list.put(Integer.valueOf(mapId), SM); // 註解: 將地圖信息加入列表
             }

     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public L1SpecialMap getSpecialMap(int mapid) {
     return this._list.get(Integer.valueOf(mapid));
   }

   public boolean isSpecialMap(int mapid) {
     Set<Integer> keys = this._list.keySet();

     boolean OK = false;
     for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext(); ) {
       int givemon = ((Integer)iterator.next()).intValue();
       if (givemon == mapid) {
         OK = true;
         break;
       }
     }
     return OK;
   }
 }


