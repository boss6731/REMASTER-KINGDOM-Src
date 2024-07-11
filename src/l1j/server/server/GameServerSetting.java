     package l1j.server.server;

     import l1j.server.Config;

     public class GameServerSetting
     {
       private static GameServerSetting _instance;

       public static GameServerSetting getInstance() {
         if (_instance == null) {
           _instance = new GameServerSetting();
         }
         return _instance;
       }


        // 聲明一些靜態布爾變量，用於控制不同類型的聊天功能
        public static boolean 一般聊天 = false; // 一般聊天

        public static boolean 悄悄話 = false; // 私聊（悄悄話）

        public static boolean 世界聊天 = false; // 世界聊天

        public static boolean 公會聊天 = false; // 公會聊天

        public static boolean 組隊聊天 = false; // 組隊聊天

        public static boolean 買賣 = false; // 買賣聊天
       public static boolean Att = false;
       public static boolean NYEvent = false;
       public static boolean ServerDown = false;
       private int maxLevel = Config.CharSettings.LimitLevel;

       public int get_maxLevel() {
         return this.maxLevel;
       }

       public void set_maxLevel(int maxLevel) {
         this.maxLevel = maxLevel;
       }
     }


