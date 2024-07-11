package l1j.server.EventSystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.StringTokenizer;

import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.server.model.Instance.L1PcInstance;

public class EventSystemInfo {
	static EventSystemInfo newInstance(ResultSet rs) throws SQLException {
		EventSystemInfo pInfo = newInstance();
		pInfo._get_id = rs.getInt("getid");
		pInfo._npc_id = rs.getInt("npcid");
		pInfo._console_type = rs.getString("console_type");
		pInfo._event_name = rs.getString("event_name");
		pInfo._action_id = rs.getString("action_id");
		pInfo._action_name = rs.getString("action_name");
		pInfo._spawn_loc = rs.getString("spawn_loc");
		pInfo._event_map_id = rs.getInt("event_map_id");
		pInfo._teleport_x = rs.getInt("teleport_x");
		pInfo._teleport_y = rs.getInt("teleport_y");
		pInfo._drop_item = rs.getString("drop_item");
		pInfo._min_count = rs.getString("min_count");
		pInfo._max_count = rs.getString("max_count");
		pInfo._drop_min_chance = rs.getString("drop_min_chance");
		pInfo._drop_max_chance = rs.getString("drop_max_chance");
		pInfo._event_time = rs.getString("event_time");
		pInfo._event_end_time = rs.getInt("event_end_time");
		pInfo._is_mapout = MapOutType(rs.getString("mapout"));

		String[] spawn_yoil = new String[] { "全部" };
		try {
			if (rs.getString("spawn_yoil") != null) {
				StringTokenizer stt = new StringTokenizer(rs.getString("spawn_yoil"), "|");
				spawn_yoil = new String[stt.countTokens()];
				for (int i = 0; stt.hasMoreTokens(); ++i)
					spawn_yoil[i] = stt.nextToken();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		pInfo.setYoil(spawn_yoil);

		return pInfo;
	}

	public static boolean MapOutType(String type) {
		if (type.equalsIgnoreCase("true"))
			return true;
		else
			return false;
	}

	private static EventSystemInfo newInstance() {
		return new EventSystemInfo();
	}

	private boolean _is_event = false;
	private int _get_id;
	private int _npc_id;
	private String _console_type;
	private String _event_name;
	private String _action_id;
	private String _action_name;
	private String _spawn_loc;
	private int _event_map_id;
	private int _teleport_x;
	private int _teleport_y;
	private String _drop_item;
	private String _min_count;
	private String _max_count;
	private String _drop_min_chance;
	private String _drop_max_chance;
	private String _event_time;
	private String[] yoil;
	private int _event_end_time;
	private boolean _is_mapout = false;
	private boolean _is_open = false;

	private EventSystemInfo() {
	}

	public int get_id() {
		return _get_id;
	}

	public int get_npc_id() {
		return _npc_id;
	}

	public String get_console_type() {
		return _console_type;
	}

	public String get_event_name() {
		return _event_name;
	}

	public String get_action_id() {
		return _action_id;
	}

	public String get_action_name() {
		return _action_name;
	}

	public String get_spawn_loc() {
		return _spawn_loc;
	}

	public int get_event_map_id() {
		return _event_map_id;
	}

	public int get_teleport_x() {
		return _teleport_x;
	}

	public int get_teleport_y() {
		return _teleport_y;
	}

	public String get_drop_item() {
		return _drop_item;
	}

	public String get_min_count() {
		return _min_count;
	}

	public String get_max_count() {
		return _max_count;
	}

	public String get_drop_min_chance() {
		return _drop_min_chance;
	}

	public String get_drop_max_chance() {
		return _drop_max_chance;
	}

	public String get_event_time() {
		return _event_time;
	}

	public int get_event_end_time() {
		return _event_end_time;
	}

	public String[] getYoil() {
		return yoil;
	}

	public void setYoil(String[] yoil) {
		this.yoil = yoil;
	}

	public boolean is_mapout() {
		return _is_mapout;
	}

	public boolean set_mapout(boolean flag) {
		_is_event = flag;
		return _is_mapout;
	}

	public boolean is_event() {
		return _is_event;
	}

	public boolean set_event(boolean flag) {
		_is_event = flag;
		return _is_event;
	}

	public boolean is_open() {
		return _is_open;
	}

	public boolean set_open(boolean flag) {
		return _is_open = flag;
	}

	void print() {
	}

	public int calc_probability(int i, L1PcInstance pc) {
		Random random = new Random(System.nanoTime());
		int probability = 0;
		String[] drop_min_chance = (String[]) MJArrangeParser
				.parsing(get_drop_min_chance(), ",", MJArrangeParseeFactory.createStringArrange()).result();
		String[] drop_max_chance = (String[]) MJArrangeParser
				.parsing(get_drop_max_chance(), ",", MJArrangeParseeFactory.createStringArrange()).result();

		if (drop_min_chance[i] == drop_max_chance[i])
			probability = Integer.parseInt(drop_max_chance[i]);
		else
			probability = random.nextInt(Integer.parseInt(drop_max_chance[i]) - Integer.parseInt(drop_min_chance[i])
					+ Integer.parseInt(drop_max_chance[i]));

		return probability;
	}

	public int calc_count(int i, L1PcInstance pc) {
		String[] drop_min_count = (String[]) MJArrangeParser
				.parsing(get_min_count(), ",", MJArrangeParseeFactory.createStringArrange()).result();
		String[] drop_max_count = (String[]) MJArrangeParser
				.parsing(get_max_count(), ",", MJArrangeParseeFactory.createStringArrange()).result();
		int count = 0;
		if (Integer.parseInt(drop_min_count[i]) == Integer.parseInt(drop_max_count[i]))
			count = Integer.parseInt(drop_max_count[i]);
		else
			count = MJRnd.next(Integer.parseInt(drop_min_count[i]), Integer.parseInt(drop_max_count[i]));

		return count;
	}

	public boolean isMap(L1PcInstance pc) {
		int mapId = get_event_map_id();
		if (pc.getMapId() == mapId) {
			return true;
		}
		return false;
	}
}
