 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.HashMap;
 import java.util.Map;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;

 public class NoDropItemTable
 {
   private static NoDropItemTable _instance;
   private Map<Integer, L1NoDropItems> _list = new HashMap<>();

   public static NoDropItemTable getInstance() {
     if (_instance == null) {
       _instance = new NoDropItemTable();
     }
     return _instance;
   }

   public NoDropItemTable() {
     NoDropItemsLoader(this._list);
   }

   public void reload() {
     Map<Integer, L1NoDropItems> list = new HashMap<>();
     NoDropItemsLoader(list);
     this._list = list;
   }

   public void NoDropItemsLoader(Map<Integer, L1NoDropItems> list) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     L1NoDropItems nodrop_items = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM non_drop_penalty_items");
       rs = pstm.executeQuery();
       while (rs.next()) {
         nodrop_items = new L1NoDropItems();
         nodrop_items.setItemId(rs.getInt("item_id"));
         list.put(Integer.valueOf(nodrop_items.getItemId()), nodrop_items);
       }
     } catch (SQLException e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public L1NoDropItems getPresentItem(int level) {
     return this._list.get(Integer.valueOf(level));
   }


   public class L1NoDropItems
   {
     private int _item_id;


     public int getItemId() {
       return this._item_id;
     }

     public void setItemId(int i) {
       this._item_id = i;
     }
   }
 }


