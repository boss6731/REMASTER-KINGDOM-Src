package l1j.server.server.server.command.executor;

import l1j.server.server.server.Controller.HomeTownTimeController;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;

public class L1HomeTown implements L1CommandExecutor {

	private L1HomeTown() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1HomeTown();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String para1 = st.nextToken();
			if (para1.equalsIgnoreCase("每天")) {
				HomeTownTimeController.getInstance().dailyProc();
			} else if (para1.equalsIgnoreCase("每月")) {
				HomeTownTimeController.getInstance().monthlyProc();
				HomeTownTimeController.getInstance(). monthlyProc();
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage("請輸入 .hometown [每天，每月]。")));
		}
	}
}
