package l1j.server.server.model.Instance;

import l1j.server.MJRaidSystem.MJRaidObject;
import l1j.server.server.templates.L1Npc;

public class L1RaidDoorInstance extends L1NpcInstance{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MJRaidObject _raidObj;
	
	public L1RaidDoorInstance(L1Npc template) {
		super(template);
	}

	public void setRaidObject(MJRaidObject obj){
		_raidObj = obj;
	}

	public MJRaidObject getRaidObject(){
		return _raidObj;
	}

	@Override
	public void onTalkAction(L1PcInstance pc){
		if(_raidObj == null)
			return;
		
		_raidObj.doorMove(pc);
	}
	
	@Override
	public void deleteMe() {
//		new Throwable(String.format("[%s]L1RaidDoorInstance::deleted...", MJString.get_current_datetime())).printStackTrace();
		super.deleteMe();
	}
}
