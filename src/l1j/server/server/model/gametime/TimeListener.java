package l1j.server.server.model.gametime;

public interface TimeListener {
  void onMonthChanged(BaseTime paramBaseTime);
  
  void onDayChanged(BaseTime paramBaseTime);
  
  void onHourChanged(BaseTime paramBaseTime);
  
  void onMinuteChanged(BaseTime paramBaseTime);
  
  void onSecondChanged(BaseTime paramBaseTime);
}


