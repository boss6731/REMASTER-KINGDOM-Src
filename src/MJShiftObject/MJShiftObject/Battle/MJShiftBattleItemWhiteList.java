package MJShiftObject.Battle;

import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class MJShiftBattleItemWhiteList {
	public static final String DBNAME_THEBES = "server_battle_white_list_thebes";
	public static final String DBNAME_DOMTOWER = "server_battle_white_list_domtower";
	public static final String DBNAME_FISLAND = "server_battle_white_list_forisland";
	private String m_db_name;
	private HashMap<Integer, Byte> m_white_list;

	public MJShiftBattleItemWhiteList(String db_name) {
		this.m_db_name = db_name;
		this.m_white_list = do_load();
	}

	public void do_reload() {
		this.m_white_list = do_load();
	}

	private HashMap<Integer, Byte> do_load() {
		final HashMap<Integer, Byte> white_list = new HashMap<>();
		Selector.exec(String.format("從 %s 選擇 itemId", new Object[]{this.m_db_name}), (SelectorHandler) new FullSelectorHandler() {
			public void result(ResultSet rs) throws Exception {
				while (rs.next())
					white_list.put(Integer.valueOf(rs.getInt("itemId")), Byte.valueOf(-128));
			}
		});
		return white_list;
	}

	public boolean use(L1PcInstance pc, L1ItemInstance item) {
		if (pc.isGm()) {
			return true;
		}
		if (item == null) {
			return true;
		}
		if (!pc.is_shift_client()) {
			return true;
		}
		L1Item template = item.getItem();
		if (template instanceof l1j.server.server.templates.L1EtcItem) {
			return this.m_white_list.containsKey(Integer.valueOf(template.getItemId()));
		}
		return true;
	}
}


