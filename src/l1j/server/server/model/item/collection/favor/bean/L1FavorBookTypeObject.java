package l1j.server.server.model.item.collection.favor.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import l1j.server.server.utils.StringUtil;

public class L1FavorBookTypeObject {
	private static final SimpleDateFormat DATE_FORMAT	= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private int type;
	private String desc;
	private Timestamp startTime;
	private Timestamp endTime;
	private String startTimeToString;
	private String endTimeToString;
	
	public L1FavorBookTypeObject(ResultSet rs) throws SQLException {
		this(rs.getInt("type"), rs.getString("desc"), rs.getTimestamp("startTime"), rs.getTimestamp("endTime"));
	}
	
	public L1FavorBookTypeObject(int type, String desc, Timestamp startTime, Timestamp endTime) {
		this.type		= type;
		this.desc		= desc;
		this.startTime	= startTime;
		this.endTime	= endTime;
		if (startTime != null) {
			this.startTimeToString	= DATE_FORMAT.format(startTime);
		}
		if (endTime != null) {
			this.endTimeToString	= DATE_FORMAT.format(endTime);
		}
	}
	
	public int getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public String getStartTimeToString() {
		return startTimeToString;
	}
	public String getEndTimeToString() {
		return endTimeToString;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("type : ").append(type).append(StringUtil.LineString);
		sb.append("desc : ").append(desc).append(StringUtil.LineString);
		if(startTime != null){
			sb.append("startTime : ").append(startTimeToString).append(StringUtil.LineString);
		}
		if(endTime != null){
			sb.append("endTime : ").append(endTimeToString).append(StringUtil.LineString);
		}
		return sb.toString();
	}
}
