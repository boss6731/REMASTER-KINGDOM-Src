package l1j.server.AinhasadSpecialStat2;

import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_SPMR;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AinhasadSpecialStat2Info {
	static AinhasadSpecialStat2Info newInstance(ResultSet rs) throws SQLException {
		AinhasadSpecialStat2Info Info = newInstance();
//		Info._obj_id = rs.getInt("obj_id");
//		Info._char_name = rs.getString("char_name");
		Info._group = rs.getInt("group_id");
		Info._index = rs.getInt("index_id");
		Info._hours = rs.getInt("hours");
		Info._magic_hit = rs.getInt("magic_hit");
		Info._all_pierce = rs.getInt("all_pierce");
		Info._pvp_add_dmg = rs.getInt("pvp_add_dmg");
		Info._max_mp = rs.getInt("max_mp");
		Info._add_exp = rs.getInt("add_exp");
		Info._me = rs.getInt("me");
		Info._all_resis = rs.getInt("all_resis");
		Info._pvp_reduction = rs.getInt("pvp_reduction");
		Info._max_hp = rs.getInt("max_hp");	
		Info._attack_speed = rs.getInt("attack_speed");
		Info._move_speed = rs.getInt("move_speed");
		Info._desc_id = rs.getInt("desc_id");
		Info._point = rs.getInt("point");
		Info._type	= rs.getInt("type");
		return Info;
	}
	
	private static AinhasadSpecialStat2Info newInstance() {
		return new AinhasadSpecialStat2Info();
	}
	
//	private int _obj_id;
//	private String _char_name;
	private int _type;
	private int _point;
	private int _desc_id;
	private int _group;
	private int _index;
	private int _hours;
	private int _magic_hit;
	private int _all_pierce;
	private int _pvp_add_dmg;
	private int _max_mp;
	private int _add_exp;
	private int _me;
	private int _all_resis;
	private int _pvp_reduction;
	private int _max_hp;
	private int _attack_speed;
	private int _move_speed;
	
//	public int get_obj_id() {
//		return _obj_id;
//	}
	
//	public String get_char_name() {
//		return _char_name;
//	}
	public int get_type() {
		return _type;
	}
	public int get_point() {
		return _point;
	}
	public int get_desc_id() {
		return _desc_id;
	}
	
	public int get_group() {
		return _group;
	}
	
	public int set_group(int group) {
		return _group = group;
	}
	
	public int get_index() {
		return _index;
	}
	
	
	public int set_index(int index) {
		return _index = index;
	}
	
	public int get_hours() {
		return _hours;
	}
	
	public int set_hours(int hours) {
		return _hours = hours;
	}
	
	public int get_magic_hit() {
		return _magic_hit;
	}
	
	public int add_magic_hit(int magic_hit) {
		return _magic_hit += magic_hit;
	}
	
	public int set_magic_hit(int magic_hit) {
		return _magic_hit = magic_hit;
	}
	
	public int get_all_pierce() {
		return _all_pierce;
	}
	
	public int add_all_pierce(int all_pierce) {
		return _all_pierce += all_pierce;
	}
	
	public int set_all_pierce(int all_pierce) {
		return _all_pierce = all_pierce;
	}
	
	public int get_pvp_add_dmg() {
		return _pvp_add_dmg;
	}
	
	public int add_pvp_add_dmg(int pvp_add_dmg) {
		return _pvp_add_dmg += pvp_add_dmg;
	}
	
	public int set_pvp_add_dmg(int pvp_add_dmg) {
		return _pvp_add_dmg = pvp_add_dmg;
	}
	
	public int get_max_mp() {
		return _max_mp;
	}
	
	public int add_max_mp(int max_mp) {
		return _max_mp += max_mp;
	}
	
	public int set_max_mp(int max_mp) {
		return _max_mp = max_mp;
	}
	
	public int get_add_exp() {
		return _add_exp;
	}
	
	public int add_add_exp(int add_exp) {
		return _add_exp += add_exp;
	}
	
	public int set_add_exp(int add_exp) {
		return _add_exp = add_exp;
	}
	
	public int get_me() {
		return _me;
	}
	
	public int add_me(int me) {
		return _me += me;
	}
	
	public int set_me(int me) {
		return _me = me;
	}
	
	public int get_all_resis() {
		return _all_resis;
	}
	
	public int add_all_resis(int all_resis) {
		return _all_resis += all_resis;
	}
	
	public int set_all_resis(int all_resis) {
		return _all_resis = all_resis;
	}
	
	public int get_pvp_reduction() {
		return _pvp_reduction;
	}
	
	public int add_pvp_reduction(int pvp_reduction) {
		return _pvp_reduction += pvp_reduction;
	}
	
	public int set_pvp_reduction(int pvp_reduction) {
		return _pvp_reduction = pvp_reduction;
	}
	
	public int get_max_hp() {
		return _max_hp;
	}
	
	public int add_max_hp(int max_hp) {
		return _max_hp += max_hp;
	}
	
	public int set_max_hp(int max_hp) {
		return _max_hp = max_hp;
	}
	
	public int get_attack_speed() {
		return _attack_speed;
	}
	
	public int add_attack_speed(int attack_speed) {
		return _attack_speed += attack_speed;
	}
	
	public int set_attack_speed(int attack_speed) {
		return _attack_speed = attack_speed;
	}
	
	public int get_move_speed() {
		return _move_speed;
	}
	
	public int add_move_speed(int move_speed) {
		return _move_speed += move_speed;
	}
	
	public int set_move_speed(int move_speed) {
		return _move_speed = move_speed;
	}
	
	public static void einhasad_faith_option(L1PcInstance pc, int index, Timestamp endTime, boolean onOff) {
		AinhasadSpecialStat2Info info = AinhasadSpecialStat2Loader.getInstance().getSpecialStat(index);
		if (!onOff) {
			pc.delAinhasad_faith(index);
		}
		if (pc.isAinhasad_faith(index)) {
			return;
		}
		
		int type = onOff ? 1 : -1;
		if (info != null) {
			if (info.get_magic_hit() != 0) {
				pc.addBaseMagicHitUp(type * info.get_magic_hit());
			}
			if (info.get_all_pierce() != 0) {
				pc.addSpecialPierce(eKind.ALL, type * info.get_all_pierce());
			}
			if (info.get_pvp_add_dmg() !=0 ) {
				pc.getResistance().addPVPweaponTotalDamage(type * info.get_pvp_add_dmg());
			}
			if (info.get_max_mp() != 0) {
				pc.addMaxMp(type * info.get_max_mp());
			}
			if (info.get_add_exp() != 0) {
				pc.add_item_exp_bonus(type * info.get_add_exp());
			}
			if (info.get_me() != 0) {
				pc.addMagicDodgeProbability(type * info.get_me());
			}
			if (info.get_all_resis() != 0) {
				pc.addSpecialResistance(eKind.ALL, type * info.get_all_resis());
			}
			if (info.get_pvp_reduction() != 0) {
				pc.getResistance().addcalcPcDefense(type * info.get_pvp_reduction());
			}
			if (info.get_max_hp() != 0) {
				pc.addMaxHp(type * info.get_max_hp());
			}
			if (info.get_attack_speed() != 0) {
				pc.addAttackDelayRate(type * info.get_attack_speed());
			}
			if (info.get_move_speed() != 0) {
				pc.addMoveDelayRate(type * info.get_move_speed());
			}
			if (onOff) {
				pc.addAinhasad_faith(index, endTime);
			}
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			pc.sendPackets(new S_HPUpdate(pc));
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			pc.sendPackets(new S_SPMR(pc));
		}
	}
}
