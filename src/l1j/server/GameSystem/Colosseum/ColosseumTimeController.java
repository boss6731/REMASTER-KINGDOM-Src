package l1j.server.GameSystem.Colosseum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
import l1j.server.server.model.L1World;

public class ColosseumTimeController implements Runnable {
	public static final int SLEEP_TIME = 15000;

	private static ColosseumTimeController _instance;
	private final Map<Integer, L1Colosseum> _list = new ConcurrentHashMap<Integer, L1Colosseum>();

	public static ColosseumTimeController getInstance() {
		if (_instance == null) {
			_instance = new ColosseumTimeController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			checkUbTime(); // 檢查UB開始時間
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void checkUbTime() {
		for (int i = 1; i <= 1; i++) {
			L1Colosseum ub = ColosseumTable.getInstance().getUb(i);
			if (ub.checkUbTime() && !ub.isActive()) {
				if (L1Colosseum.Info.colouse) {
					L1Colosseum.Info.colouse = false;
					L1World.getInstance().broadcastPacketToAll(
							SC_NOTIFICATION_MESSAGE.make_stream("\\f3稍後奇岩村將開始競技場活動.", MJSimpleRgb.green(), 1), true);
					L1World.getInstance().broadcastServerMessage("\\f3稍後奇岩村將開始競技場活動.");
				}
				_list.put(i, ub);
				ub.start();
			}
		}
	}

	public int checkUbId() {
		if (_list.size() == 0)
			return 1;
		for (int i = 1; i <= 5; i++) {
			L1Colosseum h = _list.get(i);
			if (h == null)
				return i;
		}
		return 5;
	}
}
