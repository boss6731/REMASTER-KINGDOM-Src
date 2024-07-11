         package l1j.server.server.monitor;

         import l1j.server.server.GeneralThreadPool;

         public class LoggerInstance extends FileLogger implements Runnable {
           private static LoggerInstance _instance = null;




           public static LoggerInstance getInstance() {
             if (_instance == null) {
               _instance = new LoggerInstance();
               GeneralThreadPool.getInstance().execute(_instance);
             }

             return _instance;
           }

           public void run() {
             try {
               flush();
             } catch (Exception exception) {

             } finally {
               GeneralThreadPool.getInstance().schedule(this, 60000L);
             }
           }
         }


