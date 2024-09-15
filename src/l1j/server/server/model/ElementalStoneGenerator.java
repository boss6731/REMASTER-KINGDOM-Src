package l1j.server.server.model;

import java.util.ArrayList;
import java.util.Random;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.types.Point;

public class ElementalStoneGenerator implements Runnable {

	public static final int SLEEP_TIME = 300 * 1000; // 설치 종료후, 재설치까지의 시간 ms
	private static final int ELVEN_FOREST_MAPID = 4;
	private static final int MAX_COUNT = Config.ServerAdSetting.ELEMENTALSTONEAMOUNT; // 설치 개수
	private static final int INTERVAL = 3; // 설치 간격초
	private static final int FIRST_X = 32911;
	private static final int FIRST_Y = 32210;
	private static final int LAST_X = 33141;
	private static final int LAST_Y = 32500;
	private static final int ELEMENTAL_STONE_ID = 40515; // 정령의 돌

	private ArrayList<L1GroundInventory> _itemList = new ArrayList<L1GroundInventory>(
			MAX_COUNT);
	private Random _random = new Random(System.nanoTime());

	private static ElementalStoneGenerator _instance = null;

	private ElementalStoneGenerator() {
	}

	public static ElementalStoneGenerator getInstance() {
		if (_instance == null) {
			_instance = new ElementalStoneGenerator();
		}
		return _instance;
	}

	private final L1Object _dummy = new L1Object();

	/**
	 * 返回是否可以在指定位置放置石頭。
	 */
	private boolean canPut(L1Location loc) {
		_dummy.setMap(loc.getMap());
		_dummy.setX(loc.getX());
		_dummy.setY(loc.getY());

		// 檢查可視範圍內的玩家
		if (L1World.getInstance().getVisiblePlayer(_dummy).size() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 確定下一個安裝點。
	 */
	private Point nextPoint() {
		int newX = _random.nextInt(LAST_X - FIRST_X) + FIRST_X;
		int newY = _random.nextInt(LAST_Y - FIRST_Y) + FIRST_Y;

		return new Point(newX, newY);
	}

	/**
	 * 從列表中刪除撿起的石頭。
	 */
	private void removeItemsPickedUp() {
		L1GroundInventory gInventory  = null;
		for (int i = 0; i < _itemList.size(); i++) {
			gInventory = _itemList.get(i);
			if (!gInventory.checkItem(ELEMENTAL_STONE_ID)) {
				_itemList.remove(i);
				i--;
			}
		}
	}

	/**
	 * 지정된 위치에 돌을 둔다.
	 */
	private void putElementalStone(L1Location loc) {
		L1GroundInventory gInventory = L1World.getInstance().getInventory(loc);

		L1ItemInstance item = ItemTable.getInstance().createItem(
				ELEMENTAL_STONE_ID);
		item.setEnchantLevel(0);
		item.setCount(1);
		gInventory.storeItem(item);
		_itemList.add(gInventory);
	}

	@Override
	public void run() {
		try {
			L1Map map = L1WorldMap.getInstance().getMap(
					(short) ELVEN_FOREST_MAPID);
			L1Location loc = null;

			removeItemsPickedUp();

			if (_itemList.size() < MAX_COUNT) { // 在減少的情況下設置
				loc = new L1Location(nextPoint(), map);

				if (canPut(loc)) {
					// XXX 如果所有PC都在安裝範圍內，會進入無限循環…
					putElementalStone(loc);
				}

				// 與執行緒優化有關。讓我們減少sleep的濫用。-- Mazik
				GeneralThreadPool.getInstance().schedule(this, INTERVAL * 1000); // 定期安裝
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
