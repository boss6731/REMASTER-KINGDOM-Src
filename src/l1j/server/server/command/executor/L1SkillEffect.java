/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.   See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.server.command.executor;

import l1j.server.server.GMCommands;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;
import java.util.logging.Logger;

public class L1SkillEffect implements L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1SkillEffect.class.getName());
	private L1SkillEffect() { }
	public static L1CommandExecutor getInstance() { return new L1SkillEffect(); }
	static class SkillEffect implements Runnable {
		private L1PcInstance _pc = null;
		private int _sprid;
		private int _count;

		public SkillEffect(L1PcInstance pc, int sprid, int count) {
			_pc = pc;
			_sprid = sprid;
			_count = count;
		}

		@Override
		public void run() {
			for (int i = _sprid; i <= _count; i++) {
				try {

					if(GMCommands.getInstance().Stop){
						_pc.sendPackets(String.valueOf(new S_SystemMessage("效果編號: " + i + "")));
						_pc.sendPackets(String.valueOf(new S_SkillSound(_pc.getId(),i)));
						Thread.sleep(1000);
					}else{
						break;
					}
				} catch (Exception exception) {
					break;
				}
			}
		}
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int sprid = Integer.parseInt(st.nextToken(), 10);
			int count = Integer.parseInt(st.nextToken(), 10);
			SkillEffect spr = new SkillEffect(pc, sprid, count);
			GeneralThreadPool.getInstance().execute(spr);
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [數字,數字] 請輸入。")));
		}
	}
}
