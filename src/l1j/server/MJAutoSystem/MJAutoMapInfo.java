package l1j.server.MJAutoSystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_START_PLAY_SUPPORT_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport.SC_START_PLAY_SUPPORT_ACK.eResult;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.utils.IntRange;

public class MJAutoMapInfo {
	private static HashMap<Integer, MJAutoMapInfo> m_map_info;

	public static void do_load() {
		HashMap<Integer, MJAutoMapInfo> map_info = new HashMap<Integer, MJAutoMapInfo>();
		Selector.exec("select * from auto_map_info", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					MJAutoMapInfo o = newInstance(rs);
					map_info.put(o.get_mapid(), o);
				}
			}
		});
		m_map_info = map_info;
	}

	public static MJAutoMapInfo get_map_info(int mapid) {
		MJAutoMapInfo mInfo = m_map_info.get(mapid);
		if (mInfo == null) {
			L1Map map = L1WorldMap.getInstance().getMap((short) mapid);
			if (map != null && map.getBaseMapId() != mapid) {
				mInfo = m_map_info.get(map.getBaseMapId());
			}
		}
		return mInfo;
	}

	private static MJAutoMapInfo newInstance(ResultSet rs) throws SQLException {
		return newInstance()
				.set_mapid(rs.getInt("mapid"))
				.set_description(rs.getString("description"))
				.set_min_level(rs.getInt("min_level"))
				.set_max_level(rs.getInt("max_level"))
				.set_type(PSS_Type(rs.getString("type")));
	}

	public static int PSS_Type(String type) {
		if (type.equalsIgnoreCase("輔助"))
			return 1;
		else if (type.equalsIgnoreCase("周邊"))
			return 2;
		else if (type.equalsIgnoreCase("全部"))
			return 0;
		return 3;
	}

	private static MJAutoMapInfo newInstance() {
		return new MJAutoMapInfo();
	}

	private int m_mapid;
	private String m_description;
	private int m_min_level;
	private int m_max_level;
	private int m_type;

	private MJAutoMapInfo() {
	}

	public MJAutoMapInfo set_mapid(int mapid) {
		m_mapid = mapid;
		return this;
	}

	public MJAutoMapInfo set_description(String description) {
		m_description = description;
		return this;
	}

	public MJAutoMapInfo set_min_level(int min_level) {
		m_min_level = min_level;
		return this;
	}

	public MJAutoMapInfo set_max_level(int max_level) {
		m_max_level = max_level;
		return this;
	}

	public MJAutoMapInfo set_type(int type) {
		m_type = type;
		return this;
	}

	public int get_mapid() {
		return m_mapid;
	}

	public String get_description() {
		return m_description;
	}

	public int get_min_level() {
		return m_min_level;
	}

	public int get_max_level() {
		return m_max_level;
	}

	public int get_type() {
		return m_type;
	}

	public boolean type_check(L1PcInstance pc, int type) {
		if (get_type() == 0) {// 輔助
			SC_START_PLAY_SUPPORT_ACK ack = SC_START_PLAY_SUPPORT_ACK.newInstance();
			ack.set_result(eResult.UNKNOWN_ERROR);
			pc.sendPackets(ack, MJEProtoMessages.SC_START_PLAY_SUPPORT_ACK, false);
			pc.sendPackets("這裡無法使用 PSS 系統。");
			return true;
		} else if (get_type() == 1 && (type == 2 || type == 3)) { // 周邊
			SC_START_PLAY_SUPPORT_ACK ack = SC_START_PLAY_SUPPORT_ACK.newInstance();
			ack.set_result(eResult.UNKNOWN_ERROR);
			pc.sendPackets(ack, MJEProtoMessages.SC_START_PLAY_SUPPORT_ACK, false);
			pc.sendPackets("這裡只能使用 PSS 系統的輔助功能。");
			return true;
		} else if (get_type() == 2 && type == 3) {// 全部
			SC_START_PLAY_SUPPORT_ACK ack = SC_START_PLAY_SUPPORT_ACK.newInstance();
			ack.set_result(eResult.UNKNOWN_ERROR);
			pc.sendPackets(ack, MJEProtoMessages.SC_START_PLAY_SUPPORT_ACK, false);
			pc.sendPackets("這裡只能使用 PSS 系統的周邊功能。");
			return true;
		}

		if (!IntRange.includes(pc.getLevel(), get_min_level(), get_max_level())) {
			SC_START_PLAY_SUPPORT_ACK ack = SC_START_PLAY_SUPPORT_ACK.newInstance();
			ack.set_result(eResult.UNKNOWN_ERROR);
			pc.sendPackets(ack, MJEProtoMessages.SC_START_PLAY_SUPPORT_ACK, true);
			pc.sendPackets(
					String.format("%s 只能從 %d 級到 %d 級使用 PSS 系統。", get_description(), get_min_level(), get_max_level()));
			return true;
		}
		return false;
	}
}
