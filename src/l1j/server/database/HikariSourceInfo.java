package l1j.server.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import l1j.server.MJTemplate.MJString;

/**
 * <b>使用 Hikari Pool 的選項模型</b>
 * 
 * @see HikariSource
 * @see HikariDataSource
 **/
public class HikariSourceInfo {
	private static final int DEFAULT_POOL_SIZE = 64;

	public static HikariDataSource defaultSource = null;

	public String jdbcURL;
	public String userName;
	public String password;
	public boolean autoCommit;
	public String connectionTestQuery;
	public long connectionTimeoutMillis;
	public long validationTimeoutMillis;
	public long idleTimeoutMillis;
	public long maxLifeTimeMillis;
	public int maximumPoolSize;
	public int minimumIdle;
	public int leakDetectionThresholdMillis;
	public String poolName;

	public boolean useServerPrepStmts;
	public boolean cachePrepStmts;
	public int prepStmtCacheSize;
	public int prepStmtCacheSqlLimit;
	public boolean useLocalSessionState;
	public boolean rewriteBatchedStatements;
	public boolean cacheResultSetMetadata;
	public boolean cacheServerConfiguration;
	public boolean elideSetAutoCommits;
	public boolean maintainTimeStats;

	public HikariSourceInfo() {
		jdbcURL = MJString.EmptyString;
		userName = MJString.EmptyString;
		password = MJString.EmptyString;
		autoCommit = true;
		connectionTestQuery = MJString.EmptyString;
		connectionTimeoutMillis = 30000L;
		validationTimeoutMillis = 5000L;
		idleTimeoutMillis = 600000L;
		maxLifeTimeMillis = 1800000L;
		maximumPoolSize = DEFAULT_POOL_SIZE;
		minimumIdle = DEFAULT_POOL_SIZE;
		leakDetectionThresholdMillis = 0;
		poolName = MJString.EmptyString;

		useServerPrepStmts = true;
		cachePrepStmts = true;
		prepStmtCacheSize = 250;
		prepStmtCacheSqlLimit = 2048;

		useLocalSessionState = true;
		rewriteBatchedStatements = true;
		cacheResultSetMetadata = true;
		cacheServerConfiguration = true;
		elideSetAutoCommits = true;
		maintainTimeStats = false;
	}

	public void onConfigChanged() {
		if (defaultSource == null) {
			return;
		}
		defaultSource.getHikariConfigMXBean().setConnectionTimeout(connectionTimeoutMillis);
		defaultSource.getHikariConfigMXBean().setValidationTimeout(validationTimeoutMillis);
		defaultSource.getHikariConfigMXBean().setIdleTimeout(idleTimeoutMillis);
		defaultSource.getHikariConfigMXBean().setMaxLifetime(maxLifeTimeMillis);
		defaultSource.getHikariConfigMXBean().setMaximumPoolSize(maximumPoolSize);
		defaultSource.getHikariConfigMXBean().setMinimumIdle(minimumIdle);
		defaultSource.getHikariConfigMXBean().setLeakDetectionThreshold(leakDetectionThresholdMillis);
	}

	HikariDataSource toHikari() {
		HikariConfig hc = new HikariConfig();
		hc.setJdbcUrl(jdbcURL);
		hc.setUsername(userName);
		hc.setPassword(password);
		hc.setAutoCommit(autoCommit);
		if (!MJString.isNullOrEmpty(connectionTestQuery)) {
			hc.setConnectionTestQuery(connectionTestQuery);
		}
		hc.setConnectionTimeout(connectionTimeoutMillis);
		hc.setValidationTimeout(validationTimeoutMillis);
		hc.setIdleTimeout(idleTimeoutMillis);
		hc.setMaxLifetime(maxLifeTimeMillis);
		hc.setMaximumPoolSize(maximumPoolSize);
		hc.setMinimumIdle(minimumIdle);
		hc.setLeakDetectionThreshold(leakDetectionThresholdMillis);
		hc.setPoolName(poolName);

		hc.addDataSourceProperty("useServerPrepStmts", useServerPrepStmts);
		hc.addDataSourceProperty("cachePrepStmts", cachePrepStmts);
		hc.addDataSourceProperty("prepStmtCacheSize", prepStmtCacheSize);
		hc.addDataSourceProperty("prepStmtCacheSqlLimit", prepStmtCacheSqlLimit);
		hc.addDataSourceProperty("useLocalSessionState", useLocalSessionState);
		hc.addDataSourceProperty("rewriteBatchedStatements", rewriteBatchedStatements);
		hc.addDataSourceProperty("cacheResultSetMetadata", cacheResultSetMetadata);
		hc.addDataSourceProperty("cacheServerConfiguration", cacheServerConfiguration);
		hc.addDataSourceProperty("elideSetAutoCommits", elideSetAutoCommits);
		hc.addDataSourceProperty("maintainTimeStats", maintainTimeStats);

		defaultSource = new HikariDataSource(hc);
		return defaultSource;
	}
}