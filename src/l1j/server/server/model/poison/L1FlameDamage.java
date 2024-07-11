 package l1j.server.server.model.poison;

 import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
 import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPELL_BUFF_NOTI;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.RepeatTask;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.ServerBasePacket;


 public class L1FlameDamage
   extends L1Poison
 {
   private RepeatTask _timer;
   private final L1Character _attacker;
   private final L1Character _target;
   private final int _damageSpan;
   private final int _damage;

   private L1FlameDamage(L1Character attacker, L1Character cha, int damageSpan, int damage) {
     this._attacker = attacker;
     this._target = cha;
     this._damageSpan = damageSpan;
     this._damage = damage;

     doInfection();
   }

   private class NormalPoisonTimer
     extends RepeatTask {
     NormalPoisonTimer() {
       super(L1FlameDamage.this._damageSpan);
     }


     public void execute() {
       L1PcInstance player = null;
       L1MonsterInstance mob = null;

       if (!L1FlameDamage.this._target.hasSkillEffect(50000)) {
         L1FlameDamage.this.cure();


       }
       else if (L1FlameDamage.this._target.hasSkillEffect(70705) || L1FlameDamage.this._target.hasSkillEffect(30003) || L1FlameDamage.this._target.hasSkillEffect(30004) || L1FlameDamage.this
         ._target.hasSkillEffect(157)) {
         L1FlameDamage.this.cure();


       }
       else if (L1FlameDamage.this._target instanceof L1PcInstance) {
         player = (L1PcInstance)L1FlameDamage.this._target;
         player.receiveDamage(L1FlameDamage.this._attacker, L1FlameDamage.this._damage);
         if (L1FlameDamage.this._target.hasSkillEffect(50000)) {
           player.send_effect(18509);
           player.sendPackets((ServerBasePacket)new S_DoActionGFX(player.getId(), 2));
           player.broadcastPacket((ServerBasePacket)new S_DoActionGFX(player.getId(), 2));
         }
         if (player.isDead()) {
           L1FlameDamage.this.cure();
         }
       }
       else if (L1FlameDamage.this._target instanceof L1MonsterInstance) {
         mob = (L1MonsterInstance)L1FlameDamage.this._target;
         mob.receiveDamage(L1FlameDamage.this._attacker, L1FlameDamage.this._damage);
         if (mob.hasSkillEffect(50000)) {
           mob.send_effect(18509);
           mob.broadcastPacket((ServerBasePacket)new S_DoActionGFX(mob.getId(), 2));
         }
         if (mob.isDead()) {
           L1FlameDamage.this.cure();
         }
       }
     }
   }



   boolean isDamageTarget(L1Character cha) {
     return (cha instanceof L1PcInstance || cha instanceof L1MonsterInstance);
   }


   private void doInfection() {
     if (this._target.hasSkillEffect(50000)) {
       return;
     }

     this._target.setSkillEffect(50000, 3000L);

     if (isDamageTarget(this._target)) {
       if (this._target instanceof L1PcInstance) {
         L1PcInstance target = (L1PcInstance)this._target;
         on_icons(target, 50000);
       }
       this._timer = new NormalPoisonTimer();
       GeneralThreadPool.getInstance().execute((Runnable)this._timer);
     }
   }


   public static boolean doInfection(L1Character attacker, L1Character cha, int damageSpan, int damage) {
     if (cha.hasSkillEffect(50000)) {
       return false;
     }

     new L1FlameDamage(attacker, cha, damageSpan, damage);
     return true;
   }


   public int getEffectId() {
     return 1;
   }


   public void cure() {
     if (this._timer != null) {
       this._timer.cancel();
     }
     if (this._target instanceof L1PcInstance) {
       L1PcInstance target = (L1PcInstance)this._target;
       off_icons(target, 50000);
     }
     this._target.killSkillEffectTimer(50000);
   }

   private void on_icons(L1PcInstance pc, int skillId) {
     SC_SPELL_BUFF_NOTI noti = null;
     switch (skillId) {
       case 50000:
         noti = SC_SPELL_BUFF_NOTI.newInstance();
         noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.RESTAT);
         noti.set_duration(3);
         noti.set_duration_show_type(SC_SPELL_BUFF_NOTI.eDurationShowType.TYPE_EFF_AUTO_DAY_HOUR_MIN_SEC);
         noti.set_on_icon_id(9703);
         noti.set_off_icon_id(9703);
         noti.set_tooltip_str_id(7062);
         noti.set_new_str_id(7062);
         noti.set_end_str_id(0);
         noti.set_is_good(false);
         break;
     }

     if (noti != null) {
       noti.set_spell_id(skillId - 1);
       pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
     }
   }

   private void off_icons(L1PcInstance pc, int skillId) {
     SC_SPELL_BUFF_NOTI noti = null;
     switch (skillId) {
       case 50000:
         noti = SC_SPELL_BUFF_NOTI.newInstance();
         noti.set_noti_type(SC_SPELL_BUFF_NOTI.eNotiType.END);
         noti.set_duration(0);
         noti.set_end_str_id(0);
         noti.set_is_good(true);
         break;
     }

     if (noti != null) {
       noti.set_spell_id(skillId - 1);
       pc.sendPackets((MJIProtoMessage)noti, MJEProtoMessages.SC_SPELL_BUFF_NOTI.toInt(), true);
     }
   }
 }


