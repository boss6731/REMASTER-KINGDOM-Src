 package l1j.server.server.model;

 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.server.RepeatTask;
 import l1j.server.server.model.Instance.L1PcInstance;



 public class HpARegeneration
   extends RepeatTask
 {
   private static Logger _log = Logger.getLogger(HpARegeneration.class.getName());

   public HpARegeneration(L1PcInstance pc, long interval) {
     super(interval);
     this._pc = pc;
   }
   private final L1PcInstance _pc;
   public void execute() {
     try {
       if (this._pc.isDead()) {
         return;
       }
       if (this._pc.getCurrentHp() == this._pc.getMaxHp()) {
         return;
       }
       regenHp();
     }
     catch (Exception e) {
       _log.log(Level.WARNING, e.getLocalizedMessage(), e);
     }
   }


   public void regenHp() {
     try {
       int hpAr = this._pc.getHpAr();
       int newHp = this._pc.getCurrentHp();
       newHp += hpAr;
       if (newHp >= this._pc.getMaxHp()) {
         newHp = this._pc.getMaxHp();
       }
       if (!this._pc.isDead()) {
         this._pc.setCurrentHp(newHp);
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
   }
 }


