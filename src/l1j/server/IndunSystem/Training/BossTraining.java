package l1j.server.IndunSystem.Training;

import java.util.TimerTask;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class BossTraining extends TimerTask {
	int mapId = 0;

	public BossTraining(int mapId) {
		this.mapId = mapId;
	}

	public void run() {
		L1MonsterInstance mon = null;
		L1NpcInstance npc = null;
		L1PcInstance pc = null;
		for (L1Object obj : L1World.getInstance().getVisibleObjects(this.mapId).values()) {
			if ((obj instanceof L1MonsterInstance)) {
				mon = (L1MonsterInstance)obj;
				mon.deleteMe();
				mon = null;
			}
			if ((obj instanceof L1MerchantInstance)) {
				npc = (L1MerchantInstance)obj;
				npc.deleteMe();
				npc = null;
			}
			if ((obj instanceof L1NpcInstance)) {
				npc = (L1NpcInstance)obj;
				npc.deleteMe();
				npc = null;
			}
			if ((obj instanceof L1PcInstance)) {
				pc = (L1PcInstance)obj;
//				L1Teleport.teleport(pc, 34065, 32313, (short)4, 0, true);
				pc.start_teleport(34065, 32313, 4, 0, 18339, true, false);
			}
		}
		if (this.mapId >= 1400)
			BossTrainingSystem.getInstance().removeRoom(this.mapId);
	}
}