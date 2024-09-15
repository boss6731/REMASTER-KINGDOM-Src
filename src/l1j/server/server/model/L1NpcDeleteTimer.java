package l1j.server.server.server.model;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;

public class L1NpcDeleteTimer implements Runnable {

	public L1NpcDeleteTimer(L1NpcInstance npc, int timeMillis) {
		_npc = npc;
		_timeMillis = timeMillis;
	}
	public boolean npcdead = false;
	@Override
	public void run() {
		if (_npc.getNpcId() == 8502104) {
			if (!npcdead) {
				Broadcaster.broadcastPacket(_npc, new S_DoActionGFX(_npc.getId(), ActionCodes.ACTION_Hide), true);
				npcdead = true;
				GeneralThreadPool.getInstance().schedule(this, 500);
			} else if (npcdead) {
				_npc.NpcDie();
			}
		} else {
			_npc.deleteMe();
		}
        return new l1j.server.server.model.Instance.L1PcInstance[0];
    }


	public void begin() {
		GeneralThreadPool.getInstance().schedule(this, _timeMillis);
	}

	private final L1NpcInstance _npc;
	private final int _timeMillis;
}
