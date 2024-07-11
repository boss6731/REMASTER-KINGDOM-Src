package l1j.server.MJItemSkillSystem.Model.Attack;

import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.MJCommons;

public class Atk_ChaoticAndNormal extends MJItemSkillModel {

	@Override
	public double get(L1Character attacker, L1Character defender, L1ItemInstance t_item, double dwd) {
		double cdmg = MJCommons.getChaoticDamage(attacker.getLawful());
		if(!isPlay(attacker, t_item))
			return cdmg;

		double dmg = 0D;
		if (is_magic) {
			if (isCounterMagic(defender))
				return cdmg;
			dmg = MJCommons.getMagicDamage(attacker, defender, min_val, max_val);
		} else {
			dmg = _rnd.nextInt(max_val - min_val) + min_val;
		}

		dmg = calcEnchant(t_item, dmg);
		dmg = calcAttr(defender, dmg);
		dmg = calcStat(attacker, dmg);
		dmg	= calcSp(attacker, dmg);
		if(is_magic) {
			dmg = MJCommons.getMagicDamage(dmg, defender.getResistance().getMrAfterEraseRemove());
		}
		dmg = Math.max(limit_low_val, dmg);
		dmg = Math.min(limit_high_val, dmg);

		if(eff_id > 0)
			broadcast(defender, new S_SkillSound(defender.getId(), eff_id));

		if(MJCommons.isUnbeatable(defender))
			return 0D;

		return dmg + cdmg;
	}
}
