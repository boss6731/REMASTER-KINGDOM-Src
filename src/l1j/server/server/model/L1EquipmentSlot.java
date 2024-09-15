package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.STATUS_BRAVE;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.net.ntp.TimeStamp;

import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_AVAILABLE_SPELL_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_EXP_BOOSTING_INFO_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_FOURTH_GEAR_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_UserForm.SC_USER_FORM_NOTI;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
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
import l1j.server.server.templates.L1Item;

public class L1EquipmentSlot {

	private L1PcInstance _owner;

	private ArrayList<L1ArmorSet> _currentArmorSet;

	private ArrayList<L1ItemInstance> _weapons;

	private ArrayList<L1ItemInstance> _armors;

	private int weapons_idx = 0;
	public int worldjoin_weapon_idx = 0;

	public L1EquipmentSlot(L1PcInstance owner) {
		_owner = owner;
		_weapons = new ArrayList<L1ItemInstance>();
		_armors = new ArrayList<L1ItemInstance>();
		_currentArmorSet = new ArrayList<L1ArmorSet>();
	}

	private void setWeapon(L1ItemInstance weapon) {// 무기류
		if (_weapons.size() == 1) {
			_owner.setSecondWeapon(weapon);
		} else {
			_owner.setWeapon(weapon);
		}

		int itemId = weapon.getItem().getItemId();
		
		weapon.startEquipmentTimer(_owner);
		_weapons.add(weapon);

		if (_weapons.size() == 2) {
			_owner.setCurrentWeapon(88);
			_owner.sendPackets(new S_SkillSound(_owner.getId(), 12534));
		} else {
			_owner.setCurrentWeapon(weapon.getItem().getType1());
		}

		int type = weapon.getItem().getType();
		if ((type == 7 || type == 16 || type == 17) && (weapon.get_bless_level() != 0)) {
			_owner.getAbility().addSp(weapon.get_bless_level());
		}
		
		if (type == 3) {
			if (_owner.isPassive(MJPassiveID.RAISING_WEAPON.toInt())) {
				_owner.addAttackDelayRate(10);
			}
		}
		
		int weapontype = weapon.getItem().getType1();
		int enchantlv = weapon.getEnchantLevel();
		int hitbonus = 0;
		if (enchantlv > 0) {
			switch (enchantlv) {
				case 2:		hitbonus = 1;		break;
				case 4:		hitbonus = 2;		break;
				case 6:		hitbonus = 3;		break;
				case 8:		hitbonus = 4;		break;
				case 10:	hitbonus = 5;		break;
				case 12:	hitbonus = 6;		break;
				case 14:	hitbonus = 7;		break;
			}
			if (weapontype != 20 && weapontype != 62) {
				_owner.addHitup(hitbonus);
			}else{
				_owner.addBowHitup(hitbonus);
			}
		}

		if (itemId == 203003) {
			L1PolyMorph.doPoly(_owner, 12232, 0, L1PolyMorph.MORPH_BY_ITEMMAGIC, false, false);
		}
		
		if (_owner.isSpearModeType()) {
			_owner.setSpear_Mode_Type(false);
			SC_USER_FORM_NOTI.user_form_send(_owner, _owner.isSpearModeType());
		}
		
		weapon.on_skill_effect_icons();
	}

	/**
	 * 착용중인 무기에 젤 마지막 무기를 리턴.
	 * 
	 * @return
	 */
	public L1ItemInstance getWeapon() {
		return _weapons.size() > 0 ? _weapons.get(_weapons.size() - 1) : null;
	}

	/**
	 * 착용중인 무기를 번갈아 가면서 리턴.
	 * 
	 * @return
	 */
	public L1ItemInstance getWeaponSwap() {
		if (_weapons.size() > 0) {
			if (_weapons.size() > 1)
				return _weapons.get(weapons_idx++ % 2);
			else
				return _weapons.get(0);
		}
		return null;
	}

	public boolean isWeapon(L1ItemInstance weapon) {
		return _weapons.contains(weapon);
	}

	public int getWeaponCount() {
		return _weapons.size();
	}

	public List<L1ItemInstance> getWeapons() {
		return new ArrayList<L1ItemInstance>(_weapons);
	}
	
	private void setArmor(L1ItemInstance armor) {	
		int itemId = armor.getItem().getItemId();

		if(itemId == 900111){
			_owner.setSkillEffect(L1SkillId.TELEPORT_RULER, -1);
		}
		
		int addac = armor.getAc() - armor.getAcByEnchantLevel() - armor.getAcByMagic() - armor.getaddAc() + armor.get_durability();
		if (addac != 0) {
			_owner.getAC().addAc(addac);
		}

		_armors.add(armor);
		armor.on_skill_effect_icons();
		
		for (L1ArmorSet armorSet : L1ArmorSet.getAllSet()) {
			if (armorSet.isPartOfSet(itemId) && armorSet.isValid(_owner)) {
				if (armor.getItem().getType2() == 2 && armor.getItem().getType() == 9) {
					if (!armorSet.isEquippedRingOfArmorSet(_owner)) {
						armorSet.giveEffect(_owner);
						_currentArmorSet.add(armorSet);
					}
				} else {
					armorSet.giveEffect(_owner);
					_currentArmorSet.add(armorSet);
				}
			}
		}
		
		if (itemId == 900022) {
			if (_owner.getMapId() >= 1708 && _owner.getMapId() <= 1712) {
				_owner.sendPackets(new S_SkillSound(_owner.getId(), 11101));
				_owner.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 484, true));
			}
		}

		if (itemId == 423014) {
			_owner.startAHRegeneration();
		}
		if (itemId == 423015) {
			_owner.startSHRegeneration();
		}
		if (itemId == 20380) {
			_owner.startHalloweenRegeneration();
		}
		if (itemId == 20077 || itemId == 20062 || itemId == 120077) {
			if (!_owner.hasSkillEffect(L1SkillId.INVISIBILITY) && !_owner.is_combat_field()) {
				_owner.killSkillEffectTimer(L1SkillId.BLIND_HIDING);
				_owner.setSkillEffect(L1SkillId.INVISIBILITY, -1);
				_owner.sendPackets(new S_Invis(_owner.getId(), 1));
				_owner.broadcastPacketForFindInvis(new S_Invis(_owner.getId(), 1), true);
				_owner.broadcastPacketForFindInvis(new S_RemoveObject(_owner), false);
				_owner.broadcastPacketForFindInvis(SC_WORLD_PUT_OBJECT_NOTI.make_stream(_owner), true);
				
				L1DollInstance doll = _owner.getMagicDoll();
				if (doll != null) {
					for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(doll)) {
						doll.onPerceive(pc);
					}
				}
			}
		}
		if (itemId == 20288 || itemId == 900111) {
			_owner.sendPackets(new S_Ability(1, true));
		}
		if (itemId == 20281) {
			_owner.sendPackets(new S_Ability(2, true));
		}
		if (itemId == 20036) {
			_owner.sendPackets(new S_Ability(3, true));
		}
		if (itemId == 20284) {
			_owner.sendPackets(new S_Ability(5, true));
		}
		if (itemId == 20207) {
			_owner.sendPackets(new S_SkillIconBlessOfEva(_owner.getId(), -1));
		}

		if (itemId == 20383) {
			if (armor.getChargeCount() != 0) {
				armor.setChargeCount(armor.getChargeCount() - 1);
				_owner.getInventory().updateItem(armor, L1PcInventory.COL_CHARGE_COUNT);
			}
		}
		
		if (itemId >= 900263 && itemId <= 900265) {
			_owner.set_halpas_armor(true);
			_owner.set_halpas_armor_enchant(armor.getEnchantLevel());
			_owner.setHalpasArmor(armor);
			if (_owner.ishalpaspaith()){
				
				L1SkillUse.on_icons(_owner, L1SkillId.DRAGON_ARMOR_EQUIP, -1);
				
				long endTime;
				if (armor.getHalpas_Time() !=null) {
					endTime = Timestamp.valueOf(armor.getHalpas_Time().toString()).getTime();
				} else {
					endTime = 0;
				}
				long currentTime = System.currentTimeMillis();
				int remainTime = Long.valueOf((endTime - currentTime)/1000).intValue();
				if (endTime - currentTime > 0) {
					L1SkillUse.off_icons(_owner, L1SkillId.DRAGON_ARMOR_EQUIP);
					L1SkillUse.on_icons(_owner, L1SkillId.DRAGON_ARMOR_BLESSING, remainTime);
				}
			}
		}
		
		if (itemId == 22200 || itemId == 22201 || itemId == 22202 || itemId == 22203) {// 파푸리온
			_owner.startPapuBlessing();
		} else if (itemId >= 22208 && itemId <= 22211)
			_owner.startValaBlessing();

		armor.startEquipmentTimer(_owner);
	}

	public ArrayList<L1ItemInstance> getArmors() {
		return _armors;
	}

	private void removeWeapon(L1ItemInstance weapon) {
		_weapons.remove(weapon);
		
		int itemId = weapon.getItem().getItemId();
		
		if (weapon.getEnchantMagic() != 0) {
			_owner.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, 0, weapon.getEnchantMagic(), _weapons.size()));
		}
				
		weapon.stopEquipmentTimer(_owner);

		if (_owner.hasSkillEffect(L1SkillId.ABSOLUTE_BLADE)) {
			_owner.removeSkillEffect(L1SkillId.ABSOLUTE_BLADE);
		}
		if (_owner.hasSkillEffect(L1SkillId.INFERNO)) {
			_owner.removeSkillEffect(L1SkillId.INFERNO);
		}
		if (_owner.hasSkillEffect(L1SkillId.COUNTER_BARRIER)) {
			_owner.removeSkillEffect(L1SkillId.COUNTER_BARRIER);
		}
		if (_owner.hasSkillEffect(L1SkillId.HALPAS)) {
			_owner.removeSkillEffect(L1SkillId.HALPAS);
		}
		
		if (_weapons.size() == 1) {
			_owner.setSecondWeapon(null);
			_owner.setCurrentWeapon(getWeapon().getItem().getType1());
		} else {
			_owner.setWeapon(null);
			_owner.setCurrentWeapon(0);
		}

		if (itemId == 203003) { // 데스나이트의 불검:진
			L1PolyMorph.undoPoly(_owner);
		}

		int type = weapon.getItem().getType();
		if ((type == 7 || type == 16 || type == 17) && (weapon.get_bless_level() != 0)) {
			_owner.getAbility().addSp(-weapon.get_bless_level());
		}
		
		if (type == 3) {
			if (_owner.isPassive(MJPassiveID.RAISING_WEAPON.toInt())) {
				_owner.addAttackDelayRate(-10);
			}
		}
		
		int weapontype = weapon.getItem().getType1();
		int enchantlv = weapon.getEnchantLevel();
		int hitbonus = 0;
		if (enchantlv > 0) {
			switch (enchantlv) {
				case 2:		hitbonus = 1;		break;
				case 4:		hitbonus = 2;		break;
				case 6:		hitbonus = 3;		break;
				case 8:		hitbonus = 4;		break;
				case 10:	hitbonus = 5;		break;
				case 12:	hitbonus = 6;		break;
				case 14:	hitbonus = 7;		break;
			}
			if (weapontype != 20 && weapontype != 62) {
				_owner.addHitup(-hitbonus);
			}else{
				_owner.addBowHitup(-hitbonus);
			}
		}
		
		if (_owner.isSpearModeType()) {
			_owner.setSpear_Mode_Type(false);
			SC_USER_FORM_NOTI.user_form_send(_owner, _owner.isSpearModeType());
		}
	}

	private void removeArmor(L1ItemInstance armor) {
		int itemId = armor.getItem().getItemId();
		
		if(itemId == 900111){
			_owner.removeSkillEffect(L1SkillId.TELEPORT_RULER);
		}
		
		int addac = armor.getAc() - armor.getAcByEnchantLevel() - armor.getAcByMagic() - armor.getaddAc() + armor.get_durability();
		if (addac != 0) {
			_owner.getAC().addAc(-addac);
		}

//		if (armor.hasSkillEffectTimer(L1SkillId.BLESSED_ARMOR)) {
//			L1SkillUse.off_icons(_owner, BLESSED_ARMOR);
//			/*SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
//			noti.set_noti_type(eNotiType.END);
//			noti.set_spell_id(BLESSED_ARMOR);
//			noti.set_duration(0);
//			noti.set_end_str_id(2240);
//			noti.set_is_good(true);
//			_owner.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);*/
//		}

		removeSetItems(itemId);

		if (itemId == 900022) {
			_owner.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON1, 484, false));
		}
		
		if (itemId == 423014) {
			_owner.stopAHRegeneration();
		}
		if (itemId == 423015) {
			_owner.stopSHRegeneration();
		}
		if (itemId == 20380) {
			_owner.stopHalloweenRegeneration();
		}
		if (itemId == 20077 || itemId == 20062 || itemId == 120077) {
			_owner.delInvis();
		}
		if (itemId == 20288 || itemId == 900111) {
			_owner.sendPackets(new S_Ability(1, false));
		}
		if (itemId == 20281) {
			_owner.sendPackets(new S_Ability(2, false));
		}
		if (itemId == 20036) {
			_owner.sendPackets(new S_Ability(3, false));
		}
		if (itemId == 20284) {
			_owner.sendPackets(new S_Ability(5, false));
		}
		if (itemId == 20207) {
			_owner.sendPackets(new S_SkillIconBlessOfEva(_owner.getId(), 0));
		}
		
		if (itemId >= 900263 && itemId <= 900265) {
			_owner.set_halpas_armor(false);
			_owner.set_halpas_armor_enchant(0);
			_owner.removeHalpasArmor();
			long endTime;
			if (armor.getHalpas_Time() != null) {
				endTime = Timestamp.valueOf(armor.getHalpas_Time().toString()).getTime();
			}else {
				endTime = 0;
			}
			long currentTime = System.currentTimeMillis();
			if (endTime - currentTime > 0) {
				L1SkillUse.off_icons(_owner, L1SkillId.DRAGON_ARMOR_BLESSING);
			} else {
				L1SkillUse.off_icons(_owner, L1SkillId.DRAGON_ARMOR_EQUIP);
			}
			
		}
		
		if (itemId == 22200 || itemId == 22201 || itemId == 22202 || itemId == 22203) {// 파푸리온 갑옷
			_owner.stopPapuBlessing();
		} else if (itemId >= 22208 && itemId <= 22211)
			_owner.stopValaBlessing();

		armor.stopEquipmentTimer(_owner);
		_armors.remove(armor);
	}

	public void set(L1ItemInstance equipment) {
		L1Item item = equipment.getItem();

		if (item.getType2() == 0) {
			return;
		}
		
		if (_owner.getResistance() == null)
			_owner.resetResistance();
		
		if (equipment.getPVPDmgReducIgnore() != 0)
			_owner.add_pvp_dmg_ignore(equipment.getPVPDmgReducIgnore());
		if (equipment.getPVPMDmgReducIgnore() != 0)
			_owner.add_pvp_mdmg_ignore(equipment.getPVPMDmgReducIgnore());
		if (equipment.getPVPMdmgReduction() != 0)
			_owner.add_pvp_mdmg(equipment.getPVPMdmgReduction());
		if (equipment.getImmuneIgnore() != 0)
			_owner.add_immune_ignore(equipment.getImmuneIgnore());

		if (equipment.getDmgModifier() != 0)
			_owner.addDmgup(equipment.getDmgModifier());
		if(equipment.getMagicDmgModifier() != 0)
			_owner.addMagicDmgup(equipment.getMagicDmgModifier());
		if (equipment.getHitModifier() != 0)
			_owner.addHitup(equipment.getHitModifier());
		if (equipment.getBowDmgModifier() != 0)
			_owner.addBowDmgup(equipment.getBowDmgModifier());
		if (equipment.getBowHitModifier() != 0)
			_owner.addBowHitup(equipment.getBowHitModifier());
		if (equipment.getPvPDamage() != 0)
			_owner.getResistance().addPVPweaponTotalDamage(equipment.getPvPDamage());
		if (equipment.getPvpReduction() != 0)
			_owner.getResistance().addcalcPcDefense(equipment.getPvpReduction());

		if (equipment.getStr() != 0)
			_owner.getAbility().addAddedStr(equipment.getStr());
		if (equipment.getCon() != 0)
			_owner.getAbility().addAddedCon(equipment.getCon());
		if (equipment.getDex() != 0)
			_owner.getAbility().addAddedDex(equipment.getDex());
		if (equipment.getInt() != 0)
			_owner.getAbility().addAddedInt(equipment.getInt());
		if (equipment.getWis() != 0)
			_owner.getAbility().addAddedWis(equipment.getWis());
		if (equipment.getCha() != 0)
			_owner.getAbility().addAddedCha(equipment.getCha());
		if (equipment.getItem().getAttackDelayRate() != 0)
			_owner.addAttackDelayRate(equipment.getItem().getAttackDelayRate());
		if (equipment.getItem().getMoveDelayRate() != 0)
			_owner.addMoveDelayRate(equipment.getItem().getMoveDelayRate());

		if (equipment.getaddHp() != 0) {
			_owner.addMaxHp(equipment.getaddHp());
			_owner.sendPackets(new S_HPUpdate(_owner.getCurrentHp(), _owner.getMaxHp()));
		}
		
		if (equipment.getHp() != 0) {
			_owner.addMaxHp(equipment.getHp());
			_owner.sendPackets(new S_HPUpdate(_owner.getCurrentHp(), _owner.getMaxHp()));
		}

		if (equipment.getMp() != 0) {
			_owner.addMaxMp(equipment.getMp());
			_owner.sendPackets(new S_MPUpdate(_owner.getCurrentMp(), _owner.getMaxMp()));
		}
		
		if (equipment.getHpPercent() != 0) {
			int hpperchent = (int) Math.round(_owner.getBaseMaxHp() * (equipment.getHpPercent() * 0.01));
			_owner.addMaxHp(hpperchent);
			_owner.sendPackets(new S_HPUpdate(_owner.getCurrentHp(), _owner.getMaxHp()));
		}

		if (equipment.getMpPercent() != 0) {
			int mpperchent = (int) Math.round(_owner.getBaseMaxMp() * (equipment.getMpPercent() * 0.01));
			_owner.addMaxMp(mpperchent);
			_owner.sendPackets(new S_MPUpdate(_owner.getCurrentMp(), _owner.getMaxMp()));
		}

		if (equipment.getHpr() != 0)
			_owner.addHpr(equipment.getHpr());
		if (equipment.getMpr() != 0)
			_owner.addMpr(equipment.getMpr());

		if (equipment.getExpByItem() != 0) {
			_owner.add_item_exp_bonus(equipment.getExpByItem());
			SC_EXP_BOOSTING_INFO_NOTI.send(_owner);
		}

		if (equipment.getMr() != 0) {
			_owner.getResistance().addMr(equipment.getMr());
		}

		if (equipment.getSp() != 0) {
			_owner.getAbility().addSp(equipment.getSp());
		}

		if (equipment.getMagicHitRate() != 0)
			_owner.addBaseMagicHitUp(equipment.getMagicHitRate());
		
		if (equipment.getTitanPercent() != 0)
			_owner.add락구간상승(equipment.getTitanPercent());
		
		if (equipment.getFoeDmg() != 0)
			_owner.addFouDmg(equipment.getFoeDmg());

		if (equipment.getDamageReduction() != 0)
			_owner.addDamageReductionByArmor(equipment.getDamageReduction());
		
		if (equipment.getWeightReduction() != 0)
			_owner.addWeightReduction(equipment.getWeightReduction());

		if (equipment.getTechniqueTolerance() != 0) {
			_owner.addSpecialResistance(eKind.ABILITY, equipment.getTechniqueTolerance());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getSpiritTolerance() != 0) {
			_owner.addSpecialResistance(eKind.SPIRIT, equipment.getSpiritTolerance());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getDragonLangTolerance() != 0) {
			_owner.addSpecialResistance(eKind.DRAGON_SPELL, equipment.getDragonLangTolerance());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getFearTolerance() != 0) {
			_owner.addSpecialResistance(eKind.FEAR, equipment.getFearTolerance());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getAllTolerance() != 0) {
			_owner.addSpecialResistance(eKind.ALL, equipment.getAllTolerance());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}

		if (equipment.getTechniqueHit() != 0) {
			_owner.addSpecialPierce(eKind.ABILITY, equipment.getTechniqueHit());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getSpiritHit() != 0) {
			_owner.addSpecialPierce(eKind.SPIRIT, equipment.getSpiritHit());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getDragonLangHit() != 0) {
			_owner.addSpecialPierce(eKind.DRAGON_SPELL, equipment.getDragonLangHit());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getFearHit() != 0) {
			_owner.addSpecialPierce(eKind.FEAR, equipment.getFearHit());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getAllHit() != 0) {
			_owner.addSpecialPierce(eKind.ALL, equipment.getAllHit());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}

		if (equipment.getReductionCancel() != 0)
			_owner.addReducCancel(equipment.getReductionCancel());

		if (equipment.getDG() != 0)
			_owner.addDg(equipment.getDG());
		
		if (equipment.getTotalER() != 0)
			_owner.addEffectedER(equipment.getTotalER());
		
		if (equipment.getDefenseFire() != 0) {
			_owner.getResistance().addFire(equipment.getDefenseFire());
			_owner.sendPackets(new S_OwnCharAttrDef(_owner));
		}
		if (equipment.getDefenseWater() != 0) {
			_owner.getResistance().addWater(equipment.getDefenseWater());
			_owner.sendPackets(new S_OwnCharAttrDef(_owner));
		}
		if (equipment.getDefenseWind() != 0) {
			_owner.getResistance().addWind(equipment.getDefenseWind());
			_owner.sendPackets(new S_OwnCharAttrDef(_owner));
		}
		if (equipment.getDefenseEarth() != 0) {
			_owner.getResistance().addEarth(equipment.getDefenseEarth());
			_owner.sendPackets(new S_OwnCharAttrDef(_owner));
		}
		if (equipment.getDefenseAll() != 0) {
			_owner.getResistance().addAllNaturalResistance(equipment.getDefenseAll());
			_owner.sendPackets(new S_OwnCharAttrDef(_owner));
		}

		if (equipment.getMagicDodge() != 0)
			_owner.addMagicDodgeProbability(equipment.getMagicDodge());

		if (equipment.getAinEfficiency() != 0)
			_owner.addEinhasadBlessper(equipment.getAinEfficiency());
		
		if (equipment.getShortCriticalValue() != 0)
			_owner.add_melee_critical_rate(equipment.getShortCriticalValue());
		
		if (equipment.getLongCriticalValue() != 0)
			_owner.add_missile_critical_rate(equipment.getLongCriticalValue());
		
		if (equipment.getMagicCriticalValue() != 0)
			_owner.add_magic_critical_rate(equipment.getMagicCriticalValue());

		if (equipment.isHasteItem()) {
			_owner.addHasteItemEquipped(1);
			_owner.removeHasteSkillEffect();
			if (_owner.getMoveSpeed() != 1) {
				_owner.setMoveSpeed(1);
				_owner.sendPackets(new S_SkillHaste(_owner.getId(), 1, -1));
				_owner.broadcastPacket(new S_SkillHaste(_owner.getId(), 1, 0));
			}
		}
		if (equipment.getItemId() == 900010) {
			_owner.addThreeItemEquipped(1);
			_owner.removeThreeSkillEffect();
			_owner.sendPackets(new S_Liquor(_owner.getId(), 8));
    		_owner.broadcastPacket(new S_Liquor(_owner.getId(), 8));
    		_owner.setPearl(1);
    		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
    		noti.set_noti_type(eNotiType.NEW);
			noti.set_spell_id(L1SkillId.STATUS_DRAGON_PEARL);
			noti.set_duration(-1);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(5393);
			noti.set_off_icon_id(5393);
			noti.set_icon_priority(10);
			noti.set_tooltip_str_id(1065);
			noti.set_new_str_id(1065);
			noti.set_end_str_id(0);
			noti.set_is_good(true);
			_owner.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
			
			_owner.setSkillEffect(STATUS_BRAVE, -1);
			int objId = _owner.getId();
			_owner.sendPackets(new S_SkillBrave(objId, 1, -1));
			_owner.broadcastPacket(new S_SkillBrave(objId, 1, 0));
			_owner.setBraveSpeed(1);
			
			_owner.setFourgear(true);
			_owner.sendPackets(SC_FOURTH_GEAR_NOTI.Fourth_Gear(_owner));
			_owner.broadcastPacket(SC_FOURTH_GEAR_NOTI.Fourth_Gear(_owner));
		}
		
		if (equipment.getCCIncrease() != 0) {
			_owner.add_CC_Increase(equipment.getCCIncrease());
		}
		if (equipment.getAbnormalStatusPvpDamageReduction()!=0) {
			_owner.addAbnormalStatusPvPReduction(equipment.getCCIncrease());
		}

		_owner.getEquipSlot().setMagicHelm(equipment);
		
		if (item.getType2() == 1) {
			setWeapon(equipment);
		} else if (item.getType2() == 2) {
			setArmor(equipment);
			if (item.getType() == 2) {
				_owner.setAmory(equipment);
			}
		}
	}
	//TODO 스킬에 필요한 장비를 해제 했을때(프로토 변경시 밑에보고 따라하기)중요성높아짐
	public void remove(L1ItemInstance equipment) {
		L1Item item = equipment.getItem();
		
		/*if (item.getType2() == 2 && item.getType() == 7 || item.getType2() == 1 && item.getType() != 2) {
			if (_owner.hasSkillEffect(L1SkillId.BLOW_ATTACK)) {
				_owner.removeSkillEffect(L1SkillId.BLOW_ATTACK);
			}
		} else {
			equipment.off_skill_effect_icons();
		}*/

		// 해제하려는 아이템이 무기일경우 블로우어택 삭제
		/*if (equipment.getItem().getType2() == 1) {
			if (_owner.hasSkillEffect(L1SkillId.BLOW_ATTACK)) {
				_owner.removeSkillEffect(L1SkillId.BLOW_ATTACK);
				_owner.sendPackets(new S_ServerMessage(5267));
			}
		}*/ /*else {
			equipment.off_skill_effect_icons();
		}*/

		equipment.off_skill_effect_icons();
		

		if (item.getType2() == 0) {
			return;
		}
		
		if (equipment.getPVPDmgReducIgnore() != 0)
			_owner.add_pvp_dmg_ignore(-equipment.getPVPDmgReducIgnore());
		if (equipment.getPVPMDmgReducIgnore() != 0)
			_owner.add_pvp_mdmg_ignore(-equipment.getPVPMDmgReducIgnore());
		if (equipment.getPVPMdmgReduction() != 0)
			_owner.add_pvp_mdmg(-equipment.getPVPMdmgReduction());
		if (equipment.getImmuneIgnore() != 0)
			_owner.add_immune_ignore(-equipment.getImmuneIgnore());

		if (equipment.getDmgModifier() != 0)
			_owner.addDmgup(-equipment.getDmgModifier());
		if(equipment.getMagicDmgModifier() != 0)
			_owner.addMagicDmgup(-equipment.getMagicDmgModifier());
		if (equipment.getHitModifier() != 0)
			_owner.addHitup(-equipment.getHitModifier());
		if (equipment.getBowDmgModifier() != 0)
			_owner.addBowDmgup(-equipment.getBowDmgModifier());
		if (equipment.getBowHitModifier() != 0)
			_owner.addBowHitup(-equipment.getBowHitModifier());
		if (equipment.getPvPDamage() != 0)
			_owner.getResistance().addPVPweaponTotalDamage(-equipment.getPvPDamage());
		if (equipment.getPvpReduction() != 0)
			_owner.getResistance().addcalcPcDefense(-equipment.getPvpReduction());

		if (equipment.getStr() != 0)
			_owner.getAbility().addAddedStr(-equipment.getStr());
		if (equipment.getCon() != 0)
			_owner.getAbility().addAddedCon(-equipment.getCon());
		if (equipment.getDex() != 0)
			_owner.getAbility().addAddedDex(-equipment.getDex());
		if (equipment.getInt() != 0)
			_owner.getAbility().addAddedInt(-equipment.getInt());
		if (equipment.getWis() != 0)
			_owner.getAbility().addAddedWis(-equipment.getWis());
		if (equipment.getCha() != 0)
			_owner.getAbility().addAddedCha(-equipment.getCha());
		if (equipment.getItem().getAttackDelayRate() != 0)
			_owner.addAttackDelayRate(-equipment.getItem().getAttackDelayRate());
		if (equipment.getItem().getMoveDelayRate() != 0)
			_owner.addMoveDelayRate(-equipment.getItem().getMoveDelayRate());

		if (equipment.getaddHp() != 0) {
			_owner.addMaxHp(-equipment.getaddHp());
			_owner.sendPackets(new S_HPUpdate(_owner.getCurrentHp(), _owner.getMaxHp()));
		}
		
		if (equipment.getHp() != 0) {
			_owner.addMaxHp(-equipment.getHp());
			_owner.sendPackets(new S_HPUpdate(_owner.getCurrentHp(), _owner.getMaxHp()));
		}

		if (equipment.getMp() != 0) {
			_owner.addMaxMp(-equipment.getMp());
			_owner.sendPackets(new S_MPUpdate(_owner.getCurrentMp(), _owner.getMaxMp()));
		}
		
		if (equipment.getHpPercent() != 0) {
			int hpperchent = (int) Math.round(_owner.getBaseMaxHp() * (equipment.getHpPercent() * 0.01));
			_owner.addMaxHp(-hpperchent);
			_owner.sendPackets(new S_HPUpdate(_owner.getCurrentHp(), _owner.getMaxHp()));
		}

		if (equipment.getMpPercent() != 0) {
			int mpperchent = (int) Math.round(_owner.getBaseMaxMp() * (equipment.getMpPercent() * 0.01));
			_owner.addMaxMp(-mpperchent);
			_owner.sendPackets(new S_MPUpdate(_owner.getCurrentMp(), _owner.getMaxMp()));
		}
		
		if (equipment.getHpr() != 0)
			_owner.addHpr(-equipment.getHpr());
		if (equipment.getMpr() != 0)
			_owner.addMpr(-equipment.getMpr());

		if (equipment.getExpByItem() != 0) {
			_owner.add_item_exp_bonus(-equipment.getExpByItem());
			SC_EXP_BOOSTING_INFO_NOTI.send(_owner);
		}

		if (equipment.getMr() != 0) {
			_owner.getResistance().addMr(-equipment.getMr());
		}

		if (equipment.getSp() != 0) {
			_owner.getAbility().addSp(-equipment.getSp());
		}

		if (equipment.getMagicHitRate() != 0)
			_owner.addBaseMagicHitUp(-equipment.getMagicHitRate());

		if (equipment.getTitanPercent() != 0)
			_owner.add락구간상승(-equipment.getTitanPercent());

		if (equipment.getFoeDmg() != 0)
			_owner.addFouDmg(-equipment.getFoeDmg());

		if (equipment.getDamageReduction() != 0)
			_owner.addDamageReductionByArmor(-equipment.getDamageReduction());

		if (equipment.getWeightReduction() != 0)
			_owner.addWeightReduction(-equipment.getWeightReduction());

		if (equipment.getTechniqueTolerance() != 0) {
			_owner.addSpecialResistance(eKind.ABILITY, -equipment.getTechniqueTolerance());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getSpiritTolerance() != 0) {
			_owner.addSpecialResistance(eKind.SPIRIT, -equipment.getSpiritTolerance());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getDragonLangTolerance() != 0) {
			_owner.addSpecialResistance(eKind.DRAGON_SPELL, -equipment.getDragonLangTolerance());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getFearTolerance() != 0) {
			_owner.addSpecialResistance(eKind.FEAR, -equipment.getFearTolerance());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getAllTolerance() != 0) {
			_owner.addSpecialResistance(eKind.ALL, -equipment.getAllTolerance());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}

		if (equipment.getTechniqueHit() != 0) {
			_owner.addSpecialPierce(eKind.ABILITY, -equipment.getTechniqueHit());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getSpiritHit() != 0) {
			_owner.addSpecialPierce(eKind.SPIRIT, -equipment.getSpiritHit());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getDragonLangHit() != 0) {
			_owner.addSpecialPierce(eKind.DRAGON_SPELL, -equipment.getDragonLangHit());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getFearHit() != 0) {
			_owner.addSpecialPierce(eKind.FEAR, -equipment.getFearHit());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}
		if (equipment.getAllHit() != 0) {
			_owner.addSpecialPierce(eKind.ALL, -equipment.getAllHit());
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(_owner);
		}

		if (equipment.getReductionCancel() != 0)
			_owner.addReducCancel(-equipment.getReductionCancel());

		if (equipment.getDG() != 0)
			_owner.addDg(-equipment.getDG());

		if (equipment.getTotalER() != 0)
			_owner.addEffectedER(-equipment.getTotalER());

		if (equipment.getDefenseFire() != 0) {
			_owner.getResistance().addFire(-equipment.getDefenseFire());
			_owner.sendPackets(new S_OwnCharAttrDef(_owner));
		}
		if (equipment.getDefenseWater() != 0) {
			_owner.getResistance().addWater(-equipment.getDefenseWater());
			_owner.sendPackets(new S_OwnCharAttrDef(_owner));
		}
		if (equipment.getDefenseWind() != 0) {
			_owner.getResistance().addWind(-equipment.getDefenseWind());
			_owner.sendPackets(new S_OwnCharAttrDef(_owner));
		}
		if (equipment.getDefenseEarth() != 0) {
			_owner.getResistance().addEarth(-equipment.getDefenseEarth());
			_owner.sendPackets(new S_OwnCharAttrDef(_owner));
		}
		if (equipment.getDefenseAll() != 0) {
			_owner.getResistance().addAllNaturalResistance(-equipment.getDefenseAll());
			_owner.sendPackets(new S_OwnCharAttrDef(_owner));
		}

		if (equipment.getMagicDodge() != 0)
			_owner.addMagicDodgeProbability(-equipment.getMagicDodge());

		if (equipment.getAinEfficiency() != 0)
			_owner.addEinhasadBlessper(-equipment.getAinEfficiency());
		
		if (equipment.getShortCriticalValue() != 0)
			_owner.add_melee_critical_rate(-equipment.getShortCriticalValue());
		
		if (equipment.getLongCriticalValue() != 0)
			_owner.add_missile_critical_rate(-equipment.getLongCriticalValue());
		
		if (equipment.getMagicCriticalValue() != 0)
			_owner.add_magic_critical_rate(-equipment.getMagicCriticalValue());

		if (equipment.isHasteItem()) {
			_owner.addHasteItemEquipped(-1);
			_owner.removeHasteSkillEffect();
			if (_owner.getMoveSpeed() != 0) {
				_owner.setMoveSpeed(0);
				_owner.sendPackets(new S_SkillHaste(_owner.getId(), 0, 0));
				_owner.broadcastPacket(new S_SkillHaste(_owner.getId(), 0, 0));
			}
		}
		
		if (equipment.getItemId() == 900010) {
			_owner.addThreeItemEquipped(-1);
			_owner.removeThreeSkillEffect();
			_owner.sendPackets(new S_Liquor(_owner.getId(), 0));
			_owner.broadcastPacket(new S_Liquor(_owner.getId(), 0));
			_owner.setPearl(0);
			
			_owner.removeSkillEffect(STATUS_BRAVE);
			int objId = _owner.getId();
			_owner.sendPackets(new S_SkillBrave(objId, 0, 0));
			_owner.broadcastPacket(new S_SkillBrave(objId, 0, 0));
			_owner.setBraveSpeed(0);
			
			_owner.setFourgear(false);
			_owner.sendPackets(SC_FOURTH_GEAR_NOTI.Fourth_Gear(_owner));
			_owner.broadcastPacket(SC_FOURTH_GEAR_NOTI.Fourth_Gear(_owner));
		}
		
		if (equipment.getCCIncrease() != 0) {
			_owner.add_CC_Increase(-equipment.getCCIncrease());
		}
		if (equipment.getAbnormalStatusPvpDamageReduction()!=0) {
			_owner.addAbnormalStatusPvPReduction(-equipment.getAbnormalStatusPvpDamageReduction());
		}

		_owner.getEquipSlot().removeMagicHelm(_owner.getId(), equipment);

		if (item.getType2() == 1) {
			removeWeapon(equipment);
			if (_owner.hasSkillEffect(L1SkillId.DANCING_BLADES)) {
				_owner.sendPackets(new S_SkillIconAura(154, 0));
				_owner.removeSkillEffect(L1SkillId.DANCING_BLADES);

			}
		} else if (item.getType2() == 2) {
			removeArmor(equipment);
			if (item.getType() == 2) {
				_owner.setAmory(null);
			}
		}

		//_owner.sendPackets(String.format("%s을(를) 해제 하였습니다.", equipment.getLogName()));
	}

	private static final HashMap<Integer, List<Integer>> mItemSkillMapped;
	static {
		//TODO 마법의 투구 추가하면 C_UseSkill.java에서도 추가해줘야 한다(버그방지)
		mItemSkillMapped = new HashMap<>();
		mItemSkillMapped.put(20013, Arrays.asList(L1SkillId.PHYSICAL_ENCHANT_DEX, L1SkillId.HASTE));
		mItemSkillMapped.put(20014, Arrays.asList(L1SkillId.HEAL, L1SkillId.EXTRA_HEAL));
		mItemSkillMapped.put(20015, Arrays.asList(L1SkillId.DETECTION, L1SkillId.PHYSICAL_ENCHANT_STR, L1SkillId.ENCHANT_WEAPON));
		mItemSkillMapped.put(20008, Arrays.asList(L1SkillId.HASTE));
		mItemSkillMapped.put(20023, Arrays.asList(L1SkillId.GREATER_HASTE));
	}

	public void setMagicHelm(L1ItemInstance item) {
		List<Integer> skills = mItemSkillMapped.get(item.getItemId());
		if (skills == null || skills.size() <= 0) {
			return;
		}
		SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
		for (Integer skillId : skills) {
			noti.appendNewSpell(skillId, true);
		}
		_owner.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
	}

	public void removeMagicHelm(int objectId, L1ItemInstance item) {
		List<Integer> skills = mItemSkillMapped.get(item.getItemId());
		if (skills == null || skills.size() <= 0) {
			return;
		}
		SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
		for (Integer skillId : skills) {
			if (!SkillsTable.getInstance().spellCheck(objectId, skillId)) {
				noti.appendNewSpell(skillId, false);
			}
		}
		if (noti.get_spell_info() != null && noti.get_spell_info().size() > 0) {
			_owner.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
		}
	}

	/**
	 * 셋트 아이템 해제
	 * 
	 * @param itemId
	 */
	public void removeSetItems(int itemId) {
		for (L1ArmorSet armorSet : L1ArmorSet.getAllSet()) {
			if (armorSet.isPartOfSet(itemId) && _currentArmorSet.contains(armorSet) && !armorSet.isValid(_owner)) {
				armorSet.cancelEffect(_owner);
				_currentArmorSet.remove(armorSet);
			}
		}
		
		/*for (L1ArmorSet armorSet : ArmorSetTable.getInstance().values()) {
			if (armorSet.isPartOfSet(itemId) && _currentArmorSet.contains(armorSet) && !armorSet.isValid(_owner)) {
				armorSet.cancelEffect(_owner);
				_currentArmorSet.remove(armorSet);
			}
		}*/
	}
	
	public void removeSetItemsbless(int itemId) {
		for (L1ArmorSet armorSet : L1ArmorSet.getAllSet()) {
			if (armorSet.isPartOfSet(itemId) && _currentArmorSet.contains(armorSet) && armorSet.isValid(_owner)) {
				armorSet.cancelEffect(_owner);
				_currentArmorSet.remove(armorSet);
			}
		}
		
		/*for (L1ArmorSet armorSet : ArmorSetTable.getInstance().values()) {
			if (armorSet.isPartOfSet(itemId) && _currentArmorSet.contains(armorSet) && !armorSet.isValid(_owner)) {
				armorSet.cancelEffect(_owner);
				_currentArmorSet.remove(armorSet);
			}
		}*/
	}
}