package l1j.server.Beginner.View;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_FINISH_ACK;
import l1j.server.server.model.Instance.L1PcInstance;

import java.util.List;

public class MJBeginnerFinishedView implements MJBeginnerView {
	private L1PcInstance pc;
	private int questId;
	private List<Integer> optionalRewardIndexes;

	MJBeginnerFinishedView(L1PcInstance pc, int questId, List<Integer> optionalRewardIndexes) {
		this.pc = pc;
		this.questId = questId;
		this.optionalRewardIndexes = optionalRewardIndexes;
	}

	public L1PcInstance pc() {
		return pc;
	}

	public int questId() {
		return questId;
	}

	public List<Integer> optionalRewardIndexes() {
		return optionalRewardIndexes;
	}

	private void viewInternal(SC_QUEST_FINISH_ACK.eResultCode resultCode) {
		// 創建新的 SC_QUEST_FINISH_ACK 實例
		SC_QUEST_FINISH_ACK ack = SC_QUEST_FINISH_ACK.newInstance();

		// 設置任務 ID
		ack.set_id(questId());

		// 設置結果代碼
		ack.set_result(resultCode);

		// 發送封包給玩家客戶端
		pc().sendPackets(ack, MJEProtoMessages.SC_QUEST_FINISH_ACK);
	}

	public void onSuccess() {
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.SUCCESS);
	}

	public void onFail() {
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.FAIL);
	}

	public void onAlreadyFinished() {
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.FAIL_ALREADY_FINISHED);
	}

	public void onNotCompleted() {
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.FAIL_NOT_COMPLETED);
	}

	public void onInvalidRewardIndexes() {
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.FAIL_NOT_VALID_REWARD_INDEXES);
	}

	public void onObsolete() {
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.FAIL_OBSOLETE);
	}

	static class MJBeginnerFinishedDevelopView extends MJBeginnerFinishedView {

		MJBeginnerFinishedDevelopView(L1PcInstance pc, int questId, List<Integer> optionalRewardIndexes) {
			super(pc, questId, optionalRewardIndexes);
		}

		public class MJBeginnerFinishedView extends SomeParentClass {

			//@override
			public void onSuccess() {
				System.out.println(String.format("MJBeginnerFinishedView -> 成功. 任務ID: %d", questId()));
				super.onSuccess();
			}

			//@override
			public void onFail() {
				System.out.println(String.format("MJBeginnerFinishedView -> 失敗. 任務ID: %d", questId()));
				super.onFail();
			}

			//@override
			public void onAlreadyFinished() {
				System.out.println(String.format("MJBeginnerFinishedView -> 已經完成. 任務ID: %d", questId()));
				super.onAlreadyFinished();
			}

			//@override
			public void onNotCompleted() {
				System.out.println(String.format("MJBeginnerFinishedView -> 未完成. 任務ID: %d", questId()));
				super.onNotCompleted();
			}

			//@override
			public void onInvalidRewardIndexes() {
				System.out.println(String.format("MJBeginnerFinishedView -> 獎勵索引無效. 任務ID: %d", questId()));
				super.onInvalidRewardIndexes();
			}

			//@override
			public void onObsolete() {
				System.out.println(String.format("MJBeginnerFinishedView -> 已過期. 任務ID: %d", questId()));
				super.onObsolete();
			}

			private int questId() {
				// 假設這個方法返回當前任務的 ID
				// 這裡應該有實際的邏輯來獲取任務 ID
				return 0;
			}

		}
	}
}
