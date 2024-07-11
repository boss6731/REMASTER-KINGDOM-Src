 package l1j.server.server.templates;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;

 public abstract class L1Item implements Serializable {
   private static final long serialVersionUID = 1L;
   private int _type2;
   private int _itemId;
   private String _name;
   private String _name_view;
   private String _Magic_name;
   private String _nameId;
   private int _type;
   private int _type1;
   private int _material;
   private int _weight;
   private int _gfxId;
   private int _groundGfxId;
   private int _minLevel;
   private int _itemDescId;
   private int _maxLevel;
   private int _bless;
   private boolean _tradable;
   private boolean _cantDelete;
   private boolean _save_at_once;

   public int getType2() {
     return this._type2;
   }

   public void setType2(int type) {
     this._type2 = type;
   }



   public int getItemId() {
     return this._itemId;
   }

   public void setItemId(int itemId) {
     this._itemId = itemId;
   }



   public String getName() {
     return this._name;
   }

   public void setName(String name) {
     this._name = name;
   }



   public String getNameView() {
     return this._name_view;
   }

   public void setNameView(String name_view) {
     this._name_view = name_view;
   }



   public String getMagicName() {
     return this._Magic_name;
   }

   public void setMagicName(String name) {
     this._Magic_name = name;
   }



   public String getNameId() {
     return this._nameId;
   }
   public int getItemNameIdInt() {
     String namestr = this._nameId.replaceAll("[^0-9]", "");
     int nameint = Integer.parseInt(namestr);
     return nameint;
   }

   public void setNameId(String nameid) {
     this._nameId = nameid;
   }

























   public int getType() {
     return this._type;
   }

   public void setType(int type) {
     this._type = type;
   }















   public int getType1() {
     return this._type1;
   }

   public void setType1(int type1) {
     this._type1 = type1;
   }









   public int getMaterial() {
     return this._material;
   }

   public void setMaterial(int material) {
     this._material = material;
   }



   public int getWeight() {
     return this._weight;
   }

   public void setWeight(int weight) {
     this._weight = weight;
   }



   public int getGfxId() {
     return this._gfxId;
   }

   public void setGfxId(int gfxId) {
     this._gfxId = gfxId;
   }



   public int getGroundGfxId() {
     return this._groundGfxId;
   }

   public void setGroundGfxId(int groundGfxId) {
     this._groundGfxId = groundGfxId;
   }








   public int getItemDescId() {
     return this._itemDescId;
   }



   public void setItemDescId(int descId) {
     this._itemDescId = descId;
   }

   public int getMinLevel() {
     return this._minLevel;
   }

   public void setMinLevel(int level) {
     this._minLevel = level;
   }



   public int getMaxLevel() {
     return this._maxLevel;
   }

   public void setMaxLevel(int maxlvl) {
     this._maxLevel = maxlvl;
   }



   public int getBless() {
     return this._bless;
   }

   public void setBless(int i) {
     this._bless = i;
   }



   public boolean isTradable() {
     return this._tradable;
   }

   public void setTradable(boolean flag) {
     this._tradable = flag;
   }



   public boolean isCantDelete() {
     return this._cantDelete;
   }

   public void setCantDelete(boolean flag) {
     this._cantDelete = flag;
   }






   public boolean isToBeSavedAtOnce() {
     return this._save_at_once;
   }

   public void setToBeSavedAtOnce(boolean flag) {
     this._save_at_once = flag;
   }



   private int _dmgSmall = 0;

   public int getDmgSmall() {
     return this._dmgSmall;
   }

   public void setDmgSmall(int dmgSmall) {
     this._dmgSmall = dmgSmall;
   }

   private int _dmgLarge = 0;

   public int getDmgLarge() {
     return this._dmgLarge;
   }

   public void setDmgLarge(int dmgLarge) {
     this._dmgLarge = dmgLarge;
   }





   private int _safeEnchant = 0;

   public int get_safeenchant() {
     return this._safeEnchant;
   }

   public void set_safeenchant(int safeenchant) {
     this._safeEnchant = safeenchant;
   }

   private boolean _useRoyal = false;

   public boolean isUseRoyal() {
     return this._useRoyal;
   }

   public void setUseRoyal(boolean flag) {
     this._useRoyal = flag;
   }

   private boolean _useKnight = false;

   public boolean isUseKnight() {
     return this._useKnight;
   }

   public void setUseKnight(boolean flag) {
     this._useKnight = flag;
   }

   private boolean _useElf = false;

   public boolean isUseElf() {
     return this._useElf;
   }

   public void setUseElf(boolean flag) {
     this._useElf = flag;
   }

   private boolean _useMage = false;

   public boolean isUseMage() {
     return this._useMage;
   }

   public void setUseMage(boolean flag) {
     this._useMage = flag;
   }

   private boolean _useDarkelf = false;

   public boolean isUseDarkelf() {
     return this._useDarkelf;
   }

   public void setUseDarkelf(boolean flag) {
     this._useDarkelf = flag;
   }

   private boolean _useDragonKnight = false;

   public boolean isUseDragonKnight() {
     return this._useDragonKnight;
   }

   public void setUseDragonKnight(boolean flag) {
     this._useDragonKnight = flag;
   }

   private boolean _useBlackwizard = false;

   public boolean isUseBlackwizard() {
     return this._useBlackwizard;
   }

   private boolean _use전사 = false;

   public boolean isUse전사() {
     return this._use전사;
   }

   public void setUse전사(boolean flag) {
     this._use전사 = flag;
   }

   private boolean _useFencer = false;

   public boolean isUseFencer() {
     return this._useFencer;
   }

   public void setUseFencer(boolean flag) {
     this._useFencer = flag;
   }

   private boolean _useLancer = false;

   public boolean isUseLancer() {
     return this._useLancer;
   }

   public void setUseLancer(boolean flag) {
     this._useLancer = flag;
   }

   private boolean _useHighPet = false;

   public boolean isUseHighPet() {
     return this._useHighPet;
   }

   public void setUseHighPet(boolean flag) {
     this._useHighPet = flag;
   }

   public void setUseBlackwizard(boolean flag) {
     this._useBlackwizard = flag;
   }

   private byte _addstr = 0;

   public byte get_addstr() {
     return this._addstr;
   }

   public void set_addstr(byte addstr) {
     this._addstr = addstr;
   }

   private byte _adddex = 0;

   public byte get_adddex() {
     return this._adddex;
   }

   public void set_adddex(byte adddex) {
     this._adddex = adddex;
   }

   private byte _addcon = 0;

   public byte get_addcon() {
     return this._addcon;
   }

   public void set_addcon(byte addcon) {
     this._addcon = addcon;
   }

   private int _addexp = 0;

   public int getAddExp() {
     return this._addexp;
   }

   public void setAddExp(int addexp) {
     this._addexp = addexp;
   }

   private byte _addint = 0;

   public byte get_addint() {
     return this._addint;
   }

   public void set_addint(byte addint) {
     this._addint = addint;
   }

   private byte _addwis = 0;

   public byte get_addwis() {
     return this._addwis;
   }

   public void set_addwis(byte addwis) {
     this._addwis = addwis;
   }

   private byte _addcha = 0;

   public byte get_addcha() {
     return this._addcha;
   }

   public void set_addcha(byte addcha) {
     this._addcha = addcha;
   }

   private int _addhp = 0;

   public int get_addhp() {
     return this._addhp;
   }

   public void set_addhp(int addhp) {
     this._addhp = addhp;
   }

   private int _addmp = 0;

   public int get_addmp() {
     return this._addmp;
   }

   public void set_addmp(int addmp) {
     this._addmp = addmp;
   }

   private int _addhpr = 0;

   public int get_addhpr() {
     return this._addhpr;
   }

   public void set_addhpr(int addhpr) {
     this._addhpr = addhpr;
   }

   private int _addmpr = 0;

   public int get_addmpr() {
     return this._addmpr;
   }

   public void set_addmpr(int addmpr) {
     this._addmpr = addmpr;
   }

   private int _addsp = 0;

   public int get_addsp() {
     return this._addsp;
   }

   public void set_addsp(int addsp) {
     this._addsp = addsp;
   }


   private int _addeinhasadper = 0;

   public int get_addeinhasadper() {
     return this._addeinhasadper;
   }

   public void set_addeinhasadper(int einhasad) {
     this._addeinhasadper = einhasad;
   }



   private int _mdef = 0;

   public int get_mdef() {
     return this._mdef;
   }

   public void set_mdef(int i) {
     this._mdef = i;
   }

   private boolean _isHasteItem = false;

   public boolean isHasteItem() {
     return this._isHasteItem;
   }

   public void setHasteItem(boolean flag) {
     this._isHasteItem = flag;
   }

   private int _maxUseTime = 0; private int _useType; private int _foodVolume;

   public int getMaxUseTime() {
     return this._maxUseTime;
   }

   public void setMaxUseTime(int i) {
     this._maxUseTime = i;
   }






   public int getUseType() {
     return this._useType;
   }

   public void setUseType(int useType) {
     this._useType = useType;
   }






   public int getFoodVolume() {
     return this._foodVolume;
   }

   public void setFoodVolume(int volume) {
     this._foodVolume = volume;
   }




   public int getLightRange() {
     if (this._itemId == 40001)
       return 11;
     if (this._itemId == 7005)
       return 14;
     if (this._itemId == 40002)
       return 14;
     if (this._itemId == 40004)
       return 14;
     if (this._itemId == 40005) {
       return 8;
     }
     return 0;
   }


   public int getLightFuel() {
     if (this._itemId == 40001)
       return 600;
     if (this._itemId == 7005)
       return 600;
     if (this._itemId == 40002)
       return 0;
     if (this._itemId == 40003)
       return 600;
     if (this._itemId == 40004)
       return 0;
     if (this._itemId == 40005) {
       return 600;
     }
     return 0;
   }



   public boolean isStackable() {
     return false;
   }

   public int get_locx() {
     return 0;
   }

   public int get_locy() {
     return 0;
   }

   public short get_mapid() {
     return 0;
   }

   public int get_delayid() {
     return 0;
   }

   public int get_delaytime() {
     return 0;
   }

   public int getMaxChargeCount() {
     return 0;
   }

   public int get_delayEffect() {
     return 0;
   }



   private int _hitModifier = 0;

   public int getHitModifier() {
     return this._hitModifier;
   }

   public void setHitModifier(int i) {
     this._hitModifier = i;
   }

   private int _dmgModifier = 0;

   public int getDmgModifier() {
     return this._dmgModifier;
   }

   public void setDmgModifier(int i) {
     this._dmgModifier = i;
   }

   private int _magicDmgModifier = 0; private int _grade; private int _price; private int damage_reduction; private boolean _isEndedTimeMessage; private int _missile_critical_probability; private int _melee_critical_probability; private int _magic_critical_probability; private int _reduc_cancel; private int _WeaponReductionCancel; private int _titan_percent; private int _use_effectid; private int _overlay_surf_id; private int _PVPWeaponReductionCancel; private int _PVPMagicReduction;

   public int getMagicDmgModifier() {
     return this._magicDmgModifier;
   }
   private int _PVPMagicReductionCancel; private int _DG; private int _HpPercent; private int _MpPercent; private WareHouseLeaveType _warehousetype; private int _warehouse_limit_level; private int _IIg; private int _CC_Increase; private int _double_dmg_enchant_value; private int _weak_point_chance; private int _weak_point_enchant_value; private double _move_delay_rate; private double _attack_delay_rate; public int _mpAr16;
   public void setMagicDmgModifier(int i) {
     this._magicDmgModifier = i;
   }

   public int getDoubleDmgChance() {
     return 0;
   }

   public int get_canbedmg() {
     return 0;
   }

   public boolean isTwohandedWeapon() {
     return false;
   }


   public int get_ac() {
     return 0;
   }





   public int getWeightReduction() {
     return 0;
   }

   public int getDmgRate() {
     return 0;
   }

   public int getHitRate() {
     return 0;
   }

   public int getBowHitRate() {
     return 0;
   }

   public int getBowDmgRate() {
     return 0;
   }

   public int get_defense_water() {
     return 0;
   }

   public int get_defense_fire() {
     return 0;
   }

   public int get_defense_earth() {
     return 0;
   }

   public int get_defense_wind() {
     return 0;
   }

   public int get_defense_all() {
     return 0;
   }

   public int get_regist_calcPcDefense() {
     return 0;
   }

   public int get_regist_PVPweaponTotalDamage() {
     return 0;
   }



   public int getGrade() {
     return this._grade;
   }

   public void setGrade(int grade) {
     this._grade = grade;
   }



   public int get_price() {
     return this._price;
   }

   public void set_price(int price) {
     this._price = price;
   }



   public int get_damage_reduction() {
     return this.damage_reduction;
   }

   public void set_damage_reduction(int i) {
     this.damage_reduction = CommonUtil.get_current(i, 0, 127);
   }



   public boolean isEndedTimeMessage() {
     return this._isEndedTimeMessage;
   }

   public void setEndedTimeMessage(boolean b) {
     this._isEndedTimeMessage = b;
   }



   public void set_missile_critical_probability(int missile_critical_probability) {
     this._missile_critical_probability = missile_critical_probability;
   }

   public int get_missile_critical_probability() {
     return this._missile_critical_probability;
   }



   public void set_melee_critical_probability(int melee_critical_probability) {
     this._melee_critical_probability = melee_critical_probability;
   }

   public int get_melee_critical_probability() {
     return this._melee_critical_probability;
   }



   public void set_magic_critical_probability(int magic_critical_probability) {
     this._magic_critical_probability = magic_critical_probability;
   }

   public int get_magic_critical_probability() {
     return this._magic_critical_probability;
   }


   public abstract int getMagicHitup();


   public abstract void setMagicHitup(int paramInt);


   public abstract void setSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind parameKind, int paramInt);

   public abstract int getSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind parameKind);

   public abstract void setSpecialResistanceMap(HashMap<Integer, Integer> paramHashMap);

   public abstract HashMap<Integer, Integer> getSpecialResistanceMap();

   public abstract void setSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind parameKind, int paramInt);

   public abstract int getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind parameKind);

   public abstract void setSpecialPierceMap(HashMap<Integer, Integer> paramHashMap);

   public abstract HashMap<Integer, Integer> getSpecialPierceMap();

   public abstract void equipmentItem(L1PcInstance paramL1PcInstance, boolean paramBoolean);

   public int getArmorReductionCancel() {
     return this._reduc_cancel;
   }

   public void setArmorReductionCancel(int i) {
     this._reduc_cancel = i;
   }




   public int getWeaponReductionCancel() {
     return this._WeaponReductionCancel;
   }

   public void setWeaponReductionCancel(int 감소데미지) {
     this._WeaponReductionCancel = 감소데미지;
   }



   public int getTitanPercent() {
     return this._titan_percent;
   }

   public void setTitanPercent(int i) {
     this._titan_percent = i;
   }



   public int getUseEffectId() {
     return this._use_effectid;
   }

   public void setUseEffetId(int i) {
     this._use_effectid = i;
   }



   public int getOverlaySurfId() {
     return this._overlay_surf_id;
   }

   public void setOverlaySurfId(int i) {
     this._overlay_surf_id = i;
   }




   public int getPVPWeaponReductionCancel() {
     return this._PVPWeaponReductionCancel;
   }

   public void setPVPWeaponReductionCancel(int 감소데미지) {
     this._PVPWeaponReductionCancel = 감소데미지;
   }




   public int getPVPMagicReduction() {
     return this._PVPMagicReduction;
   }

   public void setPVPMagicReduction(int 감소데미지) {
     this._PVPMagicReduction = 감소데미지;
   }




   public int getPVPMagicReductionCancel() {
     return this._PVPMagicReductionCancel;
   }

   public void setPVPMagicReductionCancel(int 감소데미지) {
     this._PVPMagicReductionCancel = 감소데미지;
   }



   public int getDG() {
     return this._DG;
   }

   public void setDG(int DG) {
     this._DG = DG;
   }



   public int getHpPercent() {
     return this._HpPercent;
   }

   public void setHpPercent(int HpPercent) {
     this._HpPercent = HpPercent;
   }



   public int getMpPercent() {
     return this._MpPercent;
   }

   public void setMpPercent(int MpPercent) {
     this._MpPercent = MpPercent;
   }



   public WareHouseLeaveType getWareHouseLimitType() {
     return this._warehousetype;
   }

   public void setWareHouseLimitType(WareHouseLeaveType e) {
     this._warehousetype = e;
   }



   public int getWareHouseLimitLevel() {
     return this._warehouse_limit_level;
   }

   public void setWareHouseLimitLevel(int i) {
     this._warehouse_limit_level = i;
   }



   public int getIIg() {
     return this._IIg;
   }

   public void setIIg(int IIG) {
     this._IIg = IIG;
   }

   public int getCCIncrease() {
     return this._CC_Increase;
   }

   public void setCCIncrease(int i) {
     this._CC_Increase = i;
   }




   public int get_double_dmg_enchant_value() {
     return this._double_dmg_enchant_value;
   }

   public void set_double_dmg_enchant_value(int _double_dmg_enchant_value) {
     this._double_dmg_enchant_value = _double_dmg_enchant_value;
   }



   public int get_weak_point_chance() {
     return this._weak_point_chance;
   }

   public void set_weak_point_chance(int _weak_point_chance) {
     this._weak_point_chance = _weak_point_chance;
   }



   public int get_weak_point_enchant_value() {
     return this._weak_point_enchant_value;
   }

   public void set_weak_point_enchant_value(int _weak_point_enchant_value) {
     this._weak_point_enchant_value = _weak_point_enchant_value;
   }



   public double getMoveDelayRate() {
     return this._move_delay_rate;
   }

   public void setMoveDelayRate(double move_delay_rate) {
     this._move_delay_rate = move_delay_rate;
   }



   public double getAttackDelayRate() {
     return this._attack_delay_rate;
   }

   public void setAttackDelayRate(double attack_delay_rate) {
     this._attack_delay_rate = attack_delay_rate;
   }



   public int getMpAr16() {
     return this._mpAr16;
   }

   public void setMpAr16(int i) {
     this._mpAr16 = i;
   }
 }


