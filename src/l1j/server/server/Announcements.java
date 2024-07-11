     package l1j.server.server;

     import java.io.Closeable;
     import java.io.File;
     import java.io.FileNotFoundException;
     import java.io.FileReader;
     import java.io.IOException;
     import java.io.LineNumberReader;
     import java.util.ArrayList;
     import java.util.List;
     import java.util.StringTokenizer;
     import java.util.logging.Logger;
     import l1j.server.server.model.Instance.L1PcInstance;
     import l1j.server.server.model.L1World;
     import l1j.server.server.serverpackets.S_SystemMessage;
     import l1j.server.server.serverpackets.ServerBasePacket;
     import l1j.server.server.utils.StreamUtil;

     public class Announcements
     {
       public static final int SLEEP_TIME = 600000;
       private static Announcements _instance;
       private final List<String> _announcements = new ArrayList<>();
       private static Logger _log = Logger.getLogger(Announcements.class.getName());

       private Announcements() {
         loadAnnouncements();
       }

         public static Announcements getInstance() {
            // 如果單例實例 _instance 為空
             if (_instance == null) {
                // 創建一個新的 Announcements 實例
                 _instance = new Announcements();
             }

            // 返回單例實例
             return _instance;
         }

         private void loadAnnouncements() {
            // 清空當前的公告列表
             this._announcements.clear();

            // 創建一個指向 "data/announcements.txt" 文件的 File 對象
             File file = new File("data/announcements.txt");

            // 如果文件存在
             if (file.exists()) {
                // 從磁盤讀取公告
                 readFromDisk(file);
             } else {
                // 如果文件不存在，記錄一條配置信息
                 _log.config("data/announcements.txt doesn't exist");
             }
         }

         public void showAnnouncements(L1PcInstance showTo) {
            // 遍歷公告列表中的每條公告
             for (String msg : this._announcements) {
                // 發送公告消息給 L1PcInstance 實例
                 showTo.sendPackets((ServerBasePacket)new S_SystemMessage(msg));
             }
         }

       private void readFromDisk(File file) {
         LineNumberReader lnr = null;

         try { int i = 0;
           String line = null;
           lnr = new LineNumberReader(new FileReader(file));
           StringTokenizer st = null;
           while ((line = lnr.readLine()) != null) {
             st = new StringTokenizer(line, "\n\r");
             if (st.hasMoreTokens()) {
               String announcement = st.nextToken();
               this._announcements.add(announcement);

               i++;
             }
           }

            // 記錄已加載的公告數量
             _log.config("公告" + i + "加載"); // "公告" + i + "加載"

            // 捕獲 FileNotFoundException 並處理，但不進行任何操作
             catch (FileNotFoundException fileNotFoundException) { }

            // 捕獲 IOException 並輸出堆棧軌跡
             catch (IOException e) {
                 e.printStackTrace();
             }

             // 最後確保資源被正確關閉
             finally {
                 StreamUtil.close(new Closeable[] { lnr });
             }

       public void announceToAll(String msg) {
         L1World.getInstance().broadcastServerMessage(msg);
       }
     }


