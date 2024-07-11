package l1j.server.Pc_Golden_Buff;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Info;
import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Loader;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.model.Instance.L1PcInstance;

public class Pc_Golden_Buff_Loader {
	private static Pc_Golden_Buff_Loader _instance;
	public static Pc_Golden_Buff_Loader getInstance() {
		if(_instance == null)
			_instance = new Pc_Golden_Buff_Loader();
		return _instance;
	}
	public static void reload() {
		if(_instance != null) {
			_instance = new Pc_Golden_Buff_Loader();
		}
	}
	
	private HashMap<Integer, Pc_Golden_Buff_Info> _golden_buff_info;
	private HashMap<L1PcInstance, Pc_Golden_Buff_Info> _golden_buff_user_list;
	
	private Pc_Golden_Buff_Loader() {
		load();
	}
	
	private void load() {
		final HashMap<Integer, Pc_Golden_Buff_Info> bonus = new HashMap<Integer, Pc_Golden_Buff_Info>();
		Selector.exec("select * from pc_golden_buff", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					Pc_Golden_Buff_Info pInfo = Pc_Golden_Buff_Info.newInstance(rs);
					if(pInfo == null)
						continue;
					bonus.put(pInfo.get_Buff_Id(), pInfo);
				}
			}
		});
		_golden_buff_info = bonus;
	}
	
	public Pc_Golden_Buff_Info getBuffOption(int index) {
		if (_golden_buff_info.containsKey(index)) {
			return _golden_buff_info.get(index);
		}
		return null;
	}
	
/*	public void addSpecialStat2(int charId, String charname, int index) {
		AinhasadSpecialStat2Info Info = getSpecialStat(index);
		Updator.exec("insert into character_special_stat2 set obj_id=?, char_name=?, group=?, index=?, endTime=?, type=?", new Handler(){
			long currentTime = System.currentTimeMillis();
			long endTime = currentTime + Info.get_hours() * 60 * 60 * 1000;
			//Timestamp EndTime = new Timestamp(endTime);
			
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				System.out.println(Info.get_group()+"+"+Info.get_index()+"+"+new Timestamp(endTime)+"+"+Info.get_type());
				int idx = 0;
				pstm.setInt(++idx, charId);
				pstm.setString(++idx, charname);
				pstm.setInt(++idx, Info.get_group());
				pstm.setInt(++idx, Info.get_index());
				pstm.setTimestamp(++idx, new Timestamp(endTime));
				pstm.setInt(++idx, Info.get_type());
			}
		});
		_special_stat2.put(charId, Info);
	}*/
	


	public void deletePcGoldenBuff(L1PcInstance pc, int index) {
		if (index == 0) {
			pc.getAccount().del_Index0_1();
			pc.getAccount().del_Index0_2();
			pc.getAccount().del_Index0_3();
			pc.getAccount().del_Index0_Remain_Time();
		}else if (index == 1) {
			pc.getAccount().del_Index1_1();
			pc.getAccount().del_Index1_2();
			pc.getAccount().del_Index1_3();
			pc.getAccount().del_Index1_Remain_Time();
		}
	}
	/*public void updateSpecialStat2(L1PcInstance pc) {
		AinhasadSpecialStat2Info Info = getSpecialStat(pc.getId());
		Updator.exec("update character_special_stat2 set obj_id=?, group=?, index=?, hour=? where obj_id=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, pc.getId());
				pstm.setInt(++idx, Info.get_group());
				pstm.setInt(++idx, Info.get_index());
				pstm.setInt(++idx, Info.get_hours());
				pstm.setInt(++idx, pc.getId());
			}
		});
	}*/
}
