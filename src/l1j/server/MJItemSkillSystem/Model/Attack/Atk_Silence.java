package l1j.server.MJItemSkillSystem.Model.Attack;

import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.MJCommons;

public class Atk_Silence extends MJItemSkillModel{
	@Override
	public double get(L1Character attacker, L1Character defender, L1ItemInstance t_item, double dwd) {
		if(!isPlay(attacker, t_item))
			return 0D;
		
		int pure = getProbability(attacker, t_item);
		if(is_magic){
			if(!MJCommons.isMagicSuccess(attacker, defender, pure))
				return 0D;
			
			if(isCounterMagic(defender))
				return 0D;
		}else{
			if(!isInPercent(pure))
				return 0D;
		}
		
		if(defender.hasSkillEffect(L1SkillId.SILENCE))
			return 0D;
		
/*/		double time	=  _rnd.nextInt(max_val - min_val) + min_val;
		time 		= calcEnchant(t_item, time);
		time		= calcAttr(defender, time);
		time		= calcStat(attacker, time);*/
		defender.setSkillEffect(L1SkillId.SILENCE, 5);
		
		if(eff_id > 0)
			broadcast(defender, new S_SkillSound(defender.getId(), eff_id));
		return 0D;
	}
}
