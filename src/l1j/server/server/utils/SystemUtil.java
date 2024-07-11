 package l1j.server.server.utils;

 import java.util.Calendar;
 import java.util.Date;



 public class SystemUtil
 {
   public static long getUsedMemoryMB() {
     return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024L / 1024L;
   }

   private static Date date = new Date(0L);
     // 定義一個包含星期幾的字串陣列
     private static String[] weekDay = new String[] { "日", "一", "二", "三", "四", "五", "六" };


   public static String getYoil(long time) {
     date.setTime(time);
     Calendar c = Calendar.getInstance();
     c.setTime(date);
     return weekDay[c.get(7) - 1];
   }
 }


