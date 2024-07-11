 package l1j.server.server.model.monitor;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;

 public abstract class L1PcMonitor
   implements Runnable {
   protected int _id;

   public L1PcMonitor(int oId) {
     this._id = oId;
   }


   public final void run() {
     L1PcInstance pc = (L1PcInstance)L1World.getInstance().findObject(this._id);
     if (pc == null || pc.getNetConnection() == null) {
       return;
     }
     execTask(pc);
   }

   public abstract void execTask(L1PcInstance paramL1PcInstance);
 }


