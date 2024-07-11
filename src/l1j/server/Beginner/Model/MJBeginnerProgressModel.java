package l1j.server.Beginner.Model;

import l1j.server.Beginner.View.MJBeginnerProgressView;

class MJBeginnerProgressModel implements MJBeginnerModel<MJBeginnerProgressView> {
	private MJBeginnerModelProvider provider;

	MJBeginnerProgressModel(MJBeginnerModelProvider provider) {
		this.provider = provider;
	}

	@Override
	public void on(final MJBeginnerProgressView view) {
		MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(view.pc());
		if (user != null) {
			return;
		}

		user = MJBeginnerUserProvider.provider().onNewUser(view.pc());
		MJBeginnerQuestData latestQuestData = null;
		for (MJBeginnerUserProgress progress : user.values()) {
			if (progress.finished()) {
				view.beginView(progress.convertClientSKipModel());
				continue;
			}
			MJBeginnerQuestData questData = provider.questDataModel().questData(progress.questId());
			if (questData == null) {
				continue;
			}
			if (questData.hasTeleport()) {
				latestQuestData = questData;
			}
			progress.registeredEvent(view.pc());
			progress.registeredSubEvent(view.pc());
			view.beginView(progress.convertClientModel());
		}
		// TODO 如果您還有任何剩餘的任務，請傳送到那裡（登入時！）
		/*
		 * if(latestQuestData != null){
		 * latestQuestData.teleport().onTeleport(view.pc());
		 * }
		 */
		view.onView();
	}

	static class MJBeginnerProgressSkipModel extends MJBeginnerProgressModel {
		MJBeginnerProgressSkipModel(MJBeginnerModelProvider provider) {
			super(provider);
		}

		@Override
		public void on(final MJBeginnerProgressView view) {
			MJBeginnerUser user = MJBeginnerUserProvider.provider().convertUser(view.pc());
			if (user != null) {
				return;
			}
			user = MJBeginnerUserProvider.provider().onNewUser(view.pc());
			for (MJBeginnerQuestData questData : MJBeginnerModelProvider.provider().questDataModel().questValues()) {
				MJBeginnerUserProgress progress = user.onSkipped(view.pc(), questData.id());
				view.beginView(progress.convertClientSKipModel());
			}
			view.onView();
		}
	}
}
