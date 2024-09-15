package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;

import MJFX.UIAdapter.MJUIAdapter;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
import l1j.server.server.utils.SQLUtil;

public class IpTable {

	public static IpTable getInstance() {
		if (_instance == null) {
			_instance = new IpTable();
		}
		return _instance;
	}

	private IpTable() {
		if (!isInitialized) {
			_banip = new ArrayList<String>();
			getIpTable();
		}
	}

	public static void reload() {
		IpTable oldInstance = _instance;
		if (Config.Synchronization.Operation_Manager)
			MJUIAdapter.on_banip_clear();
		_instance = new IpTable();
		oldInstance._banip.clear();
	}

	public void banIp(String ip) {
		Connection con = null;
		PreparedStatement pstm = null;

		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO ban_ip SET ip=?");
			pstm.setString(1, ip);
			pstm.execute();
			_banip.add(ip);
			if (Config.Synchronization.Operation_Manager)
				MJUIAdapter.on_banip_append(ip);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public boolean isBannedIp(String s) {
		return _banip.contains(s);
	}

	public void getIpTable() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ban_ip");

			rs = pstm.executeQuery();

			while (rs.next()) {
				String s = rs.getString(1);
				_banip.add(s);
				if (Config.Synchronization.Operation_Manager)
					MJUIAdapter.on_banip_append(s);
			}

			isInitialized = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public boolean liftBanIp(String ip) {
		boolean ret = false;
		Connection con = null;
		PreparedStatement pstm = null;

		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM ban_ip WHERE ip=?");
			pstm.setString(1, ip);
			pstm.execute();
			ret = _banip.remove(ip);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return ret;
	}

	public void rangeBanIp(Integer[] octet) {
		Updator.batch("insert ignore into ban_ip SET ip=?", new BatchHandler() {
			@Override
			public void handle(PreparedStatement pstm, int callNumber) throws Exception {
				pstm.setString(1, String.format("%d.%d.%d.%d", octet[0], octet[1], octet[2], callNumber + 1));
			}
		}, 255);
	}

	public void rangeBanIp(String ip) {
		Connection c = null;
		PreparedStatement p = null;
		String ip1, ip2, ip3;
		StringTokenizer st = new StringTokenizer(ip, ".");

		ip1 = st.nextToken();
		ip2 = st.nextToken();
		ip3 = st.nextToken();
		try {
			c = L1DatabaseFactory.getInstance().getConnection();
			for (int i = 1; i <= 255; i++) {
				String temp = ip1 + "." + ip2 + "." + ip3 + "." + i;

				if (isBannedIp(temp)) {
					continue;
				}

				p = c.prepareStatement("INSERT INTO ban_ip SET ip=?");
				p.setString(1, temp);
				p.execute();
				_banip.add(temp);
				p.close();
			}
		} catch (Exception e) {// TODO: handle exception
			e.printStackTrace();
		} finally {
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
	}

	private static ArrayList<String> _banip;

	public static boolean isInitialized;

	private static IpTable _instance;

}
