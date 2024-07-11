 package l1j.server.server.model.skill;

 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.L1Character;


 public class L1SkillDelay
 {
   static class SkillDelayTimer
     implements Runnable
   {
     private L1Character _cha;
     private boolean _link;

     public SkillDelayTimer(L1Character cha, long time, boolean link) {
       this._cha = cha;
       this._link = link;
     }


     public void run() {
       stopDelayTimer();
     }

     public void stopDelayTimer() {
       if (this._link) {
         this._cha.setLinkSkillDelay(false);
       } else {
         this._cha.setSkillDelay(false);
       }
     } }

   public static void onSkillUse(L1Character cha, long time, boolean link) {
     if (link) {
       cha.setLinkSkillDelay(true);
     } else {
       cha.setSkillDelay(true);
     }  GeneralThreadPool.getInstance().schedule(new SkillDelayTimer(cha, time, link), time);
   }
 }


