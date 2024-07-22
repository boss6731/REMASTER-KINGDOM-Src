package MJShiftObject.Battle.ForgottenIsland;

import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJFIslandCharacterInfo extends MJShiftBattleCharacterInfo {
	public static MJFIslandCharacterInfo newInstance(L1PcInstance pc, int destination_id, String source_name, MJFIslandTeamInfo tInfo) {
		return new MJFIslandCharacterInfo(pc, destination_id, source_name, tInfo);
	}

	private MJFIslandCharacterInfo(L1PcInstance pc, int destination_id, String source_name, MJFIslandTeamInfo tInfo) {
		super(pc, destination_id, source_name, tInfo);
	}

	public String to_name_pair() {
		return String.format("(%s)%s", new Object[]{this.home_server_name, this.owner_name});
	}
}


