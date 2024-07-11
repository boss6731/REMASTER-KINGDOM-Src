package l1j.server.CPMWBQSystem.info;

import java.sql.ResultSet;

public class CPMWBQinfo {
	public static CPMWBQinfo newInstance(ResultSet rs) throws Exception{
		CPMWBQinfo BookInfo = newInstance();
		BookInfo.Map_id = rs.getInt("map_id");
		BookInfo.Map_desc = rs.getInt("map_desc");
		BookInfo.Mon_count = rs.getInt("mon_count");
		BookInfo.is_clear = rs.getInt("isclear") == 1 ? true : false;
		return BookInfo;
	}
	
	public static CPMWBQinfo newInstance(){
		return new CPMWBQinfo();
	}
	
	private int char_id;
	private int Map_id;
	private int Map_desc;
	private int Mon_count;
	private boolean is_clear;
	
	public int getCharid() {
		return char_id;
	}

	public void setCharid(int char_id) {
		this.char_id = char_id;
	}

	public int getMapid() {
		return Map_id;
	}

	public void setMapid(int map_id) {
		Map_id = map_id;
	}

	public int getMapdesc() {
		return Map_desc;
	}

	public void setMapdesc(int map_desc) {
		Map_desc = map_desc;
	}

	public int getMoncount() {
		return Mon_count;
	}

	public void setMoncount(int mon_count) {
		Mon_count = mon_count;
	}
	
	public boolean isIsclear() {
		return is_clear;
	}

	public void setIsclear(boolean is_clear) {
		this.is_clear = is_clear;
	}
	
}
