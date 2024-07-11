 package l1j.server.server.clientpackets;

 import java.util.Calendar;
 import java.util.NoSuchElementException;
 import java.util.StringTokenizer;
 import java.util.TimeZone;
 import l1j.server.Config;
 import l1j.server.MJINNSystem.Loader.MJINNHelperLoader;
 import l1j.server.server.GameClient;
 import l1j.server.server.datatables.AuctionBoardTable;
 import l1j.server.server.datatables.HouseTable;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.datatables.NpcActionTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.npc.L1NpcHtml;
 import l1j.server.server.model.npc.action.L1NpcAction;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.storage.CharactersItemStorage;
 import l1j.server.server.templates.L1AuctionBoard;
 import l1j.server.server.templates.L1House;

 public class C_Amount
   extends ClientBasePacket {
   private static final String C_AMOUNT = "[C] C_Amount";

   public C_Amount(byte[] decrypt, GameClient client) throws Exception {
     super(decrypt);
     if (client == null)
       return;  int objectId = readD();
     int amount = readD();


     long _amount = amount;
     if (_amount <= 0L) {
       return;
     }


     int c = readC();
     String s = readS();

     L1PcInstance pc = client.getActiveChar();
     if (pc == null)
       return;  L1NpcInstance npc = (L1NpcInstance)L1World.getInstance().findObject(objectId);

     if (npc == null) {
       return;
     }

     if (MJINNHelperLoader.getInstance().onResult(npc, pc, s, amount)) {
       return;
     }
     String s1 = "";
     String s2 = "";
     try {
       StringTokenizer stringtokenizer = new StringTokenizer(s);
       s1 = stringtokenizer.nextToken();
       s2 = stringtokenizer.nextToken();
     } catch (NoSuchElementException e) {
       s1 = "";
       s2 = "";
     }
     if (s1.equalsIgnoreCase("agapply")) {
       String pcName = pc.getName();
       AuctionBoardTable boardTable = new AuctionBoardTable();
       for (L1AuctionBoard l1AuctionBoard : boardTable.getAuctionBoardTableList()) {
         if (pcName.equalsIgnoreCase(l1AuctionBoard.getBidder())) {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(523));
           return;
         }
       }
       int houseId = Integer.valueOf(s2).intValue();
       L1AuctionBoard board = boardTable.getAuctionBoardTable(houseId);
       if (board != null) {
         int nowPrice = board.getPrice();
         long _nowPrice = nowPrice;
         if (_nowPrice <= 0L) {
           return;
         }

         if (_amount < _nowPrice) {
           return;
         }

         if (pc.getInventory().findItemId(40308).getCount() < _amount) {
           return;
         }

         int nowBidderId = board.getBidderId();
         if (pc.getInventory().consumeItem(40308, amount)) {

           board.setPrice(amount);
           board.setBidder(pcName);
           board.setBidderId(pc.getId());
           boardTable.updateAuctionBoard(board);
           if (nowBidderId != 0) {

             L1PcInstance bidPc = (L1PcInstance)L1World.getInstance().findObject(nowBidderId);
             if (bidPc != null) {
               bidPc.getInventory().storeItem(40308, nowPrice);


               bidPc.sendPackets((ServerBasePacket)new S_ServerMessage(525, String.valueOf(nowPrice)));
             } else {
               L1ItemInstance item = ItemTable.getInstance().createItem(40308);
               item.setCount(nowPrice);
               CharactersItemStorage storage = CharactersItemStorage.create();
               storage.storeItem(nowBidderId, item);
             }
           }
         } else {
           pc.sendPackets((ServerBasePacket)new S_ServerMessage(189));
         }
       }
     } else if (s1.equalsIgnoreCase("agsell")) {
       int houseId = Integer.valueOf(s2).intValue();
       AuctionBoardTable boardTable = new AuctionBoardTable();
       L1AuctionBoard board = new L1AuctionBoard();

       L1Clan ownerClan = null;
       for (L1Clan clan : L1World.getInstance().getAllClans()) {
         if (clan.getLeaderId() == pc.getId()) {
           ownerClan = clan;

           break;
         }
       }
       if (ownerClan == null) {
         return;
       }


         L1House house = HouseTable.getInstance().getHouseTable(houseId); // 註解: 獲取房屋信息
         if (house == null || ownerClan.getHouseId() != house.getHouseId()) { // 註解: 如果房屋不存在或擁有者的房屋ID與獲取到的房屋ID不匹配
             return; // 註解: 返回，停止後續執行
         }

         if (pc.getInventory().findItemId(40308).getCount() < amount) { // 註解: 如果玩家背包中編號40308的物品數量少於設定的金額
             pc.sendPackets((ServerBasePacket)new S_SystemMessage("無法設定超過持有金額的數量。")); // 註解: 發送系統消息通知玩家無法設定超過持有金額的數量
             return; // 註解: 返回，停止後續執行
         }
       if (board != null) {

         board.setHouseId(houseId);
         board.setHouseName(house.getHouseName());
         board.setHouseArea(house.getHouseArea());
         TimeZone tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
         Calendar cal = Calendar.getInstance(tz);
         cal.add(5, 1);
         cal.set(12, 0);
         cal.set(13, 0);
         board.setDeadline(cal);
         board.setPrice(amount);
         board.setLocation(house.getLocation());
         board.setOldOwner(pc.getName());
         board.setOldOwnerId(pc.getId());
         board.setBidder("");
         board.setBidderId(0);
         boardTable.insertAuctionBoard(board);

         house.setOnSale(true);
         house.setPurchaseBasement(true);
         HouseTable.getInstance().updateHouse(house);
       }
     } else {
       L1NpcAction action = NpcActionTable.getInstance().get(s, pc, (L1Object)npc);

       if (action != null) {
         L1NpcHtml result = action.executeWithAmount(s, pc, (L1Object)npc, amount);
         if (result != null) {
           pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(npc.getId(), result));
         }
         return;
       }
     }
   }


   public String getType() {
     return "[C] C_Amount";
   }
 }


