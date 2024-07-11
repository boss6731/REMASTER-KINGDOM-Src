package l1j.server.CPMWBQSystem.info;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CPMWBQmapInfo {
	public static CPMWBQmapInfo newInstance(ResultSet rs) throws SQLException{
		return newInstance().setmapId(rs.getInt("mapid"))
				.setmapname(rs.getString("mapname"))
				.setlimitTime(rs.getInt("limitTime"))
				.setdescName(rs.getString("descname"))
				.setTeleport(rs.getBoolean("isteleport"))
				.setlocX(rs.getInt("locx"))
				.setlocY(rs.getInt("locy"))
				.setLocMapid(rs.getInt("locmap"))
				.setcostItem(rs.getInt("costitem"))
				.setcostCounter(rs.getInt("costcounter"));
	}
	
	private static CPMWBQmapInfo newInstance(){
		return new CPMWBQmapInfo();
	}
	
	private int mapId;
	private String mapname;
	private int limitTime;
	private String descName;
	private boolean isTeleport;
	private int locX;
	private int locY;
	private int locMapid;
	private int costItem;
	private int costCounter;
	
	public int LocMapid() {
		return locMapid;
	}

	public CPMWBQmapInfo setLocMapid(int locMapid) {
		this.locMapid = locMapid;
		return this;
	}
	
	public String maname() {
		return mapname;
	}
	public CPMWBQmapInfo setmapname(String Mapname) {
		this.mapname = Mapname;
		return this;
	}
	public int mapId() {
		return mapId;
	}
	
	public CPMWBQmapInfo setmapId(int MapId) {
		this.mapId = MapId;
		return this;
	}
	
	public int limitTime() {
		return limitTime;
	}
	
	public CPMWBQmapInfo setlimitTime(int LimitTime) {
		this.limitTime = LimitTime;
		return this;
	}
	
	public String descName() {
		return descName;
	}
	
	public CPMWBQmapInfo setdescName(String DescName) {
		this.descName = DescName;
		return this;
	}
	
	public boolean isTeleport() {
		return isTeleport;
	}
	
	public CPMWBQmapInfo setTeleport(boolean IsTeleport) {
		this.isTeleport = IsTeleport;
		return this;
	}
	
	public int locX() {
		return locX;
	}
	
	public CPMWBQmapInfo setlocX(int LocX) {
		this.locX = LocX;
		return this;
	}
	
	public int locY() {
		return locY;
	}
	
	public CPMWBQmapInfo setlocY(int LocY) {
		this.locY = LocY;
		return this;
	}
	
	public int costItem() {
		return costItem;
	}
	
	public CPMWBQmapInfo setcostItem(int CostItem) {
		this.costItem = CostItem;
		return this;
	}
	
	public int costCounter() {
		return costCounter;
	}
	
	public CPMWBQmapInfo setcostCounter(int CostCounter) {
		this.costCounter = CostCounter;
		return this;
	}
}
