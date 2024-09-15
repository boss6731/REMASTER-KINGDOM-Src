package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.templates.ShopBuyLimit;
import l1j.server.server.utils.SQLUtil;

public class ShopTable {

	private static ShopTable _instance;

	private final Map<Integer, L1Shop> _allShops = new ConcurrentHashMap<Integer, L1Shop>();
	private HashMap<Integer, Integer> _allEquipmentChangeShops = new HashMap<Integer, Integer>();

	public static ShopTable getInstance() {
		if (_instance == null) {
			_instance = new ShopTable();
		}
		return _instance;
	}

	private ShopTable() {
		loadShops();
		loadEquipmentChangeShop();
	}

	public static void reload() {
		ShopTable oldInstance = _instance;
		_instance = new ShopTable();
		oldInstance._allShops.clear();
		oldInstance._allEquipmentChangeShops.clear();
	}

	private ArrayList<Integer> enumNpcIds() {
		ArrayList<Integer> ids = new ArrayList<Integer>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT DISTINCT npc_id FROM shop");
			rs = pstm.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt("npc_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return ids;
	}

	private HashMap<Integer, L1ShopItem> _sellings;
	private HashMap<Integer, ArrayList<L1ShopItem>> _purchasings;

	private L1Shop loadShop(int npcId, ResultSet rs) throws SQLException {
		List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
		List<L1ShopItem> purchasingList = new ArrayList<L1ShopItem>();

		L1ShopItem item = null;
		while (rs.next()) {
			int itemId = rs.getInt("item_id");
			int sellingPrice = rs.getInt("selling_price");
			int purchasingPrice = rs.getInt("purchasing_price");
			int packCount = rs.getInt("pack_count");
			int enchant = rs.getInt("enchant");
			int attrenchant = parseType(rs.getString("attr_enchant"));
			boolean timeLimit = Boolean.valueOf(rs.getString("time_limit"));
			int endtime = rs.getInt("end_time");
			boolean carving = Boolean.valueOf(rs.getString("carving"));
			int bless = BlessParseType(rs.getString("bless"));
			int limitlevel = rs.getInt("buy_level_limit");
			int classType = rs.getInt("classType");
			int clan_shop_type = Clan_Shop_ParseType(rs.getString("clan_shop_type"));
			packCount = packCount == 0 ? 1 : packCount;
			if (0 <= sellingPrice) {
				item = new L1ShopItem(itemId, sellingPrice, packCount, enchant, timeLimit, endtime, carving, bless, attrenchant, limitlevel, classType, clan_shop_type);
				if (Config.ServerAdSetting.IsValidShopSystem) {
					if (sellingPrice > 0) {
						item.setNpcId(npcId);
						if (_sellings != null) {
							L1ShopItem i = _sellings.get(itemId);
							if (i == null)
								_sellings.put(itemId, item);
							else if (sellingPrice < i.getPrice())
								_sellings.put(itemId, item);
						}
					}
				}

				ShopBuyLimit sbl = ShopBuyLimitInfo.getInstance().getShopBuyLimit(itemId);
				if (sbl != null) {
					if (packCount > 1) {
						System.out.println("◆◆◆◆[警告]◆◆◆◆ : " + npcId + " : 商店中限制購買的物品已輸入 packcount。");
					}
				}

				sellingList.add(item);
			}
			if (0 <= purchasingPrice) {
				item = new L1ShopItem(itemId, purchasingPrice, packCount, enchant, timeLimit, endtime, carving, bless, attrenchant, limitlevel, classType, clan_shop_type);
				if (Config.ServerAdSetting.IsValidShopSystem) {
					if (purchasingPrice > 0) {
						item.setNpcId(npcId);
						if (_purchasings != null) {
							ArrayList<L1ShopItem> list = _purchasings.get(itemId);
							if (list == null) {
								list = new ArrayList<L1ShopItem>(8);
								_purchasings.put(itemId, list);
							}
							list.add(item);
						}
					}
				}
				purchasingList.add(item);
			}
		}
		return new L1Shop(npcId, sellingList, purchasingList);
	}

	public void Reload(int npcid) { // 重新加載特定的 NPC
		ArrayList<Integer> list = enumNpcIds();
		for (Integer i : list) {
			if (i != npcid)
				continue;

			Selector.exec("select * from shop where npc_id=? order by order_id", new SelectorHandler() {

				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setInt(1, i);
				}

				@Override
				public void result(ResultSet rs) throws Exception {
					_allShops.put(i, loadShop(i, rs));
				}
			});
		}
	}

	private void loadShops() {
		if (Config.ServerAdSetting.IsValidShopSystem) {
			_sellings = new HashMap<Integer, L1ShopItem>(1024);
			_purchasings = new HashMap<Integer, ArrayList<L1ShopItem>>(1024);
		}

		ArrayList<Integer> list = enumNpcIds();
		for (Integer i : list) {
			Selector.exec("select * from shop where npc_id=? order by order_id", new SelectorHandler() {
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setInt(1, i);
				}

				@Override
				public void result(ResultSet rs) throws Exception {
					_allShops.put(i, loadShop(i, rs));
				}
			});
		}

		if (Config.ServerAdSetting.IsValidShopSystem) {
			for (Integer itemId : _purchasings.keySet()) {
				L1ShopItem sell = _sellings.get(itemId);
				ArrayList<L1ShopItem> purs = _purchasings.get(itemId);
				if (sell == null || purs == null || purs.size() <= 0)
					continue;

				int sellp = sell.getPrice();
				for (L1ShopItem buy : purs) {
					if (sellp < buy.getPrice())
						System.out.println(String.format("[發現商店項目中買入價格高於賣出價格，NPC: %d，物品: %d，賣出: %d，買入: %d]", buy.getNpcId(), itemId, sellp, buy.getPrice()));
				}
				purs.clear();
			}
			_sellings.clear();
			_sellings = null;
			_purchasings.clear();
			_purchasings = null;
		}
	}

	private void loadEquipmentChangeShop() {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection();
				PreparedStatement pstm = con.prepareStatement("SELECT * FROM equipment_change_shop");
				ResultSet rs = pstm.executeQuery()) {
			while (rs.next()) {
				int itemId = rs.getInt("item_id");
				int itemType = rs.getInt("item_type");
				_allEquipmentChangeShops.put(itemId, itemType);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getEquipmentChangeItemClass(int itemId) {
		if (_allEquipmentChangeShops.get(itemId) != null) {
			return _allEquipmentChangeShops.get(itemId);
		}
		return -1;
	}

	public void set(int npcId, L1Shop shop) {
		_allShops.put(npcId, shop);
	}

	public HashMap<Integer, Integer> getEquipmentChangeShop() {
		return _allEquipmentChangeShops;
	};

	public L1Shop get(int npcId) {
		return _allShops.get(npcId);
	}

	/* 與屏障相關 */
	public void addShop(int npcId, L1Shop shop) {
		_allShops.put(npcId, shop);
	}

	/**
	 * 根據輸入的類型字符串返回對應的數值。
	 *
	 * @param type 表示類型的字符串，例如 "火靈:1級"。
	 * @return 對應的數值：對應的火靈、水靈、風靈和地靈級別數字。如果類型不匹配，返回0。
	 */
	private int parseType(String type) {
		if (type.equals("火靈:1級"))
			return 1;
		else if (type.equals("火靈:2級"))
			return 2;
		else if (type.equals("火靈:3級"))
			return 3;
		else if (type.equals("火靈:4級"))
			return 4;
		else if (type.equals("火靈:5級"))
			return 5;
		else if (type.equals("水靈:1級"))
			return 6;
		else if (type.equals("水靈:2級"))
			return 7;
		else if (type.equals("水靈:3級"))
			return 8;
		else if (type.equals("水靈:4級"))
			return 9;
		else if (type.equals("水靈:5級"))
			return 10;
		else if (type.equals("風靈:1級"))
			return 11;
		else if (type.equals("風靈:2級"))
			return 12;
		else if (type.equals("風靈:3級"))
			return 13;
		else if (type.equals("風靈:4級"))
			return 14;
		else if (type.equals("風靈:5級"))
			return 15;
		else if (type.equals("地靈:1級"))
			return 16;
		else if (type.equals("地靈:2級"))
			return 17;
		else if (type.equals("地靈:3級"))
			return 18;
		else if (type.equals("地靈:4級"))
			return 19;
		else if (type.equals("地靈:5級"))
			return 20;
		return 0;
	}

	/**
	 * 根據輸入的類型字符串返回對應的數值。
	 *
	 * @param type 表示類型的字符串，例如 "祝福"。
	 * @return 對應的數值：祝福為0，正常為1，詛咒為2。如果類型不匹配，返回1。
	 */
	private int BlessParseType(String type) {
		if (type.equalsIgnoreCase("祝福"))
			return 0;
		else if (type.equalsIgnoreCase("正常"))
			return 1;
		else if (type.equalsIgnoreCase("詛咒"))
			return 2;
		return 1;
	}

	/**
	 * 根據輸入的類型字符串返回對應的數值。
	 *
	 * @param type 表示類型的字符串，例如 "盟主"。
	 * @return 對應的數值：盟主為10，盟主/副盟主/守護為9，盟主/副盟主/守護/精英/普通為8。如果類型為null或不匹配，返回0。
	 */
	private int Clan_Shop_ParseType(String type) {
		if (type != null) {
			if (type.equalsIgnoreCase("盟主"))
				return 10;
			else if (type.equalsIgnoreCase("盟主/副盟主/守護"))
				return 9;
			else if (type.equalsIgnoreCase("盟主/副盟主/守護/精英/普通"))
				return 8;
		}
		return 0;
	}

	/* 與屏障相關 */
	public void delShop(int npcId) {
		_allShops.remove(npcId);
	}
}
