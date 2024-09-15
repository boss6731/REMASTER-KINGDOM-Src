package l1j.server.server.server.clientpackets;

import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.MJINNSystem.Loader.MJINNHelperLoader;
import l1j.server.server.GameClient;
import l1j.server.server.server.datatables.AuctionBoardTable;
import l1j.server.server.server.datatables.HouseTable;
import l1j.server.server.server.datatables.ItemTable;
import l1j.server.server.server.datatables.NpcActionTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtml;
import l1j.server.server.model.npc.action.L1NpcAction;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.storage.CharactersItemStorage;
import l1j.server.server.templates.L1AuctionBoard;
import l1j.server.server.templates.L1House;

public class C_Amount extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_AMOUNT = "[C] C_Amount";

	public C_Amount(byte[] decrypt, GameClient client) throws Exception {
		super(decrypt);
		if (client == null) return;
		int objectId = readD();
		int amount = readD();

		// 修正了行會據點拍賣公告板的錯誤
		long _amount = amount;
		if(_amount <= 0){ // 新增部分
			return;
		}
		
		@SuppressWarnings("unused")
		int c = readC();
		String s = readS();

		L1PcInstance pc = client.getActiveChar();
		if ( pc == null)return;
		L1NpcInstance npc = (L1NpcInstance) L1World.getInstance().findObject(objectId);	

		if (npc == null) {
			return;
		}
		
		if(MJINNHelperLoader.getInstance().onResult(npc, pc, s, amount))
			return;

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
		if (s1.equalsIgnoreCase("agapply")) { // 當參與拍賣時
			String pcName = pc.getName();
			AuctionBoardTable boardTable = new AuctionBoardTable();
			for (L1AuctionBoard board : boardTable.getAuctionBoardTableList()) {
				if (pcName.equalsIgnoreCase(board.getBidder())) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(523))); // 已經參加了另一個房子的拍賣。
					return;
				}
			}
			int houseId = Integer.valueOf(s2);
			L1AuctionBoard board = boardTable.getAuctionBoardTable(houseId);
			if (board != null) {
				int nowPrice = board.getPrice();
			    long _nowPrice = nowPrice;
				if (_nowPrice <= 0 ){ // 新增部分
					return;
				}
				// 關於拍賣公告板的錯誤修正
				if (_amount < _nowPrice){// 新增部分
			       return; 
			    }
			    
			    if (pc.getInventory().findItemId(L1ItemId.ADENA).getCount() < _amount){ // 新增部分
					return; 
				}

				int nowBidderId = board.getBidderId();
				if (pc.getInventory().consumeItem(L1ItemId.ADENA, amount)) {
					// 更新拍賣公告板
					board.setPrice(amount);
					board.setBidder(pcName);
					board.setBidderId(pc.getId());
					boardTable.updateAuctionBoard(board);
					if (nowBidderId != 0) {
						// 退還金幣給出價者
						L1PcInstance bidPc = (L1PcInstance) L1World.getInstance().findObject(nowBidderId);
						if (bidPc != null) { // 在線時
							bidPc.getInventory().storeItem(L1ItemId.ADENA, nowPrice);
							// 由於已有人提出比您出價更高的金額，很遺憾您未中標。%n
							// 我們將退還您在拍賣中託付的%0金幣。%n 謝謝。%n%n
							bidPc.sendPackets(String.valueOf(new S_ServerMessage(525, String.valueOf(nowPrice))));
						} else { // 離線時
							L1ItemInstance item = ItemTable.getInstance().createItem(L1ItemId.ADENA);
							item.setCount(nowPrice);
							CharactersItemStorage storage = CharactersItemStorage.create();
							storage.storeItem(nowBidderId, item);
						}
					}
				} else {
					pc.sendPackets(String.valueOf(new S_ServerMessage(189))); // 1金幣不足。
				}
			}
		} else if (s1.equalsIgnoreCase("agsell")) { // 當出售物品時
			int houseId = Integer.valueOf(s2);
			AuctionBoardTable boardTable = new AuctionBoardTable();
			L1AuctionBoard board = new L1AuctionBoard();

			L1Clan ownerClan = null;
			for (L1Clan clan : L1World.getInstance().getAllClans()) {
				if (clan.getLeaderId() == pc.getId()) {
					ownerClan = clan;
					break;
				}
			}
			
			if( ownerClan == null )
			{
				return;
			}

			L1House house = HouseTable.getInstance().getHouseTable(houseId);
			if( house == null || ownerClan.getHouseId() != house.getHouseId() )
			{
				return;
			}
			
			if( pc.getInventory().findItemId(L1ItemId.ADENA).getCount() < amount)
			{
				pc.sendPackets(String.valueOf(new S_SystemMessage("無法設置超過持有金額的數量。")));
				return;
			}

			if (board != null) {
				// 拍賣公告板錄入新信息
				board.setHouseId(houseId);
				board.setHouseName(house.getHouseName());
				board.setHouseArea(house.getHouseArea());
				TimeZone tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
				Calendar cal = Calendar.getInstance(tz);
				cal.add(Calendar.DATE, 1); // 拍賣更新在1天后
				cal.set(Calendar.MINUTE, 0); // 剪掉分鐘和秒
				cal.set(Calendar.SECOND, 0);
				board.setDeadline(cal);
				board.setPrice(amount);
				board.setLocation(house.getLocation());
				board.setOldOwner(pc.getName());
				board.setOldOwnerId(pc.getId());
				board.setBidder("");
				board.setBidderId(0);
				boardTable.insertAuctionBoard(board);

				house.setOnSale(true);  // 設置為拍賣中
				house.setPurchaseBasement(true); // 設置為未購買地下密室
				HouseTable.getInstance().updateHouse(house); // 更新至資料庫
			}
		} else {
		    L1NpcAction action = NpcActionTable.getInstance().get(s, pc, npc);

		    if (action != null) {
				L1NpcHtml result = action.executeWithAmount(s, pc, npc, amount);
				if (result != null) {
					pc.sendPackets(new S_NPCTalkReturn(npc.getId(), result));
				}
				return;
			}
		}
	}

	@Override
	public String getType() {
		return C_AMOUNT;
	}
}
