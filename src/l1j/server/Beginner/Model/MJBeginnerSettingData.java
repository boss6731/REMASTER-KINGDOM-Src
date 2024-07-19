package l1j.server.Beginner.Model;

import java.util.HashSet;

import l1j.server.Beginner.View.MJBeginnerFinishedView;
import l1j.server.Beginner.View.MJBeginnerProgressView;
import l1j.server.Beginner.View.MJBeginnerRevealView;
import l1j.server.Beginner.View.MJBeginnerStartView;
import l1j.server.Beginner.View.MJBeginnerTeleportView;

class MJBeginnerSettingData {
	private static MJBeginnerSettingData settingHolder; // 靜態變數，用於保存設置實例

	// 靜態方法，用於改變設置實例
	static void changedSettingsHolder(MJBeginnerSettingData data) {
		settingHolder = data;
	}

	private boolean developMode; // 開發模式標誌
	private int mode; // 模式代碼
	private HashSet<Integer> skippedQuests; // 被跳過的任務
	private HashSet<Integer> hipassedQuests; // 被高級跳過的任務

	// 返回開發模式
	boolean developMode() {
		return developMode;
	}

	// 返回當前模式
	MJBeginnerMode mode() {
		return MJBeginnerMode.fromInt(mode);
	}

	// 返回當前模式的處理器
	MJBeginnerModeHandler modeHandler() {
		return mode().modeHandler();
	}

	// 返回被跳過的任務集合
	HashSet<Integer> skippedQuests() {
		return skippedQuests;
	}

	// 返回被高級跳過的任務集合
	HashSet<Integer> hipassedQuests() {
		return hipassedQuests;
	}

	// 判斷某個任務是否被跳過
	boolean skipped(int questId) {
		return skippedQuests().contains(questId);
	}

	// 判斷某個任務是否被高級跳過
	boolean hispassed(int questId) {
		return hipassedQuests().contains(questId);
	}


	public enum MJBeginnerMode {
		NORMAL(0, new MJBeginnerNormalModeHandler()), // 常規模式
		HIPASS(1, new MJBeginnerHiPassModeHandler()), // 高級跳過模式
		SKIP(2, new MJBeginnerSkipModeHandler());     // 跳過模式

		private int value; // 模式數值
		private MJBeginnerModeHandler modeHandler; // 模式處理器

		// 枚舉類的構造函數
		MJBeginnerMode(int value, MJBeginnerModeHandler modeHandler) {
			this.value = value;
			this.modeHandler = modeHandler;
		}

		// 獲取模式的整數值
		int toInt() {
			return value;
		}

		// 獲取模式的處理器
		MJBeginnerModeHandler modeHandler() {
			return modeHandler;
		}

		// 判斷當前模式是否等於給定模式
		boolean equals(MJBeginnerMode v) {
			return value == v.value;
		}

		// 根據整數值返回對應的模式
		static MJBeginnerMode fromInt(int i) {
			switch (i) {
				case 0:
					return NORMAL;
				case 1:
					return HIPASS;
				case 2:
					return SKIP;
				default:
					System.out.println(String.format("無效的 MJ新手模式 : %d", i));
					return null;
			}
		}
	}

	static abstract class MJBeginnerModeHandler {
		// 抽象方法，用於計算增加的步驟數量
		abstract int calculateAddedStepCount(int questId, int addedQuantity, int requiredQuantity);

		// 抽象方法，用於返回進度模型
		abstract MJBeginnerModel<MJBeginnerProgressView> progressModel(MJBeginnerModelProvider provider);

		// 抽象方法，用於返回揭示模型
		abstract MJBeginnerModel<MJBeginnerRevealView> revealModel(int questId, MJBeginnerModelProvider provider);

		// 抽象方法，用於返回開始模型
		abstract MJBeginnerModel<MJBeginnerStartView> startModel(int questId, MJBeginnerModelProvider provider);

		// 抽象方法，用於返回完成模型
		abstract MJBeginnerModel<MJBeginnerFinishedView> finishedModel(int questId, MJBeginnerModelProvider provider);

		// 抽象方法，用於返回傳送模型
		abstract MJBeginnerModel<MJBeginnerTeleportView> teleportModel(int questId, MJBeginnerModelProvider provider);
	}

	// 具體的初學者正常模式處理器實現
	private static class MJBeginnerNormalModeHandler extends MJBeginnerModeHandler {
		// 根據任務ID及其他參數計算增加的步驟數量
		@override
		int calculateAddedStepCount(int questId, int addedQuantity, int requiredQuantity) {
			// 如果任務被跳過，使用 SKIP 模式處理器計算
			if (settingHolder.skipped(questId)) {
				return MJBeginnerMode.SKIP.modeHandler().calculateAddedStepCount(questId, addedQuantity, requiredQuantity);
			}
			// 如果任務被高級跳過，使用 HIPASS 模式處理器計算
			else if (settingHolder.hispassed(questId)) {
				return MJBeginnerMode.HIPASS.modeHandler().calculateAddedStepCount(questId, addedQuantity, requiredQuantity);
			}
			// 否則返回增加的數量
			return addedQuantity;
		}

		// 返回進度模型
		@override
		MJBeginnerModel<MJBeginnerProgressView> progressModel(MJBeginnerModelProvider provider) {
			return new MJBeginnerProgressModel(provider);
		}

		// 其他方法的實現假設在此處繼續...
		@override
		MJBeginnerModel<MJBeginnerRevealView> revealModel(int questId, MJBeginnerModelProvider provider) {
			// 返回對應的揭示模型
			return new MJBeginnerRevealModel(provider);
		}

		@override
		MJBeginnerModel<MJBeginnerStartView> startModel(int questId, MJBeginnerModelProvider provider) {
			// 返回對應的開始模型
			return new MJBeginnerStartModel(provider);
		}

		@override
		MJBeginnerModel<MJBeginnerFinishedView> finishedModel(int questId, MJBeginnerModelProvider provider) {
			// 返回對應的完成模型
			return new MJBeginnerFinishedModel(provider);
		}

		@override
		MJBeginnerModel<MJBeginnerTeleportView> teleportModel(int questId, MJBeginnerModelProvider provider) {
			// 返回對應的傳送模型
			return new MJBeginnerTeleportModel(provider);
		}


		@override
		MJBeginnerModel<MJBeginnerRevealView> revealModel(int questId, MJBeginnerModelProvider provider) {
			// 檢查設置中是否指定該任務被跳過，如果是，則使用 SKIP 模式的處理器來產生模型
			if (settingHolder.skipped(questId)) {
				return MJBeginnerMode.SKIP.modeHandler().revealModel(questId, provider);
				// 檢查設置中是否指定該任務被高級跳過，如果是，則使用 HIPASS 模式的處理器來產生模型
			} else if (settingHolder.hispassed(questId)) {
				return MJBeginnerMode.HIPASS.modeHandler().revealModel(questId, provider);
			}
			// 如果任務沒有被跳過，則產生一個新的揭示模型實例
			return new MJBeginnerRevealModel(provider);
		}

		@override
		MJBeginnerModel<MJBeginnerStartView> startModel(int questId, MJBeginnerModelProvider provider) {
			// 如果任務被跳過，使用 SKIP 模式的處理器來產生開始模型
			if (settingHolder.skipped(questId)) {
				return MJBeginnerMode.SKIP.modeHandler().startModel(questId, provider);
				// 如果任務被高級跳過，則使用 HIPASS 模式的處理器來產生開始模型
			} else if (settingHolder.hispassed(questId)) {
				return MJBeginnerMode.HIPASS.modeHandler().startModel(questId, provider);
			}
			// 如果任務沒有被跳過，則產生一個新的開始模型實例
			return new MJBeginnerStartModel(provider);
		}

		@override
		MJBeginnerModel<MJBeginnerFinishedView> finishedModel(int questId, MJBeginnerModelProvider provider) {
			// 如果任務被跳過，則使用 SKIP 模式的處理器來產生完成模型
			if (settingHolder.skipped(questId)) {
				return MJBeginnerMode.SKIP.modeHandler().finishedModel(questId, provider);
				// 如果任務被高級跳過，則使用 HIPASS 模式的處理器來產生完成模型
			} else if (settingHolder.hispassed(questId)) {
				return MJBeginnerMode.HIPASS.modeHandler().finishedModel(questId, provider);
			}
			// 如果任務沒有被跳過，則產生一個新的完成模型實例
			return new MJBeginnerFinishModel(provider);
		}

		// 具體的初學者高級跳過模式處理器實現
		private static class MJBeginnerHiPassModeHandler extends MJBeginnerModeHandler {

			@override
			int calculateAddedStepCount(int questId, int addedQuantity, int requiredQuantity) {
				// 如果任務被跳過，使用 SKIP 模式處理器計算增加的步驟數量
				if (settingHolder.skipped(questId)) {
					return MJBeginnerMode.SKIP.modeHandler().calculateAddedStepCount(questId, addedQuantity, requiredQuantity);
				}
				// 否則返回所需的步驟數量
				return requiredQuantity;
			}

			@override
			MJBeginnerModel<MJBeginnerProgressView> progressModel(MJBeginnerModelProvider provider) {
			// 返回進度模型
				return new MJBeginnerProgressModel(provider);
			}

			@override
			MJBeginnerModel<MJBeginnerRevealView> revealModel(int questId, MJBeginnerModelProvider provider) {
				// 如果任務被跳過，使用 SKIP 模式處理器產生揭示模型
				if (settingHolder.skipped(questId)) {
					return MJBeginnerMode.SKIP.modeHandler().revealModel(questId, provider);
				}
				// 否則創建並返回一個新的揭示模型實例
				return new MJBeginnerRevealModel(provider);
			}

			@override
			MJBeginnerModel<MJBeginnerStartView> startModel(int questId, MJBeginnerModelProvider provider) {
				// 如果任務被跳過，使用 SKIP 模式處理器產生開始模型
				if (settingHolder.skipped(questId)) {
					return MJBeginnerMode.SKIP.modeHandler().startModel(questId, provider);
				}
				// 否則創建並返回一個新的開始模型實例
				return new MJBeginnerStartModel(provider);
			}

			@override
			MJBeginnerModel<MJBeginnerFinishedView> finishedModel(int questId, MJBeginnerModelProvider provider) {
				// 如果任務被跳過，使用 SKIP 模式處理器產生完成模型
				if (settingHolder.skipped(questId)) {
					return MJBeginnerMode.SKIP.modeHandler().finishedModel(questId, provider);
				}
				// 否則創建並返回一個新的完成模型實例
				return new MJBeginnerFinishModel(provider);
			}

			@override
			MJBeginnerModel<MJBeginnerTeleportView> teleportModel(int questId, MJBeginnerModelProvider provider) {
				// 如果任務被跳過，使用 SKIP 模式處理器產生傳送模型
				if (settingHolder.skipped(questId)) {
					return MJBeginnerMode.SKIP.modeHandler().teleportModel(questId, provider);
				}
				// 否則創建並返回一個新的傳送模型實例
				return new MJBeginnerTeleportModel(provider);
			}
		}

		// 具體的初學者跳過模式處理器實現
		private static class MJBeginnerSkipModeHandler extends MJBeginnerModeHandler {

			@override
			int calculateAddedStepCount(int questId, int addedQuantity, int requiredQuantity) {
				// 跳過模式直接返回所需的步驟數量
				return requiredQuantity;
			}

			@override
			MJBeginnerModel<MJBeginnerProgressView> progressModel(MJBeginnerModelProvider provider) {
				// 返回跳過模式的進度模型
				return new MJBeginnerProgressModel.MJBeginnerProgressSkipModel(provider);
			}

			@override
			MJBeginnerModel<MJBeginnerRevealView> revealModel(int questId, MJBeginnerModelProvider provider) {
				// 返回跳過模式的揭示模型
				return new MJBeginnerRevealModel.MJBeginnerRevealSkipModel(provider);
			}

			@override
			MJBeginnerModel<MJBeginnerStartView> startModel(int questId, MJBeginnerModelProvider provider) {
				// 返回跳過模式的開始模型
				return new MJBeginnerStartModel.MJBeginnerStartSkipModel(provider);
			}

			@override
			MJBeginnerModel<MJBeginnerFinishedView> finishedModel(int questId, MJBeginnerModelProvider provider) {
				// 返回跳過模式的完成模型
				return new MJBeginnerFinishModel.MJBeginnerFinishSkipModel(provider);
			}

			@override
			MJBeginnerModel<MJBeginnerTeleportView> teleportModel(int questId, MJBeginnerModelProvider provider) {
				// 返回跳過模式的傳送模型
				return new MJBeginnerTeleportModel.MJBeginnerTeleportSkipModel(provider);
			}
		}
