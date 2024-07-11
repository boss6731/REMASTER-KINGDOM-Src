package l1j.server.IndunEx.PlayInfo;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.IndunEx.CrocodileInDungeonEx;
import l1j.server.IndunEx.GludioInDungeonEx;
import l1j.server.IndunEx.RoomInfo.MJIndunRoomModel;
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

public class CrocodileInDungeon {

	private static CrocodileInDungeon _instance;
	public L1PcInstance _pc;
	private final Map<Integer, CrocodileInDungeonEx> _list = new ConcurrentHashMap<Integer, CrocodileInDungeonEx>();

	public static CrocodileInDungeon getInstance() {
		if (_instance == null) {
			_instance = new CrocodileInDungeon();
		}
		return _instance;
	}

	public void CrocodileInstanceDungeonStart(int price, MJIndunRoomModel model) {
		int clonemap = blankMapId();
		if (clonemap != 732){
			L1WorldMap.getInstance().cloneMap(732, clonemap);
		}
		
		CrocodileInDungeonEx cd = new CrocodileInDungeonEx(clonemap, price, model);
		
		_list.put(clonemap, cd);
		cd.Start();
	}

	public int blankMapId(){
		if(_list.size() == 0)
			return 732;
		for(int i = 14101 ; i <= 14200; i++){
			if (MapsTable.getInstance().getMap(i) != null) {
				continue;
			}
			CrocodileInDungeonEx h = _list.get(i);
			if(h == null)
				return i;
		}  
		return 776;
	}
	
	public CrocodileInDungeonEx getCrocodile(int id){
		return _list.get(id);
	}

	public void removeCrocodile(int id){
		_list.remove(id);
	}

	public int countCrocodile(){
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
			
			removeCrocodile(mapid);
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
