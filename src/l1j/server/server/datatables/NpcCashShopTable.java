 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.shop.L1Shop;
 import l1j.server.server.templates.L1ShopItem;
 import l1j.server.server.utils.SQLUtil;


 public class NpcCashShopTable
 {
   private static NpcCashShopTable _instance;
   private final Map<Integer, L1Shop> _npcShops = new HashMap<>();

   public static NpcCashShopTable getInstance() {
     if (_instance == null) {
       _instance = new NpcCashShopTable();
     }
     return _instance;
   }

   public static void reloding() {
     NpcCashShopTable oldInstance = _instance;
     _instance = new NpcCashShopTable();
     oldInstance._npcShops.clear();
   }

   private NpcCashShopTable() {
     loadShops();
   }

   private ArrayList<Integer> enumNpcIds() {
     ArrayList<Integer> ids = new ArrayList<>();

     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT DISTINCT npc_id FROM shop_cash");
       rs = pstm.executeQuery();
       while (rs.next()) {
         ids.add(Integer.valueOf(rs.getInt("npc_id")));
       }
     } catch (Exception e) {
       e.printStackTrace();
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
         item.setCount(count);
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
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM shop_cash WHERE npc_id=?");
       L1Shop shop = null;
       for (Iterator<Integer> iterator = enumNpcIds().iterator(); iterator.hasNext(); ) { int npcId = ((Integer)iterator.next()).intValue();
         pstm.setInt(1, npcId);
         rs = pstm.executeQuery();
         shop = loadShop(npcId, rs);
         this._npcShops.put(Integer.valueOf(npcId), shop);
         rs.close(); }

     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public L1Shop get(int npcId) {
     return this._npcShops.get(Integer.valueOf(npcId));
   }
 }


