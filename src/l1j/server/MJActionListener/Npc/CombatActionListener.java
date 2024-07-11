package l1j.server.MJActionListener.Npc;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJActionListener.ActionListener;
import l1j.server.MJCombatSystem.MJCombatEGameType;
import l1j.server.MJCombatSystem.MJCombatObserver;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

public class CombatActionListener extends NpcActionListener {
	public static CombatActionListener newInstance(NpcActionListener listener, ResultSet rs) throws SQLException {
		return newInstance(listener).set_g_type(MJCombatEGameType.from_kr_desc(rs.getString("combat_type")));
	}

	public static CombatActionListener newInstance(NpcActionListener listener) {
		return (CombatActionListener) newInstance().drain(listener);
	}

	public static CombatActionListener newInstance() {
		return new CombatActionListener();
	}

	private MJCombatObserver _combat_observer;
	private MJCombatEGameType _g_type;

	private CombatActionListener() {
	}

	@Override
	public ActionListener deep_copy() {
		return deep_copy(newInstance());
	}

	@Override
	public ActionListener drain(ActionListener listener) {
		if (listener instanceof CombatActionListener) {
			CombatActionListener c_listener = (CombatActionListener) listener;
			set_combat_observer(c_listener.get_combat_observer());
			set_g_type(c_listener.get_g_type());
		}
		return super.drain(listener);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public String result_un_opened(L1PcInstance pc) {
		pc.sendPackets("現在不是遊戲時間。");
		return super.result_un_opened(pc);
	}

	public String result_already(L1PcInstance pc) {
		pc.sendPackets("遊戲已經開始了。");
		return super.result_un_opened(pc);
	}

	@override
	public String result_short_level(L1PcInstance pc) {
		pc.sendPackets(String.format("%d級開始可以進入。", get_need_level()));
		return super.result_short_level(pc);
	}

	@override
	public String result_limit_level(L1PcInstance pc) {
		pc.sendPackets(String.format("%d級以上無法再進行狩獵。", get_need_level()));
		return super.result_limit_level(pc);
	}

	@override
	public String result_no_buff(L1PcInstance pc) {
		pc.sendPackets("未滿足入場條件。");
		return super.result_no_buff(pc);
	}

	@override
	public String result_no_pc_buff(L1PcInstance pc) {
		pc.sendPackets("此行為僅限使用網吧使用券時可用。");
		return super.result_no_pc_buff(pc);
	}

	@override
	public String result_short_item(L1PcInstance pc) {
		L1Item item = ItemTable.getInstance().getTemplate(get_need_item_id());
		pc.sendPackets(String.format("%s %d缺少。", item.getName(), get_need_item_amount()));
		return super.result_short_item(pc);
	}

	@Override
	public String result_success(L1PcInstance pc) {
		if (_combat_observer == null || _combat_observer.is_closed() || _g_type.equals(MJCombatEGameType.NONE))
			return result_un_opened(pc);
		if (_combat_observer.is_running() || _combat_observer.is_disposing())
			return result_already(pc);

		_combat_observer.enter(pc);
		return super.result_success(pc);
	}

	public CombatActionListener set_combat_observer(MJCombatObserver observer) {
		_combat_observer = observer;
		return this;
	}

	public MJCombatObserver get_combat_observer() {
		return _combat_observer;
	}

	public CombatActionListener set_g_type(MJCombatEGameType g_type) {
		_g_type = g_type;
		return this;
	}

	public MJCombatEGameType get_g_type() {
		return _g_type;
	}
}
