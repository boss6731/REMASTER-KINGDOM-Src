package l1j.server.server.model.map;

import java.util.Map;

import MJFX.UIAdapter.MJUIAdapter;
import l1j.server.MapReader;

public class L1WorldMap {

	private static L1WorldMap _instance;
	private Map<Integer, L1Map> _maps;

	public static L1WorldMap getInstance() {
		if (_instance == null) {
			_instance = new L1WorldMap();
		}
		return _instance;
	}

	private L1WorldMap() {
		MapReader in = MapReader.getDefaultReader();

		try {
			_maps = in.read();
			if (_maps == null) {
				throw new RuntimeException("MAP讀取失敗");
			}
		} catch (Exception e) {
			e.printStackTrace();
			MJUIAdapter.on_exit();
		}
	}

	public L1Map getMap(short mapId) {
		L1Map map = _maps.get((int) mapId);
		if (map == null) {
			map = L1Map.newNull();
		}
		return map;
	}

	public boolean getMapCK(short mapId) {
		L1Map map = _maps.get((int) mapId);
		return map != null;
	}


	public void cloneMap(int targetId, int newId){//레이드
		L1Map copymap = null;
		copymap = _maps.get(targetId).copyMap(newId);
		_maps.put(newId, copymap);
	}

	public L1Map cloneMapAndGet(int targetId, int newId){
		L1Map copymap = null;
		copymap = _maps.get(targetId).copyMap(newId);
		_maps.put(newId, copymap);
		return copymap;
	}

	// 오림 인던 관련
	public synchronized void addMap(L1Map map) {
		_maps.put(map.getId(), map);
	}

	public synchronized void removeMap(int mapId) {
		_maps.remove(mapId);
	}
}
