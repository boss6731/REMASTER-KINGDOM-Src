package l1j.server.server.command.executor;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.L1Character;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound;

public class L1Clear implements l1j.server.server.command.executor.L1CommandExecutor {

	private L1Clear() {
	}

	public static l1j.server.server.command.executor.L1CommandExecutor getInstance() {
		return new L1Clear();
	}

	public <L1PcInstance> void execute(L1PcInstance pc, String cmdName, String arg) {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, 20)) {
					if (obj instanceof L1MonsterInstance) {
						L1MonsterInstance npc = (L1MonsterInstance) obj;
						if(npc.getNpcId() == 50000220)
							continue;
						
						npc.receiveDamage((L1Character) pc, 1000000);
						if (npc.getCurrentHp() <= 0) {
							pc.sendPackets(new S_SkillSound(obj.getId(), 11748));
							pc.broadcastPacket(new S_SkillSound(obj.getId(), 11748));
						} else {
							pc.sendPackets(new S_SkillSound(obj.getId(), 11748));
							pc.broadcastPacket(new S_SkillSound(obj.getId(), 11748));
						}
					}
				}
                return new L1PcInstance[0];
            }
		});
	}


}