 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Set;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1TownNpcInfo;
 import l1j.server.server.utils.SQLUtil;




 public class TownNpcInfoTable
 {
   public static TownNpcInfoTable _instance;
   public Map<String, L1TownNpcInfo> _list = new HashMap<>();

   public static TownNpcInfoTable getInstance() {
     if (_instance == null) {
       _instance = new TownNpcInfoTable();
     }
     return _instance;
   }

   public static void reload() {
     TownNpcInfoTable oldInstance = _instance;
     _instance = new TownNpcInfoTable();
     oldInstance._list.clear();
   }

   private TownNpcInfoTable() {
     loadTownNpcName();
   }

   private void loadTownNpcName() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM town_npc_info");
       rs = pstm.executeQuery();
       while (rs.next()) {
         L1TownNpcInfo TN = new L1TownNpcInfo();
         TN.setNpcId(rs.getInt("npc_id"));
         TN.setNpcName(rs.getString("npc_name"));
         TN.setSprId(rs.getInt("sprite_id"));
         TN.setTownId(parseTown(rs.getString("town_id")));
         String key = TN.getNpcId() + ":" + TN.getTownId();
         this._list.put(key, TN);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public L1TownNpcInfo getTownNpcInfo(String npcid) {
     return this._list.get(npcid);
   }

   public boolean isTownNpcInfo(String npcid) {
     Set<String> keys = this._list.keySet();

     boolean OK = false;
     for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
       String townnpc = iterator.next();
       if (townnpc.equalsIgnoreCase(npcid)) {
         OK = true;
         break;
       }
     }
     return OK;
   }

   private int parseTown(String type) {
     if (type.equalsIgnoreCase("TalkingIsland"))
       return 1;
     if (type.equalsIgnoreCase("SilverKnight"))
       return 2;
     if (type.equalsIgnoreCase("Gludio"))
       return 3;
     if (type.equalsIgnoreCase("OrcForest"))
       return 4;
     if (type.equalsIgnoreCase("Windawood"))
       return 5;
     if (type.equalsIgnoreCase("Kent"))
       return 6;
     if (type.equalsIgnoreCase("Giran"))
       return 7;
     if (type.equalsIgnoreCase("Heine"))
       return 8;
     if (type.equalsIgnoreCase("Weldern"))
       return 9;
     if (type.equalsIgnoreCase("Oren"))
       return 10;
     if (type.equalsIgnoreCase("Aden"))
       return 12;
     return 0;
   }
 }


