 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Map;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;




 public class TownNpcTax
 {
   public static TownNpcTax _instance;
   public Map<Integer, L1TownNpcTax> _list = new HashMap<>();

   public static TownNpcTax getInstance() {
     if (_instance == null) {
       _instance = new TownNpcTax();
     }
     return _instance;
   }

   public static void reload() {
     TownNpcTax oldInstance = _instance;
     _instance = new TownNpcTax();
     oldInstance._list.clear();
   }

   private TownNpcTax() {
     loadTownNpcName();
   }

   private void loadTownNpcName() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM town_npc_tax");
       rs = pstm.executeQuery();
       while (rs.next()) {
         L1TownNpcTax TN = new L1TownNpcTax();
         TN.setNpcId(rs.getInt("npc_id"));
         TN.setTownId(parseTown(rs.getString("town_id")));
         this._list.put(Integer.valueOf(TN.getNpcId()), TN);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public L1TownNpcTax getTownNpcInfo(int npcid) {
     return this._list.get(Integer.valueOf(npcid));
   }

   public boolean getTownNpcTaxCheck(int npcid) {
     this._list.get(Integer.valueOf(npcid));
     if (this._list != null)
       return true;
     return false;
   }

  private int parseTown(String type) {
     if (type.equalsIgnoreCase("說話之島"))
       return 1;
     if (type.equalsIgnoreCase("銀騎士"))
       return 2;
     if (type.equalsIgnoreCase("古魯丁"))
       return 3;
     if (type.equalsIgnoreCase("半獸人森林"))
       return 4;
     if (type.equalsIgnoreCase("風木"))
       return 5;
     if (type.equalsIgnoreCase("肯特"))
       return 6;
     if (type.equalsIgnoreCase("吉倫"))
       return 7;
     if (type.equalsIgnoreCase("海音"))
       return 8;
     if (type.equalsIgnoreCase("威爾登"))
       return 9;
     if (type.equalsIgnoreCase("歐瑞"))
       return 10;
     if (type.equalsIgnoreCase("亞丁"))
       return 12;
     return 0;
   }

   public class L1TownNpcTax {
     private int _id;
     private int _npcid;
     private int _townid;

     public int getId() {
       return this._id;
     }

     public void setId(int id) {
       this._id = id;
     }

     public int getNpcId() {
       return this._npcid;
     }

     public void setNpcId(int id) {
       this._npcid = id;
     }

     public int getTownId() {
       return this._townid;
     }

     public void setTownId(int town) {
       this._townid = town;
     }
   }
 }


