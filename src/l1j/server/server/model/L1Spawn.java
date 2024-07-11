 package l1j.server.server.model;

 import java.lang.reflect.Constructor;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Random;
 import l1j.server.Config;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.IdFactory;
 import l1j.server.server.datatables.TownNpcInfoTable;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.monitor.LoggerInstance;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.templates.L1TownNpcInfo;
 import l1j.server.server.types.Point;
 import l1j.server.server.utils.L1SpawnUtil;



 public class L1Spawn
 {
     private final L1Npc _template;
     private int _id;
     private String _location;
     private int _maximumCount;
     private int _npcid;
     private int _groupId;
     private int _locx;
     private int _locy;
     private int _randomx;
     private int _randomy;
     private int _locx1;
     private int _locy1;
     private int _locx2;
     private int _locy2;
     private int _heading;
     private int _minRespawnDelay;
     private int _maxRespawnDelay;
     private final Constructor<?> _constructor;
     private short _mapid;
     private boolean _respaenScreen;
     private int _movementDistance;
     private boolean _rest;
     private int _spawnType;
     private int _delayInterval;
     private HashMap<Integer, Point> _homePoint = null;

     private static Random _random = new Random(System.nanoTime());
     private String _name;
     private boolean _initSpawn;
     private boolean _spawnHomePoint;
     public static final int SPAWN_TYPE_PC_AROUND = 1;
     public static final int PC_AROUND_DISTANCE = 30;

     private class SpawnTask implements Runnable {
         private SpawnTask(int spawnNumber, int objectId) {
             this._spawnNumber = spawnNumber;
             this._objectId = objectId;
         }
         private int _spawnNumber;
         private int _objectId;

         public void run() {
             L1Spawn.this.doSpawn(this._spawnNumber, this._objectId);
         }
     }







     public String getName() {
         return this._name;
     }

     public void setName(String name) {
         this._name = name;
     }

     public short getMapId() {
         return this._mapid;
     }

     public void setMapId(short _mapid) {
         this._mapid = _mapid;
     }

     public boolean isRespawnScreen() {
         return this._respaenScreen;
     }

     public void setRespawnScreen(boolean flag) {
         this._respaenScreen = flag;
     }

     public int getMovementDistance() {
         return this._movementDistance;
     }

     public void setMovementDistance(int i) {
         this._movementDistance = i;
     }

     public int getAmount() {
         return this._maximumCount;
     }

     public int getGroupId() {
         return this._groupId;
     }

     public int getId() {
         return this._id;
     }

     public String getLocation() {
         return this._location;
     }

     public int getLocX() {
         return this._locx;
     }

     public int getLocY() {
         return this._locy;
     }

     public int getNpcId() {
         return this._npcid;
     }

     public int getHeading() {
         return this._heading;
     }

     public int getRandomx() {
         return this._randomx;
     }

     public int getRandomy() {
         return this._randomy;
     }

     public int getLocX1() {
         return this._locx1;
     }

     public int getLocY1() {
         return this._locy1;
     }

     public int getLocX2() {
         return this._locx2;
     }

     public int getLocY2() {
         return this._locy2;
     }

     public int getMinRespawnDelay() {
         return this._minRespawnDelay;
     }

     public int getMaxRespawnDelay() {
         return this._maxRespawnDelay;
     }

     public void setAmount(int amount) {
         this._maximumCount = amount;
     }

     public void setId(int id) {
         this._id = id;
     }

     public void setGroupId(int i) {
         this._groupId = i;
     }

     public void setLocation(String location) {
         this._location = location;
     }

     public void setLocX(int locx) {
         this._locx = locx;
     }

     public void setLocY(int locy) {
         this._locy = locy;
     }

     public void setNpcid(int npcid) {
         this._npcid = npcid;
     }

     public void setHeading(int heading) {
         this._heading = heading;
     }

     public void setRandomx(int randomx) {
         this._randomx = randomx;
     }

     public void setRandomy(int randomy) {
         this._randomy = randomy;
     }

     public void setLocX1(int locx1) {
         this._locx1 = locx1;
     }

     public void setLocY1(int locy1) {
         this._locy1 = locy1;
     }

     public void setLocX2(int locx2) {
         this._locx2 = locx2;
     }

     public void setLocY2(int locy2) {
         this._locy2 = locy2;
     }

     public void setMinRespawnDelay(int i) {
         this._minRespawnDelay = i;
     }

     public void setMaxRespawnDelay(int i) {
         this._maxRespawnDelay = i;
     }

     private int calcRespawnDelay() {
         int respawnDelay = this._minRespawnDelay * 1000;
         if (this._delayInterval > 0) {
             respawnDelay += _random.nextInt(this._delayInterval) * 1000;
         }
         return respawnDelay;
     }

     public void executeSpawnTask(int spawnNumber, int objectId) {
         SpawnTask task = new SpawnTask(spawnNumber, objectId);
         GeneralThreadPool.getInstance().schedule(task, calcRespawnDelay());
     }
     public L1Spawn(L1Npc mobTemplate) throws SecurityException, ClassNotFoundException {
         this._initSpawn = false;
         this._template = mobTemplate;
         String implementationName = this._template.getImpl();
         this._constructor = Class.forName("l1j.server.server.model.Instance." + implementationName + "Instance").getConstructors()[0];
     } public void init() {
         this._delayInterval = this._maxRespawnDelay - this._minRespawnDelay;
         this._initSpawn = true;
         if (Config.ServerAdSetting.SPAWNHOMEPOINT && Config.ServerAdSetting.SPAWNHOMEPOINTCOUNT <= getAmount() && Config.ServerAdSetting.SPAWNHOMEPOINTDELAY >= getMinRespawnDelay() && isAreaSpawn()) {
             this._spawnHomePoint = true;
             this._homePoint = new HashMap<>();
         }

         int spawnNum = 0;
         while (spawnNum < this._maximumCount)
         {
             doSpawn(++spawnNum);
         }
         this._initSpawn = false;
     }

     protected void doSpawn(int spawnNumber) {
         doSpawn(spawnNumber, 0);
     }

     protected L1NpcInstance doSpawn(int spawnNumber, int objectId) {
         L1NpcInstance mob = null;
         try {
             Object[] parameters = { this._template };

             int newlocx = getLocX();
             int newlocy = getLocY();
             int tryCount = 0;









             mob = (L1NpcInstance)this._constructor.newInstance(parameters);
             if (objectId == 0) {
                 mob.setId(IdFactory.getInstance().nextId());
             } else {
                 mob.setId(objectId);
             }

             if (0 <= getHeading() && getHeading() <= 7) {
                 mob.setHeading(getHeading());
             } else {
                 mob.setHeading(5);
             }

             int npcId = mob.getNpcTemplate().get_npcId();
             if (npcId == 45488 && getMapId() == 9) {
                 mob.setMap((short)(getMapId() + _random.nextInt(2)));
             } else if (npcId == 45601 && getMapId() == 11) {
                 mob.setMap((short)(getMapId() + _random.nextInt(3)));
             } else {
                 mob.setMap(getMapId());
             }
             mob.setMovementDistance(getMovementDistance());
             mob.setRest(isRest());
             ArrayList<L1PcInstance> players = null;
             L1PcInstance pc = null;
             L1Location loc = null;
             Point pt = null;
             while (tryCount <= 50) {
                 switch (getSpawnType()) {
                     case 1:
                         if (!this._initSpawn) {
                             players = new ArrayList<>();
                             for (L1PcInstance _pc : L1World.getInstance().getAllPlayers()) {
                                 if (getMapId() == _pc.getMapId()) {
                                     players.add(_pc);
                                 }
                             }
                             if (players.size() > 0) {

                                 pc = players.get(_random.nextInt(players.size()));
                                 loc = pc.getLocation().randomLocation(30, false);
                                 newlocx = loc.getX();
                                 newlocy = loc.getY();
                                 break;
                             }
                         }

                         default:
                             if (isAreaSpawn()) {
                                 if (!this._initSpawn && this._spawnHomePoint) {
                                     pt = this._homePoint.get(Integer.valueOf(spawnNumber));
                                     loc = (new L1Location(pt, getMapId())).randomLocation(Config.ServerAdSetting.SPAWNHOMEPOINTRANGE, false);
                                     if (loc == null) {

                                         LoggerInstance.getInstance().addError("L1Spawn.java: loc = null  ' " + getNpcId() + " ' / ' " + getName() + " ' ");
                                         break;
                                     }
                                     newlocx = loc.getX();
                                     newlocy = loc.getY();
                                 } else {
                                     int rangeX = getLocX2() - getLocX1();
                                     int rangeY = getLocY2() - getLocY1();
                                     newlocx = _random.nextInt(rangeX) + getLocX1();
                                     newlocy = _random.nextInt(rangeY) + getLocY1();
                                 }
                                 if (tryCount > 49) {
                                     newlocx = getLocX();
                                     newlocy = getLocY();
                                 }  break;
                             }  if (isRandomSpawn()) {
                                 newlocx = getLocX() + (int)(Math.random() * getRandomx()) - (int)(Math.random() * getRandomx());
                                 newlocy = getLocY() + (int)(Math.random() * getRandomy()) - (int)(Math.random() * getRandomy()); break;
                             }
                             newlocx = getLocX();
                             newlocy = getLocY();
                             break;
                 }
                 mob.setX(newlocx);
                 mob.setHomeX(newlocx);
                 mob.setY(newlocy);
                 mob.setHomeY(newlocy);

                 if (mob.getMap().isInMap(mob.getLocation()) && mob.getMap().isPassable(mob.getLocation()) &&
                         mob instanceof L1MonsterInstance) {
                     if (isRespawnScreen()) {
                         break;
                     }
                     L1MonsterInstance mobtemp = (L1MonsterInstance)mob;
                     if (L1World.getInstance().getVisiblePlayer((L1Object)mobtemp).size() == 0) {
                         break;
                     }
                     SpawnTask task = new SpawnTask(spawnNumber, mob.getId());
                     GeneralThreadPool.getInstance().schedule(task, 3000L);
                     return mob;
                 }

                 tryCount++;
             }

             if (mob instanceof L1MonsterInstance) {
                 ((L1MonsterInstance)mob).initHide();
             }

             mob.setSpawn(this);
             mob.setRespawn(true);
             mob.setSpawnNumber(spawnNumber);
             if (this._initSpawn && this._spawnHomePoint) {
                 pt = new Point(mob.getX(), mob.getY());
                 this._homePoint.put(Integer.valueOf(spawnNumber), pt);
             }

             if (npcId == 45573 && mob.getMapId() == 2) {
                 for (L1PcInstance _pc : L1World.getInstance().getAllPlayers()) {
                     if (_pc.getMapId() == 2) {
                         _pc.start_teleport(32664, 32797, 2, _pc.getHeading(), 18339, true, false);
                     }
                 }
             }

             if (npcId == 5095) {
                 Broadcaster.broadcastPacket((L1Character)mob, (ServerBasePacket)new S_DoActionGFX(mob.getId(), 4));
                 L1NpcDeleteTimer timer = new L1NpcDeleteTimer(mob, 60000);
                 timer.begin();
             }

             String key = npcId + ":" + L1TownLocation.getTownIdByLoc(mob.getX(), mob.getY());
             if (TownNpcInfoTable.getInstance().isTownNpcInfo(key)) {
                 L1TownNpcInfo temp = TownNpcInfoTable.getInstance().getTownNpcInfo(key);
                 if (temp != null &&
                         L1TownLocation.getTownIdByLoc(mob.getX(), mob.getY()) == temp.getTownId()) {
                     mob.setNameId(temp.getNpcName());
                     mob.setCurrentSprite(temp.geSprId());
                 }
             }

             L1SpawnUtil.spawnAction(mob, npcId);
             L1World.getInstance().storeObject((L1Object)mob);
             L1World.getInstance().addVisibleObject((L1Object)mob);
             if (mob instanceof l1j.server.server.model.Instance.L1MerchantInstance) {
                 mob.getMap().setPassable(mob.getLocation(), false);
             }

             if (mob instanceof L1MonsterInstance) {
                 L1MonsterInstance mobtemp = (L1MonsterInstance)mob;
                 if (!this._initSpawn && mobtemp.getHiddenStatus() == 0) {
                     mobtemp.onNpcAI();
                 }
             }
             if (getGroupId() != 0) {
                 L1MobGroupSpawn.getInstance().doSpawn(mob, getGroupId(), isRespawnScreen(), this._initSpawn);
             }
             mob.getLight().turnOnOffLight();
             mob.startChat(4);
             mob.startChat(0);
             return mob;
         } catch (Exception e) {
             System.out.println(String.format("產生 ID：%d，NPC ID：%d", new Object[] { Integer.valueOf(this._id), Integer.valueOf(this._npcid) }));
             e.printStackTrace();

             return null;
         }
     }
     public void setRest(boolean flag) {
         this._rest = flag;
     }

     public boolean isRest() {
         return this._rest;
     }





     private int getSpawnType() {
         return this._spawnType;
     }

     public void setSpawnType(int type) {
         this._spawnType = type;
     }

     private boolean isAreaSpawn() {
         return (getLocX1() != 0 && getLocY1() != 0 && getLocX2() != 0 && getLocY2() != 0);
     }

     private boolean isRandomSpawn() {
         return (getRandomx() != 0 || getRandomy() != 0);
     }
 }


