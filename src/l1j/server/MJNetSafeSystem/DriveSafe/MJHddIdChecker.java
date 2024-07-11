package l1j.server.MJNetSafeSystem.DriveSafe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import l1j.server.MJTemplate.MJObjectWrapper;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.Account;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJHddIdChecker {
	public static final int DENIALS_TYPE_NONE = -1; // 乾淨的硬碟序號
	public static final int DENIALS_TYPE_INVALID_HDD_ID = 0; // 無法識別的硬碟序號
	public static final int DENIALS_TYPE_POWER_KICK = 1; // 因永久封禁被踢出
	public static final int DENIALS_TYPE_POWER_KICK_DUPLICATE = 2; // 因永久封禁重複登入的客戶端被踢出
}

	public static int get_denials(final String hdd_id) {
		if (l1j.server.MJTemplate.MJString.isNullOrEmpty(hdd_id))
			return DENIALS_TYPE_INVALID_HDD_ID;

		final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper<Integer>();
		wrapper.value = DENIALS_TYPE_INVALID_HDD_ID;
		Selector.exec("select reason from netsafe_denials_hdd where hdd_id=? LIMIT 1", new SelectorHandler() {

			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, hdd_id);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				if (rs.next())
					wrapper.value = rs.getInt("reason");
			}
		});
		return wrapper.value;
	}

	public static boolean is_denials(final String hdd_id) {
		if (l1j.server.MJTemplate.MJString.isNullOrEmpty(hdd_id))
			return false;

		MJObjectWrapper<Boolean> wrapper = new MJObjectWrapper<Boolean>();
		wrapper.value = false;
		Selector.exec("select * from netsafe_denials_hdd where hdd_id=? LIMIT 1", new SelectorHandler() {

			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, hdd_id);
			}

			@Override
			public void result(ResultSet rs) throws Exception {
				wrapper.value = rs.next();
			}
		});
		return wrapper.value;
	}

	public static void delete(String account) {
		final String hdd_id = MJAccountDriveInfo.get_account_to_drive(account);
		if (l1j.server.MJTemplate.MJString.isNullOrEmpty(hdd_id))
			return;

		Updator.exec("delete from netsafe_denials_hdd where hdd_id=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, hdd_id);
			}
		});
	}

	public static void update(final String hdd_id, final int reason) {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Updator.exec("insert ignore into netsafe_denials_hdd set hdd_id=?, reason=?, datetime=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setString(1, hdd_id);
				pstm.setInt(2, reason);
				pstm.setTimestamp(3, time);
			}
		});
	}

	public static String update_denials(final String account, final int reason) {
		if (l1j.server.MJTemplate.MJString.isNullOrEmpty(account))
			return "";

		String hdd_id = MJAccountDriveInfo.get_account_to_drive(account);
		if (l1j.server.MJTemplate.MJString.isNullOrEmpty(hdd_id))
			return "";

		update(hdd_id, reason);
		return hdd_id;
	}

	public static String update_denials(final L1PcInstance pc, final int reason) {
		return update_denials(pc.getAccountName(), reason);
	}

	public static String update_denials(final Account account, final int reason) {
		return update_denials(account.getName(), reason);
	}

	public static int numOfHddCount(String hddId) {
		final MJObjectWrapper<Integer> wrapper = new MJObjectWrapper<>();
		wrapper.value = 0;
		Selector.exec("select count(hdd_id) as numOfHddCount from netsafe_account_to_drive where hdd_id=?",
				new SelectorHandler() {

					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						pstm.setString(1, hddId);
					}

					@Override
					public void result(ResultSet rs) throws Exception {
						if (rs.next()) {
							wrapper.value = rs.getInt("numOfHddCount");
						}
					}
				});
		return wrapper.value;
	}

}
