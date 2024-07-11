package l1j.server.Beginner.Model;

import l1j.server.Beginner.View.MJBeginnerRevealView;
import l1j.server.server.model.Instance.L1PcInstance;

class MJBeginnerRevealModel implements MJBeginnerModel<MJBeginnerRevealView>{
	private MJBeginnerModelProvider provider;
	MJBeginnerRevealModel(MJBeginnerModelProvider provider){
		this.provider = provider;
	}
	
	@Override
	public void on(final MJBeginnerRevealView view) {
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
				view.onAlreadyRevealed();
			}

			@Override
			public void onNotFound() {
				if(questData.hasPrerequisite() && !questData.prerequisite().matches(pc)){
					view.onFail();
					return;
				}
				view.onSuccess();
			}
		});
	}
	
	static class MJBeginnerRevealSkipModel extends MJBeginnerRevealModel{

		MJBeginnerRevealSkipModel(MJBeginnerModelProvider provider) {
			super(provider);
		}
		
		@Override
		public void on(final MJBeginnerRevealView view) {
			super.on(view);
		}
	}
}
