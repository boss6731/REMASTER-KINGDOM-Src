package l1j.server.CPMWBQSystem.Database;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import l1j.server.CPMWBQSystem.info.CPMWBQmapInfo;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

public class CPMWBQmapInfoTable {
	
	private static HashMap<String, CPMWBQmapInfo> CPMW_BQ_Map_Info;
	public static void do_load(){
		HashMap<String, CPMWBQmapInfo> CPMW_BQ_Map_info = new HashMap<String, CPMWBQmapInfo>();
		Selector.exec("select * from cpmw_bookquest_mapinfo", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					CPMWBQmapInfo minfo = CPMWBQmapInfo.newInstance(rs);
					CPMW_BQ_Map_info.put(minfo.maname(), minfo);
				}
			}
		});
		CPMW_BQ_Map_Info = CPMW_BQ_Map_info;
	}
	
	public static HashMap<String, CPMWBQmapInfo> Get_Map() {
		return CPMW_BQ_Map_Info;
	}
	
	public static CPMWBQmapInfo Get_Map_Info(String key) {
		return CPMW_BQ_Map_Info.get(key);
	}
	
	public static String getMapInfo(String desc) {
		try {
			Set<String> keys = Get_Map().keySet();
			String mapname = null;
			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				mapname = iterator.next();
				if(Get_Map().get(mapname).descName().equalsIgnoreCase(desc)) {
					return mapname;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getMapInfo(int mapid) {
		try {
			Set<String> keys = Get_Map().keySet();
			String mapname = null;
			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				mapname = iterator.next();
				if(Get_Map().get(mapname).mapId() == mapid) {
					return mapname;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
