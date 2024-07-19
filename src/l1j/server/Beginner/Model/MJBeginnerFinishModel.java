package l1j.server.Beginner.Model;

import java.util.List;

import l1j.server.Beginner.View.MJBeginnerFinishedView;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventProvider;

class MJBeginnerFinishModel implements MJBeginnerModel<MJBeginnerFinishedView> {
	private MJBeginnerModelProvider provider;

	// 構造函數，初始化provider
	MJBeginnerFinishModel(MJBeginnerModelProvider provider) {
		this.provider = provider;
	}

	// 覆蓋on方法，處理視圖的邏輯
	@override
	public void on(final MJBeginnerFinishedView view) {
		// 從provider中獲取questDataModel，根據view中的questId查詢任務數據
		final MJBeginnerQuestData questData = provider.questDataModel().questData(view.questId());

		// 如果questData為空，視圖調用onObsolete方法，並返回
		if (questData == null) {
			view.onObsolete();
			return;
		}

		// 這裡可以添加更多的邏輯來處理questData
	}


	// 將view的pc轉換為MJBeginnerUser對象
	MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(view.pc());
if (user == null) { // 如果user為null，表示轉換失敗，調用view的onFail方法並返回
		view.onFail();
		return;
	}

// 獲取用戶狀態，並添加狀態監聽器
user.state(view.questId(), new MJBeginnerUserStateListener() {
		@override
		public void onStarted(boolean completed) { // 當任務開始時觸發
			if (!completed) { // 如果任務未完成，調用view的onNotCompleted方法並返回
				view.onNotCompleted();
				return;
			}
			if (questData.hasOptionalRewardList()) { // 如果有選擇性獎勵列表
				List<Integer> optionalRewardIndexes = view.optionalRewardIndexes(); // 獲取選擇性獎勵索引列表
				if (optionalRewardIndexes == null || optionalRewardIndexes.size() <= 0) { // 如果索引列表為空或大小為0，調用view的onInvalidRewardIndexes方法並返回
					view.onInvalidRewardIndexes();
					return;
				}
				try {
					questData.optionalRewardList().rewards().get(optionalRewardIndexes.get(0)).onReward(view.pc()); // 根據索引獲取獎勵並發放給玩家
				} catch (Exception e) { // 捕獲異常，如果發生異常，調用view的onInvalidRewardIndexes方法並返回
					view.onInvalidRewardIndexes();
					return;
				}
			}

			if (questData.hasRewardList()) { // 如果有獎勵列表，發放獎勵給玩家
				questData.rewardList().onReward(view.pc());
			}
			user.onFinished(view.pc(), view.questId()); // 標記任務完成
			view.onSuccess(); // 調用view的onSuccess方法
			MJObjectEventProvider.provider().pcEventFactory().fireQuestFinished(view.pc(), view.questId()); // 觸發任務完成事件
		}
});

@override
public void onFinished() {
			// 如果任務已經完成，調用view的onAlreadyFinished方法
			view.onAlreadyFinished();
		}

@override
public void onRevealed() {
			// 如果任務已經被揭示但未完成，調用view的onNotCompleted方法
			view.onNotCompleted();
		}

		@override
		public void onNotFound() {
			// 如果找不到任務，調用view的onFail方法
			view.onFail();
		}
	});
}

		// 定義一個靜態內部類，繼承自MJBeginnerFinishModel
static class MJBeginnerFinishSkipModel extends MJBeginnerFinishModel {
		// 構造函數，調用父類的構造函數
		MJBeginnerFinishSkipModel(MJBeginnerModelProvider provider) {
			super(provider);
		}

		// 覆蓋on方法
		@override
public void on(final MJBeginnerFinishedView view) {
			// 直接調用view的onSuccess方法，表示任務成功完成
			view.onSuccess();
			// 觸發任務完成事件，通知系統該任務已完成
			MJObjectEventProvider.provider().pcEventFactory().fireQuestFinished(view.pc(), view.questId());
		}
	}
}
