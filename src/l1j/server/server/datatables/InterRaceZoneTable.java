package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.utils.SQLUtil;

public class InterRaceZoneTable {
	private static InterRaceZoneTable _instance;

	private final ArrayList<String> _Lockey;

	private InterRaceZoneTable() {
		_Lockey = new ArrayList<String>();
		load();
	}

	public static InterRaceZoneTable getInstance() {
		if (_instance == null) {
			_instance = new InterRaceZoneTable();
		}
		return _instance;
	}

	public boolean isLockey(String key) {
		return _Lockey.contains(key);
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM inter_race_zone");

			rs = pstm.executeQuery();

			while (rs.next()) {
				int srcX = rs.getInt("loc_X");
				int srcY = rs.getInt("loc_Y");
				int srcMapId = rs.getInt("loc_mapId");
				String key = new StringBuilder().append(srcMapId).append(srcX)
						.append(srcY).toString();
				_Lockey.add(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
//			System.out.println("[InterRaceZone] size : " + _Lockey.size());
		}
	}
	
	public static void do_update(int x, int y, int map){
		Updator.exec("insert into inter_race_zone set loc_X=?, loc_Y=?, loc_mapId=?", new Handler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						pstm.setInt(++idx, x);
						pstm.setInt(++idx, y);
						pstm.setInt(++idx, map);
					}
				});
	}

	public static void reload() {
		InterRaceZoneTable oldInstance = _instance;
		_instance = new InterRaceZoneTable();
		oldInstance._Lockey.clear();
	}
}
