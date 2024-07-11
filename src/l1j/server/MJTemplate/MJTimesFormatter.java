package l1j.server.MJTemplate;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MJTimesFormatter {
	/**
	 * @ "yyyy-MM-dd HH:mm:ss"
	 */
	public static final MJTimesFormatter BASIC = new MJTimesFormatter("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * @ "yyyy-MM-dd"
	 */
	public static final MJTimesFormatter DATE_ONLY = new MJTimesFormatter("yyyy-MM-dd");

	/**
	 * @ "HH:mm:ss"
	 */
	public static final MJTimesFormatter TIME_ONLY = new MJTimesFormatter("HH:mm:ss");
	
	/**
	 * @ "yyyy-MM-dd HH:mm:ss.SSS"
	 */
	public static final MJTimesFormatter DOUBLE = new MJTimesFormatter("yyyy-MM-dd HH:mm:ss.SSS");

	/**
	 * @ "yyyy-MM-dd'T'HH:mm:ss.SSS"
	 */
	public static final MJTimesFormatter DOUBLE_T = new MJTimesFormatter("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
	private DateTimeFormatter formatter; 
	public MJTimesFormatter(String pattern){
		formatter = DateTimeFormatter.ofPattern(pattern, Locale.KOREA);
	}
	
	public String toString(long timeMillis){
		return MJTime.toDateTime(timeMillis).format(formatter);
	}
}
