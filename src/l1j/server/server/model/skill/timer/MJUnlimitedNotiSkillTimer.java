 package l1j.server.server.model.skill.timer;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.skill.noti.MJNotiSkillModel;

 public class MJUnlimitedNotiSkillTimer
   extends MJNotiSkillTimer {
   MJUnlimitedNotiSkillTimer(L1PcInstance character, MJNotiSkillModel model, long timeMillis) {
     super(character, model, timeMillis);
   }


   public void begin() {
     refreshPartyMemberStatus();
   }
 }


