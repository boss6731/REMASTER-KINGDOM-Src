package l1j.server.Beginner.Model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import l1j.server.server.model.Instance.L1PcInstance;

class MJBeginnerUser {
	static MJBeginnerUser newInstance(int objectId) {
		MJBeginnerUser user = new MJBeginnerUser();
		user.objectId = objectId;
		return user;
	}

	private int objectId;
	private HashMap<Integer, MJBeginnerUserProgress> progresses;

	private MJBeginnerUser() {
		this.progresses = new HashMap<>();
	}

	private MJBeginnerUserProgress safeProgress(int questId) {
		return progresses.get(questId);
	}

	Collection<MJBeginnerUserProgress> values() {
		return progresses.values();
	}

	void append(MJBeginnerUserProgress progress) {
		progresses.put(progress.questId(), progress);
	}

	void onStart(L1PcInstance pc, int questId) {
		MJBeginnerUserProgress progress = MJBeginnerUserProgress.newInstance(objectId, questId);
		progresses.put(questId, progress);
		progress.insertDatabase();
		progress.allocateObjective();
		progress.registeredEvent(pc);
		progress.registeredSubEvent(pc);
	}

	void onFinished(L1PcInstance pc, int questId) {
		MJBeginnerUserProgress progress = safeProgress(questId);
		if (progress != null) {
			progress.finishProgress();
		}
	}

	MJBeginnerUserProgress onSkipped(L1PcInstance pc, int questId) {
		MJBeginnerUserProgress progress = safeProgress(questId);
		if (progress != null) {
			progress.finishProgress();
		} else {
			progress = MJBeginnerUserProgress.newInstance(objectId, questId);
			progresses.put(questId, progress);
			progress.insertDatabase();
			progress.finishProgress();
		}
		return progress;
	}

	boolean onRevealedComplete(L1PcInstance pc, int questId) {
		MJBeginnerUserProgress progress = safeProgress(questId);
		if (progress == null || progress.finished() || progress.completed()) {
			return false;
		}
		progress.onRevealedCompleteProgress();
		MJBeginnerEventProvider.invokeUpdateNoti(pc, progress.convertClientModel());
		return true;
	}

	int onRevealedComplete(L1PcInstance pc) {
		// 因為這一點，使用並發就太浪費了…
		// 將其用作捕獲方法是一種浪費...請小心，因為它不是您經常使用的東西。
		int skipped = 0;
		try {
			for (MJBeginnerUserProgress progress : values()) {
				if (onRevealedComplete(pc, progress.questId())) {
					++skipped;
				}
			}
		} catch (Exception e) {
		}
		return skipped;
	}

	void state(int questId, MJBeginnerUserStateListener listener) {
		MJBeginnerUserProgress progress = safeProgress(questId);
		if (progress == null) {
			listener.onNotFound();
		} else if (progress.finished()) {
			listener.onFinished();
		} else if (progress.started()) {
			listener.onStarted(progress.completed());
		} else {
			listener.onRevealed();
		}
	}

	/**
	 * <b>任務已經完成了嗎？
	 * 
	 * @param questId 任務ID
	 * @return boolean true 如果已經完成。
	 **/
	boolean finished(int questId) {
		MJBeginnerUserProgress progress = safeProgress(questId);
		return progress != null && progress.finished();
	}

	/**
	 * <b>任務已經完成了嗎？
	 * 
	 * @param ids {@code List<Integer>} 要檢查的任務 ID
	 * @param 和   true:and /false:or
	 * @return boolean true 如果已經完成。
	 **/
	boolean finished(List<Integer> ids, boolean and) {
		return and ? finishedAnd(ids) : finishedOr(ids);
	}

	/**
	 * <b>任務已經開始了嗎？
	 * 
	 * @param questId 任務ID
	 * @return boolean true 如果已經開始。
	 **/
	boolean started(int questId) {
		MJBeginnerUserProgress progress = safeProgress(questId);
		return progress != null && progress.started();
	}

	/**
	 * <b>任務已經發生了嗎？
	 * 
	 * @param questId 任務ID
	 * @return boolean true 如果已經發生。
	 **/
	boolean revealed(int questId) {
		return safeProgress(questId) != null;
	}

	/**
	 * <b>任務已經發生了嗎？
	 * 
	 * @param ids {@code List<Integer>} 要檢查的任務 ID
	 * @param 和   true:and /false:or
	 * @return boolean true 如果已經發生。
	 **/
	boolean revealed(List<Integer> ids, boolean and) {
		return and ? revealedAnd(ids) : revealedOr(ids);
	}

	private boolean finishedAnd(List<Integer> ids) {
		for (Integer questId : ids) {
			if (!finished(questId)) {
				return false;
			}
		}
		return true;
	}

	private boolean finishedOr(List<Integer> ids) {
		for (Integer questId : ids) {
			if (finished(questId)) {
				return true;
			}
		}
		return false;
	}

	private boolean revealedAnd(List<Integer> ids) {
		for (Integer questId : ids) {
			if (!revealed(questId)) {
				return false;
			}
		}
		return true;
	}

	private boolean revealedOr(List<Integer> ids) {
		for (Integer questId : ids) {
			if (revealed(questId)) {
				return true;
			}
		}
		return false;
	}
}
