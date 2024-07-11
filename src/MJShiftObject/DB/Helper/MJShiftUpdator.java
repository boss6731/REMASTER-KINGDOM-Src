 package MJShiftObject.DB.Helper;

 import MJShiftObject.MJShiftObjectManager;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.BatchHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.server.utils.SQLUtil;

 public class MJShiftUpdator
   extends Updator {
   public static final int MAXIMUM_BATCH_COUNT = 1000;

   public static void truncate(String table_name) {
     exec(String.format("truncate table %s", new Object[] { table_name }), null);
   }

   public static int exec(String query, Handler handler) {
     return (new MJShiftUpdator()).execute(query, handler);
   }

   public static void batch(String query, BatchHandler handler, int loop_count) {
     (new MJShiftUpdator()).execBatch(query, (Handler)handler, loop_count);
   }


   public int execute(String query, Handler handler) {
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = MJShiftObjectManager.getInstance().get_connection();
       pstm = con.prepareStatement(query);
       if (handler != null)
         handler.handle(pstm);
       return pstm.executeUpdate();
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(pstm, con);
     }
     return 0;
   }


   public void execBatch(String query, Handler handler, int loop_count) {
     if (!(handler instanceof BatchHandler)) {
       throw new IllegalArgumentException("handler is not BatchHandler...!");
     }
     Connection con = null;
     PreparedStatement pstm = null;
     try {
       con = MJShiftObjectManager.getInstance().get_connection();
       con.setAutoCommit(false);
       pstm = con.prepareStatement(query);
       for (int i = 0; i < loop_count; i++) {
         handler.handle(pstm);
         pstm.addBatch();
         pstm.clearParameters();

         if (i > 0 && i % 1000 == 0) {
           pstm.executeBatch();
           con.commit();
           pstm.clearBatch();
         }
       }
       pstm.executeBatch();
       con.commit();
       pstm.clearBatch();
     } catch (Exception e) {
       try {
         con.rollback();
       } catch (Exception e1) {
         e1.printStackTrace();
       }
       e.printStackTrace();
     } finally {
       try {
         con.setAutoCommit(true);
       } catch (Exception e) {
         e.printStackTrace();
       }
       SQLUtil.close(pstm, con);
     }
   }
 }


