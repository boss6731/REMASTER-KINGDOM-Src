 package l1j.server.server.model;

 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Calendar;
 import java.util.HashSet;
 import java.util.Set;
 import java.util.SortedSet;
 import java.util.TimeZone;
 import java.util.TreeSet;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.Config;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.datatables.ItemTable;
 import l1j.server.server.datatables.UBSpawnTable;
 import l1j.server.server.datatables.UBTable;
 import l1j.server.server.model.Instance.L1ItemInstance;
 import l1j.server.server.model.Instance.L1MonsterInstance;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;
 import l1j.server.server.templates.L1Item;
 import l1j.server.server.utils.IntRange;




 public class L1UltimateBattle
 {
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
     private boolean _active;
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
     private boolean _enter전사;
     private boolean _enterFencer;
     private boolean _enterLancer;
     private boolean _enterMale;
     private boolean _enterFemale;
     private boolean _usePot;
     private int _hpr;
     private int _mpr;
     private static int BEFORE_MINUTE = 5;

     private static int ubcount = 0;

     private Set<Integer> _managers = new HashSet<>();

     private SortedSet<Integer> _ubTimes = new TreeSet<>();

     private static final Logger _log = Logger.getLogger(L1UltimateBattle.class.getName());

     private final ArrayList<L1PcInstance> _members = new ArrayList<>(); private String[] _ubInfo;

     private void sendRoundMessage(int curRound) {
// 根據不同的競技場 ID 發送對應的消息
         switch (this._ubId) {
             case 1:
             case 2:
             case 3:
             case 5:
                 // 當前回合為 1
                 if (curRound == 1) {
                     sendMessage("競技場管理員: 第 1 軍隊進場!");
                     break;
                 }
                 // 當前回合為 2
                 if (curRound == 2) {
                     sendMessage("競技場管理員: 第 2 軍隊進場!");
                     break;
                 }
                 // 當前回合為 3
                 if (curRound == 3) {
                     sendMessage("競技場管理員: 第 3 軍隊進場!");
                     break;
                 }
                 // 當前回合為 4
                 if (curRound == 4) {
                     sendMessage("競技場管理員: 最終戰開始！限時5分鐘。");
                 }
                 break;
             case 4:
                 // 當前回合為 1
                 if (curRound == 1) {
                     sendMessage("競技場管理員: 第 1 軍隊進場!");
                     break;
                 }
                 // 當前回合為 2
                 if (curRound == 2) {
                     sendMessage("競技場管理員: 第 2 軍隊進場!");
                     break;
                 }
                 // 當前回合為 3
                 if (curRound == 3) {
                     sendMessage("競技場管理員: 最終戰開始！限時5分鐘。");
                 }
                 break;
         }
     }

     private void spawnSupplies(int curRound) {
         switch (this._ubId) {
             case 1:
             case 2:
                 if (curRound == 1) {
                    // 在地面生成物品
                     spawnGroundItem(40308, 1000, 60); // 生成 1000 個 ID 為 40308 的物品，存活時間 60 秒
                     spawnGroundItem(40017, 3, 20);    // 生成 3 個 ID 為 40017 的物品，存活時間 20 秒
                     spawnGroundItem(40011, 5, 20);    // 生成 5 個 ID 為 40011 的物品，存活時間 20 秒
                     spawnGroundItem(40012, 3, 20);    // 生成 3 個 ID 為 40012 的物品，存活時間 20 秒
                     spawnGroundItem(40317, 1, 5);     // 生成 1 個 ID 為 40317 的物品，存活時間 5 秒
                     spawnGroundItem(40079, 1, 10);    // 生成 1 個 ID 為 40079 的物品，存活時間 10 秒

                    // 發送訊息
                     sendMessage("競技場管理員: 第 1 軍隊的進場已完成。");
                     sendMessage("競技場管理員: 1 分鐘後第 2 軍隊將開始進場。");
                     break;

                 }
                 if (curRound == 2) {
                     // 在第二輪，生成以下物品
                     spawnGroundItem(40308, 5000, 50); // 生成 5000 個 ID 為 40308 的物品，存活時間 50 秒
                     spawnGroundItem(40017, 7, 20);    // 生成 7 個 ID 為 40017 的物品，存活時間 20 秒
                     spawnGroundItem(40011, 10, 20);   // 生成 10 個 ID 為 40011 的物品，存活時間 20 秒
                     spawnGroundItem(40012, 5, 20);    // 生成 5 個 ID 為 40012 的物品，存活時間 20 秒
                     spawnGroundItem(40317, 1, 7);     // 生成 1 個 ID 為 40317 的物品，存活時間 7 秒
                     spawnGroundItem(40093, 1, 10);    // 生成 1 個 ID 為 40093 的物品，存活時間 10 秒
                     spawnGroundItem(40079, 1, 10);    // 生成 1 個 ID 為 40079 的物品，存活時間 10 秒

                     // 發送消息通知
                     sendMessage("競技場管理員: 第 2 軍隊的進場已完成。");
                     sendMessage("競技場管理員: 2 分鐘後第 3 軍隊將開始進場。");
                     break;
                 }
                 // 其他輪數應該有相似的代碼，根據需要添加


                 if (curRound == 3) {
                    // 在第三輪，生成以下物品
                     spawnGroundItem(40308, 10000, 30); // 生成 10000 個 ID 為 40308 的物品，存活時間 30 秒
                     spawnGroundItem(40017, 7, 20);     // 生成 7 個 ID 為 40017 的物品，存活時間 20 秒
                     spawnGroundItem(40011, 20, 20);    // 生成 20 個 ID 為 40011 的物品，存活時間 20 秒
                     spawnGroundItem(40012, 10, 10);    // 生成 10 個 ID 為 40012 的物品，存活時間 10 秒
                     spawnGroundItem(40317, 1, 10);     // 生成 1 個 ID 為 40317 的物品，存活時間 10 秒
                     spawnGroundItem(40094, 1, 10);     // 生成 1 個 ID 為 40094 的物品，存活時間 10 秒
                     spawnGroundItem(40079, 1, 10);     // 生成 1 個 ID 為 40079 的物品，存活時間 10 秒

                    // 發送消息通知
                     sendMessage("競技場管理員: 第 3 軍隊的進場已完成。");
                     sendMessage("競技場管理員: 6 分鐘後最終戰將開始。");

                     }
                     break;
             case 3:
                 if (curRound == 1) {
                    // 在第一輪，生成以下物品
                     spawnGroundItem(40308, 1000, 60); // 生成 1000 個 ID 為 40308 的物品，存活時間 60 秒
                     spawnGroundItem(40017, 3, 20);    // 生成 3 個 ID 為 40017 的物品，存活時間 20 秒
                     spawnGroundItem(40011, 5, 20);    // 生成 5 個 ID 為 40011 的物品，存活時間 20 秒
                     spawnGroundItem(40012, 3, 20);    // 生成 3 個 ID 為 40012 的物品，存活時間 20 秒
                     spawnGroundItem(40317, 1, 5);     // 生成 1 個 ID 為 40317 的物品，存活時間 5 秒
                     spawnGroundItem(40079, 1, 10);    // 生成 1 個 ID 為 40079 的物品，存活時間 10 秒

                    // 發送消息通知
                     sendMessage("競技場管理員: 第 1 軍隊的進場已完成。");
                     sendMessage("競技場管理員: 1 分鐘後第 2 軍隊將開始進場。");
                     break;
                 }
                 if (curRound == 2) {
                     // 在第二輪，生成以下物品
                     spawnGroundItem(40308, 3000, 50); // 生成 3000 個 ID 為 40308 的物品，存活時間 50 秒
                     spawnGroundItem(40017, 5, 20);    // 生成 5 個 ID 為 40017 的物品，存活時間 20 秒
                     spawnGroundItem(40011, 10, 20);   // 生成 10 個 ID 為 40011 的物品，存活時間 20 秒
                     spawnGroundItem(40012, 5, 20);    // 生成 5 個 ID 為 40012 的物品，存活時間 20 秒
                     spawnGroundItem(40317, 1, 7);     // 生成 1 個 ID 為 40317 的物品，存活時間 7 秒
                     spawnGroundItem(40093, 1, 10);    // 生成 1 個 ID 為 40093 的物品，存活時間 10 秒

                    // 發送消息通知
                     sendMessage("競技場管理員: 第 2 軍隊的進場已完成。");
                     sendMessage("競技場管理員: 2 分鐘後第 3 軍隊將開始進場。");
                     break;
                 }
                 if (curRound == 3) {
                    // 在第三輪，生成以下物品
                     spawnGroundItem(40308, 5000, 30); // 生成 5000 個 ID 為 40308 的物品，存活時間 30 秒
                     spawnGroundItem(40017, 10, 20);   // 生成 10 個 ID 為 40017 的物品，存活時間 20 秒
                     spawnGroundItem(40011, 15, 20);   // 生成 15 個 ID 為 40011 的物品，存活時間 20 秒
                     spawnGroundItem(40012, 7, 10);    // 生成 7 個 ID 為 40012 的物品，存活時間 10 秒
                     spawnGroundItem(40317, 1, 10);    // 生成 1 個 ID 為 40317 的物品，存活時間 10 秒
                     spawnGroundItem(40094, 1, 10);    // 生成 1 個 ID 為 40094 的物品，存活時間 10 秒

                    // 發送消息通知
                     sendMessage("競技場管理員: 第 3 軍隊的進場已完成。");
                     sendMessage("競技場管理員: 6 分鐘後最終戰將開始。");
                     break;


                     case 4:
                         if (curRound == 1) {
                        // 在第一輪，生成以下物品
                             spawnGroundItem(40308, 200, 60); // 生成 200 個 ID 為 40308 的物品，存活時間 60 秒
                             spawnGroundItem(40017, 3, 20);   // 生成 3 個 ID 為 40017 的物品，存活時間 20 秒
                             spawnGroundItem(40011, 5, 20);   // 生成 5 個 ID 為 40011 的物品，存活時間 20 秒
                             spawnGroundItem(40317, 1, 5);    // 生成 1 個 ID 為 40317 的物品，存活時間 5 秒
                             spawnGroundItem(40079, 1, 10);   // 生成 1 個 ID 為 40079 的物品，存活時間 10 秒

                            // 發送消息通知
                             sendMessage("競技場管理員: 第 1 軍隊的進場已完成。");
                             sendMessage("競技場管理員: 2 分鐘後第 2 軍隊將開始進場。");
                             break;
                         }
                         if (curRound == 2) {
                            // 在第二輪，生成以下物品
                             spawnGroundItem(40308, 500, 50); // 生成 500 個 ID 為 40308 的物品，存活時間 50 秒
                             spawnGroundItem(40017, 7, 20);   // 生成 7 個 ID 為 40017 的物品，存活時間 20 秒
                             spawnGroundItem(40011, 12, 20);  // 生成 12 個 ID 為 40011 的物品，存活時間 20 秒
                             spawnGroundItem(40012, 5, 20);   // 生成 5 個 ID 為 40012 的物品，存活時間 20 秒
                             spawnGroundItem(40317, 1, 7);    // 生成 1 個 ID 為 40317 的物品，存活時間 7 秒
                             spawnGroundItem(40093, 1, 10);   // 生成 1 個 ID 為 40093 的物品，存活時間 10 秒

                            // 發送消息通知
                             sendMessage("競技場管理員: 第 2 軍隊的進場已完成。");
                             sendMessage("競技場管理員: 6 分鐘後最終戰將開始。");
                         }
                         break;
                     case 5:
                         if (curRound == 1) {
                            // 在第一輪，生成以下物品
                             spawnGroundItem(40308, 1000, 60); // 生成 1000 個 ID 為 40308 的物品，存活時間 60 秒
                             spawnGroundItem(40017, 3, 20);    // 生成 3 個 ID 為 40017 的物品，存活時間 20 秒
                             spawnGroundItem(40011, 5, 20);    // 生成 5 個 ID 為 40011 的物品，存活時間 20 秒
                             spawnGroundItem(40012, 3, 20);    // 生成 3 個 ID 為 40012 的物品，存活時間 20 秒
                             spawnGroundItem(40317, 1, 5);     // 生成 1 個 ID 為 40317 的物品，存活時間 5 秒
                             spawnGroundItem(40079, 1, 10);    // 生成 1 個 ID 為 40079 的物品，存活時間 10 秒

                            // 發送消息通知
                             sendMessage("競技場管理員: 第 1 軍隊的進場已完成。");
                             sendMessage("競技場管理員: 1 分鐘後第 2 軍隊將開始進場。");
                             break;
                         }
                         if (curRound == 2) {
                            // 在第二輪，生成以下物品
                             spawnGroundItem(40308, 5000, 50); // 生成 5000 個 ID 為 40308 的物品，存活時間 50 秒
                             spawnGroundItem(40017, 7, 20);    // 生成 7 個 ID 為 40017 的物品，存活時間 20 秒
                             spawnGroundItem(40011, 10, 20);   // 生成 10 個 ID 為 40011 的物品，存活時間 20 秒
                             spawnGroundItem(40012, 5, 20);    // 生成 5 個 ID 為 40012 的物品，存活時間 20 秒
                             spawnGroundItem(40317, 1, 7);     // 生成 1 個 ID 為 40317 的物品，存活時間 7 秒
                             spawnGroundItem(40093, 1, 10);    // 生成 1 個 ID 為 40093 的物品，存活時間 10 秒
                             spawnGroundItem(40079, 1, 10);    // 生成 1 個 ID 為 40079 的物品，存活時間 10 秒

                            // 發送消息通知
                             sendMessage("競技場管理員: 第 2 軍隊的進場已完成。");
                             sendMessage("競技場管理員: 2 分鐘後第 3 軍隊將開始進場。");
                             break;

                                 }  if (curRound == 3) {
                                        // 在第三輪，生成以下物品
                     }
                             spawnGroundItem(40308, 10000, 30);     // 生成 5000 個 ID 為 40308 的物品，存活時間 30 秒
                             spawnGroundItem(40017, 7, 20);         // 生成 7 個 ID 為 40017 的物品，存活時間 20 秒
                             spawnGroundItem(40011, 20, 20);        // 生成 10 個 ID 為 40011 的物品，存活時間 20 秒
                             spawnGroundItem(40012, 10, 10);        // 生成 5 個 ID 為 40012 的物品，存活時間 10 秒
                             spawnGroundItem(40317, 1, 10);         // 生成 1 個 ID 為 40317 的物品，存活時間 10 秒
                             spawnGroundItem(40094, 1, 10);         // 生成 1 個 ID 為 40094 的物品，存活時間 10 秒
                             spawnGroundItem(40079, 1, 10);         // 生成 1 個 ID 為 40079 的物品，存活時間 10 秒
                         // 發送消息通知
                         sendMessage("競技場管理員: 第 3 軍隊的進場已完成。");
                         sendMessage("競技場管理員: 6 分鐘後最終戰將開始。");
                                 }
                                 break;
         }
     }

     private void removeRetiredMembers() {
         L1PcInstance[] temp = getMembersArray();
         for (int i = 0; i < temp.length; i++) {
             if (temp[i].getMapId() != this._mapId) {
                 removeMember(temp[i]);
             }
         }
     }

     private void sendMessage(String msg) {
         for (L1PcInstance pc : getMembersArray()) {
             pc.sendPackets((ServerBasePacket)new S_SystemMessage(msg));
         }
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
             loc = this._location.randomLocation((getLocX2() - getLocX1()) / 2, false);
             if (temp.isStackable()) {
                 item = ItemTable.getInstance().createItem(itemId);
                 item.setEnchantLevel(0);
                 item.setCount(stackCount);
                 ground = L1World.getInstance().getInventory(loc.getX(), loc.getY(), this._mapId);
                 if (ground.checkAddItem(item, stackCount) == 0) {
                     ground.storeItem(item);
                 }
             } else {
                 item = null;
                 for (int createCount = 0; createCount < stackCount; createCount++) {
                     item = ItemTable.getInstance().createItem(itemId);
                     item.setEnchantLevel(0);
                     ground = L1World.getInstance().getInventory(loc.getX(), loc.getY(), this._mapId);
                     if (ground.checkAddItem(item, stackCount) == 0) {
                         ground.storeItem(item);
                     }
                 }
             }
         }
     }

     private void clearColosseum() {
         L1MonsterInstance mob = null;
         L1Inventory inventory = null;
         for (L1Object obj : L1World.getInstance().getVisibleObjects(this._mapId).values()) {
             if (obj instanceof L1MonsterInstance) {
                 mob = (L1MonsterInstance)obj;
                 if (!mob.isDead()) {
                     mob.setDead(true);
                     mob.setStatus(8);
                     mob.setCurrentHp(0);
                     mob.deleteMe();
                 }  continue;
             }
             if (obj instanceof L1Inventory) {
                 inventory = (L1Inventory)obj;
                 inventory.clearItems();
             }
         }
     }



     class UbThread
             implements Runnable
     {
         private void countDown() throws InterruptedException {
             for (int loop = 0; loop < L1UltimateBattle.BEFORE_MINUTE * 60 - 15; loop++) {
                 Thread.sleep(1000L);
             }

             L1UltimateBattle.this.removeRetiredMembers();

             L1UltimateBattle.this.sendMessage("競技場管理員: 怪物很快就會出現。祝你好運！.");
             Thread.sleep(5000L);
             L1UltimateBattle.this.sendMessage("競技場管理員: 比賽還有10秒開始.");

             Thread.sleep(5000L);
             L1UltimateBattle.this.sendMessage("競技場管理員: 5 !!");

             Thread.sleep(1000L);
             L1UltimateBattle.this.sendMessage("競技場管理員: 4 !!");

             Thread.sleep(1000L);
             L1UltimateBattle.this.sendMessage("競技場管理員: 3 !!");

             Thread.sleep(1000L);
             L1UltimateBattle.this.sendMessage("競技場管理員: 2 !!");

             Thread.sleep(1000L);
             L1UltimateBattle.this.sendMessage("競技場管理員: 1 !!");

             Thread.sleep(1000L);
             L1UltimateBattle.this.removeRetiredMembers();
         }

         private void waitForNextRound(int curRound) throws InterruptedException {
             int[] WAIT_TIME_TABLE = { 7, 12, 36, 18 };

             int wait = WAIT_TIME_TABLE[curRound - 1];
             if (L1UltimateBattle.this._ubId == 4) {
                 if (curRound == 1) {
                     wait = 12;
                 } else if (curRound == 2) {
                     wait = 36;
                 } else if (curRound == 3) {
                     wait = 18;
                 }
             }
             for (int i = 0; i < wait; i++) {
                 Thread.sleep(10000L);
             }

             L1UltimateBattle.this.removeRetiredMembers();
         }

         public void run() {
             try {
                 L1UltimateBattle.this.setActive(true);
                 countDown();
                 L1UltimateBattle.this.setNowUb(true);
                 L1UbPattern pattern = null;
                 ArrayList<L1UbSpawn> spawnList = null;
                 for (int round = 1; round <= L1UltimateBattle.ubcount; round++) {
                     L1UltimateBattle.this.sendRoundMessage(round);

                     pattern = UBSpawnTable.getInstance().getPattern(L1UltimateBattle.this._ubId, L1UltimateBattle.this._pattern);

                     spawnList = pattern.getSpawnList(round);

                     for (L1UbSpawn spawn : spawnList) {
                         if (L1UltimateBattle.this.getMembersCount() > 0) {
                             spawn.spawnAll();
                         }

                         Thread.sleep((spawn.getSpawnDelay() * 1000));
                     }

                     if (L1UltimateBattle.this.getMembersCount() > 0) {
                         L1UltimateBattle.this.spawnSupplies(round);
                     }

                     for (L1PcInstance pc : L1UltimateBattle.this.getMembersArray()) {
                         UBTable.getInstance().writeUbScore(L1UltimateBattle.this.getUbId(), pc);
                     }
                     waitForNextRound(round);
                 }

                 for (L1PcInstance pc : L1UltimateBattle.this.getMembersArray()) {
                     int[] loc = Getback.GetBack_Location(pc, true);


                     pc.start_teleport(loc[0], loc[1], loc[2], 5, 18339, true, false);
                     L1UltimateBattle.this.removeMember(pc);
                 }
                 L1UltimateBattle.this.clearColosseum();
                 L1UltimateBattle.this.setActive(false);
                 L1UltimateBattle.this.setNowUb(false);
             } catch (Exception e) {
                 L1UltimateBattle._log.log(Level.SEVERE, "L1UltimateBattle[]Error", e);
             }
         }
     }

     public void start() {
         switch (getUbId()) { // 根據無限大戰的 ID 進行操作

             case 2:
                 L1World.getInstance().broadcastServerMessage("稍後將在威爾頓村開始無限大戰。");
                 // 發送信息：稍後將在威爾頓村開始無限大戰。
                 L1World.getInstance().broadcastServerMessage("稍後將在威爾頓村開始無限大戰。");
                 break;

             case 3:
                 L1World.getInstance().broadcastServerMessage("稍後將在格魯丁村開始無限大戰。");
                 // 發送信息：稍後將在格魯丁村開始無限大戰。
                 L1World.getInstance().broadcastServerMessage("稍後將在格魯丁村開始無限大戰。");
                 break;

             case 4:
                 L1World.getInstance().broadcastServerMessage("稍後將在說話島開始無限大戰。");
                 // 發送信息：稍後將在說話島開始無限大戰。
                 L1World.getInstance().broadcastServerMessage("稍後將在說話島開始無限大戰。");
                 break;

             case 5:
                 L1World.getInstance().broadcastServerMessage("稍後將在銀騎士村開始無限大戰。");
                 // 發送信息：稍後將在銀騎士村開始無限大戰。
                 L1World.getInstance().broadcastServerMessage("稍後將在銀騎士村開始無限大戰。");
                 break;
         }
     }




         this._pattern = 1;
         if (this._ubId == 4) {
             ubcount = 3;
         } else {
             ubcount = 4;
         }

         UbThread ub = new UbThread();
         GeneralThreadPool.getInstance().execute(ub);
     }

     public void addMember(L1PcInstance pc) {
         if (!this._members.contains(pc)) {
             this._members.add(pc);
         }
     }

     public void removeMember(L1PcInstance pc) {
         this._members.remove(pc);
     }

     public void clearMembers() {
         this._members.clear();
     }

     public boolean isMember(L1PcInstance pc) {
         return this._members.contains(pc);
     }

     public L1PcInstance[] getMembersArray() {
         return this._members.<L1PcInstance>toArray(new L1PcInstance[this._members.size()]);
     }

     public int getMembersCount() {
         return this._members.size();
     }

     private void setNowUb(boolean i) {
         this._isNowUb = i;
     }

     public boolean isNowUb() {
         return this._isNowUb;
     }

     public int getUbId() {
         return this._ubId;
     }

     public void setUbId(int id) {
         this._ubId = id;
     }

     public short getMapId() {
         return this._mapId;
     }

     public void setMapId(short mapId) {
         this._mapId = mapId;
     }

     public int getMinLevel() {
         return this._minLevel;
     }

     public void setMinLevel(int level) {
         this._minLevel = level;
     }

     public int getMaxLevel() {
         return this._maxLevel;
     }

     public void setMaxLevel(int level) {
         this._maxLevel = level;
     }

     public int getMaxPlayer() {
         return this._maxPlayer;
     }

     public void setMaxPlayer(int count) {
         this._maxPlayer = count;
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

     public void setEnter전사(boolean enter전사) {
         this._enter전사 = enter전사;
     }

     public void setEnterMale(boolean enterMale) {
         this._enterMale = enterMale;
     }

     public void setEnterFemale(boolean enterFemale) {
         this._enterFemale = enterFemale;
     }

     public void setEnterlancer(boolean enterlancer) {
         this._enterLancer = enterlancer;
     }

     public boolean canUsePot() {
         return this._usePot;
     }

     public void setUsePot(boolean usePot) {
         this._usePot = usePot;
     }

     public int getHpr() {
         return this._hpr;
     }

     public void setHpr(int hpr) {
         this._hpr = hpr;
     }

     public int getMpr() {
         return this._mpr;
     }

     public void setMpr(int mpr) {
         this._mpr = mpr;
     }

     public int getLocX1() {
         return this._locX1;
     }

     public void setLocX1(int locX1) {
         this._locX1 = locX1;
     }

     public int getLocY1() {
         return this._locY1;
     }

     public void setLocY1(int locY1) {
         this._locY1 = locY1;
     }

     public int getLocX2() {
         return this._locX2;
     }

     public void setLocX2(int locX2) {
         this._locX2 = locX2;
     }

     public int getLocY2() {
         return this._locY2;
     }

     public void setLocY2(int locY2) {
         this._locY2 = locY2;
     }

     public void resetLoc() {
         this._locX = (this._locX2 + this._locX1) / 2;
         this._locY = (this._locY2 + this._locY1) / 2;
         this._location = new L1Location(this._locX, this._locY, this._mapId);
     }

     public L1Location getLocation() {
         return this._location;
     }

     public void addManager(int npcId) {
         this._managers.add(Integer.valueOf(npcId));
     }

     public boolean containsManager(int npcId) {
         return this._managers.contains(Integer.valueOf(npcId));
     }

     public void addUbTime(int time) {
         this._ubTimes.add(Integer.valueOf(time));
     }

     public String getNextUbTime() {
         return intToTimeFormat(nextUbTime());
     }

     private int nextUbTime() {
         SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
         int nowTime = Integer.valueOf(sdf.format(getRealTime().getTime())).intValue();
         SortedSet<Integer> tailSet = this._ubTimes.tailSet(Integer.valueOf(nowTime));
         if (tailSet.isEmpty()) {
             tailSet = this._ubTimes;
         }
         return ((Integer)tailSet.first()).intValue();
     }

     private static String intToTimeFormat(int n) {
         return (n / 100) + ":" + (n % 100 / 10) + "" + (n % 10);
     }

     private static Calendar getRealTime() {
         TimeZone _tz = TimeZone.getTimeZone(Config.Synchronization.TimeZone);
         Calendar cal = Calendar.getInstance(_tz);
         return cal;
     }

     public boolean checkUbTime() {
         SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
         Calendar realTime = getRealTime();
         realTime.add(12, BEFORE_MINUTE);
         int nowTime = Integer.valueOf(sdf.format(realTime.getTime())).intValue();
         return this._ubTimes.contains(Integer.valueOf(nowTime));
     }

     private void setActive(boolean f) {
         this._active = f;
     }

     public boolean isActive() {
         return this._active;
     }

     public boolean canPcEnter(L1PcInstance pc) {
         _log.log(Level.FINE, "pcname=" + pc.getName() + " ubid=" + this._ubId + " minlvl=" + this._minLevel + " maxlvl=" + this._maxLevel);
         if (!IntRange.includes(pc.getLevel(), this._minLevel, this._maxLevel)) {
             return false;
         }

         if ((!pc.isCrown() || !this._enterRoyal) && (!pc.isKnight() || !this._enterKnight) && (!pc.isWizard() || !this._enterMage) && (!pc.isElf() || !this._enterElf) && (!pc.isDarkelf() || !this._enterDarkelf) && (
                 !pc.isDragonknight() || !this._enterDragonknight) && (!pc.isBlackwizard() || !this._enterBlackwizard) && (!pc.is전사() || !this._enter전사) && (!pc.isFencer() || !this._enterFencer) && (!pc.isLancer() || !this._enterLancer)) {
             return false;
         }

         return true;
     }



     public String[] makeUbInfoStrings() {
         if (this._ubInfo != null) {
             return this._ubInfo; // 如果已經有無限大戰信息，直接返回
         }
         String nextUbTime = getNextUbTime(); // 獲取下一次無限大戰的時間
         StringBuilder classesBuff = new StringBuilder(); // 用於存儲可參加無限大戰的職業信息
         if (this._enterBlackwizard) {
             classesBuff.append("幻術師 "); // 幻術師
         }
         if (this._enterDragonknight) {
             classesBuff.append("龍騎士 "); // 龍騎士
         }
         if (this._enterDarkelf) {
             classesBuff.append("黑暗妖精 "); // 黑暗精靈
         }
         if (this._enterMage) {
             classesBuff.append("法師 "); // 魔法師
         }
         if (this._enterElf) {
             classesBuff.append("妖精 "); // 精靈
         }
         if (this._enterKnight) {
             classesBuff.append("騎士 "); // 騎士
         }
         if (this._enterRoyal) {
             classesBuff.append("王族 "); // 君主
         }
         if (this._enter전사) {
             classesBuff.append("戰士 "); // 戰士
         }
         if (this._enterFencer) {
             classesBuff.append("劍士 "); // 劍士
         }
         if (this._enterLancer) {
             classesBuff.append("黃金槍騎 "); // 槍騎士
         }

// 在這裡可以繼續其他邏輯，例如返回組織好的信息字符串數組
         return new String[] { nextUbTime, classesBuff.toString() };
     }
         String classes = classesBuff.toString().trim();

         StringBuilder sexBuff = new StringBuilder();
         if (this._enterMale) {
             sexBuff.append("男性 ");
         }
         if (this._enterFemale) {
             sexBuff.append("女性 ");
         }

         String sex = sexBuff.toString().trim(); // 去除最後的空格
         String loLevel = String.valueOf(this._minLevel); // 最低等級
         String hiLevel = String.valueOf(this._maxLevel); // 最高等級
         String teleport = this._location.getMap().isEscapable() ? "可能" : "不可能"; // 傳送是否可能
         String res = this._location.getMap().isUseResurrection() ? "可能" : "不可能"; // 復活是否可能
         String pot = "可能"; // 使用藥水是否可能
         String hpr = String.valueOf(this._hpr); // 每秒恢復生命值
         String mpr = String.valueOf(this._mpr); // 每秒恢復魔力值
         String summon = this._location.getMap().isTakePets() ? "可能" : "不可能"; // 帶寵物是否可能
         String summon2 = this._location.getMap().isRecallPets() ? "可能" : "不可能"; // 召回寵物是否可能
         // 組織好的無限大戰信息字符串數組
         this._ubInfo = new String[] {
                 nextUbTime, classes, sex, loLevel, hiLevel, teleport, res, pot, hpr, mpr, summon, summon2
         };
         return this._ubInfo; // 返回無限大戰信息

}


