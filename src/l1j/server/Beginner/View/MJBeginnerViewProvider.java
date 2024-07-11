package l1j.server.Beginner.View;

import java.util.List;

import l1j.server.Beginner.MJBeginnerService;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJBeginnerViewProvider {
	private static final MJBeginnerViewProvider provider = new MJBeginnerViewProvider();
	public static MJBeginnerViewProvider provider(){
		return provider;
	}
	
	private boolean developMode;
	private MJBeginnerViewProvider(){
		developMode = false;
	}
	
	public void initialize(MJBeginnerService service){
	}
	
	public void onDevelopModeChanged(boolean developMode){
		this.developMode = developMode;
	}
	
	public MJBeginnerRevealView newRevealView(L1PcInstance pc, int questId){
		return developMode ? new MJBeginnerRevealView.MJBeginnerRevealDevelopView(pc, questId) : new MJBeginnerRevealView(pc, questId);
	}
	
	public MJBeginnerProgressView newProgressView(L1PcInstance pc){
		return developMode ? new MJBeginnerProgressView.MJBeginnerProgressDevelopView(pc) : new MJBeginnerProgressView(pc);
	}
	
	public MJBeginnerStartView newStartView(L1PcInstance pc, int questId){
		return developMode ? new MJBeginnerStartView.MJBeginnerStartDevelopView(pc, questId) : new MJBeginnerStartView(pc, questId);
	}
	
	public MJBeginnerFinishedView newFinishedView(L1PcInstance pc, int questId, List<Integer> optionalRewardIndexes){
		return developMode ? new MJBeginnerFinishedView.MJBeginnerFinishedDevelopView(pc, questId, optionalRewardIndexes) : new MJBeginnerFinishedView(pc, questId, optionalRewardIndexes);
	}
	
	public MJBeginnerTeleportView newTeleportView(L1PcInstance pc, int questId){
		return developMode ? new MJBeginnerTeleportView.MJBeginnerTeleportDevelopView(pc, questId) : new MJBeginnerTeleportView(pc, questId);
	}
}
