package l1j.server.server.model.skill.timer;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Character;

public class MJSimpleSkillTimer extends L1SkillTimer {
    private L1Character character;

    private final long timeMillis;

    private final int skillId;

    private int remainingSeconds;

    private boolean stop;

    MJSimpleSkillTimer(L1Character character, int skillId, long timeMillis) {
        this.character = character;
        this.skillId = skillId;
        this.timeMillis = timeMillis;
        this.remainingSeconds = (int)(timeMillis / 1000L);
        this.stop = false;
    }

    public L1Character owner() {
        return this.character;
    }

    public int skillId() {
        return this.skillId;
    }

    public long timeMillis() {
        return this.timeMillis;
    }

    public boolean stopped() {
        return this.stop;
    }

    public int remainingSeconds() {
        return this.remainingSeconds;
    }

    public void remainingSeconds(int remainingSeconds) {
        this.remainingSeconds = remainingSeconds;
    }

    public void begin() {
        GeneralThreadPool.getInstance().schedule(this, 1000L);
    }

    public void end() {
        this.stop = true;
        MJSkillStopper.stopSkill(this.character, this.skillId);
        this.character = null;
    }

    public void kill() {
        this.stop = true;
        this.character = null;
    }
}
