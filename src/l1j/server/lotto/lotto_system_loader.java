package l1j.server.lotto;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class lotto_system_loader {
	private static lotto_system_loader _instance;
	
	public static lotto_system_loader getInstance() {
		if (_instance == null) {
			_instance = new lotto_system_loader();
		}
		return _instance;
	}
	public static void reload() {
		if(_instance != null) {
			_instance = new lotto_system_loader();
		}
	}
	
	private lotto_system_loader() {
		load();
	}
	private HashMap<Integer, lotto_system_info> _lotto;
	
	private void load() {
		
	}

	public void loadlotto() {
		final HashMap<Integer, lotto_system_info> round = new HashMap<Integer, lotto_system_info>();
		Selector.exec("select * from lotto_history", new FullSelectorHandler() { 
			public void result(ResultSet rs) throws Exception{
				while (rs.next()) {
					lotto_system_info lInfo = lotto_system_info.newInstance(rs);
					if (lInfo == null) {
						continue;
					}
					round.put(lInfo.get_round(), lInfo);
				}
			}
		});
		_lotto = round;
	}
	public lotto_system_info getlotto(int round) {
		if (_lotto.containsKey(round)) {
			return _lotto.get(round);
		}
		return null;
	}
	
	public HashMap<Integer, lotto_system_info> getlottoall(){
		return _lotto;
	}
	
	
	public void addLottoPc(int round, int pccount) {
		Updator.exec("update lotto_history set pc_count=? where round=?", new Handler() {
			@Override
			public void handle (PreparedStatement pstm) throws Exception{
				int idx = 0;
				pstm.setInt(++idx, pccount);
				pstm.setInt(++idx, round);
			}
		});
	}
	
	public void updateLotto(int round, int a, int b) {
		Updator.exec("update lotto_history set number1=?, number2=? where round=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception{
				int idx = 0;
				pstm.setInt(++idx, a);
				pstm.setInt(++idx, b);
				pstm.setInt(++idx, round);
			}
		});
	}
	
	public void updateLotto(int round, boolean b) {
		Updator.exec("update lotto_history set prize=? where round=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception{
				int idx = 0;
				pstm.setBoolean(++idx, b);
				pstm.setInt(++idx, round);
			}
		});
	}
	public void addLotto(int round) {
		Updator.exec("INSERT INTO lotto_history SET round=?", new Handler(){
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, round);
			}
		});
	}

		
}
