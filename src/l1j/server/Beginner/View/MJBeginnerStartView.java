package l1j.server.Beginner.View;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_START_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_START_ACK.eResultCode;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJBeginnerStartView implements MJBeginnerView {
	private L1PcInstance pc;
	private int questId;

	// 構造方法，用於初始化 pc 和 questId
	MJBeginnerStartView(L1PcInstance pc, int questId) {
		this.pc = pc;
		this.questId = questId;
	}

	// 取得 pc 的方法
	public L1PcInstance pc() {
		return pc;
	}

	// 取得 questId 的方法
	public int questId() {
		return questId;
	}

	// 私有方法，用於處理內部視圖邏輯
	private void viewInternal(eResultCode resultCode) {
		// 創建一個 SC_QUEST_START_ACK 封包
		SC_QUEST_START_ACK ack = SC_QUEST_START_ACK.newInstance();
		// 設置封包的任務 ID
		ack.set_id(questId);
		// 設置封包的結果代碼
		ack.set_result(resultCode);

		// 發送任務開始確認封包
		pc.sendPackets(ack, MJEProtoMessages.SC_QUEST_START_ACK);
	}

	
	public void onSuccess(){
		viewInternal(eResultCode.SUCCESS);
	}
	
	public void onFail(){
		viewInternal(eResultCode.FAIL);
	}
	
	public void onAlreadyStarted(){
		viewInternal(eResultCode.FAIL_ALREADY_STARTED);
	}
	
	public void onAlreadyFinished(){
		viewInternal(eResultCode.FAIL_ALREADY_FINISHED);
	}
	
	public void onObsolete(){
		viewInternal(eResultCode.FAIL_OBSOLETE);
	}

	static class MJBeginnerStartDevelopView extends MJBeginnerStartView {
		MJBeginnerStartDevelopView(L1PcInstance pc, int questId) {
			super(pc, questId);
		}

		//@override
		public void onSuccess() {
			System.out.println(String.format("MJBeginnerStartView -> 成功. 任務ID: %d", questId()));
			super.onSuccess();
		}

		//@override
		public void onFail() {
			System.out.println(String.format("MJBeginnerStartView -> 失敗. 任務ID: %d", questId()));
			super.onFail();
		}

		//@override
		public void onAlreadyStarted() {
			System.out.println(String.format("MJBeginnerStartView -> 已開始. 任務ID: %d", questId()));
			super.onAlreadyStarted();
		}

		//@override
		public void onAlreadyFinished() {
			System.out.println(String.format("MJBeginnerStartView -> 已完成. 任務ID: %d", questId()));
			super.onAlreadyFinished();
		}

		//@override
		public void onObsolete() {
			System.out.println(String.format("MJBeginnerStartView -> 已過期. 任務ID: %d", questId()));
			super.onObsolete();
		}
	}
}
