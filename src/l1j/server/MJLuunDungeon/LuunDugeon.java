package l1j.server.MJLuunDungeon;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;

public class LuunDugeon {

	private static LuunDugeon _instance;
	public L1PcInstance _pc;
	private final Map<Integer, L1LuunDugeon> _list = new ConcurrentHashMap<Integer, L1LuunDugeon>();
	public boolean isactive = false;

	public static LuunDugeon getInstance() {
		if (_instance == null) {
			_instance = new LuunDugeon();
		}
		return _instance;
	}

	public void LuunDungeonStart() {
		L1LuunDugeon gd = new L1LuunDugeon();
		_list.put(4001, gd);
		_list.put(4002, gd);
		isactive = true;
		gd.Start();
	}
	
	public L1LuunDugeon getLuunDugeon(int id){
		return _list.get(id);
	}

	public void removeLuunDugeon(int id){
		_list.remove(id);
	}

	public int countLuunDugeon(){
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
			
			removeLuunDugeon(mapid);
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
					|| ob instanceof L1PetInstance
					|| ob instanceof L1DoorInstance)
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
