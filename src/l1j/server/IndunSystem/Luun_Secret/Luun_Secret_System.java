package l1j.server.IndunSystem.Luun_Secret;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

public class Luun_Secret_System {

	private static Luun_Secret_System _instance;
	private final ArrayList<Integer> _map = new ArrayList<Integer>();
	private final Map<Integer, Luun_Secret> _list = new ConcurrentHashMap<Integer, Luun_Secret>();

	public static Luun_Secret_System getInstance() {
		if (_instance == null) {
			_instance = new Luun_Secret_System();
		}
		return _instance;
	}

	/**
	 * HadinSystem.java可以認為只是管理地圖。
	 * 創建一張地圖並將其告訴實驗室。
	 * 將一方交給 Hardin.java
	 * 運行一個線程來處理聚會使用事件
	 **/
	public void Luun_SecretStart(L1PcInstance pc, int _mapnum) {

		/*
		 * int id = blankMapId();
		 * if (id != 830){
		 * L1WorldMap.getInstance().cloneMap(830, id);
		 * }
		 * 
		 * Luun_Secret ls = new Luun_Secret(id);
		 * _list.put(id, ls);
		 */

		Luun_Secret ls = new Luun_Secret(_mapnum);

		_list.put(_mapnum, ls);
		ls.Start();
	}

	/**
	 * 取得一個空的地圖ID
	 * 
	 * @返回
	 */
	public int blankMapId() {
		if (_list.size() == 0)
			return 815;
		for (int i = 1830; i <= 1930; i++) {
			Luun_Secret h = _list.get(i);
			if (h == null)
				return i;
		}
		return 1930;
	}

	public Luun_Secret getLuun_Secret(int id) {
		return _list.get(id);
	}

	public void removeLuun_Secret(int id) {
		_list.remove(id);
	}

	public int countLuun_Secret() {
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

			removeLuun_Secret(mapid);
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
