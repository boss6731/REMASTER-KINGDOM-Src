package l1j.server.GameSystem.Colosseum;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class ColosseumTable {
	private static Logger _log = Logger.getLogger(ColosseumTable.class.getName());

	private static ColosseumTable _instance = new ColosseumTable();

	private HashMap<Integer, L1Colosseum> _ub = new HashMap<Integer, L1Colosseum>();

	public static ColosseumTable getInstance() {
		return _instance;
	}

	private ColosseumTable() {
		loadTable();
	}

	public static void reload() {
		synchronized (_instance) {
			ColosseumTable oldInstance = _instance;
			_instance = new ColosseumTable();
			oldInstance._ub.clear();
		}
	}

	private void loadTable() {

		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ub_settings");
			rs = pstm.executeQuery();
			L1Colosseum ub = null;
			while (rs.next()) {

				ub = new L1Colosseum();
				ub.setUbId(rs.getInt("ub_id"));
				ub.setMapId(rs.getShort("ub_mapid"));
				ub.setLocX1(rs.getInt("ub_area_x1"));
				ub.setLocY1(rs.getInt("ub_area_y1"));
				ub.setLocX2(rs.getInt("ub_area_x2"));
				ub.setLocY2(rs.getInt("ub_area_y2"));
				ub.setMinLevel(rs.getInt("min_lvl"));
				ub.setMaxLevel(rs.getInt("max_lvl"));
				ub.setMaxPlayer(rs.getInt("max_player"));
				ub.setEnterRoyal(rs.getBoolean("enter_royal"));
				ub.setEnterKnight(rs.getBoolean("enter_knight"));
				ub.setEnterMage(rs.getBoolean("enter_mage"));
				ub.setEnterElf(rs.getBoolean("enter_elf"));
				ub.setEnterDarkelf(rs.getBoolean("enter_darkelf"));
				ub.setEnterDragonknight(rs.getBoolean("enter_dragonknight"));
				ub.setEnterBlackwizard(rs.getBoolean("enter_blackwizard"));
				ub.setEnterWarrior(rs.getBoolean("enter_warrior"));
				ub.setEnterFencer(rs.getBoolean("enter_Fencer"));
				ub.setEnterMale(rs.getBoolean("enter_male"));
				ub.setEnterFemale(rs.getBoolean("enter_female"));
				ub.setEnterFLancer(rs.getBoolean("enter_lancer"));
				ub.setUsePot(rs.getBoolean("use_pot"));
				ub.setHpr(rs.getInt("hpr_bonus"));
				ub.setMpr(rs.getInt("mpr_bonus"));
				ub.resetLoc();

				_ub.put(ub.getUbId(), ub);
			}
		} catch (SQLException e) {
			_log.warning("無法初始化 ubsettings：" + e);
			System.out.println("無法初始化 ubsettings：" + e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		// ub_managers load
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ub_managers");
			rs = pstm.executeQuery();
			L1Colosseum ub = null;
			while (rs.next()) {
				ub = getUb(rs.getInt("ub_id"));
				if (ub != null) {
					ub.addManager(rs.getInt("ub_manager_npc_id"));
				}
			}
		} catch (SQLException e) {
			_log.warning("無法初始化 ub_managers：" + e);
			System.out.println("無法初始化 ub_managers：" + e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		// ub_times load
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ub_times");
			rs = pstm.executeQuery();
			L1Colosseum ub = null;
			while (rs.next()) {
				ub = getUb(rs.getInt("ub_id"));
				if (ub != null) {
					ub.addUbTime(rs.getInt("ub_time"));
				}
			}
		} catch (SQLException e) {
			_log.warning("無法初始化 ub_times：" + e);
			System.out.println("無法初始化 ub_times：" + e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		_log.config("UB列表 " + _ub.size() + "槍械裝填");
	}

	public L1Colosseum getUb(int ubId) {
		return _ub.get(ubId);
	}

	public Collection<L1Colosseum> getAllUb() {
		return Collections.unmodifiableCollection(_ub.values());
	}

	public L1Colosseum getUbForNpcId(int npcId) {
		for (L1Colosseum ub : _ub.values()) {
			if (ub.containsManager(npcId)) {
				return ub;
			}
		}
		return null;
	}

	/**
	 * 傳回指定 UBID 的最大模式數。
	 *
	 * @param ubId
	 *             UBID 調查。
	 * @return 最大模式數。
	 */
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

	/**
	 * DB UB排名報名
	 */

	private void updateUbScore(final int ubId, final L1PcInstance pc, final int score) {
		Updator.exec("UPDATE ub_rank SET score=? WHERE ub_id=? AND char_name=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, score);
				pstm.setInt(2, ubId);
				pstm.setString(3, pc.getName());
			}
		});
	}

	private void insertUbScore(final int ubId, final L1PcInstance pc) {
		Updator.exec("INSERT INTO ub_rank SET ub_id=?, char_name=?, score=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, ubId);
				pstm.setString(2, pc.getName());
				pstm.setInt(3, pc.getUbScore());
			}
		});
	}

	public void writeUbScore(final int ubId, final L1PcInstance pc) {
		Selector.exec("SELECT * FROM ub_rank WHERE ub_id=? AND char_name=?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, ubId);
				pstm.setString(2, pc.getName());
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if (rs.next()) {
					final int score = rs.getInt("score") + pc.getUbScore();
					updateUbScore(ubId, pc, score);
				} else {
					insertUbScore(ubId, pc);
				}
			}
		});
	}
}