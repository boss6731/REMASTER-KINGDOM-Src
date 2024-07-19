package l1j.server.Beginner.View;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_START_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_START_ACK.eResultCode;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJBeginnerStartView implements MJBeginnerView {

	// 私有成員變量，用於存儲玩家實例和任務 ID
	private L1PcInstance pc;
	private int questId;

	// 構造函數，初始化私有成員變量
	MJBeginnerStartView(L1PcInstance pc, int questId) {
		this.pc = pc;
		this.questId = questId;
	}

	// 公共方法，用於獲取玩家實例
	public L1PcInstance pc() {
		return pc;
	}

	// 公共方法，用於獲取任務 ID
	public int questId() {
		return questId;
	}

	// 私有方法，用於內部處理視圖
	private void viewInternal(SC_QUEST_START_ACK.eResultCode resultCode) {
		SC_QUEST_START_ACK ack = SC_QUEST_START_ACK.newInstance();
		ack.set_id(questId());
		ack.set_result(resultCode);
		// 新的任務註釋
		pc.sendPackets(ack, MJEProtoMessages.SC_QUEST_START_ACK);
	}

	// 公共方法，表示成功時的處理邏輯
	public void onSuccess() {
		viewInternal(SC_QUEST_START_ACK.eResultCode.SUCCESS);
	}

	// 公共方法，表示失敗時的處理邏輯
	public void onFail() {
		viewInternal(SC_QUEST_START_ACK.eResultCode.FAIL);
	}

	// 公共方法，表示任務已經開始時的處理邏輯
	public void onAlreadyStarted() {
		viewInternal(SC_QUEST_START_ACK.eResultCode.FAIL_ALREADY_STARTED);
	}

	// 公共方法，表示任務已經完成時的處理邏輯
	public void onAlreadyFinished() {
		viewInternal(SC_QUEST_START_ACK.eResultCode.FAIL_ALREADY_FINISHED);
	}

	// 公共方法，表示任務已過期時的處理邏輯
	public void onObsolete() {
		viewInternal(SC_QUEST_START_ACK.eResultCode.FAIL_OBSOLETE);
	}

	// 靜態內部類，繼承自 MJBeginnerStartView
	static class MJBeginnerStartDevelopView extends MJBeginnerStartView {

		// 構造函數，調用父類的構造函數
		MJBeginnerStartDevelopView(L1PcInstance pc, int questId) {
			super(pc, questId);
		}

		// 覆寫 onSuccess 方法，添加日誌記錄並調用父類方法
		@override
		public void onSuccess() {
			System.out.println(String.format("MJ新手開始視圖 -> 成功. %d", questId()));
			super.onSuccess();
		}

		// 覆寫 onFail 方法，添加日誌記錄並調用父類方法
		@override
		public void onFail() {
			System.out.println(String.format("MJ新手開始視圖 -> 失敗. %d", questId()));
			super.onFail();
		}

		// 覆寫 onAlreadyStarted 方法，添加日誌記錄並調用父類方法
		@override
		public void onAlreadyStarted() {
			System.out.println(String.format("MJ新手開始視圖 -> 任務已經開始. %d", questId()));
			super.onAlreadyStarted();
		}

		// 覆寫 onAlreadyFinished 方法，添加日誌記錄並調用父類方法
		@override
		public void onAlreadyFinished() {

			System.out.println(String.format("MJ新手開始視圖 -> 任務已經完成. %d", questId()));
			super.onAlreadyFinished();
		}

		// 覆寫 onObsolete 方法，添加日誌記錄並調用父類方法
		@override
		public void onObsolete() {
			System.out.println(String.format("MJ新手開始視圖 -> 任務已過時. %d", questId()));
			super.onObsolete();
		}
	}
}
