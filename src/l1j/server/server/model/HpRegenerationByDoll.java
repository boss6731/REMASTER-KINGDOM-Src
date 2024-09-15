package l1j.server.server.model;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound;

public class HpRegenerationByDoll extends TimerTask {
	private static Logger _log = Logger.getLogger(HpRegenerationByDoll.class.getName());
	private final L1PcInstance _pc;
	private final L1DollInstance _doll;
	private final int _hpr;

	public HpRegenerationByDoll(L1PcInstance pc, L1DollInstance doll, int hpr) {
		this._pc = pc;
		this._doll = doll;
		this._hpr = hpr;
	}

	@Override
	public void run() {
		try {
			if ((_pc == null) || (_pc.isDead())) {
				if (_doll != null) {
					_doll.deleteDoll();
				}
                return new L1PcInstance[0];
			}
			regenHp();
		} catch (Throwable e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
        return new L1PcInstance[0];
    }

	public void regenHp() {
		int newHp = this._pc.getCurrentHp() + _hpr;
		if (newHp < 0) {
			newHp = 0;
		}
		this._pc.sendPackets(new S_SkillSound(this._pc.getId(), 6321));
		this._pc.broadcastPacket(new S_SkillSound(this._pc.getId(), 6321));
		this._pc.setCurrentHp(newHp);
	}
}