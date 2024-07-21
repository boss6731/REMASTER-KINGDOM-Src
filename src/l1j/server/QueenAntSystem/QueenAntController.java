package l1j.server.QueenAntSystem;



import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.ForgottenIsland.FIController;
import l1j.server.MJTemplate.Lineage2D.MJPoint;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Instance.*;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;

import l1j.server.server.model.L1World;

import l1j.server.server.serverpackets.S_DisplayEffect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;

import java.util.ArrayList;
import java.util.Iterator;

public class QueenAntController implements Runnable {

	// Boss的基本屬性和地圖ID
	private int _boss_id = 899995;
	private int _boss_mapx = 32896;
	private int _boss_mapy = 32824;
	private int _boss_mapid = 15891;
	private int _mapid = 15881;
	private int _hall_mapid = 15871;

	// 系統運行狀態標識
	public static boolean _END = false;
	private int stage = 0;

	// 各種狀態標識
	public static final int STATUS_START = 0;
	public static final int STATUS_1ST_EVENT = 1;
	public static final int STATUS_2ND_EVENT = 2;
	public static final int STATUS_3RD_EVENT = 3;
	public static final int STATUS_END = 4;
	public static final int STATUS_STOP = 5;

	public int _executeStatus = STATUS_START;

	private static boolean Running = false;

	public static int Time = 3600;

	private int remain_time = 0;

	// 存放NPC和效果
	private FastTable<L1NpcInstance> _npc_info;
	private final FastTable<L1EffectInstance> effect_list = new FastTable<L1EffectInstance>();
	L1NpcInstance[] enter_antqueen = new L1NpcInstance[10];
	L1NpcInstance[] circle = new L1NpcInstance[20];
	L1NpcInstance erzabe_egg;

	// 設置是否運行
	public void setReady(boolean flag) {
		Running = flag;
	}

	// 檢查是否運行
	public static boolean isReady() {
		return Running;
	}

	// 運行方法
	//@override
	public void run() {
		// 主循環
		while (Running) {
			try {
				// 檢查時間和毒雲傷害
				TimeCheck();
				Poison_cloud_damage();
				switch (stage) {
					case STATUS_START:
						if (remain_time == 0) {
							// 發送消息給玩家
							sendStartMessage();
							// 開始毒雲牆線程
							GeneralThreadPool.getInstance().execute(new FIController.PoisonCloudWallThread());
							// 生成NPC列表
							_npc_info = QueenAntSpawnlistLoader.getInstance().spawnlist(0);
						}
						stage = STATUS_1ST_EVENT;
						// 計畫下一次調度
						GeneralThreadPool.getInstance().schedule(this, 1000L);
						return;

					case STATUS_1ST_EVENT:
						if (Time == 3000) {
							// 發送消息給玩家
							sendFirstEventMessage();
							// 生成進入的螞蟻女王和法陣
							spawnAntQueensAndCircles();
							stage = STATUS_2ND_EVENT;
							remain_time = 300;
						}
						GeneralThreadPool.getInstance().schedule(this, 1000L);
						return;
					case STATUS_2ND_EVENT:
						handleSecondEvent();
						remain_time--;
						GeneralThreadPool.getInstance().schedule(this, 1000L);

						return;

					case STATUS_3RD_EVENT:
						handleThirdEvent();
						remain_time++;
						if (remain_time >= 10) {
							stage = STATUS_END;
							remain_time = 0;
							erzabe_egg.deleteMe();
						}
						GeneralThreadPool.getInstance().schedule(this, 1000L);
						return;

					case STATUS_END:
						Object_Check();
						GeneralThreadPool.getInstance().schedule(this, 1000L);
						return;

					case STATUS_STOP:
						GeneralThreadPool.getInstance().schedule(this, 1000L);
						return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		reset();
	}

	// 開始方法
	public void Start() {
		if (Running) {
			_END = true;
			// 發送關閉消息給GM
			for (L1PcInstance pc : PcCK(true)) {
				if (pc.isGm()) {
					pc.sendPackets("\f3[僅管理員可見通知]: 女王螞蟻隱藏處系統正在關閉。1分鐘後請在事件警報表中 +1 設置並重新加載。");
				}
			}
		} else {
			Time = 3600;
			_END = false;
			GeneralThreadPool.getInstance().schedule(this, 1000L);
			reset();
			setReady(true);
		}
	}

	// 重置方法
	private void reset() {
		L1Object boss = L1World.getInstance().findNpc(_boss_id);
		if (boss != null && boss instanceof L1NpcInstance && !(boss instanceof L1DollInstance)) {
			L1NpcInstance npc = (L1NpcInstance) boss;
			deleteNpc(npc);
		}
		stage = STATUS_START;
		remain_time = 0;
		// 其他重置操作
	}

	// 地圖清理方法
	public void MapOut() {
		for (L1PcInstance c : L1World.getInstance().getAllPlayers()) {
			if (c.getMapId() == _boss_mapid  c.getMapId() == _mapid  c.getMapId() == _hall_mapid) {
				int[] loc = L1TownLocation.getGetBackLoc(L1TownLocation.TOWNID_WINDAWOOD);
				c.start_teleport(loc[0], loc[1], loc[2], c.getHeading(), 12261, true);
			}
		}
	}

	// 獲取符合條件的玩家列表方法
	public ArrayList<L1PcInstance> PcCK(boolean world) {
		ArrayList<L1PcInstance> _pc = new ArrayList<L1PcInstance>();
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (world) {
				_pc.add(pc);
			} else {
				if (pc.getMapId() == _mapid || pc.getMapId() == _boss_mapid)
					_pc.add(pc);
			}
		}
		return _pc;
	}

	// 獲取符合條件的boss玩家列表方法
	public ArrayList<L1PcInstance> PcCKBoss() {
		ArrayList<L1PcInstance> _pc = new ArrayList<L1PcInstance>();
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc.getMapId() == _boss_mapid) {
				_pc.add(pc);
			}
		}
		return _pc;
	}

	// 時間檢查方法
	private void TimeCheck() {
		if (Time > 0) {
			Time--;
		}
		if (Time == 0) {
			Running = false;
			_END = true;
		}
		if (_END) {
			try {
				// 發送隱藏處消失消息給玩家
				for (L1PcInstance pc : PcCK(true)) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "沙塵暴中女王螞蟻的隱藏處正在消失。"));
				}
				// 清理NPC和效果
				clearNpcsAndEffects();
				// 清理法陣和進入的螞蟻女王
				clearCirclesAndAntQueens();
				Thread.sleep(7000);
				MapOut();
				Running = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// 第二事件處理方法
	private void handleSecondEvent() {
		if (remain_time == 300) {
			// 發送第二事件消息
			for (L1PcInstance pc : PcCK(false)) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "勇士們...請來救我...跟著光進入我的房間..."));
				pc.sendPackets("勇士們...請來救我...跟著光進入我的房間...");
			}
		}
		if (remain_time == 240) {
			// 發送解鎖枷鎖消息
			for (L1PcInstance pc : PcCK(false)) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "只有您能解開束縛我的枷鎖.."));
				pc.sendPackets("只有您能解開束縛我的枷鎖..");
			}
		}
		if (remain_time == 180) {
			// 發送鑰匙消息
			for (L1PcInstance pc : PcCK(false)) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "閃亮的地板石是救我的鑰匙.. 去那裡..."));
				pc.sendPackets("閃亮的地板石是救我的鑰匙.. 去那裡...");
			}
			// 刪除進入的螞蟻女王
			for (int i = 0; i < 10; i++) {
				enter_antqueen[i].deleteMe();
			}
		}
		if (remain_time < 180) {
			// 發送聚集力量消息
			for (L1PcInstance pc : PcCK(false)) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "請聚集更多的力量...拜託了..."));
				pc.sendPackets("請聚集更多的力量...拜託了...");
			}
			// 傳送玩家並創建新物品
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc.getMapId() == _boss_mapid) {
					if (!pc.isErzabe_circle() && !pc.isDead()) {
						MJPoint pt = MJPoint.newInstance(32833, 32766, 5, (short) _hall_mapid, 50);
						pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 12261, true);
					} else {
						createNewItem(pc, Config.ServerAdSetting.QueenAnt_Itemid, Config.ServerAdSetting.QueenAnt_Count);
					}
					pc.sendPackets(S_DisplayEffect.newInstance(2));
					pc.send_effect(19255);
				}
			}
			// 刪除法陣
			for (int i = 0; i < 20; i++) {
				circle[i].deleteMe();
			}
			// 顯示效果
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(erzabe_egg)) {
				erzabe_egg.onPerceive(pc);
				S_DoActionGFX gfx = new S_DoActionGFX(erzabe_egg.getId(), 8);
				pc.sendPackets(gfx);
				erzabe_egg.send_effect(14258);
			}
		}
		stage = STATUS_3RD_EVENT;
		remain_time = 0;
	}

	// 第三事件處理方法
	private void handleThirdEvent() {
		if (remain_time == 0) {
			// 發送第三事件消息
			for (L1PcInstance pc : PcCK(false)) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "..是吃飯時間了..!!"));
				pc.sendPackets("..是吃飯時間了..!!");
			}
			// 生成boss
			spawn(_boss_mapx, _boss_mapy, (short) _boss_mapid, 0, _boss_id, ActionCodes.ACTION_Appear);
		}
	}

	// 清理NPC和效果方法
	private void clearNpcsAndEffects() {
		if (_npc_info != null) {
			for (Iterator<L1NpcInstance> i = _npc_info.listIterator(); i.hasNext();) {
				L1NpcInstance npc = i.next();
				if (npc != null) {
					deleteNpc(npc);
				}
			}
			_npc_info.clear();
		}
		if (effect_list != null) {
			for (Iterator<L1EffectInstance> i = effect_list.listIterator(); i.hasNext();) {
				L1EffectInstance effect = i.next();
				if (effect != null) {
					deleteNpc(effect);
				}
			}
			effect_list.clear();
		}
	}

	// 清理法陣和進入的螞蟻女王方法
	private void clearCirclesAndAntQueens() {
		for (int i = 0; i < 20; i++) {
			if (circle[i] != null) {
				circle[i].deleteMe();
			}
		}

		if (erzabe_egg != null)
			erzabe_egg.deleteMe();

		for (int i = 0; i < 10; i++) {
			if (enter_antqueen[i] != null) {
				enter_antqueen[i].deleteMe();



			}
		}
	}

	// 對象檢查方法
	private void Object_Check() {
		L1NpcInstance mob = null;
		for (L1Object object : L1World.getInstance().getVisibleObjects(_boss_mapid).values()) {
			if (object instanceof L1MonsterInstance) {
				mob = (L1NpcInstance) object;
				int npc = mob.getNpcTemplate().get_npcId();
				if (npc == _boss_id) {
					if (mob != null && mob.isDead()) {
						if (remain_time == 0) {
							// 發送boss擊敗消息
							for (L1PcInstance pc : PcCK(false)) {
								pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "你已經擊敗了女王螞蟻。1分鐘後，女王螞蟻的房間將崩塌。"));
								pc.sendPackets("你已經擊敗了女王螞蟻。1分鐘後，女王螞蟻的房間將崩塌。");
							}
						}
						handleRoomCollapse();
					}
				}
			}
		}
	}

	// 房間崩塌處理方法
	private void handleRoomCollapse() {
		if (remain_time == 30) {
			// 發送整理消息
			for (L1PcInstance pc : PcCK(false)) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "在房間崩塌前完成整理並返回駐地。"));
				pc.sendPackets("在房間崩塌前完成整理並返回駐地。");
			}
		}
		if (remain_time == 50) {
			// 發送返回駐地消息
			for (L1PcInstance pc : PcCK(false)) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "女王螞蟻的房間正在崩塌。為了安全起見，請返回駐地。"));
				pc.sendPackets("女王螞蟻的房間正在崩塌。為了安全起見，請返回駐地。");
			}
		}
		if (remain_time > 60) {
			// 傳送玩家回駐地
			for (L1PcInstance pc : PcCKBoss()) {
				MJPoint pt = MJPoint.newInstance(32833, 32766, 15, (short) 15871, 50);
				pc.start_teleport(pt.x, pt.y, pt.mapId, pc.getHeading(), 18339, true);
			}
			stage = STATUS_STOP;
		}
		remain_time++;
	}

	// 處理毒雲傷害的方法
	private void Poison_cloud_damage() {
		for (Iterator<L1EffectInstance> i = effect_list.listIterator(); i.hasNext();) {
			L1EffectInstance effect = i.next();
			if (effect.getNpcId() == 8503185) {
				for (L1Object object : L1World.getInstance().getVisibleObjects(_mapid).values()) {
					if (object instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) object;
						// 檢查玩家是否在毒雲範圍內
						if (pc.getLocation().getTileLineDistance(new Point(effect.getLocation())) < 5 && !pc.isDead()) {
							if (pc != null) {
								pc.receiveDamage(effect, Config.ServerAdSetting.QueenAnt_Magnetic_Dmg); // 給予玩家毒雲傷害
								pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage));
								pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage));
							}
						}
					}





				}
			}
		}
	}

	// 創建新物品並添加到玩家背包的方法
	private boolean createNewItem(L1PcInstance pc, int item_id, int count) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				pc.sendPackets(new S_ServerMessage(82)); // 顯示背包已滿的信息
				return false;
			}
			pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // 顯示物品已獲得的信息
			return true;
		} else {
			return false;
		}


	}

	// 生成NPC的方法
	private static void spawn(int x, int y, short MapId, int Heading, int npcId, int actioncode) {
		try {
			// 創建新的NPC實例
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			// 設置NPC的ID
			npc.setId(IdFactory.getInstance().nextId());
			// 設置NPC的地圖ID
			npc.setMap(MapId);
			int tryCount = 0;
			do {
				tryCount++;
				npc.setX(x);
				npc.setY(y);
				// 檢查地圖位置是否可通行
				if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
					break;
				}
				Thread.sleep(1);
			} while (tryCount < 50);
			if (tryCount >= 50) {
				npc.getLocation().forward(Heading);
			}
			// 設置NPC的初始位置和方向
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(Heading);

			// 向玩家顯示NPC的生成動畫
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(npc)) {
				npc.onPerceive(pc);
				S_DoActionGFX gfx = new S_DoActionGFX(npc.getId(), actioncode);
				pc.sendPackets(gfx);
			}

			// 在世界中存儲和顯示NPC對象
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			// 開啟NPC的光源並開始聊天
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 刪除NPC的方法
	private void deleteNpc(L1NpcInstance npc) {
		// 設置地圖位置可通行
		npc.getMap().setPassable(npc.getX(), npc.getY(), true);
		// 刪除NPC
		npc.deleteMe();
	}

	// 刪除效果的方法
	private void deleteNpc(L1EffectInstance effect) {
		// 刪除效果
	}

	// 發送開始消息的方法
	private void sendStartMessage() {
		for (L1PcInstance pc : PcCK(true)) {
			pc.sendPackets(new
		}
	}




