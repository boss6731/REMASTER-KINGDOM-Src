package l1j.server.server.model.item.function;

import l1j.server.server.ActionCodes;
import l1j.server.server.Controller.FishingTimeController;
import l1j.server.server.datatables.FishingZoneTable;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Fishing;
import l1j.server.server.serverpackets.S_FishingTime;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

@SuppressWarnings("serial")
public class Fishing extends L1ItemInstance {
	public static void clickItem(L1PcInstance pc, L1ItemInstance item, int fishX, int fishY) {
		if (pc.isFishing()) {
			pc.sendPackets(new S_SystemMessage("釣魚: 進行中"), true);
		} else
			startFishing(pc, item, fishX, fishY);
	}

	private static void startFishing(L1PcInstance pc, L1ItemInstance item, int fishX, int fishY) {
		int itemId = item.getItemId();
		int chargeCount = item.getChargeCount();
		if (pc.getMapId() != 5490 || fishX <= 32704 || fishX >= 32831 || fishY <= 32768 || fishY >= 32895) {
			// 無法在此投擲釣竿.
			pc.sendPackets(new S_ServerMessage(1138));
			return;
		}

		if ((itemId == 41294 || itemId == 41305 || itemId == 41306 || itemId == 600229 || itemId == 9991
				|| itemId == 87058 || itemId == 87059 || itemId == 4100293) && chargeCount <= 0) {
			return;
		}

		if (pc.getInventory().getWeight100() > 82) { // 超重
			pc.sendPackets(new S_SystemMessage("負重過高，無法釣魚。"));
			return;
		}
//		if (pc.getInventory().getSize() >= 180) {
		if (pc.getInventory().getSize() >= 200) {
			pc.sendPackets(new S_ServerMessage(263));
			return;
		}

		if (isFishingeZone(fishX, fishY, pc.getMapId())) {
			L1ItemInstance useItem = pc.getInventory().getItem(item.getId());
			if (useItem != null) {
				pc._fishingRod = useItem;
			} else {
				pc.sendPackets(new S_ServerMessage(1137));
				return;
			}
			if (pc._fishingRod.getItemId() == 600229 || pc._fishingRod.getItemId() == 87058
					|| pc._fishingRod.getItemId() == 87059 || pc._fishingRod.getItemId() == 4100293
					|| pc.getInventory().consumeItem(41295, 1)) {
				pc._fishingX = fishX;
				pc._fishingY = fishY;
				pc.sendPackets(new S_Fishing(pc.getId(), ActionCodes.ACTION_Fishing, fishX, fishY));
				pc.broadcastPacket(new S_Fishing(pc.getId(), ActionCodes.ACTION_Fishing, fishX, fishY));
				pc.setFishing(true);
				boolean ck = false;
				int Time = 0;
				if (pc._fishingRod.getItemId() == 600229) { // 成長釣竿
					ck = true;
					Time = 30;
					item.setChargeCount(item.getChargeCount() - 1);
					pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
					pc.setFishingTime(System.currentTimeMillis() + 30000);
				} else if (pc._fishingRod.getItemId() == 4100293) {
					ck = true;
					Time = 30;
					item.setChargeCount(item.getChargeCount() - 1);
					pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
					pc.setFishingTime(System.currentTimeMillis() + 30000);
				} else if (pc._fishingRod.getItemId() == 87058) { // 古代銀釣竿
					ck = true;
					Time = 90;
					item.setChargeCount(item.getChargeCount() - 1);
					pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
					pc.setFishingTime(System.currentTimeMillis() + 90000);
				} else if (pc._fishingRod.getItemId() == 87059) { // 古代金釣竿
					ck = true;
					Time = 90;
					item.setChargeCount(item.getChargeCount() - 1);
					pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
					pc.setFishingTime(System.currentTimeMillis() + 90000);
				} else if (pc._fishingRod.getItemId() == 41293) { // 只是釣竿
					Time = 240;
					pc.setFishingTime(System.currentTimeMillis() + 240000);
					pc.sendPackets(new S_FishingTime(S_FishingTime.FISH_WINDOW, 1, ck, 240), true);
				} else {
					Time = 80;
					item.setChargeCount(item.getChargeCount() - 1);
					pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
					pc.setFishingTime(System.currentTimeMillis() + 80000);
				}
				FishingTimeController.getInstance().addMember(pc);
				pc.sendPackets(S_InventoryIcon.iconNewUnLimit(L1SkillId.Fishing_etc, 4497, true));
				pc.sendPackets(new S_FishingTime(S_FishingTime.FISH_WINDOW, 1, ck, Time), true);
			} else {
				// 需要餌才能釣魚.
				pc.sendPackets(new S_ServerMessage(1137), true);
			}
		} else {
			// 無法在此處投擲釣竿.
			pc.sendPackets(new S_ServerMessage(1138), true);
		}
	}

	public static boolean isFishingeZone(int locX, int locY, int mapId) {
		String key = new StringBuilder().append(mapId).append(locX).append(locY).toString();
		if (FishingZoneTable.getInstance().isLockey(key)) {
			return true;
		}
		return false;
	}

}
