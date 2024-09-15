package l1j.server.server.model.skill.timer;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Character;

public class MJSimpleSkillTimer extends L1SkillTimer{
	private L1Character character;
	private final long timeMillis;
	private final int skillId;
	private int remainingSeconds;
	private boolean stop;
	MJSimpleSkillTimer(L1Character character, int skillId, long timeMillis) {
		this.character = character;
		this.skillId = skillId;
		this.timeMillis = timeMillis;
		this.remainingSeconds = (int)(timeMillis / 1000);
		this.stop = false;
	}

	@Override
	public L1Character owner(){
		return character;
	}

	@Override
	public int skillId(){
		return skillId;
	}

	@Override
	public long timeMillis(){
		return timeMillis; 
	}
	
	@Override
	public boolean stopped(){
		return stop;
	}

	@Override
	public int remainingSeconds() {
		return remainingSeconds;
	}

	@Override
	public void remainingSeconds(int remainingSeconds) {
		this.remainingSeconds = remainingSeconds;
	}
	
	@Override
	public void begin() {
		GeneralThreadPool.getInstance().schedule(this, 1000);
	}
	
	@Override
	public void end() {
		stop = true;
		MJSkillStopper.stopSkill(character, skillId);
		character = null;
	}

	@Override
	public void kill() {
		stop = true;
		character = null;
	}

}
