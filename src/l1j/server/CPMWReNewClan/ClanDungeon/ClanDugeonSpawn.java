package l1j.server.CPMWReNewClan.ClanDungeon;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class ClanDugeonSpawn {
	private static ClanDugeonSpawn _instance;

	public static ClanDugeonSpawn getInstance() {
		if (_instance == null) {
			_instance = new ClanDugeonSpawn();
		}
		return _instance;
	}

	private ClanDugeonSpawn() {
	}
	
	public L1DoorInstance fillSpawndoor(int x, int y, int mapid, int heading, int npcid) {
		L1DoorInstance door = null;
		L1Npc l1npc = NpcTable.getInstance().getTemplate(npcid);
		if (l1npc != null) {
			try {
				String s = l1npc.getImpl();
				Constructor<?> constructor = Class.forName("l1j.server.server.model.Instance." + s + "Instance")
						.getConstructors()[0];
				Object parameters[] = { l1npc };
				door = (L1DoorInstance) constructor.newInstance(parameters);
				door = (L1DoorInstance) constructor.newInstance(parameters);
				door.setId(IdFactory.getInstance().nextId());
				door.setDoorId(npcid);
				door.setCurrentSprite(l1npc.get_gfxid());
				door.setX(x);
				door.setY(y);
				door.setMap((short) mapid);
				door.setHomeX(door.getX());
				door.setHomeY(door.getY());
				if(door.getDoorId() == 120825){
					door.setDirection(1);
					door.setLeftEdgeLocation(32822);
					door.setRightEdgeLocation(32829);
				} else if(door.getDoorId() == 120826) {
					door.setDirection(1);
					door.setLeftEdgeLocation(32820);
					door.setRightEdgeLocation(32827);
				} else if(door.getDoorId() == 120827) {
					door.setDirection(0);
					door.setLeftEdgeLocation(32893);
					door.setRightEdgeLocation(32900);
				} else if(door.getDoorId() == 120828) {
					door.setDirection(0);
					door.setLeftEdgeLocation(32892);
					door.setRightEdgeLocation(32899);
				} else if(door.getDoorId() == 120829) {
					door.setDirection(1);
					door.setLeftEdgeLocation(32959);
					door.setRightEdgeLocation(32966);
				} else if(door.getDoorId() == 120830) {
					door.setDirection(1);
					door.setLeftEdgeLocation(32960);
					door.setRightEdgeLocation(32967);
				}
				door.setMaxHp(9999999);
				door.setCurrentHp(9999999);
				door.isPassibleDoor(false);
				door.setOpenStatus(ActionCodes.ACTION_Close);
				L1World.getInstance().storeObject(door);
				L1World.getInstance().addVisibleObject(door);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		l1npc = null;
		return door;
	}

	public FastTable<L1NpcInstance> fillSpawnTable(int mapid, int type, int round, int subround) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc l1npc = null;
		L1NpcInstance mon = null;
		FastTable<L1NpcInstance> list = null;
		list = new FastTable<L1NpcInstance>();
		L1ClanDugeon LCD = ClanDugeon.getInstance().getClanDugeon(mapid);
		LCD.stageunit = new ArrayList<L1NpcInstance>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM cpmw_clandugeon_spawn");
			rs = pstm.executeQuery();
			while (rs.next()) {
				if (type != rs.getInt("dungeon_type"))
					continue;
				
				if (round != rs.getInt("round"))
					continue;
				
				if (subround != rs.getInt("sub_round"))
					continue;

				int npcid = rs.getInt("npc_id");
				int count = rs.getInt("count");
				int locX = rs.getInt("locX");
				int locY = rs.getInt("locY");
				int heading = rs.getInt("heading");
				int randomRange = rs.getInt("randomRange");
				randomRange = randomRange > 20 ? 20 : randomRange;
				if(rs.getString("boss").equalsIgnoreCase("true")) {
					LCD.stageboss = new L1NpcInstance[count];
				}
				for (int i = 0; i < count; i++) {
					l1npc = NpcTable.getInstance().getTemplate(npcid);
					if (l1npc != null) {
						try {
							mon = NpcTable.getInstance().newNpcInstance(npcid);
							mon.setId(IdFactory.getInstance().nextId());
							mon.setMap((short)mapid);
							if (randomRange == 0) {
								mon.getLocation().set(locX,locY,mapid);
								mon.getLocation().forward(5);
							} else {
								int tryCount = 0;
								do {
									tryCount++;
									mon.setX(locX + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
									mon.setY(locY + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
									if (mon.getMap(). isInMap(mon.getLocation()) && mon.getMap(). isPassable(mon.getLocation())) {
										break;
									}
									Thread.sleep(1);
								} while (tryCount < 50);

								if (tryCount >= 50) {
									mon.getLocation().set(locX,locY,mapid);
									mon.getLocation().forward(5);
								}
							}
							if(type == 3 && rs.getInt("npc_id") == 120810) {
								mon.setHomeX(LCD.icegolemking.getX());
								mon.setHomeY(LCD.icegolemking.getY());
								mon.setHeading(LCD.icegolemking.getHeading());
								mon.setMap((short)mapid);
							} else {
								mon.setHomeX(mon.getX());
								mon.setHomeY(mon.getY());
								mon.setHeading(heading);
							}
							if (rs.getInt("npc_id") == 120810 || rs.getInt("npc_id") == 120821) {
								LCD.appearaction(mon);
							}
							L1World.getInstance().storeObject(mon);
							L1World.getInstance().addVisibleObject(mon);
							mon.getLight().turnOnOffLight();
							if(rs.getString("boss").equalsIgnoreCase("true")) {
								if(rs.getInt("npc_id") == 120810) {
									LCD.icegolemking.deleteMe();
								}
								LCD.stageboss[i] = mon;
							}
							if(type == 3 && rs.getString("boss").equalsIgnoreCase("false") && rs.getInt("npc_id") != 120805) {
								LCD.stageunit.add(mon);
							}
							if(type == 3 && rs.getInt("npc_id") == 120805) {
								LCD.icegolemking = mon;
							}
							list.add(mon);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return list;
	}

}
