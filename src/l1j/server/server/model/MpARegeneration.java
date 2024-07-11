 package l1j.server.server.model;

 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.server.RepeatTask;
 import l1j.server.server.model.Instance.L1PcInstance;

 public class MpARegeneration
   extends RepeatTask
 {
   private static Logger _log = Logger.getLogger(MpARegeneration.class.getName());

   private final L1PcInstance _pc;

   private int tick = 0;

   public MpARegeneration(L1PcInstance pc, long interval) {
     super(interval);
     this._pc = pc;
   }


   public void execute() {
     try {
       if (this._pc.isDead()) {
         return;
       }
       if (this._pc.getCurrentMp() == this._pc.getMaxMp()) {
         return;
       }
       this.tick++;
       regenMp();
     } catch (Exception e) {
       _log.log(Level.WARNING, e.getLocalizedMessage(), e);
     }
   }

   public void regenMp() {
     try {
       int mpAr = 0;
       if (this.tick == 4) {
         mpAr = this._pc.getMpAr();
         this.tick = 0;
       }
       int mpAr16 = this._pc.getMpAr16();

       int newMp = this._pc.getCurrentMp() + mpAr + mpAr16;
       if (newMp >= this._pc.getMaxMp()) {
         newMp = this._pc.getMaxMp();
       }

       this._pc.setCurrentMp(newMp);
     }
     catch (Exception e) {
       e.printStackTrace();
     }
   }
 }


