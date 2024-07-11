 package l1j.server.server.model.Instance;

 import java.util.concurrent.atomic.AtomicInteger;
 import l1j.server.Config;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.NPCTalkDataTable;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1NpcTalkData;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1Quest;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.serverpackets.S_ChangeHeading;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.types.Point;

 public class L1PeopleInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;
   private AtomicInteger _restCallCount;
   private static final long REST_MILLISEC = 10000L;

   public L1PeopleInstance(L1Npc template) {
     super(template);
     this._restCallCount = new AtomicInteger(0);
   }


   public void onAction(L1PcInstance pc) {
     if (pc == null || (getCurrentHp() <= 0 && isDead())) {
       return;
     }
     L1Attack attack = new L1Attack(pc, this);
     if (attack.calcHit()) {
       attack.calcDamage();
     }
     attack.action();
     attack.commit();
     attack = null;
   }


   public void onNpcAI() {
     if (isAiRunning()) {
       return;
     }
     setActived(false);
     startAI();
   }



   public void onTalkAction(L1PcInstance player) {
     int objid = getId();
     L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
     int npcid = getNpcTemplate().get_npcId();
     L1Quest quest = player.getQuest();
     String htmlid = null;
     String[] htmldata = null;

     int pcX = player.getX();
     int pcY = player.getY();
     int npcX = getX();
     int npcY = getY();

     long curtime = System.currentTimeMillis() / 1000L;
     if (player.getNpcActionTime() + 2L > curtime) {
       return;
     }
     player.setNpcActionTime(curtime);

     if (getNpcTemplate().getChangeHead()) {
       if (pcX == npcX && pcY < npcY) {
         setHeading(0);
       } else if (pcX > npcX && pcY < npcY) {
         setHeading(1);
       } else if (pcX > npcX && pcY == npcY) {
         setHeading(2);
       } else if (pcX > npcX && pcY > npcY) {
         setHeading(3);
       } else if (pcX == npcX && pcY > npcY) {
         setHeading(4);
       } else if (pcX < npcX && pcY > npcY) {
         setHeading(5);
       } else if (pcX < npcX && pcY == npcY) {
         setHeading(6);
       } else if (pcX < npcX && pcY < npcY) {
         setHeading(7);
       }
       broadcastPacket((ServerBasePacket)new S_ChangeHeading(this));



       if (this._restCallCount.getAndIncrement() == 0) {
         setRest(true);
       }

       GeneralThreadPool.getInstance().schedule(new RestMonitor(), 10000L);
     }

     L1SkillUse l1skilluse = new L1SkillUse();
     L1Object obj = L1World.getInstance().findObject(objid);
     String npcName = ((L1NpcInstance)obj).getNpcTemplate().get_name();

     if (talking != null) {
       switch (npcid) {

       }  htmlid = null;
       htmldata = null;



       if (htmlid != null) {
         if (htmldata != null) {
           player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, htmlid, htmldata));
         } else {
           player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, htmlid));
         }

       } else if (player.getLawful() < -1000) {
         player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 2));
       } else {
         player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 1));
       }
     }
   }





   public void receiveDamage(L1Character attacker, int damage) {
     if (getCurrentHp() > 0 && !isDead()) {
       if (damage > 0) {
         if (hasSkillEffect(66)) {
           removeSkillEffect(66);
         } else if (hasSkillEffect(212)) {
           removeSkillEffect(212);
         } else if (hasSkillEffect(103)) {
           removeSkillEffect(103);
         }
       }



       if (attacker instanceof L1PcInstance && damage > 0) {
         L1PcInstance pc = (L1PcInstance)attacker;
         pc.set_pet_target(this);
       }

       if (hasSkillEffect(5055)) {
         double presher_dmg = 0.0D;
         if (attacker == getPresherPc()) {
           presher_dmg = damage * Config.MagicAdSetting_Lancer.PRESHER_PCPCDMG;
         } else {
           presher_dmg = damage * Config.MagicAdSetting_Lancer.PRESHER_ETCPCDMG;
         }
         addPresherDamage((int)presher_dmg);
       }

       int newHp = getCurrentHp() - damage;
       if (newHp <= 0 && !isDead()) {
         setCurrentHp(0);
         setDead(true);
         setStatus(8);
         Death death = new Death(attacker);
         GeneralThreadPool.getInstance().execute(death);
       }
       if (newHp > 0) {
         setCurrentHp(newHp);
       }
     } else if ((getCurrentHp() != 0 || isDead()) &&
       !isDead()) {
       setDead(true);
       setStatus(8);
       Death death = new Death(attacker);
       GeneralThreadPool.getInstance().execute(death);
     }
   }

   class Death implements Runnable {
     L1Character _lastAttacker;

     public Death(L1Character lastAttacker) {
       this._lastAttacker = lastAttacker;
     }


     public void run() {
       try {
         if (L1PeopleInstance.this.hasSkillEffect(5055)) {
           L1PeopleInstance.this.setPresherPc(null);
           L1PeopleInstance.this.setPresherDamage(0);

           if (L1PeopleInstance.this.getPresherDeathRecall()) {
             L1PeopleInstance.this.setPresherDeathRecall(false);
           }
           L1PeopleInstance.this.removeSkillEffect(5055);
         }

         L1PeopleInstance.this.setDeathProcessing(true);
         L1PeopleInstance.this.setCurrentHp(0);
         L1PeopleInstance.this.setDead(true);
         L1PeopleInstance.this.setStatus(8);
         L1PeopleInstance.this.getMap().setPassable((Point)L1PeopleInstance.this.getLocation(), true);
         Broadcaster.broadcastPacket(L1PeopleInstance.this, (ServerBasePacket)new S_DoActionGFX(L1PeopleInstance.this.getId(), 8), true);
         L1PeopleInstance.this.startChat(1);

         int lawful = 5000;
         if (lawful > 0) {
           this._lastAttacker.addLawful(-lawful);
         }
         L1PeopleInstance.this.setDeathProcessing(false);
         L1PeopleInstance.this.allTargetClear();
         L1PeopleInstance.this.startDeleteTimer();
       } catch (Exception e) {
         e.printStackTrace();
       }
     }
   }

   class RestMonitor
     implements Runnable {
     public void run() {
       if (L1PeopleInstance.this._restCallCount.decrementAndGet() == 0)
         L1PeopleInstance.this.setRest(false);
     }
   }
 }


