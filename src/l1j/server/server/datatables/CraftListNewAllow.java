 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import java.util.Iterator;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;


 public class CraftListNewAllow
 {
   private static CraftListNewAllow _instance;
   private ArrayList<Integer> _idlist = new ArrayList<>();

   public static CraftListNewAllow getInstance() {
     if (_instance == null) {
       _instance = new CraftListNewAllow();
     }
     return _instance;
   }


   public void reload() {
     CraftListNewAllow old = _instance;
     _instance = new CraftListNewAllow();
     old._idlist.clear();
     old = null;
   }

   private CraftListNewAllow() {
     this._idlist = allIdList();
   }

   private ArrayList<Integer> allIdList() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     ArrayList<Integer> idlist = new ArrayList<>();
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("select * from craftlist_new_allow");
       rs = pstm.executeQuery();
       while (rs.next()) {
         idlist.add(Integer.valueOf(rs.getInt("craft_id")));
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

   public boolean isCraft(int craft_id) {
     for (Iterator<Integer> iterator = this._idlist.iterator(); iterator.hasNext(); ) { int id = ((Integer)iterator.next()).intValue();
       if (craft_id == id) {
         return true;
       } }

     return false;
   }
 }


