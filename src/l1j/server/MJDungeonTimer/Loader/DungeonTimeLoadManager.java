package l1j.server.MJDungeonTimer.Loader;

import java.util.Calendar;

import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.Updator.DungeonTimeUpdator;
import l1j.server.MJIndexStamp.MJEStampIndex;
import l1j.server.MJIndexStamp.MJIndexStampManager;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.Command.MJCommandTree;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.RealTimeClock;

public class DungeonTimeLoadManager implements MJCommand {
	private static DungeonTimeLoadManager _instance;

	public static DungeonTimeLoadManager getInstance() {
		if (_instance == null)
			_instance = new DungeonTimeLoadManager();
		return _instance;
	}

	public static void release() {
		if (_instance != null) {
			_instance.dispose();
			_instance = null;
		}
	}

	public static void reload() {
		DungeonTimeLoadManager old = _instance;
		_instance = new DungeonTimeLoadManager();
		if (old != null) {
			old.dispose();
			old = null;
		}
	}

	private MJCommandTree _commands;

	private DungeonTimeLoadManager() {
		_commands = createCommand();
	}

	public void do_truncate(Calendar current) {
		try {
			Updator.dungeon_delete("tb_dungeon_time_account_information");
			Updator.dungeon_delete("tb_dungeon_time_char_information");
			MJIndexStampManager.update(MJEStampIndex.SIDX_DUNGEON_TIME_TRUNCATE, current);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void do_charge_count_truncate(Calendar current) {
		try {
			Updator.dungeon_charge_count_update("tb_dungeon_time_account_information");
			Updator.dungeon_charge_count_update("tb_dungeon_time_char_information");
			MJIndexStampManager.update(MJEStampIndex.SIDX_DUNGEON_TIME_TRUNCATE, current);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void do_truncate() {
		Calendar cal = MJIndexStampManager.select(MJEStampIndex.SIDX_DUNGEON_TIME_TRUNCATE);
		Calendar current = RealTimeClock.getInstance().getRealTimeCalendar();
		if (cal == null ||
				cal.get(Calendar.YEAR) != current.get(Calendar.YEAR) ||
				cal.get(Calendar.MONTH) != current.get(Calendar.MONDAY) ||
				cal.get(Calendar.DAY_OF_MONTH) != current.get(Calendar.DAY_OF_MONTH)) {

			do_truncate(current);
		}
	}

	public void load() {
		do_truncate();
		DungeonTimeInformationLoader.getInstance();
		DungeonTimeUpdator.getInstance().run();
		DungeonTimePotionLoader.getInstance();
	}

	public void dispose() {
	}

	@Override
	public void execute(MJCommandArgs args) {
		_commands.execute(args, new StringBuilder(256).append(_commands.to_operation()));
	}

	private MJCommandTree createCommand() {
		return new MJCommandTree(".地下城計時器", "執行與地城計時器相關的命令。", null)
				.add_command(createReloadCommand())
				.add_command(createInitializeCommand())
				.add_command(createSchedulerCommand());
	}

	private MJCommandTree createReloadCommand() {
		return new MJCommandTree("重載", "執行與重載相關的命令。", null)
				.add_command(new MJCommandTree("信息", "", null) {
					@override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						DungeonTimeInformationLoader.reload();
						args.notify("已重載 tb_dungeon_time_information 表。");
					}
				})
				.add_command(new MJCommandTree("藥水", "", null) {
					@override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						DungeonTimePotionLoader.reload();
						args.notify("已重載 tb_dungeon_time_potion 表。");
					}
				});
	}

	private MJCommandTree createInitializeCommand() {
		return new MJCommandTree("初始化", "執行與初始化相關的命令。", null)
				.add_command(new MJCommandTree("角色", "初始化指定角色的地下城時間。", new String[] { "角色名稱" }) {
					@override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						String character_name = args.nextString();
						L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
						if (pc == null) {
							args.notify(String.format("找不到角色名稱 %s。", character_name));
							return;
						}
						DungeonTimeProgressLoader.update(pc);
						DungeonTimeProgressLoader.delete(pc);
						pc.initialize_dungeon_progress();
						pc.sendPackets("\f2使用者帳號的所有地下城時間已初始化。");
						args.notify(String.format("已初始化 %s 的所有地下城時間。", character_name));

						DungeonTimeProgressLoader.load(pc);
						DungeonTimeInformation dtInfo = DungeonTimeInformationLoader.getInstance()
								.from_map_id(pc.getMapId());
						if (dtInfo != null) {
							pc.send_dungeon_progress(dtInfo);
						}
					}
				})
				.add_command(new MJCommandTree("全部初始化", "初始化所有角色的地下城時間。", null) {
					@override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						DungeonTimeUpdator.getInstance()
								.do_initialize(RealTimeClock.getInstance().getRealTimeCalendar(), false);
						DungeonTimeUpdator.getInstance()
								.do_initialize(RealTimeClock.getInstance().getRealTimeCalendar(), true);
						args.notify("已完成全部初始化。");
					}
				});
	}

	private MJCommandTree createSchedulerCommand() {
		return new MJCommandTree("排程", "執行與排程相關的命令。", null)
				.add_command(new MJCommandTree("狀態", "顯示當前排程的狀態。", null) {
					@override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						args.notify(String.format("當前排程狀態：%s",
								DungeonTimeUpdator.getInstance().is_running() ? "開啟" : "關閉"));
					}
				})
				.add_command(new MJCommandTree("重新啟動", "重新啟動排程。", null) {
					@override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						DungeonTimeUpdator.getInstance().dispose();
						Thread.sleep(500L);
						DungeonTimeUpdator.getInstance().run();
						args.notify("排程已重新啟動。");
					}
				})
				.add_command(new MJCommandTree("停止", "停止排程。", null) {
					@override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						DungeonTimeUpdator.getInstance().dispose();
						args.notify("排程已停止。");
					}
				});
	}
}
