package l1j.server.server.server.Controller;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


import l1j.server.Config;
import l1j.server.MJExpRevision.MJEFishingType;
import l1j.server.MJExpRevision.MJFishingExpInfo;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.SC_REST_EXP_INFO_NOTI;
import l1j.server.server.GameServerSetting;
import l1j.server.server.server.datatables.ExpTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.server.datatables.FastTable;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.server.serverpackets.S_Fishing;
import l1j.server.server.server.serverpackets.ServerBasePacket;

public class FishingTimeController implements Runnable {

	public static final int SLEEP_TIME = 1000;

	private static FishingTimeController _instance;

	private final List<L1PcInstance> _fishingList = (List<L1PcInstance>) new FastTable<L1PcInstance>();

	private static Random _random = new Random(System.nanoTime());

	public static FishingTimeController getInstance() {
		if (_instance == null) {
			_instance = new FishingTimeController();
		}
		return _instance;
	}

	public void run() {
		try {
			fishing();
		} catch (Exception e1) {
			e1.printStackTrace();// 異常處理輸出
		} finally {
		}
		return null;
	}

	public void addMember(L1PcInstance pc) {
		if (pc == null || _fishingList.contains(pc)) {
			return;
		}
		_fishingList.add(pc);
	}

	public boolean isMember(L1PcInstance pc) {
		if (_fishingList.contains(pc)) {
			return true;
		}
		return false;
	}

	public void removeMember(L1PcInstance pc) {
		if (pc == null || !_fishingList.contains(pc)) {
			return;
		}
		_fishingList.remove(pc);
	}

	public boolean grownUpFishing = false;
	public boolean highGrownUpFishing = false;
	public boolean ancientSilverFishing = false;
	public boolean ancientGoldFishing = false;

	private void fishing() {
		if (_fishingList.size() > 0) {
			long currentTime = System.currentTimeMillis();
			L1PcInstance pc = null;
			Iterator<L1PcInstance> iter = _fishingList.iterator();
			while (iter.hasNext()) {
				pc = iter.next();
				if (pc == null)
					continue;
				if (pc.getMapId() != 5490 || pc.isDead() || pc.getNetConnection() == null || pc.getCurrentHp() == 0)
					continue;

				if (pc.isFishing()) {
					if (pc.getLevel() >= Config.CharSettings.MaxLevel) {
						endFishing(pc);
						pc.sendPackets("由於等級限制，無法再進行釣魚。");
						continue;
					}
					long time = pc.getFishingTime();
					if (currentTime > (time + 1000)) {
						// TODO 如果有餌料
						if (pc._fishingRod.getItemId() == 600229 || pc._fishingRod.getItemId() == 87058
								|| pc._fishingRod.getItemId() == 87059 || pc._fishingRod.getItemId() == 4100293
								|| pc.getInventory().consumeItem(41295, 1)) {
							// TODO 安裝了繞線器的高彈性釣竿
							if (pc._fishingRod.getItemId() == 41294) {
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
									if (item.getChargeCount() <= 0) {
										L1ItemInstance newfishingRod = null;
										pc.getInventory().removeItem(item, 1);
										newfishingRod = pc.getInventory().storeItem(41293, 1);
										pc._fishingRod = newfishingRod;
										endFishing(pc);
									} else {
										item.setChargeCount(item.getChargeCount() - 1);
										pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
										pc.setFishingTime(System.currentTimeMillis() + 80000);
										pc.sendPackets(new S_Fishing(80));
										installHighElasticityFishingRodWithReel(pc);
									}
								}
								// TODO 安裝了繞線器的銀色釣竿
							} else if (pc._fishingRod.getItemId() == 41305) {
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
									if (item.getChargeCount() <= 0) {
										L1ItemInstance newfishingRod = null;
										pc.getInventory().removeItem(item, 1);
										newfishingRod = pc.getInventory().storeItem(41293, 1);
										pc._fishingRod = newfishingRod;
										endFishing(pc);
									} else {
										item.setChargeCount(item.getChargeCount() - 1);
										pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
										pc.setFishingTime(System.currentTimeMillis() + 80000);
										pc.sendPackets(new S_Fishing(80));
										installSilverFishingRodWithReel(pc);
									}
								}
								// TODO 安裝了繞線器的金色釣竿
							} else if (pc._fishingRod.getItemId() == 41306) {
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
									if (item.getChargeCount() <= 0) {
										L1ItemInstance newfishingRod = null;
										pc.getInventory().removeItem(item, 1);
										newfishingRod = pc.getInventory().storeItem(41293, 1);
										pc._fishingRod = newfishingRod;
										endFishing(pc);
									} else {
										item.setChargeCount(item.getChargeCount() - 1);
										pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
										pc.setFishingTime(System.currentTimeMillis() + 80000);
										pc.sendPackets(new S_Fishing(80));
										installGoldenFishingRodWithReel(pc);
									}
								}
								// TODO 黃金青蛙釣竿
							} else if (pc._fishingRod.getItemId() == 9991) {
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
									if (item.getChargeCount() <= 0) {
										L1ItemInstance newfishingRod = null;
										pc.getInventory().removeItem(item, 1);
										newfishingRod = pc.getInventory().storeItem(9993, 1); // 斷裂的釣竿
										pc._fishingRod = newfishingRod;
										endFishing(pc);
									} else {
										item.setChargeCount(item.getChargeCount() - 1);
										pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
										pc.setFishingTime(System.currentTimeMillis() + 80000);
										pc.sendPackets(new S_Fishing(80));
										goldenFrogFishingRod(pc);
									}
								}
								// TODO 古代的銀色釣竿
							} else if (pc._fishingRod.getItemId() == 87058) {
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
									if (item.getChargeCount() <= 0) {
										L1ItemInstance newfishingRod = null;
										pc.getInventory().removeItem(item, 1);
										newfishingRod = pc.getInventory().storeItem(41293, 1);
										pc._fishingRod = newfishingRod;
										endFishing(pc);
									} else {
										item.setChargeCount(item.getChargeCount() - 1);
										pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
										pc.setFishingTime(System.currentTimeMillis() + 60000);
										pc.sendPackets(new S_Fishing(60));
										ancientSilverFishingRod = true;
										Ancientsilver(pc);
									}
								}
								// TODO 古代的金色釣竿
							} else if (pc._fishingRod.getItemId() == 87059) {
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
									if (item.getChargeCount() <= 0) {
										L1ItemInstance newfishingRod = null;
										pc.getInventory().removeItem(item, 1);
										newfishingRod = pc.getInventory().storeItem(41293, 1);
										pc._fishingRod = newfishingRod;
										endFishing(pc);
									} else {
										item.setChargeCount(item.getChargeCount() - 1);
										pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
										pc.setFishingTime(System.currentTimeMillis() + 60000);
										pc.sendPackets(new S_Fishing(60));
										ancientGoldFishingRod = true;
										Ancientgold(pc);
									}
								}
								// TODO 成長的釣竿
							} else if (pc._fishingRod.getItemId() == 600229) {
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
									if (item.getChargeCount() <= 0) {
										L1ItemInstance newfishingRod = null;
										pc.getInventory().removeItem(item, 1);
										newfishingRod = pc.getInventory().storeItem(41293, 1);
										pc._fishingRod = newfishingRod;
										endFishing(pc);
									} else {
										item.setChargeCount(item.getChargeCount() - 1);
										pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
										pc.setFishingTime(System.currentTimeMillis() + 20000);
										pc.sendPackets(new S_Fishing(20));
										growthFishingRod = true;
										growthFishingRod(pc);
									}
								}
								// TODO 高級成長的釣竿
							} else if (pc._fishingRod.getItemId() == 4100293) {
								L1ItemInstance item = pc._fishingRod;
								if (item != null) {
									if (item.getChargeCount() <= 0) {
										L1ItemInstance newfishingRod = null;
										pc.getInventory().removeItem(item, 1);
										newfishingRod = pc.getInventory().storeItem(41293, 1);
										pc._fishingRod = newfishingRod;
										endFishing(pc);
									} else {
										item.setChargeCount(item.getChargeCount() - 1);
										pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
										pc.setFishingTime(System.currentTimeMillis() + 20000);
										pc.sendPackets(new S_Fishing(20));
										advancedGrowthFishingRod = true;
										advancedGrowthFishingRod(pc);
									}
								}
								// TODO 一般釣竿
							} else if (pc._fishingRod.getItemId() == 41293) {
								pc.setFishingTime(System.currentTimeMillis() + 240000);
								pc.sendPackets(new S_Fishing(240));
								highElasticFishingRod(pc);
							}
						} else {
							// 沒有餌料所以結束處理區間.
							endFishing(pc);
						}
					}
				}
			}
		}
	}

	private void installSilverFishingRodWithReel(L1PcInstance pc) {

	}

	private void installHighElasticityFishingRodWithReel(L1PcInstance pc) {

	}

	public void endFishing(L1PcInstance pc) {
		//new Throwable().printStackTrace();// TODO 錯誤輸出例外測試用
		pc.setFishingTime(0);
		pc.setFishingReady(false);
		pc.setFishing(false);
		pc._fishingRod = null;
		boolean growthFishing = false;
		boolean advancedGrowthFishing = false;
		if (growthFishing) {
			growthFishing = false;
		} else if (advancedGrowthFishing) {
			advancedGrowthFishing = false;
		} else if (ancientSilverFishing) {
			ancientSilverFishing = false;
		} else if (ancientGoldFishing) {
			ancientGoldFishing = false;
		}
		pc.sendPackets(S_InventoryIcon.icoEnd(L1SkillId.Fishing_etc));
		pc.sendPackets(new S_CharVisualUpdate(pc));
		Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
		removeMember(pc);
	}

	// TODO 古代的銀色釣竿
	private void ancientSilver(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 6000) { // 藍色貝利亞娜
			successFishing(pc, 41297, "$15565");
		} else if (chance < 8000) { // 貝利亞娜
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8040) { // 紅色貝利亞娜
			successFishing(pc, 49092, "紅色貝利亞娜");
		} else if (chance < 8070) { // 鹦鹉貝利亞娜
			successFishing(pc, 41298, "$15566");
		} else if (chance < 8350) { // 古代的銀色釣竿
			successFishing(pc, 87058, "古代的銀色釣竿");
		} else if (chance < 8351) { // 小銀色貝利亞娜
			successFishing(pc, 87056, "古代的銀色捲線器300次");
		} else if (chance < 8352) { // 小金色貝利亞娜
			successFishing(pc, 41300, "$17523");
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(1136)));
			// 釣魚失敗了。
		}
	}

	// TODO 古代的金色釣竿
	private void ancientGold(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 6000) { // 藍色貝利亞娜
			successFishing(pc, 41297, "$15565");
		} else if (chance < 8000) { // 貝利亞娜
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8020) { // 紅色貝利亞娜
			successFishing(pc, 49092, "紅色貝利亞娜");
		} else if (chance < 8050) { // 鹦鹉貝利亞娜
			successFishing(pc, 41298, "$15566");
		} else if (chance < 8350) { // 古代的金色釣竿
			successFishing(pc, 87059, "古代的金色釣竿");
		} else if (chance < 8351) { // 古代的銀色捲線器300次
			successFishing(pc, 87057, "古代的金色捲線器 300次");
		} else if (chance < 8352) { // 小金色貝利亞娜
			successFishing(pc, 41300, "$17523");
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(1136)));
			// 釣魚失敗了。
		}
	}

	private void growthFishingRod(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 6000) { // 貝利亞娜
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8000) { // 藍色貝利亞娜
			successFishing(pc, 41297, "$15565");
		} else if (chance < 8020) { // 鹦鹉貝利亞娜
			successFishing(pc, 41298, "$15566");
		} else if (chance < 8350) { // 純淨聖水
			successFishing(pc, 820018, "$20462");
		} else if (chance < 8360) { // 龍的寶箱
			successFishing(pc, 1000006, "$24608");
		} else if (chance < 8370) { // 愛因哈薩德的禮物
			successFishing(pc, 600230, "$20909");
		} else
			successFishing(pc, 41296, "$15564");
	}

	private void advancedGrowthFishingRod(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 6000) { // 貝利亞娜
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8000) { // 藍色貝利亞娜
			successFishing(pc, 41297, "$15565");
		} else if (chance < 8020) { // 鹦鹉貝利亞娜
			successFishing(pc, 41298, "$15566");
		} else if (chance < 8350) { // 純淨聖水
			successFishing(pc, 820018, "$20462");
		} else if (chance < 8360) { // 巨物貝利亞娜
			successFishing(pc, 49093, "$20462");
		} else if (chance < 8370) { // 龍的寶箱
			successFishing(pc, 1000006, "$24608");
		} else if (chance < 8390) { // 愛因哈薩德的禮物
			successFishing(pc, 600230, "$20909");
		} else {
			successFishing(pc, 41296, "$15564");
		}
	}

	private void bullfrogFishingRod(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 6000) { // 貝利亞娜
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8000) { // 藍色貝利亞娜
			successFishing(pc, 41297, "$15565");
		} else if (chance < 8020) { // 鹦鹉貝利亞娜
			successFishing(pc, 41298, "$15566");
		} else if (chance < 8350) { // 濕漉漉的釣魚包
			successFishing(pc, 41301, "$15815");
		} else if (chance < 8351) { // 小銀色貝利亞娜
			successFishing(pc, 41299, "$17521");
		} else if (chance < 8352) { // 小金色貝利亞娜
			successFishing(pc, 41300, "$17523");
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(1136)));
			// 釣魚失敗了。
		}
	}

	private void reelMountedHighElasticityFishingRod(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 6000) { // 貝利亞娜
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8000) { // 藍色貝利亞娜
			successFishing(pc, 41297, "$15565");
		} else if (chance < 8020) { // 鹦鹉貝利亞娜
			successFishing(pc, 41298, "$15566");
		} else if (chance < 8350) { // 濕漉漉的釣魚包
			successFishing(pc, 41301, "$15815");
		} else if (chance < 8351) { // 小銀色貝利亞娜
			successFishing(pc, 41299, "$17521");
		} else if (chance < 8352) { // 小金色貝利亞娜
			successFishing(pc, 41300, "$17523");
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(1136)));
			// 釣魚失敗了。
		}
	}

	private void reelMountedSilverFishingRod(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 4000) { // 貝利亞娜
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8000) { // 藍色貝利亞娜
			successFishing(pc, 41297, "$15565");
		} else if (chance < 8040) { // 鹦鹉貝利亞娜
			successFishing(pc, 41298, "$15566");
		} else if (chance < 8350) { // 濕漉漉的釣魚包
			successFishing(pc, 41301, "$15815");
		} else if (chance < 8352) { // 小銀色貝利亞娜
			successFishing(pc, 41299, "$17521");
		} else if (chance < 8353) { // 大銀色貝利亞娜
			successFishing(pc, 41303, "$17522");
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(1136)));
			// 釣魚失敗了。
		}
	}

	private void reelMountedGoldFishingRod(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1;
		if (chance < 3500) { // 藍色貝利亞娜
			successFishing(pc, 41297, "$15565");
		} else if (chance < 8000) { // 貝利亞娜
			successFishing(pc, 41296, "$15564");
		} else if (chance < 8050) { // 鹦鹉貝利亞娜
			successFishing(pc, 41298, "$15566");
		} else if (chance < 8350) { // 濕漉漉的釣魚包
			successFishing(pc, 41301, "$15815");
		} else if (chance < 8352) { // 小金色貝利亞娜
			successFishing(pc, 41300, "$17523");
		} else if (chance < 8354) { // 大金色貝利亞娜
			successFishing(pc, 41304, "$17524");
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(1136)));
			// 釣魚失敗了。
		}
	}

	private void highElasticityFishingRod(L1PcInstance pc) {
		int chance = _random.nextInt(10000) + 1; // 100%
			// 貝里亞納
		if (chance < 4000) {
			successFishing(pc, 41296, "$15564");
			// 藍色貝里亞納
		} else if (chance < 8000) {
			successFishing(pc, 41297, "$15565");
			// 鸚鵡貝里亞納
		} else if (chance < 8010) {
			successFishing(pc, 41298, "$15566");
			// 潮濕的釣魚袋
		} else if (chance < 8350) {
			successFishing(pc, 41301, "$15815");
		} else {
			pc.sendPackets(String.valueOf(new S_ServerMessage(1136))); // 16%
			// 釣魚失敗了。
		}
		pc.sendPackets(String.valueOf(new S_ServerMessage(1147)));
	}

	private boolean check_weight(L1PcInstance pc) {
//		if (pc.getInventory().getSize() > (180 - 16)) {
		if (pc.getInventory().getSize() > (200 - 16)) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(263)));
			return false;
		}
		return true;
	}

	private void distribute_message(int owner_object_id, String message, int effect_id) {
        ServerBasePacket[] pcks = new ServerBasePacket[]{new ServerBasePacket(S_PacketBox.GREEN_MESSAGE, message),
                new ServerBasePacket(owner_object_id, effect_id),};
        L1World.getInstance().broadcast_map(5490, Arrays.toString(pcks));
	}

	private void distribute_message(int owner_object_id, int itemid) {
		switch (itemid) {
			case 41303:
				distribute_message(owner_object_id, "有人釣到了大銀色貝里亞納!", 13641);
				break;
			case 41304:
				distribute_message(owner_object_id, "有人釣到了大金色貝里亞納!", 13639);
				break;
			case 49094:
				distribute_message(owner_object_id, "有人釣到了古代銀色貝里亞納!", 13641);
				break;
			case 49095:
				distribute_message(owner_object_id, "有人釣到了古代金色貝里亞納!", 13639);
				break;
		}
	}

	private void calculate_exp(L1PcInstance pc, MJEFishingType f_type) {
		try {
			int level = Math.max(pc.getLevel(), 2);
			MJFishingExpInfo eInfo = MJFishingExpInfo.find_fishing_exp_info(f_type, level);
			if (eInfo == null)
				return;

			long need_exp = ExpTable.getNeedExpNextLevel(52);
			double exp = need_exp * eInfo.get_default_exp();
			int ain = pc.getAccount().getBlessOfAin();
			if (ain >= 10000) {
				double ain_effect = 1D;
				if (ain > 20000)
					ain_effect += 0.3D;
				if (pc.hasSkillEffect(L1SkillId.DRAGON_TOPAZ) && ain >= 20000)
					ain_effect += 0.8D;
				exp += (exp * ain_effect);
				pc.getAccount().addBlessOfAin(
						-(int) SC_REST_EXP_INFO_NOTI.calcDecreaseCharacterEinhasad(pc, exp * eInfo.get_ain_ration()),
						pc);
			}

//			if (pc.hasSkillEffect(L1SkillId.PC_CAFE)) {
			if (pc.isPcBuff()) {
				exp *= 1.1;
			}

			exp = exp * eInfo.get_addition_exp();
			exp = Math.max(1, exp);

			// System.out.println("處罰前的經驗值 : " + exp);
			/**
			 * 等級別處罰
			 */
			exp = exp - ((exp * (level * 0.01)) * 0.5);
			// System.out.println("處罰後的經驗值 : " + exp);

			if (pc.getLevel() >= GameServerSetting.getInstance().get_maxLevel()) {
				long maxexp = ExpTable.getExpByLevel(GameServerSetting.getInstance().get_maxLevel() + 1);
				if (pc.get_exp() + exp >= maxexp)
					return;
			}

			if ((exp + pc.get_exp()) > ExpTable.getExpByLevel((level + 1))) {
				exp = (ExpTable.getExpByLevel((level + 1)) - pc.get_exp());
			}
			pc.add_exp((long) exp);
			pc.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void successFishing(L1PcInstance pc, int itemid, String message) {
		if (!check_weight(pc))
			return;

		L1ItemInstance item = pc.getInventory().storeItem(itemid, 1);
		if (item != null) {
			pc.sendPackets(new S_ServerMessage(1185, message)); // 釣魚成功，釣到了%0%o.
			ServerBasePacket pck = new S_SkillSound(pc.getId(), 763);
			pc.sendPackets(pck, false);
			pc.broadcastPacket(pck);
		}
		distribute_message(pc.getId(), itemid);
		if (grownUpFishing) {
			calculate_exp(pc, MJEFishingType.GROWN_UP);
		} else if (highGrownUpFishing) {
			calculate_exp(pc, MJEFishingType.HIGH_GROWN_UP);
		} else if (ancientSilverFishing) {
			calculate_exp(pc, MJEFishingType.ANCIENT_SILVER);
		} else if (ancientGoldFishing) {
			calculate_exp(pc, MJEFishingType.ANCIENT_GOLD);
			calculate_exp(pc, MJEFishingType.ACIENT_GOLD);
		}
	}
}