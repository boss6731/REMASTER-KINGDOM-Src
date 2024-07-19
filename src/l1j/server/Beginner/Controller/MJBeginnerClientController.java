package l1j.server.Beginner.Controller;

import java.util.List;

import l1j.server.Beginner.Model.MJBeginnerModelProvider;
import l1j.server.Beginner.View.MJBeginnerFinishedView;
import l1j.server.Beginner.View.MJBeginnerProgressView;
import l1j.server.Beginner.View.MJBeginnerRevealView;
import l1j.server.Beginner.View.MJBeginnerStartView;
import l1j.server.Beginner.View.MJBeginnerTeleportView;
import l1j.server.Beginner.View.MJBeginnerViewProvider;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJBeginnerClientController {

	// 構造函數
	MJBeginnerClientController() {
	}

	// 當進度發生時執行
	public void onProgress(L1PcInstance pc) {
		MJBeginnerProgressView view = MJBeginnerViewProvider.provider().newProgressView(pc);
		MJBeginnerModelProvider.provider().progressModel().on(view);
	}

	// 當揭示事件發生時執行
	public void onReveal(L1PcInstance pc, int questId) {
		MJBeginnerRevealView view = MJBeginnerViewProvider.provider().newRevealView(pc, questId);
		MJBeginnerModelProvider.provider().revealModel(questId).on(view);
	}

	// 當啟動事件發生時執行
	public void onStart(L1PcInstance pc, int questId) {
		MJBeginnerStartView view = MJBeginnerViewProvider.provider().newStartView(pc, questId);
		MJBeginnerModelProvider.provider().startModel(questId).on(view);
	}

	// 當完成事件發生時執行
	public void onFinished(L1PcInstance pc, int questId, List<Integer> optionalRewardIndexes) {
		MJBeginnerFinishedView view = MJBeginnerViewProvider.provider().newFinishedView(pc, questId, optionalRewardIndexes);
		MJBeginnerModelProvider.provider().finishedModel(questId).on(view);
	}

	// 當傳送事件發生時執行
	public void onTeleport(L1PcInstance pc, int questId) {
		MJBeginnerTeleportView view = MJBeginnerViewProvider.provider().newTeleportView(pc, questId);
		MJBeginnerModelProvider.provider().teleportModel(questId).on(view);
	}

	// MJBeginnerDeveloperController 類，繼承自 MJBeginnerClientController
	static class MJBeginnerDeveloperController extends MJBeginnerClientController {

		@override
		public void onProgress(L1PcInstance pc) {
			long prev = System.currentTimeMillis();  // 記錄開始時間
			super.onProgress(pc);  // 調用父類的 onProgress 方法
			System.out.println(String.format("[CS_QUEST_PROGRESS_REQ] user : %s perform : %dms", pc.getName(), (System.currentTimeMillis() - prev)));  // 打印執行時間
		}

		@override
		public void onReveal(L1PcInstance pc, int questId) {
			long prev = System.currentTimeMillis();  // 記錄開始時間
			super.onReveal(pc, questId);  // 調用父類的 onReveal 方法

			System.out.println(String.format("[CS_QUEST_REVEAL_REQ] questId : %d, user : %s, perform : %dms", questId, pc.getName(), (System.currentTimeMillis() - prev)));  // 打印執行時間
		}

		@override
		public void onStart(L1PcInstance pc, int questId) {
			long prev = System.currentTimeMillis();  // 記錄開始時間
			super.onStart(pc, questId);  // 調用父類的 onStart 方法
			System.out.println(String.format("[CS_QUEST_START_REQ] questId : %d, user : %s, perform : %dms", questId, pc.getName(), (System.currentTimeMillis() - prev)));  // 打印執行時間
		}

		@override
		public void onFinished(L1PcInstance pc, int questId, List<Integer> optionalRewardIndexes) {
			long prev = System.currentTimeMillis();  // 記錄開始時間
			super.onFinished(pc, questId, optionalRewardIndexes);  // 調用父類的 onFinished 方法
			System.out.println(String.format("[CS_QUEST_FINISH_REQ] questId : %d, user : %s, perform : %dms", questId, pc.getName(), (System.currentTimeMillis() - prev)));  // 打印執行時間
		}

		@override
		public void onTeleport(L1PcInstance pc, int questId) {
			long prev = System.currentTimeMillis();  // 記錄開始時間
			super.onTeleport(pc, questId);  // 調用父類的 onTeleport 方法
			System.out.println(String.format("[CS_QUEST_TELEPORT_REQ] questId : %d, user : %s, perform : %dms", questId, pc.getName(), (System.currentTimeMillis() - prev)));  // 打印執行時間
		}

	}
}