package l1j.server.IndunSystem.Whale_Room;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.datatables.MapsTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_ChatPacket;

public class WhaleTreasureRoomSystem {

	private static WhaleTreasureRoomSystem _instance;
	private final ArrayList<Integer> _map = new ArrayList<Integer>();
	private final Map<Integer, WhaleTreasureRoom> _list = new ConcurrentHashMap<Integer, WhaleTreasureRoom>();

	public static WhaleTreasureRoomSystem getInstance() {
		if (_instance == null) {
			_instance = new WhaleTreasureRoomSystem();
		}
		return _instance;
	}

	/**
	 * HadinSystem.java可以認為只是管理地圖。
	 * 創建一張地圖並將其告訴實驗室。
	 * 將一方交給 Hardin.java
	 * 運行一個線程來處理聚會使用事件
	 **/
	public void WhaleTreasureRoomStart(L1PcInstance pc, int _mapnum) {

		/*
		 * int id = blankMapId();
		 * if (id != 830){
		 * L1WorldMap.getInstance().cloneMap(830, id);
		 * }
		 * 
		 * Luun_Secret ls = new Luun_Secret(id);
		 * _list.put(id, ls);
		 */
		int clonemap = blankMapId();
		if (_mapnum != 1650) {
			L1WorldMap.getInstance().cloneMap(1650, _mapnum);
		}
		WhaleTreasureRoom wt = new WhaleTreasureRoom(_mapnum);

		_list.put(_mapnum, wt);
		wt.Start();
	}

	/**
	 * 取得一個空的地圖ID
	 * 
	 * @返回
	 */
	public int blankMapId() {
		if (_list.size() == 0)
			return 1650;
		for (int i = 14401; i <= 14500; i++) {
			if (MapsTable.getInstance().getMap(i) != null) {
				continue;
			}
			WhaleTreasureRoom h = _list.get(i);
			if (h == null)
				return i;
		}
		return 112001;
	}

	public WhaleTreasureRoom getWhaleTreasureRoom(int id) {
		return _list.get(id);
	}

	public void removeWhaleTreasureRoom(int id) {
		_list.remove(id);
	}

	public int countWhaleTreasureRoom() {
		return _list.size();
	}

	public void Reset(int mapid) {
		try {
			for (L1NpcInstance mon : MonList) {
				if (mon == null || mon._destroyed || mon.isDead()) {
					continue;
				}
				mon.deleteMe();
			}
			Object_Delete(mapid);
			if (MonList.size() > 0)
				MonList.clear();

			removeWhaleTreasureRoom(mapid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ArrayList<L1NpcInstance> MonList = new ArrayList<L1NpcInstance>();

	public void AddMon(L1NpcInstance npc) {
		MonList.add(npc);
	}

	private void Object_Delete(int mapid) {
		for (L1Object ob : L1World.getInstance().getVisibleObjects(mapid)
				.values()) {
			if (ob == null || ob instanceof L1DollInstance
					|| ob instanceof L1SummonInstance
					|| ob instanceof L1PetInstance)
				continue;
			if (ob instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) ob;
				if (npc._destroyed || npc.isDead())
					continue;
				npc.deleteMe();
			}
		}
		for (L1ItemInstance obj : L1World.getInstance().getAllItem()) {
			if (obj.getMapId() != mapid)
				continue;
			L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
			groundInventory.removeItem(obj);
		}
	}

}
