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
import l1j.server.server.model.Instance.L1FollowerInstance;
import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_WorldPutObject.get

public class S_FollowerPack extends ServerBasePacket {

	private static final String S_FOLLOWER_PACK = "[S] S_FollowerPack";
	
	private static final int STATUS_POISON = 1;

	public S_FollowerPack(L1FollowerInstance follower, L1PcInstance pc) {
		writeC(Opcodes.S_PUT_OBJECT);
		writeH(follower.getX());
		writeH(follower.getY());
		writeD(follower.getId());
		writeH(follower.getCurrentSpriteId());
		writeC(follower.getStatus());
		writeC(follower.getHeading());
		writeC(follower.getLight().getChaLightSize());
		writeC(follower.getMoveSpeed());
		writeD(0);
		writeH(0);
		writeS(follower.getNameId());
		writeS(follower.getTitle());
		int status = 0;
		if (follower.getPoison() != null) {
			if (follower.getPoison().getEffectId() == 1) {
				status |= STATUS_POISON;
			}
		}
		writeC(status);
		writeD(0);
		writeS(null);
		writeS(null);
		writeC(0);
		writeC(0xFF);
		writeC(0);
		writeC(follower.getLevel());
		writeC(0);
		writeC(0xFF);
		writeC(0xFF);
		writeC(0);
		writeC(0);
		writeC(0xFF);
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_FOLLOWER_PACK;
	}

}
