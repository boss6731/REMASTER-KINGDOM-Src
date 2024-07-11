package l1j.server.MJItemSkillSystem.Model.Attack;

import java.util.ArrayList;
import java.util.Collection;

import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.MJCommons;

public class Atk_WideAttack extends MJItemSkillModel {

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

			if (eff_id > 0) {
				broadcast(defender, new S_SkillSound(attacker.getId(), eff_id));

				Collection<L1Object> list = attacker.getKnownObjects();
				if (list.size() <= 0)
					return 0;

				ArrayList<L1Character> tars = new ArrayList<L1Character>(list.size());
				tars.add(defender);
				for (L1Object obj : list) {
					if (obj == null || !(obj instanceof L1Character) || obj instanceof L1DollInstance)
						continue;

					L1Character c = (L1Character) obj;
					if (obj instanceof L1PcInstance)
						continue;
					if (c.getMap().isSafetyZone(c.getLocation()))
						continue;
					if (c.isDead() || MJCommons.getDistance(defender.getX(), defender.getY(), c.getX(), c.getY()) > Location_count)
						continue;
					if (c == defender)
						continue;
					
					if (!isCounterMagic(c) && !MJCommons.isUnbeatable(defender))
						c.receiveDamage(attacker, (int) dmg);
					
					tars.add(c);
				}
			}
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

			if (pve_eff_id > 0) {
				broadcast(defender, new S_SkillSound(attacker.getId(), pve_eff_id));
				Collection<L1Object> list = attacker.getKnownObjects();
				if (list.size() <= 0)
					return 0;

				ArrayList<L1Character> tars = new ArrayList<L1Character>(list.size());
				tars.add(defender);
				for (L1Object obj : list) {
					if (obj == null || !(obj instanceof L1Character) || obj instanceof L1DollInstance)
						continue;

					L1Character c = (L1Character) obj;
					if (obj instanceof L1PcInstance)
						continue;
					if (c.getMap().isSafetyZone(c.getLocation()))
						continue;
					if (c.isDead() || MJCommons.getDistance(defender.getX(), defender.getY(), c.getX(), c.getY()) > Location_count)
						continue;
					if (c == defender)
						continue;
					
					if (!isCounterMagic(c) && !MJCommons.isUnbeatable(defender))
						c.receiveDamage(attacker, (int) dmg);
					
					tars.add(c);
				}
			}
		}

		if (MJCommons.isUnbeatable(defender)) {
			return 0D;
		}
		return dmg;
	}

}
