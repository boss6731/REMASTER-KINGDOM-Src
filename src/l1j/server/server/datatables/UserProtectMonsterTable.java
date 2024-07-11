 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.ArrayList;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;

 public class UserProtectMonsterTable {
   private static UserProtectMonsterTable _instance;

   public static UserProtectMonsterTable getInstance() {
     if (_instance == null) {
       _instance = new UserProtectMonsterTable();
     }
     return _instance;
   }

   private static ArrayList<Integer> _list = new ArrayList<>();

   private UserProtectMonsterTable() {
     load(_list);
   }

   public void reload() {
     ArrayList<Integer> list = new ArrayList<>();
     load(list);
     _list = list;
   }

   public void load(ArrayList<Integer> list) {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("select * from UserProtectMonster");
       rs = pstm.executeQuery();
       while (rs.next()) {
         _list.add(Integer.valueOf(rs.getInt("monster_id")));
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public int getUserProtectMonsterId(int id) {
     int monid = 0;
     for (Integer a : _list) {
       if (a.intValue() == id) {
         return a.intValue();
       }
     }
     return monid;
   }
 }


