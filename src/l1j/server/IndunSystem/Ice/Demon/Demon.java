package l1j.server.IndunSystem.Ice.Demon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Random;

import l1j.server.IndunSystem.Ice.Queen.Queen;
import l1j.server.IndunSystem.Ice.Queen.QueenSystem;
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

public class Demon implements Runnable {
	private static final String[] BossNames = new String[] {
			"冰雪女王",
			"冰惡魔",
			"冰兔子媽媽"
	};
	private static final Integer[] BossIds = new Integer[] {
			45609,
			81201,
			7320217,
	};
	
	
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
	private int m_select_bosses_index;
	public Demon(int id){
		_map = id;
		m_select_bosses_index = _random.nextInt(BossNames.length);
	}
	
	int[] _MonsterList= { 5080, 5081,  5082, 5083, 5084, 5085 };

	@Override
	public void run() {
		Calendar cal = Calendar.getInstance();
		int hour = Calendar.HOUR;
		int minute = Calendar.MINUTE;
		/* 0 上午 , 1 下午 * */
		String period = "下午";
		if (cal.get(Calendar.AM_PM) == 0) {
			period = "上午";
		}
		Running = true;
		FirstRoom = true;
		Time = 3600;
		SpawnMonster();
		while(Running){
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
			}catch(Exception e){
			}finally{
				try{
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		Demon_Delete();

//      System.out.println("" + period + " " + cal.get(hour) + "點" + cal.get(minute) + "分" + "   ■■■■■■ 冰惡魔 結束 " + _map + " ■■■■■■");
		System.out.println("" + period + " " + cal.get(hour) + "點" + cal.get(minute) + "分" + "   ■" + BossNames[m_select_bosses_index] + "■ 結束 地圖:" + _map + "■");
        return new L1PcInstance[0];
    }

	public void Start() {
		Calendar cal = Calendar.getInstance();
		int hour = Calendar.HOUR;
		int minute = Calendar.MINUTE;
		/** 0 上午 , 1 下午 * */
		String period = "下午";
		if (cal.get(Calendar.AM_PM) == 0) {
			period = "上午";
		}
		GeneralThreadPool.getInstance().schedule(this, 2000);
//      System.out.println("" + period + " " + cal.get(hour) + "點" + cal.get(minute) + "分" + "   ■■■■■■ 冰惡魔 開始 " + _map + " ■■■■■■");
		System.out.println("" + period + " " + cal.get(hour) + "點" + cal.get(minute) + "分" + "   ■" + BossNames[m_select_bosses_index] + "■ 開始 地圖:" + _map + "■");
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
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, min + "分鐘後將被強制移動到村莊。"));
				}
			}
		}
	}
	
	private void First() {
		if (_list1.size() > 0) {
			for (int i = _list1.size()-1; i >= 0; i--) {
				L1NpcInstance npc = _list1.get(i);
				if (npc.getCurrentHp() <= 0) {
					remove(npc, 1);
				}
			}
		} else {
			openDoor(5147); // 打開第一扇門
			FirstRoom = false;
			SecondRoom = true;
		}
	}

	private void Second() {
		if (_list2.size() > 0) {
			for (int i = _list2.size()-1; i >= 0; i--) {
				L1NpcInstance npc = _list2.get(i);
				if (npc.getCurrentHp() <= 0) {
					remove(npc, 2);
				}
			}
		} else {
			openDoor(5148); // 打開第二扇門.
			SecondRoom = false;
			ThirdRoom = true;
		}
	}

	private void Third() {
		if (_list3.size() > 0) {
			for (int i = _list3.size()-1; i >= 0; i--) {
				L1NpcInstance npc = _list3.get(i);
				if (npc.getCurrentHp() <= 0) {
					remove(npc, 3);
				}
			}
		} else {
			openDoor(5149); // 打開第三扇門.
			ThirdRoom = false;
			FourthRoom = true;
		}
	}
	private void Fourth() {
		if (_list4.size() > 0) {
			for (int i = _list4.size()-1; i >= 0; i--) {
				L1NpcInstance npc = _list4.get(i);
				if (npc.getCurrentHp() <= 0) {
					remove(npc, 4);
				}
			}
		} else {
			openDoor(5150); // 打開第四扇門.
			FourthRoom = false;
			BossRoom = true;
		}
	}
	private void Boss() {
		if (_list5.size() > 0) {
			for (int i = _list5.size()-1; i >= 0; i--) {
				L1NpcInstance npc = _list5.get(i);
				if (npc.getCurrentHp() <= 0) {
					remove(npc, 5);
				}
			}
		} else {
			for (L1Object obj : L1World.getInstance().getVisibleObjects(_map).values()) {
				if (obj instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) obj;
					pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "請去見牆後的斯賓。"));
				}
			}
			openDoor(5151); // 打開第五扇門.
			BossRoom = false;
		}
	}
	
	private void Demon_Delete(){
		Collection<L1Object> cklist = L1World.getInstance().getVisibleObjects(_map).values();
		for(L1Object ob : cklist){
			if(ob == null)
				continue;
			
			if(ob instanceof L1ItemInstance){
				L1ItemInstance obj = (L1ItemInstance)ob;
				L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
				groundInventory.removeItem(obj);
			}else if(ob instanceof L1NpcInstance){
				L1NpcInstance npc = (L1NpcInstance)ob;
				npc.deleteMe();
			}else if(ob instanceof L1Inventory) {
				((L1Inventory)ob).clearItems();
			}
		}
		DemonSystem.getInstance().removeDemon(_map);
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
		if(check == 0){End();}
	}
	
	private void openDoor(int doorId) {
		L1DoorInstance door = null;
		for (L1Object object : L1World.getInstance().getVisibleObjects(_map).values()) {
			if(object instanceof L1DoorInstance){
				door = (L1DoorInstance)object;
				if (door.getNpcTemplate().get_npcId() == doorId){
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
// L1Teleport.teleport(pc, 34068, 32311, (short)4, 4, true); // 這是將被傳送的座標
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
		case 0: if (npc == null || _list0.contains(npc)) { return; }
		_list0.add(npc); break;
		case 1: if (npc == null || _list1.contains(npc)) { return; }
		_list1.add(npc); break;
		case 2: if (npc == null || _list2.contains(npc)) { return; }
		_list2.add(npc); break;
		case 3: if (npc == null || _list3.contains(npc)) { return; }
		_list3.add(npc); break;
		case 4: if (npc == null || _list4.contains(npc)) { return; }
		_list4.add(npc); break;
		case 5: if (npc == null || _list5.contains(npc)) { return; }
		_list5.add(npc); break;
		}
	}
	
	private void remove(L1NpcInstance npc, int type) {
		switch (type) {
		case 0: if (npc == null || !_list0.contains(npc)) { return; }
		_list0.remove(npc); break;
		case 1: if (npc == null || !_list1.contains(npc)) { return; }
		_list1.remove(npc); break;
		case 2: if (npc == null || !_list2.contains(npc)) { return; }
		_list2.remove(npc); break;
		case 3: if (npc == null || !_list3.contains(npc)) { return; }
		_list3.remove(npc); break;
		case 4: if (npc == null || !_list4.contains(npc)) { return; }
		_list4.remove(npc); break;
		case 5: if (npc == null || !_list5.contains(npc)) { return; }
		_list5.remove(npc); break;
		}
	}

	private void ListClear(int type) {
		switch (type) {
		case 0: _list0.clear(); break;
		case 1: _list1.clear(); break;
		case 2: _list2.clear(); break;
		case 3: _list3.clear(); break;
		case 4: _list4.clear(); break;
		case 5: _list5.clear(); break;
		}
	}
	
	private void SpawnMonster() {
		// NPC生成
		spawn(32734, 32802, (short) _map, 4, 5086, 1, 2, 0); // 象牙塔特工
		spawn(32860, 32920, (short) _map, 6, 5087, 1, 2, 0); // 斯賓

		// 門生成
		spawn(32784, 32818, (short) _map, 0, 5147, 1, 2, 0);
		spawn(32852, 32806, (short) _map, 0, 5148, 1, 2, 0);
		spawn(32822, 32855, (short) _map, 0, 5149, 1, 2, 0);
		spawn(32762, 32916, (short) _map, 0, 5150, 1, 2, 0);
		spawn(32852, 32920, (short) _map, 0, 5151, 1, 2, 0);

		// 1號房間生成
		for (int i = 0; i < 15; i++) { 
			spawn(32765, 32818, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 12, 2, 1);
		}
		spawn(32749, 32800, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 2, 1);
		spawn(32749, 32817, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 2, 1);
		spawn(32766, 32833, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 2, 1);
		spawn(32772, 32802, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 2, 1);

		// 2號房間生成
		spawn(32813, 32805, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 2, 2);
		spawn(32819, 32807, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 2, 2);
		spawn(32819, 32805, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 2, 2);
		for (int i = 0; i < 15; i++) {
			spawn(32833, 32806, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 13, 2, 2);
		}
		
		// 3號房間生成
		spawn(32859, 32832, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 2, 3);
		spawn(32857, 32831, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 2, 3);
		spawn(32855, 32834, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 0, 2, 3);
		for (int i = 0; i < 15; i++) {
			spawn(32850, 32853, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 10, 2, 3);
		}

		// 4號房間生成
		for (int i = 0; i < 15; i++) {
			spawn(32767, 32892, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 12, 2, 4);
		}

		// 5號房間生成 (添加BOSS怪物：冰惡魔)
		for (int i = 0; i < 15; i++) {
			spawn(32767, 32892, (short) _map, 6, _MonsterList[_random.nextInt(100) % _MonsterList.length], 12, 2, 5);
		}
		
		spawn(32845, 32920, (short) _map, 6, BossIds[m_select_bosses_index], 0, 2, 5); // 冰惡魔
	}
	
	public static void spawn(int x, int y, short MapId, int Heading, int npcId, int randomRange, int type, int roomnumber) {
		try {// 類型原本是2，但冰雪女王的類型是1，跟隨類型的話
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
			if (npc instanceof L1DoorInstance){
				if (npc.getNpcId() == 5147){
					((L1DoorInstance) npc).setLeftEdgeLocation(32816);
					((L1DoorInstance) npc).setRightEdgeLocation(32821);
					((L1DoorInstance) npc).setDirection(1);
				} else if (npc.getNpcId() == 5148){
					((L1DoorInstance) npc).setLeftEdgeLocation(32804);
					((L1DoorInstance) npc).setRightEdgeLocation(32809);
					((L1DoorInstance) npc).setDirection(1);
				} else if (npc.getNpcId() == 5149){
					((L1DoorInstance) npc).setLeftEdgeLocation(32853);
					((L1DoorInstance) npc).setRightEdgeLocation(32858);
					((L1DoorInstance) npc).setDirection(1);
				} else if (npc.getNpcId() == 5150){
					((L1DoorInstance) npc).setLeftEdgeLocation(32760);
					((L1DoorInstance) npc).setRightEdgeLocation(32764);
					((L1DoorInstance) npc).setDirection(0);
				} else if (npc.getNpcId() == 5151){
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

			switch(type){
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
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 開始聊天

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
