package l1j.server.MJTemplate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import l1j.server.server.model.gametime.RealTimeClock;

public class MJFormatter {
private static final SimpleDateFormat T_Double_Formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
	public static String get_tdouble_formatter_time(){
		return get_tdouble_formatter_time(new GregorianCalendar());
	}
	public static synchronized String get_tdouble_formatter_time(Calendar cal){
		return T_Double_Formatter.format(cal.getTime());
	}
	public static String get_tdouble_formatter_time(Timestamp ts){
		Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
		if(ts != null)
			cal.setTimeInMillis(ts.getTime());
		return get_tdouble_formatter_time(cal);
	}
}
