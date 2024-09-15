package l1j.server.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;


import l1j.server.L1DatabaseFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.server.IdFactory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.EventTimeTemp;
import l1j.server.server.utils.MJCommons;

public class EventTimeTable {

	private static EventTimeTable instance;

	public static EventTimeTable getInstance() {
		if (instance == null) {
			instance = new EventTimeTable();
		}
		return instance;
	}

	private EventTimeTable() {
		store_event_time();
		store_npc();
	}

	public static int getNowDatByString(String s) {
		return 0;
	}

	public void reload() {
		EventTimeTable oladInstance = instance;
		instance = new EventTimeTable();
		oladInstance.list.clear();
		oladInstance.npc_list.clear();
		oladInstance = null;
	}

	private ConcurrentHashMap<Integer, EventTimeTemp> list = new ConcurrentHashMap<Integer, EventTimeTemp>();

	private void store_event_time() {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection();
				PreparedStatement pst = con.prepareStatement("SELECT * FROM event_boss_time");
				ResultSet rst = pst.executeQuery();) {
			EventTimeTemp ett = null;
			while (rst.next()) {
				ett = new EventTimeTemp();
				ett.set_type(rst.getInt("type"));
				ett.set_npcid(rst.getInt("npcid"));
				ett.setTitle(rst.getString("title"));
				ett.setSubNpc(rst.getString("sub_npc"));
				ett.setSubTitle(rst.getString("sub_title"));
				ett.setSubActid(rst.getString("sub_actid"));
				ett.set_loc_x(rst.getInt("loc_x"));
				ett.set_loc_y(rst.getInt("loc_y"));
				ett.set_loc_map(rst.getInt("loc_map"));
				ett.set_loc_rnd(rst.getInt("loc_rnd"));

				ett.set_hour(rst.getInt("hour"));
				ett.set_minute(rst.getInt("minute"));
				ett.set_delete_time(rst.getInt("delete_time"));

				String[] spawn_yoil = new String[] { "全部" };
				try {
					StringTokenizer stt = new StringTokenizer(rst.getString("day"), ",");
					spawn_yoil = new String[stt.countTokens()];
					for (int i = 0; stt.hasMoreTokens(); ++i) {
						spawn_yoil[i] = stt.nextToken();
					}

					ett.setYoil(spawn_yoil);
				} catch (Exception e) {
					e.printStackTrace();
				}

				long time_i = get_current_time();
				long spawn_time = 0;
				int now_day = getNowDay();
				int index = 0;
				String real_day_string = getNowDayByInt(now_day);
				boolean is_in_day_string = false;
				int is_in_day_check_index = 0;
				for (String check_week : ett.getYoil()) {
					if (check_week.equalsIgnoreCase(real_day_string)) {
						is_in_day_string = true;
						break;
					}
					is_in_day_check_index++;
				}

				if (is_in_day_string) {
					// 如果已經輸入了開始時間，則跳過
					if (ett.get_startdate() != 0) {
						continue;
					}

					// 如果該星期包含在 getYoil 中，檢查時間
					spawn_time = get_spawn_hour(ett.get_hour(), ett.get_minute());
					if (time_i < spawn_time) {
                        // *System.out.println("時間尚未過去，因此輸入原有的生成時間: " + is_in_day_check_index);
                        // *當前時間小於生成時間（尚未到達事件時間）
                        // *則保持原有的生成時間，並將其對應的星期索引加1。
                        ett.set_startdate(spawn_time);
						ett.set_next_day_index(is_in_day_check_index);
					} else {
						if ((is_in_day_check_index+1) >= ett.getYoil().length) {
							is_in_day_check_index = 0;
						} else {
							is_in_day_check_index++;
						}

						String next_week = ett.getYoil()[is_in_day_check_index];
						
						//System.out.println(next_week);
						int week_day = getNowDatByString(next_week);

						int day_gap = now_day - week_day; // 7 - 1

						if (day_gap > 0) {
							day_gap = (7 - day_gap);
						} else {
							if (day_gap == 0 && !next_week.equalsIgnoreCase("全部") && ett.getYoil().length == 1) {
								day_gap += 7;
							} else {
								day_gap *= -1;
							}
						}

						// System.out.println("now_day : " + now_day + " / week_day : " + week_day);
						// System.out.println(ett.getTitle() + " / day_gap : " + day_gap);

						spawn_time = get_spawn_hour(ett.get_hour(), ett.get_minute()) + (day_gap * 86400000);

						if (time_i > spawn_time) {
							continue;
						}

						// System.out.println("week : " + week + " / week_day : " +
						// week_day + " / day_gap : " + day_gap);
						/**
						 * 根據當前時間和每個星期的時間，如果本週已經過去，則計算下一個星期的時間。
						 **/
						ett.set_startdate(spawn_time);
						ett.set_next_day_index(is_in_day_check_index);
						// System.out.println("index : " + index);
					}
				} else {
					for (String week : ett.getYoil()) {
						// "日 = 1"："一 = 2"："二 = 3"："三 = 4"："四 = 5"："五 = 6"："六 = 7"
						if (week.equalsIgnoreCase("全部")) {
							spawn_time = get_spawn_hour(ett.get_hour(), ett.get_minute());
							// System.out.println("time_i : " + time_i + " / spawn_time : " + spawn_time);
							if (time_i > spawn_time) {
								spawn_time = get_spawn_hour(ett.get_hour(), ett.get_minute()) + 86400000;
							}
							ett.set_startdate(spawn_time);
							ett.set_next_day_index(index);
							continue;
						}
						if (ett.get_startdate() != 0) {
							continue;
						}

						int week_day = getNowDatByString(week);
						int day_gap = now_day - week_day; // 7 - 1

						if (day_gap > 0) {
							day_gap = (7 - day_gap);
						} else {
							if (day_gap == 0 && !week.equalsIgnoreCase("全部") && ett.getYoil().length == 1) {
								day_gap += 7;
							} else {
								day_gap *= -1;
							}
						}

						// System.out.println("now_day : " + now_day + " / week_day : " + week_day);
						// System.out.println(ett.getTitle() + " / day_gap : " + day_gap);

						spawn_time = get_spawn_hour(ett.get_hour(), ett.get_minute()) + (day_gap * 86400000);

						if (time_i > spawn_time) {
							continue;
						}

						// System.out.println("week : " + week + " / week_day : " +
						// week_day + " / day_gap : " + day_gap);
						/**
						 * 根據當前時間和每週特定的時間，如果本週已過，則計算下一個相同星期的時間。
						 **/
						ett.set_startdate(spawn_time);
						ett.set_next_day_index(index);
						// System.out.println("index : " + index);
					}
					index++;

				}

				ett.set_enddate(ett.get_startdate() + (ett.get_delete_time() * 60000));

				String is_tel = rst.getString("is_tel");
				if (is_tel.equalsIgnoreCase("true"))
					ett.set_tel(true);
				else if (is_tel.equalsIgnoreCase("false"))
					ett.set_tel(false);

				ett.set_tel_x(rst.getInt("tel_x"));
				ett.set_tel_y(rst.getInt("tel_y"));
				ett.set_tel_map(rst.getInt("tel_map"));
				ett.set_tel_rnd(rst.getInt("tel_rnd"));

				ett.set_tel_count(rst.getInt("tel_count"));

				String is_msg = rst.getString("is_msg");
				if (is_msg.equalsIgnoreCase("true"))
					ett.setMsg(true);
				else if (is_msg.equalsIgnoreCase("false"))
					ett.setMsg(false);
				ett.set_boss_message(rst.getString("boss_msg"));

				String is_yn = rst.getString("is_yn");
				if (is_yn.equalsIgnoreCase("true"))
					ett.setYn(true);
				else if (is_yn.equalsIgnoreCase("false"))
					ett.setYn(false);
				ett.set_yn_ment(rst.getString("yn_ment"));

				String is_effect = rst.getString("is_effect");
				if (is_effect.equalsIgnoreCase("true"))
					ett.set_is_Effect(true);
				else if (is_effect.equalsIgnoreCase("false"))
					ett.set_is_Effect(false);
				ett.set_Effect(rst.getInt("effect"));

				String alarm_onoff = rst.getString("alarm_onoff");
				if (alarm_onoff.equalsIgnoreCase("true"))
					ett.set_alarm_onoff(true);
				else if (alarm_onoff.equalsIgnoreCase("false"))
					ett.set_alarm_onoff(false);

				ett.setAinEffect(rst.getString("ain_effect").equalsIgnoreCase("true") ? true : false);
				ett.setNewEffect(rst.getString("New_effect").equalsIgnoreCase("true") ? true : false);

				list.put(ett.get_type(), ett);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getNowDayByString(String week) {
		int nowDay = 0;
		switch (week) {
			case "日":
				nowDay = 1;
				break;
			case "一":
				nowDay = 2;
				break;
			case "二":
				nowDay = 3;
				break;
			case "三":
				nowDay = 4;
				break;
			case "四":
				nowDay = 5;
				break;
			case "五":
				nowDay = 6;
				break;
			case "六":
				nowDay = 7;
				break;
		}

		return nowDay;
	}

	private static String getNowDayByInt(int num) {
		switch (num) {
			case 1:
				return "日";
			case 2:
				return "一";
			case 3:
				return "二";
			case 4:
				return "三";
			case 5:
				return "四";
			case 6:
				return "五";
			case 7:
				return "六";
		}
		return "日";
	}

	public static int getNowDay() {
		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);

		int nowDay = cal.get(Calendar.DAY_OF_WEEK);
		return nowDay;
	}

	private FastTable<L1NpcInstance> npc_list = new FastTable<L1NpcInstance>();

	private void store_npc() {
		try {
			Iterator<EventTimeTemp> iter = list.values().iterator();
			EventTimeTemp ett = null;
			L1NpcInstance npc = null;

			while (iter.hasNext()) {
				ett = iter.next();
				if (ett == null) {
					continue;
				}
				npc = NpcTable.getInstance().newNpcInstance(ett.get_npcid());

				if (npc == null) {
					continue;
				}

				if (ett.getSubNpc() != null && ett.getSubTitle() != null) {
					if (MJCommons.isNullOrEmpty(ett.getSubNpc()) || MJCommons.isNullOrEmpty(ett.getSubTitle())) {
						System.out.println(String.format("[●警告●:event_boss_time] [type: %d] [sub_npc: %s] [sub_title: %s] 相關欄位的輸入不是 (NULL)。請按 (DELETE) 鍵將該欄位更改為 null。", ett.get_type(), ett.getSubNpc(), ett.getSubTitle()));
								ett.getSubNpc(), ett.getSubTitle()));
						continue;
					}
				}

				npc.setId(IdFactory.getInstance().nextId());
				npc.set_boss_type(ett.get_type());
				if (ett.getTitle() != null) {
					String[] subactid = null;
					npc.setEventTitle(ett.getTitle());
					npc.setEventSubTitle(ett.getSubTitle());
					npc.setEventSubNpc(ett.getSubNpc());

					String[] subtitle = (String[]) MJArrangeParser.parsing(ett.getSubTitle(), ",", MJArrangeParseeFactory.createStringArrange()).result();
					String[] subnpc = (String[]) MJArrangeParser.parsing(ett.getSubNpc(), ",", MJArrangeParseeFactory.createStringArrange()).result();

					for (int i = 0; i < subtitle.length; i++) {
						int subnpcid = Integer.parseInt(subnpc[i]);
						L1Object subinfo = L1World.getInstance().isNpcShop(subnpcid);
						if (subinfo instanceof L1NpcInstance) {
							L1NpcInstance sub = (L1NpcInstance) subinfo;
							sub.set_is_sub_npc(true);
						}
					}

					if (ett.getSubActid() != null) {
						subactid = (String[]) MJArrangeParser.parsing(ett.getSubActid(), ",", MJArrangeParseeFactory.createStringArrange()).result();
						for (int i = 0; i < subtitle.length; i++) {
							int subnpcid = Integer.parseInt(subnpc[i]);
							L1Object subinfo = L1World.getInstance().isNpcShop(subnpcid);
							if (subinfo instanceof L1NpcInstance) {
								L1NpcInstance sub = (L1NpcInstance) subinfo;
								if (!subactid[i].equalsIgnoreCase("none"))
									sub.setEventSubActid(subactid[i]);
							}
						}
					}
				}
				npc.setX(ett.get_loc_x());
				npc.setHomeX(ett.get_loc_x());
				npc.setY(ett.get_loc_y());
				npc.setHomeY(ett.get_loc_y());
				npc.setMap((short) ett.get_loc_map());
				npc.setHomeRnd(ett.get_loc_rnd());

				npc.set_boss_time(ett.get_startdate());
				npc.set_end_boss_time(ett.get_enddate());
				npc.setBossIngTime(ett.get_delete_time());
				npc.set_boss_hour(ett.get_hour());
				npc.set_boss_minute(ett.get_minute());
				npc.setYoil(ett.getYoil());
				npc.set_next_day_index(ett.get_next_day_index());

				npc.set_is_boss_tel(ett.is_tel());
				npc.set_boss_tel_x(ett.get_tel_x());
				npc.set_boss_tel_y(ett.get_tel_y());
				npc.set_boss_tel_m(ett.get_tel_map());
				npc.set_boss_tel_rnd(ett.get_tel_rnd());

				npc.set_boss_tel_count(ett.get_tel_count());

				npc.set_is_boss_msg(ett.isMsg());
				npc.set_boss_msg(ett.get_boss_message());

				npc.set_boss_yn(ett.isYn());
				npc.set_boss_yn_msg(ett.get_yn_ment());

				npc.set_is_boss_effect(ett.is_Effect());
				npc.set_boss_effect(ett.get_Effect());

				npc.set_is_boss_alarm(ett.is_alarm_onoff());

				npc.setEventTimeTemp(ett);

				npc.setHeading(5);
				npc_list.add(npc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
     * -- 計算生成的 '時間'
     */
	public long get_spawn_hour(int hour, int minute) {
		Date date = getDate(hour, minute);

		long time = date.getTime();
		return time;
	}

	public static long get_current_time() {
		Date date = new Date(System.currentTimeMillis());
		long time = date.getTime();
		return time;
	}

	public Iterator<L1NpcInstance> get_npc_iter() {
		return npc_list.iterator();
	}

	public static Date getDate(int hour, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), hour, minute, cal.get(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

}