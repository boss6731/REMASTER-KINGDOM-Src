 package l1j.server.server.model;

 import java.util.TimerTask;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class ReportDeley
   extends TimerTask {
   private static Logger _log = Logger.getLogger(ReportDeley.class
       .getName());

   private final L1PcInstance _pc;

   public ReportDeley(L1PcInstance pc) {
     this._pc = pc;
     this._pc.setReport(false);
   }


   public void run() {
     try {
       if (this._pc.isDead()) {
         return;
       }
       this._pc.setReport(true);
       cancel();
     } catch (Throwable e) {
       _log.log(Level.WARNING, e.getLocalizedMessage(), e);
     }
   }
 }


