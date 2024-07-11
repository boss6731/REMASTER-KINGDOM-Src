package l1j.server.GameSystem.Colosseum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE_NOT;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SCENE_NOTI;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1GroundInventory;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.L1SpawnUtil;

public class L1Colosseum {
	public static ColosseumInfo Info;

	public static class ColosseumInfo {
		public boolean colouse;
		public int colowatetime;
		public int round1time;
		public int boss1time;
		public int round2time;
		public int boss2time;
		public int round3time;
		public int boss3time;
		public int round4time;
		public int boss4time;
		public int round5time;
		public int boss5time;
		public int hiddenbosstime;
		public boolean spawnpotion;
		public boolean spawnbuffball;
		public int infinity_sign_id;
		public int infinity_sign_count;
		public int infinity_sign_ain;

		public ColosseumInfo() {
			colouse = true;
			colowatetime = 5;
			round1time = 1;
			boss1time = 1;
			round2time = 1;
			boss2time = 1;
			round3time = 1;
			boss3time = 1;
			round4time = 1;
			boss4time = 1;
			round5time = 1;
			boss5time = 1;
			hiddenbosstime = 1;
			spawnpotion = false;
			spawnbuffball = false;
			infinity_sign_id = 4101617;
			infinity_sign_count = 1;
			infinity_sign_ain = 100;
		}
	}

	public static void load_config() {
		try {
			Info = MJJsonUtil.fromFile("./config/colosseumInfo.json", ColosseumInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error("無法載入 ./config/colosseumInfo.json 文件.");
		}
	}

	private int _locX;
	private int _locY;
	private L1Location _location;
	private short _mapId;
	private int _locX1;
	private int _locY1;
	private int _locX2;
	private int _locY2;

	private int _ubId;
	private int _pattern;
	private boolean _isNowUb;
	private boolean _active = false;

	private int _minLevel;
	private int _maxLevel;
	private int _maxPlayer;

	private boolean _enterRoyal;
	private boolean _enterKnight;
	private boolean _enterMage;
	private boolean _enterElf;
	private boolean _enterDarkelf;
	private boolean _enterDragonknight;
	private boolean _enterBlackwizard;
	private boolean _enterWarrior;
	private boolean _enterFencer;
	private boolean _enterFLancer;
	private boolean _enterMale;
	private boolean _enterFemale;
	private boolean _usePot;
	private int _hpr;
	private int _mpr;

	private static int BEFORE_MINUTE = 5;

	private static int ubcount = 0;

	private static Random _random = new Random(System.nanoTime());

	private static final int BuffBall[] = { 7320012, 7320013, 7320014, 7320015, 7320016, 7320017 };

	private Set<Integer> _managers = new HashSet<Integer>();

	private SortedSet<Integer> _ubTimes = new TreeSet<Integer>();

	private static final Logger _log = Logger.getLogger(L1Colosseum.class.getName());

	private final ArrayList<L1PcInstance> _members = new ArrayList<L1PcInstance>();

	private void sendRoundMessage(int curRound, int npcid) {
		if (curRound == 2) {
			if (npcid == 45573) {
				sendEffect(SC_SCENE_NOTI.BAPHOMET);
			} else {
				sendEffect(SC_SCENE_NOTI.ZEROS);
			}
		} else if (curRound == 3) {
			sendEffect(SC_SCENE_NOTI.STAGE2);
		} else if (curRound == 4) {
			if (npcid == 45601) {
				sendEffect(SC_SCENE_NOTI.DEATH_KNIGHT);
			} else {
				sendEffect(SC_SCENE_NOTI.DEMON);
			}
		} else if (curRound == 5) {
			sendEffect(SC_SCENE_NOTI.STAGE3);
		} else if (curRound == 6) {
			if (npcid == 45573) {
				sendEffect(SC_SCENE_NOTI.PHOENIX);
			} else {
				sendEffect(SC_SCENE_NOTI.BIGDRAKE);
			}
		} else if (curRound == 7) {
			sendEffect(SC_SCENE_NOTI.STAGE4);
		} else if (curRound == 8) {
			if (npcid == 45752) {
				sendEffect(SC_SCENE_NOTI.BARLOG);
			} else {
				sendEffect(SC_SCENE_NOTI.REAPER);
			}
		} else if (curRound == 9) {
			sendEffect(SC_SCENE_NOTI.STAGE5);
		} else if (curRound == 10) {
			if (npcid == 120027) {
				sendEffect(SC_SCENE_NOTI.ATUBAKING);
			} else {
				sendEffect(SC_SCENE_NOTI.PBARLOG);
			}
		} else if (curRound == 11) {
			sendEffect(SC_SCENE_NOTI.HIDDEN_REPPER);
		}
	}

	private void spawnSupplies(int curRound) {
		if (curRound == 2) {
			if (L1Colosseum.Info.spawnpotion) {
				spawnGroundItem(L1ItemId.ADENA, 3000000, 50);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 3, 20);
				spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 5, 20);
				spawnGroundItem(L1ItemId.POTION_OF_GREATER_HEALING, 3, 20);
				spawnGroundItem(40317, 1, 5);
				spawnGroundItem(40079, 1, 10);
			}
			if (L1Colosseum.Info.spawnbuffball) {
				spawnBuffBall();
			}
			sendMessage("競技場管理員：第一軍已經投入完畢.", 7000);
			sendMessage("競技場管理員：約1分鐘後第二軍將開始投入.", 7000);
		} else if (curRound == 3) {
			if (L1Colosseum.Info.spawnpotion) {
				spawnGroundItem(L1ItemId.ADENA, 5000000, 50);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 7, 20);
				spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 10, 20);
				spawnGroundItem(L1ItemId.POTION_OF_GREATER_HEALING, 5, 20);
				spawnGroundItem(40317, 1, 7);
				spawnGroundItem(40093, 1, 10);
				spawnGroundItem(40079, 1, 10);
			}
			if (L1Colosseum.Info.spawnbuffball) {
				spawnBuffBall();
			}
			sendMessage("競技場管理員：第二軍已經投入完畢.", 7000);
			sendMessage("競技場管理員：約1分鐘後第三軍將開始投入.", 7000);
		} else if (curRound == 5) {
			if (L1Colosseum.Info.spawnpotion) {
				spawnGroundItem(L1ItemId.ADENA, 10000000, 50);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 7, 20);
				spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 20, 20);
				spawnGroundItem(L1ItemId.POTION_OF_GREATER_HEALING, 10, 10);
				spawnGroundItem(40317, 1, 10);
				spawnGroundItem(40094, 1, 10);
				spawnGroundItem(40079, 1, 10);
			}
			if (L1Colosseum.Info.spawnbuffball) {
				spawnBuffBall();
			}
			sendMessage("競技場管理員：第三軍的投入已完成.", 7000);
			sendMessage("競技場管理員：約1分鐘後第四軍將開始投入.", 7000);
		} else if (curRound == 7) {
			if (L1Colosseum.Info.spawnpotion) {
				spawnGroundItem(L1ItemId.ADENA, 10000000, 50);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 7, 20);
				spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 20, 20);
				spawnGroundItem(L1ItemId.POTION_OF_GREATER_HEALING, 10, 10);
				spawnGroundItem(40317, 1, 10);
				spawnGroundItem(40094, 1, 10);
				spawnGroundItem(40079, 1, 10);
			}
			if (L1Colosseum.Info.spawnbuffball) {
				spawnBuffBall();
			}
			sendMessage("競技場管理員：第四軍的投入已完成.", 7000);
			sendMessage("競技場管理員：約1分鐘後第五軍將開始投入.", 7000);
		} else if (curRound == 9) {
			if (L1Colosseum.Info.spawnpotion) {
				spawnGroundItem(L1ItemId.ADENA, 10000000, 50);
				spawnGroundItem(L1ItemId.POTION_OF_CURE_POISON, 7, 20);
				spawnGroundItem(L1ItemId.POTION_OF_EXTRA_HEALING, 20, 20);
				spawnGroundItem(L1ItemId.POTION_OF_GREATER_HEALING, 10, 10);
				spawnGroundItem(40317, 1, 10);
				spawnGroundItem(40094, 1, 10);
				spawnGroundItem(40079, 1, 10);
			}
			if (L1Colosseum.Info.spawnbuffball) {
				spawnBuffBall();
			}
			sendMessage("競技場管理員：第五軍的投入已完成.", 7000);
		}
	}

	private void removeRetiredMembers() {
		L1PcInstance[] temp = getMembersArray();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i] == null || temp[i].getMapId() != _mapId) {
				removeMember(temp[i]);
			}
		}
		temp = null;
	}

	private void spawnBuffBall() {
		L1Location loc = null;
		for (int i = 0; i < 30; i++) {
			loc = _location.randomLocation((getLocX2() - getLocX1()) / 2, false);
			L1SpawnUtil.spawn2(loc.getX(), loc.getY(), _mapId, BuffBall[_random.nextInt(BuffBall.length)], 0, 60000 * 2,
					0);
		}
	}

	private void sendMessage(String msg, int time) {
		L1PcInstance[] temp = getMembersArray();
		S_SystemMessage sm = new S_SystemMessage(msg);
		for (L1PcInstance pc : temp) {
			SC_NOTIFICATION_MESSAGE_NOT noti = SC_NOTIFICATION_MESSAGE_NOT.newInstance();
			noti.set_suffileNumber(-1);
			noti.set_notificationMessage("\\f=" + msg);
			noti.set_messageRGB(new MJSimpleRgb(0, 250, 0));
			noti.set_duration(time / 1000);
			pc.sendPackets(noti, MJEProtoMessages.SC_NOTIFICATION_MESSAGE_NOT, true);
			pc.sendPackets(sm);
		}
		temp = null;
	}

	private void sendMessagecolor(String msg, int time) {
		L1PcInstance[] temp = getMembersArray();
		S_SystemMessage sm = new S_SystemMessage(msg);
		for (L1PcInstance pc : temp) {
			SC_NOTIFICATION_MESSAGE_NOT noti = SC_NOTIFICATION_MESSAGE_NOT.newInstance();
			noti.set_suffileNumber(-1);
			noti.set_notificationMessage(msg);
			noti.set_messageRGB(new MJSimpleRgb(0, 250, 0));
			noti.set_duration(time / 1000);
			pc.sendPackets(noti, MJEProtoMessages.SC_NOTIFICATION_MESSAGE_NOT, true);
			pc.sendPackets(sm);
		}
		temp = null;
	}

	private void sendEffect(String msg) {
		L1PcInstance[] temp = getMembersArray();
		for (L1PcInstance pc : temp) {
			pc.sendPackets(SC_SCENE_NOTI.make_stream(msg), true);
		}
		temp = null;
	}

	private void spawnGroundItem(int itemId, int stackCount, int count) {
		L1Item temp = ItemTable.getInstance().getTemplate(itemId);
		if (temp == null) {
			return;
		}
		L1Location loc = null;
		L1ItemInstance item = null;
		L1GroundInventory ground = null;
		for (int i = 0; i < count; i++) {
			loc = _location.randomLocation((getLocX2() - getLocX1()) / 2, false);
			if (temp.isStackable()) {
				item = ItemTable.getInstance().createItem(itemId);
				item.setEnchantLevel(0);
				item.setCount(stackCount);
				ground = L1World.getInstance().getInventory(loc.getX(), loc.getY(), _mapId);
				if (ground.checkAddItem(item, stackCount) == L1Inventory.OK) {
					ground.storeItem(item);
				}
			} else {
				item = null;
				for (int createCount = 0; createCount < stackCount; createCount++) {
					item = ItemTable.getInstance().createItem(itemId);
					item.setEnchantLevel(0);
					ground = L1World.getInstance().getInventory(loc.getX(), loc.getY(), _mapId);
					if (ground.checkAddItem(item, stackCount) == L1Inventory.OK) {
						ground.storeItem(item);
					}
				}
			}
		}
	}

	private void clearColosseum() {
		L1MonsterInstance mob = null;
		L1Inventory inventory = null;
		for (Object obj : L1World.getInstance().getVisibleObjects(_mapId).values()) {
			if (obj instanceof L1MonsterInstance) {
				mob = (L1MonsterInstance) obj;
				if (!mob.isDead()) {
					mob.setDead(true);
					mob.setStatus(ActionCodes.ACTION_Die);
					mob.setCurrentHp(0);
					mob.deleteMe();
				}
			} else if (obj instanceof L1Inventory) {
				inventory = (L1Inventory) obj;
				inventory.clearItems();
			}
		}
		L1Colosseum.Info.colouse = true;
	}

	public L1Colosseum() {
	}

	class UbThread implements Runnable {
		private void countDown() throws InterruptedException {
			for (int loop = 0; loop < BEFORE_MINUTE * 60 - 15; loop++) {
				Thread.sleep(1000);
			}
			removeRetiredMembers();
			sendMessage("競技場管理員：怪物即將出現.", 5000); // 10 秒前
			Thread.sleep(5000);
			removeRetiredMembers();
			sendMessage("競技場管理員：10秒後比賽開始.", 5000); // 10 秒前
			sendEffect(SC_SCENE_NOTI.START_FOG);
			Thread.sleep(5000);
			sendMessage("競技場管理員： 5", 1000); // 5 秒前
			removeRetiredMembers();

			Thread.sleep(1000);
			sendMessage("競技場管理員： 4", 1000); // 4 秒前
			removeRetiredMembers();

			Thread.sleep(1000);
			sendMessage("競技場管理員： 3", 1000); // 3초전
			removeRetiredMembers();

			Thread.sleep(1000);
			sendMessage("競技場管理員： 2", 1000); // 2초전
			removeRetiredMembers();

			Thread.sleep(1000);
			sendMessage("競技場管理員： 1", 1000); // 1초전
			removeRetiredMembers();

			Thread.sleep(1000);
			removeRetiredMembers();
		}

		private void waitForNextRound(int curRound) throws InterruptedException {
			final int WAIT_TIME_TABLE[] = { L1Colosseum.Info.round1time, L1Colosseum.Info.boss1time,
					L1Colosseum.Info.round2time, L1Colosseum.Info.boss2time, L1Colosseum.Info.round3time,
					L1Colosseum.Info.boss3time, L1Colosseum.Info.round4time, L1Colosseum.Info.boss4time,
					L1Colosseum.Info.round5time, L1Colosseum.Info.boss5time, L1Colosseum.Info.hiddenbosstime };
			int wait = WAIT_TIME_TABLE[curRound - 1];
			for (int i = 0; i < wait; i++) {
				Thread.sleep(1000);
			}
			removeRetiredMembers();
		}

		@Override
		public void run() {
			try {
				setActive(true);
				countDown();
				setNowUb(true);
				L1ColosseumPattern pattern = null;
				ArrayList<L1ColosseumSpawn> spawnList = null;
				for (int round = 1; round <= ubcount; round++) {
					pattern = ColosseumSpawnTable.getInstance().getPattern(_ubId, _pattern);
					spawnList = pattern.getSpawnList(round);
					if (_ubId == 1) {
						sendRoundMessage(round, spawnList.get(0).getNpcTemplateId());
					}
					if (round % 2 == 0) {
						Thread.sleep(5000);
					} else if (round == 11) {
						Thread.sleep(10000);
					}
					for (L1ColosseumSpawn spawn : spawnList) {
						if (getMembersCount() > 0) {
							spawn.spawnAll();
							if (spawn.get_message() != null) {
								if (!spawn.get_message().equalsIgnoreCase("")) {
									sendMessagecolor(spawn.get_message(), 5);
								}
							}
						}
						Thread.sleep(spawn.getSpawnDelay() * 1000);
					}

					if (getMembersCount() > 0) {
						spawnSupplies(round);
					}

					for (L1PcInstance pc : getMembersArray()) {
						ColosseumTable.getInstance().writeUbScore(getUbId(), pc);
					}
					waitForNextRound(round);
				}

				for (L1PcInstance pc : getMembersArray()) {
					int[] loc = Getback.GetBack_Location(pc, true);
					pc.start_teleport(loc[0], loc[1], loc[2], pc.getHeading(), 169, true);
					removeMember(pc);
				}
				clearColosseum();
				setActive(false);
				setNowUb(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void start() {
		_pattern = 1;
		ubcount = 11;

		UbThread ub = new UbThread();
		GeneralThreadPool.getInstance().execute(ub);
	}

	public void addMember(L1PcInstance pc) {
		if (!_members.contains(pc)) {
			_members.add(pc);
		}
	}

	public void removeMember(L1PcInstance pc) {
		_members.remove(pc);
	}

	public void clearMembers() {
		_members.clear();
	}

	public boolean isMember(L1PcInstance pc) {
		return _members.contains(pc);
	}

	public L1PcInstance[] getMembersArray() {

		return _members.toArray(new L1PcInstance[_members.size()]);
	}

	public int getMembersCount() {
		return _members.size();
	}

	private void setNowUb(boolean i) {
		_isNowUb = i;
	}

	public boolean isNowUb() {
		return _isNowUb;
	}

	public int getUbId() {
		return _ubId;
	}

	public void setUbId(int id) {
		_ubId = id;
	}

	public short getMapId() {
		return _mapId;
	}

	public void setMapId(short mapId) {
		this._mapId = mapId;
	}

	public int getMinLevel() {
		return _minLevel;
	}

	public void setMinLevel(int level) {
		_minLevel = level;
	}

	public int getMaxLevel() {
		return _maxLevel;
	}

	public void setMaxLevel(int level) {
		_maxLevel = level;
	}

	public int getMaxPlayer() {
		return _maxPlayer;
	}

	public void setMaxPlayer(int count) {
		_maxPlayer = count;
	}

	public void setEnterRoyal(boolean enterRoyal) {
		this._enterRoyal = enterRoyal;
	}

	public void setEnterKnight(boolean enterKnight) {
		this._enterKnight = enterKnight;
	}

	public void setEnterMage(boolean enterMage) {
		this._enterMage = enterMage;
	}

	public void setEnterElf(boolean enterElf) {
		this._enterElf = enterElf;
	}

	public void setEnterDarkelf(boolean enterDarkelf) {
		this._enterDarkelf = enterDarkelf;
	}

	public void setEnterDragonknight(boolean enterDragonknight) {
		this._enterDragonknight = enterDragonknight;
	}

	public void setEnterBlackwizard(boolean enterBlackwizard) {
		this._enterBlackwizard = enterBlackwizard;
	}

	public void setEnterWarrior(boolean enter戰士) {
		this._enterWarrior = enter戰士;
	}

	public void setEnterFencer(boolean enter劍士) {
		this._enterFencer = enter劍士;
	}

	public void setEnterFLancer(boolean enter黃金槍騎) {
		this._enterFLancer = enter黃金槍騎;
	}

	public void setEnterMale(boolean enterMale) {
		this._enterMale = enterMale;
	}

	public void setEnterFemale(boolean enterFemale) {
		this._enterFemale = enterFemale;
	}

	public boolean canUsePot() {
		return _usePot;
	}

	public void setUsePot(boolean usePot) {
		this._usePot = usePot;
	}

	public int getHpr() {
		return _hpr;
	}

	public void setHpr(int hpr) {
		this._hpr = hpr;
	}

	public int getMpr() {
		return _mpr;
	}

	public void setMpr(int mpr) {
		this._mpr = mpr;
	}

	public int getLocX1() {
		return _locX1;
	}

	public void setLocX1(int locX1) {
		this._locX1 = locX1;
	}

	public int getLocY1() {
		return _locY1;
	}

	public void setLocY1(int locY1) {
		this._locY1 = locY1;
	}

	public int getLocX2() {
		return _locX2;
	}

	public void setLocX2(int locX2) {
		this._locX2 = locX2;
	}

	public int getLocY2() {
		return _locY2;
	}

	public void setLocY2(int locY2) {
		this._locY2 = locY2;
	}

	public void resetLoc() {
		_locX = (_locX2 + _locX1) / 2;
		_locY = (_locY2 + _locY1) / 2;
		_location = new L1Location(_locX, _locY, _mapId);
	}

	public L1Location getLocation() {
		return _location;
	}

	public void addManager(int npcId) {
		_managers.add(npcId);
	}

	public boolean containsManager(int npcId) {
		return _managers.contains(npcId);
	}

	public void addUbTime(int time) {
		_ubTimes.add(time);
	}

	public String getNextUbTime() {
		return intToTimeFormat(nextUbTime());
	}

	private int nextUbTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		int nowTime = Integer.valueOf(sdf.format(getRealTime().getTime()));
		SortedSet<Integer> tailSet = _ubTimes.tailSet(nowTime);
		if (tailSet.isEmpty()) {
			tailSet = _ubTimes;
		}
		return tailSet.first();
	}

	private static String intToTimeFormat(int n) {
		return n / 100 + ":" + n % 100 / 10 + "" + n % 10;
	}

	private static Calendar getRealTime() {
		TimeZone _tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
		Calendar cal = Calendar.getInstance(_tz);
		return cal;
	}

	public boolean checkUbTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		Calendar realTime = getRealTime();
		realTime.add(Calendar.MINUTE, BEFORE_MINUTE);
		int nowTime = Integer.valueOf(sdf.format(realTime.getTime()));
		return _ubTimes.contains(nowTime);
	}

	private void setActive(boolean f) {
		_active = f;
	}

	public boolean isActive() {
		return _active;
	}

	public boolean canPcEnter(L1PcInstance pc) {
		_log.log(Level.FINE, "pcname=" + pc.getName() + " ubid=" + _ubId
				+ " minlvl=" + _minLevel + " maxlvl=" + _maxLevel);
		if (!IntRange.includes(pc.getLevel(), _minLevel, _maxLevel)) {
			return false;
		}

		if (!((pc.isCrown() && _enterRoyal)
				|| ((pc.isKnight()) && _enterKnight)
				|| (pc.isWizard() && _enterMage) || (pc.isElf() && _enterElf)
				|| (pc.isDarkelf() && _enterDarkelf)
				|| (pc.isDragonknight() && _enterDragonknight)
				|| (pc.isBlackwizard() && _enterBlackwizard)
				|| (pc.is전사() && _enterWarrior)
				|| (pc.isFencer() && _enterFencer)
				|| (pc.isLancer() && _enterFLancer))) {
			return false;
		}

		return true;
	}

	private String[] _ubInfo;

	public String[] makeUbInfoStrings() {
		if (_ubInfo != null) {
			return _ubInfo;
		}
		String nextUbTime = getNextUbTime();
		StringBuilder classesBuff = new StringBuilder();
		if (_enterBlackwizard) {
			classesBuff.append("幻術師 ");
		}
		if (_enterDragonknight) {
			classesBuff.append("龍騎士 ");
		}
		if (_enterDarkelf) {
			classesBuff.append("黑暗妖精 ");
		}
		if (_enterMage) {
			classesBuff.append("法師 ");
		}
		if (_enterElf) {
			classesBuff.append("妖精 ");
		}
		if (_enterKnight) {
			classesBuff.append("騎士 ");
		}
		if (_enterRoyal) {
			classesBuff.append("王族 ");
		}
		if (_enterWarrior) {
			classesBuff.append("戰士 ");
		}
		if (_enterFencer) {
			classesBuff.append("劍士 ");
		}
		if (_enterFLancer) {
			classesBuff.append("黃金槍騎 ");
		}
		String classes = classesBuff.toString().trim();

		StringBuilder sexBuff = new StringBuilder();
		if (_enterMale) {
			sexBuff.append("男性 ");
		}
		if (_enterFemale) {
			sexBuff.append("女性 ");
		}
		String sex = sexBuff.toString().trim();
		String loLevel = String.valueOf(_minLevel);
		String hiLevel = String.valueOf(_maxLevel);
		String teleport = _location.getMap().isEscapable() ? "可能" : "不可能";
		String res = _location.getMap().isUseResurrection() ? "可能" : "不可能";
		String pot = "可能";
		String hpr = String.valueOf(_hpr);
		String mpr = String.valueOf(_mpr);
		String summon = _location.getMap().isTakePets() ? "可能" : "不可能";
		String summon2 = _location.getMap().isRecallPets() ? "可能" : "不可能";
		_ubInfo = new String[] { nextUbTime, classes, sex, loLevel, hiLevel,
				teleport, res, pot, hpr, mpr, summon, summon2 };
		return _ubInfo;
	}
}
