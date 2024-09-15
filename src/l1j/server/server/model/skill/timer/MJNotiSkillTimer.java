package l1j.server.server.model.skill.timer;

import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJProto.MainServer_Client.PartyUISpellInfo;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.Instance.L1PcInstance;
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
		this.remainingSeconds = (int)(timeMillis / 1000);
		this.stop = false;
	}
	
	private int selectedSpellIconId() {
		if(character != null && model.connectedPassiveId() != -1) {
			MJPassiveID passiveId = MJPassiveID.fromInt(model.connectedPassiveId());
			if(passiveId != null && character.isPassive(passiveId.toInt()) && model.connectedPassvieIconId() != 0) {
				return model.connectedPassvieIconId();
			}
		}
		return model.iconId();
	}
	
	public PartyUISpellInfo partySpellInfo(){
		PartyUISpellInfo pInfo = PartyUISpellInfo.newInstance();
		pInfo.set_spell_id(model.skillId() - 1);
		pInfo.set_bufficon_id(selectedSpellIconId());
		pInfo.set_tooltip_id(model.tooltipStrId());
		pInfo.set_is_good(model.isGood());
		pInfo.set_duration((int)remainingSeconds);
		return pInfo;
	}
	
	protected void refreshPartyMemberStatus() {
		if (character == null)
			return;
		L1Party party = character.getParty();
		if (party != null) {
			party.refreshPartyMemberStatus(character);
		}
	}
	
	@Override
	public L1Character owner(){
		return character;
	}

	@Override
	public int skillId(){
		return model.skillId();
	}

	@Override
	public long timeMillis(){
		return timeMillis; 
	}
	
	@Override
	public boolean stopped(){
		return stop;
	}

	@Override
	public int remainingSeconds() {
		return remainingSeconds;
	}

	@Override
	public void remainingSeconds(int remainingSeconds) {
		this.remainingSeconds = remainingSeconds;
	}
	
	@Override
	public void begin() {
		GeneralThreadPool.getInstance().schedule(this, 1000);
		refreshPartyMemberStatus();
	}
	
	@Override
	public void end() {
		stop = true;
		refreshPartyMemberStatus();
		MJSkillStopper.stopSkill(character, skillId());
		character = null;
	}

	@Override
	public void kill() {
		stop = true;
		refreshPartyMemberStatus();
		character = null;
	}
}
