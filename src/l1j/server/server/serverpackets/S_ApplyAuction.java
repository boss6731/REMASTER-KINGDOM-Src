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
/*
 * 本程式是自由軟體；您可以依據自由軟體基金會所發佈的GNU通用公共許可證條款來重新發佈或修改此程式；
 * 您可以選擇第2版許可證或（依您的選擇）任何後續版本。
 *
 * 本程式的發布目的是希望它能夠對您有所幫助，
 * 但不提供任何擔保；甚至不含默示擔保，
 * 如適售性或適合於特定用途。詳情請參閱GNU通用公共許可證。
 *
 * 您應該已經收到一份與本程式一同發行的GNU通用公共許可證副本；如果沒有，請寫信給
 * 自由軟體基金會，地址是：59 Temple Place - Suite 330, Boston, MA 02111-1307, USA。
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.utils.SQLUtil;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_ApplyAuction extends l1j.server.server.serverpackets.ServerBasePacket {

	private static final String S_APPLYAUCTION = "[S] S_ApplyAuction";

	public S_ApplyAuction(int objectId, String houseNumber) {
		buildPacket(objectId, houseNumber);
	}

	private void buildPacket(int objectId, String houseNumber) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM board_auction WHERE house_id=?");
			int number = Integer.valueOf(houseNumber);
			pstm.setInt(1, number);
			rs = pstm.executeQuery();
			while (rs.next()) {
				int nowPrice = rs.getInt(5);
				int bidderId = rs.getInt(10);
				writeC(Opcodes.S_HYPERTEXT_INPUT);
				writeD(objectId);
				writeD(0); // ?
				if (bidderId == 0) { // 無競標者
					writeD(nowPrice); // 輸入目前價格為旋轉控制的初始價格
					writeD(nowPrice); // 輸入價格下限
				} else { // 有競標者
					writeD(nowPrice + 1); // 輸入目前價格加1為旋轉控制的初始價格
					writeD(nowPrice + 1); // 輸入價格下限
				}
				writeD(2000000000); // 輸入價格上限
				writeH(0); // ?
				writeS("agapply");
				writeS("agapply " + houseNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	
	@Override
	public String getType() {
		return S_APPLYAUCTION;
	}
}
