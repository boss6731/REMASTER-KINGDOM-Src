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
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.templates.L1ShopItem;
 import l1j.server.server.utils.SQLUtil;


 public class NpcShopTable
 {
   private static Logger _log = Logger.getLogger(NpcShopTable.class.getName());

   private static NpcShopTable _instance;

   private final Map<Integer, L1Shop> _npcShops = new HashMap<>();

   public static NpcShopTable getInstance() {
     if (_instance == null) {
       _instance = new NpcShopTable();
     }
     return _instance;
   }

   public static void reloding() {
     NpcShopTable oldInstance = _instance;
     _instance = new NpcShopTable();
     if (oldInstance != null && oldInstance._npcShops != null)
       oldInstance._npcShops.clear();
   }

   private NpcShopTable() {
     loadShops();
   }

   private ArrayList<Integer> enumNpcIds() {
     ArrayList<Integer> ids = new ArrayList<>();

     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT DISTINCT npc_id FROM shop_npc");
       rs = pstm.executeQuery();
       while (rs.next()) {
         ids.add(Integer.valueOf(rs.getInt("npc_id")));
       }
     } catch (Exception e) {
       _log.log(Level.SEVERE, "NpcShopTable[]Error", e);
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
         L1Item temp = ItemTable.getInstance().getTemplate(itemId);

         item = new L1ShopItem(itemId, sellingPrice, 1, enchant, temp.getBless());
         item.set_count(count);
         sellingList.add(item);
       }
       if (0 <= purchasingPrice) {
         L1Item temp = ItemTable.getInstance().getTemplate(itemId);

         item = new L1ShopItem(itemId, sellingPrice, 1, enchant, temp.getBless());
         purchasingList.add(item);
       }
     }
     return new L1Shop(npcId, sellingList, purchasingList);
   }

   private void loadShops() {
     ArrayList<Integer> npcids = enumNpcIds();
     for (Integer i : npcids) {
       Selector.exec("select * from shop_npc where npc_id=?", new SelectorHandler()
           {
             public void handle(PreparedStatement pstm) throws Exception {
               pstm.setInt(1, i.intValue());
             }


             public void result(ResultSet rs) throws Exception {
               NpcShopTable.this._npcShops.put(i, NpcShopTable.this.loadShop(i.intValue(), rs));
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


