 package l1j.server.server.model.gametime;

 import java.text.SimpleDateFormat;
 import java.util.Calendar;
 import java.util.TimeZone;
 import l1j.server.server.utils.IntRange;


 public abstract class BaseTime
 {
   protected final int _time;
   protected final Calendar _calendar;

   public BaseTime() {
     this(System.currentTimeMillis());
   }

   public BaseTime(long timeMillis) {
     this._time = makeTime(timeMillis);
     this._calendar = makeCalendar(this._time);
   }

   protected Calendar makeCalendar(int time) {
     Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
     cal.setTimeInMillis(0L);
     cal.add(13, this._time);
     return cal;
   }

   protected abstract int makeTime(long paramLong);

   protected abstract long getBaseTimeInMil();

   public int get(int field) {
     return this._calendar.get(field);
   }

   public int getSeconds() {
     return this._time;
   }

   public Calendar getCalendar() {
     return (Calendar)this._calendar.clone();
   }

   public boolean isNight() {
     int hour = this._calendar.get(11);
     return !IntRange.includes(hour, 6, 17);
   }

   public String toString() {
     SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
     f.setTimeZone(this._calendar.getTimeZone());
     return f.format(this._calendar.getTime()) + "(" + getSeconds() + ")";
   }

   public boolean equals_time(int hour, int minute, int second) {
     return (this._calendar.get(11) == hour && this._calendar
       .get(12) == minute && this._calendar
       .get(13) == second);
   }

   public boolean equals_day_time(int day, int hour, int minute, int second) {
     return (this._calendar.get(7) == day && this._calendar
       .get(11) == hour && this._calendar
       .get(12) == minute && this._calendar
       .get(13) == second);
   }
 }


