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

package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.utils.SQLUtil;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_AuctionBoardRead extends ServerBasePacket {

	private static final String S_AUCTIONBOARDREAD = "[S] S_AuctionBoardRead";

	public S_AuctionBoardRead(int objectId, String house_number) {
		buildPacket(objectId, house_number);
	}

	private void buildPacket(int objectId, String house_number) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			int number = Integer.valueOf(house_number);
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_auction WHERE house_id=?");
			pstm.setInt(1, number);
			rs = pstm.executeQuery();
			Calendar cal = null;
			while (rs.next()) {
				writeC(Opcodes.S_HYPERTEXT); // 寫入操作碼
				writeD(objectId); // 寫入物件ID
				writeS("agsel"); // 寫入固定字串 "agsel"
				writeS(house_number); // 寫入房屋編號
				writeH(9); // 寫入以下字符行數量
				writeS(rs.getString(2)); // 寫入房屋名稱
				writeS(rs.getString(6)); // 寫入房屋位置
				writeS(String.valueOf(rs.getString(3))); // 寫入房屋面積
				writeS(rs.getString(7)); // 寫入前所有者
				writeS(rs.getString(9)); // 寫入當前出價者
				writeS(String.valueOf(rs.getInt(5))); // 寫入當前出價價格

				cal = timestampToCalendar((Timestamp) rs.getObject(4));
				int month = cal.get(Calendar.MONTH) + 1; // 取得月份並加1
				int day = cal.get(Calendar.DATE); // 取得日期
				int hour = cal.get(Calendar.HOUR_OF_DAY); // 取得小時數

				writeS(String.valueOf(month)); // 寫入截止月份
				writeS(String.valueOf(day)); // 寫入截止日期
				writeS(String.valueOf(hour)); // 寫入截止時間
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
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
		return S_AUCTIONBOARDREAD;
	}
}


