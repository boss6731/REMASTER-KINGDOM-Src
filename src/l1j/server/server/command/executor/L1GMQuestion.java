package l1j.server.server.server.command.executor;

import l1j.server.server.model.L1Question;
import l1j.server.server.server.model.Instance.L1PcInstance;

public class L1GMQuestion implements L1CommandExecutor {

	private L1GMQuestion() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1GMQuestion();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			if (L1Question.mainstart) {
				pc.sendPackets("當前正在進行問卷調查。");
				pc.sendPackets("進行中的問卷內容 : " + L1Question.maintext);
			}
			L1Question.getInstance(arg);
		} catch (Exception e) {
			pc.sendPackets(".問卷 [問卷內容] 請輸入");
		}
	}
}
