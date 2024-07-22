package MJShiftObject.DB.Helper;

import MJShiftObject.MJShiftObjectManager;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.utils.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MJShiftUpdator extends Updator {
	public static final int MAXIMUM_BATCH_COUNT = 1000; // 最大批量計數

	// 靜態方法，用於清空表
	public static void truncate(String table_name) {
		exec(String.format("truncate table %s", table_name), null);
	}

	// 靜態方法，用於執行更新操作
	public static int exec(String query, Handler handler) {
		return (new MJShiftUpdator()).execute(query, handler);
	}

	// 靜態方法，用於批量處理
	public static void batch(String query, BatchHandler handler, int loop_count) {
		(new MJShiftUpdator()).execBatch(query, handler, loop_count);
	}

	// 執行更新操作
	public int execute(String query, Handler handler) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = MJShiftObjectManager.getInstance().get_connection(); // 獲取資料庫連接
			pstm = con.prepareStatement(query); // 準備查詢語句
			if (handler != null)
				handler.handle(pstm); // 處理查詢語句的參數
			return pstm.executeUpdate(); // 執行更新操作
		} catch (Exception e) {
			e.printStackTrace(); // 打印異常信息
		} finally {
			SQLUtil.close(pstm, con); // 關閉資料庫資源
		}
		return 0; // 返回0表示操作失敗
	}

	// 執行批量更新操作
	public void execBatch(String query, Handler handler, int loop_count) {
		if (!(handler instanceof BatchHandler)) {
			throw new IllegalArgumentException("handler is not BatchHandler...!");
		}
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = MJShiftObjectManager.getInstance().get_connection(); // 獲取資料庫連接
			con.setAutoCommit(false); // 關閉自動提交
			pstm = con.prepareStatement(query); // 準備查詢語句
			for (int i = 0; i < loop_count; i++) {
				handler.handle(pstm); // 處理查詢語句的參數
				pstm.addBatch(); // 將操作添加到批量中
				pstm.clearParameters(); // 清除參數

				if (i > 0 && i % MAXIMUM_BATCH_COUNT == 0) {
					pstm.executeBatch(); // 當達到最大批量計數時，執行批量操作
					con.commit(); // 提交變更
					pstm.clearBatch(); // 清除批量
				}
			}
			pstm.executeBatch(); // 執行剩餘的批量操作
			con.commit(); // 提交變更
			pstm.clearBatch(); // 清除批量
		} catch (Exception e) {
			try {
				if (con != null) {
					con.rollback(); // 回滾變更
				}
			} catch (Exception e1) {
				e1.printStackTrace(); // 打印回滾異常信息
			}
			e.printStackTrace(); // 打印異常信息
		} finally {
			try {
				if (con != null) {
					con.setAutoCommit(true); // 恢復自動提交
				}
			} catch (Exception e) {
				e.printStackTrace(); // 打印異常信息
			}
			SQLUtil.close(pstm, con); // 關閉資料庫資源
		}
	}
}


