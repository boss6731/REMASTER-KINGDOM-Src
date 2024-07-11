 package l1j.server.server.model.gametime;

 import java.util.List;
 import java.util.concurrent.CopyOnWriteArrayList;
 import l1j.server.server.GeneralThreadPool;
 import l1j.server.server.utils.IntRange;


 public class GameTimeClock
 {
   private static GameTimeClock _instance;
   private volatile GameTime _currentTime = new GameTime();
   private GameTime _previousTime = null;
   private List<TimeListener> _listeners = new CopyOnWriteArrayList<>();
   private List<MJIDayAndNightHandler> m_day_and_night_listeners = new CopyOnWriteArrayList<>();

   private class TimeUpdater implements Runnable {
     public void run() {
       try {
         GameTimeClock.this._previousTime = null;
         GameTimeClock.this._previousTime = GameTimeClock.this._currentTime;
         boolean is_previous_night = GameTimeClock.this.is_night();
         GameTimeClock.this._currentTime = new GameTime();
         GameTimeClock.this.notifyChanged();
         boolean is_current_night = GameTimeClock.this.is_night();
         if (is_previous_night != is_current_night) {
           GeneralThreadPool.getInstance().execute(new GameTimeClock.DayAndNightListener(is_current_night));
         }
       } catch (Exception e) {
         e.printStackTrace();
       }
       GeneralThreadPool.getInstance().schedule(this, 500L);
     }
     private TimeUpdater() {} }

   private boolean isFieldChanged(int field) {
     return (this._previousTime.get(field) != this._currentTime.get(field));
   }

   private void notifyChanged() {
     if (isFieldChanged(2)) {
       for (TimeListener listener : this._listeners) {
         listener.onMonthChanged(this._currentTime);
       }
     }
     if (isFieldChanged(5)) {
       for (TimeListener listener : this._listeners) {
         listener.onDayChanged(this._currentTime);
       }
     }
     if (isFieldChanged(11)) {
       for (TimeListener listener : this._listeners) {
         listener.onHourChanged(this._currentTime);
       }
     }
     if (isFieldChanged(12)) {
       for (TimeListener listener : this._listeners) {
         listener.onMinuteChanged(this._currentTime);
       }
     }
   }

   private GameTimeClock() {
     GeneralThreadPool.getInstance().execute(new TimeUpdater());
   }

   public static void init() {
     _instance = new GameTimeClock();
   }

   public static GameTimeClock getInstance() {
     return _instance;
   }

   public GameTime getGameTime() {
     return this._currentTime;
   }

   public void addListener(TimeListener listener) {
     this._listeners.add(listener);
   }

   public void removeListener(TimeListener listener) {
     this._listeners.remove(listener);
   }

   public void add_days_listener(MJIDayAndNightHandler listener) {
     this.m_day_and_night_listeners.add(listener);
   }

   public void remove_days_listener(MJIDayAndNightHandler listener) {
     this.m_day_and_night_listeners.remove(listener);
   }

   public boolean is_night() {
     return !is_day();
   }

   public boolean is_day() {
     return IntRange.includes(get_gametime_total_seconds(), 21600, 72000);
   }

   public int get_gametime_total_seconds() {
     GameTime time = this._currentTime;
     return time.get(11) * 3600 + time
       .get(12) * 60 + time
       .get(13);
   }

   class DayAndNightListener implements Runnable { private boolean m_is_night;

     DayAndNightListener(boolean is_night) {
       this.m_is_night = is_night;
     }

     public void run() {
       try {
         if (this.m_is_night) {
           on_night();
         } else {
           on_day();
         }
       } catch (Exception e) {
         e.printStackTrace();
       }
     }

     private void on_night() {
       for (MJIDayAndNightHandler listener : GameTimeClock.this.m_day_and_night_listeners)
         listener.on_night();
     }

     private void on_day() {
       for (MJIDayAndNightHandler listener : GameTimeClock.this.m_day_and_night_listeners)
         listener.on_day();
     } }

 }


