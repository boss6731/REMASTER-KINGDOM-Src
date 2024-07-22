package MJShiftObject.Battle.DomTower;

import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJDomTowerCharacterInfo extends MJShiftBattleCharacterInfo {
	public static MJDomTowerCharacterInfo newInstance(L1PcInstance pc, int destination_id, String source_name, MJDomTowerTeamInfo tInfo) {
		return new MJDomTowerCharacterInfo(pc, destination_id, source_name, tInfo);
	}

	private MJDomTowerCharacterInfo(L1PcInstance pc, int destination_id, String source_name, MJDomTowerTeamInfo tInfo) {
		super(pc, destination_id, source_name, tInfo);
	}

	public String to_name_pair() {
		return String.format("(%s)%s", new Object[]{this.home_server_name, this.owner_name});
	}
}


