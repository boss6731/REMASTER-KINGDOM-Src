package l1j.server.Beginner.View;

import java.util.List;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_FINISH_ACK;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJBeginnerFinishedView implements MJBeginnerView {
	private L1PcInstance pc;
	private int questId;
	private List<Integer> optionalRewardIndexes;

	// 構造函數，初始化 pc、questId 和 optionalRewardIndexes
	MJBeginnerFinishedView(L1PcInstance pc, int questId, List<Integer> optionalRewardIndexes) {
		this.pc = pc;
		this.questId = questId;
		this.optionalRewardIndexes = optionalRewardIndexes;
	}

	// 獲取 pc 實例的方法
	public L1PcInstance pc() {
		return pc;
	}

	// 獲取 questId 的方法
	public int questId() {
		return questId;
	}

	// 獲取 optionalRewardIndexes 的方法
	public List<Integer> optionalRewardIndexes() {
		return optionalRewardIndexes;
	}

	// 發送封包的方法
	private void viewInternal(SC_QUEST_FINISH_ACK.eResultCode resultCode) {
	// 新任務註釋
		SC_QUEST_FINISH_ACK ack = SC_QUEST_FINISH_ACK.newInstance();
		ack.set_id(questId());
		ack.set_result(resultCode);
		pc().sendPackets(ack, MJEProtoMessages.SC_QUEST_FINISH_ACK);
	}

	// 任務成功時調用的方法
	public void onSuccess() {
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.SUCCESS);
	}

	// 任務失敗時調用的方法
	public void onFail() {
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.FAIL);
	}

	// 任務已完成時調用的方法
	public void onAlreadyFinished() {
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.FAIL_ALREADY_FINISHED);
	}

	// 任務未完成時調用的方法
	public void onNotCompleted() {
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.FAIL_NOT_COMPLETED);
	}

	// 獎勵索引無效時調用的方法
	public void onInvalidRewardIndexes() {
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.FAIL_NOT_VALID_REWARD_INDEXES);
	}

	// 任務過期時調用的方法
	public void onObsolete() {
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.FAIL_OBSOLETE);
	}

	// 靜態內部類，用於開發模式
	static class MJBeginnerFinishedDevelopView extends MJBeginnerFinishedView {
		// 構造函數，調用父類構造函數
		MJBeginnerFinishedDevelopView(L1PcInstance pc, int questId, List<Integer> optionalRewardIndexes) {
			super(pc, questId, optionalRewardIndexes);
		}

		// 覆寫 onSuccess 方法，打印調試信息
		@override
		public void onSuccess() {
			System.out.println(String.format("MJ新手完成消息 -> 成功時. %d", questId()));
			super.onSuccess();
		}
		// 覆寫 onFail 方法，打印調試信息
		@override
		public void onFail() {
			System.out.println(String.format("MJ新手完成消息 -> 失敗時. %d", questId()));
			super.onFail();
		}

		// 覆寫 onAlreadyFinished 方法，打印調試信息
		@override
		public void onAlreadyFinished() {
			System.out.println(String.format("MJ新手完成消息 -> 已完成時. %d", questId()));
			super.onAlreadyFinished();
		}

		// 覆寫 onNotCompleted 方法，打印調試信息
		@override
		public void onNotCompleted() {
			System.out.println(String.format("MJ新手完成消息 -> 未完成時. %d", questId()));
			super.onNotCompleted();
		}

		// 覆寫 onInvalidRewardIndexes 方法，打印調試信息
		@override
		public void onInvalidRewardIndexes() {
			System.out.println(String.format("MJ新手完成消息 -> 獎勵索引無效時. %d", questId()));
			super.onInvalidRewardIndexes();
		}

		// 覆寫 onObsolete 方法，打印調試信息
		@override
		public void onObsolete() {
			System.out.println(String.format("MJ新手完成消息 -> 過期時. %d", questId()));
			super.onObsolete();
		}
	}
}
