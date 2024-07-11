 package l1j.server.server.model;

 import java.sql.Timestamp;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.HashMap;
 import java.util.List;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_AVAILABLE_SPELL_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EXP_BOOSTING_INFO_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_FOURTH_GEAR_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
 import l1j.server.MJTemplate.MJProto.MainServer_Client_UserForm.SC_USER_FORM_NOTI;
 import l1j.server.server.datatables.SkillsTable;
 import l1j.server.server.model.Instance.L1DollInstance;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.serverpackets.S_Ability;
 import l1j.server.server.serverpackets.S_HPUpdate;
 import l1j.server.server.serverpackets.S_Invis;
 import l1j.server.server.serverpackets.S_Liquor;
 import l1j.server.server.serverpackets.S_MPUpdate;
 import l1j.server.server.serverpackets.S_OwnCharAttrDef;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_RemoveObject;
 import l1j.server.server.serverpackets.S_SkillBrave;
 import l1j.server.server.serverpackets.S_SkillHaste;
 import l1j.server.server.serverpackets.S_SkillIconAura;
 import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Item;












 public class L1EquipmentSlot
 {
   private L1PcInstance _owner;
   private ArrayList<L1ArmorSet> _currentArmorSet;
   private ArrayList<L1ItemInstance> _weapons;
   private ArrayList<L1ItemInstance> _armors;
   private int weapons_idx = 0;
   public int worldjoin_weapon_idx = 0;

   public L1EquipmentSlot(L1PcInstance owner) {
     this._owner = owner;
     this._weapons = new ArrayList<>();
     this._armors = new ArrayList<>();
     this._currentArmorSet = new ArrayList<>();
   }

   private void setWeapon(L1ItemInstance weapon) {
     if (this._weapons.size() == 1) {
       this._owner.setSecondWeapon(weapon);
     } else {
       this._owner.setWeapon(weapon);
     }

     int itemId = weapon.getItem().getItemId();

     weapon.startEquipmentTimer(this._owner);
     this._weapons.add(weapon);

     if (this._weapons.size() == 2) {
       this._owner.setCurrentWeapon(88);
       this._owner.sendPackets((ServerBasePacket)new S_SkillSound(this._owner.getId(), 12534));
     } else {
       this._owner.setCurrentWeapon(weapon.getItem().getType1());
     }

     int type = weapon.getItem().getType();
     if ((type == 7 || type == 16 || type == 17) && weapon.get_bless_level() != 0) {
       this._owner.getAbility().addSp(weapon.get_bless_level());
     }

     if (type == 3 &&
       this._owner.isPassive(MJPassiveID.RAISING_WEAPON.toInt())) {
       this._owner.addAttackDelayRate(10.0D);
     }


     int weapontype = weapon.getItem().getType1();
     int enchantlv = weapon.getEnchantLevel();
     int hitbonus = 0;
     if (enchantlv > 0) {
       switch (enchantlv) { case 2:
           hitbonus = 1; break;
         case 4: hitbonus = 2; break;
         case 6: hitbonus = 3; break;
         case 8: hitbonus = 4; break;
         case 10: hitbonus = 5; break;
         case 12: hitbonus = 6; break;
         case 14: hitbonus = 7; break; }

       if (weapontype != 20 && weapontype != 62) {
         this._owner.addHitup(hitbonus);
       } else {
         this._owner.addBowHitup(hitbonus);
       }
     }

     if (itemId == 203003) {
       L1PolyMorph.doPoly((L1Character)this._owner, 12232, 0, 1, false, false);
     }

     if (this._owner.isSpearModeType()) {
       this._owner.setSpear_Mode_Type(false);
       SC_USER_FORM_NOTI.user_form_send(this._owner, this._owner.isSpearModeType());
     }

     weapon.on_skill_effect_icons();
   }






   public L1ItemInstance getWeapon() {
     return (this._weapons.size() > 0) ? this._weapons.get(this._weapons.size() - 1) : null;
   }






   public L1ItemInstance getWeaponSwap() {
     if (this._weapons.size() > 0) {
       if (this._weapons.size() > 1) {
         return this._weapons.get(this.weapons_idx++ % 2);
       }
       return this._weapons.get(0);
     }
     return null;
   }

   public boolean isWeapon(L1ItemInstance weapon) {
     return this._weapons.contains(weapon);
   }

   public int getWeaponCount() {
     return this._weapons.size();
   }

   public List<L1ItemInstance> getWeapons() {
     return new ArrayList<>(this._weapons);
   }

   private void setArmor(L1ItemInstance armor) {
     int itemId = armor.getItem().getItemId();

     if (itemId == 900111) {
       this._owner.setSkillEffect(8463, -1L);
     }

     int addac = armor.getAc() - armor.getAcByEnchantLevel() - armor.getAcByMagic() - armor.getaddAc() + armor.get_durability();
     if (addac != 0) {
       this._owner.getAC().addAc(addac);
     }

     this._armors.add(armor);
     armor.on_skill_effect_icons();

     for (L1ArmorSet armorSet : L1ArmorSet.getAllSet()) {
       if (armorSet.isPartOfSet(itemId) && armorSet.isValid(this._owner)) {
         if (armor.getItem().getType2() == 2 && armor.getItem().getType() == 9) {
           if (!armorSet.isEquippedRingOfArmorSet(this._owner)) {
             armorSet.giveEffect(this._owner);
             this._currentArmorSet.add(armorSet);
           }  continue;
         }
         armorSet.giveEffect(this._owner);
         this._currentArmorSet.add(armorSet);
       }
     }


     if (itemId == 900022 &&
       this._owner.getMapId() >= 1708 && this._owner.getMapId() <= 1712) {
       this._owner.sendPackets((ServerBasePacket)new S_SkillSound(this._owner.getId(), 11101));
       this._owner.sendPackets((ServerBasePacket)new S_PacketBox(180, 484, true));
     }


     if (itemId == 423014) {
       this._owner.startAHRegeneration();
     }
     if (itemId == 423015) {
       this._owner.startSHRegeneration();
     }
     if (itemId == 20380) {
       this._owner.startHalloweenRegeneration();
     }
     if ((itemId == 20077 || itemId == 20062 || itemId == 120077) &&
       !this._owner.hasSkillEffect(60) && !this._owner.is_combat_field()) {
       this._owner.killSkillEffectTimer(97);
       this._owner.setSkillEffect(60, -1L);
       this._owner.sendPackets((ServerBasePacket)new S_Invis(this._owner.getId(), 1));
       this._owner.broadcastPacketForFindInvis((ServerBasePacket)new S_Invis(this._owner.getId(), 1), true);
       this._owner.broadcastPacketForFindInvis((ServerBasePacket)new S_RemoveObject((L1Object)this._owner), false);
       this._owner.broadcastPacketForFindInvis(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this._owner), true);

       L1DollInstance doll = this._owner.getMagicDoll();
       if (doll != null) {
         for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)doll)) {
           doll.onPerceive(pc);
         }
       }
     }

     if (itemId == 20288 || itemId == 900111) {
       this._owner.sendPackets((ServerBasePacket)new S_Ability(1, true));
     }
     if (itemId == 20281) {
       this._owner.sendPackets((ServerBasePacket)new S_Ability(2, true));
     }
     if (itemId == 20036) {
       this._owner.sendPackets((ServerBasePacket)new S_Ability(3, true));
     }
     if (itemId == 20284) {
       this._owner.sendPackets((ServerBasePacket)new S_Ability(5, true));
     }
     if (itemId == 20207) {
       this._owner.sendPackets((ServerBasePacket)new S_SkillIconBlessOfEva(this._owner.getId(), -1));
     }

     if (itemId == 20383 &&
       armor.getChargeCount() != 0) {
       armor.setChargeCount(armor.getChargeCount() - 1);
       this._owner.getInventory().updateItem(armor, 128);
     }


     if (itemId >= 900263 && itemId <= 900265) {
       this._owner.set_halpas_armor(true);
       this._owner.set_halpas_armor_enchant(armor.getEnchantLevel());
       this._owner.setHalpasArmor(armor);
       if (this._owner.ishalpaspaith()) {
         long endTime;
         L1SkillUse.on_icons(this._owner, 8003, -1);


         if (armor.getHalpas_Time() != null) {
           endTime = Timestamp.valueOf(armor.getHalpas_Time().toString()).getTime();
         } else {
           endTime = 0L;
         }
         long currentTime = System.currentTimeMillis();
         int remainTime = Long.valueOf((endTime - currentTime) / 1000L).intValue();
         if (endTime - currentTime > 0L) {
           L1SkillUse.off_icons(this._owner, 8003);
           L1SkillUse.on_icons(this._owner, 8001, remainTime);
         }
       }
     }

     if (itemId == 22200 || itemId == 22201 || itemId == 22202 || itemId == 22203) {
       this._owner.startPapuBlessing();
     } else if (itemId >= 22208 && itemId <= 22211) {
       this._owner.startValaBlessing();
     }
     armor.startEquipmentTimer(this._owner);
   }

   public ArrayList<L1ItemInstance> getArmors() {
     return this._armors;
   }

   private void removeWeapon(L1ItemInstance weapon) {
     this._weapons.remove(weapon);

     int itemId = weapon.getItem().getItemId();

     if (weapon.getEnchantMagic() != 0) {
       this._owner.sendPackets((ServerBasePacket)new S_PacketBox(154, 0, weapon.getEnchantMagic(), this._weapons.size()));
     }

     weapon.stopEquipmentTimer(this._owner);

     if (this._owner.hasSkillEffect(92)) {
       this._owner.removeSkillEffect(92);
     }
     if (this._owner.hasSkillEffect(136)) {
       this._owner.removeSkillEffect(136);
     }
     if (this._owner.hasSkillEffect(91)) {
       this._owner.removeSkillEffect(91);
     }
     if (this._owner.hasSkillEffect(245)) {
       this._owner.removeSkillEffect(245);
     }

     if (this._weapons.size() == 1) {
       this._owner.setSecondWeapon(null);
       this._owner.setCurrentWeapon(getWeapon().getItem().getType1());
     } else {
       this._owner.setWeapon(null);
       this._owner.setCurrentWeapon(0);
     }

     if (itemId == 203003) {
       L1PolyMorph.undoPoly((L1Character)this._owner);
     }

     int type = weapon.getItem().getType();
     if ((type == 7 || type == 16 || type == 17) && weapon.get_bless_level() != 0) {
       this._owner.getAbility().addSp(-weapon.get_bless_level());
     }

     if (type == 3 &&
       this._owner.isPassive(MJPassiveID.RAISING_WEAPON.toInt())) {
       this._owner.addAttackDelayRate(-10.0D);
     }


     int weapontype = weapon.getItem().getType1();
     int enchantlv = weapon.getEnchantLevel();
     int hitbonus = 0;
     if (enchantlv > 0) {
       switch (enchantlv) { case 2:
           hitbonus = 1; break;
         case 4: hitbonus = 2; break;
         case 6: hitbonus = 3; break;
         case 8: hitbonus = 4; break;
         case 10: hitbonus = 5; break;
         case 12: hitbonus = 6; break;
         case 14: hitbonus = 7; break; }

       if (weapontype != 20 && weapontype != 62) {
         this._owner.addHitup(-hitbonus);
       } else {
         this._owner.addBowHitup(-hitbonus);
       }
     }

     if (this._owner.isSpearModeType()) {
       this._owner.setSpear_Mode_Type(false);
       SC_USER_FORM_NOTI.user_form_send(this._owner, this._owner.isSpearModeType());
     }
   }

   private void removeArmor(L1ItemInstance armor) {
     int itemId = armor.getItem().getItemId();

     if (itemId == 900111) {
       this._owner.removeSkillEffect(8463);
     }

     int addac = armor.getAc() - armor.getAcByEnchantLevel() - armor.getAcByMagic() - armor.getaddAc() + armor.get_durability();
     if (addac != 0) {
       this._owner.getAC().addAc(-addac);
     }












     removeSetItems(itemId);

     if (itemId == 900022) {
       this._owner.sendPackets((ServerBasePacket)new S_PacketBox(180, 484, false));
     }

     if (itemId == 423014) {
       this._owner.stopAHRegeneration();
     }
     if (itemId == 423015) {
       this._owner.stopSHRegeneration();
     }
     if (itemId == 20380) {
       this._owner.stopHalloweenRegeneration();
     }
     if (itemId == 20077 || itemId == 20062 || itemId == 120077) {
       this._owner.delInvis();
     }
     if (itemId == 20288 || itemId == 900111) {
       this._owner.sendPackets((ServerBasePacket)new S_Ability(1, false));
     }
     if (itemId == 20281) {
       this._owner.sendPackets((ServerBasePacket)new S_Ability(2, false));
     }
     if (itemId == 20036) {
       this._owner.sendPackets((ServerBasePacket)new S_Ability(3, false));
     }
     if (itemId == 20284) {
       this._owner.sendPackets((ServerBasePacket)new S_Ability(5, false));
     }
     if (itemId == 20207) {
       this._owner.sendPackets((ServerBasePacket)new S_SkillIconBlessOfEva(this._owner.getId(), 0));
     }

     if (itemId >= 900263 && itemId <= 900265) {
       long endTime; this._owner.set_halpas_armor(false);
       this._owner.set_halpas_armor_enchant(0);
       this._owner.removeHalpasArmor();

       if (armor.getHalpas_Time() != null) {
         endTime = Timestamp.valueOf(armor.getHalpas_Time().toString()).getTime();
       } else {
         endTime = 0L;
       }
       long currentTime = System.currentTimeMillis();
       if (endTime - currentTime > 0L) {
         L1SkillUse.off_icons(this._owner, 8001);
       } else {
         L1SkillUse.off_icons(this._owner, 8003);
       }
     }


     if (itemId == 22200 || itemId == 22201 || itemId == 22202 || itemId == 22203) {
       this._owner.stopPapuBlessing();
     } else if (itemId >= 22208 && itemId <= 22211) {
       this._owner.stopValaBlessing();
     }
     armor.stopEquipmentTimer(this._owner);
     this._armors.remove(armor);
   }

   public void set(L1ItemInstance equipment) {
     L1Item item = equipment.getItem();

     if (item.getType2() == 0) {
       return;
     }

     if (this._owner.getResistance() == null) {
       this._owner.resetResistance();
     }
     if (equipment.getPVPDmgReducIgnore() != 0)
       this._owner.add_pvp_dmg_ignore(equipment.getPVPDmgReducIgnore());
     if (equipment.getPVPMDmgReducIgnore() != 0)
       this._owner.add_pvp_mdmg_ignore(equipment.getPVPMDmgReducIgnore());
     if (equipment.getPVPMdmgReduction() != 0)
       this._owner.add_pvp_mdmg(equipment.getPVPMdmgReduction());
     if (equipment.getImmuneIgnore() != 0) {
       this._owner.add_immune_ignore(equipment.getImmuneIgnore());
     }
     if (equipment.getDmgModifier() != 0)
       this._owner.addDmgup(equipment.getDmgModifier());
     if (equipment.getMagicDmgModifier() != 0)
       this._owner.addMagicDmgup(equipment.getMagicDmgModifier());
     if (equipment.getHitModifier() != 0)
       this._owner.addHitup(equipment.getHitModifier());
     if (equipment.getBowDmgModifier() != 0)
       this._owner.addBowDmgup(equipment.getBowDmgModifier());
     if (equipment.getBowHitModifier() != 0)
       this._owner.addBowHitup(equipment.getBowHitModifier());
     if (equipment.getPvPDamage() != 0)
       this._owner.getResistance().addPVPweaponTotalDamage(equipment.getPvPDamage());
     if (equipment.getPvpReduction() != 0) {
       this._owner.getResistance().addcalcPcDefense(equipment.getPvpReduction());
     }
     if (equipment.getStr() != 0)
       this._owner.getAbility().addAddedStr(equipment.getStr());
     if (equipment.getCon() != 0)
       this._owner.getAbility().addAddedCon(equipment.getCon());
     if (equipment.getDex() != 0)
       this._owner.getAbility().addAddedDex(equipment.getDex());
     if (equipment.getInt() != 0)
       this._owner.getAbility().addAddedInt(equipment.getInt());
     if (equipment.getWis() != 0)
       this._owner.getAbility().addAddedWis(equipment.getWis());
     if (equipment.getCha() != 0)
       this._owner.getAbility().addAddedCha(equipment.getCha());
     if (equipment.getItem().getAttackDelayRate() != 0.0D)
       this._owner.addAttackDelayRate(equipment.getItem().getAttackDelayRate());
     if (equipment.getItem().getMoveDelayRate() != 0.0D) {
       this._owner.addMoveDelayRate(equipment.getItem().getMoveDelayRate());
     }
     if (equipment.getaddHp() != 0) {
       this._owner.addMaxHp(equipment.getaddHp());
       this._owner.sendPackets((ServerBasePacket)new S_HPUpdate(this._owner.getCurrentHp(), this._owner.getMaxHp()));
     }

     if (equipment.getHp() != 0) {
       this._owner.addMaxHp(equipment.getHp());
       this._owner.sendPackets((ServerBasePacket)new S_HPUpdate(this._owner.getCurrentHp(), this._owner.getMaxHp()));
     }

     if (equipment.getMp() != 0) {
       this._owner.addMaxMp(equipment.getMp());
       this._owner.sendPackets((ServerBasePacket)new S_MPUpdate(this._owner.getCurrentMp(), this._owner.getMaxMp()));
     }

     if (equipment.getHpPercent() != 0) {
       int hpperchent = (int)Math.round(this._owner.getBaseMaxHp() * equipment.getHpPercent() * 0.01D);
       this._owner.addMaxHp(hpperchent);
       this._owner.sendPackets((ServerBasePacket)new S_HPUpdate(this._owner.getCurrentHp(), this._owner.getMaxHp()));
     }

     if (equipment.getMpPercent() != 0) {
       int mpperchent = (int)Math.round(this._owner.getBaseMaxMp() * equipment.getMpPercent() * 0.01D);
       this._owner.addMaxMp(mpperchent);
       this._owner.sendPackets((ServerBasePacket)new S_MPUpdate(this._owner.getCurrentMp(), this._owner.getMaxMp()));
     }

     if (equipment.getHpr() != 0)
       this._owner.addHpr(equipment.getHpr());
     if (equipment.getMpr() != 0) {
       this._owner.addMpr(equipment.getMpr());
     }
     if (equipment.getExpByItem() != 0) {
       this._owner.add_item_exp_bonus(equipment.getExpByItem());
       SC_EXP_BOOSTING_INFO_NOTI.send(this._owner);
     }

     if (equipment.getMr() != 0) {
       this._owner.getResistance().addMr(equipment.getMr());
     }

     if (equipment.getSp() != 0) {
       this._owner.getAbility().addSp(equipment.getSp());
     }

     if (equipment.getMagicHitRate() != 0) {
       this._owner.addBaseMagicHitUp(equipment.getMagicHitRate());
     }
     if (equipment.getTitanPercent() != 0) {
       this._owner.add락구간상승(equipment.getTitanPercent());
     }
     if (equipment.getFoeDmg() != 0) {
       this._owner.addFouDmg(equipment.getFoeDmg());
     }
     if (equipment.getDamageReduction() != 0) {
       this._owner.addDamageReductionByArmor(equipment.getDamageReduction());
     }
     if (equipment.getWeightReduction() != 0) {
       this._owner.addWeightReduction(equipment.getWeightReduction());
     }
     if (equipment.getTechniqueTolerance() != 0) {
       this._owner.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, equipment.getTechniqueTolerance());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getSpiritTolerance() != 0) {
       this._owner.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, equipment.getSpiritTolerance());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getDragonLangTolerance() != 0) {
       this._owner.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, equipment.getDragonLangTolerance());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getFearTolerance() != 0) {
       this._owner.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, equipment.getFearTolerance());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getAllTolerance() != 0) {
       this._owner.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, equipment.getAllTolerance());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }

     if (equipment.getTechniqueHit() != 0) {
       this._owner.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, equipment.getTechniqueHit());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getSpiritHit() != 0) {
       this._owner.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, equipment.getSpiritHit());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getDragonLangHit() != 0) {
       this._owner.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, equipment.getDragonLangHit());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getFearHit() != 0) {
       this._owner.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, equipment.getFearHit());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getAllHit() != 0) {
       this._owner.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, equipment.getAllHit());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }

     if (equipment.getReductionCancel() != 0) {
       this._owner.addReducCancel(equipment.getReductionCancel());
     }
     if (equipment.getDG() != 0) {
       this._owner.addDg(equipment.getDG());
     }
     if (equipment.getTotalER() != 0) {
       this._owner.addEffectedER(equipment.getTotalER());
     }
     if (equipment.getDefenseFire() != 0) {
       this._owner.getResistance().addFire(equipment.getDefenseFire());
       this._owner.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this._owner));
     }
     if (equipment.getDefenseWater() != 0) {
       this._owner.getResistance().addWater(equipment.getDefenseWater());
       this._owner.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this._owner));
     }
     if (equipment.getDefenseWind() != 0) {
       this._owner.getResistance().addWind(equipment.getDefenseWind());
       this._owner.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this._owner));
     }
     if (equipment.getDefenseEarth() != 0) {
       this._owner.getResistance().addEarth(equipment.getDefenseEarth());
       this._owner.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this._owner));
     }
     if (equipment.getDefenseAll() != 0) {
       this._owner.getResistance().addAllNaturalResistance(equipment.getDefenseAll());
       this._owner.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this._owner));
     }

     if (equipment.getMagicDodge() != 0) {
       this._owner.addMagicDodgeProbability(equipment.getMagicDodge());
     }
     if (equipment.getAinEfficiency() != 0) {
       this._owner.addEinhasadBlessper(equipment.getAinEfficiency());
     }
     if (equipment.getShortCriticalValue() != 0) {
       this._owner.add_melee_critical_rate(equipment.getShortCriticalValue());
     }
     if (equipment.getLongCriticalValue() != 0) {
       this._owner.add_missile_critical_rate(equipment.getLongCriticalValue());
     }
     if (equipment.getMagicCriticalValue() != 0) {
       this._owner.add_magic_critical_rate(equipment.getMagicCriticalValue());
     }
     if (equipment.isHasteItem()) {
       this._owner.addHasteItemEquipped(1);
       this._owner.removeHasteSkillEffect();
       if (this._owner.getMoveSpeed() != 1) {
         this._owner.setMoveSpeed(1);
         this._owner.sendPackets((ServerBasePacket)new S_SkillHaste(this._owner.getId(), 1, -1));
         this._owner.broadcastPacket((ServerBasePacket)new S_SkillHaste(this._owner.getId(), 1, 0));
       }
     }
     if (equipment.getItemId() == 900010) {
       this._owner.addThreeItemEquipped(1);
       this._owner.removeThreeSkillEffect();
       this._owner.sendPackets((ServerBasePacket)new S_Liquor(this._owner.getId(), 8));
       this._owner.broadcastPacket((ServerBasePacket)new S_Liquor(this._owner.getId(), 8));
       this._owner.setPearl(1);
       SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
       noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.NEW);
       noti.set_spell_id(22017);
       noti.set_duration(-1);
       noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
       noti.set_on_icon_id(5393);
       noti.set_off_icon_id(5393);
       noti.set_icon_priority(10);
       noti.set_tooltip_str_id(1065);
       noti.set_new_str_id(1065);
       noti.set_end_str_id(0);
       noti.set_is_good(true);
       this._owner.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);

       this._owner.setSkillEffect(1000, -1L);
       int objId = this._owner.getId();
       this._owner.sendPackets((ServerBasePacket)new S_SkillBrave(objId, 1, -1));
       this._owner.broadcastPacket((ServerBasePacket)new S_SkillBrave(objId, 1, 0));
       this._owner.setBraveSpeed(1);

       this._owner.setFourgear(true);
       this._owner.sendPackets(SC_FOURTH_GEAR_NOTI.Fourth_Gear(this._owner));
       this._owner.broadcastPacket(SC_FOURTH_GEAR_NOTI.Fourth_Gear(this._owner));
     }

     if (equipment.getCCIncrease() != 0) {
       this._owner.add_CC_Increase(equipment.getCCIncrease());
     }
     if (equipment.getAbnormalStatusPvpDamageReduction() != 0) {
       this._owner.addAbnormalStatusPvPReduction(equipment.getCCIncrease());
     }

     this._owner.getEquipSlot().setMagicHelm(equipment);

     if (item.getType2() == 1) {
       setWeapon(equipment);
     } else if (item.getType2() == 2) {
       setArmor(equipment);
       if (item.getType() == 2) {
         this._owner.setAmory(equipment);
       }
     }
   }

   public void remove(L1ItemInstance equipment) {
     L1Item item = equipment.getItem();



















     equipment.off_skill_effect_icons();


     if (item.getType2() == 0) {
       return;
     }

     if (equipment.getPVPDmgReducIgnore() != 0)
       this._owner.add_pvp_dmg_ignore(-equipment.getPVPDmgReducIgnore());
     if (equipment.getPVPMDmgReducIgnore() != 0)
       this._owner.add_pvp_mdmg_ignore(-equipment.getPVPMDmgReducIgnore());
     if (equipment.getPVPMdmgReduction() != 0)
       this._owner.add_pvp_mdmg(-equipment.getPVPMdmgReduction());
     if (equipment.getImmuneIgnore() != 0) {
       this._owner.add_immune_ignore(-equipment.getImmuneIgnore());
     }
     if (equipment.getDmgModifier() != 0)
       this._owner.addDmgup(-equipment.getDmgModifier());
     if (equipment.getMagicDmgModifier() != 0)
       this._owner.addMagicDmgup(-equipment.getMagicDmgModifier());
     if (equipment.getHitModifier() != 0)
       this._owner.addHitup(-equipment.getHitModifier());
     if (equipment.getBowDmgModifier() != 0)
       this._owner.addBowDmgup(-equipment.getBowDmgModifier());
     if (equipment.getBowHitModifier() != 0)
       this._owner.addBowHitup(-equipment.getBowHitModifier());
     if (equipment.getPvPDamage() != 0)
       this._owner.getResistance().addPVPweaponTotalDamage(-equipment.getPvPDamage());
     if (equipment.getPvpReduction() != 0) {
       this._owner.getResistance().addcalcPcDefense(-equipment.getPvpReduction());
     }
     if (equipment.getStr() != 0)
       this._owner.getAbility().addAddedStr(-equipment.getStr());
     if (equipment.getCon() != 0)
       this._owner.getAbility().addAddedCon(-equipment.getCon());
     if (equipment.getDex() != 0)
       this._owner.getAbility().addAddedDex(-equipment.getDex());
     if (equipment.getInt() != 0)
       this._owner.getAbility().addAddedInt(-equipment.getInt());
     if (equipment.getWis() != 0)
       this._owner.getAbility().addAddedWis(-equipment.getWis());
     if (equipment.getCha() != 0)
       this._owner.getAbility().addAddedCha(-equipment.getCha());
     if (equipment.getItem().getAttackDelayRate() != 0.0D)
       this._owner.addAttackDelayRate(-equipment.getItem().getAttackDelayRate());
     if (equipment.getItem().getMoveDelayRate() != 0.0D) {
       this._owner.addMoveDelayRate(-equipment.getItem().getMoveDelayRate());
     }
     if (equipment.getaddHp() != 0) {
       this._owner.addMaxHp(-equipment.getaddHp());
       this._owner.sendPackets((ServerBasePacket)new S_HPUpdate(this._owner.getCurrentHp(), this._owner.getMaxHp()));
     }

     if (equipment.getHp() != 0) {
       this._owner.addMaxHp(-equipment.getHp());
       this._owner.sendPackets((ServerBasePacket)new S_HPUpdate(this._owner.getCurrentHp(), this._owner.getMaxHp()));
     }

     if (equipment.getMp() != 0) {
       this._owner.addMaxMp(-equipment.getMp());
       this._owner.sendPackets((ServerBasePacket)new S_MPUpdate(this._owner.getCurrentMp(), this._owner.getMaxMp()));
     }

     if (equipment.getHpPercent() != 0) {
       int hpperchent = (int)Math.round(this._owner.getBaseMaxHp() * equipment.getHpPercent() * 0.01D);
       this._owner.addMaxHp(-hpperchent);
       this._owner.sendPackets((ServerBasePacket)new S_HPUpdate(this._owner.getCurrentHp(), this._owner.getMaxHp()));
     }

     if (equipment.getMpPercent() != 0) {
       int mpperchent = (int)Math.round(this._owner.getBaseMaxMp() * equipment.getMpPercent() * 0.01D);
       this._owner.addMaxMp(-mpperchent);
       this._owner.sendPackets((ServerBasePacket)new S_MPUpdate(this._owner.getCurrentMp(), this._owner.getMaxMp()));
     }

     if (equipment.getHpr() != 0)
       this._owner.addHpr(-equipment.getHpr());
     if (equipment.getMpr() != 0) {
       this._owner.addMpr(-equipment.getMpr());
     }
     if (equipment.getExpByItem() != 0) {
       this._owner.add_item_exp_bonus(-equipment.getExpByItem());
       SC_EXP_BOOSTING_INFO_NOTI.send(this._owner);
     }

     if (equipment.getMr() != 0) {
       this._owner.getResistance().addMr(-equipment.getMr());
     }

     if (equipment.getSp() != 0) {
       this._owner.getAbility().addSp(-equipment.getSp());
     }

     if (equipment.getMagicHitRate() != 0) {
       this._owner.addBaseMagicHitUp(-equipment.getMagicHitRate());
     }
     if (equipment.getTitanPercent() != 0) {
       this._owner.add락구간상승(-equipment.getTitanPercent());
     }
     if (equipment.getFoeDmg() != 0) {
       this._owner.addFouDmg(-equipment.getFoeDmg());
     }
     if (equipment.getDamageReduction() != 0) {
       this._owner.addDamageReductionByArmor(-equipment.getDamageReduction());
     }
     if (equipment.getWeightReduction() != 0) {
       this._owner.addWeightReduction(-equipment.getWeightReduction());
     }
     if (equipment.getTechniqueTolerance() != 0) {
       this._owner.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, -equipment.getTechniqueTolerance());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getSpiritTolerance() != 0) {
       this._owner.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, -equipment.getSpiritTolerance());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getDragonLangTolerance() != 0) {
       this._owner.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, -equipment.getDragonLangTolerance());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getFearTolerance() != 0) {
       this._owner.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, -equipment.getFearTolerance());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getAllTolerance() != 0) {
       this._owner.addSpecialResistance(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -equipment.getAllTolerance());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }

     if (equipment.getTechniqueHit() != 0) {
       this._owner.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ABILITY, -equipment.getTechniqueHit());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getSpiritHit() != 0) {
       this._owner.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.SPIRIT, -equipment.getSpiritHit());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getDragonLangHit() != 0) {
       this._owner.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.DRAGON_SPELL, -equipment.getDragonLangHit());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getFearHit() != 0) {
       this._owner.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.FEAR, -equipment.getFearHit());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }
     if (equipment.getAllHit() != 0) {
       this._owner.addSpecialPierce(SC_SPECIAL_RESISTANCE_NOTI.eKind.ALL, -equipment.getAllHit());
       SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(this._owner);
     }

     if (equipment.getReductionCancel() != 0) {
       this._owner.addReducCancel(-equipment.getReductionCancel());
     }
     if (equipment.getDG() != 0) {
       this._owner.addDg(-equipment.getDG());
     }
     if (equipment.getTotalER() != 0) {
       this._owner.addEffectedER(-equipment.getTotalER());
     }
     if (equipment.getDefenseFire() != 0) {
       this._owner.getResistance().addFire(-equipment.getDefenseFire());
       this._owner.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this._owner));
     }
     if (equipment.getDefenseWater() != 0) {
       this._owner.getResistance().addWater(-equipment.getDefenseWater());
       this._owner.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this._owner));
     }
     if (equipment.getDefenseWind() != 0) {
       this._owner.getResistance().addWind(-equipment.getDefenseWind());
       this._owner.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this._owner));
     }
     if (equipment.getDefenseEarth() != 0) {
       this._owner.getResistance().addEarth(-equipment.getDefenseEarth());
       this._owner.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this._owner));
     }
     if (equipment.getDefenseAll() != 0) {
       this._owner.getResistance().addAllNaturalResistance(-equipment.getDefenseAll());
       this._owner.sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this._owner));
     }

     if (equipment.getMagicDodge() != 0) {
       this._owner.addMagicDodgeProbability(-equipment.getMagicDodge());
     }
     if (equipment.getAinEfficiency() != 0) {
       this._owner.addEinhasadBlessper(-equipment.getAinEfficiency());
     }
     if (equipment.getShortCriticalValue() != 0) {
       this._owner.add_melee_critical_rate(-equipment.getShortCriticalValue());
     }
     if (equipment.getLongCriticalValue() != 0) {
       this._owner.add_missile_critical_rate(-equipment.getLongCriticalValue());
     }
     if (equipment.getMagicCriticalValue() != 0) {
       this._owner.add_magic_critical_rate(-equipment.getMagicCriticalValue());
     }
     if (equipment.isHasteItem()) {
       this._owner.addHasteItemEquipped(-1);
       this._owner.removeHasteSkillEffect();
       if (this._owner.getMoveSpeed() != 0) {
         this._owner.setMoveSpeed(0);
         this._owner.sendPackets((ServerBasePacket)new S_SkillHaste(this._owner.getId(), 0, 0));
         this._owner.broadcastPacket((ServerBasePacket)new S_SkillHaste(this._owner.getId(), 0, 0));
       }
     }

     if (equipment.getItemId() == 900010) {
       this._owner.addThreeItemEquipped(-1);
       this._owner.removeThreeSkillEffect();
       this._owner.sendPackets((ServerBasePacket)new S_Liquor(this._owner.getId(), 0));
       this._owner.broadcastPacket((ServerBasePacket)new S_Liquor(this._owner.getId(), 0));
       this._owner.setPearl(0);

       this._owner.removeSkillEffect(1000);
       int objId = this._owner.getId();
       this._owner.sendPackets((ServerBasePacket)new S_SkillBrave(objId, 0, 0));
       this._owner.broadcastPacket((ServerBasePacket)new S_SkillBrave(objId, 0, 0));
       this._owner.setBraveSpeed(0);

       this._owner.setFourgear(false);
       this._owner.sendPackets(SC_FOURTH_GEAR_NOTI.Fourth_Gear(this._owner));
       this._owner.broadcastPacket(SC_FOURTH_GEAR_NOTI.Fourth_Gear(this._owner));
     }

     if (equipment.getCCIncrease() != 0) {
       this._owner.add_CC_Increase(-equipment.getCCIncrease());
     }
     if (equipment.getAbnormalStatusPvpDamageReduction() != 0) {
       this._owner.addAbnormalStatusPvPReduction(-equipment.getAbnormalStatusPvpDamageReduction());
     }

     this._owner.getEquipSlot().removeMagicHelm(this._owner.getId(), equipment);

     if (item.getType2() == 1) {
       removeWeapon(equipment);
       if (this._owner.hasSkillEffect(155)) {
         this._owner.sendPackets((ServerBasePacket)new S_SkillIconAura(154, 0));
         this._owner.removeSkillEffect(155);
       }

     } else if (item.getType2() == 2) {
       removeArmor(equipment);
       if (item.getType() == 2) {
         this._owner.setAmory(null);
       }
     }
   }






   private static final HashMap<Integer, List<Integer>> mItemSkillMapped = new HashMap<>(); static {
     mItemSkillMapped.put(Integer.valueOf(20013), Arrays.asList(new Integer[] { Integer.valueOf(26), Integer.valueOf(43) }));
     mItemSkillMapped.put(Integer.valueOf(20014), Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(19) }));
     mItemSkillMapped.put(Integer.valueOf(20015), Arrays.asList(new Integer[] { Integer.valueOf(13), Integer.valueOf(42), Integer.valueOf(12) }));
     mItemSkillMapped.put(Integer.valueOf(20008), Arrays.asList(new Integer[] { Integer.valueOf(43) }));
     mItemSkillMapped.put(Integer.valueOf(20023), Arrays.asList(new Integer[] { Integer.valueOf(54) }));
   }

   public void setMagicHelm(L1ItemInstance item) {
     List<Integer> skills = mItemSkillMapped.get(Integer.valueOf(item.getItemId()));
     if (skills == null || skills.size() <= 0) {
       return;
     }
     SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
     for (Integer skillId : skills) {
       noti.appendNewSpell(skillId.intValue(), true);
     }
     this._owner.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
   }

   public void removeMagicHelm(int objectId, L1ItemInstance item) {
     List<Integer> skills = mItemSkillMapped.get(Integer.valueOf(item.getItemId()));
     if (skills == null || skills.size() <= 0) {
       return;
     }
     SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
     for (Integer skillId : skills) {
       if (!SkillsTable.getInstance().spellCheck(objectId, skillId.intValue())) {
         noti.appendNewSpell(skillId.intValue(), false);
       }
     }
     if (noti.get_spell_info() != null && noti.get_spell_info().size() > 0) {
       this._owner.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
     }
   }






   public void removeSetItems(int itemId) {
     for (L1ArmorSet armorSet : L1ArmorSet.getAllSet()) {
       if (armorSet.isPartOfSet(itemId) && this._currentArmorSet.contains(armorSet) && !armorSet.isValid(this._owner)) {
         armorSet.cancelEffect(this._owner);
         this._currentArmorSet.remove(armorSet);
       }
     }
   }








   public void removeSetItemsbless(int itemId) {
     for (L1ArmorSet armorSet : L1ArmorSet.getAllSet()) {
       if (armorSet.isPartOfSet(itemId) && this._currentArmorSet.contains(armorSet) && armorSet.isValid(this._owner)) {
         armorSet.cancelEffect(this._owner);
         this._currentArmorSet.remove(armorSet);
       }
     }
   }
 }


