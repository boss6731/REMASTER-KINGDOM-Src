 package l1j.server.server.model.Instance;

 import l1j.server.Config;
 import l1j.server.MJCompanion.Instance.MJCompanionInstance;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
 import l1j.server.MJWarSystem.MJCastleWar;
 import l1j.server.MJWarSystem.MJCastleWarBusiness;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1CastleLocation;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1WarSpawn;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_RemoveObject;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.types.Point;

 public class L1TowerInstance extends L1NpcInstance {
   private static final long serialVersionUID = 1L;
   protected L1Character _lastattacker;
   private int _castle_id;
   protected int _crackStatus;

   public L1TowerInstance(L1Npc template) {
     super(template);
   }






   public void onPerceive(L1PcInstance perceivedFrom) {
     if (perceivedFrom == null)
       return;
     perceivedFrom.addKnownObject((L1Object)this);
     if (perceivedFrom.getAI() == null) {
       perceivedFrom.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
     }
   }


   public void onAction(L1PcInstance player) {
     if (getCurrentHp() > 0 && !isDead()) {
       L1Attack attack = new L1Attack(player, this);
       if (attack.calcHit()) {
         attack.calcDamage();
         attack.addPcPoisonAttack(player, this);
       }
       attack.action();
       attack.commit();
     }
   }


   public void receiveDamage(L1Character attacker, int damage) {
     if (this._castle_id == 0) {
       if (isSubTower()) {
         this._castle_id = 7;
       } else {
         this._castle_id = L1CastleLocation.getCastleId(getX(), getY(), getMapId());
       }
     }
     if (this._castle_id > 0 && MJCastleWarBusiness.getInstance().isNowWar(this._castle_id)) {

       if (this._castle_id == 7 && !isSubTower()) {
         int subTowerDeadCount = 0;
         L1TowerInstance tower = null;
         for (L1Object l1object : L1World.getInstance().getObject()) {
           if (l1object instanceof L1TowerInstance) {
             tower = (L1TowerInstance)l1object;

             subTowerDeadCount++;
             if (tower.isSubTower() && tower.isDead() && subTowerDeadCount == 4) {
               break;
             }
           }
         }

         if (subTowerDeadCount < 3) {
           return;
         }
       }

       L1PcInstance pc = null;
       if (attacker instanceof L1PcInstance) {
         pc = (L1PcInstance)attacker;
       } else if (attacker instanceof MJCompanionInstance) {
         pc = ((MJCompanionInstance)attacker).get_master();
       } else if (attacker instanceof L1PetInstance) {
         pc = ((L1PetInstance)attacker).getMaster();
       } else if (attacker instanceof L1SummonInstance) {
         pc = (L1PcInstance)((L1SummonInstance)attacker).getMaster();
       }
       if (pc == null) {
         return;
       }

       MJCastleWar war = MJCastleWarBusiness.getInstance().get(this._castle_id);
       L1Clan defense = war.getDefenseClan();
       L1Clan offense = pc.getClan();

       if (defense == null) {
         System.out.println(String.format("[攻城塔]防守中的血盟不存在。 城堡 : %d", new Object[] { Integer.valueOf(this._castle_id) }));

         return;
       }
       if (defense.getClanName().equalsIgnoreCase("紅色騎士團") &&
               war._boss != null &&
               !war._boss.isDead()) {
         pc.sendPackets("\f3需要先擊敗守護塔周圍的 Boss 才能攻擊守護塔。");

         return;
       }
       if (!pc.isGm() && (
         offense == null || war.getOffenseClan(offense.getClanId()) == null)) {
         return;
       }

       if (getCurrentHp() > 0 && !isDead()) {
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
           setStatus(35);
           setStatus(35);
           this._lastattacker = attacker;
           this._crackStatus = 0;
           Death death = new Death();
           GeneralThreadPool.getInstance().execute(death);
         }
         if (newHp > 0) {
           setCurrentHp(newHp);
           if (getMaxHp() * 1 / 4 > getCurrentHp()) {
             if (this._crackStatus != 3) {
               broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 34));
               setStatus(34);
               setStatus(34);
               this._crackStatus = 3;
             }
           } else if (getMaxHp() * 2 / 4 > getCurrentHp()) {
             if (this._crackStatus != 2) {
               broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 33));
               setStatus(33);
               setStatus(33);
               this._crackStatus = 2;
             }
           } else if (getMaxHp() * 3 / 4 > getCurrentHp() &&
             this._crackStatus != 1) {
             broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 32));
             setStatus(32);
             setStatus(32);
             this._crackStatus = 1;
           }

         }
       } else if (!isDead()) {
         setDead(true);
         setStatus(35);
         setStatus(35);
         this._lastattacker = attacker;
         Death death = new Death();
         GeneralThreadPool.getInstance().execute(death);
       }
     }
   }



   public void setCurrentHp(int i) {
     super.setCurrentHp(i);
   }

   class Death implements Runnable {
     L1Character lastAttacker = L1TowerInstance.this._lastattacker;
     L1Object object = L1World.getInstance().findObject(L1TowerInstance.this.getId());
     L1TowerInstance npc = (L1TowerInstance)this.object;


     public void run() {
       if (L1TowerInstance.this.hasSkillEffect(5055)) {
         L1TowerInstance.this.setPresherPc(null);
         L1TowerInstance.this.setPresherDamage(0);

         if (L1TowerInstance.this.getPresherDeathRecall()) {
           L1TowerInstance.this.setPresherDeathRecall(false);
         }
         L1TowerInstance.this.removeSkillEffect(5055);
       }

       L1TowerInstance.this.setCurrentHp(0);
       L1TowerInstance.this.setDead(true);
       L1TowerInstance.this.setStatus(35);
       L1TowerInstance.this.setStatus(35);
       int targetobjid = this.npc.getId();

       this.npc.getMap().setPassable((Point)this.npc.getLocation(), true);

       this.npc.broadcastPacket((ServerBasePacket)new S_DoActionGFX(targetobjid, 35));

       if (!L1TowerInstance.this.isSubTower()) {
         L1WarSpawn warspawn = new L1WarSpawn();
         warspawn.SpawnCrown(L1TowerInstance.this._castle_id);
       }
     }
   }


   public void deleteMe() {
     this._destroyed = true;
     if (getInventory() != null) {
       getInventory().clearItems();
     }
     allTargetClear();
     this._master = null;
     L1World.getInstance().removeVisibleObject((L1Object)this);
     L1World.getInstance().removeObject((L1Object)this);
     for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)this)) {
       pc.removeKnownObject((L1Object)this);
       pc.sendPackets((ServerBasePacket)new S_RemoveObject((L1Object)this));
     }
     removeAllKnownObjects();
   }

   public boolean isSubTower() {
     return (getNpcTemplate().get_npcId() == 81190 ||
       getNpcTemplate().get_npcId() == 81191 ||
       getNpcTemplate().get_npcId() == 81192 ||
       getNpcTemplate().get_npcId() == 81193);
   }
 }


