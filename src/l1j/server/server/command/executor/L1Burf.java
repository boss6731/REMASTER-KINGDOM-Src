package l1j.server.server.server.command.executor;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Object;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;

public class L1Burf implements l1j.server.server.command.executor.L1CommandExecutor {

	private L1Burf() {  }

	public static l1j.server.server.command.executor.L1CommandExecutor getInstance() {
		return new L1Burf();
	}

	static class Burfskill implements Runnable {
		private L1PcInstance _pc = null;
		private int _sprid;
		private int _count;

		public Burfskill(L1PcInstance pc, int sprid, int count) {
			_pc = pc;
			_sprid = sprid;
			_count = count;
		}

		@Override
		public void run() {
			for (int i = 0; i < _count; i++) {
				try {
					Thread.sleep(500);
					int num = _sprid + i;
					if(_pc.getOnlineStatus() == 0)
						break;
					_pc.sendPackets(String.valueOf(new S_SystemMessage("技能編號: " + num)));
					_pc.sendPackets(String.valueOf(new S_SkillSound(_pc.getId(), _sprid+i)));
					_pc.broadcastPacket(new S_SkillSound(_pc.getId(), _sprid+i));
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

			Burfskill spr = new Burfskill(pc, sprid, count);
			GeneralThreadPool.getInstance().execute(spr);

		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " 請輸入 [castgfx]。")));
		}
	}

	@Override
	public void execute(L1Object pc, String cmdName, String arg) {

	}
/*	@override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer stringtokenizer = new StringTokenizer(arg);
			int sprid = Integer.parseInt(stringtokenizer.nextToken());

			pc.sendPackets(new S_SkillSound(pc.getId(), sprid));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), sprid));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName + " 請輸入 [castgfx]。"));
		}
	}

 */
}
