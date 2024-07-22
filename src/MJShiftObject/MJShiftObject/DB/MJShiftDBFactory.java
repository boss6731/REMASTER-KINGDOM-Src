package MJShiftObject.DB;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import l1j.server.server.utils.LeakCheckedConnection;

import java.sql.Connection;
import java.sql.SQLException;


public class MJShiftDBFactory {
	private ComboPooledDataSource _source; // 資料庫連接池

	// 帶參數的構造函數，用於初始化資料庫連接池
	public MJShiftDBFactory(MJShiftDBArgs args) throws SQLException {
		try {
			this._source = new ComboPooledDataSource();
			this._source.setDriverClass(args.DRIVER_NAME); // 設置驅動類
			this._source.setJdbcUrl(args.URL); // 設置資料庫 URL
			this._source.setUser(args.USER_NAME); // 設置用戶名
			this._source.setPassword(args.PASSWORD); // 設置密碼
			this._source.setInitialPoolSize(args.MIN_POOL_SIZE); // 設置初始連接池大小
			this._source.setMinPoolSize(args.MIN_POOL_SIZE); // 設置最小連接池大小
			this._source.setMaxPoolSize(args.MAX_POOL_SIZE); // 設置最大連接池大小
			this._source.setAcquireIncrement(5); // 每次增量獲取的連接數
			this._source.setAcquireRetryAttempts(30); // 獲取連接的重試次數
			this._source.setAcquireRetryDelay(1000); // 獲取連接的重試延遲時間（毫秒）
			this._source.setIdleConnectionTestPeriod(60); // 空閒連接測試周期（秒）
			this._source.setPreferredTestQuery("SELECT 1"); // 首選測試查詢
			this._source.setTestConnectionOnCheckin(true); // 在檢入時測試連接
			this._source.setTestConnectionOnCheckout(false); // 在檢出時不測試連接
			this._source.getConnection().close(); // 測試連接是否有效
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("could not init DB connection:" + e);
		}
	}

	// 獲取資料庫連接池
	public ComboPooledDataSource getDataSource() {
		return this._source;
	}

	// 關閉資料庫連接池
	public void shutdown() {
		try {
			this._source.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			this._source = null;
		}
	}

	// 獲取資料庫連接
	public Connection get_connection() {
		Connection con = null;
		while (con == null) {
			try {
				con = this._source.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("L1DatabaseFactory: getConnection() failed, trying again " + e);
			}
		}
		return Config.Connection.EnableDatabaseResourceLeaksDetection ? LeakCheckedConnection.create(con) : con;
	}
}


