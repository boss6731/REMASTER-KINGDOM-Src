 package l1j.server.server.Controller;

 import java.util.Calendar;
 import java.util.TimeZone;
 import l1j.server.Config;
 import l1j.server.server.datatables.AuctionBoardTable;
 import l1j.server.server.datatables.ClanTable;
 import l1j.server.server.datatables.HouseTable;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.templates.L1AuctionBoard;
 import l1j.server.server.templates.L1House;




 public class HouseTaxTimeController
   implements Runnable
 {
   public static final int SLEEP_TIME = 600000;
   private static HouseTaxTimeController _instance;

   public static HouseTaxTimeController getInstance() {
     if (_instance == null) {
       _instance = new HouseTaxTimeController();
     }
     return _instance;
   }


   public void run() {
     try {
       checkTaxDeadline();
     } catch (Exception exception) {}
   }


   public Calendar getRealTime() {
     TimeZone tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
     Calendar cal = Calendar.getInstance(tz);
     return cal;
   }

   private void checkTaxDeadline() {
     for (L1House house : HouseTable.getInstance().getHouseTableList()) {
       if (!house.isOnSale() &&
         house.getTaxDeadline().before(getRealTime())) {
         sellHouse(house);
       }
     }
   }


   private void sellHouse(L1House house) {
     AuctionBoardTable boardTable = new AuctionBoardTable();
     L1AuctionBoard board = new L1AuctionBoard();
     if (board != null) {

       int houseId = house.getHouseId();
       board.setHouseId(houseId);
       board.setHouseName(house.getHouseName());
       board.setHouseArea(house.getHouseArea());
       TimeZone tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
       Calendar cal = Calendar.getInstance(tz);
       cal.add(5, 1);
       cal.set(12, 0);
       cal.set(13, 0);
       board.setDeadline(cal);
       board.setPrice(100000);
       board.setLocation(house.getLocation());
       board.setOldOwner("");
       board.setOldOwnerId(0);
       board.setBidder("");
       board.setBidderId(0);
       boardTable.insertAuctionBoard(board);
       house.setOnSale(true);
       house.setPurchaseBasement(true);
       cal.add(5, Config.ServerAdSetting.HOUSETAXINTERVAL);
       house.setTaxDeadline(cal);
       HouseTable.getInstance().updateHouse(house);

       for (L1Clan clan : L1World.getInstance().getAllClans()) {
         if (clan.getHouseId() == houseId) {
           clan.setHouseId(0);
           ClanTable.getInstance().updateClan(clan);
         }
       }
     }
   }
 }


