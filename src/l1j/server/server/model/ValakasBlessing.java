 package l1j.server.server.model;

 import java.util.TimerTask;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class ValakasBlessing extends TimerTask {
   private L1PcInstance _pc;

   public ValakasBlessing(L1PcInstance pc) {
     this._pc = pc;
   }

   public void run() {
     try {
       if (this._pc == null || this._pc.isDead()) {
         return;
       }
       regenManaPoint();
     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   private void regenManaPoint() {
     this._pc.setCurrentMp(this._pc.getCurrentMp() + 4);
   }
 }


