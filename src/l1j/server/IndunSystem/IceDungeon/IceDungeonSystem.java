package l1j.server.IndunSystem.IceDungeon;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_ChatPacket;

public class IceDungeonSystem {

	private static IceDungeonSystem _instance;
	private final ArrayList<Integer> _map = new ArrayList<Integer>();
	private final Map<Integer, IceDungeon> _list = new ConcurrentHashMap<Integer, IceDungeon>();

	public static IceDungeonSystem getInstance() {
		if (_instance == null) {
			_instance = new IceDungeonSystem();
		}
		return _instance;
	}

	public IceDungeonSystem() {
		_map.add(2101);
	}

	public void startDungeon(L1PcInstance pc) {
		if (countDungeon() >= 49) {
			pc.sendPackets(new S_ChatPacket(pc, "進入!!冰之女皇副本的人太多了。"));
			return;
		}
		int id = blankMapId();
		if (id != 2101)
			L1WorldMap.getInstance().cloneMap(2101, id);
		IceDungeon dungeon = new IceDungeon(id);
		pc.start_teleport(32728, 32811, id, 5, 18339, true, true);// 原32728 32819
		_list.put(id, dungeon);
		dungeon.Start();
	}

	public int blankMapId() {
		if (_list.size() == 0)
			return 2101;
		for (int i = 2101; i <= 2200; i++) {
			IceDungeon h = _list.get(i);
			if (h == null)
				return i;
		}
		return 2200;
	}

	public IceDungeon getDungeon(int id) {
		return _list.get(id);
	}

	public void removeDungeon(int id) {
		_list.remove(id);
	}

	public int countDungeon() {
		return _list.size();
	}
}
