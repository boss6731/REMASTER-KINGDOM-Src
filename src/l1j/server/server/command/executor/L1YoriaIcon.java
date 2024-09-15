package l1j.server.server.server.command.executor;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import l1j.server.server.server.command.executor.L1CommandExecutor;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1YoriaIcon implements L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1YoriaIcon.class.getName());

	private L1YoriaIcon() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1YoriaIcon();
	}

	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int _sprid = Integer.parseInt(st.nextToken(), 10);
			int count = Integer.parseInt(st.nextToken(), 10);
			for (int i = 0; i < count; i++) {
				try {
					Thread.sleep(2000);
					int num = _sprid + i;
					pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, _sprid + i, 10)); // 無限制封包
					pc.sendPackets(String.valueOf(new S_SystemMessage("料理圖示 : " + num + " 號。")));
				} catch (Exception exception) {
					break;
				}
			}
		} catch (Exception exception) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " 請輸入 [id] [出現數量]。")));
		}
	}
}