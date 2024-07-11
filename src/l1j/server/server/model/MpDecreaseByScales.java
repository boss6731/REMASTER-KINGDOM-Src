 package l1j.server.server.model;

 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.server.RepeatTask;
 import l1j.server.server.model.Instance.L1PcInstance;




 public class MpDecreaseByScales
   extends RepeatTask
 {
   private static Logger _log = Logger.getLogger(MpDecreaseByScales.class
       .getName());

   private final L1PcInstance _pc;

   public MpDecreaseByScales(L1PcInstance pc, long interval) {
     super(interval);
     this._pc = pc;
   }


   public void execute() {
     try {
       if (this._pc.isDead() || this._pc.getCurrentMp() < 6) {
         killSkill();
         return;
       }
       regenMp();
     } catch (Throwable e) {
       _log.log(Level.WARNING, e.getLocalizedMessage(), e);
     }
   }

   public void regenMp() {
     int newMp = this._pc.getCurrentMp() - 6;

     this._pc.setCurrentMp(newMp);
   }

   public void killSkill() {
     if (this._pc.hasSkillEffect(185)) {
       this._pc.removeSkillEffect(185);
     } else if (this._pc.hasSkillEffect(190)) {
       this._pc.removeSkillEffect(190);
     } else if (this._pc.hasSkillEffect(195)) {
       this._pc.removeSkillEffect(195);
     }
   }
 }


