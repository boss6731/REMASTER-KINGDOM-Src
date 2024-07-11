package l1j.server.AinhasadSpecialStat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.model.Instance.L1PcInstance;

public class AinhasadSpecialStatLoader {
	private static AinhasadSpecialStatLoader _instance;
	public static AinhasadSpecialStatLoader getInstance() {
		if(_instance == null)
			_instance = new AinhasadSpecialStatLoader();
		return _instance;
	}
	public static void reload() {
		if(_instance != null) {
			_instance = new AinhasadSpecialStatLoader();
		}
	}
	
	private HashMap<Integer, AinhasadSpecialStatInfo> _special_stat;
	private AinhasadSpecialStatLoader() {
		load();
	}
	
	private void load() {
		final HashMap<Integer, AinhasadSpecialStatInfo> bonus = new HashMap<Integer, AinhasadSpecialStatInfo>();
		Selector.exec("select * from character_special_stat", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					AinhasadSpecialStatInfo pInfo = AinhasadSpecialStatInfo.newInstance(rs);
					if(pInfo == null)
						continue;
					
					bonus.put(pInfo.get_obj_id(), pInfo);
				}
			}
		});
		_special_stat = bonus;
	}
	
	public AinhasadSpecialStatInfo getSpecialStat(int objid) {
		if (_special_stat.containsKey(objid)) {
			return _special_stat.get(objid);
		}
		return null;
	}
	
	
	public void addSpecialStat(int charId, String charname) {
		AinhasadSpecialStatInfo Info = new AinhasadSpecialStatInfo();
		Updator.exec("INSERT INTO character_special_stat SET obj_id=?, char_name=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, charId);
				pstm.setString(++idx, charname);
			}
		});
		_special_stat.put(charId, Info);
	}

	public void updateSpecialStat(L1PcInstance pc) {
		AinhasadSpecialStatInfo Info = getSpecialStat(pc.getId());
		if (Info != null) {
			Updator.exec("update character_special_stat set obj_id=?, bless=?, lucky=?, vital=?, invoke=?, invoke_val_1=?, invoke_val_2=?, restore=?, restore_val_1=?, restore_val_2=?, potion=?, potion_val_1=?, potion_val_2=?, current_stat=?, total_stat=?, point=?, cur_enchant_level=? where obj_id=?", new Handler(){
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					int idx = 0;
					pstm.setInt(++idx, pc.getId());
					pstm.setInt(++idx, Info.get_bless());
					pstm.setInt(++idx, Info.get_lucky());
					pstm.setInt(++idx, Info.get_vital());
					pstm.setInt(++idx, Info.get_invoke());
					pstm.setInt(++idx, Info.get_invoke_val_1());
					pstm.setInt(++idx, Info.get_invoke_val_2());
					pstm.setInt(++idx, Info.get_restore());
					pstm.setInt(++idx, Info.get_restore_val_1());
					pstm.setInt(++idx, Info.get_restore_val_2());
					pstm.setInt(++idx, Info.get_potion());
					pstm.setInt(++idx, Info.get_potion_val_1());
					pstm.setInt(++idx, Info.get_potion_val_2());
					pstm.setInt(++idx, Info.get_current_stat());
					pstm.setInt(++idx, Info.get_total_stat());
					pstm.setInt(++idx, Info.get_point());
					pstm.setInt(++idx, Info.get_cur_enchant_level());
					pstm.setInt(++idx, pc.getId());
				}
			});
		}
	}
}
