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

import l1j.server.server.Opcodes;

// 引用自 l1j.server.server.serverpackets 包的類
// ServerBasePacket

public class S_SkillIconShield extends ServerBasePacket {

	// 構造函數，用於設置盾牌圖標效果
	public S_SkillIconShield(int type, int time) {
		writeC(Opcodes.S_MAGE_SHIELD);  // 寫入操作碼，表示法師盾牌效果
		writeH(time);                   // 寫入盾牌效果的持續時間
		writeC(type);                   // 寫入盾牌效果的類型
		writeD(0);                      // 寫入額外數據，這裡為0，可能表示無額外信息
	}

	// 覆蓋父類的 getContent 方法，獲取封包內容的字節數組
	@override
	public byte[] getContent() {
		return getBytes();  // 獲取封包內容的字節數組
	}
}


