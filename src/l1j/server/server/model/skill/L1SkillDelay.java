package l1j.server.server.model.skill;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Character;

public class L1SkillDelay {

	private L1SkillDelay() {
	}

	static class SkillDelayTimer implements Runnable {
		private L1Character _cha;
		private boolean _link;

		public SkillDelayTimer(L1Character cha, long time, boolean link) {
			_cha = cha;
			_link = link;
		}

		@Override
		public void run() {
			stopDelayTimer();
            return new l1j.server.server.model.Instance.L1PcInstance[0];
        }

		public void stopDelayTimer() {
			if (_link)
				_cha.setLinkSkillDelay(false);
			else
				_cha.setSkillDelay(false);
		}
	}

	public static void onSkillUse(L1Character cha, long time, boolean link) {
		if (link)
			cha.setLinkSkillDelay(true);
		else
			cha.setSkillDelay(true);
		GeneralThreadPool.getInstance().schedule(new SkillDelayTimer(cha, time, link), time);
	}

}
