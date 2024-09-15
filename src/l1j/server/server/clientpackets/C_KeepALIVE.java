/* This program is free software; you can redistribute it and/or modify
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
package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.favor.L1FavorBookInventory;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.serverpackets.S_GameTime;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_KeepALIVE extends ClientBasePacket {

	private static final String C_KEEP_ALIVE = "[C] C_KeepALIVE";
	private GameClient client;

	public C_KeepALIVE(byte decrypt[], GameClient client) {
		super(decrypt);
		// XXX: 傳送遊戲時間 (因為發送了3字節的數據，可能需要將其用於某些操作)
		L1PcInstance pc = client.getActiveChar();
		pc.sendPackets(new S_GameTime());
	}

	@Override
	public String getType() {
		return C_KEEP_ALIVE;
	}
	private void favorBookInventoryTimeOut(){
		long currentTime = System.currentTimeMillis();

		L1PcInstance pc = client.getActiveChar();
		L1FavorBookInventory favorBook = pc.getFavorBook();
		if(favorBook == null){
			return;
		}
		for (L1FavorBookUserObject user : favorBook.getList()){
			if (user == null || user.getType().getEndTime() == null || user.getType().getEndTime().getTime() > currentTime){
				continue;
			}
			favorBook.deleteFavor(user);
		}
	}
	
}