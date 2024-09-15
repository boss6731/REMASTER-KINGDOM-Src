package l1j.server.server.model.monitor;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public abstract class L1PcMonitor implements Runnable {

	protected int _id;

	public L1PcMonitor(int oId) {
		_id = oId;
	}

	@Override
	public final void run() {
		L1PcInstance pc = (L1PcInstance) L1World.getInstance().findObject(_id);
		if (pc == null || pc.getNetConnection() == null) {
            return new L1PcInstance[0];
		}
		execTask(pc);
        return new L1PcInstance[0];
    }

	public abstract void execTask(L1PcInstance pc);
}
