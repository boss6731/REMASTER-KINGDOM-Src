package l1j.server.server.model.skill.timer;

import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJProto.MainServer_Client.PartyUISpellInfo;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.skill.noti.MJNotiSkillModel;

public class MJNotiSkillTimer extends L1SkillTimer {
    private L1PcInstance character;

    private final long timeMillis;

    private int remainingSeconds;

    private MJNotiSkillModel model;

    private boolean stop;

    MJNotiSkillTimer(L1PcInstance character, MJNotiSkillModel model, long timeMillis) {
        this.character = character;
        this.model = model;
        this.timeMillis = timeMillis;
        this.remainingSeconds = (int)(timeMillis / 1000L);
        this.stop = false;
    }

    private int selectedSpellIconId() {
        if (this.character != null && this.model.connectedPassiveId() != -1) {
            MJPassiveID passiveId = MJPassiveID.fromInt(this.model.connectedPassiveId());
            if (passiveId != null && this.character.isPassive(passiveId.toInt()) && this.model.connectedPassvieIconId() != 0)
                return this.model.connectedPassvieIconId();
        }
        return this.model.iconId();
    }

    public PartyUISpellInfo partySpellInfo() {
        PartyUISpellInfo pInfo = PartyUISpellInfo.newInstance();
        pInfo.set_spell_id(this.model.skillId() - 1);
        pInfo.set_bufficon_id(selectedSpellIconId());
        pInfo.set_tooltip_id(this.model.tooltipStrId());
        pInfo.set_is_good(this.model.isGood());
        pInfo.set_duration(this.remainingSeconds);
        return pInfo;
    }

    protected void refreshPartyMemberStatus() {
        if (this.character == null)
            return;
        L1Party party = this.character.getParty();
        if (party != null)
            party.refreshPartyMemberStatus(this.character);
    }

    public L1Character owner() {
        return (L1Character)this.character;
    }

    public int skillId() {
        return this.model.skillId();
    }

    public long timeMillis() {
        return this.timeMillis;
    }

    public boolean stopped() {
        return this.stop;
    }

    public int remainingSeconds() {
        return this.remainingSeconds;
    }

    public void remainingSeconds(int remainingSeconds) {
        this.remainingSeconds = remainingSeconds;
    }

    public void begin() {
        GeneralThreadPool.getInstance().schedule(this, 1000L);
        refreshPartyMemberStatus();
    }

    public void end() {
        this.stop = true;
        refreshPartyMemberStatus();
        MJSkillStopper.stopSkill((L1Character)this.character, skillId());
        this.character = null;
    }

    public void kill() {
        this.stop = true;
        refreshPartyMemberStatus();
        this.character = null;
    }
}
