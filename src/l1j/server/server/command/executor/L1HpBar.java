package l1j.server.server.command.executor;

import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1HpBar implements L1CommandExecutor {

	private L1HpBar() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1HpBar();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		if (arg.equalsIgnoreCase("開")) {
			pc.setSkillEffect(L1SkillId.GMSTATUS_HPBAR, -1);
			for (L1Object obj : pc.getKnownObjects()) {
				if (isHpBarTarget(obj)) {
					pc.sendPackets(new S_HPMeter((L1Character)obj));
				}
			}
		} else if (arg.equalsIgnoreCase("關")) {
			pc.removeSkillEffect(L1SkillId.GMSTATUS_HPBAR);
			for (L1Object obj : pc.getKnownObjects()) {
				if (isHpBarTarget(obj)) {
					pc.sendPackets(String.valueOf(new S_HPMeter(obj.getId(), 0xFF, 0xff)));
				}
			}
		} else {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [開,關] 請輸入。")));
		}
	}

	public static boolean isHpBarTarget(L1Object obj) {
		if (obj instanceof L1MonsterInstance) {
			return true;
		}
		if (obj instanceof L1PcInstance) {
			return true;
		}
		if (obj instanceof L1SummonInstance) {
			return true;
		}
		if (obj instanceof L1PetInstance) {
			return true;
		}
		if(obj instanceof MJCompanionInstance)
			return true;
		return false;
	}
}
