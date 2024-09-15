package l1j.server.tempSkillSystem;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJPassiveSkill.MJPassiveInfo;
import l1j.server.MJPassiveSkill.MJPassiveLoader;
import l1j.server.MJPassiveSkill.MJPassiveUserLoader;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ADD_SPELL_PASSIVE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_AVAILABLE_SPELL_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Spell.SC_ADD_TEMP_PASSIVE_SPELL_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Spell.SC_REMOVE_TEMP_PASSIVE_SPELL_NOTI;
import l1j.server.server.GameClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.clientpackets.C_LoginToServer;
import l1j.server.server.clientpackets.C_NewCharSelect;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.BinaryOutputStream;

public class tempSkillSystemInfo {
	static tempSkillSystemInfo newInstance(ResultSet rs) throws SQLException{
		tempSkillSystemInfo info = newInstance();
		info._item_id = rs.getInt("item_id");
		info._skill_id = rs.getInt("skill_id");
		info._passive_id = rs.getInt("passive_id");
		info._type = rs.getString("type");
		info._class = rs.getString("class");
		info._skill_name = rs.getString("skill_name");
		info._passive_name = rs.getString("passive_name");
		return info;
	}
	private static tempSkillSystemInfo newInstance() {
		return new tempSkillSystemInfo();
	}
	private String _skill_name;
	private String get_skill_name() {
		return _skill_name;
	}

	private String _passive_name;
	private String get_passive_name() {
		return _passive_name;
	}

	private int _item_id;
	public int get_item_id() {
		return _item_id;
	}
	private int _skill_id;
	public int get_skill_id() {
		return _skill_id;
	}
	private int _passive_id;
	public int get_passive_id() {
		return _passive_id;
	}

	private String _type;
	public String get_type() {
		return _type;
	}

	public int get_type_int() {
		int type = 0;
		switch (get_type()) {
			case "active":
				type = 1;
				break;
			case "passive":
				type = 2;
				break;
			case "legend":
				type = 3;
				break;
		}

		return type;
	}

	private String _class;
	public String get_class() {
		return _class;
	}
	public int get_class_id() {
		int class_id = 0;
		switch (get_class()) {
			case "王族":
				class_id = 0;
				break;
			case "騎士":
				class_id = 1;
				break;
			case "妖精":
				class_id = 2;
				break;
			case "法師":
				class_id = 3;
				break;
			case "黑暗妖精":
				class_id = 4;
				break;
			case "龍騎士":
				class_id = 5;
				break;
			case "幻術師":
				class_id = 6;
				break;
			case "戰士":
				class_id = 7;
				break;
			case "劍士":
				class_id = 8;
				break;
			case "黃金槍騎":
				class_id = 9;
				break;
		}
		return class_id;
	}

	public static void temp_skill(L1PcInstance pc, int itemId, boolean onOff) {
		tempSkillSystemInfo info = tempSkillSystemLoader.getInstance().getTempSkillSystemInfo(itemId);
		switch (info.get_type()) {
			case "active":
				temp_skill_active(pc, itemId, onOff);
				break;
			case "passive":
				temp_skill_passive(pc, itemId, onOff);
				break;
			case "legend":
				temp_skill_legend(pc, itemId, onOff);
		}
	}

	private static void temp_skill_legend(L1PcInstance pc, int itemid, boolean onOff) {
		int classid;
		classid = pc.getType();
		switch (classid) {
			case 0://王族 Prime
			case 1://騎士 Force Stun
			case 3://法師 Eternity
			case 4://黑暗妖精 Avenger
			case 6://幻術師 Potential
			case 5://龍騎士 Halpas
				temp_skill_active(pc, itemid, onOff);
				break;
			case 2://妖精 Glorious
			case 7://戰士 Demolition
			case 9://黃金槍騎 Cruel Conviction
				temp_skill_passive(pc, itemid, onOff);
				break;
			case 8:
				break;
		}
	}

	private static void temp_skill_active(L1PcInstance pc, int itemid, boolean onOff) {
		tempSkillSystemInfo info = tempSkillSystemLoader.getInstance().getTempSkillSystemInfo(itemid);
		int skillid = info.get_skill_id();
		if (info.get_type_int() == 3) {
			int classid = pc.getType();
			// 對應職業的技能ID設置
			int skillid;
			switch (get_class_id()) {
				case 0:// 王族 Prime
					skillid = 241;
					break;
				case 1:// 騎士 Force Stun
					skillid = 242;
					break;
				case 3:// 法師 Eternity
					skillid = 243;
					break;
				case 4:// 黑暗妖精 Avenger
					skillid = 244;
					break;
				case 6:// 幻術師 Potential
					skillid = 246;
					break;
				case 5:// 龍騎士 Halpas
					skillid = 245;
					break;
				case 8:// 劍士
					skillid = 249; // 假設的技能ID，請根據實際需求修改
					break;
			}
		}


		if (!onOff && pc.getInventory().countItems(itemid) <= 1) {
			pc.delTempSkillActive(skillid);
		}
		if (pc.isTempSkillActive(skillid)) {
			return;
		}
//		System.out.println(pc.getType()+"+"+info.get_class_id());
		if (info.get_type_int() != 3) {
			if (pc.getType() != info.get_class_id()) {
				return;
			}
		}

		if (SkillsTable.getInstance().spellCheck(pc.getId(), skillid)) {
			return;
		}

		if (onOff) {
			SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
			noti.appendNewSpell(skillid, true);
			pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
		} else if (!onOff){
			SC_AVAILABLE_SPELL_NOTI noti = SC_AVAILABLE_SPELL_NOTI.newInstance();
			noti.appendNewSpell(skillid, false);
			pc.sendPackets(noti, MJEProtoMessages.SC_AVAILABLE_SPELL_NOTI, true);
		}

		if (onOff) {
			pc.addTempSkillActive(skillid);
		}
	}
	private static void temp_skill_passive(L1PcInstance pc, int itemid, boolean onOff) {
		tempSkillSystemInfo info = tempSkillSystemLoader.getInstance().getTempSkillSystemInfo(itemid);
		int passiveid = info.get_passive_id();
		if (info.get_type_int() == 3) {
			int classid = pc.getType();
			switch (classid) {
				// 對應職業的被動技能ID設置
				int passiveid;
				switch (get_class_id()) {
					case 2:// 妖精 Glorious
						passiveid = 22;
						break;
					case 7:// 戰士 Demolition
						passiveid = 90;
						break;
					case 9:// 黃金槍騎 Cruel Conviction
						passiveid = 52;
						break;
				}
			}

		if (!onOff && pc.getInventory().countItems(itemid) <= 1) {
			pc.delTempSkillPassive(passiveid);
		}
		if (pc.isTempSkillPassive(passiveid)) {
			return;
		}

		if (info.get_type_int() != 3) {
			if (pc.getType() != info.get_class_id()) {
				return;
			}
		}
		MJPassiveInfo pInfo = MJPassiveLoader.getInstance().fromPassiveId(passiveid);
/*		if (pc.getLevel() < pInfo.getUseMinLevel()) {
			return;
		}
		if (pc.isPassive(pInfo.getPassiveId())) {
			return;
		}*/

		if (onOff) {
			if (pc.isPassive(passiveid)) {
				return;
			}
			if (!MJPassiveLoader.getInstance().masterPassive(pc, pInfo, true)) {
				return;
			}
		} else if (!onOff) {
			pc.delPassive(passiveid);
			pc.sendPackets(SC_WORLD_PUT_NOTI.make_stream(pc, pc.getMap().isUnderwater(), false));
			pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, true));
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					GameClient clnt = pc.getNetConnection();
					String name = pc.getName();
					int x = pc.getX();
					int y = pc.getY();
					int mapId = pc.getMapId();
					C_NewCharSelect.restartProcess(pc); // 先重置
					try {
						Thread.sleep(700L); // 0.5秒
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PANTHERA, true));
						C_LoginToServer.doEnterWorld(name, clnt, false, x, y, mapId); // 嘗試重新連接
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}
			}, 500);//0.5秒
			if (pc.isElf()) {
				pc.setElfAttr(0);
				pc.setGlory_Earth_Attr(0);
			}
		}
		if (onOff) {
			pc.addTempSkillPassive(passiveid);
		}
	}


	public static byte[] getItemView(L1PcInstance pc, tempSkillSystemInfo info) {
		BinaryOutputStream os = new BinaryOutputStream();
		try {
			if (info != null) {
				if (info.get_type_int() == 1) { // 主動技能
					String s1 = info.get_skill_name();
					os.writeC(39);
					os.writeS("\\aA " + s1);
				} else if (info.get_type_int() == 2) { // 被動技能
					String s2 = info.get_passive_name();
					os.writeC(39);
					os.writeS("\\aA " + s2);
				} else if (info.get_type_int() == 3) { // 傳說
					String s3 = "";
					if (pc.getType() == 0 || pc.getType() == 1 || pc.getType() == 3 || pc.getType() == 4
							|| pc.getType() == 6 || pc.getType() == 5) {
						switch (pc.getType()) {
							case 0:// 王族 Prime
								s3 = "Prime";
								break;
							case 1:// 騎士 Force Stun
								s3 = "Force Stun";
								break;
							case 3:// 法師 Eternity
								s3 = "Eternity";
								break;
							case 4:// 黑暗妖精 Avenger
								s3 = "Avenger";
								break;
							case 6:// 幻術師 Potential
								s3 = "Potential";
								break;
							case 5:// 龍騎士 Halpas
								s3 = "Halpas";
								break;
						}
						os.writeC(39);
						os.writeS("\\aA "+s3);
					} else if (pc.getType() == 2 || pc.getType() == 7 || pc.getType() == 9) {
						String s4 ="";
						switch (pc.getType()) {
							case 2:// 妖精 Glorious
								s4 = "Glorious";
								break;
							case 7: // 戰士 Demolition
								s4 = "Demolition";
								break;
							case 9: // 黃金槍騎 Cruel Conviction
								s4 = "Cruel Conviction";
								break;
						}
						os.writeC(39);
						os.writeS("\\aA "+s4);
					}
				}
				return os.getBytes();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
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

}}
