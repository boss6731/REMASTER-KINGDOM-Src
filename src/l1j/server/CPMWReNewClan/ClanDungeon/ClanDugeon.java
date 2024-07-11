package l1j.server.CPMWReNewClan.ClanDungeon;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJTemplate.MJJsonUtil;
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

public class ClanDugeon {

	private static ClanDugeon _instance;
	public L1PcInstance _pc;
	private final Map<Integer, L1ClanDugeon> _list = new ConcurrentHashMap<Integer, L1ClanDugeon>();

	public static ClanDugeon getInstance() {
		if (_instance == null) {
			_instance = new ClanDugeon();
		}
		return _instance;
	}

	public ClanDugeonInfo ClanDugeonInfo;

	public class ClanDugeonInfo {
		public boolean livedropitem;
		public int waitingtimes;
		public int waitingtimer;
		public int waitingtimeSR;
		public int minlv;
		public boolean checkitem;
		public int maxuser;
		public int step2per;
		public int step3per;
		public int step4per;
		public int step5per;
		public int hour_Clan;
		public boolean dayplay;

		public ClanDugeonInfo() {
			livedropitem = false;
			waitingtimes = 30;
			waitingtimer = 60;
			waitingtimeSR = 5;
			minlv = 55;
			checkitem = true;
			maxuser = 50;
			step2per = 50;
			step3per = 50;
			step4per = 50;
			step5per = 50;
			hour_Clan = 06;
			dayplay = false;
		}
	}

	public void load_config() {
		try {
			ClanDugeonInfo = MJJsonUtil.fromFile("./config/ClanDugeonInfo.json", ClanDugeonInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error("無法載入 ./config/ClanDugeonInfo.json 檔案。");
		}
	}

	public void DailyClanDungeonStart(int type, L1PcInstance pc) {
		int clonemap = blankMapId(type);
		if (type == 1) {
			if (clonemap != 840)
				L1WorldMap.getInstance().cloneMap(840, clonemap);

			L1ClanDugeon gd = new L1ClanDugeon(clonemap, type, pc);
			_list.put(clonemap, gd);
			gd.Start(type);
		} else if (type == 2) {
			if (clonemap != 890)
				L1WorldMap.getInstance().cloneMap(890, clonemap);

			L1ClanDugeon gd = new L1ClanDugeon(clonemap, type, pc);
			_list.put(clonemap, gd);
			gd.Start(type);
		} else {
			if (clonemap != 751)
				L1WorldMap.getInstance().cloneMap(751, clonemap);

			L1ClanDugeon gd = new L1ClanDugeon(clonemap, type, pc);
			_list.put(clonemap, gd);
			gd.Start(type);
		}
	}

	public int blankMapId(int type) {
		if (type == 1) {
			if (_list.size() == 0)
				return 840;
			for (int i = 840; i <= 889; i++) {
				L1ClanDugeon h = _list.get(i);
				if (h == null)
					return i;
			}
			return 889;
		} else if (type == 2) {
			if (_list.size() == 0)
				return 890;
			for (int i = 890; i <= 939; i++) {
				L1ClanDugeon h = _list.get(i);
				if (h == null)
					return i;
			}
			return 939;
		} else {
			if (_list.size() == 0)
				return 751;
			for (int i = 940; i <= 959; i++) {
				L1ClanDugeon h = _list.get(i);
				if (h == null)
					return i;
			}
			return 959;
		}
	}

	public L1ClanDugeon getClanDugeon(int id) {
		return _list.get(id);
	}

	public void removeClanDugeon(int id) {
		_list.remove(id);
	}

	public int countClanDugeon() {
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

			removeClanDugeon(mapid);
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
