package l1j.server.Beginner.Model;

import l1j.server.Beginner.View.MJBeginnerStartView;
import l1j.server.server.model.Instance.L1PcInstance;

class MJBeginnerStartModel implements MJBeginnerModel<MJBeginnerStartView>{
	private MJBeginnerModelProvider provider;
	MJBeginnerStartModel(MJBeginnerModelProvider provider){
		this.provider = provider;
	}
	
	protected MJBeginnerModelProvider provider(){
		return provider;
	}
	
	@Override
	public void on(final MJBeginnerStartView view) {
		MJBeginnerQuestData questData = provider.questDataModel().questData(view.questId());
		if(questData == null){
			view.onObsolete();
			return;
		}
		
		final L1PcInstance pc = view.pc();
		final MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(pc);
		if(user == null){
			view.onObsolete();
			return;
		}
		
		user.state(view.questId(), new MJBeginnerUserStateListener(){
			@Override
			public void onStarted(boolean completed) {
				view.onAlreadyStarted();
			}

			@Override
			public void onFinished() {
				view.onAlreadyFinished();
			}

			@Override
			public void onRevealed() {
				view.onFail();
			}

			@Override
			public void onNotFound() {
				if(questData.hasPrerequisite() && !questData.prerequisite().matches(pc)){
					view.onFail();
					return;
				}
				user.onStart(view.pc(), view.questId());
				if(questData.hasAdvanceRewardList()){
					questData.advanceRewardList().onReward(view.pc());
				}
				view.onSuccess();
			}
		});
	}

	static class MJBeginnerStartSkipModel extends MJBeginnerStartModel{
		MJBeginnerStartSkipModel(MJBeginnerModelProvider provider) {
			super(provider);
		}
		
		@Override
		public void on(final MJBeginnerStartView view) {
			MJBeginnerQuestData questData = provider().questDataModel().questData(view.questId());
			if(questData == null){
				view.onObsolete();
				return;
			}
			
			final L1PcInstance pc = view.pc();
			final MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(pc);
			if(user == null){
				view.onObsolete();
				return;
			}
			
			user.state(view.questId(), new MJBeginnerUserStateListener(){
				@Override
				public void onStarted(boolean completed) {
					view.onAlreadyStarted();
				}

				@Override
				public void onFinished() {
					view.onAlreadyFinished();
				}

				@Override
				public void onRevealed() {
					view.onFail();
				}

				@Override
				public void onNotFound() {
					if(questData.hasPrerequisite() && !questData.prerequisite().matches(pc)){
						view.onFail();
						return;
					}
					MJBeginnerEventProvider.invokeUpdateNoti(view.pc(), user.onSkipped(pc, view.questId()).convertClientModel());
					view.onSuccess();
				}
			});
		}
	}
}
