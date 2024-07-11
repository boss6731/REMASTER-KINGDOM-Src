package l1j.server.MJItemSkillSystem.Model;

import java.util.Random;

import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.MJCommons;

/**********************************
 * 
 * MJ Item Skill Model. made by mjsoft, 2017.
 * 
 **********************************/
public abstract class MJItemSkillModel {
	protected static Random _rnd = new Random(System.nanoTime());

	public int itemId; // item id.
	public int condition; // condition..
	public int d_prob; // default probability.
	public int e_prob; // enchant probability.
	public int s_prob; // stat probability.
	public double s_weight; // stat probability in weight.
	public int limit_low_val; //
	public int limit_high_val; //
	public int min_val; // min value
	public int max_val; // max value
	public int stat_val; // stat value
	public double stat_valweight; // stat value weight.
	public int e_val; // enchant value.
	public double e_valweight; // enchant value weight.
	public int eff_id; // effect id
	public int pve_eff_id; // pve_effect id
	public int Location_count; // Location
	public int attr_type; // attribute type
	public boolean is_magic; // is magic skill?
	public boolean is_sp_val;
	public double sp_val_weight;

	/**
	 * t_item is traget item instance. dwd is default weapon damage.
	 **/
	public abstract double get(L1Character attacker, L1Character defender, L1ItemInstance t_item, double dwd);

	protected boolean isInPercent(int p) {
		return (_rnd.nextInt(100) + 1) <= p;
	}

	protected int getProbability(L1Character c, L1ItemInstance t_item) {
		int prob = d_prob;
		if (condition <= 0) {
			prob += e_prob * t_item.getEnchantLevel();
		} else if ((e_prob > 0) && (t_item.getEnchantLevel() >= condition)) {
			int ptmp = t_item.getEnchantLevel() - condition + 1;
			prob += ptmp * e_prob;
		}
		if (s_prob != 0) {
			int stat = 0;
			switch (s_prob) {
			case 1:
				stat = c.getAbility().getTotalStr();
				break;
			case 2:
				stat = c.getAbility().getTotalDex();
				break;
			case 3:
				stat = c.getAbility().getTotalCon();
				break;
			case 4:
				stat = c.getAbility().getTotalWis();
				break;
			case 5:
				stat = c.getAbility().getTotalInt();
				break;
			case 6:
				stat = c.getAbility().getTotalCha();
				break;
			}
			prob = (int) (prob + prob * (stat * s_weight));
		}
		
		if (c instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) c;
			if (pc.get_acurucy_meister() != 0) {
				prob += pc.get_acurucy_meister();
				pc.set_acurucy_meister(0);
			}
		}
		
		if (c instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) c;
			AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
			if (Info != null) {
				if (t_item.getItem().getType2() == 1) {
					if (Info.get_invoke_val_1() > 0)
						prob += Info.get_invoke_val_1() * 0.01;
				} else if (t_item.getItem().getType2() == 2) {
					if (Info.get_invoke_val_2()+pc.get_armor_magic_pro() > 0)
						prob += (Info.get_invoke_val_2() + pc.get_armor_magic_pro()) * 0.01;
				}
			}
		}
		
		return prob;
	}

	protected boolean isPlay(L1Character c, L1ItemInstance t_item) {
		return condition <= t_item.getEnchantLevel() && isInPercent(getProbability(c, t_item));
	}

	protected double calcSp(L1Character c, double pure_dmg) {
		double dmg = pure_dmg;
		if (is_sp_val && sp_val_weight != 0) {
			int sp = c.getAbility().getSp();
			dmg += (sp * sp_val_weight);
			// dmg += (dmg * (sp * sp_val_weight));
		}
		return dmg;
	}

	protected double calcStat(L1Character c, double pure_dmg) {
		double dmg = pure_dmg;
		if (stat_val != 0) {
			int stat = 0;
			switch (stat_val) {
			case 1:
				stat = c.getAbility().getTotalStr();
				break;
			case 2:
				stat = c.getAbility().getTotalDex();
				break;
			case 3:
				stat = c.getAbility().getTotalCon();
				break;
			case 4:
				stat = c.getAbility().getTotalWis();
				break;
			case 5:
				stat = c.getAbility().getTotalInt();
				break;
			case 6:
				stat = c.getAbility().getTotalCha();
				break;
			}
			// dmg += (dmg * (stat * stat_valweight));
			dmg += (stat * stat_valweight);
		}

		return dmg;
	}

	protected double calcEnchant(L1ItemInstance item, double pure_dmg) {
		double dmg = pure_dmg;
		if (e_val > 0) {
			int ech = item.getEnchantLevel();
			if (ech > e_val) {
				ech -= e_val + 1;

				dmg += (ech * e_valweight);
				// dmg += (dmg * (ech * e_valweight));
			}
		}
		return dmg;
	}

	protected double calcAttr(L1Character defender, double pure_dmg) {
		double dmg = pure_dmg;
		if (defender.getResistance() != null) {
			switch (attr_type) {
			case L1Skills.ATTR_EARTH:
				dmg = MJCommons.getAttrDamage(dmg, defender.getResistance().getEarth());
				break;
			case L1Skills.ATTR_FIRE:
				dmg = MJCommons.getAttrDamage(dmg, defender.getResistance().getFire());
				break;
			case L1Skills.ATTR_WATER:
				dmg = MJCommons.getAttrDamage(dmg, defender.getResistance().getWater());
				break;
			case L1Skills.ATTR_WIND:
				dmg = MJCommons.getAttrDamage(dmg, defender.getResistance().getWind());
				break;
			}
		}

		return dmg;
	}

	protected void broadcast(L1Character c, ServerBasePacket pck) {
		c.sendPackets(pck, false);
		c.broadcastPacket(pck);
	}

	protected void sendPackets(L1Character c, ServerBasePacket pck) {
		c.sendPackets(pck, false);
		c.broadcastPacket(pck);
	}

	protected boolean isCounterMagic(L1Character c) {
		if (c.hasSkillEffect(L1SkillId.COUNTER_MAGIC)) {
			c.removeSkillEffect(L1SkillId.COUNTER_MAGIC);
			broadcast(c, new S_SkillSound(c.getId(), 10702));
			return true;
		}
		return false;
	}

	public boolean isAttack() {
		return true;
	}
}
