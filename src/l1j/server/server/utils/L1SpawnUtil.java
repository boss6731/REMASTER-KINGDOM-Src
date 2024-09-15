package l1j.server.server.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.IndunSystem.Orim.OrimController;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcInfoTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.SprTable;
import l1j.server.server.datatables.TrapTable;
import l1j.server.server.datatables.NpcInfoTable.NpcInfoData;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TrapInstance;
import l1j.server.server.model.trap.L1Trap;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.types.Point;

public class L1SpawnUtil {
	private static Logger _log = Logger.getLogger(L1SpawnUtil.class.getName());

	public static L1NpcInstance spawnAndGet(L1PcInstance pc, int npcId, int randomRange, int timeMillisToDelete) {
		L1NpcInstance npc = null;
		try {
			npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());
			if (randomRange == 0) {
				npc.getLocation(). set(pc.getLocation());
				npc.getLocation(). forward(pc.getHeading());
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(pc.getX() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(pc.getY() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap(). isInMap(npc.getLocation()) && npc.getMap(). isPassable(npc.getLocation())) {
						break;
					}
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation(). set(pc.getLocation());
					npc.getLocation(). forward(pc.getHeading());
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(pc.getHeading());

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);
			
			if (npcId == 181164) { // 基爾塔斯大火焰
				npc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(npc));
			    //npc.broadcastPacket(S_WorldPutObject.get(npc));
			    npc.broadcastPacket(new S_DoActionGFX(npc.getId(), 4));
			    npc.setStatus(4); 
			    npc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(npc));
			   // npc.broadcastPacket(S_WorldPutObject.get(npc));
			    npc.broadcastPacket(new S_DoActionGFX(npc.getId(), 0));
			    npc.setStatus(0);
			    npc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(npc));
			    //npc.broadcastPacket(S_WorldPutObject.get(npc));
			    npc.broadcastPacket(new S_DoActionGFX(npc.getId(), 8));
			    npc.setStatus(8);
			}
			npc.getNpcTemplate().doBornNpc(npc);
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
		return npc;
	}
	
	public static void spawn(L1PcInstance pc, int npcId, int randomRange, int timeMillisToDelete) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());
			if (randomRange == 0) {
				npc.getLocation(). set(pc.getLocation());
				npc.getLocation(). forward(pc.getHeading());
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(pc.getX() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(pc.getY() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap(). isInMap(npc.getLocation()) && npc.getMap(). isPassable(npc.getLocation())) {
						break;
					}
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation(). set(pc.getLocation());
					npc.getLocation(). forward(pc.getHeading());
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(pc.getHeading());

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);
			
			if (npcId == 181164) { // 基爾塔斯大火焰
				npc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(npc));
			   // npc.broadcastPacket(S_WorldPutObject.get(npc));
			    npc.broadcastPacket(new S_DoActionGFX(npc.getId(), 4));
			    npc.setStatus(4); 
			    npc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(npc));
			    //npc.broadcastPacket(S_WorldPutObject.get(npc));
			    npc.broadcastPacket(new S_DoActionGFX(npc.getId(), 0));
			    npc.setStatus(0);
			    npc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(npc));
			   // npc.broadcastPacket(S_WorldPutObject.get(npc));
			    npc.broadcastPacket(new S_DoActionGFX(npc.getId(), 8));
			    npc.setStatus(8);
			}
			npc.getNpcTemplate().doBornNpc(npc);
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
	}
	
	public static void spawngmcmd(L1PcInstance pc, int npcId) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());
			npc.getLocation(). set(pc.getLocation());
			//npc.getLocation(). forward(pc.getHeading());
			
			npc.setHomeX(pc.getX());
			npc.setHomeY(pc.getY());
			npc.setHeading(pc.getHeading());

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);
			
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 開始聊天
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 生成NPC
	 * @param x X座標
	 * @param y Y座標
	 * @param map 地圖
	 * @param npcId NPC編號
	 * @param randomRange 隨機範圍
	 * @param timeMillisToDelete 刪除時間（毫秒）
	 * @param movemap 設置移動的地圖 - 用於安塔瑞德
	 */
	public static void spawn2(int x, int y, short map, int npcId, int randomRange, int timeMillisToDelete, int movemap) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(map);
			if (randomRange == 0) {
				npc.getLocation().set(x,y,map);
				npc.getLocation().forward(5);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap(). isInMap(npc.getLocation()) && npc.getMap(). isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().set(x,y,map);
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
			
			if(npcId == 900007 || npcId == 900008 || npcId == 900036 || npcId == 900037 || npcId == 900219 || npcId == 5101 || npcId == 5102){ // 設定要移動的地圖
				L1FieldObjectInstance fobj = (L1FieldObjectInstance)npc;
				fobj.setMoveMapId(movemap);
			}

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);
			
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
	}
	
	public static void spawn3(int x, int y, short MapId, int Heading, int npcId, int randomRange, boolean isUsePainwand,
			int timeMillisToDelete) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(MapId);
			if (randomRange == 0) {
				npc.getLocation().forward(Heading);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if ((npc.getMap().isInMap(npc.getLocation())) && (npc.getMap().isPassable(npc.getLocation()))) {
						break;
					}
					Thread.sleep(1L);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().forward(Heading);
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(Heading);
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			npc.getLight().turnOnOffLight();
			npc.startChat(0);
			if (timeMillisToDelete > 0) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, "L1SpawnUtil[]Error6", e);
		}
	}
	
	public static L1MonsterInstance spawnfieldboss(int x, int y, short map, int npcId, int randomRange, int timeMillisToDelete, int movemap) {
		L1NpcInstance npc = null;
		try {
			npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(map);
			if (randomRange == 0) {
				npc.getLocation().set(x,y,map);
				npc.getLocation().forward(5);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap(). isInMap(npc.getLocation()) && npc.getMap(). isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().set(x,y,map);
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
			
			if(npcId == 900007 || npcId == 900008 || npcId == 900036 || npcId == 900037 || npcId == 900219 || npcId == 5101 || npcId == 5102){ // 設定要移動的地圖
				L1FieldObjectInstance fobj = (L1FieldObjectInstance)npc;
				fobj.setMoveMapId(movemap);
			}
			
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);
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
		return npc instanceof L1MonsterInstance ? (L1MonsterInstance)npc : null;
	}

	/** 活動命令 **/
	public static L1NpcInstance Gmspawn(int npcId, int x, int y, short mapid,
			int heading, int timeMinToDelete) {
		L1NpcInstance npc = null;
		try {
			npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(mapid);
			npc.setX(x);
			npc.setY(y);
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(heading);
			npc.getMoveState().setHeading(heading);

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			if (0 < timeMinToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMinToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return npc;
	}
	
	public static void spawn10(int x, int y, short map, int heading,
			int npcId, int timeMillisToDelete, L1Clan clan) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());					
			npc.setMap(map);
			npc.getLocation().set(x, y, map);
			npc.getLocation().forward(heading);
			
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(heading);
			if (npc instanceof L1MerchantInstance) {
				L1MerchantInstance mer = (L1MerchantInstance) npc;
				mer.setNameId(clan.getClanName());
				mer.setClanid(clan.getClanId());
				mer.setClanname(clan.getClanName());
			}

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 開始聊天
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, 	timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void spawn5(int x, int y, short MapId, int Heading, int npcId, int randomRange, boolean isUsePainwand) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(MapId);
			if (randomRange == 0) {
				npc.getLocation().forward(Heading);
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
					npc.getLocation().forward(Heading);
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(npc.getHeading());
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 開始聊天

			if (npc.getMapId() == 9101) {
				if (npcId == 91240) {
					if (Heading == 4)
						OrimController.getInstance().setShell1(npc);
					else
						OrimController.getInstance().setShell2(npc);
				} else if ((npcId != 91222) && (npcId != 91233) && (npcId != 91235) && (npcId != 91243)) {
					OrimController.getInstance().addMonList(npc);
				}
			}

		} catch (Exception e) {
			_log.log(Level.SEVERE, "L1SpawnUtil[]Error3", e);
		}
	}
	
	public static void spawn(L1NpcInstance pc, int npcId, int randomRange) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());

			if (randomRange == 0) {
				npc.getLocation().set(pc.getLocation());
				npc.getLocation().forward(pc.getHeading());
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(pc.getX() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(pc.getY() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap(). isInMap(npc.getLocation()) && npc.getMap(). isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().set(pc.getLocation());
					npc.getLocation().forward(pc.getHeading());
				}
			}
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(pc.getHeading());
			
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 開始聊天
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 時間毫秒
	public static void spawn6(int x, int y, short map, int npcId, int randomRange, int timeMillisToDelete, int movemap) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(map);
			if (randomRange == 0) {
				npc.getLocation().set(x, y, map);
				npc.getLocation().forward(5);
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

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(4);

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

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
	}

	
	public static L1NpcInstance spawnCount(int x, int y, short map, int npcId, int randomRange, int timeMillisToDelete, int count) {
		L1NpcInstance npc = null;
		try {
			for (int i = 0; i < count; i++) {
				npc = NpcTable.getInstance().newNpcInstance(npcId);
				npc.setId(IdFactory.getInstance().nextId());
				npc.setMap(map);
				if (randomRange == 0) {
					npc.getLocation().set(x, y, map);
					npc.getLocation().forward(5);
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

				npc.setHomeX(npc.getX());
				npc.setHomeY(npc.getY());
				npc.setHeading(6);

				if (npcId == 45545 || npcId == 45516 || npcId == 45529) {
					L1MonsterInstance mon = (L1MonsterInstance) npc;
					mon.setMovementDistance(15);
				}

				L1World.getInstance().storeObject(npc);
				L1World.getInstance().addVisibleObject(npc);

				npc.getLight().turnOnOffLight();
				npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
				npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 開始聊天
				if (0 < timeMillisToDelete) {
					L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
					timer.begin();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return npc;
	}

	
	public static void spawn(L1NpcInstance pc, int npcId, int randomRange, int timeMillisToDelete) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());

			if (randomRange == 0) {
				npc.getLocation().set(pc.getLocation());
				npc.getLocation().forward(pc.getHeading());
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(pc.getX() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(pc.getY() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap(). isInMap(npc.getLocation()) && npc.getMap(). isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().set(pc.getLocation());
					npc.getLocation().forward(pc.getHeading());
				}
			}
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(pc.getHeading());
			
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);
			
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
	}

	public static void spawn(L1PcInstance pc, int i, int j, int k, boolean b) {
		// TODO Auto-generated method stub
		
	}
	
	public static L1NpcInstance spawn4(int x, int y, short map, int heading, int npcId, int randomRange, int timeMillisToDelete, int movemap) {
		return spawn4(x, y, map, heading, npcId, randomRange, timeMillisToDelete, movemap, false);
	}
	
	public static L1NpcInstance spawn4(int x, int y, short map, int heading, int npcId, int randomRange, int timeMillisToDelete, int movemap, boolean level) {
		L1NpcInstance npc = null;
		try {
			if (level) {
				npc = NpcTable.getInstance().newNpcInstance(npcId + 1000000);
			} else {
				npc = NpcTable.getInstance().newNpcInstance(npcId);
			}

			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(map);
			if (randomRange == 0) {
				npc.getLocation().set(x, y, map);
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
				}
			}
			if (npc instanceof L1MonsterInstance) {
				((L1MonsterInstance) npc).initHide();
			}
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(heading);

			if (npc.getNpcId() == 5000091) {
				L1DoorInstance door = (L1DoorInstance) npc;
				door.setDoorId(npc.getNpcTemplate().get_npcId());
				door.setDirection(0);
				door.setLeftEdgeLocation(door.getX());
				door.setRightEdgeLocation(door.getX());

				door.setOpenStatus(ActionCodes.ACTION_Close);
				door.isPassibleDoor(false);
				door.setPassable(1);
			}
			
			if (npc.getNpcId() == 7800220 ) {
				L1DoorInstance door = (L1DoorInstance) npc;
				door.setDoorId(npc.getNpcTemplate().get_npcId());
				int doorX = door.getX();
				int doorY = door.getY();
				door.setDirection(1);
				door.setLeftEdgeLocation(doorY - 3);
				door.setRightEdgeLocation(doorY);
				door.setOpenStatus(ActionCodes.ACTION_Close);
				door.isPassibleDoor(false);
				door.setPassable(1);
			}
			if (npc.getNpcId() == 7800221 || npc.getNpcId() == 7800222 || npc.getNpcId() == 7800223) {
				L1DoorInstance door = (L1DoorInstance) npc;
				door.setDoorId(npc.getNpcTemplate().get_npcId());
				int doorX = door.getX();
				int doorY = door.getY();
				door.setDirection(0);
				door.setLeftEdgeLocation(doorX);
				door.setRightEdgeLocation(doorX + 3);
				door.setOpenStatus(ActionCodes.ACTION_Close);
				door.isPassibleDoor(false);
				door.setPassable(1);
			}
			
			if (npc.getNpcId() == 78000226) {
				npc.setHomeX(npc.getX());
				npc.setHomeY(npc.getY());
				npc.setHeading(heading);
				npc.setPassable(0);
			}
			
			
			
			if (npc.getNpcId() == 50000221 || npc.getNpcId() == 50000270) {
				npc.setPassObject(false);
			}
			if (npc.getNpcId() >= 7800220 && npc.getNpcId() <= 7800223) {
				npc.setPassObject(false);
			}
			
			if (npc.getNpcId() == 120620) {
				for (L1PcInstance _pc : L1World.getInstance().getVisiblePlayer(npc)) {
					npc.onPerceive(_pc);
					S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_AxeWalk);
					_pc.sendPackets(gfx);
				}
			}
			
			if(npcId == 120620){ // 設定要移動的地圖
				L1FieldObjectInstance fobj = (L1FieldObjectInstance)npc;
				fobj.setMoveMapId(movemap);
			}
			
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 開始聊天
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return npc;
	}
	
	public static L1NpcInstance spawnnpc(int x, int y, short map, int npcId, int randomRange, int timeMillisToDelete, int movemap) {
		int heading = 5;
		if (npcId == 7000044 || npcId == 100325 || npcId == 100326 || (npcId == 100213 && x == 33094 && y == 33401)
				|| npcId == 100563 || npcId == 100646 || npcId == 100692)
			heading = 6;
		else if (npcId == 100430 || npcId == 100709 || npcId == 100710)
			heading = 4;
		return spawn4(x, y, map, heading, npcId, randomRange,timeMillisToDelete, movemap, false);
	}
	
	public static L1TrapInstance spawnTrap(int x, int y, short map, int npcId) {
		L1Trap trapTemp = null;
	
			trapTemp = TrapTable.getInstance().getTemplate(npcId);
			final Point rndPt = new Point();
			final int span = 0;
			rndPt.setX(0);
			rndPt.setY(0);
			final L1TrapInstance trap = new L1TrapInstance(IdFactory.getInstance().nextId(), trapTemp, new L1Location(x, y, map), rndPt, span);
			L1World.getInstance().addVisibleObject(trap);
			L1WorldTraps.getInstance().addTrap(trap);

		return trap;
	}
	public static void spawnAction(L1NpcInstance npc, int npcId){
		NpcInfoData info = NpcInfoTable.getInstance().getNpcInfo(npcId);
		if(info == null || info._spawnActionId == 0){
			return;
		}
		npc.setStatus(info._spawnActionId);
		S_CharVisualUpdate visual = new S_CharVisualUpdate(npc, 0);
		for(L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc)){
			npc.onPerceive(pc);
			pc.sendPackets(visual);
		}
		npc.setStatus(0);
		visual.clear();
		visual = null;
		npc.setParalysisTime(SprTable.getInstance().getActionSpeed(npc.getTempCharGfx(), info._spawnActionId) + 100);// 動作發生延遲
		
	}
	
}