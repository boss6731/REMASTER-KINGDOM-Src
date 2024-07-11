package l1j.server.DragonRaidSystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.StringTokenizer;

public class DragonRaidSystemInfo {
	static DragonRaidSystemInfo newInstance(ResultSet rs) throws SQLException {
		DragonRaidSystemInfo RaidInfo = newInstance();
		String spawn_time = rs.getString("spawn_time");
		RaidInfo._bossnum = rs.getInt("boss_num");
		RaidInfo._bossname = rs.getString("boss_name");
		RaidInfo._dragonid = rs.getInt("dragon_id");
		RaidInfo._mapx = rs.getInt("map_x");
		RaidInfo._mapy = rs.getInt("map_y");
		RaidInfo._mapid = rs.getInt("map_id");
		RaidInfo._spawn_day = rs.getString("spawn_day");
		
		if (spawn_time.length() > 0) {
			StringTokenizer stt = new StringTokenizer(spawn_time, ",");
			int[][] time = new int[stt.countTokens()][2];
			int idx = 0;
			while (stt.hasMoreTokens()) {
				String boss_time = stt.nextToken();
				String boss_h = boss_time.substring(0, boss_time.indexOf(":"));
				String boss_m = boss_time.substring(boss_h.length() + 1, boss_time.length());
				time[idx][0] = Integer.valueOf(boss_h);
				time[idx][1] = Integer.valueOf(boss_m);
				idx += 1;
			}
			RaidInfo._bosstime = time;
		}
		return RaidInfo;
	}
	
	private static DragonRaidSystemInfo newInstance() {
		return new DragonRaidSystemInfo();
	}
	
	private int _bossnum;
	private String _bossname;
	private int _dragonid;
	private int _mapx;
	private int _mapy;
	private int _mapid;
	private String _spawn_day;
	private int[][] _bosstime;
	
	public int getBossNum(){
		return _bossnum;
	}
	
	public String getBossName(){
		return _bossname;
	}
	
	public int getDragonId(){
		return _dragonid;
	}
	
	public int getMapX(){
		return _mapx;
	}
	
	public int getMapY(){
		return _mapy;
	}

	public int getMapId(){
		return _mapid;
	}
	
	public String getSpawnDay(){
		return _spawn_day;
	}

	public int[][] getBossTime(){
		return _bosstime;
	}
	
	public boolean isSpawnTime(int h, int m, Calendar oCalendar){
		for(int[] t : _bosstime){
			if(t[0]==h && t[1]==m) {
				return true;
			}
		}
		return false;
	}
}