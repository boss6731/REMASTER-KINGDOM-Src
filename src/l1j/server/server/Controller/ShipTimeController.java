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
package l1j.server.server.Controller;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.GameTimeClock;

public class ShipTimeController implements Runnable {
	private static ShipTimeController _instance;

	public static ShipTimeController getInstance() {
		if (_instance == null) {
			_instance = new ShipTimeController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			checkShipTime(); // 檢查船的到達時間
			GeneralThreadPool.getInstance().schedule(this, 5000);
		} catch (Exception e1) {
		}
	}

	private void checkShipTime() {
		int servertime = GameTimeClock.getInstance().getGameTime().getSeconds();
		int nowtime = servertime % 86400;
		if (nowtime >= 34 * 360 && nowtime < 35 * 360
				|| nowtime >= 64 * 360 && nowtime < 65 * 360
				|| nowtime >= 94 * 360 && nowtime < 95 * 360
				|| nowtime >= 124 * 360 && nowtime < 125 * 360
				|| nowtime >= 154 * 360 && nowtime < 155 * 360
				|| nowtime >= 184 * 360 && nowtime < 185 * 360
				|| nowtime >= 214 * 360 && nowtime < 215 * 360
				|| nowtime >= 04 * 360 && nowtime < 05 * 360) {

			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc.getMapId() == 5) {
					pc.start_teleport(32538, 32728, 4, pc.getHeading(), 18339, true, false);
					pc.getInventory().consumeItem(40299, 1); // 前往本土的船票
				} else if (pc.getMapId() == 6) {
					pc.start_teleport(32631, 32983, 0, pc.getHeading(), 18339, true, false);
					pc.getInventory().consumeItem(40298, 1); // 前往說話之島的船票
				} else if (pc.getMapId() == 447) {
					pc.start_teleport(32297, 33087, 440, pc.getHeading(), 18339, true, false);
					pc.getInventory().consumeItem(40302, 1); // 前往海賊島的船票
				} else if (pc.getMapId() == 446) {
					pc.start_teleport(32750, 32874, 445, pc.getHeading(), 18339, true, false);
					pc.getInventory().consumeItem(40303, 1); // 前往隱藏島碼頭的船票
				}
			}
		}

	}
}
