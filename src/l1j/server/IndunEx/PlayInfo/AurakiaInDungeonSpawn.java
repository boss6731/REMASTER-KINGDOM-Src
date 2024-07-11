package l1j.server.IndunEx.PlayInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class AurakiaInDungeonSpawn {
	private static AurakiaInDungeonSpawn _instance;

	public static AurakiaInDungeonSpawn getInstance() {
		if (_instance == null) {
			_instance = new AurakiaInDungeonSpawn();
		}
		return _instance;
	}

	private AurakiaInDungeonSpawn() {
	}

	public FastTable<L1NpcInstance> fillSpawnTable(int mapid, int type) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc l1npc = null;
		L1NpcInstance field = null;
		FastTable<L1NpcInstance> list = null;
		list = new FastTable<L1NpcInstance>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_aurakia_indun");
			rs = pstm.executeQuery();
			while (rs.next()) {
				if (type != rs.getInt("type"))
					continue;
				l1npc = NpcTable.getInstance().getTemplate(rs.getInt("npc_id"));
				if (l1npc != null) {
					try {
						field = NpcTable.getInstance().newNpcInstance(rs.getInt("npc_id"));
						field.setId(IdFactory.getInstance().nextId());
						field.setX(rs.getInt("locx"));
						field.setY(rs.getInt("locy"));
						field.setMap((short) mapid);
						field.setMap(L1WorldMap.getInstance().getMap((short) mapid));
						field.setHomeX(field.getX());
						field.setHomeY(field.getY());
						field.setHeading(rs.getInt("heading"));
						field.setLightSize(l1npc.getLightSize());
						field.getLight().turnOnOffLight();
						field.setSpawnLocation(rs.getString("location"));
						L1World.getInstance().storeObject(field);
						L1World.getInstance().addVisibleObject(field);
						list.add(field);
					} catch (Exception e) {
						e.printStackTrace();
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

	public FastTable<L1NpcInstance> fillSpawnTable(int mapid, int attr, int type, L1NpcInstance npc, boolean countB) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc l1npc = null;
		L1NpcInstance field = null;
		FastTable<L1NpcInstance> list = null;
		list = new FastTable<L1NpcInstance>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_aurakia_indun");
			rs = pstm.executeQuery();
			while (rs.next()) {
				if (type != rs.getInt("type"))
					continue;
				
				if (attr != rs.getInt("attr"))
					continue;

				int npcid = rs.getInt("npc_id");
				int count = rs.getInt("count");
				if (countB)
					count *= 2;
				
				int locX = rs.getInt("locX");
				int locY = rs.getInt("locY");
				int heading = rs.getInt("heading");
				String location = rs.getString("location");
				for (int i = 0; i < count; i++) {
					l1npc = NpcTable.getInstance().getTemplate(npcid);
					if (l1npc != null) {
						try {
							field = NpcTable.getInstance().newNpcInstance(npcid);
							field.setId(IdFactory.getInstance().nextId());
							field.setX(locX);
							field.setY(locY);
							field.setMap((short) mapid);
							field.setMap(L1WorldMap.getInstance().getMap(
									(short) mapid));
							field.setHomeX(field.getX());
							field.setHomeY(field.getY());
							field.getMoveState().setHeading(heading);
							field.setLightSize(l1npc.getLightSize());
							field.getLight().turnOnOffLight();
							field.setSpawnLocation(location);
							L1World.getInstance().storeObject(field);
							L1World.getInstance().addVisibleObject(field);
							if (field instanceof L1MonsterInstance) {
								field.onNpcAI();
								((L1MonsterInstance) field).attackTarget(npc);
							}
							list.add(field);
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
