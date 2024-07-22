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
	// 定義開場消息的靜態數組
	private static final String[] OPENING_MESSAGES = new String[]{
			"特貝拉斯支配戰已經開始了。參加者請迅速入場。",
			"支配之塔已經開始了。參加者請迅速入場。",
			"被遺忘的島嶼已經開始了。參加者請迅速入場。"
	};

	// 實現 MJCommand 接口的 execute 方法
	public void execute(MJCommandArgs args) {
		try {
			// 根據命令參數執行不同的操作
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
					args.notify("mj_shiftserver.properties已重新加載。");
					break;
				case 9:
					cancel_server_battle_manage(args);
					break;
				default:
					throw new Exception(); // 如果命令無效，拋出異常
			}
		} catch (Exception e) {
			// 捕獲所有異常並通知用戶正確的命令格式
			args.notify(".伺服器遷移 [1.伺服器遷移信息][2.伺服器遷移]");
			args.notify(".伺服器遷移 [3.對抗戰信息][4.參加對抗戰]");
			args.notify(".伺服器遷移 [5.取消參加對抗戰][6.開設對抗戰]");
			args.notify(".伺服器遷移 [7.全體參加對抗戰]");
			args.notify(".伺服器遷移 [8.重新加載配置][9.取消對抗戰]");
		} finally {
			// 確保資源被正確釋放
			args.dispose();
		}
	}

	// 顯示傳輸信息

	private void show_transfer_info(MJCommandArgs args) {
		// 獲取當前可用的伺服器信息
		List<CommonServerInfo> servers = MJShiftObjectManager.getInstance().get_commons_servers(false);

		// 如果沒有可用的伺服器，則通知用戶並返回
		if (servers == null || servers.size() <= 0) {
			args.notify("目前沒有可遷移的伺服器信息。");
			return;
		}

		// 計數成功的伺服器數量
		int success_count = servers.size();

		// 遍歷每個伺服器信息
		for (CommonServerInfo csInfo : servers) {
			String message = "可遷移";

			// 檢查伺服器是否開啟
			if (!csInfo.server_is_on) {
				--success_count;
				message = "不可遷移(伺服器關閉)";
			}

			// 檢查伺服器是否允許遷移
			if (!csInfo.server_is_transfer) {
				--success_count;
				message = "不可遷移(功能關閉)";
			}

			// 通知用戶每個伺服器的狀態
			args.notify(String.format("- [%s] %s", csInfo.server_description, message));
		}

		// 如果沒有成功的伺服器，則通知用戶
		if (success_count <= 0) {
			args.notify("目前沒有可遷移的伺服器。");
		}

		return;
	}


	private void do_character_transfer(MJCommandArgs args) {
		try {
			// 取得角色名稱和伺服器標識
			String character_name = args.nextString();
			String server_identity = args.nextString();

			// 檢查角色名稱和伺服器標識是否為空
			if (l1j.server.MJTemplate.MJString.isNullOrEmpty(character_name) || l1j.server.MJTemplate.MJString.isNullOrEmpty(server_identity))
				throw new Exception();

			// 在當前世界中查找角色
			L1PcInstance pc = L1World.getInstance().findpc(character_name);
			if (pc == null) {
				args.notify(String.format("%s目前沒有連接到當前世界地圖。", character_name));
				return;
			}

			// 檢查是否正在進行對抗戰
			if (MJShiftObjectManager.getInstance().is_battle_server_running()) {
				args.notify("對抗戰進行中，無法使用伺服器遷移。");
				return;
			}

			try {
				// 執行伺服器遷移
				MJShiftObjectManager.getInstance().do_send(pc, MJEShiftObjectType.TRANSFER, server_identity, MJString.EmptyString);
				args.notify(String.format("%s已被遷移到%s伺服器。", character_name, server_identity));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			// 通知用戶正確的命令格式
			args.notify(".伺服器遷移 2 [角色名稱] [目標伺服器標識]");
		}
	}

	private void show_battle_info(MJCommandArgs args) {
		// 獲取當前正在進行對抗戰的伺服器信息
		List<CommonServerBattleInfo> servers = MJShiftObjectManager.getInstance().get_battle_servers_info();

		// 如果沒有伺服器在進行對抗戰，則通知用戶並返回
		if (servers == null || servers.size() <= 0) {
			args.notify("目前沒有正在進行對抗戰的伺服器信息。");
			return;
		}

		// 獲取當前參加的伺服器標識
		String enter_server_identity = MJShiftObjectManager.getInstance().get_battle_server_identity();

		// 計算正在進行對抗戰的伺服器數量
		int success_count = servers.size();

		// 遍歷每個伺服器的對抗戰信息
		for (CommonServerBattleInfo bInfo : servers) {
			// 獲取當前實時日曆
			Calendar start_cal = RealTimeClock.getInstance().getRealTimeCalendar();
			Calendar ended_cal = (Calendar)start_cal.clone();

			// 設置開始和結束時間
			start_cal.setTimeInMillis(bInfo.get_start_millis());
			ended_cal.setTimeInMillis(bInfo.get_ended_millis());

			// 構建伺服器對抗戰狀態信息
			String message = String.format("- [%s] 當前 %s %02d:%02d:%02d ~ %02d:%02d:%02d",
					bInfo.get_server_identity(),
					enter_server_identity.equals(bInfo.get_server_identity()) ? "參加中" : bInfo.is_ended() ? "已結束" : bInfo.is_run() ? "進行中" : "預定中",
					start_cal.get(Calendar.HOUR_OF_DAY), start_cal.get(Calendar.MINUTE), start_cal.get(Calendar.SECOND),
					ended_cal.get(Calendar.HOUR_OF_DAY), ended_cal.get(Calendar.MINUTE), ended_cal.get(Calendar.SECOND)
			);

			// 如果伺服器不在進行對抗戰，減少成功計數
			if (!bInfo.is_run())
				--success_count;

			// 通知用戶每個伺服器的對抗戰狀態
			args.notify(message);
		}

		// 如果沒有正在進行對抗戰的伺服器，則通知用戶
		if (success_count <= 0) {
			args.notify("目前沒有正在進行對抗戰的伺服器。");
		}

		return;
	}


	private void enter_servers_battle(MJCommandArgs args) {
		try {
			// 檢查當前是否已經參加了某個伺服器的對抗戰
			if (MJShiftObjectManager.getInstance().is_battle_server_enter()) {
				args.notify(String.format("目前已參加%s伺服器的對抗戰，無法參加新的對抗戰。", MJShiftObjectManager.getInstance().get_battle_server_identity()));
				return;
			}

			// 獲取目標伺服器的標識
			String server_identity = args.nextString();

			// 獲取目標伺服器的對抗戰信息
			CommonServerBattleInfo bInfo = MJShiftObjectHelper.get_battle_server_info(server_identity);

			// 檢查目標伺服器是否存在預定的對抗戰
			if (bInfo == null) {
				args.notify(String.format("%s沒有預定的對抗戰。", server_identity));
				return;
			}

			// 設置開場消息和進入類型
			String opening_message = MJString.EmptyString;
			int enter_type = 0;
			switch (bInfo.get_battle_name()) {
				case "特貝拉斯": // 特貝
					opening_message = OPENING_MESSAGES[0];
					enter_type = MJShiftBattleManager.ENTER_TYPE_THEBE;
					break;
				case "支配之塔": // 支配
					opening_message = OPENING_MESSAGES[1];
					enter_type = MJShiftBattleManager.ENTER_TYPE_DOMTOWER;
					break;
				case "被遺忘的島嶼": // 被遺忘的島嶼
					opening_message = OPENING_MESSAGES[2];
					enter_type = MJShiftBattleManager.ENTER_TYPE_FISLAND;
					break;
				default: // 未知類型
					args.notify(String.format("%s伺服器預定的對抗戰類型未知。", server_identity));
					return;
			}

			// 清除伺服器遷移數據並進入對抗戰伺服器
			MJShiftObjectHelper.truncate_shift_datas(MJShiftObjectManager.getInstance().get_home_server_identity(), true, true);
			MJShiftObjectManager.getInstance().do_enter_battle_server(bInfo, enter_type, bInfo.get_current_kind());

			// 通知用戶參加對抗戰
			args.notify(String.format("參加%s伺服器的對抗戰。", server_identity));

			// 向所有玩家廣播對抗戰的開場消息
			L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[]{
					new S_SystemMessage(opening_message),
					new S_PacketBox(S_PacketBox.GREEN_MESSAGE, opening_message),
			});

		} catch (MJCommandArgsIndexException e) {
			// 捕獲命令參數異常並通知用戶正確的命令格式
			args.notify(".伺服器遷移 4 [參加伺服器標識]");
		} catch (Exception e) {
			// 捕獲所有其他異常並打印堆棧跟蹤，通知用戶正確的命令格式
			e.printStackTrace();
			args.notify(".伺服器遷移 4 [參加伺服器標識]");
		}
	}

	private void cancel_server_battle(MJCommandArgs args) {
		// 檢查是否為自己開設的對抗戰
		if (MJShiftObjectManager.getInstance().is_my_battle_server()) {
			args.notify("不能取消自己開設的對抗戰。");
			return;
		}

		// 取消參加對抗戰
		MJShiftObjectManager.getInstance().do_cancel_battle_server();
		args.notify("已取消參加對抗戰。");

		// 刪除對抗戰伺服器信息
		MJShiftObjectHelper.delete_battle_server(MJShiftObjectManager.getInstance().get_home_server_identity());
	}

	private void cancel_server_battle_manage(MJCommandArgs args) {
		// 檢查對抗戰是否正在運行
		if (!MJShiftObjectManager.getInstance().is_battle_server_running()) {
			args.notify("對抗戰未在運行中。");
			return;
		}

		// 檢查是否為自己開設的對抗戰
		if (!MJShiftObjectManager.getInstance().is_my_battle_server()) {
			args.notify("不是自己的伺服器，無法取消對抗戰。");
			return;
		}

		// 取消參加對抗戰
		MJShiftObjectManager.getInstance().do_cancel_battle_server();
		args.notify("已取消參加對抗戰。");

		// 刪除對抗戰伺服器信息
		MJShiftObjectHelper.delete_battle_server(MJShiftObjectManager.getInstance().get_home_server_identity());
	}

	private void reservation_server_battle(MJCommandArgs args) {
		try {
			// 檢查當前是否已經參加了某個伺服器的對抗戰
			if (MJShiftObjectManager.getInstance().is_battle_server_enter()) {
				args.notify(String.format("目前已參加%s伺服器的對抗戰，無法註冊新的對抗戰。", MJShiftObjectManager.getInstance().get_battle_server_identity()));
				return;
			}

			// 獲取對抗戰持續時間（分鐘）
			int minute = args.nextInt();

			// 獲取對抗戰名稱
			String battle_name = args.nextString();

			// 判斷是否為本地伺服器
			boolean is_local_server = args.nextInt() == 1;

			// 初始化對抗戰管理器和白名單
			MJShiftBattlePlayManager<?> manager = null;
			MJShiftBattleItemWhiteList whitelist = null;

			// 計算當前時間和結束時間（毫秒）
			long current_millis = System.currentTimeMillis();
			long ended_millis = (minute * 60000) + current_millis;

			// 創建並設置對抗戰信息
			CommonServerBattleInfo bInfo = CommonServerBattleInfo.newInstance()
					.set_server_identity(MJShiftObjectManager.getInstance().get_home_server_identity())
					.set_start_millis(current_millis)
					.set_ended_millis(ended_millis - (MJShiftObjectManager.getInstance().get_my_server_battle_ready_seconds() * 1000))
					.set_battle_name(battle_name);

			// 設置對抗戰管理器、白名單、開場消息和種類
			int kind = 3;
			String opening_message = MJString.EmptyString;
			switch (battle_name) {
				case "特貝拉斯": // 特貝
					manager = new MJThebePlayManager(bInfo.get_ended_millis(), is_local_server);
					whitelist = new MJShiftBattleItemWhiteList(MJShiftBattleItemWhiteList.DBNAME_THEBES);
					opening_message = OPENING_MESSAGES[0];
					kind = 3;
					break;
				case "支配之塔": // 支配
					manager = new MJDomTowerPlayManager(bInfo.get_ended_millis(), is_local_server);
					whitelist = new MJShiftBattleItemWhiteList(MJShiftBattleItemWhiteList.DBNAME_DOMTOWER);
					opening_message = OPENING_MESSAGES[1];
					kind = 7;
					break;
				case "被遺忘的島嶼": // 被遺忘的島嶼
					manager = new MJFIslandPlayManager(bInfo.get_ended_millis(), is_local_server);
					whitelist = new MJShiftBattleItemWhiteList(MJShiftBattleItemWhiteList.DBNAME_FISLAND);
					opening_message = OPENING_MESSAGES[2];
					kind = 4;
					break;
				default:
					throw new Exception();
			}

			// 設定對抗戰的種類
			bInfo.set_current_kind(kind);

			// 如果不是本地伺服器，則預定伺服器對抗戰
			if (!is_local_server) {
				MJShiftObjectHelper.reservation_server_battle(
						MJShiftObjectManager.getInstance().get_home_server_identity(),
						current_millis,
						ended_millis - (MJShiftObjectManager.getInstance().get_my_server_battle_store_ready_seconds() * 1000),
						bInfo.get_current_kind(),
						bInfo.get_battle_name()
				);
			}

			// 計算並設置開始和結束時間
			Calendar start_cal = RealTimeClock.getInstance().getRealTimeCalendar();
			Calendar ended_cal = (Calendar)start_cal.clone();
			start_cal.setTimeInMillis(current_millis);
			ended_cal.setTimeInMillis(ended_millis);



			// 構建並通知伺服器對抗戰的開場消息
			String message = String.format("[伺服器對抗戰開設 %s] %02d:%02d:%02d ~ %02d:%02d:%02d", is_local_server ? "本地專用" : "對抗戰",
					start_cal.get(Calendar.HOUR_OF_DAY), start_cal.get(Calendar.MINUTE), start_cal.get(Calendar.SECOND),
					ended_cal.get(Calendar.HOUR_OF_DAY), ended_cal.get(Calendar.MINUTE), ended_cal.get(Calendar.SECOND)
			);
			args.notify(message);

			// 清除伺服器遷移數據並進入對抗戰伺服器
			MJShiftObjectHelper.truncate_shift_datas(MJShiftObjectManager.getInstance().get_home_server_identity(), true, true);
			MJShiftObjectManager.getInstance().do_enter_battle_server(bInfo, manager, whitelist, bInfo.get_current_kind());

			// 向所有玩家廣播對抗戰的開場消息
			L1World.getInstance().broadcastPacketToAll(new ServerBasePacket[]{
					new S_SystemMessage(opening_message),
					new S_PacketBox(S_PacketBox.GREEN_MESSAGE, opening_message),
			});

		} catch (MJCommandArgsIndexException e) {
			// 捕獲命令參數異常並通知用戶正確的命令格式
			args.notify(".伺服器對抗戰預定 [持續時間(分鐘)] [對抗戰名稱] [是否本地伺服器(1:是, 0:否)]");
			args.notify("對抗戰名稱 => 特貝拉斯, 支配之塔, 被遺忘的島嶼");
			args.notify("伺服器類型 => 1 = 本地, 0 = 對抗戰");
		} catch (Exception e) {
			// 捕獲所有其他異常並打印堆棧跟蹤，通知用戶正確的命令格式
			e.printStackTrace();
			args.notify(".伺服器對抗戰預定 [持續時間(分鐘)] [對抗戰名稱] [是否本地伺服器(1:是, 0:否)]");
			args.notify("對抗戰名稱 => 特貝拉斯, 支配之塔, 被遺忘的島嶼");
			args.notify("伺服器類型 => 1 = 本地, 0 = 對抗戰");
		}
	}

	private void enter_all_player_server_battle(MJCommandArgs args) {
		try {
			// 檢查是否有正在運行的對抗戰
			if (!MJShiftObjectManager.getInstance().is_battle_server_running()) {
				args.notify("目前沒有參加中的對抗戰。");
				return;
			}

			// 設定參數（這裡設置為 "12852"，具體含義需要根據具體需求來設置）
			String parameters = "12852";

			// 遍歷所有玩家並將其傳送到對抗戰伺服器
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				MJShiftObjectManager.getInstance().do_send_battle_server(pc, parameters);
			}

		} catch (Exception e) {
			// 捕獲所有異常並打印堆疊跟蹤，以便進行調試
			e.printStackTrace();
		}
	}




