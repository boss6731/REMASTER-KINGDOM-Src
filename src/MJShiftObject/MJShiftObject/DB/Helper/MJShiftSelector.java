package MJShiftObject.DB.Helper;

import MJShiftObject.MJShiftObjectManager;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.utils.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MJShiftSelector extends Selector {

	// 靜態方法，用於執行查詢
	public static void exec(String query, SelectorHandler handler) {
		(new MJShiftSelector()).execute(query, handler);
	}

	// 執行查詢並處理結果
	public int execute(String query, Handler handler) {
		if (!(handler instanceof SelectorHandler)) {
			throw new IllegalArgumentException("handler is not SelectorHandler...!");
		}
		SelectorHandler sHandler = (SelectorHandler) handler;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = MJShiftObjectManager.getInstance().get_connection(); // 獲取資料庫連接
			pstm = con.prepareStatement(query); // 準備查詢語句
			sHandler.handle(pstm); // 處理查詢語句的參數
			rs = pstm.executeQuery(); // 執行查詢
			sHandler.result(rs); // 處理查詢結果
			return 1;
		} catch (Exception e) {
			e.printStackTrace(); // 輸出異常信息
		} finally {
			SQLUtil.close(rs, pstm, con); // 關閉資料庫資源
		}
		return 0; // 返回0表示操作失敗
	}
}


