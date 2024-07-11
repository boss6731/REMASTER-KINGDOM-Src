 package l1j.server.server.model.skill.timer;

 import l1j.server.server.model.L1Character;

 public class MJUnlimitedSkillTimer extends MJSimpleSkillTimer {
   MJUnlimitedSkillTimer(L1Character character, int skillId, long timeMillis) {
     super(character, skillId, timeMillis);
   }

   public void begin() {}
 }


