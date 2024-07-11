package l1j.server.DeathMatch;

import java.util.ArrayList;

import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;

public class DeathMatchSystem {
	private static DeathMatchSystem _instance;
	
	public static DeathMatchSystem getInstance() {
		if (_instance == null) {
			_instance = new DeathMatchSystem();
		}
		return _instance;
	}
	
	public void DeathMatchStart() {
		DeathMatch DM = new DeathMatch(13005);
		
		DM.Start();
	}
	public void Reset() {
		try {
			Object_Delete();
			TeamListDelete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<L1PcInstance> TeamRedList = new ArrayList<L1PcInstance>();
	private int getPlayMemberTRCount() {
		return TeamRedList.size();
	}
	public void addPlayMemberTR(L1PcInstance pc) {
		TeamRedList.add(pc);
	}
	
	public L1PcInstance[] getTeamRedList() {
		return (L1PcInstance[]) TeamRedList.toArray(new L1PcInstance[getPlayMemberTRCount()]);
	}
	
	public ArrayList<L1PcInstance> getTeamRed() {
		return TeamRedList;
	}
	public ArrayList<L1PcInstance> getTeamBlue() {
		return TeamBlueList;
	}
	
	
	private ArrayList<L1PcInstance> TeamBlueList = new ArrayList<L1PcInstance>();
	private int getPlayMemberTBCount() {
		return TeamBlueList.size();
	}
	public void addPlayMemberTB(L1PcInstance pc) {
		TeamBlueList.add(pc);
	}
	
	
	public L1PcInstance[] getTeamBlueList() {
		return (L1PcInstance[]) TeamBlueList.toArray(new L1PcInstance[getPlayMemberTBCount()]);
	}
	
	private void TeamListDelete() {
		TeamRedList.clear();
		TeamBlueList.clear();
	}
	
	private void Object_Delete() {
		for (L1Object ob : L1World.getInstance().getVisibleObjects(13006).values()) {
			if (ob == null || ob instanceof L1DollInstance
					|| ob instanceof L1SummonInstance
					|| ob instanceof L1PetInstance)
				continue;
			if (ob instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) ob;
				npc.deleteMe();
			}
		}
		for (L1ItemInstance obj : L1World.getInstance().getAllItem()) {
			if (obj.getMapId() != 13006)
				continue;
			L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
			groundInventory.removeItem(obj);
		}
		
		for (L1Object ob : L1World.getInstance().getVisibleObjects(13005).values()) {
			if (ob == null || ob instanceof L1DollInstance
					|| ob instanceof L1SummonInstance
					|| ob instanceof L1PetInstance)
				continue;
			if (ob instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) ob;
				npc.deleteMe();
			}
		}
		
		for (L1ItemInstance obj : L1World.getInstance().getAllItem()) {
			if (obj.getMapId() != 13005)
				continue;
			L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
			groundInventory.removeItem(obj);
		}
	}
}
