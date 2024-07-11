 package l1j.server.server.utils;




 public class PerformanceTimer
 {
   private long _begin = System.currentTimeMillis();

   public void reset() {
     this._begin = System.currentTimeMillis();
   }

   public long get() {
     return System.currentTimeMillis() - this._begin;
   }
 }


