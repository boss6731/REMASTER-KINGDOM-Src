 package l1j.server.server.model.poison;

 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.RepeatTask;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class L1DemolitionDamage
   extends L1Poison
 {
   private RepeatTask _timer;
   private final L1Character _attacker;
   private final L1Character _target;
   private final int _damageSpan;
   private final int _damage;

   private L1DemolitionDamage(L1Character attacker, L1Character cha, int damageSpan, int damage) {
     this._attacker = attacker;
     this._target = cha;
     this._damageSpan = damageSpan;
     this._damage = damage;

     doInfection();
   }

   private class NormalPoisonTimer
     extends RepeatTask {
     NormalPoisonTimer() {
       super(L1DemolitionDamage.this._damageSpan);
     }


     public void execute() {
       L1PcInstance player = null;
       L1MonsterInstance mob = null;

       if (!L1DemolitionDamage.this._target.hasSkillEffect(388)) {
         L1DemolitionDamage.this.cure();


       }
       else if (L1DemolitionDamage.this._target.hasSkillEffect(70705) || L1DemolitionDamage.this._target.hasSkillEffect(30003) || L1DemolitionDamage.this
         ._target.hasSkillEffect(30004) || L1DemolitionDamage.this._target.hasSkillEffect(157)) {
         L1DemolitionDamage.this.cure();


       }
       else if (L1DemolitionDamage.this._target instanceof L1PcInstance) {
         player = (L1PcInstance)L1DemolitionDamage.this._target;
         player.receiveDamage(L1DemolitionDamage.this._attacker, L1DemolitionDamage.this._damage);
         if (L1DemolitionDamage.this._target.hasSkillEffect(388)) {
           player.sendPackets((ServerBasePacket)new S_DoActionGFX(player.getId(), 2));
           player.broadcastPacket((ServerBasePacket)new S_DoActionGFX(player.getId(), 2));
         }
         if (player.isDead()) {
           L1DemolitionDamage.this.cure();
         }
       }
       else if (L1DemolitionDamage.this._target instanceof L1MonsterInstance) {
         mob = (L1MonsterInstance)L1DemolitionDamage.this._target;
         mob.receiveDamage(L1DemolitionDamage.this._attacker, L1DemolitionDamage.this._damage);
         if (mob.hasSkillEffect(388)) {
           mob.broadcastPacket((ServerBasePacket)new S_DoActionGFX(mob.getId(), 2));
         }
         if (mob.isDead()) {
           L1DemolitionDamage.this.cure();
         }
       }
     }
   }



   boolean isDamageTarget(L1Character cha) {
     return (cha instanceof L1PcInstance || cha instanceof L1MonsterInstance);
   }

   private void doInfection() {
     if (isDamageTarget(this._target)) {
       this._timer = new NormalPoisonTimer();
       GeneralThreadPool.getInstance().execute((Runnable)this._timer);
     }
   }

   public static boolean doInfection(L1Character attacker, L1Character cha, int damageSpan, int damage) {
     new L1DemolitionDamage(attacker, cha, damageSpan, damage);
     return true;
   }


   public int getEffectId() {
     return 1;
   }


   public void cure() {
     if (this._timer != null)
       this._timer.cancel();
   }
 }


