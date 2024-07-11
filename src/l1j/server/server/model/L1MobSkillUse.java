 package l1j.server.server.model;

 import java.lang.reflect.Constructor;
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.Random;
 import java.util.concurrent.ConcurrentHashMap;
 import l1j.server.MJ3SEx.EActionCodes;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
 import l1j.server.MJTemplate.MJString;
 import l1j.server.server.IdFactory;
 import l1j.server.server.datatables.MobSkillTable;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.datatables.SkillsTable;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_NpcChatPacket;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1MobSkill;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.templates.L1Skills;






 public class L1MobSkillUse
 {
   private static Random _rnd = new Random(System.nanoTime());

   private L1MobSkill _mobSkillTemplate = null;

   private L1NpcInstance _attacker = null;

   private L1Character _target = null;


   private long _sleepTime = 0L;

   private int[] _skillUseCount;

   public void dispose() {
     this._mobSkillTemplate = null;
     this._attacker = null;
     this._target = null;
   }

   public L1MobSkillUse(L1NpcInstance npc) {
     try {
       if (npc == null)
         return;
       this._sleepTime = 0L;
       this._mobSkillTemplate = MobSkillTable.getInstance().getTemplate(npc.getNpcTemplate().get_npcId());
       if (this._mobSkillTemplate == null) {
         return;
       }
       this._attacker = npc;
       this._skillUseCount = new int[getMobSkillTemplate().getSkillSize()];
     } catch (Exception exception) {}
   }


   private int getSkillUseCount(int idx) {
     return this._skillUseCount[idx];
   }

   private void skillUseCountUp(int idx) {
     this._skillUseCount[idx] = this._skillUseCount[idx] + 1;
   }

   public void resetAllSkillUseCount() {
     if (getMobSkillTemplate() == null) {
       return;
     }

     for (int i = 0; i < getMobSkillTemplate().getSkillSize(); i++) {
       this._skillUseCount[i] = 0;
     }
   }

   public long getSleepTime() {
     return this._sleepTime;
   }

   public void setSleepTime(long i) {
     this._sleepTime = i;
   }

   public L1MobSkill getMobSkillTemplate() {
     return this._mobSkillTemplate;
   }

   public boolean skillUse(L1Character tg) {
     try {
       if (tg == null || this._mobSkillTemplate == null) {
         return false;
       }
       this._target = tg;

       int type = getMobSkillTemplate().getType(0);

       if (type == 0) {
         return false;
       }

       int i = 0;
       i = 0;
       for (; i < getMobSkillTemplate().getSkillSize() && getMobSkillTemplate().getType(i) != 0; i++) {

         int changeType = getMobSkillTemplate().getChangeTarget(i);
         if (changeType > 0) {
           this._target = changeTarget(changeType, i);
         } else {
           this._target = tg;
         }

         if (isSkillUseble(i)) {



           boolean is_use = false;
           type = getMobSkillTemplate().getType(i);
           if (type == 1) {
             if (physicalAttack(i) == true) {
               skillUseCountUp(i);
               is_use = true;
             }
           } else if (type == 2) {
             if (magicAttack(i) == true) {
               skillUseCountUp(i);
               is_use = true;
             }
           } else if (type == 3) {
             if (summon(i) == true) {
               skillUseCountUp(i);
               is_use = true;
             }
           } else if (type == 4 &&
             poly(i) == true) {
             skillUseCountUp(i);
             is_use = true;
           }

           if (is_use)
           { String ment = getMobSkillTemplate().getSpellMent(i);
             if (!MJString.isNullOrEmpty(ment)) {
               Broadcaster.broadcastPacket((L1Character)this._attacker, (ServerBasePacket)new S_NpcChatPacket(this._attacker, ment, 2));
             }
             return true; }
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
     return false;
   }

   private boolean summon(int idx) {
     int summonId = getMobSkillTemplate().getSummon(idx);
     int min = getMobSkillTemplate().getSummonMin(idx);
     int max = getMobSkillTemplate().getSummonMax(idx);
     int count = 0;

     if (summonId == 0) {
       return false;
     }

     count = _rnd.nextInt(max) + min;
     mobspawn(summonId, count);

     this._attacker.broadcastPacket((ServerBasePacket)new S_SkillSound(this._attacker.getId(), 761));

     S_DoActionGFX gfx = new S_DoActionGFX(this._attacker.getId(), 19);
     this._attacker.broadcastPacket((ServerBasePacket)gfx);

     this._sleepTime = this._attacker.getCurrentSpriteInterval(EActionCodes.spell_nodir);
     return true;
   }

   private boolean poly(int idx) {
     int polyId = getMobSkillTemplate().getPolyId(idx);
     boolean usePoly = false;

     if (polyId == 0) {
       return false;
     }

     for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer((L1Object)this._attacker)) {
       if (pc == null || pc.isDead()) {
         continue;
       }
       if (pc.isGhost()) {
         continue;
       }
       if (pc.isGmInvis()) {
         continue;
       }
       if (!this._attacker.glanceCheck(pc.getX(), pc.getY())) {
         continue;
       }

       int npcId = this._attacker.getNpcTemplate().get_npcId();
       switch (npcId) {
         case 81082:
           pc.getInventory().takeoffEquip(945);
           break;
       }


       L1PolyMorph.doPoly((L1Character)pc, polyId, 1800, 4, false, false);

       usePoly = true;
     }
     if (usePoly) {
       for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer((L1Object)this._attacker)) {
         if (pc == null)
           continue;
         pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 230));
         pc.broadcastPacket((ServerBasePacket)new S_SkillSound(pc.getId(), 230));
       }

       S_DoActionGFX gfx = new S_DoActionGFX(this._attacker.getId(), 19);
       this._attacker.broadcastPacket((ServerBasePacket)gfx);

       this._sleepTime = this._attacker.getCurrentSpriteInterval(EActionCodes.spell_nodir);
     }

     return usePoly;
   }

   private boolean magicAttack(int idx) {
     L1SkillUse skillUse = new L1SkillUse();
     int skillid = getMobSkillTemplate().getSkillId(idx);
     boolean canUseSkill = false;
     if (this._attacker.hasSkillEffect(64)) {
       return false;
     }

     if ((skillid >= 22020 && skillid <= 22029) || (skillid >= 22041 && skillid <= 22052) || (skillid >= 7001 && skillid <= 7050)) {

       if (this._attacker.hasSkillEffect(22033)) {
         return false;
       }
       this._attacker.setSkillEffect(22033, 10000L);
     }


     int npcId = this._attacker.getNpcTemplate().get_npcId();
     switch (npcId) {
       case 45529:
       case 45617:
       case 7320219:
         if (skillid == 707049 || skillid == 707050 || skillid == 707051 || skillid == 707052) {
           boolean isuse = false;
           for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer((L1Object)this._attacker)) {
             if (pc == null || pc.isDead()) {
               continue;
             }
             if (skillUse.checkUseSkill(null, skillid, pc.getId(), pc.getX(), pc.getY(), null, 0, 0, (L1Character)this._attacker)) {
               isuse = true;
               skillUse.handleCommands(null, skillid, pc.getId(), pc.getX(), pc.getY(), null, 0, 0, (L1Character)this._attacker);
             }

             if (isuse) {
               if (getMobSkillTemplate().getLeverage(idx) > 0) {
                 skillUse.setLeverage(getMobSkillTemplate().getLeverage(idx));
               }
               this._sleepTime = this._attacker.getCurrentSpriteInterval(EActionCodes.spell_dir);
             }
           }
           return true;
         }
         break;
     }



     if (skillid > 0) {
       canUseSkill = skillUse.checkUseSkill(null, skillid, this._target.getId(), this._target.getX(), this._target.getY(), null, 0, 0, (L1Character)this._attacker);
     }
     if (canUseSkill == true) {
       if (getMobSkillTemplate().getLeverage(idx) > 0) {
         skillUse.setLeverage(getMobSkillTemplate().getLeverage(idx));
       }

       if (this._target instanceof l1j.server.server.model.Instance.L1CastleGuardInstance) {
         return false;
       }
       skillUse.handleCommands(null, skillid, this._target.getId(), this._target.getX(), this._target.getY(), null, 0, 0, (L1Character)this._attacker);
       L1Skills skill = SkillsTable.getInstance().getTemplate(skillid);
       if (skill.getTarget().equals("attack") && skillid != 18) {
         this._sleepTime = this._attacker.getCurrentSpriteInterval(EActionCodes.spell_dir);
       } else {
         this._sleepTime = this._attacker.getCurrentSpriteInterval(EActionCodes.spell_nodir);
       }

       return true;
     }
     return false;
   }

   private boolean physicalAttack(int idx) {
     Map<Integer, Integer> targetList = new ConcurrentHashMap<>();
     int areaWidth = getMobSkillTemplate().getAreaWidth(idx);
     int areaHeight = getMobSkillTemplate().getAreaHeight(idx);
     int range = getMobSkillTemplate().getRange(idx);
     int actId = getMobSkillTemplate().getActid(idx);
     int gfxId = getMobSkillTemplate().getGfxid(idx);

     if (this._attacker.getLocation().getTileLineDistance(this._target.getLocation()) > range) {
       return false;
     }

     if (!this._attacker.glanceCheck(this._target.getX(), this._target.getY())) {
       return false;
     }

     this._attacker.setHeading(this._attacker.targetDirection(this._target.getX(), this._target.getY()));

     if (areaHeight > 0) {
       L1Character cha = null;
       ArrayList<L1Object> olist = L1World.getInstance().getVisibleBoxObjects((L1Object)this._attacker, this._attacker.getHeading(), areaWidth, areaHeight);
       if (olist != null) {
         for (L1Object obj : olist) {
           if (obj == null || !(obj instanceof L1Character)) {
             continue;
           }
           cha = (L1Character)obj;
           if (cha.isDead()) {
             continue;
           }
           if (!this._attacker.glanceCheck(cha.getX(), cha.getY())) {
             continue;
           }
           if (this._target instanceof L1PcInstance || this._target instanceof l1j.server.server.model.Instance.L1SummonInstance || this._target instanceof l1j.server.server.model.Instance.L1PetInstance || this._target instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance) {
             if (cha instanceof L1PcInstance) {
               L1PcInstance pc = (L1PcInstance)cha;
               if (pc.isGhost() || pc.isGmInvis()) {
                 continue;
               }
               if ((this._attacker instanceof l1j.server.server.model.Instance.L1SummonInstance || this._attacker instanceof l1j.server.server.model.Instance.L1PetInstance || this._target instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance) && (
                 cha.getId() == this._attacker.getMaster().getId() || cha.getZoneType() == 1)) {
                 continue;
               }
             } else if (!(obj instanceof l1j.server.server.model.Instance.L1SummonInstance) && !(obj instanceof l1j.server.server.model.Instance.L1PetInstance) && !(obj instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance)) {
               continue;
             }
             targetList.put(Integer.valueOf(obj.getId()), Integer.valueOf(0)); continue;
           }
           if (obj instanceof L1MonsterInstance) {
             targetList.put(Integer.valueOf(obj.getId()), Integer.valueOf(0));
           }
         }
       }
     } else {

       targetList.put(Integer.valueOf(this._target.getId()), Integer.valueOf(0));
     }

     if (targetList.size() == 0) {
       return false;
     }

     Iterator<Integer> ite = targetList.keySet().iterator();
     L1Attack attack = null;

     EActionCodes actionCode = EActionCodes.attack;
     while (ite.hasNext()) {
       int targetId = ((Integer)ite.next()).intValue();
       actionCode = EActionCodes.attack;




       if ((L1Character)L1World.getInstance().findObject(targetId) == null) {
         continue;
       }
       attack = new L1Attack((L1Character)this._attacker, (L1Character)L1World.getInstance().findObject(targetId));
       if (attack.calcHit()) {
         if (getMobSkillTemplate().getLeverage(idx) > 0) {
           attack.setLeverage(getMobSkillTemplate().getLeverage(idx));
         }
         attack.calcDamage();
       }
       if (actId > 0) {
         attack.setActId(actId);
       }

       if (targetId == this._target.getId()) {
         if (gfxId > 0) {
           S_SkillSound pck; switch (this._attacker.getNpcId()) {
             case 45263:
             case 7320176:
             case 7320180:
             case 7320182:
               this._attacker.broadcastPacket((ServerBasePacket)new S_DoActionGFX(this._attacker.getId(), 18));
               pck = new S_SkillSound(this._target.getId(), gfxId);
               this._target.broadcastPacket((ServerBasePacket)pck, false);
               this._target.sendPackets((ServerBasePacket)pck, true);







               actionCode = EActionCodes.spell_dir;
               break;
             default:
               this._attacker.broadcastPacket((ServerBasePacket)new S_SkillSound(this._attacker.getId(), gfxId));
               break;
           }
         }
         attack.action();
       }
       attack.commit();
     }

     this._sleepTime = this._attacker.getCurrentSpriteInterval(actionCode);
     return true;
   }

   private boolean isSkillUseble(int skillIdx) {
     boolean useble = false;

     if (getMobSkillTemplate().getTriggerRandom(skillIdx) > 0) {
       int chance = _rnd.nextInt(100) + 1;
       if (chance < getMobSkillTemplate().getTriggerRandom(skillIdx)) {
         useble = true;
       } else {
         return false;
       }
     }

     if (getMobSkillTemplate().getTriggerHp(skillIdx) > 0) {
       int hpRatio = this._attacker.getCurrentHp() * 100 / this._attacker.getMaxHp();
       if (hpRatio <= getMobSkillTemplate().getTriggerHp(skillIdx)) {
         useble = true;
       } else {
         return false;
       }
     }

     if (getMobSkillTemplate().getTriggerCompanionHp(skillIdx) > 0) {
       L1NpcInstance companionNpc = searchMinCompanionHp();
       if (companionNpc == null) {
         return false;
       }

       int hpRatio = companionNpc.getCurrentHp() * 100 / companionNpc.getMaxHp();
       if (hpRatio <= getMobSkillTemplate().getTriggerCompanionHp(skillIdx)) {
         useble = true;
         this._target = (L1Character)companionNpc;
       } else {
         return false;
       }
     }

     if (getMobSkillTemplate().getTriggerRange(skillIdx) != 0) {
       int distance = this._attacker.getLocation().getTileLineDistance(this._target.getLocation());

       if (getMobSkillTemplate().isTriggerDistance(skillIdx, distance)) {
         useble = true;
       } else {
         return false;
       }
     }

     if (getMobSkillTemplate().getTriggerCount(skillIdx) > 0) {
       if (getSkillUseCount(skillIdx) < getMobSkillTemplate().getTriggerCount(skillIdx)) {
         useble = true;
       } else {
         return false;
       }
     }
     return useble;
   }


   private L1NpcInstance searchMinCompanionHp() {
     L1NpcInstance minHpNpc = null;
     int hpRatio = 100;

     int family = this._attacker.getNpcTemplate().get_family();

     for (L1Object object : L1World.getInstance().getVisibleObjects((L1Object)this._attacker)) {
       if (object == null)
         continue;
       if (object instanceof L1NpcInstance) {
         L1NpcInstance npc = (L1NpcInstance)object;
         if (npc.getNpcTemplate().get_family() == family) {
           int companionHpRatio = npc.getCurrentHp() * 100 / npc.getMaxHp();
           if (companionHpRatio < hpRatio) {
             hpRatio = companionHpRatio;
             minHpNpc = npc;
           }
         }
       }
     }
     return minHpNpc;
   }



   private void mobspawn(int summonId, int count) {
     for (int i = 0; i < count; i++) {
       mobspawn(summonId);
     }
   }

   private void mobspawn(int summonId) {
     try {
       L1Npc spawnmonster = NpcTable.getInstance().getTemplate(summonId);
       if (spawnmonster != null) {
         L1NpcInstance mob = null;
         try {
           String implementationName = spawnmonster.getImpl();



           Constructor<?> _constructor = Class.forName("l1j.server.server.model.Instance." + implementationName + "Instance").getConstructors()[0];
           mob = (L1NpcInstance)_constructor.newInstance(new Object[] { spawnmonster });
           mob.setId(IdFactory.getInstance().nextId());
           L1Location loc = this._attacker.getLocation().randomLocation(8, false);
           int heading = _rnd.nextInt(8);
           mob.setX(loc.getX());
           mob.setY(loc.getY());
           mob.setHomeX(loc.getX());
           mob.setHomeY(loc.getY());
           short mapid = this._attacker.getMapId();
           mob.setMap(mapid);
           mob.setHeading(heading);
           L1World.getInstance().storeObject((L1Object)mob);
           L1World.getInstance().addVisibleObject((L1Object)mob);
           L1Object object = L1World.getInstance().findObject(mob.getId());
           L1MonsterInstance newnpc = (L1MonsterInstance)object;
           if (summonId == 45061 || summonId == 45161 || summonId == 45181 || summonId == 45455) {
             newnpc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(newnpc.getId(), 11));
             newnpc.setStatus(13);
             newnpc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream((L1NpcInstance)newnpc));

             newnpc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(newnpc.getId(), 4));
             newnpc.setStatus(0);
             newnpc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream((L1NpcInstance)newnpc));
           }

           newnpc.onNpcAI();
           newnpc.getLight().turnOnOffLight();
           newnpc.startChat(4);
           newnpc.startChat(0);
         } catch (Exception e) {
           e.printStackTrace();
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
     }
   }
   private L1Character changeTarget(int type, int idx) {
     L1NpcInstance l1NpcInstance;
     List<L1Character> targetList;
     L1Character cha;
     switch (type)
     { case 2:
         return (L1Character)this._attacker;

       case 3:
         targetList = new ArrayList<>();
         cha = null;
         for (L1Object obj : L1World.getInstance().getVisibleObjects((L1Object)this._attacker)) {
           if (obj == null)
             continue;
           if (obj instanceof L1PcInstance || obj instanceof l1j.server.server.model.Instance.L1PetInstance || obj instanceof l1j.server.server.model.Instance.L1SummonInstance || obj instanceof l1j.server.MJCompanion.Instance.MJCompanionInstance) {
             cha = (L1Character)obj;
             int distance = this._attacker.getLocation().getTileLineDistance(cha.getLocation());

             if (!getMobSkillTemplate().isTriggerDistance(idx, distance)) {
               continue;
             }

             if (!this._attacker.glanceCheck(cha.getX(), cha.getY())) {
               continue;
             }

             if (!this._attacker.getHateList().containsKey(cha)) {
               continue;
             }

             if (cha.isDead()) {
               continue;
             }

             if (cha instanceof L1PcInstance && (
               (L1PcInstance)cha).isGhost()) {
               continue;
             }

             targetList.add((L1Character)obj);
           }
         }

         if (targetList.size() == 0) {
           target = this._target;
         } else {
           int randomSize = targetList.size() * 100;
           int targetIndex = _rnd.nextInt(randomSize) / 100;
           target = targetList.get(targetIndex);
         }






         return target; }  L1Character target = this._target; return target;
   }
 }


