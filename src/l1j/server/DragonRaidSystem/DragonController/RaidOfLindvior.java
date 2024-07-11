package l1j.server.DragonRaidSystem.DragonController;

import java.util.ArrayList;

import l1j.server.DragonRaidSystem.DragonRaidSystemInfo;
import l1j.server.DragonRaidSystem.DragonRaidSystemLoader;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE_NOT;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;

public class RaidOfLindvior implements Runnable {
	DragonRaidSystemInfo dinfo = DragonRaidSystemLoader.find(3);

	private boolean _END = false;

	private int stage = 1;

	private static final int EVENT = 1;
	private static final int END = 2;

	private boolean Running = false;

	private int Time = 3600;

	private int CurrentTime = 0;

	private int Town_Effect = 0;

	private int teleporter_id = 73201223;

	public void setReady(boolean flag) {
		Running = flag;
	}

	public boolean isReady() {
		return Running;
	}

	@Override
	public void run() {
		while (Running) {
			try {
				TimeCheck();
				switch (stage) {
					case EVENT:
						CurrentTime++;
						TownEffectCheck();
						if (CurrentTime == 30) {
							spawn(dinfo.getMapX(), dinfo.getMapY(), (short) dinfo.getMapId(), 0, dinfo.getDragonId(),
									ActionCodes.ACTION_Appear, 3600 * 1000);
							stage = 2;
							CurrentTime = 0;
						}
					case END:
						if (_END == true) {
							CurrentTime++;
							if (CurrentTime > 60)
								Running = false;
							break;
						}
						Object_Check();
						break;
					default:
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		reset();
	}

	public void Start() {
		reset();
		L1World.getInstance().broadcastPacketToAll(SC_NOTIFICATION_MESSAGE_NOT.CenterMessage("我能在某處感受到龍的力量。", 5), true);
		spawn(34266, 32834, (short) 15410, 0, teleporter_id, ActionCodes.ACTION_Appear, 3600 * 1000);
		GeneralThreadPool.getInstance().schedule(this, 1000);
		setReady(true);
	}

	private void TimeCheck() {
		if (Time > 0) {
			Time--;
		}
		if (Time == 0) {
			Running = false;
		}

		if (Time == 300) {
			for (L1PcInstance pc : PcCK()) {
				SC_NOTIFICATION_MESSAGE_NOT.CenterMessage(pc, "突襲5分鐘後結束。", 5);
			}
		}
		if (Time == 180) {
			for (L1PcInstance pc : PcCK()) {
				SC_NOTIFICATION_MESSAGE_NOT.CenterMessage(pc, "突襲3分鐘後結束。", 5);
			}
		}
		if (Time == 60) {
			for (L1PcInstance pc : PcCK()) {
				SC_NOTIFICATION_MESSAGE_NOT.CenterMessage(pc, "突襲1分鐘後結束。", 5);
			}
		}
		if (Time == 10) {
			for (L1PcInstance pc : PcCK()) {
				SC_NOTIFICATION_MESSAGE_NOT.CenterMessage(pc, "突襲將在 10 秒後結束。", 5);
			}
		}
		if (Time == 5) {
			for (L1PcInstance pc : PcCK()) {
				SC_NOTIFICATION_MESSAGE_NOT.CenterMessage(pc, "突襲將在 5 秒後結束。", 5);
			}
		}

		L1Object teleporter = L1World.getInstance().findNpc(teleporter_id);
		if (teleporter == null) {
			Running = false;
			MapOut();
		}
	}

	private void TownEffectCheck() {
		if (Town_Effect == 0)
			spawn(33925, 33350, (short) 4, 0, 73201231, ActionCodes.ACTION_Appear, 6000);
		if (Town_Effect == 10)
			spawn(33927, 33353, (short) 4, 0, 73201231, ActionCodes.ACTION_Appear, 6000);
		if (Town_Effect == 20)
			spawn(33932, 33349, (short) 4, 0, 73201231, ActionCodes.ACTION_Appear, 6000);
		if (Town_Effect == 30)
			spawn(33933, 33338, (short) 4, 0, 73201231, ActionCodes.ACTION_Appear, 6000);
		if (Town_Effect == 40) {
			spawn(33920, 33353, (short) 4, 0, 73201231, ActionCodes.ACTION_Appear, 6000);
			Town_Effect = 0;
		}
		Town_Effect++;
	}

	private void Object_Check() {
		L1NpcInstance mob = null;
		for (L1Object object : L1World.getInstance().getVisibleObjects(dinfo.getMapId()).values()) {
			if (object instanceof L1MonsterInstance) {
				mob = (L1NpcInstance) object;
				int npc = mob.getNpcTemplate().get_npcId();
				if (npc == dinfo.getDragonId()) {
					if (mob != null && mob.isDead()) {
						_END = true;
					}
				}
			}
		}
	}

	private void reset() {
		L1Object boss = L1World.getInstance().findNpc(dinfo.getDragonId());
		if (boss != null && boss instanceof L1NpcInstance && !(boss instanceof L1DollInstance)) {
			L1NpcInstance npc = (L1NpcInstance) boss;
			deleteNpc(npc);
		}

		L1Object teleporter = L1World.getInstance().findNpc(teleporter_id);
		if (teleporter != null && teleporter instanceof L1NpcInstance && !(teleporter instanceof L1DollInstance)) {
			L1NpcInstance npc = (L1NpcInstance) teleporter;
			deleteNpc(npc);
		}

		MapOut();
	}

	public ArrayList<L1PcInstance> PcCK() {
		ArrayList<L1PcInstance> _pc = new ArrayList<L1PcInstance>();
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getMapId() == dinfo.getMapId())
				_pc.add(pc);
		}
		return _pc;
	}

	private static void spawn(int x, int y, short MapId, int Heading, int npcId, int actioncode,
			int timeMillisToDelete) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(MapId);
			npc.setX(x);
			npc.setY(y);
			npc.getLocation().forward(Heading);
			npc.setHomeX(x);
			npc.setHomeY(y);
			npc.setHeading(Heading);

			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc)) {
				npc.onPerceive(pc);
				S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), actioncode);
				pc.sendPackets(gfx);
			}

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);

			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteNpc(L1NpcInstance npc) {
		npc.getMap().setPassable(npc.getX(), npc.getY(), true);
		npc.deleteMe();
	}

	public void MapOut() {
		for (L1PcInstance pc : PcCK()) {
			int[] loc = Getback.GetBack_Location(pc, false);
			pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 18339, true);
		}
	}
}
