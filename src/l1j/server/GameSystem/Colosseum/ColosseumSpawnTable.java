package l1j.server.GameSystem.Colosseum;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class ColosseumSpawnTable {
	private static Logger _log = Logger.getLogger(ColosseumSpawnTable.class.getName());

	private static ColosseumSpawnTable _instance;

	private HashMap<Integer, L1ColosseumSpawn> _spawnTable = new HashMap<Integer, L1ColosseumSpawn>();;

	public static ColosseumSpawnTable getInstance() {
		if (_instance == null) {
			_instance = new ColosseumSpawnTable();
		}
		return _instance;
	}

	public static void reload() {
		ColosseumSpawnTable old = _instance;
		_instance = new ColosseumSpawnTable();
		if (old != null) {
			old.dispose();
			old = null;
		}
	}

	public void dispose() {
		if (_spawnTable != null) {
			_spawnTable.clear();
			_spawnTable = null;
		}
	}

	private ColosseumSpawnTable() {
		loadSpawnTable();
	}

	private void loadSpawnTable() {

		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_ub");
			rs = pstm.executeQuery();
			L1Npc npcTemp = null;
			L1ColosseumSpawn spawnDat = null;
			while (rs.next()) {
				npcTemp = NpcTable.getInstance().getTemplate(rs.getInt(6));
				if (npcTemp == null) {
					continue;
				}

				spawnDat = new L1ColosseumSpawn();
				spawnDat.setId(rs.getInt(1));
				spawnDat.setUbId(rs.getInt(2));
				spawnDat.setPattern(rs.getInt(3));
				spawnDat.setGroup(rs.getInt(4));
				spawnDat.setName(npcTemp.get_name());
				if (rs.getInt(10) != 0) {
					if (MJRnd.next(100) < (rs.getInt(11) <= 0 ? 0 : rs.getInt(11))) {
						spawnDat.setNpcTemplateId(rs.getInt(6));
					} else {
						spawnDat.setNpcTemplateId(rs.getInt(10));
					}
				} else {
					spawnDat.setNpcTemplateId(rs.getInt(6));
				}
				spawnDat.setAmount(rs.getInt(7));
				spawnDat.setSpawnDelay(rs.getInt(8));
				spawnDat.setSealCount(rs.getInt(9));
				spawnDat.set_message(rs.getString(12));

				_spawnTable.put(spawnDat.getId(), spawnDat);
			}
		} catch (SQLException e) {
			// problem with initializing spawn, go to next one
			_log.warning("無法初始化生成：" + e);
			System.out.println("無法初始化生成：" + e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_log.config("UBmonster 配置清單 " + _spawnTable.size() + "黃金槍騎");
	}

	public L1ColosseumSpawn getSpawn(int spawnId) {
		return _spawnTable.get(spawnId);
	}

	public int getMaxPattern(int ubId) {
		int n = 0;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT MAX(pattern) FROM spawnlist_ub WHERE ub_id=?");
			pstm.setInt(1, ubId);
			rs = pstm.executeQuery();
			if (rs.next()) {
				n = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return n;
	}

	public L1ColosseumPattern getPattern(int ubId, int patternNumer) {
		L1ColosseumPattern pattern = new L1ColosseumPattern();
		for (L1ColosseumSpawn spawn : _spawnTable.values()) {
			if (spawn.getUbId() == ubId && spawn.getPattern() == patternNumer) {
				pattern.addSpawn(spawn.getGroup(), spawn);
			}
		}
		pattern.freeze();

		return pattern;
	}
}
