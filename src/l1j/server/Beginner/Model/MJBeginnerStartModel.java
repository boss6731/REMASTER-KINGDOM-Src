package l1j.server.Beginner.Model;

import l1j.server.Beginner.View.MJBeginnerStartView;
import l1j.server.server.model.Instance.L1PcInstance;

class MJBeginnerStartModel implements MJBeginnerModel<MJBeginnerStartView> {
	private MJBeginnerModelProvider provider;

	// 構造函數，接受一個模型提供程序作為參數
	MJBeginnerStartModel(MJBeginnerModelProvider provider) {
		this.provider = provider;
	}

	// 返回當前的模型提供程序
	protected MJBeginnerModelProvider provider() {
		return provider;
	}

	// 實現接口方法，處理開始視圖
	@override
	public void on(final MJBeginnerStartView view) {
		// 獲取任務數據，根據視圖中的任務ID
		MJBeginnerQuestData questData = provider.questDataModel().questData(view.questId());

		// 如果任務數據為空，標記視圖為過時
		if (questData == null) {
			view.onObsolete();
			return;
		}

		// 獲取玩家實例和用戶信息
		final L1PcInstance pc = view.pc();
		final MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(pc);

		// 如果用戶信息為空，標記視圖為過時
		if (user == null) {
			view.onObsolete();
			return;
		}

			// 設置用戶狀態監聽器
		user.state(view.questId(), new MJBeginnerUserStateListener() {
			@override
			public void onStarted(boolean completed) {
				view.onAlreadyStarted();
			}

			@override
			public void onFinished() {
				view.onAlreadyFinished();
			}

			@override
			public void onRevealed() {
				view.onFail();
			}

			@override
			public void onNotFound() {
				// 如果任務有前置條件，並且玩家不符合，則標記為失敗
				if (questData.hasPrerequisite() && !questData.prerequisite().matches(pc)) {
					view.onFail();
					return;
				}

				// 開始任務
				user.onStart(view.pc(), view.questId());

				// 如果任務有提前獎勵列表，則進行獎勵發放
				if (questData.hasAdvanceRewardList()) {
					questData.advanceRewardList().onReward(view.pc());
				}

				// 標記成功
				view.onSuccess();
			}
		});
	}
}

// 繼承自 MJBeginnerStartModel 類的跳過模式開始模型
static class MJBeginnerStartSkipModel extends MJBeginnerStartModel {

	// 構造函數，接受一個模型提供程序作為參數
	MJBeginnerStartSkipModel(MJBeginnerModelProvider provider) {
		super(provider);
	}

	// 重寫的 on 方法，處理開始視圖
	@override
	public void on(final MJBeginnerStartView view) {
		// 獲取任務數據，根據視圖中的任務ID
		MJBeginnerQuestData questData = provider().questDataModel().questData(view.questId());

		// 如果任務數據為空，標記視圖為過時
		if (questData == null) {
			view.onObsolete();
			return;
		}

		// 獲取玩家實例和用戶信息
		final L1PcInstance pc = view.pc();
		final MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(pc);

		// 如果用戶信息為空，標記視圖為過時
		if (user == null) {
			view.onObsolete();
			return;
		}

		// 設置用戶狀態監聽器
		user.state(view.questId(), new MJBeginnerUserStateListener() {
			@override
			public void onStarted(boolean completed) {
				view.onAlreadyStarted();
			}

			@override
			public void onFinished() {
				view.onAlreadyFinished();
			}

			@override
			public void onRevealed() {
				view.onFail();
			}

			@override
			public void onNotFound() {
				// 如果任務有前置條件，並且玩家不符合，則標記為失敗
				if (questData.hasPrerequisite() && !questData.prerequisite().matches(pc)) {
					view.onFail();
					return;
				}

				// 跳過任務並觸發更新通知
				MJBeginnerEventProvider.invokeUpdateNoti(view.pc(), user.onSkipped(pc, view.questId()).convertClientModel());
				view.onSuccess();
			}
		});
	}
}
