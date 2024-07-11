package l1j.server.MJActionListener.Npc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.MJActionListener.ActionListener;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CONNECT_HIBREEDSERVER_NOTI_PACKET;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Item;

public class TeleporterActionListener extends NpcActionListener {
	public static TeleporterActionListener newInstance(NpcActionListener listener, ResultSet rs) throws SQLException {
		return newInstance(listener)
				.set_destination_name(rs.getString("destination_name"))
				.set_teleport_x(rs.getInt("teleport_x"))
				.set_teleport_y(rs.getInt("teleport_y"))
				.set_teleport_map_id(rs.getString("teleport_map_id"))
				.set_range(rs.getInt("range"));
	}

	public static TeleporterActionListener newInstance(NpcActionListener listener) {
		return (TeleporterActionListener) newInstance().drain(listener);
	}

	public static TeleporterActionListener newInstance() {
		return new TeleporterActionListener();
	}

	private String _destination_name;
	private int _teleport_x;
	private int _teleport_y;
	private short[] _teleport_map_id;
	private int _range;
	private ArrayList<MJPoint> passables;

	protected TeleporterActionListener() {
	}

	@Override
	public ActionListener deep_copy() {
		return deep_copy(newInstance());
	}

	@Override
	public ActionListener drain(ActionListener listener) {
		if (listener instanceof TeleporterActionListener) {
			TeleporterActionListener t_listener = (TeleporterActionListener) listener;
			set_destination_name(t_listener.get_destination_name());
			set_teleport_x(t_listener.get_teleport_x());
			set_teleport_y(t_listener.get_teleport_y());
			set_teleport_map_id(t_listener.get_teleport_map_id());
			set_range(t_listener.get_range());
		}
		return super.drain(listener);
	}

	@Override
	public void dispose() {
		super.dispose();
		_destination_name = null;
	}

	public TeleporterActionListener set_destination_name(String destination_name) {
		_destination_name = destination_name;
		return this;
	}

	public String get_destination_name() {
		return _destination_name;
	}

	public TeleporterActionListener set_teleport_x(int teleport_x) {
		_teleport_x = teleport_x;
		return this;
	}

	public int get_teleport_x() {
		return _teleport_x;
	}

	public TeleporterActionListener set_teleport_y(int teleport_y) {
		_teleport_y = teleport_y;
		return this;
	}

	public int get_teleport_y() {
		return _teleport_y;
	}

	public TeleporterActionListener set_teleport_map_id(short[] teleport_map_id) {
		_teleport_map_id = teleport_map_id;
		return this;
	}

	public TeleporterActionListener set_teleport_map_id(String teleport_map_ids) {
		Integer[] ids = (Integer[]) MJArrangeParser
				.parsing(teleport_map_ids, ",", MJArrangeParseeFactory.createIntArrange()).result();

		int size = ids.length;
		_teleport_map_id = new short[size];
		for (int i = size - 1; i >= 0; --i)
			_teleport_map_id[i] = ids[i].shortValue();
		return this;
	}

	public short[] get_teleport_map_id() {
		return _teleport_map_id;
	}

	public TeleporterActionListener set_range(int range) {
		_range = range;
		return this;
	}

	public int get_range() {
		return _range;
	}

	@Override
	public String result_un_opened(L1PcInstance pc) {
		// pc.sendPackets(String.format("%s 目前未開啟。", get_destination_name()));
		// 你不能進入。
		pc.sendPackets(1229);
		return super.result_un_opened(pc);
	}

	@Override
	public String result_short_level(L1PcInstance pc) {
		// pc.sendPackets(String.format("%s可以從%d級進入。", get_destination_name(),
		// get_need_level()));
		// 沒有條目：等級不正確
		pc.sendPackets(5359);
		pc.sendPackets(SC_NOTIFICATION_MESSAGE.make_stream("入場的最低等級不符合要求。", MJSimpleRgb.green(), 5));
		return super.result_short_level(pc);
	}

	@Override
	public String result_limit_level(L1PcInstance pc) {
		// pc.sendPackets(String.format("%s可以從%d層進入。",
		// get_destination_name(), get_need_level()));
		// 沒有條目：等級不正確
		pc.sendPackets(5359);
		pc.sendPackets(SC_NOTIFICATION_MESSAGE.make_stream("入場的最高等級不符合要求。", MJSimpleRgb.green(), 5));
		return super.result_limit_level(pc);
	}

	@Override
	public String result_no_buff(L1PcInstance pc) {
		pc.sendPackets(new S_ServerMessage(5359));
		return super.result_no_buff(pc);
	}

	@Override
	public String result_no_pc_buff(L1PcInstance pc) {
		pc.sendPackets(new S_ServerMessage(4487));
		return super.result_no_pc_buff(pc);
	}

	@Override
	public String result_short_item(L1PcInstance pc) {
		L1Item item = ItemTable.getInstance().getTemplate(get_need_item_id());
		int needAmount = get_need_item_amount();
		if (needAmount > 0)
			pc.sendPackets(new S_ServerMessage(5359));
		// pc.sendPackets(String.format("%s %d이(가) 부족합니다.", item.getName(),
		// get_need_item_amount()));
		else
			pc.sendPackets(String.format("沒有%s。", item.getName()));
		return super.result_short_item(pc);
	}

	@Override
	public String result_success(L1PcInstance pc) {
		MJPoint pt = null;
		if (get_teleport_map_id()[0] == 5490) {
			pt = MJPoint.newInstance();
			pt.x = get_teleport_x();
			pt.y = get_teleport_y();
			pt.mapId = get_teleport_map_id()[0];
		} else {
			makePassableMap();
			pt = passables.get(MJRnd.next(passables.size()));
		}
		// TODO 使用特定 NC 輸入時的條件語句
		switch (get_npc_id()) {
			case 80082:
				L1PolyMorph.undoPoly(pc);
				break;
		}

		// 利用TODO interserver（似乎供本地使用）
		switch (get_npc_id()) {
			case 8502072:
				SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, pt.x, pt.y, pt.mapId,
						SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_TEST);
				break;
			case 70086:
				SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, pt.x, pt.y, pt.mapId,
						SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_FISLAND);
				break;

			case 8500028:
			case 8500029:
			case 8500030:
			case 8500031:
			case 8500032:
			case 8500033:
			case 8500034:
			case 8500035:
			case 8500036:
			case 8500037:
			case 8500038:
				if (Config.Login.InterServerUse) {
					SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, pt.x, pt.y, pt.mapId,
							SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_DOM_TOWER);
				} else {
					pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true, false);
				}
				break;

			case 8502037:
			case 8502042:
				// System.out.println(pt.x+"+"+pt.y+"+"+pt.mapId);
				SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, pt.x, pt.y, pt.mapId,
						SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_MAGIC_DOLL_RACE);
				break;
			case 8502038:
				SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, pt.x, pt.y, pt.mapId,
						SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_BACK);
				break;
			default:
				// SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.do_send(pc, pt.x, pt.y, pt.mapId,
				// SC_CONNECT_HIBREEDSERVER_NOTI_PACKET.RESERVED_INTER_BACK_RESTART);
				pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true, false);
				break;
		}

		// 不使用 TODO 間伺服器的正常傳送
		/*
		 * int range_x = MJRnd.next(get_range());
		 * if(MJRnd.isBoolean())
		 * range_x *= -1;
		 * int range_y = MJRnd.next(get_range());
		 * if(MJRnd.isBoolean())
		 * range_y *= -1;
		 * pc.start_teleport(get_teleport_x() + range_x, get_teleport_y() + range_y,
		 * get_teleport_map_id()[0], pc.getHeading(), 169, true, false);
		 */

		return super.result_success(pc);
	}

	public void makePassableMap() {
		if (passables == null) {

			L1Map map = L1WorldMap.getInstance().getMap(get_teleport_map_id()[0]);
			passables = new ArrayList<MJPoint>(get_range() * get_range());
			int r = get_range();
			int minX = get_teleport_x() - r;
			int maxX = get_teleport_x() + r;
			int minY = get_teleport_y() - r;
			int maxY = get_teleport_y() + r;
			for (int x = maxX; x >= minX; --x) {
				for (int y = maxY; y >= minY; --y) {
					if (MJPoint.isValidPosition(map, x, y)) {
						MJPoint pt = MJPoint.newInstance();
						pt.x = x;
						pt.y = y;
						pt.mapId = (short) map.getId();
						passables.add(pt);
					}
				}
			}
		}
	}

	public boolean simple_teleport(L1PcInstance pc) {
		makePassableMap();
		MJPoint pt = passables.get(MJRnd.next(passables.size()));

		// TODO 伺服器間利用率
		pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true, false);
		return true;

		// 不要使用 TODO 伺服器間
		/*
		 * L1Map map = L1WorldMap.getInstance().getMap(get_teleport_map_id()[0]);
		 * int try_count = 0;
		 * int r = get_range();
		 * int cx = 0;
		 * int cy = 0;
		 * int x = get_teleport_x();
		 * int y = get_teleport_y();
		 * do{
		 * cx = x + (MJRnd.isBoolean() ? MJRnd.next(r) : -MJRnd.next(r));
		 * cy = y + (MJRnd.isBoolean() ? MJRnd.next(r) : -MJRnd.next(r));
		 * }while(!MJPoint.isValidPosition(map, cx, cy) && ++try_count < 10);
		 * 
		 * if(try_count >= 10){
		 * cx = x;
		 * cy = y;
		 * }
		 * pc.start_teleport(cx, cy, map.getId(), pc.getHeading(), 169, true, false);
		 * return true;
		 */
	}
}
