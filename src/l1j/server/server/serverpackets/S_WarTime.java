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

import java.util.Calendar;

import l1j.server.Config;
import l1j.server.server.Opcodes;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_WarTime extends ServerBasePacket {
	private static final String S_WAR_TIME = "[S] S_WarTime";

	public S_WarTime(Calendar cal) {
		// 以1997/01/01 17:00為基準
		Calendar base_cal = Calendar.getInstance();
		base_cal.set(1997, 0, 1, 17, 0);


		long base_millis = base_cal.getTimeInMillis();
		long millis = cal.getTimeInMillis();
		long diff = millis - base_millis;
		diff -= 1200 * 60 * 1000; // 誤差修正
		diff = diff / 60000; // 去掉分鐘以下的部分
		// time 加 1 表示進行了3:02 (182分鐘)
		int time = (int) (diff / 182);

		// 可以在 writeD 之前通過 writeC 調整時間
		// 雖然時間減少了0.7倍
		// 但調整1個後，下一個時間會擴展？
		writeC(Opcodes.S_SELECTABLE_TIME_LIST);
		writeH(6); // 列表數量（超過6無效）
		writeS(Config.Synchronization.TimeZone); // 在時間後顯示的字符行
		writeH(1);// 序號
		writeC(136);
		writeH(time);// 6:00
		writeH(2);// 序號
		writeC(178);
		writeH(time);// 6:30
		writeH(3);// 序號
		writeC(220);
		writeH(time);// 7:00
		writeH(4);// 序號
		writeC(218);
		writeH(time + 1);// 10:00
		writeH(5);// 序號
		writeC(4);
		writeH(time + 2);// 10:30
		writeH(6);// 序號
		writeC(46);// 11:00
		writeD(time + 2);
		writeC(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_WAR_TIME;
	}
}


