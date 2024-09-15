package l1j.server.server.server.Controller;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import l1j.server.DeathMatch.DeathMatch;
import l1j.server.DeathMatch.DeathMatchSystem;
import l1j.server.ForgottenIsland.FIController;
import l1j.server.InfinityBattle.InfinityBattle;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Builder.MJLiftGateBuilder;
import l1j.server.MJTemplate.Interface.MJMonsterDeathHandler;
import l1j.server.MJTemplate.Interface.MJMonsterTransformHandler;
import l1j.server.MJTemplate.L1Instance.MJL1LiftGateInstance;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_CHANGE_NOTI;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_INFO_NOTI;
import l1j.server.QueenAntSystem.QueenAntController;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.clientpackets.C_Attr;
import l1j.server.server.server.datatables.EventLogTable;
import l1j.server.server.datatables.EventTimeTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.L1SpawnUtil;

public class EventThread implements Runnable {

	private static EventThread instance;

	L1Map[] CrackIntheTower = new L1Map[1];

	private static final int[] _Crackfloor = { 101, 102, 103, 104, 105, 106,
			107, 108, 109 };

	public int num1 = 0;

	public static EventThread getInstance() {
//        System.out.println("開始了嗎？");
		if (instance == null) {
			instance = new EventThread();
		}
		return instance;
	}

	public static MJL1LiftGateInstance _leftBDoor;
	public static MJL1LiftGateInstance _rightBDoor;
	public static MJL1LiftGateInstance _centerBDoor;

	public static int _CrackInTheTower;

	public static int getCrackIntheTower() {
		return _CrackInTheTower;
	}

	public static void setCrackIntheTower(int num) {
		_CrackInTheTower = num;
	}

	private EventThread() {
		if (_leftBDoor == null) {
			MJLiftGateBuilder builder = new MJLiftGateBuilder().setGfx(16115);
			_leftBDoor = builder.build(32731, 32852, (short) 15404, false, 3);
			_leftBDoor.down();
			_rightBDoor = builder.build(32731, 32878, (short) 15404, false, 3);
			_rightBDoor.down();
			_centerBDoor = builder.build(16116, 32718, 32863, (short) 15404,
					true, 5);
			_centerBDoor.down();
		}
	}

	private static void on(int id) {
		GeneralThreadPool.getInstance().schedule(new BalogDoorController(id),
				30000L);
	}

	public static class BalogDoorController implements Runnable {
		private int _id;

		BalogDoorController(int ownerId) {
			_id = ownerId;
		}

		@Override
		public void run() {
			L1MonsterInstance m = (L1MonsterInstance) L1World.getInstance()
					.findObject(_id);
			if (m == null || m.isDead()) {
				_leftBDoor.down();
				_rightBDoor.down();
				_centerBDoor.down();
				return null;
			}

			try {
				if (MJRnd.isBoolean()) {
					_leftBDoor.takeClose(10000L);
					_rightBDoor.up();
					_centerBDoor.up();
				} else if (MJRnd.isBoolean()) {
					_leftBDoor.up();
					_rightBDoor.takeClose(10000L);
					_centerBDoor.up();
				} else {
					_leftBDoor.up();
					_rightBDoor.up();
					_centerBDoor.takeClose(10000L);
				}
			} finally {
				GeneralThreadPool.getInstance().schedule(this, 30000L);
			}
			return null;
		}
	}

	private void start_event_boss() {
		try {
			Iterator<L1NpcInstance> npc_iter = EventTimeTable.getInstance().get_npc_iter();
			L1NpcInstance npc = null;
//            System.out.println("來了嗎1？: ");

			while (npc_iter.hasNext()) {
				npc = npc_iter.next();
				if (npc == null) {
					continue;
				}

				if (get_boss_spawn_day_check(npc.getYoil(),	npc.get_next_day_index()) && get_boss_spawn_time(npc.get_boss_hour(), npc.get_boss_minute() )) {
//                    System.out.println("來了嗎？: " + npc.getNpcId());
					EventLogTable.table().newBossSpawn(npc, System.currentTimeMillis(), npc.get_end_boss_time());
					// 將 NPC 排除在活動通知之外
					if (!npc.is_boss_alarm())
						continue;

					if (npc.getNpcId() == 81111 || npc.getNpcId() == 8500129
							|| npc.getNpcId() == 8502042) {
						continue;
					}

					if (npc.get_boss_type() == 5) {
						InfinityBattle.getInstance().Start();
					}

					// 老闆出現時的全屏消息
					SC_NOTIFICATION_INFO_NOTI.onEventTicks(L1World.getInstance().getAllPlayers(), 500L);
					Collection<L1PcInstance> allPlayers = L1World.getInstance().getAllPlayers();
					for (L1PcInstance pc : allPlayers) {
						if (pc.isBossNotify()) {
							if (npc.get_boss_msg() != null) {
								pc.sendPackets(String.valueOf(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, npc.get_boss_msg().toString())));

								// 老闆出現時的特效
								if (npc.get_boss_effect() != 0) {
									if (pc.isPrivateShop()|| !pc.isBossNotify())
										continue;
									if (npc.get_boss_effect() > 10)
										pc.sendPackets(String.valueOf(new S_SkillSound(pc.getId(), npc.get_boss_effect())));
									else
										pc.sendPackets(S_DisplayEffect.newInstance(npc.get_boss_effect()));
								}
							}
						}
					}
					// 老闆移動台詞
					if (npc.get_boss_yn()) {
						for (L1PcInstance pc : allPlayers) {
							if (pc.isBossNotify()) {
								if (pc.isPrivateShop())
									continue;
								pc.setBossNpc(npc.getNpcId());
								pc.sendPackets(String.valueOf(new S_Message_YN(C_Attr.MSGCODE_6008_EVENT_BOSS, 6008,npc.get_boss_yn_msg())));
							}
						}
					}
					// TODO 蟻后出現巢穴特效
					if (npc.getNpcId() == 8503163) {
						for (L1PcInstance pc : allPlayers) {
							SC_NOTIFICATION_CHANGE_NOTI.sendQueenAnt(pc);
						}
						QueenAntController open = new QueenAntController();
						open.Start();
					}
					// System.out.println(npc.getNpcId());
					if (npc.getNpcId() == 120717) {
						int i = _Crackfloor[MJRnd.next(_Crackfloor.length)];
						CrackIntheTower[0] = L1WorldMap.getInstance().getMap((short) i).set_CrackIntheTower(true);
						num1 = i;
						// System.out.println("地圖編號: " + i);
						setCrackIntheTower(num1);
						int floor = num1 - 100;
						for (L1PcInstance pc : allPlayers) {
							SC_NOTIFICATION_CHANGE_NOTI.AnimationAlam(pc, npc);
							pc.sendPackets(String.valueOf(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "傲慢之塔 " + floor + "層出現了可疑的陰影。")));
						}
						// System.out.println(floor + "層裂縫開啟");
					}
					if (npc.get_boss_type() == 50) {
						FIController.getInstance().Start();
						for (L1PcInstance pc : allPlayers) {
							pc.sendPackets(String.valueOf(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "被遺忘的島嶼已經開放。")));
						}
					}

					if (npc.get_boss_type() == 51 || npc.get_boss_type() == 52 || npc.get_boss_type() == 53 || npc.get_boss_type() == 54) {
						DeathMatch open = new DeathMatch(13005);
						for (L1PcInstance pc : allPlayers) {
							pc.sendPackets(String.valueOf(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "團隊死亡競賽已經開始。")));
						}
						System.out.println("死亡競賽開始");
						open.Start();
					}

					// TODO NPC生成後1小時內刪除
					MJPoint pt = MJPoint.newInstance(npc.getHomeX(),
							npc.getHomeY(), npc.getHomeRnd(),
							(short) npc.getMapId(), 50);
					L1NpcInstance realNpc = L1SpawnUtil.Gmspawn(npc.getNpcId(),
							pt.x, pt.y, pt.mapId, npc.getHeading(),
							npc.getBossIngTime() * 60 * 1000);
					if (npc.getNpcId() == 45752) {
						_leftBDoor.up();
						_rightBDoor.up();
						L1MonsterInstance m = (L1MonsterInstance) realNpc;
						m.setTransformHandler(new MJMonsterTransformHandler() {
							@Override
							public void onTransFormNotify(L1MonsterInstance m) {
								_centerBDoor.up();
								on(m.getId());
							}
						});
						m.setDeathHandler(new MJMonsterDeathHandler() {
							@Override
							public boolean onDeathNotify(L1MonsterInstance m) {
								_leftBDoor.down();
								_rightBDoor.down();
								_centerBDoor.down();
								return false;
							}
						});
					}
				}

				Calendar end_calendar = Calendar.getInstance();
				long end_time = npc.get_end_boss_time();
				Date end = new Date(end_time);
				end_calendar.setTime(end);
				// System.out.println("結束比較時間 : " + end_time);

				int end_hour = end_calendar.get(Calendar.HOUR_OF_DAY);
				int end_min = end_calendar.get(Calendar.MINUTE);

				// System.out.println("結束時間 - " + end_hour + " : " + end_min);

				if (EventTimeTable.getNowDay() >= end_calendar.get(Calendar.DAY_OF_WEEK)
						&& get_boss_delete_time(end_hour, end_min)) {
					int next_index = npc.get_next_day_index() + 1;
					if (next_index == npc.getYoil().length) {
						next_index -= 1;
					} else if (next_index > npc.getYoil().length) {
						next_index = 0;
					}

					if (npc.getNpcId() == 120717) {
						if (CrackIntheTower[0] != null) {
							CrackIntheTower[0].set_CrackIntheTower(false);
							num1 = 0;
							setCrackIntheTower(0);
						}
					}
					if (npc.get_boss_type() == 50) {
						FIController.getInstance().End();
					}

					int now_day = EventTimeTable.getNowDay();
					int week_day = EventTimeTable.getNowDatByString(npc.getYoil()[next_index]);
					int day_gap = now_day - week_day;

					if (day_gap > 0) {
						day_gap = (7 - day_gap) * -1;
					}

					long spawn_time = npc.get_boss_time()
							+ ((day_gap * -1) * 86400000);

					/**
					 * 根據當前時間和每週的時間，如果本週的時間已經過去，則計算下週的時間。
					 **/
					npc.set_boss_time(spawn_time);
					npc.set_next_day_index(next_index);
					npc.set_end_boss_time(npc.get_boss_time()
							+ (npc.getBossIngTime() * 60000));

					if (!npc.is_boss_alarm())
						continue;

					npc.set_boss_time(npc.get_boss_time() + 86400);
					Iterator<L1PcInstance> pc_iter = L1World.getInstance()
							.getAllPlayers().iterator();
					L1PcInstance pc = null;

					while (pc_iter.hasNext()) {
						pc = pc_iter.next();
						if (pc == null) {
							continue;
						}

						if (pc.getBossNpc() != 0) {
							pc.setBossNpc(0);
						}
						pc.sendPackets(SC_NOTIFICATION_INFO_NOTI.make_stream(pc, npc.getNpcId(), true));
						// pc.sendPackets(SC_NOTIFICATION_INFO_NOTI.make_stream(pc,
						// 0, false));//更新封包
					}
				}

				/*
				 * try { GeneralThreadPool.getInstance().schedule(new Runnable()
				 * {
				 *
				 * @override public void run() { for (L1PcInstance pc :
				 * L1World.getInstance().getAllPlayers()) { if (pc.getBossNpc()
				 * != 0) { pc.setBossNpc(0); } } } }, npc.get_boss_time() *
				 * 1000);// 首領出現的等待時間 } catch (Exception e) {
				 * e.printStackTrace(); }
				 */
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean get_boss_spawn_day_check(String[] yoil, int index) {
		int now_day = EventTimeTable.getNowDay();
		int week_day = EventTimeTable.getNowDatByString(yoil[index]);

		if (now_day == week_day)
			return true;

		return false;
	}

	/**
	 * 獲取時間
	 *
	 * @param h
	 * @param m
	 * @return
	 */
	public boolean get_boss_spawn_time(int h, int m) {
		Date set = new Date(System.currentTimeMillis());
		int hour = set.getHours();
		int minute = set.getMinutes();

		if (m >= 60) {
			h += m / 60;
			m -= (m / 60) * 60;
		}

		if (hour == h && minute == m) {
			return true;
		}
		return false;
	}

	public boolean get_boss_delete_time(int h, int m) {
		Date set = new Date(System.currentTimeMillis());
		int hour = set.getHours();
		int minute = set.getMinutes();

		if (m >= 60) {
			h += m / 60;
			m -= (m / 60) * 60;
		}

		if (hour >= h && minute >= m) {
			return true;
		}
		return false;
	}

	/**
	 * 控制器每分鐘調用一次。
	 */
	@Override
	public void run() {
		try {
			// System.out.println("事件執行緒");
			start_event_boss();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			GeneralThreadPool.getInstance().schedule(this, 60000L);
		}
		return null;
	}

}