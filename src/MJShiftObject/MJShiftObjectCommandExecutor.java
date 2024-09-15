package MJShiftObject;

import java.util.Calendar;
import java.util.List;

import MJShiftObject.Battle.MJShiftBattleItemWhiteList;
import MJShiftObject.Battle.MJShiftBattleManager;
import MJShiftObject.Battle.MJShiftBattlePlayManager;
import MJShiftObject.Battle.DomTower.MJDomTowerPlayManager;
import MJShiftObject.Battle.ForgottenIsland.MJFIslandPlayManager;
import MJShiftObject.Battle.Thebe.MJThebePlayManager;
import MJShiftObject.Template.CommonServerBattleInfo;
import MJShiftObject.Template.CommonServerInfo;
import l1j.server.MJTemplate.MJString;
import l1j.server.MJTemplate.Command.MJCommand;
import l1j.server.MJTemplate.Command.MJCommandArgs;
import l1j.server.MJTemplate.Exceptions.MJCommandArgsIndexException;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;

public class MJShiftObjectCommandExecutor implements MJCommand {
	private static final String[] OPENING_MESSAGES = new String[]{
			"泰貝拉斯支配戰開始了。參加者請迅速入場。",
			"支配之塔開始了。參加者請迅速入場。",
			"被遺忘的島嶼開始了。參加者請迅速入場。",
	};


	public void execute(MJCommandArgs args) {
		try {
			switch (args.nextInt()) {
				case 1:
					show_transfer_info(args);
					break;
				case 2:
					do_character_transfer(args);
					break;
				case 3:
					show_battle_info(args);
					break;
				case 4:
					enter_servers_battle(args);
					break;
				case 5:
					cancel_server_battle(args);
					break;
				case 6:
					reservation_server_battle(args);
					break;
				case 7:
					enter_all_player_server_battle(args);
					break;
				case 8:
					MJShiftObjectManager.getInstance().reload_config();
					args.notify("mj_shiftserver.properties 已重新載入。");
					break;
				case 9:
					cancel_server_battle_manage(args);
					break;
				default:
					throw new Exception();
			}
		} catch (Exception e) {
			args.notify(".伺服器移動 [1.伺服器轉移資訊][2.伺服器轉移]");
			args.notify(".伺服器移動 [3.對抗戰資訊][4.參加對抗戰]");
			args.notify(".伺服器移動 [5.取消對抗戰參加][6.開設對抗戰]");
			args.notify(".伺服器移動 [7.全體進入對抗戰]");
			args.notify(".伺服器移動 [8.重新載入配置][9.取消對抗戰遊戲]");
		} finally {
			args.dispose();
		}
	}

	private void show_transfer_info(MJCommandArgs args) {
		List<CommonServerInfo> servers = MJShiftObjectManager.getInstance().get_commons_servers(false);
		if (servers == null || servers.size() <= 0) {
			args.notify("目前沒有可轉移的伺服器資訊。");
			return;
		}
		int success_count = servers.size();
		for (CommonServerInfo csInfo : servers) {
			String message = "可轉移";
			if (!csInfo.server_is_on) {
				--success_count;
				message = "不可轉移(伺服器關閉)";
			}
			if (!csInfo.server_is_transfer) {
				--success_count;
				message = "不可轉移(功能關閉)";
			}
			args.notify(String.format("- [%s] %s", csInfo.server_description, message));
		}
		if (success_count <= 0) {
			args.notify("目前沒有可轉移的伺服器。");
		}
		return;
	}

	private void do_character_transfer(MJCommandArgs args) {
		try {
			String character_name = args.nextString();
			String server_identity = args.nextString();
			if (l1j.server.MJTemplate.MJString.isNullOrEmpty(character_name) || l1j.server.MJTemplate.MJString.isNullOrEmpty(server_identity))
				throw new Exception();

			L1PcInstance pc = L1World.getInstance().findpc(character_name);
			if (pc == null) {
				args.notify(String.format("%s 尚未連接到當前的世界地圖。", character_name));
				return;
			}
			if (MJShiftObjectManager.getInstance().is_battle_server_running()) {
				args.notify("對抗戰進行中無法使用伺服器轉移。");
				return;
			}

			try {
				MJShiftObjectManager.getInstance().do_send(pc, MJEShiftObjectType.TRANSFER, server_identity, MJString.EmptyString);
				args.notify(String.format("已將 %s 轉移到 %s 伺服器。", character_name, server_identity));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			args.notify(".伺服器移動 2 [角色名] [轉移伺服器識別名]");
		}
	}

	private void show_battle_info(MJCommandArgs args) {
		List<CommonServerBattleInfo> servers = MJShiftObjectManager.getInstance().get_battle_servers_info();
		if (servers == null || servers.size() <= 0) {
			args.notify("目前沒有進行中的對抗戰伺服器資訊。");
			return;
		}
		String enter_server_identity = MJShiftObjectManager.getInstance().get_battle_server_identity();
		int success_count = servers.size();
		for (CommonServerBattleInfo bInfo : servers) {
			Calendar start_cal = RealTimeClock.getInstance().getRealTimeCalendar();
			Calendar ended_cal = (Calendar) start_cal.clone();
			start_cal.setTimeInMillis(bInfo.get_start_millis());
			ended_cal.setTimeInMillis(bInfo.get_ended_millis());
			String message = String.format("- [%s] 當前 %s %02d:%02d:%02d ~ %02d:%02d:%02d",
					bInfo.get_server_identity(),
					enter_server_identity.equals(bInfo.get_server_identity()) ? "參加中" : bInfo.is_ended() ? "已結束" : bInfo.is_run() ? "進行中" : "預約中",
					start_cal.get(Calendar.HOUR_OF_DAY), start_cal.get(Calendar.MINUTE), start_cal.get(Calendar.SECOND),
					ended_cal.get(Calendar.HOUR_OF_DAY), ended_cal.get(Calendar.MINUTE), ended_cal.get(Calendar.SECOND)
			);
			if (!bInfo.is_run())
				--success_count;

			args.notify(message);
		}
		if (success_count <= 0) {
			args.notify("目前沒有進行中的對抗戰伺服器。");
		}
		return;
	}

	private void enter_servers_battle(MJCommandArgs args) {
		try {
			if (MJShiftObjectManager.getInstance().is_battle_server_enter()) {
				args.notify(String.format("目前參加了 %s 伺服器的對抗戰，無法參加新的對抗戰。", MJShiftObjectManager.getInstance().get_battle_server_identity()));
				return;
			}

			String server_identity = args.nextString();
			CommonServerBattleInfo bInfo = MJShiftObjectHelper.get_battle_server_info(server_identity);
			if (bInfo == null) {
				args.notify(String.format("在 %s 沒有預約的對抗戰。", server_identity));
				return;
			}
			String opening_message = MJString.EmptyString;
			int enter_type = 0;
			switch (bInfo.get_battle_name()) {
				case "泰貝拉斯支配戰":
					opening_message = OPENING_MESSAGES[0];
					enter_type = MJShiftBattleManager.ENTER_TYPE_THEBE;
					break;
				case "支配之塔":
					opening_message = OPENING_MESSAGES[1];
					enter_type = MJShiftBattleManager.ENTER_TYPE_DOMTOWER;
					break;
				case "被遺忘的島嶼":
					opening_message = OPENING_MESSAGES[2];

					enter_type = MJShiftBattleManager.ENTER_TYPE_FISLAND;
					break;
				default:
					args.notify(String.format("在 %s 預約的對抗戰類型未知。%s", server_identity, opening_message));
					break;
			}
			MJShiftObjectHelper.truncate_shift_datas(MJShiftObjectManager.getInstance().get_home_server_identity(), true, true);
			MJShiftObjectManager.getInstance().do_enter_battle_server(bInfo, enter_type, bInfo.get_current_kind());
			args.notify(String.format("參加 %s 對抗戰。", server_identity));
			L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[]{
					new S_SystemMessage(opening_message),
					new S_PacketBox(S_PacketBox.GREEN_MESSAGE, opening_message),
			});
		} catch (MJCommandArgsIndexException e) {
			args.notify(".伺服器移動 4 [參加伺服器識別名]");
		} catch (Exception e) {
			e.printStackTrace();
			args.notify(".伺服器移動 4 [參加伺服器識別名]");
		}
	}

	private void cancel_server_battle(MJCommandArgs args) {
		if (MJShiftObjectManager.getInstance().is_my_battle_server()) {
			args.notify("無法取消您直接創建的對抗戰。");
			return;
		}
		MJShiftObjectManager.getInstance().do_cancel_battle_server();
		args.notify("取消參加對抗戰。");
		MJShiftObjectHelper.delete_battle_server(MJShiftObjectManager.getInstance().get_home_server_identity());
	}

	private void cancel_server_battle_manage(MJCommandArgs args) {
		if (!MJShiftObjectManager.getInstance().is_battle_server_running()) {
			args.notify("對抗戰未在運行中。");
			return;
		}
		if (!MJShiftObjectManager.getInstance().is_my_battle_server()) {
			args.notify("不是我的伺服器無法取消遊戲。");
			return;
		}
		MJShiftObjectManager.getInstance().do_cancel_battle_server();
		args.notify("取消參加對抗戰。");
		MJShiftObjectHelper.delete_battle_server(MJShiftObjectManager.getInstance().get_home_server_identity());
	}

	private void reservation_server_battle(MJCommandArgs args) {
		try {
			if (MJShiftObjectManager.getInstance().is_battle_server_enter()) {
				args.notify(String.format("目前參加了 %s 伺服器的對抗戰，無法註冊新的對抗戰。", MJShiftObjectManager.getInstance().get_battle_server_identity()));
				return;
			}

			int minute = args.nextInt();
			String battle_name = args.nextString();
			boolean is_local_server = args.nextInt() == 1;
			MJShiftBattlePlayManager<?> manager = null;
			MJShiftBattleItemWhiteList whitelist = null;

			long current_millis = System.currentTimeMillis();
			long ended_millis = (minute * 60000) + current_millis;

			CommonServerBattleInfo bInfo =
					CommonServerBattleInfo.newInstance()
							.set_server_identity(MJShiftObjectManager.getInstance().get_home_server_identity())
							.set_start_millis(current_millis)
							.set_ended_millis(ended_millis - (MJShiftObjectManager.getInstance().get_my_server_battle_ready_seconds() * 1000))
							.set_battle_name(battle_name);

			int kind = 3;
			String opening_message = MJString.EmptyString;
			switch (battle_name) {
				case "泰貝拉斯支配戰":
					manager = new MJThebePlayManager(bInfo.get_ended_millis(), is_local_server);
					whitelist = new MJShiftBattleItemWhiteList(MJShiftBattleItemWhiteList.DBNAME_THEBES);
					opening_message = OPENING_MESSAGES[0];
					kind = 3;
					break;
				case "支配之塔":
					manager = new MJDomTowerPlayManager(bInfo.get_ended_millis(), is_local_server);
					whitelist = new MJShiftBattleItemWhiteList(MJShiftBattleItemWhiteList.DBNAME_DOMTOWER);
					opening_message = OPENING_MESSAGES[1];
					kind = 7;
					break;
				case "被遺忘的島嶼":
					manager = new MJFIslandPlayManager(bInfo.get_ended_millis(), is_local_server);
					whitelist = new MJShiftBattleItemWhiteList(MJShiftBattleItemWhiteList.DBNAME_FISLAND);
					opening_message = OPENING_MESSAGES[2];
					kind = 4;
					break;
				default:
					throw new Exception();
			}
			bInfo.set_current_kind(kind);
			if (!is_local_server) {
				MJShiftObjectHelper.reservation_server_battle(
						MJShiftObjectManager.getInstance().get_home_server_identity(),
						current_millis,
						ended_millis - (MJShiftObjectManager.getInstance().get_my_server_battle_store_ready_seconds() * 1000),
						bInfo.get_current_kind(),
						bInfo.get_battle_name()
				);
			}
			Calendar start_cal = RealTimeClock.getInstance().getRealTimeCalendar();
			Calendar ended_cal = (Calendar) start_cal.clone();
			start_cal.setTimeInMillis(current_millis);
			ended_cal.setTimeInMillis(ended_millis);
			String message = String.format("[伺服器對抗戰開設 %s] %02d:%02d:%02d ~ %02d:%02d:%02d", is_local_server ? "僅限本地" : "對抗戰",
					start_cal.get(Calendar.HOUR_OF_DAY), start_cal.get(Calendar.MINUTE), start_cal.get(Calendar.SECOND),
					ended_cal.get(Calendar.HOUR_OF_DAY), ended_cal.get(Calendar.MINUTE), ended_cal.get(Calendar.SECOND)
			);
			args.notify(message);
			MJShiftObjectHelper.truncate_shift_datas(MJShiftObjectManager.getInstance().get_home_server_identity(), true, true);
			MJShiftObjectManager.getInstance().do_enter_battle_server(bInfo, manager, whitelist, bInfo.get_current_kind());
			L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[]{
					new S_SystemMessage(opening_message),
					new S_PacketBox(S_PacketBox.GREEN_MESSAGE, opening_message),
			});
		} catch (Exception e) {
			args.notify(".伺服器移動 6 [對抗戰持續時間(分鐘)] [對抗戰類型] [伺服器類型]");
			args.notify("對抗戰類型 => 泰貝拉斯支配戰, 支配之塔, 被遺忘的島嶼");
			args.notify("伺服器類型 => 1=本地, 0=對抗戰");
		}

		private void enter_all_player_server_battle (MJCommandArgs args){
			try {
				if (!MJShiftObjectManager.getInstance().is_battle_server_running()) {
					args.notify("目前沒有參加中的對抗戰。");
					return;
				}
				String parameters = "12852";
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers())
					MJShiftObjectManager.getInstance().do_send_battle_server(pc, parameters);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}