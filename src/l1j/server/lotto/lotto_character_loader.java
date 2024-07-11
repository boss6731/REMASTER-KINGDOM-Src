package l1j.server.lotto;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.L1QueryUtil;
import l1j.server.server.utils.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class lotto_character_loader {
	private static lotto_character_loader _instance;
	
	public static lotto_character_loader getInstance() {
		if (_instance == null) {
			_instance = new lotto_character_loader();
		}
		return _instance;
	}
	public static void reload() {
		if(_instance != null) {
			_instance = new lotto_character_loader();
		}
	}
	
	private lotto_character_loader() {
		load();
	}
	
	private void load() {
		
	}
	private HashMap<Integer, lotto_character_info> _lotto_characters;
	
	public void getlottocharacter(int round) {
		loadroundcharacter(round);
	}
	
	public void loadroundcharacter(int round) {
		final HashMap<Integer, lotto_character_info> roundcharacter = new HashMap<Integer, lotto_character_info>();
		Selector.exec("select * from characters_lotto_history", new FullSelectorHandler() { 
			public void result(ResultSet rs) throws Exception{
				while (rs.next()) {
					lotto_character_info lInfo = lotto_character_info.newInstance(rs);
					if (lInfo == null) {
						continue;
					}
					if (lInfo.get_round() != round) {
						continue;
					}
					roundcharacter.put(lInfo.get_char_id(), lInfo);
				}
			}
		});
		_lotto_characters = roundcharacter;
	}
	
	public lotto_character_info getlottonumber(int objid) {
		if(!_lotto_characters.isEmpty()){
			if(_lotto_characters.containsKey(objid)) {
				return _lotto_characters.get(objid);
			} else { 
				return null;
			}
		}
		return null;
	}
	
	public HashMap<Integer, lotto_character_info> getlottoinfo() {
		return _lotto_characters;
	}
	public void dellotto() {
		Connection con = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			L1QueryUtil.execute(con, "DELETE FROM characters_lotto_history", new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
		}
	}
	
	public void updateLotto(L1PcInstance pc, int round, int number1, int number2) {
		Updator.exec("insert into characters_lotto_history set char_obj_id=?, char_name=?, round=?, number1=?, number2=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception{
				int idx = 0;
				pstm.setInt(++idx, pc.getId());
				pstm.setString(++idx, pc.getName());
				pstm.setInt(++idx, round);
				pstm.setInt(++idx, number1);
				pstm.setInt(++idx, number2);
		
			}
		});
	}
	
}
