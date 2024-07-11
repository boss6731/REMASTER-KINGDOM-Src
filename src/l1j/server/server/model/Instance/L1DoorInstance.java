 package l1j.server.server.model.Instance;

 import l1j.server.Config;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1CastleLocation;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Clan;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.map.L1WorldMap;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_Door;
 import l1j.server.server.serverpackets.S_DoorPack;
 import l1j.server.server.serverpackets.S_RemoveObject;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.types.Point;

 public class L1DoorInstance
   extends L1NpcInstance
 {
   private static final long serialVersionUID = 1L;
   public static final int PASS = 0;
   public static final int NOT_PASS = 1;
   private int _doorId = 0;
   private int _direction = 0;
   private int _leftEdgeLocation = 0;
   private int _rightEdgeLocation = 0;
   private int _openStatus = 29;
   private int _passable = 1;
   private int _keeperId = 0;
   private int _autostatus = 0; private int _crackStatus;

   public L1DoorInstance(L1Npc template) {
     super(template);
   }


   public void onAction(L1PcInstance pc) {
     if (getMaxHp() == 0 || getMaxHp() == 1) {
       return;
     }

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


   public void onPerceive(L1PcInstance perceivedFrom) {
     perceivedFrom.addKnownObject((L1Object)this);
     if (perceivedFrom.getAI() != null) {
       return;
     }
     perceivedFrom.sendPackets((ServerBasePacket)new S_DoorPack(this));
     sendDoorPacket(perceivedFrom);

     if (!isDead()) {

       if (this._crackStatus == 5) {
         perceivedFrom.sendPackets((ServerBasePacket)new S_DoActionGFX(getId(), 36));
       } else if (this._crackStatus == 4) {
         perceivedFrom.sendPackets((ServerBasePacket)new S_DoActionGFX(getId(), 35));
       } else if (this._crackStatus == 3) {
         perceivedFrom.sendPackets((ServerBasePacket)new S_DoActionGFX(getId(), 34));
       } else if (this._crackStatus == 2) {
         perceivedFrom.sendPackets((ServerBasePacket)new S_DoActionGFX(getId(), 33));
       } else if (this._crackStatus == 1) {
         perceivedFrom.sendPackets((ServerBasePacket)new S_DoActionGFX(getId(), 32));
       }
     } else {
       perceivedFrom.sendPackets((ServerBasePacket)new S_DoActionGFX(getId(), 37));
     }
   }


   public void deleteMe() {
     setPassable(0);
     sendDoorPacket((L1PcInstance)null);

     this._destroyed = true;
     if (getInventory() != null) {
       getInventory().clearItems();
     }
     allTargetClear();
     this._master = null;
     L1World.getInstance().removeVisibleObject((L1Object)this);
     L1World.getInstance().removeObject((L1Object)this);
     for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)this)) {
       if (pc == null)
         continue;  pc.removeKnownObject((L1Object)this);
       pc.sendPackets((ServerBasePacket)new S_RemoveObject((L1Object)this));
     }
     removeAllKnownObjects();
   }



   public void receiveDamage(L1Character attacker, int damage) {
     if (getMaxHp() == 0 || getMaxHp() == 1) {
       return;
     }
     int GfxId = getCurrentSpriteId();
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
         setStatus(DoorAction(GfxId, 37));
         Death death = new Death(attacker);
         GeneralThreadPool.getInstance().execute(death);
       }
       if (newHp > 0) {
         if (attacker instanceof L1PcInstance) {
           L1PcInstance pc = (L1PcInstance)attacker;
           L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
           if (clan != null) {
             int castleId = clan.getCastleId();
             if (castleId != L1CastleLocation.getCastleIdByArea(pc)) {
               L1CastleGuardInstance guard = null;
               for (L1Object object : L1World.getInstance().getVisibleObjects((L1Object)pc)) {
                 if (object instanceof L1CastleGuardInstance) {
                   guard = (L1CastleGuardInstance)object;
                   guard.setTarget(pc);
                 }
               }
             }
           } else {
             L1CastleGuardInstance guard = null;
             for (L1Object object : L1World.getInstance().getVisibleObjects((L1Object)pc)) {
               if (object instanceof L1CastleGuardInstance) {
                 guard = (L1CastleGuardInstance)object;
                 guard.setTarget(pc);
               }
             }
           }
         }

         setCurrentHp(newHp);
         if (getMaxHp() * 1 / 6 > getCurrentHp()) {
           if (this._crackStatus != 5) {
             broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), DoorAction(GfxId, 36)));
             setStatus(DoorAction(GfxId, 36));
             this._crackStatus = 5;
           }
         } else if (getMaxHp() * 2 / 6 > getCurrentHp()) {
           if (this._crackStatus != 4) {
             broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 35));
             setStatus(35);
             this._crackStatus = 4;
           }
         } else if (getMaxHp() * 3 / 6 > getCurrentHp()) {
           if (this._crackStatus != 3) {
             broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 34));
             setStatus(34);
             this._crackStatus = 3;
           }
         } else if (getMaxHp() * 4 / 6 > getCurrentHp()) {
           if (this._crackStatus != 2) {
             broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 33));
             setStatus(33);
             this._crackStatus = 2;
           }
         } else if (getMaxHp() * 5 / 6 > getCurrentHp() &&
           this._crackStatus != 1) {
           broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 32));
           setStatus(32);
           this._crackStatus = 1;
         }

       }
     } else if (!isDead()) {
       setDead(true);
       setStatus(DoorAction(GfxId, 37));

       Death death = new Death(attacker);
       GeneralThreadPool.getInstance().execute(death);
     }
   }


   public void setCurrentHp(int i) {
     super.setCurrentHp(i);
   }

   class Death implements Runnable {
     L1Character _lastAttacker;

     public Death(L1Character lastAttacker) {
       this._lastAttacker = lastAttacker;
     }


     public void run() {
       if (L1DoorInstance.this.hasSkillEffect(5055)) {
         L1DoorInstance.this.setPresherPc(null);
         L1DoorInstance.this.setPresherDamage(0);

         if (L1DoorInstance.this.getPresherDeathRecall()) {
           L1DoorInstance.this.setPresherDeathRecall(false);
         }
         L1DoorInstance.this.removeSkillEffect(5055);
       }

       int GfxId = L1DoorInstance.this.getCurrentSpriteId();
       L1DoorInstance.this.setCurrentHp(0);
       L1DoorInstance.this.setDead(true);
       L1DoorInstance.this.isPassibleDoor(true);
       L1DoorInstance.this.setStatus(L1DoorInstance.this.DoorAction(GfxId, 37));
       L1DoorInstance.this.getMap().setPassable((Point)L1DoorInstance.this.getLocation(), true);
       L1DoorInstance.this.broadcastPacket((ServerBasePacket)new S_DoActionGFX(L1DoorInstance.this.getId(), L1DoorInstance.this.DoorAction(GfxId, 37)));
       L1DoorInstance.this.setPassable(0);
       L1DoorInstance.this.sendDoorPacket((L1PcInstance)null);
     }
   }

   class DoorTimer
     implements Runnable {
     public void run() {
       if (L1DoorInstance.this._destroyed) {
         return;
       }
       L1DoorInstance.this.close();
     }
   }

   private void sendDoorPacket(L1PcInstance pc) {
     int entranceX = getEntranceX();
     int entranceY = getEntranceY();
     int leftEdgeLocation = getLeftEdgeLocation();
     int rightEdgeLocation = getRightEdgeLocation();

     int size = rightEdgeLocation - leftEdgeLocation;
     if (size == 0) {
       sendPacket(pc, entranceX, entranceY);

     }
     else if (getDirection() == 0) {
       for (int x = leftEdgeLocation; x <= rightEdgeLocation; x++) {
         if (size >= 5) {
           sendPacket(pc, x, entranceY);
           sendPacket(pc, x, entranceY - 1);
         } else {
           sendPacket(pc, x, entranceY);
         }
       }
     } else {
       for (int y = leftEdgeLocation; y <= rightEdgeLocation; y++) {
         if (size >= 4) {
           sendPacket(pc, entranceX, y);
           sendPacket(pc, entranceX + 1, y);
         } else {
           sendPacket(pc, entranceX, y);
         }
       }
     }
   }



   private void sendPacket(L1PcInstance pc, int x, int y) {
     int att = 0;
     if (!isDead() || getOpenStatus() == 29) {
       att = 1;
     }
     if (isDead() || getOpenStatus() == 28) {
       att = 0;
     }
     S_Door packet = new S_Door(x, y, getDirection(), att);
     if (pc != null) {
       pc.sendPackets((ServerBasePacket)packet);
     } else {
       broadcastPacket((ServerBasePacket)packet);
     }
   }

   public void open() {
     if (isDead()) {
       return;
     }
     if (getOpenStatus() == 29) {
       isPassibleDoor(true);
       if (getDoorId() == 113 || getDoorId() == 125) {
         GeneralThreadPool.getInstance().schedule(new DoorTimer(), 5000L);
       }

       if (getDoorId() >= 4100 && getDoorId() <= 4111) {
         GeneralThreadPool.getInstance().schedule(new DoorTimer(), 300000L);
       }
       if (getDoorId() >= 8001 && getDoorId() <= 8010) {
         GeneralThreadPool.getInstance().schedule(new DoorTimer(), 1800000L);
       }
       if (getDoorId() == 2110) {
         GeneralThreadPool.getInstance().schedule(new DoorTimer(), 60000L);
       }
       if (getDoorId() == 2111) {
         GeneralThreadPool.getInstance().schedule(new DoorTimer(), 60000L);
       }
       if (getDoorId() == 2112) {
         GeneralThreadPool.getInstance().schedule(new DoorTimer(), 60000L);
       }
       if (getDoorId() == 2113) {
         GeneralThreadPool.getInstance().schedule(new DoorTimer(), 60000L);
       }
       broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 28));
       setOpenStatus(28);
       setPassable(0);
       sendDoorPacket((L1PcInstance)null);
     }
   }

   public void close() {
     if (isDead()) {
       return;
     }
     if (getOpenStatus() == 28) {
       isPassibleDoor(false);
       broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 29));
       setOpenStatus(29);
       setPassable(1);
       sendDoorPacket((L1PcInstance)null);
     }
   }




























   public int DoorAction(int GfxId, int dooraction) {
     int Action = dooraction;
     if (Action == 37) {
       if (GfxId == 11987 || GfxId == 11989 || GfxId == 11991 || GfxId == 12127 || GfxId == 12129 || GfxId == 12133 || GfxId == 12163 || GfxId == 12164 || GfxId == 12167 || GfxId == 12170)
       {

         Action = 36;
       }
     } else if (Action == 36 && (
       GfxId == 11987 || GfxId == 11989 || GfxId == 11991 || GfxId == 12127 || GfxId == 12129 || GfxId == 12133 || GfxId == 12163 || GfxId == 12164 || GfxId == 12167 || GfxId == 12170)) {


       Action = 35;
     }

     return Action;
   }




   public void isPassibleDoor(boolean flag) {
     int leftEdgeLocation = getLeftEdgeLocation();
     int rightEdgeLocation = getRightEdgeLocation();
     int size = rightEdgeLocation - leftEdgeLocation;
     if (size == 0) {
       L1WorldMap.getInstance().getMap(getMapId()).setPassable(getX(), getY(), flag);
     }
     else if (getDirection() == 0) {
       int doorX = leftEdgeLocation;
       for (; doorX <= rightEdgeLocation; doorX++) {
         L1WorldMap.getInstance().getMap(getMapId()).setPassable(doorX, getY(), flag);
       }
     } else {
       int doorY = leftEdgeLocation;
       for (; doorY <= rightEdgeLocation; doorY++) {
         L1WorldMap.getInstance().getMap(getMapId()).setPassable(getX(), doorY, flag);
       }
     }
   }


   public void repairGate() {
     if (getMaxHp() > 1) {
       setDead(false);
       setCurrentHp(getMaxHp());
       setStatus(0);
       setCrackStatus(0);
       setOpenStatus(28);
       close();
     }
   }

   public int getDoorId() { return this._doorId; } public void setDoorId(int i) {
     this._doorId = i;
   } public int getDirection() {
     return this._direction;
   } public void setDirection(int i) {
     if (i == 0 || i == 1) {
       this._direction = i;
     }
   }

   public int getEntranceX() {
     int entranceX = 0;
     if (getDirection() == 0) {
       entranceX = getX();
     } else {
       entranceX = getX() - 1;
     }
     return entranceX;
   }

   public int getEntranceY() {
     int entranceY = 0;
     if (getDirection() == 0) {
       entranceY = getY() + 1;
     } else {
       entranceY = getY();
     }
     return entranceY;
   }

   public int getLeftEdgeLocation() {
     return this._leftEdgeLocation;
   }
   public void setLeftEdgeLocation(int i) {
     this._leftEdgeLocation = i;
   }

   public int getRightEdgeLocation() {
     return this._rightEdgeLocation;
   }
   public void setRightEdgeLocation(int i) {
     this._rightEdgeLocation = i;
   }

   public int getOpenStatus() {
     return this._openStatus;
   }

   public void setOpenStatus(int i) {
     if (i == 28 || i == 29) {
       this._openStatus = i;
     }
   }

   public int getPassable() {
     return this._passable;
   }

   public void setPassable(int i) {
     if (i == 0 || i == 1)
       this._passable = i;
   }

   public int getKeeperId() {
     return this._keeperId; } public void setKeeperId(int i) {
     this._keeperId = i;
   }

   public int getCrackStatus() {
     return this._crackStatus; } public void setCrackStatus(int i) {
     this._crackStatus = i;
   }
   public int getAutoStatus() {
     return this._autostatus;
   }

   public void setAutoStatus(int i) {
     this._autostatus = i;
   }
 }


