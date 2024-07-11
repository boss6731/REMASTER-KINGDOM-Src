 package l1j.server.server.model.monitor;

 import l1j.server.server.model.Instance.L1PcInstance;


















 public class L1PcGhostMonitor
   extends L1PcMonitor
 {
   public L1PcGhostMonitor(int oId) {
     super(oId);
   }


   public void execTask(L1PcInstance pc) {
     pc.endGhost();
   }
 }


