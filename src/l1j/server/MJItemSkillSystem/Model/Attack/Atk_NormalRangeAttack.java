package l1j.server.MJItemSkillSystem.Model.Attack;

import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.utils.MJCommons;

public class Atk_NormalRangeAttack extends MJItemSkillModel {
	@Override
	public double get(L1Character attacker, L1Character defender, L1ItemInstance t_item, double dwd) {
		if (!isPlay(attacker, t_item)) {
			return 0D;
		}

		double dmg = 0D;
		if (is_magic) {
			if (isCounterMagic(defender))
				return 0D;
			dmg = MJCommons.getMagicDamage(attacker, defender, min_val, max_val);
		} else {
			dmg = _rnd.nextInt(max_val - min_val) + min_val;
		}

		if (defender instanceof L1PcInstance) {
			dmg = calcEnchant(t_item, dmg);
			dmg = calcAttr(defender, dmg);
			dmg = calcStat(attacker, dmg);
			dmg = calcSp(attacker, dmg);
			if (is_magic) {
				dmg = MJCommons.getMagicDamage(dmg, defender.getResistance().getMrAfterEraseRemove());
			}
			dmg = Math.max(limit_low_val, dmg);
			dmg = Math.min(limit_high_val, dmg);

			if (eff_id > 0)
				broadcast(attacker, new S_UseAttackSkill(attacker, defender.getId(), eff_id, defender.getX(), defender.getY(), t_item.getItem().getType1() + 1, false));

		} else {
			dmg = calcEnchant(t_item, dmg);
			dmg = calcAttr(defender, dmg);
			dmg = calcStat(attacker, dmg);
			dmg = calcSp(attacker, dmg);
			if (is_magic) {
				dmg = MJCommons.getMagicDamage(dmg, defender.getResistance().getMrAfterEraseRemove());
			}
			dmg = Math.max(limit_low_val, dmg);
			dmg = Math.min(limit_high_val, dmg);

			if (pve_eff_id > 0)
				broadcast(attacker, new S_UseAttackSkill(attacker, defender.getId(), pve_eff_id, defender.getX(), defender.getY(), t_item.getItem().getType1() + 1, false));
		}

		if (MJCommons.isUnbeatable(defender)) {
			return 0D;
		}
		return dmg;
	}

}
