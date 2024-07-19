package l1j.server.Beginner.Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import l1j.server.Beginner.Model.MJBeginnerQuestData.MJBeginnerObjectiveListData.MJBeginnerObjective;
import l1j.server.Beginner.Model.MJBeginnerSubEventData.MJBeginnerSubEventListener;
import l1j.server.MJTemplate.MJProto.MainServer_Client.QuestProgress;
import l1j.server.MJTemplate.MJProto.MainServer_Client.QuestProgress.ObjectiveProgress;
import l1j.server.server.model.Instance.L1PcInstance;

class MJBeginnerUserProgress {
	// 創建新的 MJBeginnerUserProgress 實例
	static MJBeginnerUserProgress newInstance(int objectId, int questId) {
		MJBeginnerUserProgress progress = new MJBeginnerUserProgress();
		progress.objectId = objectId;  // 設置 objectId
		progress.questId = questId;    // 設置 questId
		progress.startTime = System.currentTimeMillis();  // 設置當前時間為開始時間
		return progress;  // 返回新的進度實例
	}

	// 從 ResultSet 創建新的 MJBeginnerUserProgress 實例
	static MJBeginnerUserProgress newInstance(int objectId, ResultSet rs) throws SQLException {
		MJBeginnerUserProgress progress = new MJBeginnerUserProgress();
		progress.objectId = objectId;
		progress.questId = rs.getInt("quest_id");
		progress.startTime = convertTimestamp(rs.getTimestamp("start_time"));
		progress.finishTime = convertTimestamp(rs.getTimestamp("finish_time"));
		return progress;
	}

	// 將 Timestamp 轉換為 long
	static long convertTimestamp(Timestamp ts) {
		return ts == null ? 0 : ts.getTime();  // 如果 ts 為空返回 0，否則返回時間
	}

	private int objectId;  // 物件 ID
	private int questId;   // 任務 ID
	private long startTime;  // 開始時間
	private long finishTime;  // 結束時間
	private HashMap<Integer, MJBeginnerUserProgressObjective> objectives;  // 目標集合

	// 私有構造函數，防止直接實例化
	private MJBeginnerUserProgress() {
	}

	class MJBeginnerUserProgress {

		// 獲取 objectId
		int objectId() {
			return objectId;
		}

		// 獲取 questId
		int questId() {
			return questId;
		}

		// 獲取 startTime
		long startTime() {
			return startTime;
		}

		// 獲取 finishTime
		long finishTime() {
			return finishTime;
		}

		// 設置 objectives
		void objectives(List<MJBeginnerUserProgressObjective> objectives) {
			this.objectives = new HashMap<>(objectives.size());
			for (MJBeginnerUserProgressObjective objective : objectives) {
				this.objectives.put(objective.objectiveId(), objective);
			}
		}

		// 檢查是否已完成
		boolean finished() {
			return finishTime() > 0;
		}

		// 檢查是否已開始
		boolean started() {
			return startTime() > 0;
		}

		// 檢查是否有 objective
		boolean hasObjective() {
			return objectives != null && objectives.size() > 0;
		}

		// 分配 objective
		void allocateObjective() {
			MJBeginnerQuestData questData = mappedObjectiveData();
			objectives = new HashMap<>(questData.objectiveList().objective().size());
			for (MJBeginnerObjective objectiveData : questData.objectiveList().objective()) {
				MJBeginnerUserProgressObjective objective = MJBeginnerUserProgressObjective.newInstance(questId(), objectiveData.id(), 0);
				objectives.put(objective.objectiveId(), objective);
				objective.insertDatabase(objectId());
			}
		}

		// 私有成員變量
		private int objectId;  // 物件 ID
		private int questId;   // 任務 ID
		private long startTime;  // 開始時間
		private long finishTime;  // 結束時間
		private HashMap<Integer, MJBeginnerUserProgressObjective> objectives;  // 目標集合

		// 私有構造函數，防止直接實例化
		private MJBeginnerUserProgress() {
		}

	}

	class MJBeginnerUserProgress {

		// 註冊事件
		void registeredEvent(L1PcInstance pc) {
			for (MJBeginnerUserProgressObjective objective : objectives.values()) {
				MJBeginnerEventProvider.provider().registeredEvent(pc, this, objective);
			}
		}

		// 註冊子事件
		void registeredSubEvent(L1PcInstance pc) {
			MJBeginnerSubEventListener subEvent = MJBeginnerModelProvider.provider().subEventModel().model(questId());
			if (subEvent != null) {
				subEvent.onQuest(pc, questId());
			}
		}

		// 完成進度
		void finishProgress() {
			finishTime = System.currentTimeMillis();
			updateDatabase();
		}

		// 在揭示完成進度時執行
		void onRevealedCompleteProgress() {
			MJBeginnerQuestData questData = mappedObjectiveData();
			if (questData.hasObjectiveList()) {
				for (MJBeginnerObjective objectiveData : questData.objectiveList().objective()) {
					MJBeginnerUserProgressObjective objective = objectives.get(objectiveData.id());
					objective.quantity(objectiveData.requiredQuantity());
				}
			}
		}

		// 檢查是否完成
		boolean completed() {
			for (MJBeginnerUserProgressObjective objective : objectives.values()) {
				MJBeginnerObjective objectiveData = objective.mappedObjectiveData();
				if (objective.quantity() < objectiveData.requiredQuantity()) {
					return false;
				}
			}
			return true;
		}

		// 其他相關成員和方法
		private int objectId;  // 物件 ID
		private int questId;   // 任務 ID
		private long startTime;  // 開始時間
		private long finishTime;  // 結束時間
		private HashMap<Integer, MJBeginnerUserProgressObjective> objectives;  // 目標集合

		// 私有構造函數，防止直接實例化
		private MJBeginnerUserProgress() {
		}

		// 更新數據庫的假設方法
		private void updateDatabase() {
		// 需要提供具體的實現來更新數據庫
		}
	}

	class MJBeginnerUserProgress {

		// 將進度轉換為客戶端模型
		QuestProgress convertClientModel() {
			QuestProgress questProgress = QuestProgress.newInstance();
			questProgress.set_id(questId());  // 設置任務 ID
			questProgress.set_start_time(startTime());  // 設置開始時間
			questProgress.set_is_shown_in_quest_window(true);  // 顯示在任務窗口中

			if (finished()) {  // 如果已完成，設置完成時間
				questProgress.set_finish_time(finishTime());
			}

			if (hasObjective()) {  // 如果有目標，添加目標進度
				for (MJBeginnerUserProgressObjective objective : objectives.values()) {
					int requiredQuantity = objective.mappedObjectiveData().requiredQuantity();
					ObjectiveProgress objectiveProgress = ObjectiveProgress.newInstance();
					objectiveProgress.set_id(objective.objectiveId());  // 設置目標 ID
					objectiveProgress.set_quantity(Math.min(objective.quantity(), requiredQuantity));  // 設置當前數量
					objectiveProgress.set_required_quantity(requiredQuantity);  // 設置所需數量
					questProgress.add_objectives(objectiveProgress);  // 添加目標進度
				}
			}
			return questProgress;  // 返回任務進度
		}

		// 將進度轉換為客戶端跳過模型
		QuestProgress convertClientSkipModel() {
			QuestProgress questProgress = QuestProgress.newInstance();
			questProgress.set_id(questId());  // 設置任務 ID
			questProgress.set_start_time(startTime());  // 設置開始時間
			questProgress.set_is_shown_in_quest_window(true);  // 顯示在任務窗口中

			if (finished()) {  // 如果已完成，設置完成時間
				questProgress.set_finish_time(finishTime());
			}
			return questProgress;  // 返回任務進度
		}

		// 獲取映射的任務數據
		MJBeginnerQuestData mappedObjectiveData() {
			return MJBeginnerModelProvider.provider()
					.questDataModel()
					.questData(questId());  // 根據任務 ID 獲取任務數據
		}

		// 其他相關成員和方法
		private int objectId;  // 物件 ID
		private int questId;   // 任務 ID
		private long startTime;  // 開始時間
		private long finishTime;  // 結束時間
		private HashMap<Integer, MJBeginnerUserProgressObjective> objectives;  // 目標集合

		// 私有構造函數，防止直接實例化
		private MJBeginnerUserProgress() {
		}
	}

	class MJBeginnerUserProgress {

		// 將進度插入到數據庫
		void insertDatabase() {
			MJBeginnerUserDatabaseProvider.provider().insertUserProgress(objectId(), questId(), startTime());
		}

		// 更新數據庫中的進度
		void updateDatabase() {
			MJBeginnerUserDatabaseProvider.provider().updateUserProgress(objectId(), questId(), startTime(), finishTime());
		}

		// 其他相關成員和方法
		private int objectId;  // 物件 ID
		private int questId;   // 任務 ID
		private long startTime;  // 開始時間
		private long finishTime;  // 結束時間
		private HashMap<Integer, MJBeginnerUserProgressObjective> objectives;  // 目標集合

		// 獲取 objectId
		int objectId() {
			return objectId;
		}

		// 獲取 questId
		int questId() {
			return questId;
		}

		// 獲取 startTime
		long startTime() {
			return startTime;
		}

		// 獲取 finishTime
		long finishTime() {
			return finishTime;
		}

		// 私有構造函數，防止直接實例化
		private MJBeginnerUserProgress() {
		}
	}
