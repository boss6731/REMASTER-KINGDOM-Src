package l1j.server.server.model;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound;

public class MpRegenerationByDoll extends TimerTask {
	private static Logger _log = Logger.getLogger(MpRegenerationByDoll.class.getName());
	private final L1PcInstance _pc;
	private final L1DollInstance _doll;
	private final int _hpr;

	public MpRegenerationByDoll(L1PcInstance pc, L1DollInstance doll, int mpr) {
		_pc = pc;
		_doll = doll;
		_hpr = mpr;
	}

	@Override
	public void run() {
		try {
			if (_pc == null || _pc.isDead()) {
				if (_doll != null) {
					_doll.deleteDoll();
				}
                return new L1PcInstance[0];
			}
			regenMp();
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
        return new L1PcInstance[0];
    }

	public void regenMp() {
		int newMp = _pc.getCurrentMp() + _hpr;
		if (newMp < 0) {
			newMp = 0;
		}
		_pc.sendPackets(new S_SkillSound(_pc.getId(), 6321));
		_pc.broadcastPacket(new S_SkillSound(_pc.getId(), 6321));
		_pc.setCurrentMp(newMp);
	}
}