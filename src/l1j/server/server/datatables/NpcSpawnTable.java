package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.server.model.L1Spawn;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class NpcSpawnTable {

	private static Logger _log = Logger.getLogger(NpcSpawnTable.class.getName());

	private static NpcSpawnTable _instance;

	private Map<Integer, L1Spawn> _spawntable = new HashMap<Integer, L1Spawn>();

	private int _highestId;

	public static NpcSpawnTable getInstance() {
		if (_instance == null) {
			_instance = new NpcSpawnTable("all");
		}
		return _instance;
	}

	public void reload() {
		NpcSpawnTable oldInstance = _instance;
		_instance = new NpcSpawnTable("L1Merchant");
		oldInstance._spawntable.clear();
	}

	private NpcSpawnTable(String type) {
		fillNpcSpawnTable(type);
	}

	private void fillNpcSpawnTable(String type) {

		int spawnCount = 0;

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_npc");
			rs = pstm.executeQuery();
			do {
				if (!rs.next()) {
					break;
				}

				if (Config.ServerAdSetting.GMSHOP == false) {
					int npcid = rs.getInt(1);
					if (npcid >= Config.ServerAdSetting.GMSHOPMINID && npcid <= Config.ServerAdSetting.GMSHOPMAXID) {
						continue;
					}
				}
				if (Config.ServerAdSetting.BASETOWN == false) {
					int npcid = rs.getInt(1);
					if (npcid >= Config.ServerAdSetting.BASETOWNMINID && npcid <= Config.ServerAdSetting.BASETOWNMAXID) {
						continue;
					}
				}

				int npcTemplateid = rs.getInt("npc_templateid");
				L1Npc l1npc = l1j.server.server.datatables.NpcTable.getInstance().getTemplate(npcTemplateid);
				if (!type.equalsIgnoreCase("all")) {
					if (l1npc != null) {
						if (!type.equalsIgnoreCase(l1npc.getImpl())) {
							continue;
						}
					}
				}
				L1Spawn l1spawn;
				if (l1npc == null) {
					_log.warning("ID為：" + npcTemplateid + " 的怪物數據在NPC表中缺失");
					System.out.println("ID為：" + npcTemplateid + " 的怪物數據在NPC表中缺失");
					l1spawn = null;
				} else {
					if (rs.getInt("count") == 0) {
						continue;
					}

					int loc_x = rs.getInt("locx");
					int loc_y = rs.getInt("locy");
					int loc_map = rs.getShort("mapid");
					if (l1npc.getImpl().equalsIgnoreCase("L1People")) {
						L1Map map = L1WorldMap.getInstance().getMap((short) loc_map);
						if (!map.isPassable(loc_x, loc_y)) {
							MJPoint pt = MJPoint.newInstance(loc_x, loc_y, 10, (short) loc_map, 50);
							loc_x = pt.x;
							loc_y = pt.y;
						}
					}

					l1spawn = new L1Spawn(l1npc);
					l1spawn.setId(rs.getInt("id"));
					l1spawn.setAmount(rs.getInt("count"));
					l1spawn.setLocX(loc_x);
					l1spawn.setLocY(loc_y);
					l1spawn.setRandomx(rs.getInt("randomx"));
					l1spawn.setRandomy(rs.getInt("randomy"));
					l1spawn.setLocX1(0);
					l1spawn.setLocY1(0);
					l1spawn.setLocX2(0);
					l1spawn.setLocY2(0);
					l1spawn.setHeading(rs.getInt("heading"));
					l1spawn.setMinRespawnDelay(rs.getInt("respawn_delay"));
					l1spawn.setMapId(rs.getShort("mapid"));
					l1spawn.setMovementDistance(rs.getInt("movement_distance"));
					l1spawn.setName(l1npc.get_name());
					l1spawn.init();
					spawnCount += l1spawn.getAmount();

					_spawntable.put(new Integer(l1spawn.getId()), l1spawn);
					if (l1spawn.getId() > _highestId) {
						_highestId = l1spawn.getId();
					}
				}
			} while (true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		_log.config("NPC配置列表加載了 " + _spawntable.size() + " 條目");
		_log.fine("總NPC數量: " + spawnCount + " 項");
	}

	public void storeSpawn(L1PcInstance pc, L1Npc npc) {
		Connection con = null;
		PreparedStatement pstm = null;

		try {
			int count = 1;
			String note = npc.get_name();
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO spawnlist_npc SET location=?,count=?,npc_templateid=?,locx=?,locy=?,heading=?,mapid=?");
			pstm.setString(1, note);
			pstm.setInt(2, count);
			pstm.setInt(3, npc.get_npcId());
			pstm.setInt(4, pc.getX());
			pstm.setInt(5, pc.getY());
			pstm.setInt(6, pc.getHeading());
			pstm.setInt(7, pc.getMapId());
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void removeSpawn(L1NpcInstance paramL1NpcInstance) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select id from spawnlist_npc where npc_templateid=? and mapid=? and locx=? and locy=?");
			pstm.setInt(1, paramL1NpcInstance.getNpcId());
			pstm.setInt(2, paramL1NpcInstance.getMapId());
			pstm.setInt(3, paramL1NpcInstance.getX());
			pstm.setInt(4, paramL1NpcInstance.getY());
			rs = pstm.executeQuery();
			if (!rs.next()) {
				return;
			}
			int i = rs.getInt("id");
			this._spawntable.remove(Integer.valueOf(i));
			SQLUtil.close(rs, pstm);
			pstm = con.prepareStatement("delete from spawnlist_npc where npc_templateid=? and mapid=? and locx=? and locy=?");
			pstm.setInt(1, paramL1NpcInstance.getNpcId());
			pstm.setInt(2, paramL1NpcInstance.getMapId());
			pstm.setInt(3, paramL1NpcInstance.getX());
			pstm.setInt(4, paramL1NpcInstance.getY());
			pstm.execute();
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public L1Spawn getTemplate(int i) {
		return _spawntable.get(i);
	}

	public void addNewSpawn(L1Spawn l1spawn) {
		_highestId++;
		l1spawn.setId(_highestId);
		_spawntable.put(l1spawn.getId(), l1spawn);
	}

}
