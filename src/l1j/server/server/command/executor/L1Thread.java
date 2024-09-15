package l1j.server.server.server.command.executor;

import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Thread implements L1CommandExecutor {

	private L1Thread() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Thread();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			Thread[] th = new Thread[Thread.activeCount()];
			Thread.enumerate(th);
			for (int i = 0; i < th.length; i++) {
				pc.sendPackets(String.valueOf(new S_SystemMessage("[" + i + "] 使用中的執行緒 : [" + th[i] + "]")));
			}
			pc.sendPackets(String.valueOf(new S_SystemMessage("當前使用中的執行緒數量 : [" + Thread.activeCount() + "]")));

		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " 輸入數量。")));
		}
	}
}
