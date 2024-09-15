package l1j.server.server.templates;

import java.io.Serializable;
import java.util.HashMap;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.CommonUtil;

public abstract class L1Item implements Serializable {

	private static final long serialVersionUID = 1L;

	public L1Item() {
	}

	// ■■■■■■ L1EtcItem, L1Weapon, L1Armor 的共通項目 ■■■■■■

	private int _type2; // 0=L1EtcItem, 1=L1Weapon, 2=L1Armor
	
	
		
	/**
	 * @return 0 if L1EtcItem, 1 if L1Weapon, 2 if L1Armor
	 */
	public int getType2() {
		return _type2;
	}

	public void setType2(int type) {
		_type2 = type;
	}

	private int _itemId;

	public int getItemId() {
		return _itemId;
	}

	public void setItemId(int itemId) {
		_itemId = itemId;
	}

	private String _name;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	private String _name_view;

	public String getNameView() {
		return _name_view;
	}

	public void setNameView(String name_view) {
		_name_view = name_view;
	}

	private String _Magic_name;

	public String getMagicName() {
		return _Magic_name;
	}

	public void setMagicName(String name) {
		_Magic_name = name;
	}

	private String _nameId;

	public String getNameId() {
		return _nameId;
	}
	public int getItemNameIdInt() {
		String namestr = _nameId.replaceAll("[^0-9]", "");
		int nameint = Integer.parseInt(namestr);
		return nameint;
	}

	public void setNameId(String nameid) {
		_nameId = nameid;
	}

	private int _type;

	/**
	 * 返回項目的類型。<br>
	 *
	 * @return
	 *         <p>
	 *         [其他物品]<br>
	 *         0:箭, 1:魔杖, 2:燈, 3:寶石, 4:圖騰, 5:爆竹, 6:藥水,
	 *         7:食物, 8:卷軸, 9:任務物品, 10:魔法書, 11:寵物物品, 12:其他,
	 *         13:材料, 14:活動, 15:刺, 17:傳送卷軸
	 *         </p>
	 *         <p>
	 *         [武器]<br>
	 *         1:劍, 2:匕首, 3:雙手劍, 4:弓, 5:矛, 6:鈍器, 7:法杖,
	 *         8:飛刀, 9:箭, 10:護手, 11:爪, 12:雙劍,
	 *         13:單弓, 14:單矛, 15:雙手鈍器, 16:雙手法杖,
	 *         17:鑰匙圈, 18:鏈劍
	 *         </p>
	 *         <p>
	 *         [盔甲]<br>
	 *         1:頭盔, 2:盔甲, 3:T, 4:斗篷, 5:手套, 6:靴子, 7:盾, 8:護身符,
	 *         9:戒指, 10:腰帶, 11:戒指2, 12:耳環  30:徽章  31:吊墜
	 */
	public int getType() {
		return _type;
	}

	public void setType(int type) {
		_type = type;
	}

	private int _type1;

	/**
	 * 返回物品的種類。<br>
	 *
	 * @return
	 *         <p>
	 *         [武器]<br>
	 *         劍:4, 匕首:46, 雙手劍:50, 弓:20, 鈍器:11, 矛:24,
	 *         法杖:40, 飛刀:2922, 箭:66, 護手:62, 爪:58,
	 *         雙刀:54, 單手弓:20, 單手矛:24, 雙手鈍器:11,
	 *         雙手法杖:40
	 *         </p>
	 */
	public int getType1() {
		return _type1;
	}

	public void setType1(int type1) {
		_type1 = type1;
	}

	private int _material;

	/**
	 * 返回物品的材質。
	 *
	 * @return 0:無 1:液體 2:網 3:植物性 4:動物性 5:土 6:粉 7:血 8:木 9:骨 10:龍鱗 11:鐵 12:鋼
	 *         13:銅 14:銀 15:金 16:白金 17:秘銀 18:黑秘銀 19:玻璃 20:寶石 21:礦物 22:奧利哈鋼
	 */
	public int getMaterial() {
		return _material;
	}

	public void setMaterial(int material) {
		_material = material;
	}

	private int _weight;

	public int getWeight() {
		return _weight;
	}

	public void setWeight(int weight) {
		_weight = weight;
	}

	private int _gfxId;

	public int getGfxId() {
		return _gfxId;
	}

	public void setGfxId(int gfxId) {
		_gfxId = gfxId;
	}

	private int _groundGfxId;

	public int getGroundGfxId() {
		return _groundGfxId;
	}

	public void setGroundGfxId(int groundGfxId) {
		_groundGfxId = groundGfxId;
	}

	private int _minLevel;

	private int _itemDescId;

	/**
	 * 返回鑑定時顯示的 ItemDesc.tbl 中的消息 ID。
	 */
	public int getItemDescId() {
		return _itemDescId;
	}
	
	

	public void setItemDescId(int descId) {
		_itemDescId = descId;
	}
	
	public int getMinLevel() {
		return _minLevel;
	}

	public void setMinLevel(int level) {
		_minLevel = level;
	}

	private int _maxLevel;

	public int getMaxLevel() {
		return _maxLevel;
	}

	public void setMaxLevel(int maxlvl) {
		_maxLevel = maxlvl;
	}

	private int _bless;

	public int getBless() {
		return _bless;
	}

	public void setBless(int i) {
		_bless = i;
	}

	private boolean _tradable;

	public boolean isTradable() {
		return _tradable;
	}

	public void setTradable(boolean flag) {
		_tradable = flag;
	}

	private boolean _cantDelete;

	public boolean isCantDelete() {
		return _cantDelete;
	}

	public void setCantDelete(boolean flag) {
		_cantDelete = flag;
	}

	private boolean _save_at_once;

	/**
	 * 返回當物品數量變化時是否應立即寫入資料庫。
	 */
	public boolean isToBeSavedAtOnce() {
		return _save_at_once;
	}

	public void setToBeSavedAtOnce(boolean flag) {
		_save_at_once = flag;
	}

	// ■■■■■■ L1EtcItem, L1Weapon 的共通項目 ■■■■■■

	private int _dmgSmall = 0;

	public int getDmgSmall() {
		return _dmgSmall;
	}

	public void setDmgSmall(int dmgSmall) {
		_dmgSmall = dmgSmall;
	}

	private int _dmgLarge = 0;

	public int getDmgLarge() {
		return _dmgLarge;
	}

	public void setDmgLarge(int dmgLarge) {
		_dmgLarge = dmgLarge;
	}

	// ■■■■■■ L1EtcItem, L1Armor 的共通項目 ■■■■■■

// ■■■■■■ L1Weapon, L1Armor 的共通項目 ■■■■■■

	private int _safeEnchant = 0;

	public int get_safeenchant() {
		return _safeEnchant;
	}

	public void set_safeenchant(int safeenchant) {
		_safeEnchant = safeenchant;
	}

	private boolean _useRoyal = false;

	public boolean isUseRoyal() {
		return _useRoyal;
	}

	public void setUseRoyal(boolean flag) {
		_useRoyal = flag;
	}

	private boolean _useKnight = false;

	public boolean isUseKnight() {
		return _useKnight;
	}

	public void setUseKnight(boolean flag) {
		_useKnight = flag;
	}

	private boolean _useElf = false;

	public boolean isUseElf() {
		return _useElf;
	}

	public void setUseElf(boolean flag) {
		_useElf = flag;
	}

	private boolean _useMage = false;

	public boolean isUseMage() {
		return _useMage;
	}

	public void setUseMage(boolean flag) {
		_useMage = flag;
	}

	private boolean _useDarkelf = false;

	public boolean isUseDarkelf() {
		return _useDarkelf;
	}

	public void setUseDarkelf(boolean flag) {
		_useDarkelf = flag;
	}

	private boolean _useDragonKnight = false;

	public boolean isUseDragonKnight() {
		return _useDragonKnight;
	}

	public void setUseDragonKnight(boolean flag) {
		_useDragonKnight = flag;
	}

	private boolean _useBlackwizard = false;

	public boolean isUseBlackwizard() {
		return _useBlackwizard;
	}

	private boolean _useWarrior = false;

	public boolean isUseWarrior() {
		return _useWarrior;
	}

	public void setUseWarrior(boolean flag) {
		_useWarrior = flag;
	}
	
	private boolean _useFencer = false;

	public boolean isUseFencer() {
		return _useFencer;
	}

	public void setUseFencer(boolean flag) {
		_useFencer = flag;
	}
	
	private boolean _useLancer = false;

	public boolean isUseLancer() {
		return _useLancer;
	}

	public void setUseLancer(boolean flag) {
		_useLancer = flag;
	}

	private boolean _useHighPet = false;

	public boolean isUseHighPet() {
		return _useHighPet;
	}

	public void setUseHighPet(boolean flag) {
		_useHighPet = flag;
	}

	public void setUseBlackwizard(boolean flag) {
		_useBlackwizard = flag;
	}

	private byte _addstr = 0;

	public byte get_addstr() {
		return _addstr;
	}

	public void set_addstr(byte addstr) {
		_addstr = addstr;
	}

	private byte _adddex = 0;

	public byte get_adddex() {
		return _adddex;
	}

	public void set_adddex(byte adddex) {
		_adddex = adddex;
	}

	private byte _addcon = 0;

	public byte get_addcon() {
		return _addcon;
	}

	public void set_addcon(byte addcon) {
		_addcon = addcon;
	}

	private int _addexp = 0;

	public int getAddExp() {
		return _addexp;
	}

	public void setAddExp(int addexp) {
		_addexp = addexp;
	}

	private byte _addint = 0;

	public byte get_addint() {
		return _addint;
	}

	public void set_addint(byte addint) {
		_addint = addint;
	}

	private byte _addwis = 0;

	public byte get_addwis() {
		return _addwis;
	}

	public void set_addwis(byte addwis) {
		_addwis = addwis;
	}

	private byte _addcha = 0;

	public byte get_addcha() {
		return _addcha;
	}

	public void set_addcha(byte addcha) {
		_addcha = addcha;
	}

	private int _addhp = 0;

	public int get_addhp() {
		return _addhp;
	}

	public void set_addhp(int addhp) {
		_addhp = addhp;
	}

	private int _addmp = 0;

	public int get_addmp() {
		return _addmp;
	}

	public void set_addmp(int addmp) {
		_addmp = addmp;
	}

	private int _addhpr = 0;

	public int get_addhpr() {
		return _addhpr;
	}

	public void set_addhpr(int addhpr) {
		_addhpr = addhpr;
	}

	private int _addmpr = 0;

	public int get_addmpr() {
		return _addmpr;
	}

	public void set_addmpr(int addmpr) {
		_addmpr = addmpr;
	}

	private int _addsp = 0;

	public int get_addsp() {
		return _addsp;
	}

	public void set_addsp(int addsp) {
		_addsp = addsp;
	}

	/** 2017-11-06 愛因哈薩德祝福減少機率更新 **/
	private int _addeinhasadper = 0;

	public int get_addeinhasadper() {
		return _addeinhasadper;
	}

	public void set_addeinhasadper(int einhasad) {
		_addeinhasadper = einhasad;
	}

	/** 2017-11-06 愛因哈薩德祝福減少機率重製 **/

	private int _mdef = 0;

	public int get_mdef() {
		return _mdef;
	}

	public void set_mdef(int i) {
		this._mdef = i;
	}

	private boolean _isHasteItem = false;

	public boolean isHasteItem() {
		return _isHasteItem;
	}

	public void setHasteItem(boolean flag) {
		_isHasteItem = flag;
	}

	private int _maxUseTime = 0;

	public int getMaxUseTime() {
		return _maxUseTime;
	}

	public void setMaxUseTime(int i) {
		_maxUseTime = i;
	}

	private int _useType;

	/**
	 * 返回使用時決定反應的類型。
	 */
	public int getUseType() {
		return _useType;
	}

	public void setUseType(int useType) {
		_useType = useType;
	}

	private int _foodVolume;

	/**
	 * 返回以肉類等物品設定的飽食度。
	 */
	public int getFoodVolume() {
		return _foodVolume;
	}

	public void setFoodVolume(int volume) {
		_foodVolume = volume;
	}

	/**
	 * 返回以燈等物品設定的亮度。
	 */
	public int getLightRange() {
		if (_itemId == 40001) {
			return 11;
		} else if (_itemId == 7005) {
			return 14;
		} else if (_itemId == 40002) {
			return 14;
		} else if (_itemId == 40004) {
			return 14;
		} else if (_itemId == 40005) {
			return 8;
		} else {
			return 0;
		}
	}

	public int getLightFuel() {
		if (_itemId == 40001) {
			return 600;
		} else if (_itemId == 7005) {
			return 600;
		} else if (_itemId == 40002) {
			return 0;
		} else if (_itemId == 40003) {
			return 600;
		} else if (_itemId == 40004) {
			return 0;
		} else if (_itemId == 40005) {
			return 600;
		} else {
			return 0;
		}
	}

	// ■■■■■■ 覆蓋 L1EtcItem 的項目 ■■■■■■
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

	// ■■■■■■ 覆蓋 L1Weapon 的項目 ■■■■■■

	private int _hitModifier = 0;

	public int getHitModifier() {
		return _hitModifier;
	}

	public void setHitModifier(int i) {
		_hitModifier = i;
	}

	private int _dmgModifier = 0;

	public int getDmgModifier() {
		return _dmgModifier;
	}

	public void setDmgModifier(int i) {
		_dmgModifier = i;
	}
	
    private int _magicDmgModifier = 0;
	
	public int getMagicDmgModifier() {
		return _magicDmgModifier;
	}

	public void setMagicDmgModifier(int i) {
		_magicDmgModifier = i;
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

	// ■■■■■■ 覆蓋 L1Armor 的項目 ■■■■■■
	public int get_ac() {
		return 0;
	}

	// public int getDamageReduction() {
	// return 0;
	// }

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

	private int _grade; // ● 飾品階段

	public int getGrade() {
		return _grade;
	}

	public void setGrade(int grade) {
		_grade = grade;
	}

	private int _price; // ● 價格

	public int get_price() {
		return _price;
	}

	public void set_price(int price) {
		_price = price;
	}

	private int damage_reduction;

	public int get_damage_reduction() {
		return damage_reduction;
	}

	public void set_damage_reduction(int i) {
		this.damage_reduction = CommonUtil.get_current(i, 0, 127);//最大顯示
	}

	private boolean _isEndedTimeMessage;

	public boolean isEndedTimeMessage() {
		return _isEndedTimeMessage;
	}

	public void setEndedTimeMessage(boolean b) {
		_isEndedTimeMessage = b;
	}

	private int _missile_critical_probability;

	public void set_missile_critical_probability(int missile_critical_probability) {
		_missile_critical_probability = missile_critical_probability;
	}

	public int get_missile_critical_probability() {
		return _missile_critical_probability;
	}

	private int _melee_critical_probability;

	public void set_melee_critical_probability(int melee_critical_probability) {
		_melee_critical_probability = melee_critical_probability;
	}

	public int get_melee_critical_probability() {
		return _melee_critical_probability;
	}

	private int _magic_critical_probability;

	public void set_magic_critical_probability(int magic_critical_probability) {
		_magic_critical_probability = magic_critical_probability;
	}

	public int get_magic_critical_probability() {
		return _magic_critical_probability;
	}

	public abstract int getMagicHitup();

	public abstract void setMagicHitup(int i);

	public abstract void setSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind kind, int value);

	public abstract int getSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind kind);

	public abstract void setSpecialResistanceMap(HashMap<Integer, Integer> itemResistance);

	public abstract HashMap<Integer, Integer> getSpecialResistanceMap();

	public abstract void setSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind kind, int value);

	public abstract int getSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind kind);

	public abstract void setSpecialPierceMap(HashMap<Integer, Integer> itemPierce);

	public abstract HashMap<Integer, Integer> getSpecialPierceMap();

	public abstract void equipmentItem(L1PcInstance pc, boolean isEquipped);

	private int _reduc_cancel;

	// -- 新增於防具的傷害減少忽視效果（減傷忽視效果）
	public int getArmorReductionCancel() {
		return _reduc_cancel;
	}

	public void setArmorReductionCancel(int i) {
		_reduc_cancel = i;
	}

	// -- 新增於武器類的傷害減少忽視效果（減傷忽視效果）
	private int _WeaponReductionCancel;

	public int getWeaponReductionCancel() {
		return _WeaponReductionCancel;
	}
	/**
	 * 設置武器傷害減少取消值
	 *
	 * @param 減少傷害 傷害減少值
	 */
	public void setWeaponReductionCancel(int reductionDamage) {
		_WeaponReductionCancel = reductionDamage;
	}

	private int _titan_percent;

	public int getTitanPercent() {
		return _titan_percent;
	}

	public void setTitanPercent(int i) {
		_titan_percent = i;
	}

	private int _use_effectid;

	public int getUseEffectId() {
		return _use_effectid;
	}

	public void setUseEffetId(int i) {
		_use_effectid = i;
	}

	private int _overlay_surf_id;

	public int getOverlaySurfId() {
		return _overlay_surf_id;
	}

	public void setOverlaySurfId(int i) {
		_overlay_surf_id = i;
	}

	// pvp 傷害減少忽視
	private int _PVPWeaponReductionCancel;

	public int getPVPWeaponReductionCancel() {
		return _PVPWeaponReductionCancel;
	}
	/**
	 * 設置 PvP 武器傷害減少取消值
	 *
	 * @param 減少傷害 傷害減少值
	 */
	public void setPVPWeaponReductionCancel(int reductionDamage) {
		_PVPWeaponReductionCancel = reductionDamage;
	}

	// pvp 魔法傷害減少
	private int _PVPMagicReduction;

	public int getPVPMagicReduction() {
		return _PVPMagicReduction;
	}
	/**
	 * 設置 PvP 魔法傷害減少值
	 *
	 * @param 減少傷害 傷害減少值
	 */
	public void setPVPMagicReduction(int reductionDamage) {
		_PVPMagicReduction = reductionDamage;
	}

	// pvp 魔法傷害減少忽視
	private int _PVPMagicReductionCancel;

	public int getPVPMagicReductionCancel() {
		return _PVPMagicReductionCancel;
	}
	/**
	 * 設置 PvP 魔法傷害減少取消值
	 *
	 * @param 減少傷害 傷害減少值
	 */
	public void setPVPMagicReductionCancel(int reductionDamage) {
		_PVPMagicReductionCancel = reductionDamage;
	}
	
	private int _DG;

	public int getDG() {
		return _DG;
	}

	public void setDG(int DG) {
		_DG = DG;
	}
	
	private int _HpPercent;

	public int getHpPercent() {
		return _HpPercent;
	}

	public void setHpPercent(int HpPercent) {
		_HpPercent = HpPercent;
	}
	
	private int _MpPercent;

	public int getMpPercent() {
		return _MpPercent;
	}

	public void setMpPercent(int MpPercent) {
		_MpPercent = MpPercent;
	}

	private WareHouseLeaveType _warehousetype;

	public WareHouseLeaveType getWareHouseLimitType() {
		return _warehousetype;
	}

	public void setWareHouseLimitType(WareHouseLeaveType e) {
		_warehousetype = e;
	}

	private int _warehouse_limit_level;

	public int getWareHouseLimitLevel() {
		return _warehouse_limit_level;
	}

	public void setWareHouseLimitLevel(int i) {
		_warehouse_limit_level = i;
	}
	
	private int _IIg;

	public int getIIg() {
		return _IIg;
	}
	
	public void setIIg(int IIG) {
		_IIg = IIG;
	}
	private int _CC_Increase;
	public int getCCIncrease() {
		return _CC_Increase;
	}
	
	public void setCCIncrease (int i) {
		_CC_Increase = i;
	}
	

	private int _double_dmg_enchant_value;
	
	public int get_double_dmg_enchant_value() {
		return _double_dmg_enchant_value;
	}

	public void set_double_dmg_enchant_value(int _double_dmg_enchant_value) {
		this._double_dmg_enchant_value = _double_dmg_enchant_value;
	}

	private int _weak_point_chance;

	public int get_weak_point_chance() {
		return _weak_point_chance;
	}

	public void set_weak_point_chance(int _weak_point_chance) {
		this._weak_point_chance = _weak_point_chance;
	}

	private int _weak_point_enchant_value;

	public int get_weak_point_enchant_value() {
		return _weak_point_enchant_value;
	}

	public void set_weak_point_enchant_value(int _weak_point_enchant_value) {
		this._weak_point_enchant_value = _weak_point_enchant_value;
	}
	
	private double _move_delay_rate;

	public double getMoveDelayRate() {
		return _move_delay_rate;
	}

	public void setMoveDelayRate(double move_delay_rate) {
		_move_delay_rate = move_delay_rate;
	}
	
	private double _attack_delay_rate;
	
	public double getAttackDelayRate() {
		return _attack_delay_rate;
	}

	public void setAttackDelayRate(double attack_delay_rate) {
		_attack_delay_rate = attack_delay_rate;
	}
	
	public int _mpAr16;
	
	public int getMpAr16() {
		return _mpAr16;
	}
	
	public void setMpAr16(int i) {
		_mpAr16 = i;
	}
}
