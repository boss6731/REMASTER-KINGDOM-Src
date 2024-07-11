 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.util.Map;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.templates.L1Town;
 import l1j.server.server.utils.SQLUtil;




 public class TownTable
 {
   private static TownTable _instance;
   private final Map<Integer, L1Town> _towns = new ConcurrentHashMap<>();

   public static TownTable getInstance() {
     if (_instance == null) {
       _instance = new TownTable();
     }

     return _instance;
   }

   private TownTable() {
     load();
   }

   public void load() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     this._towns.clear();

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM town");


       rs = pstm.executeQuery();
       L1Town town = null;
       while (rs.next()) {
         town = new L1Town();
         int townid = rs.getInt("town_id");
         town.set_townid(townid);
         town.set_name(rs.getString("name"));
         town.set_leader_id(rs.getInt("leader_id"));
         town.set_leader_name(rs.getString("leader_name"));
         town.set_tax_rate(rs.getInt("tax_rate"));
         town.set_tax_rate_reserved(rs.getInt("tax_rate_reserved"));
         town.set_sales_money(rs.getInt("sales_money"));
         town.set_sales_money_yesterday(rs
             .getInt("sales_money_yesterday"));
         town.set_town_tax(rs.getInt("town_tax"));
         town.set_town_fix_tax(rs.getInt("town_fix_tax"));

         this._towns.put(new Integer(townid), town);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public L1Town[] getTownTableList() {
     return (L1Town[])this._towns.values().toArray((Object[])new L1Town[this._towns.size()]);
   }

   public L1Town getTownTable(int id) {
     return this._towns.get(Integer.valueOf(id));
   }

   public boolean isLeader(L1PcInstance pc, int town_id) {
     L1Town town = getTownTable(town_id);
     return (town.get_leader_id() == pc.getId());
   }




   public void updateTaxRate() {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("UPDATE town SET tax_rate = tax_rate_reserved");
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public void updateSalesMoneyYesterday() {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("UPDATE town SET sales_money_yesterday = sales_money, sales_money = 0");
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }
 }


