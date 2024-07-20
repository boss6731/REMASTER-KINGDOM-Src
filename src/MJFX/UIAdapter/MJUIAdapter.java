package MJFX.UIAdapter;

import MJFX.Logger.MJFxLogger;
import MJFX.MJFxEntry;
import l1j.server.swing.chocco;

public class MJUIAdapter {

	// 檢查是否在 MJFx 環境中運行
	private static boolean is_mjfx() {
		return (MJFxEntry.getInstance() != null);
	}

	// 處理添加交易聊天信息的方法
	public static void on_chat_trade_append(String s) {
		if (is_mjfx()) {
			// 目前這裡沒有任何操作，可以根據需求添加
		}
	}

	// 處理添加交易信息的方法
	public static void on_trade_append(String s) {
		if (is_mjfx()) {
			// 在 MJFx 環境中，更新交易日誌
			MJFxLogger.TRADE.update_log(s);
		} else {
			// 在非 MJFx 環境中，追加交易信息到相應的控件
			chocco.txtTrade.append(s);
		}
	}

	// 處理添加倉庫信息的方法
	public static void on_warehouse_append(String s) {
		if (is_mjfx()) {
			// 在 MJFx 環境中，更新倉庫日誌
			MJFxLogger.WAREHOUSE.update_log(s);
		} else {
			// 在非 MJFx 環境中，追加倉庫信息到相應的控件
			chocco.txtWarehouse.append(s);
		}
	}

	// 處理添加強化信息的方法
	public static void on_enchant_append(String s) {
		if (is_mjfx()) {
			// 在 MJFx 環境中，更新強化監控日誌
			MJFxLogger.ENCHANT_MONITOR.update_log(s);
		} else {
			// 在非 MJFx 環境中，追加強化信息到相應的控件
			chocco.txtEnchant.append(s);
		}
	}

	// 處理添加物品拾取信息的方法
	public static void on_item_append(String s) {
		if (is_mjfx()) {
			// 在 MJFx 環境中，更新物品日誌
			MJFxLogger.ITEM.update_log(s);
		} else {
			// 在非 MJFx 環境中，追加物品拾取信息到相應的控件
			chocco.txtPickup.append(s);
		}
	}

	// 處理建立帳號的方法
	public static void on_create_account(String account, String hostname) {
		if (is_mjfx()) {
			// 在 MJFx 環境中，記錄建立帳號信息
			MJFxLogger.ACCOUNT_CREATE.append_log(
					String.format("<建立帳號> %s -%s", new Object[]{ account, hostname })
			);
		}
	}



	public class MJUIAdapter {

		// 檢查是否在 MJFx 環境中運行
		private static boolean is_mjfx() {
			return (MJFxEntry.getInstance() != null);
		}

		// 處理 GM 指令的方法
		public static void on_gm_command(String s) {
			if (is_mjfx()) {
				MJFxLogger.GM_COMMAND.update_log(s);
			}
		}

		// 處理追加 GM 指令結果的方法
		public static void on_gm_command_append(String s) {
			if (is_mjfx()) {
				MJFxLogger.GM_COMMAND.append_log(String.format("<GM指令結果>%s", new Object[]{s}));
			} else {
				System.out.println(s);
			}
		}

		// 處理迷你遊戲信息的方法
		public static void on_minigame_append(String s) {
			if (is_mjfx()) {
				MJFxLogger.MINIGAME.append_log(s);
			}
		}

		// 處理老闆通知的方法
		public static void on_boss_append(int npc_id, String boss_name, int x, int y, int map_id) {
			if (is_mjfx()) {
				MJFxLogger.BOSS_TIMER.append_log(
						String.format("<老闆通知> %s(%d) %d,%d,%d", new Object[]{boss_name, npc_id, x, y, map_id})
				);
			}
		}

		// 清除所有聊天和日誌訊息的方法
		public static void on_flush() {
			if (!is_mjfx()) {
				chocco.txtGlobalChat.setText("");
				chocco.txtClanChat.setText("");
				chocco.txtPartyChat.setText("");
				chocco.txtWhisper.setText("");
				chocco.txtShop.setText("");
				chocco.txtTrade.setText("");
				chocco.txtWarehouse.setText("");
				chocco.txtEnchant.setText("");
				chocco.txtPickup.setText("");
			}
		}
	}


