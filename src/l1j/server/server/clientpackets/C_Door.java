package l1j.server.server.server.clientpackets;

import java.util.ArrayList;
import java.util.Random;

import l1j.server.Config;
import l1j.server.MJTemplate.MJL1Type;
import l1j.server.MJTemplate.TreasureChest.MJL1TreasureChest;
import l1j.server.server.ActionCodes;

import l1j.server.server.server.datatables.HouseTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;

public class C_Door extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_DOOR = "[C] C_Door";

	private static Random _random = new Random(System.nanoTime());

	public C_Door(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);
		int locX = readH(); // 點擊門
		int locY = readH(); // 點擊門
		int objectId = readD();

		L1PcInstance pc = client.getActiveChar();
		if (pc == null)
			return;

		// L1DoorInstance door =
		// (L1DoorInstance)L1World.getInstance().findObject(objectId);
		L1DoorInstance door = null;
		L1Object obj = L1World.getInstance().findObject(objectId);
		if(obj == null)
			return;
		if(obj.instanceOf(MJL1Type.L1TYPE_TREASURE_CHEST)){
			((MJL1TreasureChest)obj).open(pc);
			return;
		} else if (obj instanceof L1DoorInstance) {
			door = (L1DoorInstance) obj;
		}

//		System.out.println("確認是否收到封包" + objectId + " + " + door.getDoorId() + " + " + door.getNpcId());
		
		if (L1World.getInstance().findObject(objectId) == null || !(L1World.getInstance().findObject(objectId) instanceof L1DoorInstance)) {
			return;
		}

		if (locX > pc.getX() + 1 || locX < pc.getX() - 1 || locY > pc.getY() + 1 || locY < pc.getY() - 1) {
			return;
		}

		if (door.getNpcId() >= 5147 && door.getNpcId() <= 5151) {
			return;
		}
		
		if(door.getDoorId() >= 120825 && door.getDoorId() <= 120830) {
			return;
		}

		if (door != null && !isExistKeeper(pc, door.getKeeperId())) {
			if (door.getNpcId() >= 780220 && door.getNpcId()<=7800223) { // 魯恩城的寶藏探險之門
//                System.out.println("不行，回去?");
				return;
			}
			if (door.getNpcId() >= 7800316 && door.getNpcId() <= 7800319) { // 阿烏拉基亞淨化雕像
				if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
					door.close();
				} else {
					return;
				}
			}
			if (door.getNpcId() >= 7800240 && door.getNpcId() <= 7800243) { // 魯恩城的寶藏探險箱子
				if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
					if (pc.getInventory().checkItem(14000006, 1)){
						pc.getInventory().consumeItem(14000006, 1);
						door.open();
						Luun_Secret_Box(pc, door);
					}
				} 
				return;
			}
			if (door.getNpcId() == 7800245 || door.getNpcId() == 7800246) { // 伊娃王國?新寶物房箱子
				if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
					door.open();
					WhaleBox(pc, door);
				} 
				return;
			}
			if (door.getDoorId() == 113) {
				if (pc.getInventory().checkItem(40163)) {
					pc.getInventory().consumeItem(40163, 1);
				} else {
					return;
				}
			}
			// if(door.getDoorId() == 125){
			// if(pc.getInventory().checkItem(40313)){
			// pc.getInventory().consumeItem(40313, 1);
			// }else{
			// return;
			// }
			// }

			// 說話之島地城2層首領之門
			if (door.getDoorId() >= 4100 && door.getDoorId() <= 4111) {
				if (pc.getInventory().checkItem(40313, 1)) {
					pc.getInventory().consumeItem(40313, 1);
					pc.baphometRoom = true;
				} else {
					pc.baphometRoom = false;
					return;
				}
			}
			if (door.getDoorId() >= 8001 && door.getDoorId() <= 8010) {
				if (pc.getInventory().checkItem(L1ItemId.GIRANCAVE_BOXKEY, 1)) {
					giranCaveBox(pc, door);
					return;
				} else {
					return;
				}
			}
			if (door.getDoorId() >= 900151 && door.getDoorId() <= 900154) {// 哈丁之門
				return;
			}

			/** 火龍的安息處 */
			if (door.getNpcId() >= 7210013 && door.getNpcId() <= 7210015) {
				return;
			}
			if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
				if (!(door.getDoorId() >= 7210016 && door.getDoorId() <= 7210019)) {
					door.close();
				}
			} else if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
				door.open();
				if (door.getDoorId() >= 7210016 && door.getDoorId() <= 7210019) {
					phoenixEgg(pc, door);
				}
			}
		}
	}

	/**
	 * 伊娃王國?新 20221012 寶物房
	 * @param pc
	 * @param door
	 */
	private void WhaleBox(L1PcInstance pc, L1DoorInstance door) {
		int items[] = Config.TreasureBox.EVA_BOX_ITEMS;
		ArrayList<Integer> itemList = new ArrayList<Integer>();
		for (Integer temp : items) {
			itemList.add(temp);
		}
		if (itemList.size() ==0 || itemList.isEmpty()) {
			return;
		}
		int counts[] = Config.TreasureBox.EVA_BOX_COUNTS;
		ArrayList<Integer> countsList = new ArrayList<Integer>();
		for (Integer temp : counts) {
			countsList.add(temp);
		}
		if (itemList.size() != countsList.size()) {
			System.out.println("伊娃王國箱子物品數量錯誤");
			return;
		}
		int proc[] = Config.TreasureBox.EVA_BOX_PRO;
		ArrayList<Integer> proList = new ArrayList<Integer>();
		for (Integer temp : proc) {
			proList.add(temp);
		}
		if (itemList.size() != proList.size()) {
			System.out.println("伊娃王国箱子物品概率错误");
			return;
		}
		int SumTotal= 0;
		for (int i = 0; i<proList.size(); i++) {
			SumTotal += proList.get(i);
		}
		if (SumTotal == 0) {
			System.out.println("伊娃王國尋寶箱錯誤");
			return;
		}
		
		int ran = _random.nextInt(SumTotal) + 1; 
		int TempProc = 0;
		for (int i = 0; i< itemList.size();i++) {
			TempProc += proList.get(i);
			if (ran < TempProc) {
				L1ItemInstance item = pc.getInventory().storeItem(itemList.get(i), countsList.get(i), true);
				if (countsList.get(i) == 1) {
					pc.sendPackets(String.valueOf(new S_SystemMessage(item.getName() + " 已獲得。")));
				} else {
					pc.sendPackets(String.valueOf(new S_SystemMessage(item.getName() + "(" + countsList.get(i) + ") 已獲得。")));
				}
				break;
			}
		}
	}



	/**
	 * 魯恩城尋寶箱
	 * @param pc
	 * @param door
	 */
	private void Luun_Secret_Box(L1PcInstance pc, L1DoorInstance door) {
 		int items[] = Config.TreasureBox.LUUN_SECRET_BOX_ITEMS;
		ArrayList<Integer> itemList = new ArrayList<Integer>();
		for (Integer temp : items) {
			itemList.add(temp);
		}
		if (itemList.size() ==0 || itemList.isEmpty()) {
			return;
		}
		int counts[] = Config.TreasureBox.LUUN_SECRET_BOX_COUNTS;
		ArrayList<Integer> countsList = new ArrayList<Integer>();
		for (Integer temp : counts) {
			countsList.add(temp);
		}
		if (itemList.size() != countsList.size()) {
			System.out.println("魯恩城尋寶箱物品數量錯誤");
			return;
		}
		int proc[] = Config.TreasureBox.LUUN_SECRET_BOX_PRO;
		ArrayList<Integer> proList = new ArrayList<Integer>();
		for (Integer temp : proc) {
			proList.add(temp);
		}
		if (itemList.size() != proList.size()) {
			System.out.println("魯恩城尋寶箱物品概率錯誤");
			return;
		}
		int SumTotal= 0;
		for (int i = 0; i<proList.size(); i++) {
			SumTotal += proList.get(i);
		}
		if (SumTotal == 0) {
			System.out.println("魯恩城尋寶箱錯誤");
			return;
		}
		
		int ran = _random.nextInt(SumTotal) + 1; 
		int TempProc = 0;
		for (int i = 0; i< itemList.size();i++) {
			TempProc += proList.get(i);
			if (ran < TempProc) {
				L1ItemInstance item = pc.getInventory().storeItem(itemList.get(i), countsList.get(i), true);
				if (countsList.get(i) == 1) {
					pc.sendPackets(String.valueOf(new S_SystemMessage(item.getName() + " 已獲得。")));
				} else {
					pc.sendPackets(String.valueOf(new S_SystemMessage(item.getName() + "(" + countsList.get(i) + ") 已獲得。")));
				}
				break;
			}
		}
	}


	// TODO 奇岩監獄箱
	private void giranCaveBox(L1PcInstance pc, L1DoorInstance door) {
		int ran = _random.nextInt(100) + 1;
		if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
			pc.getInventory().consumeItem(L1ItemId.GIRANCAVE_BOXKEY, 1);
			door.open();
			if (ran >= 0 && ran <= 15) {
				pc.getInventory().storeItem(40308, 10000);
				pc.sendPackets(String.valueOf(new S_SystemMessage("金幣 (10000) 已獲得。")));
			} else if (ran >= 16 && ran <= 29) {
				pc.getInventory().storeItem(40308, 20000);
				pc.sendPackets(String.valueOf(new S_SystemMessage("金幣 (20000) 已獲得。")));
			} else if (ran >= 30 && ran <= 49) {
				pc.getInventory().storeItem(40308, 30000);
				pc.sendPackets(String.valueOf(new S_SystemMessage("金幣 (30000) 已獲得。")));
			} else if (ran >= 50 && ran <= 59) {
				pc.getInventory().storeItem(40308, 50000);
				pc.sendPackets(String.valueOf(new S_SystemMessage("金幣 (50000) 已獲得。")));
			} else if (ran >= 60 && ran <= 64) {
				pc.getInventory().storeItem(40308, 100000);
				pc.sendPackets(String.valueOf(new S_SystemMessage("金幣 (100000) 已獲得。")));
			} else if (ran >= 65 && ran <= 69) {
				pc.getInventory().storeItem(40308, 200000);
				pc.sendPackets(String.valueOf(new S_SystemMessage("金幣 (200000) 已獲得。")));
			} else if (ran >= 70 && ran <= 72) {
				pc.getInventory().storeItem(40308, 300000);
				pc.sendPackets(String.valueOf(new S_SystemMessage("金幣 (300000) 已獲得。")));
			} else if (ran >= 73 && ran <= 75) {
				pc.getInventory().storeItem(40308, 400000);
				pc.sendPackets(String.valueOf(new S_SystemMessage("金幣 (400000) 已獲得。")));
			} else if (ran >= 76 && ran <= 78) {
				pc.getInventory().storeItem(40308, 500000);
				pc.sendPackets(String.valueOf(new S_SystemMessage("金幣 (500000) 已獲得。")));
			} else if (ran >= 79 && ran <= 80) {
				pc.getInventory().storeItem(40308, 1000000);
				pc.sendPackets(String.valueOf(new S_SystemMessage("金幣 (1000000) 已獲得。")));
			} else if (ran >= 81 && ran <= 90) {
				pc.getInventory().storeItem(40074, 3);
				pc.sendPackets(String.valueOf(new S_SystemMessage("對防具施法的卷軸 (3) 已獲得。")));
			} else if (ran >= 91 && ran <= 100) {
				pc.getInventory().storeItem(40087, 3);
				pc.sendPackets(String.valueOf(new S_SystemMessage("對武器施法的卷軸 (3) 已獲得。")));
			}
		}

	}

	// TODO 奇岩監獄箱
	private void phoenixEgg(L1PcInstance pc, L1DoorInstance door) {
		int ran = _random.nextInt(100) + 1;
		L1ItemInstance item = null;
		if (ran >= 0 && ran <= 10) {
			item = pc.getInventory().storeItem(40010, 5);
			pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getName() + " (5)")));
		} else if (ran >= 11 && ran <= 20) {
			item = pc.getInventory().storeItem(40010, 10);
			pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getName() + " (10)")));
		} else if (ran >= 21 && ran <= 30) {
			item = pc.getInventory().storeItem(40010, 15);
			pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getName() + " (15)")));
		} else if (ran >= 31 && ran <= 40) {
			item = pc.getInventory().storeItem(40010, 30);
			pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getName() + " (30)")));
		} else if (ran >= 41 && ran <= 50) {
			item = pc.getInventory().storeItem(40010, 50);
			pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getName() + " (50)")));
		} else if (ran >= 51 && ran <= 60) {
			item = pc.getInventory().storeItem(40012, 5);
			pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getName() + " (5)")));
		} else if (ran >= 61 && ran <= 70) {
			item = pc.getInventory().storeItem(40012, 10);
			pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getName() + " (10)")));
		} else if (ran >= 71 && ran <= 80) {
			item = pc.getInventory().storeItem(40012, 15);
			pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getName() + " (15)")));
		} else if (ran >= 81 && ran <= 90) {
			item = pc.getInventory().storeItem(40012, 30);
			pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getName() + " (30)")));
		} else if (ran >= 91 && ran <= 100) {
			item = pc.getInventory().storeItem(40012, 50);
			pc.sendPackets(String.valueOf(new S_ServerMessage(403, item.getName() + " (50)")));
		}
	}

	private boolean isExistKeeper(L1PcInstance pc, int keeperId) {
		if (keeperId == 0) {
			return false;
		}

		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				if (keeperId == house.getKeeperId()) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String getType() {
		return C_DOOR;
	}
}
