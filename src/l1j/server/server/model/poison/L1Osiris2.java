 package l1j.server.server.model.poison;

 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.Random;
 import l1j.server.Config;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.RepeatTask;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1EffectSpawn;
 import l1j.server.server.model.L1Magic;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_Paralysis;
 import l1j.server.server.serverpackets.S_UseAttackSkill;
 import l1j.server.server.serverpackets.ServerBasePacket;








 public class L1Osiris2
   extends L1Poison
 {
   private RepeatTask _timer;
   private final L1Character _attacker;
   private final L1Character _target;
   private final L1Character _targetoftarget;
   private final int _damageSpan;
   private final int _damage;
   private int _time2;
   private int _shockStunDuration;

   private L1Osiris2(L1Character attacker, L1Character target, L1Character targetoftarget, int damageSpan, int damage) {
     this._attacker = attacker;
     this._target = target;
     this._targetoftarget = targetoftarget;
     this._damageSpan = damageSpan;
     this._damage = damage;

     doInfection();
   }

   private class NormalPoisonTimer
     extends RepeatTask {
     NormalPoisonTimer() {
       super(L1Osiris2.this._damageSpan);
     }


     public void execute() {
       L1PcInstance player = null;
       L1NpcInstance npc = null;
       L1Character cha = null;
       int effectNpcId = 8502108;
       int effectNpcId2 = 8502107;
       int targetLevel = 0;
       int diffLevel = 0;

       Random random = new Random(System.nanoTime());
       L1Osiris2.this._time2 = L1Osiris2.this._targetoftarget.getSkillEffectTimeSec(15037);

       if (3000 - L1Osiris2.this._time2 * 1000 > 0)
       {
         if (!L1Osiris2.this._targetoftarget.hasSkillEffect(15037)) {
           L1Osiris2.this.cure();


         }
         else if (L1Osiris2.this._targetoftarget.hasSkillEffect(70705) || L1Osiris2.this._targetoftarget.hasSkillEffect(30003) || L1Osiris2.this
           ._targetoftarget.hasSkillEffect(30004) || L1Osiris2.this._targetoftarget.hasSkillEffect(157)) {
           L1Osiris2.this.cure();


         }
         else if (L1Osiris2.this._targetoftarget instanceof L1Character) {

           cha = L1Osiris2.this._targetoftarget;
           if (L1Osiris2.this._targetoftarget.hasSkillEffect(15037)) {

             ArrayList<L1Object> list3 = new ArrayList<>();
             ArrayList<L1Object> osirislist3 = new ArrayList<>();
             list3 = L1World.getInstance().getVisibleObjects((L1Object)L1Osiris2.this._targetoftarget, 2);

             for (int i = 0; i < list3.size(); i++) {
               if (list3.get(i) instanceof L1PcInstance &&
                 list3.get(i) != L1Osiris2.this._attacker && list3.get(i) != L1Osiris2.this._target && list3.get(i) != L1Osiris2.this._targetoftarget) {
                 osirislist3.add(list3.get(i));
               }
             }

             if (osirislist3.size() >= 1) {





               Collections.shuffle(osirislist3);
               L1Object osiristarget3 = osirislist3.get(0);

               L1Character Otarget3 = (L1Character)osiristarget3;






               int tX = 0;
               int tY = 0;
               tX = Otarget3.getX();
               tY = Otarget3.getY();
               int direction = L1Osiris2.this._targetoftarget.targetDirection(tX, tY);

               L1Osiris2.this._targetoftarget.sendPackets((ServerBasePacket)new S_UseAttackSkill(L1Osiris2.this._targetoftarget, Otarget3.getId(), 21087, tX, tY, 0, false), false);
               Broadcaster.broadcastPacket(L1Osiris2.this._targetoftarget, (ServerBasePacket)new S_UseAttackSkill(L1Osiris2.this._targetoftarget, Otarget3.getId(), 21087, tX, tY, 0, false));




               L1EffectSpawn.getInstance().spawnEffect2(effectNpcId, 2000, Otarget3.getX(), Otarget3.getY(), Otarget3.getMapId(), Otarget3);

               if (Otarget3 instanceof L1PcInstance) {
                 L1PcInstance pc = (L1PcInstance)Otarget3;
                 targetLevel = pc.getLevel();
               } else if (Otarget3 instanceof l1j.server.server.model.Instance.L1MonsterInstance || Otarget3 instanceof l1j.server.server.model.Instance.L1SummonInstance || Otarget3 instanceof l1j.server.server.model.Instance.L1PetInstance || Otarget3 instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance) {
                 npc = (L1NpcInstance)Otarget3;
                 targetLevel = npc.getLevel();
               }
               player = (L1PcInstance)L1Osiris2.this._attacker;
               diffLevel = player.getLevel() - targetLevel;



               if (player != null) {
                 if (diffLevel < Config.MagicAdSetting_Illusion.OSIRIS_LVL) {
                   int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS;
                   L1Osiris2.this._shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
                 } else if (diffLevel >= Config.MagicAdSetting_Illusion.OSIRIS_LVL1 && diffLevel <= Config.MagicAdSetting_Illusion.OSIRIS_LVL2) {
                   int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS1;
                   L1Osiris2.this._shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
                 } else if (diffLevel >= Config.MagicAdSetting_Illusion.OSIRIS_LVL3 && diffLevel <= Config.MagicAdSetting_Illusion.OSIRIS_LVL4) {
                   int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS2;
                   L1Osiris2.this._shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
                 } else if (diffLevel >= Config.MagicAdSetting_Illusion.OSIRIS_LVL5 && diffLevel <= Config.MagicAdSetting_Illusion.OSIRIS_LVL6) {
                   int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS3;
                   L1Osiris2.this._shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
                 } else if (diffLevel >= Config.MagicAdSetting_Illusion.OSIRIS_LVL7 && diffLevel <= Config.MagicAdSetting_Illusion.OSIRIS_LVL8) {
                   int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS4;
                   L1Osiris2.this._shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
                 } else if (diffLevel > Config.MagicAdSetting_Illusion.OSIRIS_LVL9) {
                   int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS5;
                   L1Osiris2.this._shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
                 }
               }
               L1Magic _magic = new L1Magic((L1Character)player, Otarget3);
               if (_magic.calcProbabilityMagic(5037)) {
                 if (player.getWeapon() != null) {
                   L1ItemInstance weapon = player.getWeapon();
                   if (weapon.getItemId() == 7000265) {
                     L1Osiris2.this._shockStunDuration = L1Osiris2.this._shockStunDuration + 1000;
                   }
                 }
                 L1EffectSpawn.getInstance().spawnEffect2(effectNpcId2, L1Osiris2.this._shockStunDuration, Otarget3.getX(), Otarget3.getY(), Otarget3.getMapId(), Otarget3);

                 if (Otarget3 instanceof L1PcInstance) {
                   L1PcInstance pc = (L1PcInstance)Otarget3;
                   if (pc.hasSkillEffect(5037))
                     pc.removeSkillEffect(5037);
                   pc.sendPackets((ServerBasePacket)new S_Paralysis(9, true));
                   pc.setSkillEffect(230, L1Osiris2.this._shockStunDuration);
                 }
                 else if (Otarget3 instanceof l1j.server.server.model.Instance.L1MonsterInstance || Otarget3 instanceof l1j.server.server.model.Instance.L1SummonInstance || Otarget3 instanceof l1j.server.server.model.Instance.L1PetInstance || Otarget3 instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance) {
                   npc = (L1NpcInstance)Otarget3;
                   npc.setSkillEffect(230, L1Osiris2.this._shockStunDuration);
                   npc.setParalyzed(true);
                   npc.setParalysisTime(L1Osiris2.this._shockStunDuration);
                 }
               }
             }

             L1Osiris2.this.cure();
           }
           if (L1Osiris2.this._targetoftarget.isDead()) {
             L1Osiris2.this.cure();
           }
         }
       }
     }
   }



   boolean isDamageTarget(L1Character cha) {
     return (cha instanceof L1PcInstance || cha instanceof l1j.server.server.model.Instance.L1MonsterInstance);
   }

   private void doInfection() {
     if (isDamageTarget(this._target)) {
       this._timer = new NormalPoisonTimer();
       GeneralThreadPool.getInstance().execute((Runnable)this._timer);
     }
   }

   public static boolean doInfection(L1Character attacker, L1Character cha, L1Character target, int damageSpan, int damage) {
     new L1Osiris2(attacker, cha, target, damageSpan, damage);
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


