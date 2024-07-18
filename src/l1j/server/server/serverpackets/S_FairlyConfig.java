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
import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_FairlyConfig extends ServerBasePacket {


	private static final String S_CHARACTER_CONFIG = "[S] S_CharacterConfig";

	public S_FairlyConfig(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		byte[] data = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int ok = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection(); // 獲取資料庫連接
			pstm = con.prepareStatement("SELECT * FROM character_Fairly_Config WHERE object_id=?"); // 準備 SQL 查詢語句
			pstm.setInt(1, pc.getId()); // 設置查詢條件中的 object_id
			rs = pstm.executeQuery(); // 執行查詢
			while (rs.next()) {
				data = rs.getBytes(2); // 從結果集中獲取字節數組
				ok = 1; // 設置 ok 為 1，表示有數據
			}
		} catch (Exception e) {
			e.printStackTrace(); // 捕捉並打印異常
		} finally {
			SQLUtil.close(rs); // 關閉結果集
			SQLUtil.close(pstm); // 關閉 PreparedStatement
			SQLUtil.close(con); // 關閉連接
		}
		writeC(Opcodes.S_EVENT); // 寫入操作碼
		writeC(0xBC); // 寫入固定數據 0xBC
		if (ok != 0) { // 如果有數據
			writeByte(data); // 寫入數據
			// System.out.println("妖精信息存在。"); // 註釋掉的中文輸出
			pc.setFairyInfo(data); // 設置 pc 對象的妖精信息屬性
		} else { // 如果沒有數據
			for (int i = 0; i < 512; i++) {
				writeC(0); // 寫入 512 個 0
			}
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_CHARACTER_CONFIG;
	}
}


