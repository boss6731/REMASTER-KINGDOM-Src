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
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Warehouse.ClanWarehouse;
import l1j.server.server.model.Warehouse.WarehouseManager;

public class S_RetrievePledgeList extends ServerBasePacket {
	public boolean NonValue = false;
	public boolean 使用中 = false;

	public S_RetrievePledgeList(int objid, L1PcInstance pc) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan == null) {
			return;
		}

		ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());

		if (!clanWarehouse.setWarehouseUsingChar(pc.getId(), 0)) {
			int id = clanWarehouse.getWarehouseUsingChar();

			L1Object prevUser = L1World.getInstance().findObject(id);

			if (prevUser instanceof L1PcInstance) {
				L1PcInstance usingPc = (L1PcInstance) prevUser;

				if (usingPc.getClan() == clan) {
					// 1 血盟成員正在使用倉庫，請稍後再試。
					pc.sendPackets("血盟成員 " + usingPc.getName() + " 正在使用血盟倉庫，請稍後再試。");
					使用中 = true;
					return;
				}
			}
			if (!clanWarehouse.setWarehouseUsingChar(pc.getId(), id)) {
				// 1 血盟成員正在使用倉庫，請稍後再試。
				pc.sendPackets("血盟成員 " + clanWarehouse.getName() + " 正在使用血盟倉庫，請稍後再試。");
				使用中 = true;
				return;
			}
		}

		// 剩餘的代碼部分...
	}
}

//        if (pc.getInventory().getSize() < 180) {
if (pc.getInventory().getSize() < 200) {
		int size = clanWarehouse.getSize();
		if (size > 0) {
			writeC(Opcodes.S_RETRIEVE_LIST);
			writeD(objid);
			writeH(size);
			writeC(5); // 血盟倉庫
		L1ItemInstance item = null;
		for (Object itemObject : clanWarehouse.getItems()) {
			item = (L1ItemInstance) itemObject;
			writeD(item.getId());
			writeC(item.getItem().getType2());
			writeH(item.get_gfxid());
			writeC(item.getItem().getBless());
			writeD(item.getCount());
			writeC(item.isIdentified() ? 1 : 0);
			writeS(item.getViewName());
			writeC(0);
		}
		writeD(500); // 提取金額
		writeD(0x00000000);
		writeH(0x00);
		} else {
			this.NonValue = true;
		}
	} else {
		clanWarehouse.setWarehouseUsingChar(0, 0);
		pc.sendPackets(new S_ServerMessage(263)); // 1一個角色可以攜帶的最大物品數量是180個。
	}
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}


