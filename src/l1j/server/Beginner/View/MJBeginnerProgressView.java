package l1j.server.Beginner.View;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.QuestProgress;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_PROGRESS_ACK;
import l1j.server.server.model.Instance.L1PcInstance;

public class BeginnerProgressView implements MJBeginnerView {
	private L1PcInstance pc;
	private SC_QUEST_PROGRESS_ACK ack;

	// 構造函數，初始化 pc 和 ack 實例
	BeginnerProgressView(L1PcInstance pc) {
		this.pc = pc;
		this.ack = SC_QUEST_PROGRESS_ACK.newInstance();
	}

	// 獲取 pc 實例的方法
	public L1PcInstance pc() {
		return pc;
	}

	// 添加任務進度到 ack 實例的方法
	public void beginView(QuestProgress questProgress) {
		ack.add_quest_list(questProgress);
	}

	// 發送 ack 封包的方法
	public void onView() {
		// 新任務註釋
		pc().sendPackets(ack, MJEProtoMessages.SC_QUEST_PROGRESS_ACK);
	}

	// 獲取任務進度數量的方法
	public int numOfQuestProgresses() {
		return ack.get_quest_list() == null ? 0 : ack.get_quest_list().size();
	}

	// 靜態內部類，用於開發模式
	static class BeginnerProgressDevelopView extends BeginnerProgressView {
		// 構造函數，調用父類構造函數
		BeginnerProgressDevelopView(L1PcInstance pc) {
			super(pc);
		}

		// 覆寫 onView 方法，打印調試信息
		@override
		public void onView() {
			System.out.println(String.format("新手進度視圖 -> 正在查看. %s %d 獎勵物品.", pc().getName(), numOfQuestProgresses()));
			super.onView();
		}
	}
}
