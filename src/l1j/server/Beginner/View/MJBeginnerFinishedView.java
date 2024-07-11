package l1j.server.Beginner.View;

import java.util.List;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_FINISH_ACK;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJBeginnerFinishedView implements MJBeginnerView {
	private L1PcInstance pc;
	private int questId;
	private List<Integer> optionalRewardIndexes;
	MJBeginnerFinishedView(L1PcInstance pc, int questId, List<Integer> optionalRewardIndexes){
		this.pc = pc;
		this.questId = questId;
		this.optionalRewardIndexes = optionalRewardIndexes;
	}
	
	public L1PcInstance pc(){
		return pc;
	}
	
	public int questId(){
		return questId;
	}
	
	public List<Integer> optionalRewardIndexes(){
		return optionalRewardIndexes;
	}
	
	private void viewInternal(SC_QUEST_FINISH_ACK.eResultCode resultCode){
		// 新任務註釋
		SC_QUEST_FINISH_ACK ack = SC_QUEST_FINISH_ACK.newInstance();
		ack.set_id(questId());
		ack.set_result(resultCode);
		pc().sendPackets(ack, MJEProtoMessages.SC_QUEST_FINISH_ACK);
	}
	
	public void onSuccess(){
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.SUCCESS);
	}
	
	public void onFail(){
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.FAIL);
	}
	
	public void onAlreadyFinished(){
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.FAIL_ALREADY_FINISHED);
	}
	
	public void onNotCompleted(){
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.FAIL_NOT_COMPLETED);
	}
	
	public void onInvalidRewardIndexes(){
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.FAIL_NOT_VALID_REWARD_INDEXES);
	}
	
	public void onObsolete(){
		viewInternal(SC_QUEST_FINISH_ACK.eResultCode.FAIL_OBSOLETE);
	}
	
	static class MJBeginnerFinishedDevelopView extends MJBeginnerFinishedView{

		MJBeginnerFinishedDevelopView(L1PcInstance pc, int questId, List<Integer> optionalRewardIndexes) {
			super(pc, questId, optionalRewardIndexes);
		}
		
		@Override
		public void onSuccess(){
			System.out.println(String.format("MJBeginnerFinishedView -> onSuccess. %d", questId()));
			super.onSuccess();
		}
		
		@Override
		public void onFail(){
			System.out.println(String.format("", questId()));
			super.onFail();
		}
		
		@Override
		public void onAlreadyFinished(){
			System.out.println(String.format("MJBeginnerFinishedView -> onAlreadyFinished. %d", questId()));
			super.onAlreadyFinished();
		}
		
		@Override
		public void onNotCompleted(){
			System.out.println(String.format("MJBeginnerFinishedView -> onNotCompleted. %d", questId()));
			super.onNotCompleted();
		}
		
		@Override
		public void onInvalidRewardIndexes(){
			System.out.println(String.format("MJBeginnerFinishedView -> onInvalidRewardIndexes. %d", questId()));
			super.onInvalidRewardIndexes();
		}
		
		@Override
		public void onObsolete(){
			System.out.println(String.format("MJBeginnerFinishedView -> onObsolete. %d", questId()));
			super.onObsolete();
		}
	}
}
