package l1j.server.CPMWBQSystem.info;

import java.sql.ResultSet;

public class CPMWBQNpcinfo {
	public static CPMWBQNpcinfo newInstance(ResultSet rs) throws Exception{
		CPMWBQNpcinfo BookInfo = newInstance();
		BookInfo.Class_id = rs.getInt("class_id");
		BookInfo.Map_desc = rs.getInt("map_desc");
		return BookInfo;
	}
	
	public static CPMWBQNpcinfo newInstance(){
		return new CPMWBQNpcinfo();
	}
	private int Class_id;
	private int Map_desc;
	
	public int get_class_id() {
		return Class_id;
	}

	public void set_class_id(int class_id) {
		this.Class_id = class_id;
	}
	
	public int getMapdesc() {
		return Map_desc;
	}

	public void setMapdesc(int map_desc) {
		Map_desc = map_desc;
	}
}
