package l1j.server.Beginner.View;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_START_ACK;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_START_ACK.eResultCode;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJBeginnerStartView implements MJBeginnerView {
	private L1PcInstance pc;
	private int questId;

	MJBeginnerStartView(L1PcInstance pc, int questId) {
		this.pc = pc;
		this.questId = questId;
	}

	public L1PcInstance pc() {
		return pc;
	}

	public int questId() {
		return questId;
	}

	private void viewInternal(eResultCode resultCode) {
		SC_QUEST_START_ACK ack = SC_QUEST_START_ACK.newInstance();
		ack.set_id(questId);
		ack.set_result(resultCode);
		// 新任務註釋
		pc.sendPackets(ack, MJEProtoMessages.SC_QUEST_START_ACK);
	}

	public void onSuccess() {
		viewInternal(eResultCode.SUCCESS);
	}

	public void onFail() {
		viewInternal(eResultCode.FAIL);
	}

	public void onAlreadyStarted() {
		viewInternal(eResultCode.FAIL_ALREADY_STARTED);
	}

	public void onAlreadyFinished() {
		viewInternal(eResultCode.FAIL_ALREADY_FINISHED);
	}

	public void onObsolete() {
		viewInternal(eResultCode.FAIL_OBSOLETE);
	}

	static class MJBeginnerStartDevelopView extends MJBeginnerStartView {
		MJBeginnerStartDevelopView(L1PcInstance pc, int questId) {
			super(pc, questId);
		}

		@Override
		public void onSuccess() {
			System.out.println(String.format("MJBeginnerStartView -> onSuccess. %d", questId()));
			super.onSuccess();
		}

		@Override
		public void onFail() {
			System.out.println(String.format("MJBeginnerStartView -> onFail. %d", questId()));
			super.onFail();
		}

		@Override
		public void onAlreadyStarted() {
			System.out.println(String.format("MJBeginnerStartView -> onAlreadyStarted. %d", questId()));
			super.onAlreadyStarted();
		}

		@Override
		public void onAlreadyFinished() {
			System.out.println(String.format("MJBeginnerStartView -> onAlreadyFinished. %d", questId()));
			super.onAlreadyFinished();
		}

		@Override
		public void onObsolete() {
			System.out.println(String.format("MJBeginnerStartView -> onObsolete. %d", questId()));
			super.onObsolete();
		}

	}
}
