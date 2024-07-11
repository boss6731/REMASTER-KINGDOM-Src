package l1j.server.MJItemSkillSystem.Model.Defence;

import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.MJCommons;

public class Def_HpMpRegen extends MJItemSkillModel{

	@Override
	public double get(L1Character attacker, L1Character defender, L1ItemInstance t_item, double dwd) {
		if(MJCommons.isBlessed(defender) || MJCommons.isUnbeatable(defender) || !isPlay(defender, t_item))
			return 0D;
		
		double heal = min_val;
		
		if (100 * defender.getCurrentHp() / defender.getMaxHp() > 10) {

			if(min_val != max_val){
				heal		= _rnd.nextInt(max_val - min_val) + min_val;
				heal		= calcEnchant(t_item, heal);
				heal		= calcAttr(defender, heal);
				heal		= calcStat(defender, heal);
				heal		= Math.max(limit_low_val, heal);
				heal		= Math.min(limit_high_val, heal);
			}
			heal			= MJCommons.calcHealing(defender, heal);
			
			if(t_item.getEnchantLevel() > 0) {
				heal += (heal * (t_item.getEnchantLevel() * 0.1));
			}
			
			defender.setCurrentHp((int) (defender.getCurrentHp() + heal));
			defender.setCurrentMp((int) (defender.getCurrentMp() + (heal / 2)));
			
//			System.out.println("HP: "+heal);

			
			if(eff_id > 0) {
				if(t_item.getEnchantLevel() <= 0) {
					broadcast(defender, new S_SkillSound(defender.getId(), eff_id));
				} else if(t_item.getEnchantLevel() == 1){
					broadcast(defender, new S_SkillSound(defender.getId(), eff_id + 2));
				}else if(t_item.getEnchantLevel() >= 2){
					broadcast(defender, new S_SkillSound(defender.getId(), eff_id + 4));
				}
			}
		}
		
		defender.setSkillEffect(L1SkillId.ARMOR_BLESSING, 500);
		
		return 0D;
	}
	
	@Override
	public boolean isAttack(){
		return false;
	}
}
