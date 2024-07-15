package l1j.server.server.model.Instance;

import static l1j.server.server.model.item.L1ItemId.B_POTION_OF_GREATER_HASTE_SELF;
import static l1j.server.server.model.item.L1ItemId.B_POTION_OF_HASTE_SELF;
import static l1j.server.server.model.item.L1ItemId.POTION_OF_EXTRA_HEALING;
import static l1j.server.server.model.item.L1ItemId.POTION_OF_GREATER_HASTE_SELF;
import static l1j.server.server.model.item.L1ItemId.POTION_OF_GREATER_HASTE_SELF1;
import static l1j.server.server.model.item.L1ItemId.POTION_OF_GREATER_HEALING;
import static l1j.server.server.model.item.L1ItemId.POTION_OF_HASTE_SELF;
import static l1j.server.server.model.item.L1ItemId.POTION_OF_HEALING;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.PortableServer._ServantActivatorStub;

import l1j.server.Config;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJBotSystem.MJBotType;
import l1j.server.MJCompanion.Instance.MJCompanionInstance;
import l1j.server.MJEffectSystem.MJEffectModel;
import l1j.server.MJEffectSystem.Loader.MJEffectModelLoader;
import l1j.server.MJPassiveSkill.MJPassiveID;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Interface.MJMonsterDeathHandler;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_WORLD_PUT_OBJECT_NOTI;

import l1j.server.MJTemplate.Spawn.MJSpawnInfo;
import l1j.server.MJWebServer.Dispatcher.my.service.mapview.MJMyMapViewService;
import l1j.server.NpcAstar.AStar;
import l1j.server.NpcAstar.Node;
import l1j.server.NpcAstar.NpcAStar;
import l1j.server.NpcAstar.World;
import l1j.server.server.ActionCodes;
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
import l1j.server.server.model.Instance.L1DoorInstance.DoorTimer;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
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

public class L1NpcInstance extends L1Character {
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


  public long NpcDeleteTime = 0;

  // private static final long DELETE_TIME = 40000L; // 怪物刪除時間40秒原版
  private L1Npc _npcTemplate;

  private L1Spawn _spawn;

  private MJSpawnInfo m_spawn_ex;

  private int _spawnNumber;
  private int _petcost;

  protected L1Inventory _inventory = new L1Inventory();
  private L1MobSkillUse mobSkill;
  private static Random _random = new Random(System.nanoTime());


  private boolean firstFound = true;
//	private boolean firstRun = true;

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
  protected List<L1ItemInstance> _targetItemList = new ArrayList<L1ItemInstance>();
  public L1Character _target = null;
  protected L1ItemInstance _targetItem = null;
  protected L1Character _master = null;
  private boolean _deathProcessing = false;
  private L1MobGroupInfo _mobGroupInfo = null;
  private int _mobGroupId = 0;
  private int CubeTime; // 方塊時間
  private L1PcInstance CubePc; // 方塊使用者
  private int Cube = 20;
  private int num; /* 與增強相關 */


  private DeleteTimer _deleteTask;
  private ScheduledFuture<?> _future = null;

  private Map<Integer, Integer> _digestItems;
  public boolean _digestItemRunning = false;

  private boolean _發綑狀態 = false;

  public boolean 發綑狀態() {
    return _發綑狀態;
  }

  public void set發綑狀態(boolean flag) {
    _發綑狀態 = flag;
  }

  public static final int MOVE_TYPE_NORMAL = 0;
  public static final int MOVE_TYPE_ASTAR = 1;
  public static final int MOVE_TYPE_ASTAR_FAIL = 2;

  protected int _moveType = MOVE_TYPE_NORMAL;

  public NpcAStar _aStar;

  public L1Character ActiveTarget = null;
  public final HashSet<Integer> backingTargets = new HashSet<>();

  public boolean STATUS_Escape = false;

  public boolean Escape = false;

  private static Logger _log = Logger.getLogger(L1NpcInstance.class.getName());

  private int mEmptyWalkableCount;

  public L1NpcInstance(L1Npc template) {
    mEmptyWalkableCount = 0;
    setStatus(0);
    setMoveSpeed(0);
    setDead(false);
    setRespawn(false);

//		_aStar = new NpcAStar(getMap(), new Point(0, 0));

    if (template != null) {
      if (template.getUseAction() ==1 || template.getUseAction() ==2) {
        iPath = new int[51][2];
        aStar = new AStar();
        aStar.setCha(this);
      }

      setting_template(template);
    }
  }

  private String Spawn_Location;

  public String getSpawnLocation() {// 哈丁系統
    return Spawn_Location;
  }

  public void setSpawnLocation(String st) {
    Spawn_Location = st;
  }

  private double calcRandomVal(int seed, int ranval, double rate) {
    return rate * (ranval - seed);
  }

  protected void setting_template(L1Npc template) {
    _npcTemplate = template;
    double rate = 0;
    double diff = 0;

    setName(template.get_name());
    setNameId(template.get_nameid());

    int randomlevel = 0;
    int level = template.get_level();
    if (template.get_randomlevel() != 0) {
      randomlevel = _random.nextInt(template.get_randomlevel() - level + 1);
      diff = template.get_randomlevel() - level;
      rate = randomlevel / diff;
      randomlevel += template.get_level();
      level = randomlevel;
    }
    setLevel(level);

    int ac = template.get_ac();
    if (template.get_randomac() != 0) {
      ac = (int) (ac + calcRandomVal(ac, template.get_randomac(), rate));
    }
    this.ac.setAc(ac);

    if (template.get_randomlevel() == 0) {
      ability.setStr(template.get_str());
      ability.setCon(template.get_con());
      ability.setDex(template.get_dex());
      ability.setInt(template.get_int());
      ability.setWis(template.get_wis());
      resistance.setBaseMr(template.get_mr());
    } else {
      ability.setStr((byte) Math.min(template.get_str() + diff, 127));
      ability.setCon((byte) Math.min(template.get_con() + diff, 127));
      ability.setDex((byte) Math.min(template.get_dex() + diff, 127));
      ability.setInt((byte) Math.min(template.get_int() + diff, 127));
      ability.setWis((byte) Math.min(template.get_wis() + diff, 127));
      resistance.setBaseMr((byte) Math.min(template.get_mr() + diff, 127));

      addHitup((int) diff * 2);
      addDmgup((int) diff * 2);
    }

    int hp = template.get_hp();
    if (template.get_randomhp() != 0) {
      hp = (int) (hp + calcRandomVal(hp, template.get_randomhp(), rate));
    }
    setMaxHp(hp);
    setCurrentHp(getMaxHp());

    int mp = template.get_mp();
    if (template.get_randommp() != 0) {
      mp = (int) (mp + calcRandomVal(mp, template.get_randommp(), rate));
    }
    setMaxMp(mp);
    setCurrentMp(mp);

    setAgro(template.is_agro());
    setAgrocoi(template.is_agrocoi());
    setAgrososc(template.is_agrososc());
    setCurrentSprite(template.get_gfxid());

    if (template.get_randomexp() == 0) {
      // 根據等級設定經驗值
      if (template.get_exp() < 127) {
        set_exp(template.get_level());
      } else {
        set_exp(template.get_exp());
      }
      // setExp(template.get_exp());
    } else {
      // 根據等級設定經驗值
      if (template.get_exp() < 127) {
        set_exp(template.get_level() + template.get_randomexp() + randomlevel);
      } else {
        set_exp(template.get_randomexp() + randomlevel);
      }
      // setExp(template.get_randomexp() + randomlevel);
    }

// 原始碼
    /*
     * if (template.get_randomexp() == 0) { set_exp(template.get_exp()); } else {
     * set_exp(template.get_randomexp() + randomlevel); }
     */

    int lawful = template.get_lawful();
    if (template.get_randomlawful() != 0) {
      lawful = (int) (lawful + calcRandomVal(lawful, template.get_randomlawful(), rate));
    }
    setLawful(lawful);
    setTempLawful(lawful);

    setPickupItem(template.is_picupitem());
    if (template.get_digestitem() > 0) {
      _digestItems = new HashMap<Integer, Integer>();
    }
    setKarma(template.getKarma());
    setLightSize(template.getLightSize());

    mobSkill = new L1MobSkillUse(this);
  }

  class NpcAI implements Runnable {
    public void start() {
      setAiRunning(true);
      GeneralThreadPool.getInstance().schedule(NpcAI.this, 0);
    }

    private void stop() {
      if (mobSkill != null) {
        try {
          mobSkill.resetAllSkillUseCount();
        } catch (Exception e) {
        }
      }
      GeneralThreadPool.getInstance().schedule(new DeathSyncTimer(), 0);
    }

    private void schedule(long delay) {
      GeneralThreadPool.getInstance().schedule(NpcAI.this, delay);
    }

    @Override
    public void run() {
      try {
        synchronized (synchObject) {
          // 如果未繼續運行，停止並返回
          if (notContinued()) {
            stop();
            return;
          }
          // 如果有癱瘓時間，計劃任務並重置癱瘓時間
          if (0 < _paralysisTime) {
            schedule(_paralysisTime);
            _paralysisTime = 0;
            setParalyzed(false);
            return;
            // 如果處於癱瘓、睡眠狀態或有凍結技能效果，延遲200毫秒後再運行
          } else if (isParalyzed() || isSleeped() || hasSkillEffect(L1SkillId.STATUS_FREEZE)) {
            schedule(200);
            return;
            // 如果NPC ID為707026，檢查當前HP百分比
          } else if (getNpcId() == 707026) {
            int p = (int) (((double) getCurrentHp() / (double) getMaxHp()) * 100D);
            // 如果HP百分比大於90，延遲200毫秒後再運行
            if (p > 90) {
              schedule(200);
              return;
            }
          }
          // 更新對象狀態
          updateObject();
          // 如果AI處理未完成，計劃休眠時間
          if (!AIProcess()) {
            schedule(getSleepTime());
            return;
          }
        }
        stop();
      } catch (Exception e) {
        // 輸出NPC ID和異常信息
        System.out.println("NPC ID : " + getNpcTemplate().get_npcId());
        _log.log(Level.WARNING, "NpcAI中發生了異常。", e);
      }
    }

    private boolean notContinued() {
      return _destroyed || isDead() || getCurrentHp() <= 0 || getHiddenStatus() != HIDDEN_STATUS_NONE;
    }
  }

  protected void startAI() {
    new NpcAI().start();
  }

  class DeathSyncTimer implements Runnable {
    private void schedule(long delay) {
      GeneralThreadPool.getInstance().schedule(DeathSyncTimer.this, delay);
    }

    @Override
    public void run() {
      if (isDeathProcessing()) {
        schedule(getSleepTime());
        return;
      }
      allTargetClear();
      setAiRunning(false);
    }
  }

  public void 몬스터Teleport() {
    int lvl = this.getLevel();
    if (getMovementDistance() <= 0) {
      if (Config.ServerAdSetting.NpcMaxYN) {
        if (lvl >= Config.ServerAdSetting.NpcMax) {
          if (this instanceof L1MonsterInstance) {
            if (getLocation().getTileLineDistance(new Point(getHomeX(), getHomeY())) > Config.ServerAdSetting.NpcLocation) {
              npcInitialize();
            }
          }
        }
      }
    }
  }

  private L1NpcInstance _doppels;
  private L1Character _attacker;
  private L1Character _defender;

  public void DoppelSkillCheck(L1NpcInstance doppel, L1Character attacker, L1Character defender) {
    _doppels = doppel;
    _attacker = attacker;
    _defender = defender;
    return;
  }

  private static void delenpc(int npcid) {
    L1NpcInstance npc = null;
    for (L1Object object : L1World.getInstance().getObject()) {
      if (object instanceof L1NpcInstance) {
        npc = (L1NpcInstance) object;
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

    if (_doppels != null) {
      if (_defender.getCurrentHp() == 0 || _defender.isDead()
              || !_doppels.getMap().isInMap(_defender.getLocation())
              || !_doppels.getMap().isInMap(_attacker.getLocation())) {
        delenpc(_doppels.getNpcId());
      }
    }

    // TODO 如果在安全區，刪除怪物（防止召喚或馴服時被濫用）
    // 如果這個對象是 L1SummonInstance 的實例
    /*
     * if (this instanceof L1SummonInstance) {
     * if (this.getMapId() == 4 && getMap().isSafetyZone(this.getLocation())) {
     * deleteMe();
     * }
     * }
     */

    if (this instanceof L1MerchantInstance) {
      if (this.getNpcId() == 5095) { // 沙漠風暴
        for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
          L1Location newLocation = pc.getLocation().randomLocation(30, true);
          int newX = newLocation.getX();
          int newY = newLocation.getY();
          short mapId = (short) newLocation.getMapId();
          if (pc.getLocation().getTileLineDistance(new Point(this.getLocation())) < 2 && !pc.isDead()) {
            if (pc != null) {
              pc.start_teleport(newX, newY, mapId, pc.getHeading(), 18339, true, false);
            }
          }
        }
      }
    }
  }

    if (this instanceof L1MerchantInstance) {
      if (this.getNpcId() == 7210040) {
        for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
          if (pc.getLocation().getTileLineDistance(new Point(this.getLocation())) < 2 && !pc.isDead()) {
            if (pc != null) {
              Random random1 = new Random();
              int chance = random1.nextInt(100) + 1;
              if (chance < 40) {
                // L1Teleport.teleport(pc, 33445, 33130, (short)
                // 4, pc.getHeading(), true);
                pc.start_teleport(33445, 33130, 4, pc.getHeading(), 18339, true, false);
              } else if (chance < 60) {
                // L1Teleport.teleport(pc, 33428, 33244, (short)
                // 4, pc.getHeading(), true);
                pc.start_teleport(33428, 33244, 4, pc.getHeading(), 18339, true, false);
              } else if (chance < 70) {
                // L1Teleport.teleport(pc, 33474, 33165, (short)
                // 4, pc.getHeading(), true);
                pc.start_teleport(33474, 33165, 4, pc.getHeading(), 18339, true, false);
              } else if (chance < 80) {
                // L1Teleport.teleport(pc, 33495, 33197, (short)
                // 4, pc.getHeading(), true);
                pc.start_teleport(33495, 33197, 4, pc.getHeading(), 18339, true, false);
              }
            }
          }
        }
      }
    }

  setSleepTime(300);

  checkTarget();
  怪物傳送(); // 将 monsterTeleport() 翻译为 怪物傳送()
if (_target == null && _master == null) {
    搜尋目標(); // 将 searchTarget() 翻译为 搜尋目標()
  }

  使用物品(); // 将 onItemUse() 翻译为 使用物品()

if (_target == null) {
    if (是束縛狀態()) { // 将 발묶임상태() 翻译为 是束縛狀態()
      return true;
    }

    backingTargets.clear();
    檢查目標物品(); // 将 checkTargetItem() 翻译为 檢查目標物品()
    if (是拾取物品() && _targetItem == null) { // 将 isPickupItem() 翻译为 是拾取物品()
      搜尋目標物品(); // 将 searchTargetItem() 翻译为 搜尋目標物品()
    }
  }

      if (_targetItem == null) {
        if (noTarget()) {
          return true;
        }
      } else {
        L1Inventory groundInventory = L1World.getInstance().getInventory(_targetItem.getX(), _targetItem.getY(), _targetItem.getMapId());
        if (groundInventory.checkItem(_targetItem.getItemId())) {
          onTargetItem();
        } else {
          _targetItemList.remove(_targetItem);
          _targetItem = null;
          setSleepTime(1000);
          return false;
        }
      }
    } else {
      if (getHiddenStatus() == HIDDEN_STATUS_NONE) {
        onTarget();
      } else {
        return true;
      }
    }

    return false;
  }

  public void onItemUse() {
  }

  public void searchTarget() {
  }

  public boolean checkCondition() {
    return false;
  }

  public void checkTarget() {
    if (_target == null || _target.getMapId() != getMapId() || _target.isDead() || _target.getCurrentHp() <= 0
            || (_target.isInvisble() && !getNpcTemplate().is_agrocoi() && !_hateList.containsKey(_target)) || (_target instanceof L1SummonInstance && ((L1SummonInstance) _target).isDestroyed())
            || (_target instanceof L1PetInstance && ((L1PetInstance) _target).isDestroyed()) || (_target instanceof MJCompanionInstance && ((MJCompanionInstance) _target).isDestroyed())) {

      if (_target != null) {
        tagertClear();
      }

      if (!_hateList.isEmpty()) {
        _target = _hateList.getMaxHateCharacter();
//				_moveType = MOVE_TYPE_NORMAL;
//				_moveType = MOVE_TYPE_ASTAR; //20221226
//				_aStar.resetTryCount(); //20221226
        checkTarget();
      }
    }
  }

  public void delInvis() {
    if (hasSkillEffect(L1SkillId.INVISIBILITY)) {
      killSkillEffectTimer(L1SkillId.INVISIBILITY);
      sendPackets(new S_Invis(getId(), 0));
      broadcastPacket(new S_Invis(getId(), 0));
      broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
    }
    if (hasSkillEffect(L1SkillId.BLIND_HIDING)) {
      killSkillEffectTimer(L1SkillId.BLIND_HIDING);
      sendPackets(new S_Invis(getId(), 0));
      broadcastPacket(new S_Invis(getId(), 0));
      broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
      if (isPassive(MJPassiveID.BLIND_HIDDING_ASSASSIN.toInt())) {
        sendPackets(new S_Invis(getId(), 0));
        broadcastPacket(new S_Invis(getId(), 0));
        broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
        addMoveDelayRate(-50);
      }
    }
  }

  public void checkTargetItem() {
    if (_targetItem == null || _targetItem.getMapId() != getMapId()
            || getLocation().getTileDistance(_targetItem.getLocation()) > 15) {
      if (!_targetItemList.isEmpty()) {
        _targetItem = _targetItemList.get(0);
        _targetItemList.remove(0);
        checkTargetItem();
      } else {
        _targetItem = null;
      }
    }
  }

  protected boolean aStarFollow() {
    Point current = new Point(getX(), getY());
    while (current.isSamePoint(_aStar.getFinalPath().get(0))) {
      _aStar.eraseOneNodeFromFinalPath();
    }
    int dir = current.getHeading(_aStar.getFinalPath().get(0));
    if (dir == -1) {
      return false;
    } else if (getMap().isPassable(current, dir)
            && !isObjectExist(current.getX(), current.getY(), (short) getMapId(), dir)) {
      _aStar.eraseOneNodeFromFinalPath();
      setDirectionMoveSpeed(dir);
      setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
      return true;
    }

    return false;
  }

protected boolean aStarMove() {
// 獲取當前點座標
        Point current = new Point(getX(), getY());

// 當前點與路徑的第一個點相同，移除第一個點
        while (current.isSamePoint(_aStar.getFinalPath().get(0))) {
        _aStar.eraseOneNodeFromFinalPath();
        }

// 獲取從當前點到路徑下一個點的移動方向
        int dir = current.getHeading(_aStar.getFinalPath().get(0));

// 如果方向無效，清除目標（註釋顯示不會進入這個分支）
        if (dir == -1) {
        clearTarget(); // 여기에 들어올 일은 없다.（到這裡不會進入）
        } else if (getMap().isPassable(current, dir)
        && !isObjectExist(current.getX(), current.getY(), (short) getMapId(), dir)) {
        // 如果方向可通行且目標位置沒有其他對象存在，移除當前路徑點，並設置移動方向和速度
        _aStar.eraseOneNodeFromFinalPath();
        setDirectionMoveSpeed(dir);
        setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
        return true;
        } else {
        // 如果路徑不可通行，返回 false
        return false;
        }

// 返回 true 表示移動執行完畢
        return true;
        }

  protected boolean findNextTarget() {
    for (L1Object object : L1World.getInstance().getVisibleObjects(this, 10)) {
      if (object instanceof L1PcInstance) {
        L1Character cha = (L1Character) object;
        if (cha != _target) {
          if (object instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) object;
            if (pc.isGhost()) {
              continue;
            }
            if (pc.isGm()) {
              continue;
            }
          }
          _hateList.add(cha, 0);
          _target = cha;
//					_moveType = MOVE_TYPE_NORMAL;
//					_moveType = MOVE_TYPE_ASTAR;
//					_aStar.resetTryCount();
          return true;
        }
      }
    }

    return false;
  }

  private void doNormalFollow() {
    Point current = new Point(getX(), getY());

    int dir = moveDirection2(_master.getX(), _master.getY(),
            getLocation().getLineDistance(new Point(_master.getX(), _master.getY())));

    if (dir != -1 && getMap().isPassable(current, dir)
            && !isObjectExist(current.getX(), current.getY(), (short) getMapId(), dir)) {
      setDirectionMoveSpeed(dir);
      setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
    } else {
      Point targetPoint = new Point(_master.getX(), _master.getY());
      _aStar.setTarget(getMap(), targetPoint);
      if (_aStar.compute(current) == null) {
        _moveType = MOVE_TYPE_ASTAR_FAIL;
        doAStarFailFollow();
        return;
      } else {
        _moveType = MOVE_TYPE_ASTAR;
        doAStarFollow();
        return;
      }
    }

  }

  private void doAStarFollow() {
    Point current = new Point(getX(), getY());
    Point targetPoint = new Point(_master.getX(), _master.getY());

    if (_aStar.getTarget().getX() != targetPoint.getX() || _aStar.getTarget().getY() != targetPoint.getY()) {
      _aStar.setTarget(getMap(), targetPoint);

      if (_aStar.compute(current) == null) {
        _moveType = MOVE_TYPE_ASTAR_FAIL;
        doAStarFailFollow();
        return;
      }
    } else if (_aStar.getTryCount() >= 10) {
      _moveType = MOVE_TYPE_ASTAR_FAIL;
      doAStarFailFollow();
      return;
    }

    if (!aStarFollow()) {
      if (_aStar.compute(current) == null) {
        _moveType = MOVE_TYPE_ASTAR_FAIL;
        doAStarFailFollow();
        return;
      }
      if (!aStarMove()) {
        setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
      }
    } else {
      setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
    }
  }

  private void doAStarFailFollow() {
    Point current = new Point(getX(), getY());
    Point targetPoint = new Point(_master.getX(), _master.getY());

    if (_aStar.getTarget().getX() != targetPoint.getX() || _aStar.getTarget().getY() != targetPoint.getY()) {
      _aStar.setTarget(getMap(), targetPoint);
    }

    if (_aStar.getTryCount() < 10 && _aStar.compute(current) != null) {
      _moveType = MOVE_TYPE_ASTAR;
      doAStarFollow();
      return;
    }

    int dir = moveDirection3(_target.getX(), _target.getY());

    if (dir != -1 && getMap().isPassable(current, dir) && !isObjectExist(current.getX(), current.getY(), (short) getMapId(), dir)) {
      setDirectionMoveSpeed(dir);
    }

    setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
  }

/*	private void doNormalMove() {
		try {
			Point current = new Point(getX(), getY());

			int dir = moveDirection2(_target.getX(), _target.getY(),
					getLocation().getLineDistance(new Point(_target.getX(), _target.getY())));
			if (dir != -1 && getMap().isPassable(current, dir)
					&& !isObjectExist(current.getX(), current.getY(), (short) getMapId(), dir)) {
				setDirectionMove(dir);
				setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
			} else {
				Point targetPoint = new Point(_target.getX(), _target.getY());
				_aStar.setTarget(getMap(), targetPoint);
				if (_aStar.compute(current) == null || targetPoint == null) {
					_moveType = MOVE_TYPE_ASTAR_FAIL;
					doAStarFailMove();
					return;
				} else {
					_moveType = MOVE_TYPE_ASTAR;
					doAStarMove();
					return;
				}
			}
		} catch (Exception e) {
			// System.out.println("엔피씨에러 : " + this.getNpcId());
		}

	}*/

  public int AStar_Try_Count = 0;

/*	private void doAStarMove() {
		try {
			Point current = new Point(getX(), getY());
			Point targetPoint = new Point(_target.getX(), _target.getY());
			if (_aStar.getTarget().getX() != targetPoint.getX() || _aStar.getTarget().getY() != targetPoint.getY()) {
				_aStar.setTarget(getMap(), targetPoint);
				if (_aStar.compute(current) == null) {
					_moveType = MOVE_TYPE_ASTAR_FAIL;
					doAStarFailMove();
					return;
				}
			} else if (_aStar.getTryCount() >= 20) {//10에서 수정
				_moveType = MOVE_TYPE_ASTAR_FAIL;
				doAStarFailMove();
				return;
			}

			if (!aStarMove()) {
				if (AStar_Try_Count > 20) {//10에서 수정
					backingTargets.add(_target.getId());
					tagertClear();
					// _moveType = MOVE_TYPE_ASTAR_FAIL;
					// doAStarFailMove();
					AStar_Try_Count = 0;
					return;
				}

				if (this instanceof L1MonsterInstance) {
					if (_aStar.compute(current) == null) {
						_moveType = MOVE_TYPE_ASTAR_FAIL;
						doAStarFailMove();
						return;
					}
					if (!aStarMove()) {
						setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
					}
				} else if (this instanceof L1SummonInstance || this instanceof L1FollowerInstance) {
					if (_aStar.compute(current) == null) {
						_moveType = MOVE_TYPE_ASTAR_FAIL;
						doAStarFailMove();
						return;
					}
					if (!aStarMove()) {
						setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
					}
				}
				AStar_Try_Count++;
			} else {
				setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
			}
		} catch (Exception e) {
			// System.out.println("엔피씨에러1 : " + this.getNpcId());
		}
	}
*/
/*	private void doAStarFailMove() {
		try {
			Point current = new Point(getX(), getY());
			Point targetPoint = new Point(_target.getX(), _target.getY());

			if (_aStar.getTarget().getX() != targetPoint.getX() || _aStar.getTarget().getY() != targetPoint.getY()) {
				_aStar.setTarget(getMap(), targetPoint);
			}

			if (this instanceof L1MonsterInstance) {
				if (findNextTarget()) {
					setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
					return;
				}
			}

			int dir = targetDirection(targetPoint.getX(), targetPoint.getY());

//			if (dir != -1 && isExsistCharacterBetweenTarget(dir)) {
//				setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
//				return;
//			}

			dir = moveDirection3(_target.getX(), _target.getY());

			if (dir != -1 && getMap().isPassable(current, dir) && !isExsistCharacterBetweenTarget(dir)) {
				setDirectionMove(dir);
			}

			if (dir == -1) {
				backingTargets.add(_target.getId());
				_hateList.remove(_target);
				if (_target != null && _target.equals(_target)) {
					_target = null;
					_moveType = MOVE_TYPE_ASTAR;
					doAStarMove();
					return;
				}
			}

			setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
		} catch (Exception e) {
			// System.out.println("엔피씨에러2 : " + this.getNpcId());
		}
	}*/

  public boolean onAdditionalNpcAi() {
    return false;
  }
  private boolean isTeleportAction;
  public void teleportDmgAction(){
    L1Location newLoc = getLocation().randomLocation(3, 6, false);
    teleport(newLoc.getX(), newLoc.getY(), getMoveState().getHeading());
    setCurrentMp(getCurrentMp() - 10);
  }
  public int moveDirectionIndun(int mapid, int x, int y) {
    return moveDirectionIndun(mapid, x, y, getLocation().getLineDistance(new Point(x, y)));
  }

  public int moveDirectionIndun(int mapid, int x, int y, double d) {
    int dir = 0;
    int calcx = getX() - x;
    int calcy = getY() - y;

    if(this.getMapId() != mapid || Math.abs(calcx) > 30 || Math.abs(calcy) > 30){
      allTargetClear();
      return -1;
    }
    if((hasSkillEffect(L1SkillId.DARKNESS) || hasSkillEffect(L1SkillId.CURSE_BLIND)) && d >= 2D)return -1;
    else if(d > 30D)return -1;
    else if(d > courceRange)dir = targetDirection(x, y);
    else{
      dir = _serchCource(x, y, mapid);
      if(dir == -1)dir = targetDirection(x, y);
    }
    return dir;
  }


  public L1Character _backtarget;
  protected int cnt = 0;
  public void onTarget() {
     try {
        int targetx = _target.getX();
        int targety = _target.getY();
        setActived(true);
        _targetItemList.clear();
        _targetItem = null;
        L1Character target = _target;
        if (target == null) return;
        if (!isActionable() && !isMovable()) {
          return;
        }
        // 如果攻擊速度和移動速度都是0，則返回
        // if (getAtkspeed() == 0 && getPassispeed() == 0) return;
        if ((hasSkillEffect(L1SkillId.DARKNESS) || hasSkillEffect(L1SkillId.CURSE_BLIND)) && getLocation().getTileLineDistance(target.getLocation()) > 1) {
          tagertClear();
          return;
        }
        if (getNpcId() == 8513) return; // 검은 기사단 요리사 (黑暗騎士團廚師)

        if (!isActionable() && isMovable()) {
          if (getLocation().getTileLineDistance(target.getLocation()) > 15) tagertClear();
          else {
            int dir = targetReverseDirection(targetx, targety);
            dir = checkObject(getX(), getY(), getMapId(), dir);
            if (dir == -1) return;
            // 設定方向移動
        setSleepTime(setDirectionMoveSpeed(dir));
          }
        } else {
          if (onAdditionalNpcAi()) return;

          if (STATUS_Escape) { // 도망 몬스터 (逃跑怪物)
            L1Location newLoc = getLocation().randomLocation(10, 20, false);
            int dir = moveDirection(getMapId(), newLoc.getX(), newLoc.getY());
            dir = checkObject(getX(), getY(), getMapId(), dir);
            if (dir == -1) {
              STATUS_Escape = false;
            } else {
              // 設定方向移動
        setSleepTime(setDirectionMoveSpeed(dir));
            }
            newLoc = null;
            return;
          }

          boolean isSkillUse = false;
          isSkillUse = mobSkill.skillUse(target);
          if (isSkillUse) {
            setSleepTime(mobSkill.getSleepTime() + 500);
            // 設定睡眠時間
        return;
          }

          if (getNpcId() == 45164 && getMapId() == 9 && target.getMapId() == 9 && (targetx == 32663 && targety == 33149 || targetx == 32669 && targety == 33158 || targetx == 32669 && targety == 33161) && getLocation().getTileDistance(target.getLocation()) <= 2) {
            getMoveState().setHeading(targetDirection(targetx, targety));
            attackTarget(target);
          } else if (isAttackPosition(target, getNpcTemplate().get_ranged())) {
            getMoveState().setHeading(targetDirection(targetx, targety));
            attackTarget(target);
          } else {
            if (isMovable()) {
              int distance = getLocation().getTileDistance(target.getLocation());
              if (target.getMapId() != getMapId() || distance > 30) {
                tagertClear();
                return;
              }

              if (firstFound && getNpcTemplate().is_teleport() && distance > 3 && distance < 15 && nearTeleport(targetx, targety)) {
                firstFound = false;
                return;
              }
              if (getNpcTemplate().is_teleport() && 20 > CommonUtil.random(100) && getCurrentMp() >= 10 && distance > 6 && distance < 15) {
                if (nearTeleport(targetx, targety)) return;
              } else if (isTeleportAction && getNpcTemplate().is_teleport() && getCurrentMp() >= 10) {
                isTeleportAction = false;
                teleportDmgAction();
                return;
              }

              if ((getNpcId() == 7800007 || getNpcId() == 7800064) && (getX() > 32790 && getX() < 32808 && getY() > 32855 && getY() < 32872)) { // 글루디오 연구실 (格魯迪歐研究所)
                return;
              }

              int dir = moveDirection(target.getMapId(), targetx, targety);
              dir = checkObject(getX(), getY(), getMapId(), dir);

              if (getNpcId() == 7800007 || getNpcId() == 7800064
        || (getNpcId() >= 7800010 && getNpcId() <= 7800014)
        || (getNpcId() >= 7800020 && getNpcId() <= 7800021)
        || (getNpcId() >= 7800030 && getNpcId() <= 7800031)
        || (getNpcId() >= 7800040 && getNpcId() <= 7800041)
        || (getNpcId() >= 7800050 && getNpcId() <= 7800051)
        || (getNpcId() >= 7800054 && getNpcId() <= 7800055)
        || (getNpcId() >= 7800060 && getNpcId() <= 7800063)) { // 글루디오 연구실 (格魯迪歐研究所)
                if (!(getX() > 32790 && getX() < 32808 && getY() > 32855 && getY() < 32872)) {
                  dir = moveDirectionIndun(target.getMapId(), targetx, targety);
                  // 設定方向移動
        setSleepTime(setDirectionMoveSpeed(dir));
        return;
                }
              }

              // 유니콘 사원 (獨角獸神殿)
        if (dir == -1 && ((getNpcId() >= 7200008 && getNpcId() <= 7200020) || getNpcId() == 7200055 || getNpcId() == 7200056 || (getNpcId() >= 7200030 && getNpcId() <= 7200041))) {
          L1Map m = L1WorldMap.getInstance().getMap(getMapId());
          if (m.getOriginalTile(getX(), getY()) == 12) {
            getMoveState().setHeading(targetDirection(targetx, targety));
            dir = getMoveState().getHeading();
          }
        }

        if (dir == -1) {
          cnt++;
          if (cnt > 5) {
            _backtarget = target;
            tagertClear();
            cnt = 0;
          }
        } else if (발묶임상태()) { // 發生狀態
          return;
        } else {
          boolean door =
        java
        複製程式碼
        boolean door = World.瞬間移動(getX(), getY(), getMapId(), calcheading(this, targetx, targety)); // 瞬間移動
        boolean tail = World.isThroughObject(getX(), getY(), getMapId(), dir);

        if ((getNpcId() >= 7200008 && getNpcId() <= 7200020) || getNpcId() == 7200055 || getNpcId() == 7200056 || (getNpcId() >= 7200030 && getNpcId() <= 7200041)) { // 유니콘 사원 (獨角獸神殿)
          door = false;
          tail = true;
        }

        if (door || !tail) {
          cnt++;
          if (cnt > 5) {
            _backtarget = target;
            tagertClear();
            cnt = 0;
          }
          return;
        }
        // 設定方向移動
        setSleepTime(setDirectionMoveSpeed(dir));
        }
            } else tagertClear();
          }
        }
      } catch (Exception e) {
        // System.out.println("NPC錯誤3  : " + this.getNpcId());
      }
   }

  /*	public void onTarget() {
		mEmptyWalkableCount = 0;
		setActived(true);
		_targetItemList.clear();
		_targetItem = null;
		L1Character target = _target;
		if (_target == null)
			return;
		if (hasSkillEffect(L1SkillId.DARKNESS) && getLocation().getTileLineDistance(target.getLocation()) > 1) {
			tagertClear();
			return;
		}
		if (getNpcId() >= 7320012 && getNpcId() <= 7320017) {
			tagertClear();
			return;
		}
		if (target == null)
			return;

		if (STATUS_Escape) {
			int escapeDistance = 15;
			if (getLocation().getTileLineDistance(target.getLocation()) > escapeDistance) {
				tagertClear();
			} else {
				int dir = targetReverseDirection(target.getX(), target.getY());
				dir = checkObject(getX(), getY(), getMapId(), dir);
				setSleepTime(setDirectionMove(dir));
			}
			return;
		}

		if (!isActionable() && isMovable()) {
			int escapeDistance = 15;
			if (getLocation().getTileLineDistance(target.getLocation()) > escapeDistance) {
				tagertClear();
			} else {
				int dir = targetReverseDirection(target.getX(), target.getY());
				dir = checkObject(getX(), getY(), getMapId(), dir);
				setSleepTime(setDirectionMove(dir));
			}
		} else {
			// 광역 이펙트 스킬
			MJEffectModel model = MJEffectModelLoader.getInstance().get(getNpcId());
			if (model != null) {
				if (model.isEffectSet()) {
					model.runEffect(this);
					if (mobSkill != null) {
						setSleepTime(mobSkill.getSleepTime() + 500);
					} else {
						setSleepTime(500);
					}
					return;
				}
			}

			// todo 不要碰原來的怪物技能
			boolean isSkillUse = false;
			if (mobSkill != null) {
				try {
					if (target != null) {
						isSkillUse = mobSkill.skillUse(target);
						if (isSkillUse == true) {
							setSleepTime(mobSkill.getSleepTime());
							return;
						}
					}
				} catch (Exception e) {
					System.out.println("[isSkillUse]: " + isSkillUse);
					System.out.println("[mobSkill]: " + mobSkill);
					System.out.println("[mobSkill.skillUse]: " + mobSkill.skillUse(target));
					System.out.println("[mobSkill.getSleepTime()]: " + mobSkill.getSleepTime());
					e.printStackTrace();
				}
			}

			if (isAttackPosition(target, getNpcTemplate().get_ranged())) {
				setHeading(targetDirection(target.getX(), target.getY()));
				attackTarget(target);
			} else {
				if (isMovable()) {
					int distance = getLocation().getTileDistance(target.getLocation());
					if (target.getMapId() != getMapId() && distance > 60) {
						tagertClear();
						return;
					}
					if (firstFound == true && getNpcTemplate().is_teleport() && distance > 3 && distance < 15) {
						if (nearTeleport(target.getX(), target.getY()) == true) {
							firstFound = false;
							return;
						}
					}

					if (getNpcTemplate().is_teleport() && 20 > _random.nextInt(100) && getCurrentMp() >= 10 && distance > 6 && distance < 15) {
						if (nearTeleport(target.getX(), target.getY()) == true) {
							return;
						}
					}

					if (발묶임상태()) {
						return;
					}

					switch (_moveType) {
					case MOVE_TYPE_NORMAL:
						doNormalMove();
						break;

					case MOVE_TYPE_ASTAR:
						doAStarMove();
						break;

					case MOVE_TYPE_ASTAR_FAIL:
						doAStarFailMove();
						break;
					}

				} else {
					tagertClear();
				}
			}
		}
	}
*/
  public void die(L1Character lastAttacker) {
    if (this != null) {
      setDeathProcessing(true);
      setCurrentHp(0);
      setDead(true);
      setStatus(ActionCodes.ACTION_Die);
      getMap().setPassable(getLocation(), true);
      Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);
      startChat(CHAT_TIMING_DEAD);
      setDeathProcessing(false);
      set_exp(0);
      setKarma(0);
      setLawful(0);
      allTargetClear();
      startDeleteTimer();
    }
  }

  public void setHate(L1Character cha, int hate) {
    if (cha != null && cha.getId() != getId()) {
      if (cha.getAI() == null || (cha.getAI().getBotType() != MJBotType.REDKNIGHT
              && cha.getAI().getBotType() != MJBotType.PROTECTOR)) {
        if (!isFirstAttack() && hate != 0) {
          // hate += 20;
          hate += getMaxHp() / 10;
          setFirstAttack(true);
        }

        _hateList.add(cha, hate);
        _dropHateList.add(cha, hate);
        _target = _hateList.getMaxHateCharacter();
        _moveType = MOVE_TYPE_ASTAR;
//				_moveType = MOVE_TYPE_NORMAL;
        checkTarget();
      }
    }
  }

  public void setLink(L1Character cha) {
  }

  public void serchLink(L1PcInstance targetPlayer, int family) {
    Collection<L1Object> targetKnownObjects = targetPlayer.getKnownObjects();
    L1NpcInstance npc = null;
    L1MobGroupInfo mobGroupInfo = null;
    for (Object knownObject : targetKnownObjects) {
      if (knownObject == null)
        continue;
      if (knownObject instanceof L1NpcInstance) {
        npc = (L1NpcInstance) knownObject;
        if (npc.getNpcTemplate() != null && npc.getNpcTemplate().get_agrofamily() > 0) {
          if (npc.getNpcTemplate().get_agrofamily() == 1) {
            if (npc.getNpcTemplate().get_family() == family) {
              npc.setLink(targetPlayer);
            }
          } else {
            npc.setLink(targetPlayer);
          }
        }
        mobGroupInfo = getMobGroupInfo();
        if (mobGroupInfo != null) {
          if (getMobGroupId() != 0 && getMobGroupId() == npc.getMobGroupId()) {
            npc.setLink(targetPlayer);
          }
        }
      }
    }
  }

  public void attackTarget(L1Character target) {
     if (target == null)
        return;
      if (target instanceof L1PcInstance) {
        L1PcInstance player = (L1PcInstance) target;
        if (player.get_teleport()) {
          return;
        }
       } else if (target instanceof L1PetInstance) {
        L1PetInstance pet = (L1PetInstance) target;
        L1Character cha = pet.getMaster();
        if (cha instanceof L1PcInstance) {
          L1PcInstance player = (L1PcInstance) cha;
          if (player.get_teleport()) {
            return;
          }
        }
       } else if (target instanceof MJCompanionInstance) {
        MJCompanionInstance companion = (MJCompanionInstance) target;
        if (companion.get_master() == null || companion.get_master().get_teleport())
          return;
       } else if (target instanceof L1SummonInstance) {
        L1SummonInstance summon = (L1SummonInstance) target;
        L1Character cha = summon.getMaster();
        if (cha instanceof L1PcInstance) {
          L1PcInstance player = (L1PcInstance) cha;
          if (player.get_teleport()) {
            return;
          }
        }
       }
       if (this instanceof L1PetInstance) {
         L1PetInstance pet = (L1PetInstance) this;
         L1Character cha = pet.getMaster();
         if (cha instanceof L1PcInstance) {
           L1PcInstance player = (L1PcInstance) cha;
           if (player.get_teleport()) {
             return;
           }
         }
        } else if (this instanceof MJCompanionInstance) {
         MJCompanionInstance companion = (MJCompanionInstance) this;
         if (companion.get_master() == null || companion.get_master().get_teleport())
           return;

        } else if (this instanceof L1SummonInstance) {
         L1SummonInstance summon = (L1SummonInstance) this;
         L1Character cha = summon.getMaster();
         if (cha instanceof L1PcInstance) {
           L1PcInstance player = (L1PcInstance) cha;
           if (player.get_teleport()) {
             return;
           }
         }
        }
        if (target instanceof L1NpcInstance) {
          L1NpcInstance npc = (L1NpcInstance) target;
          if (npc.getHiddenStatus() != HIDDEN_STATUS_NONE) {
            allTargetClear();
            return;
          }
        }

        boolean isCounterBarrier = false;
        boolean isMortalBody = false;
        boolean isConqure = false;
        L1Attack attack = new L1Attack(this, target);
        if (attack.calcHit()) {
          if (target.hasSkillEffect(L1SkillId.COUNTER_BARRIER)) {
            L1Magic magic = new L1Magic(target, this);
            boolean isProbability = magic.calcProbabilityMagic(L1SkillId.COUNTER_BARRIER);
            boolean isShortDistance = attack.isShortDistance();
            if (isProbability && isShortDistance) {
              if (target.isPassive(MJPassiveID.COUNTER_BARRIER_MASTER.toInt())) {
                int hp_bonus = target.getCurrentHp() + (target.getAbility().getTotalCon() / 2);
                isCounterBarrier = true;
                target.setCurrentHp(hp_bonus);
              } else {
                if (target != null && target.isPassive(MJPassiveID.PARADOX.toInt()) && MJRnd.isWinning(1000000, 500000)) {
                  target.send_effect(18518);
                  isCounterBarrier = false;
                } else {
                  isCounterBarrier = true;
                }
              }
            }
          } else if (target.hasSkillEffect(L1SkillId.MORTAL_BODY)) {
            L1Magic magic = new L1Magic(target, this);
            boolean isProbability = magic.calcProbabilityMagic(L1SkillId.MORTAL_BODY);
            boolean isShortDistance1 = attack.isShortDistance1();
            if (isProbability && isShortDistance1) {
              isMortalBody = true;
            }
          } else if (target.isPassive(MJPassiveID.CONQUEROR.toInt())) { //확률 작업해야됨 (需要機率處理)
            L1Magic magic = new L1Magic(target, this);
            boolean isProbability = magic.calcProbabilityMagic(L1SkillId.CONQUEROR);
            boolean isShortDistance = attack.isShortDistance();
            if (isShortDistance && isProbability) {
              isConqure = true;
            }
          }

          if (!isCounterBarrier && !isMortalBody && !isConqure) {
            attack.calcDamage();

            if (target instanceof L1PcInstance) {
              applySpecialEnchant((L1PcInstance) target);
            }
          }
        }
        if (isCounterBarrier) {
          attack.actionCounterBarrier();
          attack.commitCounterBarrier();
        } else if (isMortalBody) {
          attack.calcDamage();
          attack.actionMortalBody();
          attack.commitMortalBody();
          attack.commit();
        } else if (isConqure) {
          attack.commitConqure();
          attack.actionConqure();
        } else {
          attack.action();
          attack.commit();
        }
        setSleepTime(getCurrentSpriteInterval(EActionCodes.attack));
   }



   private void applySpecialEnchant(L1PcInstance pc) {
     if (pc.getWeapon() == null || !pc.getWeapon().isSpecialEnchantable()) {
        return;
      }


      for (int i = 1; i <= 3; ++i) {
        int specialEnchant = pc.getWeapon().getSpecialEnchant(i);

        if (specialEnchant == 0) {
          break;
        }

        if (_random.nextInt(100) >= 1) {
          continue;
        }

        boolean success = true;

        switch (specialEnchant) {
          // 여기 각 성능별 처리 (在這裡處理各種性能)
        case L1ItemInstance.CHAOS_SPIRIT:
          success = false;
          break;
          case L1ItemInstance.CORRUPT_SPIRIT:
            new L1SkillUse().handleCommands(pc, L1SkillId.COUNTER_MAGIC, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
            break;
            case L1ItemInstance.ANTARAS_SPIRIT:
              case L1ItemInstance.BALLACAS_SPIRIT:
                case L1ItemInstance.LINDBIOR_SPIRIT:
                  success = false;
                  break;
                  case L1ItemInstance.PAPURION_SPIRIT:
                    if (hasSkillEffect(L1SkillId.STATUS_BRAVE) || hasSkillEffect(L1SkillId.STATUS_HASTE) || hasSkillEffect(L1SkillId.HOLY_WALK) || hasSkillEffect(L1SkillId.MOVING_ACCELERATION)) {
                      killSkillEffectTimer(L1SkillId.STATUS_BRAVE);
                      killSkillEffectTimer(L1SkillId.STATUS_HASTE);
                      killSkillEffectTimer(L1SkillId.HOLY_WALK);
                      killSkillEffectTimer(L1SkillId.MOVING_ACCELERATION);
                      broadcastPacket(new S_SkillBrave(getId(), 0, 0));
                      setBraveSpeed(0);
                      broadcastPacket(new S_SkillHaste(getId(), 0, 0));
                      setMoveSpeed(0);
                    }
                    break;
                    case L1ItemInstance.DEATHKNIGHT_SPIRIT:
                      case L1ItemInstance.BAPPOMAT_SPIRIT:
                        success = false;
                        break;
                        case L1ItemInstance.BALLOG_SPIRIT:
                          break;
                          case L1ItemInstance.ARES_SPIRIT:
                            success = false;
                            break;
        }

        if (success) {
          break;
        }
       }
   }



private int _passable = NOT_PASS;
  private static final int NOT_PASS = 1;
  private static final int OK_PASS = 0;

  public int getPassable() {
    return _passable;
  }
  public void setPassable(int i) {
    if (i == PASS || i == NOT_PASS) {
      _passable = i;
    }
  }
  public boolean _breakable = false;
  public void setBrakeable(boolean b){
    _breakable = b;
  }
  public boolean isBrakeable(){
    return _breakable;
  }

  private boolean _floorOpenStatus;
  public void setFloorOpenStatus(boolean b){
    _floorOpenStatus = b;
  }
  public boolean getFloorOpenStatus(){
    return _floorOpenStatus;
  }

  public void searchTargetItem() {
    ArrayList<L1GroundInventory> gInventorys = new ArrayList<L1GroundInventory>();

    for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
      if (obj == null)
        continue;
      if (obj != null && obj instanceof L1GroundInventory) {
        gInventorys.add((L1GroundInventory) obj);
      }
    }
    if (gInventorys.size() == 0) {
      return;
    }

    int pickupIndex = (int) (Math.random() * gInventorys.size());
    L1GroundInventory inventory = gInventorys.get(pickupIndex);
    for (L1ItemInstance item : inventory.getItems()) {
      if (item == null)
        continue;
      if (getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
        _targetItem = item;
        _targetItemList.add(_targetItem);
      }
    }
  }

  public void searchItemFromAir() {
    // TODO 하피 같은 애들 그리폰같은애들 하늘로 갔다가 안내려와야하는 애들 번호적기
    if (getNpcId() == 14212144 || getNpcId() == 14212134)
      return;

    ArrayList<L1GroundInventory> gInventorys = new ArrayList<L1GroundInventory>();

    for (L1Object obj : L1World.getInstance().getVisibleObjects(this, 4)) {
      if (obj == null)
        continue;
      if (obj != null && obj instanceof L1GroundInventory && ((L1GroundInventory) obj).getSize() > 0) {
        gInventorys.add((L1GroundInventory) obj);
      }
    }
    if (gInventorys.size() == 0) {
      return;
    }

    int pickupIndex = (int) (Math.random() * gInventorys.size());
    L1GroundInventory inventory = gInventorys.get(pickupIndex);
    for (L1ItemInstance item : inventory.getItems()) {
      if (item == null)
        continue;
      if (item.getItem().getType() == 6 // potion
              || item.getItem().getType() == 7) { // food
        if (getHiddenStatus() == HIDDEN_STATUS_FLY) {
          setHiddenStatus(HIDDEN_STATUS_NONE);
          broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Movedown));
          setStatus(0);

          broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
          onNpcAI();
          startChat(CHAT_TIMING_HIDE);
          _targetItem = item;
          _targetItemList.add(_targetItem);
        }
      }
    }
  }

  public static void shuffle(L1Object[] arr) {
    for (int i = arr.length - 1; i > 0; i--) {
      int t = (int) (Math.random() * i);

      L1Object tmp = arr[i];
      arr[i] = arr[t];
      arr[t] = tmp;
    }
  }

  public void onTargetItem() {
    if (getLocation().getTileLineDistance(_targetItem.getLocation()) == 0) {
      pickupTargetItem(_targetItem);
    } else {
      int dir = moveDirection(_targetItem.getMapId(), _targetItem.getX(), _targetItem.getY());
      if (dir == -1) {
        _targetItemList.remove(_targetItem);
        _targetItem = null;
      } else {
        setSleepTime(setDirectionMoveSpeed(dir));
      }
    }
  }

  public void searchItemFromAir() {
     // TODO 列出像哈維和獅鷲獸這樣飛上天空後不會下來的怪物ID
        if (getNpcId() == 14212144 || getNpcId() == 14212134)
          return;

        ArrayList<L1GroundInventory> gInventorys = new ArrayList<L1GroundInventory>();

        for (L1Object obj : L1World.getInstance().getVisibleObjects(this, 4)) {
          if (obj == null)
            continue;
          if (obj instanceof L1GroundInventory && ((L1GroundInventory) obj).getSize() > 0) {
            gInventorys.add((L1GroundInventory) obj);
          }
        }
        if (gInventorys.size() == 0) {
          return;
        }

        int pickupIndex = (int) (Math.random() * gInventorys.size());
        L1GroundInventory inventory = gInventorys.get(pickupIndex);
        for (L1ItemInstance item : inventory.getItems()) {
          if (item == null)
            continue;
          if (item.getItem().getType() == 6 // potion
        || item.getItem().getType() == 7) { // food
            if (getHiddenStatus() == HIDDEN_STATUS_FLY) {
              setHiddenStatus(HIDDEN_STATUS_NONE);
              broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Movedown));
              setStatus(0);

              broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
              onNpcAI();
              startChat(CHAT_TIMING_HIDE);
              _targetItem = item;
              _targetItemList.add(_targetItem);
            }
          }
        }
   }



   public static void shuffle(L1Object[] arr) {
     for (int i = arr.length - 1; i > 0; i--) {
        int t = (int) (Math.random() * i);

        L1Object tmp = arr[i];
        arr[i] = arr[t];
        arr[t] = tmp;
      }
   }



   public void onTargetItem() {
     if (getLocation().getTileLineDistance(_targetItem.getLocation()) == 0) {
        pickupTargetItem(_targetItem);
      } else {
        int dir = moveDirection(_targetItem.getMapId(), _targetItem.getX(), _targetItem.getY());
        if (dir == -1) {
          _targetItemList.remove(_targetItem);
          _targetItem = null;
        } else {
          setSleepTime(setDirectionMoveSpeed(dir));
        }
      }
   }



   public void pickupTargetItem(L1ItemInstance targetItem) {
     if (targetItem == null)
        return;


      targetItem.setGiveItem(true);
       L1Inventory groundInventory = L1World.getInstance().getInventory(targetItem.getX(), targetItem.getY(), targetItem.getMapId());
        L1ItemInstance item = groundInventory.tradeItem(targetItem, targetItem.getCount(), getInventory());
        if (item == null)
          return;

        if (this instanceof MJCompanionInstance) {
          MJCompanionInstance pet = (MJCompanionInstance) this;
          L1PcInstance pc = (L1PcInstance) pet.get_master();

          if (pc != null && ItemMessageTable.getInstance().isPickUpMessage(item.getItemId())) {
            L1ItemMessage temp = ItemMessageTable.getInstance().getPickUpMessage(item.getItemId());
            String men = "";
            if (temp != null) {
              if (temp.getType() == 1) {
                if (temp.isMentuse()) {
                  if (temp.getOption() == 1)
                    men = "" + pc.getName() + "玩家"; // 님께서
        else
          men = "某人"; //  누군가가

        if (temp.getMent() != null) {
          L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(temp.getMent()));
          L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, temp.getMent()));
        } else {
          String locationName = MapsTable.getInstance().getMapName(pc.getMapId());
          String itemName = item.getViewName();
          if (itemName == null)
            itemName = item.getName();
          String message = String.format("" + men + " %s \\fH獲得於 " + locationName + "。", itemName);
          String message2 = String.format("" + men + " %s \\fH獲得於 " + locationName + "。", itemName);
          L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[]{new S_SystemMessage(message), new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message2)});
        }
                }
              }
            }
          }
        }
   }


    light.turnOnOffLight();
    onGetItem(item);
    _targetItemList.remove(_targetItem);
    _targetItem = null;
    setSleepTime(1000);
  }

  /**
   * 距離值擷取.
   */
  public int getDistance(int x, int y, int tx, int ty) {
    long dx = tx - x;
    long dy = ty - y;
    return (int) Math.sqrt(dx * dx + dy * dy);
  }

  /**
   * 거리안에 있다면 참
   */
  public boolean isDistance(int x, int y, int m, int tx, int ty, int tm, int loc) {
    int distance = getDistance(x, y, tx, ty);
    if (loc < distance)
      return false;
    if (m != tm)
      return false;
    return true;
  }

  public boolean noTarget() {
    if (_master != null && _master.getMapId() == getMapId() && getLocation().getTileLineDistance(_master.getLocation()) > 2) {
      int dir = moveDirection(_master.getMapId(), _master.getX(), _master.getY());
//			System.out.println(_master.getName()+"+"+_master.getMapId()+"+"+ _master.getX()+"+"+ _master.getY());
      if (dir != -1) {
        boolean tail = World.isThroughObject(getX(), getY(), getMapId(), dir);
        boolean obj = World.isMapdynamic(aStar.getXY(dir, true) + getX(), aStar.getXY(dir, false) + getY(), getMapId());
        boolean door = World.문이동(getX(), getY(), getMapId(), dir);
        if(this instanceof L1DollInstance) {
          doNormalFollow();
          obj = false;
          return false;
        }
        if(tail && !obj && !door) {
          setDirectionMoveSpeed(dir);
        }
//				setDirectionMoveSpeed(dir);
//				setSleepTime(getCurrentSpriteInterval(EActionCodes.walk));
        setSleepTime(setDirectionMoveSpeed(dir));
      } else {
        return true;
      }


/*			switch (_moveType) {
			case MOVE_TYPE_NORMAL:
				doNormalFollow();
				break;

			case MOVE_TYPE_ASTAR:
				doAStarFollow();
				break;

			case MOVE_TYPE_ASTAR_FAIL:
				doAStarFailFollow();
				break;
			}*/
    } else {
      if (L1World.getInstance().getRecognizePlayer(this).size() == 0) {
        return true;
      }

      if (_master == null && isMovable() && !isRest()) {
        L1MobGroupInfo mobGroupInfo = getMobGroupInfo();
        if (mobGroupInfo == null || mobGroupInfo != null && mobGroupInfo.isLeader(this)) {
          if (_randomMoveDistance == 0) {
            _randomMoveDistance = _random.nextInt(5) + 1;
            _randomMoveDirection = _random.nextInt(20);
            if (getHomeX() != 0 && getHomeY() != 0 && _randomMoveDirection < 8 && _random.nextInt(3) == 0) {
              _randomMoveDirection = moveDirection(getMapId(), getHomeX(), getHomeY());
            }
          } else {
            _randomMoveDistance--;
          }
          if (_randomMoveDirection < 8 && getHomeX() != 0 && getHomeY() != 0) {
            byte dis = 0;
            if (getNpcId() == 70009)// 게렝
              dis = 3;
            else if (getNpcId() == 70027)// 디오
              dis = 2;
            if (dis != 0 && getLocation().getLineDistance(new Point(getHomeX(), getHomeY())) > dis)
              _randomMoveDirection = moveDirection(getMapId(), getHomeX(), getHomeY());
          }
          int dir = checkObject(getX(), getY(), getMapId(), _randomMoveDirection);
          if (dir != -1) {
            setSleepTime(setDirectionMoveSpeed(dir));
          }
        } else {
          L1NpcInstance leader = mobGroupInfo.getLeader();
          if (getLocation().getTileLineDistance(leader.getLocation()) > 2) {
            int dir = moveDirection(leader.getMapId(), leader.getX(), leader.getY());
            if (dir == -1) {
              return true;
            } else {
              setSleepTime(setDirectionMoveSpeed(dir));
            }
          }
        }
      }
    }
    return false;
  }

  public void onFinalAction(L1PcInstance pc, String s) {
  }

  public void tagertClear() {
    if (_target != null) {
      _hateList.remove(_target);
    }
    _target = null;
    _moveType = MOVE_TYPE_NORMAL;
  }

  public void targetRemove(L1Character target) {
    _hateList.remove(target);
    if (_target != null && _target.equals(target)) {
      _target = null;
      _moveType = MOVE_TYPE_NORMAL;
    }
  }

  public void allTargetClear() {
    _hateList.clear();
    _dropHateList.clear();
    _target = null;
    _targetItemList.clear();
    _targetItem = null;
    _moveType = MOVE_TYPE_NORMAL;
  }

  public void setMaster(L1Character cha) {
    _master = cha;
  }

  public L1Character getMaster() {
    return _master;
  }

  public void onNpcAI() {
  }

  public void refineItem() {
    int[] materials = null;
    int[] counts = null;
    int[] createitem = null;
    int[] createcount = null;

    if (_npcTemplate.get_npcId() == 45032) {
      if (get_exp() != 0 && !_inventory.checkItem(20)) {
        materials = new int[] { 40508, 40521, 40045 };
        counts = new int[] { 150, 3, 3 };
        createitem = new int[] { 20 };
        createcount = new int[] { 1 };
        if (_inventory.checkItem(materials, counts)) {
          for (int i = 0; i < materials.length; i++) {
            _inventory.consumeItem(materials[i], counts[i]);
          }
          for (int j = 0; j < createitem.length; j++) {
            _inventory.storeItem(createitem[j], createcount[j]);
          }
        }
      }
      if (get_exp() != 0 && !_inventory.checkItem(19)) {
        materials = new int[] { 40494, 40521 };
        counts = new int[] { 150, 3 };
        createitem = new int[] { 19 };
        createcount = new int[] { 1 };
        if (_inventory.checkItem(materials, counts)) {
          for (int i = 0; i < materials.length; i++) {
            _inventory.consumeItem(materials[i], counts[i]);
          }
          for (int j = 0; j < createitem.length; j++) {
            _inventory.storeItem(createitem[j], createcount[j]);
          }
        }
      }
      if (get_exp() != 0 && !_inventory.checkItem(3)) {
        materials = new int[] { 40494, 40521 };
        counts = new int[] { 50, 1 };
        createitem = new int[] { 3 };
        createcount = new int[] { 1 };
        if (_inventory.checkItem(materials, counts)) {
          for (int i = 0; i < materials.length; i++) {
            _inventory.consumeItem(materials[i], counts[i]);
          }
          for (int j = 0; j < createitem.length; j++) {
            _inventory.storeItem(createitem[j], createcount[j]);
          }
        }
      }
      if (get_exp() != 0 && !_inventory.checkItem(100)) {
        materials = new int[] { 88, 40508, 40045 };
        counts = new int[] { 4, 80, 3 };
        createitem = new int[] { 100 };
        createcount = new int[] { 1 };
        if (_inventory.checkItem(materials, counts)) {
          for (int i = 0; i < materials.length; i++) {
            _inventory.consumeItem(materials[i], counts[i]);
          }
          for (int j = 0; j < createitem.length; j++) {
            _inventory.storeItem(createitem[j], createcount[j]);
          }
        }
      }
      if (get_exp() != 0 && !_inventory.checkItem(89)) {
        materials = new int[] { 88, 40494 };
        counts = new int[] { 2, 80 };
        createitem = new int[] { 89 };
        createcount = new int[] { 1 };
        if (_inventory.checkItem(materials, counts)) {
          for (int i = 0; i < materials.length; i++) {
            _inventory.consumeItem(materials[i], counts[i]);
          }
          L1ItemInstance item = null;
          for (int j = 0; j < createitem.length; j++) {
            item = _inventory.storeItem(createitem[j], createcount[j]);
            if (getNpcTemplate().get_digestitem() > 0) {
              setDigestItem(item);
            }
          }
        }
      }
    } else if (_npcTemplate.get_npcId() == 81069) {
      if (get_exp() != 0 && !_inventory.checkItem(40542)) {
        materials = new int[] { 40032 };
        counts = new int[] { 1 };
        createitem = new int[] { 40542 };
        createcount = new int[] { 1 };
        if (_inventory.checkItem(materials, counts)) {
          for (int i = 0; i < materials.length; i++) {
            _inventory.consumeItem(materials[i], counts[i]);
          }
          for (int j = 0; j < createitem.length; j++) {
            _inventory.storeItem(createitem[j], createcount[j]);
          }
        }
      }
    } else if (_npcTemplate.get_npcId() == 45166 || _npcTemplate.get_npcId() == 45167) {
      if (get_exp() != 0 && !_inventory.checkItem(40726)) {
        materials = new int[] { 40725 };
        counts = new int[] { 1 };
        createitem = new int[] { 40726 };
        createcount = new int[] { 1 };
        if (_inventory.checkItem(materials, counts)) {
          for (int i = 0; i < materials.length; i++) {
            _inventory.consumeItem(materials[i], counts[i]);
          }
          for (int j = 0; j < createitem.length; j++) {
            _inventory.storeItem(createitem[j], createcount[j]);
          }
        }
      }
    }
  }

  public L1HateList getHateList() {
    return _hateList;
  }

  private int _paralysisTime = 0;

  public void setParalysisTime(int ptime) {
    _paralysisTime = ptime;
  }

  public int getParalysisTime() {
    return _paralysisTime;
  }

  public final void startHpRegeneration() {
    int hprInterval = getNpcTemplate().get_hprinterval();
    int hpr = getNpcTemplate().get_hpr();
    if (!_hprRunning && hprInterval > 0 && hpr > 0) {
      _hprTimer = new HprTimer(hpr, hprInterval);
      GeneralThreadPool.getInstance().schedule(_hprTimer, hprInterval);
      _hprRunning = true;
    }
  }

  public final void stopHpRegeneration() {
    if (_hprRunning) {
      _hprTimer.cancel();
      _hprRunning = false;
    }
  }

  public final void startMpRegeneration() {
    int mprInterval = getNpcTemplate().get_mprinterval();
    int mpr = getNpcTemplate().get_mpr();
    if (!_mprRunning && mprInterval > 0 && mpr > 0) {
      _mprTimer = new MprTimer(mpr, mprInterval);
      GeneralThreadPool.getInstance().schedule(_mprTimer, mprInterval);
      _mprRunning = true;
    }
  }

  public final void stopMpRegeneration() {
    if (_mprRunning) {
      _mprTimer.cancel();
      _mprRunning = false;
    }
  }

  private boolean _hprRunning = false;

  private HprTimer _hprTimer;

  class HprTimer implements Runnable {
    private boolean _active;
    private long _interval;
    private int _point;

    @Override
    public void run() {
      try {
        if (!_active) {
          return;
        }
        if ((!_destroyed && !isDead()) && (getCurrentHp() > 0 && getCurrentHp() < getMaxHp())) {
          setCurrentHp(getCurrentHp() + _point);

          GeneralThreadPool.getInstance().schedule(this, _interval);
        } else {
          _hprRunning = false;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public HprTimer(int point, long interval) {
      if (point < 1) {
        point = 1;
      }
      _point = point;
      _active = true;
      _interval = interval;
    }

    public void cancel() {
      _active = false;
    }

  }

  private boolean _mprRunning = false;

  private MprTimer _mprTimer;

  class MprTimer implements Runnable {
    private boolean _active;
    private long _interval;
    private int _point;

    @Override
    public void run() {
      try {
        if (!_active) {
          return;
        }

        if ((!_destroyed && !isDead()) && (getCurrentHp() > 0 && getCurrentMp() < getMaxMp())) {
          setCurrentMp(getCurrentMp() + _point);

          GeneralThreadPool.getInstance().schedule(this, _interval);
        } else {
          _mprRunning = false;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public MprTimer(int point, long interval) {
      if (point < 1) {
        point = 1;
      }
      _point = point;
      _active = true;
      _interval = interval;
    }

    public void cancel() {
      _active = false;
    }
  }

  class DigestItemTimer implements Runnable {
    @Override
    public void run() {
      if (!_digestItemRunning) {
        _digestItemRunning = true;
      }

      Object[] keys = null;
      L1ItemInstance digestItem = null;

      if (!_destroyed && _digestItems.size() > 0) {
        keys = _digestItems.keySet().toArray();
        Integer key = null;
        Integer digestCounter = null;
        for (int i = 0; i < keys.length; i++) {
          key = (Integer) keys[i];
          digestCounter = _digestItems.get(key);
          digestCounter -= 1;
          if (digestCounter <= 0) {
            _digestItems.remove(key);
            digestItem = getInventory().getItem(key);
            if (digestItem != null) {
              getInventory().removeItem(digestItem, digestItem.getCount());
            }
          } else {
            _digestItems.put(key, digestCounter);
          }
        }
        GeneralThreadPool.getInstance().schedule(this, 1000);
      } else {
        _digestItemRunning = false;
      }
    }
  }

  private boolean _pickupItem;

  public boolean isPickupItem() {
    return _pickupItem;
  }

  public void setPickupItem(boolean flag) {
    _pickupItem = flag;
  }

  @Override
  public L1Inventory getInventory() {
    return _inventory;
  }

  public void setInventory(L1Inventory inventory) {
    _inventory = inventory;
  }

  public L1Npc getNpcTemplate() {
    return _npcTemplate;
  }

  public int getNpcId() {
    return _npcTemplate.get_npcId();
  }

  public int getNpcClassId() {
    return _npcTemplate.get_npc_class_id();
  }

  public void setPetcost(int i) {
    _petcost = i;
  }

  public int getPetcost() {
    return _petcost;
  }

  public void setSpawn(L1Spawn spawn) {
    _spawn = spawn;
  }

  public L1Spawn getSpawn() {
    return _spawn;
  }

  public void set_spawn_ex(MJSpawnInfo sInfo) {
    m_spawn_ex = sInfo;
  }

  public MJSpawnInfo get_spawn_ex() {
    return m_spawn_ex;
  }

  public void setSpawnNumber(int number) {
    _spawnNumber = number;
  }

  public int getSpawnNumber() {
    return _spawnNumber;
  }

  public void onDecay(boolean isReuseId) {
    int id = 0;
    if (isReuseId) {
      id = getId();
    } else {
      id = 0;
    }
    _spawn.executeSpawnTask(_spawnNumber, id);
  }

  public int PASS = 1;

  @Override
  public void onPerceive(L1PcInstance perceivedFrom) {
    if (this == null || perceivedFrom == null)
      return;// 하딘 시스템

    perceivedFrom.addKnownObject(this);
    if (perceivedFrom.getAI() == null) {
      perceivedFrom.sendPackets(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));

      if (getNpcTemplate().get_npcId() == 900168) // 보스방 후문 문
        perceivedFrom.sendPackets(new S_Door(getX(), getY(), 0, PASS));// 하딘시스템
    }

    if (hasSkillEffect(L1SkillId.TRUE_TARGET)) {
      if (getTrueTargetClan() == perceivedFrom.getClanid()
              || getTrueTargetParty() == perceivedFrom.getPartyID()) {
        perceivedFrom.sendPackets(new S_TrueTargetNew(getId(), true));
      }
    }
    getMap().setPassable(getLocation(), true);
    onNpcAI();
  }

  @Override
  public void dispose() {
    _mobGroupInfo = null;
    _inventory = null;

    if (_digestItems != null) {
      _digestItems.clear();
      _digestItems = null;
    }

    if (getNpcTemplate().get_npcId() != 8500138) {
      if (mobSkill != null) {
        mobSkill.dispose();
        mobSkill = null;
      }
    }

    _spawn = null;
    super.dispose();
  }

  public void deleteMe() {
    try {
      _destroyed = true;
      if (isDead()) {
        if (this instanceof L1MonsterInstance) {
          L1MonsterInstance m = (L1MonsterInstance) this;
          MJMonsterDeathHandler h = m.getDeathHandler();
          if (h != null)
            h.onDeathNotify(m);
        }
      }

      getMap().setPassable(getLocation(), true);

      if (getInventory() != null) {
        getInventory().clearItems();
      }

      //clearDropList();
      allTargetClear();

      L1World.getInstance().removeVisibleObject(this);
      L1World.getInstance().removeObject(this);
      List<L1PcInstance> players = L1World.getInstance().getRecognizePlayer(this);
      if (players.size() > 0) {
        S_RemoveObject s_deleteNewObject = new S_RemoveObject(this);
        for (L1PcInstance pc : players) {
          if (pc != null) {
            pc.removeKnownObject(this);
            pc.sendPackets(s_deleteNewObject);
          }
        }
      }
      removeAllKnownObjects();
      L1World.getInstance().removeVisibleObject(this);
      L1World.getInstance().removeObject(this);

      mobSkill = null;

      L1MobGroupInfo mobGroupInfo = getMobGroupInfo();
      if (mobGroupInfo == null) {
        if (isReSpawn()) {
          onDecay(true);
        }
      } else {
        if (mobGroupInfo.removeMember(this) == 0) {
          setMobGroupInfo(null);
          if (isReSpawn()) {
            onDecay(false);
          }
        }
      }
      if (m_spawn_ex != null)
        m_spawn_ex.on_death(this);

      if (this instanceof L1MonsterInstance) {
        dispose();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void deleteMe2() {
    _destroyed = true;

    if (isDead()) {
      if (this instanceof L1MonsterInstance) {
        L1MonsterInstance m = (L1MonsterInstance) this;
        MJMonsterDeathHandler h = m.getDeathHandler();
        if (h != null)
          h.onDeathNotify(m);
      }
    }

    if (getInventory() != null) {
      getInventory().clearItems();
    }
    _master = null;
    L1World world = L1World.getInstance();

    List<L1PcInstance> players = world.getRecognizePlayer(this);
    if (players != null && players.size() > 0) {
      S_RemoveObject s_deleteNewObject = new S_RemoveObject(this);
      for (L1PcInstance pc : players) {
        if (pc != null) {
          pc.removeKnownObject(this);
          pc.sendPackets(s_deleteNewObject);
        }
      }
    }
    removeAllKnownObjects();
/*		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);*/
    world.removeVisibleObject(this);
    world.removeObject(this);
    dispose();
  }

  public void ReceiveManaDamage(L1Character attacker, int damageMp) {
  }

  public void receiveCounterBarrierDamage(L1Character attacker, int damage) {
    receiveDamage(attacker, damage);
  }

  public void receiveConqureDamage(L1Character attacker, int damage) {

    receiveDamage(attacker, damage);
  }

  public void receiveDamage(L1Character attacker, int damage) {
  }

  public void setDigestItem(L1ItemInstance item) {
    if (item == null)
      return;

    _digestItems.put(new Integer(item.getId()), new Integer(getNpcTemplate().get_digestitem()));
    if (!_digestItemRunning) {
      DigestItemTimer digestItemTimer = new DigestItemTimer();
      GeneralThreadPool.getInstance().execute(digestItemTimer);
    }
  }

  public void onGetItem(L1ItemInstance item) {
    refineItem();
    getInventory().shuffle();
    if (getNpcTemplate().get_digestitem() > 0) {
      setDigestItem(item);
    }
  }

  public void approachPlayer(L1PcInstance pc) {
    if (pc == null)
      return;
    if (pc.hasSkillEffect(L1SkillId.INVISIBILITY) || pc.hasSkillEffect(L1SkillId.BLIND_HIDING)) {
      return;
    }
    if (getHiddenStatus() == HIDDEN_STATUS_SINK) {
      if (getCurrentHp() == getMaxHp()) {
        if (pc.getLocation().getTileLineDistance(this.getLocation()) <= 2) {
          appearOnGround(pc);
        }
      }
    } else if (getHiddenStatus() == HIDDEN_STATUS_FLY) {
      if (getCurrentHp() == getMaxHp()) {
        if (pc.getLocation().getTileLineDistance(this.getLocation()) <= 1) {
          appearOnGround(pc);
        }
      } else {
        // if (getNpcTemplate().get_npcId() != 45681) {
        searchItemFromAir();
        // }
      }
    }

  }

  public void appearOnGround(L1PcInstance pc) {
    if (pc == null)
      return;
    if (getHiddenStatus() == HIDDEN_STATUS_SINK) {
      setHiddenStatus(HIDDEN_STATUS_NONE);

      setStatus(0);
      broadcastPacket(new S_RemoveObject(this), true);//???왜 지우지?
      broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Appear));
      broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
      if (!pc.hasSkillEffect(L1SkillId.INVISIBILITY) && !pc.hasSkillEffect(L1SkillId.BLIND_HIDING) && !pc.isGm()) {
        _hateList.add(pc, 0);
        _target = pc;
        _moveType = MOVE_TYPE_NORMAL;
      }
      onNpcAI();
    } else if (getHiddenStatus() == HIDDEN_STATUS_FLY) {
      setHiddenStatus(HIDDEN_STATUS_NONE);
      broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Movedown));
      setStatus(0);
      broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
      if (!pc.hasSkillEffect(L1SkillId.INVISIBILITY) && !pc.hasSkillEffect(L1SkillId.BLIND_HIDING) && !pc.isGm()) {
        _hateList.add(pc, 0);
        _target = pc;
        _moveType = MOVE_TYPE_NORMAL;
      }
      onNpcAI();
      startChat(CHAT_TIMING_HIDE);
    }
  }

  protected void on_moved() {
  }

  private static final byte[] HEADING_TABLE_X		= { 0, 1, 1, 1, 0, -1, -1, -1 };
  private static final byte[] HEADING_TABLE_Y		= { -1, -1, 0, 1, 1, 1, 0, -1 };

  /*	public void setDirectionMove(int dir) {
		if(dir >= 0){
			if (發動束縛狀態()) return;
			int nx = HEADING_TABLE_X[dir], ny = HEADING_TABLE_Y[dir];
			getMoveState().setHeading(dir);

			if(!(this instanceof L1DollInstance)) {
				getMap().setPassable(getLocation(), true);
			}
			int nnx = getX() + nx;
			int nny = getY() + ny;
			setX(nnx);
			setY(nny);
			if(!(this instanceof L1DollInstance)) {
				getMap().setPassable(getLocation(), false);
			}
//			L1World.getInstance().moveVisibleObject(this, getMapId());
//			L1World.getInstance().onMoveObject(this);
//			broadcastPacket(new S_MoveCharPacket(this), true);

			if(getMovementDistance() > 0){
				if((this instanceof L1GuardInstance || this instanceof L1GuardianInstance || this instanceof L1CastleGuardInstance || this instanceof L1MerchantInstance || this instanceof L1MonsterInstance)
						&& getLocation().getLineDistance(new Point(getHomeX(), getHomeY())) > getMovementDistance()){
					teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
				}
			}else if(this instanceof L1MonsterInstance && (getMapId() == 4 || getMapId() == 9)
					&& getLocation().getLineDistance(new Point(getHomeX(), getHomeY())) > 100){// 野外怪物超過100格回到原點傳送
				teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
			}

			int npcid = getNpcTemplate().get_npcId();
			if((npcid >= 45912 && npcid <= 45916) && getMapId() == 4){//將格魯丁幽靈移動回原來的位置
				if(!(getX() >= 32591 && getX() <= 32644 && getY() >= 32643 && getY() <= 32688 && getMapId() == 4))
					teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
			}else if((npcid == 45752 || npcid == 45753) && getMapId() == 15404){//巴洛克如果走出門外則移動
				if(!(getX() >= 32720 && getX() <= 32742 && getY() >= 32851 && getY() <= 32877))
					teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
			}else if(npcid == 7220091 && getMapId() == 1319){//憤怒的巴洛克
				if(!(getX() >= 32719 && getX() <= 32785 && getY() >= 32919 && getY() <= 33002))
					teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
			}else if((npcid == 800185 || npcid == 800177 || npcid == 800187 || npcid == 800188) && getMapId() == 12853){//監視者希爾
				if(!(getX() >= 32786 && getX() <= 32815 && getY() >= 32781 && getY() <= 32813))
					teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
			}else if((npcid == 800186 || npcid == 800178) && getMapId() == 12854){//蝙蝠吸血鬼
				if(!(getX() >= 32787 && getX() <= 32817 && getY() >= 32779 && getY() <= 32810))
					teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
			}else if((npcid == 800186 || npcid == 800178) && getMapId() == 12857){//沙漠風暴木乃伊領主
				if(!(getX() >= 32655 && getX() <= 32687 && getY() >= 32838 && getY() <= 32870))
					teleport(getHomeX(), getHomeY(), getMoveState().getHeading());
			}
		}
	}*/

    public long setDirectionMoveSpeed(int dir) {
    long itv = getNpcTemplate().doProbabilityBornNpc(this);
    if (itv > 0)
      return itv;

    if (dir >= 0) {
      int nx = 0;
      int ny = 0;

      switch (dir) {
        case 1:
          nx = 1;
          ny = -1;
          setHeading(1);
          break;
        case 2:
          nx = 1;
          ny = 0;
          setHeading(2);
          break;
        case 3:
          nx = 1;
          ny = 1;
          setHeading(3);
          break;
        case 4:
          nx = 0;
          ny = 1;
          setHeading(4);
          break;
        case 5:
          nx = -1;
          ny = 1;
          setHeading(5);
          break;
        case 6:
          nx = -1;
          ny = 0;
          setHeading(6);
          break;
        case 7:
          nx = -1;
          ny = -1;
          setHeading(7);
          break;
        case 0:
          nx = 0;
          ny = -1;
          setHeading(0);
          break;
        default:
          break;
      }

      if (getMapId() == 13004 && !getMap().isPassable(getX() + nx, getY() + ny)) {
        return getCurrentSpriteInterval(EActionCodes.walk);
      }

      getMap().setPassable(getLocation(), true);

      int nnx = getX() + nx;
      int nny = getY() + ny;
      setX(0);
      setY(0);
      setX(nnx);
      setY(nny);

      if (!(this instanceof L1DollInstance)) {
        getMap().setPassable(getLocation(), false);
      }
      /*
       * if(getMapId() == 53 && !getMap().isPassable(getLocation())) {
       * System.out.println((nnx - nx) + " " + (nny - ny) + " " + dir); }
       */

/*			if (!(this instanceof L1DollInstance) && !(this instanceof L1TowerInstance) && !(this instanceof MJCompanionInstance))
				getMap().setPassable(getLocation(), false);
*/
      broadcastPacket(new S_MoveCharPacket(this));
      MJMyMapViewService.service().onMoveObject(this);

      /*
       * if (this.getMoveDelayRate() > 0) {
       * broadcastPacket(SC_NPC_SPEED_VALUE_FLAG_NOTI.speed_send(this)); }
       */

      on_moved();
      // TODO 엔피씨 제자리텔 적용
      if (getMovementDistance() > 0) {
        if (this instanceof L1GuardInstance || this instanceof L1CastleGuardInstance || this instanceof L1MerchantInstance || this instanceof L1MonsterInstance) {
          if (getLocation().getLineDistance(new Point(getHomeX(), getHomeY())) > getMovementDistance()) {
            // TODO npc테이블에서 true로 할 경우 해당몬스터만 제자리텔을 사용한다.
//						if (this.getNpcTemplate().isMoveMent()) {
            npcInitialize();
//						}
          }
        }
      }

      if (getNpcTemplate().get_npcId() >= 45912 && getNpcTemplate().get_npcId() <= 45916) {
        if (getX() >= 32591 && getX() <= 32644 && getY() >= 32643 && getY() <= 32688 && getMapId() == 4) {
          if (getMovementDistance() <= 0)
            if (Config.ServerAdSetting.NpcMaxYN)
              npcInitialize();
        }
      }
    }
    return getCurrentSpriteInterval(EActionCodes.walk);
  }

  public int moveDirection3(int x, int y) {
    int dir = targetDirection(x, y);
    dir = checkObject(getX(), getY(), getMapId(), dir);

    return dir;
  }



  public int moveDirection(int mapid, int x, int y) {
    return moveDirection(mapid, x, y, getLocation().getLineDistance(new Point(x, y)));
  }

    public int directionFromCurrent(int cx, int cy, int tx, int ty) {
// 计算X方向到目标的距离
      float dis_x = Math.abs(cx - tx);

// 计算Y方向到目标的距离
      float dis_y = Math.abs(cy - ty);

// 计算到目标的最大距离
      float dis = Math.max(dis_x, dis_y);

      if (dis == 0)
        return 8;

// 计算四舍五入后的X和Y方向的比例
      int avg_x = (int) Math.floor((dis_x / dis) + 0.59f);
      int avg_y = (int) Math.floor((dis_y / dis) + 0.59f);

      int dir_x = 0;
      int dir_y = 0;

// 根据坐标位置设置X方向
      if (cx < tx)
        dir_x = 1;
      if (cx > tx)
        dir_x = -1;

// 根据坐标位置设置Y方向
      if (cy < ty)
        dir_y = 1;
      if (cy > ty)
        dir_y = -1;

// 如果X或Y方向的比例为0，设置方向为0
      if (avg_x == 0)
        dir_x = 0;
      if (avg_y == 0)
        dir_y = 0;

// 返回对应的方向值
      if (dir_x == 1 && dir_y == -1)
        return 1; // 上
      if (dir_x == 1 && dir_y == 0)
        return 2; // 右上
      if (dir_x == 1 && dir_y == 1)
        return 3; // 右
      if (dir_x == 0 && dir_y == 1)
        return 4; // 右下
      if (dir_x == -1 && dir_y == 1)
        return 5; // 下
      if (dir_x == -1 && dir_y == 0)
        return 6; // 左下
      if (dir_x == -1 && dir_y == -1)
        return 7; // 左
      if (dir_x == 0 && dir_y == -1)
        return 0; // 左上

      return -1;
    }

    public int moveDirection(int mapid, int x, int y, double d) {
      int dir = 0;
// 如果有技能效果“黑暗”，且距离大于等于2，则返回-1
      if (hasSkillEffect(L1SkillId.DARKNESS) && d >= 2D) {
        return -1;
      }
// 如果距离大于30，则返回-1
      else if (d > 30D) {
        return -1;
      }
// 如果距离大于范围，计算目标方向并检查障碍物
      else if (d > courceRange) {
        dir = targetDirection(x, y);
        dir = checkObject(getX(), getY(), getMapId(), dir);
      }
// 否则，搜索路径
      else {
        dir = _serchCource(x, y, mapid);
// 如果路径搜索失败，计算目标方向并检查目标之间是否存在障碍物
        if (dir == -1) {
          dir = targetDirection(x, y);
          if (!isExsistCharacterBetweenTarget(dir)) {
            dir = checkObject(getX(), getY(), getMapId(), dir);
          }
        }
      }
      return dir;
    }

  public int moveDirection2(int x, int y, double d) {
    if (hasSkillEffect(L1SkillId.DARKNESS) == true && d >= 2D) {
      return -1;
    } else if (d > 30D) {
      return -1;
    } else {
      int newX = getX();
      int newY = getY();

      int firstDir = directionFromCurrent(newX, newY, x, y);
      int dir = firstDir;
      while (true) {
        if (dir == 8) {
          if (firstDir == 8) {
            firstDir = 0;
          }
          return firstDir;
        }
        if (!getMap().isPassable(newX, newY, dir)) {
          return -1;
        }
        switch (dir) {
          case 0:
            newY -= 1;
            break;
          case 1:
            newX += 1;
            newY -= 1;
            break;
          case 2:
            newX += 1;
            break;
          case 3:
            newX += 1;
            newY += 1;
            break;
          case 4:
            newY += 1;
            break;
          case 5:
            newX -= 1;
            newY += 1;
            break;
          case 6:
            newX -= 1;
            break;
          case 7:
            newX -= 1;
            newY -= 1;
            break;
        }
        dir = directionFromCurrent(newX, newY, x, y);
      }
    }
  }

  public boolean isExsistCharacterBetweenTarget(int dir) {
    if (!(this instanceof L1MonsterInstance)) {
      return false;
    }
    if (_target == null) {
      return false;
    }

    L1Character cha = null;
    L1PcInstance pc = null;

    for (L1Object object : L1World.getInstance().getVisibleObjects(this, 1)) {
      if (object instanceof L1PcInstance || object instanceof L1SummonInstance
              || object instanceof L1PetInstance) {
        cha = (L1Character) object;
        if (!cha.isDead()) {

          boolean matched = false;
/*					for (int i = 0; i < 8; ++i) {
						if (!cha.getMap().isUserPassable(cha.getX(), cha.getY(), i)) {
							matched = true;
							break;
						}
					}*/
          for (int i = 0; i < 4; ++i) {
            if (!cha.getMap().isUserPassable(cha.getX(), cha.getY(), i)
                    && !cha.getMap().isUserPassable(cha.getX(), cha.getY(), i + 4)) {
              matched = true;
              break;
            }
          }

          if (!matched) {
            continue;
          }

          if (object instanceof L1PcInstance) {
            pc = (L1PcInstance) object;
            if (pc.isGhost()) {
              continue;
            }
          }
          _hateList.add(cha, 0);
          _target = cha;
//					_moveType = MOVE_TYPE_ASTAR;
//					_moveType = MOVE_TYPE_NORMAL;
//					_aStar.resetTryCount();
          return true;
        }
      }
    }
    return false;
  }

  public int targetReverseDirection(int tx, int ty) {
    int dir = targetDirection(tx, ty);
    dir += 4;
    if (dir > 7) {
      dir -= 8;
    }
    return dir;
  }

  private static boolean isObjectExist(int x, int y, short m, int d) {
    L1Location loc = new L1Location();
    loc.setX(x);
    loc.setY(y);
    loc.setMap(m);

    loc.forward(d);

    for (L1Object object : L1World.getInstance().getVisiblePoint(loc, 0)) {
      if (object instanceof L1Character && !(object instanceof L1DollInstance)) {
        if (m >= 732 && m <= 776)
          return false;

        if (!((L1Character) object).isDead()) {
          return true;
        }
      }
    }

    return false;

  }

  public static int checkObject2(int x, int y, short m, int d) {
    L1Map map = L1WorldMap.getInstance().getMap(m);

    L1Location loc = new L1Location();
    loc.setX(x);
    loc.setY(y);
    loc.setMap(m);

    switch (d) {
      case 1:
        loc.forward(1);
        if (map.isPassable(x, y, 1) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 1;
        }
        loc.backward(1);

        loc.forward(0);
        if (map.isPassable(x, y, 0) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 0;
        }
        loc.backward(0);

        loc.forward(2);
        if (map.isPassable(x, y, 2) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 2;
        }
        break;
      case 2:
        loc.forward(2);
        if (map.isPassable(x, y, 2) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 2;
        }
        loc.backward(2);

        loc.forward(1);
        if (map.isPassable(x, y, 1) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 1;
        }
        loc.backward(1);

        loc.forward(3);
        if (map.isPassable(x, y, 3) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 3;
        }

        break;
      case 3:
        loc.forward(3);
        if (map.isPassable(x, y, 3) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 3;
        }
        loc.backward(3);

        loc.forward(2);
        if (map.isPassable(x, y, 2) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 2;
        }
        loc.backward(2);

        loc.forward(4);
        if (map.isPassable(x, y, 4) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 4;
        }
        break;
      case 4:
        loc.forward(4);
        if (map.isPassable(x, y, 4) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 4;
        }
        loc.backward(4);

        loc.forward(3);
        if (map.isPassable(x, y, 3) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 3;
        }
        loc.backward(3);

        loc.forward(5);
        if (map.isPassable(x, y, 5) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 5;
        }
        break;
      case 5:
        loc.forward(5);
        if (map.isPassable(x, y, 5) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 5;
        }
        loc.backward(5);

        loc.forward(4);
        if (map.isPassable(x, y, 4) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 4;
        }
        loc.backward(4);

        loc.forward(6);
        if (map.isPassable(x, y, 6) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 6;
        }
        break;
      case 6:
        loc.forward(6);
        if (map.isPassable(x, y, 6) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 6;
        }
        loc.backward(6);

        loc.forward(5);
        if (map.isPassable(x, y, 5) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 5;
        }
        loc.backward(5);

        loc.forward(7);
        if (map.isPassable(x, y, 7) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 7;
        }
        break;
      case 7:
        loc.forward(7);
        if (map.isPassable(x, y, 7) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 7;
        }
        loc.backward(7);

        loc.forward(6);
        if (map.isPassable(x, y, 6) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 6;
        }
        loc.backward(6);

        loc.forward(0);
        if (map.isPassable(x, y, 0) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 0;
        }
        break;
      case 0:
        loc.forward(0);
        if (map.isPassable(x, y, 0) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 0;
        }
        loc.backward(0);

        loc.forward(7);
        if (map.isPassable(x, y, 7) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 7;
        }
        loc.backward(7);

        loc.forward(1);
        if (map.isPassable(x, y, 1) && L1World.getInstance().getVisiblePoint(loc, 0).size() == 0) {
          return 1;
        }
        break;
      default:
        break;
    }
    return -1;
  }

  public static int checkObject(int x, int y, short m, int d) {
    L1Map map = L1WorldMap.getInstance().getMap(m);
    switch (d) {
      case 1:
        if (map.isPassable(x, y, 1)) {
          return 1;
        } else if (map.isPassable(x, y, 0)) {
          return 0;
        } else if (map.isPassable(x, y, 2)) {
          return 2;
        }
        break;
      case 2:
        if (map.isPassable(x, y, 2)) {
          return 2;
        } else if (map.isPassable(x, y, 1)) {
          return 1;
        } else if (map.isPassable(x, y, 3)) {
          return 3;
        }
        break;
      case 3:
        if (map.isPassable(x, y, 3)) {
          return 3;
        } else if (map.isPassable(x, y, 2)) {
          return 2;
        } else if (map.isPassable(x, y, 4)) {
          return 4;
        }
        break;
      case 4:
        if (map.isPassable(x, y, 4)) {
          return 4;
        } else if (map.isPassable(x, y, 3)) {
          return 3;
        } else if (map.isPassable(x, y, 5)) {
          return 5;
        }
        break;
      case 5:
        if (map.isPassable(x, y, 5)) {
          return 5;
        } else if (map.isPassable(x, y, 4)) {
          return 4;
        } else if (map.isPassable(x, y, 6)) {
          return 6;
        }
        break;
      case 6:
        if (map.isPassable(x, y, 6)) {
          return 6;
        } else if (map.isPassable(x, y, 5)) {
          return 5;
        } else if (map.isPassable(x, y, 7)) {
          return 7;
        }
        break;
      case 7:
        if (map.isPassable(x, y, 7)) {
          return 7;
        } else if (map.isPassable(x, y, 6)) {
          return 6;
        } else if (map.isPassable(x, y, 0)) {
          return 0;
        }
        break;
      case 0:
        if (map.isPassable(x, y, 0)) {
          return 0;
        } else if (map.isPassable(x, y, 7)) {
          return 7;
        } else if (map.isPassable(x, y, 1)) {
          return 1;
        }
        break;
      default:
        break;
    }
    return -1;
  }

    // 计算从对象 o 到目标点 (x, y) 的方向
    public int calcheading(L1Object o, int x, int y) {
      return calcheading(o.getX(), o.getY(), x, y);
    }

    // 路径寻找变量
    private AStar aStar; // A* 路径寻找算法实例
    private Node tail; // 路径的尾节点
    private int[][] iPath; // 存储路径的二维数组
    private int iCurrentPath; // 当前路径索引

    public int _serchCource(int x, int y, int m) {
      try {
        aStar.cleanTail(); // 清理尾节点
        tail = aStar.searchTail(this, x, y, m, true); // 使用A*算法搜索尾节点

        try {
          if (tail != null) {
            iCurrentPath = -1;
            while (tail != null) {
              // 如果尾节点是当前坐标，结束循环
              if (tail.x == getX() && tail.y == getY())
                break;
              // 如果路径数组已满，返回-1
              if (iCurrentPath >= iPath.length - 1) return -1;
              // 如果对象已销毁或死亡，返回-1
              if (_destroyed || isDead()) return -1;

              // 保存路径点
              iPath[++iCurrentPath][0] = tail.x;
              iPath[iCurrentPath][1] = tail.y;

              // 移动到前一个节点
              tail = tail.prev;
            }
            // 计算并返回从当前位置到路径点的方向
            return iCurrentPath != -1 ? aStar.calcheading(getX(), getY(), iPath[iCurrentPath][0], iPath[iCurrentPath][1]) : -1;
          } else {
            aStar.cleanTail(); // 清理尾节点
            tail = aStar.關閉搜尋圖塊(this, x, y, m, true); // 近距离搜索尾节点
            if (tail != null && !(tail.x == getX() && tail.y == getY())) {
              iCurrentPath = -1;
              while (tail != null) {
                // 如果尾节点是当前坐标，结束循环
                if (tail.x == getX() && tail.y == getY())
                  break;
                // 如果路径数组已满，返回-1
                if (iCurrentPath >= iPath.length - 1) return -1;
                // 如果对象已销毁或死亡，返回-1
                if (_destroyed || isDead()) return -1;

                // 保存路径点
                iPath[++iCurrentPath][0] = tail.x;
                iPath[iCurrentPath][1] = tail.y;

                // 移动到前一个节点
                tail = tail.prev;
              }
              // 计算并返回从当前位置到路径点的方向
              return iCurrentPath != -1 ? aStar.calcheading(getX(), getY(), iPath[iCurrentPath][0], iPath[iCurrentPath][1]) : -1;
            } else {
              int chdir = calcheading(this, x, y);
              if (getMoveState().getHeading() != chdir) {
                this.getMoveState().setHeading(calcheading(this, x, y));
                Broadcaster.broadcastPacket(this, new S_ChangeHeading(this), true);
              }
            }
            return getMoveState().getHeading();
          }
        } catch (Exception e) {
          return -1;
        }
      } catch (Exception e) {
        return -1;
      }
    }



  /*	public int _serchCource(int x, int y) {
		int i;
		int locCenter = courceRange + 1;
		int diff_x = x - locCenter;
		int diff_y = y - locCenter;
		int[] locBace = { getX() - diff_x, getY() - diff_y, 0, 0 };
		int[] locNext = new int[4];
		int[] locCopy;
		int[] dirFront = new int[5];
		boolean serchMap[][] = new boolean[locCenter * 2 + 1][locCenter * 2 + 1];
		LinkedList<int[]> queueSerch = new LinkedList<int[]>();

		for (int j = courceRange * 2 + 1; j > 0; j--) {
			for (i = courceRange - Math.abs(locCenter - j); i >= 0; i--) {
				serchMap[j][locCenter + i] = true;
				serchMap[j][locCenter - i] = true;
			}
		}

		int[] firstCource = { 2, 4, 6, 0, 1, 3, 5, 7 };
		for (i = 0; i < 8; i++) {
			System.arraycopy(locBace, 0, locNext, 0, 4);
			_moveLocation(locNext, firstCource[i]);
			if (locNext[0] - locCenter == 0 && locNext[1] - locCenter == 0) {
				return firstCource[i];
			}
			if (serchMap[locNext[0]][locNext[1]]) {
				int tmpX = locNext[0] + diff_x;
				int tmpY = locNext[1] + diff_y;
				boolean found = false;
				switch (i) {
				case 0:
					found = getMap().isPassable(tmpX, tmpY + 1, i);
					break;
				case 1:
					found = getMap().isPassable(tmpX - 1, tmpY + 1, i);
					break;
				case 2:
					found = getMap().isPassable(tmpX - 1, tmpY, i);
					break;
				case 3:
					found = getMap().isPassable(tmpX - 1, tmpY - 1, i);
					break;
				case 4:
					found = getMap().isPassable(tmpX, tmpY - 1, i);
					break;
				case 5:
					found = getMap().isPassable(tmpX + 1, tmpY - 1, i);
					break;
				case 6:
					found = getMap().isPassable(tmpX + 1, tmpY, i);
					break;
				case 7:
					found = getMap().isPassable(tmpX + 1, tmpY + 1, i);
					break;
				default:
					break;
				}
				if (found) {
					locCopy = new int[4];
					System.arraycopy(locNext, 0, locCopy, 0, 4);
					locCopy[2] = firstCource[i];
					locCopy[3] = firstCource[i];
					queueSerch.add(locCopy);
				}
				serchMap[locNext[0]][locNext[1]] = false;
			}
		}
		locBace = null;

		while (queueSerch.size() > 0) {
			locBace = queueSerch.removeFirst();
			_getFront(dirFront, locBace[2]);
			for (i = 4; i >= 0; i--) {
				System.arraycopy(locBace, 0, locNext, 0, 4);
				_moveLocation(locNext, dirFront[i]);
				if (locNext[0] - locCenter == 0 && locNext[1] - locCenter == 0) {
					return locNext[3];
				}
				if (serchMap[locNext[0]][locNext[1]]) {
					int tmpX = locNext[0] + diff_x;
					int tmpY = locNext[1] + diff_y;
					boolean found = false;
					if (i == 0) {
						found = getMap().isPassable(tmpX, tmpY + 1, i);
					} else if (i == 1) {
						found = getMap().isPassable(tmpX - 1, tmpY + 1, i);
					} else if (i == 2) {
						found = getMap().isPassable(tmpX - 1, tmpY, i);
					} else if (i == 3) {
						found = getMap().isPassable(tmpX - 1, tmpY - 1, i);
					} else if (i == 4) {
						found = getMap().isPassable(tmpX, tmpY - 1, i);
					}
					if (found) {
						locCopy = new int[4];
						System.arraycopy(locNext, 0, locCopy, 0, 4);
						locCopy[2] = dirFront[i];
						queueSerch.add(locCopy);
					}
					serchMap[locNext[0]][locNext[1]] = false;
				}
			}
			locBace = null;
		}
		return -1;
	}
*/
  private void _moveLocation(int[] ary, int d) {
    switch (d) {
      case 1:
        ary[0] = ary[0] + 1;
        ary[1] = ary[1] - 1;
        break;
      case 2:
        ary[0] = ary[0] + 1;
        break;
      case 3:
        ary[0] = ary[0] + 1;
        ary[1] = ary[1] + 1;
        break;
      case 4:
        ary[1] = ary[1] + 1;
        break;
      case 5:
        ary[0] = ary[0] - 1;
        ary[1] = ary[1] + 1;
        break;
      case 6:
        ary[0] = ary[0] - 1;
        break;
      case 7:
        ary[0] = ary[0] - 1;
        ary[1] = ary[1] - 1;
        break;
      case 0:
        ary[1] = ary[1] - 1;
        break;
      default:
        break;
    }
    ary[2] = d;
  }

  private void _getFront(int[] ary, int d) {
    switch (d) {
      case 1:
        ary[4] = 2;
        ary[3] = 0;
        ary[2] = 1;
        ary[1] = 3;
        ary[0] = 7;
        break;
      case 2:
        ary[4] = 2;
        ary[3] = 4;
        ary[2] = 0;
        ary[1] = 1;
        ary[0] = 3;
        break;
      case 3:
        ary[4] = 2;
        ary[3] = 4;
        ary[2] = 1;
        ary[1] = 3;
        ary[0] = 5;
        break;
      case 4:
        ary[4] = 2;
        ary[3] = 4;
        ary[2] = 6;
        ary[1] = 3;
        ary[0] = 5;
        break;
      case 5:
        ary[4] = 4;
        ary[3] = 6;
        ary[2] = 3;
        ary[1] = 5;
        ary[0] = 7;
        break;
      case 6:
        ary[4] = 4;
        ary[3] = 6;
        ary[2] = 0;
        ary[1] = 5;
        ary[0] = 7;
        break;
      case 7:
        ary[4] = 6;
        ary[3] = 0;
        ary[2] = 1;
        ary[1] = 5;
        ary[0] = 7;
        break;
      case 0:
        ary[4] = 2;
        ary[3] = 6;
        ary[2] = 0;
        ary[1] = 1;
        ary[0] = 7;
        break;
      default:
        break;
    }
  }

  private void useHealPotion(int healHp, int effectId) {
    broadcastPacket(new S_SkillSound(getId(), effectId));
    if (this.hasSkillEffect(L1SkillId.POLLUTE_WATER)) {
      healHp /= 2;
    }
    if (this instanceof L1PetInstance) {
      ((L1PetInstance) this).setCurrentHp(getCurrentHp() + healHp);
    } else if (this instanceof L1SummonInstance) {
      ((L1SummonInstance) this).setCurrentHp(getCurrentHp() + healHp);
    } else {
      setCurrentHp(getCurrentHp() + healHp);
    }
  }

  public void useHastePotion(int time) {
    broadcastPacket(new S_SkillHaste(getId(), 1, time));
    broadcastPacket(new S_SkillSound(getId(), 191));
    setMoveSpeed(1);
    setSkillEffect(L1SkillId.STATUS_HASTE, time * 1000);
  }

  public static final int USEITEM_HEAL = 0;
  public static final int USEITEM_HASTE = 1;
  public static int[] healPotions = { POTION_OF_GREATER_HEALING, POTION_OF_EXTRA_HEALING, POTION_OF_HEALING, 4100021, 4100656, 4100657, 4100658 };
  public static int[] haestPotions = { B_POTION_OF_GREATER_HASTE_SELF, POTION_OF_GREATER_HASTE_SELF, POTION_OF_GREATER_HASTE_SELF1, B_POTION_OF_HASTE_SELF, POTION_OF_HASTE_SELF, 7006, 4100662 };

  public void useItem(int type, int chance) {
    if (hasSkillEffect(L1SkillId.DECAY_POTION)) {
      return;
    }

    Random random = new Random();
    if (random.nextInt(100) > chance) {
      return;
    }

    if (type == USEITEM_HEAL) { // 회복관련
      if (getInventory().consumeItem(POTION_OF_GREATER_HEALING, 1)) {
        useHealPotion(75, 197);
      } else if (getInventory().consumeItem(POTION_OF_EXTRA_HEALING, 1)) {
        useHealPotion(45, 194);
      } else if (getInventory().consumeItem(POTION_OF_HEALING, 1)) {
        useHealPotion(15, 189);
      }
    } else if (type == USEITEM_HASTE) {
      if (hasSkillEffect(L1SkillId.STATUS_HASTE)) {
        return;
      }

      if (getInventory().consumeItem(B_POTION_OF_GREATER_HASTE_SELF, 1)
              || getInventory().consumeItem(POTION_OF_GREATER_HASTE_SELF1, 1)) {
        useHastePotion(2100);
      } else if (getInventory().consumeItem(POTION_OF_GREATER_HASTE_SELF, 1)) {
        useHastePotion(1800);
      } else if (getInventory().consumeItem(B_POTION_OF_HASTE_SELF, 1)) {
        useHastePotion(350);
      } else if (getInventory().consumeItem(POTION_OF_HASTE_SELF, 1) || getInventory().consumeItem(7006, 1) || getInventory().consumeItem(4100662, 1)) {
        useHastePotion(300);
      }
    }
  }

  public boolean nearTeleport(int nx, int ny) {
    int rdir = _random.nextInt(8);
    int dir;
    for (int i = 0; i < 8; i++) {
      dir = rdir + i;
      if (dir > 7) {
        dir -= 8;
      }
      switch (dir) {
        case 1:
          nx++;
          ny--;
          break;
        case 2:
          nx++;
          break;
        case 3:
          nx++;
          ny++;
          break;
        case 4:
          ny++;
          break;
        case 5:
          nx--;
          ny++;
          break;
        case 6:
          nx--;
          break;
        case 7:
          nx--;
          ny--;
          break;
        case 0:
          ny--;
          break;
        default:
          break;
      }
      if (getMap().isPassable(nx, ny)) {
        dir += 4;
        if (dir > 7) {
          dir -= 8;
        }
        teleport(nx, ny, dir);
        setCurrentMp(getCurrentMp() - 10);
        return true;
      }
    }
    return false;
  }

  public boolean RunTeleport(int nx, int ny) {
    if (getMap().isPassable(nx, ny)) {
      teleport(nx, ny, CommonUtil.random(7)); // 3은 방향
      return true;
    }
    return false;
  }

  public void teleport(int nx, int ny, int dir) {
    for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
      if (pc == null)
        continue;
      pc.sendPackets(new S_SkillSound(getId(), 169));
      pc.sendPackets(new S_RemoveObject(this));
      pc.removeKnownObject(this);
    }
    getMap().setPassable(getLocation(), true);

    setX(nx);
    setY(ny);
    setHeading(dir);

    if (!(this instanceof L1DollInstance) && !(this instanceof L1TowerInstance) && !(this instanceof MJCompanionInstance)) {
      getMap().setPassable(getLocation(), false);
    }
//		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {

    broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.make_stream(this));
//		}
  }

  private void npcInitialize() {
    setCurrentHp(getMaxHp());
    setCurrentMp(getMaxMp());
    allTargetClear();
    teleport(getHomeX(), getHomeY(), getHeading());
  }

  private String _nameId;
  private boolean _Agro;
  private boolean _Agrocoi;
  private boolean _Agrososc;
  private int _homeX;
  private int _homeY;
  private int _homeRnd;
  private boolean _reSpawn;
  private int _lightSize;
  private boolean _weaponBreaked;
  private int _hiddenStatus;
  private int _movementDistance = 0;
  private int _tempLawful = 0;

  public String getNameId() {
    return _nameId;
  }

  public void setNameId(String s) {
    _nameId = s;
  }

  public boolean isAgro() {
    return _Agro;
  }

  public void setAgro(boolean flag) {
    _Agro = flag;
  }

  public boolean isAgrocoi() {
    return _Agrocoi;
  }

  public void setAgrocoi(boolean flag) {
    _Agrocoi = flag;
  }

  public boolean isAgrososc() {
    return _Agrososc;
  }

  public void setAgrososc(boolean flag) {
    _Agrososc = flag;
  }

  public int getHomeX() {
    return _homeX;
  }

  public void setHomeX(int i) {
    _homeX = i;
  }

  public int getHomeY() {
    return _homeY;
  }

  public void setHomeY(int i) {
    _homeY = i;
  }

  public int getHomeRnd() {
    return _homeRnd;
  }

  public void setHomeRnd(int i) {
    _homeRnd = i;
  }

  public boolean isReSpawn() {
    return _reSpawn;
  }

  public void setRespawn(boolean flag) {
    _reSpawn = flag;
  }

  public int getLightSize() {
    return _lightSize;
  }

  public void setLightSize(int i) {
    _lightSize = i;
  }

  public boolean isWeaponBreaked() {
    return _weaponBreaked;
  }

  public void setWeaponBreaked(boolean flag) {
    _weaponBreaked = flag;
  }

  public int getHiddenStatus() {
    return _hiddenStatus;
  }

  public void setHiddenStatus(int i) {
    _hiddenStatus = i;
  }

  public int getMovementDistance() {
    return _movementDistance;
  }

  public void setMovementDistance(int i) {
    _movementDistance = i;
  }

  public int getTempLawful() {
    return _tempLawful;
  }

  public void setTempLawful(int i) {
    _tempLawful = i;
  }

  protected void setAiRunning(boolean aiRunning) {
    _aiRunning = aiRunning;
  }

  protected boolean isAiRunning() {
    return _aiRunning;
  }

  protected void setActived(boolean actived) {
    _actived = actived;
  }

  protected boolean isActived() {
    return _actived;
  }

  protected void setFirstAttack(boolean firstAttack) {
    _firstAttack = firstAttack;
  }

  protected boolean isFirstAttack() {
    return _firstAttack;
  }

  protected void setSleepTime(long sleep_time) {
    /*
     * if(getNpcId() == 7320180 && sleep_time <= 300) { try { throw new
     * Exception(sleep_time + ""); }catch(Exception e) { e.printStackTrace(); } }
     */
    _sleep_time = sleep_time;
  }

  protected long getSleepTime() {
    /*
     * if(getNpcId() == 7320180 && _sleep_time <= 300) { try { throw new
     * Exception(_sleep_time + ""); }catch(Exception e) { e.printStackTrace(); } }
     */
    return _sleep_time;
  }

  public void setDeathProcessing(boolean deathProcessing) {
    _deathProcessing = deathProcessing;
  }

  protected boolean isDeathProcessing() {
    return _deathProcessing;
  }

  public int drainMana(int drain) {
    if (_drainedMana >= Config.MagicAdSetting.MANADRAINLIMITPERNPC) {
      return 0;
    }
    int result = Math.min(drain, getCurrentMp());
    if (_drainedMana + result > Config.MagicAdSetting.MANADRAINLIMITPERNPC) {
      result = Config.MagicAdSetting.MANADRAINLIMITPERNPC - _drainedMana;
    }
    _drainedMana += result;
    return result;
  }

  public boolean _destroyed = false;

  public boolean isDestroyed() {
    return _destroyed;
  }

  protected void transform(int transformId) {
    stopHpRegeneration();
    stopMpRegeneration();
    int transformGfxId = getNpcTemplate().getTransformGfxId();
    if (transformGfxId != 0) {
      broadcastPacket(new S_SkillSound(getId(), transformGfxId));
    }
    L1Npc npcTemplate = NpcTable.getInstance().getTemplate(transformId);
    setting_template(npcTemplate);

    sendShape(getCurrentSpriteId());
    ArrayList<L1PcInstance> list = null;
    list = L1World.getInstance().getRecognizePlayer(this);
    for (L1PcInstance pc : list) {
      if (pc != null)
        onPerceive(pc);
    }
  }

  public void setRest(boolean _rest) {
    this._rest = _rest;
  }

  public boolean isRest() {
    return _rest;
  }

  public boolean isResurrect() {
    return _isResurrect;
  }

  public void setResurrect(boolean flag) {
    _isResurrect = flag;
  }

  @Override
  public synchronized void resurrect(int hp) {

    if (_destroyed) {
      return;
    }
    if (_deleteTask != null) {
      if (!_future.cancel(false)) {
        return;
      }
      _deleteTask = null;
      _future = null;
    }
    super.resurrect(hp);
    startHpRegeneration();
    startMpRegeneration();
    L1SkillUse skill = new L1SkillUse();
    skill.handleCommands(null, L1SkillId.CANCELLATION, getId(), getX(), getY(), null, 0, L1SkillUse.TYPE_LOGIN,
            this);
  }

  protected synchronized void startDeleteTimer() {
    if (_deleteTask != null) {
      return;
    }
    _deleteTask = new DeleteTimer(getId());

    long delay_time = 0;
    if (get_delete_delay() > 0)
      delay_time = get_delete_delay() * 1000;
    else
      delay_time = MapsTable.getInstance().get_monster_respawn_seconds(getMapId());

    _future = GeneralThreadPool.getInstance().schedule(_deleteTask, delay_time);
  }

  protected static class DeleteTimer implements Runnable {
    private int _id;

    protected DeleteTimer(int oId) {
      try {
        _id = oId;
// 如果對象不存在，直接返回
        if (L1World.getInstance().findObject(_id) == null)
          return;

// 如果對象不是 L1NpcInstance 類型，拋出異常
        if (!(L1World.getInstance().findObject(_id) instanceof L1NpcInstance)) {
          throw new IllegalArgumentException("只允許 L1NpcInstance [無需理會。]");
        }
      } catch (Exception e) {
// 捕獲異常並打印堆棧跟蹤
        e.printStackTrace();
        System.out.println("[DeleteTimer 錯誤] : " + L1World.getInstance().findObject(_id));
      }
    }

    @Override
    public void run() {
      L1NpcInstance npc = (L1NpcInstance) L1World.getInstance().findObject(_id);
      if (npc == null || !npc.isDead() || npc._destroyed) {
        return;
      }
      try {
        npc.deleteMe();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public boolean isInMobGroup() {
    return getMobGroupInfo() != null;
  }

  public L1MobGroupInfo getMobGroupInfo() {
    return _mobGroupInfo;
  }

  public void setMobGroupInfo(L1MobGroupInfo m) {
    _mobGroupInfo = m;
  }

  public int getMobGroupId() {
    return _mobGroupId;
  }

  public void setMobGroupId(int i) {
    _mobGroupId = i;
  }

  public void startChat(int chatTiming) {
    if (chatTiming == CHAT_TIMING_APPEARANCE && (this.isDead() || this.STATUS_Escape)) {
      return;
    }
    if (chatTiming == CHAT_TIMING_DEAD && !this.isDead()) {
      return;
    }
    if (chatTiming == CHAT_TIMING_HIDE && this.isDead()) {
      return;
    }
    if (chatTiming == CHAT_TIMING_SPAWN && this.isDead()) {
      return;
    }
    if (chatTiming == CHAT_TIMING_ESCAPE && (this.isDead() || !this.STATUS_Escape)) {
      return;
    }

    int npcId = this.getNpcTemplate().get_npcId();
    L1NewNpcChat npcChat = null;
    switch (chatTiming) {
      case CHAT_TIMING_APPEARANCE:
        npcChat = NewNpcChatTable.getInstance().getTemplateWalk(npcId);
        break;
      case CHAT_TIMING_DEAD:
        npcChat = NewNpcChatTable.getInstance().getTemplateDead(npcId);
        break;
      case CHAT_TIMING_HIDE:
        npcChat = NewNpcChatTable.getInstance().getTemplateHide(npcId);
        break;
      case CHAT_TIMING_SPAWN:
        npcChat = NewNpcChatTable.getInstance().getTemplateSpawn(npcId);
        break;
      case CHAT_TIMING_ESCAPE:
        npcChat = NewNpcChatTable.getInstance().getTemplateEscape(npcId);
        break;
      default:
        break;
    }

    if (npcChat == null) {
      return;
    }
    L1NewNpcChatTimer npcChatTimer;
    if (!npcChat.isRepeat()) {
      npcChatTimer = new L1NewNpcChatTimer(this, npcChat);
    } else {
      npcChatTimer = new L1NewNpcChatTimer(this, npcChat, npcChat.getChatInterval());
    }
    npcChatTimer.startChat();
  }

  /*
   * public void startChat(int chatTiming) { if (chatTiming ==
   * CHAT_TIMING_APPEARANCE && this.isDead()) { return; } if (chatTiming ==
   * CHAT_TIMING_DEAD && !this.isDead()) { return; } if (chatTiming ==
   * CHAT_TIMING_HIDE && this.isDead()) { return; } if (chatTiming ==
   * CHAT_TIMING_GAME_TIME && this.isDead()) { return; }
   *
   * int npcId = this.getNpcTemplate().get_npcId(); L1NpcChat npcChat = null;
   * switch (chatTiming) { case CHAT_TIMING_APPEARANCE: npcChat =
   * NpcChatTable.getInstance().getTemplateAppearance(npcId); break; case
   * CHAT_TIMING_DEAD: npcChat =
   * NpcChatTable.getInstance().getTemplateDead(npcId); break; case
   * CHAT_TIMING_HIDE: npcChat =
   * NpcChatTable.getInstance().getTemplateHide(npcId); break; case
   * CHAT_TIMING_GAME_TIME: npcChat =
   * NpcChatTable.getInstance().getTemplateGameTime(npcId); break; default: break;
   * } if (npcChat == null) { return; }
   *
   * L1NpcChatTimer npcChatTimer; if (!npcChat.isRepeat()) { npcChatTimer = new
   * L1NpcChatTimer(this, npcChat); } else { npcChatTimer = new
   * L1NpcChatTimer(this, npcChat, npcChat.getRepeatInterval()); }
   * npcChatTimer.startChat(npcChat.getStartDelayTime()); }
   */

  /** 큐브다 */
  public void setCubeTime(int CubeTime) {
    this.CubeTime = CubeTime;
  }

  public void setCubePc(L1PcInstance CubePc) {
    this.CubePc = CubePc;
  }

  public L1PcInstance CubePc() {
    return CubePc;
  }

  public boolean Cube() {
    return Cube-- <= 0;
  }

  public void set_num(int num) {
    this.num = num;
  }

  public int get_num() {
    return num;
  }

  public void randomWalk() {
    tagertClear();
    int dir = checkObject(getX(), getY(), getMapId(), _random.nextInt(20));
    if (dir != -1) {
      setSleepTime(setDirectionMoveSpeed(dir));
    }
  }

  public void randomWalk(int mul) {
    tagertClear();
    int dir = checkObject(getX(), getY(), getMapId(), _random.nextInt(20));
    if (dir != -1) {
      setSleepTime(setDirectionMoveSpeed(dir) * mul);
    }
  }

  public int calcSleepTime(int i) {
    int sleepTime = i;
    switch (getMoveState().getMoveSpeed()) {
      case 0:
        break;
      case 1:
        sleepTime -= (sleepTime * 0.25);
        break;
      case 2:
        sleepTime *= 2;
        break;
    }
    if (getMoveState().getBraveSpeed() == 1) {
      sleepTime -= (sleepTime * 0.25);
    }
    return sleepTime;
  }

  private String title;

  public String getEventTitle() {
    return title;
  }

  public void setEventTitle(String s) {
    this.title = s;
  }

  private String subnpc;

  public String getEventSubNpc() {
    return subnpc;
  }

  public void setEventSubNpc(String s) {
    this.subnpc = s;
  }

  private String subtitle;

  public String getEventSubTitle() {
    return subtitle;
  }

  public void setEventSubTitle(String s) {
    this.subtitle = s;
  }

  private String subactid;

  public String getEventSubActid() {
    return subactid;
  }

  public void setEventSubActid(String s) {
    this.subactid = s;
  }

  private boolean is_sub_npc;

  public boolean is_sub_npc() {
    return is_sub_npc;
  }

  public void set_is_sub_npc(boolean b) {
    this.is_sub_npc = b;
  }

  private long boss_time;

  /** 보스 뜰시간 */
  public long get_boss_time() {
    return boss_time;
  }

  public void set_boss_time(long l) {
    this.boss_time = l;
  }

  private long boss_end_time;

  public long get_end_boss_time() {
    return boss_end_time;
  }

  public void set_end_boss_time(long l) {
    this.boss_end_time = l;
  }

  private int _boss_ing_time;

  public int getBossIngTime() {
    return _boss_ing_time;
  }

  public void setBossIngTime(int i) {
    _boss_ing_time = i;
  }


  private String[] _yoil;

  public String[] getYoil() {
    return _yoil;
  }

  public void setYoil(String[] yoil) {
    _yoil = yoil;
  }

  private int _next_day_index;

  public int get_next_day_index() {
    return _next_day_index;
  }

  public void set_next_day_index(int _next_day_index) {
    this._next_day_index = _next_day_index;
  }

  private int boss_type;

  public int get_boss_type() {
    return boss_type;
  }

  public void set_boss_type(int i) {
    this.boss_type = i;
  }

  private int boss_hour;

  public int get_boss_hour() {
    return boss_hour;
  }

  public void set_boss_hour(int i) {
    this.boss_hour = CommonUtil.get_current(i, 0, 24);
  }

  private int boss_minute;

  public int get_boss_minute() {
    return boss_minute;
  }

  public void set_boss_minute(int i) {
    this.boss_minute = CommonUtil.get_current(i, 0, 60);
  }

  private boolean is_boss_tel;

  public boolean is_boss_tel() {
    return is_boss_tel;
  }

  public void set_is_boss_tel(boolean b) {
    this.is_boss_tel = b;
  }

  private int boss_tel_x;

  public int get_boss_tel_x() {
    return boss_tel_x;
  }

  public void set_boss_tel_x(int i) {
    this.boss_tel_x = i;
  }

  private int boss_tel_y;

  public int get_boss_tel_y() {
    return boss_tel_y;
  }

  public void set_boss_tel_y(int i) {
    this.boss_tel_y = i;
  }

  private int boss_tel_m;

  public int get_boss_tel_m() {
    return boss_tel_m;
  }

  public void set_boss_tel_m(int i) {
    this.boss_tel_m = i;
  }

  private int boss_tel_rnd;

  public int get_boss_tel_rnd() {
    return boss_tel_rnd;
  }

  public void set_boss_tel_rnd(int i) {
    this.boss_tel_rnd = i;
  }

  private int boss_tel_count;

  public int get_boss_tel_count() {
    return boss_tel_count;
  }

  public void set_boss_tel_count(int i) {
    this.boss_tel_count = i;
  }

  private boolean is_boss_msg;

  public boolean is_boss_msg() {
    return is_boss_msg;
  }

  public void set_is_boss_msg(boolean b) {
    this.is_boss_msg = b;
  }

  private String boss_msg;

  public String get_boss_msg() {
    return boss_msg;
  }

  public void set_boss_msg(String s) {
    this.boss_msg = s;
  }

  private boolean boss_yn;

  public boolean get_boss_yn() {
    return boss_yn;
  }

  public void set_boss_yn(boolean b) {
    this.boss_yn = b;
  }

  private String boss_yn_msg;

  public String get_boss_yn_msg() {
    return boss_yn_msg;
  }

  public void set_boss_yn_msg(String s) {
    this.boss_yn_msg = s;
  }

  private boolean is_boss_effect;

  public boolean is_boss_effect() {
    return is_boss_effect;
  }

  public void set_is_boss_effect(boolean b) {
    this.is_boss_effect = b;
  }

  private int boss_effect;

  public int get_boss_effect() {
    return boss_effect;
  }

  public void set_boss_effect(int i) {
    this.boss_effect = i;
  }

  private boolean is_boss_alarm;

  public boolean is_boss_alarm() {
    return is_boss_alarm;
  }

  public void set_is_boss_alarm(boolean b) {
    this.is_boss_alarm = b;
  }

  private EventTimeTemp _eventTimeTemp;

  public void setEventTimeTemp(EventTimeTemp temp) {
    _eventTimeTemp = temp;
  }

  public EventTimeTemp getEventtimeTemp() {
    return _eventTimeTemp;
  }

  public void deleteRe() {
    _destroyed = true;

    getMap().setPassable(getLocation(), true);

    if (getInventory() != null) {
      getInventory().clearItems();
    }
    allTargetClear();
    _master = null;

    L1World.getInstance().removeVisibleObject(this);
    L1World.getInstance().removeObject(this);
    List<L1PcInstance> players = L1World.getInstance().getRecognizePlayer(this);
    if (players.size() > 0) {
      S_RemoveObject s_deleteNewObject = new S_RemoveObject(this);
      for (L1PcInstance pc : players) {
        if (pc != null) {
          pc.removeKnownObject(this);
          pc.sendPackets(s_deleteNewObject);
        }
      }
    }
    removeAllKnownObjects();
    // onDecay(false);

  }

  public L1NpcInstance CastleTarget = null;

  public long skillDelayTime = 0;
  public int castgfx = 0;
  public long isMentTime = 0;

  public Object synchObject = new Object();
  private boolean _isPassObject = true;

  public boolean isPassObject() {
    return _isPassObject;
  }

  public void setPassObject(boolean b) {
    _isPassObject = b;
  }

  private long _explosion_remain_time;

  public long getExplosionTime() {
    return _explosion_remain_time;
  }

  public void setExplosionTime(long l) {
    _explosion_remain_time = l;
  }

  public void startExplosionTime(long l) {
    setExplosionTime(l);
    GeneralThreadPool.getInstance().schedule(new ExplosionCounter(this), 1000);
  }

  public class ExplosionCounter implements Runnable {
    private L1NpcInstance _npc;
    private boolean _isSending;

    public ExplosionCounter(L1NpcInstance npc) {
      _npc = npc;
      _isSending = false;
    }

    @Override
    public void run() {
      try {
        _explosion_remain_time -= 1000;
        if (!_isSending && _explosion_remain_time <= 30000) {
          _isSending = true;
          Broadcaster.broadcastPacket(_npc, S_ExplosionNoti.get(_npc, _explosion_remain_time), true);
        }
      } catch (Exception e) {

      } finally {
        if (_explosion_remain_time <= 0 || isDead())
          _explosion_remain_time = 0;
        else
          GeneralThreadPool.getInstance().schedule(this, 1000);
      }
    }
  }

  private String _shopName;

  public String getShopName() {
    return _shopName;
  }

  public void setShopName(String name) {
    _shopName = name;
  }

  public boolean isMovable() {
    return _npcTemplate.isMovable();
  }

  public boolean isActionable() {
    return _npcTemplate.isActionable();
  }

  public boolean isDynamic() {
    return _npcTemplate.isDynamic();
  }

  private static int _instanceType = -1;

  @Override
  public int getL1Type() {
    return _instanceType == -1 ? _instanceType = super.getL1Type() | MJL1Type.L1TYPE_NPC : _instanceType;
  }

  public void NpcDie() {
    try {
      setDeathProcessing(true);
      setCurrentHp(0);
      setDead(true);
      getMap().setPassable(getLocation(), true);
      setDeathProcessing(false);
      set_exp(0);
      setKarma(0);
      setLawful(0);
      allTargetClear();
      deleteMe2();
    } catch (Exception e) {
    }
  }

  public int _delete_delay = 0;

  public void set_delete_delay(int delay) {
    _delete_delay = delay;
  }

  public int get_delete_delay() {
    return _delete_delay;
  }


  public int _mapnum = 0;

  public void set_Mapnum(int mapid) {
    _mapnum = mapid;
  }

  public int get_mapnum() {
    return _mapnum;
  }

  protected void updateObject() {
  }
}
