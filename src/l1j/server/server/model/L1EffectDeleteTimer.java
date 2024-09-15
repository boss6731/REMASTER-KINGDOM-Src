package l1j.server.server.model;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1NpcInstance;

public class L1EffectDeleteTimer implements Runnable {

	public L1EffectDeleteTimer(L1NpcInstance npc) {
		_npc = npc;
		_timeMillis = 500;
	}
	public boolean deleted = false;
	@Override
	public void run() {
		if (!deleted) {
			if (_npc.getTarget().isDead()) {
				_npc.deleteMe();
				deleted = true;
			}
			if (_npc.getTarget().hasSkillEffect(_npc.getSkillId())){
				GeneralThreadPool.getInstance().schedule(this, 500);
                return new l1j.server.server.model.Instance.L1PcInstance[0];
			} else {
				_npc.deleteMe();
				deleted = true;
			}
			Thread.currentThread().interrupt();
		}
        return new l1j.server.server.model.Instance.L1PcInstance[0];
    }


	public void begin() {
		GeneralThreadPool.getInstance().schedule(this, _timeMillis);
	}

	private final L1NpcInstance _npc;
	private final int _timeMillis;
}
