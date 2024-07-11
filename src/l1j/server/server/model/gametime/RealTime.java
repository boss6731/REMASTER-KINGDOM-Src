 package l1j.server.server.model.gametime;

 import java.util.Calendar;
 import java.util.TimeZone;

 public class RealTime
   extends BaseTime {
   protected Calendar makeCalendar(int time) {
     Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
     cal.setTimeInMillis(0L);
     cal.add(13, this._time);
     return cal;
   }


   protected long getBaseTimeInMil() {
     return 0L;
   }


   protected int makeTime(long timeMillis) {
     long t1 = timeMillis - getBaseTimeInMil();
     if (t1 < 0L) {
       throw new IllegalArgumentException();
     }
     int t2 = (int)(t1 / 1000L);
     return t2;
   }
 }


