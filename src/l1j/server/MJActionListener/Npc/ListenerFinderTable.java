package l1j.server.MJActionListener.Npc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
// XXX 增加傳送返回區域
public class ListenerFinderTable {
	private static ListenerFinderTable _instance;
	public static ListenerFinderTable getInstance() {
		if(_instance == null)
			_instance = new ListenerFinderTable();
		return _instance;
	}
	public static void reload() {
		if(_instance != null) {
			_instance = new ListenerFinderTable();
		}
	}
	
	private HashMap<Integer, ListenerInfo> _Listener;
	private ListenerFinderTable() {
		load();
	}
	
	private void load() {
		final HashMap<Integer, ListenerInfo> listener = new HashMap<Integer, ListenerInfo>();
		Selector.exec("select * from tb_act_listener_npc_teleporter", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					ListenerInfo pInfo = ListenerInfo.newInstance(rs);
					if(pInfo == null)
						continue;		
					
					String[] map_list = (String[])MJArrangeParser.parsing(pInfo.get_map_id(), ",", MJArrangeParseeFactory.createStringArrange()).result();		
					for (String map : map_list) {
						int mapId = Integer.parseInt(map);
						listener.put(mapId, pInfo);
					}
				}
			}
		});
		_Listener = listener;
	}
	
	public ListenerInfo getListenerInfo(int MapId) {
		if (_Listener.containsKey(MapId)) {
			return _Listener.get(MapId);
		}
		return null;
	}
	
	public static class ListenerInfo {
		public static ListenerInfo newInstance(ResultSet rs) throws SQLException {
			ListenerInfo pInfo = newInstance();
			pInfo._npc_id = rs.getInt("npc_id");
			pInfo._map_id = rs.getString("teleport_map_id");
			pInfo._action_id = rs.getString("action_name");
			return pInfo;
		}
		
		private static ListenerInfo newInstance() {
			return new ListenerInfo();
		}
		
		private int _npc_id;
		private String _map_id;
		private String _action_id;
		
		public int get_npc_id() {
			return _npc_id;
		}
		
		public String get_map_id() {
			return _map_id;
		}
		
		public String get_action_id() {
			return _action_id;
		}
	}
}
