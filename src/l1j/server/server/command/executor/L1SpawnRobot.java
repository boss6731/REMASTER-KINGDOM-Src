package l1j.server.server.server.command.executor;

import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1SpawnRobot implements L1CommandExecutor {

	private L1SpawnRobot() {}

	public static L1CommandExecutor getInstance() {
		return new L1SpawnRobot();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			pc.sendPackets(String.valueOf(new S_SystemMessage("空白")));
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [類型 : 0.原地, 1.移動]")));
		}
	}
}