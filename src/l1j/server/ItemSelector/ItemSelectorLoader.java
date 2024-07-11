package l1j.server.ItemSelector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ItemSelector;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

@SuppressWarnings("serial")
public class ItemSelectorLoader extends L1ItemInstance {
	// public static enum SelectorType{
	// NORMAL, SPELL
	// }
	private static ItemSelectorLoader _instance;

	public static ItemSelectorLoader getInstance() {
		if (_instance == null)
			_instance = new ItemSelectorLoader();
		return _instance;
	}

	public static void reload() {
		ItemSelectorLoader tmp = _instance;
		_instance = new ItemSelectorLoader();
		if (tmp != null) {
			tmp.clear();
			tmp = null;
		}
	}

	private HashMap<Integer, ArrayList<ItemSelectorModel>> _models;

	private ItemSelectorLoader() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ArrayList<ItemSelectorModel> list = null;
		_models = new HashMap<Integer, ArrayList<ItemSelectorModel>>(64);
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from item_selector");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int itemid = rs.getInt("itemId");
				int selectItemId = rs.getInt("selectItemId");
				int enchatLevel = rs.getInt("enchantLevel");

				list = _models.get(itemid);
				if (list == null) {
					list = new ArrayList<ItemSelectorModel>(16);
					_models.put(itemid, list);
					_models.put(selectItemId, list);
					_models.put(enchatLevel, list);
				}
				list.add(new ItemSelectorModel(itemid, selectItemId, enchatLevel));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public void processItemSelector(L1PcInstance pc, L1ItemInstance useItem) {
		try {
			if (!pc.getInventory().checkItem(useItem.getItemId(), 1)) {
				// System.out.println("選擇物品 ERR : !pc.getInventory().checkItem(useItemId, 1) /
				// 使用者: " + pc.getName());
				return;
			}

			pc.sendPackets(new S_ItemSelector(useItem), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<ItemSelectorModel> get(int i) {
		return _models.get(i);
	}

	public void clear() {
		if (_models != null) {
			for (ArrayList<ItemSelectorModel> list : _models.values())
				list.clear();
			_models.clear();
			_models = null;
		}
	}
}
