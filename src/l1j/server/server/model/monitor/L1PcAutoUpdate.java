 package l1j.server.server.model.monitor;

 import l1j.server.server.model.Instance.L1PcInstance;

 public class L1PcAutoUpdate
   extends L1PcMonitor {
   public L1PcAutoUpdate(int oId) {
     super(oId);
   }


   public void execTask(L1PcInstance pc) {
     if (((pc.getX() >= 33627 && pc.getX() <= 33636 && pc.getY() >= 32673 && pc.getY() <= 32682) || (pc.getX() >= 32824 && pc.getX() <= 32832 && pc.getY() >= 32815 && pc.getY() <= 32822) || (pc
       .getX() >= 33166 && pc.getX() <= 33174 && pc.getY() >= 32771 && pc.getY() <= 32778)) &&
       pc.isInvisble()) {
       pc.delInvis();
     }

     pc.updateObject0();
   }
 }


