 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.HashMap;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1BugFight;

 public class BugFightTable {
   private static BugFightTable _instance;
   private HashMap<Integer, L1BugFight> _namelist;

   public static BugFightTable getInstance() {
     if (_instance == null) {
       _instance = new BugFightTable();
     }
     return _instance;
   }

   public BugFightTable() {
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
       try {
         if (namelist != null)
           namelist.close();
       } catch (Exception exception) {}


       try {
         if (statement != null)
           statement.close();
       } catch (Exception exception) {}


       try {
         if (con != null)
           con.close();
       } catch (Exception exception) {}


       namelist = null;
       statement = null;
       con = null;
     }
   }

   private void BadnameTable(ResultSet Data) throws Exception {
     L1BugFight name = null;

     while (Data.next()) {
       name = new L1BugFight();
       name.setNum(Data.getInt(1));
       name.setWinCount(Data.getInt(2));
       name.setLoseCount(Data.getInt(3));
       this._namelist.put(Integer.valueOf(name.getNum()), name);
     }
   }






   public L1BugFight getTemplate(int name) {
     return this._namelist.get(new Integer(name));
   }
 }


