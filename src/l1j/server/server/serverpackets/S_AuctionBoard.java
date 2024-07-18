package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.utils.SQLUtil;

public class S_AuctionBoard extends ServerBasePacket {


	private static final String S_AUCTIONBOARD = "[S] S_AuctionBoard";

	public S_AuctionBoard(L1NpcInstance board) {
		buildPacket(board);
	}

	private void buildPacket(L1NpcInstance board) {
		ArrayList<Integer> houseList = new ArrayList<Integer>();
		int houseId = 0;
		int count = 0;
		int[] id = null;
		String[] name = null;
		int[] area = null;
		int[] month = null;
		int[] day = null;
		int[] hour = null;
		int[] price = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_auction");
			rs = pstm.executeQuery();
			while (rs.next()) {
				houseId = rs.getInt(1);
				if (board.getX() == 33424 && board.getY() == 32824) { // 拍賣公告板（奇岩）
					if (houseId >= 262145 && houseId <= 262189) {
						houseList.add(houseId);
						count++;
					}
				} else if (board.getX() == 33585 && board.getY() == 33235) { // 拍賣公告板（海音）
					if (houseId >= 327681 && houseId <= 327691) {
						houseList.add(houseId);
						count++;
					}
				} else if (board.getX() == 33959 && board.getY() == 33253) { // 拍賣公告板（亞丁）
					if (houseId >= 458753 && houseId <= 458819) {
						houseList.add(houseId);
						count++;
					}
				} else if (board.getX() == 32612 && board.getY() == 32775) { // 拍賣公告板（古魯丁）
					if (houseId >= 65537 && houseId <= 65542) {
						houseList.add(houseId);
						count++;
					}

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		// 配置封包
		writeC(Opcodes.S_AGIT_LIST);
		writeD(board.getId());

		writeH(count); // 寫入記錄數量

		// 初始化數組以存儲房屋詳細信息
		id = new int[count];
		name = new String[count];
		area = new int[count];
		month = new int[count];
		day = new int[count];
		hour = new int[count];
		price = new int[count];
		// 擷取房屋詳細信息並寫入封包
		for (int i = 0; i < count; ++i) {
			// 假設這裡有代碼從 houseList 中擷取詳細信息並填充到各數組
			// 例如：
			// id[i] = 取得 houseList 中的 houseId
			// name[i] = 取得對應的房屋名稱
			// area[i] = 取得對應的房屋面積
			// month[i] = 取得對應的結束月份
			// day[i] = 取得對應的結束日期
			// hour[i] = 取得對應的結束時間
			// price[i] = 取得對應的當前價格

			writeD(id[i]); // 寫入房屋號碼
			writeS(name[i]); // 寫入房屋名稱
			writeH(area[i]); // 寫入房屋面積
			writeC(month[i]); // 寫入截止月份
			writeC(day[i]); // 寫入截止日期
			writeH(hour[i]); // 寫入截止時間
			writeD(price[i]); // 寫入當前投標價格
		}
	}

	private Calendar timestampToCalendar(Timestamp ts) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts.getTime());
		return cal;
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_AUCTIONBOARD;
	}
}


