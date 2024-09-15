package l1j.server.server.model;


import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.RepeatTask;
import l1j.server.server.model.Instance.L1PcInstance;


public class HpARegeneration extends RepeatTask {

	private static Logger _log = Logger.getLogger(HpARegeneration.class.getName());
	private final L1PcInstance _pc;
	public HpARegeneration(L1PcInstance pc, long interval) {
		super(interval);
		_pc = pc;
	}
	@Override
	public void execute() {
		try {
			if (_pc.isDead()) {
				return;
			}
			if (_pc.getCurrentHp() == _pc.getMaxHp()) {
				return;
			}
			regenHp();
		
		} catch (Exception e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void regenHp() {
		try {
		
			int hpAr = _pc.getHpAr();
			int newHp = _pc.getCurrentHp();
			newHp += hpAr;
			if (newHp >= _pc.getMaxHp()){
				newHp = _pc.getMaxHp();
			}
			if (!_pc.isDead()) {
				_pc.setCurrentHp(newHp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
