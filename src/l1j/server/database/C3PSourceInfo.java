package l1j.server.database;

import java.beans.PropertyVetoException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import l1j.server.MJTemplate.MJString;

/**
 * <b>使用c3p0的選項模型</b>
 * 
 * @see C3PSource
 * @see ComboPooledDataSource
 **/
public class C3PSourceInfo {
	public String driverClass;
	public String jdbcURL;
	public String userName;
	public String password;

	public int initialPoolSize;
	public int minPoolSize;
	public int maxPoolSize;
	public int acquireIncrement;
	public int acquireRetryAttempts;
	public int acquireRetryDelay;
	public int idleConnectionTestPeriod;
	public String preferredTestQuery;
	public int maxIdleTime;
	public boolean testConnectionOnCheckin;
	public boolean testConnectionOnCheckout;
	public boolean autoCommitOnClose;
	public int checkoutTimeout;
	public int maxStatements;
	public int maxStatementsPerConnection;

	public C3PSourceInfo() {
		driverClass = "com.mysql.cj.jdbc.Driver";
		jdbcURL = MJString.EmptyString;
		userName = MJString.EmptyString;
		password = MJString.EmptyString;

		initialPoolSize = 32;
		minPoolSize = 32;
		maxPoolSize = 126;
		acquireIncrement = 5;
		acquireRetryAttempts = 30;
		acquireRetryDelay = 500;
		idleConnectionTestPeriod = 300;
		preferredTestQuery = MJString.EmptyString;
		maxIdleTime = 0;
		testConnectionOnCheckin = true;
		testConnectionOnCheckout = false;
		autoCommitOnClose = true;
		checkoutTimeout = 30000;
		maxStatements = 256;
		maxStatementsPerConnection = 16;
	}

	ComboPooledDataSource toComboPooledDataSource() throws PropertyVetoException {
		ComboPooledDataSource source = new ComboPooledDataSource();
		source.setDriverClass(driverClass);
		source.setJdbcUrl(jdbcURL);
		source.setUser(userName);
		source.setPassword(password);
		source.setInitialPoolSize(initialPoolSize);
		source.setMinPoolSize(minPoolSize);
		source.setMaxPoolSize(maxPoolSize);
		source.setAcquireIncrement(acquireIncrement);
		source.setAcquireRetryAttempts(acquireRetryAttempts);
		source.setAcquireRetryDelay(acquireRetryDelay);
		source.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
		if (!MJString.isNullOrEmpty(preferredTestQuery)) {
			source.setPreferredTestQuery(preferredTestQuery);
		}
		source.setMaxIdleTime(maxIdleTime);
		source.setTestConnectionOnCheckin(testConnectionOnCheckin);
		source.setTestConnectionOnCheckout(testConnectionOnCheckout);
		source.setAutoCommitOnClose(autoCommitOnClose);
		source.setCheckoutTimeout(checkoutTimeout);
		source.setMaxStatements(maxStatements);
		source.setMaxStatementsPerConnection(maxStatementsPerConnection);
		source.setBreakAfterAcquireFailure(false);
		return source;
	}
}
