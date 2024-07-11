 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.Iterator;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;





















 public class NoDropItem
 {
   private static NoDropItem _instance;
   private static ArrayList<Integer> _idlist = new ArrayList<>();


   public static NoDropItem getInstance() {
     if (_instance == null) {
       _instance = new NoDropItem();
     }
     return _instance;
   }


   private NoDropItem() {
     _idlist = allIdList();
   }


   private static ArrayList<Integer> allIdList() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     ArrayList<Integer> idlist = new ArrayList<>();
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("select * from NoDropItem");
       rs = pstm.executeQuery();
       while (rs.next()) {
         idlist.add(Integer.valueOf(rs.getInt("item_id")));
       }
     }
     catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }

     return idlist;
   }


   public void storeId(int itemid) {
     int index = _idlist.indexOf(Integer.valueOf(itemid));
     if (index != -1) {
       return;
     }
     Connection con = null;
     PreparedStatement pstm = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("INSERT INTO NoDropItem SET item_id=?");
       pstm.setInt(1, itemid);
       pstm.execute();
       _idlist.add(Integer.valueOf(itemid));
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   public void deleteId(int itemid) {
     Connection con = null;
     PreparedStatement pstm = null;
     int index = _idlist.indexOf(Integer.valueOf(itemid));
     if (index == -1) {
       return;
     }
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("DELETE FROM NoDropItem WHERE item_id=?");
       pstm.setInt(1, itemid);
       pstm.execute();
       _idlist.remove(index);
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }


   public static void reload() {
     _idlist.clear();
     _idlist = allIdList();
   }


   public ArrayList<Integer> getIdList() {
     return _idlist;
   }


   public boolean isNoDropItem(int itemId) {
     for (Iterator<Integer> iterator = _idlist.iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
       if (itemId == id) {
         return true;
       } }

     return false;
   }
 }


