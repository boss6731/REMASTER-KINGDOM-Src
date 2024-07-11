package l1j.server.MJWebServer.Dispatcher.Template.Coupon.POJO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class AppCouponInfo {
	public String _coupon;

	public static AppCouponInfo loadDatabaseCouponInfo() {
		AppCouponInfo appCash = null;
		
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM payment_info");
			rs = pstm.executeQuery();
			while (rs.next()) {
				appCash = new AppCouponInfo();
				appCash._coupon = rs.getString("code");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		
		
		return appCash;
	}
}
