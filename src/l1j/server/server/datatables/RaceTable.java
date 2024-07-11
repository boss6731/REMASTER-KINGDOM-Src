 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1Racer;
 import l1j.server.server.utils.SQLUtil;

 public class RaceTable {
   private static RaceTable _instance;
   private HashMap<Integer, L1Racer> _namelist;

   public static RaceTable getInstance() {
     if (_instance == null) {
       _instance = new RaceTable();
     }
     return _instance;
   }

   public RaceTable() {
     this._namelist = new HashMap<>();
     bnl();
   }

   private void bnl() {
     Connection con = null;
     PreparedStatement statement = null;
     ResultSet namelist = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       statement = con.prepareStatement("SELECT * FROM util_racer");
       namelist = statement.executeQuery();

       BadnameTable(namelist);
       namelist.close();
       statement.close();
       con.close();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(namelist, statement, con);
     }
   }

   private void BadnameTable(ResultSet Data) throws Exception {
     L1Racer name = null;
     while (Data.next()) {
       name = new L1Racer();
       name.setNum(Data.getInt(1));
       name.setWinCount(Data.getInt(2));
       name.setLoseCount(Data.getInt(3));
       this._namelist.put(Integer.valueOf(name.getNum()), name);
     }
   }





   public L1Racer getTemplate(int name) {
     return this._namelist.get(new Integer(name));
   }
 }


