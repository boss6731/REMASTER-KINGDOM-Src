package l1j.server.MJDungeonTimer.Updator;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.LinkedBlockingQueue;

import l1j.server.Config;
import l1j.server.CraftList.CraftListLoader;
import l1j.server.ItemDropLimit.ItemDropLimitLoader;
import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeLoadManager;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeProgressLoader;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_ATTENDANCE_USER_DATA_EXTEND;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.server.Account;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.CharacterFreeShieldTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.gametime.TimeListener;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.eCustomQuestType;

public class DungeonTimeUpdator implements TimeListener {
	private static DungeonTimeUpdator _instance;

	public static DungeonTimeUpdator getInstance() {
		if (_instance == null)
			_instance = new DungeonTimeUpdator();
		return _instance;
	}

	public static void release() {
		if (_instance != null) {
			_instance.dispose();
			_instance = null;
		}
	}

	public static void reload() {
		DungeonTimeUpdator old = _instance;
		_instance = new DungeonTimeUpdator();
		if (old != null) {
			old.dispose();
			old = null;
		}
	}

	public static boolean is_possible_update_character(L1PcInstance pc) {
		return pc != null && pc.getNetConnection() != null && pc.getAI() == null && !pc._destroyed;
	}

	private DungeonTimeConsumer _consumer;

	private DungeonTimeUpdator() {
	}

	public void run() {
		System.out.println(String.format("[%s]地下城時間更新器::啟動()", MJString.get_current_datetime()));
		RealTimeClock.getInstance().addListener(this, Calendar.SECOND);
		_consumer = new DungeonTimeConsumer();
		GeneralThreadPool.getInstance().execute(_consumer);
	}

	public void dispose() {
		System.out.println(String.format("[%s]地下城時間更新器::重置()", MJString.get_current_datetime()));
		RealTimeClock.getInstance().removeListener(this, Calendar.SECOND);
		if (_consumer != null) {
			_consumer.offer(Boolean.FALSE);
			_consumer = null;
		}
	}

	public boolean is_running() {
		return _consumer != null;
	}

	@Override
	public void onMonthChanged(BaseTime time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDayChanged(BaseTime time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHourChanged(BaseTime time) {
	}

	@Override
	public void onMinuteChanged(BaseTime time) {
		// TODO Auto-generated method stub

	}

	public void do_initialize(Calendar cal, boolean charge) {
		dispose();
		GeneralThreadPool.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				S_SystemMessage message = new S_SystemMessage(Config.Message.DUNGEON_TIME_UPDATOR);
				try {
					L1World.getInstance().getAllPlayerStream().filter((L1PcInstance pc) -> {
						return is_possible_update_character(pc);
					}).forEach((L1PcInstance pc) -> {
						try {
							DungeonTimeProgressLoader.update(pc);
							pc.initialize_dungeon_progress();
							pc.sendPackets(message, false);
							if (pc.noPlayerCK || pc.noPlayerck2 || pc.getAI() != null) {
								return;
							}

							if (charge) {
								DungeonTimeLoadManager.getInstance().do_charge_count_truncate(cal);
								pc.get_dungeon_information();
							} else
								DungeonTimeLoadManager.getInstance().do_truncate(cal);

							DungeonTimeProgressLoader.load(pc);
							DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance()
									.from_map_id(pc.getMapId());
							if (dtInfo != null) {
								pc.send_dungeon_progress(dtInfo);
							}
						} catch (Exception e) {
							System.out.println("地牢時間更新器::進行初始化()");
							e.printStackTrace();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (!charge) {
						DungeonTimeUpdator.this.run();
						message.clear();
					}
				}
				try {
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						CharacterFreeShieldTable.getInstance().resetGaho(pc);
						/*
						 * pc.getAccount().setPcGaho(3);
						 * pc.getAccount().setPcGahoUse(0);
						 */
						// pc.getAccount().setFeatherCount(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	public void onSecondChanged(BaseTime time) {
		/** 던전초기화 시간 **/
		if (time.equals_time(Config.ServerAdSetting.hour, Config.ServerAdSetting.minute,
				Config.ServerAdSetting.second)) {
			do_initialize(time.getCalendar(), false);
		}
		if (time.equals_day_time(Config.ServerAdSetting.day, Config.ServerAdSetting.hour, Config.ServerAdSetting.minute,
				Config.ServerAdSetting.second)) {
			do_initialize(time.getCalendar(), true);
		}
		if (time.equals_time(Config.ServerAdSetting.limit_hour, Config.ServerAdSetting.limit_minute,
				Config.ServerAdSetting.limit_second)) {
			ItemDropLimitLoader.getInstance().clearLimitItem();
			System.out.println(String.format("[%s]物品掉落限制加載器::清除限制物品", getLocalTime()));
		}
		if (Config.ServerAdSetting.Quest_true) {
			if (time.equals_time(Config.ServerAdSetting.Quest_hour, Config.ServerAdSetting.Quest_minute,
					Config.ServerAdSetting.Quest_second)) {
				String message = String.format("\\fT[任務通知] 每日任務重置時間已到，正在進行的任務將被刪除。");
				L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(message));
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message));
				DELETE_CUSTOM_DAY_QUEST();
				if (time.equals_day_time(Config.ServerAdSetting.Quest_day, Config.ServerAdSetting.Quest_hour,
						Config.ServerAdSetting.Quest_minute, Config.ServerAdSetting.Quest_second)) {
					String message_week = String.format("\\fT[任務通知] 每週任務重置時間已到，正在進行的任務將被刪除。");
					L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(message_week));
					L1World.getInstance()
							.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message_week));
					DELETE_CUSTOM_WEEK_QUEST();
				}
			}
		}
		if (time.equals_time(Config.ServerAdSetting.craft_hour, Config.ServerAdSetting.craft_minute,
				Config.ServerAdSetting.craft_second)) {
			CraftListLoader.getInstance().updateLimitItem();
			CraftListLoader.getInstance().reload();
			String message = String.format(Config.Message.CRAFT_LIMIT_TIME);
			L1World.getInstance().broadcastPacketToAll(new S_SystemMessage(message));
			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, message));
			System.out.println(String.format("[%s]製作清單加載器::清除製作限制", getLocalTime()));
		} else {
			if (_consumer != null) {
				_consumer.offer(Boolean.TRUE);
			}
		}
	}

	static class DungeonTimeConsumer implements Runnable {
		private LinkedBlockingQueue<Boolean> _signal;

		DungeonTimeConsumer() {
			_signal = new LinkedBlockingQueue<Boolean>();
		}

		public void offer(Boolean b) {
			_signal.offer(b);
		}

		@Override
		public void run() {
			try {
				boolean is_run = Boolean.TRUE;
				while (is_run) {
					is_run = _signal.take();
					if (!is_run)
						return;

					do_update();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void do_update() {
			try {
				DungeonTimeInformationLoader loader = DungeonTimeInformationLoader.getInstance();
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					if (!is_possible_update_character(pc))
						continue;

					DungeonTimeInformation dtInfo = loader.from_map_id(pc.getMapId());
					if (dtInfo == null)
						continue;

					pc.dec_dungeon_progress(dtInfo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String getLocalTime() {
		return formatter.format(new GregorianCalendar().getTime());
	}

	void DELETE_CUSTOM_DAY_QUEST() {
		Updator.exec("delete from character_custom_quest where quest_type=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, eCustomQuestType.ONEDAY.toInt());
			}
		});
	}

	void DELETE_CUSTOM_WEEK_QUEST() {
		Updator.exec("delete from character_custom_quest where quest_type=?", new Handler() {
			@Override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, eCustomQuestType.WEEK.toInt());
			}
		});
	}

	// public void DELETE_CUSTOM_DAY_QUEST1() {
	// Connection con = null;
	// PreparedStatement pstm = null;
	// try {
	// con = L1DatabaseFactory.getInstance().getConnection();
	// pstm = con.prepareStatement("DELETE FROM character_custom_quest WHERE
	// quest_type=?");
	// pstm.setInt(1, eCustomQuestType.ONEDAY.toInt());
	// pstm.execute();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// SQLUtil.close(pstm);
	// SQLUtil.close(con);
	// }
	// }
	//
	// public void DELETE_CUSTOM_WEEK_QUEST() {
	// Connection con = null;
	// PreparedStatement pstm = null;
	// try {
	// con = L1DatabaseFactory.getInstance().getConnection();
	// pstm = con.prepareStatement("DELETE FROM character_custom_quest WHERE
	// quest_type=?");
	// pstm.setInt(1, eCustomQuestType.WEEK.toInt());
	// pstm.execute();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// SQLUtil.close(pstm);
	// SQLUtil.close(con);
	// }
	// }

}
