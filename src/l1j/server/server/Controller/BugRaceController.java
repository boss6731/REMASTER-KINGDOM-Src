 package l1j.server.server.Controller;
 import MJFX.UIAdapter.MJUIAdapter;
 import java.sql.Connection;
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
 import javolution.util.FastTable;
 import l1j.server.MJTemplate.MJJsonUtil;
 import l1j.server.MJTemplate.MJRnd;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
 import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
 import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.datatables.RaceTable;
 import l1j.server.server.datatables.ShopTable;
 import l1j.server.server.model.Instance.L1DoorInstance;
 import l1j.server.server.model.Instance.L1NpcInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.Instance.MJMessengerInstance;
 import l1j.server.server.model.L1Character;
 import l1j.server.server.model.L1Object;
 import l1j.server.server.model.L1World;
 import l1j.server.server.model.shop.L1Shop;
 import l1j.server.server.serverpackets.S_AttackPacket;
 import l1j.server.server.serverpackets.S_NpcChatPacket;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Npc;
 import l1j.server.server.templates.L1RaceTicket;
 import l1j.server.server.templates.L1Racer;
 import l1j.server.server.templates.L1ShopItem;
 import l1j.server.server.templates.WareHouseLeaveType;
 import l1j.server.server.utils.SQLUtil;

 public class BugRaceController implements Runnable {
   private static BugRaceController _instance;
     // 註解: 定義了一個靜態常量字符串數組，用於表示 bug 狀態
     public static final String[] bugStateStrings = new String[] { "差", "好", "差", "好", "普通" };



   public static BugRaceInfo bugRaceInfo;



   public static final int RACE_SELLER_NPCID = 8502074;




   public static class BugRaceInfo
   {
     public int BugBasicSpeed = 250;
     public boolean IsRateRealTimeUpdate = true;
     public double BugMinRate = 2.0D;
     public double BUGREDEMPTION = 0.95D;
     public double[] BugMaxRates = new double[] { 10.0D, 10.0D, 10.0D, 10.0D, 10.0D };
     public boolean IsAutoTumble = true;
     public int MaxTumbleProbability = 60;
     public int[] BugStateSpeeds = new int[] { -2, -1, 1, 2, 0 };
   }


   public static void load_config() {
     try {
       bugRaceInfo = (BugRaceInfo)MJJsonUtil.fromFile("./config/bug_race.json", BugRaceInfo.class);
     } catch (Exception e) {
       e.printStackTrace();
       throw new Error("無法載入 ./config/bug_race.json 文件.");
     }
   }


   private static int RACE_INTERVAL = 60000;

   public static final int EXECUTE_STATUS_NONE = 0;

   public static final int EXECUTE_STATUS_PREPARE = 1;
   public static final int EXECUTE_STATUS_READY = 2;
   public static final int EXECUTE_STATUS_STANDBY = 3;
   public static final int EXECUTE_STATUS_PROGRESS = 4;
   public static final int EXECUTE_STATUS_FINALIZE = 5;
   private int _executeStatus = 0;

   public int _raceCount = 0;
   long _nextRaceTime = System.currentTimeMillis() + 60000L;
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
   List<L1ShopItem> _purchasingList = new ArrayList<>();
   public L1NpcInstance[] _littleBugBear = new L1NpcInstance[5];

   int Lucky = 0;
   private static Random rnd = new Random(System.nanoTime());


   private final HashMap<Integer, L1RaceTicket> _race = new HashMap<>(20);



   public HashMap<Integer, L1RaceTicket> getAllTemplates() {
     return this._race;
   }

   public Collection<L1Shop> get_shops() {
     return _shops.values();
   }

   private int[] Start_X = new int[] { 33522, 33520, 33518, 33516, 33514 };
   private int[] Start_Y = new int[] { 32861, 32863, 32865, 32867, 32869 };




     private static final ArrayList<BugStruct> _bugs = new ArrayList<>(20); // 定義一個靜態的 BugStruct 列表，初始容量為 20
     static {
         _bugs.add(new BugStruct(1, 16081, "庫茲")); // 庫茲 (Curse)
         _bugs.add(new BugStruct(2, 16082, "冰之女皇")); // 冰之女皇 (Ice Queen)
         _bugs.add(new BugStruct(3, 16083, "巴風特")); // 巴風特 (Baphomet)
         _bugs.add(new BugStruct(4, 16084, "拉基")); // 拉基 (Lagi)
         _bugs.add(new BugStruct(5, 16085, "朗卡")); // 朗卡 (Lanka)
         _bugs.add(new BugStruct(6, 16086, "死亡騎士")); // 死亡騎士 (Death Knight)
         _bugs.add(new BugStruct(7, 16087, "惡魔")); // 惡魔 (Demon)
         _bugs.add(new BugStruct(8, 16088, "木乃伊")); // 木乃伊 (Mummy)
         _bugs.add(new BugStruct(9, 16089, "吸血鬼")); // 吸血鬼 (Vampire)
         _bugs.add(new BugStruct(10, 16090, "阿瑞")); // 阿瑞 (Ari)
         _bugs.add(new BugStruct(11, 16091, "喜兒")); // 喜兒 (Sheer)
         _bugs.add(new BugStruct(12, 16092, "拿貝")); // 拿貝 (Na'Be)
         _bugs.add(new BugStruct(13, 16093, "賽伊")); // 賽伊 (Sai)
         _bugs.add(new BugStruct(14, 16094, "巫妖")); // 巫妖 (Lich)
         _bugs.add(new BugStruct(15, 16095, "達古爾")); // 達古爾 (Dagor)
         _bugs.add(new BugStruct(16, 16096, "科阿")); // 科阿 (Koa)
         _bugs.add(new BugStruct(17, 16097, "奎尼")); // 奎尼 (Queenie)
         _bugs.add(new BugStruct(18, 16098, "卡米")); // 卡米 (Kami)
         _bugs.add(new BugStruct(19, 16099, "吉亞")); // 吉亞 (Gya)
         _bugs.add(new BugStruct(20, 16100, "熔岩")); // 熔岩 (Lava)
     }



   public static int[] _time = new int[5];
   public static String _first = null;

   public int[] _ticket = new int[] { 0, 0, 0, 0, 0 };

   private HashMap<Integer, BugTicketInfo> m_tickets = new HashMap<>(20);


   public double[] _winRate = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D };
   public double[] _winViewRate = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D };


   public String[] _bugCondition = new String[] { "普通", "好", "差", "好", "普通" }; // 定義一個包含 bug 狀態的字符串數組

   public double[] _ration = new double[] { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D };

   public boolean[] _is_downs = new boolean[] { false, false, false, false, false };

   public int _round;

   public static BugRaceController getInstance() {
     if (_instance == null) {
       _instance = new BugRaceController();
     }
     return _instance;
   }




   public static class BugTicketInfo
   {
     public int racerId;


     public int itemId;


     public int packCount;


     public int converter_itemid;
   }



   public BugTicketInfo find_ticket_info(int itemid) {
     return this.m_tickets.get(Integer.valueOf(itemid));
   }

   public boolean sellings() {
     return (this._executeStatus == 1 || this._executeStatus == 2);
   }

   public void run() {
     try {
       long remainTime;
       switch (this._executeStatus) {
         case 0:
           if (checkStartRace()) {
             initRaceGame();
             this._executeStatus = 1;

             GeneralThreadPool.getInstance().schedule(this, 1000L); break;
           }
           GeneralThreadPool.getInstance().schedule(this, 1000L);
           break;


         case 1:
           startSellTicket();
           this._executeStatus = 2;
           GeneralThreadPool.getInstance().schedule(this, 1000L);
           break;

         case 2:
           remainTime = checkTicketSellTime();
           if (remainTime > 0L) {
             GeneralThreadPool.getInstance().schedule(this, remainTime); break;
           }
           this._executeStatus = 3;
           GeneralThreadPool.getInstance().schedule(this, 1000L);
           break;



         case 3:
           if (checkWatingTime()) {
             startBugRace();
             this._executeStatus = 4;
           }
           GeneralThreadPool.getInstance().schedule(this, 1000L);
           break;


         case 4:
           if (broadcastBettingRate() &&
             this._complete) {
             this._executeStatus = 5;
           }

           GeneralThreadPool.getInstance().schedule(this, 1000L);
           break;

         case 5:
           wrapUpRace();
           this._executeStatus = 0;
           GeneralThreadPool.getInstance().schedule(this, 1000L);
           break;
       }

     } catch (Exception e) {
       e.printStackTrace();
     }
   }

   public boolean checkStartRace() {
     long currentTime = System.currentTimeMillis();
     if (this._nextRaceTime < currentTime) {
       this._nextRaceTime = currentTime + RACE_INTERVAL;
       return true;
     }
     return false;
   }

     public void initRaceGame() {
         try {
             this._round++; // 註解: 增加比賽回合數
             this._ranking = 0; // 註解: 初始化排名
             this._complete = false; // 註解: 設置比賽未完成
             _first = null; // 註解: 初始值為 null

             this.Lucky = rnd.nextInt(50); // 註解: 隨機生成一個 0 到 49 的數字

             broadcastNpc("請出售您持有的賽馬票。"); // 註解: 發送消息給 NPC，提示玩家出售賽馬票
             this._is_downs = new boolean[] { false, false, false, false, false }; // 註解: 初始化五個布爾值為 false

             initNpc(); // 註解: 初始化 NPC

             initShopNpc(); // 註解: 初始化商店 NPC

             sleepTime(); // 註解: 暫停一段時間

             loadDog(); // 註解: 加載賽犬數據

             initWinRate(); // 註解: 初始化勝率

             doorAction(false); // 註解: 關閉門操作
         } catch (Exception exception) { // 註解: 捕捉任何異常以避免崩潰
             // 錯誤處理
         }
     }


   public synchronized void on_buy_ticket(int ticket_id, int amount) {
     BugTicketInfo tInfo = this.m_tickets.get(Integer.valueOf(ticket_id));
     if (tInfo == null) {
       return;
     }
     int ticket_count = this._ticketCount[tInfo.racerId] + amount * tInfo.packCount;
     this._ticketCount[tInfo.racerId] = ticket_count;
     SettingRate();
   }


   public void initTicketCount() {
     for (int row = 0; row < 5; row++) {
       this._ticketCount[row] = 20;
       this._ration[row] = 5.0D;
     }
     SettingRate();
   }


   public void initNpc() {
     L1NpcInstance n = null;
     for (Object obj : L1World.getInstance().getVisibleObjects(4).values()) {
       if (obj instanceof L1NpcInstance) {
         n = (L1NpcInstance)obj;
         if (n.getNpcTemplate().get_npcId() == 70041) {
           this._npc[0] = n; continue;
         }  if (n.getNpcTemplate().get_npcId() == 8502074) {
           this._npc[1] = n; continue;
         }  if (n.getNpcTemplate().get_npcId() == 70042) {
           this._npc[2] = n; continue;
         }  if (n.getNpcTemplate().get_npcId() == 8500200) {
           this._box_npc = (MJMessengerInstance)n;
         }
       }
     }
   }


   private static ConcurrentHashMap<Integer, L1Shop> _shops = new ConcurrentHashMap<>(); private FastTable<L1NpcInstance> list;
   public void initShopNpc() {
     List<L1ShopItem> sellingList = new ArrayList<>();
     make_shop(8502074, sellingList);
     make_shop(70041, sellingList);
     make_shop(70042, sellingList);
   }








   private L1Shop make_shop(int npcid, List<L1ShopItem> sellings) {
     L1Shop shop = new L1Shop(npcid, sellings, this._purchasingList);
     _shops.put(Integer.valueOf(npcid), shop);
     ShopTable.getInstance().addShop(npcid, shop);
     return shop;
   }

   private void sleepTime() {
     for (int i = 0; i < 5; i++) {
       int bugState = _rnd.nextInt(5);
       int addValue = 0;

       this._bugCondition[i] = bugStateStrings[bugState];
       addValue = bugRaceInfo.BugStateSpeeds[bugState];
       _time[i] = bugRaceInfo.BugBasicSpeed + addValue;
     }
   }


   public void initWinRate() {
     double presentation_rate = MJRnd.next_double(20.0D, 30.0D);
     for (int i = 0; i < 5; i++) {
       this._winRate[i] = Double.parseDouble(_df.format(MJRnd.next_double(20.1D, 21.0D)));
       this._winViewRate[i] = Double.parseDouble(_df.format(presentation_rate));
     }
   }

   private BugRaceController() { this.list = new FastTable(); load_config(); this._round = 0;
     Selector.exec("select max(round) as r_nd from bug_history", (SelectorHandler)new FullSelectorHandler() { public void result(ResultSet rs) throws Exception { if (rs.next())
               BugRaceController.this._round = rs.getInt("r_nd");  } }
       ); } public L1NpcInstance find_bug(int object_id) { for (L1NpcInstance npc : this.list) {
       if (npc.getId() == object_id)
         return npc;
     }
     return null; }


   public boolean down_bug(int object_id) {
     boolean do_down = false;
     for (int i = this.list.size() - 1; i >= 0; ) {
       L1NpcInstance npc = (L1NpcInstance)this.list.get(i);
       if (npc.getId() != object_id) {
         i--; continue;
       }
       this._is_downs[i] = true;
       do_down = true;
     }

     return do_down;
   }

   public Iterator<L1NpcInstance> get_race_iter() {
     return this.list.iterator();
   }

   public void setSpeed(int i, int speed) {
     _time[i] = speed;
   }

   public int getSpeed(int i) {
     return _time[i];
   }

   private void loadDog() {
     L1Npc dogs = null;
     List<L1PcInstance> players = null;

     this.list.clear();

     Collections.shuffle(_bugs);
     for (int m = 0; m < 5; m++) {
       try {
         BugStruct bs = _bugs.get(m);
         dogs = new L1Npc();
         dogs.set_family(0);
         dogs.set_agrofamily(0);
         dogs.set_picupitem(false);

         Object[] parameters = { dogs };

         this._littleBugBear[m] =
           (L1NpcInstance)Class.forName("l1j.server.server.model.Instance.L1NpcInstance").getConstructors()[0].newInstance(parameters);
         this._littleBugBear[m].setCurrentSprite(bs.gfx);

         this._littleBugBear[m].setNameId(String.format("#%d %s", new Object[] { Integer.valueOf(bs.id), bs.name }));

         this._littleBugBear[m].setName(bs.name);
         this._littleBugBear[m].set_num(bs.id);
         this._littleBugBear[m].setX(this.Start_X[m]);
         this._littleBugBear[m].setY(this.Start_Y[m]);
         this._littleBugBear[m].setMap((short)4);
         this._littleBugBear[m].setHeading(6);
         this._littleBugBear[m].setId(IdFactory.getInstance().nextId());

         L1World.getInstance().storeObject((L1Object)this._littleBugBear[m]);
         L1World.getInstance().addVisibleObject((L1Object)this._littleBugBear[m]);

         this.list.add(this._littleBugBear[m]);

         players = L1World.getInstance().getVisiblePlayer((L1Object)this._littleBugBear[m]);
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
         // 註解: 遍歷所有 NPC，將消息廣播給每一個 NPC
         for (int i = 0; i < this._npc.length; i++) {
             if (this._npc[i] != null) {
                 this._npc[i].broadcastPacket((ServerBasePacket)new S_NpcChatPacket(this._npc[i], msg, 2));
             }
         }
         // 註解: 將消息添加到迷你遊戲的日誌中
         MJUIAdapter.on_minigame_append(String.format("<熊怪競賽%d>%s", new Object[] { Integer.valueOf(this._round), msg }));
     }

     public void broadcastBox(String msg) {
         // 註解: 如果箱子 NPC 存在，設置當前消息並廣播
         if (this._box_npc != null) {
             this._box_npc.set_current_message(String.format("\f3%s", new Object[] { msg }));
             this._box_npc.broadcast_message();
         }
     }



     public void announceWinner(String msg) { // 註解: 廣播獲勝者消息
         for (int i = 0; i < this._npc.length; i++) { // 註解: 遍歷所有 NPC
             if (this._npc[i] != null) {
                 this._npc[i].broadcastPacket((ServerBasePacket)new S_NpcChatPacket(this._npc[i], msg, 2)); // 註解: 發送消息給 NPC
             }
         }
         MJUIAdapter.on_minigame_append(String.format("<熊怪競賽%d>%s", new Object[] { Integer.valueOf(this._round), msg })); // 註解: 記錄消息到迷你遊戲日誌
     }

     public void doorAction(boolean open) { // 註解: 開關門操作
         L1DoorInstance door = null; // 註解: 初始化門實例
         for (Object object : L1World.getInstance().getObject()) { // 註解: 遍歷世界中的所有對象
             if (object instanceof L1DoorInstance) { // 註解: 判斷對象是否為門實例
                 door = (L1DoorInstance)object;
                 if (door != null && door.equalsCurrentSprite(1487)) { // 註解: 判斷門的精靈 ID 是否為 1487
                     if (open && door.getOpenStatus() == 29) { // 註解: 如果開門並且門狀態為 29，則開門
                         door.open();
                     }
                     if (!open && door.getOpenStatus() == 28) { // 註解: 如果關門並且門狀態為 28，則關門
                         door.close();
                     }
                 }
             }
         }
     }

     public void startSellTicket() {
         LoadNpcShopList(); // 註解: 加載 NPC 商店列表

         initTicketCount(); // 註解: 初始化票數

         broadcastNpc("賽馬票銷售開始了。"); // 註解: 廣播消息給 NPC，告知賽馬票銷售開始

         setBugState(0); // 註解: 設置比賽狀態為 0

         this._ticketSellRemainTime = 180; // 註解: 設置票務銷售剩餘時間為 180 秒
     }

     public void startSellTicket() {
         LoadNpcShopList(); // 註解: 加載 NPC 商店列表
         initTicketCount(); // 註解: 初始化票數
         broadcastNpc("賽馬票銷售開始了。"); // 註解: 廣播消息給 NPC，告知賽馬票銷售開始
         setBugState(0); // 註解: 設置比賽狀態為 0
         this._ticketSellRemainTime = 180; // 註解: 設置票務銷售剩餘時間為 180 秒
     }

     public long checkTicketSellTime() {
         if (this._ticketSellRemainTime == 180) { // 註解: 如果剩餘時間為 180 秒
             this._ticketSellRemainTime -= 60; // 註解: 減少 60 秒
             broadcastNpc("熊怪競賽開始前 3 分鐘!!"); // 註解: 廣播消息告知比賽開始前 3 分鐘
             broadcastBox("銷售結束：還有 3 分鐘"); // 註解: 廣播消息告知銷售結束還有 3 分鐘
             return 60000L; // 註解: 返回 60000 毫秒 (即 1 分鐘)
         }
         if (this._ticketSellRemainTime == 120) { // 註解: 如果剩餘時間為 120 秒
             this._ticketSellRemainTime -= 60; // 註解: 減少 60 秒
             broadcastNpc("熊怪競賽開始前 2 分鐘!!"); // 註解: 廣播消息告知比賽開始前 2 分鐘
             broadcastBox("銷售結束：還有 2 分鐘"); // 註解: 廣播消息告知銷售結束還有 2 分鐘
             return 60000L; // 註解: 返回 60000 毫秒 (即 1 分鐘)
         }
         if (this._ticketSellRemainTime == 60) { // 註解: 如果剩餘時間為 60 秒
             this._ticketSellRemainTime -= 60; // 註解: 減少 60 秒
             broadcastNpc("熊怪競賽開始前 1 分鐘!!"); // 註解: 廣播消息告知比賽開始前 1 分鐘
             broadcastBox("銷售結束：還有 1 分鐘"); // 註解: 廣播消息告知銷售結束還有 1 分鐘
             GeneralThreadPool.getInstance().schedule(new Runnable() {
                 public void run() {
                     try {
                         BugRaceController.this.broadcastNpc("30 秒後賽馬票銷售將結束。"); // 註解: 廣播消息告知 30 秒後票務銷售結束
                         for (int i = 30; i >= 1; i--) {
                             BugRaceController.this.broadcastBox(String.format("銷售結束：還有 %d 秒", new Object[] { Integer.valueOf(i) })); // 註解: 每秒廣播剩餘時間
                             Thread.sleep(1000L); // 註解: 暫停 1 秒
                         }
                         BugRaceController.this.broadcastBox("銷售已經結束。"); // 註解: 廣播消息告知銷售已結束
                     } catch (Exception exception) {}
                 }
             }, 35000L); // 註解: 計劃在 35 秒後執行
             return 60000L; // 註解: 返回 60000 毫秒 (即 1 分鐘)
         }
         if (this._ticketSellRemainTime == 30) { // 註解: 如果剩餘時間為 30 秒
             this._ticketSellRemainTime = 0; // 註解: 設置剩餘時間為 0
         }
     }













       return 30000L;
     }
     initShopNpc();
     broadcastNpc("출발 준비!");
     SettingRate();
     this._raceWatingTime = 5;
     return 0L;
   }

   private boolean checkWatingTime() {
     setBugState(1);
     if (this._raceWatingTime > 0) {
       broadcastNpc(this._raceWatingTime + "초");
       this._raceWatingTime--;

       return false;
     }

     return true;
   }


   private void startBugRace() {
     broadcastNpc("시작");
     doorAction(true);

     StartGame();

     this._currentBroadcastRacer = 0;
   }

   private boolean broadcastBettingRate() {
     if (this._currentBroadcastRacer == 5) {
       return true;
     }

     if (this._currentBroadcastRacer == 0) {
       broadcastNpc("배팅 배율을 발표하겠습니다.");
     }

     broadcastNpc(this._littleBugBear[this._currentBroadcastRacer].getNameId() + ": " + this._ration[this._currentBroadcastRacer] + " ");

     this._currentBroadcastRacer++;

     return false;
   }












































   public void SettingRate() {
     double total = getTotalTicketCount();
     ArrayList<RationInfo> temporary_ration = new ArrayList<>(5);
     for (int row = 0; row < 5; row++) {
       double cnt = this._ticketCount[row];
       RationInfo rInfo = new RationInfo();
       rInfo.idx = row;
       rInfo.ration = this._ration[row];
       if (cnt <= 0.0D) {
         while (rInfo.ration <= bugRaceInfo.BugMinRate)
           rInfo.ration = MJRnd.next_double() * 10.0D;
       } else {
         rInfo.ration = total / cnt;
       }
       if (rInfo.ration <= bugRaceInfo.BugMinRate)
         rInfo.ration = bugRaceInfo.BugMinRate + MJRnd.next_double();
       temporary_ration.add(rInfo);
     }
     Collections.sort(temporary_ration);
     for (int i = 0; i < 5; i++) {
       double need_range = bugRaceInfo.BugMaxRates[i] - 1.0D;
       double range = bugRaceInfo.BugMaxRates[i];
       RationInfo rInfo = temporary_ration.get(i);
       if (rInfo.ration >= range) {
         double d = MJRnd.next_double();
         rInfo.ration = need_range + d;
       }
       this._ration[rInfo.idx] = Double.parseDouble(_df.format(rInfo.ration));
     }
   }

   static class RationInfo implements Comparable<RationInfo> {
     int idx;
     double ration;

     public int compareTo(RationInfo o) {
       double d = o.ration - this.ration;
       return (d > 0.0D) ? 1 : ((d < 0.0D) ? -1 : 0);
     }
   }

   public double[] calc_rations() {
     double total = getTotalTicketCount();
     double[] rations = new double[5];
     for (int i = 4; i >= 0; i--)
       rations[i] = total / this._ticketCount[i];
     return rations;
   }

   public void AddWinCount(int j) {
     L1Racer racer = RaceTable.getInstance().getTemplate(this._littleBugBear[j].get_num());
     if (racer != null) {
       racer.setWinCount(racer.getWinCount() + 1);
       racer.setLoseCount(racer.getLoseCount());
       SaveAllRacer(racer, this._littleBugBear[j].get_num());
     }
   }

   public void AddLoseCount(int j) {
     L1Racer racer = RaceTable.getInstance().getTemplate(this._littleBugBear[j].get_num());
     if (racer != null) {
       racer.setWinCount(racer.getWinCount());
       racer.setLoseCount(racer.getLoseCount() + 1);
       SaveAllRacer(racer, this._littleBugBear[j].get_num());
     }
   }

   public void SaveAllRacer(L1Racer racer, int num) {
     Connection con = null;
     PreparedStatement statement = null;
     try {
       con = L1DatabaseFactory.getInstance().getConnection();
       statement = con.prepareStatement("UPDATE util_racer SET 승리횟수=?, 패횟수=? WHERE 레이서번호=" + num);
       statement.setInt(1, racer.getWinCount());
       statement.setInt(2, racer.getLoseCount());
       statement.execute();
     } catch (Exception e) {
       e.printStackTrace();
       System.out.println("[::::::] SaveAllRacer 메소드 에러 발생");
     } finally {
       SQLUtil.close(statement, con);
     }
   }

   public void SetWinRaceTicketPrice(int id, double rate) {
     L1ShopItem newItem = new L1ShopItem(id, (int)(500.0D * rate * bugRaceInfo.BUGREDEMPTION), 1);
     this._purchasingList.add(newItem);
     initShopNpc();
   }

   public void SetLoseRaceTicketPrice(int id, double rate) {
     L1ShopItem newItem = new L1ShopItem(id, 0, 1);
     this._purchasingList.add(newItem);
     initShopNpc();
   }

   private int next_ticket_id() {
     return 8000000 + GetIssuedTicket() + 1;
   }

   private BugTicketInfo create_ticket(int racer_id, int pack_count, int converter_itemid, String color_code) {
     BugTicketInfo tInfo = new BugTicketInfo();
     tInfo.itemId = next_ticket_id();
     tInfo.racerId = racer_id;
     tInfo.packCount = pack_count;
     tInfo.converter_itemid = (converter_itemid == -1) ? tInfo.itemId : converter_itemid;
     if (pack_count > 1) {
       SaveRace(tInfo.itemId, String.format("%s%d-%d %s X %d", new Object[] { color_code, Integer.valueOf(this._round), Integer.valueOf(this._littleBugBear[racer_id].get_num()), this._littleBugBear[racer_id].getName(), Integer.valueOf(pack_count) }));
     } else {
       SaveRace(tInfo.itemId, String.format("%s%d-%d %s", new Object[] { color_code, Integer.valueOf(this._round), Integer.valueOf(this._littleBugBear[racer_id].get_num()), this._littleBugBear[racer_id].getName() }));
     }  this.m_tickets.put(Integer.valueOf(tInfo.itemId), tInfo);
     return tInfo;
   }

   private L1ShopItem create_ticket_shop_item(BugTicketInfo tInfo) {
     return new L1ShopItem(tInfo.itemId, 500 * tInfo.packCount, tInfo.packCount);
   }

   public void LoadNpcShopList() {
     try {
       List<L1ShopItem> sellingList = new ArrayList<>(); int i;
       for (i = 0; i < 5; i++) {
         BugTicketInfo tInfo = create_ticket(i, 1, -1, "");
         sellingList.add(create_ticket_shop_item(tInfo));
         int default_itemid = tInfo.itemId;
         this._ticket[i] = default_itemid;
       }
       for (i = 0; i < 5; i++) {
         int default_itemid = this._ticket[i];
         BugTicketInfo tInfo = create_ticket(i, 30000, default_itemid, "\\fY");
         sellingList.add(create_ticket_shop_item(tInfo));
       }
       for (i = 0; i < 5; i++) {
         int default_itemid = this._ticket[i];
         BugTicketInfo tInfo = create_ticket(i, 60000, default_itemid, "\\aH");
         sellingList.add(create_ticket_shop_item(tInfo));
       }
       for (i = 0; i < 5; i++) {
         int default_itemid = this._ticket[i];
         BugTicketInfo tInfo = create_ticket(i, 90000, default_itemid, "\\aG");
         sellingList.add(create_ticket_shop_item(tInfo));
       }

       make_shop(8502074, sellingList);
       make_shop(70041, sellingList);
       make_shop(70042, sellingList);





     }
     catch (Exception e) {
       e.printStackTrace();
     }
   }

   public void reLoadNpcShopList() {
     try {
       List<L1ShopItem> sellingList = new ArrayList<>(); int i;
       for (i = 0; i < 5; i++) {
         BugTicketInfo tInfo = create_ticket(i, 1, -1, "");
         sellingList.add(create_ticket_shop_item(tInfo));
         int default_itemid = tInfo.itemId;
         this._ticket[i] = default_itemid;
       }
       for (i = 0; i < 5; i++) {
         int default_itemid = this._ticket[i];
         BugTicketInfo tInfo = create_ticket(i, 30000, default_itemid, "\\fY");
         sellingList.add(create_ticket_shop_item(tInfo));
       }
       for (i = 0; i < 5; i++) {
         int default_itemid = this._ticket[i];
         BugTicketInfo tInfo = create_ticket(i, 60000, default_itemid, "\\aH");
         sellingList.add(create_ticket_shop_item(tInfo));
       }
       for (i = 0; i < 5; i++) {
         int default_itemid = this._ticket[i];
         BugTicketInfo tInfo = create_ticket(i, 90000, default_itemid, "\\aG");
         sellingList.add(create_ticket_shop_item(tInfo));
       }

       make_shop(8502074, sellingList);
       make_shop(70041, sellingList);
       make_shop(70042, sellingList);





     }
     catch (Exception e) {
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
     etcItem.setGfxId(143);
     etcItem.setGroundGfxId(151);
     etcItem.setWareHouseLimitType(WareHouseLeaveType.NO_WAREHOUSE);
     etcItem.setMinLevel(0);
     etcItem.setMaxLevel(0);
     etcItem.setBless(1);
     etcItem.setTradable(false);
     etcItem.setDmgSmall(0);
     etcItem.setDmgLarge(0);
     etcItem.set_stackable(true);

     AddTicket(etcItem);
   }

   public void goalIn(final int i) {
     synchronized (this) {
       this._ranking++;

       if (this._ranking == 1) {
         _first = this._littleBugBear[i].getName();
         SetWinRaceTicketPrice(this._ticket[i], this._ration[i]);
         AddWinCount(i);
         GeneralThreadPool.getInstance().schedule(new Runnable()
             {
               public void run() {
                 BugRaceController.this.우승자멘트(String.format("제 %d회 우승자는 '%s' 입니다.", new Object[] { Integer.valueOf(this.this$0._round), this.this$0._littleBugBear[this.val$i].getNameId() }));
               }
             }700L);
         Updator.exec("insert into bug_history set round=?, winner_id=?, winner_name=?, total_ticket_count=?, winner_ticket_count=?, winner_ration=?, total_price=?, winner_price=?", new Handler()
             {
               public void handle(PreparedStatement pstm) throws Exception {
                 int idx = 0;
                 int total_count = BugRaceController.this.getTotalTicketCount();
                 double winner_ration = BugRaceController.this._ration[i];
                 pstm.setInt(++idx, BugRaceController.this._round);
                 pstm.setInt(++idx, i);
                 pstm.setString(++idx, BugRaceController.this._littleBugBear[i].getName());
                 pstm.setInt(++idx, total_count);
                 pstm.setInt(++idx, BugRaceController.this._ticketCount[i]);
                 pstm.setDouble(++idx, winner_ration);
                 pstm.setInt(++idx, total_count * 500);
                 pstm.setInt(++idx, (int)(winner_ration * BugRaceController.this._ticketCount[i]));

                 MJUIAdapter.on_minigame_append(String.format("<버그베어경주%d> 승리:%s, 총 티켓수:%d, 승자티켓수:%d, 전체금액:%d, 승자금액:%d", new Object[] { Integer.valueOf(this.this$0._round), this.this$0._littleBugBear[this.val$i].getName(), Integer.valueOf(total_count), Integer.valueOf(this.this$0._ticketCount[this.val$i]), Integer.valueOf(total_count * 500), Integer.valueOf((int)(winner_ration * this.this$0._ticketCount[this.val$i])) }));
               }
             });
       } else {
         SetLoseRaceTicketPrice(this._ticket[i], this._ration[i]);
         AddLoseCount(i);
       }
     }

     if (this._ranking == 5) {
       this._complete = true;
     }
   }

   public void wrapUpRace() throws Exception {
     setBugState(2);
     this._littleBugBear[0].deleteMe();
     this._littleBugBear[1].deleteMe();
     this._littleBugBear[2].deleteMe();
     this._littleBugBear[3].deleteMe();
     this._littleBugBear[4].deleteMe();
     this._raceCount++;
     broadcastNpc("다음 버그베어경기를 준비중입니다.");
   }

   public void BroadcastAllUser(String text) {
     for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {
       try {
         player.sendPackets((ServerBasePacket)new S_SystemMessage(text));
       } catch (Exception exception) {}
     }
   }


   private void StartGame() {
     for (int i = 0; i < 5; i++) {
       RunBug bug = new RunBug(i);

       GeneralThreadPool.getInstance().schedule(bug, 100L);
     }
   }

   public class RunBug implements Runnable {
     private int _status = 0;

     private int[][] _BUG_INFO = new int[][] { { 45, 4, 5, 6, 50 }, { 42, 6, 5, 7, 50 }, { 39, 8, 5, 8, 50 }, { 36, 10, 5, 9, 50 }, { 33, 12, 5, 10, 50 } };



     private int _bugId;


     private int _remainRacingCount;


     private Random _rndGen = new Random(System.nanoTime());

     public RunBug(int bugId) {
       this._bugId = bugId;
       this._remainRacingCount = this._BUG_INFO[this._bugId][0];
     }

     private boolean is_down() {
       boolean result = BugRaceController.this._is_downs[this._bugId];
       BugRaceController.this._is_downs[this._bugId] = false;
       return result;
     }


     public void run() {
       try {
         switch (this._status) {
           case 0:
             if (this._remainRacingCount == 0) {
               this._remainRacingCount = this._BUG_INFO[this._bugId][1];
               this._status = 1;
             } else {
               if (is_down() || (BugRaceController.bugRaceInfo.IsAutoTumble && this._rndGen.nextInt(BugRaceController.bugRaceInfo.MaxTumbleProbability) < 1 && this._rndGen.nextInt(100) > (int)BugRaceController.this._winRate[this._bugId])) {
                 BugRaceController.this._littleBugBear[this._bugId].broadcastPacket((ServerBasePacket)new S_AttackPacket((L1Character)BugRaceController.this._littleBugBear[this._bugId], BugRaceController.this._littleBugBear[this._bugId].getId(), 30));
                 long sleepTime = BugRaceController.this._littleBugBear[this._bugId].getCurrentSpriteInterval(30);
                 GeneralThreadPool.getInstance().schedule(this, sleepTime); break;
               }
               BugRaceController.this._littleBugBear[this._bugId].setDirectionMoveSpeed(6);
               this._remainRacingCount--;

               GeneralThreadPool.getInstance().schedule(this, BugRaceController._time[this._bugId]);
               break;
             }


           case 1:
             if (this._remainRacingCount == 0) {
               this._remainRacingCount = this._BUG_INFO[this._bugId][2];
               this._status = 2;
             } else {
               if (is_down() || (BugRaceController.bugRaceInfo.IsAutoTumble && this._rndGen.nextInt(BugRaceController.bugRaceInfo.MaxTumbleProbability) < 2 && this._rndGen.nextInt(100) > (int)BugRaceController.this._winRate[this._bugId])) {
                 BugRaceController.this._littleBugBear[this._bugId].broadcastPacket((ServerBasePacket)new S_AttackPacket((L1Character)BugRaceController.this._littleBugBear[this._bugId], BugRaceController.this._littleBugBear[this._bugId].getId(), 30));
                 long sleepTime = BugRaceController.this._littleBugBear[this._bugId].getCurrentSpriteInterval(30);
                 GeneralThreadPool.getInstance().schedule(this, sleepTime); break;
               }
               BugRaceController.this._littleBugBear[this._bugId].setDirectionMoveSpeed(7);
               this._remainRacingCount--;

               GeneralThreadPool.getInstance().schedule(this, BugRaceController._time[this._bugId]);
               break;
             }


           case 2:
             if (this._remainRacingCount == 0) {
               this._remainRacingCount = this._BUG_INFO[this._bugId][3];
               this._status = 3;
             } else {
               if (is_down() || (BugRaceController.bugRaceInfo.IsAutoTumble && this._rndGen.nextInt(BugRaceController.bugRaceInfo.MaxTumbleProbability) < 2 && this._rndGen.nextInt(100) > (int)BugRaceController.this._winRate[this._bugId])) {
                 BugRaceController.this._littleBugBear[this._bugId].broadcastPacket((ServerBasePacket)new S_AttackPacket((L1Character)BugRaceController.this._littleBugBear[this._bugId], BugRaceController.this._littleBugBear[this._bugId].getId(), 30));
                 long sleepTime = BugRaceController.this._littleBugBear[this._bugId].getCurrentSpriteInterval(30);
                 GeneralThreadPool.getInstance().schedule(this, sleepTime); break;
               }
               BugRaceController.this._littleBugBear[this._bugId].setDirectionMoveSpeed(0);
               this._remainRacingCount--;

               GeneralThreadPool.getInstance().schedule(this, BugRaceController._time[this._bugId]);
               break;
             }


           case 3:
             if (this._remainRacingCount == 0) {
               this._status = 4;
             } else {
               if (is_down() || (BugRaceController.bugRaceInfo.IsAutoTumble && this._rndGen.nextInt(BugRaceController.bugRaceInfo.MaxTumbleProbability) < 2 && this._rndGen.nextInt(100) > (int)BugRaceController.this._winRate[this._bugId])) {
                 BugRaceController.this._littleBugBear[this._bugId].broadcastPacket((ServerBasePacket)new S_AttackPacket((L1Character)BugRaceController.this._littleBugBear[this._bugId], BugRaceController.this._littleBugBear[this._bugId].getId(), 30));
                 long sleepTime = BugRaceController.this._littleBugBear[this._bugId].getCurrentSpriteInterval(30);
                 GeneralThreadPool.getInstance().schedule(this, sleepTime); break;
               }
               BugRaceController.this._littleBugBear[this._bugId].setDirectionMoveSpeed(1);
               this._remainRacingCount--;

               GeneralThreadPool.getInstance().schedule(this, BugRaceController._time[this._bugId]);
               break;
             }


           case 4:
             if (BugRaceController.this._littleBugBear[this._bugId].getX() == 33525) {
               BugRaceController.this.goalIn(this._bugId); break;
             }  if (is_down() || (BugRaceController.bugRaceInfo.IsAutoTumble && BugRaceController.this._littleBugBear[this._bugId].getX() < 33522 && this._rndGen.nextInt(BugRaceController.bugRaceInfo.MaxTumbleProbability) < 2 && this._rndGen.nextInt(100) > (int)BugRaceController.this._winRate[this._bugId])) {
               BugRaceController.this._littleBugBear[this._bugId].broadcastPacket((ServerBasePacket)new S_AttackPacket((L1Character)BugRaceController.this._littleBugBear[this._bugId], BugRaceController.this._littleBugBear[this._bugId].getId(), 30));
               long sleepTime = BugRaceController.this._littleBugBear[this._bugId].getCurrentSpriteInterval(30);
               GeneralThreadPool.getInstance().schedule(this, sleepTime); break;
             }
             BugRaceController.this._littleBugBear[this._bugId].setDirectionMoveSpeed(2);
             this._remainRacingCount--;

             GeneralThreadPool.getInstance().schedule(this, BugRaceController._time[this._bugId]);
             break;
         }


       } catch (Exception e) {
         e.printStackTrace();
       }
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
     this._race.put(new Integer(race.getItemId()), race);
     ItemTable.getInstance().getAllTemplates().put(Integer.valueOf(race.getItemId()), race);
   }

   public int GetIssuedTicket() {
     return this._race.size();
   }
   static class BugStruct { public int id; public int gfx; public String name;

     public BugStruct(int id, int gfx, String name) {
       this.id = id;
       this.gfx = gfx;
       this.name = name;
     } }

 }


