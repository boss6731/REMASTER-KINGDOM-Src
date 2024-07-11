package l1j.server.Beginner.Model;

import java.util.List;

import l1j.server.Beginner.View.MJBeginnerFinishedView;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventProvider;

class MJBeginnerFinishModel implements MJBeginnerModel<MJBeginnerFinishedView>{
	private MJBeginnerModelProvider provider;
	MJBeginnerFinishModel(MJBeginnerModelProvider provider){
		this.provider = provider;
	}
	
	@Override
	public void on(final MJBeginnerFinishedView view) {
		final MJBeginnerQuestData questData = provider.questDataModel().questData(view.questId());
		if(questData == null){
			view.onObsolete();
			return;
		}
		
		MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(view.pc());
		if(user == null){
			view.onFail();
			return;
		}
		user.state(view.questId(), new MJBeginnerUserStateListener(){
			@Override
			public void onStarted(boolean completed) {
				if(!completed){
					view.onNotCompleted();
					return;
				}
				if(questData.hasOptionalRewardList()){
					List<Integer> optionalRewardIndexes = view.optionalRewardIndexes();
					if(optionalRewardIndexes == null || optionalRewardIndexes.size() <= 0){
						view.onInvalidRewardIndexes();
						return;
					}
					try{
						questData.optionalRewardList().rewards().get(optionalRewardIndexes.get(0)).onReward(view.pc());
					}catch(Exception e){
						view.onInvalidRewardIndexes();
						return;
					}
				}
				
				if(questData.hasRewardList()){
					questData.rewardList().onReward(view.pc());
				}
				user.onFinished(view.pc(), view.questId());
				view.onSuccess();
				MJObjectEventProvider.provider().pcEventFactory().fireQuestFinished(view.pc(), view.questId());
			}

			@Override
			public void onFinished() {
				view.onAlreadyFinished();
			}

			@Override
			public void onRevealed() {
				view.onNotCompleted();
			}

			@Override
			public void onNotFound() {
				view.onFail();
			}
		});
	}
	
	static class MJBeginnerFinishSkipModel extends MJBeginnerFinishModel{
		MJBeginnerFinishSkipModel(MJBeginnerModelProvider provider) {
			super(provider);
		}
		
		@Override
		public void on(final MJBeginnerFinishedView view) {
			view.onSuccess();
			MJObjectEventProvider.provider().pcEventFactory().fireQuestFinished(view.pc(), view.questId());
		}
	}
}
