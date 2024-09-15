package l1j.server.server.templates;

import java.util.HashMap;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;

public class L1ArmorSets {

	public L1ArmorSets() {	}

	private int _id;

	public int getId() { 
		return _id;
		}
	public void setId(int i) {
		_id = i;
		}

	//-- 添加
	private int main_id;
	public int get_main_id() {
		return main_id;
	}
	public void set_main_id(int i) {
		this.main_id = i;
	}
	
	private String _sets;
	public String getSets() {  
		return _sets; 
		}
	public void setSets(String s) {
		_sets = s; 
		}

	private int _polyId;
	public int getPolyId() { 
		return _polyId; 
		}
	public void setPolyId(int i) {
		_polyId = i;  
		}

	private int _poly_desc;
	public int getPolyDesc() {
		return _poly_desc;  
		}
	public void setPolyDesc(int i) { 
		_poly_desc = i;
 }
	
	private int _ac;
	public int getAc() { 
		return _ac; 
		}
	public void setAc(int i) {
		_ac = i; 
		}

	private int _hp;
	public int getHp() { 
		return _hp; 
		}
	public void setHp(int i) {
		_hp = i; 
		}

	private int _mp;
	public int getMp() {
		return _mp;  
		}
	public void setMp(int i) {
		_mp = i;  
		}

	private int _hpr;
	public int getHpr() { 
		return _hpr; 
		}
	public void setHpr(int i) {
		_hpr = i; 
		}

	private int _mpr;
	public int getMpr() { 
		return _mpr; 
		}
	public void setMpr(int i) {
		_mpr = i;  
		}

	private int _mr;
	public int getMr() { 
		return _mr; 
		}
	public void setMr(int i) {
		_mr = i; 
		}

	private int _str;
	public int getStr() { 
		return _str;  
		}
	public void setStr(int i) { 
		_str = i; 
		}

	private int _dex;
	public int getDex() {
		return _dex;
		}
	public void setDex(int i) { 
		_dex = i; 
		}

	private int _con;
	public int getCon() { 
		return _con;  
		}
	public void setCon(int i) { 
		_con = i; 
		}

	private int _wis;
	public int getWis() { 
		return _wis; 
		}
	public void setWis(int i) { 
		_wis = i;
		}

	private int _cha;
	public int getCha() {  return _cha;  }
	public void setCha(int i) {  _cha = i;  }

	private int _intl;
	public int getIntl() {  return _intl;  }
	public void setIntl(int i) {  _intl = i;  }

	private int _defense_water = 0;
	public int get_defense_water() {  return _defense_water;  }
	public void set_defense_water(int i) {  _defense_water = i;  }
	
	private int _defense_earth = 0;
	public int get_defense_earth() {  return _defense_earth;  }
	public void set_defense_earth(int i) {  _defense_earth = i;  }

	private int _defense_wind = 0;
	public int get_defense_wind() {  return _defense_wind;  }
	public void set_defense_wind(int i) {  _defense_wind = i;  }

	private int _defense_fire = 0;
	public int get_defense_fire() {  return _defense_fire;  }
	public void set_defense_fire(int i) {  _defense_fire = i;  }
	
	private int _defense_all = 0;
	public int get_defense_all() {	return this._defense_all;	}
	public void set_defense_all(int i) {	_defense_all = i;	}
	
	private int _sp = 0;
	public void set_sp(int sp) {
		_sp = sp;
	}
	public int get_sp() {
		return _sp;
	}
	private int _melee_damage = 0;
	public void set_melee_damage(int melee_damage) {
		_melee_damage = melee_damage;
	}
	public int get_melee_damage() {
		return _melee_damage;
	}
	private int _melee_hit = 0;
	public void set_melee_hit(int melee_hit) {
		_melee_hit = melee_hit;
	}
	public int get_melee_hit() {
		return _melee_hit;
	}
	private int _missile_damage = 0;
	public void set_missile_damage(int missile_damage) {
		_missile_damage = missile_damage;
	}
	public int get_missile_damage() {
		return _missile_damage;
	}
	private int _missile_hit = 0;
	public void set_missile_hit(int missile_hit) {
		_missile_hit = missile_hit;
	}
	public int get_missile_hit() {
		return _missile_hit;
	}

	private int m_magicHitup = 0;
	public int getMagicHitup(){
		return m_magicHitup;
	}
	public void setMagicHitup(int i){
		m_magicHitup = i;
	}
	
	private int _regist_calcPcDefense = 0;
	public int get_regist_calcPcDefense() {	
		return _regist_calcPcDefense;	
		}
	public void set_regist_calcPcDefense(int i) {
		_regist_calcPcDefense = i;
		}
	
	private int _regist_PVPweaponTotalDamage = 0;
	public int get_regist_PVPweaponTotalDamage() {
		return _regist_PVPweaponTotalDamage;	
		}
	public void set_regist_PVPweaponTotalDamage(int i) {
		_regist_PVPweaponTotalDamage = i;	
		}
	
	private int _technique;
	private int _spirit;
	private int _dragonlang;
	private int _fear;
	private int _all_tolerance;
	private int _techniquehit;
	private int _spirithit;
	private int _dragonlanghit;
	private int _fearhit;
	private int _allhit;
	
	public int getTechniqueTolerance() {
		return this._technique;
	}

	public void setTechniqueTolerance(int i) {
		this._technique = i;
	}

	public int getSpiritTolerance() {
		return this._spirit;
	}

	public void setSpiritTolerance(int i) {
		this._spirit = i;
	}

	public int getDragonLangTolerance() {
		return this._dragonlang;
	}

	public void setDragonLangTolerance(int i) {
		this._dragonlang = i;
	}

	public int getFearTolerance() {
		return this._fear;
	}

	public void setFearTolerance(int i) {
		this._fear = i;
	}

	public int getAllTolerance() {
		return this._all_tolerance;
	}

	public void setAllTolerance(int i) {
		this._all_tolerance = i;
	}

	public int getTechniqueHit() {
		return this._techniquehit;
	}

	public void setTechniqueHit(int i) {
		this._techniquehit = i;
	}

	public int getSpiritHit() {
		return this._spirithit;
	}

	public void setSpiritHit(int i) {
		this._spirithit = i;
	}

	public int getDragonLangHit() {
		return this._dragonlanghit;
	}

	public void setDragonLangHit(int i) {
		this._dragonlanghit = i;
	}

	public int getFearHit() {
		return this._fearhit;
	}

	public void setFearHit(int i) {
		this._fearhit = i;
	}

	public int getAllHit() {
		return this._allhit;
	}

	public void setAllHit(int i) {
		this._allhit = i;
	}
	

	private int _weightReduction = 0;
	public int getWeightReduction() {
		return _weightReduction;
		}
	public void setWeightReduction(int i) {
		_weightReduction = i;
		}
	

	
	
}