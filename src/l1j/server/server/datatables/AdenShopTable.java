 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.Collections;
 import java.util.HashMap;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1AdenShopItem;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.utils.SQLUtil;





















 public class AdenShopTable
 {
   private static AdenShopTable _instance;
   private final HashMap<Integer, L1AdenShopItem> _allShops = new HashMap<>();

   public static int data_length = 0;

   public static AdenShopTable getInstance() {
     if (_instance == null) {
       _instance = new AdenShopTable();
     }
     return _instance;
   }

   public static void reload() {
     AdenShopTable oldInstance = _instance;
     _instance = new AdenShopTable();
     if (oldInstance != null && oldInstance._allShops != null)
       oldInstance._allShops.clear();
   }

   private AdenShopTable() {
     loadShops();
   }

   private void loadShops() {
     data_length = 0;
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM shop_aden order by id asc");
       rs = pstm.executeQuery();
       while (rs.next()) {
         int id = rs.getInt("id");
         int itemid = rs.getInt("itemid");
         int icon_id = rs.getInt("icon_id");
         int price = rs.getInt("price");
         int type = rs.getInt("type");
         int status = rs.getInt("status");
         String html = rs.getString("html");
         int pack = rs.getInt("pack");
         if (type < 2 || type > 5)
           type = 5;
         this._allShops.put(Integer.valueOf(itemid), new L1AdenShopItem(id, itemid, icon_id, price, pack, html, status, type));


         L1Item item = ItemTable.getInstance().getTemplate(itemid);
         String itemname = item.getName();
         if (pack > 1)
           itemname = itemname + "(" + pack + ")";
         if (item.getMaxUseTime() > 0) {
           itemname = itemname + " [" + item.getMaxUseTime() + "]";
         }









         data_length += 30;
         data_length += (itemname.getBytes("UTF-16LE")).length + 2;
         if (!html.equalsIgnoreCase("")) {
           byte[] test = html.getBytes("MS949");
           for (int i = 0; i < test.length; ) {
             if ((test[i] & 0xFF) >= 127) {
               i += 2;
             } else {
               i++;
             }  data_length += 2;
           }
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public L1AdenShopItem get(int itemid) {
     return this._allShops.get(Integer.valueOf(itemid));
   }

   public int Size() {
     return this._allShops.size();
   }

   public Collection<L1AdenShopItem> toArray() {
     ArrayList<L1AdenShopItem> list = new ArrayList<>(this._allShops.values());
     Collections.sort(list);
     return list;
   }
 }


