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

import java.io.IOException;

import l1j.server.server.Opcodes;

public class S_HowManyMake extends ServerBasePacket {


	public S_HowManyMake(int objId, int max, String htmlId) {
		writeC(Opcodes.S_HYPERTEXT_INPUT); // 寫入操作碼，假設為超文本輸入
		writeD(objId); // 寫入對象 ID
		writeD(0); // 寫入 0（意圖不明）
		writeD(0); // 寫入旋轉控制的初始價格
		writeD(0); // 寫入價格的下限
		writeD(max); // 寫入價格的上限
		writeH(0); // 寫入 0（意圖不明）
		writeS("request"); // 寫入字符串 "request"
		writeS(htmlId); // 寫入 HTML ID
	}
}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}


