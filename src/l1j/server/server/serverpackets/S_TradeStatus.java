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

// 參考自 l1j.server.server.serverpackets:
// ServerBasePacket

public class S_TradeStatus extends ServerBasePacket {
	public S_TradeStatus(int type) {
		writeC(Opcodes.S_XCHG_RESULT);
		writeC(type); // 0:交易完成 1:交易取消
	}

	@override
	public byte[] getContent() {
		return getBytes();
	}

	@override
	public String getType() {
		return _S__2A_TRADESTATUS;
	}

	private static final String _S__2A_TRADESTATUS = "[S] S_TradeStatus";
}
