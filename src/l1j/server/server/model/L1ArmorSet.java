 package l1j.server.server.model;

 import java.util.ArrayList;
 import java.util.StringTokenizer;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.templates.L1ArmorSets;

 public abstract class L1ArmorSet
 {
   public static ArrayList<L1ArmorSet> getAllSet() {
     return _allSet;
   }

   private static ArrayList<L1ArmorSet> _allSet = new ArrayList<>(); public abstract boolean isEquippedRingOfArmorSet(L1PcInstance paramL1PcInstance); public abstract boolean isPartOfSet(int paramInt); public abstract boolean isValid(L1PcInstance paramL1PcInstance); public abstract void cancelEffect(L1PcInstance paramL1PcInstance);
   public abstract void giveEffect(L1PcInstance paramL1PcInstance);
   public static class L1ArmorSetImpl extends L1ArmorSet { private final int[] _ids;
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
     private final ArrayList<L1ArmorSetEffect> _effects; private final L1ArmorSets m_source_effects;
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

       impl.addEffect(new AcHpMpBonusEffect(armorSets
             .getAc(), armorSets.getHp(), armorSets.getMp(), armorSets
             .getHpr(), armorSets.getMpr(), armorSets.getMr(), armorSets
             .getWeightReduction()));
       impl.addEffect(new StatBonusEffect(armorSets.getStr(), armorSets
             .getDex(), armorSets.getCon(), armorSets
             .getWis(), armorSets.getCha(), armorSets
             .getIntl(), armorSets.get_sp(), armorSets
             .get_melee_damage(), armorSets.get_melee_hit(), armorSets
             .get_missile_damage(), armorSets.get_missile_hit(), armorSets
             .getMagicHitup(), armorSets.get_regist_calcPcDefense(), armorSets
             .get_regist_PVPweaponTotalDamage(), armorSets.getTechniqueHit(), armorSets
             .getSpiritHit(), armorSets
             .getDragonLangHit(), armorSets
             .getFearHit(), armorSets
             .getAllHit()));
       impl.addEffect(new defense(armorSets.get_defense_water(), armorSets
             .get_defense_earth(), armorSets
             .get_defense_wind(), armorSets
             .get_defense_fire(), armorSets
             .get_defense_all(), armorSets
             .getTechniqueTolerance(), armorSets
             .getSpiritTolerance(), armorSets
             .getDragonLangTolerance(), armorSets
             .getFearTolerance(), armorSets
             .getAllTolerance()));


       L1ArmorSet._allSet.add(impl);
       return impl;
     }


     protected L1ArmorSetImpl(int[] ids, L1ArmorSets source_effects) {
       this._ids = ids;
       this._effects = new ArrayList<>();
       this.m_source_effects = source_effects;
     }

     public boolean is_signle_items() {
       return (this._ids.length == 1);
     }

     public L1ArmorSets get_source_effects() {
       return this.m_source_effects;
     }

     public void addEffect(L1ArmorSetEffect effect) {
       this._effects.add(effect);
     }

     public void removeEffect(L1ArmorSetEffect effect) {
       this._effects.remove(effect);
     }


     public void cancelEffect(L1PcInstance pc) {
       for (L1ArmorSetEffect effect : this._effects) {
         effect.cancelEffect(pc);
       }
     }


     public void giveEffect(L1PcInstance pc) {
       for (L1ArmorSetEffect effect : this._effects) {
         effect.giveEffect(pc);
       }
     }


     public final boolean isValid(L1PcInstance pc) {
       return (pc.getInventory().checkEquipped(this._ids) && pc.getInventory().checkEquipped(get_source_effects().get_main_id()));
     }


     public boolean isPartOfSet(int id) {
       if (id == get_source_effects().get_main_id()) {
         return true;
       }
       for (int i : this._ids) {
         if (id == i) {
           return true;
         }
       }
       return false;
     }


     public boolean isEquippedRingOfArmorSet(L1PcInstance pc) {
       L1PcInventory pcInventory = pc.getInventory();
       L1ItemInstance armor = null;
       boolean isSetContainRing = false;

       for (int id : this._ids) {
         armor = pcInventory.findItemId(id);
         if (armor.getItem().getType2() == 2 && armor
           .getItem().getType() == 9) {
           isSetContainRing = true;

           break;
         }
       }
       if (armor != null && isSetContainRing) {
         int itemId = armor.getItem().getItemId();
         if (pcInventory.getTypeEquipped(2, 9) >= 2) {
           L1ItemInstance[] ring = new L1ItemInstance[4];
           ring = pcInventory.getRingEquipped();
           if (ring != null && ring.length > 0) {
             int count = 0;
             for (L1ItemInstance item : ring) {
               if (item != null)
               {
                 if (item.getItemId() == itemId)
                   count++;  }
             }
             if (count >= 2)
               return true;
           }
         }
       }
       return false;
     } }

 }


