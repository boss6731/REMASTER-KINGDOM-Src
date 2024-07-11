 package l1j.server.server.model;

 import java.util.logging.Logger;
 import l1j.server.server.IdFactory;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.datatables.UBTable;
 import l1j.server.server.model.Instance.L1MonsterInstance;




 public class L1UbSpawn
   implements Comparable<L1UbSpawn>
 {
   private int _id;
   private int _ubId;
   private int _pattern;
   private int _group;
   private int _npcTemplateId;
   private int _amount;
   private int _spawnDelay;
   private int _sealCount;
   private String _name;

   public int getId() {
     return this._id;
   }

   public void setId(int id) {
     this._id = id;
   }

   public int getUbId() {
     return this._ubId;
   }

   public void setUbId(int ubId) {
     this._ubId = ubId;
   }

   public int getPattern() {
     return this._pattern;
   }

   public void setPattern(int pattern) {
     this._pattern = pattern;
   }

   public int getGroup() {
     return this._group;
   }

   public void setGroup(int group) {
     this._group = group;
   }

   public int getNpcTemplateId() {
     return this._npcTemplateId;
   }

   public void setNpcTemplateId(int npcTemplateId) {
     this._npcTemplateId = npcTemplateId;
   }

   public int getAmount() {
     return this._amount;
   }

   public void setAmount(int amount) {
     this._amount = amount;
   }

   public int getSpawnDelay() {
     return this._spawnDelay;
   }

   public void setSpawnDelay(int spawnDelay) {
     this._spawnDelay = spawnDelay;
   }

   public int getSealCount() {
     return this._sealCount;
   }

   public void setSealCount(int i) {
     this._sealCount = i;
   }

   public String getName() {
     return this._name;
   }

   public void setName(String name) {
     this._name = name;
   }

   public void spawnOne() {
     L1UltimateBattle ub = UBTable.getInstance().getUb(this._ubId);
     L1Location loc = ub.getLocation().randomLocation((ub.getLocX2() - ub.getLocX1()) / 2, false);
     L1MonsterInstance mob = new L1MonsterInstance(NpcTable.getInstance().getTemplate(getNpcTemplateId()));

     mob.setId(IdFactory.getInstance().nextId());
     mob.setHeading(5);
     mob.setX(loc.getX());
     mob.setHomeX(loc.getX());
     mob.setY(loc.getY());
     mob.setHomeY(loc.getY());
     mob.setMap((short)loc.getMapId());
     mob.setUbSealCount(getSealCount());
     mob.setUbId(getUbId());
     L1World.getInstance().storeObject((L1Object)mob);
     L1World.getInstance().addVisibleObject((L1Object)mob);
     mob.onNpcAI();
     mob.getLight().turnOnOffLight();
   }

   public void spawnAll() {
     for (int i = 0; i < getAmount(); i++) {
       spawnOne();
     }
   }

   public int compareTo(L1UbSpawn rhs) {
     if (getId() < rhs.getId()) {
       return -1;
     }
     if (getId() > rhs.getId()) {
       return 1;
     }
     return 0;
   }

   private static final Logger _log = Logger.getLogger(L1UbSpawn.class.getName());
 }


