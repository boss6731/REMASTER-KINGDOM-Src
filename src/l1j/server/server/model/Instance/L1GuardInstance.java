 package l1j.server.server.model.Instance;

 import l1j.server.Config;
 import l1j.server.MJCompanion.Instance.MJCompanionInstance;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.NPCTalkDataTable;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1NpcTalkData;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_ChangeHeading;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_NPCTalkReturn;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.types.Point;





 public class L1GuardInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;
   private int _check_count = 0;


   public void searchTarget() {
     L1Character targetPlayer = null;

     for (L1Object obj : L1World.getInstance().getVisibleObjects((L1Object)this)) {












       if (obj instanceof L1MonsterInstance) {
         L1MonsterInstance mon = (L1MonsterInstance)obj;

         if (getMapId() == 1708 && (
           mon.getCurrentHp() > 0 || !mon.isDead())) {
           targetPlayer = mon;

           break;
         }
       }

       if (obj instanceof L1PcInstance) {
         L1PcInstance pc = (L1PcInstance)obj;
         if (pc.isWanted()) {
           targetPlayer = pc;

           break;
         }
       }
       if (targetPlayer == null) {
         this._check_count++;
         if (this._check_count == 300) {
           setHeading(MJRnd.next(7));
           S_ChangeHeading heading = new S_ChangeHeading(this);
           Broadcaster.broadcastPacket(this, (ServerBasePacket)heading, false);
           heading.clear();
           this._check_count = 0;
         }
       }
     }










     if (targetPlayer != null) {
       this._hateList.add(targetPlayer, 0);
       this._target = targetPlayer;
     }
   }

   public void setTarget(L1PcInstance targetPlayer) {
     if (targetPlayer != null) {
       this._hateList.add(targetPlayer, 0);
       this._target = targetPlayer;
     }
   }


   public boolean noTarget() {
     if (getLocation().getTileLineDistance(new Point(getHomeX(), getHomeY())) > 0) {
       int dir = moveDirection(getMapId(), getHomeX(), getHomeY());
       if (dir != -1) {
         setSleepTime(setDirectionMoveSpeed(dir));
       } else {
         teleport(getHomeX(), getHomeY(), 1);
       }

     } else if (L1World.getInstance().getRecognizePlayer((L1Object)this).size() == 0) {
       return true;
     }

     return false;
   }

   public L1GuardInstance(L1Npc template) {
     super(template);
   }


   public void onNpcAI() {
     if (isAiRunning()) {
       return;
     }
     setActived(false);
     startAI();
   }


   public void onAction(L1PcInstance pc) {
     if (pc == null)
       return;
     if (!isDead()) {
       if (getCurrentHp() > 0) {
         L1Attack attack = new L1Attack(pc, this);
         if (attack.calcHit()) {
           attack.calcDamage();
           attack.addPcPoisonAttack(pc, this);
         }
         attack.action();
         attack.commit();
       } else {
         L1Attack attack = new L1Attack(pc, this);
         attack.calcHit();
         attack.action();
       }
     }
   }


   public void onTalkAction(L1PcInstance player) {
     int objid = getId();
     L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
     int npcid = getNpcTemplate().get_npcId();
     String htmlid = null;
     String[] htmldata = null;
     boolean hascastle = false;
     String clan_name = "";
     String pri_name = "";

     if (talking != null) {
       if (npcid == 70549 || npcid == 70985) {
         hascastle = checkHasCastle(player, 1);
         if (hascastle) {
           htmlid = "城門守衛"; // gateokeeper
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "守衛op"; // gatekeeperop
         }
       } else if (npcid == 70656) {
         hascastle = checkHasCastle(player, 1);
         if (hascastle) {
           htmlid = "守衛"; // gatekeeper
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "守衛op"; // gatekeeperop
         }
       } else if (npcid == 70600 || npcid == 70986) {
         hascastle = checkHasCastle(player, 2);
         if (hascastle) {
           htmlid = "半獸人守衛"; // orckeeper
         } else {
           htmlid = "半獸人守衛操作"; // orckeeperop
         }
       }
     }
   }
   public void onTalkAction(L1PcInstance player) {
     int objid = getId();
     L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
     int npcid = getNpcTemplate().get_npcId();
     String htmlid = null;
     String[] htmldata = null;
     boolean hascastle = false;
     String clan_name = "";
     String pri_name = "";

     if (talking != null) {
       if (npcid == 70549 || npcid == 70985) {
         hascastle = checkHasCastle(player, 1);
         if (hascastle) {
           htmlid = "城門守衛"; // gateokeeper
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "城門守衛操作"; // gatekeeperop
         }
       } else if (npcid == 70656) {
         hascastle = checkHasCastle(player, 1);
         if (hascastle) {
           htmlid = "守衛"; // gatekeeper
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "守衛操作"; // gatekeeperop
         }
       } else if (npcid == 70600 || npcid == 70986) {
         hascastle = checkHasCastle(player, 2);
         if (hascastle) {
           htmlid = "半獸人守衛"; // orckeeper
         } else {
           htmlid = "半獸人守衛操作"; // orckeeperop
         }
       } else if (npcid == 70687 || npcid == 70987) {
         hascastle = checkHasCastle(player, 3);
         if (hascastle) {
           htmlid = "城門守衛"; // gateokeeper
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "城門守衛操作"; // gatekeeperop
         }
       } else if (npcid == 70778) {
         hascastle = checkHasCastle(player, 3);
         if (hascastle) {
           htmlid = "守衛"; // gatekeeper
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "守衛操作"; // gatekeeperop
         }
       } else if (npcid == 70800  npcid == 70988  npcid == 70989  npcid == 70990  npcid == 70991) {
         hascastle = checkHasCastle(player, 4);
         if (hascastle) {
           htmlid = "城門守衛"; // gateokeeper
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "城門守衛操作"; // gatekeeperop
         }
       } else if (npcid == 70817) {
         hascastle = checkHasCastle(player, 4);
         if (hascastle) {
           htmlid = "守衛"; // gatekeeper
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "守衛操作"; // gatekeeperop
         }
       } else if (npcid == 70862  npcid == 70992) {
         hascastle = checkHasCastle(player, 5);
         if (hascastle) {
           htmlid = "城門守衛"; // gateokeeper
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "城門守衛操作"; // gatekeeperop
         }
       } else if (npcid == 70863) {
         hascastle = checkHasCastle(player, 5);
         if (hascastle) {
           htmlid = "守衛"; // gatekeeper
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "守衛操作"; // gatekeeperop
         }
       } else if (npcid == 70993  npcid == 70994) {
         hascastle = checkHasCastle(player, 6);
         if (hascastle) {
           htmlid = "城門守衛"; // gateokeeper
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "城門守衛操作"; // gatekeeperop
         }
       } else if (npcid == 70995) {
         hascastle = checkHasCastle(player, 6);
         if (hascastle) {
           htmlid = "守衛"; // gatekeeper
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "守衛操作"; // gatekeeperop
         }
       } else if (npcid == 70996) {
         hascastle = checkHasCastle(player, 7);
         if (hascastle) {
           htmlid = "守衛"; // gatekeeper
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "守衛操作"; // gatekeeperop
         }
       }
     }
   }
       else if (npcid == 60514) {
         for (L1Clan clan : L1World.getInstance().getAllClans()) {
           if (clan.getCastleId() == 1) {
             clan_name = clan.getClanName();
             pri_name = clan.getLeaderName();
             break;
           }
         }
         htmlid = "ktguard6";
         htmldata = new String[] { getName(), clan_name, pri_name };
       } else if (npcid == 60560) {
         for (L1Clan clan : L1World.getInstance().getAllClans()) {
           if (clan.getCastleId() == 2) {
             clan_name = clan.getClanName();
             pri_name = clan.getLeaderName();
             break;
           }
         }
         htmlid = "orcguard6";
         htmldata = new String[] { getName(), clan_name, pri_name };
       } else if (npcid == 60552 || npcid == 5155) {
         for (L1Clan clan : L1World.getInstance().getAllClans()) {
           if (clan.getCastleId() == 3) {
             clan_name = clan.getClanName();
             pri_name = clan.getLeaderName();
             break;
           }
         }
         htmlid = "wdguard6";
         htmldata = new String[] { getName(), clan_name, pri_name };
       } else if (npcid == 60524 || npcid == 60525 || npcid == 60529 || npcid == 7320232) {
         for (L1Clan clan : L1World.getInstance().getAllClans()) {
           if (clan.getCastleId() == 4) {
             clan_name = clan.getClanName();
             pri_name = clan.getLeaderName();
             break;
           }
         }
         htmlid = "grguard6";
         htmldata = new String[] { getName(), clan_name, pri_name };
       } else if (npcid == 70857) {
         for (L1Clan clan : L1World.getInstance().getAllClans()) {
           if (clan.getCastleId() == 5) {
             clan_name = clan.getClanName();
             pri_name = clan.getLeaderName();
             break;
           }
         }
         htmlid = "heguard6";
         htmldata = new String[] { getName(), clan_name, pri_name };
       } else if (npcid == 60530 || npcid == 60531) {
         for (L1Clan clan : L1World.getInstance().getAllClans()) {
           if (clan.getCastleId() == 6) {
             clan_name = clan.getClanName();
             pri_name = clan.getLeaderName();
             break;
           }
         }
         htmlid = "dcguard6";
         htmldata = new String[] { getName(), clan_name, pri_name };
       } else if (npcid == 60533 || npcid == 60534) {
         for (L1Clan clan : L1World.getInstance().getAllClans()) {
           if (clan.getCastleId() == 7) {
             clan_name = clan.getClanName();
             pri_name = clan.getLeaderName();
             break;
           }
         }
         htmlid = "adguard6";
         htmldata = new String[] { getName(), clan_name, pri_name };
       } else if (npcid == 81156) {
         for (L1Clan clan : L1World.getInstance().getAllClans()) {
           if (clan.getCastleId() == 8) {
             clan_name = clan.getClanName();
             pri_name = clan.getLeaderName();
             break;
           }
         }
         htmlid = "ktguard6";
         htmldata = new String[] { getName(), clan_name, pri_name };
       }

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



   public void onFinalAction() {}



   public void doFinalAction() {}



   public void receiveDamage(L1Character attacker, int damage) {
     if (attacker == null)
       return;
     if (getCurrentHp() > 0 && !isDead()) {
       if (damage >= 0 &&
         !(attacker instanceof L1EffectInstance)) {
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
     } else if (!isDead()) {
       setDead(true);
       setStatus(8);
       Death death = new Death(attacker);
       GeneralThreadPool.getInstance().execute(death);
     }
   }



 public void checkTarget() {
         if (this._target == null || Math.abs(getX() - getHomeX()) > 20 || Math.abs(getY() - getHomeY()) > 20 || this._target.getMapId() != getMapId() || this._target.getCurrentHp() <= 0 || this._target.isDead() ||
         (this._target.isInvisble() && !getNpcTemplate().is_agrocoi() && !this._hateList.containsKey(this._target)) ||
         (this._target instanceof L1SummonInstance && ((L1SummonInstance)this._target).isDestroyed()) ||
         (this._target instanceof L1PetInstance && ((L1PetInstance)this._target).isDestroyed()) ||
         (this._target instanceof MJCompanionInstance && ((MJCompanionInstance)this._target).isDestroyed())) {

// 如果當前目標為空或遠離家位置超過20單位距離，或目標在不同地圖上，或目標生命值為0，或目標已死亡，或以下條件之一成立時：
         if (this._target != null) {
         tagertClear(); // 清除目標
         }

// 如果仇恨列表不為空，則設置仇恨列表中最高仇恨的角色為目標，並重新檢查目標。
         if (!this._hateList.isEmpty()) {
         this._target = this._hateList.getMaxHateCharacter();
         checkTarget();
         }
         }
         }



   public void setCurrentHp(int i) {
     super.setCurrentHp(i);

     if (getMaxHp() > getCurrentHp())
       startHpRegeneration();
   }

   class Death
     implements Runnable
   {
     L1Character _lastAttacker;

     public Death(L1Character lastAttacker) {
       this._lastAttacker = lastAttacker;
     }


     public void run() {
       if (L1GuardInstance.this.hasSkillEffect(5055)) {
         L1GuardInstance.this.setPresherPc(null);
         L1GuardInstance.this.setPresherDamage(0);

         if (L1GuardInstance.this.getPresherDeathRecall()) {
           L1GuardInstance.this.setPresherDeathRecall(false);
         }
         L1GuardInstance.this.removeSkillEffect(5055);
       }

       L1GuardInstance.this.setDeathProcessing(true);
       L1GuardInstance.this.setCurrentHp(0);
       L1GuardInstance.this.setDead(true);
       L1GuardInstance.this.setStatus(8);

       L1GuardInstance.this.getMap().setPassable((Point)L1GuardInstance.this.getLocation(), true);

       L1GuardInstance.this.broadcastPacket((ServerBasePacket)new S_DoActionGFX(L1GuardInstance.this.getId(), 8));

       L1GuardInstance.this.startChat(1);

       L1GuardInstance.this.setDeathProcessing(false);

       L1GuardInstance.this.allTargetClear();

       L1GuardInstance.this.startDeleteTimer();
     }
   }

   private boolean checkHasCastle(L1PcInstance pc, int castleId) {
     boolean isExistDefenseClan = false;
     for (L1Clan clan : L1World.getInstance().getAllClans()) {
       if (castleId == clan.getCastleId()) {
         isExistDefenseClan = true;
         break;
       }
     }
     if (!isExistDefenseClan) {
       return true;
     }

     if (pc.getClanid() != 0) {
       L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
       if (clan != null &&
         clan.getCastleId() == castleId) {
         return true;
       }
     }

     return false;
   }
 }


