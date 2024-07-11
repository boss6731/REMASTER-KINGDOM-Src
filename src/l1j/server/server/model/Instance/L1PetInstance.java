 package l1j.server.server.model.Instance;

 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.List;
 import java.util.Random;
 import l1j.server.server.IdFactory;
 import l1j.server.server.datatables.ExpTable;
 import l1j.server.server.datatables.PetItemTable;
 import l1j.server.server.datatables.PetTable;
 import l1j.server.server.datatables.PetTypeTable;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1GroundInventory;
 import l1j.server.server.model.L1Inventory;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1PcInventory;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_HPMeter;
 import l1j.server.server.serverpackets.S_NpcChatPacket;
 import l1j.server.server.serverpackets.S_PetCtrlMenu;
 import l1j.server.server.serverpackets.S_PetMenuPacket;
 import l1j.server.server.serverpackets.S_PetPack;
 import l1j.server.server.serverpackets.S_ReturnedStat;
 import l1j.server.server.serverpackets.S_ServerMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.storage.CharactersItemStorage;
 import l1j.server.server.storage.mysql.MySqlCharacterStorage;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.templates.L1Pet;
 import l1j.server.server.templates.L1PetItem;
 import l1j.server.server.templates.L1PetType;
 import l1j.server.server.types.Point;

 public class L1PetInstance
   extends L1NpcInstance {
   private static final long serialVersionUID = 1L;
   private static Random _random = new Random(System.nanoTime()); private L1ItemInstance _weapon; private L1ItemInstance _armor; private int _hitByWeapon; private int _damageByWeapon; private int _currentPetStatus;

   public synchronized void deleteMe() {
     Object[] aobj = this._master.getPetList().values().toArray();
     for (int i = 0; i < aobj.length; i++) {
       if (aobj[i] == this)
         this._petMaster.sendPackets((ServerBasePacket)new S_ReturnedStat(12, i * 3, getId(), false));
     }  super.deleteMe();
   }
   private L1PcInstance _petMaster; private int _petMasterId; private int _itemObjId; private L1PetType _type; private int _expPercent;

   public boolean noTarget() {
     if (this._currentPetStatus == 3)
       return true;
     if (this._currentPetStatus == 4) {
       if (this._petMaster != null && this._petMaster.getMapId() == getMapId() && getLocation().getTileLineDistance((Point)this._petMaster.getLocation()) < 5) {
         int dir = targetReverseDirection(this._petMaster.getX(), this._petMaster.getY());
         dir = checkObject(getX(), getY(), getMapId(), dir);
         setSleepTime(setDirectionMoveSpeed(dir));
       } else {
         this._currentPetStatus = 3;
         return true;
       }
     } else if (this._currentPetStatus == 5) {
       if (Math.abs(getHomeX() - getX()) > 1 ||
         Math.abs(getHomeY() - getY()) > 1) {
         int dir = moveDirection(getMapId(), getHomeX(), getHomeY());
         if (dir == -1) {
           setHomeX(getX());
           setHomeY(getY());
         } else {
           setSleepTime(setDirectionMoveSpeed(dir));
         }
       }
     } else if (this._currentPetStatus == 7) {
       if (this._petMaster != null && this._petMaster
         .getMapId() == getMapId() && getLocation().getTileLineDistance((Point)this._petMaster
           .getLocation()) <= 1) {
         this._currentPetStatus = 3;
         return true;
       }
       int locx = this._petMaster.getX() + _random.nextInt(1);
       int locy = this._petMaster.getY() + _random.nextInt(1);
       int dir = moveDirection(this._petMaster.getMapId(), locx, locy);
       if (dir == -1) {
         this._currentPetStatus = 3;
         return true;
       }
       setSleepTime(setDirectionMoveSpeed(dir));
     } else if (this._petMaster != null && this._petMaster.getMapId() == getMapId()) {
       if (getLocation().getTileLineDistance((Point)this._petMaster.getLocation()) > 2) {
         int dir = moveDirection(this._petMaster.getMapId(), this._petMaster.getX(), this._petMaster.getY());
         if (dir == -1) {
           this._currentPetStatus = 3;
           return true;
         }
         setSleepTime(setDirectionMoveSpeed(dir));
         if (this._currentPetStatus == 8) {
           collect();
         }
       }
     } else {
       this._currentPetStatus = 3;
       return true;
     }
     return false;
   }

   public L1PetInstance(L1Npc template, L1PcInstance master, L1Pet l1pet) {
     super(template);

     this._petMaster = master;
     this._petMasterId = master.getId();
     this._itemObjId = l1pet.get_itemobjid();
     this._type = PetTypeTable.getInstance().get(template.get_npcId());

     setId(l1pet.get_objid());
     setName(l1pet.get_name());
     setLevel(l1pet.get_level());
     setMaxHp(l1pet.get_hp());
     setCurrentHp(l1pet.get_hp());
     setMaxMp(l1pet.get_mp());
     setCurrentMp(l1pet.get_mp());
     set_exp(l1pet.get_exp());
     setExpPercent(ExpTable.getExpPercentage(l1pet.get_level(), l1pet
           .get_exp()));
     setLawful(l1pet.get_lawful());
     setTempLawful(l1pet.get_lawful());

     setMaster(master);
     setX(master.getX() + _random.nextInt(5) - 2);
     setY(master.getY() + _random.nextInt(5) - 2);
     setMap(master.getMapId());
     setHeading(5);
     setLightSize(template.getLightSize());

     this._currentPetStatus = 3;

     L1World.getInstance().storeObject((L1Object)this);
     L1World.getInstance().addVisibleObject((L1Object)this);
     for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)this)) {
       onPerceive(pc);
     }
     master.addPet(this);
     Object[] aobj = master.getPetList().values().toArray();
     master.sendPackets((ServerBasePacket)new S_ReturnedStat(12, (aobj.length + 1) * 3, getId(), true));
   }

   public L1PetInstance(L1NpcInstance target, L1PcInstance master, int itemid) {
     super((L1Npc)null);

     this._petMaster = master;
     this._petMasterId = master.getId();
     this._itemObjId = itemid;
     this._type = PetTypeTable.getInstance().get(target
         .getNpcTemplate().get_npcId());

     setId(IdFactory.getInstance().nextId());
     setting_template(target.getNpcTemplate());
     setCurrentHp(target.getCurrentHp());
     setCurrentMp(target.getCurrentMp());
     set_exp(750L);
     setExpPercent(0);
     setLawful(0);
     setTempLawful(0);

     setMaster(master);
     setX(target.getX());
     setY(target.getY());
     setMap(target.getMapId());
     setHeading(target.getHeading());
     setLightSize(target.getLightSize());
     setPetcost(6);
     setInventory(target.getInventory());
     target.setInventory((L1Inventory)null);

     this._currentPetStatus = 3;

     target.deleteMe();
     L1World.getInstance().storeObject((L1Object)this);
     L1World.getInstance().addVisibleObject((L1Object)this);
     for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)this)) {
       onPerceive(pc);
     }

     master.addPet(this);
     Object[] aobj = master.getPetList().values().toArray();
     master.sendPackets((ServerBasePacket)new S_ReturnedStat(12, (aobj.length + 1) * 3, getId(), true));
     PetTable.getInstance().storeNewPet(target, getId(), itemid);
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
       }

       if (attacker instanceof L1PcInstance && damage > 0) {
         L1PcInstance player = (L1PcInstance)attacker;
         player.set_pet_target(this);
       }

       int newHp = getCurrentHp() - damage;
       if (newHp <= 0) {
         death(attacker);
       } else {
         setCurrentHp(newHp);
       }
     } else if (!isDead()) {
       death(attacker);
     }
   }

   public synchronized void death(L1Character lastAttacker) {
     if (!isDead()) {
       setDead(true);
       setStatus(8);
       setCurrentHp(0);

       getMap().setPassable((Point)getLocation(), true);
       broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 8));
     }
   }


   public void evolvePet(int new_itemobjid) {
     L1Pet l1pet = PetTable.getInstance().getTemplate(this._itemObjId);
     if (l1pet == null) {
       return;
     }

     int newNpcId = this._type.getNpcIdForEvolving();
     int tmpMaxHp = getMaxHp();
     int tmpMaxMp = getMaxMp();

     transform(newNpcId);
     this._type = PetTypeTable.getInstance().get(newNpcId);

     setLevel(1L);
     setMaxHp(tmpMaxHp / 2);
     setMaxMp(tmpMaxMp / 2);
     setCurrentHp(getMaxHp());
     setCurrentMp(getMaxMp());
     set_exp(0L);
     setExpPercent(0);

     getInventory().clearItems();

     PetTable.getInstance().deletePet(this._itemObjId);

     l1pet.set_itemobjid(new_itemobjid);
     l1pet.set_npcid(newNpcId);
     l1pet.set_name(getName());
     l1pet.set_level(getLevel());
     l1pet.set_hp(getMaxHp());
     l1pet.set_mp(getMaxMp());
     l1pet.set_exp(get_exp());
     PetTable.getInstance().storeNewPet(this, getId(), new_itemobjid);

     this._itemObjId = new_itemobjid;
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
     monster.setLevel(getLevel());
     monster.setMaxHp(getMaxHp());
     monster.setCurrentHp(getCurrentHp());
     monster.setMaxMp(getMaxMp());
     monster.setCurrentMp(getCurrentMp());

     this._petMaster.getPetList().remove(Integer.valueOf(getId()));
     deleteMe();

     if (this._petMaster.getPetList().isEmpty()) {
       this._petMaster.sendPackets((ServerBasePacket)new S_PetCtrlMenu((L1PcInstance)this._master, monster, false));
     }

     this._petMaster.getInventory().removeItem(this._itemObjId, 1);
     PetTable.getInstance().deletePet(this._itemObjId);

     L1World.getInstance().storeObject((L1Object)monster);
     L1World.getInstance().addVisibleObject((L1Object)monster);
     for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)monster)) {
       onPerceive(pc);
     }
   }

   public void collect() {
     L1PcInventory l1PcInventory = this._petMaster.getInventory();
     List<L1ItemInstance> items = this._inventory.getItems();
     int size = this._inventory.getSize();
     L1ItemInstance item = null;
     for (int i = 0; i < size; i++) {
       item = items.get(0);
       if (!item.isEquipped())
       {

         if (this._petMaster.getInventory().checkAddItem(item, item
             .getCount()) == 0) {
           this._inventory.tradeItem(item, item.getCount(), (L1Inventory)l1PcInventory);
           this._petMaster.sendPackets((ServerBasePacket)new S_ServerMessage(143, getName(), item
                 .getLogName()));
         } else {
           L1GroundInventory l1GroundInventory = L1World.getInstance().getInventory(getX(),
               getY(), getMapId());
           this._inventory.tradeItem(item, item.getCount(), (L1Inventory)l1GroundInventory);
         }  }
     }
   }

   public void unloadMaster() {
     getMap().setPassable((Point)getLocation(), true);
     dropItem();
     if (this._petMaster != null)
       this._petMaster.getPetList().remove(Integer.valueOf(getId()));
     deleteMe();
   }
   public void dropItem() {
     L1PcInventory l1PcInventory;
     L1Inventory targetInventory = null;
     List<L1ItemInstance> items = this._inventory.getItems();
     if (items == null || items.size() <= 0) {
       return;
     }
     int size = this._inventory.getSize();
     L1ItemInstance item = null;
     if (this._petMaster != null) {
       l1PcInventory = this._petMaster.getInventory();
     } else {
       L1Object obj = L1World.getInstance().findObject(this._petMasterId);
       if (obj == null || !(obj instanceof L1PcInstance)) {
         if (!MySqlCharacterStorage.isValidUserObjectId(this._petMasterId)) {
           L1GroundInventory l1GroundInventory = L1World.getInstance().getInventory(getX(), getY(), getMapId());
         }
       } else {
         L1PcInstance pc = (L1PcInstance)obj;
         if (pc.getNetConnection() != null && pc.getNetConnection().isConnected()) {
           l1PcInventory = pc.getInventory();
         }
       }
     }


     if (l1PcInventory == null) {
       CharactersItemStorage storage = CharactersItemStorage.create();
       for (int i = size - 1; i >= 0; i--) {
         item = items.get(0);
         item.setEquipped(false);
         try {
           storage.storeItem(this._petMasterId, item);
         } catch (Exception e) {
           e.printStackTrace();
         }
       }
       this._inventory.clearItems();
     }
     else {

       for (int i = 0; i < size; i++) {
         item = items.get(0);
         item.setEquipped(false);
         this._inventory.tradeItem(item, item.getCount(), (L1Inventory)l1PcInventory);
       }
     }
   }

   public void call() {
     int id = this._type.getMessageId(L1PetType.getMessageNumber(getLevel()));
     if (id != 0) {
       broadcastPacket((ServerBasePacket)new S_NpcChatPacket(this, "$" + id, 0));
     }

     setCurrentPetStatus(7);
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


   public void onPerceive(L1PcInstance perceivedFrom) {
     perceivedFrom.addKnownObject((L1Object)this);
     if (perceivedFrom.getAI() != null) {
       return;
     }
     perceivedFrom.sendPackets((ServerBasePacket)new S_PetPack(this, perceivedFrom));
     if (isDead()) {
       perceivedFrom.sendPackets((ServerBasePacket)new S_DoActionGFX(getId(), 8));
     }

     if (this._petMaster != null && this._petMaster == perceivedFrom) {
       this._petMaster.sendPackets((ServerBasePacket)new S_HPMeter(this));
     }
   }


   public void onAction(L1PcInstance player) {
     if (player == null) {
       return;
     }
     L1Character cha = getMaster();
     L1PcInstance master = (L1PcInstance)cha;
     if (master == null)
       return;  if (master.get_teleport()) {
       return;
     }
     if (getZoneType() == 1) {
       L1Attack attack_mortion = new L1Attack(player, this);
       attack_mortion.action();

       return;
     }
     if (player.checkNonPvP(player, this)) {
       return;
     }

     L1Attack attack = new L1Attack(player, this);
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
     if (this._petMaster.equals(player)) {
       player.sendPackets((ServerBasePacket)new S_PetMenuPacket(this, getExpPercent()));
       L1Pet l1pet = PetTable.getInstance().getTemplate(this._itemObjId);
       if (l1pet != null) {
         l1pet.set_exp(get_exp());
         l1pet.set_level(getLevel());
         l1pet.set_hp(getMaxHp());
         l1pet.set_mp(getMaxMp());
         PetTable.getInstance().storePet(l1pet);
       }
     }
   }


   public void onFinalAction(L1PcInstance player, String action) {
     int status = actionType(action);
     if (status == 0) {
       return;
     }
     if (status == 6) {
       liberate();
     } else {
       Object[] petList = this._petMaster.getPetList().values().toArray();
       L1PetInstance pet = null;
       L1PetType type = null;
       for (Object petObject : petList) {
         if (petObject instanceof L1PetInstance) {
           pet = (L1PetInstance)petObject;
           if (this._petMaster != null && this._petMaster.getLevel() >= pet.getLevel()) {
             pet.setCurrentPetStatus(status);
           } else {
             type = PetTypeTable.getInstance().get(pet.getNpcTemplate().get_npcId());
             int id = type.getDefyMessageId();
             if (id != 0) {
               broadcastPacket((ServerBasePacket)new S_NpcChatPacket(pet, "$" + id, 0));
             }
           }
         }
       }

       player.sendPackets((ServerBasePacket)new S_PetMenuPacket(this, getExpPercent()));
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
     if (item == null || item.getItem() == null)
       return;
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

   private int actionType(String action) {
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
     } else if (action.equalsIgnoreCase("getitem")) {
       status = 8;

       collection();
     }
     return status;
   }

   private void collection() {
     ArrayList<L1GroundInventory> gInventorys = new ArrayList<>();

     for (L1Object obj : L1World.getInstance().getVisibleObjects((L1Object)this, 10)) {
       if (obj != null && obj instanceof L1GroundInventory) {
         gInventorys.add((L1GroundInventory)obj);
       }
     }

     int groundinv = gInventorys.size();
     for (int i = 0; i < groundinv; i++) {
       L1GroundInventory inventory = gInventorys.get(i);
       for (L1ItemInstance item : inventory.getItems()) {
         if (getInventory().checkAddItem(item, item.getCount()) == 0 &&
           !item.getItem().isUseHighPet()) {
           this._targetItem = item;
           this._targetItemList.add(this._targetItem);
         }
       }
     }
   }


   public void setCurrentHp(int i) {
     super.setCurrentHp(i);

     if (getMaxHp() > getCurrentHp()) {
       startHpRegeneration();
     }

     if (this._petMaster != null) {
       L1PcInstance Master = this._petMaster;
       Master.sendPackets((ServerBasePacket)new S_HPMeter(this));
     }
   }


   public void setCurrentMp(int i) {
     super.setCurrentMp(i);

     if (getMaxMp() > getCurrentMp()) {
       startMpRegeneration();
     }
   }

   public void setCurrentPetStatus(int i) {
     this._currentPetStatus = i;
     if (this._currentPetStatus == 5) {
       setHomeX(getX());
       setHomeY(getY());
     }
     if (this._currentPetStatus == 7) {
       allTargetClear();
     }

     if (this._currentPetStatus == 3) {
       allTargetClear();
     }
     else if (!isAiRunning()) {
       startAI();
     }
   }


   public void usePetWeapon(L1ItemInstance weapon) {
     if (getWeapon() == null) {
       setPetWeapon(weapon);
     }
     else if (getWeapon().equals(weapon)) {
       removePetWeapon(getWeapon());
     } else {
       removePetWeapon(getWeapon());
       setPetWeapon(weapon);
     }
   }


   public void usePetArmor(L1ItemInstance armor) {
     if (getArmor() == null) {
       setPetArmor(armor);
     }
     else if (getArmor().equals(armor)) {
       removePetArmor(getArmor());
     } else {
       removePetArmor(getArmor());
       setPetArmor(armor);
     }
   }


   private void setPetWeapon(L1ItemInstance weapon) {
     int itemId = weapon.getItem().getItemId();
     L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);

     if (petItem == null)
       return;
     setHitByWeapon(petItem.getHitModifier());
     setDamageByWeapon(petItem.getDamageModifier());
     getAbility().addAddedStr(petItem.getAddStr());
     getAbility().addAddedCon(petItem.getAddCon());
     getAbility().addAddedDex(petItem.getAddDex());
     getAbility().addAddedInt(petItem.getAddInt());
     getAbility().addAddedWis(petItem.getAddWis());
     addMaxHp(petItem.getAddHp());
     addMaxMp(petItem.getAddMp());
     getAbility().addSp(petItem.getAddSp());
     getResistance().addMr(petItem.getAddMr());

     setWeapon(weapon);
     weapon.setEquipped(true);
   }

   public void setPetArmor(L1ItemInstance armor) {
     int itemId = armor.getItem().getItemId();
     L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);

     if (petItem == null)
       return;
     getAC().addAc(petItem.getAddAc());
     getAbility().addAddedStr(petItem.getAddStr());
     getAbility().addAddedCon(petItem.getAddCon());
     getAbility().addAddedDex(petItem.getAddDex());
     getAbility().addAddedInt(petItem.getAddInt());
     getAbility().addAddedWis(petItem.getAddWis());
     addMaxHp(petItem.getAddHp());
     addMaxMp(petItem.getAddMp());
     getAbility().addSp(petItem.getAddSp());
     getResistance().addMr(petItem.getAddMr());

     setArmor(armor);
     armor.setEquipped(true);
   }

   public void removePetWeapon(L1ItemInstance weapon) {
     int itemId = weapon.getItem().getItemId();
     L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);

     if (petItem == null)
       return;
     setHitByWeapon(0);
     setDamageByWeapon(0);
     getAbility().addAddedStr(-petItem.getAddStr());
     getAbility().addAddedCon(-petItem.getAddCon());
     getAbility().addAddedDex(-petItem.getAddDex());
     getAbility().addAddedInt(-petItem.getAddInt());
     getAbility().addAddedWis(-petItem.getAddWis());
     addMaxHp(-petItem.getAddHp());
     addMaxMp(-petItem.getAddMp());
     getAbility().addSp(-petItem.getAddSp());
     getResistance().addMr(-petItem.getAddMr());

     setWeapon((L1ItemInstance)null);
     weapon.setEquipped(false);
   }

   public void removePetArmor(L1ItemInstance armor) {
     int itemId = armor.getItem().getItemId();
     L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);

     if (petItem == null)
       return;
     getAC().addAc(-petItem.getAddAc());
     getAbility().addAddedStr(-petItem.getAddStr());
     getAbility().addAddedCon(-petItem.getAddCon());
     getAbility().addAddedDex(-petItem.getAddDex());
     getAbility().addAddedInt(-petItem.getAddInt());
     getAbility().addAddedWis(-petItem.getAddWis());
     addMaxHp(-petItem.getAddHp());
     addMaxMp(-petItem.getAddMp());
     getAbility().addSp(-petItem.getAddSp());
     getResistance().addMr(-petItem.getAddMr());

     setArmor((L1ItemInstance)null);
     armor.setEquipped(false);
   }

   public int getCurrentPetStatus() {
     return this._currentPetStatus;
   }

   public int getItemObjId() {
     return this._itemObjId;
   }

   public void setExpPercent(int expPercent) {
     this._expPercent = expPercent;
   }

   public int getExpPercent() {
     return this._expPercent;
   }


   public void setWeapon(L1ItemInstance weapon) {
     this._weapon = weapon;
   }

   public L1ItemInstance getWeapon() {
     return this._weapon;
   }



   public void setArmor(L1ItemInstance armor) {
     this._armor = armor;
   }

   public L1ItemInstance getArmor() {
     return this._armor;
   }



   public void setHitByWeapon(int i) {
     this._hitByWeapon = i;
   }

   public int getHitByWeapon() {
     return this._hitByWeapon;
   }



   public void setDamageByWeapon(int i) {
     this._damageByWeapon = i;
   }

   public int getDamageByWeapon() {
     return this._damageByWeapon;
   }








   public L1PcInstance getMaster() {
     return this._petMaster;
   }

   public int getMasterId() {
     return this._petMasterId;
   }

   public L1PetType getPetType() {
     return this._type;
   }


   public boolean checkCondition() {
     if (this._petMaster == null) {
       return true;
     }

     if (this._petMaster.isInWarArea()) {
       dropItem();
       this._petMaster.getPetList().remove(Integer.valueOf(getId()));
       deleteMe();

       return true;
     }

     return false;
   }


   public void usePetWeapon(L1PcInstance pc, L1PetInstance pet, L1ItemInstance weapon) {
     if (pet.getWeapon() == null) {
       setPetWeapon(pc, pet, weapon);
     }
     else if (pet.getWeapon().equals(weapon)) {
       removePetWeapon(pc, pet, pet.getWeapon());
     } else {
       removePetWeapon(pc, pet, pet.getWeapon());
       setPetWeapon(pc, pet, weapon);
     }
   }



   public void usePetArmor(L1PcInstance pc, L1PetInstance pet, L1ItemInstance armor) {
     if (pet.getArmor() == null) {
       setPetArmor(pc, pet, armor);
     }
     else if (pet.getArmor().equals(armor)) {
       removePetArmor(pc, pet, pet.getArmor());
     } else {
       removePetArmor(pc, pet, pet.getArmor());
       setPetArmor(pc, pet, armor);
     }
   }



   public void setPetWeapon(L1PcInstance pc, L1PetInstance pet, L1ItemInstance weapon) {
     int itemId = weapon.getItem().getItemId();
     L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
     if (petItem == null) {
       return;
     }

     pet.setHitByWeapon(petItem.getHitModifier());
     pet.setDamageByWeapon(petItem.getDamageModifier());
     pet.getAbility().addAddedStr(petItem.getAddStr());
     pet.getAbility().addAddedCon(petItem.getAddCon());
     pet.getAbility().addAddedDex(petItem.getAddDex());
     pet.getAbility().addAddedInt(petItem.getAddInt());
     pet.getAbility().addAddedWis(petItem.getAddWis());
     pet.addMaxHp(petItem.getAddHp());
     pet.addMaxMp(petItem.getAddMp());
     pet.getAbility().addSp(petItem.getAddSp());
     pet.getResistance().addMr(petItem.getAddMr());

     pet.setWeapon(weapon);
     weapon.setEquipped(true);
   }


   public void removePetWeapon(L1PcInstance pc, L1PetInstance pet, L1ItemInstance weapon) {
     int itemId = weapon.getItem().getItemId();
     L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
     if (petItem == null) {
       return;
     }

     pet.setHitByWeapon(0);
     pet.setDamageByWeapon(0);
     pet.getAbility().addAddedStr(-petItem.getAddStr());
     pet.getAbility().addAddedCon(-petItem.getAddCon());
     pet.getAbility().addAddedDex(-petItem.getAddDex());
     pet.getAbility().addAddedInt(-petItem.getAddInt());
     pet.getAbility().addAddedWis(-petItem.getAddWis());
     pet.addMaxHp(-petItem.getAddHp());
     pet.addMaxMp(-petItem.getAddMp());
     pet.getAbility().addSp(-petItem.getAddSp());
     pet.getResistance().addMr(-petItem.getAddMr());

     pet.setWeapon((L1ItemInstance)null);
     weapon.setEquipped(false);
   }


   public void setPetArmor(L1PcInstance pc, L1PetInstance pet, L1ItemInstance armor) {
     int itemId = armor.getItem().getItemId();
     L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
     if (petItem == null) {
       return;
     }

     pet.getAC().addAc(petItem.getAddAc());
     pet.getAbility().addAddedStr(petItem.getAddStr());
     pet.getAbility().addAddedCon(petItem.getAddCon());
     pet.getAbility().addAddedDex(petItem.getAddDex());
     pet.getAbility().addAddedInt(petItem.getAddInt());
     pet.getAbility().addAddedWis(petItem.getAddWis());
     pet.addMaxHp(petItem.getAddHp());
     pet.addMaxMp(petItem.getAddMp());
     pet.getAbility().addSp(petItem.getAddSp());
     pet.getResistance().addMr(petItem.getAddMr());

     pet.setArmor(armor);
     armor.setEquipped(true);
   }


   public void removePetArmor(L1PcInstance pc, L1PetInstance pet, L1ItemInstance armor) {
     int itemId = armor.getItem().getItemId();
     L1PetItem petItem = PetItemTable.getInstance().getTemplate(itemId);
     if (petItem == null) {
       return;
     }

     pet.getAC().addAc(-petItem.getAddAc());
     pet.getAbility().addAddedStr(-petItem.getAddStr());
     pet.getAbility().addAddedCon(-petItem.getAddCon());
     pet.getAbility().addAddedDex(-petItem.getAddDex());
     pet.getAbility().addAddedInt(-petItem.getAddInt());
     pet.getAbility().addAddedWis(-petItem.getAddWis());
     pet.addMaxHp(-petItem.getAddHp());
     pet.addMaxMp(-petItem.getAddMp());
     pet.getAbility().addSp(-petItem.getAddSp());
     pet.getResistance().addMr(-petItem.getAddMr());

     pet.setArmor((L1ItemInstance)null);
     armor.setEquipped(false);
   }
 }


