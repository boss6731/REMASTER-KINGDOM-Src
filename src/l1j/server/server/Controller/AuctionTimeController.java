 package l1j.server.server.Controller;

 import java.util.Calendar;
 import java.util.TimeZone;
 import l1j.server.Config;
 import l1j.server.server.datatables.AuctionBoardTable;
 import l1j.server.server.datatables.ClanTable;
 import l1j.server.server.datatables.HouseTable;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.storage.CharactersItemStorage;
 import l1j.server.server.templates.L1AuctionBoard;
 import l1j.server.server.templates.L1House;




 public class AuctionTimeController
   implements Runnable
 {
   public static final int SLEEP_TIME = 60000;
   private static AuctionTimeController _instance;

   public static AuctionTimeController getInstance() {
     if (_instance == null) {
       _instance = new AuctionTimeController();
     }
     return _instance;
   }


   public void run() {
     try {
       checkAuctionDeadline();
     } catch (Exception exception) {}
   }


   public Calendar getRealTime() {
     TimeZone tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
     Calendar cal = Calendar.getInstance(tz);
     return cal;
   }

   private void checkAuctionDeadline() {
     AuctionBoardTable boardTable = new AuctionBoardTable();
     for (L1AuctionBoard board : boardTable.getAuctionBoardTableList()) {
       if (board.getDeadline().before(getRealTime())) {
         endAuction(board);
       }
     }
   }

   private void endAuction(L1AuctionBoard board) {
     int houseId = board.getHouseId();
     int price = board.getPrice();
     int oldOwnerId = board.getOldOwnerId();
     String bidder = board.getBidder();
     int bidderId = board.getBidderId();

     if (oldOwnerId != 0 && bidderId != 0) {

       L1PcInstance oldOwnerPc = (L1PcInstance)L1World.getInstance().findObject(oldOwnerId);
       int payPrice = (int)(price * 0.9D);
       if (oldOwnerPc != null) {
         oldOwnerPc.getInventory().storeItem(40308, payPrice);


         oldOwnerPc.sendPackets((ServerBasePacket)new S_ServerMessage(527, String.valueOf(payPrice)));
       } else {
         L1ItemInstance item = ItemTable.getInstance().createItem(40308);
         item.setCount(payPrice);
         try {
           CharactersItemStorage storage = CharactersItemStorage.create();
           storage.storeItem(oldOwnerId, item);
         } catch (Exception e) {
           e.printStackTrace();
         }
       }

       L1PcInstance bidderPc = (L1PcInstance)L1World.getInstance().findObject(bidderId);
       if (bidderPc != null)
       {

         bidderPc.sendPackets((ServerBasePacket)new S_ServerMessage(524, String.valueOf(price), bidder));
       }
       deleteHouseInfo(houseId);
       setHouseInfo(houseId, bidderId);
       deleteNote(houseId);
     } else if (oldOwnerId == 0 && bidderId != 0) {
       L1PcInstance bidderPc = (L1PcInstance)L1World.getInstance().findObject(bidderId);
       if (bidderPc != null)
       {

         bidderPc.sendPackets((ServerBasePacket)new S_ServerMessage(524, String.valueOf(price), bidder));
       }
       setHouseInfo(houseId, bidderId);
       deleteNote(houseId);
     } else if (oldOwnerId != 0 && bidderId == 0) {

       L1PcInstance oldOwnerPc = (L1PcInstance)L1World.getInstance().findObject(oldOwnerId);
       if (oldOwnerPc != null)
       {

         oldOwnerPc.sendPackets((ServerBasePacket)new S_ServerMessage(528));
       }
       deleteNote(houseId);
     } else if (oldOwnerId == 0 && bidderId == 0) {

       Calendar cal = getRealTime();
       cal.add(5, 1);
       cal.set(12, 0);
       cal.set(13, 0);
       board.setDeadline(cal);
       AuctionBoardTable boardTable = new AuctionBoardTable();
       boardTable.updateAuctionBoard(board);
     }
   }








   private void deleteHouseInfo(int houseId) {
     for (L1Clan clan : L1World.getInstance().getAllClans()) {
       if (clan.getHouseId() == houseId) {
         clan.setHouseId(0);
         ClanTable.getInstance().updateClan(clan);
       }
     }
   }









   private void setHouseInfo(int houseId, int bidderId) {
     for (L1Clan clan : L1World.getInstance().getAllClans()) {
       if (clan.getLeaderId() == bidderId) {
         clan.setHouseId(houseId);
         ClanTable.getInstance().updateClan(clan);
         break;
       }
     }
   }









   private void deleteNote(int houseId) {
     L1House house = HouseTable.getInstance().getHouseTable(houseId);
     house.setOnSale(false);
     Calendar cal = getRealTime();
     cal.add(5, Config.ServerAdSetting.HOUSETAXINTERVAL);
     cal.set(12, 0);
     cal.set(13, 0);
     house.setTaxDeadline(cal);
     HouseTable.getInstance().updateHouse(house);


     AuctionBoardTable boardTable = new AuctionBoardTable();
     boardTable.deleteAuctionBoard(houseId);
   }
 }


