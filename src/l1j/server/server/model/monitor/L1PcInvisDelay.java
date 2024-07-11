 package l1j.server.server.model.monitor;

 import l1j.server.server.model.Instance.L1PcInstance;


















 public class L1PcInvisDelay
   extends L1PcMonitor
 {
   public L1PcInvisDelay(int oId) {
     super(oId);
   }


   public void execTask(L1PcInstance pc) {
     pc.addInvisDelayCounter(-1);
   }
 }


