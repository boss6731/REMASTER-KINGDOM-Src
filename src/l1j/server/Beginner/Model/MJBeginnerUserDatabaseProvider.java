package l1j.server.Beginner.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Updator;
import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;
import l1j.server.MJTemplate.MJSqlHelper.Handler.SelectorHandler;

// 定義 MJBeginnerUserDatabaseProvider 類
class MJBeginnerUserDatabaseProvider {
	private static final MJBeginnerUserDatabaseProvider provider = new MJBeginnerUserDatabaseProvider(); // 單例模式

	// 提供靜態方法獲取單例實例
	static MJBeginnerUserDatabaseProvider provider() {
		return provider;
	}

	// 私有構造函數
	private MJBeginnerUserDatabaseProvider() {
	}

	// 選取用戶進度的方法
	MJBeginnerUser selectUserProgress(final int objectId) {
		MJBeginnerUser user = MJBeginnerUser.newInstance(objectId); // 創建新用戶實例
		// 執行 SQL 查詢
		Selector.exec("select * from beginner_user_progress where object_id=?", new SelectorHandler() {
			@override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, objectId); // 設置查詢條件
			}

			@override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					MJBeginnerUserProgress progress = MJBeginnerUserProgress.newInstance(objectId, rs); // 創建進度實例
					user.append(progress); // 添加進度到用戶
					if (progress.finished()) {
						continue; // 如果進度已完成，跳過
					}
					progress.objectives(selectUserProgressObjective(objectId, progress.questId())); // 設置進度目標
				}
			}
		});
		return user; // 返回用戶實例
	}

	// 假設存在的方法，根據 objectId 和 questId 選取進度目標
	private List<Objective> selectUserProgressObjective(int objectId, int questId) {
		// 您需要提供這個方法的實現
		return new ArrayList<>(); // 這只是佔位符，替換為實際實現
	}


	// 選取用戶進度目標的方法
	private List<MJBeginnerUserProgressObjective> selectUserProgressObjective(final int objectId, final int questId) {
		final List<MJBeginnerUserProgressObjective> objectives = new LinkedList<>();
		// 執行 SQL 查詢
		Selector.exec("select quest_id, objective_id, quantity from beginner_user_progress_objective where object_id=? and quest_id=?", new SelectorHandler() {
			@override
			public void handle(PreparedStatement pstm) throws Exception {
				pstm.setInt(1, objectId); // 設置查詢條件 objectId
				pstm.setInt(2, questId); // 設置查詢條件 questId
			}

			@override
			public void result(ResultSet rs) throws Exception {
				while (rs.next()) {
					MJBeginnerUserProgressObjective objective = MJBeginnerUserProgressObjective.newInstance(rs); // 創建進度目標實例
					objectives.add(objective); // 將目標添加到列表中
				}
			}
		});
		return objectives; // 返回進度目標列表
	}

	// 更新用戶進度的方法
	void updateUserProgress(final int objectId, final int questId, final long startTime, final long finishTime) {
		// 執行 SQL 更新
		Updator.exec("update beginner_user_progress set start_time=?, finish_time=? where object_id=? and quest_id=?", new Handler() {
			@override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setTimestamp(++idx, new Timestamp(startTime)); // 設置開始時間
				pstm.setTimestamp(++idx, new Timestamp(finishTime)); // 設置完成時間
				pstm.setInt(++idx, objectId); // 設置 objectId
				pstm.setInt(++idx, questId); // 設置 questId
			}
		});
	}

	// 插入用戶進度的方法
	void insertUserProgress(final int objectId, final int questId, final long startTime) {
		Updator.exec("insert into beginner_user_progress set object_id=?, quest_id=?, start_time=?", new Handler() {
			@override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, objectId); // 設置 objectId
				pstm.setInt(++idx, questId); // 設置 questId
				pstm.setTimestamp(++idx, new Timestamp(startTime)); // 設置開始時間
			}
		});
	}

	// 更新用戶進度目標的方法
	void updateUserProgressObjective(final int objectId, final int questId, final int objectiveId, final int quantity) {
		Updator.exec("update beginner_user_progress_objective set quantity=? where object_id=? and quest_id=? and objective_id=?", new Handler() {
			@override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, quantity); // 設置數量
				pstm.setInt(++idx, objectId); // 設置 objectId
				pstm.setInt(++idx, questId); // 設置 questId
				pstm.setInt(++idx, objectiveId); // 設置 objectiveId
			}
		});
	}

	// 插入用戶進度目標的方法
	void insertUserProgressObjective(final int objectId, final int questId, final int objectiveId) {
		Updator.exec("insert into beginner_user_progress_objective set object_id=?, quest_id=?, objective_id=?, quantity=0", new Handler() {
			@override
			public void handle(PreparedStatement pstm) throws Exception {
				int idx = 0;
				pstm.setInt(++idx, objectId); // 設置 objectId
				pstm.setInt(++idx, questId); // 設置 questId
				pstm.setInt(++idx, objectiveId); // 設置 objectiveId
			}
		});
	}

}