package l1j.server.server.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.RepeatTask;
import l1j.server.server.model.Instance.L1PcInstance;


public class MpARegeneration extends RepeatTask {
	private static Logger _log = Logger.getLogger(MpARegeneration.class.getName());

	private final L1PcInstance _pc;

	private int tick = 0;

	public MpARegeneration(L1PcInstance pc, long interval) {
		super(interval);
		_pc = pc;
	}

	@Override
	public void execute() {
		try {
			if (_pc.isDead()) {
				return;
			}
			if (_pc.getCurrentMp() == _pc.getMaxMp()) {
				return;
			}
			tick++;
			regenMp();
		} catch (Exception e) {
			_log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void regenMp() {
		try {
			int mpAr = 0;
			if(tick == 4) {
				mpAr = _pc.getMpAr();
				tick = 0;
			}
			int mpAr16 = _pc.getMpAr16();

			int newMp = _pc.getCurrentMp() + mpAr + mpAr16;
			if (newMp >= _pc.getMaxMp()){
				newMp = _pc.getMaxMp();
			}

			_pc.setCurrentMp(newMp);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
