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

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;




public class S_BookMarkLoad extends ServerBasePacket {
	private static final String S_BookMarkLoad = "[S] S_BookmarkLoad";

	private static Logger _log = Logger.getLogger(S_BookMarkLoad.class.getName());

	public S_BookMarkLoad(L1PcInstance pc) {
		try {
			int size = pc._bookmarks.size(); // 普通書籤的數量
			int fastsize = pc._speedbookmarks.size(); // 快捷書籤的數量
			int booksize = pc.getMark_count() + 6; // 總書籤數量加上補足的數量
			int tempsize = booksize - 1 - size - fastsize; // 填充的空白書籤數量

			// 寫入封包內容
			writeC(Opcodes.S_VOICE_CHAT);
			writeC(42); // 魔數（Magic Number）
			writeC(booksize); // 寫入總書籤數量
			writeC(0x00); // 魔數
			writeC(0x02); // 魔數

			// 寫入普通書籤的索引
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					writeC(i);
				}
			}

			// 寫入快捷書籤的索引
			if (fastsize > 0) {
				for (int i = 0; i < fastsize; i++) {
					writeC(pc._speedbookmarks.get(i).getNumId());
				}
			}

			// 填充空白書籤
			if (tempsize > 0) {
				for (int i = 0; i < tempsize; i++) {
					writeC(0xff);
				}
			}

			// 寫入書籤詳細信息
			writeH(pc.getMark_count()); // 總書籤數量
			writeH(size); // 普通書籤數量
			for (int i = 0; i < size; i++) {
				writeD(pc._bookmarks.get(i).getNumId()); // 書籤的編號
				writeS(pc._bookmarks.get(i).getName()); // 書籤的名稱
				writeH(pc._bookmarks.get(i).getMapId()); // 書籤的地圖ID
				writeH(pc._bookmarks.get(i).getLocX()); // 書籤的X座標
				writeH(pc._bookmarks.get(i).getLocY()); // 書籤的Y座標
			}
		} catch (Exception e) {
			_log.log(Level.WARNING, "S_BookMarkLoad 發生例外。", e);
		} finally {
		}
	}




	@Override

	public byte[] getContent() {
		return getBytes();
	}



	public String getType() {
		return S_BookMarkLoad;

	}

}