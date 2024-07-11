 package l1j.server.server.command.executor;

 import java.sql.Connection;
 import l1j.server.L1DatabaseFactory;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.utils.L1QueryUtil;
 import l1j.server.server.utils.SQLUtil;

 public class L1DatabaseInit
   implements L1CommandExecutor
 {
   public static L1CommandExecutor getInstance() {
     return new L1DatabaseInit();
   }

   public void execute(L1PcInstance pc, String cmdName, String poby) {
     Connection con = null;

     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       L1QueryUtil.execute(con, "DELETE FROM board_free", new Object[0]);
     }
     catch (Exception e) {
       e.printStackTrace();
     } finally {
       SQLUtil.close(con);
     }
   }
 }


