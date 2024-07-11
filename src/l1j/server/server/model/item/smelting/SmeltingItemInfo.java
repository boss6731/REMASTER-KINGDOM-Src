 package l1j.server.server.model.item.smelting;

 import java.util.ArrayList;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_HPUpdate;
 import l1j.server.server.serverpackets.S_MPUpdate;
 import l1j.server.server.serverpackets.S_OwnCharAttrDef;
 import l1j.server.server.serverpackets.S_SPMR;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class SmeltingItemInfo
 {
   private int _item_id;
   private String _item_name;
   private int _index;
   private int _smelting_scroll_id;
   private String _smelting_scroll_item_name;

   private static SmeltingItemInfo newInstance() {
     return new SmeltingItemInfo();
   }



   public int get_item_id() {
     return this._item_id;
   }



   public String get_item_name() {
     return this._item_name;
   }


   public int get_index() {
     return this._index;
   }

   public int get_smelting_scroll_id() {
     return this._smelting_scroll_id;
   }


   public String get_smelting_scroll_item_name() {
     return this._smelting_scroll_item_name;
   }
   private static ArrayList<Integer> smelting = new ArrayList<>();




   public static void smelting_option(L1PcInstance pc, L1ItemInstance item, boolean onOff) {
     if (item.getSmeltingValue() == 0) {
       onOff = false;
     } else if (item.getSmeltingValue() >= 1) {
       if (item.getSmeltingItemId1() != 0) {
         smelting.add(Integer.valueOf(item.getSmeltingItemId1()));
       }
       if (item.getSmeltingItemId2() != 0) {
         smelting.add(Integer.valueOf(item.getSmeltingItemId2()));
       }
     }

     int type = onOff ? 1 : -1;
     for (int i = 0; i < smelting.size(); i++) {
       SmeltingScrollInfo info = SmeltingScrollLoader.getInstance().getSmeltingScrollInfo(((Integer)smelting.get(i)).intValue());
       if (info.get_str() != 0) {
         pc.getAbility().addAddedStr(type * info.get_str());
       }
       if (info.get_dex() != 0) {
         pc.getAbility().addAddedDex(type * info.get_dex());
       }
       if (info.get_con() != 0) {
         pc.getAbility().addAddedCon(type * info.get_con());
       }
       if (info.get_wis() != 0) {
         pc.getAbility().addAddedWis(type * info.get_wis());
       }
       if (info.get_int() != 0) {
         pc.getAbility().addAddedInt(type * info.get_int());
       }
       if (info.get_cha() != 0) {
         pc.getAbility().addAddedCha(type * info.get_cha());
       }
       if (info.get_all_Stat() != 0) {
         pc.getAbility().addAddedStr(type * info.get_all_Stat());
         pc.getAbility().addAddedDex(type * info.get_all_Stat());
         pc.getAbility().addAddedInt(type * info.get_all_Stat());
         pc.getAbility().addAddedWis(type * info.get_all_Stat());
         pc.getAbility().addAddedCon(type * info.get_all_Stat());
         pc.getAbility().addAddedCha(type * info.get_all_Stat());
       }
       if (info.get_short_dmg() != 0) {
         pc.addDmgup(type * info.get_short_dmg());
       }
       if (info.get_long_dmg() != 0) {
         pc.addBowDmgup(type * info.get_long_dmg());
       }
       if (info.get_short_hit() != 0) {
         pc.addHitup(type * info.get_short_hit());
       }
       if (info.get_long_hit() != 0) {
         pc.addBowHitup(type * info.get_long_hit());
       }
       if (info.get_short_critical() != 0) {
         pc.add_melee_critical_rate(type * info.get_short_critical());
       }
       if (info.get_long_critical() != 0) {
         pc.add_missile_critical_rate(type * info.get_long_critical());
       }
       if (info.get_reduction() != 0) {
         pc.addDamageReductionByArmor(type * info.get_reduction());
       }
       if (info.get_pvp_dmg() != 0) {
         pc.getResistance().addPVPweaponTotalDamage(type * info.get_pvp_dmg());
       }
       if (info.get_pvp_reduction() != 0) {
         pc.getResistance().addcalcPcDefense(type * info.get_pvp_reduction());
       }
       if (info.get_dg() != 0) {
         pc.addDg(type * info.get_dg());
       }
       if (info.get_er() != 0) {
         pc.addEffectedER(type * info.get_er());
       }
       if (info.get_ac() != 0) {
         pc.getAC().addAc(type * info.get_ac() * -1);
       }

       if (info.get_mr() != 0) {
         pc.getResistance().addMr(type * info.get_mr());
       }



       if (info.get_sp() != 0) {
         pc.getAbility().addSp(type * info.get_sp());
       }

       if (info.get_weight() != 0) {
         pc.addWeightReduction(type * info.get_weight());
       }
       if (info.get_max_hp() != 0) {
         pc.addMaxHp(type * info.get_max_hp());
       }

       if (info.get_max_mp() != 0) {
         pc.addMaxMp(type * info.get_max_mp());
       }

       if (info.get_resis_fire() != 0) {
         pc.getResistance().addFire(type * info.get_resis_fire());
       }

       if (info.get_resis_earth() != 0) {
         pc.getResistance().addEarth(type * info.get_resis_earth());
       }

       if (info.get_resis_water() != 0) {
         pc.getResistance().addWater(type * info.get_resis_water());
       }

       if (info.get_resis_wind() != 0) {
         pc.getResistance().addWind(type * info.get_resis_wind());
       }

       if (info.get_resis_all() != 0) {
         pc.getResistance().addAllNaturalResistance(type * info.get_resis_all());
       }

       if (info.get_m_resis_ability() != 0) {
         pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, type * info.get_m_resis_ability());
       }

       if (info.get_m_resis_spirit() != 0) {
         pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, type * info.get_m_resis_spirit());
       }

       if (info.get_m_resis_dragon() != 0) {
         pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, type * info.get_m_resis_dragon());
       }

       if (info.get_m_resis_fear() != 0) {
         pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, type * info.get_m_resis_fear());
       }

       if (info.get_m_resis_all() != 0) {
         pc.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, type * info.get_m_resis_all());
       }

       if (info.get_m_hit_ability() < 0) {
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, type * info.get_m_hit_ability());
       }

       if (info.get_m_hit_spirit() != 0) {
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, type * info.get_m_hit_spirit());
       }

       if (info.get_m_hit_dragon() != 0) {
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, type * info.get_m_hit_dragon());
       }

       if (info.get_m_hit_fear() != 0) {
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, type * info.get_m_hit_fear());
       }

       if (info.get_m_hit_all() != 0) {
         pc.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, type * info.get_m_hit_all());
       }


       if (info.get_exp() != 0) {
         pc.add_item_exp_bonus((type * info.get_exp()));
       }

       if (info.get_magichit() != 0) {
         pc.addBaseMagicHitUp(type * info.get_magichit());
       }
       if (info.get_attackspeed() != 0) {
         pc.addAttackDelayRate((type * info.get_attackspeed()));
       }
       if (info.get_me() != 0) {
         pc.addMagicDodgeProbability(type * info.get_me());
       }
       if (info.get_magic_critical() != 0) {
         pc.add_magic_critical_rate(type * info.get_magic_critical());
       }

       if (info.get_dominion_tel() != 0) {
         pc.set_dominion_tel(type);
       }

       if (info.get_hpRegen() != 0) {
         pc.addHpAr(type * info.get_hpRegen());
       }
       if (info.get_mpRegen() != 0) {
         pc.addMpAr(type * info.get_mpRegen());
       }
       if (info.get_pvp_magic_reduc() != 0) {
         pc.add_Magic_defense_per(type * info.get_pvp_magic_reduc());
       }
       if (info.get_pvp_reduction_per() != 0) {
         pc.add_pvp_defense_per(type * info.get_pvp_reduction_per());
       }
       if (info.get_armor_magic_pro() != 0) {
         pc.add_armor_magic_pro(type * info.get_armor_magic_pro());
       }

       if (info.get_status_time_reduce() != 0) {
         pc.add_status_time_reduce(type * info.get_status_time_reduce());
       }
       if (info.get_portion_ration() != 0) {
         pc.addPotionRecoveryRatePct(type * info.get_portion_ration());
       }

       pc.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(pc));
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
       pc.sendPackets((ServerBasePacket)new S_HPUpdate(pc));
       pc.sendPackets((ServerBasePacket)new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
       pc.sendPackets((ServerBasePacket)new S_SPMR(pc));
     }
     smelting.clear();
   }
 }


