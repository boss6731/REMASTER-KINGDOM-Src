package l1j.server.QueenAntSystem;

import java.util.ArrayList;
import java.util.Iterator;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.types.Point;
import l1j.server.server.utils.L1SpawnUtil;

public class QueenAntController implements Runnable {
	private int _boss_id = 899995;
	private int _boss_mapx = 32896;
	private int _boss_mapy = 32824;
	private int _boss_mapid = 15891;
	private int _mapid = 15881;
	private int _hall_mapid = 15871;
	public static boolean _END = false;
	private int stage = 0;

	public static final int STATUS_START 		= 0;
	public static final int STATUS_1ST_EVENT 	= 1;
	public static final int STATUS_2ND_EVENT 	= 2;
	public static final int STATUS_3RD_EVENT	= 3;
	public static final int STATUS_END 			= 4;
	public static final int STATUS_STOP 		= 5;

	public int _executeStatus = STATUS_START;

	private static boolean Running = false;

	public static int Time = 3600;

	private int remain_time = 0;

	private FastTable<L1NpcInstance> _npc_info;
	private final FastTable<L1EffectInstance> effect_list = new FastTable<L1EffectInstance>();
	L1NpcInstance[] enter_antqueen = new L1NpcInstance[10];
	L1NpcInstance[] circle = new L1NpcInstance[20];
	L1NpcInstance erzabe_egg;
	public void setReady(boolean flag) {
		Running = flag;
	}

	public static boolean isReady() {
		return Running;
	}

	@Override
	public void run() {
		while (Running) {
			try {
							//  _END = true; // 測試用:請註釋
				TimeCheck();
				Poison_cloud_damage();
				switch (stage) {
					case STATUS_START:
						if (remain_time == 0) {
							for (L1PcInstance pc : PcCK(true)) {
								pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "沙塵暴中，蟻后!的藏身處開始顯現。"));
							}
							GeneralThreadPool.getInstance().execute(new PoisonCloudWallThread());
							_npc_info = QueenAntSpawnlistLoader.getInstance().spawnlist(0);
						}
						stage = STATUS_1ST_EVENT;
						// Time = 3010;// 立即召喚埃爾扎貝（測試用：請註釋）
						GeneralThreadPool.getInstance().schedule(this, 1000L);
						return;
					case STATUS_1ST_EVENT:
						if (Time == 3000) {
							for (L1PcInstance pc : PcCK(false)) {
								pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "現在，女王的房間已經打開。"));
								pc.sendPackets("\f3現在，女王的房間已經打開。2分鐘後入口將消失。");
							}
						}
				}
						for (int i = 0; i < 10; i++) {
							MJPoint pt = MJPoint.newInstance(32830, 32830, 100, (short) _mapid, 50);
							enter_antqueen[i] = L1SpawnUtil.spawnnpc(pt.x, pt.y, (short) pt.mapId, 8503182, 0, 0, 0);
						}
						erzabe_egg = L1SpawnUtil.spawnnpc(32897, 32828, (short) _boss_mapid, 8503184, 0, 0, 0);
						
						for (int i = 0; i < 20; i++) {
							MJPoint pt = MJPoint.newInstance(32897, 32828, 10, (short) _boss_mapid, 50);
							circle[i] = L1SpawnUtil.spawnnpc(pt.x, pt.y, (short) pt.mapId, 8503183, 0, 0, 0);
						}
						
						stage = STATUS_2ND_EVENT;
						remain_time = 300;
					}
			GeneralThreadPool.getInstance().schedule(this, 1000L);
			return;
			case STATUS_2ND_EVENT:

				if (remain_time == 300) {
					for (L1PcInstance pc : PcCK(false)) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "勇士啊...請救救我...跟隨光線到我的房間..."));
						pc.sendPackets("勇士啊...請救救我...跟隨光線到我的房間...");
					}
				}

				remain_time--;
				if (remain_time == 240) {
					for (L1PcInstance pc : PcCK(false)) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "只有你能解除束縛我的枷鎖.."));
						pc.sendPackets("只有你能解除束縛我的枷鎖..");
					}
				}

				if (remain_time == 180) {
					for (L1PcInstance pc : PcCK(false)) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "發光的地板磚是救我的鑰匙.. 去那裡..."));
						pc.sendPackets("發光的地板磚是救我的鑰匙.. 去那裡...");
					}
				}
						
						for (int i = 0; i < 10; i++) {
							enter_antqueen[i].deleteMe();
						}
					}

		if (remain_time == 300) {
			for (L1PcInstance pc : PcCK(false)) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "勇士啊...請救救我...跟隨光線到我的房間..."));
				pc.sendPackets("勇士啊...請救救我...跟隨光線到我的房間...");
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "請聚集更多的力量...拜託了..."));
				pc.sendPackets("請聚集更多的力量...拜託了...");
			}
		}
						
						for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
							if (pc.getMapId() == _boss_mapid) {
								if (!pc.isErzabe_circle() && !pc.isDead()) {
									if (pc != null) {
										MJPoint pt = MJPoint.newInstance(32833, 32766, 5, (short) _hall_mapid, 50);
										pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 12261, true);
									}
								} else {
									createNewItem(pc, Config.ServerAdSetting.QueenAnt_Itemid, Config.ServerAdSetting.QueenAnt_Count);
								}
							}
						}

						for (int i = 0; i < 20; i++) {
							circle[i].deleteMe();
							MJPoint pt = MJPoint.newInstance(32897, 32828, 10, (short) _boss_mapid, 50);
							circle[i] = L1SpawnUtil.spawnnpc(pt.x, pt.y, (short) pt.mapId, 8503183, 0, 0, 0);
						}
					}

							remain_time--;
						if (remain_time == 240) {
								for (L1PcInstance pc : PcCK(false)) {
									pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "請聚集更多的力量...拜託了..."));
									pc.sendPackets("請聚集更多的力量...拜託了...");
								}
							}
						
						for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
							if (pc.getMapId() == _boss_mapid) {
								if (!pc.isErzabe_circle() && !pc.isDead()) {
									if (pc != null) {
										MJPoint pt = MJPoint.newInstance(32833, 32766, 5, (short) _hall_mapid, 50);
										pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 12261, true);
									}
								} else {
									createNewItem(pc, Config.ServerAdSetting.QueenAnt_Itemid, Config.ServerAdSetting.QueenAnt_Count);
								}
							}
						}
						
						for (int i = 0; i < 20; i++) {
							circle[i].deleteMe();
							MJPoint pt = MJPoint.newInstance(32897, 32828, 10, (short) _boss_mapid, 50);
							circle[i] = L1SpawnUtil.spawnnpc(pt.x, pt.y, (short) pt.mapId, 8503183, 0, 0, 0);
						}
					}
					
					if (remain_time == 180) {
							for (L1PcInstance pc : PcCK(false)) {
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "請聚集更多的力量...拜託了..."));
							pc.sendPackets("請聚集更多的力量...拜託了...");
							}
							}
						
						for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
							if (pc.getMapId() == _boss_mapid) {
								if (!pc.isErzabe_circle() && !pc.isDead()) {
									if (pc != null) {
										MJPoint pt = MJPoint.newInstance(32833, 32766, 5, (short) _hall_mapid, 50);
										pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 12261, true);
									}
								} else {
									createNewItem(pc, Config.ServerAdSetting.QueenAnt_Itemid, Config.ServerAdSetting.QueenAnt_Count);
								}
							}
						}
						
						for (int i = 0; i < 20; i++) {
							circle[i].deleteMe();
							MJPoint pt = MJPoint.newInstance(32897, 32828, 10, (short) _boss_mapid, 50);
							circle[i] = L1SpawnUtil.spawnnpc(pt.x, pt.y, (short) pt.mapId, 8503183, 0, 0, 0);
						}
					}

						if (remain_time == 120) {
						for (L1PcInstance pc : PcCK(false)) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "請聚集更多的力量...拜託了..."));
						pc.sendPackets("請聚集更多的力量...拜託了...");
						}
						}
						
						for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
							if (pc.getMapId() == _boss_mapid) {
								if (!pc.isErzabe_circle() && !pc.isDead()) {
									if (pc != null) {
										MJPoint pt = MJPoint.newInstance(32833, 32766, 5, (short) _hall_mapid, 50);
										pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 12261, true);
									}
								} else {
									createNewItem(pc, Config.ServerAdSetting.QueenAnt_Itemid, Config.ServerAdSetting.QueenAnt_Count);
								}
							}
						}
						
						for (int i = 0; i < 20; i++) {
							circle[i].deleteMe();
							MJPoint pt = MJPoint.newInstance(32897, 32828, 10, (short) _boss_mapid, 50);
							circle[i] = L1SpawnUtil.spawnnpc(pt.x, pt.y, (short) pt.mapId, 8503183, 0, 0, 0);
						}
					}

						if (remain_time == 60) {
						for (L1PcInstance pc : PcCK(false)) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "啊啊.. 差不多完成了..."));
						pc.sendPackets("啊啊.. 差不多完成了...");
						}
						}
						
						for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
							if (pc.getMapId() == _boss_mapid) {
								if (!pc.isErzabe_circle() && !pc.isDead()) {
									if (pc != null) {
										MJPoint pt = MJPoint.newInstance(32833, 32766, 5, (short) _hall_mapid, 50);
										pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 12261, true);
									}
								} else {
									createNewItem(pc, Config.ServerAdSetting.QueenAnt_Itemid, Config.ServerAdSetting.QueenAnt_Count);
								}
							}
						}
						
						for (int i = 0; i < 20; i++) {
							circle[i].deleteMe();
							MJPoint pt = MJPoint.newInstance(32897, 32828, 10, (short) _boss_mapid, 50);
							circle[i] = L1SpawnUtil.spawnnpc(pt.x, pt.y, (short) pt.mapId, 8503183, 0, 0, 0);
						}
					}

						if (remain_time < 0) {
						for (L1PcInstance pc : PcCK(false)) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "..多虧了你，我被束縛的枷鎖已解開.. 現在..."));
						pc.sendPackets("..多虧了你，我被束縛的枷鎖已解開.. 現在...");
						}
						}

						for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
							if (pc.getMapId() == _boss_mapid) {
								if (!pc.isErzabe_circle() && !pc.isDead()) {
									if (pc != null) {
										MJPoint pt = MJPoint.newInstance(32833, 32766, 5, (short) _hall_mapid, 50);
										pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 12261, true);
									}
								} else {
									createNewItem(pc, Config.ServerAdSetting.QueenAnt_Itemid, Config.ServerAdSetting.QueenAnt_Count);
								}
								pc.sendPackets(S_DisplayEffect.newInstance(2));
								pc.send_effect(19255);
							}
						}
						
						for (int i = 0; i < 20; i++) {
							circle[i].deleteMe();
						}

						for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(erzabe_egg)) {
							erzabe_egg.onPerceive(pc);
							S_DoActionGFX gfx = new S_DoActionGFX(erzabe_egg.getId(), 8);
							pc.sendPackets(gfx);
							erzabe_egg.send_effect(14258);
						}
						
						stage = STATUS_3RD_EVENT;
						remain_time = 0;
					}
					GeneralThreadPool.getInstance().schedule(this, 1000L);
					return;

					case STATUS_3RD_EVENT:
					if (remain_time == 0) {
					for (L1PcInstance pc : PcCK(false)) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "..用餐時間到了..!!"));
					pc.sendPackets("..用餐時間到了..!!");
					}
					spawn(_boss_mapx, _boss_mapy, (short) _boss_mapid, 0, _boss_id, ActionCodes.ACTION_Appear);
					}

					remain_time++;
					if (remain_time >= 10) {
						stage = STATUS_END;
						remain_time = 0;
						erzabe_egg.deleteMe();
					}
					GeneralThreadPool.getInstance().schedule(this, 1000L);
					return;
				case STATUS_END:
					Object_Check();
					GeneralThreadPool.getInstance().schedule(this, 1000L);
					return;
				case STATUS_STOP:
					GeneralThreadPool.getInstance().schedule(this, 1000L);
					return;
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
		if (Running) {
		_END = true;
		for (L1PcInstance pc : PcCK(true)) {
		if (pc.isGm()) {
		pc.sendPackets("\f3[系統通知]: 蟻后藏身處系統正在關閉中。請在1分鐘後在事件報警表中添加+1後重新加載。");
		}
		}
		} else {
		Time = 3600;
		_END = false;
		GeneralThreadPool.getInstance().schedule(this, 1000L);
		reset();
		setReady(true);
		}
		}


		private void TimeCheck() {
		if (Time > 0) {
			Time--;
		}
		if (Time == 0) {
			Running = false;
			_END = true;
		}
		if (_END == true) {
			try {
				for (L1PcInstance pc : PcCK(true)) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "在沙塵暴中，蟻后的藏身處正逐漸消失。"));
				}

				if (_npc_info != null) {
					for (Iterator<L1NpcInstance> i = _npc_info.listIterator(); i.hasNext();) {
						L1NpcInstance npc = i.next();
						if (npc != null) {
							deleteNpc(npc);
						}
					}
					_npc_info.clear();
				}
			}
				
				if (effect_list != null) {
					for (Iterator<L1EffectInstance> i = effect_list.listIterator(); i.hasNext();) {
						L1EffectInstance effect = i.next();
						if (effect != null) {
							deleteNpc(effect);
						}
					}
					effect_list.clear();
				}
				
				for (int i = 0; i < 20; i++) { // 마법진 생성
					if (circle[i] != null) {
						circle[i].deleteMe();
					}
				}
				
				if (erzabe_egg != null)
					erzabe_egg.deleteMe();
				
				for (int i = 0; i < 10; i++) { // 마법진 생성
					if (enter_antqueen[i] != null) {
						enter_antqueen[i].deleteMe();
					}
				}
				
				Thread.sleep(7000);
				MapOut();
				Running = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void Object_Check() {
		L1NpcInstance mob = null;
		for (L1Object object : L1World.getInstance().getVisibleObjects(_boss_mapid).values()) {
			if (object instanceof L1MonsterInstance) {
				mob = (L1NpcInstance) object;
				int npc = mob.getNpcTemplate().get_npcId();
				if (npc == _boss_id) {
					if (mob != null && mob.isDead()) {
						if (remain_time == 0) {
							for (L1PcInstance pc : PcCK(false)) {
								pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "已擊敗蟻后。1分鐘後蟻后的房間將倒塌。"));
								pc.sendPackets("已擊敗蟻后。1分鐘後蟻后的房間將倒塌。");
							}
						}
						if (remain_time == 30) {
							for (L1PcInstance pc : PcCK(false)) {
								pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "請在房間倒塌前完成整備並返回駐地。"));
								pc.sendPackets("請在房間倒塌前完成整備並返回駐地。");
							}
						}
						if (remain_time == 50) {
							for (L1PcInstance pc : PcCK(false)) {
								pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "蟻后的房間正在倒塌。為了安全起見，返回駐地。"));
								pc.sendPackets("蟻后的房間正在倒塌。為了安全起見，返回駐地。");
							}
						}
					}
						if (remain_time > 60) {
							for (L1PcInstance pc : PcCKBoss()) {
								MJPoint pt = MJPoint.newInstance(32833, 32766, 15, (short) 15871, 50);
								pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true);
							}
							stage = STATUS_STOP;
						}
						remain_time++;
					}
				}
			}
		}
	}

	private void reset() {
		L1Object boss = L1World.getInstance().findNpc(_boss_id);
		if (boss != null && boss instanceof L1NpcInstance && !(boss instanceof L1DollInstance)) {
			L1NpcInstance npc = (L1NpcInstance) boss;
			deleteNpc(npc);
		}
	}
	
	public void MapOut() {
		for (L1PcInstance c : L1World.getInstance().getAllPlayers()) {
			if (c.getMapId() == _boss_mapid || c.getMapId() == _mapid || c.getMapId() == _hall_mapid) {
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_WINDAWOOD);
				c.start_teleport(loc[0], loc[1], loc[2], c.getHeading(), 12261, true);
			}
		}
	}

	public ArrayList<L1PcInstance> PcCK(boolean world) {
		ArrayList<L1PcInstance> _pc = new ArrayList<L1PcInstance>();
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (world) {
				_pc.add(pc);
			} else {
				if (pc.getMapId() == _mapid || pc.getMapId() == _boss_mapid)
					_pc.add(pc);
			}
		}
		return _pc;
	}
	
	public ArrayList<L1PcInstance> PcCKBoss() {
		ArrayList<L1PcInstance> _pc = new ArrayList<L1PcInstance>();
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getMapId() == _boss_mapid) {
				_pc.add(pc);
			}
		}
		return _pc;
	}

	private static void spawn(int x, int y, short MapId, int Heading, int npcId, int actioncode) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(MapId);
			int tryCount = 0;
			do {
				tryCount++;
				npc.setX(x);
				npc.setY(y);
				if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
					break;
				}
				Thread.sleep(1);
			} while (tryCount < 50);
			if (tryCount >= 50) {
				npc.getLocation().forward(Heading);
			}
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteNpc(L1NpcInstance npc) {
		npc.getMap().setPassable(npc.getX(), npc.getY(), true);
		npc.deleteMe();
	}
	
	public class PoisonCloudWallThread implements Runnable {
		private int _x_space;
		private int _z_space;

		public PoisonCloudWallThread() {
			_x_space = 8;
			_z_space = 8;
		}

		@Override
		public void run() {
			int[][] cpos = new int[4][2];
			try {
				for (int width = 96; width >= 0; width -= _z_space) {
					int left = 32830 - width;
					int top = 32830 - width;
					int right = 32830 + width;
					int bottom = 32830 + width;
					int magnetic_time = (Config.ServerAdSetting.QueenAnt_magnetic_time * 1000) - 7000;
					int ment_thread_time = _END ? 10 : magnetic_time;
					int ment_thread_time2 = _END ? 10 : 7000;

					for (int i = 0; i < width * 2; i += _x_space) {
						cpos[0][0] = left + i;
						cpos[0][1] = top;

						cpos[1][0] = right;
						cpos[1][1] = top + i + _x_space;

						cpos[2][0] = right - i - _x_space;
						cpos[2][1] = bottom;

						cpos[3][0] = left;
						cpos[3][1] = bottom - i;
						if (width > 15) {
							for (int j = 0; j < 4; j++) {
								int effect_id = 8503180;
								if (j == 0 || j == 2)
									effect_id = 8503181;
								L1EffectInstance effect = L1EffectSpawn.getInstance().spawnEffect(effect_id, ment_thread_time + ment_thread_time2, cpos[j][0], cpos[j][1], (short) _mapid);
								effect_list.add(effect);
							}
						}
					}
					
					for (int i = 0; i < width * 4; i += _x_space) {
						cpos[0][0] = left + i;
						cpos[0][1] = top;

						cpos[1][0] = right;
						cpos[1][1] = top + i;

						cpos[2][0] = right - i;
						cpos[2][1] = bottom + _x_space;

						cpos[3][0] = left - _x_space;
						cpos[3][1] = bottom - i;
						if (width > 15) {
							for (int j = 0; j < 4; j++) {
								L1EffectInstance effect = L1EffectSpawn.getInstance().spawnEffect(8503185, 3600000, cpos[j][0] + 5, cpos[j][1] - 5, (short) _mapid);
								effect_list.add(effect);
							}
						}
					}
					Thread.sleep(ment_thread_time);
					for (L1PcInstance pc : PcCK(false)) {
						if (!_END)
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "蟻后的意志正在發揮作用。請做好應對變化的準備。"));
					}
					Thread.sleep(ment_thread_time2);
					for (L1PcInstance pc : PcCK(false)) {
						if (!_END)
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "觀察到蟻后的意志。警戒狀態已改變。"));
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void Poison_cloud_damage() {
		for (Iterator<L1EffectInstance> i = effect_list.listIterator(); i.hasNext();) {
			L1EffectInstance effect = i.next();
			if (effect.getNpcId() == 8503185) {
				for (L1Object object : L1World.getInstance().getVisibleObjects(_mapid).values()) {
					if (object instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) object;
						if (pc.getLocation().getTileLineDistance(new Point(effect.getLocation())) < 5 && !pc.isDead()) {
							if (pc != null) {
								pc.receiveDamage(effect, Config.ServerAdSetting.QueenAnt_Magnetic_Dmg);//자기장 대미지
								pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage));
								pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage));
							}
						}
					}
				}
			}
		}
	}
	
	private boolean createNewItem(L1PcInstance pc, int item_id, int count) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				pc.sendPackets(new S_ServerMessage(82));
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
			return true;
		} else {
			return false;
		}
	}
	
}
