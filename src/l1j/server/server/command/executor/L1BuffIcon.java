package l1j.server.server.server.command.executor;

import l1j.server.server.model.L1Object;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;
import java.util.logging.Logger;

public abstract class L1BuffIcon implements l1j.server.server.command.executor.L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1BuffIcon.class.getName());

	private L1BuffIcon() {
	}

	public static l1j.server.server.command.executor.L1CommandExecutor getInstance() {
		L1BuffIcon l1BuffIcon;
        l1BuffIcon = new L1BuffIcon() {
			@Override
			public void execute(L1Object pc, String cmdName, String arg) {

			}
		};
        return l1BuffIcon;
	}

	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int _sprid = Integer.parseInt(st.nextToken(), 10);
			int count = Integer.parseInt(st.nextToken(), 10);
			for (int i = 0; i < count; i++) {
				try {					
					Thread.sleep(1000);
					int num = _sprid + i;
					pc.sendPackets(new S_PacketBox(S_PacketBox.BUFFICON, _sprid + i, 10000, true, pc).toString());
					pc.sendPackets(String.valueOf(new S_SystemMessage("無限圖標 : " + num + " 號。")));
				} catch (Exception exception) {
					break;
				}
			}
		} catch (Exception exception) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " 請輸入 [id] [出現數量]。")));
		}
	}
}
