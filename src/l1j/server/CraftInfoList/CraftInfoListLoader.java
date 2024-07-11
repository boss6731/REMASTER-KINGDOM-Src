package l1j.server.CraftInfoList;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

public class CraftInfoListLoader {
	private static CraftInfoListLoader _instance;
	public static CraftInfoListLoader getInstance() {
		if(_instance == null)
			_instance = new CraftInfoListLoader();
		return _instance;
	}
	
	public static void reload() {
		if(_instance != null) {
			_instance = new CraftInfoListLoader();
		}
	}	
	
	private HashMap<Integer, CraftListAllInfo> _craft_all_list;
	private HashMap<Integer, ArrayList<CraftListInputItemsInfo>> _input_items;
	private HashMap<Integer, ArrayList<CraftListOutputInfo>> _output_info;
	private HashMap<Integer, ArrayList<CraftListOutputItemsInfo>> _output_items;
	private HashMap<Integer, ArrayList<CraftListOutputEventItemsInfo>> _event_output_items;
	
	private CraftInfoListLoader() {
		_craft_all_list = load_all_list();
		_input_items = load_input_items();
		_output_info = load_output_info();
		_output_items = load_output_items();
		_event_output_items = load_event_output_items();
	}
	
	private HashMap<Integer, CraftListAllInfo> load_all_list() {
		final HashMap<Integer, CraftListAllInfo> all_listMap = new HashMap<Integer, CraftListAllInfo>();
		Selector.exec("select * from craft_list_all", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					CraftListAllInfo Info = CraftListAllInfo.newInstance(rs);
					if(Info == null)
						continue;

					all_listMap.put(new Integer(Info.get_craft_id()), Info);
				}
			}
		});
		return _craft_all_list = all_listMap;
	}
	
	private HashMap<Integer, ArrayList<CraftListInputItemsInfo>> load_input_items() {
		final HashMap<Integer, ArrayList<CraftListInputItemsInfo>> inputlistMap = new HashMap<Integer, ArrayList<CraftListInputItemsInfo>>();
		Selector.exec("select * from craft_input_items", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					CraftListInputItemsInfo Info = CraftListInputItemsInfo.newInstance(rs);
					if(Info == null)
						continue;

					ArrayList<CraftListInputItemsInfo> inputList = inputlistMap.get(Info.get_craft_id());
					if (inputList == null) {
						inputList = new ArrayList<CraftListInputItemsInfo>();
						inputlistMap.put(new Integer(Info.get_craft_id()), inputList);
					}
					inputList.add(Info);
				}
			}
		});
		return _input_items = inputlistMap;
	}
	
	private HashMap<Integer, ArrayList<CraftListOutputInfo>> load_output_info() {
		final HashMap<Integer, ArrayList<CraftListOutputInfo>> outputlistMap = new HashMap<Integer, ArrayList<CraftListOutputInfo>>();
		Selector.exec("select * from craft_output_info", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					CraftListOutputInfo Info = CraftListOutputInfo.newInstance(rs);
					if(Info == null)
						continue;

					ArrayList<CraftListOutputInfo> outputList = outputlistMap.get(Info.get_craft_id());
					if (outputList == null) {
						outputList = new ArrayList<CraftListOutputInfo>();
						outputlistMap.put(new Integer(Info.get_craft_id()), outputList);
					}
					outputList.add(Info);
				}
			}
		});
		return _output_info = outputlistMap;
	}
	
	private HashMap<Integer, ArrayList<CraftListOutputItemsInfo>> load_output_items() {
		final HashMap<Integer, ArrayList<CraftListOutputItemsInfo>> output_items_Map = new HashMap<Integer, ArrayList<CraftListOutputItemsInfo>>();
		Selector.exec("select * from craft_output_items", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					CraftListOutputItemsInfo Info = CraftListOutputItemsInfo.newInstance(rs);
					if(Info == null)
						continue;

					ArrayList<CraftListOutputItemsInfo> outputitems = output_items_Map.get(Info.get_craft_id());
					if (outputitems == null) {
						outputitems = new ArrayList<CraftListOutputItemsInfo>();
						output_items_Map.put(new Integer(Info.get_craft_id()), outputitems);
					}
					outputitems.add(Info);
				}
			}
		});
		return _output_items = output_items_Map;
	}
	
	private HashMap<Integer, ArrayList<CraftListOutputEventItemsInfo>> load_event_output_items() {
		final HashMap<Integer, ArrayList<CraftListOutputEventItemsInfo>> event_output_items_Map = new HashMap<Integer, ArrayList<CraftListOutputEventItemsInfo>>();
		Selector.exec("select * from craft_output_event_items", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()) {
					CraftListOutputEventItemsInfo Info = CraftListOutputEventItemsInfo.newInstance(rs);
					if(Info == null)
						continue;

					ArrayList<CraftListOutputEventItemsInfo> event_outputitems = event_output_items_Map.get(Info.get_craft_id());
					if (event_outputitems == null) {
						event_outputitems = new ArrayList<CraftListOutputEventItemsInfo>();
						event_output_items_Map.put(new Integer(Info.get_craft_id()), event_outputitems);
					}
					event_outputitems.add(Info);
				}
			}
		});
		return _event_output_items = event_output_items_Map;
	}

	public HashMap<Integer, CraftListAllInfo> getAllCraftList() {
		if (_craft_all_list != null)
			return _craft_all_list;
		return null;
	}
	
	public ArrayList<CraftListOutputInfo> getOutputInfo(int craftId) {
		if (_output_info != null)
			return _output_info.get(craftId);
		return null;
	}
	
	public ArrayList<CraftListInputItemsInfo> getInputItems(int CraftId) {
		return _input_items.get(CraftId);
	}
	
	public ArrayList<CraftListOutputItemsInfo> getOutputItems(int CraftId) {
		return _output_items.get(CraftId);
	}
	
	public ArrayList<CraftListOutputEventItemsInfo> getEventOutputItems(int CraftId) {
		return _event_output_items.get(CraftId);
	}
}
