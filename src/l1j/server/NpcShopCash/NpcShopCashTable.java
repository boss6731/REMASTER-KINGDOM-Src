package l1j.server.NpcShopCash;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class NpcShopCashTable {
	private static NpcShopCashTable _instance;
	private Map<Integer, L1CashType> _list = new HashMap<Integer, L1CashType>();

	public static NpcShopCashTable getInstance() {
		if (_instance == null) {
			_instance = new NpcShopCashTable();
		}
		return _instance;
	}

	public NpcShopCashTable() {
		loadCashType(_list);
	}

	public void reload() {
		Map<Integer, L1CashType> list = new HashMap<Integer, L1CashType>();
		loadCashType(list);
		_list = list;
	}

	public void loadCashType(Map<Integer, L1CashType> list) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1CashType at = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM npc_shop_cash");
			rs = pstm.executeQuery();
			while (rs.next()) {
				at = new L1CashType();
				int npcid = rs.getInt("npc_id");
				at.setCashType(rs.getInt("cash_item_id"));
				at.setCashName(rs.getString("cash_name"));
				at.setCashDesc(rs.getInt("cash_desc_id"));
				String[] random_enchant = rs.getString("random_enchant") != null ? rs.getString("random_enchant").split(",") : null; 
				String[] random_enchant_chance = rs.getString("random_enchant_chance") != null ? rs.getString("random_enchant_chance").split(",") : null;
				at.set_random_enchant_use(rs.getString("is_random_enchant").equalsIgnoreCase("true"));
				at.set_random_enchant(random_enchant);
				at.set_random_enchant_chance(random_enchant_chance);
				at.set_max_use_count(rs.getInt("cash_use_max_count"));
				at.setShopType(rs.getString("shop_type"));
				// âÍé©ò¦ïÒ×¾úş£¬ÖÇåı£¬ãÀâËÑ¦İ¾ØªßÂïÁü½ãÀÜÅ÷×ßÂïÁ...
				// åıÍıãÀâËÑ¦İ¾ØªßÂïÁ£¬âÍé©ôÕÊ¥İ¾Øªûú?áãÕ±êÈÕÎŞÅéÄ...
				// åıÍıãÀÜÅ÷×ßÂïÁ£¬öÎâËÑ¦İ¾Øªâ¦?ûú?áãàâöÇ? 0, 0
				// ãÀøĞ?ßÂù¡????£¨ÜôãÀò¦?ïÒîÜ£¬ì»ãÀúŞÑÑßÂù¡??úŞÑÑßÂù¡ÜôÊ¦Îßüµ¡£)
				// õÌÓŞŞÅéÄÕá... åıÍıŞÅéÄõ±Î¦ìéïÒâ¦Õá£¬üåõóúŞó¹è¦£¬ì×ó®âÍé©Íªß©õÌÓŞ?ì¤ù­Øóó¹è¦...
				list.put(npcid, at);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public L1CashType getNpcCashType(int npcid) {
		return (L1CashType) _list.get(npcid);
	}

	public class L1CashType {
		private int _type;
		private String _typename;
		private int _desc;
		private boolean _random_enchant_use;
		private String[] _random_enchant;
		private String[] _random_enchant_chance;
		private int _max_use_count;
		private String _shop_type;
		
		public L1CashType() {
		}

		public int getCashType() {
			return _type;
		}

		public void setCashType(int i) {
			_type = i;
		}

		public String getCashName() {
			return _typename;
		}

		public void setCashName(String name) {
			_typename = name;
		}

		public int getCashDesc() {
			return _desc;
		}

		public void setCashDesc(int i) {
			_desc = i;
		}

		public String[] get_random_enchant() {
			return _random_enchant;
		}

		public void set_random_enchant(String[] _random_enchant) {
			this._random_enchant = _random_enchant;
		}

		public String[] get_random_enchant_chance() {
			return _random_enchant_chance;
		}

		public void set_random_enchant_chance(String[] _random_enchant_chance) {
			this._random_enchant_chance = _random_enchant_chance;
		}

		public boolean is_random_enchant_use() {
			return _random_enchant_use;
		}

		public void set_random_enchant_use(boolean _random_enchant_use) {
			this._random_enchant_use = _random_enchant_use;
		}

		public int get_max_use_count() {
			return _max_use_count;
		}

		public void set_max_use_count(int _max_use_count) {
			this._max_use_count = _max_use_count;
		}
		
		public String isShopType() {
			return _shop_type;
		}
		
		public void setShopType(String flg) {
			_shop_type = flg;
		}
	}
}