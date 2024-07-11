package l1j.server.IndunSystem.FanstasyIsland;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Random;

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
	// private static final int FOURTH_STEP = 5;
	private static final int LAST_STEP = 5;
	private static final int END = 6;

	private int _status;
	private L1NpcInstance unicorn;
	private L1NpcInstance boss;
	private L1PcInstance pc;

	private boolean Running = true;

	public ArrayList<L1NpcInstance> BasicNpcList;
	public ArrayList<L1NpcInstance> NpcList;

	public FantasyIsland(int id, L1PcInstance pc) {
		_map = (short) id;
		this.pc = pc;
	}

	@Override
	public void run() {
		setting();
		NpcList = FantasyIslandSpawn.getInstance().fillSpawnTable(_map, 1, true);
		while (Running) {
			try {

				if (NpcList != null) {
					for (L1NpcInstance npc : NpcList) {
						if (npc == null || npc.isDead())
							NpcList.remove(npc);
					}
				}

				if (unicorn.isDead()) {
					if (pc != null) {
						// L1Teleport.teleport(pc, 33968, 32961, (short) 4, 2, true);
						pc.start_teleport(33968, 32961, 4, 2, 18339, true, false);
						pc.getInventory().consumeItem(810006);
						pc.getInventory().consumeItem(810007);
						pc = null;
					}
					endRaid();
				}

				checkHp();
				checkPc();

				switch (stage) {
					case WAIT_RAID:
						if (NpcList.size() > 0)
							continue;
						Sleep(5000);
						Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17691", 0));
						// 感谢你来帮忙。

						Sleep(2000);

						Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17692", 0));
						// 异界的存在很快就会回来。

						Sleep(2000);

						Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17693", 0));
						// 在那之前，请为我争取时间解除封印。

						Sleep(3000);

						Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17947", 0));
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17947"));
						// 请使用魔法杖击败敌人。
						pc.getInventory().storeItem(810006, 1);
						pc.sendPackets(new S_SystemMessage("$17948"));
						Sleep(5000);
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17701"));
						pc.sendPackets(new S_PacketBox(S_PacketBox.ROUND, 1, 3));

						// 敵人正在聚集。
						// 11點方向生成 波爾西斯, 貝內博斯
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200008, 0, 0, 5);
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200012, 0, 0, 5);
						stage = 2;
						break;

					case FIRST_STEP:
						Sleep(10000);

						// 1點方向生成 蠍子+ 美杜莎
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200009, 0, 0, 5);
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200013, 0, 0, 5);
						Sleep(10000);

						// 5點方向生成 科特盧特+ 佩爾佩爾
						L1SpawnUtil.spawnCount(32801, 32872, _map, 7200010, 0, 0, 5);
						L1SpawnUtil.spawnCount(32801, 32872, _map, 7200014, 0, 0, 5);
						Sleep(10000);

						// 7點方向生成 美加+ 比亞
						L1SpawnUtil.spawnCount(32790, 32861, _map, 7200011, 0, 0, 5);
						L1SpawnUtil.spawnCount(32790, 32861, _map, 7200015, 0, 0, 5);
						// 大地的精靈出現了!!!
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17944"));
						stage = 3;
						break;
					/** 第二階段進展 **/
					case SECOND_STEP:
						Sleep(10000);
						pc.sendPackets(new S_PacketBox(S_PacketBox.ROUND, 2, 3));
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17703"));
						// 更多的敵人即將到來。請準備
						pc.getInventory().storeItem(810006, 1);
						pc.sendPackets(new S_SystemMessage("$17948"));
						Sleep(5000);
						// 11點 波西斯、貝尼博斯
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200008, 0, 0, 5);
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200012, 0, 0, 5);
						// 1點方向 美杜莎+蝎子+大地之靈
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200009, 0, 0, 5);
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200013, 0, 0, 5);
						L1SpawnUtil.spawnCount(32810, 32863, _map, 7200018, 0, 0, 1);
						Sleep(10000);
						// 5 點鐘方向 外套 + 佩爾
						L1SpawnUtil.spawnCount(32801, 32872, _map, 7200010, 0, 0, 5);
						L1SpawnUtil.spawnCount(32801, 32872, _map, 7200014, 0, 0, 5);
						Sleep(20000);
						// 7點兆加+維亞
						L1SpawnUtil.spawnCount(32790, 32861, _map, 7200011, 0, 0, 5);
						L1SpawnUtil.spawnCount(32790, 32861, _map, 7200015, 0, 0, 5);
						Sleep(5000);
						// 11點 波西斯、貝尼博斯
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200008, 0, 0, 5);
						L1SpawnUtil.spawnCount(32799, 32852, _map, 7200012, 0, 0, 5);
						Sleep(5000);
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17942"));
						// 偉大的風之精靈出現了！
						stage = 4;
						break;
					/** 3단계 **/
					case THIRD_STEP:
						Sleep(3000);
						pc.sendPackets(new S_PacketBox(S_PacketBox.ROUND, 3, 3));
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17703"));
						// 更多的敵人即將到來。請準備
						pc.getInventory().storeItem(810006, 1);
						pc.sendPackets(new S_SystemMessage("$17948"));
						Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17706", 0));

						Sleep(5000);
						// 4支軍隊同時出現+大風之靈
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
						// 4支軍隊同時生成
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
						// 想要奪走獨角獸？這樣可不行！！
						Sleep(5000);
						// 夢幻的支配者+ 四處生成
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
							boss = L1SpawnUtil.spawnCount(32790, 32861, _map, 7200020, 0, 0, 1); // 九尾狐
						} else if (chance <= 30) {
							boss = L1SpawnUtil.spawnCount(32790, 32861, _map, 7199998, 0, 0, 1); // 阿比什
						} else if (chance <= 45) {
							boss = L1SpawnUtil.spawnCount(32790, 32861, _map, 7199999, 0, 0, 1); // 阿茲莫丹
						}

						stage = 5;
						break;
					case LAST_STEP:
						if (boss.isDead() || boss == null) {
							Sleep(5000);
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17707"));
							// 夢想的統治者已被擊敗.
							Sleep(5000);
							Broadcaster.broadcastPacket(unicorn, new S_SkillSound(unicorn.getId(), 1911));
							Sleep(1000);
							Broadcaster.broadcastPacket(unicorn, new S_ChangeShape(unicorn.getId(), 12493));
							// Sleep(5000);
							// 謝謝你！
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17708"));
							Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17708", 0));

							Sleep(3000);
							// 暫時不會回來.
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17709"));
							Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17709", 0));

							Sleep(3000);
							// 我想我很快就得回夢幻島了。.
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17710"));
							Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17710", 0));

							Sleep(3000);
							// 我想送你一件禮物。我希望你喜歡它.
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "$17712"));
							Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17712", 0));

							Sleep(3000);
							Broadcaster.broadcastPacket(unicorn, new S_SkillSound(unicorn.getId(), 169));
							L1ItemInstance item = ItemTable.getInstance().createItem(31089);
							L1World.getInstance().getInventory(unicorn.getX(), unicorn.getY(), unicorn.getMapId())
									.storeItem(item);
							// L1ItemInstance item1 = ItemTable.getInstance().createItem(itemId);
							// if (item1 != null) {
							// L1World.getInstance().getInventory(unicorn.getX(), unicorn.getY(),
							// unicorn.getMapId()).storeItem(item1);
							// }

							unicorn.deleteMe();
							stage = 6;
						}
						break;
					case END:
						Thread.sleep(10000);
						if (pc.getMapId() == _map) {
							// pc.sendPackets(new S_ServerMessage(1480));
							// 系統提示：5秒後傳送。
							pc.sendPackets(new S_SystemMessage("稍後將移動到村莊."));
						}
						Thread.sleep(10000);

						// L1Teleport.teleport(pc, 33449, 32790, (short) 4, 2, true);
						pc.start_teleport(33451, 32812, 4, 2, 18339, true, false);
						pc.getInventory().consumeItem(810006);
						pc.getInventory().consumeItem(810007);
						pc = null;
						break;
					default:
						break;
				}
			} catch (Exception e) {
			} finally {
				try {
					Thread.sleep(1500);
				} catch (Exception e) {
				}
			}
		}
		endRaid();
	}

	private void Sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
		}
	}

	private void setting() {
		for (L1NpcInstance npc : BasicNpcList) {
			if (npc != null) {
				if (npc.getName().equalsIgnoreCase("獨角獸")) {
					unicorn = npc;
				}
			}
		}
	}

	private void checkHp() {
		if ((unicorn.getMaxHp() * 1 / 5) > unicorn.getCurrentHp()) { // 2000
			if (_status != 4) {
				Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17949", 0));
				// 我想這將會變得困難.
				_status = 4;
			}
		} else if ((unicorn.getMaxHp() * 2 / 5) > unicorn.getCurrentHp()) { // 4000
			if (_status != 3) {
				Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17950", 0));
				// 如果我能再撐一會兒就好了…
				_status = 3;
			}
		} else if ((unicorn.getMaxHp() * 3 / 5) > unicorn.getCurrentHp()) { // 6000
			if (_status != 2) {
				Broadcaster.broadcastPacket(unicorn, new S_NpcChatPacket(unicorn, "$17952", 0));
				_status = 2;
			}
		} else if ((unicorn.getMaxHp() * 4 / 5) > unicorn.getCurrentHp()) { // 8000
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

	public void Start() {
		// 獲取當前時間的 Calendar 實例
		Calendar cal = Calendar.getInstance();

		// 定義常量對應小時和分鐘
		int 時間 = Calendar.HOUR;
		int 分鐘 = Calendar.MINUTE;

		/** 上午 0 點、下午 1 點 **/
		// 初始化變量為 "下午"
		String 上午下午 = "下午";
		// 如果當前時間是上午
		if (cal.get(Calendar.AM_PM) == 0) {
			// 設置變量為 "上午"
			上午下午 = "上午";
		}

		// 使用線程池調度當前對象，延遲 2000 毫秒後執行
		GeneralThreadPool.getInstance().schedule(this, 2000);

		// 打印格式化的消息，包含上午/下午、時間、分鐘和地圖信息
		System.out.println(
				上午下午 + " " + cal.get(時間) + "時" + cal.get(分鐘) + "分" + "   ■■■■■■ 夢幻的島嶼開始 " + _map + " ■■■■■■"
		);
	}

	private void endRaid() {
		// 獲取當前時間的 Calendar 實例
		Calendar cal = Calendar.getInstance();

		// 定義常量對應小時和分鐘
		int 時間 = Calendar.HOUR;
		int 分鐘 = Calendar.MINUTE;

		/** 0 上午 , 1 下午 * */
		// 初始化變量為 "下午"
		String 上午下午 = "下午";
		// 如果當前時間是上午
		if (cal.get(Calendar.AM_PM) == 0) {
		// 設置變量為 "上午"
			上午下午 = "上午";
		}

		// 如果活動進行中
		if (Running) {
			// 獲取指定地圖上的所有可見對象
			Collection<L1Object> cklist = L1World.getInstance().getVisibleObjects(_map).values();
			for (L1Object ob : cklist) {
				// 如果對象為空，繼續下一個循環
				if (ob == null)
					continue;

				// 如果對象是物品實例
				if (ob instanceof L1ItemInstance) {
					L1ItemInstance obj = (L1ItemInstance) ob;
				// 獲取物品所在位置的地面物品
					L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
				// 移除物品
					groundInventory.removeItem(obj);
				}
					// 如果對象是 NPC 實例
				else if (ob instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) ob;
					// 刪除 NPC
					npc.deleteMe();
				}
			}

			// 將活動狀態設置為已結束
			Running = false;
			// 從系統中移除該地圖的活動
			FantasyIslandSystem.getInstance().remove(_map);
			// 打印格式化的消息
			System.out.println(
					上午下午 + " " + cal.get(時間) + "時" + cal.get(分鐘) + "分鐘" + "   ■■■■■■ 夢幻的島嶼結束 " + _map + " ■■■■■■"
			);
		}
	}
