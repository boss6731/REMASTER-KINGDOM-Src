/**
 * 타이머 관련 맵에 대한 컨트롤러
 * 2008. 12. 04
 */

package l1j.server.server.server.Controller;

import java.util.ArrayList;

import l1j.server.server.server.datatables.DoorSpawnTable;
import l1j.server.server.model.L1World;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1TimeMap;

public class TimeMapController implements Runnable{
	public static final int SLEEP_TIME = 1000;

	private ArrayList<L1TimeMap> mapList; // 地圖儲存庫
	private static TimeMapController instance; // 單一單例對象

	/**
	 * 單例模式實現 - 返回單一對象
	 * @return (TimeMapController) 單一對象
	 */
	public static TimeMapController getInstance(){
		if(instance == null) instance = new TimeMapController();
		return instance;
	}
	/**
	 * 默認構造函數（單例模式實現為 private）
	 */
	private TimeMapController(){
		mapList = new ArrayList<L1TimeMap>();
	}
	/**
	 * Thread abstract Method
	 */
	@Override
	public void run() {
		try {
			for (L1TimeMap timeMap : array()) {
				if (timeMap.count()) {
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						if (pc == null)
							continue;
						if (timeMap.getId() != pc.getMapId()) {
							continue;
						}

						switch (pc.getMapId()) {
							case 72:
							case 73:
							case 74:
								pc.start_teleport(34056, 32279, 4, 5, 18339, true, false);
								break;
							case 460:
							case 461:
							case 462:
							case 463:
							case 464:
							case 465:
							case 466:
								pc.start_teleport(32664, 32855, 457, 5, 18339, true, false);
								break;
							case 470:
							case 471:
							case 472:
							case 473:
							case 474:
								pc.start_teleport(32663, 32853, 467, 5, 18339, true, false);
								break;
							case 475:
							case 476:
							case 477:
							case 478:
								pc.start_teleport(32660, 32876, 468, 5, 18339, true, false);
								break;
							default:
								break;
						}
					}
					DoorSpawnTable.getInstance().getDoor(timeMap.getDoor()).close();
					remove(timeMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// _log.warning(e.getMessage());
			// System.out.println(e.getMessage());
		}
	}
	/**
	 * 註冊設定有時間事件的地圖
	 * 為避免重複註冊，與已註冊的地圖ID比較，不存在則註冊
	 * 如果大小為0，即初始狀態，因無比較對象則無條件註冊
	 * @param (TimeMap) 要註冊的地圖對象
	 */
	public void add(L1TimeMap map){
		if(mapList.size() > 0){
			boolean found = false;
			for(L1TimeMap m : array()){
				if(m.getId() == map.getId()){
					found = true;
					break;
				}
			}
			if(!found)
			{
				mapList.add(map);
			}
		}else mapList.add(map);
	}
	/**
	 * 刪除設定有時間事件的地圖
	 * 為避免重複刪除或IndexOutOfBoundsException，與已註冊的地圖ID比較後存在則刪除
	 * @param (TimeMap) 要刪除的地圖對象
	 */
	private void remove(L1TimeMap map){
		for(L1TimeMap m : array()){
			if(m.getId() == map.getId()){
				mapList.remove(map);
				break;
			}
		}
		map = null;
	}
	/**
	 * 返回已註冊的事件地圖數組
	 * @return    (TimeMap[])    地圖對象數組
	 */
	private L1TimeMap[] array(){
		return mapList.toArray(new L1TimeMap[mapList.size()]);
	}
}