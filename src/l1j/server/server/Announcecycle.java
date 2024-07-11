 package l1j.server.server;

 import java.io.File;
 import java.io.FileReader;
 import java.io.IOException;
 import java.io.LineNumberReader;
 import java.util.Collection;
 import java.util.StringTokenizer;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import javolution.util.FastList;
 import l1j.server.Config;
 import l1j.server.server.model.Instance.L1PcInstance;
 import l1j.server.server.model.L1World;
 import l1j.server.server.serverpackets.S_SystemMessage;
 import l1j.server.server.serverpackets.ServerBasePacket;

 public class Announcecycle {
     // 定義一個 Logger 對象，用於記錄日誌信息
     private static Logger _log = Logger.getLogger(Announcecycle.class.getName());

     // 定義一個靜態的 Announcecycle 實例
     private static Announcecycle _instance;

     // 定義一個存儲公告循環的列表
     private FastList<String> _Announcecycle;

     // 定義公告循環的大小初始化為 0
     private int _Announcecyclesize = 0;

     // 私有構造函數，調用時加載公告循環
     private Announcecycle() {
         loadAnnouncecycle();
     }

     // 獲取 Announcecycle 的單例實例
     public static Announcecycle getInstance() {
         // 如果實例為空，則創建一個新的 Announcecycle 實例
         if (_instance == null) {
             _instance = new Announcecycle();
         }
         // 返回 Announcecycle 的實例
         return _instance;
     }

     public void reloadAnnouncecycle() {
         // 創建一個指向 "data/Announcecycle.txt" 文件的 File 對象
         File file = new File("data/Announcecycle.txt");

         // 如果文件存在
         if (file.exists()) {
             // 暫存當前的公告循環列表
             FastList<String> tmp = this._Announcecycle;

             // 從磁盤讀取新的公告循環列表
             this._Announcecycle = readFromDiskmulti(file);

             // 如果舊的公告循環列表不為空
             if (tmp != null) {
                 // 清除舊的公告循環列表並將其設為 null
                 tmp.clear();
                 tmp = null;
             }
         } else {
             // 如果文件不存在，記錄一條配置信息
             _log.config("data/Announcecycle.txt");
         }
     }

     public void loadAnnouncecycle() {
    // 創建一個指向 "data/Announcecycle.txt" 文件的 File 對象
         File file = new File("data/Announcecycle.txt");

    // 如果文件存在
         if (file.exists()) {
    // 暫存當前的公告循環列表
             FastList<String> tmp = this._Announcecycle;

    // 從磁盤讀取新的公告循環列表
             this._Announcecycle = readFromDiskmulti(file);

    // 如果舊的公告循環列表不為空
             if (tmp != null) {
    // 清除舊的公告循環列表並將其設為 null
                 tmp.clear();
                 tmp = null;
             }

    // 執行公告循環
             doAnnouncecycle();
         } else {
    // 如果文件不存在，記錄一條配置信息
             _log.config("data/Announcecycle.txt");
         }
     }

   private FastList<String> readFromDiskmulti(File file) {
     FastList<String> list = new FastList();
     LineNumberReader lnr = null;
     try {
       int i = 0;
       String line = null;
       lnr = new LineNumberReader(new FileReader(file));
       while ((line = lnr.readLine()) != null) {
         StringTokenizer st = new StringTokenizer(line, "\n\r");
         if (st.hasMoreTokens()) {
           String showAnnouncecycle = st.nextToken();
           list.add(showAnnouncecycle);
           i++;
         }
       }

         // 記錄已加載的公告週期數量
         _log.config("公告週期：已加載 " + i + " Announcecycle.");
     } catch (IOException e1) {
    // 如果在讀取公告週期時發生錯誤，記錄錯誤並輸出堆棧軌跡
         _log.log(Level.SEVERE, "讀取公告週期時發生錯誤", e1);
     } finally {
         try {
             // 嘗試關閉 LineNumberReader 對象
             lnr.close();
         } catch (IOException e) {
             // 如果關閉時發生錯誤，輸出堆棧軌跡
             e.printStackTrace();
         }
     }
        // 返回讀取到的公告週期列表
       return list;

   public void doAnnouncecycle() {
     AnnouncTask rs = new AnnouncTask();
     GeneralThreadPool.getInstance().scheduleAtFixedRate(rs, 180000L, (60000 * Config.ServerAdSetting.ANNOUNCECYCLETIME));
   }

   class AnnouncTask
     implements Runnable {
     public void run() {
       try {
         Announcecycle.this.ShowAnnounceToAll((String)Announcecycle.this._Announcecycle.get(Announcecycle.this._Announcecyclesize));
         Announcecycle.this._Announcecyclesize++;
         if (Announcecycle.this._Announcecyclesize >= Announcecycle.this._Announcecycle.size())
           Announcecycle.this._Announcecyclesize = 0;
       } catch (Exception e) {
         Announcecycle._log.log(Level.WARNING, "", e);
       }
     }
   }

   private void ShowAnnounceToAll(String msg) {
     Collection<L1PcInstance> allpc = L1World.getInstance().getAllPlayers();
     S_SystemMessage pck = new S_SystemMessage(msg);
     for (L1PcInstance pc : allpc)
       pc.sendPackets((ServerBasePacket)pck, false);
     pck.clear();
   }
 }


