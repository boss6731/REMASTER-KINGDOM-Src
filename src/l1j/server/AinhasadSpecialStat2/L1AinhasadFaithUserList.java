/*package l1j.server.AinhasadSpecialStat2;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.model.Instance.L1PcInstance;

public class L1AinhasadFaithUserList {
	private final L1PcInstance owner;
//	private final ConcurrentHashMap<>;
	private final ArrayList<L1AinhasadFaithUserObject> listData;
	
	public L1AinhasadFaithUserList(L1PcInstance pc) {
		this.owner 		= pc;
//		this.mapData 	= new ConcurrentHashMap<>();
		this.listData	= new ArrayList<>();
		init();
	}
	
	private void init() {
		ArrayList<L1AinhasadFaithUserObject> userList = L1AinhasadFaithUserLoader.getAinhasadFaithUserList(owner.getId());
		if (userList != null && !userList.isEmpty()) {
			for (L1AinhasadFaithUserObject user : userList) {

//				ConcurrentHashMap<Integer, L1AinhasadFaithUserObject> indexMap = 
				
				AinhasadSpecialStat2Info info = AinhasadSpecialStat2Loader.getInstance().getSpecialStat(user.getIndex());
				if (info != null) {
					AinhasadSpecialStat2Info.einhasad_faith_option(owner, info.get_index(), true);
				}
			}
		}
	}
}*/
