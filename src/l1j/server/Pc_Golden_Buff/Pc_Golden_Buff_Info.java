package l1j.server.Pc_Golden_Buff;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Info;
import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Loader;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPECIAL_RESISTANCE_NOTI.eKind;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.utils.BinaryOutputStream;

public class Pc_Golden_Buff_Info {
	static Pc_Golden_Buff_Info newInstance(ResultSet rs) throws SQLException {
		Pc_Golden_Buff_Info Info = newInstance();
		Info._buff_id = rs.getInt("buff_id");
		Info._group = rs.getInt("group_id");
		Info._index = rs.getInt("index_id");
		Info._buff_type = rs.getInt("buff_type");
		Info._grade = rs.getInt("grade");
		Info._hours = rs.getInt("add_hour");
		Info._ac = rs.getInt("ac");
		Info._short_dmg = rs.getInt("short_dmg");
		Info._long_dmg = rs.getInt("long_dmg");
		Info._short_hit = rs.getInt("short_hit");
		Info._long_hit = rs.getInt("long_hit");
		Info._mr = rs.getInt("mr");
		Info._magic_hit = rs.getInt("magic_hit");
		Info._sp = rs.getInt("sp");
		Info._require_count = rs.getInt("require_count");
		
		return Info;
	}
	
	private static Pc_Golden_Buff_Info newInstance() {
		return new Pc_Golden_Buff_Info();
	}
	
//	private int _obj_id;
//	private String _char_name;
	private int _buff_id;
	private int _group;
	private int _index;
	private int _buff_type;
	private int _grade;
	private int _hours;
	private int _require_count;
	private int _ac, _short_dmg, _long_dmg, _short_hit, _long_hit, _mr, _sp, _magic_hit;
	
	
//	public int get_obj_id() {
//		return _obj_id;
//	}
	
//	public String get_char_name() {
//		return _char_name;
//	}
	public int get_Buff_Id() {
		return _buff_id;
	}
	public int get_Group() {
		return _group;
	}
	public int get_Index() {
		return _index;
	}
	public int get_Buff_Type(){
		return _buff_type;
	}
	public int get_Grade() {
		return _grade;
	}
	public int get_Hours() {
		return _hours;
	}
	public int get_Require_Count() {
		return _require_count;
	}
	public int get_Ac() {
		return _ac;
	}
	public int get_Short_Dmg() {
		return _short_dmg;
	}
	public int get_Short_Hit() {
		return _short_hit;
	}
	public int get_Long_Dmg() {
		return _long_dmg;
	}
	public int get_Long_Hit() {
		return _long_hit;
	}
	public int get_Mr() {
		return _mr;
	}
	public int get_Sp() {
		return _sp;
	}
	public int get_Magic_Hit() {
		return _magic_hit;
	}
	
	public static void Pc_Golden_Buff_Option(L1PcInstance pc, int buffid, boolean onOff) {
		Pc_Golden_Buff_Info info = Pc_Golden_Buff_Loader.getInstance().getBuffOption(buffid);
		if (!onOff) {
			pc.delPcGoldenBuff(buffid);
		}
		if (pc.isPcGoldenBuff(buffid)) {
			return;
		}

		int type = onOff ? 1 : -1;
		if (info != null) {
			if (info.get_Ac() != 0) {
				pc.getAC().addAc(type * -info.get_Ac());
			}
			if (info.get_Mr() !=0) {
				pc.getResistance().addMr(type * info.get_Mr());
			}
			if (info.get_Short_Dmg() != 0) {
				pc.addDmgup(type * info.get_Short_Dmg());
			}
			if (info.get_Short_Hit() != 0) {
				pc.addHitup(type * info.get_Short_Hit());
			}
			if (info.get_Long_Dmg() != 0) {
				pc.addBowDmgup(type * info.get_Long_Dmg());
			}
			if (info.get_Long_Hit() != 0) {
				pc.addBowHitup(type * info.get_Long_Hit());
			}
			if (info.get_Magic_Hit() != 0) {
				pc.addMagicHit(type * info.get_Magic_Hit());
			}
			if (info.get_Sp() !=0) {
				pc.getAbility().addSp(type * info.get_Sp());
			}
			
			if (onOff) {
				pc.addPcGoldenBuff(buffid);
			}
			
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			SC_SPECIAL_RESISTANCE_NOTI.sendCharacterInfo(pc);
			pc.sendPackets(new S_HPUpdate(pc));
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			pc.sendPackets(new S_SPMR(pc));
		}
	}
	public static byte[] getOptionView(Pc_Golden_Buff_Info info) {
		BinaryOutputStream os = new BinaryOutputStream();
		try {
			if (info != null) {
				if (info.get_Ac() != 0) {
					os.writeC(56);
					os.writeC(info.get_Ac());
				}
				if (info.get_Mr() !=0) {
					os.writeC(15);
					os.writeH(info.get_Mr());
				}
				if (info.get_Short_Dmg() != 0) {
					os.writeC(47);
					os.writeC(info.get_Short_Dmg());
				}
				if (info.get_Short_Hit() != 0) {
					os.writeC(48);
					os.writeC(info.get_Short_Hit());
				}
				if (info.get_Long_Dmg() != 0) {
					os.writeC(35);
					os.writeC(info.get_Long_Dmg());
				}
				if (info.get_Long_Hit() != 0) {
					os.writeC(24);
					os.writeC(info.get_Long_Hit());
				}
				if (info.get_Magic_Hit() != 0) {
					os.writeC(40);
					os.writeC(info.get_Magic_Hit());
				}
				if (info.get_Sp() !=0) {
					os.writeC(17);
					os.writeC(info.get_Sp());
				}

				return os.getBytes();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
}
