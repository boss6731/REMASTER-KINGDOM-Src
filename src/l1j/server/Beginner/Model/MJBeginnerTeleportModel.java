package l1j.server.Beginner.Model;

import l1j.server.Beginner.View.MJBeginnerTeleportView;
import l1j.server.server.model.Instance.L1PcInstance;

class MJBeginnerTeleportModel implements MJBeginnerModel<MJBeginnerTeleportView>{
	private MJBeginnerModelProvider provider;
	MJBeginnerTeleportModel(MJBeginnerModelProvider provider){
		this.provider = provider;
	}
	
	@Override
	public void on(final MJBeginnerTeleportView view) {
		MJBeginnerQuestData questData = provider.questDataModel().questData(view.questId());
		if(questData == null || !questData.hasTeleport()){
			view.onWrongLocation();
			return;
		}
		
		final L1PcInstance pc = view.pc();
		final MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(pc);
		if(user == null){
			view.onWrongLocation();
			return;
		}
		
		user.state(view.questId(), new MJBeginnerUserStateListener(){
			@Override
			public void onStarted(boolean completed) {
				if(questData.teleport().onTeleport(pc)){
					view.onSuccess();
				}else{
					view.onNotEnoughAdena();
				}
			}

			@Override
			public void onFinished() {
				view.onCantTeleportNow();
			}

			@Override
			public void onRevealed() {
				if(questData.teleport().onTeleport(pc)){
					view.onSuccess();
				}else{
					view.onNotEnoughAdena();
				}
			}

			@Override
			public void onNotFound() {
				view.onFail();
			}
		});
	}
	
	static class MJBeginnerTeleportSkipModel extends MJBeginnerTeleportModel{
		MJBeginnerTeleportSkipModel(MJBeginnerModelProvider provider) {
			super(provider);
		}
		
		@Override
		public void on(final MJBeginnerTeleportView view) {
			super.on(view);
		}
	}
}
