package l1j.server.server.model.skill.noti;

import java.util.LinkedList;
import java.util.Map;
import l1j.server.MJTemplate.MJProto.MainServer_Client.PartyUISpellInfo;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.timer.L1SkillTimer;
import l1j.server.server.model.skill.timer.MJNotiSkillTimer;

public class MJNotiSkillService {
    private static final MJNotiSkillService service = new MJNotiSkillService();

    private Map<Integer, MJNotiSkillModel> models;

    public static MJNotiSkillService service() {
        return service;
    }

    public void newModels(MJNotiSkillDatabaseProvider provider) {
        this.models = provider.models();
    }

    public MJNotiSkillModel model(int skillId) {
        return this.models.get(Integer.valueOf(skillId));
    }

    public LinkedList<PartyUISpellInfo> activatedSpells(L1PcInstance pc) {
        LinkedList<PartyUISpellInfo> activatedSpells = new LinkedList<>();
        for (Map.Entry<Integer, L1SkillTimer> entry : (Iterable<Map.Entry<Integer, L1SkillTimer>>)pc.hasSkills()) {
            if (!(entry.getValue() instanceof MJNotiSkillTimer))
                continue;
            MJNotiSkillTimer timer = (MJNotiSkillTimer)entry.getValue();
            activatedSpells.add(timer.partySpellInfo());
        }
        return activatedSpells;
    }
}
