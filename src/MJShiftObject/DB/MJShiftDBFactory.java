     package MJShiftObject.DB;

     import com.mchange.v2.c3p0.ComboPooledDataSource;
     import java.sql.Connection;
     import java.sql.SQLException;
     import l1j.server.Config;
     import l1j.server.server.utils.LeakCheckedConnection;

     public class MJShiftDBFactory {
       private ComboPooledDataSource _source;

       public MJShiftDBFactory(MJShiftDBArgs args) throws SQLException {
         try {
           this._source = new ComboPooledDataSource();
           this._source.setDriverClass(args.DRIVER_NAME);
           this._source.setJdbcUrl(args.URL);
           this._source.setUser(args.USER_NAME);
           this._source.setPassword(args.PASSWORD);
           this._source.setInitialPoolSize(args.MIN_POOL_SIZE);
           this._source.setMinPoolSize(args.MIN_POOL_SIZE);
           this._source.setMaxPoolSize(args.MAX_POOL_SIZE);
           this._source.setAcquireIncrement(5);
           this._source.setAcquireRetryAttempts(30);
           this._source.setAcquireRetryDelay(1000);
           this._source.setIdleConnectionTestPeriod(60);
           this._source.setPreferredTestQuery("SELECT 1");
           this._source.setTestConnectionOnCheckin(true);
           this._source.setTestConnectionOnCheckout(false);
           this._source.getConnection().close();
         } catch (Exception e) {
           e.printStackTrace();
           throw new SQLException("could not init DB connection:" + e);
         }
       }
       public void shutdown() {

         try { this._source.close(); }
         catch (Exception exception) {  }
         finally
         { this._source = null; }

       }

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


