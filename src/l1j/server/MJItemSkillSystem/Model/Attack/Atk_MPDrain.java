package l1j.server.MJItemSkillSystem.Model.Attack;

import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.MJCommons;

public class Atk_MPDrain extends MJItemSkillModel {

	@Override
	public double get(L1Character attacker, L1Character defender, L1ItemInstance t_item, double dwd) {
		int dmp = defender.getCurrentMp();
		if (dmp > 0) {
			int pure = getProbability(attacker, t_item);
			int success = MJRnd.next(0, 100);

			if (success < pure) {
				int drn = MJRnd.next(min_val, max_val);
				if (drn > dmp)
					drn = dmp;

				defender.setCurrentMp((int) (defender.getCurrentMp() - drn));
				attacker.setCurrentMp((int) (attacker.getCurrentMp() + drn));
			}
		}

		if (MJCommons.isUnbeatable(defender) || !isPlay(attacker, t_item))
			return 0D;

		int pure = getProbability(attacker, t_item);
		if (is_magic) {
			if (!MJCommons.isMagicSuccess(attacker, defender, pure))
				return 0D;

			if (isCounterMagic(defender))
				return 0D;
		} else {
			if (!isInPercent(pure))
				return 0D;
		}

		if (eff_id > 0)
			broadcast(defender, new S_SkillSound(defender.getId(), eff_id));
		return 0D;
	}
}