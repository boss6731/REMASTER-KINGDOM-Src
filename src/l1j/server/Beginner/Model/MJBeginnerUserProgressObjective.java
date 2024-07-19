package l1j.server.Beginner.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.Beginner.Model.MJBeginnerQuestData.MJBeginnerObjectiveListData.MJBeginnerObjective;

class MJBeginnerUserProgressObjective {

	// 創建新的 MJBeginnerUserProgressObjective 實例
	static MJBeginnerUserProgressObjective newInstance(int questId, int objectiveId, int quantity) {
		MJBeginnerUserProgressObjective objective = new MJBeginnerUserProgressObjective();
		objective.questId(questId);  // 設置任務 ID
		objective.objectiveId(objectiveId);  // 設置目標 ID
		objective.quantity(quantity);  // 設置數量
		return objective;
	}

	// 從 ResultSet 創建新的 MJBeginnerUserProgressObjective 實例
	static MJBeginnerUserProgressObjective newInstance(ResultSet rs) throws SQLException {
		return newInstance(
				rs.getInt("quest_id"),  // 從 ResultSet 中獲取任務 ID
				rs.getInt("objective_id"),  // 從 ResultSet 中獲取目標 ID
				rs.getInt("quantity")  // 從 ResultSet 中獲取數量
		);
	}

	// 私有成員變量
	private int questId;  // 任務 ID
	private int objectiveId;  // 目標 ID
	private int quantity;  // 數量

	// 獲取 questId
	int questId() {
		return questId;
	}

	// 設置 questId
	void questId(int questId) {
		this.questId = questId;
	}

	// 獲取 objectiveId
	int objectiveId() {
		return objectiveId;
	}

	// 設置 objectiveId
	void objectiveId(int objectiveId) {
		this.objectiveId = objectiveId;
	}

	// 獲取 quantity
	int quantity() {
		return quantity;
	}

	// 設置 quantity
	void quantity(int quantity) {
		this.quantity = quantity;
	}

	// 私有構造函數，防止直接實例化
	private MJBeginnerUserProgressObjective() {
	}

	class MJBeginnerUserProgressObjective {

		// 私有成員變量
		private int questId;  // 任務 ID
		private int objectiveId;  // 目標 ID
		private int quantity;  // 數量

		// 獲取 questId
		int questId() {
			return questId;
		}

		// 設置 questId
		void questId(int questId) {
			this.questId = questId;
		}

		// 獲取 objectiveId
		int objectiveId() {
			return objectiveId;
		}

		// 設置 objectiveId
		void objectiveId(int objectiveId) {
			this.objectiveId = objectiveId;
		}

		// 獲取 quantity
		int quantity() {
			return quantity;
		}

		// 增加 quantity 並返回新的值
		int addedQuantity(int added) {
			return quantity += added;
		}

		// 設置 quantity
		void quantity(int quantity) {
			this.quantity = quantity;
		}

		// 獲取映射的目標數據
		MJBeginnerObjective mappedObjectiveData() {
			return MJBeginnerModelProvider.provider()
					.questDataModel()
					.questData(questId())
					.objectiveList()
					.find(objectiveId());
		}

		// 更新數據庫中的進度目標
		void updateDatabase(int objectId) {
			MJBeginnerUserDatabaseProvider.provider().updateUserProgressObjective(objectId, questId(), objectiveId(), quantity());
		}

		// 插入進度目標到數據庫
		void insertDatabase(int objectId) {
			MJBeginnerUserDatabaseProvider.provider().insertUserProgressObjective(objectId, questId(), objectiveId());
		}

		// 靜態工廠方法用於創建新的實例
		static MJBeginnerUserProgressObjective newInstance(int questId, int objectiveId, int quantity) {
			MJBeginnerUserProgressObjective objective = new MJBeginnerUserProgressObjective();
			objective.questId(questId);  // 設置任務 ID
			objective.objectiveId(objectiveId);  // 設置目標 ID
			objective.quantity(quantity);  // 設置數量
			return objective;
		}

		// 靜態工廠方法用於從 ResultSet 創建新的實例
		static MJBeginnerUserProgressObjective newInstance(ResultSet rs) throws SQLException {
			return newInstance(
					rs.getInt("quest_id"),  // 從 ResultSet 中獲取任務 ID
					rs.getInt("objective_id"),  // 從 ResultSet 中獲取目標 ID
					rs.getInt("quantity")  // 從 ResultSet 中獲取數量
			);
		}

		// 私有構造函數，防止直接實例化
		private MJBeginnerUserProgressObjective() {
		}
	}
}