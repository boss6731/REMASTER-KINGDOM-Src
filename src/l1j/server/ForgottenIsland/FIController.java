package l1j.server.ForgottenIsland;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.MapsTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.gametime.TimeListener;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.types.Point;

public class FIController implements TimeListener {
	private static FIController _instance;

	public static FIController getInstance() {
		if (_instance == null)
			_instance = new FIController();
		return _instance;
	}

	public void reload() {
		dispose();
		if (_instance != null) {
			_instance = new FIController();
		}
	}

	private static boolean Running = false;

	public void setReady(boolean flag) {
		Running = flag;
	}

	public static boolean isReady() {
		return Running;
	}

	public void Start() {
		setReady(true);
		run();
	}

	public void End() {
		setReady(false);
		dispose();
	}

	public void run() {
		RealTimeClock.getInstance().addListener(this, Calendar.SECOND);
	}

	public void dispose() {
		if (effect_list != null) {
			for (L1EffectInstance effect : effect_list) {
				effect.deleteMe();
			}
			effect_list.clear();
		}

		RealTimeClock.getInstance().removeListener(this, Calendar.SECOND);
	}

	private HashMap<Integer, FICloudSpawnInfo> _cloud_spawn;
	private HashMap<Integer, FINightSpawnInfo> _night_spawnlist;

	private FIController() {
		CloudSpawnLoad();
		Night_Spawn_List();
	}

	private void CloudSpawnLoad() {
		HashMap<Integer, FICloudSpawnInfo> infos = new HashMap<Integer, FICloudSpawnInfo>();
		Selector.exec("select * from fi_cloud_spawn", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					try {
						FICloudSpawnInfo Info = FICloudSpawnInfo.newInstance(rs);
						infos.put(Info.get_id(), Info);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		_cloud_spawn = infos;
	}

	private void Night_Spawn_List() {
		HashMap<Integer, FINightSpawnInfo> infos = new HashMap<Integer, FINightSpawnInfo>();
		Selector.exec("select * from fi_night_spawn", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					try {
						FINightSpawnInfo Info = FINightSpawnInfo.newInstance(rs);
						infos.put(Info.get_spawn_time(), Info);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		_night_spawnlist = infos;
	}

	private int _spawn_time = -1;

	public void NightSpawn() {
		int servertime = GameTimeClock.getInstance().getGameTime().getSeconds();
		int nowtime = ((servertime % 86400) / 10) / 360;
		L1NpcInstance npc = null;
		L1Npc l1npc = null;

		if (nowtime == _spawn_time)
			return;

		if (nowtime != _spawn_time)
			_spawn_time = -1;

		FINightSpawnInfo Info = _night_spawnlist.get(nowtime);
		if (Info == null)
			return;

		_spawn_time = nowtime;

		if (Info.get_type() == 1) {
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayers(Info.get_map_id())) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "感覺到某處有陰森的氣息."));

				if (pc.isDead())
					continue;

				String[] npcids = (String[]) MJArrangeParser
						.parsing(Info.get_npc_id(), ",", MJArrangeParseeFactory.createStringArrange()).result();
				String[] count = (String[]) MJArrangeParser
						.parsing(Info.get_count(), ",", MJArrangeParseeFactory.createStringArrange()).result();
				int size = 0;
				for (String ids : npcids) {
					int npcid = Integer.parseInt(ids);
					int npc_count = Integer.parseInt(count[size++]);
					for (int i = 0; i < npc_count; i++) {
						l1npc = NpcTable.getInstance().getTemplate(npcid);
						if (l1npc != null) {
							try {
								int mapid = Info.get_map_id();
								int x = pc.getX();
								int y = pc.getY();

								npc = NpcTable.getInstance().newNpcInstance(npcid);
								npc.setId(IdFactory.getInstance().nextId());
								MJPoint pt = MJPoint.newInstance(x, y, Info.get_range(), (short) mapid, 50);

								npc.setX(pt.x);
								npc.setY(pt.y);
								npc.setMap((short) mapid);
								npc.setMap(L1WorldMap.getInstance().getMap((short) mapid));
								npc.setHomeX(npc.getX());
								npc.setHomeY(npc.getY());
								npc.setHeading(6);
								npc.setLightSize(l1npc.getLightSize());
								npc.getLight().turnOnOffLight();
								L1World.getInstance().storeObject(npc);
								L1World.getInstance().addVisibleObject(npc);
								npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} else if (Info.get_type() == 2) {
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayers(Info.get_map_id())) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "感覺到某處有陰森的氣息."));

				if (pc.isDead())
					continue;

				if (!MJRnd.isWinning(1000000, Info.get_spawn_percent()))
					continue;

				String[] npcids = (String[]) MJArrangeParser
						.parsing(Info.get_npc_id(), ",", MJArrangeParseeFactory.createStringArrange()).result();
				String[] count = (String[]) MJArrangeParser
						.parsing(Info.get_count(), ",", MJArrangeParseeFactory.createStringArrange()).result();
				int size = 0;
				for (String ids : npcids) {
					int npcid = Integer.parseInt(ids);
					int npc_count = Integer.parseInt(count[size++]);
					for (int i = 0; i < npc_count; i++) {
						l1npc = NpcTable.getInstance().getTemplate(npcid);
						if (l1npc != null) {
							try {
								int mapid = Info.get_map_id();
								int x = pc.getX();
								int y = pc.getY();

								npc = NpcTable.getInstance().newNpcInstance(npcid);
								npc.setId(IdFactory.getInstance().nextId());
								MJPoint pt = MJPoint.newInstance(x, y, Info.get_range(), (short) mapid, 50);

								npc.setX(pt.x);
								npc.setY(pt.y);
								npc.setMap((short) mapid);
								npc.setMap(L1WorldMap.getInstance().getMap((short) mapid));
								npc.setHomeX(npc.getX());
								npc.setHomeY(npc.getY());
								npc.setHeading(6);
								npc.setLightSize(l1npc.getLightSize());
								npc.getLight().turnOnOffLight();
								L1World.getInstance().storeObject(npc);
								L1World.getInstance().addVisibleObject(npc);
								npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} else if (Info.get_type() == 3) {
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayers(Info.get_map_id())) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "感覺到某處有陰森的氣息."));
			}

			String[] npcids = (String[]) MJArrangeParser
					.parsing(Info.get_npc_id(), ",", MJArrangeParseeFactory.createStringArrange()).result();
			String[] count = (String[]) MJArrangeParser
					.parsing(Info.get_count(), ",", MJArrangeParseeFactory.createStringArrange()).result();
			int size = 0;
			for (String ids : npcids) {
				int npcid = Integer.parseInt(ids);
				int npc_count = Integer.parseInt(count[size++]);
				for (int i = 0; i < npc_count; i++) {
					l1npc = NpcTable.getInstance().getTemplate(npcid);
					if (l1npc != null) {
						try {
							int mapid = Info.get_map_id();
							int x = MapsTable.getInstance().getStartX(mapid) + ((MapsTable.getInstance().getEndX(mapid)
									- MapsTable.getInstance().getStartX(mapid)) / 2);
							int y = MapsTable.getInstance().getStartY(mapid) + ((MapsTable.getInstance().getEndY(mapid)
									- MapsTable.getInstance().getStartY(mapid)) / 2);

							npc = NpcTable.getInstance().newNpcInstance(npcid);
							npc.setId(IdFactory.getInstance().nextId());
							MJPoint pt = MJPoint.newInstance(x, y, Info.get_range(), (short) mapid, 50);

							npc.setX(pt.x);
							npc.setY(pt.y);
							npc.setMap((short) mapid);
							npc.setMap(L1WorldMap.getInstance().getMap((short) mapid));
							npc.setHomeX(npc.getX());
							npc.setHomeY(npc.getY());
							npc.setHeading(6);
							npc.setLightSize(l1npc.getLightSize());
							npc.getLight().turnOnOffLight();
							L1World.getInstance().storeObject(npc);
							L1World.getInstance().addVisibleObject(npc);
							npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private int _cloud_spawn_time = -1;

	public void cloud_spawn() {
		int servertime = GameTimeClock.getInstance().getGameTime().getSeconds();
		int nowtime = ((servertime % 86400) / 10) / 360;

		if (nowtime == _cloud_spawn_time)
			return;

		if (nowtime != _cloud_spawn_time)
			_cloud_spawn_time = -1;

		FICloudSpawnInfo pInfo = _cloud_spawn.get(MJRnd.next(0, _cloud_spawn.size()));
		if (pInfo == null)
			return;

		for (L1EffectInstance effect : effect_list) {
			effect.deleteMe();
		}
		effect_list.clear();

		_cloud_spawn_time = nowtime;

		switch (nowtime) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 20:
			case 21:
			case 22:
			case 23:
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayers(pInfo.get_map_id())) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "在島上的某處感知到痛苦的氣息."));
				}
				GeneralThreadPool.getInstance().execute(new PoisonCloudWallThread(pInfo));

				break;
		}
	}

	private final FastTable<L1EffectInstance> effect_list = new FastTable<L1EffectInstance>();
	private int _mapid = 0;

	public class PoisonCloudWallThread implements Runnable {
		private FICloudSpawnInfo _pInfo;
		private int _x_space;
		private int _z_space;
		private int _x;
		private int _y;
		private int _map_id;
		private int _range;

		public PoisonCloudWallThread(FICloudSpawnInfo pInfo) {
			_pInfo = pInfo;
			_x_space = 8;
			_z_space = 8;
			_x = pInfo.get_x();
			_y = pInfo.get_y();
			_map_id = pInfo.get_map_id();
			_range = pInfo.get_range();
			_mapid = _map_id;
		}

		@Override
		public void run() {
			if (_pInfo == null)
				return;

			int[][] cpos = new int[4][2];
			try {
				int remain_time = (60000) * 5;

				for (int width = _range; width >= 0; width -= _z_space) {
					int left = _x - width;
					int top = _y - width;
					int right = _x + width;
					int bottom = _y + width;

					for (int i = 0; i < width * 4; i += _x_space) {
						cpos[0][0] = left + i;
						cpos[0][1] = top;

						cpos[1][0] = right;
						cpos[1][1] = top + i;

						cpos[2][0] = right - i;
						cpos[2][1] = bottom + _x_space;

						cpos[3][0] = left - _x_space;
						cpos[3][1] = bottom - i;

						for (int j = 0; j < 4; j++) {
							L1EffectInstance effect = L1EffectSpawn.getInstance().spawnEffect(8503185, remain_time,
									cpos[j][0] + 5, cpos[j][1] - 5, (short) _map_id);
							effect_list.add(effect);
						}
					}
				}

				L1EffectInstance position1 = L1EffectSpawn.getInstance().spawnEffect(8503185, remain_time, _x - 4,
						_y + 4, (short) _map_id);
				effect_list.add(position1);
				L1EffectInstance position2 = L1EffectSpawn.getInstance().spawnEffect(8503185, remain_time, _x + 4,
						_y - 4, (short) _map_id);
				effect_list.add(position2);
				L1EffectInstance position3 = L1EffectSpawn.getInstance().spawnEffect(8503185, remain_time, _x + 4,
						_y + 4, (short) _map_id);
				effect_list.add(position3);
				L1EffectInstance position4 = L1EffectSpawn.getInstance().spawnEffect(8503185, remain_time, _x - 4,
						_y - 4, (short) _map_id);
				effect_list.add(position4);
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
								pc.receiveDamage(effect, Config.ServerAdSetting.CLOUD_POISON_DAMAGE / 2);// 磁場損傷
								pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage));
								pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage));
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onMonthChanged(BaseTime time) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDayChanged(BaseTime time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHourChanged(BaseTime time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMinuteChanged(BaseTime time) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSecondChanged(BaseTime time) {
		// TODO Auto-generated method stub
		cloud_spawn();
		NightSpawn();
		Poison_cloud_damage();
	}
}
