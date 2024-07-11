package l1j.server.IndunEx.RoomInfo;

import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.ArenaUserInfo.eRole;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eArenaTeam;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJIndunRoomMemberModel {
	static MJIndunRoomMemberModel newInstance(L1PcInstance pc){
		MJIndunRoomMemberModel model = new MJIndunRoomMemberModel();
		model.member = pc;
		return model;
	}
	
	public L1PcInstance member;
	boolean room_owner;
	boolean ready;
	eRole role;
	eArenaTeam team;
	private MJIndunRoomMemberModel(){
		room_owner = false;
		ready = false;
		role = eRole.Player;
		team = eArenaTeam.TEAM_A;
	}
}
