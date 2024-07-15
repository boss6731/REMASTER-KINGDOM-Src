 package l1j.server.server.datatables;

 import java.lang.reflect.Constructor;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.HashMap;
 import java.util.Map;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.trap.L1Trap;
 import l1j.server.server.storage.TrapStorage;
 import l1j.server.server.utils.SQLUtil;





















 public class TrapTable
 {
   private static TrapTable _instance;
   private Map<Integer, L1Trap> _traps = new HashMap<>();

   private TrapTable() {
     initialize();
   }


   private L1Trap createTrapInstance(String name, TrapStorage storage) throws Exception {
     String packageName = "l1j.server.server.model.trap.";

     Constructor<?> con = Class.forName("l1j.server.server.model.trap." + name).getConstructor(new Class[] { TrapStorage.class });

     return (L1Trap)con.newInstance(new Object[] { storage });
   }

   private void initialize() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();

       pstm = con.prepareStatement("SELECT * FROM trap");

       rs = pstm.executeQuery();
       L1Trap trap = null;
       while (rs.next()) {
         String typeName = rs.getString("type");

         trap = createTrapInstance(typeName, new SqlTrapStorage(rs));


         this._traps.put(Integer.valueOf(trap.getId()), trap);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public static TrapTable getInstance() {
     if (_instance == null) {
       _instance = new TrapTable();
     }
     return _instance;
   }

   public static void reload() {
     TrapTable oldInstance = _instance;
     _instance = new TrapTable();

     oldInstance._traps.clear();
   }

   public L1Trap getTemplate(int id) {
     return this._traps.get(Integer.valueOf(id));
   }

   private class SqlTrapStorage implements TrapStorage {
     private final ResultSet _rs;

     public SqlTrapStorage(ResultSet rs) {
       this._rs = rs;
     }


     public String getString(String name) {
       try {
         return this._rs.getString(name);
       } catch (SQLException sQLException) {

         return "";
       }
     }

     public int getInt(String name) {
       try {
         return this._rs.getInt(name);
       } catch (SQLException sQLException) {


         return 0;
       }
     }

     public boolean getBoolean(String name) {
       try {
         return this._rs.getBoolean(name);
       } catch (SQLException sQLException) {

         return false;
       }
     }
   }
 }

