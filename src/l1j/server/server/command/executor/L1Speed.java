package l1j.server.server.server.command.executor;

import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.model.Instance.L1PcInstance;


public class L1Speed implements L1CommandExecutor {
	
	private L1Speed() {	}
	
	public static L1CommandExecutor getInstance() {
		return new L1Speed();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			l1j.server.server.model.skill.L1BuffUtil.haste(pc, 9999 * 1000);
			l1j.server.server.model.skill.L1BuffUtil.brave(pc, 9999 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
			pc.sendPackets(String.valueOf(new l1j.server.server.serverpackets.S_SystemMessage(".速度 命令錯誤")));
		}
	}
}
