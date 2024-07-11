 package l1j.server.server.datatables;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.Timestamp;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Calendar;
 import java.util.List;
 import java.util.Map;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.templates.L1House;
 import l1j.server.server.utils.SQLUtil;


 public class HouseTable
 {
   private static HouseTable _instance;
   private final Map<Integer, L1House> _house = new ConcurrentHashMap<>();

   public static HouseTable getInstance() {
     if (_instance == null) {
       _instance = new HouseTable();
     }
     return _instance;
   }

   public static void reload() {
     HouseTable oldInstance = _instance;
     _instance = new HouseTable();
     oldInstance._house.clear();
   }

   private Calendar timestampToCalendar(Timestamp ts) {
     Calendar cal = Calendar.getInstance();
     cal.setTimeInMillis(ts.getTime());
     return cal;
   }

   public HouseTable() {
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT * FROM house ORDER BY house_id");
       rs = pstm.executeQuery();
       L1House house = null;
       while (rs.next()) {
         house = new L1House();
         house.setHouseId(rs.getInt(1));
         house.setHouseName(rs.getString(2));
         house.setHouseArea(rs.getInt(3));
         house.setLocation(rs.getString(4));
         house.setKeeperId(rs.getInt(5));
         house.setOnSale((rs.getInt(6) == 1));
         house.setPurchaseBasement((rs.getInt(7) == 1));
         house.setTaxDeadline(timestampToCalendar((Timestamp)rs.getObject(8)));
         this._house.put(Integer.valueOf(house.getHouseId()), house);
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public L1House[] getHouseTableList() {
     return (L1House[])this._house.values().toArray((Object[])new L1House[this._house.size()]);
   }

   public L1House getHouseTable(int houseId) {
     return this._house.get(Integer.valueOf(houseId));
   }

   public void updateHouse(L1House house) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("UPDATE house SET house_name=?, house_area=?, location=?, keeper_id=?, is_on_sale=?, is_purchase_basement=?, tax_deadline=? WHERE house_id=?");

       pstm.setString(1, house.getHouseName());
       pstm.setInt(2, house.getHouseArea());
       pstm.setString(3, house.getLocation());
       pstm.setInt(4, house.getKeeperId());
       pstm.setInt(5, (house.isOnSale() == true) ? 1 : 0);
       pstm.setInt(6, (house.isPurchaseBasement() == true) ? 1 : 0);
       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       String fm = formatter.format(house.getTaxDeadline().getTime());
       pstm.setString(7, fm);
       pstm.setInt(8, house.getHouseId());
       pstm.execute();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }
   }

   public static List<Integer> getHouseIdList() {
     List<Integer> houseIdList = new ArrayList<>();

     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       pstm = con.prepareStatement("SELECT house_id FROM house ORDER BY house_id");
       rs = pstm.executeQuery();
       while (rs.next()) {
         int houseId = rs.getInt("house_id");
         houseIdList.add(Integer.valueOf(houseId));
       }
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs);
       SQLUtil.close(pstm);
       SQLUtil.close(con);
     }

     return houseIdList;
   }
 }


