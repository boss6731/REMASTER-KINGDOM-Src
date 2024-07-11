package l1j.server.MJTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class MJTime {
	public static LocalDateTime toDateTime(long millis){
		return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
	
	public static long convertLong(Timestamp ts) {
		return ts == null ? 0L : ts.getTime();
	}
	
}
