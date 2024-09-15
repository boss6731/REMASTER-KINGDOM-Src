package l1j.server.server.model.skill;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.server.Controller.SkillDataController;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;

public class SkillData {
	
	private L1PcInstance pc;
	
	public SkillData(L1PcInstance cha) {
		pc = cha;
	}
	
	private boolean str_ice;
	
	public boolean get_str_ice() {
		return str_ice;
	} 
	
	private int str_ice_time;
	public int get_str_ice_time() {
		return str_ice_time;
	}
	public void set_str_ice_time(int i) {
		this.str_ice_time = i;
	}
	
	public void start_str_ice() {
		end_str_ice();
		end_dex_ice();
		end_int_ice();
		pc.removeSkillEffect(L1SkillId.STR_BUFF);
		pc.removeSkillEffect(L1SkillId.DEX_BUFF);
		pc.removeSkillEffect(L1SkillId.INT_BUFF);
		if (!str_ice) {
			str_ice = true;
			str_ice_time = 900;
			pc.addHitup(5);
			pc.addDmgup(3);
			pc.getAbility().addAddedStr(1);
			pc.setSkillEffect(L1SkillId.STR_BUFF, str_ice_time * 1000);
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			SkillDataController.getInstance().add_str_ice(pc);
			pc.send_effect(9820);
//			pc.sendPackets(new S_SkillSound(pc.getId(), 7976), true);			
//			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7976), true);
			
			SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_spell_id(L1SkillId.STR_BUFF);
			noti.set_duration(str_ice_time);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(4354);
			noti.set_off_icon_id(4354);
			noti.set_tooltip_str_id(1720);
			noti.set_new_str_id(1720);
			noti.set_end_str_id(2854);
			noti.set_is_good(true);
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		}
	}
	public void end_str_ice() {
		if (str_ice) {
			str_ice = false;
			str_ice_time = 0;
			/*pc.addHitup(-5);
			pc.addDmgup(-3);
			pc.getAbility().addAddedStr(-1);
			pc.sendPackets(new S_OwnCharStatus(pc), true);*/
		}
	}
	
	private boolean dex_ice;
	
	public boolean get_dex_ice() {
		return dex_ice;
	} 
	
	private int dex_ice_time;
	public int get_dex_ice_time() {
		return dex_ice_time;
	}
	public void set_dex_ice_time(int i) {
		this.dex_ice_time = i;
	}
	
	public void start_dex_ice() {
		end_str_ice();
		end_dex_ice();
		end_int_ice();
		pc.removeSkillEffect(L1SkillId.STR_BUFF);
		pc.removeSkillEffect(L1SkillId.DEX_BUFF);
		pc.removeSkillEffect(L1SkillId.INT_BUFF);
		if (!dex_ice) {
			dex_ice = true;
			dex_ice_time = 900;
			pc.addBowHitup(5);
			pc.addBowDmgup(3);
			pc.getAbility().addAddedDex((byte) 1);
			pc.setSkillEffect(L1SkillId.DEX_BUFF, dex_ice_time * 1000);
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()), true);
			SkillDataController.getInstance().add_dex_ice(pc);
			pc.send_effect(9817);
//			pc.sendPackets(new S_SkillSound(pc.getId(), 7976), true);
//			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7976), true);
			
			SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_spell_id(L1SkillId.DEX_BUFF);
			noti.set_duration(dex_ice_time);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(4354);
			noti.set_off_icon_id(4354);
			noti.set_tooltip_str_id(1719);
			noti.set_new_str_id(1719);
			noti.set_end_str_id(2854);
			noti.set_is_good(true);
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		}
	}
	public void end_dex_ice() {
		if (dex_ice) {
			dex_ice = false;
			dex_ice_time = 0;
			/*pc.addBowHitup(-5);
			pc.addBowDmgup(-3);
			pc.getAbility().addAddedDex(-1);
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.ER_UpDate, pc.getTotalER()), true);*/
		}
	}
	
	private boolean int_ice;
	
	public boolean get_int_ice() {
		return int_ice;
	} 
	
	private int int_ice_time;
	public int get_int_ice_time() {
		return int_ice_time;
	}
	public void set_int_ice_time(int i) {
		this.int_ice_time = i;
	}
	
	public void start_int_ice() {
		end_str_ice();
		end_dex_ice();
		end_int_ice();
		pc.removeSkillEffect(L1SkillId.STR_BUFF);
		pc.removeSkillEffect(L1SkillId.DEX_BUFF);
		pc.removeSkillEffect(L1SkillId.INT_BUFF);
		if (!int_ice) {
			int_ice = true;
			int_ice_time = 900;
			pc.addMaxMp(50);
			pc.getAbility().addSp(2);
			pc.getAbility().addAddedInt(1);
			pc.setSkillEffect(L1SkillId.INT_BUFF, int_ice_time * 1000);
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
			SkillDataController.getInstance().add_int_ice(pc);
			pc.send_effect(9818);
//			pc.sendPackets(new S_SkillSound(pc.getId(), 7976), true);			
//			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7976), true);
			
			SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_spell_id(L1SkillId.INT_BUFF);
			noti.set_duration(int_ice_time);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(4354);
			noti.set_off_icon_id(4354);
			noti.set_tooltip_str_id(1721);
			noti.set_new_str_id(1721);
			noti.set_end_str_id(2854);
			noti.set_is_good(true);
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		}
	}
	public void end_int_ice() {
		if (int_ice) {
			int_ice = false;
			int_ice_time = 0;
			/*pc.addMaxMp(-50);
			pc.getAbility().addSp(-2);
			pc.getAbility().addAddedInt(-1);
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
			pc.sendPackets(new S_OwnCharStatus(pc), true);*/
		}
	}
	
	private boolean str_aden_scroll;
	private boolean dex_aden_scroll;
	private boolean int_aden_scroll;
	private int str_aden_time;
	private int dex_aden_time;
	private int int_aden_time;
	
	public boolean get_str_aden_scroll() {
		return str_aden_scroll;
	} 
	
	public int get_str_aden_scroll_time() {
		return str_aden_time;
	}
	public void set_str_aden_scroll_time(int i) {
		this.str_aden_time = i;
	}
	
	public boolean get_dex_aden_scroll() {
		return dex_aden_scroll;
	} 
	
	public int get_dex_aden_scroll_time() {
		return dex_aden_time;
	}
	public void set_dex_aden_scroll_time(int i) {
		this.dex_aden_time = i;
	}
	
	public boolean get_int_aden_scroll() {
		return int_aden_scroll;
	} 
	
	public int get_int_aden_scroll_time() {
		return int_aden_time;
	}
	public void set_int_aden_scroll_time(int i) {
		this.int_aden_time = i;
	}
	
	public void delete_aden_scroll() {
		if (get_str_aden_scroll()) {
			str_aden_scroll = false;
			str_aden_time = 0;
			pc.removeSkillEffect(L1SkillId.STR_ADEN_SCROLL_BUFF);
		}
		if (get_dex_aden_scroll()) {
			dex_aden_scroll = false;
			dex_aden_time = 0;
			pc.removeSkillEffect(L1SkillId.DEX_ADEN_SCROLL_BUFF);
		}
		if (get_int_aden_scroll()) {
			int_aden_scroll = false;
			int_aden_time = 0;
			pc.removeSkillEffect(L1SkillId.INT_ADEN_SCROLL_BUFF);
		}
	}
	
	public void start_str_aden_scroll() {
		delete_aden_scroll();
		if (!get_str_aden_scroll()) {
			str_aden_scroll = true;
			set_str_aden_scroll_time(900);
			pc.addHitup(3);
			pc.addDmgup(3);
			pc.getAbility().addAddedStr(1);
			pc.setSkillEffect(L1SkillId.STR_ADEN_SCROLL_BUFF, str_aden_time * 1000);
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
			SkillDataController.getInstance().add_int_ice(pc);
			pc.send_effect(9818);
//			pc.sendPackets(new S_SkillSound(pc.getId(), 7976), true);			
//			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7976), true);
			
			SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
			noti.set_noti_type(eNotiType.RESTAT);
			noti.set_spell_id(L1SkillId.INT_BUFF);
			noti.set_duration(int_ice_time);
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(4354);
			noti.set_off_icon_id(4354);
			noti.set_tooltip_str_id(1721);
			noti.set_new_str_id(1721);
			noti.set_end_str_id(2854);
			noti.set_is_good(true);
			pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
		}
	}
	
	
	
	
	
	

}