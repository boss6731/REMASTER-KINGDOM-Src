package l1j.server.InfinityBattle;

import java.util.ArrayList;
import java.util.Random;

import l1j.server.server.model.Instance.L1PcInstance;

public abstract class InfinityBattleInfo {
	
	public enum Status {READY, ROUND_1, ROUND_2, ROUND_3, END, REST};
	
	public Status MiniGameStatus;
	
	protected final Random _random = new Random();
	
	protected ArrayList<L1PcInstance> _1st_team_members = new ArrayList<L1PcInstance>();
	protected ArrayList<L1PcInstance> _2nd_team_members = new ArrayList<L1PcInstance>();
	protected ArrayList<L1PcInstance> _3rd_team_members = new ArrayList<L1PcInstance>();
	protected ArrayList<L1PcInstance> _4th_team_members = new ArrayList<L1PcInstance>();
	
	protected static final short INFINITI_BATTLE_MAPID = 750;
	
	protected abstract void addTeamMembers(L1PcInstance pc);
	
	protected abstract void CheckInfinityBattlePlayer();
	protected abstract void NoReadyInfinityBattle();
	protected abstract void EndInfinityBattle();
	protected abstract void ClearInfinityBattle();

	protected void setMiniGameStatus(Status i) 	{ MiniGameStatus = i;		}
	public Status getMiniGameStatus() 			{ return MiniGameStatus;	}
	
	public void addFirstTeamMember(L1PcInstance pc) 	{ _1st_team_members.add(pc); }
	public void removeFirstTeamMember(L1PcInstance pc) 	{ _1st_team_members.remove(pc); }
	public boolean isFirstTeamMember(L1PcInstance pc) 	{ return _1st_team_members.contains(pc); 	} 
	
	public void addSecondTeamMember(L1PcInstance pc) 	{ _2nd_team_members.add(pc); }
	public void removeSecondTeamMember(L1PcInstance pc) 	{ _2nd_team_members.remove(pc); }
	public boolean isSecondTeamMember(L1PcInstance pc) 	{ return _2nd_team_members.contains(pc); 	} 
	
	public void addThirdTeamMember(L1PcInstance pc) 	{ _3rd_team_members.add(pc); }
	public void removeThirdTeamMember(L1PcInstance pc) 	{ _3rd_team_members.remove(pc); }
	public boolean isThirdTeamMember(L1PcInstance pc) 	{ return _3rd_team_members.contains(pc); 	} 
	
	public void addFourthTeamMember(L1PcInstance pc) 	{ _4th_team_members.add(pc); }
	public void removeFourthTeamMember(L1PcInstance pc) 	{ _4th_team_members.remove(pc); }
	public boolean isFourthTeamMember(L1PcInstance pc) 	{ return _4th_team_members.contains(pc); 	} 
	
	public void clearPlayerMember() { 
		_1st_team_members.clear();
		_2nd_team_members.clear();
		_3rd_team_members.clear();
		_4th_team_members.clear();
	}
	
	public int getTeamMemberCount(int team) {
		int member_count = 0;
		switch(team) {
			case 0:
				member_count = _1st_team_members.size();
				break;
			case 1:
				member_count = _2nd_team_members.size();
				break;
			case 2:
				member_count = _3rd_team_members.size();
				break;
			case 3:
				member_count = _4th_team_members.size();
				break;
			case 4:
				member_count = _1st_team_members.size() + _2nd_team_members.size() + _3rd_team_members.size() + _4th_team_members.size();
				break;
		}
		return member_count;	 	
	}
}
