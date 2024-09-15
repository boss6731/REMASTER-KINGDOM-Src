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
		// דזסעלעÙגעה׃רמÜÜזר׀Û¡בך
		pc().sendPackets(ack, MJEProtoMessages.SC_QUEST_PROGRESS_ACK);
	}

	public int numOfQuestProgresses() {
		// ÚקüÞלעÙגעה׃רײ×רתמÜג¦ױב£¬ו‎ֽ‎ײ×רת?ֽצצ־ÚקüÞ 0
		return ack.get_quest_list() == null ? 0 : ack.get_quest_list().size();
	}

	static class MJBeginnerProgressDevelopView extends MJBeginnerProgressView {
		MJBeginnerProgressDevelopView(L1PcInstance pc) {
			super(pc);
		}

		//@override
		public void onView() {
			// גֳץף׃׳מסטּÊ«Ù£צאûתלעÙגעה׃רג¦ױב
			System.out.println(String.format("MJBeginnerProgressView -> onView. טּÊ«Ù£: %s, לעÙגעה׃רג¦ױב: %d", pc().getName(), numOfQuestProgresses()));
			super.onView();
		}
	}
}
