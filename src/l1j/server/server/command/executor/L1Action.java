package l1j.server.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Action implements L1CommandExecutor {

	private L1Action() {  }

	public static l1j.server.server.command.executor.L1CommandExecutor getInstance() {
		return new L1Action();
	}

	@Override
	public void execute(L1Object pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int actId = Integer.parseInt(st.nextToken(), 10);
			pc.sendPackets(String.valueOf(new S_DoActionGFX(pc.getId(), actId)));
		} catch (Exception exception) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [actid] 請這樣輸入。")));
		}
	}
}
