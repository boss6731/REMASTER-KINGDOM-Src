package l1j.server.database;

import java.beans.PropertyVetoException;
import java.sql.Connection;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJString;

/**
 * <b>管理DataSource的類別</b>
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
public abstract class DBSource implements Shutdownable{
	private static final long CONNECTION_SLEEP_MILLIS = 10L;




	/**
	 * 透過DBSourceInfo的Json設定文件來創建數據源。
	 * <p>如果hikariPath和c3pPath都為空，框架將不會加載數據庫設置。</p>
	 * <p>如果同時設置hikariPath和c3pPath，則優先加載hikaricp。</p>
	 * @param sInfo {@link DBSourceInfo} 數據源信息
	 * @param executor {@link AsyncExecutor} 線程池
	 * @return {@link DBSource}
	 * @see HikariSource
	 * @see C3PSource
	 **/
	public static DBSource fromJson(DBSourceInfo sInfo) throws PropertyVetoException{
		return MJString.isNullOrEmpty(sInfo.hikariPath) ? 
				fromC3PJson(sInfo.c3pPath) :
				fromHikariJson(sInfo.hikariPath);
	}




	/**
	 * 通過Hikari Json配置文件創建HikariSource。
	 * @param jsonPath Hikari Json配置文件的路徑
	 * @param executor {@link AsyncExecutor}
	 * @param useReport 是否編寫sql報告。如果為true，則編寫；如果為false，則不編寫
	 * @return {@link HikariSource}
	 * @see HikariSource
	 **/
	public static DBSource fromHikariJson(String jsonPath){
		HikariSourceInfo sInfo = MJJsonUtil.fromFile(jsonPath, HikariSourceInfo.class);
		return new HikariSource(sInfo).checkConnection();
	}





	/**
	 * 通過c3p0 Json配置文件創建C3PSourceInfo。
	 * @param jsonPath c3p0 Json配置文件的路徑
	 * @param executor {@link AsyncExecutor}
	 * @param useReport 是否編寫sql報告。如果為true，則編寫；如果為false，則不編寫
	 * @return {@link C3PSource}
	 * @see C3PSource
	 **/
	public static DBSource fromC3PJson(String jsonPath) throws PropertyVetoException{
		C3PSourceInfo sInfo = MJJsonUtil.fromFile(jsonPath, C3PSourceInfo.class);
		return new C3PSource(sInfo).checkConnection();
	}


	
	
	
	private DataSource source;
	protected DBSource(DataSource source){
		this.source = source;
	}


	
	
	protected DBSource checkConnection(){
		try{
			Connection connection = source.getConnection();
			boolean success = connection != null;
			connection.close();
			if(!success){
				throw new RuntimeException(String.format("datasource load and getConnection fail %s", this));
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(String.format("datasource load fail! %s", this));
		}
		return this;
	}


	
	
	
	Connection getConnection(String sql) throws InterruptedException{
		do{
			if(source == null)
				return null;
			
			try{
				Connection connection = source.getConnection();
				if(connection != null){
					return connection;
				}
				System.out.println(String.format("getConnection fail...!\r\n%s", sql));
				new Throwable().printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
			Thread.sleep(CONNECTION_SLEEP_MILLIS);
		}while(true);
	}





	/**
	 * <b>返回不單獨管理的連接。</b>
	 * <p>為了提高自由度，設置為public。</p>
	 * @return {@link Connection}
	 * @see Connection
	 **/
	public Connection getConnection() throws InterruptedException{
		do{
			if(source == null)
				return null;
			
			try{
				Connection connection = source.getConnection();
				if(connection != null){
					return connection;
				}
				System.out.println(String.format("getConnection fail...!"));
				new Throwable().printStackTrace();
			}catch(Exception e){
				e.printStackTrace();
			}
			Thread.sleep(CONNECTION_SLEEP_MILLIS);
		}while(true);
	}



	/**
	 * 關閉當前的DataSource。
	 **/
	@Override
	public void shutdown() {
		if(source instanceof ComboPooledDataSource) {
			((ComboPooledDataSource)source).close();
		}else {
			L1DatabaseFactory.closeAs(source);			
		}
		source = null;
	}
	
}

