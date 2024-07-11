package l1j.server;

import java.beans.PropertyVetoException;
import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJString;
import l1j.server.database.DBSource;
import l1j.server.database.DBSourceInfo;
import l1j.server.database.Shutdownable;

/**
 * 提供訪問數據庫的各種接口
 */
public class L1DatabaseFactory {
	private static L1DatabaseFactory _instance = null;

	/**
	 * @return L1DatabaseFactory
	 * @throws SQLException
	 * @throws PropertyVetoException
	 */
	public static L1DatabaseFactory getInstance() throws SQLException {
		if (_instance == null) {
			try {
				_instance = new L1DatabaseFactory();
			} catch (PropertyVetoException e) {
				RuntimeException re = new RuntimeException();
				re.addSuppressed(e);
				throw re;
			}
		}
		return _instance;
	}

	public static void reload() throws SQLException {
		L1DatabaseFactory old = _instance;
		try {
			_instance = new L1DatabaseFactory();
		} catch (PropertyVetoException e) {
			RuntimeException re = new RuntimeException();
			re.addSuppressed(e);
			throw re;
		} finally {
			if (old != null) {
				old.shutdown();
			}
		}
	}

	private DBSource source;

	private L1DatabaseFactory() throws SQLException, PropertyVetoException {
		DBSourceInfo sInfo = MJJsonUtil.fromFile("./config/database.json", DBSourceInfo.class);
		if (sInfo == null || (MJString.isNullOrEmpty(sInfo.hikariPath) && MJString.isNullOrEmpty(sInfo.c3pPath))) {
			throw new RuntimeException("./config/database.json 資訊有誤，請檢查。");
		}

		source = DBSource.fromJson(sInfo);
		if (source == null) {
			throw new RuntimeException("數據庫加載失敗。");
		}
	}

	public void shutdown() {
		shutdown(source);
		source = null;
	}

	/**
	 * 通過數據庫連接，獲取 connection 對象。
	 * 
	 * @return Connection connection 對象
	 * @throws SQLException
	 */
	public Connection getConnection() {
		try {
			return source == null ? null : source.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void closeAs(Object obj) {
		if (obj instanceof AutoCloseable) {
			close((AutoCloseable) obj);
		} else if (obj instanceof Closeable) {
			close((Closeable) obj);
		} else if (obj instanceof Shutdownable) {
			shutdown((Shutdownable) obj);
		}
	}

	public static void close(AutoCloseable close) {
		try {
			if (close != null) {
				close.close();
			}
		} catch (Exception e) {
		}
	}

	public static void close(AutoCloseable... closeables) {
		for (AutoCloseable closeable : closeables) {
			close(closeable);
		}
	}

	public static void close(Closeable close) {
		try {
			if (close != null) {
				close.close();
			}
		} catch (Exception e) {
		}
	}

	public static void close(Closeable... closeables) {
		for (Closeable closeable : closeables) {
			close(closeable);
		}
	}

	public static void shutdown(Shutdownable shutdownable) {
		try {
			if (shutdownable != null) {
				shutdownable.shutdown();
			}
		} catch (Exception e) {
		}
	}

	public static void shutdown(Shutdownable... shutdownables) {
		for (Shutdownable shutdownable : shutdownables) {
			shutdown(shutdownable);
		}
	}
}
