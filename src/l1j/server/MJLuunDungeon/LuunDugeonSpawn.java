package l1j.server.MJLuunDungeon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class LuunDugeonSpawn {
	private static LuunDugeonSpawn _instance;

	public static LuunDugeonSpawn getInstance() {
		if (_instance == null) {
			_instance = new LuunDugeonSpawn();
		}
		return _instance;
	}

	private LuunDugeonSpawn() {
	}

	public FastTable<L1NpcInstance> fillSpawnTable(int mapid, int round) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc l1npc = null;
		L1NpcInstance mon = null;
		FastTable<L1NpcInstance> list = null;
		list = new FastTable<L1NpcInstance>();
		L1LuunDugeon LCD = LuunDugeon.getInstance().getLuunDugeon(mapid);
		LCD.stageunit = new ArrayList<L1NpcInstance>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM mj_LuunDugeon_spawn");
			rs = pstm.executeQuery();
			while (rs.next()) {
				if (round != rs.getInt("round"))
					continue;

				int npcid = rs.getInt("npc_id");
				int count = rs.getInt("count");
				int locX = rs.getInt("locX");
				int locY = rs.getInt("locY");
				int heading = rs.getInt("heading");
				int randomRange = rs.getInt("randomRange");
				randomRange = randomRange > 20 ? 20 : randomRange;
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
							mon.setHomeX(mon.getX());
							mon.setHomeY(mon.getY());
							mon.setHeading(heading);
							
							if (rs.getInt("npc_id") == 120810 || rs.getInt("npc_id") == 120821) {
								LCD.appearaction(mon);
							}
							L1World.getInstance().storeObject(mon);
							L1World.getInstance().addVisibleObject(mon);
							mon.getLight().turnOnOffLight();
							if(rs.getString("boss").equalsIgnoreCase("true")) {
								LCD.stageBoss = mon;
							}
							if(rs.getString("boss").equalsIgnoreCase("false")) {
								LCD.stageunit.add(mon);
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
