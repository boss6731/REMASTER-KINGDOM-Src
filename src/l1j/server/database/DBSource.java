package l1j.server.database;

import java.beans.PropertyVetoException;
import java.sql.Connection;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJString;

/**
 * <b>DataSource用於管理...的類別</b>
 * 
 * @author mjsoft
 * @see DataSource
 * @see AsyncExecutor
 * @see SQLCounter
 * @see SQLExecutor
 * @see SQLReporter
 * @see SafeStatement
 * @see HikariSource
 * @see HikariSourceInfo
 * @see C3PSource
 * @see C3PSourceInfo
 **/
public abstract class DBSource implements Shutdownable {
	private static final long CONNECTION_SLEEP_MILLIS = 10L;

	/**
	 * DBSourceInfo Json使用設定檔建​​立資料來源.
	 * <p>
	 * hikariPath, c3pPath 如果兩者都為空，框架將不會載入資料庫設定。
	 * </p>
	 * <p>
	 * hikariPath, c3pPath 如果兩者都設置，則首先載入 hikaricp。.
	 * </p>
	 * 
	 * @param sInfo    {@link DBSourceInfo} 資料來源資訊
	 * @param executor {@link AsyncExecutor} 執行緒池
	 * @return {@link DBSource}
	 * @see HikariSource
	 * @see C3PSource
	 **/
	public static DBSource fromJson(DBSourceInfo sInfo) throws PropertyVetoException {
		return MJString.isNullOrEmpty(sInfo.hikariPath) ? fromC3PJson(sInfo.c3pPath) : fromHikariJson(sInfo.hikariPath);
	}

	/**
	 * 使用 Hikari Json 設定檔建立 HikariSource。
	 * 
	 * @param jsonPath  Hikari Json 設定檔的路徑
	 * @param 執行器       {@link AsyncExecutor}
	 * @param useReport 是否建立sql報表。如果為真，則寫，如果為假，則不寫。
	 * @return {@link HikariSource}
	 * @see HikariSource
	 **/
	public static DBSource fromHikariJson(String jsonPath) {
		HikariSourceInfo sInfo = MJJsonUtil.fromFile(jsonPath, HikariSourceInfo.class);
		return new HikariSource(sInfo).checkConnection();
	}

	/**
	 * 使用c3p0 Json設定檔建立C3PSourceInfo。
	 * 
	 * @param jsonPath  Hikari Json 設定檔的路徑
	 * @param 執行器       {@link AsyncExecutor}
	 * @param useReport 是否建立sql報表。如果為真，則寫，如果為假，則不寫。
	 * @return {@link C3PSource}
	 * @see C3PSource
	 **/
	public static DBSource fromC3PJson(String jsonPath) throws PropertyVetoException {
		C3PSourceInfo sInfo = MJJsonUtil.fromFile(jsonPath, C3PSourceInfo.class);
		return new C3PSource(sInfo).checkConnection();
	}

	private DataSource source;

	protected DBSource(DataSource source) {
		this.source = source;
	}

	protected DBSource checkConnection() {
		try {
			Connection connection = source.getConnection();
			boolean success = connection != null;
			connection.close();
			if (!success) {
				throw new RuntimeException(String.format("資料來源加載和獲取連接失敗 %s", this));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(String.format("資料來源加載失敗！%s", this));
		}
		return this;
	}

	Connection getConnection(String sql) throws InterruptedException {
		do {
			if (source == null)
				return null;

			try {
				Connection connection = source.getConnection();
				if (connection != null) {
					return connection;
				}
				System.out.println(String.format("取得連線失敗...！\r\n%s", sql));
				new Throwable().printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(CONNECTION_SLEEP_MILLIS);
		} while (true);
	}

	/**
	 * <b>傳回未單獨管理的連線。</b>
	 * <p>
	 * 增加自由度，公開.
	 * </p>
	 * 
	 * @return {@link Connection}
	 * @see Connection
	 **/
	public Connection getConnection() throws InterruptedException {
		do {
			if (source == null)
				return null;

			try {
				Connection connection = source.getConnection();
				if (connection != null) {
					return connection;
				}
				System.out.println(String.format("取得連線失敗...！"));
				new Throwable().printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(CONNECTION_SLEEP_MILLIS);
		} while (true);
	}

	/**
	 * 終止目前資料來源.
	 **/
	@Override
	public void shutdown() {
		if (source instanceof ComboPooledDataSource) {
			((ComboPooledDataSource) source).close();
		} else {
			L1DatabaseFactory.closeAs(source);
		}
		source = null;
	}

}
