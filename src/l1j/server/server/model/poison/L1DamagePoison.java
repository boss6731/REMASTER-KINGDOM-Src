 package l1j.server.server.model.poison;

 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.RepeatTask;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class L1DamagePoison
   extends L1Poison
 {
   private RepeatTask _timer;
   private final L1Character _attacker;
   private final L1Character _target;
   private final int _damageSpan;
   private final int _damage;
   private boolean _tomahawk;

   private L1DamagePoison(L1Character attacker, L1Character cha, int damageSpan, int damage, boolean tomahawk) {
     this._attacker = attacker;
     this._target = cha;
     this._damageSpan = damageSpan;
     this._damage = damage;
     this._tomahawk = tomahawk;

     doInfection();
   }

   private class NormalPoisonTimer
     extends RepeatTask {
     NormalPoisonTimer() {
       super(L1DamagePoison.this._damageSpan);
     }


     public void execute() {
       L1PcInstance player = null;
       L1MonsterInstance mob = null;

       if (!L1DamagePoison.this._target.hasSkillEffect(1006) && !L1DamagePoison.this._target.hasSkillEffect(1020)) {
         L1DamagePoison.this.cure();


       }
       else if (L1DamagePoison.this._target.hasSkillEffect(70705) || L1DamagePoison.this._target.hasSkillEffect(30003) || L1DamagePoison.this._target.hasSkillEffect(30004) || L1DamagePoison.this
         ._target.hasSkillEffect(157)) {
         L1DamagePoison.this.cure();


       }
       else if (L1DamagePoison.this._target instanceof L1PcInstance) {
         player = (L1PcInstance)L1DamagePoison.this._target;
         player.receiveDamage(L1DamagePoison.this._attacker, L1DamagePoison.this._damage);
         if (L1DamagePoison.this._target.hasSkillEffect(1020)) {
           L1DamagePoison.this._target.setPoisonEffect(0);
           L1SkillUse.on_icons(player, 229, 1);
           player.sendPackets((ServerBasePacket)new S_DoActionGFX(player.getId(), 2));
           player.broadcastPacket((ServerBasePacket)new S_DoActionGFX(player.getId(), 2));
         }
         if (player.isDead()) {
           L1DamagePoison.this.cure();
         }
       }
       else if (L1DamagePoison.this._target instanceof L1MonsterInstance) {
         mob = (L1MonsterInstance)L1DamagePoison.this._target;
         mob.receiveDamage(L1DamagePoison.this._attacker, L1DamagePoison.this._damage);
         if (mob.hasSkillEffect(1020)) {
           mob.broadcastPacket((ServerBasePacket)new S_DoActionGFX(mob.getId(), 2));
         }
         if (mob.isDead()) {
           L1DamagePoison.this.cure();
         }
       }
     }
   }



   boolean isDamageTarget(L1Character cha) {
     return (cha instanceof L1PcInstance || cha instanceof L1MonsterInstance);
   }

   private void doInfection() {
     if (this._tomahawk) {
       this._target.setSkillEffect(1020, 7000L);
     } else {
       if (this._target instanceof L1PcInstance) {
         L1PcInstance pc = (L1PcInstance)this._target;
         pc.sendPackets((ServerBasePacket)new S_PacketBox(161, pc, 1, 30));
       }
       this._target.setSkillEffect(1006, 30000L);
       this._target.setPoisonEffect(1);
     }
     if (isDamageTarget(this._target)) {
       this._timer = new NormalPoisonTimer();
       GeneralThreadPool.getInstance().execute((Runnable)this._timer);
     }
   }

   public static boolean doInfection(L1Character attacker, L1Character cha, int damageSpan, int damage, boolean tomahawk) {
     if (!isValidTarget(cha)) {
       return false;
     }
     cha.setPoison(new L1DamagePoison(attacker, cha, damageSpan, damage, tomahawk));
     return true;
   }


   public int getEffectId() {
     return 1;
   }


   public void cure() {
     if (this._timer != null) {
       this._timer.cancel();
     }
     if (this._tomahawk) {
       this._target.killSkillEffectTimer(1020);
       this._target.setPoisonEffect(0);
       this._target.sendPackets((ServerBasePacket)new S_ServerMessage(3993));
     } else {
       if (this._target instanceof L1PcInstance) {
         L1PcInstance pc = (L1PcInstance)this._target;
         pc.sendPackets((ServerBasePacket)new S_PacketBox(161, pc, 0, 0));
       }
       this._target.setPoisonEffect(0);
       this._target.killSkillEffectTimer(1006);
     }
     this._target.setPoison(null);
   }
 }


