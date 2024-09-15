package l1j.server.server.model.skill.noti;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eDurationShowType;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI.eNotiType;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJNotiSkillModel {
	static MJNotiSkillModel newModel(ResultSet rs) throws SQLException{
		MJNotiSkillModel model = new MJNotiSkillModel();
		model.skillId = rs.getInt("skill_id");
		model.iconId = rs.getInt("icon_id");
		model.tooltipStrId = rs.getInt("tooltip_strid");
		model.newStrId = rs.getInt("new_strid");
		model.endStrId = rs.getInt("end_strid");
		model.isGood = rs.getBoolean("is_good");
		model.connectedPassiveId = rs.getInt("connected_passive_id");
		model.connectedPassvieIconId = rs.getInt("connected_passive_icon_id");
		return model;
	}
	
	public void icons(L1PcInstance pc, int duration, boolean onOff) {
		SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
		noti.set_spell_id(skillId);
		noti.set_noti_type(onOff ? eNotiType.RESTAT : eNotiType.END);
		noti.set_duration(onOff ? duration : 0);
//		System.out.println(skillId);
		if (onOff) {
			noti.set_duration_show_type(eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
			noti.set_on_icon_id(iconId);
			// noti.set_icon_priority(10); // 圖標位置選定
			if (tooltipStrId > 0) noti.set_tooltip_str_id(tooltipStrId);
			if (newStrId > 0) noti.set_new_str_id(newStrId);
			if (endStrId > 0) noti.set_end_str_id(endStrId);
			noti.set_is_good(isGood);
		}
		noti.set_off_icon_id(iconId);
		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
	}
	
	int skillId;
	int iconId;
	int tooltipStrId;
	int newStrId;
	int endStrId;
	boolean isGood;
	int connectedPassiveId;
	int connectedPassvieIconId;
	private MJNotiSkillModel(){
	}
	
	public int skillId(){
		return skillId;
	}
	
	public int iconId(){
		return iconId;
	}
	
	public int tooltipStrId(){
		return tooltipStrId;
	}
	
	public int newStrId(){
		return newStrId;
	}
	
	public int endStrId(){
		return endStrId;
	}
	
	public boolean isGood(){
		return isGood;
	}
	
	public int connectedPassiveId() {
		return connectedPassiveId;
	}
	
	public int connectedPassvieIconId() {
		return connectedPassvieIconId;
	}
}
