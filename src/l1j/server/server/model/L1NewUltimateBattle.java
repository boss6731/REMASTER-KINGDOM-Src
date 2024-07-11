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






 public class L1NewUltimateBattle
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

   private static final Logger _log = Logger.getLogger(L1NewUltimateBattle.class.getName());

   private final ArrayList<L1PcInstance> _members = new ArrayList<>(); private String[] _ubInfo;

     private void sendRoundMessage(int curRound) {
         switch (this._ubId) {
             case 1:
             case 2:
             case 3:
             case 5: // 處理 UB ID 為 1, 2, 3, 5 的情況
                 if (curRound == 1) {
                     sendMessage("競技場管理人: 第 1 軍隊進入!"); // 發送第 1 軍隊進入的訊息
                     break;
                 }
                 if (curRound == 2) {
                     sendMessage("競技場管理人: 第 2 軍隊進入!"); // 發送第 2 軍隊進入的訊息
                     break;
                 }
                 if (curRound == 3) {
                     sendMessage("競技場管理人: 第 3 軍隊進入!"); // 發送第 3 軍隊進入的訊息
                     break;
                 }
                 if (curRound == 4) {
                     sendMessage("競技場管理人: 最終戰開始! 限制時間為 5 分鐘。"); // 發送最終戰開始的訊息，限時 5 分鐘
                 }
                 break;
             case 4: // 處理 UB ID 為 4 的情況
                 if (curRound == 1) {
                     sendMessage("競技場管理人: 第 1 軍隊進入!"); // 發送第 1 軍隊進入的訊息
                     break;
                 }
                 if (curRound == 2) {
                     sendMessage("競技場管理人: 第 2 軍隊進入!"); // 發送第 2 軍隊進入的訊息
                     break;
                 }
                 if (curRound == 3) {
                     sendMessage("競技場管理人: 最終戰開始! 限制時間為 5 分鐘。"); // 發送最終戰開始的訊息，限時 5 分鐘
                 }
                 break;
         }
     }

     private void spawnSupplies(int curRound) {
         switch (this._ubId) {
             case 1:
             case 2: // 處理 UB ID 為 1 和 2 的情況
                 if (curRound == 1) {
                     spawnGroundItem(40308, 1000, 60);
                     spawnGroundItem(40017, 3, 20);
                     spawnGroundItem(40011, 5, 20);
                     spawnGroundItem(40012, 3, 20);
                     spawnGroundItem(40317, 1, 5);
                     spawnGroundItem(40079, 1, 10);

                     sendMessage("競技場管理人: 第 1 軍隊的投放已完成。"); // 發送第 1 軍隊投放完成的訊息
                     sendMessage("競技場管理人: 1 分鐘後第 2 軍隊將開始投放。"); // 發送1分鐘後第 2 軍隊將開始投放的訊息
                     break;
                 }
                 if (curRound == 2) {
                     spawnGroundItem(40308, 5000, 50);
                     spawnGroundItem(40017, 7, 20);
                     spawnGroundItem(40011, 10, 20);
                     spawnGroundItem(40012, 5, 20);
                     spawnGroundItem(40317, 1, 7);
                     spawnGroundItem(40093, 1, 10);
                     spawnGroundItem(40079, 1, 10);

                     sendMessage("競技場管理人: 第 2 軍隊的投放已完成。"); // 發送第 2 軍隊投放完成的訊息
                     sendMessage("競技場管理人: 2 分鐘後第 3 軍隊將開始投放。"); // 發送2分鐘後第 3 軍隊將開始投放的訊息
                     break;
                 }
                 if (curRound == 3) {
                     spawnGroundItem(40308, 10000, 30);
                     spawnGroundItem(40017, 7, 20);
                     spawnGroundItem(40011, 20, 20);
                     spawnGroundItem(40012, 10, 10);
                     spawnGroundItem(40317, 1, 10);
                     spawnGroundItem(40094, 1, 10);
                     spawnGroundItem(40079, 1, 10);

                     sendMessage("競技場管理人: 第 3 軍隊的投放已完成。"); // 發送第 3 軍隊投放完成的訊息
                     sendMessage("競技場管理人: 6 分鐘後將開始最終戰。"); // 發送6分鐘後將開始最終戰的訊息
                 }
                 break;
         }
     }

     break;

     case 3: // 處理 UB ID 為 3 的情況

             if (curRound == 1) {
                 spawnGroundItem(40308, 1000, 60);
                 spawnGroundItem(40017, 3, 20);
                 spawnGroundItem(40011, 5, 20);
                 spawnGroundItem(40012, 3, 20);
                 spawnGroundItem(40317, 1, 5);
                 spawnGroundItem(40079, 1, 10);

                 sendMessage("競技場管理人: 第 1 軍隊的投放已完成。"); // 發送第 1 軍隊投放完成的訊息

     sendMessage("競技場管理人: 1 分鐘後第 2 軍隊將開始投放。"); // 發送1分鐘後第 2 軍隊將開始投放的訊息

     break;
             }
             if (curRound == 2) {
                 spawnGroundItem(40308, 3000, 50);
                 spawnGroundItem(40017, 5, 20);
                 spawnGroundItem(40011, 10, 20);
                 spawnGroundItem(40012, 5, 20);
                 spawnGroundItem(40317, 1, 7);
                 spawnGroundItem(40093, 1, 10);

                 sendMessage("競技場管理人: 第 2 軍隊的投放已完成。"); // 發送第 2 軍隊投放完成的訊息

     sendMessage("競技場管理人: 2 分鐘後第 3 軍隊將開始投放。"); // 發送2分鐘後第 3 軍隊將開始投放的訊息

     break;
             }
             if (curRound == 3) {
                 spawnGroundItem(40308, 5000, 30);
                 spawnGroundItem(40017, 10, 20);
                 spawnGroundItem(40011, 15, 20);
                 spawnGroundItem(40012, 7, 10);
                 spawnGroundItem(40317, 1, 10);
                 spawnGroundItem(40094, 1, 10);

                 sendMessage("競技場管理人: 第 3 軍隊的投放已完成。"); // 發送第 3 軍隊投放完成的訊息

     sendMessage("競技場管理人: 6 分鐘後將開始最終戰。"); // 發送6分鐘後將開始最終戰的訊息
             }
             break;
         break;
         case 4: // 處理 UB ID 為 4 的情況
                 if (curRound == 1) {
                     spawnGroundItem(40308, 200, 60);
                     spawnGroundItem(40017, 3, 20);
                     spawnGroundItem(40011, 5, 20);
                     spawnGroundItem(40317, 1, 5);
                     spawnGroundItem(40079, 1, 10);

                     sendMessage("競技場管理人: 第 1 軍隊的投放已完成。"); // 發送第 1 軍隊投放完成的訊息
     sendMessage("競技場管理人: 2 分鐘後第 2 軍隊將開始投放。"); // 發送2分鐘後第 2 軍隊將開始投放的訊息
     break;
                 }
                 if (curRound == 2) {
                     spawnGroundItem(40308, 500, 50);
                     spawnGroundItem(40017, 7, 20);
                     spawnGroundItem(40011, 12, 20);
                     spawnGroundItem(40012, 5, 20);
                     spawnGroundItem(40317, 1, 7);
                     spawnGroundItem(40093, 1, 10);

                     sendMessage("競技場管理人: 第 2 軍隊的投放已完成。"); // 發送第 2 軍隊投放完成的訊息
     sendMessage("競技場管理人: 6 分鐘後將開始最終戰。"); // 發送6分鐘後將開始最終戰的訊息
                 }
                 break;
                 case 5: // 處理 UB ID 為 5 的情況
                         if (curRound == 1) {
                             spawnGroundItem(40308, 1000, 60);
                             spawnGroundItem(40017, 3, 20);
                             spawnGroundItem(40011, 5, 20);
                             spawnGroundItem(40012, 3, 20);
                             spawnGroundItem(40317, 1, 5);
                             spawnGroundItem(40079, 1, 10);

                             sendMessage("競技場管理人: 第 1 軍隊的投放已完成。"); // 發送第 1 軍隊投放完成的訊息
     sendMessage("競技場管理人: 1 分鐘後第 2 軍隊將開始投放。"); // 發送1分鐘後第 2 軍隊將開始投放的訊息
     break;
                         }
                         if (curRound == 2) {
                             spawnGroundItem(40308, 5000, 50);
                             spawnGroundItem(40017, 7, 20);
                             spawnGroundItem(40011, 10, 20);
                             spawnGroundItem(40012, 5, 20);
                             spawnGroundItem(40317, 1, 7);
                             spawnGroundItem(40093, 1, 10);
                             spawnGroundItem(40079, 1, 10);

                             sendMessage("競技場管理人: 第 2 軍隊的投放已完成。"); // 發送第 2 軍隊投放完成的訊息
     sendMessage("競技場管理人: 2 分鐘後第 3 軍隊將開始投放。"); // 發送2分鐘後第 3 軍隊將開始投放的訊息
     break;
                         }

     public void start() {
         switch (getUbId()) {

             case 2: // 處理 UB ID 為 2 的情況
                 L1World.getInstance().broadcastServerMessage("稍後將在威爾登村莊開始無限大戰。"); // 發送威爾登村莊即將開始無限大戰的訊息
                 break;
             case 3: // 處理 UB ID 為 3 的情況
                 L1World.getInstance().broadcastServerMessage("稍後將在古魯丁村莊開始無限大戰。"); // 發送古魯丁村莊即將開始無限大戰的訊息
                 break;
             case 4: // 處理 UB ID 為 4 的情況
                 L1World.getInstance().broadcastServerMessage("稍後將在說話之島開始無限大戰。"); // 發送說話之島即將開始無限大戰的訊息
                 break;
             case 5: // 處理 UB ID 為 5 的情況
                 L1World.getInstance().broadcastServerMessage("稍後將在銀騎士村莊開始無限大戰。"); // 發送銀騎士村莊即將開始無限大戰的訊息
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

       if ((!pc.isCrown() || !this._enterRoyal)
               && (!pc.isKnight() || !this._enterKnight)
               && (!pc.isWizard() || !this._enterMage)
               && (!pc.isElf() || !this._enterElf)
               && (!pc.isDarkelf() || !this._enterDarkelf)
               && (!pc.isDragonknight() || !this._enterDragonknight)
               && (!pc.isBlackwizard() || !this._enterBlackwizard)
               && (!pc.is전사() || !this._enter戰士)
               && (!pc.isFencer() || !this._enterFencer)
               && (!pc.isLancer() || !this._enterLancer)) {
           return false; // 如果玩家不符合任何職業的進入條件，則返回 false
       }

       return true; // 如果玩家符合任何職業的進入條件，則返回 true



       public String[] makeUbInfoStrings() {
           if (this._ubInfo != null) {
               return this._ubInfo; // 如果 _ubInfo 已經存在，則直接返回
           }
           String nextUbTime = getNextUbTime(); // 獲取下一個無限大戰的時間
           StringBuilder classesBuff = new StringBuilder();
           if (this._enterBlackwizard) {
               classesBuff.append("幻術師 "); // 如果允許黑暗巫師進入，則添加 "幻術師" 字串
           }
           if (this._enterDragonknight) {
               classesBuff.append("龍騎士 "); // 如果允許龍騎士進入，則添加 "龍騎士" 字串
           }
           if (this._enterDarkelf) {
               classesBuff.append("黑暗精靈 "); // 如果允許黑暗妖精進入，則添加 "黑暗精靈" 字串
           }
           if (this._enterMage) {
               classesBuff.append("法師 "); // 如果允許法師進入，則添加 "法師" 字串
           }
           if (this._enterElf) {
               classesBuff.append("妖精 "); // 如果允許妖精進入，則添加 "妖精" 字串
           }
           if (this._enterKnight) {
               classesBuff.append("騎士 "); // 如果允許騎士進入，則添加 "騎士" 字串
           }
           if (this._enterRoyal) {
               classesBuff.append("王族 "); // 如果允許王族進入，則添加 "君主" 字串
           }
           if (this._enter전사) {
               classesBuff.append("戰士 "); // 如果允許戰士進入，則添加 "戰士" 字串
           }
           if (this._enterFencer) {
               classesBuff.append("劍士 "); // 如果允許劍士進入，則添加 "劍士" 字串
           }
           if (this._enterLancer) {
               classesBuff.append("黃金槍騎 "); // 如果允許槍兵進入，則添加 "槍兵" 字串
           }

// 組合最終的 UB 資訊字串
           this._ubInfo = new String[] {"下一個無限大戰時間:" + nextUbTime,"允許進入的職業: " + classesBuff.toString().trim()
           };

           return this._ubInfo; // 返回生成的 UB 資訊字串
       }
       String classes = classesBuff.toString().trim(); // 把允許進入的職業字串去除兩端空白後轉為字串

       StringBuilder sexBuff = new StringBuilder();
       if (this._enterMale) {
           sexBuff.append("男 "); // 如果允許男性進入，則添加 "男 " 字串
       }
       if (this._enterFemale) {
           sexBuff.append("女 "); // 如果允許女性進入，則添加 "女 " 字串
       }
       String sex = sexBuff.toString().trim(); // 把允許進入的性別字串去除兩端空白後轉為字串
       String loLevel = String.valueOf(this._minLevel); // 將最低等級轉換為字串
       String hiLevel = String.valueOf(this._maxLevel); // 將最高等級轉換為字串
       String teleport = this._location.getMap().isEscapable() ? "允許" : "不允許"; // 判斷地圖是否允許傳送
       String res = this._location.getMap().isUseResurrection() ? "允許" : "不允許"; // 判斷地圖是否允許復活
       String pot = "允許"; // 是否允許使用藥水 (此處固定為允許)
       String hpr = String.valueOf(this._hpr); // 將每秒恢復的血量轉換為字串
       String mpr = String.valueOf(this._mpr); // 將每秒恢復的魔量轉換為字串
       String summon = this._location.getMap().isTakePets() ? "允許" : "不允許"; // 判斷地圖是否允許帶寵物
       String summon2 = this._location.getMap().isRecallPets() ? "允許" : "不允許"; // 判斷地圖是否允許召回寵物

// 組合最終的 UB 資訊字串數組
       this._ubInfo = new String[] { nextUbTime, classes, sex, loLevel, hiLevel, teleport, res, pot, hpr, mpr, summon, summon2 };
       return this._ubInfo; // 返回生成的 UB 資訊字串數組
   }


