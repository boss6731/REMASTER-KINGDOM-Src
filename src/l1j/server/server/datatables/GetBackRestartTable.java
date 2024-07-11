 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1GetBackRestart;
 import l1j.server.server.utils.SQLUtil;



 public class GetBackRestartTable
 {
   private static GetBackRestartTable _instance;
   private final HashMap<Integer, L1GetBackRestart> _getbackrestart = new HashMap<>();

   public static GetBackRestartTable getInstance() {
     if (_instance == null) {
       _instance = new GetBackRestartTable();
     }
     return _instance;
   }


   public void reload() {
     GetBackRestartTable oldInstance = _instance;
     _instance = new GetBackRestartTable();
     if (oldInstance != null) {
       oldInstance = null;
     }
   }

   public GetBackRestartTable() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM getback_restart");
       rs = pstm.executeQuery();
       L1GetBackRestart gbr = null;
       while (rs.next()) {
         gbr = new L1GetBackRestart();
         int area = rs.getInt("area");
         gbr.setArea(area);
         gbr.setLocX(rs.getInt("locx"));
         gbr.setLocY(rs.getInt("locy"));
         gbr.setMapId(rs.getShort("mapid"));

         this._getbackrestart.put(new Integer(area), gbr);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public L1GetBackRestart[] getGetBackRestartTableList() {
     return (L1GetBackRestart[])this._getbackrestart.values().toArray((Object[])new L1GetBackRestart[this._getbackrestart.size()]);
   }
 }


