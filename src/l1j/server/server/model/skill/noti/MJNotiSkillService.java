package l1j.server.server.model.skill.noti;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import l1j.server.MJTemplate.MJProto.MainServer_Client.PartyUISpellInfo;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.timer.L1SkillTimer;
import l1j.server.server.model.skill.timer.MJNotiSkillTimer;

public class MJNotiSkillService {
	private static final MJNotiSkillService service = new MJNotiSkillService();

	public static MJNotiSkillService service() {
		return service;
	}

	private MJNotiSkillService() {
	}

	private Map<Integer, MJNotiSkillModel> models;

	public void newModels(MJNotiSkillDatabaseProvider provider) {
		models = provider.models();
	}

	public MJNotiSkillModel model(int skillId) {
		return models.get(skillId);
	}

	public LinkedList<PartyUISpellInfo> activatedSpells(final L1PcInstance pc) {
		LinkedList<PartyUISpellInfo> activatedSpells = new LinkedList<>();
		for (Entry<Integer, L1SkillTimer> entry : pc.hasSkills()) {
			if (!(entry.getValue() instanceof MJNotiSkillTimer)) {
				continue;
			}
			MJNotiSkillTimer timer = (MJNotiSkillTimer) entry.getValue();
			activatedSpells.add(timer.partySpellInfo());
		}
		return activatedSpells;
	}

	public void newModels(l1j.server.server.model.skill.noti.MJNotiSkillDatabaseProvider entire) {

	}
}
