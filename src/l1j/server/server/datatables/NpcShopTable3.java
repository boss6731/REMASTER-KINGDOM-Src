 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.server.model.shop.L1Shop;
 import l1j.server.server.templates.L1ShopItem;
 import l1j.server.server.utils.SQLUtil;


 public class NpcShopTable3
 {
   private static Logger _log = Logger.getLogger(NpcShopTable3.class.getName());

   private static NpcShopTable3 _instance;

   private final Map<Integer, L1Shop> _npcShops = new HashMap<>();

   public static NpcShopTable3 getInstance() {
     if (_instance == null) {
       _instance = new NpcShopTable3();
     }
     return _instance;
   }

   public static void reloding() {
     NpcShopTable3 oldInstance = _instance;
     _instance = new NpcShopTable3();
     if (oldInstance != null && oldInstance._npcShops != null)
       oldInstance._npcShops.clear();
   }

   private NpcShopTable3() {
     loadShops();
   }

   private ArrayList<Integer> enumNpcIds() {
     ArrayList<Integer> ids = new ArrayList<>();

     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT DISTINCT npc_id FROM shop_npc3");
       rs = pstm.executeQuery();
       while (rs.next()) {
         ids.add(Integer.valueOf(rs.getInt("npc_id")));
       }
     } catch (Exception e) {
       _log.log(Level.SEVERE, "NpcShopTable3[]Error", e);
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
     return ids;
   }

   private L1Shop loadShop(int npcId, ResultSet rs) throws SQLException {
     List<L1ShopItem> sellingList = new ArrayList<>();
     List<L1ShopItem> purchasingList = new ArrayList<>();
     L1ShopItem item = null;
     while (rs.next()) {
       int itemId = rs.getInt("item_id");
       int sellingPrice = rs.getInt("selling_price");
       int purchasingPrice = rs.getInt("purchasing_price");
       int count = rs.getInt("count");
       int enchant = rs.getInt("enchant");
       if (0 <= sellingPrice) {
         item = new L1ShopItem(itemId, sellingPrice, 1, enchant, false, 0, false, 1, 0, 0, 10, 0);
         item.set_count(count);
         sellingList.add(item);
       }
       if (0 <= purchasingPrice) {
         item = new L1ShopItem(itemId, purchasingPrice, 1, enchant, false, 0, false, 1, 0, 0, 10, 0);
         purchasingList.add(item);
       }
     }
     return new L1Shop(npcId, sellingList, purchasingList);
   }

   private void loadShops() {
     ArrayList<Integer> npcids = enumNpcIds();
     for (Integer i : npcids) {
       Selector.exec("select * from shop_npc3 where npc_id=?", new SelectorHandler()
           {
             public void handle(PreparedStatement pstm) throws Exception {
               pstm.setInt(1, i.intValue());
             }


             public void result(ResultSet rs) throws Exception {
               NpcShopTable3.this._npcShops.put(i, NpcShopTable3.this.loadShop(i.intValue(), rs));
             }
           });
     }
   }

   public L1Shop get(int npcId) {
     return this._npcShops.get(Integer.valueOf(npcId));
   }


   public Map<Integer, L1Shop> get() {
     return this._npcShops;
   }
 }


