package l1j.server.IndunSystem.Whale_Room;

import java.util.ArrayList;
import java.util.Random;

import javolution.util.FastTable;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.types.Point;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 魯雲城秘密地下城
 * 
 * @作者薩滿
 *
 */
public class WhaleTreasureRoom implements Runnable {

	private Random rnd = new Random(System.currentTimeMillis());
	private final ArrayList<L1PcInstance> playmember = new ArrayList<L1PcInstance>();
	private ArrayList<L1NpcInstance> MonsterList;

	private int _mapnum = 0;
	private boolean start = false;
	private int time = 0;
	private boolean timer = false;
	private int gametime = 20;

	private int step = 0;
	private int sub_step = 0;
	private int sub_step1 = 0;
	private int sub_step2 = 0;
	private int sub_step3 = 0;
	private int sub_step4 = 0;
	private int sub_step5 = 0;
	private int sub_step6 = 0;
	private int sub_step7 = 0;
	private int sub_step8 = 0;
	private int sub_step9 = 0;
	private int sub_step10 = 0;

	private L1PcInstance pc;

	L1DoorInstance[] _box = new L1DoorInstance[10];

	private boolean _close = false;

	public WhaleTreasureRoom(int id, L1PcInstance pc) {
		_mapnum = (short) id;
		this.pc = pc;
	}

	public void Start() {
		GeneralThreadPool.getInstance().execute(this);
	}

	public WhaleTreasureRoom(int mapid) {
		_mapnum = mapid;
		spawnBox();
		MonsterList = new ArrayList<L1NpcInstance>();
		GeneralThreadPool.getInstance().schedule(new timer(), 1000);
	}

	class timer implements Runnable {
		boolean playercheck = false;

		@Override
		public void run() {
			try {
				if (_close) {
					return;
				}
				checkMember();
				end_check();
				time--;
				if (!playercheck) {
					step = 0;
					playercheck = true;
				}
				if (!timer) {
					timer = true;
				}
				GeneralThreadPool.getInstance().schedule(this, 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		try {
			if (_close) {
				return;
			}
			switch (step) {
				case 0: // 一樓起點。
					if (sub_step == 0) {
						if (getPlayMembersCount() == 0) {
							GeneralThreadPool.getInstance().schedule(this, 1 * 1000);
							return;
						}
						time = gametime;
						sub_step = 1;
					} else if (sub_step == 1) {
						if (time <= 0) {
							step = 1;
						}
					}
					GeneralThreadPool.getInstance().schedule(this, 1 * 1000);
					return;

				case 1:
					quit();
					GeneralThreadPool.getInstance().schedule(this, 1 * 1000);
					return;
			}
		} catch (Exception e) {
		}
		GeneralThreadPool.getInstance().schedule(this, 1000);
	}

	public void addPlayMember(L1PcInstance pc) {
		playmember.add(pc);
	}

	public int getPlayMembersCount() {
		return playmember.size();
	}

	public void removePlayMember(L1PcInstance pc) {
		playmember.remove(pc);
	}

	public void clearPlayMember() {
		playmember.clear();
	}

	public boolean isPlayMember(L1PcInstance pc) {
		return playmember.contains(pc);
	}

	public L1PcInstance[] getPlayMemberArray() {
		return (L1PcInstance[]) playmember.toArray(new L1PcInstance[getPlayMembersCount()]);
	}

	private void checkMember() {
		for (L1Object obj : L1World.getInstance().getVisibleObjects(_mapnum).values()) {
			if (obj instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) obj;
				if (!isPlayMember(pc)) {
					addPlayMember(pc);
				}
			}
		}
		if (getPlayMembersCount() > 0) {
			for (L1PcInstance pc : getPlayMemberArray()) {
				if (pc.getMapId() != _mapnum) {
					removePlayMember(pc);
				}
			}
		}
	}

	private void end_check() {
		if (start && getPlayMembersCount() <= 0 || (start && time > gametime)) {
			quit();
		}
	}

	private void spawnBox() {

		for (int i = 0; i < 4; i++) {
			_box[i] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32792, 32795 + (i * 3), (short) _mapnum, 7800245, 0, 0, 0); // 小盒子
			_box[i + 4] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32805, 32795 + (i * 3), (short) _mapnum, 7800245, 0, 0,
					0); // 小盒子
		}
		_box[8] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32801, 32795, (short) _mapnum, 7800245, 0, 0, 0); // 小盒子
		_box[9] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32799, 32804, (short) _mapnum, 7800246, 0, 0, 0); // 大盒子

	}

	private void quit() {
		HOME_TELEPORT();
		Object_Delete();
		WhaleTreasureRoomSystem.getInstance().Reset(_mapnum);
		_close = true;
	}

	private void HOME_TELEPORT() {
		for (L1PcInstance pc : getPlayMemberArray()) {
			try {
				if (pc == null)
					continue;
				pc.start_teleport(32736 + rnd.nextInt(12), 32987 + rnd.nextInt(14), 63, 5, 18339, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void Object_Delete() {
		for (L1Object ob : L1World.getInstance().getVisibleObjects(_mapnum).values()) {
			if (ob == null || ob instanceof L1DollInstance
					|| ob instanceof L1SummonInstance
					|| ob instanceof L1PetInstance)
				continue;
			if (ob instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) ob;
				npc.deleteMe();
			}
		}
		for (L1ItemInstance obj : L1World.getInstance().getAllItem()) {
			if (obj.getMapId() != _mapnum)
				continue;
			L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
			groundInventory.removeItem(obj);
		}
	}
}
