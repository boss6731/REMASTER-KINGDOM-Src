package l1j.server.MJDTSSystem;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.MJActionListener.ActionListenerLoader;
import l1j.server.MJActionListener.Npc.ListenerFinderTable;
import l1j.server.MJActionListener.Npc.TeleporterActionListener;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.MJCommons;

public class MJDTSElement {
	public static MJDTSElement newInstance(ResultSet rs) throws SQLException {
		return newInstance()
				.set_item_id(rs.getInt("item_id"))
				.set_use_count(rs.getInt("use_count"))
				.set_x(rs.getInt("x"))
				.set_y(rs.getInt("y"))
				.set_map_id(rs.getInt("map_id"))
				.set_area(rs.getInt("area"))
				.set_validation(rs.getBoolean("validation"))
				.set_min_level(rs.getInt("min_level"))
				.set_max_level(rs.getInt("max_level"));
	}

	public static MJDTSElement newInstance() {
		return new MJDTSElement();
	}

	private int _item_id;
	private int _use_count;
	private int _x;
	private int _y;
	private int _map_id;
	private int _area;
	private boolean _validation;
	private int _min_level;
	private int _max_level;

	private MJDTSElement() {
	}

	public MJDTSElement set_item_id(int val) {
		_item_id = val;
		return this;
	}

	public int get_item_id() {
		return _item_id;
	}

	public MJDTSElement set_use_count(int val) {
		_use_count = val;
		return this;
	}

	public int get_use_count() {
		return _use_count;
	}

	public MJDTSElement set_x(int val) {
		_x = val;
		return this;
	}

	public int get_x() {
		return _x;
	}

	public MJDTSElement set_y(int val) {
		_y = val;
		return this;
	}

	public int get_y() {
		return _y;
	}

	public MJDTSElement set_map_id(int val) {
		_map_id = val;
		return this;
	}

	public int get_map_id() {
		return _map_id;
	}

	public MJDTSElement set_area(int val) {
		_area = val;
		return this;
	}

	public int get_area() {
		return _area;
	}

	public MJDTSElement set_validation(boolean val) {
		_validation = val;
		return this;
	}

	public boolean get_validation() {
		return _validation;
	}

	public MJDTSElement set_min_level(int val) {
		_min_level = val;
		return this;
	}

	public int get_min_level() {
		return _min_level;
	}

	public MJDTSElement set_max_level(int val) {
		_max_level = val;
		return this;
	}

	public int get_max_level() {
		return _max_level;
	}

	private boolean is_valid(L1PcInstance pc) {
		if (pc.isGm())
			return true;

		return pc.getMap().isEscapable()
				&& !pc.isDead()
				&& !MJCommons.isNonAction(pc)
				&& !pc.get_teleport()
				&& !pc.isGhost()
				&& !pc.is_non_action()
				&& !pc.is_combat_field();
	}

	public void use(L1PcInstance pc, L1ItemInstance item) {
		int lvl = pc.getLevel();
		if ((_max_level > 0 && _max_level < lvl) || (_min_level > 0 && _min_level > lvl)) {
			pc.sendPackets(String.format("%s 物品只能在 %d 等級到 %d 等級之間使用。", item.getName(), _min_level, _max_level));
			return;
		}

		if (_validation && !is_valid(pc)) {
			pc.sendPackets("無法使用。");
			return;
		}

		TeleporterActionListener listener = ActionListenerLoader.getInstance()
				.findListener(ListenerFinderTable.getInstance().getListenerInfo(_map_id));
		if (listener != null) {
			if (!listener.is_opened()) {
				pc.sendPackets("目前無法進入。");
				return;
			}
		}

		if (_use_count > 0) {
			int amount = 1;
			if (item.isStackable() && _use_count > 1)
				amount = _use_count;

			if (!pc.getInventory().consumeItem(item, amount)) {
				pc.sendPackets(String.format("%s 物品至少需要 %d 個才能使用。", item.getName(), amount));
				return;
			}
		}

		MJPoint pt = MJPoint.newInstance(_x, _y, _area, (short) _map_id, 50);
		pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true, false);
	}
}