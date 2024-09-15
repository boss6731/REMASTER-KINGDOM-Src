package l1j.server.server.server.command.executor;

import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1ResetTrap implements L1CommandExecutor {

	private L1ResetTrap() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1ResetTrap();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		L1WorldTraps.getInstance().resetAllTraps();
		pc.sendPackets(String.valueOf(new S_SystemMessage("陷阱已重新佈置。")));
	}
}
