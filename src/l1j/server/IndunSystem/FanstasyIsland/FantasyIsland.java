package l1j.server.IndunSystem.FanstasyIsland;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Random;

import l1j.server.MJInstanceSystem.MJInstanceEnums;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.L1SpawnUtil;

public class FantasyIsland implements Runnable {

	private short _map;
	private int stage = 1;
	private static final int WAIT_RAID = 1;
	private static final int FIRST_STEP = 2;
	private static final int SECOND_STEP = 3;
	private static final int THIRD_STEP = 4;
	//private static final int FOURTH_STEP = 5;
	private static final int LAST_STEP = 5;
	private static final int END = 6;

	private int _status;
	private L1NpcInstance unicorn;
	private L1NpcInstance boss;
	private L1PcInstance pc;

	private boolean Running = true;

	public ArrayList<L1NpcInstance> BasicNpcList;
	public ArrayList<L1NpcInstance> NpcList;

	public FantasyIsland(int id, L1PcInstance pc){
		_map = (short)id;
		this.pc = pc;
	}

	@Override
	public void run() {
		setting();
		NpcList = FantasyIslandSpawn.getInstance().fillSpawnTable(_map, 1, true);
		while(Running){
			try {

				if(NpcList != null){
					for(L1NpcInstance npc : NpcList){
						if(npc == null || npc.isDead())
							NpcList.remove(npc);
					}
				}

				if (unicorn.isDead()) {
					if (pc != null) {
						//L1Teleport.teleport(pc, 33968, 32961, (short)  4, 2, true);
						pc.start_teleport(33968, 32961, 4, 2, 18339, true, false);
						pc.getInventory().consumeItem(810006);
						pc.getInventory().consumeItem(810007);
						pc = null;
					}
					endRaid();
				}

				checkHp();
				checkPc();

				switch(stage){
					case WAIT_RAID:
						if(NpcList.size() > 0)
							continue;
						Sleep(5000);
						Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17691", 0));
						// 謝謝你來幫忙。
						Sleep(2000);
						Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17692", 0));
						// 異界的存在即將回來。
						Sleep(2000);
						Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17693", 0));
						// 在那之前，請幫我爭取時間解除封印。
						Sleep(3000);
						Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17947", 0));
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17947"));
						// 請用魔法棒消滅敵人。
						pc.getInventory().storeItem(810006, 1);
						pc.sendPackets(new S_SystemMessage("$17948"));
						Sleep(5000);
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17701"));
						pc.sendPackets(new S_PacketBox(S_PacketBox.ROUND, 1, 3));
						// 敵人正在湧來。
						// 在11點鐘方向生成波爾西斯和貝尼沃斯。
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200008, 0, 0, 5);
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200012, 0, 0, 5);
						stage = 2;
						break;
					case FIRST_STEP:
						Sleep(10000);
						// 1點方向生成蠍子和美杜莎
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200009, 0, 0, 5);
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200013, 0, 0, 5);
						Sleep(10000);
						// 5點方向生成科特魯和菲爾菲爾
						L1SpawnUtil.spawnCount(32801, 32872, _map, 7200010, 0, 0, 5);
						L1SpawnUtil.spawnCount(32801, 32872, _map, 7200014, 0, 0, 5);
						Sleep(10000);
						// 7點方向生成梅加和比亞
						L1SpawnUtil.spawnCount(32790, 32861, _map, 7200011, 0, 0, 5);
						L1SpawnUtil.spawnCount(32790, 32861, _map, 7200015, 0, 0, 5);
						// 土之大精靈出現了!!!
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17944"));
						stage = 3;
						break;
					/* 第二階段進行 **/
					case SECOND_STEP:
						Sleep(10000);
						pc.sendPackets(new S_PacketBox(S_PacketBox.ROUND, 2, 3));
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17703"));
						// 敵人更多地湧來了，請做好準備
						pc.getInventory().storeItem(810006, 1);
						pc.sendPackets(new S_SystemMessage("$17948"));
						Sleep(5000);
						// 11點方向生成波爾西斯和貝尼沃斯
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200008, 0, 0, 5);
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200012, 0, 0, 5);
						// 1點方向生成美杜莎、蠍子和土之大精靈
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200009, 0, 0, 5);
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200013, 0, 0, 5);
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200018, 0, 0, 1);
						Sleep(10000);
						// 5點方向生成科特魯和菲爾菲爾
						L1SpawnUtil.spawnCount(32801, 32872, _map, 7200010, 0, 0, 5);
						L1SpawnUtil.spawnCount(32801, 32872, _map, 7200014, 0, 0, 5);
						Sleep(20000);
						// 7點方向生成梅加和比亞
						L1SpawnUtil.spawnCount(32790, 32861, _map, 7200011, 0, 0, 5);
						L1SpawnUtil.spawnCount(32790, 32861, _map, 7200015, 0, 0, 5);
						Sleep(5000);
						// 11點方向生成波爾西斯和貝尼沃斯
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200008, 0, 0, 5);
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200012, 0, 0, 5);
						Sleep(5000);
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17942"));
						// 風之大精靈出現了!!!
						stage = 4;
						break;
					/* 第三階段 **/
					case THIRD_STEP:
						Sleep(3000);
						pc.sendPackets(new S_PacketBox(S_PacketBox.ROUND, 3, 3));
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17703"));
						// 敵人更多地湧來了，請做好準備
						pc.getInventory().storeItem(810006, 1);
						pc.sendPackets(new S_SystemMessage("$17948"));
						Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17706", 0));

						Sleep(5000);
						// 四處同時生成敵人 + 風之大精靈
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200008, 0, 0, 3);
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200012, 0, 0, 3);
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200009, 0, 0, 3);
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200013, 0, 0, 3);
						L1SpawnUtil.spawnCount(32801, 32872, _map, 7200010, 0, 0, 3);
						L1SpawnUtil.spawnCount(32801, 32872, _map, 7200014, 0, 0, 3);
						L1SpawnUtil.spawnCount(32790, 32861, _map, 7200011, 0, 0, 3);
						L1SpawnUtil.spawnCount(32790, 32861, _map, 7200015, 0, 0, 3);
						L1SpawnUtil.spawnCount(32801, 32872, _map, 7200016, 0, 0, 1);
						Sleep(5000);
						// 四處同時生成敵人
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200008, 0, 0, 3);
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200012, 0, 0, 3);
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200009, 0, 0, 3);
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200013, 0, 0, 3);
						L1SpawnUtil.spawnCount(32801, 32872, _map, 7200010, 0, 0, 3);
						L1SpawnUtil.spawnCount(32801, 32872, _map, 7200014, 0, 0, 3);
						L1SpawnUtil.spawnCount(32790, 32861, _map, 7200011, 0, 0, 3);
						L1SpawnUtil.spawnCount(32790, 32861, _map, 7200015, 0, 0, 3);
						Sleep(15000);
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17995 : $17713"));
						// 想奪走獨角獸？那可不行！
						Sleep(5000);
						// 夢幻的支配者 + 四處生成敵人
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200008, 0, 0, 4);
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200012, 0, 0, 4);
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200009, 0, 0, 4);
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200013, 0, 0, 4);
						L1SpawnUtil.spawnCount(32801, 32872, _map, 7200010, 0, 0, 4);
						L1SpawnUtil.spawnCount(32801, 32872, _map, 7200014, 0, 0, 4);
						L1SpawnUtil.spawnCount(32790, 32861, _map, 7200011, 0, 0, 4);
						L1SpawnUtil.spawnCount(32790, 32861, _map, 7200015, 0, 0, 4);

						Random random = new Random(System.nanoTime());
						int chance = random.nextInt(45) + 1;
						if (chance <= 15) {
							boss = L1SpawnUtil.spawnCount(32790, 32861, _map, 7200020, 0, 0, 1);    // 九尾狐
						} else if (chance <= 30) {
							boss = L1SpawnUtil.spawnCount(32790, 32861, _map, 7199998, 0, 0, 1);    // 阿比希
						} else {
							boss = L1SpawnUtil.spawnCount(32790, 32861, _map, 7199999, 0, 0, 1);    // 阿茲莫丹
						}

						stage = 5;
						break;
					case LAST_STEP:
						if (boss.isDead() || boss == null) {
							Sleep(5000);
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17707"));
							// 夢幻的支配者被消滅了。
							Sleep(5000);
							Broadcaster.broadcastPacket(unicorn, new S_SkillSound(unicorn.getId(), 1911));
							Sleep(1000);
							Broadcaster.broadcastPacket(unicorn, new S_ChangeShape(unicorn.getId(), 12493));
							//Sleep(5000);
							// 謝謝！
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17708"));
							Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17708", 0));

							Sleep(3000);
							// 暫時它不會回來了。
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17709"));
							Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17709", 0));

							Sleep(3000);
							// 該回夢幻之島了。
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17710"));
							Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17710", 0));

							Sleep(3000);
							// 我想送您一份禮物，希望您喜歡。
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17712"));
							Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17712", 0));

							Sleep(3000);
							Broadcaster.broadcastPacket(unicorn, new S_SkillSound(unicorn.getId(), 169));
							L1ItemInstance item = ItemTable.getInstance().createItem(31089);
							L1World.getInstance().getInventory(unicorn.getX(), unicorn.getY(), unicorn.getMapId()).storeItem(item);
//						L1ItemInstance item1 = ItemTable.getInstance().createItem(itemId);
//						if (item1 != null) {
//							L1World.getInstance().getInventory(unicorn.getX(), unicorn.getY(), unicorn.getMapId()).storeItem(item1);
//						}

							unicorn.deleteMe();
							stage = 6;
						}
						break;
					case END:
						Thread.sleep(10000);
						if(pc.getMapId() == _map){
							//pc.sendPackets(new S_ServerMessage(1480));
							// 系統消息：5秒後進行傳送。
							pc.sendPackets(new S_SystemMessage("稍後將傳送到村莊."));
						}
						Thread.sleep(10000);

						//L1Teleport.teleport(pc, 33449, 32790, (short)  4, 2, true);
						pc.start_teleport(33451, 32812, 4, 2, 18339, true, false);
						pc.getInventory().consumeItem(810006);
						pc.getInventory().consumeItem(810007);
						pc = null;
						break;
					default:
						break;
				}
			}catch(Exception e){
			}finally{
				try{
					Thread.sleep(1500);
				}catch(Exception e){}
			}
		}
		endRaid();
		return new L1PcInstance[0];
	}

	private void Sleep(int time){
		try{
			Thread.sleep(time);
		}catch(Exception e){}
	}

	private void setting(){
		for(L1NpcInstance npc : BasicNpcList){
			if(npc != null){
				if(npc.getName().equalsIgnoreCase("獨角獸")){ // 如果 NPC 的名稱忽略大小寫等於 "유니콘"
					unicorn = npc;
				}
			}
		}
	}

	private void checkHp() {
		if ((unicorn.getMaxHp() / 5) > unicorn.getCurrentHp()) { //2000
			if (_status != 4) {
				Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17949", 0));
				//我覺得再也撐不下去了.
				_status = 4;
			}
		} else if ((unicorn.getMaxHp() * 2 / 5) > unicorn.getCurrentHp()) { //4000
			if (_status != 3) {
				Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17950", 0));
				//如果能再撐一下就好了....
				_status = 3;
			}
		} else if ((unicorn.getMaxHp() * 3 / 5) > unicorn.getCurrentHp()) { //6000
			if (_status != 2) {
				Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17952", 0));
				_status = 2;
			}
		} else if ((unicorn.getMaxHp() * 4 / 5) > unicorn.getCurrentHp()) { //8000
			if (_status != 1) {
				Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17952", 0));
				_status = 1;
			}
		}
	}

	private void checkPc() {
		int check = 0;
		for (L1Object obj : L1World.getInstance().getVisibleObjects(_map).values()) {
			if (obj instanceof L1PcInstance) {
				check = 1;
			}
		}
		if (check == 0) {
			if (pc != null) {
				pc = null;
			}
			endRaid();
		}
	}
	public void Start(){
		Calendar cal = Calendar.getInstance();
		int hour = Calendar.HOUR;
		int minute = Calendar.MINUTE;
		/* 0 AM, 1 PM */
		String amPm = "PM";
		if (cal.get(Calendar.AM_PM) == 0) {
			amPm = "AM";
		}
		GeneralThreadPool.getInstance().schedule(this, 2000);
		System.out.println("" + amPm + " " + cal.get(hour) + "時" + cal.get(minute) + "分" + "■■■■■■ 夢幻的島嶼開始 " + _map + " ■■■■■■");
	}
	private void endRaid(){
		Calendar cal = Calendar.getInstance();
		int hour = Calendar.HOUR;
		int minute = Calendar.MINUTE;
		/* 0 上午, 1 下午 */
		String amPm = "下午";
		if (cal.get(Calendar.AM_PM) == 0) {
			amPm = "上午";
		}
	}
		if (Running) {
			Collection<L1Object> list = L1World.getInstance().getVisibleObjects(_map).values();
			for(L1Object ob : list){
				if(ob == null) continue;
				if(ob instanceof L1ItemInstance obj){
					L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
					groundInventory.removeItem(obj);
				}else if(ob instanceof L1NpcInstance npc){
					npc.deleteMe();
				}
			}
			Running = false;
			FantasyIslandSystem.getInstance().remove(_map);
		String amPm = new String();
		MJInstanceEnums.InstSpcMessages cal = null;
		Object hour = new Object();
		Object minute = new Object();
		System.out.println("" + amPm + " " + cal.get(hour) + "時" + cal.get(minute) + "分" + "   ■■■■■■ 夢幻島嶼結束 " + _map + " ■■■■■■");
		}
	}

