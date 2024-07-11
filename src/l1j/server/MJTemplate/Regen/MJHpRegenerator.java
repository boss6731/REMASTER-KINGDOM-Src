package l1j.server.MJTemplate.Regen;

import java.util.ArrayList;

import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.AinhasadSpecialStat.Einpointffecttable;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Regen.MJReGenerator.BuffBonusInfo;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

class MJHpRegenerator implements Runnable {
	private static final ArrayList<BuffBonusInfo> buff_bonuses;
	static {
		buff_bonuses = new ArrayList<BuffBonusInfo>();
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.COOKING_1_12_N, 2));
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.COOKING_1_12_S, 2));
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.COOKING_1_19_N, 2));
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.COOKING_1_19_S, 2));
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.STATUS_CASHSCROLL, 4));
	}

	static MJHpRegenerator newInstance(L1PcInstance pc, MJReGenerator generator) {
		MJHpRegenerator regenerator = new MJHpRegenerator();
		regenerator._pc = pc;
		regenerator._generator = generator;
		return regenerator;
	}

	private L1PcInstance _pc;
	private MJReGenerator _generator;
	private boolean _is_disposed;

	private MJHpRegenerator() {
		_is_disposed = false;
	}

	L1PcInstance get_pc() {
		return _pc;
	}

	void set_pc(L1PcInstance pc) {
		_pc = pc;
	}

	void dipose() {
		_is_disposed = true;
	}

	@Override
	public void run() {
		if (_pc == null || _is_disposed)
			return;

		try {
			on_tick();
		} catch (Exception e) {
			e.printStackTrace();
		}
		do_continue();
	}

	void do_continue() {
		GeneralThreadPool.getInstance().schedule(this, _pc.get_latest_action().get_hp_generate_millis());
	}

	private void on_tick() {
		if (_generator.get_is_stopped())
			return;

		if (_pc.isDead() || _pc.getCurrentHp() >= _pc.getMaxHp() || MJReGenerator.is_under_water(_pc)
				|| _pc.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER))
			return;
		
		Einpointffecttable eff = Einpointffecttable.getInstance();
		AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(_pc.getId());
		int restore = 0;
		
		if (Info != null) {
			if (Info.get_restore() != 0) {
				restore = eff.ishp(eff.getstate(_pc, 4));
			}
		}

		if (_pc.get_food() < 40 || (MJReGenerator.is_over_weight(_pc) && _pc.getAbility().getTotalCon() < 55) || (!_pc.isWizard() && _pc.hasSkillEffect(L1SkillId.BERSERKERS))) {
			if (restore != 0 && !_pc.isDead() && _pc.getCurrentHp() < _pc.getMaxHp()) {
				_pc.setCurrentHp(Math.min(Math.max(_pc.getCurrentHp() + restore, 1), _pc.getMaxHp()));
			}
			return;
		}

		int max = 1;
		if (_pc.getLevel() > 11 && _pc.getAbility().getTotalCon() >= 14) {
			max = Math.min(14, _pc.getAbility().getTotalCon() - 12);
		}

		int equipped_tick = _pc.getInventory().hpRegenPerTick() + _pc.getHpr();
		int bonus = MJRnd.next(max) + 1;
		if (L1HouseLocation.isInHouse(_pc.getX(), _pc.getY(), _pc.getMapId()))
			bonus += 5;
		if (L1HouseLocation.isRegenLoc(_pc, _pc.getX(), _pc.getY(), _pc.getMapId()))
			bonus += 5;

		for (BuffBonusInfo bInfo : buff_bonuses) {
			if (_pc.hasSkillEffect(bInfo.skill_id))
				bonus += bInfo.bonus;
		}
		if (MJReGenerator.is_inn(_pc))
			bonus += 5;

		if (MJReGenerator.is_in_lifestream(_pc))
			bonus += 3;

		if (MJReGenerator.is_over_weight(_pc) && _pc.getAbility().getTotalCon() >= 55) {
			bonus = (int) Math.round(bonus * (25 * 0.01));
		}

		int new_hp = Math.max(_pc.getCurrentHp() + bonus + equipped_tick + restore, 1);
		if (_pc.isDead()) {
			return;
		}
		if (new_hp >= _pc.getMaxHp()) {
			_pc.setCurrentHp(_pc.getMaxHp());
			return;
		}

		_pc.setCurrentHp(Math.min(new_hp, _pc.getMaxHp()));
	}
}
