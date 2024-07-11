package l1j.server.IndunSystem.Ice.Queen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Random;

import l1j.server.IndunSystem.Ice.Demon.Demon;
import l1j.server.IndunSystem.Ice.Demon.DemonSystem;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;

public class Queen implements Runnable {

	private int _map;

	private static Random _random = new Random(System.nanoTime());

	private boolean Running = false;

	public boolean Start = false;
	private boolean FirstRoom = false;
	private boolean SecondRoom = false;
	private boolean ThirdRoom = false;
	private boolean FourthRoom = false;
	private boolean BossRoom = false;
	private boolean End = false;

	private int Time = 3600;

	public Queen(int id) {
		_map = id;
	}

	int[] _MonsterList = { 5080, 5081, 5082, 5083, 5084, 5085 };

	@Override
	public void run() {
		Calendar cal = Calendar.getInstance();
		int 時間 = Calendar.HOUR;
		int 分 = Calendar.MINUTE;
		/** 上午 0 點、下午 1 點 **/
		String 上午下午 = "下午";
		if (cal.get(Calendar.AM_PM) == 0) {
			上午下午 = "上午";
		}
		Running = true;
		FirstRoom = true;
		Time = 3600;
		SpawnMonster();
		while (Running) {
			try {
				Check();
				if (End) {
					reset();
					break;
				} else if (FirstRoom) {
					First();
				} else if (SecondRoom) {
					Second();
				} else if (ThirdRoom) {
					Third();
				} else if (FourthRoom) {
					Fourth();
				} else if (BossRoom) {
					Boss();
				}
			} catch (Exception e) {
			} finally {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		Queen_Delete();
		// System.out.println(""+ 上午下午 + " " + cal.get(時間) + "時" + cal.get(分) +
		// "分" + " ■■■■■■ 冰之女王结束 " + _map+" ■■■■■■");
		System.out.println("" + 上午下午 + " " + cal.get(時間) + "時" + cal.get(分) + "分" + "   ■冰之女王結束■地圖:" + _map + "■");
	}

	public void Start() {
		Calendar cal = Calendar.getInstance();
		int 時間 = Calendar.HOUR;
		int 分 = Calendar.MINUTE;
		/** 0 上午 , 1 下午 * */
		String 上午下午 = "下午";
		if (cal.get(Calendar.AM_PM) == 0) {
			上午下午 = "上午";
		}
		GeneralThreadPool.getInstance().schedule(this, 2000);
		// System.out.println(""+ 上午下午 + " " + cal.get(時間) + "時" + cal.get(分) +
		// "分" + " ■■■■■■ 冰之女王 開始 " + _map+" ■■■■■■");
		System.out.println("" + 上午下午 + " " + cal.get(時間) + "時" + cal.get(分) + "分" + "   ■冰之女王 開始■地圖:" + _map + "■");
	}

	private void Check() {
		if (Time > 0) {
			Time--;
		}
		if (Time <= 0) {
			End();
		} else if (Time <= 3590) {
			CheckPc();
		}
		if (Time % 60 == 0) {
			int min = Time / 60;
			for (L1Object obj : L1World.getInstance().getVisibleObjects(_map).values()) {
				if (obj instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) obj;
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, min + "分鐘後將被強制傳送到村莊."));
				}
			}
		}
	}

	private void First() {
		if (_list1.size() > 0) {
			for (int i = _list1.size() - 1; i >= 0; i--) {
				L1NpcInstance npc = _list1.get(i);
				if (npc.getCurrentHp() <= 0) {
					remove(npc, 1);
				}
			}
		} else {
			openDoor(5147); // 第一個開門。
			FirstRoom = false;
			SecondRoom = true;
		}
	}

	private void Second() {
		if (_list2.size() > 0) {
			for (int i = _list2.size() - 1; i >= 0; i--) {
				L1NpcInstance npc = _list2.get(i);
				if (npc.getCurrentHp() <= 0) {
					remove(npc, 2);
				}
			}
		} else {
			openDoor(5148); // 第二扇門打開。
			SecondRoom = false;
			ThirdRoom = true;
		}
	}

	private void Third() {
		if (_list3.size() > 0) {
			for (int i = _list3.size() - 1; i >= 0; i--) {
				L1NpcInstance npc = _list3.get(i);
				if (npc.getCurrentHp() <= 0) {
					remove(npc, 3);
				}
			}
		} else {
			openDoor(5149); // 第三扇門打開。
			ThirdRoom = false;
			FourthRoom = true;
		}
	}

	private void Fourth() {
		if (_list4.size() > 0) {
			for (int i = _list4.size() - 1; i >= 0; i--) {
				L1NpcInstance npc = _list4.get(i);
				if (npc.getCurrentHp() <= 0) {
					remove(npc, 4);
				}
			}
		} else {
			openDoor(5150); // 第四扇門打開。
			FourthRoom = false;
			BossRoom = true;
		}
	}

	private void Boss() {
		if (_list5.size() > 0) {
			for (int i = _list5.size() - 1; i >= 0; i--) {
				L1NpcInstance npc = _list5.get(i);
				if (npc.getCurrentHp() <= 0) {
					remove(npc, 5);
				}
			}
		} else {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(_map).values()) {
				if (obj instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) obj;
					pc.sendPackets(
							new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
									"請去見牆後面的馬賓[副副本內還是外?][副副本內還是外?]."));
				}
			}
			openDoor(5151); // 第五扇門打開了。
			BossRoom = false;
		}
	}

	private void Queen_Delete() {
		Collection<L1Object> cklist = L1World.getInstance().getVisibleObjects(_map).values();
		for (L1Object ob : cklist) {
			if (ob == null)
				continue;
			if (ob instanceof L1ItemInstance) {
				L1ItemInstance obj = (L1ItemInstance) ob;
				L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(),
						obj.getMapId());
				groundInventory.removeItem(obj);
			} else if (ob instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) ob;
				npc.deleteMe();
			}
		}
		QueenSystem.getInstance().removeQueen(_map);
	}

	private void reset() {
		Running = false;
		ListClear(1);
		ListClear(2);
		ListClear(3);
		ListClear(4);
		ListClear(5);
		ListClear(6);
		for (L1Object obj : L1World.getInstance().getVisibleObjects(_map).values()) {
			if (obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				mon.deleteMe();
			}
		}
	}

	private void CheckPc() {
		int check = 0;
		for (L1Object obj : L1World.getInstance().getVisibleObjects(_map).values()) {
			if (obj instanceof L1PcInstance) {
				check = 1;
			}
		}
		if (check == 0) {
			End();
		}
	}

	private void openDoor(int doorId) {
		L1DoorInstance door = null;
		for (L1Object object : L1World.getInstance().getVisibleObjects(_map).values()) {
			if (object instanceof L1DoorInstance) {
				door = (L1DoorInstance) object;
				if (door.getNpcTemplate().get_npcId() == doorId) {
					if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
						door.open();
					}
				}
			}
		}
	}

	private void End() {
		for (L1Object obj : L1World.getInstance().getVisibleObjects(_map).values()) {
			if (obj instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) obj;
				// L1Teleport.teleport(pc, 34068, 32311, (short)4, 4, true); //
				// 這些是兩個零件落下的座標。
				pc.start_teleport(34068, 32311, 4, 5, 18339, true, false);
			}
		}

		End = true;
	}

	private final ArrayList<L1NpcInstance> _list0 = new ArrayList<L1NpcInstance>();
	private final ArrayList<L1NpcInstance> _list1 = new ArrayList<L1NpcInstance>();
	private final ArrayList<L1NpcInstance> _list2 = new ArrayList<L1NpcInstance>();
	private final ArrayList<L1NpcInstance> _list3 = new ArrayList<L1NpcInstance>();
	private final ArrayList<L1NpcInstance> _list4 = new ArrayList<L1NpcInstance>();
	private final ArrayList<L1NpcInstance> _list5 = new ArrayList<L1NpcInstance>();

	public void add(L1NpcInstance npc, int type) {
		switch (type) {
			case 0:
				if (npc == null || _list0.contains(npc)) {
					return;
				}
				_list0.add(npc);
				break;
			case 1:
				if (npc == null || _list1.contains(npc)) {
					return;
				}
				_list1.add(npc);
				break;
			case 2:
				if (npc == null || _list2.contains(npc)) {
					return;
				}
				_list2.add(npc);
				break;
			case 3:
				if (npc == null || _list3.contains(npc)) {
					return;
				}
				_list3.add(npc);
				break;
			case 4:
				if (npc == null || _list4.contains(npc)) {
					return;
				}
				_list4.add(npc);
				break;
			case 5:
				if (npc == null || _list5.contains(npc)) {
					return;
				}
				_list5.add(npc);
				break;
		}
	}

	private void remove(L1NpcInstance npc, int type) {
		switch (type) {
			case 0:
				if (npc == null || !_list0.contains(npc)) {
					return;
				}
				_list0.remove(npc);
				break;
			case 1:
				if (npc == null || !_list1.contains(npc)) {
					return;
				}
				_list1.remove(npc);
				break;
			case 2:
				if (npc == null || !_list2.contains(npc)) {
					return;
				}
				_list2.remove(npc);
				break;
			case 3:
				if (npc == null || !_list3.contains(npc)) {
					return;
				}
				_list3.remove(npc);
				break;
			case 4:
				if (npc == null || !_list4.contains(npc)) {
					return;
				}
				_list4.remove(npc);
				break;
			case 5:
				if (npc == null || !_list5.contains(npc)) {
					return;
				}
				_list5.remove(npc);
				break;
		}
	}

	private void ListClear(int type) {
		switch (type) {
			case 0:
				_list0.clear();
				break;
			case 1:
				_list1.clear();
				break;
			case 2:
				_list2.clear();
				break;
			case 3:
				_list3.clear();
				break;
			case 4:
				_list4.clear();
				break;
			case 5:
				_list5.clear();
				break;
		}
	}

	private void SpawnMonster() {
		// NPC 生成
		spawn(32734, 32802, (short) _map, 4, 5086, 1, 1, 0); // 象牙塔間諜
		spawn(32860, 32920, (short) _map, 6, 5087, 1, 1, 0); // 斯溫

		// 門產卵
		spawn(32784, 32818, (short) _map, 0, 5147, 1, 1, 0);
		spawn(32852, 32806, (short) _map, 0, 5148, 1, 1, 0);
		spawn(32822, 32855, (short) _map, 0, 5149, 1, 1, 0);
		spawn(32762, 32916, (short) _map, 0, 5150, 1, 1, 0);
		spawn(32852, 32920, (short) _map, 0, 5151, 1, 1, 0);

		// 1번방 뿌리기
		for (int i = 0; i < 15; i++) {
			spawn(32765, 32818, (short) _map, 0, _MonsterList[_random.nextInt(100) % _MonsterList.length], 12, 1, 1);
		}
		spawn(32749, 32800, (short) _map, 0, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 1, 1);
		spawn(32749, 32817, (short) _map, 0, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 1, 1);
		spawn(32766, 32833, (short) _map, 0, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 1, 1);
		spawn(32772, 32802, (short) _map, 0, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 1, 1);

		// 2번 방 뿌리기 2였는데 1로 고쳤죠 메소드를 따라가보면
		spawn(32813, 32805, (short) _map, 0, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 1, 2);
		spawn(32819, 32807, (short) _map, 0, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 1, 2);
		spawn(32819, 32805, (short) _map, 0, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 1, 2);
		for (int i = 0; i < 15; i++) {
			spawn(32833, 32806, (short) _map, 0, _MonsterList[_random.nextInt(100) % _MonsterList.length], 13, 1, 2);
		}

		// 3번 방 뿌리기
		spawn(32859, 32832, (short) _map, 0, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 1, 3);
		spawn(32857, 32831, (short) _map, 0, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 1, 3);
		spawn(32855, 32834, (short) _map, 0, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 1, 3);
		for (int i = 0; i < 15; i++) {
			spawn(32850, 32853, (short) _map, 0, _MonsterList[_random.nextInt(100) % _MonsterList.length], 10, 1, 3);
		}

		// 4번 방 뿌리기
		for (int i = 0; i < 15; i++) {
			spawn(32767, 32892, (short) _map, 0, _MonsterList[_random.nextInt(100) % _MonsterList.length], 12, 1, 4);
		}

		// 5번 방 뿌리기 ( 보스 몬스터 추가 : 아이스 여왕 )
		for (int i = 0; i < 15; i++) {
			spawn(32767, 32892, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 12, 1, 5);
		}
		spawn(32845, 32920, (short) _map, 6, 45609, 0, 1, 5); // 冰皇后
	}

	public static void spawn(int x, int y, short MapId, int Heading, int npcId, int randomRange, int type,
			int roomnumber) {
		try {// 類型是乙級的月來2，但是冰雪女王是1。
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(MapId);
			if (randomRange == 0) {
				npc.getLocation().set(x, y, MapId);
				npc.getLocation().forward(Heading);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap().isInMap(npc.getLocation()) && npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);
				if (tryCount >= 50) {
					npc.getLocation().forward(Heading);
				}
			}
			if (npc instanceof L1DoorInstance) {
				if (npc.getNpcId() == 5147) {
					((L1DoorInstance) npc).setLeftEdgeLocation(32816);
					((L1DoorInstance) npc).setRightEdgeLocation(32821);
					((L1DoorInstance) npc).setDirection(1);
				} else if (npc.getNpcId() == 5148) {
					((L1DoorInstance) npc).setLeftEdgeLocation(32804);
					((L1DoorInstance) npc).setRightEdgeLocation(32809);
					((L1DoorInstance) npc).setDirection(1);
				} else if (npc.getNpcId() == 5149) {
					((L1DoorInstance) npc).setLeftEdgeLocation(32853);
					((L1DoorInstance) npc).setRightEdgeLocation(32858);
					((L1DoorInstance) npc).setDirection(1);
				} else if (npc.getNpcId() == 5150) {
					((L1DoorInstance) npc).setLeftEdgeLocation(32760);
					((L1DoorInstance) npc).setRightEdgeLocation(32764);
					((L1DoorInstance) npc).setDirection(0);
				} else if (npc.getNpcId() == 5151) {
					((L1DoorInstance) npc).setLeftEdgeLocation(32918);
					((L1DoorInstance) npc).setRightEdgeLocation(32923);
					((L1DoorInstance) npc).setDirection(1);
				}
			}
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(Heading);

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			switch (type) {
				case 1:
					Queen queen = QueenSystem.getInstance().getQueen(MapId);
					if (roomnumber == 0) {
						queen.add(npc, 0);
					} else if (roomnumber == 1) {
						queen.add(npc, 1);
					} else if (roomnumber == 2) {
						queen.add(npc, 2);
					} else if (roomnumber == 3) {
						queen.add(npc, 3);
					} else if (roomnumber == 4) {
						queen.add(npc, 4);
					} else if (roomnumber == 5) {
						queen.add(npc, 5);
					}
					break;
				case 2:
					Demon demon = DemonSystem.getInstance().getDemon(MapId);
					if (roomnumber == 0) {
						demon.add(npc, 0);
					} else if (roomnumber == 1) {
						demon.add(npc, 1);
					} else if (roomnumber == 2) {
						demon.add(npc, 2);
					} else if (roomnumber == 3) {
						demon.add(npc, 3);
					} else if (roomnumber == 4) {
						demon.add(npc, 4);
					} else if (roomnumber == 5) {
						demon.add(npc, 5);
					}
					break;
			}
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_SPAWN);
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 聊天開放時間

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
