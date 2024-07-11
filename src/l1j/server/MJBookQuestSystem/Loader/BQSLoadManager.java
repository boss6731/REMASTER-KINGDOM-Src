package l1j.server.MJBookQuestSystem.Loader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import l1j.server.MJBookQuestSystem.BQSInformation;
import l1j.server.MJBookQuestSystem.BQSWQUpdator;
import l1j.server.MJBookQuestSystem.UserSide.BQSCharacterCriteriaProgress;
import l1j.server.MJBookQuestSystem.UserSide.BQSCharacterData;
import l1j.server.MJTemplate.MJPropertyReader;
import l1j.server.MJTemplate.MJRnd;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.Command.MJCommandTree;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_CRITERIA_UPDATE_NOTI;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.matcher.Matcher;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.L1SpawnUtil;

public class BQSLoadManager implements MJCommand {
	private static BQSLoadManager _instance;

	public static BQSLoadManager getInstance() {
		if (_instance == null)
			_instance = new BQSLoadManager();
		return _instance;
	}

	public static int criteriaIdToAchievementId(int criteria_id, int achievement_level) {
		BQSIdInfo idInfo = BQSIdInfo.from_criteria_id(criteria_id, achievement_level);
		return idInfo == null ? 0 : idInfo.get_achievement_id();
	}

	public static int achievementIdToCriteriaId(int achievement_id) {
		BQSIdInfo idInfo = BQSIdInfo.from_achievement_id(achievement_id);
		return idInfo == null ? 0 : idInfo.get_criteria_id();
	}

	public static int achievementIdToAchievementLevel(int achievement_id) {
		BQSIdInfo idInfo = BQSIdInfo.from_achievement_id(achievement_id);
		return idInfo == null ? 0 : idInfo.get_achievement__level();
	}

	public static void loadBqsUpdateCalendar() {
		Selector.exec("select * from tb_mbook_wq_startup", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				if (rs.next()) {
					Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
					BQS_UPDATE_CALENDAR = (Calendar) cal.clone();
					BQS_UPDATE_CALENDAR.setTimeInMillis(rs.getTimestamp("update_info").getTime());
					BQS_UPDATE_CALENDAR.set(Calendar.HOUR_OF_DAY, BQS_UPDATE_STDHOUR);
					int diff_hours = (((int) (cal.getTimeInMillis() - BQS_UPDATE_CALENDAR.getTimeInMillis()) / 1000)
							/ 3600);
					if (diff_hours > BQS_UPDATE_HOURS)
						updateBqsUpdateCalendar();
					cal.clear();
				} else {
					updateBqsUpdateCalendar();
				}
			}
		});
	}

	public static void updateBqsUpdateCalendar() {
		Calendar oldCal = BQS_UPDATE_CALENDAR;
		BQS_UPDATE_CALENDAR = RealTimeClock.getInstance().getRealTimeCalendar();
		BQS_UPDATE_CALENDAR.set(Calendar.HOUR_OF_DAY, BQS_UPDATE_STDHOUR);
		BQS_UPDATE_CALENDAR.set(Calendar.MINUTE, 0);
		BQS_UPDATE_CALENDAR.set(Calendar.SECOND, 0);
		Updator.exec("insert into tb_mbook_wq_startup set id=1, update_info=? on duplicate key update update_info=?",
				new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						Timestamp ts = new Timestamp(BQS_UPDATE_CALENDAR.getTimeInMillis());
						pstm.setTimestamp(1, ts);
						pstm.setTimestamp(2, ts);
					}
				});
		if (oldCal != null) {
			oldCal.clear();
			oldCal = null;
		}
		BQSWQDecksLoader.getInstance().updateDecks();
	}

	public static void truncateBqsUpdateCalendar() {
		Updator.exec("truncate table tb_mbook_wq_startup", new Handler() {
			public void handle(PreparedStatement pstm) throws Exception {
			}
		});
		updateBqsUpdateCalendar();
	}

	public static int BQS_WQ_WIDTH;
	public static int BQS_WQ_HEIGHT;
	public static int BQS_WQ_MINLEVEL;
	public static int BQS_WQ_STD_EXP;
	public static int BQS_UPDATE_STDHOUR;
	public static int BQS_UPDATE_HOURS;
	public static int BQS_UPDATE_TYPE;
	public static Calendar BQS_UPDATE_CALENDAR;
	public static boolean BQS_IS_ONUPDATE_BOOKS;
	private MJCommandTree _commands;

	private BQSLoadManager() {
		_commands = createCommand();
	}

	public void run() {
		loadConfig();
		BQSIdInfo.do_load();
		BQSInformationLoader.getInstance();
		BQSRewardsLoader.getInstance();
		BQSWQInformationLoader.getInstance();
		BQSWQRewardsLoader.getInstance();
		BQSWQDecksLoader.getInstance();
		loadBqsUpdateCalendar();
		BQSWQUpdator.getInstance().run();
	}

	private void loadConfig() {
		MJPropertyReader reader = null;
		try {
			reader = new MJPropertyReader("./config/mj_bookquestsystem.properties");
			BQS_UPDATE_STDHOUR = reader.readInt("UpdateStandardHour", "9");
			BQS_UPDATE_HOURS = reader.readInt("UpdateHours", "24");
			BQS_UPDATE_TYPE = reader.readInt("UpdateType", "11");
			BQS_WQ_WIDTH = reader.readInt("WeekQuest_Width", "3");
			BQS_WQ_HEIGHT = reader.readInt("WeekQuest_Height", "3");
			BQS_WQ_MINLEVEL = reader.readInt("WeekQuest_MinLevel", "56");
			BQS_WQ_STD_EXP = reader.readInt("WeekQuest_StandardExp", "721306");
			BQS_IS_ONUPDATE_BOOKS = reader.readBoolean("IsOnUpdateBooks", "true");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.dispose();
				reader = null;
			}
		}
	}

	private MJCommandTree createCommand() {
		return new MJCommandTree(".書籍系統", "執行與怪物圖鑑 / 每週任務相關的命令。", null)
				.add_command(createCriteriaCommand())
				.add_command(createV2Command())
				.add_command(createSpawnCommand())
				.add_command(createCharacterUpdateCommand())
				.add_command(createReloadConfigCommand())
				.add_command(createInitializedCommand())
				.add_command(createSettingsCommand());
	}

	private MJCommandTree createCriteriaCommand(){
		return new MJCommandTree("圖鑑", "執行與怪物書相關的命令。", null)
			.add_command(new MJCommandTree("重新加載", "重新加載怪物圖鑑內的各種數據。", null)
				.add_command(new MJCommandTree("獎勵", "", null){
					@override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						BQSRewardsLoader.reload();
						args.notify("已重新加載 tb_mbook_reward_info/tb_mbook_reward_items 表。");
					}
				})
				.add_command(new MJCommandTree("圖鑑信息", "", null){
					@override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						BQSInformationLoader.reload();
						args.notify("已重新加載 tb_mbook_information 表。");
					}
				})
			);
	}
	.add_command(new MJCommandTree("角色重置", "根據提供的角色名稱重置該角色的怪物書。", new String[]{"角色名稱"}){

	@override
		protected void to_handle_command(MJCommandArgs args) throws Exception {
		String character_name = args.nextString();
		L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
		L1PcInstance gm = args.getOwner();
		if (pc == null) {
		args.notify("找不到角色名稱 %s。");
		return;
		}
				GeneralThreadPool.getInstance().execute(new Runnable(){
					@Override
					public void run(){
						try {
						BQSCharacterData bqs = pc.getBqs();
						for (BQSCharacterCriteriaProgress progress : bqs.get_progresses().values()) {
						progress.set_current_ahcievement_level(0);
						progress.get_progress().set_quantity(0L);
						pc.sendPackets(SC_CRITERIA_UPDATE_NOTI.newInstance(progress.get_progress()), MJEProtoMessages.SC_CRITERIA_UPDATE_NOTI, true);
						}
						bqs.get_progresses().clear();
						bqs.get_achievements().clear();
						BQSCharacterDataLoader.storeCharacterBqs(pc.getBqs(), false);
						pc.sendPackets("由管理員重置了圖鑑信息。怪物已更新，但為了獲取獎勵，請重新登錄。");
						gm.sendPackets(String.format("已重置 %s 的怪物書信息。", pc.getName()));
						} catch (Exception e) {
						e.printStackTrace();
					}
				}
						});
				}
						
						.add_command(new MJCommandTree("全部重置", "重置當前在線的所有角色的圖鑑。", null) {

	protected void to_handle_command(MJCommandArgs args) throws Exception {
		L1PcInstance gm = args.getOwner();
		GeneralThreadPool.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				BQS_IS_ONUPDATE_BOOKS = false;
				S_SystemMessage message = new S_SystemMessage("由管理員重置了怪物圖鑑信息。怪物已更新，但為了獲取獎勵，請重新登錄。");
				int amount = 0;
				try {
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						if (pc == null)
							continue;

						BQSCharacterData bqs = pc.getBqs();
						if (bqs == null || bqs.get_progresses() == null)
							continue;

						++amount;
						for (BQSCharacterCriteriaProgress progress : bqs.get_progresses().values()) {
							progress.set_current_ahcievement_level(0);
							progress.get_progress().set_quantity(0L);
							pc.sendPackets(SC_CRITERIA_UPDATE_NOTI.newInstance(progress.get_progress()),
									MJEProtoMessages.SC_CRITERIA_UPDATE_NOTI, true);
						}
						bqs.get_progresses().clear();
						bqs.get_achievements().clear();
						BQSCharacterDataLoader.storeCharacterBqs(pc.getBqs(), false);
						pc.sendPackets(message, false);
					}
					gm.sendPackets(String.format("已重置 %d 名角色的圖鑑信息。", amount));
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					message.clear();
					BQS_IS_ONUPDATE_BOOKS = true;
				}
			}
		});
	}

	});}

	private MJCommandTree createV2Command() {
		return new MJCommandTree("週任務", "執行與週任務相關的命令。", null)
				.add_command(new MJCommandTree("重新加載", "重新加載週任務內的各種數據。", null)
						.add_command(new MJCommandTree("獎勵", "", null) {
							@override
							protected void to_handle_command(MJCommandArgs args) throws Exception {
								BQSWQRewardsLoader.reload();
								args.notify("已重新加載 tb_mbook_wq_reward_info/tb_mbook_wq_reward_items 表。");
							}
						})
						.add_command(new MJCommandTree("週任務信息", "", null) {
							@override
							protected void to_handle_command(MJCommandArgs args) throws Exception {
								BQSWQInformationLoader.reload();
								args.notify("已重新加載 tb_mbook_information 表。");
							}
						}))
				.add_command(new MJCommandTree("角色重置", "根據提供的角色名稱重置該角色的週任務。", new String[] { "角色名稱" }) {
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						String character_name = args.nextString();
						L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
						L1PcInstance gm = args.getOwner();
						if (pc == null) {
							args.notify("找不到角色名稱 %s。");
							return;
						}
						GeneralThreadPool.getInstance().execute(new Runnable() {
							@override
							public void run() {
								try {
									BQSCharacterData bqs = pc.getBqs();
									bqs
											.set_decks_version(0L)
											.realloc_decks(pc)
											.send_decks_noti(pc);
									pc.sendPackets("由管理員重置了週任務信息。");
									gm.sendPackets(String.format("已重置 %s 的怪物書信息。", pc.getName()));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					}
				})
				.add_command(new MJCommandTree("全部重置", "重置當前在線的所有角色的週任務。", null) {
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						L1PcInstance gm = args.getOwner();
						GeneralThreadPool.getInstance().execute(new Runnable() {
							@Override
							public void run() {
								gm.sendPackets(String.format("%d 個角色的每週任務已重設。",
										BQSWQDecksLoader.getInstance().notifyUpdated()));
							}
						});
					}
				});
	}

	private MJCommandTree createSpawnCommand() {
		return new MJCommandTree("召喚", "根據圖鑑ID召喚怪物。", new String[] { "圖鑑ID", "數量" }) {
			@override
			protected void to_handle_command(MJCommandArgs args) throws Exception {
				int criteria_id = args.nextInt();
				int count = args.nextInt();
				BQSInformation bqsInfo = BQSInformationLoader.getInstance().getInformation(criteria_id);
				if (bqsInfo == null) {
					args.notify(String.format("找不到圖鑑號 %d。", criteria_id));
					return;
				}

				List<L1Npc> npcs = BQSCriteriaNpcMappedService.service().findNpcFromCriteria(bqsInfo.getCriteriaId());
				if (npcs == null || npcs.size() <= 0) {
					args.notify(String.format("找不到符合圖鑑號 %d 的npc。", criteria_id));
					return;
				}

				for (int i = count - 1; i >= 0; --i) {
					L1SpawnUtil.spawn(args.getOwner(), npcs.get(MJRnd.next(npcs.size())).get_npcId(), 0, 0);
				}
				args.notify(String.format("召喚了 %d 隻 %s(%d)。", count, bqsInfo.getNpcName(), criteria_id));
			}
		};
	}

	private MJCommandTree createCharacterUpdateCommand() {
		return new MJCommandTree("角色更新", "根據提供的圖鑑ID更新指定角色的圖鑑信息。", new String[] { "角色名稱", "圖鑑ID", "更新數量" }) {
			@override
			protected void to_handle_command(MJCommandArgs args) throws Exception {
				String character_name = args.nextString();
				int criteria_id = args.nextInt();
				int count = args.nextInt();
				L1PcInstance gm = args.getOwner();
				L1PcInstance pc = L1World.getInstance().getPlayer(character_name);
				if (pc == null) {
					args.notify("找不到角色名稱 %s。");
					return;
				}

				BQSInformation bqsInfo = BQSInformationLoader.getInstance().getInformation(criteria_id);
				if (bqsInfo == null) {
					args.notify(String.format("找不到圖鑑號 %d。", criteria_id));
					return;
				}

				GeneralThreadPool.getInstance().execute(new Runnable() {
					@Override
					public void run() {
						BQSCharacterData bqs = pc.getBqs();
						if (bqs == null)
							return;
						try {
							for (int i = count - 1; i >= 0; --i) {
								bqs.onUpdate(bqsInfo);
								Thread.sleep(100L);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							if (gm != null)
								gm.sendPackets(
										String.format("已將角色 %s 的圖鑑編號 %d 增加了 %d。", character_name, criteria_id, count));
						}
					}
				});
			}
		};
	}

	private MJCommandTree createReloadConfigCommand() {
		return new MJCommandTree("重新加載配置", "重新加載圖鑑系統使用的配置。", null) {
			@override
			protected void to_handle_command(MJCommandArgs args) throws Exception {
				loadConfig();
				BQSWQUpdator.getInstance().update_listener();
				args.notify("已重新加載 mj_bookquestsystem.properties。");
			}
		};
	}

	private MJCommandTree createInitializedCommand() {
		return new MJCommandTree("系統重置", "所有圖鑑信息將被重置。", null) {
			@override
			protected void to_handle_command(MJCommandArgs args) throws Exception {
				try {
					L1World.getInstance()
							.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "由於系統更新，圖鑑系統將暫時停用。"));
					loadConfig();
					BQS_IS_ONUPDATE_BOOKS = false;
					BQSIdInfo.do_load();
					BQSInformationLoader.reload();
					BQSRewardsLoader.reload();
					BQSWQInformationLoader.reload();
					BQSWQRewardsLoader.reload();
					BQSWQDecksLoader.truncate();
					BQSCharacterDataLoader.deleteCharactersBps(L1World.getInstance().getAllPlayers());
					BQSWQDecksLoader.reload();
					truncateBqsUpdateCalendar();
					BQSWQUpdator.getInstance().update_listener();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					BQS_IS_ONUPDATE_BOOKS = true;
					L1World.getInstance().broadcastPacketToAll(
							new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "圖鑑系統已重新啟用。為了順利進行遊戲，請重新啟動遊戲。"));
				}
			}
		};
	}

	private MJCommandTree createSettingsCommand() {
		return new MJCommandTree("設置", "將數據庫與 mj-tools... 同步。", null) {
			@override
			protected void to_handle_command(MJCommandArgs args) throws Exception {
				try {
					args.notify("正在同步 nature-achievement-criteria...");
					System.out.println("正在同步 nature-achievement-criteria...");
					settingsAchievementCriteriaCommand(args);
					args.notify("正在同步 nature-achievement_teleport...");
					System.out.println("正在同步 nature-achievement_teleport...");
					settingsAchievementTeleportCommand(args);
					args.notify("數據庫同步完成。");
					System.out.println("數據庫同步完成。");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

	private static void settingsAchievementTeleportCommand(MJCommandArgs args) {
		Updator.exec(
				"update tb_mbook_information set tel_mapId=?, tel_x=?, tel_y=?, tel_need_item_id=?, tel_need_item_amount=?",
				new Handler() {
					@Override
					public void handle(PreparedStatement pstm) throws Exception {
						int idx = 0;
						pstm.setInt(++idx, 0);
						pstm.setInt(++idx, 0);
						pstm.setInt(++idx, 0);
						pstm.setInt(++idx, 0);
						pstm.setInt(++idx, 0);
					}
				});

		Selector.exec("select * from `nature-achievement_teleport` order by achievement_id", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					final int achievementId = rs.getInt("achievement_id");
					final int mapId = rs.getInt("teleport_map_id");
					final int left = rs.getInt("teleport_left");
					final int top = rs.getInt("teleport_top");
					final int right = rs.getInt("teleport_right");
					final int bottom = rs.getInt("teleport_bottom");
					final int costDescId = rs.getInt("cost_desc_id");
					final int costAmount = rs.getInt("cost_amount");
					final int criteriaId = achievementIdToCriteriaId(achievementId);
					final String blessed = rs.getString("cost_bless_code");
					final int costBlessCode = convertBookTeleportCostBless(blessed);
					if (criteriaId <= 0) {
						System.out.println(String.format("找不到對應 %d 的 criteriaId。(settingsAchievementTeleportCommand)",
								achievementId));
						continue;
					}

					Updator.exec(
							"update tb_mbook_information set tel_mapId=?, tel_x=?, tel_y=?, tel_need_item_id=?, tel_need_item_amount=? where criteria_id=?",
							new Handler() {
								@Override
								public void handle(PreparedStatement pstm) throws Exception {
									L1Item item = ItemTable.getInstance().findItem(new Matcher<L1Item>() {
										@Override
										public boolean matches(L1Item t) {
											return t.getItemDescId() == costDescId && t.getBless() == costBlessCode;
										}
									});
									int needItemId = 140100;
									if (item == null) {
										System.out.println(String.format(
												"找不到符合 %d(%d) 的消耗道具。將以祝福卷軸強制替代。(settingsAchievementTeleportCommand)",
												achievementId, costDescId));
									} else {
										needItemId = item.getItemId();
									}
									int idx = 0;
									pstm.setInt(++idx, mapId);
									pstm.setInt(++idx, convertBookTeleportCenter(left, right));
									pstm.setInt(++idx, convertBookTeleportCenter(top, bottom));
									pstm.setInt(++idx, needItemId);
									pstm.setInt(++idx, costAmount);
									pstm.setInt(++idx, criteriaId);
								}
							});
				}
			}
		});
	}

	private static void settingsAchievementCriteriaCommand(MJCommandArgs args) {
		final HashMap<Integer, Integer> map = new HashMap<>();
		Selector.exec("select * from `nature-achievement-criteria` order by achievement_id", new FullSelectorHandler() {
			@Override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					int achievementId = rs.getInt("achievement_id");
					final int quantity = rs.getInt("required_quantity");
					final int criteriaId = achievementIdToCriteriaId(achievementId);
					if (criteriaId <= 0) {
						System.out.println(String.format("找不到對應 %d 的 criteriaId。", achievementId));
						continue;
					}

					int step = map.getOrDefault(criteriaId, 1);
					if (step >= 1 && step <= 3) {
						final String column = convertBookStepColumn(step);
						Updator.exec(String.format("update tb_mbook_information set %s=? where criteria_id=?", column),
								new Handler() {
									@Override
									public void handle(PreparedStatement pstm) throws Exception {
										pstm.setInt(1, quantity);
										pstm.setInt(2, criteriaId);
									}
								});
						map.put(criteriaId, step + 1);
					} else {
						System.out.println(String.format("criteria 資料 %d 太多。(總計：%d)", achievementId, step));
						continue;
					}
				}
			}
		});
	}

	private static int convertBookTeleportCostBless(String blessed) {
		switch (blessed) {
			case "BLESSED":
				return 0;
			case "NORMAL":
				return 1;
			case "CURSED":
				return 2;
		}
		return 0;
	}

	private static int convertBookTeleportCenter(int n, int n2) {
		int min = Math.min(n, n2);
		int max = Math.max(n, n2);
		return min == max ? min : min + ((max - min) / 2);
	}

	private static String convertBookStepColumn(int step) {
		switch (step) {
			case 1:
				return "book_step_first";
			case 2:
				return "book_step_second";
		}
		return "book_step_third";
	}

	@Override
	public void execute(MJCommandArgs args) {
		_commands.execute(args, new StringBuilder(256).append(_commands.to_operation()));
	}
}
