package l1j.server.server.server.Controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import MJFX.UIAdapter.MJUIAdapter;
import l1j.server.Config;
import l1j.server.MJBotSystem.MJBotBossNotifier;
import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJBotSystem.AI.MJBotAI;
import l1j.server.MJBotSystem.AI.MJBotMovableAI;
import l1j.server.MJBotSystem.Loader.MJBotBossNotifierLoader;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.clientpackets.C_Attr;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1MobGroupSpawn;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.server.datatables.BossMonsterSpawnList;
import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Boss;

public class BossController implements Runnable {

	private static BossController instance;
	private static final ConcurrentHashMap<Integer, Integer> _bossesId = new ConcurrentHashMap<Integer, Integer>(256);

	public static BossController getInstance() {
		if (instance == null) {
			instance = new BossController();
			GeneralThreadPool.getInstance().execute(instance);
		}
		return instance;
	}

	private void spawn_check() {
		try {
			List<L1Boss> list = BossMonsterSpawnList.getList();
			if (list.size() > 0) {
				long time = System.currentTimeMillis();
				Date date = new Date(time);
				int hour = date.getHours();
				int min = date.getMinutes();
				int sec = date.getSeconds();
				for (L1Boss b : list) {
					try {
						// 創建對象.
						if (b.isSpawnTime(hour, min, time) && sec == 0) {
							Integer id = _bossesId.get(b.getNpcId());
							if (id != null) {
								L1Object obj = L1World.getInstance().findObject(id);
								if (obj != null && obj.instanceOf(MJL1Type.L1TYPE_MONSTER)) {
									L1Character c = (L1Character) obj;
									if (!c.isDead())
										continue;
								}
							}

							int delete_time = b.getDeleteTime() * 1000;

							// TODO: 當該首領出現時，會被強制移動到指定坐標.
							if (b.getNpcId() == 8502091) {
								for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
									if (pc.getMapId() == 111) {
										pc.start_teleport(32619, 32807, 111, pc.getHeading(), 18339, true);
									}
								}
							}

							if (b.getNpcType().equalsIgnoreCase("Monster")) {
								if (b.getnonespawntime() != 0 && MJRnd.isWinning(1000000, b.getnonespawntime())) {
//                                    System.out.println(b.getMonName() + " 探測首領");
//                                    System.out.println(b.getnonespawntime() + " 探測發動概率");
									return;
								}

								L1MonsterInstance boss = boss_spawn(b, b.getX(), b.getY(), (short) b.getMap(), b.getNpcId(), b.getRndLoc(), delete_time, b.isMent(), b.isYn(), b.get_display_effect());

								if (b.getGroupId() != 0) {
									L1MobGroupSpawn.getInstance().doSpawn(boss, b.getGroupId(), false, false);
								}

								if (boss != null) {
									MJUIAdapter.on_boss_append(boss.getNpcId(), boss.getName(), boss.getX(), boss.getY(), boss.getMapId());
									_bossesId.put(boss.getNpcId(), boss.getId());
								}
							} else {
								L1NpcInstance boss = npc_spawn(b, b.getX(), b.getY(), (short) b.getMap(), b.getNpcId(), b.getRndLoc(), delete_time, b.isMent(), b.isYn(), b.get_display_effect());

								if (boss != null) {
									MJUIAdapter.on_boss_append(boss.getNpcId(), boss.getName(), boss.getX(), boss.getY(), boss.getMapId());
									_bossesId.put(boss.getNpcId(), boss.getId());
								}
							}
						}

						if (isResetTime(b)) {
							b.resetSpawnTime();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 控制器每分鐘調用一次。
	 */
	@Override
	public void run() {
		try {
			spawn_check();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			GeneralThreadPool.getInstance().schedule(this, 1000L);
			//GeneralThreadPool.getInstance().schedule(this, 60000L);
		}
		return null;
	}

	public boolean isResetTime(L1Boss b) {
		Calendar oCalendar = Calendar.getInstance();
		int hour = oCalendar.get(Calendar.HOUR_OF_DAY);
		int min = oCalendar.get(Calendar.MINUTE);
		int sec = oCalendar.get(Calendar.SECOND);
		// TODO: 在24:00時添加到列表中（顯示在管理員窗口）
		if (hour == 0 && min == 0 && sec == 0) {
			return true;
		}
		return false;
	}

	public static L1MonsterInstance boss_spawn(L1Boss b, int x, int y, short map, int npcId, int randomRange, int timeMillisToDelete, boolean ment, boolean ynment, int display_effect) {
		L1NpcInstance npc = null;
		try {
			npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(map);

			if (b.getMovement_distance() > 0)
				npc.setMovementDistance(b.getMovement_distance());

			if (randomRange == 0) {
				npc.getLocation().set(x, y, map);
				// npc.getLocation().forward(5);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
//                        System.out.println("[BossController] NPC坐標選擇完成 : " + npc.getX() + "," + npc.getY() + "," + npc.getMapId() + " / " + npc.getName());
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().set(x, y, map);
					npc.getLocation().forward(5);
//                    System.out.println("[BossController] NPC坐標選擇完成 : " + npc.getX() + "," + npc.getY() + "," + npc.getMapId() + " / " + npc.getName());
				}
			}

			if (npc.getNpcId() == 900007 || npc.getNpcId() == 900015 || npc.getNpcId() == 900036 || npc.getNpcId() == 900219) {
				for (L1PcInstance _pc : L1World.getInstance().getVisiblePlayer(npc)) {
					npc.onPerceive(_pc);
					S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_AxeWalk);
					_pc.sendPackets(gfx);
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(5);

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);


			if (ment) {
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(b.getMentMessage()));
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, b.getMentMessage()));
			}

			S_DisplayEffect effect = null;
			if (ynment && !Config.Login.StandbyServer) {
				if (display_effect > 0)
					effect = S_DisplayEffect.newInstance(display_effect);
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					if (pc.isPrivateShop() || !pc.isBossNotify() || !pc.is_world() || !pc.getMap().isEscapable() || pc.is_shift_battle())
						continue;
					if (effect != null)
						pc.sendPackets(effect, false);

					if (pc.getAI() != null && pc.getAI().getBotType() == MJBotType.HUNT) {
						MJBotBossNotifier ntf = MJBotBossNotifierLoader.getInstance().get(npc.getNpcId());
						MJBotAI ai = pc.getAI();
						if (ai instanceof MJBotMovableAI) {
							if (((MJBotMovableAI) ai).getWarCastle() != -1)
								continue;
						}

						if (ntf != null && ai.getBrain().toRand(100 - ntf.aggro) < ai.getBrain().getHormon())
							ai.teleport(npc.getX(), npc.getY(), npc.getMapId());
						continue;
					}
					pc.setBossYN(npc.getNpcId());
					// pc.sendPackets(new S_Message_YN(622, b.getYnMessage()));
					pc.sendPackets(new S_Message_YN(C_Attr.MSGCODE_6008_BOSS, 6008, b.getYnMessage()));
				}
				if (effect != null)
					effect.clear();
				try {
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
								if (pc.getBossYN() != 0) {
									pc.setBossYN(0);
								}
							}
							return null;
						}
					}, 10000);// 首領重生等待時間
				} catch (Exception e1) {
				}
			}
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 開始聊天
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
				timer.begin();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return npc instanceof L1MonsterInstance ? (L1MonsterInstance) npc : null;
	}

	public static L1NpcInstance npc_spawn(L1Boss b, int x, int y, short map, int npcId, int randomRange, int timeMillisToDelete, boolean ment, boolean ynment, int display_effect) {
		L1NpcInstance npc = null;
		try {
			npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(map);
			if (randomRange == 0) {
				npc.getLocation().set(x, y, map);
				// npc.getLocation().forward(5);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().set(x, y, map);
					npc.getLocation().forward(5);
				}
			}

			if (npc.getNpcId() == 900007 || npc.getNpcId() == 900015 || npc.getNpcId() == 900036 || npc.getNpcId() == 900219) {
				for (L1PcInstance _pc : L1World.getInstance().getVisiblePlayer(npc)) {
					npc.onPerceive(_pc);
					S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_AxeWalk);
					_pc.sendPackets(gfx);
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(5);

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			if (ment) {
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(b.getMentMessage()));
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, b.getMentMessage()));
			}

			S_DisplayEffect effect = null;
			if (ynment && !Config.Login.StandbyServer) {
				if (display_effect > 0)
					effect = S_DisplayEffect.newInstance(display_effect);
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					if (pc.isPrivateShop() || !pc.isBossNotify() || !pc.is_world() || !pc.getMap().isEscapable() || pc.is_shift_battle())
						continue;
					if (effect != null)
						pc.sendPackets(effect, false);

					if (pc.getAI() != null && pc.getAI().getBotType() == MJBotType.HUNT) {
						MJBotBossNotifier ntf = MJBotBossNotifierLoader.getInstance().get(npc.getNpcId());
						MJBotAI ai = pc.getAI();
						if (ai instanceof MJBotMovableAI) {
							if (((MJBotMovableAI) ai).getWarCastle() != -1)
								continue;
						}

						if (ntf != null && ai.getBrain().toRand(100 - ntf.aggro) < ai.getBrain().getHormon())
							ai.teleport(npc.getX(), npc.getY(), npc.getMapId());
						continue;
					}
					pc.setBossYN(npc.getNpcId());
					// pc.sendPackets(new S_Message_YN(622, b.getYnMessage()));
					pc.sendPackets(new S_Message_YN(C_Attr.MSGCODE_6008_BOSS, 6008, b.getYnMessage()));
				}
				if (effect != null)
					effect.clear();
				try {
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
								if (pc.getBossYN() != 0) {
									pc.setBossYN(0);
								}
							}
							return null;
						}
					}, 10000);// 首領重生等待時間
				} catch (Exception e1) {
				}
			}
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 開始聊天
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
				timer.begin();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return npc instanceof L1NpcInstance ? (L1NpcInstance) npc : null;
	}
}