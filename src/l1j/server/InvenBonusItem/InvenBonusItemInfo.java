package l1j.server.InvenBonusItem;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.collection.favor.bean.L1FavorBookUserObject;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_Liquor;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.utils.BinaryOutputStream;

public class InvenBonusItemInfo {
	static InvenBonusItemInfo newInstance(ResultSet rs) throws SQLException {
		InvenBonusItemInfo pInfo = newInstance();
		pInfo._item_id = rs.getInt("item_id");
		pInfo._item_name = rs.getString("item_name");
		pInfo._str = rs.getInt("str");
		pInfo._dex = rs.getInt("dex");
		pInfo._con = rs.getInt("con");
		pInfo._wis = rs.getInt("wis");
		pInfo._int = rs.getInt("int");
		pInfo._cha = rs.getInt("cha");
		pInfo._all_Stat = rs.getInt("all_stat");
		pInfo._short_dmg = rs.getInt("short_dmg");
		pInfo._long_dmg = rs.getInt("long_dmg");
		pInfo._short_hit = rs.getInt("short_hit");
		pInfo._long_hit = rs.getInt("long_hit");
		pInfo._short_critical = rs.getInt("short_critical");
		pInfo._long_critical = rs.getInt("long_critical");
		pInfo._reduction = rs.getInt("reduction");
		pInfo._pvp_dmg = rs.getInt("pvp_dmg");
		pInfo._pvp_reduction = rs.getInt("pvp_reduction");
		pInfo._dg = rs.getInt("dg");
		pInfo._er = rs.getInt("er");
		pInfo._ac = rs.getInt("ac");
		pInfo._mr = rs.getInt("mr");
		pInfo._sp = rs.getInt("sp");
		pInfo._weight = rs.getInt("weight");
		pInfo._max_hp = rs.getInt("max_hp");
		pInfo._max_mp = rs.getInt("max_mp");
		pInfo._resis_fire = rs.getInt("resis_fire");
		pInfo._resis_earth = rs.getInt("resis_earth");
		pInfo._resis_water = rs.getInt("resis_water");
		pInfo._resis_wind = rs.getInt("resis_wind");
		pInfo._resis_all = rs.getInt("resis_all");
		pInfo._m_resis_ability = rs.getInt("m_resis_ability");
		pInfo._m_resis_spirit = rs.getInt("m_resis_spirit");
		pInfo._m_resis_dragon = rs.getInt("m_resis_dragon");
		pInfo._m_resis_fear = rs.getInt("m_resis_fear");
		pInfo._m_resis_all = rs.getInt("m_resis_all");
		pInfo._m_hit_ability = rs.getInt("m_hit_ability");
		pInfo._m_hit_spirit = rs.getInt("m_hit_spirit");
		pInfo._m_hit_dragon = rs.getInt("m_hit_dragon");
		pInfo._m_hit_fear = rs.getInt("m_hit_fear");
		pInfo._m_hit_all = rs.getInt("m_hit_all");
		pInfo._magichit = rs.getInt("magichit");
		pInfo._attack_speed = rs.getInt("attack_speed");
		pInfo._exp = rs.getInt("exp");
		pInfo._dragon_pear = rs.getBoolean("dragon_pear");
		pInfo._me = rs.getInt("me");
		pInfo._magic_critical = rs.getInt("magic_critical");
		pInfo._reduction_per = rs.getInt("reduction_per");
		pInfo._dominion_tel = rs.getInt("dominion_tel");
		pInfo._portion_ration = rs.getInt("portion_ration");
		pInfo._hpRegen = rs.getInt("hp_regen");
		pInfo._mpRegen = rs.getInt("mp_regen");
		pInfo._pvp_magic_reduc = rs.getInt("pvp_magic_reduction_per");
		pInfo._pvp_reduction_per = rs.getInt("pvp_reduction_per");
		return pInfo;
	}

	private static InvenBonusItemInfo newInstance() {
		return new InvenBonusItemInfo();
	}

	private int _hpRegen;

	public int get_hpRegen() {
		return _hpRegen;
	}

	private int _mpRegen;

	public int get_mpRegen() {
		return _mpRegen;
	}

	private int _pvp_magic_reduc;

	public int get_pvp_magic_reduc() {
		return _pvp_magic_reduc;
	}

	private int _pvp_reduction_per;

	public int get_pvp_reduction_per() {
		return _pvp_reduction_per;
	}

	private int _status_time_reduce;

	public int get_status_time_reduce() {
		return _status_time_reduce;
	}

	private int _portion_ration;

	public int get_portion_ration() {
		return _portion_ration;
	}

	private Timestamp _deltime;

	public Timestamp get_deltime() {
		return _deltime;
	}

	public void set_deltime(Timestamp time) {
		_deltime = time;
	}

	private int _dominion_tel;

	public int get_dominion_tel() {
		return _dominion_tel;
	}

	private int _item_id;

	public int get_item_id() {
		return _item_id;
	}

	private String _item_name;

	public String get_item_name() {
		return _item_name;
	}

	private int _str;

	public int get_str() {
		return _str;
	}

	private int _dex;

	public int get_dex() {
		return _dex;
	}

	private int _con;

	public int get_con() {
		return _con;
	}

	private int _wis;

	public int get_wis() {
		return _wis;
	}

	private int _int;

	public int get_int() {
		return _int;
	}

	private int _cha;

	public int get_cha() {
		return _cha;
	}

	private int _all_Stat;

	public int get_all_Stat() {
		return _all_Stat;
	}

	private int _short_dmg;

	public int get_short_dmg() {
		return _short_dmg;
	}

	private int _long_dmg;

	public int get_long_dmg() {
		return _long_dmg;
	}

	private int _short_hit;

	public int get_short_hit() {
		return _short_hit;
	}

	private int _long_hit;

	public int get_long_hit() {
		return _long_hit;
	}

	private int _short_critical;

	public int get_short_critical() {
		return _short_critical;
	}

	private int _long_critical;

	public int get_long_critical() {
		return _long_critical;
	}

	private int _reduction;

	public int get_reduction() {
		return _reduction;
	}

	private int _pvp_dmg;

	public int get_pvp_dmg() {
		return _pvp_dmg;
	}

	private int _pvp_reduction;

	public int get_pvp_reduction() {
		return _pvp_reduction;
	}

	private int _dg;

	public int get_dg() {
		return _dg;
	}

	private int _er;

	public int get_er() {
		return _er;
	}

	private int _ac;

	public int get_ac() {
		return _ac;
	}

	private int _mr;

	public int get_mr() {
		return _mr;
	}

	private int _sp;

	public int get_sp() {
		return _sp;
	}

	private int _weight;

	public int get_weight() {
		return _weight;
	}

	private int _max_hp;

	public int get_max_hp() {
		return _max_hp;
	}

	private int _max_mp;

	public int get_max_mp() {
		return _max_mp;
	}

	private int _resis_fire;

	public int get_resis_fire() {
		return _resis_fire;
	}

	private int _resis_earth;

	public int get_resis_earth() {
		return _resis_earth;
	}

	private int _resis_water;

	public int get_resis_water() {
		return _resis_water;
	}

	private int _resis_wind;

	public int get_resis_wind() {
		return _resis_wind;
	}

	private int _resis_all;

	public int get_resis_all() {
		return _resis_all;
	}

	private int _m_resis_ability;

	public int get_m_resis_ability() {
		return _m_resis_ability;
	}

	private int _m_resis_spirit;

	public int get_m_resis_spirit() {
		return _m_resis_spirit;
	}

	private int _m_resis_dragon;

	public int get_m_resis_dragon() {
		return _m_resis_dragon;
	}

	private int _m_resis_fear;

	public int get_m_resis_fear() {
		return _m_resis_fear;
	}

	private int _m_resis_all;

	public int get_m_resis_all() {
		return _m_resis_all;
	}

	private int _m_hit_ability;

	public int get_m_hit_ability() {
		return _m_hit_ability;
	}

	private int _m_hit_spirit;

	public int get_m_hit_spirit() {
		return _m_hit_spirit;
	}

	private int _m_hit_dragon;

	public int get_m_hit_dragon() {
		return _m_hit_dragon;
	}

	private int _m_hit_fear;

	public int get_m_hit_fear() {
		return _m_hit_fear;
	}

	private int _m_hit_all;

	public int get_m_hit_all() {
		return _m_hit_all;
	}

	private int _exp;

	public int get_exp() {
		return _exp;
	}

	private int _magichit;

	public int get_magichit() {
		return _magichit;
	}

	private int _magic_critical;

	public int get_magic_critical() {
		return _magic_critical;
	}

	private int _me;

	public int get_me() {
		return _me;
	}

	private int _attack_speed;

	public int get_attackspeed() {
		return _attack_speed;
	}

	private boolean _dragon_pear;

	public boolean get_dragon_pear() {
		return _dragon_pear;
	}

	private int _reduction_per;

	public int get_reduction_per() {
		return _reduction_per;
	}

	public static void inven_option(L1PcInstance pc, int itemid, boolean onOff) {
		InvenBonusItemInfo info = InvenBonusItemLoader.getInstance().getInvenBonusItemInfo(itemid);

		if (!onOff && pc.getInventory().countItems(itemid) <= 1) {
			pc.delInvenBonusItems(itemid);
		}
		if (pc.isInvenBonusItems(itemid)) {
			return;
		}

		int type = onOff ? 1 : -1;
		if (info != null) {
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
				// addMr(type * info.get_mr());
				// pc.sendPackets(new S_SPMR(pc));

			}
			if (info.get_sp() != 0) {
				pc.getAbility().addSp(type * info.get_sp());
				// pc.sendPackets(new S_SPMR(pc));
			}
			if (info.get_weight() != 0) {
				pc.addWeightReduction(type * info.get_weight());
			}
			if (info.get_max_hp() != 0) {
				pc.addMaxHp(type * info.get_max_hp());
				// pc.sendPackets(new S_HPUpdate(pc));
			}
			if (info.get_max_mp() != 0) {
				pc.addMaxMp(type * info.get_max_mp());
				// pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			}
			if (info.get_resis_fire() != 0) {
				pc.getResistance().addFire(type * info.get_resis_fire());
				// pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			if (info.get_resis_earth() != 0) {
				pc.getResistance().addEarth(type * info.get_resis_earth());
				// pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			if (info.get_resis_water() != 0) {
				pc.getResistance().addWater(type * info.get_resis_water());
				// pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			if (info.get_resis_wind() != 0) {
				pc.getResistance().addWind(type * info.get_resis_wind());
				// pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			if (info.get_resis_all() != 0) {
				pc.getResistance().addAllNaturalResistance(type * info.get_resis_all());
				// pc.sendPackets(new S_OwnCharAttrDef(pc));
			}
			if (info.get_m_resis_ability() != 0) {
				pc.addSpecialResistance(eKind.ABILITY, (type * info.get_m_resis_ability()));
				// SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (info.get_m_resis_spirit() != 0) {
				pc.addSpecialResistance(eKind.SPIRIT, (type * info.get_m_resis_spirit()));
				// SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (info.get_m_resis_dragon() != 0) {
				pc.addSpecialResistance(eKind.DRAGON_SPELL, (type * info.get_m_resis_dragon()));
				// SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (info.get_m_resis_fear() != 0) {
				pc.addSpecialResistance(eKind.FEAR, (type * info.get_m_resis_fear()));
				// SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (info.get_m_resis_all() != 0) {
				pc.addSpecialResistance(eKind.ALL, (type * info.get_m_resis_all()));
				// SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (info.get_m_hit_ability() < 0) {
				pc.addSpecialPierce(eKind.ABILITY, (type * info.get_m_hit_ability()));
				// SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (info.get_m_hit_spirit() != 0) {
				pc.addSpecialPierce(eKind.SPIRIT, (type * info.get_m_hit_spirit()));
				// SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (info.get_m_hit_dragon() != 0) {
				pc.addSpecialPierce(eKind.DRAGON_SPELL, (type * info.get_m_hit_dragon()));
				// SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (info.get_m_hit_fear() != 0) {
				pc.addSpecialPierce(eKind.FEAR, (type * info.get_m_hit_fear()));
				// SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			}
			if (info.get_m_hit_all() != 0) {
				pc.addSpecialPierce(eKind.ALL, (type * info.get_m_hit_all()));

			}

			if (info.get_exp() != 0) {
				pc.add_item_exp_bonus(type * info.get_exp());
			}

			if (info.get_magichit() != 0) {
				pc.addBaseMagicHitUp(type * info.get_magichit());
			}
			if (info.get_attackspeed() != 0) {
				pc.addAttackDelayRate(type * info.get_attackspeed());
			}
			if (info.get_me() != 0) {
				pc.addMagicDodgeProbability(type * info.get_me());
			}
			if (info.get_magic_critical() != 0) {
				pc.add_magic_critical_rate(type * info.get_magic_critical());
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
			/*
			 * if (info.get_armor_magic_pro() != 0){
			 * pc.add_armor_magic_pro(type * info.get_armor_magic_pro());
			 * }
			 */

			if (info.get_status_time_reduce() != 0) {
				pc.add_status_time_reduce(type * info.get_status_time_reduce());
			}
			if (info.get_portion_ration() != 0) {
				pc.addPotionRecoveryRatePct(type * info.get_portion_ration());
			}

			if (info.get_dragon_pear()) {
				if (onOff) {
					pc.addThreeItemEquipped(1);
					pc.removeThreeSkillEffect();
					pc.sendPackets(new S_Liquor(pc.getId(), 8));
					pc.broadcastPacket(new S_Liquor(pc.getId(), 8));
					pc.setPearl(1);
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
					pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI, true);
				} else {
					pc.addThreeItemEquipped(-1);
					pc.removeThreeSkillEffect();
					pc.sendPackets(new S_Liquor(pc.getId(), 0));
					pc.broadcastPacket(new S_Liquor(pc.getId(), 0));
					pc.setPearl(0);
				}
			}

			if (info.get_dominion_tel() != 0) {
				pc.set_dominion_tel(type);
				// System.out.println(pc.is_dominion_tel());
			}

			if (onOff) {
				pc.addInvenBonusItems(itemid);
			}
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			pc.sendPackets(new S_HPUpdate(pc));
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			pc.sendPackets(new S_SPMR(pc));

		}
	}

	public static byte[] getItemView(InvenBonusItemInfo info) {
		BinaryOutputStream os = new BinaryOutputStream();
		try {
			if (info != null) {
				if (info.get_str() != 0) {
					os.writeC(8);
					os.writeC(info.get_str());
				}
				if (info.get_dex() != 0) {
					os.writeC(9);
					os.writeC(info.get_dex());
				}
				if (info.get_con() != 0) {
					os.writeC(10);
					os.writeC(info.get_con());
				}
				if (info.get_wis() != 0) {
					os.writeC(11);
					os.writeC(info.get_wis());
				}
				if (info.get_int() != 0) {
					os.writeC(12);
					os.writeC(info.get_int());
				}
				if (info.get_cha() != 0) {
					os.writeC(13);
					os.writeC(info.get_cha());
				}
				if (info.get_all_Stat() != 0) {
					os.writeC(8);
					os.writeC(info.get_all_Stat());
					os.writeC(9);
					os.writeC(info.get_all_Stat());
					os.writeC(10);
					os.writeC(info.get_all_Stat());
					os.writeC(11);
					os.writeC(info.get_all_Stat());
					os.writeC(12);
					os.writeC(info.get_all_Stat());
					os.writeC(13);
					os.writeC(info.get_all_Stat());
				}
				if (info.get_short_dmg() != 0) {
					os.writeC(47);
					os.writeC(info.get_short_dmg());
				}
				if (info.get_long_dmg() != 0) {
					os.writeC(35);
					os.writeC(info.get_long_dmg());
				}
				if (info.get_short_hit() != 0) {
					os.writeC(48);
					os.writeC(info.get_short_hit());
				}
				if (info.get_long_hit() != 0) {
					os.writeC(24);
					os.writeC(info.get_long_hit());
				}
				if (info.get_short_critical() != 0) {
					os.writeC(100);
					os.writeC(info.get_short_critical());
				}
				if (info.get_long_critical() != 0) {
					os.writeC(99);
					os.writeC(info.get_long_critical());
				}
				if (info.get_reduction() != 0) {
					os.writeC(63);
					os.writeC(info.get_reduction());
				}
				if (info.get_pvp_dmg() != 0) {
					os.writeC(59);
					os.writeC(info.get_pvp_dmg());
				}
				if (info.get_pvp_reduction() != 0) {
					os.writeC(60);
					os.writeC(info.get_pvp_reduction());
				}
				if (info.get_dg() != 0) {
					os.writeC(51);
					os.writeC(info.get_dg());
				}
				if (info.get_er() != 0) {
					os.writeC(93);
					os.writeC(info.get_er());
				}
				if (info.get_ac() != 0) {
					os.writeC(56);
					os.writeC(-info.get_ac());
				}
				if (info.get_mr() != 0) {
					os.writeC(15);
					os.writeH(info.get_mr());
				}
				if (info.get_sp() != 0) {
					os.writeC(17);
					os.writeC(info.get_sp());
				}
				if (info.get_weight() != 0) {
					os.writeC(90);
					os.writeH(info.get_weight());
				}
				if (info.get_max_hp() != 0) {
					os.writeC(14);
					os.writeH(info.get_max_hp());
				}
				if (info.get_max_mp() != 0) {
					os.writeC(32);
					os.writeH(info.get_max_mp());
				}
				if (info.get_resis_fire() != 0) {
					os.writeC(27);
					os.writeC(info.get_resis_fire());
				}
				if (info.get_resis_earth() != 0) {
					os.writeC(30);
					os.writeC(info.get_resis_earth());
				}
				if (info.get_resis_water() != 0) {
					os.writeC(28);
					os.writeC(info.get_resis_water());
				}
				if (info.get_resis_wind() != 0) {
					os.writeC(29);
					os.writeC(info.get_resis_wind());
				}
				if (info.get_resis_all() != 0) {
					os.writeC(155);
					os.writeC(info.get_resis_all());
				}
				if (info.get_m_resis_ability() != 0) {
					os.writeC(117);
					os.writeC(info.get_m_resis_ability());
				}
				if (info.get_m_resis_spirit() != 0) {
					os.writeC(118);
					os.writeC(info.get_m_resis_spirit());
				}
				if (info.get_m_resis_dragon() != 0) {
					os.writeC(119);
					os.writeC(info.get_m_resis_dragon());
				}
				if (info.get_m_resis_fear() != 0) {
					os.writeC(120);
					os.writeC(info.get_m_resis_fear());
				}
				if (info.get_m_resis_all() != 0) {
					os.writeC(121);
					os.writeC(info.get_m_resis_all());
				}
				if (info.get_m_hit_ability() < 0) {
					os.writeC(122);
					os.writeC(info.get_m_hit_ability());
				}
				if (info.get_m_hit_spirit() != 0) {
					os.writeC(123);
					os.writeC(info.get_m_hit_spirit());
				}
				if (info.get_m_hit_dragon() != 0) {
					os.writeC(124);
					os.writeC(info.get_m_hit_dragon());
				}
				if (info.get_m_hit_fear() != 0) {
					os.writeC(125);
					os.writeC(info.get_m_hit_fear());
				}
				if (info.get_m_hit_all() != 0) {
					os.writeC(126);
					os.writeC(info.get_m_hit_all());
				}
				if (info.get_exp() != 0) {
					os.writeC(36);
					os.writeC(info.get_exp());
				}
				if (info.get_magichit() != 0) {
					os.writeC(40);
					os.writeC(info.get_magichit());
				}
				if (info.get_me() != 0) {
					os.writeC(89);
					os.writeD(info.get_me());
				}
				if (info.get_magic_critical() != 0) {
					os.writeC(50);
					os.writeH(info.get_magic_critical());
				}
				if (info.get_reduction_per() !=0){
					os.writeC(192);
					os.writeH(info.get_reduction_per());
				}
				
				if (info.get_attackspeed() != 0) {
					os.writeC(39);
					os.writeS("\fI效果:\aA 攻擊速度增加 ");
				}
				if (info.get_dragon_pear()) {
					os.writeC(39);
					os.writeS("\aA3段加速效果 ");
				}
				}
				if (info.get_hpRegen() != 0){
		        	os.writeC(205);
					os.writeH(info.get_hpRegen());
//		        	os.writeC(39);
//		        	os.writeS("\\fIHP 絕對恢復:\\aA +"+info.get_hpRegen()+"(32초)");
				}
				if (info.get_mpRegen() !=0){
					os.writeC(204);
					os.writeH(info.get_mpRegen());
//					os.writeC(39);
//		        	os.writeS("\\fIMP 絕對恢復:\\aA +"+info.get_mpRegen()+"(64초)");
				}
				if (info.get_pvp_magic_reduc() != 0){
					os.writeC(166);
					os.writeC(info.get_pvp_magic_reduc());
				}
				if (info.get_pvp_reduction_per() != 0){
					os.writeC(193);
					os.writeC(info.get_pvp_reduction_per());
				}
/*				if (info.get_armor_magic_pro() != 0){
					os.writeC(244);
					os.writeH(info.get_armor_magic_pro());
				}*/
				if (info.get_status_time_reduce() != 0){
					os.writeC(241);
					os.writeH(info.get_status_time_reduce());
				}
				if (info.get_portion_ration() != 0){
					os.writeC(65);
					os.writeC(info.get_portion_ration());
					os.writeC(info.get_portion_ration());
				}
				return os.getBytes();
			}
		}catch(

	Exception e)
	{
		e.printStackTrace();
	}finally
	{
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}return null;
}}
