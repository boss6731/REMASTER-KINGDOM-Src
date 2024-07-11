package l1j.server.MJItemSkillSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJItemSkillSystem.Model.MJItemSkillModel;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_ChainSpot;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_Chaotic;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_ChaoticAndNormal;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_CriticalAttack;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_CriticalAttack2;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_DamagePoison;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_DiceDagger;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_Disease;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_HPDrain;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_HPDrainAndGet;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_Hold;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_Legend_Weapon;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_MPDrain;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_MPDrainAndDisease;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_MPDrainAndGet;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_NormalAttack;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_NormalRangeAttack;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_PumpkinCurse;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_Silence;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_TargetWideAttack;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_TurnUndead;
import l1j.server.MJItemSkillSystem.Model.Attack.Atk_WideAttack;
import l1j.server.MJItemSkillSystem.Model.Defence.Def_HpMpRegen;
import l1j.server.MJItemSkillSystem.Model.Defence.Def_HpRegen;
import l1j.server.MJItemSkillSystem.Model.Defence.Def_MpRegen;
import l1j.server.MJItemSkillSystem.Model.Defence.Def_ProbReduction;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.SQLUtil;

public class MJItemSkillModelLoader {
	private static MJItemSkillModelLoader _instance;

	public static MJItemSkillModelLoader getInstance() {
		if (_instance == null)
			_instance = new MJItemSkillModelLoader();
		return _instance;
	}

	public static void reload() {
		MJItemSkillModelLoader tmp = _instance;
		_instance = new MJItemSkillModelLoader();
		if (tmp != null) {
			tmp.clear();
			tmp = null;
		}
	}

	public static void release() {
		if (_instance != null) {
			_instance.clear();
			_instance = null;
		}
	}

	private FastMap<Integer, MJItemSkillModel> _models_atk;
	private FastMap<Integer, MJItemSkillModel> _models_def;

	private MJItemSkillModelLoader() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from tb_itemskill_model");
			rs = pstm.executeQuery();
			int rows = SQLUtil.calcRows(rs);
			_models_atk = new FastMap<Integer, MJItemSkillModel>(rows);
			_models_def = new FastMap<Integer, MJItemSkillModel>(rows);
			while (rs.next()) {
				MJItemSkillModel model = parseType(rs.getString("type"));
				if (model == null)
					continue;

				model.itemId = rs.getInt("item_id");
				model.condition = rs.getInt("condition");
				model.d_prob = rs.getInt("default_prob");
				model.e_prob = rs.getInt("enchant_prob");
				model.s_prob = parseStat(rs.getString("stat_prob"));
				model.s_weight = rs.getInt("stat_weight") * 0.01D;
				model.limit_low_val = rs.getInt("limit_low_val");
				model.limit_high_val = rs.getInt("limit_high_val");
				model.min_val = rs.getInt("min_val");
				model.max_val = rs.getInt("max_val");
				model.stat_val = parseStat(rs.getString("stat_val"));
				model.stat_valweight = rs.getInt("stat_val_weight") * 0.01D;
				model.e_val = rs.getInt("enchant_val");
				model.e_valweight = rs.getInt("enchant_val_weight") * 0.01D;
				model.eff_id = rs.getInt("effect");
				model.pve_eff_id = rs.getInt("PVE_Effect");
				model.Location_count = rs.getInt("Location");
				model.attr_type = parseAttr(rs.getString("attr_type"));
				model.is_magic = rs.getBoolean("is_magic");
				model.is_sp_val = rs.getBoolean("is_sp_val");
				model.sp_val_weight = rs.getInt("sp_val_weight") * 0.01D;
				if (model.isAttack())
					_models_atk.put(model.itemId, model);
				else
					_models_def.put(model.itemId, model);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public MJItemSkillModel getAtk(int i) {
		return _models_atk.get(i);
	}

	public MJItemSkillModel getDef(int i) {
		return _models_def.get(i);
	}

	public void clear() {
		if (_models_atk != null) {
			_models_atk.clear();
			_models_atk = null;
		}

		if (_models_def != null) {
			_models_def.clear();
			_models_def = null;
		}
	}

	private int parseAttr(String type) {
		if (type.equalsIgnoreCase("earth"))
			return L1Skills.ATTR_EARTH;
		else if (type.equalsIgnoreCase("fire"))
			return L1Skills.ATTR_FIRE;
		else if (type.equalsIgnoreCase("water"))
			return L1Skills.ATTR_WATER;
		else if (type.equalsIgnoreCase("wind"))
			return L1Skills.ATTR_WIND;
		return L1Skills.ATTR_NONE;
	}

	private int parseStat(String type) {
		if (type.equalsIgnoreCase("力量"))
			return 1;
		else if (type.equalsIgnoreCase("敏捷"))
			return 2;
		else if (type.equalsIgnoreCase("體質"))
			return 3;
		else if (type.equalsIgnoreCase("精神"))
			return 4;
		else if (type.equalsIgnoreCase("智力"))
			return 5;
		else if (type.equalsIgnoreCase("魅力"))
			return 6;
		return 0;
	}

	private MJItemSkillModel parseType(String type) {
		if (type.equalsIgnoreCase("武器-單一(近距離)"))
			return new Atk_NormalAttack();
		else if (type.equalsIgnoreCase("武器-單一(遠距離)"))
			return new Atk_NormalRangeAttack();
		else if (type.equalsIgnoreCase("武器-廣域(打擊者)"))
			return new Atk_WideAttack();
		else if (type.equalsIgnoreCase("武器-廣域(被擊者)"))
			return new Atk_TargetWideAttack();
		else if (type.equalsIgnoreCase("武器-弱點暴露"))
			return new Atk_ChainSpot();
		else if (type.equalsIgnoreCase("武器-混亂追加打擊(無特效)"))
			return new Atk_Chaotic();
		else if (type.equalsIgnoreCase("武器-混亂追加打擊(附加特效)"))
			return new Atk_ChaoticAndNormal();
		else if (type.equalsIgnoreCase("武器-傷害毒"))
			return new Atk_DamagePoison();
		else if (type.equalsIgnoreCase("武器-不幸的匕首"))
			return new Atk_DiceDagger();
		else if (type.equalsIgnoreCase("武器-疾病"))
			return new Atk_Disease();
		else if (type.equalsIgnoreCase("武器-定身"))
			return new Atk_Hold();
		else if (type.equalsIgnoreCase("武器-體力吸收"))
			return new Atk_HPDrain();
		else if (type.equalsIgnoreCase("武器-體力吸收(加重傷害)"))
			return new Atk_HPDrainAndGet();
		else if (type.equalsIgnoreCase("武器-魔力吸收"))
			return new Atk_MPDrain();
		else if (type.equalsIgnoreCase("武器-魔力吸收(加重傷害)"))
			return new Atk_MPDrainAndGet();
		else if (type.equalsIgnoreCase("武器-魔力吸收(疾病)"))
			return new Atk_MPDrainAndDisease();
		else if (type.equalsIgnoreCase("武器-南瓜詛咒"))
			return new Atk_PumpkinCurse();
		else if (type.equalsIgnoreCase("武器-沉默"))
			return new Atk_Silence();
		else if (type.equalsIgnoreCase("武器-驅散不死族"))
			return new Atk_TurnUndead();
		else if (type.equalsIgnoreCase("武器-機率致命打擊"))
			return new Atk_CriticalAttack();
		else if (type.equalsIgnoreCase("武器-機率致命打擊(劍鬼)"))
			return new Atk_CriticalAttack2();
		else if (type.equalsIgnoreCase("防具-體力恢復"))
			return new Def_HpRegen();
		else if (type.equalsIgnoreCase("防具-魔力恢復"))
			return new Def_MpRegen();
		else if (type.equalsIgnoreCase("防具-機率減少傷害"))
			return new Def_ProbReduction();
		else if (type.equalsIgnoreCase("武器-傳說武器"))
			return new Atk_Legend_Weapon();
		else if (type.equalsIgnoreCase("防具-體力魔力恢復"))
			return new Def_HpMpRegen();
		return null;
	}
}