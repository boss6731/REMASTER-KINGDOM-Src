 package l1j.server.server.model.monitor;

 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.Instance.L1PcInstance;


















 public class L1PcHellMonitor
   extends L1PcMonitor
 {
   public L1PcHellMonitor(int oId) {
     super(oId);
   }


   public void execTask(L1PcInstance pc) {
     if (pc.isDead()) {
       return;
     }
     pc.setHellTime(pc.getHellTime() - 1);
     if (pc.getHellTime() <= 0) {
       Runnable r = new L1PcMonitor(pc.getId())
         {
           public void execTask(L1PcInstance pc) {
             pc.endHell();
           }
         };
       GeneralThreadPool.getInstance().execute(r);
     }
   }
 }


