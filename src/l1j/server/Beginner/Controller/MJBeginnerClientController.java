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
	MJBeginnerClientController() {
	}

	public void onProgress(L1PcInstance pc) {
		MJBeginnerProgressView view = MJBeginnerViewProvider.provider().newProgressView(pc);
		MJBeginnerModelProvider.provider().progressModel().on(view);
	}

	public void onReveal(L1PcInstance pc, int questId) {
		MJBeginnerRevealView view = MJBeginnerViewProvider.provider().newRevealView(pc, questId);
		MJBeginnerModelProvider.provider().revealModel(questId).on(view);
	}

	public void onStart(L1PcInstance pc, int questId) {
		MJBeginnerStartView view = MJBeginnerViewProvider.provider().newStartView(pc, questId);
		MJBeginnerModelProvider.provider().startModel(questId).on(view);
	}

	public void onFinished(L1PcInstance pc, int questId, List<Integer> optionalRewardIndexes) {
		MJBeginnerFinishedView view = MJBeginnerViewProvider.provider().newFinishedView(pc, questId,
				optionalRewardIndexes);
		MJBeginnerModelProvider.provider().finishedModel(questId).on(view);
	}

	public void onTeleport(L1PcInstance pc, int questId) {
		MJBeginnerTeleportView view = MJBeginnerViewProvider.provider().newTeleportView(pc, questId);
		MJBeginnerModelProvider.provider().teleportModel(questId).on(view);
	}

	static class MJBeginnerDeveloperController extends MJBeginnerClientController {
		@Override
		public void onProgress(L1PcInstance pc) {
			long prev = System.currentTimeMillis();
			super.onProgress(pc);
			System.out.println(String.format("[CS_QUEST_PROGRESS_REQ] 用戶：%s 執行：%d 毫秒", pc.getName(),
					(System.currentTimeMillis() - prev)));
		}

		@Override
		public void onReveal(L1PcInstance pc, int questId) {
			long prev = System.currentTimeMillis();
			super.onReveal(pc, questId);
			System.out.println(String.format("[CS_QUEST_REVEAL_REQ] 任務ID：%d，用戶：%s，執行：%d 毫秒", questId, pc.getName(),
					(System.currentTimeMillis() - prev)));
		}

		@Override
		public void onStart(L1PcInstance pc, int questId) {
			long prev = System.currentTimeMillis();
			super.onStart(pc, questId);
			System.out.println(String.format("[CS_QUEST_START_REQ] 任務ID：%d，用戶：%s，執行：%d 毫秒", questId, pc.getName(),
					(System.currentTimeMillis() - prev)));
		}

		@Override
		public void onFinished(L1PcInstance pc, int questId, List<Integer> optionalRewardIndexes) {
			long prev = System.currentTimeMillis();
			super.onFinished(pc, questId, optionalRewardIndexes);
			System.out.println(String.format("[CS_QUEST_FINISH_REQ] 任務ID：%d，用戶：%s，執行：%d 毫秒", questId, pc.getName(),
					(System.currentTimeMillis() - prev)));
		}

		@Override
		public void onTeleport(L1PcInstance pc, int questId) {
			long prev = System.currentTimeMillis();
			super.onTeleport(pc, questId);
			System.out.println(String.format("[CS_QUEST_TELEPORT_REQ] 任務ID：%d，用戶：%s，執行：%d 毫秒", questId, pc.getName(),
					(System.currentTimeMillis() - prev)));
		}

	}
}
