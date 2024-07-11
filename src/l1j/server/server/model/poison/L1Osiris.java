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
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.serverpackets.S_Paralysis;
 import l1j.server.server.serverpackets.S_UseAttackSkill;
 import l1j.server.server.serverpackets.ServerBasePacket;










 public class L1Osiris
   extends L1Poison
 {
   private RepeatTask _timer;
   private final L1Character _attacker;
   private final L1Character _target;
   private final int _damageSpan;
   private final int _damage;
   private int _time;
   private int _shockStunDuration;

   private L1Osiris(L1Character attacker, L1Character cha, int damageSpan, int damage) {
     this._attacker = attacker;
     this._target = cha;
     this._damageSpan = damageSpan;
     this._damage = damage;

     doInfection();
   }

   private class NormalPoisonTimer
     extends RepeatTask {
     NormalPoisonTimer() {
       super(L1Osiris.this._damageSpan);
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
       L1Osiris.this._time = L1Osiris.this._target.getSkillEffectTimeSec(15037);


       if (3000 - L1Osiris.this._time * 1000 > 0) {
         if (!L1Osiris.this._target.hasSkillEffect(15037)) {
           L1Osiris.this.cure();


         }
         else if (L1Osiris.this._target.hasSkillEffect(70705) || L1Osiris.this._target.hasSkillEffect(30003) || L1Osiris.this
           ._target.hasSkillEffect(30004) || L1Osiris.this._target.hasSkillEffect(157)) {
           L1Osiris.this.cure();

         }
         else if (L1Osiris.this._target instanceof L1Character) {
           if (L1Osiris.this._target.hasSkillEffect(15037)) {
             ArrayList<L1Object> list1 = new ArrayList<>();
             ArrayList<L1Object> osirislist1 = new ArrayList<>();
             list1 = L1World.getInstance().getVisibleObjects((L1Object)L1Osiris.this._target, 2);
             for (int i = 0; i < list1.size(); i++) {
               if (list1.get(i) instanceof L1PcInstance &&
                 list1.get(i) != L1Osiris.this._attacker) {
                 osirislist1.add(list1.get(i));
               }
             }

             if (osirislist1.size() >= 1) {




               Collections.shuffle(osirislist1);
               L1Object osiristarget = osirislist1.get(0);
               L1Character Otarget = (L1Character)osiristarget;







               int tX = 0;
               int tY = 0;
               tX = Otarget.getX();
               tY = Otarget.getY();
               int direction = L1Osiris.this._target.targetDirection(tX, tY);



















               L1Osiris.this._target.sendPackets((ServerBasePacket)new S_UseAttackSkill(L1Osiris.this._target, Otarget.getId(), 21549, tX, tY, 0, false), false);
               Broadcaster.broadcastPacket(L1Osiris.this._target, (ServerBasePacket)new S_UseAttackSkill(L1Osiris.this._target, Otarget.getId(), 21087, tX, tY, 0, false));


               L1EffectSpawn.getInstance().spawnEffect(effectNpcId, 2000, Otarget.getX(), Otarget.getY(), L1Osiris.this._target.getMapId());

               if (Otarget instanceof L1PcInstance) {
                 L1PcInstance pc = (L1PcInstance)Otarget;
                 targetLevel = pc.getLevel();
               } else if (Otarget instanceof l1j.server.server.model.Instance.L1MonsterInstance || Otarget instanceof l1j.server.server.model.Instance.L1SummonInstance || Otarget instanceof l1j.server.server.model.Instance.L1PetInstance || Otarget instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance) {
                 npc = (L1NpcInstance)Otarget;
                 targetLevel = npc.getLevel();
               }
               player = (L1PcInstance)L1Osiris.this._attacker;
               diffLevel = player.getLevel() - targetLevel;



               if (player != null) {
                 if (diffLevel < Config.MagicAdSetting_Illusion.OSIRIS_LVL) {
                   int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS;
                   L1Osiris.this._shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
                 } else if (diffLevel >= Config.MagicAdSetting_Illusion.OSIRIS_LVL1 && diffLevel <= Config.MagicAdSetting_Illusion.OSIRIS_LVL2) {
                   int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS1;
                   L1Osiris.this._shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
                 } else if (diffLevel >= Config.MagicAdSetting_Illusion.OSIRIS_LVL3 && diffLevel <= Config.MagicAdSetting_Illusion.OSIRIS_LVL4) {
                   int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS2;
                   L1Osiris.this._shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
                 } else if (diffLevel >= Config.MagicAdSetting_Illusion.OSIRIS_LVL5 && diffLevel <= Config.MagicAdSetting_Illusion.OSIRIS_LVL6) {
                   int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS3;
                   L1Osiris.this._shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
                 } else if (diffLevel >= Config.MagicAdSetting_Illusion.OSIRIS_LVL7 && diffLevel <= Config.MagicAdSetting_Illusion.OSIRIS_LVL8) {
                   int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS4;
                   L1Osiris.this._shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
                 } else if (diffLevel > Config.MagicAdSetting_Illusion.OSIRIS_LVL9) {
                   int[] SkillTimeArray = Config.MagicAdSetting_Illusion.OSIRIS_MS5;
                   L1Osiris.this._shockStunDuration = SkillTimeArray[random.nextInt(SkillTimeArray.length)];
                 }
               }
               L1Magic _magic = new L1Magic((L1Character)player, Otarget);
               if (_magic.calcProbabilityMagic(5037)) {
                 if (player.getWeapon() != null) {
                   L1ItemInstance weapon = player.getWeapon();
                   if (weapon.getItemId() == 7000265) {
                     L1Osiris.this._shockStunDuration = L1Osiris.this._shockStunDuration + 1000;
                   }
                 }
                 L1EffectSpawn.getInstance().spawnEffect2(effectNpcId2, 5037, Otarget.getX(), Otarget.getY(), Otarget.getMapId(), Otarget);

                 if (Otarget instanceof L1PcInstance) {
                   L1PcInstance pc = (L1PcInstance)Otarget;
                   if (pc.hasSkillEffect(5037)) {
                     pc.removeSkillEffect(5037);
                   }
                   L1SkillUse.on_icons(pc, 5037, L1Osiris.this._shockStunDuration / 1000);
                   pc.sendPackets((ServerBasePacket)new S_Paralysis(13, true));

                   pc.setSkillEffect(5037, L1Osiris.this._shockStunDuration);
                 }
                 else if (Otarget instanceof l1j.server.server.model.Instance.L1MonsterInstance || Otarget instanceof l1j.server.server.model.Instance.L1SummonInstance || Otarget instanceof l1j.server.server.model.Instance.L1PetInstance || Otarget instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance) {
                   npc = (L1NpcInstance)Otarget;
                   npc.setSkillEffect(5037, L1Osiris.this._shockStunDuration);
                   npc.setParalyzed(true);
                   npc.setParalysisTime(L1Osiris.this._shockStunDuration);
                 }
               }



               Otarget.setSkillEffect(15037, 3000L);
               L1Osiris2.doInfection((L1Character)player, L1Osiris.this._target, Otarget, 300, 0);
             }










             L1Osiris.this.cure();
           }
           if (L1Osiris.this._target.isDead()) {
             L1Osiris.this.cure();
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

   public static boolean doInfection(L1Character attacker, L1Character cha, int damageSpan, int damage) {
     new L1Osiris(attacker, cha, damageSpan, damage);
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


