package l1j.server.MJCombatSystem.Loader;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.MJCombatSystem.MJCombatEGameType;
import l1j.server.MJCombatSystem.MJCombatInformation;
import l1j.server.MJCombatSystem.MJCombatObserver;
import l1j.server.MJCombatSystem.Games.MJBattleFieldObserver;
import l1j.server.MJCombatSystem.Games.MJBattleTournamentObserver;
import l1j.server.MJCombatSystem.Games.MJBattleTowerObserver;
import l1j.server.MJTemplate.MJPropertyReader;
import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.Command.MJCommandTree;
import l1j.server.MJTemplate.Interface.MJSimpleListener;
import l1j.server.MJTemplate.Kda.TeamKda;
import l1j.server.MJTemplate.Kda.UserKda;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_NOTIFICATION_MESSAGE;
import l1j.server.MJTemplate.Reward.AbstractReward;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

public class MJCombatLoadManager implements MJCommand {
	private static MJCombatLoadManager _instance;

	public static MJCombatLoadManager getInstance() {
		if (_instance == null)
			_instance = new MJCombatLoadManager();
		return _instance;
	}

	public static void release() {
		if (_instance != null) {
			_instance.dispose();
			_instance = null;
		}
	}

	public static void reload() {
		MJCombatLoadManager old = _instance;
		_instance = new MJCombatLoadManager();
		if (old != null) {
			old.dispose();
			old = null;
		}
	}

	public static int COMBAT_TRAPS_SPAWN_SECOND;
	public static int COMBAT_TRAPS_SPAWN_PROB_BY_MILLION;
	public static int COMBAT_QUIT_DELAY_SECOND;

	public static MJSimpleRgb COMBAT_WINNER_MESSAGE_RGB;
	public static int COMBAT_WINNER_MESSAGE_DURATION;
	public static MJSimpleRgb COMBAT_LOSER_MESSAGE_RGB;
	public static int COMBAT_LOSER_MESSAGE_DURATION;
	public static MJSimpleRgb COMBAT_RANKER_MESSAGE_RGB;
	public static int COMBAT_RANKER_MESSAGE_DURATION;
	public static MJSimpleRgb COMBAT_DELAY_MESSAGE_RGB;
	public static int COMBAT_DELAY_MESSAGE_DURATION;

	public static int COMBAT_BT_LIFT_GFXID;
	public static int COMBAT_BT_CHEST_DROPITEM_AMOUNT;
	public static int COMBAT_BT_CHEST_DROP_RANGE;
	public static int COMBAT_BT_STAGE_PASS_SECOND;
	public static int COMBAT_BT_BOSS_SPAWN_SECOND;
	public static int COMBAT_BT_CLEAR_BONUS_AMOUNT;
	public static int COMBAT_BT_MONSTER_PACK;
	public static int COMBAT_BT_MASTER_DEATH_DELAY;
	public static int COMBAT_BT_CHANDLERY_SHOP_ID;

	private MJCommandTree _commands;
	private ConcurrentHashMap<Integer, MJCombatObserver> _current_observers;

	private MJCombatLoadManager() {
		_current_observers = new ConcurrentHashMap<Integer, MJCombatObserver>(8);
	}

	private MJCombatObserver create_observer(MJCombatEGameType g_type, MJCombatInformation c_info,
			AbstractReward[][] reward_2d) {
		if (g_type.equals(MJCombatEGameType.BATTLE_FIELD))
			return MJBattleFieldObserver.newInstance(c_info, reward_2d);
		else if (g_type.equals(MJCombatEGameType.BATTLE_TOWER))
			return MJBattleTowerObserver.newInstance(c_info, reward_2d);
		else if (g_type.equals(MJCombatEGameType.BATTLE_TOURNAMENT))
			return MJBattleTournamentObserver.newInstance(c_info, reward_2d);
		throw new IllegalArgumentException(String.format("無效的遊戲類型 %s", g_type.name()));
	}

	public MJCombatObserver create_observer(MJCombatEGameType g_type) {
		MJCombatInformation c_info = MJCombatInformationLoader.getInstance().select(g_type);
		AbstractReward[][] reward_2d = MJCombatRewardLoader.getInstance().get(g_type);
		MJCombatObserver observer = create_observer(g_type, c_info, reward_2d);
		_current_observers.put(observer.get_combat_info().get_combat_id(), observer);
		observer.add_quit_listener(new MJSimpleListener() {
			@Override
			public Object on(Object obj) {
				if (obj instanceof MJCombatObserver) {
					MJCombatObserver on_observer = (MJCombatObserver) obj;
					_current_observers.remove(on_observer.get_combat_info().get_combat_id());
				}
				return MJCombatLoadManager.this;
			}
		});
		return observer;
	}

	public MJCombatObserver execute_observer(MJCombatEGameType g_type) {
		if (g_type.equals(MJCombatEGameType.NONE))
			return null;

		MJCombatObserver observer = create_observer(g_type);
		return MJCombatObserver.execute(observer);
	}

	public MJCombatObserver get_current_observer(int combat_id) {
		return _current_observers.get(combat_id);
	}

	public void load() {
		_commands = createCommand();
		load_config();
		MJCombatRewardLoader.getInstance();
		MJCombatTrapsLoader.getInstance();
		MJCombatInformationLoader.getInstance();
	}

	private void load_config() {
		MJPropertyReader reader = null;
		try {
			reader = new MJPropertyReader("./config/mj_combat.properties");
			COMBAT_TRAPS_SPAWN_SECOND = reader.readInt("TrapsSpawnSecond", "60");
			COMBAT_TRAPS_SPAWN_PROB_BY_MILLION = reader.readInt("TrapsSpawnProbabilityByMillion", "10000");
			COMBAT_QUIT_DELAY_SECOND = reader.readInt("QuitDelaySecond", "15");

			COMBAT_WINNER_MESSAGE_DURATION = reader.readInt("WinnerMessageDuration", "10");
			COMBAT_WINNER_MESSAGE_RGB = MJSimpleRgb.fromRgb(
					reader.readInt("WinnerMessageColor_R", "50"),
					reader.readInt("WinnerMessageColor_G", "100"),
					reader.readInt("WinnerMessageColor_B", "50"));

			COMBAT_LOSER_MESSAGE_DURATION = reader.readInt("LoserMessageDuration", "10");
			COMBAT_LOSER_MESSAGE_RGB = MJSimpleRgb.fromRgb(
					reader.readInt("LoserMessageColor_R", "100"),
					reader.readInt("LoserMessageColor_G", "100"),
					reader.readInt("LoserMessageColor_B", "200"));

			COMBAT_RANKER_MESSAGE_DURATION = reader.readInt("RankerMessageDuration", "10");
			COMBAT_RANKER_MESSAGE_RGB = MJSimpleRgb.fromRgb(
					reader.readInt("RankerMessageColor_R", "200"),
					reader.readInt("RankerMessageColor_G", "100"),
					reader.readInt("RankerMessageColor_B", "100"));

			COMBAT_DELAY_MESSAGE_DURATION = reader.readInt("DelayMessageDuration", "10");
			COMBAT_DELAY_MESSAGE_RGB = MJSimpleRgb.fromRgb(
					reader.readInt("DelayMessageColor_R", "255"),
					reader.readInt("DelayMessageColor_G", "50"),
					reader.readInt("DelayMessageColor_B", "50"));

			COMBAT_BT_LIFT_GFXID = reader.readInt("BattleTournamentLiftGfxId", "15015");
			COMBAT_BT_CHEST_DROPITEM_AMOUNT = reader.readInt("BattleTournamentChestDropItemAmount", "4");
			COMBAT_BT_CHEST_DROP_RANGE = reader.readInt("BattleTournamentChestDropRange", "3");
			COMBAT_BT_STAGE_PASS_SECOND = reader.readInt("BattleTournamentStagePassSecond", "30");
			COMBAT_BT_BOSS_SPAWN_SECOND = reader.readInt("BattleTournamentBossSpawnSecond", "5");
			COMBAT_BT_CLEAR_BONUS_AMOUNT = reader.readInt("BattleTournamentClearBonusAmount", "20");
			COMBAT_BT_MONSTER_PACK = reader.readInt("BattleTournamentMonsterPack", "2");
			COMBAT_BT_MASTER_DEATH_DELAY = reader.readInt("BattleTournamentMasterDeathDelay", "10");
			COMBAT_BT_CHANDLERY_SHOP_ID = reader.readInt("BattleTournamentChandleryShopId", "200006");
		} catch (Exception e) {

		} finally {
			if (reader != null) {
				reader.dispose();
				reader = null;
			}
		}
	}

	public static ProtoOutputStream create_winner_message() {
		return create_result_message(true, COMBAT_WINNER_MESSAGE_RGB, COMBAT_WINNER_MESSAGE_DURATION);
	}

	public static ProtoOutputStream create_loser_message() {
		return create_result_message(false, COMBAT_LOSER_MESSAGE_RGB, COMBAT_LOSER_MESSAGE_DURATION);
	}

	public static ProtoOutputStream create_result_message(boolean is_winner, MJSimpleRgb rgb, int duration) {
		return create_result_message(String.format("比賽結束了。%s! %d秒後將移動到村莊。", is_winner ? "勝利" : "失敗", duration), rgb,
				duration);
	}

	public static ProtoOutputStream create_result_message(String s, MJSimpleRgb rgb, int duration) {
		return SC_NOTIFICATION_MESSAGE.make_stream(s, rgb, duration);
	}

	public static ProtoOutputStream[] create_result_notify(TeamKda winner_kda, UserKda[] ranker_kdas) {
		return new ProtoOutputStream[] {
				MJCombatLoadManager.create_winner_message(),
				MJCombatLoadManager.create_loser_message(),
				MJCombatLoadManager.create_result_message(
						String.format("比賽排名 - 殺手: %s (%d), 輸出: %s (%d), 坦克: %s (%d)",
								ranker_kdas[0] == null ? "null" : ranker_kdas[0].get_character_name(),
								ranker_kdas[0] == null ? 0 : ranker_kdas[0].get_kill(),
								ranker_kdas[1] == null ? "null" : ranker_kdas[1].get_character_name(),
								ranker_kdas[1] == null ? 0 : ranker_kdas[1].get_damage(),
								ranker_kdas[2] == null ? "null" : ranker_kdas[2].get_character_name(),
								ranker_kdas[2] == null ? 0 : ranker_kdas[2].get_tanking()),
						COMBAT_RANKER_MESSAGE_RGB, COMBAT_RANKER_MESSAGE_DURATION),
		};
	}

	public void dispose() {
	}

	@Override
	public void execute(MJCommandArgs args) {
		_commands.execute(args, new StringBuilder(256).append(_commands.to_operation()));
	}

	private MJCommandTree createCommand() {
		return new MJCommandTree("戰鬥系統", "戰鬥系統指令: [1.實例][2.重新載入]", null)
				.add_command(createViewCommand())
				.add_command(createReloadCommand());
	}

	private MJCommandTree createViewCommand() {
		return new MJCommandTree("1", "實例指令: [1.開啟清單][2.強制結束]", null)
				.add_command(new MJCommandTree("1", "", null) {
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						if (_current_observers.size() <= 0) {
							args.notify("沒有開啟的清單。");
							return;
						}
						for (MJCombatObserver observer : _current_observers.values())
							args.notify(observer.toString());
					}
				})
				.add_command(new MJCommandTree("2", "", new String[] { "戰場ID" }) {
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						int combat_id = args.nextInt();
						MJCombatObserver observer = _current_observers.get(combat_id);
						if (observer == null) {
							args.notify(String.format("無法找到當前開啟的戰場ID %d 的實例。", combat_id));
							return;
						}

						String msg = String.format("由管理員進行中的%s將在%d秒後強制結束。",
								observer.get_combat_info().get_game_type().to_kr_desc(), COMBAT_QUIT_DELAY_SECOND);
						ServerBasePacket[] message = new ServerBasePacket[] {
								new S_SystemMessage(msg),
								new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg)
						};

						L1World.getInstance().getAllPlayerStream()
								.filter((L1PcInstance pc) -> {
									return pc != null && pc.is_combat_field();
								})
								.forEach((L1PcInstance pc) -> {
									pc.sendPackets(message, false);
								});
						for (ServerBasePacket pack : message)
							pack.clear();
						try {
							observer.notify_quit();
							observer.dispose();
							args.notify("已強制結束執行中的戰場。");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	private MJCommandTree createReloadCommand() {
		return new MJCommandTree("2", "執行重新載入相關命令。[1.配置][2.資訊表][3.獎勵][4.陷阱]", null)
				.add_command(new MJCommandTree("1", "", null) {
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						load_config();
						args.notify("已重新載入 mj_combat.properties。");
					}
				})
				.add_command(new MJCommandTree("2", "", null) {
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						MJCombatInformationLoader.reload();
						args.notify("已重新載入 tb_combat_informations 表。");
					}
				})
				.add_command(new MJCommandTree("3", "", null) {
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						MJCombatRewardLoader.reload();
						args.notify("已重新載入 tb_combat_rewards 表。");
					}
				})
				.add_command(new MJCommandTree("4", "", null) {
					@Override
					protected void to_handle_command(MJCommandArgs args) throws Exception {
						MJCombatTrapsLoader.reload();
						args.notify("已重新載入 tb_combat_traps 表。");
					}
				});
	}
}