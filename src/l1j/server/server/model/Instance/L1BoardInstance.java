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

package l1j.server.server.model.Instance;

import MJNCoinSystem.MJNCoinAdenaManager;
import l1j.server.server.Controller.BugRaceController;
import l1j.server.server.serverpackets.S_Board;
import l1j.server.server.serverpackets.S_BoardRead;
import l1j.server.server.serverpackets.S_EnchantRanking;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;

public class L1BoardInstance extends L1NpcInstance {
	/**
	 * 
	 */
	// private GameServerSetting _GameServerSetting =
	// GameServerSetting.getInstance();
	private static final long serialVersionUID = 1L;

	public L1BoardInstance(L1Npc template) {
		super(template);
	}	

	@Override
	public void onAction(L1PcInstance player) {
		if (this.getNpcTemplate().get_npcId() == 999999) { // Bugbear 勝率排行榜
			if (BugRaceController.getInstance().getBugState() == 0) { // 票在售
				player.sendPackets(new S_Board(this));
			} else if (BugRaceController.getInstance().getBugState() == 1) { // 比賽中
				player.sendPackets(new S_SystemMessage("比賽中無法查看。"));
			} else if (BugRaceController.getInstance().getBugState() == 2) { // 準備下一場比賽
				player.sendPackets(new S_SystemMessage("正在準備下一場比賽。"));
			}
		/*} else if(this.getNpcTemplate().get_npcId() == 4200020) {
			player.sendPackets(S_ShowCmd.getPlayMovieNoti("http://1111111.megaplug.kr/price/pricePreviewGM27.html", -1));
		} else if(this.getNpcTemplate().get_npcId() == 4200022) {
			player.sendPackets(S_ShowCmd.getPlayMovieNoti("http://1111111.megaplug.kr/priceuse/pricePreviewuse7.html", -1));*/
			//TODO 中介交易公告板
		} else if (this.getNpcTemplate().get_npcId() == 45000178) {
			MJNCoinAdenaManager.DEFAULT.on_ncoin_adena_show_list(player, this);
			//player.sendPackets(new S_AuctionSystemBoard(this));
		} else {
			player.sendPackets(new S_Board(this));
		}
	}

	public void onAction(L1PcInstance player, int number) {
		/*if(this.getNpcTemplate().get_npcId() == 4200020) {
		player.sendPackets(S_ShowCmd.getPlayMovieNoti("http://1111111.megaplug.kr/price/pricePreviewGM27.html", -1));*/
		//TODO 中介交易公告板
		if (this.getNpcTemplate().get_npcId() == 45000178) {
			MJNCoinAdenaManager.DEFAULT.on_ncoin_adena_show_list(player, this, number);
			//player.sendPackets(new S_AuctionSystemBoard(this, number));
		} else {
			player.sendPackets(new S_Board(this, number));
		}
	}
	

	public void onActionRead(L1PcInstance player, int number) {
		if (this.getNpcTemplate().get_npcId() == 4200013) { // 強化排行榜
			player.sendPackets(new S_EnchantRanking(player, number));
			//TODO 中介交易公告板
		} else if (this.getNpcTemplate().get_npcId() == 45000178) {
			MJNCoinAdenaManager.DEFAULT.on_ncoin_adena_show_content(player, number);
//            player.sendPackets(new S_AuctionSystemBoard(number));
		} else {
			if (this.getNpcTemplate().get_npcId() == 500002) { // 建議事項
				if (!player.isGm()) {
					player.sendPackets(new S_SystemMessage("僅限管理員查看。"));
					player.sendPackets(new S_Board(this));
					return;
				}
			} else if (this.getNpcTemplate().get_npcId() == 9200036) {
				if (!player.isGm()) {
					player.sendPackets(new S_SystemMessage("僅限管理員閱覽。"));
					player.sendPackets(new S_Board(this));
					return;
				}
			}
			player.sendPackets(new S_BoardRead(this, number));
		}
	}
}
