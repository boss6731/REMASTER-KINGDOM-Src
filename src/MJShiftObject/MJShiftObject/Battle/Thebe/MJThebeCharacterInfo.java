package MJShiftObject.Battle.Thebe;

import MJShiftObject.Battle.MJShiftBattleCharacterInfo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_BASECAMP_POINTRANK_NOTI_PACKET;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJThebeCharacterInfo
		extends MJShiftBattleCharacterInfo {
	public static MJThebeCharacterInfo newInstance(L1PcInstance pc, int destination_id, String source_name, MJThebeTeamInfo tInfo) {
		return new MJThebeCharacterInfo(pc, destination_id, source_name, tInfo);
	}

	public long battle_point;
	public int battle_rank;

	private MJThebeCharacterInfo(L1PcInstance pc, int destination_id, String source_name, MJThebeTeamInfo tInfo) {
		super(pc, destination_id, source_name, tInfo);

		this.battle_point = 0L;
		this.battle_rank = 0;
	}

	public SC_BASECAMP_POINTRANK_NOTI_PACKET.PointRankInfoT to_rank_info() {
		SC_BASECAMP_POINTRANK_NOTI_PACKET.PointRankInfoT rInfo = SC_BASECAMP_POINTRANK_NOTI_PACKET.PointRankInfoT.newInstance();
		try {
			rInfo.set_user_name(to_name_pair().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		rInfo.set_user_points(this.battle_point);
		rInfo.set_user_rank(this.battle_rank);
		return rInfo;
	}

	public void on_update_rank(L1PcInstance pc, ArrayList<MJThebeCharacterInfo> ranks) {
		SC_BASECAMP_POINTRANK_NOTI_PACKET noti = SC_BASECAMP_POINTRANK_NOTI_PACKET.newInstance();
		int size = Math.min(10, ranks.size());
		MJThebeCharacterInfo bInfo = null;
		for (int i = 0; i < size; i++) {
			bInfo = ranks.get(i);
			noti.add_top_rankers(bInfo.to_rank_info());
		}
		noti.set_my_rank(to_rank_info());
		noti.set_team_points(this.team_info.team_point.longValue());
		pc.sendPackets((MJIProtoMessage) noti, MJEProtoMessages.SC_BASECAMP_POINTRANK_NOTI_PACKET, true);
	}
}


