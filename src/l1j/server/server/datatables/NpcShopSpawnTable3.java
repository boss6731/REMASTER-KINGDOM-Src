 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1NpcShop;
 import l1j.server.server.utils.SQLUtil;

 public class NpcShopSpawnTable3
 {
   private static Logger _log = Logger.getLogger(NpcShopSpawnTable.class
       .getName());

   private static NpcShopSpawnTable3 _instance;

   private ArrayList<L1NpcShop> npcShoplist = new ArrayList<>();

   public static NpcShopSpawnTable3 getInstance() {
     if (_instance == null) {
       _instance = new NpcShopSpawnTable3();
     }
     return _instance;
   }
   public static void reloding() {
     NpcShopSpawnTable3 oldInstance = _instance;
     _instance = new NpcShopSpawnTable3();
     if (oldInstance != null && oldInstance.npcShoplist != null)
       oldInstance.npcShoplist.clear();
   }

   private NpcShopSpawnTable3() {
     lode();
   }


   public void lode() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM spawnlist_npc_shop3");
       rs = pstm.executeQuery();

       while (rs.next())
       {


         L1NpcShop shop = new L1NpcShop();

         shop.setNpcId(rs.getInt("npc_id"));
         shop.setName(rs.getString("name"));
         shop.setX(rs.getInt("locx"));
         shop.setY(rs.getInt("locy"));
         shop.setMapId(rs.getShort("mapid"));
         shop.setHeading(rs.getInt("heading"));
         shop.setTitle(rs.getString("title"));
         shop.setShopName(rs.getString("shop_name"));

         this.npcShoplist.add(shop);
         shop = null;
       }

     } catch (Exception e) {
       _log.log(Level.SEVERE, "NpcShopSpawnTable[]Error", e);
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public ArrayList<L1NpcShop> getList() {
     return this.npcShoplist;
   }
 }


