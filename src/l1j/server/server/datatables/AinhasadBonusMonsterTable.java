package l1j.server.server.server.datatables;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

public class AinhasadBonusMonsterTable {

	private static AinhasadBonusMonsterTable _instance;

	public static AinhasadBonusMonsterTable getInstance() {
		if (_instance == null) {
			_instance = new AinhasadBonusMonsterTable();
		}
		return _instance;
	}

	private HashMap<Integer, Integer> _ainhasadBonusList = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> _alarmBonusList = new HashMap<Integer, Integer>();

	public void reLoad() {
		AinhasadBonusMonsterTable old = _instance;
		_instance = new AinhasadBonusMonsterTable();
		old._ainhasadBonusList.clear();
		old._alarmBonusList.clear();
		old = null;
	}

	private AinhasadBonusMonsterTable() {
		Selector.exec("SELECT * FROM einhasad_monster", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					_ainhasadBonusList.put(rs.getInt("monster_id"), rs.getInt("einhasad"));
					_alarmBonusList.put(rs.getInt("alarm_monster_id"), rs.getInt("einhasad"));
				}
			}
		});
	}

	public Integer getAinhasadBonus(int npcId) {
		return _ainhasadBonusList.get(npcId);
	}
	
	public Integer getAlarmAinhasadBonus(int npcId) {
		return _alarmBonusList.get(npcId);
	}
	
}
