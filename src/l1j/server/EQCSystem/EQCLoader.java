package l1j.server.EQCSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class EQCLoader {
	private static EQCLoader _instance;

	public static EQCLoader getInstance() {
		if (_instance == null)
			_instance = new EQCLoader();
		return _instance;
	}

	public static void reload() {
		EQCLoader tmp = _instance;
		_instance = new EQCLoader();
		if (tmp != null) {
			tmp.clear();
			tmp = null;
		}
	}

	public static void release() {
		if (_instance != null) {
			_instance.clear();
			_instance = null;
		}
	}

	private HashMap<Integer, ArrayList<EQCModel>> _models;

	private EQCLoader() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ArrayList<EQCModel> list = null;
		_models = new HashMap<Integer, ArrayList<EQCModel>>(64);
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from equipment_change_shop");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int type = rs.getInt("item_type");
				int itemid = rs.getInt("item_id");

				list = _models.get(type);
				if (list == null) {
					list = new ArrayList<EQCModel>(16);
					_models.put(type, list);
				}
				list.add(new EQCModel(itemid, type));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public void processEQC(L1PcInstance pc, int useItemId, L1ItemInstance oItem, L1ItemInstance tItem) {
		try {
			if (!pc.getInventory().checkItem(useItemId, 1) || oItem == null || tItem == null) {
				// System.out.println("設備交換訂單錯誤： !pc.getInventory().checkItem(useItemId, 1) ||
				// useItem == null || targetItem == null / 使用者: " + pc.getName());
				return;
			}
			L1Item targetTemp = tItem.getItem();
			if (pc.getInventory().checkEquipped(targetTemp.getItemId())) {
				pc.sendPackets(new S_ServerMessage(4302)); // 它不能用於正在佩戴的物品。
				return;
			}

			int targetItemId = targetTemp.getItemId();
			int targetItemType = targetTemp.getType2();

			// 1:helm, 2:armor, 3:T, 4:cloak, 5:glove, 6:boots, 7:shield, 8:amulet, 9:ring,
			// 10:belt, 11:ring2,
			// 12:earring, 13:garder, 25:ron, 15:綁腿, 31:肩鎧 should, 32:徽章, 33:吊墜 Pendant
			int subType = targetTemp.getType();
			if (targetItemType == 2 && (subType >= 8 && subType <= 12))
				targetItemType = 3;

			if (useItemId == 847 && subType != 10) { // 四大法學著作
				pc.sendPackets(new S_SystemMessage("不能使用於該物品。.")); // 常規項目
				return;
			}
			if (useItemId == 848 && subType != 12) { //魔法娃娃
				pc.sendPackets(new S_SystemMessage("不能使用於該物品。.")); // 常規項目
				return;
			}
			if (useItemId == 846 && targetItemType != 1) { //武器
				pc.sendPackets(new S_SystemMessage("不能使用於該物品。."));
				return;
			}
			if (useItemId == 844 && (targetItemType != 2 || targetItemType == 2 && subType == 25)) { // 裝置
				pc.sendPackets(new S_SystemMessage("不能使用於該物品。."));
				return;
			}

			if (useItemId == 845 && targetItemType != 3) { //斧頭
				pc.sendPackets(new S_SystemMessage("不能使用於該物品。."));
				return;
			}

			if (useItemId == 854 && (targetItemType != 2 || targetItemType == 2 && subType != 25)) { //長生不老藥符文
				pc.sendPackets(new S_SystemMessage("不能使用於該物品。.")); //長生不老藥符文
				return;
			}

			int itemClass = ShopTable.getInstance().getEquipmentChangeItemClass(targetItemId);
			if (itemClass < 0) {
				pc.sendPackets(new S_SystemMessage("不能使用於該物品。."));
				return;
			}
			pc.setEquipmentChangeItem(tItem);
			pc.setEquipmentChangeUseItemId(oItem.getItem().getItemId());
			pc.sendPackets(S_EQCPacket.get(pc, get(itemClass), tItem.getEnchantLevel(), tItem.getAttrEnchantLevel(),
					tItem.getBless()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<EQCModel> get(int i) {
		return _models.get(i);
	}

	public void clear() {
		if (_models != null) {
			for (ArrayList<EQCModel> list : _models.values())
				list.clear();
			_models.clear();
			_models = null;
		}
	}
}
