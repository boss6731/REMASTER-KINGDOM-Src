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

package l1j.server.server.server.clientpackets;


import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.utils.FaceToFace;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Propose extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_PROPOSE = "[C] C_Propose";

	public C_Propose(byte abyte0[], GameClient clientthread) {
		super(abyte0);
		int c = readC();

		L1PcInstance pc = clientthread.getActiveChar();
		if (c == 0) { // /propose(/求婚)
			if (pc == null || pc.isGhost()) {
				return;
			}
			L1PcInstance target = FaceToFace.faceToFace(pc);
			if (target != null) {
				if (pc.getPartnerId() != 0) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(657))); // 1您已經結婚了。
					return;
				}
				if (target.getPartnerId() != 0) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(658))); // 1對方已經結婚了。
					return;
				}
				if (pc.get_sex() == target.get_sex()) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(661))); // 1結婚對象必須是異性。
					return;
				}
				if (pc.getX() >= 33974 && pc.getX() <= 33976
						&& pc.getY() >= 33362 && pc.getY() <= 33365
						&& pc.getMapId() == 4 && target.getX() >= 33974
						&& target.getX() <= 33976 && target.getY() >= 33362
						&& target.getY() <= 33365 && target.getMapId() == 4) {
					target.setTempID(pc.getId()); // 保存對方的對象 ID
					target.sendPackets(String.valueOf(new S_Message_YN(654, pc.getName()))); // %0 想要與您結婚。您願意與 %0 結婚嗎？ (Y/N)
				}
			}
		} else if (c == 1) { // /divorce(/離婚)
			if (pc.getPartnerId() == 0) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(662))); // 1您還未結婚。
				return;
			}
			pc.sendPackets(String.valueOf(new S_Message_YN(653, ""))); // 如果離婚，戒指將會消失。您確定要離婚嗎？ (Y/N)
		}
	}

	@Override
	public String getType() {
		return C_PROPOSE;
	}
}
