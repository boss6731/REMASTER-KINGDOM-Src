 package l1j.server.server.templates;public class L1ArmorSets { private int _id; private int main_id; private String _sets; private int _polyId; private int _poly_desc; private int _ac; private int _hp;
   private int _mp;
   private int _hpr;
   private int _mpr;
   private int _mr;
   private int _str;
   private int _dex;
   private int _con;
   private int _wis;
   private int _cha;
   private int _intl;

   public int getId() {
     return this._id;
   }
   public void setId(int i) {
     this._id = i;
   }



   public int get_main_id() {
     return this.main_id;
   }
   public void set_main_id(int i) {
     this.main_id = i;
   }


   public String getSets() {
     return this._sets;
   }
   public void setSets(String s) {
     this._sets = s;
   }


   public int getPolyId() {
     return this._polyId;
   }
   public void setPolyId(int i) {
     this._polyId = i;
   }


   public int getPolyDesc() {
     return this._poly_desc;
   }
   public void setPolyDesc(int i) {
     this._poly_desc = i;
   }


   public int getAc() {
     return this._ac;
   }
   public void setAc(int i) {
     this._ac = i;
   }


   public int getHp() {
     return this._hp;
   }
   public void setHp(int i) {
     this._hp = i;
   }


   public int getMp() {
     return this._mp;
   }
   public void setMp(int i) {
     this._mp = i;
   }


   public int getHpr() {
     return this._hpr;
   }
   public void setHpr(int i) {
     this._hpr = i;
   }


   public int getMpr() {
     return this._mpr;
   }
   public void setMpr(int i) {
     this._mpr = i;
   }


   public int getMr() {
     return this._mr;
   }
   public void setMr(int i) {
     this._mr = i;
   }


   public int getStr() {
     return this._str;
   }
   public void setStr(int i) {
     this._str = i;
   }


   public int getDex() {
     return this._dex;
   }
   public void setDex(int i) {
     this._dex = i;
   }


   public int getCon() {
     return this._con;
   }
   public void setCon(int i) {
     this._con = i;
   }


   public int getWis() {
     return this._wis;
   }
   public void setWis(int i) {
     this._wis = i;
   }

   public int getCha() {
     return this._cha; } public void setCha(int i) {
     this._cha = i;
   }

   public int getIntl() { return this._intl; } public void setIntl(int i) {
     this._intl = i;
   }
   private int _defense_water = 0;
   public int get_defense_water() { return this._defense_water; } public void set_defense_water(int i) {
     this._defense_water = i;
   }
   private int _defense_earth = 0;
   public int get_defense_earth() { return this._defense_earth; } public void set_defense_earth(int i) {
     this._defense_earth = i;
   }
   private int _defense_wind = 0;
   public int get_defense_wind() { return this._defense_wind; } public void set_defense_wind(int i) {
     this._defense_wind = i;
   }
   private int _defense_fire = 0;
   public int get_defense_fire() { return this._defense_fire; } public void set_defense_fire(int i) {
     this._defense_fire = i;
   }
   private int _defense_all = 0;
   public int get_defense_all() { return this._defense_all; } public void set_defense_all(int i) {
     this._defense_all = i;
   }
   private int _sp = 0;
   public void set_sp(int sp) {
     this._sp = sp;
   }
   public int get_sp() {
     return this._sp;
   }
   private int _melee_damage = 0;
   public void set_melee_damage(int melee_damage) {
     this._melee_damage = melee_damage;
   }
   public int get_melee_damage() {
     return this._melee_damage;
   }
   private int _melee_hit = 0;
   public void set_melee_hit(int melee_hit) {
     this._melee_hit = melee_hit;
   }
   public int get_melee_hit() {
     return this._melee_hit;
   }
   private int _missile_damage = 0;
   public void set_missile_damage(int missile_damage) {
     this._missile_damage = missile_damage;
   }
   public int get_missile_damage() {
     return this._missile_damage;
   }
   private int _missile_hit = 0;
   public void set_missile_hit(int missile_hit) {
     this._missile_hit = missile_hit;
   }
   public int get_missile_hit() {
     return this._missile_hit;
   }

   private int m_magicHitup = 0;
   public int getMagicHitup() {
     return this.m_magicHitup;
   }
   public void setMagicHitup(int i) {
     this.m_magicHitup = i;
   }

   private int _regist_calcPcDefense = 0;
   public int get_regist_calcPcDefense() {
     return this._regist_calcPcDefense;
   }
   public void set_regist_calcPcDefense(int i) {
     this._regist_calcPcDefense = i;
   }
   private int _technique; private int _spirit; private int _dragonlang; private int _fear; private int _all_tolerance;
   private int _regist_PVPweaponTotalDamage = 0; private int _techniquehit; private int _spirithit; private int _dragonlanghit; private int _fearhit; private int _allhit;
   public int get_regist_PVPweaponTotalDamage() {
     return this._regist_PVPweaponTotalDamage;
   }
   public void set_regist_PVPweaponTotalDamage(int i) {
     this._regist_PVPweaponTotalDamage = i;
   }


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
     return this._weightReduction;
   }
   public void setWeightReduction(int i) {
     this._weightReduction = i;
   } }


