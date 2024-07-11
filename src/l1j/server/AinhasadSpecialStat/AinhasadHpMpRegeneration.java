package l1j.server.AinhasadSpecialStat;

import java.util.TimerTask;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound;

public class AinhasadHpMpRegeneration extends TimerTask {
	private final L1PcInstance _pc;
	private final AinhasadSpecialStatInfo _info;

	public AinhasadHpMpRegeneration(L1PcInstance pc, AinhasadSpecialStatInfo info) {
		_pc = pc;
		_info = info;
	}

	@Override
	public void run() {
		try {
			if (_pc == null || _pc.isDead()) {
				_pc.stopAinhasadTimer();
				return;
			}
			
			if (_info.get_restore_val_1() > 0) {
				regenMp();
			}
			
			if (_info.get_restore_val_2() > 0) {
				regenHp();
			}
			
			_pc.sendPackets(new S_SkillSound(_pc.getId(), 6321));
			_pc.broadcastPacket(new S_SkillSound(_pc.getId(), 6321));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void regenMp() {
		int newMp = _pc.getCurrentMp() + _info.get_restore_val_1();
		if (newMp < 0) {
			newMp = 0;
		}
		_pc.setCurrentMp(newMp);
	}
	
	public void regenHp() {
		int newHp = _pc.getCurrentHp() + _info.get_restore_val_2();
		if (newHp < 0) {
			newHp = 0;
		}
		_pc.setCurrentHp(newHp);
	}
}