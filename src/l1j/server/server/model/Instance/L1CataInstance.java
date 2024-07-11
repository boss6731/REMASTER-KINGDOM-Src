 package l1j.server.server.model.Instance;

 import l1j.server.Config;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.server.datatables.NPCTalkDataTable;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1NpcTalkData;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;



 public class L1CataInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;

   public L1CataInstance(L1Npc template) {
     super(template);
   }





   public void onPerceive(L1PcInstance perceivedFrom) {
     perceivedFrom.addKnownObject((L1Object)this);
     if (0 < getCurrentHp()) {
       onNpcAI();
     }

     if (perceivedFrom.getAI() == null) {
       perceivedFrom.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this), true);
     }
   }



   public void onTalkAction(L1PcInstance pc) {
     if (pc == null)
       return;
     int objid = getId();
     L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
     String htmlid = null;
     String[] htmldata = null;

     if (talking != null &&
       !pc.isCrown()) {
       pc.sendPackets((ServerBasePacket)new S_ServerMessage(2498));

       return;
     }

     if (htmlid != null) {
       if (htmldata != null) {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, htmlid, htmldata));
       } else {
         pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(objid, htmlid));
       }

     } else if (pc.getLawful() < -1000) {
       pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 2));
     } else {
       pc.sendPackets((ServerBasePacket)new S_NPCTalkReturn(talking, objid, 1));
     }
   }



   public void onAction(L1PcInstance pc) {
     if (pc == null)
       return;
     if (getCurrentHp() > 0 && !isDead()) {
       L1Attack attack = new L1Attack(pc, this);
       if (attack.calcHit()) {
         attack.calcDamage();
         attack.addPcPoisonAttack(pc, this);
       }
       attack.action();
       attack.commit();
     }
   }


   public void ReceiveManaDamage(L1Character attacker, int mpDamage) {
     if (attacker == null)
       return;  if (mpDamage > 0 && !isDead()) {
       onNpcAI();
       int newMp = getCurrentMp() - mpDamage;
       if (newMp < 0) {
         newMp = 0;
       }
       setCurrentMp(newMp);
     }
   }


   public void receiveDamage(L1Character attacker, int damage) {
     if (attacker == null) {
       return;
     }
     int castleid = 0;
     if (getNpcId() == 7000084 || getNpcId() == 7000085) {
       castleid = 1;
     } else if (getNpcId() == 7000086 || getNpcId() == 7000087) {
       castleid = 2;
     } else if (getNpcId() == 7000082 || getNpcId() == 7000083) {
       castleid = 4;
     }

     boolean isNowWar = false;
     isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleid);
     if (!isNowWar) {
       return;
     }

     if (getCurrentHp() > 0 && !isDead()) {
       if (damage > 0) {
         if (hasSkillEffect(66)) {
           removeSkillEffect(66);
         } else if (hasSkillEffect(212)) {
           removeSkillEffect(212);
         }
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
         int transformId = getNpcTemplate().getTransformId();
         if (transformId == -1) {
           setCurrentHp(0);
           setDead(true);
           setStatus(8);
           die(this);
         }
       }
     } else if (!isDead()) {
       setDead(true);
       setStatus(8);
       die(this);
     }
   }


   public void setCurrentHp(int i) {
     super.setCurrentHp(i);
   }


   public void setCurrentMp(int i) {
     super.setCurrentMp(i);
   }

   public void die(L1Character lastAttacker) {
     try {
       if (hasSkillEffect(5055)) {
         setPresherPc(null);
         setPresherDamage(0);

         if (getPresherDeathRecall()) {
           setPresherDeathRecall(false);
         }
         removeSkillEffect(5055);
       }

       setDeathProcessing(true);
       setCurrentHp(0);
       setDead(true);
       setStatus(8);
       Broadcaster.broadcastPacket(this, (ServerBasePacket)new S_DoActionGFX(getId(), 8));
       setDeathProcessing(false);
     } catch (Exception exception) {}
   }
 }


