 package MJShiftObject.DB.Helper;

 import MJShiftObject.MJShiftObjectManager;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.server.utils.SQLUtil;

 public class MJShiftSelector
   extends Selector {
   public static void exec(String query, SelectorHandler handler) {
     (new MJShiftSelector()).execute(query, (Handler)handler);
   }


   public int execute(String query, Handler handler) {
     if (!(handler instanceof SelectorHandler)) {
       throw new IllegalArgumentException("handler is not SelectorHandler...!");
     }
     SelectorHandler sHandler = (SelectorHandler)handler;
     Connection con = null;
     PreparedStatement pstm = null;
     ResultSet rs = null;
     try {
       con = MJShiftObjectManager.getInstance().get_connection();
       pstm = con.prepareStatement(query);
       sHandler.handle(pstm);
       rs = pstm.executeQuery();
       sHandler.result(rs);
       return 1;
     } catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(rs, pstm, con);
     }
     return 0;
   }
 }


