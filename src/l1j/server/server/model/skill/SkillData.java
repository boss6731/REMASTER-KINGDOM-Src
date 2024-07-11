package l1j.server.server.model.skill;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
import l1j.server.server.Controller.SkillDataController;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.ServerBasePacket;

public class SkillData {
    private L1PcInstance pc;

    private boolean str_ice;

    private int str_ice_time;

    private boolean dex_ice;

    private int dex_ice_time;

    private boolean int_ice;

    private int int_ice_time;

    private boolean str_aden_scroll;

    private boolean dex_aden_scroll;

    private boolean int_aden_scroll;

    private int str_aden_time;

    private int dex_aden_time;

    private int int_aden_time;

    public SkillData(L1PcInstance cha) {
        this.pc = cha;
    }

    public boolean get_str_ice() {
        return this.str_ice;
    }

    public int get_str_ice_time() {
        return this.str_ice_time;
    }

    public void set_str_ice_time(int i) {
        this.str_ice_time = i;
    }

    public void start_str_ice() {
        end_str_ice();
        end_dex_ice();
        end_int_ice();
        this.pc.removeSkillEffect(60208);
        this.pc.removeSkillEffect(60209);
        this.pc.removeSkillEffect(60210);
        if (!this.str_ice) {
            this.str_ice = true;
            this.str_ice_time = 900;
            this.pc.addHitup(5);
            this.pc.addDmgup(3);
            this.pc.getAbility().addAddedStr(1);
            this.pc.setSkillEffect(60208, (this.str_ice_time * 1000));
            this.pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(this.pc), true);
            SkillDataController.getInstance().add_str_ice(this.pc);
            this.pc.send_effect(9820);
            SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
            noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
            noti.set_spell_id(60208);
            noti.set_duration(this.str_ice_time);
            noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
            noti.set_on_icon_id(4354);
            noti.set_off_icon_id(4354);
            noti.set_tooltip_str_id(1720);
            noti.set_new_str_id(1720);
            noti.set_end_str_id(2854);
            noti.set_is_good(true);
            this.pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
        }
    }

    public void end_str_ice() {
        if (this.str_ice) {
            this.str_ice = false;
            this.str_ice_time = 0;
        }
    }

    public boolean get_dex_ice() {
        return this.dex_ice;
    }

    public int get_dex_ice_time() {
        return this.dex_ice_time;
    }

    public void set_dex_ice_time(int i) {
        this.dex_ice_time = i;
    }

    public void start_dex_ice() {
        end_str_ice();
        end_dex_ice();
        end_int_ice();
        this.pc.removeSkillEffect(60208);
        this.pc.removeSkillEffect(60209);
        this.pc.removeSkillEffect(60210);
        if (!this.dex_ice) {
            this.dex_ice = true;
            this.dex_ice_time = 900;
            this.pc.addBowHitup(5);
            this.pc.addBowDmgup(3);
            this.pc.getAbility().addAddedDex(1);
            this.pc.setSkillEffect(60209, (this.dex_ice_time * 1000));
            this.pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(this.pc), true);
            this.pc.sendPackets((ServerBasePacket)new S_PacketBox(132, this.pc.getTotalER()), true);
            SkillDataController.getInstance().add_dex_ice(this.pc);
            this.pc.send_effect(9817);
            SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
            noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
            noti.set_spell_id(60209);
            noti.set_duration(this.dex_ice_time);
            noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
            noti.set_on_icon_id(4354);
            noti.set_off_icon_id(4354);
            noti.set_tooltip_str_id(1719);
            noti.set_new_str_id(1719);
            noti.set_end_str_id(2854);
            noti.set_is_good(true);
            this.pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
        }
    }

    public void end_dex_ice() {
        if (this.dex_ice) {
            this.dex_ice = false;
            this.dex_ice_time = 0;
        }
    }

    public boolean get_int_ice() {
        return this.int_ice;
    }

    public int get_int_ice_time() {
        return this.int_ice_time;
    }

    public void set_int_ice_time(int i) {
        this.int_ice_time = i;
    }

    public void start_int_ice() {
        end_str_ice();
        end_dex_ice();
        end_int_ice();
        this.pc.removeSkillEffect(60208);
        this.pc.removeSkillEffect(60209);
        this.pc.removeSkillEffect(60210);
        if (!this.int_ice) {
            this.int_ice = true;
            this.int_ice_time = 900;
            this.pc.addMaxMp(50);
            this.pc.getAbility().addSp(2);
            this.pc.getAbility().addAddedInt(1);
            this.pc.setSkillEffect(60210, (this.int_ice_time * 1000));
            this.pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(this.pc), true);
            this.pc.sendPackets((ServerBasePacket)new S_MPUpdate(this.pc.getCurrentMp(), this.pc.getMaxMp()), true);
            SkillDataController.getInstance().add_int_ice(this.pc);
            this.pc.send_effect(9818);
            SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
            noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
            noti.set_spell_id(60210);
            noti.set_duration(this.int_ice_time);
            noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
            noti.set_on_icon_id(4354);
            noti.set_off_icon_id(4354);
            noti.set_tooltip_str_id(1721);
            noti.set_new_str_id(1721);
            noti.set_end_str_id(2854);
            noti.set_is_good(true);
            this.pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
        }
    }

    public void end_int_ice() {
        if (this.int_ice) {
            this.int_ice = false;
            this.int_ice_time = 0;
        }
    }

    public boolean get_str_aden_scroll() {
        return this.str_aden_scroll;
    }

    public int get_str_aden_scroll_time() {
        return this.str_aden_time;
    }

    public void set_str_aden_scroll_time(int i) {
        this.str_aden_time = i;
    }

    public boolean get_dex_aden_scroll() {
        return this.dex_aden_scroll;
    }

    public int get_dex_aden_scroll_time() {
        return this.dex_aden_time;
    }

    public void set_dex_aden_scroll_time(int i) {
        this.dex_aden_time = i;
    }

    public boolean get_int_aden_scroll() {
        return this.int_aden_scroll;
    }

    public int get_int_aden_scroll_time() {
        return this.int_aden_time;
    }

    public void set_int_aden_scroll_time(int i) {
        this.int_aden_time = i;
    }

    public void delete_aden_scroll() {
        if (get_str_aden_scroll()) {
            this.str_aden_scroll = false;
            this.str_aden_time = 0;
            this.pc.removeSkillEffect(60211);
        }
        if (get_dex_aden_scroll()) {
            this.dex_aden_scroll = false;
            this.dex_aden_time = 0;
            this.pc.removeSkillEffect(60212);
        }
        if (get_int_aden_scroll()) {
            this.int_aden_scroll = false;
            this.int_aden_time = 0;
            this.pc.removeSkillEffect(60213);
        }
    }

    public void start_str_aden_scroll() {
        delete_aden_scroll();
        if (!get_str_aden_scroll()) {
            this.str_aden_scroll = true;
            set_str_aden_scroll_time(900);
            this.pc.addHitup(3);
            this.pc.addDmgup(3);
            this.pc.getAbility().addAddedStr(1);
            this.pc.setSkillEffect(60211, (this.str_aden_time * 1000));
            this.pc.sendPackets((ServerBasePacket)new S_OwnCharStatus(this.pc), true);
            this.pc.sendPackets((ServerBasePacket)new S_MPUpdate(this.pc.getCurrentMp(), this.pc.getMaxMp()), true);
            SkillDataController.getInstance().add_int_ice(this.pc);
            this.pc.send_effect(9818);
            SC_SPELL_BUFF_NOTI noti = SC_SPELL_BUFF_NOTI.newInstance();
            noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
            noti.set_spell_id(60210);
            noti.set_duration(this.int_ice_time);
            noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
            noti.set_on_icon_id(4354);
            noti.set_off_icon_id(4354);
            noti.set_tooltip_str_id(1721);
            noti.set_new_str_id(1721);
            noti.set_end_str_id(2854);
            noti.set_is_good(true);
            this.pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
        }
    }
}
