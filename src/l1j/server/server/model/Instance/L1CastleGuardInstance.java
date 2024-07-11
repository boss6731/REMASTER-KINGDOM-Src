 package l1j.server.server.model.Instance;

 import l1j.server.Config;
 import l1j.server.MJCompanion.Instance.MJCompanionInstance;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.MJWarSystem.MJWar;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.NPCTalkDataTable;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1CastleLocation;
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




 public class L1CastleGuardInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;
   private boolean isAttackClan = false;
   private int _check_count = 0;


   public void searchTarget() {
     L1PcInstance targetPlayer = null;

     for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer((L1Object)this)) {

       if (pc.getCurrentHp() <= 0 || pc.isDead() || pc.isGm() || pc.isGhost()) {
         continue;
       }
       if (!pc.isInvisble() || getNpcTemplate().is_agrocoi()) {
         if (pc.isWanted()) {
           targetPlayer = pc;

           break;
         }
         if (MJWar.isOffenseClan(pc.getClan())) {
           targetPlayer = pc;
           this.isAttackClan = true;

           break;
         }
       }
     }
     if (targetPlayer == null) {
       this._check_count++;
       if (this._check_count == 30) {
         setHeading(MJRnd.next(7));
         S_ChangeHeading heading = new S_ChangeHeading(this);
         Broadcaster.broadcastPacket(this, (ServerBasePacket)heading, false);
         heading.clear();
         this._check_count = 0;
       }
     }

     if (targetPlayer != null) {
       int castleId = 0;
       castleId = L1CastleLocation.getCastleIdByArea(targetPlayer);
       if (castleId != 0) {
         boolean isNowWar = MJCastleWarBusiness.getInstance().isNowWar(castleId);
         if (isNowWar && !this.isAttackClan) {
           targetPlayer = null;
         }
       }
     }

     if (targetPlayer != null) {
       setTarget(targetPlayer);
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

   public L1CastleGuardInstance(L1Npc template) {
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
       if (npcid == 70549 || npcid == 70985 || npcid == 70656) {
         hascastle = checkHasCastle(player, 1);
         if (hascastle) {
           htmlid = "gateokeeper";
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "gatekeeperop";
         }
       } else if (npcid == 70600 || npcid == 70986) {
         hascastle = checkHasCastle(player, 2);
         if (hascastle) {
           htmlid = "orckeeper";
         } else {
           htmlid = "orckeeperop";
         }
       } else if (npcid == 70687 || npcid == 70987 || npcid == 70778) {
         hascastle = checkHasCastle(player, 3);
         if (hascastle) {
           htmlid = "gateokeeper";
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "gatekeeperop";
         }
       } else if (npcid == 70800 || npcid == 70988 || npcid == 70989 || npcid == 70990 || npcid == 70991 || npcid == 70817) {
         hascastle = checkHasCastle(player, 4);
         if (hascastle) {
           htmlid = "gateokeeper";
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "gatekeeperop";
         }
       } else if (npcid == 70862 || npcid == 70992 || npcid == 70863) {
         hascastle = checkHasCastle(player, 5);
         if (hascastle) {
           htmlid = "gateokeeper";
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "gatekeeperop";
         }
       } else if (npcid == 70993 || npcid == 70994 || npcid == 70995) {
         hascastle = checkHasCastle(player, 6);
         if (hascastle) {
           htmlid = "gateokeeper";
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "gatekeeperop";
         }
       } else if (npcid == 70996) {
         hascastle = checkHasCastle(player, 7);
         if (hascastle) {
           htmlid = "gatekeeper";
           htmldata = new String[] { player.getName() };
         } else {
           htmlid = "gatekeeperop";
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
         // 檢查當前目標是否為空，或者目標是否距離家超過20單位，或者目標是否在不同的地圖，或者目標的HP是否小於等於0，或者目標是否已死亡
         if (this._target == null ||
                 Math.abs(getX() - getHomeX()) > 20 || // 檢查目標距離是否超過20單位 (X軸)
                 Math.abs(getY() - getHomeY()) > 20 || // 檢查目標距離是否超過20單位 (Y軸)
                 this._target.getMapId() != getMapId() || // 檢查目標是否在不同的地圖上
                 this._target.getCurrentHp() <= 0 || // 檢查目標的HP是否小於等於0
                 this._target.isDead() || // 檢查目標是否已死亡
                 (this._target.isInvisble() && !getNpcTemplate().is_agrocoi() && !this._hateList.containsKey(this._target)) || // 檢查目標是否隱身且NPC不攻擊隱身目標且仇恨列表中不包含目標
                 (this._target instanceof L1SummonInstance && ((L1SummonInstance)this._target).isDestroyed()) || // 檢查目標是否為召喚物且已摧毀
                 (this._target instanceof L1PetInstance && ((L1PetInstance)this._target).isDestroyed()) || // 檢查目標是否為寵物且已摧毀
                 (this._target instanceof MJCompanionInstance && ((MJCompanionInstance)this._target).isDestroyed())) { // 檢查目標是否為夥伴且已摧毀

             if (this._target != null) { // 如果當前有目標
                 tagertClear(); // 清除目標
                 if (getSpawn() != null) { // 如果有重生點
                     teleport(getHomeX(), getHomeY(), getSpawn().getHeading()); // 傳送到重生點
                 } else {
                     System.out.println(String.format("找不到守衛重生資訊。%d", new Object[] { Integer.valueOf(getNpcId()) })); // 輸出錯誤訊息
                     teleport(getHomeX(), getHomeY(), 0); // 傳送到家
                 }
             }

             if (!this._hateList.isEmpty()) { // 如果仇恨列表不為空
                 this._target = this._hateList.getMaxHateCharacter(); // 選擇仇恨最大值目標
                 checkTarget(); // 遞迴檢查新目標
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
       if (L1CastleGuardInstance.this.hasSkillEffect(5055)) {
         L1CastleGuardInstance.this.setPresherPc(null);
         L1CastleGuardInstance.this.setPresherDamage(0);

         if (L1CastleGuardInstance.this.getPresherDeathRecall()) {
           L1CastleGuardInstance.this.setPresherDeathRecall(false);
         }
         L1CastleGuardInstance.this.removeSkillEffect(5055);
       }

       L1CastleGuardInstance.this.setDeathProcessing(true);
       L1CastleGuardInstance.this.setCurrentHp(0);
       L1CastleGuardInstance.this.setDead(true);
       L1CastleGuardInstance.this.setStatus(8);

       L1CastleGuardInstance.this.getMap().setPassable((Point)L1CastleGuardInstance.this.getLocation(), true);

       L1CastleGuardInstance.this.broadcastPacket((ServerBasePacket)new S_DoActionGFX(L1CastleGuardInstance.this.getId(), 8));

       L1CastleGuardInstance.this.startChat(1);

       L1CastleGuardInstance.this.setDeathProcessing(false);

       L1CastleGuardInstance.this.allTargetClear();

       L1CastleGuardInstance.this.startDeleteTimer();
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


