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

public class S_CharCreateStatus extends ServerBasePacket {
	private static final String S_CHAR_CREATE_STATUS = "[S] S_CharCreateStatus";

	public static final int REASON_OK = 0x02;

	public static final int REASON_ALREADY_EXSISTS = 0x06;

	public static final int REASON_INVALID_NAME = 0x09;

	public static final int REASON_WRONG_AMOUNT = 0x15;

	
	public static final S_CharCreateStatus NAME_FAIL		= new S_CharCreateStatus(S_CharCreateStatus.REASON_INVALID_NAME);
	public static final S_CharCreateStatus ALREADY_EXSISTS	= new S_CharCreateStatus(S_CharCreateStatus.REASON_ALREADY_EXSISTS);
	public static final S_CharCreateStatus AMOUNT_MAX		= new S_CharCreateStatus(S_CharCreateStatus.REASON_WRONG_AMOUNT);
	public static final S_CharCreateStatus OK				= new S_CharCreateStatus(S_CharCreateStatus.REASON_OK);

	public S_CharCreateStatus(int reason) {
		writeC(Opcodes.S_CREATE_CHARACTER_CHECK);
		writeC(reason);
		writeD(0x00000000);
		writeD(0x0000);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_CHAR_CREATE_STATUS;
	}
}


