package l1j.server.server.server.model;

import java.util.ArrayList;
import java.util.StringTokenizer;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ItemStatus;
import l1j.server.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.server.server.templates.L1ArmorSets;

public abstract class L1ArmorSet {	
    public abstract void giveEffect(L1PcInstance pc);
    public abstract void cancelEffect(L1PcInstance pc);
    public abstract boolean isValid(L1PcInstance pc);
    public abstract boolean isPartOfSet(int id);
    public abstract boolean isEquippedRingOfArmorSet(L1PcInstance pc);
    
    public static ArrayList<L1ArmorSet> getAllSet() {
        return _allSet;
    }
    
    private static ArrayList<L1ArmorSet> _allSet = new ArrayList<L1ArmorSet>();
    
    public static class L1ArmorSetImpl extends L1ArmorSet {
    	private static int[] getArray(String s, String sToken) {
            StringTokenizer st = new StringTokenizer(s, sToken);
            int size = st.countTokens();
            String temp = null;
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                temp = st.nextToken();
                array[i] = Integer.parseInt(temp);
            }
            return array;
        }
    	
    	public static L1ArmorSetImpl newInstance(L1ArmorSets armorSets) {
    		L1ArmorSetImpl impl = new L1ArmorSetImpl(getArray(armorSets.getSets(), ","), armorSets);
            if (armorSets.getPolyId() != -1) {
                impl.addEffect(new PolymorphEffect(armorSets.getPolyId()));
            }
            if (armorSets.getId() == 128) {
                impl.addEffect(new EvaiconEffect());
            } 
            if (armorSets.get_main_id() != 0) {
            	impl.addEffect(new ArmorSetItem(armorSets.get_main_id()));
            }
            
            impl.addEffect(new AcHpMpBonusEffect(
            		armorSets.getAc(), armorSets.getHp(), armorSets.getMp(),
                    armorSets.getHpr(), armorSets.getMpr(), armorSets.getMr(),
                    armorSets.getWeightReduction()));
            impl.addEffect(new StatBonusEffect(armorSets.getStr(),
                    armorSets.getDex(), armorSets.getCon(),
                    armorSets.getWis(), armorSets.getCha(),
                    armorSets.getIntl(), armorSets.get_sp(), 
                    armorSets.get_melee_damage(), armorSets.get_melee_hit(), 
                    armorSets.get_missile_damage(), armorSets.get_missile_hit(),
                    armorSets.getMagicHitup(), armorSets.get_regist_calcPcDefense(),
                    armorSets.get_regist_PVPweaponTotalDamage(),armorSets.getTechniqueHit(),
            		armorSets.getSpiritHit(),
            		armorSets.getDragonLangHit(),
            		armorSets.getFearHit(),
                    armorSets.getAllHit()));
            impl.addEffect(new defense(armorSets.get_defense_water(),
                    armorSets.get_defense_earth(),
                    armorSets.get_defense_wind(),
                    armorSets.get_defense_fire(),
                    armorSets.get_defense_all(),
                    armorSets.getTechniqueTolerance(),
            		armorSets.getSpiritTolerance(),
            		armorSets.getDragonLangTolerance(),
            		armorSets.getFearTolerance(),
                    armorSets.getAllTolerance()));
            
            
            _allSet.add(impl);
            return impl;
    	}
    	
        private final int _ids[];
        private final ArrayList<L1ArmorSetEffect> _effects;
        private final L1ArmorSets m_source_effects;
        protected L1ArmorSetImpl(int ids[], L1ArmorSets source_effects) {
            _ids = ids;
            _effects = new ArrayList<L1ArmorSetEffect>();
            m_source_effects = source_effects;
        }
        
        public boolean is_signle_items() {
        	return _ids.length == 1;
        }
        
        public L1ArmorSets get_source_effects() {
        	return m_source_effects;
        }

        public void addEffect(L1ArmorSetEffect effect) {
            _effects.add(effect);
        }

        public void removeEffect(L1ArmorSetEffect effect) {
            _effects.remove(effect);
        }

        @Override
        public void cancelEffect(L1PcInstance pc) {
            for (L1ArmorSetEffect effect : _effects) {
                effect.cancelEffect(pc);
            }
        }

        @Override
        public void giveEffect(L1PcInstance pc) {
            for (L1ArmorSetEffect effect : _effects) {
                effect.giveEffect(pc);
            }
        }

        @Override
        public final boolean isValid(L1PcInstance pc) {
            return pc.getInventory().checkEquipped(_ids) && pc.getInventory().checkEquipped(get_source_effects().get_main_id());
        }

        @Override
        public boolean isPartOfSet(int id) {
        	if(id == get_source_effects().get_main_id())
        		return true;
        	
            for (int i : _ids) {
                if (id == i) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public boolean isEquippedRingOfArmorSet(L1PcInstance pc) {
            L1PcInventory pcInventory = pc.getInventory();
            L1ItemInstance armor = null;
            boolean isSetContainRing = false;

            for (int id : _ids) {
                armor = pcInventory.findItemId(id);
                if (armor.getItem().getType2() == 2
                        && armor.getItem().getType() == 9) {
                    isSetContainRing = true;
                    break;
                }
            }

            if (armor != null && isSetContainRing) {
                int itemId = armor.getItem().getItemId();
                if (pcInventory.getTypeEquipped(2, 9) >= 2) {
                    L1ItemInstance ring[] = new L1ItemInstance[4];
                    ring = pcInventory.getRingEquipped();
                    if (ring != null && ring.length > 0) {
                        int count = 0;
                        for (L1ItemInstance item : ring) {
                            if (item == null)
                                continue;
                            if (item.getItemId() == itemId)
                                count++;
                        }
                        if (count >= 2)
                            return true;
                    }
                }
            }
            return false;
        }
    }
}

interface L1ArmorSetEffect {
    public void giveEffect(L1PcInstance pc);
    public void cancelEffect(L1PcInstance pc);
}

class AcHpMpBonusEffect implements L1ArmorSetEffect {
    private final int _ac;
    private final int _addHp;
    private final int _addMp;
    private final int _regenHp;
    private final int _regenMp;
    private final int _addMr;
    private final int _weightreduction;

    public AcHpMpBonusEffect(int ac, int addHp, int addMp, int regenHp,
            int regenMp, int addMr, int weightreduction) {
        _ac = ac;
        _addHp = addHp;
        _addMp = addMp;
        _regenHp = regenHp;
        _regenMp = regenMp;
        _addMr = addMr;
        _weightreduction = weightreduction;
    }

    @Override
    public void giveEffect(L1PcInstance pc) {
        pc.getAC().addAc(_ac);
        pc.addMaxHp(_addHp);
        pc.addMaxMp(_addMp);
        pc.addHpr(_regenHp);
        pc.addMpr(_regenMp);
        pc.getResistance().addMr(_addMr);
        pc.addWeightReduction(_weightreduction);
        
    }

    @Override
    public void cancelEffect(L1PcInstance pc) {
        pc.getAC().addAc(-_ac);
        pc.addMaxHp(-_addHp);
        pc.addMaxMp(-_addMp);
        pc.addHpr(-_regenHp);
        pc.addMpr(-_regenMp);
        pc.getResistance().addMr(-_addMr);
        pc.addWeightReduction(-_weightreduction);
    }
}

class StatBonusEffect implements L1ArmorSetEffect {
    private final int _str;
    private final int _dex;
    private final int _con;
    private final int _wis;
    private final int _cha;
    private final int _intl;
    private final int _sp;
	private final int _melee_damage;
	private final int _melee_hit;
	private final int _missile_damage;
	private final int _missile_hit;
	private final int _magic_hit_up;
	private final int _regist_calcPcDefense;
	private final int _regist_PVPweaponTotalDamage;
    private final int _ability_pierce;
    private final int _spirit_pierce;
    private final int _dragonS_pierce;
    private final int _fear_pierce;
    private final int _all_pierce;

    public StatBonusEffect(int str, int dex, int con, int wis, int cha, int intl, int sp, int melee_damage,
    		int melee_hit, int missile_damage, int missile_hit, int magic_hit_up, int regist_calcPcDefense, int regist_PVPweaponTotalDamage
    		,int ability_pierce, int spirit_pierce, int dragonS_pierce, int fear_pierce, int all_pierce) {
        _str = str;
        _dex = dex;
        _con = con;
        _wis = wis;
        _cha = cha;
        _intl = intl;
        _sp = sp;
        _melee_damage = melee_damage;
    	_melee_hit = melee_hit;
    	_missile_damage = missile_damage;
    	_missile_hit = missile_hit;
    	_magic_hit_up = magic_hit_up;
    	_regist_calcPcDefense = regist_calcPcDefense;
    	_regist_PVPweaponTotalDamage = regist_PVPweaponTotalDamage;
    	_ability_pierce = ability_pierce;
    	_spirit_pierce = spirit_pierce;
    	_dragonS_pierce = dragonS_pierce;
    	_fear_pierce = fear_pierce;
    	_all_pierce = all_pierce;
    	
    }

    @Override
    public void giveEffect(L1PcInstance pc) {
        pc.getAbility().addAddedStr((byte) _str);
        pc.getAbility().addAddedDex((byte) _dex);
        pc.getAbility().addAddedCon((byte) _con);
        pc.getAbility().addAddedWis((byte) _wis);
        pc.getAbility().addAddedCha((byte) _cha);
        pc.getAbility().addAddedInt((byte) _intl);
        pc.getAbility().addSp(_sp);
        if(_sp != 0)
        	pc.sendPackets(new S_SPMR(pc));
        
        pc.addDmgupByArmor(_melee_damage);
        pc.addHitup(_melee_hit);
        pc.addBowDmgupByArmor(_missile_damage);
        pc.addBowHitup(_missile_hit);
        pc.addBaseMagicHitUp(_magic_hit_up);
        pc.getResistance().addcalcPcDefense(_regist_calcPcDefense);
        pc.getResistance().addPVPweaponTotalDamage(_regist_PVPweaponTotalDamage);
    	pc.addSpecialPierce(eKind.ABILITY, _ability_pierce);
    	pc.addSpecialPierce(eKind.SPIRIT, _spirit_pierce);
    	pc.addSpecialPierce(eKind.DRAGON_SPELL, _dragonS_pierce);
    	pc.addSpecialPierce(eKind.FEAR, _fear_pierce);
    	pc.addSpecialPierce(eKind.ALL, _all_pierce);
    	SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);

    }

    @Override
    public void cancelEffect(L1PcInstance pc) {
        pc.getAbility().addAddedStr((byte) -_str);
        pc.getAbility().addAddedDex((byte) -_dex);
        pc.getAbility().addAddedCon((byte) -_con);
        pc.getAbility().addAddedWis((byte) -_wis);
        pc.getAbility().addAddedCha((byte) -_cha);
        pc.getAbility().addAddedInt((byte) -_intl);
        pc.getAbility().addSp(-_sp);
        if(_sp != 0)
        	pc.sendPackets(new S_SPMR(pc));
        pc.addDmgupByArmor(-_melee_damage);
        pc.addHitup(-_melee_hit);
        pc.addBowDmgupByArmor(-_missile_damage);
        pc.addBowHitup(-_missile_hit);
        pc.addBaseMagicHitUp(-_magic_hit_up);
        pc.getResistance().addcalcPcDefense(-_regist_calcPcDefense);
        pc.getResistance().addPVPweaponTotalDamage(-_regist_PVPweaponTotalDamage);
       	pc.addSpecialPierce(eKind.ABILITY, -_ability_pierce);
    	pc.addSpecialPierce(eKind.SPIRIT, -_spirit_pierce);
    	pc.addSpecialPierce(eKind.DRAGON_SPELL, -_dragonS_pierce);
    	pc.addSpecialPierce(eKind.FEAR, -_fear_pierce);
    	pc.addSpecialPierce(eKind.ALL, -_all_pierce);
    	SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
    }
}

class defense implements L1ArmorSetEffect {

    private final int _defense_water;
    private final int _defense_earth;
    private final int _defense_wind;
    private final int _defense_fire;
    private final int _defense_all;
    private final int _ability_resis;
    private final int _spirit_resis;
    private final int _dragonS_resis;
    private final int _fear_resis;
    private final int _all_resis;

    public defense(int defense_water, int defense_earth, int defense_wind, int defense_fire, int defense_all
    		,int ability_resis, int spirit_resis, int dragonS_resis, int fear_resis, int all_resis) {
        _defense_water = defense_water;
        _defense_earth = defense_earth;
        _defense_wind = defense_wind;
        _defense_fire = defense_fire;
        _defense_all = defense_all;
    	_ability_resis = ability_resis;
    	_spirit_resis = spirit_resis;
    	_dragonS_resis = dragonS_resis;
    	_fear_resis = fear_resis;
    	_all_resis = all_resis;
    }

    public void giveEffect(L1PcInstance pc) {
        pc.getResistance().addWater(_defense_water);
        pc.getResistance().addEarth(_defense_earth);
        pc.getResistance().addWind(_defense_wind);
        pc.getResistance().addFire(_defense_fire);
        pc.getResistance().addAllNaturalResistance(_defense_all);
    	pc.addSpecialResistance(eKind.ABILITY, _ability_resis);
    	pc.addSpecialResistance(eKind.SPIRIT, _spirit_resis);
    	pc.addSpecialResistance(eKind.DRAGON_SPELL, _dragonS_resis);
    	pc.addSpecialResistance(eKind.FEAR, _fear_resis);
    	pc.addSpecialResistance(eKind.ALL, _all_resis);
    	SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
    }

    public void cancelEffect(L1PcInstance pc) {
        pc.getResistance().addWater(-_defense_water);
        pc.getResistance().addEarth(-_defense_earth);
        pc.getResistance().addWind(-_defense_wind);
        pc.getResistance().addFire(-_defense_fire);
        pc.getResistance().addAllNaturalResistance(-_defense_all);
    	pc.addSpecialResistance(eKind.ABILITY, -_ability_resis);
    	pc.addSpecialResistance(eKind.SPIRIT, -_spirit_resis);
    	pc.addSpecialResistance(eKind.DRAGON_SPELL, -_dragonS_resis);
    	pc.addSpecialResistance(eKind.FEAR, -_fear_resis);
    	pc.addSpecialResistance(eKind.ALL, -_all_resis);
    	SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
    }
}

 



class PolymorphEffect implements L1ArmorSetEffect {
    private int _gfxId;

    public PolymorphEffect(int gfxId) {
        _gfxId = gfxId;
    }

    @Override
    public void giveEffect(L1PcInstance pc) {
        if (_gfxId == 6080 || _gfxId == 6094) {
            if (pc.get_sex() == 0) {
                _gfxId = 6094;
            } else {
                _gfxId = 6080;
            }
            if (!isRemainderOfCharge(pc)) {
                return;
            }
        }
        L1PolyMorph.doPoly(pc, _gfxId, -1, L1PolyMorph.MORPH_BY_ITEMMAGIC, false, false);
    }

    @Override
    public void cancelEffect(L1PcInstance pc) {
        if (_gfxId == 6080) {
            if (pc.get_sex() == 0) {
                _gfxId = 6094;
            }
        }
        if (pc.getCurrentSpriteId() != _gfxId) {
            return;
        }
        L1PolyMorph.undoPoly(pc);
    }

    private boolean isRemainderOfCharge(L1PcInstance pc) {
        boolean isRemainderOfCharge = false;
        if (pc.getInventory().checkItem(20383, 1)) {
            L1ItemInstance item = pc.getInventory().findItemId(20383);
            if (item != null) {
                if (item.getChargeCount() != 0) {
                    isRemainderOfCharge = true;
                }
            }
        }
        return isRemainderOfCharge;
    }

}

class EvaiconEffect implements L1ArmorSetEffect {
    public EvaiconEffect() {
    }

    @Override
    public void giveEffect(L1PcInstance pc) {
        pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), -1));
    }

    @Override
    public void cancelEffect(L1PcInstance pc) {
        pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 0));
    }
}

class ArmorSetItem implements L1ArmorSetEffect {
    private int main_itemid;
    
    public ArmorSetItem(int itemid) {
    	main_itemid = itemid;
    }
    
	@Override
	public void giveEffect(L1PcInstance pc) {
		L1ItemInstance set_item = pc.getInventory().findEquippedItemId(main_itemid);
		if (set_item != null) {
			set_item.set_main_set_armor(true);
			pc.sendPackets(String.valueOf(new S_ItemStatus(set_item)));
		}
	}

	@Override
	public void cancelEffect(L1PcInstance pc) {
		L1ItemInstance[] items = pc.getInventory().findItemsId(main_itemid);
		if (items != null) {
			for(L1ItemInstance item : items) {
				item.set_main_set_armor(false);
				pc.sendPackets(String.valueOf(new S_ItemStatus(item)));
			}
		}
	}
}