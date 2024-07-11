package l1j.server.IndunSystem.Hadin;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_ChatPacket;

public class HadinSystem {

	private static HadinSystem _instance;
	private final ArrayList<Integer> _map = new ArrayList<Integer>();
	private final Map<Integer, Hadin> _list = new ConcurrentHashMap<Integer, Hadin>();

	public static HadinSystem getInstance() {
		if (_instance == null) {
			_instance = new HadinSystem();
		}
		return _instance;
	}

	public HadinSystem() {
		_map.add(9000);
	}

	/**
	 * HadinSystem.java可以認為只是管理地圖。
	 * 創建一張地圖並將其告訴實驗室。
	 * 將一方交給 Hardin.java
	 * 運行一個線程來處理聚會使用事件
	 **/
	public void startHadin(L1PcInstance pc) {
		if (countHadin() >= 99) {
			pc.sendPackets(new S_ChatPacket(pc, "進入!!哈丁副本地下城的人數太多了"));
			return;
		}
		int id = blankMapId();
		if (id != 9000)
			L1WorldMap.getInstance().cloneMap(9000, id);
		Hadin ar = new Hadin(id);
		for (L1PcInstance Ppc : pc.getParty().getMembers()) {
			if (Ppc != null)
				// L1Teleport.teleport(Ppc, 32726, 32724, (short) id, Ppc.getHeading(), true);
				Ppc.start_teleport(32726, 32724, id, Ppc.getHeading(), 18339, true, false);
		}
		ar.BasicNpcList = HadinSpawn.getInstance().fillSpawnTable(id, 0, true);
		ar.setParty(pc.getParty());
		_list.put(id, ar);
		ar.Start();
	}

	/**
	 * 取得空地圖ID
	 * 
	 * @return
	 */
	public int blankMapId() {
		if (_list.size() == 0)
			return 9000;
		for (int i = 9000; i <= 9099; i++) {
			Hadin h = _list.get(i);
			if (h == null)
				return i;
		}
		return 9099;
	}

	public Hadin getHadin(int id) {
		return _list.get(id);
	}

	public void removeHadin(int id) {
		_list.remove(id);
	}

	public int countHadin() {
		return _list.size();
	}

}
