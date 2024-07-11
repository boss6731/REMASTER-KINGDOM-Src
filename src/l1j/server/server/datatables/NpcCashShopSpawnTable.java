 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.IdFactory;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.templates.L1NpcShop;
 import l1j.server.server.utils.SQLUtil;

 public class NpcCashShopSpawnTable
 {
   private static NpcCashShopSpawnTable _instance;
   private ArrayList<L1NpcShop> npcShoplist = new ArrayList<>();

   public static NpcCashShopSpawnTable getInstance() {
     if (_instance == null) {
       _instance = new NpcCashShopSpawnTable();
     }
     return _instance;
   }

   private NpcCashShopSpawnTable() {
     lode();
   }

   public static void reload() {
     NpcCashShopSpawnTable oldInstance = _instance;
     _instance = new NpcCashShopSpawnTable();
     oldInstance.npcShoplist.clear();
   }

   public void lode() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM spawnlist_npc_cash_shop");
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
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public ArrayList<L1NpcShop> getList() {
     return this.npcShoplist;
   }

   public void Start() {
     try {
       ArrayList<L1NpcShop> list = getList();
       for (int i = 0; i < list.size(); i++) {

         L1NpcShop shop = list.get(i);

         L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(shop.getNpcId());
         npc.setId(IdFactory.getInstance().nextId());
         npc.setMap(shop.getMapId());

         npc.getLocation().set(shop.getX(), shop.getY(), shop.getMapId());
         npc.getLocation().forward(5);

         npc.setHomeX(npc.getX());
         npc.setHomeY(npc.getY());
         npc.setHeading(shop.getHeading());

         npc.setName(shop.getName());
         npc.setTitle(shop.getTitle());





         L1World.getInstance().storeObject((L1Object)npc);
         L1World.getInstance().addVisibleObject((L1Object)npc);

         npc.getLight().turnOnOffLight();
       }





       list.clear();
     } catch (Exception e) {
       e.printStackTrace();
     }
   }
 }


