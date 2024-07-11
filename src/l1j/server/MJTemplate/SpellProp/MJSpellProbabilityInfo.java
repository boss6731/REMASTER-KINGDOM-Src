package l1j.server.MJTemplate.SpellProp;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.Config;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJSpellProbabilityInfo {
	static MJSpellProbabilityInfo newInstance(ResultSet rs) throws SQLException {
		MJSpellProbabilityInfo pInfo = newInstance();
		pInfo._skill_id = rs.getInt("skill_id");
		pInfo._description = rs.getString("description");
		pInfo._skill_type = rs.getString("skill_type");
		pInfo._pierce_lv_weight = Double.parseDouble(rs.getString("pierce_lv_weight"));
		pInfo._resis_lv_weight = Double.parseDouble(rs.getString("resis_lv_weight"));
		pInfo._int_weight = Double.parseDouble(rs.getString("int_weight"));
		pInfo._mr_weight = Double.parseDouble(rs.getString("mr_weight"));
		pInfo._pierce_weight = Double.parseDouble(rs.getString("pierce_weight"));
		pInfo._resis_weight = Double.parseDouble(rs.getString("resis_weight"));
		pInfo._default_probability = rs.getInt("default_probability");
		pInfo._min_probability = rs.getInt("min_probability");
		pInfo._max_probability = rs.getInt("max_probability");
		pInfo.boss_effectable = rs.getString("boss_not_effectable").equalsIgnoreCase("true") ? true : false;
		pInfo._is_loggin = rs.getBoolean("is_loggin");
		return pInfo;
	}
	
	private static MJSpellProbabilityInfo newInstance() {
		return new MJSpellProbabilityInfo();
	}
	
	private boolean _is_loggin;
	private int _skill_id;
	private String _description;
	private String _skill_type;
	private double _pierce_lv_weight;
	private double _resis_lv_weight;
	private double _int_weight;
	private double _mr_weight;
	private double _pierce_weight;
	private double _resis_weight;
	private int _default_probability;
	private int _min_probability;
	private int _max_probability;
	private boolean boss_effectable;
	
	private MJSpellProbabilityInfo() {
	}

	int get_skill_id() {
		return _skill_id;
	}
	
	String get_description() {
		return _description;
	}
	
	String get_skill_type() {
		return _skill_type;
	}
	
	double get_pierce_lv_weight() {
		return _pierce_lv_weight;
	}
	
	double get_resis_lv_weight() {
		return _resis_lv_weight;
	}
	
	double get_int_weight() {
		return _int_weight;
	}
	
	double get_mr_weight() {
		return _mr_weight;
	}
	
	double get_pierce_weight() {
		return _pierce_weight;
	}
	
	double get_resis_weight() {
		return _resis_weight;
	}
	
	int get_default_probability() {
		return _default_probability;
	}
	
	int get_min_probability() {
		return _min_probability;
	}
	
	int get_max_probability() {
		return _max_probability;
	}
	
	void print() {
	}
	
	int calc_probability(L1PcInstance pc, L1Character target, int attacker_int, int target_mr) {
		int probability 	= 0;
		int attackLevel 	= pc.getLevel();
		int defenseLevel 	= target.getLevel();
		int PiercePoint		= 0;
		int ResisPoint		= 0;
		
		if (get_skill_type().equalsIgnoreCase("MAGICHIT")) {
			PiercePoint = (int) ((attacker_int * _int_weight) + (pc.getTotalMagicHitup() * _pierce_weight));
			ResisPoint	= (int) (target_mr * _mr_weight);
		} else if (get_skill_type().equalsIgnoreCase("ABILITY")) {
			PiercePoint = (int) ((pc.getSpecialPierce(eKind.ABILITY) + pc.getSpecialPierce(eKind.ALL)) * _pierce_weight);
			ResisPoint = (int) ((target.getSpecialResistance(eKind.ABILITY) + target.getSpecialResistance(eKind.ALL)) * _resis_weight);
		} else if (get_skill_type().equalsIgnoreCase("SPIRIT")) {
			PiercePoint = (int) ((pc.getSpecialPierce(eKind.SPIRIT) + pc.getSpecialPierce(eKind.ALL)) * _pierce_weight);
			ResisPoint = (int) ((target.getSpecialResistance(eKind.SPIRIT) + target.getSpecialResistance(eKind.ALL)) * _resis_weight);
		} else if (get_skill_type().equalsIgnoreCase("DRAGONSPELL")) {
			PiercePoint = (int) ((pc.getSpecialPierce(eKind.DRAGON_SPELL) + pc.getSpecialPierce(eKind.ALL)) * _pierce_weight);
			ResisPoint = (int) ((target.getSpecialResistance(eKind.DRAGON_SPELL) + target.getSpecialResistance(eKind.ALL)) * _resis_weight);
		} else if (get_skill_type().equalsIgnoreCase("FEAR")) {
			PiercePoint = (int) ((pc.getSpecialPierce(eKind.FEAR) + pc.getSpecialPierce(eKind.ALL)) * _pierce_weight);
			ResisPoint = (int) ((target.getSpecialResistance(eKind.FEAR) + target.getSpecialResistance(eKind.ALL)) * _resis_weight);
		}

		if (attackLevel >= defenseLevel) {
			probability = (int) (((attackLevel - defenseLevel) * _pierce_lv_weight) + (PiercePoint - ResisPoint)) + _default_probability;
		} else if (attackLevel < defenseLevel) {
			probability = (int) (((attackLevel - defenseLevel) * _resis_lv_weight) + (PiercePoint - ResisPoint)) + _default_probability;
		}

		probability = Math.max(probability, _min_probability);
		probability = Math.min(probability, _max_probability);

		switch (_skill_id) {
		case ARMOR_BRAKE:
			if (pc.isPassive(MJPassiveID.ARMOR_BREAK_DESTINY.toInt())) {
				int lvl = pc.getLevel();
				if (lvl >= 85)
					probability += ((lvl - 84) * Config.MagicAdSetting_DarkElf.ARMOR_BREAK_DESTINY_LV);
			}
			break;
			case COUNTER_BARRIER: // 反擊障壁
				int probability_veteran = Config.MagicAdSetting_Knight.COUNTER_VETERAN; // 反擊障壁：老手
				if (pc != null && pc.isPassive(MJPassiveID.COUNTER_BARRIER_VETERAN.toInt())) { // 檢查角色是否擁有反擊障壁老手的被動技能
					int lvl = pc.getLevel(); // 獲取角色等級
					if (lvl >= 85) { // 若角色等級大於等於85，應用伺服器設置
						probability += lvl - 84; // 增加機率
					}
// 下面的代碼被註釋掉了，如果需要的話可以根據需要啟用
// if (lvl >= Config.MagicAdSetting_Knight.COUNTER_VETERAN_LV1 && lvl <= Config.MagicAdSetting_Knight.COUNTER_VETERAN_LV2) {
//     probability += probability_veteran += (lvl - Config.MagicAdSetting_Knight.COUNTER_VETERAN_LV);
// } else if (lvl >= Config.MagicAdSetting_Knight.COUNTER_VETERAN_LV3 && lvl <= Config.MagicAdSetting_Knight.COUNTER_VETERAN_LV4) {
//     probability += probability_veteran += (lvl - Config.MagicAdSetting_Knight.COUNTER_VETERAN_LV5);
// }
				}
				break;
			break;
		case SHAPE_CHANGE:
			if (target == pc) {
				probability = 100;
			}
			break;
		case CANCELLATION:
			if (!pc.isWizard()) {
				probability *= 0.5;
			}
			break;
		case STRIKER_GALE: // 스트라이커 게일
			if (pc != null && pc.isPassive(MJPassiveID.STRIKER_GAIL_SHOT.toInt())) {
				probability += 10;
			}
			break;
		}
		
		if(target instanceof L1MonsterInstance){
			L1MonsterInstance mon = (L1MonsterInstance) target;
			if(mon.getNpcTemplate().isboss() && !boss_effectable){
				probability = 0;
			}
		}

		if (_is_loggin) { // 檢查是否正在記錄日誌
			if (pc.isGm()) { // 如果角色是 GM（遊戲管理員）
				System.out.println(String.format("技能 : %s(%d,%d~%d) 攻擊者: %s(%d), 目標: %s(%d), 機率: %d",
						_description, _skill_id, _min_probability, _max_probability,
						pc.getName(), PiercePoint, target.getName(), ResisPoint, probability));
				System.out.println(String.format("-> 權重信息... pierce: %d, resis: %d",
						PiercePoint, ResisPoint));
			}
		}
		return probability;

	}
