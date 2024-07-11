 package l1j.server.server.model.Instance;

 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.Comparator;
 import java.util.List;
 import java.util.PriorityQueue;
 import java.util.Random;
 import l1j.server.BonusDropSystem.BonusDropSystemInfo;
 import l1j.server.BonusDropSystem.BonusDropSystemLoader;
 import l1j.server.CPMWBQSystem.CPMWBQSystemProvider;
 import l1j.server.CPMWBQSystem.Database.CPMWBQNpcInfoTable;
 import l1j.server.CPMWBQSystem.info.CPMWBQReward;
 import l1j.server.Config;
 import l1j.server.DollBonusEventSystem.DollBonusEventInfo;
 import l1j.server.DollBonusEventSystem.DollBonusEventLoader;
 import l1j.server.FatigueProperty;
 import l1j.server.GameSystem.Colosseum.ColosseumTable;
 import l1j.server.GameSystem.Colosseum.L1Colosseum;
 import l1j.server.MJBookQuestSystem.BQSInformation;
 import l1j.server.MJBookQuestSystem.Loader.BQSCriteriaNpcMappedService;
 import l1j.server.MJBookQuestSystem.Loader.BQSLoadManager;
 import l1j.server.MJBookQuestSystem.UserSide.BQSCharacterData;
 import l1j.server.MJCompanion.Instance.MJCompanionInstance;
 import l1j.server.MJLuunDungeon.LuunDugeon;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.MJTemplate.Chain.KillChain.MJMonsterKillChain;
 import l1j.server.MJTemplate.Interface.MJMonsterDeathHandler;
 import l1j.server.MJTemplate.Interface.MJMonsterTransformHandler;
 import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
 import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.MJTemplate.ObjectEvent.MJObjectEventProvider;
 import l1j.server.MJTempleantique.MJempleantiqueController;
 import l1j.server.server.Account;
 import l1j.server.server.Controller.TimeMapController;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.AinhasadBonusMonsterTable;
 import l1j.server.server.datatables.ClanTable;
 import l1j.server.server.datatables.DoorSpawnTable;
 import l1j.server.server.datatables.DropTable;
 import l1j.server.server.datatables.ExpTable;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.datatables.NPCTalkDataTable;
 import l1j.server.server.datatables.NewNpcChatTable;
 import l1j.server.server.datatables.ServerCustomQuestTable;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1NewNpcChatTimer;
 import l1j.server.server.model.L1NpcTalkData;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1TransFormCheck;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_Karma;
 import l1j.server.server.serverpackets.S_Liquor;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_RemoveObject;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SkillBrave;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.CustomQuest;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.templates.L1NewNpcChat;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.templates.L1TimeMap;
 import l1j.server.server.types.Point;
 import l1j.server.server.utils.CalcExp;
 import l1j.server.server.utils.CommonUtil;
 import l1j.server.server.utils.L1SpawnUtil;
 import l1j.server.server.utils.MJCommons;









 public class L1MonsterInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;
   private static Random _random = new Random(System.nanoTime());

   private MJMonsterDeathHandler _deathHandler;
   private MJMonsterTransformHandler _transformHandler;
   private boolean _run_teleport = false;

   public L1MonsterInstance setDeathHandler(MJMonsterDeathHandler handler) {
     this._deathHandler = handler;
     return this;
   }

   public MJMonsterDeathHandler getDeathHandler() {
     return this._deathHandler;
   }

   public L1MonsterInstance setTransformHandler(MJMonsterTransformHandler handler) {
     this._transformHandler = handler;
     return this;
   }


   public void onItemUse() {
     if (!isActived() && this._target != null) {
       if (getLevel() <= 45) {
         useItem(1, 40);
       }

       if (getNpcTemplate().is_doppel() && this._target instanceof L1PcInstance) {
         L1PcInstance targetPc = (L1PcInstance)this._target;
         setName(this._target.getName());
         setNameId(this._target.getName());
         setTitle(this._target.getTitle());
         setTempLawful(this._target.getLawful());
         setCurrentSprite(targetPc.getCurrentSpriteId());
         for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)this)) {
           if (pc == null)
             continue;
           pc.sendPackets((ServerBasePacket)new S_RemoveObject((L1Object)this));
           pc.removeKnownObject((L1Object)this);
           pc.updateObject();
         }
       }
     }
     if (getCurrentHp() * 100 / getMaxHp() < 40) {
       useItem(0, 50);
     }
   }


   public void onPerceive(L1PcInstance perceivedFrom) {
     perceivedFrom.addKnownObject((L1Object)this);
     if (perceivedFrom.getAI() != null) {
       return;
     }
     if (0 < getCurrentHp()) {
       perceivedFrom.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this), true);

       if (getHiddenStatus() == 1) {
         perceivedFrom.sendPackets((ServerBasePacket)new S_DoActionGFX(getId(), 11), true);
       } else if (getHiddenStatus() == 2) {

         if (getCurrentSpriteId() == 15492 || getCurrentSpriteId() == 8055) {

           perceivedFrom.sendPackets((ServerBasePacket)new S_DoActionGFX(getId(), 13));
         } else {
           perceivedFrom.sendPackets((ServerBasePacket)new S_DoActionGFX(getId(), 44), true);
         }
       }  onNpcAI();
       getMap().setPassable((Point)getLocation(), true);
       if (getBraveSpeed() == 1) {
         perceivedFrom.sendPackets((ServerBasePacket)new S_SkillBrave(getId(), 1, 600000), true);
       }
     }
   }

   public static int[][] _classGfxId = new int[][] { { 0, 1 }, { 20553, 61 }, { 37, 138 }, { 20278, 20279 }, { 2786, 2796 }, { 6658, 6661 }, { 6671, 6650 }, { 20567, 20577 }, { 18520, 18499 }, { 19296, 19299 } }; private int _ubSealCount;
   private int _ubId;
   private boolean _isCurseMimic;

   public void searchTarget() {
     L1PcInstance targetPlayer = null;
     L1MonsterInstance targetMonster = null;

     Collection<L1PcInstance> col = L1World.getInstance().getVisiblePlayer((L1Object)this);
     if (col.size() > 0) {
       PriorityQueue<L1PcInstance> target_q = new PriorityQueue<>(col.size(), new TargetSorter());
       for (L1PcInstance pc : col) {
         if (pc == null || pc.getCurrentHp() <= 0 || pc.isDead() || pc.isGm() || pc.isMonitor() || pc.isGhost()) {
           continue;
         }

         int mapId = getMapId();
         if ((mapId == 88 || mapId == 98 || mapId == 92 || mapId == 91 || mapId == 95) && (
           !pc.isInvisble() || getNpcTemplate().is_agrocoi())) {
           target_q.offer(pc);

           continue;
         }

         if ((getNpcTemplate().getKarma() < 0 && pc.getKarmaLevel() >= 1) || (getNpcTemplate().getKarma() > 0 && pc.getKarmaLevel() <= -1)) {
           continue;
         }

         if (!getNpcTemplate().is_agro() && !getNpcTemplate().is_agrososc() && getNpcTemplate().is_agrogfxid1() < 0 && getNpcTemplate().is_agrogfxid2() < 0) {
           if (pc.getLawful() < -1000) {
             target_q.offer(pc);
           }

           continue;
         }
         if (pc.hasSkillEffect(97) && pc.isPassive(MJPassiveID.BLIND_HIDDING_ASSASSIN.toInt())) {
           continue;
         }


         if (!pc.isInvisble() || getNpcTemplate().is_agrocoi()) {
           if (pc.hasSkillEffect(67)) {
             if (getNpcTemplate().isAgro_lvl()) {
               if (pc.getLevel() > getNpcTemplate().get_level()) {
                 continue;
               }
               target_q.offer(pc);
               continue;
             }
             if (getNpcTemplate().is_agrososc()) {
               target_q.offer(pc); continue;
             }
           } else {
             if (getNpcTemplate().isAgro_lvl()) {
               if (pc.getLevel() > getNpcTemplate().get_level()) {
                 continue;
               }
               target_q.offer(pc);
               continue;
             }
             if (getNpcTemplate().is_agro()) {
               target_q.offer(pc);
               continue;
             }
           }
           if (getNpcTemplate().is_agrogfxid1() >= 0 && getNpcTemplate().is_agrogfxid1() <= 4) {
             if (_classGfxId[getNpcTemplate().is_agrogfxid1()][0] == pc.getCurrentSpriteId() || _classGfxId[getNpcTemplate().is_agrogfxid1()][1] == pc.getCurrentSpriteId()) {
               target_q.offer(pc);
               continue;
             }
           } else if (pc.getCurrentSpriteId() == getNpcTemplate().is_agrogfxid1()) {
             target_q.offer(pc);

             continue;
           }
           if (getNpcTemplate().is_agrogfxid2() >= 0 && getNpcTemplate().is_agrogfxid2() <= 4) {
             if (_classGfxId[getNpcTemplate().is_agrogfxid2()][0] == pc.getCurrentSpriteId() || _classGfxId[getNpcTemplate().is_agrogfxid2()][1] == pc.getCurrentSpriteId())
               target_q.offer(pc);
             continue;
           }
           if (pc.getCurrentSpriteId() == getNpcTemplate().is_agrogfxid2()) {
             target_q.offer(pc);
           }
         }
       }

       target_q.comparator();
       while (!target_q.isEmpty()) {
         L1PcInstance target = target_q.poll();
         if (target == null || target.isDead() ||
           this.backingTargets.contains(Integer.valueOf(target.getId()))) {
           continue;
         }
         targetPlayer = target;
       }
     }







     for (L1Object obj : L1World.getInstance().getVisibleObjects((L1Object)this)) {
       if (obj instanceof L1MonsterInstance) {
         L1MonsterInstance mon = (L1MonsterInstance)obj;
         if (mon.getHiddenStatus() != 0 || mon.isDead()) {
           continue;
         }
         if ((getNpcTemplate().get_npcId() == 7310130 || getNpcTemplate().get_npcId() == 7310131 || getNpcTemplate().get_npcId() == 7310132 ||
           getNpcTemplate().get_npcId() == 7310133) && (
           mon.getNpcTemplate().get_npcId() >= 5000119 || mon.getNpcTemplate().get_npcId() <= 5000126)) {
           targetMonster = mon;

           break;
         }
         if (getNpcTemplate().get_npcId() == 45570 && (
           mon.getNpcTemplate().get_npcId() == 45391 || mon.getNpcTemplate().get_npcId() == 45450 || mon.getNpcTemplate().get_npcId() == 45482 || mon.getNpcTemplate().get_npcId() == 45569 || mon
           .getNpcTemplate().get_npcId() == 45579 || mon.getNpcTemplate().get_npcId() == 45315 || mon.getNpcTemplate().get_npcId() == 45647)) {
           targetMonster = mon;

           break;
         }

         if (getNpcTemplate().get_npcId() == 45571 && (
           mon.getNpcTemplate().get_npcId() == 45391 || mon.getNpcTemplate().get_npcId() == 45450 || mon.getNpcTemplate().get_npcId() == 45482 || mon.getNpcTemplate().get_npcId() == 45569 || mon
           .getNpcTemplate().get_npcId() == 45579 || mon.getNpcTemplate().get_npcId() == 45315 || mon.getNpcTemplate().get_npcId() == 45647)) {
           targetMonster = mon;

           break;
         }

         if (getNpcTemplate().get_npcId() == 45582 && (
           mon.getNpcTemplate().get_npcId() == 45391 || mon.getNpcTemplate().get_npcId() == 45450 || mon.getNpcTemplate().get_npcId() == 45482 || mon.getNpcTemplate().get_npcId() == 45569 || mon
           .getNpcTemplate().get_npcId() == 45579 || mon.getNpcTemplate().get_npcId() == 45315 || mon.getNpcTemplate().get_npcId() == 45647)) {
           targetMonster = mon;

           break;
         }

         if (getNpcTemplate().get_npcId() == 45587 && (
           mon.getNpcTemplate().get_npcId() == 45391 || mon.getNpcTemplate().get_npcId() == 45450 || mon.getNpcTemplate().get_npcId() == 45482 || mon.getNpcTemplate().get_npcId() == 45569 || mon
           .getNpcTemplate().get_npcId() == 45579 || mon.getNpcTemplate().get_npcId() == 45315 || mon.getNpcTemplate().get_npcId() == 45647)) {
           targetMonster = mon;

           break;
         }

         if (getNpcTemplate().get_npcId() == 45605 && (
           mon.getNpcTemplate().get_npcId() == 45391 || mon.getNpcTemplate().get_npcId() == 45450 || mon.getNpcTemplate().get_npcId() == 45482 || mon.getNpcTemplate().get_npcId() == 45569 || mon
           .getNpcTemplate().get_npcId() == 45579 || mon.getNpcTemplate().get_npcId() == 45315 || mon.getNpcTemplate().get_npcId() == 45647)) {
           targetMonster = mon;

           break;
         }

         if (getNpcTemplate().get_npcId() == 45685 && (
           mon.getNpcTemplate().get_npcId() == 45391 || mon.getNpcTemplate().get_npcId() == 45450 || mon.getNpcTemplate().get_npcId() == 45482 || mon.getNpcTemplate().get_npcId() == 45569 || mon
           .getNpcTemplate().get_npcId() == 45579 || mon.getNpcTemplate().get_npcId() == 45315 || mon.getNpcTemplate().get_npcId() == 45647)) {
           targetMonster = mon;

           break;
         }

         if (getNpcTemplate().get_npcId() == 45391 && (
           mon.getNpcTemplate().get_npcId() == 45570 || mon.getNpcTemplate().get_npcId() == 45571 || mon.getNpcTemplate().get_npcId() == 45582 || mon.getNpcTemplate().get_npcId() == 45587 || mon
           .getNpcTemplate().get_npcId() == 45605)) {
           targetMonster = mon;

           break;
         }

         if (getNpcTemplate().get_npcId() == 45450 && (
           mon.getNpcTemplate().get_npcId() == 45570 || mon.getNpcTemplate().get_npcId() == 45571 || mon.getNpcTemplate().get_npcId() == 45582 || mon.getNpcTemplate().get_npcId() == 45587 || mon
           .getNpcTemplate().get_npcId() == 45605)) {
           targetMonster = mon;

           break;
         }

         if (getNpcTemplate().get_npcId() == 45482 && (
           mon.getNpcTemplate().get_npcId() == 45570 || mon.getNpcTemplate().get_npcId() == 45571 || mon.getNpcTemplate().get_npcId() == 45582 || mon.getNpcTemplate().get_npcId() == 45587 || mon
           .getNpcTemplate().get_npcId() == 45605)) {
           targetMonster = mon;

           break;
         }

         if (getNpcTemplate().get_npcId() == 45569 && (
           mon.getNpcTemplate().get_npcId() == 45570 || mon.getNpcTemplate().get_npcId() == 45571 || mon.getNpcTemplate().get_npcId() == 45582 || mon.getNpcTemplate().get_npcId() == 45587 || mon
           .getNpcTemplate().get_npcId() == 45605)) {
           targetMonster = mon;

           break;
         }

         if (getNpcTemplate().get_npcId() == 45579 && (
           mon.getNpcTemplate().get_npcId() == 45570 || mon.getNpcTemplate().get_npcId() == 45571 || mon.getNpcTemplate().get_npcId() == 45582 || mon.getNpcTemplate().get_npcId() == 45587 || mon
           .getNpcTemplate().get_npcId() == 45605)) {
           targetMonster = mon;

           break;
         }

         if (getNpcTemplate().get_npcId() == 45315 && (
           mon.getNpcTemplate().get_npcId() == 45570 || mon.getNpcTemplate().get_npcId() == 45571 || mon.getNpcTemplate().get_npcId() == 45582 || mon.getNpcTemplate().get_npcId() == 45587 || mon
           .getNpcTemplate().get_npcId() == 45605)) {
           targetMonster = mon;

           break;
         }

         if (getNpcTemplate().get_npcId() == 45647 && (
           mon.getNpcTemplate().get_npcId() == 45570 || mon.getNpcTemplate().get_npcId() == 45571 || mon.getNpcTemplate().get_npcId() == 45582 || mon.getNpcTemplate().get_npcId() == 45587 || mon
           .getNpcTemplate().get_npcId() == 45605)) {
           targetMonster = mon;

           break;
         }

         if (getNpcTemplate().get_npcId() == 8503173 && (
           mon.getNpcTemplate().get_npcId() == 8503172 || mon.getNpcTemplate().get_npcId() == 8503174)) {
           targetMonster = mon;

           break;
         }
         if (getNpcTemplate().get_npcId() == 8503174 && (
           mon.getNpcTemplate().get_npcId() == 8503172 || mon.getNpcTemplate().get_npcId() == 8503173)) {
           targetMonster = mon;

           break;
         }
         if (getNpcTemplate().get_npcId() == 8503176 &&
           mon.getNpcTemplate().get_npcId() == 8503172) {
           targetMonster = mon;

           break;
         }
         if (getNpcTemplate().get_npcId() == 8503177 &&
           mon.getNpcTemplate().get_npcId() == 8503178) {
           targetMonster = mon;

           break;
         }
       }
     }
     if (getNpcId() >= 50000226 && getNpcId() <= 50000252) {
       for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
         if (obj instanceof L1MonsterInstance) {
           L1MonsterInstance mon = (L1MonsterInstance)obj;
           if (mon.getNpcTemplate().get_npcId() == 50000220) {
             this._hateList.add(mon, 0);
             this._target = mon;

             return;
           }
         }
       }
     }
     if (getNpcId() == 50000002) {
       for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
         if (obj instanceof L1ScarecrowInstance) {
           L1ScarecrowInstance mon = (L1ScarecrowInstance)obj;
           if (mon.getNpcTemplate().get_npcId() == 50000005) {
             this._hateList.add(mon, 0);
             this._target = mon;

             return;
           }
         }
       }
     }
     if (getNpcId() == 50000004) {
       for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
         if (obj instanceof L1ScarecrowInstance) {
           L1ScarecrowInstance mon = (L1ScarecrowInstance)obj;
           if (mon.getNpcTemplate().get_npcId() == 50000003) {
             this._hateList.add(mon, 0);
             this._target = mon;

             return;
           }
         }
       }
     }
     if (getNpcId() == 50000006) {
       for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
         if (obj instanceof L1ScarecrowInstance) {
           L1ScarecrowInstance mon = (L1ScarecrowInstance)obj;
           if (mon.getNpcTemplate().get_npcId() == 50000007) {
             this._hateList.add(mon, 0);
             this._target = mon;

             return;
           }
         }
       }
     }
     if (getNpcId() == 50000008) {
       for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
         if (obj instanceof L1MonsterInstance) {
           L1MonsterInstance mon = (L1MonsterInstance)obj;
           if (mon.getNpcTemplate().get_npcId() == 50000009) {
             this._hateList.add(mon, 0);
             this._target = mon;

             return;
           }
         }
       }
     }
     if (getNpcId() == 50000010) {
       for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
         if (obj instanceof L1MonsterInstance) {
           L1MonsterInstance mon = (L1MonsterInstance)obj;
           if (mon.getNpcTemplate().get_npcId() == 50000011) {
             this._hateList.add(mon, 0);
             this._target = mon;

             return;
           }
         }
       }
     }
     if (getNpcId() == 50000012) {
       for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
         if (obj instanceof L1MonsterInstance) {
           L1MonsterInstance mon = (L1MonsterInstance)obj;
           if (mon.getNpcTemplate().get_npcId() == 50000013) {
             this._hateList.add(mon, 0);
             this._target = mon;

             return;
           }
         }
       }
     }
     if (getNpcId() == 50000014) {
       for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
         if (obj instanceof L1MonsterInstance) {
           L1MonsterInstance mon = (L1MonsterInstance)obj;
           if (mon.getNpcTemplate().get_npcId() == 50000015) {
             this._hateList.add(mon, 0);
             this._target = mon;

             return;
           }
         }
       }
     }
     if (getNpcId() == 50000009) {
       for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
         if (obj instanceof L1MonsterInstance) {
           L1MonsterInstance mon = (L1MonsterInstance)obj;
           if (mon.getNpcTemplate().get_npcId() == 50000008) {
             this._hateList.add(mon, 0);
             this._target = mon;

             return;
           }
         }
       }
     }
     if (getNpcId() == 50000011) {
       for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
         if (obj instanceof L1MonsterInstance) {
           L1MonsterInstance mon = (L1MonsterInstance)obj;
           if (mon.getNpcTemplate().get_npcId() == 50000010) {
             this._hateList.add(mon, 0);
             this._target = mon;

             return;
           }
         }
       }
     }
     if (getNpcId() == 50000013) {
       for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
         if (obj instanceof L1MonsterInstance) {
           L1MonsterInstance mon = (L1MonsterInstance)obj;
           if (mon.getNpcTemplate().get_npcId() == 50000012) {
             this._hateList.add(mon, 0);
             this._target = mon;

             return;
           }
         }
       }
     }
     if (getNpcId() == 50000015) {
       for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
         if (obj instanceof L1MonsterInstance) {
           L1MonsterInstance mon = (L1MonsterInstance)obj;
           if (mon.getNpcTemplate().get_npcId() == 50000014) {
             this._hateList.add(mon, 0);
             this._target = mon;

             return;
           }
         }
       }
     }
     if (getMap().getBaseMapId() == 1936 && targetPlayer != null) {
       targetPlayer = null;
     }

     if (targetPlayer != null) {
       this._hateList.add(targetPlayer, 0);
       this._target = targetPlayer;
     }
     if (targetMonster != null) {
       this._hateList.add(targetMonster, 0);
       this._target = targetMonster;
     }
   }


   public void setLink(L1Character cha) {
     if (cha != null && this._hateList.isEmpty()) {
       this._hateList.add(cha, 0);
       checkTarget();
     }
   }

   public L1MonsterInstance(L1Npc template) {
     super(template);



     this._ubSealCount = 0;


     this._ubId = 0; synchronized (this) { if (template.get_npcId() == 707026) setStatus(6);  }
   }
   public void onNpcAI() { if (isAiRunning()) return;  setActived(false); startAI(); } public void onTalkAction(L1PcInstance pc) { if (pc == null) return;  int objid = getId(); L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId()); if (talking == null) { System.out.println(String.format("NPC Action을 찾을 수 없습니다. NPC ID : %d", new Object[] { Integer.valueOf(getNpcTemplate().get_npcId()) })); return; }  String htmlid = null; String[] htmldata = null; if (htmlid != null) { if (htmldata != null) { pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, htmlid, htmldata)); } else { pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, htmlid)); }  } else if (pc.getLawful() < -1000) { pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 2)); } else { pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 1)); }  } public void onAction(L1PcInstance pc) { if (pc == null) return;  if (getCurrentHp() > 0 && !isDead()) { L1Attack attack = new L1Attack(pc, this); if (attack.calcHit()) { attack.calcDamage(); attack.addPcPoisonAttack(pc, this); }  attack.action(); attack.commit(); }  } public void ReceiveManaDamage(L1Character attacker, int mpDamage) { if (attacker == null) return;  if (mpDamage > 0 && !isDead()) { setHate(attacker, mpDamage); onNpcAI(); if (attacker instanceof L1PcInstance) serchLink((L1PcInstance)attacker, getNpcTemplate().get_family());  int newMp = getCurrentMp() - mpDamage; if (newMp < 0) newMp = 0;  setCurrentMp(newMp); }  } public synchronized void receiveDamage(L1Character attacker, int damage) { if (attacker == null) return;  if (hasSkillEffect(5055)) { double presher_dmg = 0.0D; if (attacker == getPresherPc()) { presher_dmg = damage * Config.MagicAdSetting_Lancer.PRESHER_PCPCDMG; } else { presher_dmg = damage * Config.MagicAdSetting_Lancer.PRESHER_ETCPCDMG; }  addPresherDamage((int)presher_dmg); }  int newHp = getCurrentHp() - damage; if (newHp <= 0 && !isDead()) { if (attacker != null) setHate(attacker, damage);  setDead(true); if (getNpcTemplate().get_npcId() == 120910 && !(LuunDugeon.getInstance()).isactive) { L1DoorInstance door = null; for (L1Object object : L1World.getInstance().getObject()) { if (object instanceof L1DoorInstance) { door = (L1DoorInstance)object; if (door.getDoorId() == 2110 && door.getOpenStatus() != 28) door.open();  }  }  LuunDugeon.getInstance().LuunDungeonStart(); }  int transformId = getNpcTemplate().getTransformId(); if (transformId == -1) { if (attacker instanceof L1PcInstance) { L1PcInstance player = (L1PcInstance)attacker; if (HunterEventDoll(player)) { setDead(false); return; }  }  setCurrentHp(0); setStatus(8); if (this._deathHandler != null && this._deathHandler.onDeathNotify(this)) return;  Death death = new Death(attacker); GeneralThreadPool.getInstance().execute(death); L1TransFormCheck.removeTransFormList(getMapId(), getNpcTemplate().get_npcId()); } else { if (attacker instanceof L1PcInstance) { L1PcInstance player = (L1PcInstance)attacker; if (HunterEventDoll(player)) { setDead(false); return; }  }  if (transformId == 120699 || transformId == 120700 || transformId == 120701 || transformId == 120702 || transformId == 120703 || transformId == 120704 || transformId == 120705 || transformId == 120706 || transformId == 120707 || transformId == 120708) { if (MJRnd.isWinning(1000000, getNpcTemplate().getTransformProbability()) && getMap().isCrackIntheTower()) { if (getNpcTemplate().isTransformHard()) { if (L1TransFormCheck.isTransFormList(getMapId(), transformId)) { setCurrentHp(0); setStatus(8); if (this._deathHandler != null && this._deathHandler.onDeathNotify(this)) return;  Death death = new Death(attacker); GeneralThreadPool.getInstance().execute(death); L1TransFormCheck.removeTransFormList(getMapId(), getNpcTemplate().get_npcId()); } else { setDead(false); L1TransFormCheck.addTransFormList(getMapId(), transformId); transform(transformId, attacker); }  } else { setDead(false); transform(transformId, attacker); }  } else { setCurrentHp(0); setDead(true); setStatus(8); if (this._deathHandler != null && this._deathHandler.onDeathNotify(this)) return;  Death death = new Death(attacker); GeneralThreadPool.getInstance().execute(death); L1TransFormCheck.removeTransFormList(getMapId(), getNpcTemplate().get_npcId()); }  } else if (MJRnd.isWinning(1000000, getNpcTemplate().getTransformProbability())) { if (getNpcTemplate().isTransformHard()) { if (L1TransFormCheck.isTransFormList(getMapId(), transformId)) { setCurrentHp(0); setStatus(8); if (this._deathHandler != null && this._deathHandler.onDeathNotify(this)) return;  Death death = new Death(attacker); GeneralThreadPool.getInstance().execute(death); L1TransFormCheck.removeTransFormList(getMapId(), getNpcTemplate().get_npcId()); } else { setDead(false); L1TransFormCheck.addTransFormList(getMapId(), transformId); transform(transformId, attacker); }  } else { setDead(false); transform(transformId, attacker); }  } else { setCurrentHp(0); setStatus(8); if (this._deathHandler != null && this._deathHandler.onDeathNotify(this)) return;  Death death = new Death(attacker); GeneralThreadPool.getInstance().execute(death); L1TransFormCheck.removeTransFormList(getMapId(), getNpcTemplate().get_npcId()); }  }  if (attacker instanceof L1PcInstance) { L1PcInstance pc = (L1PcInstance)attacker; MJMonsterKillChain.getInstance().on_kill(pc, this); if (getNpcClassId() != 0 && !pc.noPlayerCK && BQSLoadManager.BQS_IS_ONUPDATE_BOOKS) { BQSInformation bqsInfo = BQSCriteriaNpcMappedService.service().findInformationFromNpcClassId(getNpcClassId()); BQSCharacterData bqs = pc.getBqs(); if (bqsInfo != null && bqs != null) bqs.onUpdate(bqsInfo);  }  if (CPMWBQNpcInfoTable.Get_Info(getNpcClassId()) != null && !pc.noPlayerCK && CPMWBQReward.service().use()) CPMWBQSystemProvider.provider().BQUpdateAmount(pc, this);  if (getNpcTemplate().get_npcId() == 7320012) { pc.setBuffnoch(1); pc.setCurrentMp(pc.getCurrentMp() + 100); pc.setBuffnoch(0); }  if (getNpcTemplate().get_npcId() == 7320013) { pc.setBuffnoch(1); (new L1SkillUse()).handleCommands(pc, 35, pc.getId(), pc.getX(), pc.getY(), null, 0, 4); pc.setBuffnoch(0); }  if (getNpcTemplate().get_npcId() == 7320014) { pc.setBuffnoch(1); (new L1SkillUse()).handleCommands(pc, 48, pc.getId(), pc.getX(), pc.getY(), null, 0, 4); pc.setBuffnoch(0); }  if (getNpcTemplate().get_npcId() == 7320015) { pc.setBuffnoch(1); (new L1SkillUse()).handleCommands(pc, 211, pc.getId(), pc.getX(), pc.getY(), null, 0, 4); pc.setBuffnoch(0); }  if (getNpcTemplate().get_npcId() == 7320016) { pc.setBuffnoch(1); (new L1SkillUse()).handleCommands(pc, 376, pc.getId(), pc.getX(), pc.getY(), null, 1200, 4); pc.setBuffnoch(0); }  if (getNpcTemplate().get_npcId() == 8500129 && getMapId() == 12862 && !(MJempleantiqueController.getInstance()).isopen && MJempleantiqueController.templeantique.use) { L1SpawnUtil.spawnnpc(getX(), getY(), getMapId(), 120718, 0, MJempleantiqueController.templeantique.gametime * 1000, 0); MJempleantiqueController.getInstance().Start(System.currentTimeMillis()); (ClanTable.getInstance()).TempleantiqueclanId = pc.getClanid(); L1World.getInstance().broadcastServerMessage("\\aH지배의 탑 정상에 균열의 틈새가 발생 되었습니다. 균열의 틈새를 찾으세요."); }  if (getNpcTemplate().get_npcId() == 7320017) { int time = 600; if (pc.hasSkillEffect(22017)) { pc.killSkillEffectTimer(22017); pc.sendPackets((ServerBasePacket)new S_Liquor(pc.getId(), 0)); pc.setPearl(0); }  pc.sendPackets((ServerBasePacket)new S_ServerMessage(1065, time)); pc.sendPackets((ServerBasePacket)new S_SkillSound(pc.getId(), 13283)); pc.broadcastPacket((ServerBasePacket)new S_SkillSound(pc.getId(), 13283)); pc.setSkillEffect(22017, (time * 1000)); pc.sendPackets((ServerBasePacket)new S_Liquor(pc.getId(), 8)); pc.broadcastPacket((ServerBasePacket)new S_Liquor(pc.getId(), 8)); pc.setPearl(1); }  }  }  if (getCurrentHp() > 0 && !isDead()) { if (getHiddenStatus() != 0) return;  if (damage >= 0 && !(attacker instanceof L1EffectInstance)) if (attacker instanceof L1MonsterInstance) { L1MonsterInstance mon = (L1MonsterInstance)attacker; if (mon.getNpcTemplate().get_npcId() != 8500138) setHate(attacker, damage);  } else { setHate(attacker, damage); }   if (damage > 0) if (hasSkillEffect(66)) { removeSkillEffect(66); } else if (hasSkillEffect(212)) { removeSkillEffect(212); }   onNpcAI(); if (attacker instanceof L1PcInstance) serchLink((L1PcInstance)attacker, getNpcTemplate().get_family());  if (attacker instanceof L1PcInstance && damage > 0) { L1PcInstance player = (L1PcInstance)attacker; player.set_pet_target(this); BonusDropSystemInfo bInfo = BonusDropSystemLoader.getInstance().getBonusDropSystemInfo(getNpcId()); if (bInfo != null && player.getBonusDropNpc() != getId()) player.setBonusDropNpc(getId());  if (newHp > 0 && getNpcTemplate().is_teleport_run() && 70 > _random.nextInt(100) && 100 * getCurrentHp() / getMaxHp() <= 50 && !this._run_teleport && RunTeleport(player.getX() + CommonUtil.random(-5, 5), player.getY() + CommonUtil.random(-5, 5)) == true) { this._run_teleport = true; broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this)); }  }  if (!this.Escape) { L1NewNpcChat npcChat = NewNpcChatTable.getInstance().getTemplateEscape(getNpcId()); if (npcChat != null) { int hpRatio = 100; if (0 < getMaxHp()) hpRatio = 100 * getCurrentHp() / getMaxHp();  if (hpRatio < npcChat.getMentChance()) { L1NewNpcChatTimer npcChatTimer; this.Escape = true; this.STATUS_Escape = true; if (!npcChat.isRepeat()) { npcChatTimer = new L1NewNpcChatTimer(this, npcChat); } else { npcChatTimer = new L1NewNpcChatTimer(this, npcChat, npcChat.getChatInterval()); }  npcChatTimer.startChat(); GeneralThreadPool.getInstance().schedule(new RunTimer(), 500L); }  }  }  if (newHp > 0) { setCurrentHp(newHp); hide(); }  } else if (!isDead()) { setDead(true); setStatus(8); if (this._deathHandler != null && this._deathHandler.onDeathNotify(this)) return;  Death death = new Death(attacker); GeneralThreadPool.getInstance().execute(death); }  if (attacker != null && FatigueProperty.getInstance().use_fatigue() && attacker instanceof L1PcInstance) { L1PcInstance attacker_pc = (L1PcInstance)attacker; Account account = attacker_pc.getAccount(); if (account != null && attacker_pc.getAI() == null && account.has_fatigue()) damage = (int)(damage - damage * FatigueProperty.getInstance().get_fatigue_effect_damage());  }  } class RunTimer implements Runnable {
     public void run() { try { Thread.sleep(10000L); L1MonsterInstance.this.STATUS_Escape = false; } catch (Exception e) { e.printStackTrace(); }  } } private boolean isDistance(L1PcInstance pc, int distance) { return (getLocation().getTileLineDistance((Point)pc.getLocation()) < distance); } public void giveAinhasadBonus(L1Character attacker, ArrayList<L1Character> hateList, Integer ainhasadBonus) { L1Character _attacker = attacker; ArrayList<L1Character> _hateList = hateList; int _ainhasadBonus = ainhasadBonus.intValue(); for (L1Character cha : _hateList) { if (cha.instanceOf(4)) { L1PcInstance pc = (L1PcInstance)cha; if (pc == null || pc.noPlayerCK || pc.noPlayerck2 || pc.noPlayerRobot || pc.isDead() || _attacker.getMapId() != pc.getMapId() || !isDistance(pc, 20)) continue;  pc.getAccount().addBlessOfAin(_ainhasadBonus * 10000, pc, "몬스터"); }  }  } public void setCurrentHp(int i) { super.setCurrentHp(i); if (getNpcId() == 707026 && i > 0) { int p = (int)(getCurrentHp() / getMaxHp() * 100.0D); if (getStatus() == 6 && p <= 90) { broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 4)); setStatus(0); }  }  if (getMaxHp() > getCurrentHp()) startHpRegeneration();  } public int getUbId() { return this._ubId; }
   public void setCurrentMp(int i) { super.setCurrentMp(i); if (getMaxMp() > getCurrentMp()) startMpRegeneration();  }
   public void deleteMe() { super.deleteMe(); }
   private void DominanceMonster(L1Character lastAttacker) { int chance = _random.nextInt(10000) + 1; int boss = 0; String name = lastAttacker.getName(); String bossName = ""; switch (getNpcId()) { case 8500040: case 8500041: case 8500042: case 8500043: case 8500045: case 8500046: case 8500047: case 8500048: case 8500050: case 8500051: case 8500052: case 8500054: case 8500055: case 8500056: case 8500057: case 8500058: case 8500059: case 8500062: case 8500064: case 8500065: case 8500066: case 8500067: case 8500069: case 8500070: case 8500073: case 8500074: case 8500075: case 8500077: case 8500078: case 8500079: case 8500080: case 8500082: case 8500083: case 8500084: case 8500087: case 8500089: case 8500091: case 8500092: case 8500094: case 8500096: case 8500097: case 8500098: case 8500099: case 8500101: case 8500102: case 8500103: case 8500104: case 8500106: case 8500107: case 8500108: case 8500109: case 8500110: case 8500112: case 8500113: case 8500114: case 8500115: case 8500116: case 8500117: case 8500118: if (chance < 3) { if (getMapId() == 12852) { boss = 8502001; bossName = "$12400 $26980"; } else if (getMapId() == 12853) { boss = 8502002; bossName = "$12401 $26980"; } else if (getMapId() == 12854) { boss = 8502003; bossName = "$12402 $26980"; } else if (getMapId() == 12855) { boss = 8502004; bossName = "$12403 $26980"; } else if (getMapId() == 12856) { boss = 8502005; bossName = "$12404 $26980"; } else if (getMapId() == 12857) { boss = 8502006; bossName = "$12405 $26980"; } else if (getMapId() == 12858) { boss = 8502007; bossName = "$12406 $26980"; } else if (getMapId() == 12859) { boss = 8502008; bossName = "$12407 $26980"; } else if (getMapId() == 12860) { boss = 8502009; bossName = "$12408 $26980"; } else if (getMapId() == 12861) { boss = 8502010; bossName = "$19939 $26980"; }  if (boss != 0) { L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_SystemMessage("\\fY" + bossName + ":\\aH'" + name + "'\\fY인간 주제에 나의 봉인을 풀어 주었구나..너에게 죽음을 선물하지..")); L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_PacketBox(84, "\\fY" + bossName + ":\\aH'" + name + "'\\fY인간 주제에 나의 봉인을 풀어 주었구나..너에게 죽음을 선물하지..")); L1SpawnUtil.spawn2(getX(), getY(), getMapId(), boss, 2, 1800000, 0); broadcastPacket((ServerBasePacket)new S_SkillSound(getId(), 4842)); break; }  System.out.println(String.format("%s 몬스터 %d에서 스폰됨.", new Object[] { getName(), Short.valueOf(getMapId()) })); }  break; }  } class Death implements Runnable {
     L1Character _lastAttacker; public Death(L1Character lastAttacker) { this._lastAttacker = lastAttacker; } public void run() { MJObjectEventProvider.provider().monsterEventFactory().fireMonsterKill(this._lastAttacker, L1MonsterInstance.this); switch (L1MonsterInstance.this.getNpcId()) { case 45263: case 7320176: case 7320180: case 7320182: for (L1Object obj : L1World.getInstance().getVisibleObjects((L1Object)L1MonsterInstance.this, 2)) { if (obj == null || !obj.instanceOf(4)) continue;  L1PcInstance pc = (L1PcInstance)obj; if (pc.isDead()) continue;  if (!MJCommons.isCounterMagic(pc)) pc.receiveDamage(L1MonsterInstance.this, MJRnd.next(200) + 200, 1);  }  break; }  if (L1MonsterInstance.this.hasSkillEffect(5055)) { L1MonsterInstance.this.setPresherPc(null); L1MonsterInstance.this.setPresherDamage(0); if (L1MonsterInstance.this.getPresherDeathRecall()) L1MonsterInstance.this.setPresherDeathRecall(false);  L1MonsterInstance.this.removeSkillEffect(5055); }  L1MonsterInstance.this.setDeathProcessing(true); L1MonsterInstance.this.setCurrentHp(0); L1MonsterInstance.this.setDead(true); L1MonsterInstance.this.setStatus(8); L1MonsterInstance.this.getMap().setPassable((Point)L1MonsterInstance.this.getLocation(), true); switch (L1MonsterInstance.this.getNpcId()) { case 8500142: case 8500143: case 8500144: L1MonsterInstance.this.broadcastPacket((ServerBasePacket)new S_DoActionGFX(L1MonsterInstance.this.getId(), 11)); break;default: L1MonsterInstance.this.broadcastPacket((ServerBasePacket)new S_DoActionGFX(L1MonsterInstance.this.getId(), 8)); break; }  L1MonsterInstance.this.startChat(1); if (this._lastAttacker != null) L1MonsterInstance.this.distributeExpDropKarma(this._lastAttacker);  L1MonsterInstance.this.giveUbSeal(); L1MonsterInstance.this.setDeathProcessing(false); L1MonsterInstance.this.set_exp(0L); L1MonsterInstance.this.setLawful(0); L1MonsterInstance.this.setKarma(0); L1MonsterInstance.this.startDeleteTimer(); L1MonsterInstance.this.DominanceMonster(this._lastAttacker); L1MonsterInstance.this.calcCombo(this._lastAttacker); if (this._lastAttacker != null && this._lastAttacker instanceof L1PcInstance) { L1PcInstance attacker_pc = (L1PcInstance)this._lastAttacker; if (FatigueProperty.getInstance().use_fatigue()) { Account account = attacker_pc.getAccount(); if (account != null && attacker_pc.getAI() == null) account.inc_fatigue_point(attacker_pc);  }  }  if (this._lastAttacker instanceof L1PcInstance) { L1PcInstance lat = (L1PcInstance)this._lastAttacker; List<CustomQuest> cq_list = ServerCustomQuestTable.getInstance().getCustomQuestListByMap(L1MonsterInstance.this.getMapId()); if (cq_list != null && cq_list.size() > 0) for (CustomQuest cq : cq_list) cq.result(lat);   }  if (L1MonsterInstance.this.getNpcTemplate().getDoor() > 0) { int doorId = L1MonsterInstance.this.getNpcTemplate().getDoor(); if (L1MonsterInstance.this.getNpcTemplate().getCountId() > 0) { int sleepTime = 7200; TimeMapController.getInstance().add(new L1TimeMap(L1MonsterInstance.this.getNpcTemplate().getCountId(), sleepTime, doorId)); }  L1DoorInstance door = DoorSpawnTable.getInstance().getDoor(doorId); synchronized (this) { if (door != null) door.open();  }  }  } } private void distributeExpDropKarma(L1Character lastAttacker) { if (lastAttacker == null) return;  L1PcInstance pc = null; if (lastAttacker instanceof L1PcInstance) { pc = (L1PcInstance)lastAttacker; } else if (lastAttacker instanceof MJCompanionInstance) { pc = ((MJCompanionInstance)lastAttacker).get_master(); } else if (lastAttacker instanceof L1PetInstance) { pc = ((L1PetInstance)lastAttacker).getMaster(); } else if (lastAttacker instanceof L1SummonInstance) { pc = (L1PcInstance)((L1SummonInstance)lastAttacker).getMaster(); }  if (pc != null && !pc.noPlayerCK && !pc.noPlayerck2 && !pc.isDead()) { ArrayList<L1Character> targetList = this._hateList.toTargetArrayList(); ArrayList<Integer> hateList = this._hateList.toHateArrayList(); Integer ainhasadBonus = AinhasadBonusMonsterTable.getInstance().getAinhasadBonus(getNpcTemplate().get_npcId()); if (ainhasadBonus != null) giveAinhasadBonus(lastAttacker, targetList, ainhasadBonus);  long exp = get_exp(); int level = pc.getLevel(); if (exp > 127L) level = 1;  long total = exp * level; if (level < 55) level = 55;  int levelgapm = level - getLevel(); if (levelgapm >= 5 && levelgapm <= 9) { total = (int)((exp * level) * 0.9D); } else if (levelgapm >= 10 && levelgapm <= 14) { total = (int)((exp * level) * 0.85D); } else if (levelgapm >= 15 && levelgapm <= 19) { total = (int)((exp * level) * 0.8D); } else if (levelgapm >= 20) { total = (int)((exp * level) * 0.7D); }  int levelgapt = getLevel() - level; if (levelgapt >= 5 && levelgapt <= 9) { total = (int)((exp * level) * 1.1D); } else if (levelgapt >= 10 && levelgapt <= 14) { total = (int)((exp * level) * 1.15D); } else if (levelgapt >= 15 && levelgapt <= 19) { total = (int)((exp * level) * 1.2D); } else if (levelgapt >= 20) { total = (int)((exp * level) * 1.3D); }  if (pc.isGm()) pc.sendPackets("\\f3[" + getName() + "]의 경험치는 (\\f2" + total + "\\f3) 입니다.\\f4(서버배율 미포함)");  if (isDead()) { distributeDrop(pc); giveKarma(pc); }  CalcExp.calcExp(pc, getId(), targetList, hateList, total); } else if (lastAttacker instanceof L1EffectInstance) { ArrayList<L1Character> targetList = this._hateList.toTargetArrayList(); ArrayList<Integer> hateList = this._hateList.toHateArrayList(); if (hateList.size() != 0) { int maxHate = 0; for (int i = hateList.size() - 1; i >= 0; i--) { if (maxHate < ((Integer)hateList.get(i)).intValue()) { maxHate = ((Integer)hateList.get(i)).intValue(); lastAttacker = targetList.get(i); }  }  if (lastAttacker instanceof L1PcInstance) { pc = (L1PcInstance)lastAttacker; } else if (lastAttacker instanceof MJCompanionInstance) { pc = ((MJCompanionInstance)lastAttacker).get_master(); } else if (lastAttacker instanceof L1PetInstance) { pc = ((L1PetInstance)lastAttacker).getMaster(); } else if (lastAttacker instanceof L1SummonInstance) { pc = (L1PcInstance)((L1SummonInstance)lastAttacker).getMaster(); }  long exp = get_exp(); int level = lastAttacker.getLevel(); if (exp > 127L) level = 1;  long total = exp * level; if (level < 55) level = 55;  int levelgapm = level - getLevel(); if (levelgapm >= 5 && levelgapm <= 9) { total = (int)((exp * level) * 0.9D); } else if (levelgapm >= 10 && levelgapm <= 14) { total = (int)((exp * level) * 0.85D); } else if (levelgapm >= 15 && levelgapm <= 19) { total = (int)((exp * level) * 0.8D); } else if (levelgapm >= 20) { total = (int)((exp * level) * 0.7D); }  int levelgapt = getLevel() - level; if (levelgapt >= 5 && levelgapt <= 9) { total = (int)((exp * level) * 1.1D); } else if (levelgapt >= 10 && levelgapt <= 14) { total = (int)((exp * level) * 1.15D); } else if (levelgapt >= 15 && levelgapt <= 19) { total = (int)((exp * level) * 1.2D); } else if (levelgapt >= 20) { total = (int)((exp * level) * 1.3D); }  if (isDead()) { distributeDrop(pc); giveKarma(pc); }  CalcExp.calcExp(pc, getId(), targetList, hateList, total); }  }  } private void distributeDrop(L1PcInstance pc) { ArrayList<L1Character> dropTargetList = this._dropHateList.toTargetArrayList(); ArrayList<Integer> dropHateList = this._dropHateList.toHateArrayList(); try { int npcId = getNpcTemplate().get_npcId(); if (npcId != 45640 || (npcId == 45640 && getCurrentSpriteId() == 2332)) DropTable.getInstance().dropShare(this, dropTargetList, dropHateList, pc);  } catch (Exception e) { e.printStackTrace(); }  } private void giveKarma(L1PcInstance pc) { int karma = getKarma(); if (karma != 0) { int karmaSign = Integer.signum(karma); int pcKarmaLevel = pc.getKarmaLevel(); int pcKarmaLevelSign = Integer.signum(pcKarmaLevel); if (pcKarmaLevelSign != 0 && karmaSign != pcKarmaLevelSign) karma *= 5;  pc.addKarma((int)(karma * Config.ServerRates.RateKarma)); pc.sendPackets((ServerBasePacket)new S_Karma(pc)); }  } private void giveUbSeal() { if (getUbSealCount() != 0) { L1Colosseum ub = ColosseumTable.getInstance().getUb(getUbId()); if (ub != null) for (L1PcInstance pc : ub.getMembersArray()) { int exp = 50000; int settingEXP = (int)Config.ServerRates.RateXp; double exppenalty = ExpTable.getPenaltyRate(pc.getLevel()); double addyExp = (getUbSealCount() * exp * settingEXP) * exppenalty; if (pc != null && !pc.isDead() && !pc.isGhost() && pc.getNetConnection() != null && !pc.noPlayerCK) { L1Item item = ItemTable.getInstance().getTemplate(L1Colosseum.Info.infinity_sign_id); pc.getInventory().storeItem(L1Colosseum.Info.infinity_sign_id, L1Colosseum.Info.infinity_sign_count); pc.sendPackets((ServerBasePacket)new S_SystemMessage(item.getName() + "을(를) " + L1Colosseum.Info.infinity_sign_count + "개를 획득 하셨습니다")); pc.send_effect(3944, true); pc.add_exp((int)addyExp); }  }   }  } public int getUbSealCount() { return this._ubSealCount; } public void setUbSealCount(int i) { this._ubSealCount = i; } public void setUbId(int i) { this._ubId = i; }


   private void hide() {
     int npcid = getNpcTemplate().get_npcId();
     if (npcid == 45061 || npcid == 45161 || npcid == 45181 || npcid == 45455) {
       if (getMaxHp() / 3 > getCurrentHp()) {
         int rnd = _random.nextInt(10);
         if (1 > rnd) {
           allTargetClear();
           setHiddenStatus(1);
           broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 11));
           setStatus(13);
           broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
         }
       }
     } else if (npcid == 45682) {
       if (getMaxHp() / 3 > getCurrentHp()) {
         int rnd = _random.nextInt(50);
         if (1 > rnd) {
           allTargetClear();
           setHiddenStatus(1);
           broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 20));
           setStatus(20);
           broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
         }
       }
     } else if (npcid == 45067 || npcid == 45264 || npcid == 45452 || npcid == 45090 || npcid == 45321 || npcid == 45445 || npcid == 75000) {
       if (getMaxHp() / 3 > getCurrentHp()) {
         int rnd = _random.nextInt(10);
         if (1 > rnd) {
           allTargetClear();
           setHiddenStatus(2);
           broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 44));
           setStatus(4);
           broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
         }
       }
     } else if (npcid == 45681 &&
       getMaxHp() / 3 > getCurrentHp()) {
       int rnd = _random.nextInt(50);
       if (1 > rnd) {
         allTargetClear();
         setHiddenStatus(2);
         broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 44));
         setStatus(11);
         broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
       }
     }
   }


   public void initHide() {
     int npcid = getNpcTemplate().get_npcId();
     if (npcid == 45061 || npcid == 45161 || npcid == 45181 || npcid == 45455 || npcid == 400000 || npcid == 400001) {
       int rnd = _random.nextInt(3);
       if (1 > rnd) {
         setHiddenStatus(1);
         setStatus(13);
       }
     } else if (npcid == 45045 || npcid == 45126 || npcid == 45134 || npcid == 45281 || npcid == 75003) {
       int rnd = _random.nextInt(3);
       if (1 > rnd) {
         setHiddenStatus(1);
         setStatus(4);
       }
     } else if (npcid == 217) {
       setHiddenStatus(1);
       setStatus(6);
     } else if (npcid == 45067 || npcid == 45264 || npcid == 45452 || npcid == 45090 || npcid == 45321 || npcid == 45445 || npcid == 75000) {
       setHiddenStatus(2);
       setStatus(4);
     } else if (npcid == 45681) {
       setHiddenStatus(2);
       setStatus(11);
     }
   }

   public void initHideForMinion(L1NpcInstance leader) {
     int npcid = getNpcTemplate().get_npcId();
     if (leader.getHiddenStatus() == 1) {
       if (npcid == 45061 || npcid == 45161 || npcid == 45181 || npcid == 45455 || npcid == 400000 || npcid == 400001) {
         setHiddenStatus(1);
         setStatus(13);
       } else if (npcid == 45045 || npcid == 45126 || npcid == 45134 || npcid == 45281 || npcid == 75003) {
         setHiddenStatus(1);
         setStatus(4);
       }
     } else if (leader.getHiddenStatus() == 2) {
       if (npcid == 45067 || npcid == 45264 || npcid == 45452 || npcid == 45090 || npcid == 45321 || npcid == 45445 || npcid == 75000) {
         setHiddenStatus(2);
         setStatus(4);
       } else if (npcid == 45681) {
         setHiddenStatus(2);
         setStatus(11);
       }
     }
   }

   protected void transform(int transformId, L1Character cha) {
     L1PcInstance pc = (L1PcInstance)cha;
     if (this._transformHandler != null) {
       this._transformHandler.onTransFormNotify(this);
     }
     if (getNpcTemplate().isTransformdrop()) {
       distributeDrop(pc);
     }

     transform(transformId);
     getInventory().clearItems();
   }

   private void calcCombo(L1Character lastAttacker) {
     if (lastAttacker instanceof L1PcInstance) {
       L1PcInstance pc = (L1PcInstance)lastAttacker;
       if (pc.getAccount() == null || pc.noPlayerCK) {
         return;
       }

       if (!pc.hasSkillEffect(85010)) {
         if (pc.getAccount().getBlessOfAin() / 10000 > 100 && CommonUtil.random(1000000) <= Config.ServerEnchant.ComboChance) {
           pc.setComboCount(0);
           pc.setSkillEffect(85010, 50000L);
           pc.sendPackets((ServerBasePacket)new S_PacketBox(204, pc.getComboCount()));
         }
       } else if (pc.getComboCount() <= 10) {
         pc.setComboCount(pc.getComboCount() + 1);
         pc.sendPackets((ServerBasePacket)new S_PacketBox(204, pc.getComboCount()));
       } else {
         pc.sendPackets((ServerBasePacket)new S_PacketBox(204, 11));
       }
     }
   }

   public void re() {
     setDeathProcessing(true);
     setCurrentHp(0);
     setDead(true);
     getMap().setPassable((Point)getLocation(), true);
     setDeathProcessing(false);
     set_exp(0L);
     setKarma(0);
     setLawful(0);
     allTargetClear();
     deleteRe();
   }




   public void setCurseMimic(boolean curseMimic) {
     this._isCurseMimic = curseMimic;
   }

   public boolean isCurseMimic() {
     return this._isCurseMimic;
   }

   private static int _instanceType = -1;


   public int getL1Type() {
     return (_instanceType == -1) ? (_instanceType = super.getL1Type() | 0x400) : _instanceType;
   }

   class TargetSorter
     implements Comparator<L1PcInstance> {
     public int compare(L1PcInstance o1, L1PcInstance o2) {
       int law1 = o1.getLawful();
       int law2 = o2.getLawful();
       if ((law1 < 0 || law2 < 0) &&
         law1 != law2) {
         return (law1 > law2) ? 1 : -1;
       }

       int dir1 = L1MonsterInstance.this.getLocation().getTileLineDistance((Point)o1.getLocation());
       int dir2 = L1MonsterInstance.this.getLocation().getTileLineDistance((Point)o2.getLocation());
       if (dir1 == dir2) {
         return MJRnd.isBoolean() ? 1 : -1;
       }
       return (dir1 > dir2) ? 1 : -1;
     }
   }

   private boolean HunterEventDoll(L1PcInstance pc) {
     DollBonusEventInfo bInfo = DollBonusEventLoader.getInstance().getBonusItemInfo(pc.getMapId());
     L1ItemInstance doll_item = null;

     if (bInfo != null &&
       bInfo.isMap(pc)) {
       if (bInfo.get_non_trans_id() != null) {
         String[] non_trans_ids = (String[])MJArrangeParser.parsing(bInfo.get_non_trans_id(), ",", MJArrangeParseeFactory.createStringArrange()).result();
         for (int i = 0; i < non_trans_ids.length; i++) {
           if (getNpcId() == Integer.parseInt(non_trans_ids[i])) {
             return false;
           }
         }
       }

       L1DollInstance doll = pc.getMagicDoll();
       if (doll == null) {
         return false;
       }
       doll_item = pc.getInventory().getItem(doll.getItemObjId());

       if (doll_item != null) {
         String[] Event_dolls = (String[])MJArrangeParser.parsing(bInfo.get_dollids(), ",", MJArrangeParseeFactory.createStringArrange()).result();
         for (int d = 0; d < Event_dolls.length; d++) {
           if (getNpcId() != bInfo.trans_npc(d))
           {

             if (doll_item.getItemId() == Integer.parseInt(Event_dolls[d])) {
               int rnd = _random.nextInt(999999) + 1;
               if (rnd <= bInfo.calc_probability(pc, d)) {
                 Broadcaster.broadcastPacket(this, (ServerBasePacket)new S_SkillSound(getId(), 230));
                 transform(bInfo.trans_npc(d));
                 return true;
               }
             }
           }
         }
       }
     }

     return false;
   }
 }


