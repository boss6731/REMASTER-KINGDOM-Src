package l1j.server.server.server.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import MJFX.UIAdapter.MJUIAdapter;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJJsonUtil;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.MJMessengerInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.server.datatables.RaceTable;
import l1j.server.server.server.templates.L1Racer;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1RaceTicket;

import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.templates.WareHouseLeaveType;
import l1j.server.server.utils.SQLUtil;

public class BugRaceController implements Runnable {
	private static BugRaceController _instance;
	public static final String[] bugStateStrings = new String[] {"差", "好", "差", "好", "普通"};
	public static BugRaceInfo bugRaceInfo;

	public static final int RACE_SELLER_NPCID = 8502074;

	public static class BugRaceInfo{
		public int BugBasicSpeed;
		public boolean IsRateRealTimeUpdate;
		public double BugMinRate;
		public double BUGREDEMPTION;
		public double[] BugMaxRates;
		public boolean IsAutoTumble;
		public int MaxTumbleProbability;
		public int[] BugStateSpeeds;
		public BugRaceInfo() {
			BugBasicSpeed = 250;
			IsRateRealTimeUpdate = true;
			BugMinRate = 2.0;
			BUGREDEMPTION = 0.95;
			BugMaxRates = new double[] {10.0, 10.0, 10.0, 10.0, 10.0};
			IsAutoTumble = true;
			MaxTumbleProbability = 60;
			BugStateSpeeds = new int[] {-2, -1, 1, 2, 0};
		}
	}

	public static void load_config() {
		try {
			bugRaceInfo = MJJsonUtil.fromFile("./config/bug_race.json", BugRaceInfo.class);
		}catch(Exception e) {
			e.printStackTrace();
			throw new Error("載入 ./config/bug_race.json 文件失敗。");
		}
	}

	//TODO 錯誤間隔時間調整，1表示1分鐘
	private static int RACE_INTERVAL = 1 * 60 * 1000;

	public static final int EXECUTE_STATUS_NONE = 0;
	public static final int EXECUTE_STATUS_PREPARE = 1;
	public static final int EXECUTE_STATUS_READY = 2;
	public static final int EXECUTE_STATUS_STANDBY = 3;
	public static final int EXECUTE_STATUS_PROGRESS = 4;
	public static final int EXECUTE_STATUS_FINALIZE = 5;

	private int _executeStatus = EXECUTE_STATUS_NONE;

	public int _raceCount = 0;
	long _nextRaceTime = System.currentTimeMillis() + 60 * 1000;
	public int _bugRaceState = 2;

	public int _ticketSellRemainTime;
	public int _raceWatingTime;
	public int _currentBroadcastRacer;

	L1NpcInstance[] _npc = new L1NpcInstance[3];
	MJMessengerInstance _box_npc;

	public int[] _ticketCount = new int[5];
	private static Random _rnd = new Random(System.nanoTime());
	private static DecimalFormat _df = new DecimalFormat("#.#");

	public int _ranking = 0;
	public boolean _complete = false;

	List<L1ShopItem> _purchasingList = new ArrayList<L1ShopItem>();
	public L1NpcInstance[] _littleBugBear = new L1NpcInstance[5];

	int Lucky = 0;
	private static Random rnd = new Random(System.nanoTime());

	/** 添加錯誤間隔 **/
	private final HashMap<Integer, L1RaceTicket> _race = new HashMap<Integer, L1RaceTicket>(20);//32
	//private L1Item _allTemplates[] = new L1Item[9000000];
	//private HashMap<Integer, L1Item> _allTemplates = new HashMap<Integer, L1Item>(32);

	public HashMap<Integer, L1RaceTicket> getAllTemplates() {
		return _race;
	}

	public Collection<L1Shop> get_shops(){
		return _shops.values();
	}

	private int Start_X[] = { 33522, 33520, 33518, 33516, 33514 };
	private int Start_Y[] = { 32861, 32863, 32865, 32867, 32869 };

	private static final ArrayList<BugStruct> _bugs;
	static{
		//TODO 魔法娃娃競賽
		_bugs = new ArrayList<BugStruct>(20);//32
		_bugs.add(new BugStruct(1, 16081, "庫茲"));
		_bugs.add(new BugStruct(2, 16082, "冰女"));
		_bugs.add(new BugStruct(3, 16083, "巴風特"));
		_bugs.add(new BugStruct(4, 16084, "拉基"));
		_bugs.add(new BugStruct(5, 16085, "朗卡"));
		_bugs.add(new BugStruct(6, 16086, "死亡"));
		_bugs.add(new BugStruct(7, 16087, "惡魔"));
		_bugs.add(new BugStruct(8, 16088, "木乃伊"));
		_bugs.add(new BugStruct(9, 16089, "吸血鬼"));
		_bugs.add(new BugStruct(10, 16090, "阿里"));
		_bugs.add(new BugStruct(11, 16091, "希爾"));
		_bugs.add(new BugStruct(12, 16092, "那巴爾"));
		_bugs.add(new BugStruct(13, 16093, "賽依"));
		_bugs.add(new BugStruct(14, 16094, "巫妖"));
		_bugs.add(new BugStruct(15, 16095, "達格爾"));
		_bugs.add(new BugStruct(16, 16096, "科阿"));
		_bugs.add(new BugStruct(17, 16097, "奎妮"));
		_bugs.add(new BugStruct(18, 16098, "卡米"));
		_bugs.add(new BugStruct(19, 16099, "賈伊"));
		_bugs.add(new BugStruct(20, 16100, "拉巴"));

		//TODO 錯誤間隔
		/*_bugs = new ArrayList<BugStruct>(20);//32
		_bugs.add(new BugStruct(1, 3478, "貝克杜"));
		_bugs.add(new BugStruct(2, 3479, "圖圖"));
		_bugs.add(new BugStruct(3, 3480, "巴吉"));
		_bugs.add(new BugStruct(4, 3481, "厄魯"));
		_bugs.add(new BugStruct(5, 3497, "傑弗里"));
		_bugs.add(new BugStruct(6, 3498, "凱"));
		_bugs.add(new BugStruct(7, 3499, "阿道夫"));
		_bugs.add(new BugStruct(8, 3500, "歐巴富特"));
		_bugs.add(new BugStruct(9, 3501, "倫普斯"));
		_bugs.add(new BugStruct(10, 3502, "布卡"));
		_bugs.add(new BugStruct(11, 3503, "格魯克"));
		_bugs.add(new BugStruct(12, 3504, "昆德拉"));
		_bugs.add(new BugStruct(13, 3505, "庫瑪托"));
		_bugs.add(new BugStruct(14, 3506, "杜雷克"));
		_bugs.add(new BugStruct(15, 3507, "格羅頓"));
		_bugs.add(new BugStruct(16, 3508, "奎尼巴"));
		_bugs.add(new BugStruct(17, 3509, "普魯托"));
		_bugs.add(new BugStruct(18, 3510, "杜里巴"));
		_bugs.add(new BugStruct(19, 3511, "巴魯厄爾"));
		_bugs.add(new BugStruct(20, 3512, "伊萊扎"));*/
	}

	public static int[] _time = new int[5];
	public static String _first = null;

	public int[] _ticket = { 0, 0, 0, 0, 0 };

	private HashMap<Integer, BugTicketInfo> m_tickets = new HashMap<Integer, BugTicketInfo>(20);//24

	// 勝率初始化
	public double[] _winRate = { 0, 0, 0, 0, 0 };
	public double[] _winViewRate = { 0, 0, 0, 0, 0 };
	// 狀態初始化
//    public String[] _bugCondition = { "非常好", "好", "差", "非常差", "普通" };
	public String[] _bugCondition = { "普通", "好", "差", "好", "普通" };

	public double _ration[] = { 0, 0, 0, 0, 0 };

	public boolean[] _is_downs = new boolean[]{false, false, false, false, false};

	public int _round;

	public static BugRaceController getInstance() {
		if (_instance == null) {
			_instance = new BugRaceController();
		}
		return _instance;
	}

	private BugRaceController(){
		load_config();
		_round = 0;
		Selector.exec("select max(round) as r_nd from bug_history", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				if(rs.next())
					_round = rs.getInt("r_nd");
			}
		});
	}

	public static class BugTicketInfo{
		public int racerId;
		public int itemId;
		public int packCount;
		public int converter_itemid;
	}

	public BugTicketInfo find_ticket_info(int itemid){
		return m_tickets.get(itemid);
	}

	public boolean sellings() {
		return _executeStatus == EXECUTE_STATUS_PREPARE ||
				_executeStatus == EXECUTE_STATUS_READY;
	}

	public void run() {
		try {
			switch (_executeStatus) {
				case EXECUTE_STATUS_NONE: {
					if (checkStartRace()) {
						initRaceGame();
						_executeStatus = EXECUTE_STATUS_PREPARE;
//					GeneralThreadPool.getInstance().schedule(this, 60 * 1000L);
						GeneralThreadPool.getInstance().schedule(this, 1000L);
					} else {
						GeneralThreadPool.getInstance().schedule(this, 1000L); // 1秒
					}
				}
				break;
				case EXECUTE_STATUS_PREPARE: {
					startSellTicket();
					_executeStatus = EXECUTE_STATUS_READY;
					GeneralThreadPool.getInstance().schedule(this, 1000L);
				}
				break;
				case EXECUTE_STATUS_READY: {
					long remainTime = checkTicketSellTime();
					if (remainTime > 0) {
						GeneralThreadPool.getInstance().schedule(this, remainTime);
					} else {
						_executeStatus = EXECUTE_STATUS_STANDBY;
						GeneralThreadPool.getInstance().schedule(this, 1000L);
					}
				}
				break;

				case EXECUTE_STATUS_STANDBY: {
					if (checkWatingTime()) {
						startBugRace();
						_executeStatus = EXECUTE_STATUS_PROGRESS;
					}
					GeneralThreadPool.getInstance().schedule(this, 1000L);
				}
				break;

				case EXECUTE_STATUS_PROGRESS: {
					if (broadcastBettingRate()) {
						if (_complete) {
							_executeStatus = EXECUTE_STATUS_FINALIZE;
						}
					}
					GeneralThreadPool.getInstance().schedule(this, 1000L);
				}
				break;
				case EXECUTE_STATUS_FINALIZE: {
					wrapUpRace();
					_executeStatus = EXECUTE_STATUS_NONE;
					GeneralThreadPool.getInstance().schedule(this, 1000L);
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean checkStartRace() {
		long currentTime = System.currentTimeMillis();
		if (_nextRaceTime < currentTime) {
			_nextRaceTime = currentTime + RACE_INTERVAL;
			return true;
		}
		return false;
	}

	public void initRaceGame() {
		try {
			++_round;
			_ranking = 0;
			_complete = false;
			_first = null;

			Lucky = rnd.nextInt(50);

			broadcastNpc("請出售您持有的賽跑票。");
			_is_downs = new boolean[]{false, false, false, false, false};

			// Bugbear Race初始化
			initNpc();
			// 商店NPC初始化
			initShopNpc();
			// 設定熊怪跑步速度
			sleepTime();
			// 熊怪初始化及加載
			loadDog();
			// 勝率初始化
			initWinRate();
			// 遊戲中
			doorAction(false);
		} catch (Exception e) {
		}
	}

	public synchronized void on_buy_ticket(int ticket_id, int amount){
		BugTicketInfo tInfo = m_tickets.get(ticket_id);
		if(tInfo == null)
			return;

		int ticket_count = _ticketCount[tInfo.racerId] + (amount * tInfo.packCount);
		_ticketCount[tInfo.racerId] = ticket_count;
		SettingRate();

	}

	public void initTicketCount() {
		for (int row = 0; row < 5; row++) {
			this._ticketCount[row] = 20;
			_ration[row] = 5.0D;
		}
		SettingRate();
	}

	// 初始化已創建的NPC對象。
	public void initNpc() {
		L1NpcInstance n = null;
		for (Object obj : L1World.getInstance().getVisibleObjects(4).values()) {
			if (obj instanceof L1NpcInstance) {
				n = (L1NpcInstance) obj;
				if (n.getNpcTemplate().get_npcId() == 70041) {
					_npc[0] = n;
				} else if (n.getNpcTemplate().get_npcId() == RACE_SELLER_NPCID) {
					_npc[1] = n;
				} else if (n.getNpcTemplate().get_npcId() == 70042) {
					_npc[2] = n;
				} else if(n.getNpcTemplate().get_npcId() == 8500200){
					_box_npc = (MJMessengerInstance)n;
//					_box_npc.set_is_ghost(true);
				}
			}
		}
	}

	private static ConcurrentHashMap<Integer, L1Shop> _shops = new ConcurrentHashMap<Integer, L1Shop>();
	public void initShopNpc() {
		List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
		make_shop(BugRaceController.RACE_SELLER_NPCID, sellingList);
		make_shop(70041, sellingList);
		make_shop(70042, sellingList);
		/*
		L1Shop shop = new L1Shop(70035, sellingList, _purchasingList);
		ShopTable.getInstance().addShop(70035, shop);
		L1Shop shop1 = new L1Shop(70041, sellingList, _purchasingList);
		ShopTable.getInstance().addShop(70041, shop1);
		L1Shop shop2 = new L1Shop(70042, sellingList, _purchasingList);
		ShopTable.getInstance().addShop(70042, shop2);*/
	}

	private L1Shop make_shop(int npcid, List<L1ShopItem> sellings) {
		L1Shop shop = new L1Shop(npcid, sellings, _purchasingList);
		_shops.put(npcid, shop);
		ShopTable.getInstance().addShop(npcid, shop);
		return shop;
	}

	private void sleepTime() {
		for (int i = 0; i < 5; i++) {
			int bugState = _rnd.nextInt(5);
			int addValue = 0;

			_bugCondition[i] = bugStateStrings[bugState];
			addValue = bugRaceInfo.BugStateSpeeds[bugState];
			_time[i] = bugRaceInfo.BugBasicSpeed + addValue;
		}
	}

	// 勝率處理
	public void initWinRate() {
		double presentation_rate = MJRnd.next_double(20, 30);
		for (int i = 0; i < 5; i++) {
			_winRate[i] = Double.parseDouble(_df.format(MJRnd.next_double(20.1, 21)));
			_winViewRate[i] = Double.parseDouble(_df.format(presentation_rate));
		}
	}

	private FastTable<L1NpcInstance> list = new FastTable<L1NpcInstance>();

	public L1NpcInstance find_bug(int object_id){
		for(L1NpcInstance npc : list){
			if(npc.getId() == object_id)
				return npc;
		}
		return null;
	}

	public boolean down_bug(int object_id){
		boolean do_down = false;
		for(int i=list.size() - 1; i>=0; --i){
			L1NpcInstance npc = list.get(i);
			if(npc.getId() != object_id)
				continue;

			_is_downs[i] = true;
			do_down = true;
			break;
		}
		return do_down;
	}

	public Iterator<L1NpcInstance> get_race_iter() {
		return list.iterator();
	}

	public void setSpeed(int i, int speed){
		_time[i] = speed;
	}

	public int getSpeed(int i){
		return _time[i];
	}

	private void loadDog() {
		L1Npc dogs = null;
		List<L1PcInstance> players = null;

		list.clear(); // -- 開始前初始化。

		Collections.shuffle(_bugs);
		for (int m = 0; m < 5; ++m) {
			try {
				BugStruct bs = _bugs.get(m);
				dogs = new L1Npc();
				dogs.set_family(0);
				dogs.set_agrofamily(0);
				dogs.set_picupitem(false);

				Object[] parameters = { dogs };

				_littleBugBear[m] = (L1NpcInstance) Class.forName("l1j.server.server.model.Instance.L1NpcInstance")
						.getConstructors()[0].newInstance(parameters);
				_littleBugBear[m].setCurrentSprite(bs.gfx);

				_littleBugBear[m].setNameId(String.format("#%d %s", bs.id, bs.name));
//				_littleBugBear[m].setNameId(bs.name);
				_littleBugBear[m].setName(bs.name);
				_littleBugBear[m].set_num(bs.id);
				_littleBugBear[m].setX(Start_X[m]);
				_littleBugBear[m].setY(Start_Y[m]);
				_littleBugBear[m].setMap((short) 4);
				_littleBugBear[m].setHeading(6);
				_littleBugBear[m].setId(IdFactory.getInstance().nextId());

				L1World.getInstance().storeObject(_littleBugBear[m]);
				L1World.getInstance().addVisibleObject(_littleBugBear[m]);

				list.add(_littleBugBear[m]);

				players = L1World.getInstance().getVisiblePlayer(_littleBugBear[m]);
				for (L1PcInstance member : players) {
					if (member != null) {
						member.updateObject();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void broadcastNpc(String msg) {
		for (int i = 0; i < _npc.length; ++i) {
			if (_npc[i] != null) {
				_npc[i].broadcastPacket(new S_NpcChatPacket(_npc[i], msg, 2));
			}
		}
		MJUIAdapter.on_minigame_append(String.format("<Bugbear Race %d>%s", _round, msg));
	}

	public void broadcastBox(String msg){
		if(_box_npc != null){
			_box_npc.set_current_message(String.format("\\f3%s", msg));
			_box_npc.broadcast_message();
//			_box_npc.broadcastPacket(SC_WORLD_PUT_OBJECT_NOTI.new_namechat_isntance(_box_npc, msg, false), MJEProtoMessages.SC_WORLD_PUT_OBJECT_NOTI, true, false);
//			_box_npc.broadcastPacket(new S_NpcChatPacket(_box_npc, msg, 4));
		}
	}

	public void 우승자멘트(String msg) {
		for (int i = 0; i < _npc.length; ++i) {
			if (_npc[i] != null) {
				_npc[i].broadcastPacket(new S_NpcChatPacket(_npc[i], msg, 2));
			}
		}
		MJUIAdapter.on_minigame_append(String.format("<Bugbear Race%d>%s", _round, msg));
	}

	public void doorAction(boolean open) {
		L1DoorInstance door = null;
		for (Object object : L1World.getInstance().getObject()) {
			if (object instanceof L1DoorInstance) {
				door = (L1DoorInstance) object;
				if (door != null && door.equalsCurrentSprite(1487)) {
					if (open && door.getOpenStatus() == ActionCodes.ACTION_Close) {
						door.open();
					}
					if (!open && door.getOpenStatus() == ActionCodes.ACTION_Open) {
						door.close();
					}
				}
			}
		}
	}

	public void startSellTicket() {
		LoadNpcShopList();
		// 總銷售張數初始化
		initTicketCount();
		broadcastNpc("賽跑票的販售已經開始。");
		this.setBugState(0);
		_ticketSellRemainTime = 60 * 3;
	}

	public long checkTicketSellTime() {
		if (_ticketSellRemainTime == 3 * 60) {
			_ticketSellRemainTime -= 60;
			broadcastNpc("Bugbear Race開始前 3 分鐘!!");
			broadcastBox("販售截止：3 分鐘前");
			return 60 * 1000;
		} else if (_ticketSellRemainTime == 2 * 60) {
			_ticketSellRemainTime -= 60;
			broadcastNpc("Bugbear Race開始前 2 分鐘!!");
			//broadcastBox("\aY販售截止：2 分鐘前");
			broadcastBox("販售截止：2 分鐘前");
			return 60 * 1000;
		} else if (_ticketSellRemainTime == 1 * 60) {
			_ticketSellRemainTime -= 60;
			broadcastNpc("Bugbear Race開始前 1 分鐘!!");
			//broadcastBox("\aY販售截止：1 分鐘前");//顏色未顯示
			broadcastBox("販售截止：1 分鐘前");//顏色未顯示
			GeneralThreadPool.getInstance().schedule(new Runnable(){
				@Override
				public void run(){
					try{
						broadcastNpc("30秒後賽跑票販售將截止。");
						for(int i=30; i>=1; --i){
							//broadcastBox(String.format("\\aG販售截止：%d 秒前", i));//販售截止的NPC不會進行廣播，只有Mori和Reti等會進行
							broadcastBox(String.format("販售截止：%d 秒前", i));//販售截止的NPC不會進行廣播，只有Mori和Reti等會進行
							Thread.sleep(1000);
						}
						//broadcastBox("\\aS販售已結束。");
						broadcastBox("販售已結束。");
					}catch(Exception e){}
					return null;
				}
			}, 35000);
			return 60 * 1000;
		} else if (_ticketSellRemainTime == 1 * 30) {//不會往下讀取
			_ticketSellRemainTime = 0;
    /*broadcastNpc("30秒後賽跑票販售將截止。");
    GeneralThreadPool.getInstance().execute(new Runnable(){
        @override
        public void run(){
            try{
                for(int i=30; i>=1; ++i){
                    broadcastBox(String.format("\aG販售截止：%d 秒前", i));//販售截止的NPC不會進行廣播，只有Mori和Reti等會進行
                    Thread.sleep(1000);
                }
                broadcastBox("\f9販售已結束。");
            } catch(Exception e) {}
        }
    });*/
			return 30 * 1000;
		}
		initShopNpc();
		broadcastNpc("準備出發！");
		SettingRate();
		_raceWatingTime = 5;
		return 0;
	}

	private boolean checkWatingTime() {
		setBugState(1);
		if (_raceWatingTime > 0) {
			broadcastNpc(_raceWatingTime + "秒");
			--_raceWatingTime;

			return false;
		}

		return true;
	}

	private void startBugRace() {

		broadcastNpc("開始");
		doorAction(true);

		StartGame();

		_currentBroadcastRacer = 0;
	}

	private boolean broadcastBettingRate() {
		if (_currentBroadcastRacer == 5) {
			return true;
		}

		if (_currentBroadcastRacer == 0) {
			broadcastNpc("將公布投注賠率。");
		}

		broadcastNpc(_littleBugBear[_currentBroadcastRacer].getNameId() + ": " + _ration[_currentBroadcastRacer] + " ");

		++_currentBroadcastRacer;

		return false;
	}

	/*
	private int[] ticket_count = { CommonUtil.random(150, 1000), // 自動購買人工智慧
	CommonUtil.random(150, 1000), // 自動購買人工智慧
	CommonUtil.random(150, 1000), // 自動購買人工智慧
	CommonUtil.random(150, 1000), // 自動購買人工智慧
	CommonUtil.random(150, 1000), // 自動購買人工智慧
	};*/

	/*public void SettingRate() {// 設定賠率
    for (int row = 0; row < 5; row++) {
    double rate = 0;
    // int total = this.getTotalTicketCount();
    int num_0 = ticket_count[0],
    num_1 = ticket_count[1],
    num_2 = ticket_count[2],
    num_3 = ticket_count[3],
    num_4 = ticket_count[4];
    int total = ((num_0 + num_1 + num_2 + num_3 + num_4) + this.getTotalTicketCount());
    // int cnt = this._ticketCount[row];

    int cnt = (ticket_count[row] + this._ticketCount[row]);
    if (total == 0)
    total = 1;

    if (cnt != 0) {
    rate = (double) total / (double) cnt;
    if (Lucky == row) {
    rate *= 1.0;
    }
    // System.out.println(cnt + " !!!");
    }

    // -- 以一定機率調整賠率
    // -- 彩池效果
    int i = _random.nextInt(100);
    if (i < Config.BugBug) {
    broadcastNpc("投注彩池觸發！[" + Config.BugBug1 + "]倍賠率!!!");
    rate *= Config.BugBug1;
    }
    _ration[row] = Double.parseDouble(_df.format(rate));
    }
    }*/
// TODO 設定賠率
	public void SettingRate() {
		double total = getTotalTicketCount();
		ArrayList<RationInfo> temporary_ration = new ArrayList<RationInfo>(5);
		for (int row = 0; row < 5; row++) {
			double cnt = _ticketCount[row];
			RationInfo rInfo = new RationInfo();
			rInfo.idx = row;
			rInfo.ration = _ration[row];
			if(cnt <= 0){
				while(rInfo.ration <= bugRaceInfo.BugMinRate)
					rInfo.ration = MJRnd.next_double() * 10;
			}else{
				rInfo.ration = total / cnt;
			}
			if(rInfo.ration <= bugRaceInfo.BugMinRate)
				rInfo.ration = bugRaceInfo.BugMinRate + MJRnd.next_double();
			temporary_ration.add(rInfo);
		}
		Collections.sort(temporary_ration);
		for(int i=0; i<5; ++i){
			double need_range = bugRaceInfo.BugMaxRates[i] - 1D;
			double range = bugRaceInfo.BugMaxRates[i];
			RationInfo rInfo = temporary_ration.get(i);
			if(rInfo.ration >= range){
				double d = MJRnd.next_double();
				rInfo.ration = need_range + d;
			}
			_ration[rInfo.idx] = Double.parseDouble(_df.format(rInfo.ration));
		}
	}

	static class RationInfo implements Comparable<RationInfo>{
		int idx;
		double ration;
		@Override
		public int compareTo(RationInfo o) {
			double d = o.ration - ration;
			return d > 0 ? 1 : d < 0 ? -1 : 0;
		}
	}

	public double[] calc_rations(){
		double total = getTotalTicketCount();
		double[] rations = new double[5];
		for(int i=4; i>=0; --i)
			rations[i] = total / _ticketCount[i];
		return rations;
	}

	public void AddWinCount(int j) {
		L1Racer racer = RaceTable.getInstance().getTemplate(_littleBugBear[j].get_num());
		if (racer != null) {
			racer.setWinCount(racer.getWinCount() + 1);
			racer.setLoseCount(racer.getLoseCount());
			SaveAllRacer(racer, _littleBugBear[j].get_num());
		}
	}

	public void AddLoseCount(int j) {
		L1Racer racer = RaceTable.getInstance().getTemplate(_littleBugBear[j].get_num());
		if (racer != null) {
			racer.setWinCount(racer.getWinCount());
			racer.setLoseCount(racer.getLoseCount() + 1);
			SaveAllRacer(racer, _littleBugBear[j].get_num());
		}
	}

	public void SaveAllRacer(L1Racer racer, int num) {
		java.sql.Connection con = null;
		PreparedStatement statement = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE util_racer SET win_count=?, loss_count=? WHERE racer_id=" + num);
			statement.setInt(1, racer.getWinCount());
			statement.setInt(2, racer.getLoseCount());
			statement.execute();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[::::::] SaveAllRacer 方法錯誤發生"); // Error occurred in SaveAllRacer method
		} finally {
			SQLUtil.close(statement, con);
		}
	}

	public void SetWinRaceTicketPrice(int id, double rate) {
		L1ShopItem newItem = new L1ShopItem(id, (int) ((500 * rate) * bugRaceInfo.BUGREDEMPTION), 1);// 勝利票銷售清單 賽車票購買
		_purchasingList.add(newItem);
		initShopNpc();
	}

	public void SetLoseRaceTicketPrice(int id, double rate) {
		L1ShopItem newItem = new L1ShopItem(id, 0, 1);// 勝利票銷售清單 // 賽車票購買
		_purchasingList.add(newItem);
		initShopNpc();
	}

	private int next_ticket_id(){
		return 8000000 + GetIssuedTicket() + 1;
	}

	private BugTicketInfo create_ticket(int racer_id, int pack_count, int converter_itemid, String color_code){
		BugTicketInfo tInfo = new BugTicketInfo();
		tInfo.itemId = next_ticket_id();
		tInfo.racerId = racer_id;
		tInfo.packCount = pack_count;
		tInfo.converter_itemid = converter_itemid == -1 ? tInfo.itemId : converter_itemid;
		if(pack_count > 1)
			SaveRace(tInfo.itemId, String.format("%s%d-%d %s X %d", color_code, _round, _littleBugBear[racer_id].get_num(), _littleBugBear[racer_id].getName(), pack_count));
		else
			SaveRace(tInfo.itemId, String.format("%s%d-%d %s", color_code, _round, _littleBugBear[racer_id].get_num(), _littleBugBear[racer_id].getName()));
		m_tickets.put(tInfo.itemId, tInfo);
		return tInfo;
	}

	private L1ShopItem create_ticket_shop_item(BugTicketInfo tInfo){
		return new L1ShopItem(tInfo.itemId, 500 * tInfo.packCount, tInfo.packCount);
	}

	public void LoadNpcShopList() {
		try {
			List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
			for (int i = 0; i < 5; i++) {
				BugTicketInfo tInfo = create_ticket(i, 1, -1, "");
				sellingList.add(create_ticket_shop_item(tInfo));
				int default_itemid = tInfo.itemId;
				_ticket[i] = default_itemid;
			}
			for(int i=0; i<5; ++i){
				int default_itemid = _ticket[i];
				BugTicketInfo tInfo = create_ticket(i, 30000, default_itemid, "\\fY");
				sellingList.add(create_ticket_shop_item(tInfo));
			}
			for(int i=0; i<5; ++i){
				int default_itemid = _ticket[i];
				BugTicketInfo tInfo = create_ticket(i, 60000, default_itemid, "\\aH");
				sellingList.add(create_ticket_shop_item(tInfo));
			}
			for(int i=0; i<5; ++i){
				int default_itemid = _ticket[i];
				BugTicketInfo tInfo = create_ticket(i, 90000, default_itemid, "\\aG");
				sellingList.add(create_ticket_shop_item(tInfo));
			}

			make_shop(BugRaceController.RACE_SELLER_NPCID, sellingList);
			make_shop(70041, sellingList);
			make_shop(70042, sellingList);
/*			L1Shop shop = new L1Shop(70035, sellingList, _purchasingList);
			ShopTable.getInstance().addShop(70035, shop);
			L1Shop shop1 = new L1Shop(70041, sellingList, _purchasingList);
			ShopTable.getInstance().addShop(70041, shop1);
			L1Shop shop2 = new L1Shop(70042, sellingList, _purchasingList);
			ShopTable.getInstance().addShop(70042, shop2);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reLoadNpcShopList() {
		try {
			List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
			for (int i = 0; i < 5; i++) {
				BugTicketInfo tInfo = create_ticket(i, 1, -1, "");
				sellingList.add(create_ticket_shop_item(tInfo));
				int default_itemid = tInfo.itemId;
				_ticket[i] = default_itemid;
			}
			for(int i=0; i<5; ++i){
				int default_itemid = _ticket[i];
				BugTicketInfo tInfo = create_ticket(i, 30000, default_itemid, "\\fY");
				sellingList.add(create_ticket_shop_item(tInfo));
			}
			for(int i=0; i<5; ++i){
				int default_itemid = _ticket[i];
				BugTicketInfo tInfo = create_ticket(i, 60000, default_itemid, "\\aH");
				sellingList.add(create_ticket_shop_item(tInfo));
			}
			for(int i=0; i<5; ++i){
				int default_itemid = _ticket[i];
				BugTicketInfo tInfo = create_ticket(i, 90000, default_itemid, "\\aG");
				sellingList.add(create_ticket_shop_item(tInfo));
			}

			make_shop(BugRaceController.RACE_SELLER_NPCID, sellingList);
			make_shop(70041, sellingList);
			make_shop(70042, sellingList);
/*			L1Shop shop = new L1Shop(70035, sellingList, _purchasingList);
			ShopTable.getInstance().addShop(70035, shop);
			L1Shop shop1 = new L1Shop(70041, sellingList, _purchasingList);
			ShopTable.getInstance().addShop(70041, shop1);
			L1Shop shop2 = new L1Shop(70042, sellingList, _purchasingList);
			ShopTable.getInstance().addShop(70042, shop2);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void SaveRace(int i, String j) {
		L1RaceTicket etcItem = new L1RaceTicket();
		etcItem.setItemDescId(1);
		etcItem.setType2(0);
		etcItem.setItemId(i);
		etcItem.setName(j);
		etcItem.setNameId(j);
		etcItem.setType(12);
		etcItem.setType1(12);
		etcItem.setMaterial(5);
		etcItem.setWeight(0);
		etcItem.set_price(1000);
		etcItem.setGfxId(143);//143
		etcItem.setGroundGfxId(151);
		etcItem.setWareHouseLimitType(WareHouseLeaveType.NO_WAREHOUSE);
		etcItem.setMinLevel(0);
		etcItem.setMaxLevel(0);
		etcItem.setBless(1);
		etcItem.setTradable(false);
		etcItem.setDmgSmall(0);
		etcItem.setDmgLarge(0);
		etcItem.set_stackable(true);
		// ItemTable.getInstance().AddTicket(etcItem);
		AddTicket(etcItem);
	}

	public void goalIn(final int i) {
		synchronized (this) {
			_ranking = _ranking + 1;
			// broadcastNpc(_排名 + "位 - " + _littleBugBear[i].getNameId());
			if (_ranking == 1) {
				_first = _littleBugBear[i].getName();
				SetWinRaceTicketPrice(_ticket[i], _ration[i]);
				AddWinCount(i);
				GeneralThreadPool.getInstance().schedule(new Runnable(){
					@Override
					public void run(){
						String.format("第 %d 屆冠軍是 '%s'。", _round, _littleBugBear[i].getNameId());
						return null;
					}
				}, 700L);
				Updator.exec("insert into bug_history set round=?, winner_id=?, winner_name=?, total_ticket_count=?, winner_ticket_count=?, winner_ration=?, total_price=?, winner_price=?", new Handler(){
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						int total_count = getTotalTicketCount();
						double winner_ration = _ration[i];
						pstm.setInt(++idx, _round);
						pstm.setInt(++idx, i);
						pstm.setString(++idx, _littleBugBear[i].getName());
						pstm.setInt(++idx, total_count);
						pstm.setInt(++idx, _ticketCount[i]);
						pstm.setDouble(++idx, winner_ration);
						pstm.setInt(++idx, total_count * 500);
						pstm.setInt(++idx, (int)(winner_ration * _ticketCount[i]));
						//TODO 顯示在管理員窗口
						MJUIAdapter.on_minigame_append(String.format("<Bugbear Race%d> 勝利:%s, 總票數:%d, 勝者票數:%d, 總金額:%d, 勝者金額:%d", _round, _littleBugBear[i].getName(), total_count, _ticketCount[i], total_count * 500, (int)(winner_ration * _ticketCount[i])));
					}
				});
			} else {
				SetLoseRaceTicketPrice(_ticket[i], _ration[i]);
				AddLoseCount(i);
			}
		}

		if (_ranking == 5) {
			_complete = true;
		}
	}

	public void wrapUpRace() throws Exception {
		setBugState(2);
		_littleBugBear[0].deleteMe();
		_littleBugBear[1].deleteMe();
		_littleBugBear[2].deleteMe();
		_littleBugBear[3].deleteMe();
		_littleBugBear[4].deleteMe();
		_raceCount = _raceCount + 1;
		broadcastNpc("正在準備下一場Bugbear Race。");
	}

	public void BroadcastAllUser(String text) {
		for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
			try {
				player.sendPackets(new S_SystemMessage(text));
			} catch (Exception exception) {
			}
		}
	}

	private void StartGame() {
		for (int i = 0; i < 5; ++i) {
			RunBug bug = new RunBug(i);

			GeneralThreadPool.getInstance().schedule(bug, 100);
		}
	}

	public class RunBug implements Runnable {
		private int _status = 0;

		private int[][] _BUG_INFO = {
				{ 45, 4, 5, 6, 50 },
				{ 42, 6, 5, 7, 50 },
				{ 39, 8, 5, 8, 50 },
				{ 36, 10, 5, 9, 50 },
				{ 33, 12, 5, 10, 50 }
		};

		private int _bugId;
		private int _remainRacingCount;
		private Random _rndGen = new Random(System.nanoTime());

		public RunBug(int bugId) {
			_bugId = bugId;
			_remainRacingCount = _BUG_INFO[_bugId][0];
		}

		private boolean is_down(){
			boolean result = _is_downs[_bugId];
			_is_downs[_bugId] = false;
			return result;
		}

		@Override
		public void run() {
			try {
				switch (_status) {
					case 0: {
						if (_remainRacingCount == 0) {
							_remainRacingCount = _BUG_INFO[_bugId][1];
							_status = 1;
						} else {
							if(is_down() || (bugRaceInfo.IsAutoTumble && _rndGen.nextInt(bugRaceInfo.MaxTumbleProbability) < 1 && _rndGen.nextInt(100) > (int) (_winRate[_bugId]))){
								_littleBugBear[_bugId].broadcastPacket(new S_AttackPacket(_littleBugBear[_bugId], _littleBugBear[_bugId].getId(), 30));
								long sleepTime = _littleBugBear[_bugId].getCurrentSpriteInterval(30);
								GeneralThreadPool.getInstance().schedule(this, sleepTime);//3380/2000
							} else {
								_littleBugBear[_bugId].setDirectionMoveSpeed(6);
								--_remainRacingCount;

								GeneralThreadPool.getInstance().schedule(this, _time[_bugId]);
							}
							break;
						}
					}
					case 1: {
						if (_remainRacingCount == 0) {
							_remainRacingCount = _BUG_INFO[_bugId][2];
							_status = 2;
						} else {
							if(is_down() || (bugRaceInfo.IsAutoTumble && _rndGen.nextInt(bugRaceInfo.MaxTumbleProbability) < 2 && _rndGen.nextInt(100) > (int) (_winRate[_bugId]))){
								_littleBugBear[_bugId].broadcastPacket(new S_AttackPacket(_littleBugBear[_bugId], _littleBugBear[_bugId].getId(), 30));
								long sleepTime = _littleBugBear[_bugId].getCurrentSpriteInterval(30);
								GeneralThreadPool.getInstance().schedule(this, sleepTime);
							} else {
								_littleBugBear[_bugId].setDirectionMoveSpeed(7);
								--_remainRacingCount;

								GeneralThreadPool.getInstance().schedule(this, _time[_bugId]);
							}
							break;
						}
					}
					case 2: {
						if (_remainRacingCount == 0) {
							_remainRacingCount = _BUG_INFO[_bugId][3];
							_status = 3;
						} else {
							if(is_down() || (bugRaceInfo.IsAutoTumble && _rndGen.nextInt(bugRaceInfo.MaxTumbleProbability) < 2 && _rndGen.nextInt(100) > (int) (_winRate[_bugId]))){
								_littleBugBear[_bugId].broadcastPacket(new S_AttackPacket(_littleBugBear[_bugId], _littleBugBear[_bugId].getId(), 30));
								long sleepTime = _littleBugBear[_bugId].getCurrentSpriteInterval(30);
								GeneralThreadPool.getInstance().schedule(this, sleepTime);
							} else {
								_littleBugBear[_bugId].setDirectionMoveSpeed(0);
								--_remainRacingCount;

								GeneralThreadPool.getInstance().schedule(this, _time[_bugId]);
							}
							break;
						}
					}
					case 3: {
						if (_remainRacingCount == 0) {
							_status = 4;
						} else {
							if(is_down() || (bugRaceInfo.IsAutoTumble && _rndGen.nextInt(bugRaceInfo.MaxTumbleProbability) < 2 && _rndGen.nextInt(100) > (int) (_winRate[_bugId]))){
								_littleBugBear[_bugId].broadcastPacket(new S_AttackPacket(_littleBugBear[_bugId], _littleBugBear[_bugId].getId(), 30));
								long sleepTime = _littleBugBear[_bugId].getCurrentSpriteInterval(30);
								GeneralThreadPool.getInstance().schedule(this, sleepTime);
							} else {
								_littleBugBear[_bugId].setDirectionMoveSpeed(1);
								--_remainRacingCount;

								GeneralThreadPool.getInstance().schedule(this, _time[_bugId]);
							}
							break;
						}
					}
					case 4: {
						if (_littleBugBear[_bugId].getX() == 33525) { //33527
							goalIn(_bugId);
						} else if(is_down() || (bugRaceInfo.IsAutoTumble && _littleBugBear[_bugId].getX() < 33522 && _rndGen.nextInt(bugRaceInfo.MaxTumbleProbability) < 2 && _rndGen.nextInt(100) > (int) (_winRate[_bugId]))){
							_littleBugBear[_bugId].broadcastPacket(new S_AttackPacket(_littleBugBear[_bugId], _littleBugBear[_bugId].getId(), 30));
							long sleepTime = _littleBugBear[_bugId].getCurrentSpriteInterval(30);
							GeneralThreadPool.getInstance().schedule(this, sleepTime);
						} else {
							_littleBugBear[_bugId].setDirectionMoveSpeed(2);
							--_remainRacingCount;

							GeneralThreadPool.getInstance().schedule(this, _time[_bugId]);
						}
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	public int getTotalTicketCount() {
		int total = 0;
		for (int row = 0; row < 5; row++) {
			total += this._ticketCount[row];
		}
		return total;
	}

	public int getBugState() {
		return this._bugRaceState;
	}

	public void setBugState(int state) {
		this._bugRaceState = state;
	}

	public int getRaceCount() {
		return this._raceCount;
	}

	public void setRaceCount(int cnt) {
		this._raceCount = cnt;
	}

	public void AddTicket(L1RaceTicket race) {
		_race.put(new Integer(race.getItemId()), race);
		ItemTable.getInstance().getAllTemplates().put(race.getItemId(), race);
	}

	public int GetIssuedTicket() {
		return _race.size();
	}

	static class BugStruct{
		public BugStruct(int id, int gfx, String name){
			this.id = id;
			this.gfx = gfx;
			this.name = name;
		}
		public int id;
		public int gfx;
		public String name;
	}
}
