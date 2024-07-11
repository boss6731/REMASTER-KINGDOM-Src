 package l1j.server.server;

 import MJFX.UIAdapter.MJUIAdapter;
 import java.util.logging.Logger;
 import l1j.server.server.model.Instance.L1PcInstance;




 public class Shutdown extends Thread {
   // 創建日誌記錄器
   private static Logger _log = Logger.getLogger(Shutdown.class.getName());

   // 靜態實例變量
   private static Shutdown _instance;
   private static Shutdown _counterInstance = null;

   // 關閉秒數與模式變量
   private int secondsShut;
   private int shutdownMode;

   // 定義關閉模式的常量值
   public static final int SIGTERM = 0;
   public static final int GM_SHUTDOWN = 1;
   public static final int GM_RESTART = 2;
   public static final int ABORT = 3;

    // 關閉模式的文本描述

   private static String[] _modeText = new String[] { "SIGTERM", "shuting down", "restarting", "aborting" };






   public Shutdown() {
     this.secondsShut = -1;
     this.shutdownMode = 0;
   }



   public Shutdown(int seconds, boolean restart) {
     if (seconds < 0) {
       seconds = 0;
     }
     this.secondsShut = seconds;
     if (restart) {
       this.shutdownMode = 2;
     } else {
       this.shutdownMode = 1;
     }
   }



   public static Shutdown getInstance() {
     if (_instance == null) {
       _instance = new Shutdown();
     }
     return _instance;
   }



   public void run() {
     if (this == _instance) {

      // 保存數據
       saveData();

     } else {

      // 倒數計時
       countdown();

       _log.warning("GM 關閉倒計時結束。伺服器 " + _modeText[this.shutdownMode] + " 現在關閉！");
       System.out.println("GM 關閉倒計時結束。伺服器 " + _modeText[this.shutdownMode] + " 現在關閉！");

      // 根據關閉模式執行不同的操作
       switch (this.shutdownMode) {
         case 1:
           _instance.setMode(1);
           MJUIAdapter.on_exit();
           break;
         case 2:
           _instance.setMode(2);
           System.exit(1);
           break;
       }
     }
   }



   public void startShutdown(L1PcInstance activeChar, int seconds, boolean restart) {
    // 獲取公告系統的單例實例
     Announcements _an = Announcements.getInstance();

    // 記錄警告訊息
     _log.warning("GM: " + activeChar.getId() + " 發出了關閉命令。伺服器將在 " + seconds + " 秒後 " + _modeText[this.shutdownMode] + "!");

    // 廣播關閉操作消息
     _an.announceToAll("伺服器將在 " + seconds + " 秒後 " + _modeText[this.shutdownMode] + "!");

    // 在控制台打印消息
     System.out.println("GM: " + activeChar.getId() + " 發出了關閉命令。伺服器將在 " + seconds + " 秒後 " + _modeText[this.shutdownMode] + "!");

    // 確認計數器實例不為 null，並中止其操作
     if (_counterInstance != null) {
       _counterInstance._abort();
     }

    // 創建新的關閉計數器實例
     _counterInstance = new Shutdown(seconds, restart);

    // 執行新的關閉計數器
     GeneralThreadPool.getInstance().execute(_counterInstance);
   }







   public void abort(L1PcInstance activeChar) {
      // 獲取公告系統的單例實例
     Announcements _an = Announcements.getInstance();

      // 記錄警告訊息
     _log.warning("GM: " + activeChar.getName() + " 發出了中止關閉命令。");

      // 廣播中止關閉操作的消息
     _an.announceToAll("伺服器中止關閉操作並繼續正常運行！");

      // 在控制台打印消息
     System.out.println("GM: " + activeChar.getName() + " 發出了中止關閉命令。");

      // 中止現有計數器實例
     if (_counterInstance != null) {
       _counterInstance._abort();
     }
   }


   private void setMode(int mode) {
     this.shutdownMode = mode;
   }

   int getMode() {
     return this.shutdownMode;
   }

   private void _abort() {
     this.shutdownMode = 3;
   }

   private void countdown() {
    // 獲取公告系統的單例實例
     Announcements _an = Announcements.getInstance();

     try {
       while (this.secondsShut > 0) {
         switch (this.secondsShut) {
           case 240:
             _an.announceToAll("伺服器將在 4 分鐘後關閉。");
             break;
           case 180:
             _an.announceToAll("伺服器將在 3 分鐘後關閉。");
             break;
           case 120:
             _an.announceToAll("伺服器將在 2 分鐘後關閉。");
             break;
           case 60:
             _an.announceToAll("伺服器將在 1 分鐘後關閉。");
             break;
           case 30:
             _an.announceToAll("伺服器將在 30 秒後關閉。");
             break;
           case 10:
             _an.announceToAll("伺服器將在 10 秒後關閉。");
             break;
           case 5:
             _an.announceToAll("伺服器將在 5 秒後關閉。");
             break;
           case 4:
             _an.announceToAll("伺服器將在 4 秒後關閉。");
             break;
           case 3:
             _an.announceToAll("伺服器將在 3 秒後關閉。");
             break;
           case 2:
             _an.announceToAll("伺服器將在 2 秒後關閉。");
             break;
           case 1:
             _an.announceToAll("伺服器將在 1 秒後關閉。");
             break;
         }

         this.secondsShut--;

         int delay = 1000;
         Thread.sleep(delay);

         if (this.shutdownMode == 3) {
           break;
         }
       }
     } catch (InterruptedException interruptedException) {
       Thread.currentThread().interrupt();
     }
   }







   private void saveData() {
     // 獲取公告系統的單例實例
     Announcements _an = Announcements.getInstance();

     // 根據關閉模式輸出不同的消息
     switch (this.shutdownMode) {
       case 0:
         System.err.println("收到 SIGTERM 信號。立即關閉伺服器！");
         break;
       case 1:
         System.err.println("收到 GM 關閉指令。立即關閉伺服器！");
         break;
       case 2:
         System.err.println("收到 GM 重啟指令。立即重啟伺服器！");
         break;
       default:
         System.err.println("收到未知關閉模式。立即關閉伺服器！");
         break;
     }

     // 廣播關閉或重啟操作消息
     _an.announceToAll("伺服器現在 " + _modeText[this.shutdownMode] + "！再見");

     // 斷開所有玩家連接
     GameServer.getInstance().disconnectAllCharacters();

     // 輸出數據保存及玩家斷開後的消息
     System.err.println("數據已保存。所有玩家已斷開，伺服器關閉中。");

     // 延遲操作
     try {
       int delay = 500;
       Thread.sleep(delay);
     } catch (InterruptedException interruptedException) {
       Thread.currentThread().interrupt();
     }
   }



     public void startTelnetShutdown(String IP, int seconds, boolean restart) {
        // 獲取公告系統的單例實例
         Announcements _an = Announcements.getInstance();

        // 記錄警告訊息
         _log.warning("IP: " + IP + " 發出關機命令. " + _modeText[this.shutdownMode] + " in " + seconds + " 秒！");

        // 廣播關閉操作消息
         _an.announceToAll("伺服器是 " + _modeText[this.shutdownMode] + " in " + seconds + " 秒！");

        // 在控制台打印消息
         System.out.println("IP: " + IP + " 發出關機命令. " + _modeText[this.shutdownMode] + " in " + seconds + " 秒！");

        // 確認計數器實例不為 null，並中止其操作

         if (_counterInstance != null) {
             _counterInstance._abort();
         }

        // 創建新的關閉計數器實例
         _counterInstance = new Shutdown(seconds, restart);

        // 執行新的關閉計數器
         GeneralThreadPool.getInstance().execute(_counterInstance);
     }







   public void Telnetabort(String IP) {
    // 獲取公告系統的單例實例
     Announcements _an = Announcements.getInstance();

    // 記錄警告訊息
     _log.warning("IP: " + IP + " 發出關閉 ABORT. " + _modeText[this.shutdownMode] + " 已被停止!");

    // 廣播中止操作消息
     _an.announceToAll("Server aborts " + _modeText[this.shutdownMode] + " 並繼續正常運行!");

    // 在控制台打印消息
     System.out.println("IP: " + IP + " 發出關閉 ABORT. " + _modeText[this.shutdownMode] + " 已被停止!");

    // 確認計數器實例不為 null，並中止其操作
     if (_counterInstance != null) {
       _counterInstance._abort();
     }
   }


