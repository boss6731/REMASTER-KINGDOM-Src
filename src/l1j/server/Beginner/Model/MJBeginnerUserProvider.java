package l1j.server.Beginner.Model;

import l1j.server.MJTemplate.Attribute.MJAttrKey;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_QUEST_PROGRESS_UPDATE_NOTI;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJBeginnerUserProvider {
	private static final MJAttrKey<MJBeginnerUser> beginnerUserKey = MJAttrKey.newInstance("mj-beginner-user");
	private static final MJBeginnerUserProvider provider = new MJBeginnerUserProvider();

	public static final MJBeginnerUserProvider provider() {
		return provider;
	}

	private MJBeginnerUserProvider() {
	}

	public void onProgressNoti(L1PcInstance pc) {
		MJBeginnerUser user = convertUser(pc);
		if (user == null) {
			return;
		}
		int usage = 0;
		for (MJBeginnerUserProgress progress : user.values()) {
			if (progress.finished()) {
				continue;
			}
			SC_QUEST_PROGRESS_UPDATE_NOTI noti = SC_QUEST_PROGRESS_UPDATE_NOTI.newInstance();
			noti.set_quest(progress.convertClientModel());
			// 新任務註釋
			pc.sendPackets(noti, MJEProtoMessages.SC_QUEST_PROGRESS_UPDATE_NOTI);
			System.out.println("MJBeginnerUserProvider:" + ++usage);
		}

	}

	MJBeginnerUser convertUser(L1PcInstance pc) {
		if (!pc.attribute().has(beginnerUserKey)) {
			return null;
		}
		return pc.attribute().get(beginnerUserKey).get();
	}

	private void onNewUserInternal(L1PcInstance pc, MJBeginnerUser user) {
		pc.attribute().getNotExistsNew(beginnerUserKey).set(user);
	}

	MJBeginnerUser onNewUser(L1PcInstance pc) {
		MJBeginnerUser user = MJBeginnerUserDatabaseProvider.provider().selectUserProgress(pc.getId());
		onNewUserInternal(pc, user);
		return user;
	}

}
