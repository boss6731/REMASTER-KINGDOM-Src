package l1j.server.Beginner.Model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import l1j.server.server.model.Instance.L1PcInstance;

// 定義一個 MJBeginnerUser 類
class MJBeginnerUser {

	// 定義一個靜態方法，用於創建新的 MJBeginnerUser 實例
	static MJBeginnerUser newInstance(int objectId) {
		MJBeginnerUser user = new MJBeginnerUser();  // 創建新實例
		user.objectId = objectId;  // 設置 objectId
		return user;  // 返回新實例
	}

	// 定義私有變量
	private int objectId;  // 用戶的 objectId
	private HashMap<Integer, MJBeginnerUserProgress> progresses;  // 任務進度的映射

	// 私有構造函數，初始化 progresses
	private MJBeginnerUser() {
		this.progresses = new HashMap<>();  // 初始化 HashMap
	}

	// 私有方法，安全地獲取指定任務 ID 的進度
	private MJBeginnerUserProgress safeProgress(int questId) {
		return progresses.get(questId);  // 返回對應 questId 的進度
	}

	// 公共方法，獲取所有進度的集合
	Collection<MJBeginnerUserProgress> values() {
		return progresses.values();  // 返回所有進度的集合
	}

	// 公共方法，添加進度到 progresses 中
	void append(MJBeginnerUserProgress progress) {
		progresses.put(progress.questId(), progress);  // 根據 questId 添加進度
	}





	// 當任務開始時調用
	void onStart(L1PcInstance pc, int questId){
		// 創建新的 MJBeginnerUserProgress 實例
		MJBeginnerUserProgress progress = MJBeginnerUserProgress.newInstance(objectId, questId);
		// 將進度添加到 progresses 映射中
		progresses.put(questId, progress);
		// 插入進度到資料庫
		progress.insertDatabase();
		// 分配目標
		progress.allocateObjective();
		// 註冊事件
		progress.registeredEvent(pc);
		// 註冊次要事件
		progress.registeredSubEvent(pc);
	}

	// 當任務完成時調用
	void onFinished(L1PcInstance pc, int questId){
		// 獲取指定任務 ID 的進度
		MJBeginnerUserProgress progress = safeProgress(questId);
		if(progress != null){
			// 完成進度
			progress.finishProgress();
		}
	}

	// 當任務被跳過時調用
	MJBeginnerUserProgress onSkipped(L1PcInstance pc, int questId){
		// 獲取指定任務 ID 的進度
		MJBeginnerUserProgress progress = safeProgress(questId);
		if(progress != null){
			// 完成進度
			progress.finishProgress();
		} else {
			// 創建新的進度實例
			progress = MJBeginnerUserProgress.newInstance(objectId, questId);
			// 將進度添加到 progresses 映射中
			progresses.put(questId, progress);
			// 插入進度到資料庫
			progress.insertDatabase();
			// 完成進度
			progress.finishProgress();
		}
		return progress;
	}

	// 當任務被揭示為完成時調用
	boolean onRevealedComplete(L1PcInstance pc, int questId){
		// 獲取指定任務 ID 的進度
		MJBeginnerUserProgress progress = safeProgress(questId);
		if(progress == null || progress.finished() || progress.completed()){
			return false;
		}
		// 揭示完成進度
		progress.onRevealedCompleteProgress();
		// 觸發更新通知
		MJBeginnerEventProvider.invokeUpdateNoti(pc, progress.convertClientModel());
		return true;
	}

	// 檢查並完成所有啟示任務
	int onRevealedComplete(L1PcInstance pc){
		// 由於這部分代碼的並發性問題，不值得為此使用 concurrent。
		// 用捕捉方式也不值得，因為這不是經常使用的。
		// 只需注意即可。
		int skipped = 0;
		try {
			// 遍歷所有進度並檢查是否完成
			for (MJBeginnerUserProgress progress : values()) {
				if (onRevealedComplete(pc, progress.questId())) {
					++skipped;  // 計數被跳過的任務
				}
			}
		} catch (Exception e) {
			// 捕捉所有異常，但不做處理
		}
		return skipped;  // 返回被跳過的任務數量
	}

	// 設定任務狀態
	void state(int questId, MJBeginnerUserStateListener listener) {
		MJBeginnerUserProgress progress = safeProgress(questId);
		if (progress == null) {
			listener.onNotFound();  // 若找不到進度，調用 onNotFound
		} else if (progress.finished()) {
			listener.onFinished();  // 若進度已完成，調用 onFinished
		} else if (progress.started()) {
			listener.onStarted(progress.completed());  // 若進度已開始，調用 onStarted
		} else {
			listener.onRevealed();  // 其他情況，調用 onRevealed
		}
	}

	/**
	 * <b>檢查指定任務是否已完成</b>
	 * @param questId 任務 ID
	 * @return boolean 若已完成返回 true
	 **/
	boolean finished(int questId) {
		MJBeginnerUserProgress progress = safeProgress(questId);
		return progress != null && progress.finished();  // 返回任務是否完成
	}



	/**
	 * <b>檢查指定的任務是否已完成</b>
	 * @param ids {@code List<Integer>} 要檢查的任務 ID 列表
	 * @param and true: 所有任務都已完成才能返回 true / false: 只要有一個任務已完成就返回 true
	 * @return boolean 如果已完成返回 true
	 **/
	boolean finished(List<Integer> ids, boolean and) {
		return and ? finishedAnd(ids) : finishedOr(ids);
	}

	/**
	 * <b>檢查指定的任務是否已開始</b>
	 * @param questId 任務 ID
	 * @return boolean 如果已開始返回 true
	 **/
	boolean started(int questId) {
		MJBeginnerUserProgress progress = safeProgress(questId);
		return progress != null && progress.started();
	}

	/**
	 * <b>檢查指定的任務是否已啟示</b>
	 * @param questId 任務 ID
	 * @return boolean 如果已啟示返回 true
	 **/
	boolean revealed(int questId) {
		return safeProgress(questId) != null;
	}

	/**
	 * <b>檢查指定的任務是否已啟示</b>
	 * @param ids {@code List<Integer>} 要檢查的任務 ID 列表
	 * @param and true: 所有任務都已啟示才能返回 true / false: 只要有一個任務已啟示就返回 true
	 * @return boolean 如果已啟示返回 true
	 **/
	boolean revealed(List<Integer> ids, boolean and) {
		return and ? revealedAnd(ids) : revealedOr(ids);
	}



	private boolean finishedAnd(List<Integer> ids) {
		for (Integer questId : ids) {
			if (!finished(questId)) {  // 如果有任務未完成，返回 false
				return false;
			}
		}
		return true;  // 如果所有任務都完成，返回 true
	}

	private boolean finishedOr(List<Integer> ids) {
		for (Integer questId : ids) {
			if (finished(questId)) {  // 如果有任務已完成，返回 true
				return true;
			}
		}
		return false;  // 如果所有任務都未完成，返回 false
	}

	private boolean revealedAnd(List<Integer> ids) {
		for (Integer questId : ids) {
			if (!revealed(questId)) {  // 如果有任務未啟示，返回 false
				return false;
			}
		}
		return true;  // 如果所有任務都已啟示，返回 true
	}

	private boolean revealedOr(List<Integer> ids) {
		for (Integer questId : ids) {
			if (revealed(questId)) {  // 如果有任務已啟示，返回 true
				return true;
			}
		}
		return false;  // 如果所有任務都未啟示，返回 false
	}
