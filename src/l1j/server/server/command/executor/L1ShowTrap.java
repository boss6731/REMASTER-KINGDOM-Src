package l1j.server.server.command.executor;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TrapInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1ShowTrap implements L1CommandExecutor {

    private L1ShowTrap() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ShowTrap();
    }

    @override
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        if (arg.equalsIgnoreCase("開")) {
            pc.setSkillEffect(L1SkillId.GMSTATUS_SHOWTRAPS, 0);
        } else if (arg.equalsIgnoreCase("關")) {
            pc.removeSkillEffect(L1SkillId.GMSTATUS_SHOWTRAPS);

            for (L1Object obj : pc.getKnownObjects()) {
                if (obj instanceof L1TrapInstance) {
                    pc.removeKnownObject(obj);
                    pc.sendPackets(new S_RemoveObject(obj));
                }
            }
        } else {
            pc.sendPackets(new S_SystemMessage(cmdName + " [開,關] 請輸入。 "));
        }
    }
}
