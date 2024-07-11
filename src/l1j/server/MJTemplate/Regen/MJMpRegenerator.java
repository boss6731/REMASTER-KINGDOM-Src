package l1j.server.MJTemplate.Regen;

import java.util.ArrayList;

import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.AinhasadSpecialStat.Einpointffecttable;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Regen.MJReGenerator.BuffBonusInfo;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

class MJMpRegenerator implements Runnable {
	static MJMpRegenerator newInstance(L1PcInstance pc, MJReGenerator generator) {
		MJMpRegenerator regenerator = new MJMpRegenerator();
		regenerator._pc = pc;
		regenerator._generator = generator;
		return regenerator;
	}

	private static final ArrayList<BuffBonusInfo> buff_bonuses;
	static {
		buff_bonuses = new ArrayList<BuffBonusInfo>();
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.STATUS_BLUE_POTION2, 1));
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.STATUS_BLUE_POTION, 2));
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.MEDITATION, 5));
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.CONCENTRATION, 4));
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.COOKING_1_2_N, 3));
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.COOKING_1_2_S, 3));
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.COOKING_1_20_N, 2));
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.COOKING_1_20_S, 2));
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.COOKING_1_12_N, 2));
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.COOKING_1_12_S, 2));
		buff_bonuses.add(new BuffBonusInfo(L1SkillId.STATUS_CASHSCROLL2, 4));
	}

	private L1PcInstance _pc;
	private MJReGenerator _generator;
	private boolean _is_disposed;

	private MJMpRegenerator() {
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
		GeneralThreadPool.getInstance().schedule(this, _pc.get_latest_action().get_mp_generate_millis());
	}

	private int get_wis_mpr() {
		int wis = _pc.getAbility().getTotalWis();
		if (wis == 15 || wis == 16)
			return 2;
		else if (wis == 17)
			return 3;
		else if (wis >= 18)
			return wis - 14 + 1;
		return 1;
	}

	private void on_tick() {
		if (_generator.get_is_stopped())
			return;

		if (_pc.isDead() || _pc.getCurrentMp() >= _pc.getMaxMp() || _pc.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER))
			return;
		
		Einpointffecttable eff = Einpointffecttable.getInstance();
		AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(_pc.getId());
		int restore = 0;
		
		if (Info != null) {
			if (Info.get_restore() != 0) {
				restore = eff.ismp(eff.getstate(_pc, 4));
			}
		}

		if (_pc.get_food() < 40 || (MJReGenerator.is_over_weight(_pc) && _pc.getAbility().getTotalWis() < 55)) {
			if(restore != 0 && !_pc.isDead() && _pc.getCurrentMp() < _pc.getMaxMp()) {
				_pc.setCurrentMp(Math.min(Math.max(_pc.getCurrentMp() + restore, 1), _pc.getMaxMp()));
			}
			return;
		}

		int max = get_wis_mpr();
		int equipped_tick = _pc.getInventory().mpRegenPerTick() + _pc.getMpr();
		int bonus = MJRnd.next(max) + 1;
		if (L1HouseLocation.isInHouse(_pc.getX(), _pc.getY(), _pc.getMapId()))
			bonus += 3;
		if (L1HouseLocation.isRegenLoc(_pc, _pc.getX(), _pc.getY(), _pc.getMapId()))
			bonus += 3;

		for (BuffBonusInfo bInfo : buff_bonuses) {
			if(_pc.hasSkillEffect(bInfo.skill_id)) {
				bonus += bInfo.skill_id == L1SkillId.MEDITATION && _pc.isPassive(MJPassiveID.MEDITATION_BEYOND.toInt()) 
						&& (_pc.hasSkillEffect(L1SkillId.STATUS_BLUE_POTION) || _pc.hasSkillEffect(L1SkillId.STATUS_BLUE_POTION2)) ? bInfo.bonus + (int)(_pc.getMaxMp() * 0.02) : bInfo.bonus;
			}
		}
		if (MJReGenerator.is_inn(_pc))
			bonus += 3;
		
		if (MJReGenerator.is_over_weight(_pc) && _pc.getAbility().getTotalWis() >= 55) {
			bonus = (int) Math.round(bonus * (25 * 0.01));
		}

		int new_mp = Math.max(_pc.getCurrentMp() + bonus + equipped_tick + restore, 1);
		if (_pc.isDead()) {
			return;
		}
		if (new_mp >= _pc.getMaxMp()) {
			_pc.setCurrentMp(_pc.getMaxMp());
			return;
		}

		_pc.setCurrentMp(Math.min(new_mp, _pc.getMaxMp()));
	}
}
