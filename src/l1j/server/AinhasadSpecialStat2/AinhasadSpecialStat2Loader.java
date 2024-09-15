package l1j.server.AinhasadSpecialStat2;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.model.Instance.L1PcInstance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class AinhasadSpecialStat2Loader {
	private static AinhasadSpecialStat2Loader _instance;
	public static AinhasadSpecialStat2Loader getInstance() {
		if(_instance == null)
			_instance = new AinhasadSpecialStat2Loader();
		return _instance;
	}
	public static void reload() {
		if(_instance != null) {
			_instance = new AinhasadSpecialStat2Loader();
		}
	}
	
	private HashMap<Integer, AinhasadSpecialStat2Info> _special_stat2;
	private HashMap<L1PcInstance, AinhasadSpecialStat2Info> _special_stat2_user_list;
	
	private AinhasadSpecialStat2Loader() {
		load();
	}
	
	private void load() {
		final HashMap<Integer, AinhasadSpecialStat2Info> bonus = new HashMap<Integer, AinhasadSpecialStat2Info>();
		Selector.exec("select * from einpoint_effect_faith", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					AinhasadSpecialStat2Info pInfo = AinhasadSpecialStat2Info.newInstance(rs);
					if(pInfo == null)
						continue;
					bonus.put(pInfo.get_index(), pInfo);
				}
			}
		});
		_special_stat2 = bonus;
	}
	
	public AinhasadSpecialStat2Info getSpecialStat(int index) {
		if (_special_stat2.containsKey(index)) {
			return _special_stat2.get(index);
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
	


	public void deleteSpecialStat2(int charId, String charname, int index) {
		Updator.exec("DELETE from character_special_stat2 WHERE obj_id=? and index_id=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, charId);
				pstm.setInt(++idx, index);
			}
		});
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
