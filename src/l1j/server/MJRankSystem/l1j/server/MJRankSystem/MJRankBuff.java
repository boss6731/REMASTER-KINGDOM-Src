package l1j.server.MJRankSystem;

import l1j.server.MJRankSystem.Business.MJRankBusiness;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_TOP_RANKER_NOTI;
import l1j.server.server.model.Instance.L1PcInstance;

public abstract class MJRankBuff {
	public abstract void onBuff(SC_TOP_RANKER_NOTI rnk);

	public abstract void offBuff(SC_TOP_RANKER_NOTI rnk);

	public void onStat(SC_TOP_RANKER_NOTI rnk) {
		
		L1PcInstance pc = rnk.get_characterInstance();
		if (pc == null) {
			return;
		}
		// MJEClassesType.fromInt(rnk.get_class()).addBunusStat(pc, 1);
		if (pc.getNetConnection() != null) {
			MJRankBusiness.getInstance().ack_private_version(pc, 0L);
			MJRankBusiness.getInstance().noti(pc.getNetConnection(), pc.getId());
			// pc.sendPackets(rnk.writeTo(MJEProtoMessages.SC_TOP_RANKER_NOTI), true);
		}
	}

	public void offStat(SC_TOP_RANKER_NOTI rnk) {
		L1PcInstance pc = rnk.get_characterInstance();
		if (pc == null) {
			return;
		}

		// MJEClassesType.fromInt(rnk.get_class()).addBunusStat(pc, -1);
	}
}
