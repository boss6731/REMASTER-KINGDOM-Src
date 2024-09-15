/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.server.Controller;

import java.util.Calendar;
import java.util.TimeZone;

import l1j.server.Config;

import l1j.server.server.Controller.EventThread;
import l1j.server.server.datatables.ClanTable;

import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.server.datatables.AuctionBoardTable;
import l1j.server.server.server.datatables.ItemTable;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.storage.CharactersItemStorage;
import l1j.server.server.templates.L1AuctionBoard;
import l1j.server.server.templates.L1House;

public class AuctionTimeController implements Runnable {
	public static final int SLEEP_TIME = 60000;

	private static AuctionTimeController _instance;

	public static AuctionTimeController getInstance() {
		if (_instance == null) {
			_instance = new AuctionTimeController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			checkAuctionDeadline();
		} catch (Exception e1) {
		}
		return null;
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

		if (oldOwnerId != 0 && bidderId != 0) { // 有前任擁有者且有中標者
			L1PcInstance oldOwnerPc = (L1PcInstance) L1World.getInstance().findObject(oldOwnerId);
			int payPrice = (int) (price * 0.9);
			if (oldOwnerPc != null) { // 前任擁有者在線上
				oldOwnerPc.getInventory().storeItem(L1ItemId.ADENA, payPrice);
				// 您擁有的房屋已以最終價格的 %1 金幣拍賣成交。%n
				// 扣除10%%的手續費後，剩餘金額 %0 金幣已發放給您。%n 感謝您的使用。%n%n
				oldOwnerPc.sendPackets(String.valueOf(new S_ServerMessage(527, String.valueOf(payPrice))));
			} else { // 前任擁有者不在線上
				L1ItemInstance item = ItemTable.getInstance().createItem(L1ItemId.ADENA);
				item.setCount(payPrice);
				try {
					CharactersItemStorage storage = CharactersItemStorage.create();
					storage.storeItem(oldOwnerId, item);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}


			L1PcInstance bidderPc = (L1PcInstance) L1World.getInstance().findObject(bidderId);
			if (bidderPc != null) { // 中標者在線上
				// 恭喜您。%n您參加的拍賣以最終價格 %0 金幣中標。%n
				// 您購買的房屋可以立即使用。%n 感謝您的使用。%n%n
				bidderPc.sendPackets(String.valueOf(new S_ServerMessage(524, String.valueOf(price), bidder)));
			}
			deleteHouseInfo(houseId);
			setHouseInfo(houseId, bidderId);
			deleteNote(houseId);
		} else if (oldOwnerId == 0 && bidderId != 0) { // 沒有前任擁有者且有中標者
			L1PcInstance bidderPc = (L1PcInstance) L1World.getInstance().findObject(bidderId);
			if (bidderPc != null) { // 中標者在線上
				// 恭喜您。%n您參加的拍賣以最終價格 %0 金幣中標。%n
				// 您購買的房屋可以立即使用。%n 感謝您的使用。%n%n
				bidderPc.sendPackets(String.valueOf(new S_ServerMessage(524, String.valueOf(price), bidder)));
			}
			setHouseInfo(houseId, bidderId);
			deleteNote(houseId);
		} else if (oldOwnerId != 0 && bidderId == 0) { // 有前任擁有者但無中標者
			L1PcInstance oldOwnerPc = (L1PcInstance) L1World.getInstance().findObject(oldOwnerId);
			if (oldOwnerPc != null) { // 前任擁有者在線上
				// 由於在拍賣期間內，沒有出現願意支付您所設定金額以上的出價，拍賣已被取消。%n
				// 因此，所有權已歸還給您。%n 感謝您的使用。%n%n
				oldOwnerPc.sendPackets(String.valueOf(new S_ServerMessage(528)));
			}
			deleteNote(houseId);
		} else if (oldOwnerId == 0 && bidderId == 0) { // 無前任擁有者且無中標者
			// 設定在5天後重新拍賣
			Calendar cal = getRealTime();
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			board.setDeadline(cal);
			AuctionBoardTable boardTable = new AuctionBoardTable();
			boardTable.updateAuctionBoard(board);
		}
	}

	/**
	 * 刪除前任擁有者的宅邸
	 *
	 * @param houseId
	 * @return
	 */
	private void deleteHouseInfo(int houseId) {
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan.getHouseId() == houseId) {
				clan.setHouseId(0);
				ClanTable.getInstance().updateClan(clan);
			}
		}
	}

	/**
	 * 設定中標者的宅邸
	 *
	 * @param houseId
	 * @param bidderId
	 * @return
	 */
	private void setHouseInfo(int houseId, int bidderId) {
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan.getLeaderId() == bidderId) {
				clan.setHouseId(houseId);
				ClanTable.getInstance().updateClan(clan);
				break;
			}
		}
	}

	/**
	 * 將宅邸的拍賣狀態設為OFF，並從拍賣公告板上刪除
	 *
	 * @param houseId
	 * @return
	 */
	private void deleteNote(int houseId) {
		// 將宅邸的拍賣狀態設為OFF
		EventThread HouseTable;
		L1House house = HouseTable.getInstance().getHouseTable(houseId);
		house.setOnSale(false);
		Calendar cal = getRealTime();
		cal.add(Calendar.DATE, Config.ServerAdSetting.HOUSETAXINTERVAL);
		cal.set(Calendar.MINUTE, 0); // 刪除分和秒
		cal.set(Calendar.SECOND, 0);
		house.setTaxDeadline(cal);
		HouseTable.getInstance().updateHouse(house);

		// 從拍賣公告板上刪除
		AuctionBoardTable boardTable = new AuctionBoardTable();
		boardTable.deleteAuctionBoard(houseId);
	}
}