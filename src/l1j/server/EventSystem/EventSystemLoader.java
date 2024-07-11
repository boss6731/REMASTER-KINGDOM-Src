package l1j.server.EventSystem;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Instance.L1PcInstance;

public class EventSystemLoader {
	private static EventSystemLoader _instance;

	public static EventSystemLoader getInstance() {
		if (_instance == null)
			_instance = new EventSystemLoader();
		return _instance;
	}

	public static void reload() {
		if (_instance != null) {
			_instance = new EventSystemLoader();
		}
	}

	private HashMap<Integer, EventSystemInfo> _event_system;

	private EventSystemLoader() {
		load();
	}

	private void load() {
		final HashMap<Integer, EventSystemInfo> bonus = new HashMap<Integer, EventSystemInfo>(256);
		Selector.exec("select * from event_system", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					EventSystemInfo pInfo = EventSystemInfo.newInstance(rs);
					if (pInfo == null)
						continue;

					bonus.put(pInfo.get_id(), pInfo);
				}
			}
		});
		_event_system = bonus;
	}

	public EventSystemInfo getEventSystemInfo(L1PcInstance pc) {
		for (int i = 1; i < _event_system.size() + 1; i++) {
			EventSystemInfo info = _event_system.get(i);
			if (info == null)
				continue;
			if (pc.getMapId() != info.get_event_map_id())
				continue;
			return info;
		}
		return null;
	}

	public EventSystemInfo getEventSystemInfo() {
		EventSystemInfo info = null;
		for (int i = 1; i < _event_system.size() + 1; i++) {
			info = _event_system.get(i);
			if (info == null)
				continue;
			if (!info.is_open())
				continue;
		}
		return info;
	}

	public EventSystemInfo getEventSystemInfo2(int npcid) {
		EventSystemInfo info = null;
		for (int i = 1; i < _event_system.size() + 1; i++) {
			info = _event_system.get(i);
			if (info == null) {
				continue;
			}

			if (info.get_npc_id() == npcid) {
				return info;
			}
		}
		return info;
	}

	public EventSystemInfo getEventSystemInfoNpc(int npcid) {
		for (int i = 1; i < _event_system.size() + 1; i++) {
			EventSystemInfo info = _event_system.get(i);
			if (info != null)
				if (info.get_npc_id() != npcid)
					continue;
			return info;
		}
		return null;
	}

	public void getEventSystemInfoCheck(L1PcInstance pc) {
		for (int i = 1; i < _event_system.size() + 1; i++) {
			EventSystemInfo info = _event_system.get(i);
			if (info == null)
				continue;
			if (info.get_event_name() == null)
				continue;
			pc.sendPackets("No." + info.get_id() + " 活動 : " + info.get_event_name());
		}
	}

	public EventSystemInfo getEventSystemInfo(int i) {
		EventSystemInfo info = _event_system.get(i);
		return info;
	}

	public int getEventSystemSize() {
		return _event_system.size();
	}
}
