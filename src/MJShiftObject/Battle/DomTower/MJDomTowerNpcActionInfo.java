package MJShiftObject.Battle.DomTower;

import java.util.HashMap;

import l1j.server.MJTemplate.Lineage2D.MJRectangle;
import l1j.server.server.utils.IntRange;

public class MJDomTowerNpcActionInfo {
	private static final HashMap<Integer, MJDomTowerNpcActionInfo> domTowerNpcToActions;
	private static final MJDomTowerNpcActionInfo[] domTowerNpcToActionsArray;
	static{
		domTowerNpcToActionsArray = new MJDomTowerNpcActionInfo[] {
				new MJDomTowerNpcActionInfo(8500028, new String[]{ "1" }, new int[]{12852}),
				new MJDomTowerNpcActionInfo(8500029, new String[]{ "2" }, new int[]{12853}),
				new MJDomTowerNpcActionInfo(8500030, new String[]{ "3" }, new int[]{12854}),
				new MJDomTowerNpcActionInfo(8500031, new String[]{ "4" }, new int[]{12855}),
				new MJDomTowerNpcActionInfo(8500032, new String[]{ "5" }, new int[]{12856}),
				new MJDomTowerNpcActionInfo(8500033, new String[]{ "6" }, new int[]{12857}),
				new MJDomTowerNpcActionInfo(8500034, new String[]{ "7" }, new int[]{12858}),
				new MJDomTowerNpcActionInfo(8500035, new String[]{ "8" }, new int[]{12859}),
				new MJDomTowerNpcActionInfo(8500036, new String[]{ "9" }, new int[]{12860}),
				new MJDomTowerNpcActionInfo(8500037, new String[]{ "10" }, new int[]{12861}),
				new MJDomTowerNpcActionInfo(8500038, new String[]{ "11", "12" }, new int[]{12862, 12863}),
		};
		// 12863 = 		// 최상층 중간
		
		domTowerNpcToActions = new HashMap<Integer, MJDomTowerNpcActionInfo>();
		for(MJDomTowerNpcActionInfo aInfo : domTowerNpcToActionsArray)
			domTowerNpcToActions.put(aInfo.npcid, aInfo);
	}
	public static final HashMap<Integer, MJRectangle> entry_rectangles;
	static{
		entry_rectangles = new HashMap<Integer, MJRectangle>();
		entry_rectangles.put(12852, MJRectangle.from_radius(32728, 32800, 3, 3, (short)12852));
		entry_rectangles.put(12853, MJRectangle.from_radius(32721, 32803, 3, 3, (short)12853));
		entry_rectangles.put(12854, MJRectangle.from_radius(32724, 32802, 3, 3, (short)12854));
		entry_rectangles.put(12855, MJRectangle.from_radius(32597, 32863, 3, 3, (short)12855));
		entry_rectangles.put(12856, MJRectangle.from_radius(32592, 32866, 3, 3, (short)12856));
		entry_rectangles.put(12857, MJRectangle.from_radius(32602, 32865, 3, 3, (short)12857));
		entry_rectangles.put(12858, MJRectangle.from_radius(32604, 32866, 3, 3, (short)12858));
		entry_rectangles.put(12859, MJRectangle.from_radius(32593, 32866, 3, 3, (short)12859));
		entry_rectangles.put(12860, MJRectangle.from_radius(32602, 32866, 3, 3, (short)12860));
		entry_rectangles.put(12861, MJRectangle.from_radius(32732, 32802, 3, 3, (short)12861));	
		entry_rectangles.put(12862, MJRectangle.from_radius(32639, 32796, 3, 3, (short)12862));	
		entry_rectangles.put(12863, MJRectangle.from_radius(32773, 32927, 3, 3, (short)12862));		// 頂樓中間
	}
	
	public static MJDomTowerNpcActionInfo get_action_info(int npc_id){
		return domTowerNpcToActions.get(npc_id);
	}
	
	public static MJDomTowerNpcActionInfo get_action_info_from_index(int idx) {
		return IntRange.includes(idx, 0, domTowerNpcToActionsArray.length - 1) ? domTowerNpcToActionsArray[idx] : null; 
	}
	public int npcid;
	public String[] actions;
	public int[] map_ids;
	MJDomTowerNpcActionInfo(int npcid, String[] actions, int[] map_ids){
		this.npcid = npcid;
		this.actions = actions;
		this.map_ids = map_ids;
	}
	
	public int get_select_mapid(String action){
		for(int i=actions.length - 1; i>=0; --i){
			if(actions[i].equalsIgnoreCase(action))
				return map_ids[i];
		}
		return -1;
	}
	
	public int get_first_mapid() {
		return map_ids[0];
	}
}
