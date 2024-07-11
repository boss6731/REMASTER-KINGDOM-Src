package l1j.server.MJNetSafeSystem.DriveSafe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJNetServer.MJNetServerLoadManager;
import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.utils.SQLUtil;

public class MJAccountDriveInfo {
	public static String get_account_to_drive(final String account) {
		final MJObjectWrapper<String> wrapper = new MJObjectWrapper<String>();
		wrapper.value = "";
		Selector.exec("select hdd_id from netsafe_account_to_drive where account=?", new SelectorHandler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, account);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next()) {
					wrapper.value = rs.getString("hdd_id");
				}
			}
		});
		return wrapper.value;
	}
	
	public static void update_account_to_drive(final String account, final String hdd_id) {
		int hdd_count = getAccountHddCount(hdd_id);
		if(l1j.server.MJTemplate.MJString.isNullOrEmpty(hdd_id))
			return;
		if (hdd_count >= MJNetServerLoadManager.DESKTOP_ADDRESS2ACCOUNT)
			return;
		
		Updator.exec("insert into netsafe_account_to_drive set account=?, hdd_id=? on duplicate key update hdd_id=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, account);
				pstm.setString(2, hdd_id);
				pstm.setString(3, hdd_id);
			}
		});
	}
	
	private static int getAccountHddCount(String hdd) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT count(hdd_id) as cnt FROM netsafe_account_to_drive WHERE hdd_id=? ");
			pstm.setString(1, hdd);
			rs = pstm.executeQuery();

			if (rs.next())
				return rs.getInt("cnt");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return 0;
	}
}
