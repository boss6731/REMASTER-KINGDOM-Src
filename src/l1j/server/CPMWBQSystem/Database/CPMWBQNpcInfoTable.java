package l1j.server.CPMWBQSystem.Database;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.CPMWBQSystem.info.CPMWBQNpcinfo;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

public class CPMWBQNpcInfoTable {
	
	private static HashMap<Integer, CPMWBQNpcinfo> CPMW_BQ_Npc_Info;
	public static void do_load(){
		HashMap<Integer, CPMWBQNpcinfo> CPMW_BQ_Npc_info = new HashMap<Integer, CPMWBQNpcinfo>();
		Selector.exec("select * from cpmw_bookquest_npcinfo", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					CPMWBQNpcinfo ninfo = CPMWBQNpcinfo.newInstance(rs);
					CPMW_BQ_Npc_info.put(ninfo.get_class_id(), ninfo);
				}
			}
		});
		CPMW_BQ_Npc_Info = CPMW_BQ_Npc_info;
	}
	
	public static CPMWBQNpcinfo Get_Info(int id) {
		return CPMW_BQ_Npc_Info.get(id);
	}
	
	public static int Get_Desc(int id) {
		return CPMW_BQ_Npc_Info.get(id).getMapdesc();
	}
}
