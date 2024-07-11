package l1j.server.MJBookQuestSystem.Loader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;

public class BQSIdInfo {
	private static HashMap<Integer, BQSIdInfo> m_achievement_to_criteria;
	private static HashMap<Integer, ArrayList<BQSIdInfo>> m_criteria_to_achievement;

	public static void do_load() {
		HashMap<Integer, BQSIdInfo> achievement_to_criteria = new HashMap<Integer, BQSIdInfo>();
		HashMap<Integer, ArrayList<BQSIdInfo>> criteria_to_achievement = new HashMap<Integer, ArrayList<BQSIdInfo>>();
		Selector.exec("select achievement_id,criteria_id from tb_mbook_achievements order by achievement_id asc",
				new FullSelectorHandler() {
					@Override
					public void result(ResultSet rs) throws Exception {
						while (rs.next()) {
							BQSIdInfo o = newInstance(rs);
							achievement_to_criteria.put(o.get_achievement_id(), o);

							ArrayList<BQSIdInfo> ids = criteria_to_achievement.get(o.get_criteria_id());
							if (ids == null) {
								ids = new ArrayList<BQSIdInfo>();
								criteria_to_achievement.put(o.get_criteria_id(), ids);
							}
							ids.add(o);
							o.set_achievement__level(ids.size());
						}
					}
				});

		m_achievement_to_criteria = achievement_to_criteria;
		m_criteria_to_achievement = criteria_to_achievement;
	}

	public static BQSIdInfo from_achievement_id(int achievement_id) {
		return m_achievement_to_criteria.get(achievement_id);
	}

	public static ArrayList<BQSIdInfo> from_criteria_id(int criteria_id) {
		return m_criteria_to_achievement.get(criteria_id);
	}

	public static BQSIdInfo from_criteria_id(final int criteria_id, int achievement_level) {
		ArrayList<BQSIdInfo> ids = from_criteria_id(criteria_id);
		if (ids == null) {
			Updator.exec("delete from tb_mbook_information where criteria_id=?", new Handler() {
				@Override
				public void handle(PreparedStatement pstm) throws Exception {
					pstm.setInt(1, criteria_id);
				}
			});
			System.out.println(String.format("找不到 BQSIdInfo...criteria_id : %d", criteria_id));
			return null;
		}
		if (ids.size() < achievement_level) {
			System.out.println(
					String.format("找不到 BQSIdInfo 成就等級...criteria_id : %d, 成就等級 : %d", criteria_id, achievement_level));
			return null;
		}
		return ids.get(achievement_level - 1);
	}

	private static BQSIdInfo newInstance(ResultSet rs) throws SQLException {
		return newInstance()
				.set_achievement_id(rs.getInt("achievement_id"))
				.set_criteria_id(rs.getInt("criteria_id"));
	}

	private static BQSIdInfo newInstance() {
		return new BQSIdInfo();
	}

	private int m_achievement_id;
	private int m_criteria_id;
	private int m_achievement__level;

	private BQSIdInfo() {
	}

	public BQSIdInfo set_achievement_id(int achievement_id) {
		m_achievement_id = achievement_id;
		return this;
	}

	public BQSIdInfo set_criteria_id(int criteria_id) {
		m_criteria_id = criteria_id;
		return this;
	}

	public BQSIdInfo set_achievement__level(int achievement__level) {
		m_achievement__level = achievement__level;
		return this;
	}

	public int get_achievement_id() {
		return m_achievement_id;
	}

	public int get_criteria_id() {
		return m_criteria_id;
	}

	public int get_achievement__level() {
		return m_achievement__level;
	}

}
