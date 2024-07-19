package l1j.server.Beginner.Model;

import l1j.server.Beginner.View.MJBeginnerProgressView;

class MJBeginnerProgressModel implements MJBeginnerModel<MJBeginnerProgressView> {
	// 定義一個私有變量provider
	private MJBeginnerModelProvider provider;

	// 構造函數，初始化provider
	MJBeginnerProgressModel(MJBeginnerModelProvider provider) {
		this.provider = provider;
	}

	// 覆蓋on方法，處理視圖的邏輯
	@override
	public void on(final MJBeginnerProgressView view) {
		// 將view的pc轉換為MJBeginnerUser對象
		MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(view.pc());
		if (user != null) { // 如果user不為null，表示用戶已存在，直接返回
			return;
		}

		// 如果user為null，創建一個新的MJBeginnerUser對象
		user = MJBeginnerUserProvider.provider().onNewUser(view.pc());
		MJBeginnerQuestData latestQuestData = null; // 用於存儲最新的任務數據
		for (MJBeginnerUserProgress progress : user.values()) { // 遍歷用戶的所有進度
			if (progress.finished()) { // 如果進度已完成，調用view的beginView方法並跳過此進度
				view.beginView(progress.convertClientSkipModel());
				continue;
			}

			MJBeginnerQuestData questData = provider.questDataModel().questData(progress.questId()); // 獲取當前進度的任務數據
			if (questData == null) { // 如果任務數據為null，跳過此進度
				continue;
			}
			if (questData.hasTeleport()) { // 如果任務有傳送功能，將其設為最新的任務數據
				latestQuestData = questData;
			}
			progress.registeredEvent(view.pc()); // 註冊進度事件
			progress.registeredSubEvent(view.pc()); // 註冊子事件
			view.beginView(progress.convertClientModel()); // 調用view的beginView方法，開始此進度
		}
			// TODO 如果有剩餘的任務，進行傳送（登錄時！）
			/* if (latestQuestData != null) {
			latestQuestData.teleport().onTeleport(view.pc());
			}*/
		view.onView(); // 調用view的onView方法，完成視圖的更新
	}
}

// 定義一個靜態內部類，繼承自MJBeginnerProgressModel
static class MJBeginnerProgressSkipModel extends MJBeginnerProgressModel {
	// 構造函數，調用父類的構造函數
	MJBeginnerProgressSkipModel(MJBeginnerModelProvider provider) {
		super(provider);
	}

	// 覆蓋on方法，處理視圖的邏輯
	@override
	public void on(final MJBeginnerProgressView view) {
		// 將view的pc轉換為MJBeginnerUser對象
		MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(view.pc());
		if (user != null) { // 如果user不為null，表示用戶已存在，直接返回
			return;
		}

		// 如果user為null，創建一個新的MJBeginnerUser對象
		user = MJBeginnerUserProvider.provider().onNewUser(view.pc());
		// 遍歷所有的任務數據
		for (MJBeginnerQuestData questData : MJBeginnerModelProvider.provider().questDataModel().questValues()) {
			// 將當前任務標記為已跳過
			MJBeginnerUserProgress progress = user.onSkipped(view.pc(), questData.id());
			// 調用view的beginView方法，開始此進度
			view.beginView(progress.convertClientSkipModel());
		}
			// 調用view的onView方法，完成視圖的更新
		view.onView();
	}
}
