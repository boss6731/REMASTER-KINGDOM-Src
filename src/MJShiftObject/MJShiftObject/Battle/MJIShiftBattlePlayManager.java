package MJShiftObject.Battle;

import l1j.server.server.model.Instance.L1PcInstance;

public interface MJIShiftBattlePlayManager {
	void on_tick();

	int next_update_tick();

	void on_update_tick();

	void on_enter(L1PcInstance paramL1PcInstance);

	void on_closed();
}


