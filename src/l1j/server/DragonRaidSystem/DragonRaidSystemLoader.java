package l1j.server.DragonRaidSystem;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

public class DragonRaidSystemLoader {

	private static DragonRaidSystemLoader _instance;
	public static DragonRaidSystemLoader getInstance() {
		if(_instance == null)
			_instance = new DragonRaidSystemLoader();
		return _instance;
	}
	public static void reload() {
		if(_instance != null) {
			_instance = new DragonRaidSystemLoader();
		}
	}
	
	static private List<DragonRaidSystemInfo> _Raid_system;
	private DragonRaidSystemLoader() {
		load();
	}
	
	private void load() {
		if (_Raid_system == null)
			_Raid_system = new ArrayList<DragonRaidSystemInfo>();
		_Raid_system.clear();
		ArrayList<DragonRaidSystemInfo> bonus = new ArrayList<DragonRaidSystemInfo>();
		Selector.exec("select * from dragon_raid_system", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					DragonRaidSystemInfo RaidInfo = DragonRaidSystemInfo.newInstance(rs);
					if(RaidInfo == null)
						continue;
					
					bonus.add(RaidInfo);
				}
			}
		});
		_Raid_system = bonus;
	}

	static public List<DragonRaidSystemInfo> getList() {
		synchronized (_Raid_system) {
			return new ArrayList<DragonRaidSystemInfo>(_Raid_system);
		}
	}
	
	static public int getSize() {
		return _Raid_system.size();
	}

	static public DragonRaidSystemInfo find(int bossnum) {
		synchronized (_Raid_system) {
			for (DragonRaidSystemInfo b : _Raid_system) {
				if (b.getBossNum() == bossnum)
					return b;
			}
			return null;
		}
	}

}