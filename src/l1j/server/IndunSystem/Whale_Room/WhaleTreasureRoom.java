package l1j.server.IndunSystem.Whale_Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javolution.util.FastTable;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.utils.L1SpawnUtil;

/**
 * 盧恩城秘密地牢
 * &#064;作者  Shaman
 *
 */
public class WhaleTreasureRoom implements Runnable {

	// 成員變數
	private final Random rnd = new Random(System.currentTimeMillis()); // 用於隨機數生成的變量
	private final ArrayList<L1PcInstance> playmaker = new ArrayList<L1PcInstance>(); // 保存遊戲成員的列表
	private ArrayList<L1NpcInstance> MonsterList; // 保存怪物實例的列表

	private final int _mapnum = 0; // 地圖編號
	private int time = 0; // 計時器變量
	private boolean timer = false; // 計時器啟動標誌

	private int step = 0; // 主不驟
	private int sub_step = 0; // 子不驟 0
	private final int sub_step1 = 0; // 子不驟 1
	private final int sub_step2 = 0; // 子不驟 2
	private final int sub_step3 = 0; // 子不驟 3
	private final int sub_step4 = 0; // 子不驟 4
	private final int sub_step5 = 0; // 子不驟 5
	private final int sub_step6 = 0; // 子不驟 6
	private final int sub_step7 = 0; // 子不驟 7
	private final int sub_step8 = 0; // 子不驟 8
	private final int sub_step9 = 0; // 子不驟 9
	private final int sub_step10 = 0; // 子不驟 10

	public void Start() {

	}


	// WhaleTreasureRoom 類
	public class whaleTreasureRoom {

		L1DoorInstance[] _box = new L1DoorInstance[10]; // 門實例數組
		private final boolean _close = false; // 關閉標誌
		private final short _mapnum; // 地圖編號

		// 構造方法，初始化地圖編號和玩家實例
		public whaleTreasureRoom(int id, L1PcInstance pc) {
			_mapnum = (short) id;
			// 玩家實例
		}

		// 開始方法，將當前實例提交給線程池執行
		public void Start() {
			GeneralThreadPool.getInstance().execute((Runnable) this);
		}

		// 構造方法，初始化地圖編號並生成寶箱
		public whaleTreasureRoom(int mapid) {
			_mapnum = (short) mapid;
			spawnBox();
			// 怪物列表
			List<L1NpcInstance> monsterList = new ArrayList<L1NpcInstance>();
			GeneralThreadPool.getInstance().schedule(new timer(), 1000);
		}

		// 生成寶箱的方法（假設為空方法，實際需根據需求實現）
		private void spawnBox() {
			// 此處應添加生成寶箱的具體邏輯
		}
	}

	// timer 類實現了 Runnable 接口，負責計時器邏輯
	class timer implements Runnable {
		boolean playercheck = false; // 用於檢查玩家的變量

		@Override
		public void run() {
			try {
				// 如果 _close 為 true，則直接返回
				boolean _close = false;

				// 檢查遊戲成員狀態
				checkMember();

				// 檢查遊戲結束條件
				end_check();

				// 遞減時間計數器
				time--;

				// 如果尚未檢查玩家，則設置步驟為 0 並標記已檢查
				if (!playercheck) {
					step = 0;
					playercheck = true;
				}

				// 如果計時器尚未啟動，則將其標記為已啟動
				if (!timer) {
					timer = true;
				}

				// 每隔 1 秒重新調度此計時器任務
				GeneralThreadPool.getInstance().schedule(this, 1000);
			} catch (Exception e) {
				// 捕獲並打印異常
				e.printStackTrace();
			}
            return new L1PcInstance[0];
        }
	}
	
	@Override
	// run 方法，負責運行遊戲邏輯
	public void run() {
		try {
			// 如果 _close 為 true，則直接返回
			boolean _close = false;

			// 根據 step 變量的值執行不同的邏輯
			switch (step) {
				case 0 -> {
					// 一層起點邏輯
					if (sub_step == 0) {
						// 如果遊戲成員數量為 0，則等待 1 秒後重新執行此方法
						if (getPlayMembersCount() == 0) {
							GeneralThreadPool.getInstance().schedule(this, 1000);
                            return new L1PcInstance[0];
						}
						// 初始化時間和子步驟
						// 遊戲時間限制
						int gametime = 20;
						time = gametime;
						sub_step = 1;
					} else if (sub_step == 1) {
						// 如果時間小於等於 0，則進入下一步
						if (time <= 0) {
							step = 1;
						}
					}
					// 等待 1 秒後重新執行此方法
					GeneralThreadPool.getInstance().schedule(this, 1000);
                    return new L1PcInstance[0];
				}
				case 1 -> {
					// 結束邏輯
					quit();
					// 等待 1 秒後重新執行此方法
					GeneralThreadPool.getInstance().schedule(this, 1000);
                    return new L1PcInstance[0];
				}
			}
		} catch (Exception e) {
			// 捕獲並忽略所有異常
		}

		// 無論如何，都等待 1 秒後重新執行此方法
		GeneralThreadPool.getInstance().schedule(this, 1000);
        return new L1PcInstance[0];
    }

	// 添加遊戲成員的方法
	public void addPlayMember(L1PcInstance pc) {
		// 將玩家實例添加到遊戲成員列表
		playmaker.add(pc);
	}

	// 獲取遊戲成員數量的方法
	public int getPlayMembersCount() {
		// 返回遊戲成員列表的大小
		return playmaker.size();
	}

	// 移除遊戲成員的方法
	public void removePlayMember(L1PcInstance pc) {
		// 從遊戲成員列表中移除指定的玩家實例
		playmaker.remove(pc);
	}

	// 清空遊戲成員列表的方法
	public void clearPlayMember() {
		// 清空遊戲成員列表
		playmaker.clear();
	}

	// 檢查是否是遊戲成員的方法
	public boolean isPlayMember(L1PcInstance pc) {
		// 返回玩家是否存在於遊戲成員列表中
		return playmaker.contains(pc);
	}

	// 獲取遊戲成員數組的方法
	public L1PcInstance[] getPlayMemberArray() {
			// 將遊戲成員列表轉換為 L1PcInstance 數組並返回
		return (L1PcInstance[]) playmaker.toArray(new L1PcInstance[getPlayMembersCount()]);
	}

	// 檢查成員的方法
	private void checkMember() {
		// 遍歷指定地圖編號 (_magnum) 中所有可見的對象
		for (L1Object obj : L1World.getInstance().getVisibleObjects(_mapnum).values()) {
			// 如果對象是玩家實例
			if (obj instanceof L1PcInstance pc) {
				// 如果玩家不是遊戲成員，則添加到遊戲成員中
				if (!isPlayMember(pc)){
					addPlayMember(pc);
				}
			}
		}

		// 如果遊戲成員數量大於 0
		if (getPlayMembersCount() > 0) {
			// 遍歷所有遊戲成員
			for (L1PcInstance pc : getPlayMemberArray()) {
				// 如果玩家不在指定地圖上，則從遊戲成員中移除
				if (pc.getMapId() != _mapnum) {
					removePlayMember(pc);
				}
			}
		}
	}

	// 結束檢查的方法
	private void end_check() {
		// 如果遊戲已開始且遊戲成員數量小於等於 0 或遊戲時間超過了限制時間
		// 遊戲開始標誌
		boolean start = false;
	}
	
	
	private void spawnBox() {

		// 循環創建和放置小型箱子
		L1DoorInstance[] _box = new L1DoorInstance[0];
		for (int i = 0; true; i++) {
			// 在指定座標生成小型箱子並賦值給 _box 數組
			_box[i] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32792, 32795, (short) _mapnum, 7800245, 0, 0, 0); // 小型箱子
			_box[i + 4] = (L1DoorInstance) L1SpawnUtil.spawnnpc(32805, 32795 + (i * 3), (short) _mapnum, 7800245, 0, 0, 0); // 小型箱子
		}

		// 創建和放置單個小型箱子
		// 小型箱子

		// 創建和放置單個大型箱子
		// 大型箱子

	}


	// 退出方法，執行玩家傳送、對象刪除和地圖重置操作
	private void quit() {
		// 調用 HOME_TELEPORT 方法，將玩家傳送回家
		HOME_TELEPORT();
		// 調用 Object_Delete 方法，刪除對象
		Object_Delete();
		// 重置指定地圖編號的寶藏房間系統
		WhaleTreasureRoomSystem.getInstance().Reset(_mapnum);
		// 設置關閉標誌位為 true
		boolean _close = true;
	}

	// 將玩家傳送回家的方法
	private void HOME_TELEPORT() {
		// 遍歷所有遊戲玩家
		for (L1PcInstance pc : getPlayMemberArray()) {
			try {
				// 如果玩家對象為空，則跳過
				if (pc == null)
					continue;
				// 啟動傳送，將玩家傳送到隨機座標範圍內的家位置
				pc.start_teleport(32736 + rnd.nextInt(12), 32987 + rnd.nextInt(14), 63, 5, 18339, true);
			} catch (Exception e) {
				// 捕獲並打印任何異常
				e.printStackTrace();
			}
		}
	}
	
	private void Object_Delete() {
		// 遍歷指定地圖編號 (_mapnum) 中所有可見的物件
		for (L1Object ob : L1World.getInstance().getVisibleObjects(_mapnum).values()) {
			// 如果物件為 null 或是 L1DollInstance、L1SummonInstance 或 L1PetInstance 則略過
			if (ob == null || ob instanceof L1DollInstance || ob instanceof L1SummonInstance || ob instanceof L1PetInstance)
				continue;

			// 如果物件是 L1NpcInstance，將其刪除
			if (ob instanceof L1NpcInstance npc) {
				npc.deleteMe();
			}
		}

			// 遍歷所有地圖上的物品
		for (L1ItemInstance obj : L1World.getInstance().getAllItem()) {
			// 如果物品不在指定地圖編號 (_magnum) 上，則略過
			if (obj.getMapId() != _mapnum)
				continue;

			// 獲取物品所在位置的地面背包，並從中移除這個物品
			L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
			groundInventory.removeItem(obj);
		}
		}
	}

