package l1j.server.server.server.command.executor;

import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

import java.util.StringTokenizer;

public class L1Poly implements L1CommandExecutor {

	private L1Poly() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Poly();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String name = st.nextToken();
			int polyid = Integer.parseInt(st.nextToken());

			L1PcInstance tg = L1World.getInstance().getPlayer(name);

			if (tg == null) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(73, name))); // 1%0不在遊戲中。
			} else {
				try {
					L1PolyMorph.doPoly(tg, polyid, 604800, L1PolyMorph.MORPH_BY_GM, false, false);
				} catch (Exception exception) {
					pc.sendPackets(String.valueOf(new S_SystemMessage(".變身 [角色名稱] [圖形ID] 請輸入。")));
				}
			}
		} catch (Exception e) {
			pc.sendPackets(String.valueOf(new S_SystemMessage(cmdName + " [角色名稱] [圖形ID] 請輸入。")));
		}
	}
}
