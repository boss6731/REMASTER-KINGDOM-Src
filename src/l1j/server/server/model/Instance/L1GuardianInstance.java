 package l1j.server.server.model.Instance;

 import java.util.ArrayList;
 import java.util.Random;
 import java.util.concurrent.atomic.AtomicInteger;
 import l1j.server.Config;
 import l1j.server.MJCompanion.Instance.MJCompanionInstance;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.NPCTalkDataTable;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1NpcTalkData;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ChangeHeading;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_Karma;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.S_NpcChatPacket;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.types.Point;
 import l1j.server.server.utils.CalcExp;

 public class L1GuardianInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;
   private Random _random = new Random(System.nanoTime());
   private L1Character _lastattacker;
   private static final long REST_MILLISEC = 10000L;
   private AtomicInteger _restCallCount;

   public L1GuardianInstance(L1Npc template) {
     super(template);
     this._restCallCount = new AtomicInteger(0);
   }


   public void searchTarget() {
     L1PcInstance targetPlayer = null;

     for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer((L1Object)this)) {
       if (pc == null || pc.getCurrentHp() <= 0 || pc.isDead() || pc.isGm() || pc.isGhost()) {
         continue;
       }
       if ((!pc.isInvisble() || getNpcTemplate().is_agrocoi()) &&
         !pc.isElf()) {
         targetPlayer = pc;
         wideBroadcastPacket((ServerBasePacket)new S_NpcChatPacket(this, "$804", 2));

         break;
       }
     }
     if (targetPlayer != null) {
       this._hateList.add(targetPlayer, 0);
       this._target = targetPlayer;
     }
   }



   public void setLink(L1Character cha) {
     if (cha != null && this._hateList.isEmpty()) {
       this._hateList.add(cha, 0);
       checkTarget();
     }
   }


   public void onNpcAI() {
     if (isAiRunning()) {
       return;
     }
     setActived(false);
     startAI();
   }


   public void onAction(L1PcInstance player) {
     if (this == null || player == null)
       return;
     if (player.getType() == 2 && player.getCurrentWeapon() == 0 && player.isElf()) {
       L1Attack attack = new L1Attack(player, this);

       if (attack.calcHit()) {
         if (getNpcTemplate().get_npcId() == 70848) {
           int chance = this._random.nextInt(100) + 1;
           if (chance <= 10) {
             player.getInventory().storeItem(40506, 1);
             player.sendPackets((ServerBasePacket)new S_ServerMessage(143, "$755", "$794"));
           } else if (chance <= 60 && chance > 10) {
             player.getInventory().storeItem(40507, 1);
             player.sendPackets((ServerBasePacket)new S_ServerMessage(143, "$755", "$763"));
           } else if (chance <= 70 && chance > 60) {
             player.getInventory().storeItem(40505, 1);
             player.sendPackets((ServerBasePacket)new S_ServerMessage(143, "$755", "$770"));
           }
         }
         if (getNpcTemplate().get_npcId() == 70850) {
           int chance = this._random.nextInt(100) + 1;
           if (chance <= 30) {
             player.getInventory().storeItem(40519, 5);
             player.sendPackets((ServerBasePacket)new S_ServerMessage(143, "$753", "$760 (5)"));
           }
         }
         if (getNpcTemplate().get_npcId() == 70846) {
           int chance = this._random.nextInt(100) + 1;
           if (chance <= 30) {
             player.getInventory().storeItem(40503, 1);
             player.sendPackets((ServerBasePacket)new S_ServerMessage(143, "$752", "$769"));
           }
         }
         attack.calcDamage();
         attack.addPcPoisonAttack(player, this);
       }
       attack.action();
       attack.commit();
     } else if (getCurrentHp() > 0 && !isDead()) {
       L1Attack attack = new L1Attack(player, this);
       if (attack.calcHit()) {
         attack.calcDamage();
         attack.addPcPoisonAttack(player, this);
       }
       attack.action();
       attack.commit();
     }
   }


   public void onTalkAction(L1PcInstance player) {
     if (player == null || this == null)
       return;
     int objid = getId();
     L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
     L1Object object = L1World.getInstance().findObject(getId());
     if (object == null)
       return;
     L1NpcInstance target = (L1NpcInstance)object;

     if (talking != null) {
       int pcx = player.getX();
       int pcy = player.getY();
       int npcx = target.getX();
       int npcy = target.getY();

       if (pcx == npcx && pcy < npcy) {
         setHeading(0);
       } else if (pcx > npcx && pcy < npcy) {
         setHeading(1);
       } else if (pcx > npcx && pcy == npcy) {
         setHeading(2);
       } else if (pcx > npcx && pcy > npcy) {
         setHeading(3);
       } else if (pcx == npcx && pcy > npcy) {
         setHeading(4);
       } else if (pcx < npcx && pcy > npcy) {
         setHeading(5);
       } else if (pcx < npcx && pcy == npcy) {
         setHeading(6);
       } else if (pcx < npcx && pcy < npcy) {
         setHeading(7);
       }
       broadcastPacket((ServerBasePacket)new S_ChangeHeading(this));

       if (player.getLawful() < -1000) {
         player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 2));
       } else {
         player.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 1));
       }

       if (this._restCallCount.getAndIncrement() == 0) {
         setRest(true);
       }

       GeneralThreadPool.getInstance().schedule(new RestMonitor(), 10000L);
     }
   }


   public void receiveDamage(L1Character attacker, int damage) {
     if (this == null || attacker == null)
       return;
     if (attacker instanceof L1PcInstance && damage > 0) {
       L1PcInstance pc = (L1PcInstance)attacker;
       if (pc.getType() != 2 || pc.getCurrentWeapon() != 0)
       {
         if (getCurrentHp() > 0 && !isDead()) {
           if (damage >= 0) {
             setHate(attacker, damage);
           }
           if (damage > 0) {
             if (hasSkillEffect(66)) {
               removeSkillEffect(66);
             } else if (hasSkillEffect(212)) {
               removeSkillEffect(212);
             }
           }
           onNpcAI();
           serchLink(pc, getNpcTemplate().get_family());
           if (damage > 0) {
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
             this._lastattacker = attacker;
             Death death = new Death();
             GeneralThreadPool.getInstance().execute(death);
           }
           if (newHp > 0) {
             setCurrentHp(newHp);
           }
         } else if (!isDead()) {
           setDead(true);
           setStatus(8);
           this._lastattacker = attacker;
           Death death = new Death();
           GeneralThreadPool.getInstance().execute(death);
         }
       }
     }
   }


   public void setCurrentHp(int i) {
     super.setCurrentHp(i);

     if (getMaxHp() > getCurrentHp()) {
       startHpRegeneration();
     }
   }


   public void setCurrentMp(int i) {
     super.setCurrentMp(i);

     if (getMaxMp() > getCurrentMp()) {
       startMpRegeneration();
     }
   }

   class Death
     implements Runnable
   {
     L1Character lastAttacker = L1GuardianInstance.this._lastattacker;

     public void run() {
       if (L1GuardianInstance.this.hasSkillEffect(5055)) {
         L1GuardianInstance.this.setPresherPc(null);
         L1GuardianInstance.this.setPresherDamage(0);

         if (L1GuardianInstance.this.getPresherDeathRecall()) {
           L1GuardianInstance.this.setPresherDeathRecall(false);
         }
         L1GuardianInstance.this.removeSkillEffect(5055);
       }

       L1GuardianInstance.this.setDeathProcessing(true);
       L1GuardianInstance.this.setCurrentHp(0);
       L1GuardianInstance.this.setDead(true);
       L1GuardianInstance.this.setStatus(8);
       int targetobjid = L1GuardianInstance.this.getId();
       L1GuardianInstance.this.getMap().setPassable((Point)L1GuardianInstance.this.getLocation(), true);
       L1GuardianInstance.this.broadcastPacket((ServerBasePacket)new S_DoActionGFX(targetobjid, 8));

       L1PcInstance player = null;
       if (this.lastAttacker instanceof L1PcInstance) {
         player = (L1PcInstance)this.lastAttacker;
       } else if (this.lastAttacker instanceof MJCompanionInstance) {
         player = ((MJCompanionInstance)this.lastAttacker).get_master();
       } else if (this.lastAttacker instanceof L1PetInstance) {
         player = ((L1PetInstance)this.lastAttacker).getMaster();
       } else if (this.lastAttacker instanceof L1SummonInstance) {
         player = (L1PcInstance)((L1SummonInstance)this.lastAttacker).getMaster();
       }
       if (player != null) {
         ArrayList<L1Character> targetList = L1GuardianInstance.this._hateList.toTargetArrayList();
         ArrayList<Integer> hateList = L1GuardianInstance.this._hateList.toHateArrayList();
         long exp = L1GuardianInstance.this.get_exp();
         CalcExp.calcExp(player, targetobjid, targetList, hateList, exp);



         player.addKarma((int)(L1GuardianInstance.this.getKarma() * Config.ServerRates.RateKarma));
         player.sendPackets((ServerBasePacket)new S_Karma(player));
       }
       L1GuardianInstance.this.setDeathProcessing(false);

       L1GuardianInstance.this.setKarma(0);
       L1GuardianInstance.this.setLawful(0);
       L1GuardianInstance.this.set_exp(0L);
       L1GuardianInstance.this.allTargetClear();

       L1GuardianInstance.this.startDeleteTimer();
     }
   }



   public void onFinalAction(L1PcInstance player, String action) {}



   public void doFinalAction(L1PcInstance player) {}



   public class RestMonitor
     implements Runnable
   {
     public void run() {
       if (L1GuardianInstance.this._restCallCount.decrementAndGet() == 0)
         L1GuardianInstance.this.setRest(false);
     }
   }
 }


