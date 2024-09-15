package l1j.server.server.server.command.executor;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;

public class L1Chat implements l1j.server.server.command.executor.L1CommandExecutor {

	private L1Chat() { }

	public static l1j.server.server.command.executor.L1CommandExecutor getInstance() {
		return new L1Chat();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			if (st.hasMoreTokens()) {
				String flag = st.nextToken();
				String msg;
				if (flag.compareToIgnoreCase("開") == 0) {
					L1World.getInstance().set_worldChatEnabled(true);
					msg = "世界聊天已啓用。";
				} else if (flag.compareToIgnoreCase("關") == 0) {
					L1World.getInstance().set_worldChatEnabled(false);
					msg = "世界聊天已關閉。";
				} else {
					throw new Exception();
				}
				pc.sendPackets(String.valueOf(new S_SystemMessage(msg)));
			} else {
				String msg;
				if (L1World.getInstance(). isWorldChatElabled()) {
					msg = "目前世界聊天是有效的.. 可以用 聊天 關 來停止。";
				} else {
					msg = "目前世界聊天已經停止.. 可以用 聊天 開 來啟用。";
				}
				pc.sendPackets(String.valueOf(new S_SystemMessage(msg)));
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [開, 關]")));
		}
	}

	@Override
	public void execute(L1Object pc, String cmdName, String arg) {

	}
}
