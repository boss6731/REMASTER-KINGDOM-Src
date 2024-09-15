package MJShiftObject.Battle;

import l1j.server.server.model.Instance.L1PcInstance;

public class MJShiftBattleCharacterInfo {
	public L1PcInstance owner;
	public String owner_name;
	public String home_server_name;
	public int owner_id;
	public MJShiftBattleTeamInfo<? extends MJShiftBattleCharacterInfo> team_info;
	protected MJShiftBattleCharacterInfo(L1PcInstance pc, int destination_id, String source_name, MJShiftBattleTeamInfo<? extends MJShiftBattleCharacterInfo> tInfo){
		owner = pc;
		owner_id = destination_id;
		home_server_name = pc.get_server_description();
		owner_name = source_name;
		team_info = tInfo;
	}
	
	public String to_name_pair(){
		return String.format("(%s)%s", home_server_name, owner_name);
	}
}
