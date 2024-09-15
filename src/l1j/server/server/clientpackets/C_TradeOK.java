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

package l1j.server.server.clientpackets;

import l1j.server.server.GameClient;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_TradeOK extends ClientBasePacket {

	private static final String C_TRADE_CANCEL = "[C] C_TradeOK";

	public C_TradeOK(byte abyte0[], GameClient clientthread)
			throws Exception {
		super(abyte0);

		L1PcInstance player = clientthread.getActiveChar();
		if ( player == null )return;
		L1PcInstance trading_partner = (L1PcInstance) L1World.getInstance().findObject(player.getTradeID());
		if (trading_partner != null) {
			player.setTradeOk(true);
			if (player.getTradeOk() && trading_partner.getTradeOk()) { // 雙方都按了OK
					// (180 - 16) 個以內的物品時，交易成立。
					// 原本應考慮到已有重疊物品（如金幣等）的情況。
					//                if (player.getInventory().getSize() < (180 - 16) && trading_partner.getInventory().getSize() < (180 - 16)) { // 將雙方的物品交給對方
				if (player.getInventory().getSize() < (200 - 16) && trading_partner.getInventory().getSize() < (200 - 16)) { // 將雙方的物品交給對方
					L1Trade trade = new L1Trade();
					trade.TradeOK(player);
				} else { // 將雙方的物品退還給各自
					player.sendPackets(new S_ServerMessage(263)); // \f1一個角色最多只能攜帶180個物品。
					trading_partner.sendPackets(new S_ServerMessage(263)); // \f1一個角色最多只能攜帶180個物品。
					L1Trade trade = new L1Trade();
					trade.TradeCancel(player);
				}
			}
			//** 迷你遊戲 **/
		} else {
			L1Trade trade = new L1Trade();
			trade.TradeOK(player);
			System.out.println("c opcode : ");
		}
	}

	@Override
	public String getType() {
		return C_TRADE_CANCEL;
	}

}
