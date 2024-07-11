 package l1j.server.server.model.Instance;

 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.List;
 import java.util.Map;
 import java.util.Random;
 import java.util.concurrent.ScheduledFuture;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.Config;
 import l1j.server.MJ3SEx.EActionCodes;
 import l1j.server.MJBotSystem.MJBotType;
 import l1j.server.MJCompanion.Instance.MJCompanionInstance;
 import l1j.server.MJPassiveSkill.MJPassiveID;
 import l1j.server.MJTemplate.Interface.MJMonsterDeathHandler;
 import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.MJTemplate.Spawn.MJSpawnInfo;
 import l1j.server.MJWebServer.Dispatcher.my.service.mapview.MJMyMapViewService;
 import l1j.server.NpcAstar.AStar;
 import l1j.server.NpcAstar.Node;
 import l1j.server.NpcAstar.NpcAStar;
 import l1j.server.NpcAstar.World;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.ItemMessageTable;
 import l1j.server.server.datatables.MapsTable;
 import l1j.server.server.datatables.NewNpcChatTable;
 import l1j.server.server.datatables.NpcTable;
 import l1j.server.server.model.Broadcaster;
 import l1j.server.server.model.L1Attack;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1GroundInventory;
 import l1j.server.server.model.L1HateList;
 import l1j.server.server.model.L1Inventory;
 import l1j.server.server.model.L1Location;
 import l1j.server.server.model.L1Magic;
 import l1j.server.server.model.L1MobGroupInfo;
 import l1j.server.server.model.L1MobSkillUse;
 import l1j.server.server.model.L1NewNpcChatTimer;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1Spawn;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.map.L1Map;
 import l1j.server.server.model.map.L1WorldMap;
 import l1j.server.server.model.skill.L1SkillUse;
 import l1j.server.server.serverpackets.S_ChangeHeading;
 import l1j.server.server.serverpackets.S_DoActionGFX;
 import l1j.server.server.serverpackets.S_Door;
 import l1j.server.server.serverpackets.S_ExplosionNoti;
 import l1j.server.server.serverpackets.S_Invis;
 import l1j.server.server.serverpackets.S_MoveCharPacket;
 import l1j.server.server.serverpackets.S_PacketBox;
 import l1j.server.server.serverpackets.S_RemoveObject;
 import l1j.server.server.serverpackets.S_SkillBrave;
 import l1j.server.server.serverpackets.S_SkillHaste;
 import l1j.server.server.serverpackets.S_SkillSound;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.S_TrueTargetNew;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.EventTimeTemp;
 import l1j.server.server.templates.L1ItemMessage;
 import l1j.server.server.templates.L1NewNpcChat;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.types.Point;
 import l1j.server.server.utils.CommonUtil;






















 public class L1NpcInstance
   extends L1Character
 {
   private static final long serialVersionUID = 1L;
   public static final int MOVE_SPEED = 0;
   public static final int ATTACK_SPEED = 1;
   public static final int MAGIC_SPEED = 2;
   public static final int HIDDEN_STATUS_NONE = 0;
   public static final int HIDDEN_STATUS_SINK = 1;
   public static final int HIDDEN_STATUS_FLY = 2;
   public static final int CHAT_TIMING_APPEARANCE = 0;
   public static final int CHAT_TIMING_DEAD = 1;
   public static final int CHAT_TIMING_HIDE = 2;
   public static final int CHAT_TIMING_GAME_TIME = 3;
   public static final int CHAT_TIMING_SPAWN = 4;
   public static final int CHAT_TIMING_ESCAPE = 5;
   public long NpcDeleteTime = 0L;

   private L1Npc _npcTemplate;

   private L1Spawn _spawn;

   private MJSpawnInfo m_spawn_ex;

   private int _spawnNumber;
   private int _petcost;
   protected L1Inventory _inventory = new L1Inventory();
   private L1MobSkillUse mobSkill;
   private static Random _random = new Random(System.nanoTime());


   private boolean firstFound = true;

   protected static int courceRange = 30;
   private int _drainedMana = 0;

   private boolean _rest = false;

   private boolean _isResurrect;
   private int _randomMoveDistance = 0;
   private int _randomMoveDirection = 0;

   private boolean _aiRunning = false;
   private boolean _actived = false;
   private boolean _firstAttack = false;
   private long _sleep_time;
   protected L1HateList _hateList = new L1HateList();
   protected L1HateList _dropHateList = new L1HateList();
   protected List<L1ItemInstance> _targetItemList = new ArrayList<>();
   public L1Character _target = null;
   protected L1ItemInstance _targetItem = null;
   protected L1Character _master = null;
   private boolean _deathProcessing = false;
   private L1MobGroupInfo _mobGroupInfo = null;
   private int _mobGroupId = 0;
   private int CubeTime;
   private L1PcInstance CubePc;
   private int Cube = 20;

   private int num;
   private DeleteTimer _deleteTask;
   private ScheduledFuture<?> _future = null; private Map<Integer, Integer> _digestItems;
   public boolean _digestItemRunning = false;
   private boolean _발묶임상태 = false;
   public static final int MOVE_TYPE_NORMAL = 0;
   public static final int MOVE_TYPE_ASTAR = 1;
   public static final int MOVE_TYPE_ASTAR_FAIL = 2;

   public boolean 발묶임상태() {
     return this._발묶임상태;
   }

   public void set발묶임상태(boolean flag) {
     this._발묶임상태 = flag;
   }





   protected int _moveType = 0;

   public NpcAStar _aStar;

   public L1Character ActiveTarget = null;
   public final HashSet<Integer> backingTargets = new HashSet<>();

   public boolean STATUS_Escape = false;

   public boolean Escape = false;
   private static Logger _log = Logger.getLogger(L1NpcInstance.class.getName());

   private int mEmptyWalkableCount;

   private String Spawn_Location;

   private L1NpcInstance _doppels;
   private L1Character _attacker;
   private L1Character _defender;
   public int AStar_Try_Count;
   private boolean isTeleportAction;
   public L1Character _backtarget;
   protected int cnt;
   private int _passable;
   private static final int NOT_PASS = 1;
   private static final int OK_PASS = 0;
   public boolean _breakable;
   private boolean _floorOpenStatus;
   private int _paralysisTime;
   private boolean _hprRunning;
   private HprTimer _hprTimer;
   private boolean _mprRunning;
   private MprTimer _mprTimer;
   private boolean _pickupItem;
   public int PASS;

   public String getSpawnLocation() {
     return this.Spawn_Location;
   }

   public void setSpawnLocation(String st) {
     this.Spawn_Location = st;
   }

   private double calcRandomVal(int seed, int ranval, double rate) {
     return rate * (ranval - seed);
   }

   protected void setting_template(L1Npc template) {
     this._npcTemplate = template;
     double rate = 0.0D;
     double diff = 0.0D;

     setName(template.get_name());
     setNameId(template.get_nameid());

     int randomlevel = 0;
     int level = template.get_level();
     if (template.get_randomlevel() != 0) {
       randomlevel = _random.nextInt(template.get_randomlevel() - level + 1);
       diff = (template.get_randomlevel() - level);
       rate = randomlevel / diff;
       randomlevel += template.get_level();
       level = randomlevel;
     }
     setLevel(level);

     int ac = template.get_ac();
     if (template.get_randomac() != 0) {
       ac = (int)(ac + calcRandomVal(ac, template.get_randomac(), rate));
     }
     this.ac.setAc(ac);

     if (template.get_randomlevel() == 0) {
       this.ability.setStr(template.get_str());
       this.ability.setCon(template.get_con());
       this.ability.setDex(template.get_dex());
       this.ability.setInt(template.get_int());
       this.ability.setWis(template.get_wis());
       this.resistance.setBaseMr(template.get_mr());
     } else {
       this.ability.setStr((byte)(int)Math.min(template.get_str() + diff, 127.0D));
       this.ability.setCon((byte)(int)Math.min(template.get_con() + diff, 127.0D));
       this.ability.setDex((byte)(int)Math.min(template.get_dex() + diff, 127.0D));
       this.ability.setInt((byte)(int)Math.min(template.get_int() + diff, 127.0D));
       this.ability.setWis((byte)(int)Math.min(template.get_wis() + diff, 127.0D));
       this.resistance.setBaseMr((byte)(int)Math.min(template.get_mr() + diff, 127.0D));

       addHitup((int)diff * 2);
       addDmgup((int)diff * 2);
     }

     int hp = template.get_hp();
     if (template.get_randomhp() != 0) {
       hp = (int)(hp + calcRandomVal(hp, template.get_randomhp(), rate));
     }
     setMaxHp(hp);
     setCurrentHp(getMaxHp());

     int mp = template.get_mp();
     if (template.get_randommp() != 0) {
       mp = (int)(mp + calcRandomVal(mp, template.get_randommp(), rate));
     }
     setMaxMp(mp);
     setCurrentMp(mp);

     setAgro(template.is_agro());
     setAgrocoi(template.is_agrocoi());
     setAgrososc(template.is_agrososc());
     setCurrentSprite(template.get_gfxid());

     if (template.get_randomexp() == 0) {

       if (template.get_exp() < 127L) {
         set_exp(template.get_level());
       } else {
         set_exp(template.get_exp());

       }

     }
     else if (template.get_exp() < 127L) {
       set_exp((template.get_level() + template.get_randomexp() + randomlevel));
     } else {
       set_exp((template.get_randomexp() + randomlevel));
     }









     int lawful = template.get_lawful();
     if (template.get_randomlawful() != 0) {
       lawful = (int)(lawful + calcRandomVal(lawful, template.get_randomlawful(), rate));
     }
     setLawful(lawful);
     setTempLawful(lawful);

     setPickupItem(template.is_picupitem());
     if (template.get_digestitem() > 0) {
       this._digestItems = new HashMap<>();
     }
     setKarma(template.getKarma());
     setLightSize(template.getLightSize());

     this.mobSkill = new L1MobSkillUse(this);
   }

   class NpcAI implements Runnable {
     public void start() {
       L1NpcInstance.this.setAiRunning(true);
       GeneralThreadPool.getInstance().schedule(this, 0L);
     }

     private void stop() {
       if (L1NpcInstance.this.mobSkill != null) {
         try {
           L1NpcInstance.this.mobSkill.resetAllSkillUseCount();
         } catch (Exception exception) {}
       }

       GeneralThreadPool.getInstance().schedule(new L1NpcInstance.DeathSyncTimer(), 0L);
     }

     private void schedule(long delay) {
       GeneralThreadPool.getInstance().schedule(this, delay);
     }


     public void run() {
       try {
         synchronized (L1NpcInstance.this.synchObject) {
           if (notContinued()) {
             stop();
             return;
           }
           if (0 < L1NpcInstance.this._paralysisTime) {
             schedule(L1NpcInstance.this._paralysisTime);
             L1NpcInstance.this._paralysisTime = 0;
             L1NpcInstance.this.setParalyzed(false); return;
           }
           if (L1NpcInstance.this.isParalyzed() || L1NpcInstance.this.isSleeped() || L1NpcInstance.this.hasSkillEffect(10071)) {
             schedule(200L); return;
           }
           if (L1NpcInstance.this.getNpcId() == 707026) {
             int p = (int)(L1NpcInstance.this.getCurrentHp() / L1NpcInstance.this.getMaxHp() * 100.0D);
             if (p > 90) {
               schedule(200L);
               return;
             }
           }
           L1NpcInstance.this.updateObject();
           if (!L1NpcInstance.this.AIProcess()) {
             schedule(L1NpcInstance.this.getSleepTime());
             return;
           }
         }
         stop();
       } catch (Exception e) {
         System.out.println("NPC ID : " + L1NpcInstance.this.getNpcTemplate().get_npcId());
         L1NpcInstance._log.log(Level.WARNING, "NpcAI에 예외가 발생했습니다.", e);
       }
     }

     private boolean notContinued() {
       return (L1NpcInstance.this._destroyed || L1NpcInstance.this.isDead() || L1NpcInstance.this.getCurrentHp() <= 0 || L1NpcInstance.this.getHiddenStatus() != 0);
     }
   }

   protected void startAI() {
     (new NpcAI()).start();
   }

   class DeathSyncTimer implements Runnable {
     private void schedule(long delay) {
       GeneralThreadPool.getInstance().schedule(this, delay);
     }


     public void run() {
       if (L1NpcInstance.this.isDeathProcessing()) {
         schedule(L1NpcInstance.this.getSleepTime());
         return;
       }
       L1NpcInstance.this.allTargetClear();
       L1NpcInstance.this.setAiRunning(false);
     }
   }

   public void 몬스터Teleport() {
     int lvl = getLevel();
     if (getMovementDistance() <= 0 &&
       Config.ServerAdSetting.NpcMaxYN &&
       lvl >= Config.ServerAdSetting.NpcMax &&
       this instanceof L1MonsterInstance &&
       getLocation().getTileLineDistance(new Point(getHomeX(), getHomeY())) > Config.ServerAdSetting.NpcLocation) {
       npcInitialize();
     }
   }









   public void DoppelSkillCheck(L1NpcInstance doppel, L1Character attacker, L1Character defender) {
     this._doppels = doppel;
     this._attacker = attacker;
     this._defender = defender;
   }


   private static void delenpc(int npcid) {
     L1NpcInstance npc = null;
     for (L1Object object : L1World.getInstance().getObject()) {
       if (object instanceof L1NpcInstance) {
         npc = (L1NpcInstance)object;
         if (npc.getNpcTemplate().get_npcId() == npcid) {
           if (npc != null)
             npc.deleteMe();
           npc = null;
         }
       }
     }
   }

   private boolean AIProcess() {
     if (checkCondition()) {
       return false;
     }

     if (this._doppels != null && (
       this._defender.getCurrentHp() == 0 || this._defender.isDead() ||
       !this._doppels.getMap().isInMap((Point)this._defender.getLocation()) ||
       !this._doppels.getMap().isInMap((Point)this._attacker.getLocation()))) {
       delenpc(this._doppels.getNpcId());
     }








     if (this instanceof L1MerchantInstance &&
       getNpcId() == 5095) {
       for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
         L1Location newLocation = pc.getLocation().randomLocation(30, true);
         int newX = newLocation.getX();
         int newY = newLocation.getY();
         short mapId = (short)newLocation.getMapId();
         if (pc.getLocation().getTileLineDistance(new Point((Point)getLocation())) < 2 && !pc.isDead() &&
           pc != null) {
           pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, false);
         }
       }
     }



     if (this instanceof L1MerchantInstance &&
       getNpcId() == 7210040) {
       for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
         if (pc.getLocation().getTileLineDistance(new Point((Point)getLocation())) < 2 && !pc.isDead() &&
           pc != null) {
           Random random1 = new Random();
           int chance = random1.nextInt(100) + 1;
           if (chance < 40) {


             pc.start_teleport(33445, 33130, 4, pc.getHeading(), 18339, true, false); continue;
           }  if (chance < 60) {


             pc.start_teleport(33428, 33244, 4, pc.getHeading(), 18339, true, false); continue;
           }  if (chance < 70) {


             pc.start_teleport(33474, 33165, 4, pc.getHeading(), 18339, true, false); continue;
           }  if (chance < 80)
           {

             pc.start_teleport(33495, 33197, 4, pc.getHeading(), 18339, true, false);
           }
         }
       }
     }



     setSleepTime(300L);

     checkTarget();
     몬스터Teleport();
     if (this._target == null && this._master == null) {
       searchTarget();
     }

     onItemUse();

     if (this._target == null) {
       if (발묶임상태()) {
         return true;
       }

       this.backingTargets.clear();
       checkTargetItem();
       if (isPickupItem() && this._targetItem == null) {
         searchTargetItem();
       }

       if (this._targetItem == null) {
         if (noTarget()) {
           return true;
         }
       } else {
         L1GroundInventory l1GroundInventory = L1World.getInstance().getInventory(this._targetItem.getX(), this._targetItem.getY(), this._targetItem.getMapId());
         if (l1GroundInventory.checkItem(this._targetItem.getItemId())) {
           onTargetItem();
         } else {
           this._targetItemList.remove(this._targetItem);
           this._targetItem = null;
           setSleepTime(1000L);
           return false;
         }

       }
     } else if (getHiddenStatus() == 0) {
       onTarget();
     } else {
       return true;
     }


     return false;
   }


   public void onItemUse() {}


   public void searchTarget() {}

   public boolean checkCondition() {
     return false;
   }

   public void checkTarget() {
     if (this._target == null || this._target.getMapId() != getMapId() || this._target.isDead() || this._target.getCurrentHp() <= 0 || (this._target
       .isInvisble() && !getNpcTemplate().is_agrocoi() && !this._hateList.containsKey(this._target)) || (this._target instanceof L1SummonInstance && ((L1SummonInstance)this._target).isDestroyed()) || (this._target instanceof L1PetInstance && ((L1PetInstance)this._target)
       .isDestroyed()) || (this._target instanceof MJCompanionInstance && ((MJCompanionInstance)this._target).isDestroyed())) {

       if (this._target != null) {
         tagertClear();
       }

       if (!this._hateList.isEmpty()) {
         this._target = this._hateList.getMaxHateCharacter();



         checkTarget();
       }
     }
   }

   public void delInvis() {
     if (hasSkillEffect(60)) {
       killSkillEffectTimer(60);
       sendPackets((ServerBasePacket)new S_Invis(getId(), 0));
       broadcastPacket((ServerBasePacket)new S_Invis(getId(), 0));
       broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
     }
     if (hasSkillEffect(97)) {
       killSkillEffectTimer(97);
       sendPackets((ServerBasePacket)new S_Invis(getId(), 0));
       broadcastPacket((ServerBasePacket)new S_Invis(getId(), 0));
       broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
       if (isPassive(MJPassiveID.BLIND_HIDDING_ASSASSIN.toInt())) {
         sendPackets((ServerBasePacket)new S_Invis(getId(), 0));
         broadcastPacket((ServerBasePacket)new S_Invis(getId(), 0));
         broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
         addMoveDelayRate(-50.0D);
       }
     }
   }

   public void checkTargetItem() {
     if (this._targetItem == null || this._targetItem.getMapId() != getMapId() ||
       getLocation().getTileDistance((Point)this._targetItem.getLocation()) > 15) {
       if (!this._targetItemList.isEmpty()) {
         this._targetItem = this._targetItemList.get(0);
         this._targetItemList.remove(0);
         checkTargetItem();
       } else {
         this._targetItem = null;
       }
     }
   }

   protected boolean aStarFollow() {
     Point current = new Point(getX(), getY());
     while (current.isSamePoint(this._aStar.getFinalPath().get(0))) {
       this._aStar.eraseOneNodeFromFinalPath();
     }
     int dir = current.getHeading(this._aStar.getFinalPath().get(0));
     if (dir == -1)
       return false;
     if (getMap().isPassable(current, dir) &&
       !isObjectExist(current.getX(), current.getY(), getMapId(), dir)) {
       this._aStar.eraseOneNodeFromFinalPath();
       setDirectionMoveSpeed(dir);
       setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
       return true;
     }

     return false;
   }

   protected boolean aStarMove() {
     Point current = new Point(getX(), getY());
     while (current.isSamePoint(this._aStar.getFinalPath().get(0))) {
       this._aStar.eraseOneNodeFromFinalPath();
     }
     int dir = current.getHeading(this._aStar.getFinalPath().get(0));
     if (dir == -1)
     { tagertClear(); }
     else { if (getMap().isPassable(current, dir) &&
         !isObjectExist(current.getX(), current.getY(), getMapId(), dir)) {
         this._aStar.eraseOneNodeFromFinalPath();
         setDirectionMoveSpeed(dir);
         setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
         return true;
       }
       return false; }

     return true;
   }

   protected boolean findNextTarget() {
     for (L1Object object : L1World.getInstance().getVisibleObjects((L1Object)this, 10)) {
       if (object instanceof L1PcInstance) {
         L1Character cha = (L1Character)object;
         if (cha != this._target) {
           if (object instanceof L1PcInstance) {
             L1PcInstance pc = (L1PcInstance)object;
             if (pc.isGhost()) {
               continue;
             }
             if (pc.isGm()) {
               continue;
             }
           }
           this._hateList.add(cha, 0);
           this._target = cha;



           return true;
         }
       }
     }

     return false;
   }

   private void doNormalFollow() {
     Point current = new Point(getX(), getY());

     int dir = moveDirection2(this._master.getX(), this._master.getY(),
         getLocation().getLineDistance(new Point(this._master.getX(), this._master.getY())));

     if (dir != -1 && getMap().isPassable(current, dir) &&
       !isObjectExist(current.getX(), current.getY(), getMapId(), dir)) {
       setDirectionMoveSpeed(dir);
       setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
     } else {
       Point targetPoint = new Point(this._master.getX(), this._master.getY());
       this._aStar.setTarget(getMap(), targetPoint);
       if (this._aStar.compute(current) == null) {
         this._moveType = 2;
         doAStarFailFollow();
         return;
       }
       this._moveType = 1;
       doAStarFollow();
       return;
     }
   }



   private void doAStarFollow() {
     Point current = new Point(getX(), getY());
     Point targetPoint = new Point(this._master.getX(), this._master.getY());

     if (this._aStar.getTarget().getX() != targetPoint.getX() || this._aStar.getTarget().getY() != targetPoint.getY()) {
       this._aStar.setTarget(getMap(), targetPoint);

       if (this._aStar.compute(current) == null) {
         this._moveType = 2;
         doAStarFailFollow();
         return;
       }
     } else if (this._aStar.getTryCount() >= 10) {
       this._moveType = 2;
       doAStarFailFollow();

       return;
     }
     if (!aStarFollow())
     { if (this._aStar.compute(current) == null) {
         this._moveType = 2;
         doAStarFailFollow();
         return;
       }
       if (!aStarMove()) {
         setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
       } }
     else
     { setSleepTime(getCurrentSpriteInterval(EActionCodes.walk)); }
   }
   public boolean onAdditionalNpcAi() { return false; }
   public void teleportDmgAction() { L1Location newLoc = getLocation().randomLocation(3, 6, false); teleport(newLoc.getX(), newLoc.getY(), getMoveState().getHeading()); setCurrentMp(getCurrentMp() - 10); }
   public int moveDirectionIndun(int mapid, int x, int y) { return moveDirectionIndun(mapid, x, y, getLocation().getLineDistance(new Point(x, y))); }
   public int moveDirectionIndun(int mapid, int x, int y, double d) { int dir = 0;
       int calcx = getX() - x;
       int calcy = getY() - y;
       if (getMapId() != mapid || Math.abs(calcx) > 30 || Math.abs(calcy) > 30) { allTargetClear(); return -1;
       }  if ((hasSkillEffect(40) || hasSkillEffect(20)) && d >= 2.0D) return -1;
       if (d > 30.0D) return -1;
       if (d > courceRange) { dir = targetDirection(x, y);
       } else { dir = _serchCource(x, y, mapid); if (dir == -1) dir = targetDirection(x, y);
       }  return dir;
   } public void onTarget() { try { int targetx = this._target.getX();
       int targety = this._target.getY();
       setActived(true); this._targetItemList.clear();
       this._targetItem = null;
       L1Character target = this._target;
       if (target == null) return;
       if (!isActionable() && !isMovable()) return;
       if ((hasSkillEffect(40) || hasSkillEffect(20)) && getLocation().getTileLineDistance((Point)target.getLocation()) > 1) { tagertClear();
           return;
       }  if (getNpcId() == 8513) return;
       if (!isActionable() && isMovable()) { if (getLocation().getTileLineDistance((Point)target.getLocation()) > 15) { tagertClear();
       } else { int dir = targetReverseDirection(targetx, targety);
           dir = checkObject(getX(), getY(), getMapId(), dir);
           if (dir == -1) return;
           setSleepTime(setDirectionMoveSpeed(dir));
       }
       } else { if (onAdditionalNpcAi()) return;  if (this.STATUS_Escape) { L1Location newLoc = getLocation().randomLocation(10, 20, false);
           int dir = moveDirection(getMapId(), newLoc.getX(), newLoc.getY());
           dir = checkObject(getX(), getY(), getMapId(), dir);
           if (dir == -1) { this.STATUS_Escape = false; } else { setSleepTime(setDirectionMoveSpeed(dir));
           }  newLoc = null; return;
       }  boolean isSkillUse = false;
           isSkillUse = this.mobSkill.skillUse(target);
           if (isSkillUse == true) { setSleepTime(this.mobSkill.getSleepTime() + 500L); return;
           }
           if (getNpcId() == 45164 && getMapId() == 9 && target.getMapId() == 9 && ((targetx == 32663 && targety == 33149) || (targetx == 32669 && targety == 33158) || (targetx == 32669 && targety == 33161)) && getLocation().getTileDistance((Point)target.getLocation()) <= 2) { getMoveState().setHeading(targetDirection(targetx, targety));
               attackTarget(target);
           } else if (isAttackPosition(target, getNpcTemplate().get_ranged())) { getMoveState().setHeading(targetDirection(targetx, targety));
               attackTarget(target);
           } else if (isMovable()) { int distance = getLocation().getTileDistance((Point)target.getLocation());
               if (target.getMapId() != getMapId() || distance > 30) { tagertClear();
                   return;
               }  if (this.firstFound && getNpcTemplate().is_teleport() && distance > 3 && distance < 15 && nearTeleport(targetx, targety) == true) { this.firstFound = false;
                   return;
               }  if (getNpcTemplate().is_teleport() && 20 > CommonUtil.random(100) && getCurrentMp() >= 10 && distance > 6 && distance < 15) { if (nearTeleport(targetx, targety) == true) return;  } else if (this.isTeleportAction && getNpcTemplate().is_teleport() && getCurrentMp() >= 10) { this.isTeleportAction = false; teleportDmgAction(); return; }  if ((getNpcId() == 7800007 || getNpcId() == 7800064) && getX() > 32790 && getX() < 32808 && getY() > 32855 && getY() < 32872) return;  int dir = moveDirection(target.getMapId(), targetx, targety); dir = checkObject(getX(), getY(), getMapId(), dir); if ((getNpcId() == 7800007 || getNpcId() == 7800064 || (getNpcId() >= 7800010 && getNpcId() <= 7800014) || (getNpcId() >= 7800020 && getNpcId() <= 7800021) || (getNpcId() >= 7800030 && getNpcId() <= 7800031) || (getNpcId() >= 7800040 && getNpcId() <= 7800041) || (getNpcId() >= 7800050 && getNpcId() <= 7800051) || (getNpcId() >= 7800054 && getNpcId() <= 7800055) || (getNpcId() >= 7800060 && getNpcId() <= 7800063)) && (getX() <= 32790 || getX() >= 32808 || getY() <= 32855 || getY() >= 32872)) { dir = moveDirectionIndun(target.getMapId(), targetx, targety); setSleepTime(setDirectionMoveSpeed(dir)); return; }  if (dir == -1 && ((getNpcId() >= 7200008 && getNpcId() <= 7200020) || getNpcId() == 7200055 || getNpcId() == 7200056 || (getNpcId() >= 7200030 && getNpcId() <= 7200041))) { L1Map m = L1WorldMap.getInstance().getMap(getMapId()); if (m.getOriginalTile(getX(), getY()) == 12) { getMoveState().setHeading(targetDirection(targetx, targety)); dir = getMoveState().getHeading(); }  }  if (dir == -1) { this.cnt++; if (this.cnt > 5) { this._backtarget = target; tagertClear(); this.cnt = 0; }  } else { if (발묶임상태()) return;  boolean door = World.문이동(getX(), getY(), getMapId(), calcheading((L1Object)this, targetx, targety)); boolean tail = World.isThroughObject(getX(), getY(), getMapId(), dir); if ((getNpcId() >= 7200008 && getNpcId() <= 7200020) || getNpcId() == 7200055 || getNpcId() == 7200056 || (getNpcId() >= 7200030 && getNpcId() <= 7200041)) { door = false; tail = true; }  if (door || !tail) { this.cnt++; if (this.cnt > 5) { this._backtarget = target; tagertClear(); this.cnt = 0; }  return; }  setSleepTime(setDirectionMoveSpeed(dir)); }  } else { tagertClear(); }  }  } catch (Exception exception) {} } public void die(L1Character lastAttacker) { if (this != null) { setDeathProcessing(true); setCurrentHp(0); setDead(true); setStatus(8); getMap().setPassable((Point)getLocation(), true); Broadcaster.broadcastPacket(this, (ServerBasePacket)new S_DoActionGFX(getId(), 8), true); startChat(1); setDeathProcessing(false); set_exp(0L); setKarma(0); setLawful(0); allTargetClear(); startDeleteTimer(); }  } public void setHate(L1Character cha, int hate) { if (cha != null && cha.getId() != getId() && (cha.getAI() == null || (cha.getAI().getBotType() != MJBotType.REDKNIGHT && cha.getAI().getBotType() != MJBotType.PROTECTOR))) { if (!isFirstAttack() && hate != 0) { hate += getMaxHp() / 10; setFirstAttack(true); }  this._hateList.add(cha, hate); this._dropHateList.add(cha, hate); this._target = this._hateList.getMaxHateCharacter(); this._moveType = 1; checkTarget(); }  } public void setLink(L1Character cha) {} public void serchLink(L1PcInstance targetPlayer, int family) { Collection<L1Object> targetKnownObjects = targetPlayer.getKnownObjects(); L1NpcInstance npc = null; L1MobGroupInfo mobGroupInfo = null; for (L1Object knownObject : targetKnownObjects) { if (knownObject == null) continue;  if (knownObject instanceof L1NpcInstance) { npc = (L1NpcInstance)knownObject; if (npc.getNpcTemplate() != null && npc.getNpcTemplate().get_agrofamily() > 0) if (npc.getNpcTemplate().get_agrofamily() == 1) { if (npc.getNpcTemplate().get_family() == family) npc.setLink(targetPlayer);  } else { npc.setLink(targetPlayer); }   mobGroupInfo = getMobGroupInfo(); if (mobGroupInfo != null && getMobGroupId() != 0 && getMobGroupId() == npc.getMobGroupId()) npc.setLink(targetPlayer);  }  }  } public void attackTarget(L1Character target) { if (target == null) return;  if (target instanceof L1PcInstance) { L1PcInstance player = (L1PcInstance)target; if (player.get_teleport()) return;  } else if (target instanceof L1PetInstance) { L1PetInstance pet = (L1PetInstance)target; L1Character cha = pet.getMaster(); if (cha instanceof L1PcInstance) { L1PcInstance player = (L1PcInstance)cha; if (player.get_teleport()) return;  }  } else if (target instanceof MJCompanionInstance) { MJCompanionInstance companion = (MJCompanionInstance)target; if (companion.get_master() == null || companion.get_master().get_teleport()) return;  } else if (target instanceof L1SummonInstance) { L1SummonInstance summon = (L1SummonInstance)target; L1Character cha = summon.getMaster(); if (cha instanceof L1PcInstance) { L1PcInstance player = (L1PcInstance)cha; if (player.get_teleport()) return;  }  }  if (this instanceof L1PetInstance) { L1PetInstance pet = (L1PetInstance)this; L1Character cha = pet.getMaster(); if (cha instanceof L1PcInstance) { L1PcInstance player = (L1PcInstance)cha; if (player.get_teleport()) return;  }  } else if (this instanceof MJCompanionInstance) { MJCompanionInstance companion = (MJCompanionInstance)this; if (companion.get_master() == null || companion.get_master().get_teleport()) return;  } else if (this instanceof L1SummonInstance) { L1SummonInstance summon = (L1SummonInstance)this; L1Character cha = summon.getMaster(); if (cha instanceof L1PcInstance) { L1PcInstance player = (L1PcInstance)cha; if (player.get_teleport()) return;  }  }  if (target instanceof L1NpcInstance) { L1NpcInstance npc = (L1NpcInstance)target; if (npc.getHiddenStatus() != 0) { allTargetClear(); return; }  }  boolean isCounterBarrier = false; boolean isMortalBody = false; boolean isConqure = false; L1Attack attack = new L1Attack(this, target); if (attack.calcHit()) { if (target.hasSkillEffect(91)) { L1Magic magic = new L1Magic(target, this); boolean isProbability = magic.calcProbabilityMagic(91); boolean isShortDistance = attack.isShortDistance(); if (isProbability && isShortDistance) if (target.isPassive(MJPassiveID.COUNTER_BARRIER_MASTER.toInt())) { int hp_bonus = target.getCurrentHp() + target.getAbility().getTotalCon() / 2; isCounterBarrier = true; target.setCurrentHp(hp_bonus); } else if (target != null && target.isPassive(MJPassiveID.PARADOX.toInt()) && MJRnd.isWinning(1000000, 500000)) { target.send_effect(18518); isCounterBarrier = false; } else { isCounterBarrier = true; }   } else if (target.hasSkillEffect(394)) { L1Magic magic = new L1Magic(target, this); boolean isProbability = magic.calcProbabilityMagic(394); boolean isShortDistance1 = attack.isShortDistance1(); if (isProbability && isShortDistance1) isMortalBody = true;  } else if (target.isPassive(MJPassiveID.CONQUEROR.toInt())) { L1Magic magic = new L1Magic(target, this); boolean isProbability = magic.calcProbabilityMagic(995048); boolean isShortDistance = attack.isShortDistance(); if (isShortDistance && isProbability) isConqure = true;  }  if (!isCounterBarrier && !isMortalBody && !isConqure) { attack.calcDamage(); if (target instanceof L1PcInstance) applySpecialEnchant((L1PcInstance)target);  }  }  if (isCounterBarrier) { attack.actionCounterBarrier(); attack.commitCounterBarrier(); } else if (isMortalBody) { attack.calcDamage(); attack.actionMortalBody(); attack.commitMortalBody(); attack.commit(); } else if (isConqure) { attack.commitConqure(); attack.actionConqure(); } else { attack.action(); attack.commit(); }  setSleepTime(getCurrentSpriteInterval(EActionCodes.attack)); } private void applySpecialEnchant(L1PcInstance pc) { if (pc.getWeapon() == null || !pc.getWeapon().isSpecialEnchantable()) return;  for (int i = 1; i <= 3; i++) { int specialEnchant = pc.getWeapon().getSpecialEnchant(i); if (specialEnchant == 0) break;  if (_random.nextInt(100) < 1) { boolean success = true; switch (specialEnchant) { case 1: success = false; break;case 2: (new L1SkillUse()).handleCommands(pc, 31, pc.getId(), pc.getX(), pc.getY(), null, 0, 4); break;case 3: case 4: case 5: success = false; break;case 6: if (hasSkillEffect(1000) || hasSkillEffect(1001) || hasSkillEffect(52) || hasSkillEffect(101)) { killSkillEffectTimer(1000); killSkillEffectTimer(1001); killSkillEffectTimer(52); killSkillEffectTimer(101); broadcastPacket((ServerBasePacket)new S_SkillBrave(getId(), 0, 0)); setBraveSpeed(0); broadcastPacket((ServerBasePacket)new S_SkillHaste(getId(), 0, 0)); setMoveSpeed(0); }  break;case 7: case 8: success = false; break;case 10: success = false; break; }  if (success) break;  }  }  } public int getPassable() { return this._passable; } public void setPassable(int i) { if (i == this.PASS || i == 1) this._passable = i;  } public void setBrakeable(boolean b) { this._breakable = b; } public boolean isBrakeable() { return this._breakable; } public void setFloorOpenStatus(boolean b) { this._floorOpenStatus = b; } public boolean getFloorOpenStatus() { return this._floorOpenStatus; } public void searchTargetItem() { ArrayList<L1GroundInventory> gInventorys = new ArrayList<>(); for (L1Object obj : L1World.getInstance().getVisibleObjects((L1Object)this)) { if (obj == null) continue;  if (obj != null && obj instanceof L1GroundInventory) gInventorys.add((L1GroundInventory)obj);  }  if (gInventorys.size() == 0) return;  int pickupIndex = (int)(Math.random() * gInventorys.size()); L1GroundInventory inventory = gInventorys.get(pickupIndex); for (L1ItemInstance item : inventory.getItems()) { if (item == null) continue;  if (getInventory().checkAddItem(item, item.getCount()) == 0) { this._targetItem = item; this._targetItemList.add(this._targetItem); }  }  } public void searchItemFromAir() { if (getNpcId() == 14212144 || getNpcId() == 14212134) return;  ArrayList<L1GroundInventory> gInventorys = new ArrayList<>(); for (L1Object obj : L1World.getInstance().getVisibleObjects((L1Object)this, 4)) { if (obj == null) continue;  if (obj != null && obj instanceof L1GroundInventory && ((L1GroundInventory)obj).getSize() > 0) gInventorys.add((L1GroundInventory)obj);  }  if (gInventorys.size() == 0) return;  int pickupIndex = (int)(Math.random() * gInventorys.size()); L1GroundInventory inventory = gInventorys.get(pickupIndex); for (L1ItemInstance item : inventory.getItems()) { if (item == null) continue;  if ((item.getItem().getType() == 6 || item.getItem().getType() == 7) && getHiddenStatus() == 2) { setHiddenStatus(0); broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 45)); setStatus(0); broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this)); onNpcAI(); startChat(2); this._targetItem = item; this._targetItemList.add(this._targetItem); }  }  } public static void shuffle(L1Object[] arr) { for (int i = arr.length - 1; i > 0; i--) { int t = (int)(Math.random() * i); L1Object tmp = arr[i]; arr[i] = arr[t]; arr[t] = tmp; }  } public void onTargetItem() { if (getLocation().getTileLineDistance((Point)this._targetItem.getLocation()) == 0) { pickupTargetItem(this._targetItem); } else { int dir = moveDirection(this._targetItem.getMapId(), this._targetItem.getX(), this._targetItem.getY()); if (dir == -1) { this._targetItemList.remove(this._targetItem); this._targetItem = null; } else { setSleepTime(setDirectionMoveSpeed(dir)); }  }  } public void pickupTargetItem(L1ItemInstance targetItem) { if (targetItem == null) return;  targetItem.setGiveItem(true); L1GroundInventory l1GroundInventory = L1World.getInstance().getInventory(targetItem.getX(), targetItem.getY(), targetItem.getMapId()); L1ItemInstance item = l1GroundInventory.tradeItem(targetItem, targetItem.getCount(), getInventory()); if (item == null) return;  if (this instanceof MJCompanionInstance) { MJCompanionInstance pet = (MJCompanionInstance)this; L1PcInstance pc = pet.get_master(); if (pc != null && ItemMessageTable.getInstance().isPickUpMessage(item.getItemId())) { L1ItemMessage temp = ItemMessageTable.getInstance().getPickUpMessage(item.getItemId()); String men = ""; if (temp != null && temp.getType() == 1 && temp.isMentuse()) { if (temp.getOption() == 1) { men = "" + pc.getName() + " 님께서"; } else { men = "누군가가"; }  if (temp.getMent() != null) { L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_SystemMessage(temp.getMent())); L1World.getInstance().broadcastPacketToAll((ServerBasePacket)new S_PacketBox(84, temp.getMent())); } else { String locationName = MapsTable.getInstance().getMapName(pc.getMapId()); String itemName = item.getViewName(); if (itemName == null) itemName = item.getName();  String message = String.format("" + men + " %s \\fH을(를) " + locationName + "에서 획득하였습니다.", new Object[] { itemName }); String message2 = String.format("" + men + " %s \\fH을(를) " + locationName + "에서 획득하였습니다.", new Object[] { itemName }); L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[] { (ServerBasePacket)new S_SystemMessage(message), (ServerBasePacket)new S_PacketBox(84, message2) }); }  }  }  }  this.light.turnOnOffLight(); onGetItem(item); this._targetItemList.remove(this._targetItem); this._targetItem = null; setSleepTime(1000L); } public int getDistance(int x, int y, int tx, int ty) { long dx = (tx - x); long dy = (ty - y); return (int)Math.sqrt((dx * dx + dy * dy)); } public boolean isDistance(int x, int y, int m, int tx, int ty, int tm, int loc) { int distance = getDistance(x, y, tx, ty); if (loc < distance) return false;  if (m != tm) return false;  return true; } public boolean noTarget() { if (this._master != null && this._master.getMapId() == getMapId() && getLocation().getTileLineDistance((Point)this._master.getLocation()) > 2) { int dir = moveDirection(this._master.getMapId(), this._master.getX(), this._master.getY()); if (dir != -1) { boolean tail = World.isThroughObject(getX(), getY(), getMapId(), dir); boolean obj = World.isMapdynamic(this.aStar.getXY(dir, true) + getX(), this.aStar.getXY(dir, false) + getY(), getMapId()); boolean door = World.문이동(getX(), getY(), getMapId(), dir); if (this instanceof L1DollInstance) { doNormalFollow(); obj = false; return false; }  if (tail && !obj && !door) setDirectionMoveSpeed(dir);  setSleepTime(setDirectionMoveSpeed(dir)); } else { return true; }  } else { if (L1World.getInstance().getRecognizePlayer((L1Object)this).size() == 0) return true;  if (this._master == null && isMovable() && !isRest()) { L1MobGroupInfo mobGroupInfo = getMobGroupInfo(); if (mobGroupInfo == null || (mobGroupInfo != null && mobGroupInfo.isLeader(this))) { if (this._randomMoveDistance == 0) { this._randomMoveDistance = _random.nextInt(5) + 1; this._randomMoveDirection = _random.nextInt(20); if (getHomeX() != 0 && getHomeY() != 0 && this._randomMoveDirection < 8 && _random.nextInt(3) == 0) this._randomMoveDirection = moveDirection(getMapId(), getHomeX(), getHomeY());  } else { this._randomMoveDistance--; }  if (this._randomMoveDirection < 8 && getHomeX() != 0 && getHomeY() != 0) { byte dis = 0; if (getNpcId() == 70009) { dis = 3; } else if (getNpcId() == 70027) { dis = 2; }  if (dis != 0 && getLocation().getLineDistance(new Point(getHomeX(), getHomeY())) > dis) this._randomMoveDirection = moveDirection(getMapId(), getHomeX(), getHomeY());  }  int dir = checkObject(getX(), getY(), getMapId(), this._randomMoveDirection); if (dir != -1) setSleepTime(setDirectionMoveSpeed(dir));  } else { L1NpcInstance leader = mobGroupInfo.getLeader(); if (getLocation().getTileLineDistance((Point)leader.getLocation()) > 2) { int dir = moveDirection(leader.getMapId(), leader.getX(), leader.getY()); if (dir == -1) return true;  setSleepTime(setDirectionMoveSpeed(dir)); }  }  }  }  return false; } public void onFinalAction(L1PcInstance pc, String s) {} public void tagertClear() { if (this._target != null) this._hateList.remove(this._target);  this._target = null; this._moveType = 0; } public void targetRemove(L1Character target) { this._hateList.remove(target); if (this._target != null && this._target.equals(target)) { this._target = null; this._moveType = 0; }  } public void allTargetClear() { this._hateList.clear(); this._dropHateList.clear(); this._target = null; this._targetItemList.clear(); this._targetItem = null; this._moveType = 0; } public void setMaster(L1Character cha) { this._master = cha; } public L1Character getMaster() { return this._master; } public void onNpcAI() {} public void refineItem() { int[] materials = null; int[] counts = null; int[] createitem = null; int[] createcount = null; if (this._npcTemplate.get_npcId() == 45032) { if (get_exp() != 0L && !this._inventory.checkItem(20)) { materials = new int[] { 40508, 40521, 40045 }; counts = new int[] { 150, 3, 3 }; createitem = new int[] { 20 }; createcount = new int[] { 1 }; if (this._inventory.checkItem(materials, counts)) { for (int i = 0; i < materials.length; i++) this._inventory.consumeItem(materials[i], counts[i]);  for (int j = 0; j < createitem.length; j++) this._inventory.storeItem(createitem[j], createcount[j]);  }  }  if (get_exp() != 0L && !this._inventory.checkItem(19)) { materials = new int[] { 40494, 40521 }; counts = new int[] { 150, 3 }; createitem = new int[] { 19 }; createcount = new int[] { 1 }; if (this._inventory.checkItem(materials, counts)) { for (int i = 0; i < materials.length; i++) this._inventory.consumeItem(materials[i], counts[i]);  for (int j = 0; j < createitem.length; j++) this._inventory.storeItem(createitem[j], createcount[j]);  }  }  if (get_exp() != 0L && !this._inventory.checkItem(3)) { materials = new int[] { 40494, 40521 }; counts = new int[] { 50, 1 }; createitem = new int[] { 3 }; createcount = new int[] { 1 }; if (this._inventory.checkItem(materials, counts)) { for (int i = 0; i < materials.length; i++) this._inventory.consumeItem(materials[i], counts[i]);  for (int j = 0; j < createitem.length; j++) this._inventory.storeItem(createitem[j], createcount[j]);  }  }  if (get_exp() != 0L && !this._inventory.checkItem(100)) { materials = new int[] { 88, 40508, 40045 }; counts = new int[] { 4, 80, 3 }; createitem = new int[] { 100 }; createcount = new int[] { 1 }; if (this._inventory.checkItem(materials, counts)) { for (int i = 0; i < materials.length; i++) this._inventory.consumeItem(materials[i], counts[i]);  for (int j = 0; j < createitem.length; j++) this._inventory.storeItem(createitem[j], createcount[j]);  }  }  if (get_exp() != 0L && !this._inventory.checkItem(89)) { materials = new int[] { 88, 40494 }; counts = new int[] { 2, 80 }; createitem = new int[] { 89 }; createcount = new int[] { 1 }; if (this._inventory.checkItem(materials, counts)) { for (int i = 0; i < materials.length; i++) this._inventory.consumeItem(materials[i], counts[i]);  L1ItemInstance item = null; for (int j = 0; j < createitem.length; j++) { item = this._inventory.storeItem(createitem[j], createcount[j]); if (getNpcTemplate().get_digestitem() > 0) setDigestItem(item);  }  }  }  } else if (this._npcTemplate.get_npcId() == 81069) { if (get_exp() != 0L && !this._inventory.checkItem(40542)) { materials = new int[] { 40032 }; counts = new int[] { 1 }; createitem = new int[] { 40542 }; createcount = new int[] { 1 }; if (this._inventory.checkItem(materials, counts)) { for (int i = 0; i < materials.length; i++) this._inventory.consumeItem(materials[i], counts[i]);  for (int j = 0; j < createitem.length; j++) this._inventory.storeItem(createitem[j], createcount[j]);  }  }  } else if ((this._npcTemplate.get_npcId() == 45166 || this._npcTemplate.get_npcId() == 45167) && get_exp() != 0L && !this._inventory.checkItem(40726)) { materials = new int[] { 40725 }; counts = new int[] { 1 }; createitem = new int[] { 40726 }; createcount = new int[] { 1 }; if (this._inventory.checkItem(materials, counts)) { for (int i = 0; i < materials.length; i++) this._inventory.consumeItem(materials[i], counts[i]);  for (int j = 0; j < createitem.length; j++) this._inventory.storeItem(createitem[j], createcount[j]);  }  }  } public L1HateList getHateList() { return this._hateList; } public void setParalysisTime(int ptime) { this._paralysisTime = ptime; } public int getParalysisTime() { return this._paralysisTime; } private void doAStarFailFollow() { Point current = new Point(getX(), getY());
     Point targetPoint = new Point(this._master.getX(), this._master.getY());

     if (this._aStar.getTarget().getX() != targetPoint.getX() || this._aStar.getTarget().getY() != targetPoint.getY()) {
       this._aStar.setTarget(getMap(), targetPoint);
     }

     if (this._aStar.getTryCount() < 10 && this._aStar.compute(current) != null) {
       this._moveType = 1;
       doAStarFollow();

       return;
     }
     int dir = moveDirection3(this._target.getX(), this._target.getY());

     if (dir != -1 && getMap().isPassable(current, dir) && !isObjectExist(current.getX(), current.getY(), getMapId(), dir)) {
       setDirectionMoveSpeed(dir);
     }

     setSleepTime(getCurrentSpriteInterval(EActionCodes.walk)); }
   public final void startHpRegeneration() { int hprInterval = getNpcTemplate().get_hprinterval(); int hpr = getNpcTemplate().get_hpr(); if (!this._hprRunning && hprInterval > 0 && hpr > 0) { this._hprTimer = new HprTimer(hpr, hprInterval); GeneralThreadPool.getInstance().schedule(this._hprTimer, hprInterval); this._hprRunning = true; }  }
   public final void stopHpRegeneration() { if (this._hprRunning) { this._hprTimer.cancel(); this._hprRunning = false; }  }
   public final void startMpRegeneration() { int mprInterval = getNpcTemplate().get_mprinterval(); int mpr = getNpcTemplate().get_mpr(); if (!this._mprRunning && mprInterval > 0 && mpr > 0) { this._mprTimer = new MprTimer(mpr, mprInterval); GeneralThreadPool.getInstance().schedule(this._mprTimer, mprInterval); this._mprRunning = true; }  }
   public final void stopMpRegeneration() { if (this._mprRunning) { this._mprTimer.cancel(); this._mprRunning = false; }  }
   class HprTimer implements Runnable {
     private boolean _active;
     private long _interval;
     private int _point;
     public void run() { try { if (!this._active) return;  if (!L1NpcInstance.this._destroyed && !L1NpcInstance.this.isDead() && L1NpcInstance.this.getCurrentHp() > 0 && L1NpcInstance.this.getCurrentHp() < L1NpcInstance.this.getMaxHp()) { L1NpcInstance.this.setCurrentHp(L1NpcInstance.this.getCurrentHp() + this._point); GeneralThreadPool.getInstance().schedule(this, this._interval); } else { L1NpcInstance.this._hprRunning = false; }  } catch (Exception e) { e.printStackTrace(); }  }
     public HprTimer(int point, long interval) { if (point < 1) point = 1;  this._point = point; this._active = true; this._interval = interval; }
     public void cancel() { this._active = false; } }
   class MprTimer implements Runnable {
     private boolean _active;
     private long _interval;
     private int _point;
     public void run() { try { if (!this._active) return;  if (!L1NpcInstance.this._destroyed && !L1NpcInstance.this.isDead() && L1NpcInstance.this.getCurrentHp() > 0 && L1NpcInstance.this.getCurrentMp() < L1NpcInstance.this.getMaxMp()) { L1NpcInstance.this.setCurrentMp(L1NpcInstance.this.getCurrentMp() + this._point); GeneralThreadPool.getInstance().schedule(this, this._interval); } else { L1NpcInstance.this._mprRunning = false; }  } catch (Exception e) { e.printStackTrace(); }  }
     public MprTimer(int point, long interval) { if (point < 1) point = 1;  this._point = point; this._active = true; this._interval = interval; }
     public void cancel() { this._active = false; } }
   class DigestItemTimer implements Runnable {
     public void run() { if (!L1NpcInstance.this._digestItemRunning) L1NpcInstance.this._digestItemRunning = true;  Object[] keys = null; L1ItemInstance digestItem = null; if (!L1NpcInstance.this._destroyed && L1NpcInstance.this._digestItems.size() > 0) { keys = L1NpcInstance.this._digestItems.keySet().toArray(); Integer key = null; Integer digestCounter = null; for (int i = 0; i < keys.length; i++) { key = (Integer)keys[i]; digestCounter = (Integer)L1NpcInstance.this._digestItems.get(key); digestCounter = Integer.valueOf(digestCounter.intValue() - 1); if (digestCounter.intValue() <= 0) { L1NpcInstance.this._digestItems.remove(key); digestItem = L1NpcInstance.this.getInventory().getItem(key.intValue()); if (digestItem != null) L1NpcInstance.this.getInventory().removeItem(digestItem, digestItem.getCount());  } else { L1NpcInstance.this._digestItems.put(key, digestCounter); }  }  GeneralThreadPool.getInstance().schedule(this, 1000L); } else { L1NpcInstance.this._digestItemRunning = false; }  } }
   public boolean isPickupItem() { return this._pickupItem; }
   public void setPickupItem(boolean flag) { this._pickupItem = flag; }
   public L1Inventory getInventory() { return this._inventory; }
   public void setInventory(L1Inventory inventory) { this._inventory = inventory; }
   public L1Npc getNpcTemplate() { return this._npcTemplate; }
   public int getNpcId() { return this._npcTemplate.get_npcId(); }
   public int getNpcClassId() { return this._npcTemplate.get_npc_class_id(); }
   public void setPetcost(int i) { this._petcost = i; }
   public int getPetcost() { return this._petcost; }
   public void setSpawn(L1Spawn spawn) { this._spawn = spawn; }
   public L1Spawn getSpawn() { return this._spawn; }
   public void set_spawn_ex(MJSpawnInfo sInfo) { this.m_spawn_ex = sInfo; } public MJSpawnInfo get_spawn_ex() { return this.m_spawn_ex; } public void setSpawnNumber(int number) { this._spawnNumber = number; } public int getSpawnNumber() { return this._spawnNumber; } public void onDecay(boolean isReuseId) { int id = 0; if (isReuseId) { id = getId(); } else { id = 0; }  this._spawn.executeSpawnTask(this._spawnNumber, id); } public void onPerceive(L1PcInstance perceivedFrom) { if (this == null || perceivedFrom == null) return;  perceivedFrom.addKnownObject((L1Object)this); if (perceivedFrom.getAI() == null) { perceivedFrom.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this)); if (getNpcTemplate().get_npcId() == 900168) perceivedFrom.sendPackets((ServerBasePacket)new S_Door(getX(), getY(), 0, this.PASS));  }  if (hasSkillEffect(113) && (getTrueTargetClan() == perceivedFrom.getClanid() || getTrueTargetParty() == perceivedFrom.getPartyID())) perceivedFrom.sendPackets((ServerBasePacket)new S_TrueTargetNew(getId(), true));  getMap().setPassable((Point)getLocation(), true); onNpcAI(); } public void dispose() { this._mobGroupInfo = null; this._inventory = null; if (this._digestItems != null) { this._digestItems.clear(); this._digestItems = null; }  if (getNpcTemplate().get_npcId() != 8500138 && this.mobSkill != null) { this.mobSkill.dispose(); this.mobSkill = null; }  this._spawn = null; super.dispose(); } public void deleteMe() { try { this._destroyed = true; if (isDead() && this instanceof L1MonsterInstance) { L1MonsterInstance m = (L1MonsterInstance)this; MJMonsterDeathHandler h = m.getDeathHandler(); if (h != null) h.onDeathNotify(m);  }  getMap().setPassable((Point)getLocation(), true); if (getInventory() != null) getInventory().clearItems();  allTargetClear(); L1World.getInstance().removeVisibleObject((L1Object)this); L1World.getInstance().removeObject((L1Object)this); List<L1PcInstance> players = L1World.getInstance().getRecognizePlayer((L1Object)this); if (players.size() > 0) { S_RemoveObject s_deleteNewObject = new S_RemoveObject((L1Object)this); for (L1PcInstance pc : players) { if (pc != null) { pc.removeKnownObject((L1Object)this); pc.sendPackets((ServerBasePacket)s_deleteNewObject); }  }  }  removeAllKnownObjects(); L1World.getInstance().removeVisibleObject((L1Object)this); L1World.getInstance().removeObject((L1Object)this); this.mobSkill = null; L1MobGroupInfo mobGroupInfo = getMobGroupInfo(); if (mobGroupInfo == null) { if (isReSpawn()) onDecay(true);  } else if (mobGroupInfo.removeMember(this) == 0) { setMobGroupInfo((L1MobGroupInfo)null); if (isReSpawn()) onDecay(false);  }  if (this.m_spawn_ex != null) this.m_spawn_ex.on_death(this);  if (this instanceof L1MonsterInstance) dispose();  } catch (Exception e) { e.printStackTrace(); }  } public void deleteMe2() { this._destroyed = true; if (isDead() && this instanceof L1MonsterInstance) { L1MonsterInstance m = (L1MonsterInstance)this; MJMonsterDeathHandler h = m.getDeathHandler(); if (h != null) h.onDeathNotify(m);  }  if (getInventory() != null) getInventory().clearItems();  this._master = null; L1World world = L1World.getInstance(); List<L1PcInstance> players = world.getRecognizePlayer((L1Object)this); if (players != null && players.size() > 0) { S_RemoveObject s_deleteNewObject = new S_RemoveObject((L1Object)this); for (L1PcInstance pc : players) { if (pc != null) { pc.removeKnownObject((L1Object)this); pc.sendPackets((ServerBasePacket)s_deleteNewObject); }  }  }  removeAllKnownObjects(); world.removeVisibleObject((L1Object)this); world.removeObject((L1Object)this); dispose(); } public void ReceiveManaDamage(L1Character attacker, int damageMp) {} public void receiveCounterBarrierDamage(L1Character attacker, int damage) { receiveDamage(attacker, damage); } public void receiveConqureDamage(L1Character attacker, int damage) { receiveDamage(attacker, damage); } public void receiveDamage(L1Character attacker, int damage) {} public void setDigestItem(L1ItemInstance item) { if (item == null) return;  this._digestItems.put(new Integer(item.getId()), new Integer(getNpcTemplate().get_digestitem())); if (!this._digestItemRunning) { DigestItemTimer digestItemTimer = new DigestItemTimer(); GeneralThreadPool.getInstance().execute(digestItemTimer); }  } public void onGetItem(L1ItemInstance item) { refineItem(); getInventory().shuffle(); if (getNpcTemplate().get_digestitem() > 0) setDigestItem(item);  } public void approachPlayer(L1PcInstance pc) { if (pc == null) return;  if (pc.hasSkillEffect(60) || pc.hasSkillEffect(97)) return;  if (getHiddenStatus() == 1) { if (getCurrentHp() == getMaxHp() && pc.getLocation().getTileLineDistance((Point)getLocation()) <= 2) appearOnGround(pc);  } else if (getHiddenStatus() == 2) { if (getCurrentHp() == getMaxHp()) { if (pc.getLocation().getTileLineDistance((Point)getLocation()) <= 1) appearOnGround(pc);  } else { searchItemFromAir(); }  }  } public void appearOnGround(L1PcInstance pc) { if (pc == null) return;  if (getHiddenStatus() == 1) { setHiddenStatus(0); setStatus(0); broadcastPacket((ServerBasePacket)new S_RemoveObject((L1Object)this), true); broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 4)); broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this)); if (!pc.hasSkillEffect(60) && !pc.hasSkillEffect(97) && !pc.isGm()) { this._hateList.add(pc, 0); this._target = pc; this._moveType = 0; }  onNpcAI(); } else if (getHiddenStatus() == 2) { setHiddenStatus(0); broadcastPacket((ServerBasePacket)new S_DoActionGFX(getId(), 45)); setStatus(0); broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this)); if (!pc.hasSkillEffect(60) && !pc.hasSkillEffect(97) && !pc.isGm()) { this._hateList.add(pc, 0); this._target = pc; this._moveType = 0; }  onNpcAI(); startChat(2); }  } protected void on_moved() {} public L1NpcInstance(L1Npc template) { this.AStar_Try_Count = 0;



     this.cnt = 0;

     this._passable = 1;


     this._breakable = false;


     this._paralysisTime = 0;


     this._hprRunning = false;

     this._mprRunning = false;


     this.PASS = 1;

     this._movementDistance = 0;
     this._tempLawful = 0;


     this._destroyed = false;

     this.CastleTarget = null;

     this.skillDelayTime = 0L;
     this.castgfx = 0;
     this.isMentTime = 0L;

     this.synchObject = new Object();
     this._isPassObject = true;


     this._delete_delay = 0;



    this._mapnum = 0; this.mEmptyWalkableCount = 0; setStatus(0); setMoveSpeed(0); setDead(false); setRespawn(false); if (template != null) { if (template.getUseAction() == 1 || template.getUseAction() == 2) { this.iPath = new int[51][2]; this.aStar = new AStar(); this.aStar.setCha(this); }  setting_template(template); }  } private static final byte[] HEADING_TABLE_X = new byte[] { 0, 1, 1, 1, 0, -1, -1, -1 }; private static final byte[] HEADING_TABLE_Y = new byte[] { -1, -1, 0, 1, 1, 1, 0, -1 }; private AStar aStar; private Node tail; private int[][] iPath; private int iCurrentPath; public static final int USEITEM_HEAL = 0; public static final int USEITEM_HASTE = 1; public long setDirectionMoveSpeed(int dir) { long itv = getNpcTemplate().doProbabilityBornNpc(this); if (itv > 0L) return itv;  if (dir >= 0) { int nx = 0; int ny = 0; switch (dir) { case 1: nx = 1; ny = -1; setHeading(1); break;case 2: nx = 1; ny = 0; setHeading(2); break;case 3: nx = 1; ny = 1; setHeading(3); break;case 4: nx = 0; ny = 1; setHeading(4); break;case 5: nx = -1; ny = 1; setHeading(5); break;case 6: nx = -1; ny = 0; setHeading(6); break;case 7: nx = -1; ny = -1; setHeading(7); break;case 0: nx = 0; ny = -1; setHeading(0); break; }  if (getMapId() == 13004 && !getMap().isPassable(getX() + nx, getY() + ny)) return getCurrentSpriteInterval(EActionCodes.walk);  getMap().setPassable((Point)getLocation(), true); int nnx = getX() + nx; int nny = getY() + ny; setX(0); setY(0); setX(nnx); setY(nny); if (!(this instanceof L1DollInstance)) getMap().setPassable((Point)getLocation(), false);  broadcastPacket((ServerBasePacket)new S_MoveCharPacket(this)); MJMyMapViewService.service().onMoveObject(this); on_moved(); if (getMovementDistance() > 0 && (this instanceof L1GuardInstance || this instanceof L1CastleGuardInstance || this instanceof L1MerchantInstance || this instanceof L1MonsterInstance) && getLocation().getLineDistance(new Point(getHomeX(), getHomeY())) > getMovementDistance()) npcInitialize();  if (getNpcTemplate().get_npcId() >= 45912 && getNpcTemplate().get_npcId() <= 45916 && getX() >= 32591 && getX() <= 32644 && getY() >= 32643 && getY() <= 32688 && getMapId() == 4 && getMovementDistance() <= 0 && Config.ServerAdSetting.NpcMaxYN) npcInitialize();  }  return getCurrentSpriteInterval(EActionCodes.walk); } public int moveDirection3(int x, int y) { int dir = targetDirection(x, y); dir = checkObject(getX(), getY(), getMapId(), dir); return dir; } public int moveDirection(int mapid, int x, int y) { return moveDirection(mapid, x, y, getLocation().getLineDistance(new Point(x, y))); } public int directionFromCurrent(int cx, int cy, int tx, int ty) { float dis_x = Math.abs(cx - tx); float dis_y = Math.abs(cy - ty); float dis = Math.max(dis_x, dis_y); if (dis == 0.0F) return 8;  int avg_x = (int)Math.floor((dis_x / dis + 0.59F)); int avg_y = (int)Math.floor((dis_y / dis + 0.59F)); int dir_x = 0; int dir_y = 0; if (cx < tx) dir_x = 1;  if (cx > tx) dir_x = -1;  if (cy < ty) dir_y = 1;  if (cy > ty) dir_y = -1;  if (avg_x == 0) dir_x = 0;  if (avg_y == 0) dir_y = 0;  if (dir_x == 1 && dir_y == -1) return 1;  if (dir_x == 1 && dir_y == 0) return 2;  if (dir_x == 1 && dir_y == 1) return 3;  if (dir_x == 0 && dir_y == 1) return 4;  if (dir_x == -1 && dir_y == 1) return 5;  if (dir_x == -1 && dir_y == 0) return 6;  if (dir_x == -1 && dir_y == -1) return 7;  if (dir_x == 0 && dir_y == -1) return 0;  return -1; } public int moveDirection(int mapid, int x, int y, double d) { int dir = 0; if (hasSkillEffect(40) == true && d >= 2.0D) return -1;  if (d > 30.0D) return -1;  if (d > courceRange) { dir = targetDirection(x, y); dir = checkObject(getX(), getY(), getMapId(), dir); } else { dir = _serchCource(x, y, mapid); if (dir == -1) { dir = targetDirection(x, y); if (!isExsistCharacterBetweenTarget(dir)) dir = checkObject(getX(), getY(), getMapId(), dir);  }  }  return dir; } public int moveDirection2(int x, int y, double d) { if (hasSkillEffect(40) == true && d >= 2.0D) return -1;  if (d > 30.0D) return -1;  int newX = getX(); int newY = getY(); int firstDir = directionFromCurrent(newX, newY, x, y); int dir = firstDir; while (true) { if (dir == 8) { if (firstDir == 8) firstDir = 0;  return firstDir; }  if (!getMap().isPassable(newX, newY, dir)) return -1;  switch (dir) { case 0: newY--; break;case 1: newX++; newY--; break;case 2: newX++; break;case 3: newX++; newY++; break;case 4: newY++; break;case 5: newX--; newY++; break;case 6: newX--; break;case 7: newX--; newY--; break; }  dir = directionFromCurrent(newX, newY, x, y); }  } public boolean isExsistCharacterBetweenTarget(int dir) { if (!(this instanceof L1MonsterInstance)) return false;  if (this._target == null) return false;  L1Character cha = null; L1PcInstance pc = null; for (L1Object object : L1World.getInstance().getVisibleObjects((L1Object)this, 1)) { if (object instanceof L1PcInstance || object instanceof L1SummonInstance || object instanceof L1PetInstance) { cha = (L1Character)object; if (!cha.isDead()) { boolean matched = false; for (int i = 0; i < 4; i++) { if (!cha.getMap().isUserPassable(cha.getX(), cha.getY(), i) && !cha.getMap().isUserPassable(cha.getX(), cha.getY(), i + 4)) { matched = true; break; }  }  if (!matched) continue;  if (object instanceof L1PcInstance) { pc = (L1PcInstance)object; if (pc.isGhost()) continue;  }  this._hateList.add(cha, 0); this._target = cha; return true; }  }  }  return false; } public int targetReverseDirection(int tx, int ty) { int dir = targetDirection(tx, ty); dir += 4; if (dir > 7) dir -= 8;  return dir; } private static boolean isObjectExist(int x, int y, short m, int d) { L1Location loc = new L1Location(); loc.setX(x); loc.setY(y); loc.setMap(m); loc.forward(d); for (L1Object object : L1World.getInstance().getVisiblePoint(loc, 0)) { if (object instanceof L1Character && !(object instanceof L1DollInstance)) { if (m >= 732 && m <= 776) return false;  if (!((L1Character)object).isDead()) return true;  }  }  return false; } public static int checkObject2(int x, int y, short m, int d) { L1Map map = L1WorldMap.getInstance().getMap(m); L1Location loc = new L1Location(); loc.setX(x); loc.setY(y); loc.setMap(m); switch (d) { case 1: loc.forward(1); if (map.isPassable(x, y, 1) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 1;  loc.backward(1); loc.forward(0); if (map.isPassable(x, y, 0) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 0;  loc.backward(0); loc.forward(2); if (map.isPassable(x, y, 2) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 2;  break;case 2: loc.forward(2); if (map.isPassable(x, y, 2) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 2;  loc.backward(2); loc.forward(1); if (map.isPassable(x, y, 1) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 1;  loc.backward(1); loc.forward(3); if (map.isPassable(x, y, 3) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 3;  break;case 3: loc.forward(3); if (map.isPassable(x, y, 3) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 3;  loc.backward(3); loc.forward(2); if (map.isPassable(x, y, 2) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 2;  loc.backward(2); loc.forward(4); if (map.isPassable(x, y, 4) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 4;  break;case 4: loc.forward(4); if (map.isPassable(x, y, 4) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 4;  loc.backward(4); loc.forward(3); if (map.isPassable(x, y, 3) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 3;  loc.backward(3); loc.forward(5); if (map.isPassable(x, y, 5) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 5;  break;case 5: loc.forward(5); if (map.isPassable(x, y, 5) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 5;  loc.backward(5); loc.forward(4); if (map.isPassable(x, y, 4) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 4;  loc.backward(4); loc.forward(6); if (map.isPassable(x, y, 6) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 6;  break;case 6: loc.forward(6); if (map.isPassable(x, y, 6) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 6;  loc.backward(6); loc.forward(5); if (map.isPassable(x, y, 5) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 5;  loc.backward(5); loc.forward(7); if (map.isPassable(x, y, 7) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 7;  break;case 7: loc.forward(7); if (map.isPassable(x, y, 7) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 7;  loc.backward(7); loc.forward(6); if (map.isPassable(x, y, 6) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 6;  loc.backward(6); loc.forward(0); if (map.isPassable(x, y, 0) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 0;  break;case 0: loc.forward(0); if (map.isPassable(x, y, 0) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 0;  loc.backward(0); loc.forward(7); if (map.isPassable(x, y, 7) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 7;  loc.backward(7); loc.forward(1); if (map.isPassable(x, y, 1) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) return 1;  break; }  return -1; } public static int checkObject(int x, int y, short m, int d) { L1Map map = L1WorldMap.getInstance().getMap(m); switch (d) { case 1: if (map.isPassable(x, y, 1)) return 1;  if (map.isPassable(x, y, 0)) return 0;  if (map.isPassable(x, y, 2)) return 2;  break;case 2: if (map.isPassable(x, y, 2)) return 2;  if (map.isPassable(x, y, 1)) return 1;  if (map.isPassable(x, y, 3)) return 3;  break;case 3: if (map.isPassable(x, y, 3)) return 3;  if (map.isPassable(x, y, 2)) return 2;  if (map.isPassable(x, y, 4)) return 4;  break;case 4: if (map.isPassable(x, y, 4)) return 4;  if (map.isPassable(x, y, 3)) return 3;  if (map.isPassable(x, y, 5)) return 5;  break;case 5: if (map.isPassable(x, y, 5)) return 5;  if (map.isPassable(x, y, 4)) return 4;  if (map.isPassable(x, y, 6)) return 6;  break;case 6: if (map.isPassable(x, y, 6)) return 6;  if (map.isPassable(x, y, 5)) return 5;  if (map.isPassable(x, y, 7)) return 7;  break;case 7: if (map.isPassable(x, y, 7)) return 7;  if (map.isPassable(x, y, 6)) return 6;  if (map.isPassable(x, y, 0)) return 0;  break;case 0: if (map.isPassable(x, y, 0)) return 0;  if (map.isPassable(x, y, 7)) return 7;  if (map.isPassable(x, y, 1)) return 1;  break; }  return -1; } public int calcheading(L1Object o, int x, int y) { return calcheading(o.getX(), o.getY(), x, y); } public int _serchCource(int x, int y, int m) { try { this.aStar.cleanTail(); this.tail = this.aStar.searchTail((L1Object)this, x, y, m, true); try { if (this.tail != null) { this.iCurrentPath = -1; while (this.tail != null && (this.tail.x != getX() || this.tail.y != getY())) { if (this.iCurrentPath >= this.iPath.length - 1) return -1;  if (this._destroyed || isDead()) return -1;  this.iPath[++this.iCurrentPath][0] = this.tail.x; this.iPath[this.iCurrentPath][1] = this.tail.y; this.tail = this.tail.prev; }  return (this.iCurrentPath != -1) ? this.aStar.calcheading(getX(), getY(), this.iPath[this.iCurrentPath][0], this.iPath[this.iCurrentPath][1]) : -1; }  this.aStar.cleanTail(); this.tail = this.aStar.근접서치타일((L1Object)this, x, y, m, true); if (this.tail != null && (this.tail.x != getX() || this.tail.y != getY())) { this.iCurrentPath = -1; while (this.tail != null && (this.tail.x != getX() || this.tail.y != getY())) { if (this.iCurrentPath >= this.iPath.length - 1) return -1;  if (this._destroyed || isDead()) return -1;  this.iPath[++this.iCurrentPath][0] = this.tail.x; this.iPath[this.iCurrentPath][1] = this.tail.y; this.tail = this.tail.prev; }  return (this.iCurrentPath != -1) ? this.aStar.calcheading(getX(), getY(), this.iPath[this.iCurrentPath][0], this.iPath[this.iCurrentPath][1]) : -1; }  int chdir = calcheading((L1Object)this, x, y); if (getMoveState().getHeading() != chdir) { getMoveState().setHeading(calcheading((L1Object)this, x, y)); Broadcaster.broadcastPacket(this, (ServerBasePacket)new S_ChangeHeading(this), true); }  return getMoveState().getHeading(); } catch (Exception e) { return -1; }  } catch (Exception e) { return -1; }  } private void _moveLocation(int[] ary, int d) { switch (d) { case 1: ary[0] = ary[0] + 1; ary[1] = ary[1] - 1; break;case 2: ary[0] = ary[0] + 1; break;case 3: ary[0] = ary[0] + 1; ary[1] = ary[1] + 1; break;case 4: ary[1] = ary[1] + 1; break;case 5: ary[0] = ary[0] - 1; ary[1] = ary[1] + 1; break;case 6: ary[0] = ary[0] - 1; break;case 7: ary[0] = ary[0] - 1; ary[1] = ary[1] - 1; break;case 0: ary[1] = ary[1] - 1; break; }  ary[2] = d; } private void _getFront(int[] ary, int d) { switch (d) { case 1: ary[4] = 2; ary[3] = 0; ary[2] = 1; ary[1] = 3; ary[0] = 7; break;case 2: ary[4] = 2; ary[3] = 4; ary[2] = 0; ary[1] = 1; ary[0] = 3; break;case 3: ary[4] = 2; ary[3] = 4; ary[2] = 1; ary[1] = 3; ary[0] = 5; break;case 4: ary[4] = 2; ary[3] = 4; ary[2] = 6; ary[1] = 3; ary[0] = 5; break;case 5: ary[4] = 4; ary[3] = 6; ary[2] = 3; ary[1] = 5; ary[0] = 7; break;case 6: ary[4] = 4; ary[3] = 6; ary[2] = 0; ary[1] = 5; ary[0] = 7; break;case 7: ary[4] = 6; ary[3] = 0; ary[2] = 1; ary[1] = 5; ary[0] = 7; break;case 0: ary[4] = 2; ary[3] = 6; ary[2] = 0; ary[1] = 1; ary[0] = 7; break; }  } private void useHealPotion(int healHp, int effectId) { broadcastPacket((ServerBasePacket)new S_SkillSound(getId(), effectId)); if (hasSkillEffect(173)) healHp /= 2;  if (this instanceof L1PetInstance) { ((L1PetInstance)this).setCurrentHp(getCurrentHp() + healHp); } else if (this instanceof L1SummonInstance) { ((L1SummonInstance)this).setCurrentHp(getCurrentHp() + healHp); } else { setCurrentHp(getCurrentHp() + healHp); }  }
  public void useHastePotion(int time) { broadcastPacket((ServerBasePacket)new S_SkillHaste(getId(), 1, time)); broadcastPacket((ServerBasePacket)new S_SkillSound(getId(), 191)); setMoveSpeed(1); setSkillEffect(1001, (time * 1000)); }
  public static int[] healPotions = new int[] { 40012, 40011, 40010, 4100021, 4100656, 4100657, 4100658 }; public static int[] haestPotions = new int[] { 140018, 40018, 30158, 140013, 40013, 7006, 4100662 }; private String _nameId; private boolean _Agro; private boolean _Agrocoi; private boolean _Agrososc; private int _homeX; private int _homeY; private int _homeRnd; private boolean _reSpawn; private int _lightSize; private boolean _weaponBreaked; private int _hiddenStatus; private int _movementDistance; private int _tempLawful; public boolean _destroyed; private String title; private String subnpc; private String subtitle; private String subactid; private boolean is_sub_npc; private long boss_time; private long boss_end_time; private int _boss_ing_time; private String[] _yoil; private int _next_day_index; private int boss_type; private int boss_hour; private int boss_minute; private boolean is_boss_tel; private int boss_tel_x; private int boss_tel_y; private int boss_tel_m; private int boss_tel_rnd; private int boss_tel_count; private boolean is_boss_msg; private String boss_msg; private boolean boss_yn; private String boss_yn_msg; private boolean is_boss_effect; private int boss_effect; private boolean is_boss_alarm; private EventTimeTemp _eventTimeTemp; public L1NpcInstance CastleTarget; public long skillDelayTime; public int castgfx;
  public void set_Mapnum(int mapid) { this._mapnum = mapid; } public long isMentTime; public Object synchObject; private boolean _isPassObject; private long _explosion_remain_time; private String _shopName; public void useItem(int type, int chance) { if (hasSkillEffect(71)) return;  Random random = new Random(); if (random.nextInt(100) > chance) return;  if (type == 0) { if (getInventory().consumeItem(40012, 1)) { useHealPotion(75, 197); } else if (getInventory().consumeItem(40011, 1)) { useHealPotion(45, 194); } else if (getInventory().consumeItem(40010, 1)) { useHealPotion(15, 189); }  } else if (type == 1) { if (hasSkillEffect(1001)) return;  if (getInventory().consumeItem(140018, 1) || getInventory().consumeItem(30158, 1)) { useHastePotion(2100); } else if (getInventory().consumeItem(40018, 1)) { useHastePotion(1800); } else if (getInventory().consumeItem(140013, 1)) { useHastePotion(350); } else if (getInventory().consumeItem(40013, 1) || getInventory().consumeItem(7006, 1) || getInventory().consumeItem(4100662, 1)) { useHastePotion(300); }  }  } public boolean nearTeleport(int nx, int ny) { int rdir = _random.nextInt(8); for (int i = 0; i < 8; i++) { int dir = rdir + i; if (dir > 7) dir -= 8;  switch (dir) { case 1: nx++; ny--; break;case 2: nx++; break;case 3: nx++; ny++; break;case 4: ny++; break;case 5: nx--; ny++; break;case 6: nx--; break;case 7: nx--; ny--; break;case 0: ny--; break; }  if (getMap().isPassable(nx, ny)) { dir += 4; if (dir > 7) dir -= 8;  teleport(nx, ny, dir); setCurrentMp(getCurrentMp() - 10); return true; }  }  return false; } public boolean RunTeleport(int nx, int ny) { if (getMap().isPassable(nx, ny)) { teleport(nx, ny, CommonUtil.random(7)); return true; }  return false; } public void teleport(int nx, int ny, int dir) { for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer((L1Object)this)) { if (pc == null) continue;  pc.sendPackets((ServerBasePacket)new S_SkillSound(getId(), 169)); pc.sendPackets((ServerBasePacket)new S_RemoveObject((L1Object)this)); pc.removeKnownObject((L1Object)this); }  getMap().setPassable((Point)getLocation(), true); setX(nx); setY(ny); setHeading(dir); if (!(this instanceof L1DollInstance) && !(this instanceof L1TowerInstance) && !(this instanceof MJCompanionInstance)) getMap().setPassable((Point)getLocation(), false);  broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this)); } private void npcInitialize() { setCurrentHp(getMaxHp()); setCurrentMp(getMaxMp()); allTargetClear(); teleport(getHomeX(), getHomeY(), getHeading()); } public String getNameId() { return this._nameId; } public void setNameId(String s) { this._nameId = s; } public boolean isAgro() { return this._Agro; } public void setAgro(boolean flag) { this._Agro = flag; } public boolean isAgrocoi() { return this._Agrocoi; } public void setAgrocoi(boolean flag) { this._Agrocoi = flag; } public boolean isAgrososc() { return this._Agrososc; } public void setAgrososc(boolean flag) { this._Agrososc = flag; } public int getHomeX() { return this._homeX; } public void setHomeX(int i) { this._homeX = i; } public int getHomeY() { return this._homeY; } public void setHomeY(int i) { this._homeY = i; } public int getHomeRnd() { return this._homeRnd; } public void setHomeRnd(int i) { this._homeRnd = i; } public boolean isReSpawn() { return this._reSpawn; } public void setRespawn(boolean flag) { this._reSpawn = flag; } public int getLightSize() { return this._lightSize; } public void setLightSize(int i) { this._lightSize = i; } public boolean isWeaponBreaked() { return this._weaponBreaked; } public void setWeaponBreaked(boolean flag) { this._weaponBreaked = flag; } public int getHiddenStatus() { return this._hiddenStatus; } public void setHiddenStatus(int i) { this._hiddenStatus = i; } public int getMovementDistance() { return this._movementDistance; } public void setMovementDistance(int i) { this._movementDistance = i; } public int getTempLawful() { return this._tempLawful; } public void setTempLawful(int i) { this._tempLawful = i; } protected void setAiRunning(boolean aiRunning) { this._aiRunning = aiRunning; } protected boolean isAiRunning() { return this._aiRunning; } protected void setActived(boolean actived) { this._actived = actived; } protected boolean isActived() { return this._actived; } protected void setFirstAttack(boolean firstAttack) { this._firstAttack = firstAttack; } protected boolean isFirstAttack() { return this._firstAttack; } protected void setSleepTime(long sleep_time) { this._sleep_time = sleep_time; } protected long getSleepTime() { return this._sleep_time; } public void setDeathProcessing(boolean deathProcessing) { this._deathProcessing = deathProcessing; } protected boolean isDeathProcessing() { return this._deathProcessing; } public int drainMana(int drain) { if (this._drainedMana >= Config.MagicAdSetting.MANADRAINLIMITPERNPC) return 0;  int result = Math.min(drain, getCurrentMp()); if (this._drainedMana + result > Config.MagicAdSetting.MANADRAINLIMITPERNPC) result = Config.MagicAdSetting.MANADRAINLIMITPERNPC - this._drainedMana;  this._drainedMana += result; return result; } public boolean isDestroyed() { return this._destroyed; } protected void transform(int transformId) { stopHpRegeneration(); stopMpRegeneration(); int transformGfxId = getNpcTemplate().getTransformGfxId(); if (transformGfxId != 0) broadcastPacket((ServerBasePacket)new S_SkillSound(getId(), transformGfxId));  L1Npc npcTemplate = NpcTable.getInstance().getTemplate(transformId); setting_template(npcTemplate); sendShape(getCurrentSpriteId()); ArrayList<L1PcInstance> list = null; list = L1World.getInstance().getRecognizePlayer((L1Object)this); for (L1PcInstance pc : list) { if (pc != null) onPerceive(pc);  }  } public void setRest(boolean _rest) { this._rest = _rest; } public boolean isRest() { return this._rest; } public boolean isResurrect() { return this._isResurrect; } public void setResurrect(boolean flag) { this._isResurrect = flag; } public synchronized void resurrect(int hp) { if (this._destroyed) return;  if (this._deleteTask != null) { if (!this._future.cancel(false)) return;  this._deleteTask = null; this._future = null; }  super.resurrect(hp); startHpRegeneration(); startMpRegeneration(); L1SkillUse skill = new L1SkillUse(); skill.handleCommands(null, 44, getId(), getX(), getY(), null, 0, 1, this); } protected synchronized void startDeleteTimer() { if (this._deleteTask != null) return;  this._deleteTask = new DeleteTimer(getId()); long delay_time = 0L; if (get_delete_delay() > 0) { delay_time = (get_delete_delay() * 1000); } else { delay_time = MapsTable.getInstance().get_monster_respawn_seconds(getMapId()); }  this._future = GeneralThreadPool.getInstance().schedule(this._deleteTask, delay_time); } protected static class DeleteTimer implements Runnable {
    private int _id; protected DeleteTimer(int oId) { try { this._id = oId; if (L1World.getInstance().findObject(this._id) == null) return;  if (!(L1World.getInstance().findObject(this._id) instanceof L1NpcInstance)) throw new IllegalArgumentException("if (!(obj instanceof L1NpcInstance)) {\n" + "throw new IllegalArgumentException(\"僅允許 L1NpcInstance 類型\");\n" + "}\n" +"// 繼續處理 L1NpcInstance 實例\n" +"L1NpcInstance npc = (L1NpcInstance)obj;\n" +"// 其他相關的處理邏輯");  } catch (Exception e) { e.printStackTrace(); System.out.println("[DeleteTimer 錯誤] : " + L1World.getInstance().findObject(this._id)); }  } public void run() { L1NpcInstance npc = (L1NpcInstance)L1World.getInstance().findObject(this._id); if (npc == null || !npc.isDead() || npc._destroyed) return;  try { npc.deleteMe(); } catch (Exception e) { e.printStackTrace(); }  } } public boolean isInMobGroup() { return (getMobGroupInfo() != null); } public L1MobGroupInfo getMobGroupInfo() { return this._mobGroupInfo; } public void setMobGroupInfo(L1MobGroupInfo m) { this._mobGroupInfo = m; } public int getMobGroupId() { return this._mobGroupId; } public void setMobGroupId(int i) { this._mobGroupId = i; } public void startChat(int chatTiming) { L1NewNpcChatTimer npcChatTimer; if (chatTiming == 0 && (isDead() || this.STATUS_Escape)) return;  if (chatTiming == 1 && !isDead()) return;  if (chatTiming == 2 && isDead()) return;  if (chatTiming == 4 && isDead()) return;  if (chatTiming == 5 && (isDead() || !this.STATUS_Escape)) return;  int npcId = getNpcTemplate().get_npcId(); L1NewNpcChat npcChat = null; switch (chatTiming) { case 0: npcChat = NewNpcChatTable.getInstance().getTemplateWalk(npcId); break;case 1: npcChat = NewNpcChatTable.getInstance().getTemplateDead(npcId); break;case 2: npcChat = NewNpcChatTable.getInstance().getTemplateHide(npcId); break;case 4: npcChat = NewNpcChatTable.getInstance().getTemplateSpawn(npcId); break;case 5: npcChat = NewNpcChatTable.getInstance().getTemplateEscape(npcId); break; }  if (npcChat == null) return;  if (!npcChat.isRepeat()) { npcChatTimer = new L1NewNpcChatTimer(this, npcChat); } else { npcChatTimer = new L1NewNpcChatTimer(this, npcChat, npcChat.getChatInterval()); }  npcChatTimer.startChat(); } public void setCubeTime(int CubeTime) { this.CubeTime = CubeTime; } public void setCubePc(L1PcInstance CubePc) { this.CubePc = CubePc; } public L1PcInstance CubePc() { return this.CubePc; } public boolean Cube() { return (this.Cube-- <= 0); } public void set_num(int num) { this.num = num; } public int get_num() { return this.num; } public void randomWalk() { tagertClear(); int dir = checkObject(getX(), getY(), getMapId(), _random.nextInt(20)); if (dir != -1) setSleepTime(setDirectionMoveSpeed(dir));  } public void randomWalk(int mul) { tagertClear(); int dir = checkObject(getX(), getY(), getMapId(), _random.nextInt(20)); if (dir != -1) setSleepTime(setDirectionMoveSpeed(dir) * mul);  } public int calcSleepTime(int i) { int sleepTime = i; switch (getMoveState().getMoveSpeed()) { case 1: sleepTime = (int)(sleepTime - sleepTime * 0.25D); break;case 2: sleepTime *= 2; break; }  if (getMoveState().getBraveSpeed() == 1) sleepTime = (int)(sleepTime - sleepTime * 0.25D);  return sleepTime; } public String getEventTitle() { return this.title; } public void setEventTitle(String s) { this.title = s; } public String getEventSubNpc() { return this.subnpc; } public void setEventSubNpc(String s) { this.subnpc = s; } public String getEventSubTitle() { return this.subtitle; } public void setEventSubTitle(String s) { this.subtitle = s; } public String getEventSubActid() { return this.subactid; } public void setEventSubActid(String s) { this.subactid = s; } public boolean is_sub_npc() { return this.is_sub_npc; } public void set_is_sub_npc(boolean b) { this.is_sub_npc = b; } public long get_boss_time() { return this.boss_time; } public void set_boss_time(long l) { this.boss_time = l; } public long get_end_boss_time() { return this.boss_end_time; } public void set_end_boss_time(long l) { this.boss_end_time = l; } public int getBossIngTime() { return this._boss_ing_time; } public void setBossIngTime(int i) { this._boss_ing_time = i; } public String[] getYoil() { return this._yoil; } public void setYoil(String[] yoil) { this._yoil = yoil; } public int get_next_day_index() { return this._next_day_index; } public void set_next_day_index(int _next_day_index) { this._next_day_index = _next_day_index; } public int get_boss_type() { return this.boss_type; } public void set_boss_type(int i) { this.boss_type = i; } public int get_boss_hour() { return this.boss_hour; } public void set_boss_hour(int i) { this.boss_hour = CommonUtil.get_current(i, 0, 24); } public int get_boss_minute() { return this.boss_minute; } public void set_boss_minute(int i) { this.boss_minute = CommonUtil.get_current(i, 0, 60); } public boolean is_boss_tel() { return this.is_boss_tel; } public void set_is_boss_tel(boolean b) { this.is_boss_tel = b; } public int get_boss_tel_x() { return this.boss_tel_x; } public void set_boss_tel_x(int i) { this.boss_tel_x = i; } public int get_boss_tel_y() { return this.boss_tel_y; } public void set_boss_tel_y(int i) { this.boss_tel_y = i; } public int get_boss_tel_m() { return this.boss_tel_m; } public void set_boss_tel_m(int i) { this.boss_tel_m = i; } public int get_boss_tel_rnd() { return this.boss_tel_rnd; } public void set_boss_tel_rnd(int i) { this.boss_tel_rnd = i; } public int get_boss_tel_count() { return this.boss_tel_count; } public void set_boss_tel_count(int i) { this.boss_tel_count = i; } public boolean is_boss_msg() { return this.is_boss_msg; } public void set_is_boss_msg(boolean b) { this.is_boss_msg = b; } public String get_boss_msg() { return this.boss_msg; } public void set_boss_msg(String s) { this.boss_msg = s; } public boolean get_boss_yn() { return this.boss_yn; } public void set_boss_yn(boolean b) { this.boss_yn = b; } public String get_boss_yn_msg() { return this.boss_yn_msg; } public void set_boss_yn_msg(String s) { this.boss_yn_msg = s; } public boolean is_boss_effect() { return this.is_boss_effect; } public void set_is_boss_effect(boolean b) { this.is_boss_effect = b; } public int get_boss_effect() { return this.boss_effect; } public void set_boss_effect(int i) { this.boss_effect = i; } public boolean is_boss_alarm() { return this.is_boss_alarm; } public void set_is_boss_alarm(boolean b) { this.is_boss_alarm = b; } public void setEventTimeTemp(EventTimeTemp temp) { this._eventTimeTemp = temp; } public EventTimeTemp getEventtimeTemp() { return this._eventTimeTemp; } public void deleteRe() { this._destroyed = true; getMap().setPassable((Point)getLocation(), true); if (getInventory() != null) getInventory().clearItems();  allTargetClear(); this._master = null; L1World.getInstance().removeVisibleObject((L1Object)this); L1World.getInstance().removeObject((L1Object)this); List<L1PcInstance> players = L1World.getInstance().getRecognizePlayer((L1Object)this); if (players.size() > 0) { S_RemoveObject s_deleteNewObject = new S_RemoveObject((L1Object)this); for (L1PcInstance pc : players) { if (pc != null) { pc.removeKnownObject((L1Object)this); pc.sendPackets((ServerBasePacket)s_deleteNewObject); }  }  }  removeAllKnownObjects(); } public boolean isPassObject() { return this._isPassObject; } public void setPassObject(boolean b) { this._isPassObject = b; } public long getExplosionTime() { return this._explosion_remain_time; } public void setExplosionTime(long l) { this._explosion_remain_time = l; } public void startExplosionTime(long l) { setExplosionTime(l); GeneralThreadPool.getInstance().schedule(new ExplosionCounter(this), 1000L); } public class ExplosionCounter implements Runnable {
    private L1NpcInstance _npc; private boolean _isSending; public ExplosionCounter(L1NpcInstance npc) { this._npc = npc; this._isSending = false; } public void run() { try { L1NpcInstance.this._explosion_remain_time = L1NpcInstance.this._explosion_remain_time - 1000L; if (!this._isSending && L1NpcInstance.this._explosion_remain_time <= 30000L) { this._isSending = true; Broadcaster.broadcastPacket(this._npc, (ServerBasePacket)S_ExplosionNoti.get((L1Object)this._npc, L1NpcInstance.this._explosion_remain_time), true); }  } catch (Exception exception) {  } finally { if (L1NpcInstance.this._explosion_remain_time <= 0L || L1NpcInstance.this.isDead()) { L1NpcInstance.this._explosion_remain_time = 0L; } else { GeneralThreadPool.getInstance().schedule(this, 1000L); }  }  } } public String getShopName() { return this._shopName; } public void setShopName(String name) { this._shopName = name; } public boolean isMovable() { return this._npcTemplate.isMovable(); } public boolean isActionable() { return this._npcTemplate.isActionable(); } public boolean isDynamic() { return this._npcTemplate.isDynamic(); } private static int _instanceType = -1; public int _delete_delay; public int _mapnum; public int getL1Type() { return (_instanceType == -1) ? (_instanceType = super.getL1Type() | 0x8) : _instanceType; } public void NpcDie() { try { setDeathProcessing(true); setCurrentHp(0); setDead(true); getMap().setPassable((Point)getLocation(), true); setDeathProcessing(false); set_exp(0L); setKarma(0); setLawful(0); allTargetClear(); deleteMe2(); } catch (Exception exception) {} } public void set_delete_delay(int delay) { this._delete_delay = delay; }
  public int get_delete_delay() { return this._delete_delay; }
  public int get_mapnum() { return this._mapnum; }


  protected void updateObject() {}
}


