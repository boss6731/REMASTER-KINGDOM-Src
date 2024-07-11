 package l1j.server.server.model.gametime;

 import java.util.Calendar;
 import java.util.HashMap;
 import java.util.List;
 import java.util.TimeZone;
 import java.util.concurrent.CopyOnWriteArrayList;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import l1j.server.server.GeneralThreadPool;




 public class RealTimeClock
 {
   public static final int DAY_TO_HOURS = 24;
   public static final int YEAR_TO_HOURS = 8760;
   public static final int MINUTE_SECONDS = 60;
   public static final int HOUR_SECONDS = 3600;
   public static final int DAYS_SECONDS = 86400;
   private static Logger _log = Logger.getLogger(RealTimeClock.class.getName());
   private static RealTimeClock _instance;
   private volatile RealTime _currentTime = new RealTime();
   private RealTime _previousTime = null;
   private HashMap<Integer, List<TimeListener>> _listeners;

   private RealTimeClock() {
     this._listeners = new HashMap<>(8);
     this._listeners.put(Integer.valueOf(2), new CopyOnWriteArrayList<>());
     this._listeners.put(Integer.valueOf(5), new CopyOnWriteArrayList<>());
     this._listeners.put(Integer.valueOf(11), new CopyOnWriteArrayList<>());
     this._listeners.put(Integer.valueOf(12), new CopyOnWriteArrayList<>());
     this._listeners.put(Integer.valueOf(13), new CopyOnWriteArrayList<>());
     GeneralThreadPool.getInstance().execute(new TimeUpdater());
   }

   public void print() {
     List<TimeListener> listeners_array = this._listeners.get(Integer.valueOf(12));
     for (TimeListener listener : listeners_array)
       System.out.println(listener);
     for (List<TimeListener> listeners : this._listeners.values()) {
       for (TimeListener listener : listeners)
         System.out.println(listener);
     }
   }

   private class TimeUpdater implements Runnable {
     public void run() {
       long cur = System.currentTimeMillis();
       try {
         RealTimeClock.this._previousTime = null;
         RealTimeClock.this._previousTime = RealTimeClock.this._currentTime;
         RealTimeClock.this._currentTime = new RealTime();
         RealTimeClock.this.notifyChanged();
       } catch (Exception e) {
         RealTimeClock._log.log(Level.SEVERE, "RealTimeClock[]Error", e);
       } finally {
         cur = System.currentTimeMillis() - cur;
         if (cur < 500L) { GeneralThreadPool.getInstance().schedule(this, 500L - cur); }
         else { GeneralThreadPool.getInstance().execute(this); }

       }
     }
     private TimeUpdater() {} }
   private boolean isFieldChanged(int field) {
     return (this._previousTime.get(field) != this._currentTime.get(field));
   }

   private void notifyChanged() {
     List<TimeListener> list = null;
     if (isFieldChanged(2)) {
       list = this._listeners.get(Integer.valueOf(2));
       for (TimeListener listener : list)
         listener.onMonthChanged(this._currentTime);
     }
     if (isFieldChanged(5)) {
       list = this._listeners.get(Integer.valueOf(5));
       for (TimeListener listener : list)
         listener.onDayChanged(this._currentTime);
     }
     if (isFieldChanged(11)) {
       list = this._listeners.get(Integer.valueOf(11));
       for (TimeListener listener : list)
         listener.onHourChanged(this._currentTime);
     }
     if (isFieldChanged(12)) {
       list = this._listeners.get(Integer.valueOf(12));
       for (TimeListener listener : list)
         listener.onMinuteChanged(this._currentTime);
     }
     if (isFieldChanged(13)) {
       list = this._listeners.get(Integer.valueOf(13));
       for (TimeListener listener : list)
         listener.onSecondChanged(this._currentTime);
     }
   }

   public static void init() {
     _instance = new RealTimeClock();
   }

   public static RealTimeClock getInstance() {
     return _instance;
   }

   public RealTime getRealTime() {
     return this._currentTime;
   }

   public void addListener(TimeListener listener, int type) {
     List<TimeListener> list = this._listeners.get(Integer.valueOf(type));
     if (list == null)
       throw new IllegalArgumentException(String.format("無效 的 偵聽器 類型...%d", new Object[] { Integer.valueOf(type) }));
     list.add(listener);
   }

   public void removeListener(TimeListener listener, int type) {
     List<TimeListener> list = this._listeners.get(Integer.valueOf(type));
     if (list == null) {
       throw new IllegalArgumentException(String.format("無效 的 偵聽器 類型...%d", new Object[] { Integer.valueOf(type) }));
     }
     list.remove(listener);
   }

   public Calendar getRealTimeCalendar() {
     Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
     return cal;
   }
 }


