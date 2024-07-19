package l1j.server.Beginner.Model;

import l1j.server.Beginner.View.MJBeginnerRevealView;
import l1j.server.server.model.Instance.L1PcInstance;

class MJBeginnerRevealModel implements MJBeginnerModel<MJBeginnerRevealView> {
	private MJBeginnerModelProvider provider; // 提供模型的提供者

	// 构造函数，初始化提供者
	MJBeginnerRevealModel(MJBeginnerModelProvider provider) {
		this.provider = provider;
	}

	class MJBeginnerRevealModel implements MJBeginnerModel<MJBeginnerRevealView> {
		private MJBeginnerModelProvider provider; // 提供模型的提供者

		// 構造函數，初始化提供者
		MJBeginnerRevealModel(MJBeginnerModelProvider provider) {
			this.provider = provider;
		}

		// 重寫 on 方法，處理視圖邏輯
		@override
		public void on(final MJBeginnerRevealView view) {
			MJBeginnerQuestData questData = provider.questDataModel().questData(view.questId());
			if (questData == null) {
				view.onObsolete();
				return;
			}

			final L1PcInstance pc = view.pc();
			final MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(pc);
			if (user == null) {
				view.onObsolete();
				return;
			}

			// 定義用戶狀態監聽器並處理不同狀態
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
					view.onAlreadyRevealed();
				}

				@override
				public void onNotFound() {
					if (questData.hasPrerequisite() && !questData.prerequisite().matches(pc)) {
						view.onFail();
						return;
					}
					view.onSuccess();
				}
			});
		}
	}

	static class MJBeginnerRevealSkipModel extends MJBeginnerRevealModel {

		// 構造函數，調用父類構造函數來初始化提供者
		MJBeginnerRevealSkipModel(MJBeginnerModelProvider provider) {
			super(provider);
		}

		// 重寫 on 方法，調用父類的 on 方法
		@override
		public void on(final MJBeginnerRevealView view) {
			super.on(view);
		}
	}
