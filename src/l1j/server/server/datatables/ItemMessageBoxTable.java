 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Set;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1ItemMessageBox;
 import l1j.server.server.utils.SQLUtil;



 public class ItemMessageBoxTable
 {
   public static ItemMessageBoxTable _instance;
   public Map<Integer, L1ItemMessageBox> _box_list = new HashMap<>();

   public static ItemMessageBoxTable getInstance() {
     if (_instance == null) {
       _instance = new ItemMessageBoxTable();
     }
     return _instance;
   }

   public static void reload() {
     ItemMessageBoxTable oldInstance = _instance;
     _instance = new ItemMessageBoxTable();
     oldInstance._box_list.clear();
   }

   private ItemMessageBoxTable() {
     loadItemMessage();
   }

   private void loadItemMessage() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM item_message_box");
       rs = pstm.executeQuery();
       while (rs.next()) {
         L1ItemMessageBox BM = new L1ItemMessageBox();
         BM.setItemId(rs.getInt("itemid"));
         BM.setItemName(rs.getString("itemname"));
         BM.setBonusItem(rs.getString("bonusitem"));
         BM.setEnchant(rs.getString("enchant"));
         BM.setMentoption(rs.getString("ment_option"));
         BM.setMent(rs.getString("ment"));
         this._box_list.put(Integer.valueOf(BM.getItemId()), BM);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public L1ItemMessageBox getBoxMessage(int itemid) {
     return this._box_list.get(Integer.valueOf(itemid));
   }

   public boolean isBoxMessage(int itemid) {
     Set<Integer> keys = this._box_list.keySet();

     boolean OK = false;
     for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext(); ) {
       int itemmsg = ((Integer)iterator.next()).intValue();
       if (itemmsg == itemid) {
         OK = true;
         break;
       }
     }
     return OK;
   }
 }


