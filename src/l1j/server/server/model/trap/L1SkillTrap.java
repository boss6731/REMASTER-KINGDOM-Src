 package l1j.server.server.model.trap;

 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.storage.TrapStorage;






 public class L1SkillTrap
   extends L1Trap
 {
   private final int _skillId;
   private final int _skillTimeSeconds;

   public L1SkillTrap(TrapStorage storage) {
     super(storage);

     this._skillId = storage.getInt("skillId");
     this._skillTimeSeconds = storage.getInt("skillTimeSeconds");
   }


   public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
     sendEffect(trapObj);

     (new L1SkillUse()).handleCommands(trodFrom, this._skillId, trodFrom.getId(), trodFrom
         .getX(), trodFrom.getY(), null, this._skillTimeSeconds, 4);
   }
 }


