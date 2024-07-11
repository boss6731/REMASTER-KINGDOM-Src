package l1j.server.server.model.skill.noti;

import java.sql.ResultSet;
import java.sql.SQLException;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJNotiSkillModel {
    int skillId;

    int iconId;

    int tooltipStrId;

    int newStrId;

    int endStrId;

    boolean isGood;

    int connectedPassiveId;

    int connectedPassvieIconId;

    static MJNotiSkillModel newModel(ResultSet rs) throws SQLException {
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
        noti.set_spell_id(this.skillId);
        noti.set_noti_type(onOff ? SC_SPELL_BUFF_NOTI.eNotiType.RESTAT : SC_SPELL_BUFF_NOTI.eNotiType.END);
        noti.set_duration(onOff ? duration : 0);
        if (onOff) {
            noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
            noti.set_on_icon_id(this.iconId);
            if (this.tooltipStrId > 0)
                noti.set_tooltip_str_id(this.tooltipStrId);
            if (this.newStrId > 0)
                noti.set_new_str_id(this.newStrId);
            if (this.endStrId > 0)
                noti.set_end_str_id(this.endStrId);
            noti.set_is_good(this.isGood);
        }
        noti.set_off_icon_id(this.iconId);
        pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
    }

    public int skillId() {
        return this.skillId;
    }

    public int iconId() {
        return this.iconId;
    }

    public int tooltipStrId() {
        return this.tooltipStrId;
    }

    public int newStrId() {
        return this.newStrId;
    }

    public int endStrId() {
        return this.endStrId;
    }

    public boolean isGood() {
        return this.isGood;
    }

    public int connectedPassiveId() {
        return this.connectedPassiveId;
    }

    public int connectedPassvieIconId() {
        return this.connectedPassvieIconId;
    }
}
