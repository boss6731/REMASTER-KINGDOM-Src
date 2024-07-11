package l1j.server.Beginner.View;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.QuestProgress;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_PROGRESS_ACK;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJBeginnerProgressView implements MJBeginnerView {
	private L1PcInstance pc;
	private SC_QUEST_PROGRESS_ACK ack;

	MJBeginnerProgressView(L1PcInstance pc) {
		this.pc = pc;
		this.ack = SC_QUEST_PROGRESS_ACK.newInstance();
	}

	public L1PcInstance pc() {
		return pc;
	}

	public void beginView(QuestProgress questProgress) {
		ack.add_quest_list(questProgress);
	}

	public void onView() {
		// 新任務註釋
		pc().sendPackets(ack, MJEProtoMessages.SC_QUEST_PROGRESS_ACK);
	}

	public int numOfQuestProgresses() {
		return ack.get_quest_list() == null ? 0 : ack.get_quest_list().size();
	}

	static class MJBeginnerProgressDevelopView extends MJBeginnerProgressView {
		MJBeginnerProgressDevelopView(L1PcInstance pc) {
			super(pc);
		}

		@Override
		public void onView() {
			System.out.println(String.format("MJBeginnerProgressView -> onView. %s %d items.", pc().getName(),
					numOfQuestProgresses()));
			super.onView();
		}
	}
}
