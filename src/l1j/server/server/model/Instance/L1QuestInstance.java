 package l1j.server.server.model.Instance;

 import java.util.concurrent.atomic.AtomicInteger;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.serverpackets.S_ChangeHeading;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;

















 public class L1QuestInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;
   private static final long REST_MILLISEC = 10000L;
   private AtomicInteger _restCallCount;

   public L1QuestInstance(L1Npc template) {
     super(template);
     this._restCallCount = new AtomicInteger(0);
   }


   public void onNpcAI() {
     int npcId = getNpcTemplate().get_npcId();
     if (isAiRunning()) {
       return;
     }
     if (npcId == 71075 || npcId == 70957 || npcId == 81209) {
       return;
     }
     setActived(false);
     startAI();
   }



   public void onAction(L1PcInstance pc) {
     L1Attack attack = new L1Attack(pc, this);
     if (attack.calcHit()) {
       attack.calcDamage();
       attack.addPcPoisonAttack(pc, this);
     }
     attack.action();
     attack.commit();
   }


   public void onTalkAction(L1PcInstance pc) {
     int pcX = pc.getX();
     int pcY = pc.getY();
     int npcX = getX();
     int npcY = getY();

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

     int npcId = getNpcTemplate().get_npcId();
     if (npcId == 71092 || npcId == 71093) {
       if (pc.isKnight() && pc.getQuest().get_step(3) == 4) {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "searcherk1"));
       } else {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "searcherk4"));
       }
     } else if (npcId == 71094) {
       if (pc.isDarkelf() && pc.getQuest().get_step(4) == 1) {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "endiaq1"));
       } else {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "endiaq4"));
       }
     } else if (npcId == 71062) {
       if (pc.getQuest().get_step(31) == 2) {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "kamit1b"));
       } else {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "kamit1"));
       }
     } else if (npcId == 71075) {
       if (pc.getQuest().get_step(34) == 1) {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "llizard1b"));
       } else {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "llizard1a"));
       }
     } else if (npcId == 70957 || npcId == 81209) {
       if (pc.getQuest().get_step(38) != 1) {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "roi1"));
       } else {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "roi2"));
       }
     }



     if (this._restCallCount.getAndIncrement() == 0) {
       setRest(true);
     }

     GeneralThreadPool.getInstance().schedule(new RestMonitor(), 10000L);
   }


   public void onFinalAction(L1PcInstance pc, String action) {
     if (action.equalsIgnoreCase("start")) {
       int npcId = getNpcTemplate().get_npcId();
       L1FollowerInstance follow = null;
       if ((npcId == 71092 || npcId == 71093) && pc.isKnight() && pc.getQuest().get_step(3) == 4) {
         L1Npc l1npc = NpcTable.getInstance().getTemplate(71093);
         follow = new L1FollowerInstance(l1npc, this, pc);
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), ""));
       } else if (npcId == 71094 && pc.isDarkelf() && pc.getQuest().get_step(4) == 1) {
         L1Npc l1npc = NpcTable.getInstance().getTemplate(71094);
         follow = new L1FollowerInstance(l1npc, this, pc);
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), ""));
       } else if (npcId == 71062 && pc.getQuest().get_step(31) == 2) {
         L1Npc l1npc = NpcTable.getInstance().getTemplate(71062);

         if (pc.getFollowerList().size() < 1) {
           follow = new L1FollowerInstance(l1npc, this, pc);
         }

         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), ""));
       } else if (npcId == 71075 && pc.getQuest().get_step(34) == 1) {
         L1Npc l1npc = NpcTable.getInstance().getTemplate(71075);
         follow = new L1FollowerInstance(l1npc, this, pc);
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), ""));
       } else if (npcId == 70957 || npcId == 81209) {
         L1Npc l1npc = NpcTable.getInstance().getTemplate(70957);
         follow = new L1FollowerInstance(l1npc, this, pc);
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), ""));
       }
     }
   }




   public class RestMonitor
     implements Runnable
   {
     public void run() {
       if (L1QuestInstance.this._restCallCount.decrementAndGet() == 0)
         L1QuestInstance.this.setRest(false);
     }
   }
 }


