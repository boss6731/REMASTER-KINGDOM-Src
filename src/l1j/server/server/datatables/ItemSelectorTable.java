package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


import l1j.server.L1DatabaseFactory;
import l1j.server.ItemSelector.ItemSelectorModel;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.templates.L1NpcShop;
import l1j.server.server.utils.SQLUtil;

public class ItemSelectorTable {
	private static Logger _log = Logger.getLogger(ItemSelectorTable.class.getName());
	
	
	public class SelectorData {
		public int _itemId, _index, _selectItemId, _enchantLevel, _attrLevel, _count, _endTime;

		public L1ItemInstance _item;
		public boolean _isDelete;
		public SelectorData(ResultSet rs) throws SQLException {
			_itemId			= rs.getInt("itemId");
			_index			= rs.getInt("index");
			_selectItemId	= rs.getInt("selectItemId");
			_enchantLevel	= rs.getInt("enchantLevel");
			_attrLevel		= rs.getInt("attrLevel");
			_count 			= rs.getInt("count");
//			_attrType		= rs.getInt("attrType");
			_endTime 		= rs.getInt("endTime");
			_item			= l1j.server.server.database.ItemTable.getInstance().createItem(_selectItemId);
			if(_item != null){
				_item.setIdentified(true);
				_item.setEnchantLevel(_enchantLevel);
				_item.setAttrEnchantLevel(_attrLevel);
			}
		}
	}
	
	
	
	/*public int getItemId() {
		return _itemId;
	}
	public int getSelectItemId() {
		return _selectItemId;
	}
	public int getEnchantLevel() {
		return _enchantLevel;
	}
	public int getAttrLevel() {
		return _attrLevel;
	}
	public int getCount() {
		return _count;
	}
	*/
	
	
	public void reload() {
		for(SpellMeltTable.MeltData data : _dataMap.values())data.clear();
		_dataMap.clear();
		load();
	}
	
	private static final FastMap<Integer, FastTable<SelectorData>> _dataMap					= new FastMap<Integer, FastTable<SelectorData>>();
	
	private static ItemSelectorTable _instance;
	public static ItemSelectorTable getInstance() {
		if(_instance == null)_instance = new ItemSelectorTable();
		return _instance;
	}
	
	public static boolean isSelectorInfo(int itemId){
		return _dataMap.containsKey(itemId);
	}
	
	public static FastTable<SelectorData> getSelectorInfo(int itemId){
//		System.out.println(_dataMap.getEntry(itemId));
		return _dataMap.containsKey(itemId) ? _dataMap.get(itemId) : null;
	}
		

	private ItemSelectorTable() {
		load();
	}

	private void load() {
		Connection con					= null;
		PreparedStatement pstm			= null;
		ResultSet rs					= null;
		SelectorData data				= null;
		try {
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("SELECT * FROM item_selector");
			rs		= pstm.executeQuery();
			while (rs.next()) {
				data	= new SelectorData(rs);
				FastTable<SelectorData> list = _dataMap.get(data._itemId);
				if(list == null){
					list = new FastTable<SelectorData>();
					_dataMap.put(data._itemId, list);
				}
				list.add(data);
			}
		}
		catch (Exception e) {
			_log.log(Level.SEVERE, "ItemSelectorTable[]Error", e);
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	

}
