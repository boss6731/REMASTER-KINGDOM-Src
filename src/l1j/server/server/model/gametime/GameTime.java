 package l1j.server.server.model.gametime;





 public class GameTime
   extends BaseTime
 {
   protected static final long BASE_TIME_IN_MILLIS_REAL = 1483196400065L;

   protected long getBaseTimeInMil() {
     return 1483196400065L;
   }


   protected int makeTime(long timeMillis) {
     if (timeMillis <= 1483196400065L) {
       throw new IllegalArgumentException();
     }

     int t1 = (int)((timeMillis - getBaseTimeInMil()) / 1000L);



     return t1 * 6;
   }
 }


