 package l1j.server.server.model.Instance;

 import java.util.Arrays;
 import java.util.List;
 import java.util.Random;
 import java.util.concurrent.ScheduledFuture;
 import l1j.server.Config;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.IdFactory;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1GroundInventory;
 import l1j.server.server.model.L1Inventory;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_HPMeter;
 import l1j.server.server.serverpackets.S_PetMenuPacket;
 import l1j.server.server.serverpackets.S_ReturnedStat;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.S_SummonPack;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.types.Point;

 public class L1SummonInstance
   extends L1NpcInstance {
   private static final long serialVersionUID = 1L;
   private ScheduledFuture<?> _summonFuture;
   private static final long SUMMON_TIME = 3600000L;
   private int _currentPetStatus;
   private boolean _tamed;
   private boolean _isReturnToNature = false;
   private static Random _random = new Random(System.nanoTime());


   public boolean noTarget() {
     switch (this._currentPetStatus) {
       case 3:
         return true;
       case 4:
         if (this._master != null && this._master.getMapId() == getMapId() &&
           getLocation().getTileLineDistance((Point)this._master.getLocation()) < 5) {
           int dir = targetReverseDirection(this._master.getX(), this._master.getY());
           dir = checkObject(getX(), getY(), getMapId(), dir);
           setSleepTime(setDirectionMoveSpeed(dir));
         } else {
           this._currentPetStatus = 3;
           return true;
         }
       case 5:
         if (Math.abs(getHomeX() - getX()) > 1 || Math.abs(getHomeY() - getY()) > 1) {
           int dir = moveDirection(getMapId(), getHomeX(), getHomeY());
           if (dir == -1) {
             setHomeX(getX());
             setHomeY(getY());
           } else {
             setSleepTime(setDirectionMoveSpeed(dir));
           }
         }
         return false;
     }
     if (this._master != null && this._master.getMapId() == getMapId()) {
       if (getLocation().getTileLineDistance((Point)this._master.getLocation()) > 2) {
         int dir = moveDirection(this._master.getMapId(), this._master.getX(), this._master.getY());
         if (dir == -1) {
           this._currentPetStatus = 3;
           return true;
         }
         setSleepTime(setDirectionMoveSpeed(dir));
       }
     } else {

       this._currentPetStatus = 3;
       return true;
     }
     return false;
   }

   class SummonTimer
     implements Runnable
   {
     public void run() {
       if (L1SummonInstance.this._destroyed) {
         return;
       }
       if (L1SummonInstance.this._tamed) {
         L1SummonInstance.this.liberate();
       } else {
         L1SummonInstance.this.Death((L1Character)null);
       }
     }
   }

   public L1SummonInstance(L1Npc template, L1Character master) {
     super(template);
     setId(IdFactory.getInstance().nextId());

     this._summonFuture = GeneralThreadPool.getInstance().schedule(new SummonTimer(), 3600000L);

     setMaster(master);
     setX(master.getX() + _random.nextInt(5) - 2);
     setY(master.getY() + _random.nextInt(5) - 2);
     setMap(master.getMapId());
     setHeading(5);
     setLightSize(template.getLightSize());

     this._currentPetStatus = 3;
     this._tamed = false;

     L1World.getInstance().storeObject((L1Object)this);
     L1World.getInstance().addVisibleObject((L1Object)this);
     for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)this)) {
       if (pc != null) {
         onPerceive(pc);
       }
     }
     master.addPet(this);
     Object[] aobj = master.getPetList().values().toArray();
     ((L1PcInstance)master).sendPackets((ServerBasePacket)new S_ReturnedStat(12, (aobj.length + 1) * 3, getId(), true));
   }

   public L1SummonInstance(L1NpcInstance target, L1Character master, boolean isCreateZombie) {
     super((L1Npc)null);
     setId(IdFactory.getInstance().nextId());

     if (isCreateZombie) {
       int npcId = 45065;
       L1PcInstance pc = (L1PcInstance)master;
       int level = pc.getLevel();
       if (pc.isWizard()) {
         if (level >= 24 && level <= 31) {
           npcId = 81183;
         } else if (level >= 32 && level <= 39) {
           npcId = 81184;
         } else if (level >= 40 && level <= 43) {
           npcId = 81185;
         } else if (level >= 44 && level <= 47) {
           npcId = 81186;
         } else if (level >= 48 && level <= 51) {
           npcId = 81187;
         } else if (level >= 52) {
           npcId = 81188;
         }
       } else if (pc.isElf() &&
         level >= 48) {
         npcId = 81183;
       }

       L1Npc template = NpcTable.getInstance().getTemplate(npcId).clone();
       setting_template(template);
     } else {

       setting_template(target.getNpcTemplate());
       setCurrentHp(target.getCurrentHp());
       setCurrentMp(target.getCurrentMp());
     }

     this._summonFuture = GeneralThreadPool.getInstance().schedule(new SummonTimer(), 3600000L);

     setMaster(master);
     setX(target.getX());
     setY(target.getY());
     setMap(target.getMapId());
     setHeading(target.getHeading());
     setLightSize(target.getLightSize());
     setPetcost(6);







     target.setInventory((L1Inventory)null);

     this._currentPetStatus = 3;
     this._tamed = true;

     for (L1NpcInstance each : master.getPetList().values()) {
       if (each != null) {
         each.targetRemove(target);
       }
     }
     target.deleteMe();
     L1World.getInstance().storeObject((L1Object)this);
     L1World.getInstance().addVisibleObject((L1Object)this);
     for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)this)) {
       if (pc != null)
         onPerceive(pc);
     }
     master.addPet(this);
     Object[] aobj = master.getPetList().values().toArray();
     ((L1PcInstance)master).sendPackets((ServerBasePacket)new S_ReturnedStat(12, (aobj.length + 1) * 3, getId(), true));
   }


   public void receiveDamage(L1Character attacker, int damage) {
     if (getCurrentHp() > 0) {
       if (damage > 0) {
         setHate(attacker, 0);
         if (hasSkillEffect(66)) {
           removeSkillEffect(66);
         } else if (hasSkillEffect(212)) {
           removeSkillEffect(212);
         }
         if (!isExsistMaster()) {
           this._currentPetStatus = 1;
           setTarget(attacker);
         }
       }

       if (attacker instanceof L1PcInstance && damage > 0) {
         L1PcInstance player = (L1PcInstance)attacker;
         player.set_pet_target(this);
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
       if (newHp <= 0) {
         Death(attacker);
       } else {
         setCurrentHp(newHp);
       }
     } else if (!isDead()) {

         System.out.println("警告：召喚物的HP減少處理存在錯誤。※或最初HP即為0");
         Death(attacker);
     }
   }
   public void onLeaveMaster() {
     if (!getMap().isSafetyZone((Point)getLocation())) {
       return;
     }
     getMap().setPassable((Point)getLocation(), true);
     for (L1ItemInstance item : this._inventory.getItems()) {
       L1GroundInventory l1GroundInventory = L1World.getInstance().getInventory(getX(), getY(), getMapId());
       this._inventory.tradeItem(item, item.getCount(), (L1Inventory)l1GroundInventory);
     }
     deleteMe();
   }

   public synchronized void Death(L1Character lastAttacker) {
     if (!isDead()) {
       if (hasSkillEffect(5055)) {
         setPresherPc(null);
         setPresherDamage(0);

         if (getPresherDeathRecall()) {
           setPresherDeathRecall(false);
         }
         removeSkillEffect(5055);
       }

       setDead(true);
       setCurrentHp(0);
       setStatus(8);

       getMap().setPassable((Point)getLocation(), true);

       L1Inventory targetInventory = this._master.getInventory();
       List<L1ItemInstance> items = this._inventory.getItems();
       for (L1ItemInstance item : items) {
         if (item == null)
           continue;  if (this._master.getInventory().checkAddItem(item, item.getCount()) == 0) {
           this._inventory.tradeItem(item, item.getCount(), targetInventory);
           ((L1PcInstance)this._master).sendPackets((ServerBasePacket)new S_ServerMessage(143, getName(), item.getLogName())); continue;
         }
         L1GroundInventory l1GroundInventory = L1World.getInstance().getInventory(getX(), getY(), getMapId());
         this._inventory.tradeItem(item, item.getCount(), (L1Inventory)l1GroundInventory);
       }

       if (this._tamed) {
         broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 8));
         startDeleteTimer();
       } else {
         deleteMe();
       }
     }
   }

   public synchronized void returnToNature() {
     this._isReturnToNature = true;
     if (!this._tamed) {
       getMap().setPassable((Point)getLocation(), true);
       L1Inventory targetInventory = this._master.getInventory();
       List<L1ItemInstance> items = this._inventory.getItems();
       for (L1ItemInstance item : items) {
         if (item == null)
           continue;  if (this._master.getInventory().checkAddItem(item, item.getCount()) == 0) {
           this._inventory.tradeItem(item, item.getCount(), targetInventory);
           ((L1PcInstance)this._master).sendPackets((ServerBasePacket)new S_ServerMessage(143, getName(), item.getLogName())); continue;
         }
         L1GroundInventory l1GroundInventory = L1World.getInstance().getInventory(getX(), getY(), getMapId());
         this._inventory.tradeItem(item, item.getCount(), (L1Inventory)l1GroundInventory);
       }

       deleteMe();
     } else {
       liberate();
     }
   }


   public synchronized void deleteMe() {
     if (this._destroyed) {
       return;
     }
     if (((L1PcInstance)this._master).isSM() && getNpcId() >= 120856 && getNpcId() <= 120864)
       ((L1PcInstance)this._master).setSM(false);
     if (!this._tamed && !this._isReturnToNature) {
       broadcastPacket((ServerBasePacket)new S_SkillSound(getId(), 169));
     }

     Object[] aobj = this._master.getPetList().values().toArray();
     if (aobj == null)
       return;  for (int i = 0; i < aobj.length; i++) {
       if (aobj[i] == this) {
         ((L1PcInstance)this._master).sendPackets((ServerBasePacket)new S_ReturnedStat(12, i * 3, getId(), false));
       }
     }


     this._master.getPetList().remove(Integer.valueOf(getId()));
     super.deleteMe();
     if (this._summonFuture != null) {
       this._summonFuture.cancel(false);
       this._summonFuture = null;
     }

     this._master = null;
   }


   public void liberate() {
     L1MonsterInstance monster = new L1MonsterInstance(getNpcTemplate());
     monster.setId(IdFactory.getInstance().nextId());

     monster.setX(getX());
     monster.setY(getY());
     monster.setMap(getMapId());
     monster.setHeading(getHeading());
     monster.setInventory(getInventory());
     setInventory((L1Inventory)null);
     monster.setCurrentHp(getCurrentHp());
     monster.setCurrentMp(getCurrentMp());
     monster.set_exp(0L);
     monster.setLawful(0);

     deleteMe();
     L1World.getInstance().storeObject((L1Object)monster);
     L1World.getInstance().addVisibleObject((L1Object)monster);
   }

   public void setTarget(L1Character target) {
     if (target != null && (this._currentPetStatus == 1 || this._currentPetStatus == 2 || this._currentPetStatus == 5)) {

       setHate(target, 0);
       if (!isAiRunning()) {
         startAI();
       }
     }
   }

   public void setMasterTarget(L1Character target) {
     if (target != null && (this._currentPetStatus == 1 || this._currentPetStatus == 5)) {

       setHate(target, 0);
       if (!isAiRunning()) {
         startAI();
       }
     }
   }


   public void onAction(L1PcInstance attacker) {
     if (attacker == null) {
       return;
     }
     L1Character cha = getMaster();
     if (cha == null) {
       return;
     }
     if (attacker == cha) {
       return;
     }
     L1PcInstance master = (L1PcInstance)cha;
     if (master.get_teleport()) {
       return;
     }
     if ((getZoneType() == 1 || attacker.getZoneType() == 1) &&
       isExsistMaster()) {
       L1Attack attack_mortion = new L1Attack(attacker, this);
       attack_mortion.action();

       return;
     }
     if (attacker.checkNonPvP(attacker, this)) {
       return;
     }

     L1Attack attack = new L1Attack(attacker, this);
     if (attack.calcHit()) {
       attack.calcDamage();
     }
     attack.action();
     attack.commit();
   }


   public void onTalkAction(L1PcInstance player) {
     if (isDead()) {
       return;
     }
     if (this._master.equals(player)) {
       S_PetMenuPacket pck = new S_PetMenuPacket(this, 0);
       player.sendPackets((ServerBasePacket)pck, false);
       player.sendPackets((ServerBasePacket)pck, true);
     }
   }


   public void onFinalAction(L1PcInstance player, String action) {
     int status = ActionType(action);
     if (status == 0) {
       return;
     }
     if (status == 6) {
       if (getMap().isSafetyZone((Point)getLocation())) {
         onLeaveMaster();
       }
       else if (this._tamed) {
         liberate();
       } else {
         Death((L1Character)null);
       }
     } else {

       Object[] petList = this._master.getPetList().values().toArray();
       L1SummonInstance summon = null;
       for (Object petObject : petList) {
         if (petObject != null &&
           petObject instanceof L1SummonInstance) {
           summon = (L1SummonInstance)petObject;
           summon.set_currentPetStatus(status);
         }
       }
     }
   }



   public void onPerceive(L1PcInstance perceivedFrom) {
     perceivedFrom.addKnownObject((L1Object)this);
     if (perceivedFrom.getAI() == null) {
       perceivedFrom.sendPackets((ServerBasePacket)new S_SummonPack(this, perceivedFrom));
     }
   }

   public void onItemUse() {
     if (!isActived()) {
       useItem(1, 100);
     }
     if (getCurrentHp() * 100 / getMaxHp() < 40) {
       useItem(0, 100);
     }
   }


   public void onGetItem(L1ItemInstance item) {
     if (getNpcTemplate().get_digestitem() > 0) {
       setDigestItem(item);
     }
     Arrays.sort(healPotions);
     Arrays.sort(haestPotions);
     if (Arrays.binarySearch(healPotions, item.getItem().getItemId()) >= 0) {
       if (getCurrentHp() != getMaxHp()) {
         useItem(0, 100);
       }
     }
     else if (Arrays.binarySearch(haestPotions, item.getItem().getItemId()) >= 0) {
       useItem(1, 100);
     }
   }

   private int ActionType(String action) {
     int status = 0;
     if (action.equalsIgnoreCase("aggressive")) {
       status = 1;
     } else if (action.equalsIgnoreCase("defensive")) {
       status = 2;
     } else if (action.equalsIgnoreCase("stay")) {
       status = 3;
     } else if (action.equalsIgnoreCase("extend")) {
       status = 4;
     } else if (action.equalsIgnoreCase("alert")) {
       status = 5;
     } else if (action.equalsIgnoreCase("dismiss")) {
       status = 6;
     }
     return status;
   }


   public void setCurrentHp(int i) {
     super.setCurrentHp(i);

     if (getMaxHp() > getCurrentHp()) {
       startHpRegeneration();
     }

     if (this._master instanceof L1PcInstance) {
       L1PcInstance Master = (L1PcInstance)this._master;
       Master.sendPackets((ServerBasePacket)new S_HPMeter(this));
     }
   }


   public void setCurrentMp(int i) {
     super.setCurrentMp(i);

     if (getMaxMp() > getCurrentMp()) {
       startMpRegeneration();
     }
   }

   public void set_currentPetStatus(int i) {
     this._currentPetStatus = i;
     if (this._currentPetStatus == 5) {
       setHomeX(getX());
       setHomeY(getY());
     }

     if (this._currentPetStatus == 3) {
       allTargetClear();
     }
     else if (!isAiRunning()) {
       startAI();
     }
   }


   public int get_currentPetStatus() {
     return this._currentPetStatus;
   }

   public boolean isExsistMaster() {
     boolean isExistMaster = false;
     if (getMaster() != null) {
       String masterName = getMaster().getName();
       if (L1World.getInstance().getPlayer(masterName) != null) {
         isExistMaster = true;
       }
     }
     return isExistMaster;
   }


   public boolean checkCondition() {
     if (this._master == null) {
       return true;
     }

     if (this._master instanceof L1PcInstance && ((L1PcInstance)this._master).isInWarArea()) {



       for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer((L1Object)this)) {
         visiblePc.sendPackets((ServerBasePacket)new S_SummonPack(this, visiblePc, false));
       }
       deleteMe();
       this._master = null;
       return true;
     }
     return false;
   }

   private static int _instanceType = -1;

   public int getL1Type() {
     return (_instanceType == -1) ? (_instanceType = super.getL1Type() | 0x20) : _instanceType;
   }
 }


