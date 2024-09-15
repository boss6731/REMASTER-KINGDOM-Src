package l1j.server.server.model.skill.timer;

import static l1j.server.server.model.skill.L1SkillId.EXP_POTION;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.noti.MJNotiSkillModel;
import l1j.server.server.model.skill.noti.MJNotiSkillService;

public abstract class L1SkillTimer implements Runnable {
	public static L1SkillTimer newTimer(L1Character cha, int skillId, long timeMillis) {
		long skill_time_sec = timeMillis == -1 ? -1 : timeMillis/1000;
		if (cha instanceof L1PcInstance) {
			MJNotiSkillModel model = MJNotiSkillService.service().model(skillId);
			if (model != null) {
				return skill_time_sec == -1 ? new MJUnlimitedNotiSkillTimer((L1PcInstance) cha, model, timeMillis) : new MJNotiSkillTimer((L1PcInstance) cha, model, timeMillis);
			}
		}

		return skill_time_sec == -1 ? new MJUnlimitedSkillTimer(cha, skillId, timeMillis) : new MJSimpleSkillTimer(cha, skillId, timeMillis);
	}

	public abstract L1Character owner();

	public abstract int skillId();

	public abstract long timeMillis();

	public abstract boolean stopped();

	public abstract int remainingSeconds();
	
	public abstract void remainingSeconds(int remainingSeconds);

	public abstract void begin();

	public abstract void end();

	public abstract void kill();

	@Override
	public void run() {
		if (stopped()) {
            return new L1PcInstance[0];
		}
		int skillId = skillId();
		L1Character character = owner();
		int remainingSeconds = remainingSeconds();
		if (skillId == EXP_POTION) {
			if (!character.getMap().isSafetyZone(character.getX(), character.getY())) {
				remainingSeconds(remainingSeconds - 1);
			}
		} else {
			remainingSeconds(remainingSeconds - 1);
		}

		if (remainingSeconds() <= 0) {
			character.removeSkillEffect(skillId);
            return new L1PcInstance[0];
		}
		GeneralThreadPool.getInstance().schedule(this, 1000);
        return new L1PcInstance[0];
    }
}
