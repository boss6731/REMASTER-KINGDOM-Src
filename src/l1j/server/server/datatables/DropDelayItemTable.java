 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Set;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1DropDelayItem;
 import l1j.server.server.utils.SQLUtil;



 public class DropDelayItemTable
 {
   public static DropDelayItemTable _instance;
   public Map<Integer, L1DropDelayItem> _list = new HashMap<>();

   public static DropDelayItemTable getInstance() {
     if (_instance == null) {
       _instance = new DropDelayItemTable();
     }
     return _instance;
   }

   public static void reload() {
     DropDelayItemTable oldInstance = _instance;
     _instance = new DropDelayItemTable();
     oldInstance._list.clear();
   }

   private DropDelayItemTable() {
     loadDelayItem();
   }

   private void loadDelayItem() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM drop_delay_item");
       rs = pstm.executeQuery();
       while (rs.next()) {
         L1DropDelayItem PN = new L1DropDelayItem();
         int itemid = rs.getInt("item_id");
         PN.setItemId(rs.getInt("item_id"));
         PN.setItemName(rs.getString("name"));
         this._list.put(Integer.valueOf(itemid), PN);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public L1DropDelayItem getItem(int itemid) {
     return this._list.get(Integer.valueOf(itemid));
   }

   public boolean isItem(int itemid) {
     Set<Integer> keys = this._list.keySet();

     boolean OK = false;
     for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext(); ) {
       int item = ((Integer)iterator.next()).intValue();
       if (item == itemid) {
         OK = true;
         break;
       }
     }
     return OK;
   }
 }


