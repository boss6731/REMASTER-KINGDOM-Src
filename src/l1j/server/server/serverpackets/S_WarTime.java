     package l1j.server.server.serverpackets;

     import java.util.Calendar;
     import l1j.server.Config;


     public class S_WarTime extends ServerBasePacket {
         private static final String S_WAR_TIME = "[S] S_WarTime";

         public S_WarTime(Calendar cal) {
             // 設定基準日期為1997年1月1日下午5點
             Calendar base_cal = Calendar.getInstance();
             base_cal.set(1997, 0, 1, 17, 0);

             // 計算基準日期與給定日期之間的毫秒差
             long base_millis = base_cal.getTimeInMillis();
             long millis = cal.getTimeInMillis();
             long diff = millis - base_millis;

             // 減去72000000毫秒 (20個小時) 補償時間
             diff -= 72000000L;

             // 將毫秒差轉換為分鐘
             diff /= 60000L;

             // 將分鐘差轉換為自定義時間單位 (每182分鐘為一個單位)
             int time = (int)(diff / 182L);

             // 在這裡應該還有其他程式碼來處理 `time` 變數，例如將時間寫入數據包
         }

         // 其他方法和成員變數應該在這裡，例如 `getContent()` 和 `getType()` 方法
     }




         writeC(166);
         writeH(6);
         writeS(Config.Synchronization.TimeZone);
         writeH(1);
         writeC(136);
         writeH(time);
         writeH(2);
         writeC(178);
         writeH(time);
         writeH(3);
         writeC(220);
         writeH(time);
         writeH(4);
         writeC(218);
         writeH(time + 1);
         writeH(5);
         writeC(4);
         writeH(time + 2);
         writeH(6);
         writeC(46);
         writeD(time + 2);
         writeC(0);
       }


       public byte[] getContent() {
         return getBytes();
       }

       public String getType() {
         return "[S] S_WarTime";
       }
     }


