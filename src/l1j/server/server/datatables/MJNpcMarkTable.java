 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.utils.SQLUtil;

 public class MJNpcMarkTable {
   private static MJNpcMarkTable _instance;

   public static MJNpcMarkTable getInstance() {
     if (_instance == null)
       _instance = new MJNpcMarkTable();
     return _instance;
   }
   private HashMap<Integer, Integer> _marks;
   public static void reload() {
     MJNpcMarkTable tmp = _instance;
     _instance = new MJNpcMarkTable();
     if (tmp != null) {
       tmp.clear();
       tmp = null;
     }
   }


   private MJNpcMarkTable() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     this._marks = new HashMap<>(32);
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM tb_npc_mark");
       rs = pstm.executeQuery();
       for (; rs.next(); this._marks.put(Integer.valueOf(rs.getInt("npcid")), Integer.valueOf(rs.getInt("markid"))));
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
   }

   public int get(int i) {
     Integer ig = this._marks.get(Integer.valueOf(i));
     if (ig == null)
       return 0;
     return ig.intValue();
   }

   public void clear() {
     if (this._marks != null) {
       this._marks.clear();
       this._marks = null;
     }
   }
 }


