package l1j.server.Beginner.Model;

import l1j.server.Beginner.View.MJBeginnerTeleportView;
import l1j.server.server.model.Instance.L1PcInstance;

// 定義一個類，實現 MJBeginnerModel 接口，用於處理初學者傳送模型
class MJBeginnerTeleportModel implements MJBeginnerModel<MJBeginnerTeleportView> {
	private MJBeginnerModelProvider provider; // 保存模型提供者

	// 構造函數，接受模型提供者作為參數
	MJBeginnerTeleportModel(MJBeginnerModelProvider provider) {
		this.provider = provider;
	}

	// 重寫 on 方法，處理傳送視圖
	@override
	public void on(final MJBeginnerTeleportView view) {
		// 獲取任務數據
		MJBeginnerQuestData questData = provider.questDataModel().questData(view.questId());
		if (questData == null || !questData.hasTeleport()) {
			// 如果任務數據為空或沒有傳送功能，則返回錯誤位置
			view.onWrongLocation();
			return;
		}

		// 獲取玩家實例和用戶
		final L1PcInstance pc = view.pc();
		final MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(pc);
		if (user == null) {
			// 如果用戶為空，則返回錯誤位置
			view.onWrongLocation();
			return;
		}

		// 根據用戶狀態進行處理
		user.state(view.questId(), new MJBeginnerUserStateListener() {
			@override
			public void onStarted(boolean completed) {
				// 如果傳送成功，則返回成功消息，否則返回資源不足消息
				if (questData.teleport().onTeleport(pc)) {
					view.onSuccess();
				} else {
					view.onNotEnoughAdena();
				}
			}

			@override
			public void onFinished() {
				// 如果任務已完成，則返回無法傳送消息
				view.onCantTeleportNow();
			}

			@override
			public void onRevealed() {
				// 如果傳送成功，則返回成功消息，否則返回資源不足消息
				if (questData.teleport().onTeleport(pc)) {
					view.onSuccess();
				} else {
					view.onNotEnoughAdena();
				}
			}

			@override
			public void onNotFound() {
				// 如果找不到任務狀態，則返回失敗消息
				view.onFail();
			}
		});
	}

	// 定義一個靜態內部類，繼承自 MJBeginnerTeleportModel，處理初學者傳送跳過模型
	static class MJBeginnerTeleportSkipModel extends MJBeginnerTeleportModel {

		// 構造函數，接受模型提供者作為參數
		MJBeginnerTeleportSkipModel(MJBeginnerModelProvider provider) {
			// 調用父類的構造函數
			super(provider);
		}

		// 重寫 on 方法，處理傳送視圖
		@override
		public void on(final MJBeginnerTeleportView view) {
			// 調用父類的 on 方法
			super.on(view);
		}
	}
