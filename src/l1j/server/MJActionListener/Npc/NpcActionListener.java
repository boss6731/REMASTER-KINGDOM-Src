package l1j.server.MJActionListener.Npc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import l1j.server.MJActionListener.ActionListener;
import l1j.server.MJTemplate.MJString;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

public class NpcActionListener implements ActionListener {
	public enum RESULT_INDEX {
		SUCCESS(0),
		UN_OPENED(1),
		SHORT_LEVEL(2),
		NO_BUFF(3),
		NO_PC_BUFF(4),
		SHORT_ITEM(5);

		private int _index;

		RESULT_INDEX(int index) {
			_index = index;
		}

		public int toInt() {
			return _index;
		}

		public String result(NpcActionListener listener) {
			return listener.get_result_actions(this);
		}
	};

	private static final ArrayList<Integer> m_tower_npcs;
	static {
		m_tower_npcs = new ArrayList<Integer>();
		m_tower_npcs.add(8500028);
		m_tower_npcs.add(8500029);
		m_tower_npcs.add(8500030);
		m_tower_npcs.add(8500031);
		m_tower_npcs.add(8500032);
		m_tower_npcs.add(8500033);
		m_tower_npcs.add(8500034);
		m_tower_npcs.add(8500035);
		m_tower_npcs.add(8500036);
		m_tower_npcs.add(8500037);
		m_tower_npcs.add(8500038);
	}

	public static NpcActionListener newInstance(ResultSet rs) throws SQLException {
		return newInstance()
				.set_npc_id(rs.getInt("npc_id"))
				.set_action_name(rs.getString("action_name"))
				.set_opened(rs.getBoolean("is_opened"))
				.set_need_level(rs.getInt("need_level"))
				.set_limit_level(rs.getInt("need_limit_level"))
				.set_need_buff(rs.getInt("need_buff"))
				.set_need_pc_buff(rs.getBoolean("need_pc_buff"))
				.set_need_item_id(rs.getInt("need_item_id"))
				.set_need_item_amount(rs.getInt("need_item_amount"))
				.set_result_actions(rs);
	}

	public static NpcActionListener newInstance() {
		return new NpcActionListener();
	}

	private int _npc_id;
	private String _action_name;
	private boolean _is_opened;
	private int _need_level;
	private int _limit_level;
	private int _need_buff;
	private boolean _need_pc_buff;
	private int _need_item_id;
	private int _need_item_amount;
	private String[] _result_actions;

	protected NpcActionListener() {
	}

	public void dispose() {
		_action_name = null;
		_result_actions = null;
	}

	@Override
	public ActionListener deep_copy(ActionListener listener) {
		return listener.drain(this);
	}

	@Override
	public ActionListener deep_copy() {
		return deep_copy(newInstance());
	}

	@Override
	public ActionListener drain(ActionListener listener) {
		if (listener instanceof NpcActionListener) {
			NpcActionListener n_listener = (NpcActionListener) listener;
			set_npc_id(n_listener.get_npc_id());
			set_action_name(n_listener.get_action_name());
			set_opened(n_listener.is_opened());
			set_need_level(n_listener.get_need_level());
			set_limit_level(n_listener.get_limit_level());
			set_need_buff(n_listener.get_need_buff());
			set_need_pc_buff(n_listener.is_need_pc_buff());
			set_need_item_id(n_listener.get_need_item_id());
			set_need_item_amount(n_listener.get_need_item_amount());
			set_result_actions(n_listener.get_result_actions());
		}
		return this;
	}

	public NpcActionListener set_npc_id(int npc_id) {
		_npc_id = npc_id;
		return this;
	}

	public int get_npc_id() {
		return _npc_id;
	}

	public NpcActionListener set_opened(boolean b) {
		_is_opened = b;
		return this;
	}

	public NpcActionListener set_action_name(String action_name) {
		_action_name = action_name;
		return this;
	}

	public String get_action_name() {
		return _action_name;
	}

	public boolean is_opened() {
		return _is_opened;
	}

	public String result_un_opened(L1PcInstance pc) {
		return RESULT_INDEX.UN_OPENED.result(this);
	}

	public NpcActionListener set_need_level(int need_level) {
		_need_level = need_level;
		return this;
	}

	public int get_need_level() {
		return _need_level;
	}

	public String result_short_level(L1PcInstance pc) {
		return RESULT_INDEX.SHORT_LEVEL.result(this);
	}

	public NpcActionListener set_limit_level(int Max_level) {
		_limit_level = Max_level;
		return this;
	}

	public int get_limit_level() {
		return _limit_level;
	}

	public String result_limit_level(L1PcInstance pc) {
		return RESULT_INDEX.SHORT_LEVEL.result(this);
	}

	public NpcActionListener set_need_buff(int need_buff) {
		_need_buff = need_buff;
		return this;
	}

	public int get_need_buff() {
		return _need_buff;
	}

	public String result_no_buff(L1PcInstance pc) {
		return RESULT_INDEX.NO_BUFF.result(this);
	}

	public NpcActionListener set_need_pc_buff(boolean need_pc_buff) {
		_need_pc_buff = need_pc_buff;
		return this;
	}

	public boolean is_need_pc_buff() {
		return _need_pc_buff;
	}

	public String result_no_pc_buff(L1PcInstance pc) {
		return RESULT_INDEX.NO_PC_BUFF.result(this);
	}

	public NpcActionListener set_need_item_id(int need_item_id) {
		_need_item_id = need_item_id;
		return this;
	}

	public int get_need_item_id() {
		return _need_item_id;
	}

	public String result_short_item(L1PcInstance pc) {
		return RESULT_INDEX.SHORT_ITEM.result(this);
	}

	public NpcActionListener set_need_item_amount(int need_item_amount) {
		_need_item_amount = need_item_amount;
		return this;
	}

	public int get_need_item_amount() {
		return _need_item_amount;
	}

	public String result_success(L1PcInstance pc) {
		return get_result_actions(RESULT_INDEX.SUCCESS);
	}

	public NpcActionListener set_result_actions(ResultSet rs) throws SQLException {
		RESULT_INDEX[] indices = RESULT_INDEX.values();
		String[] result_actions = new String[indices.length];
		for (RESULT_INDEX index : indices) {
			String s = rs.getString(index.name());
			if (MJString.isNullOrEmpty(s))
				result_actions[index.toInt()] = MJString.EmptyString;
			else
				result_actions[index.toInt()] = s;
		}
		return set_result_actions(result_actions);
	}

	public NpcActionListener set_result_actions(String[] result_actions) {
		_result_actions = result_actions;
		return this;
	}

	public String[] get_result_actions() {
		return _result_actions;
	}

	public String get_result_actions(RESULT_INDEX result_index) {
		return _result_actions[result_index.toInt()];
	}

	@Override
	public final boolean eqauls_action(String action) {
		return get_action_name().equalsIgnoreCase(action);
	}

	@Override
	public final String to_action(L1PcInstance pc, L1Object target) {
		boolean is_success = false;
		try {
			if (!is_opened())
				return result_un_opened(pc);

			if (pc.getLevel() < get_need_level())
				return result_short_level(pc);

			if (get_limit_level() > 0 && pc.getLevel() > get_limit_level()) {
				return result_limit_level(pc);
			}

			int need_buff = get_need_buff();
			if (need_buff > 0 && !pc.hasSkillEffect(need_buff))
				return result_no_buff(pc);
			long sysTime = System.currentTimeMillis();
			if (is_need_pc_buff()) {
				if (pc.getAccount().getBuff_網咖() != null) {
					if (pc.getAccount().getBuff_網咖().getTime() / 1000 < sysTime) {
						return result_no_pc_buff(pc);
					}
				} else {
					return result_no_pc_buff(pc);
				}

			}
			int need_item_id = get_need_item_id();
			int need_item_amount = get_need_item_amount();
			if (need_item_id > 0 && need_item_amount > 0
					&& !pc.getInventory().consumeItem(need_item_id, need_item_amount))
				return result_short_item(pc);
			else if (need_item_id > 0 && need_item_amount == -1) {
				L1ItemInstance item = pc.getInventory().findItemId(need_item_id);
				if (item == null) {
					// 如果你是主宰之塔 NPC，請檢查是否有幻象護身符。
					if (m_tower_npcs.contains(_npc_id)) {
						if (!pc.getInventory().contains_at_one(new int[] { 560028, 4100135 })) {
							return result_short_item(pc);
						}
						// 一般NPC處理
					} else {
						return result_short_item(pc);
					}
				}
			}
			is_success = true;
			return result_success(pc);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (!is_success && pc.get_is_using_items())
				pc.start_teleport(pc.getX(), pc.getY(), pc.getMapId(), pc.getHeading(), 18339, true, true);
		}
		return result_short_item(pc);
	}

	@Override
	public boolean is_action() {
		return is_opened();
	}
}
