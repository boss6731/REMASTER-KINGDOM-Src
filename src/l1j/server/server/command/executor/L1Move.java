package l1j.server.server.server.command.executor;

import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;

public class L1Move implements L1CommandExecutor {

	private L1Move() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Move();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			int locx = Integer.parseInt(st.nextToken());
			int locy = Integer.parseInt(st.nextToken());
			int mapid;
			if (st.hasMoreTokens()) {
				mapid = Integer.parseInt(st.nextToken());
			} else {
				mapid = pc.getMapId();
			}
			pc.do_simple_teleport(locx, locy, mapid);
			pc.sendPackets(String.valueOf(new S_SystemMessage("座標 " + locx + ", " + locy + ", " + mapid + " 移動成功。")));
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [X座標] [Y座標] [地圖ID] 請這樣輸入。")));
		}
	}
}
