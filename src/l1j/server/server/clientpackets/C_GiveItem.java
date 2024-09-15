package l1j.server.server.server.clientpackets;

import java.util.Calendar;
import java.util.Random;

import MJShiftObject.Battle.MJShiftBattlePlayManager;
import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;

import l1j.server.server.server.datatables.PetTypeTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1PetType;

public class C_GiveItem extends l1j.server.server.clientpackets.ClientBasePacket {
	private static final String C_GIVE_ITEM = "[C] C_GiveItem";

	/** 記錄日期和時間 **/
	Calendar rightNow = Calendar.getInstance();
	int day = rightNow.get(Calendar.DATE);
	int hour = rightNow.get(Calendar.HOUR);
	int min = rightNow.get(Calendar.MINUTE);
	int year = rightNow.get(Calendar.YEAR);
	int month = rightNow.get(Calendar.MONTH) + 1;
	String totime = "[" + year + ":" + month + ":" + day + ":" + hour + ":" + min + "]";

	public C_GiveItem(byte decrypt[], GameClient client) {
		super(decrypt);
		int targetId = readD();
		// int x = readH();
		// int y = readH();
		readH();
		readH();
		int itemId = readD();
		int count = readD();

		L1PcInstance pc = client.getActiveChar();

		if (pc == null || pc.isGhost() || pc.isTwoLogin(pc)) {
			return;
		}

		if (MJShiftBattlePlayManager.is_shift_battle(pc))
			return;

		L1Object object = L1World.getInstance().findObject(targetId);
		if (object == null || !(object instanceof L1NpcInstance)) {
			return;
		}

		L1NpcInstance target = (L1NpcInstance) object;
		L1Inventory targetInv = target.getInventory();
		L1Inventory inv = pc.getInventory();
		L1ItemInstance item = inv.getItem(itemId);
		if (item == null)
			return;

		if (!isNpcItemReceivable(target.getNpcTemplate())) {
			if (!(item.getItem().getItemId() == 40499) || !(item.getItem().getItemId() == 40507)) {
				return;
			}
		}
		if (item.isEquipped()) {
			pc.sendPackets(141);
			return;
		}
		if (item.getBless() >= 128) {
			pc.sendPackets(141);
			return;
		}
		if (item.getItemId() == 22229 || item.getItemId() == 22230 || item.getItemId() == 22231
				|| item.getItemId() == 22215 || item.getItemId() == 22216 || item.getItemId() == 22217
				|| item.getItemId() == 22218 || item.getItemId() == 22219 || item.getItemId() == 22220
				|| item.getItemId() == 22221 || item.getItemId() == 22225 || item.getItemId() == 22222
				|| item.getItemId() == 22226 || item.getItemId() == 22223 || item.getItemId() == 22227
				|| item.getItemId() == 22224 || item.getItemId() == 22228 || item.getItemId() == 900195) {
			return;
		}

		if (itemId != item.getId()) {
			pc.sendPackets(new S_Disconnect());
			return;
		}
		if (!item.isStackable() && count != 1) {
			pc.sendPackets(new S_Disconnect());
			return;
		}

		if (item.getCount() <= 0 || count <= 0) {
			pc.sendPackets(new S_Disconnect());
			return;
		}

		if (count >= item.getCount()) {
			count = item.getCount();
		}

		if (item.get_Carving() != 0) {
			pc.sendPackets("刻印的物品無法轉讓。");
			return;
		}

		if (!item.getItem().isTradable() || item.getItemId() == 40308) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(210, item.getItem().getName()))); // 1%0無法丟棄或轉讓給他人。
			return;
		}
		if (!MJCompanionInstanceCache.is_companion_oblivion(item.getId())) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(210, item.getItem().getName())));
			return;
		}

		L1PetInstance pet = null;
		for (Object petObject : pc.getPetList().values()) {
			if (petObject instanceof L1PetInstance) {
				pet = (L1PetInstance) petObject;
				if (item.getId() == pet.getItemObjId()) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(210, item.getItem().getName())));
					return;
				}
			}
		}

		L1DollInstance doll = pc.getMagicDoll();
		if (doll != null) {
			if (item.getId() == doll.getItemObjId()) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(210, item.getItem().getName())));
				return;
			}
		}

		if (!pc.isGm() && targetInv.checkAddItem(item, count) != L1Inventory.OK) {
			pc.sendPackets(String.valueOf(new S_ServerMessage(942)));
			return;
		}

		L1PetType petType = PetTypeTable.getInstance().get(target.getNpcTemplate().get_npcId());
		if (petType != null) {
			if ((petType.getBaseNpcId() == 46046 || petType.getItemIdForTaming() != 0)
					&& item.getItem().isUseHighPet()) {
				return;
			}
		}
//		if (!pc.isGm())
		item.setGiveItem(true);
		item = inv.tradeItem(item, count, targetInv);
		target.onGetItem(item);
		target.getLight().turnOnOffLight();
		pc.getLight().turnOnOffLight();

		if (petType == null || target.isDead()) {
			return;
		}
		if (item.getItemId() == petType.getItemIdForTaming()
				&& (item.getItemId() == 490026 && target.getNpcTemplate().get_npcId() == 45711)
				|| (item.getItemId() == 490027 && target.getNpcTemplate().get_npcId() == 45313)) {
			Random _rnd = new Random();
			if (item.getItemId() >= 490024 && item.getItemId() <= 490027) {
				int value = _rnd.nextInt(100) + 1;
				value += item.getItemId() == 490026 || item.getItemId() == 490027 ? 20 : 0;
				if (value > 75)
					tamePet(pc, target);
			}
		}
		/** 老虎重製 **/

		if (item.getItemId() == petType.getItemIdForTaming()) {
			tamePet(pc, target);
		} else if (item.getItemId() == 40070 && petType.getItemIdForTaming() != 0) {
			evolvePet(pc, target, item.getItemId());// 進化之果
		} else if (item.getItemId() == 41310 && petType.canEvolve() && petType.getItemIdForTaming() == 0) {
			evolvePet(pc, target, item.getItemId());
		}
	}

	private final static String receivableImpls[] = new String[] { "L1Npc", // NPC
			"L1Monster",
			"L1Guardian",
			"L1Teleporter",
			"L1Guard" };

	private boolean isNpcItemReceivable(L1Npc npc) {
		for (String impl : receivableImpls) {
			if (npc.getImpl().equals(impl)) {
				return true;
			}
		}
		return false;
	}

	private void tamePet(L1PcInstance pc, L1NpcInstance target) {
		if (target instanceof L1PetInstance || target instanceof L1SummonInstance) {
			return;
		}

		int petcost = 0;
		Object[] petlist = pc.getPetList().values().toArray();
		for (Object pet : petlist) {
			petcost += ((L1NpcInstance) pet).getPetcost();
		}
		int charisma = pc.getAbility().getTotalCha();
		if (pc.isCrown()) { // 王族
			charisma += 6;
		} else if (pc.isElf()) { // 妖精
			charisma += 12;
		} else if (pc.isWizard()) { // 魔法師
			charisma += 6;
		} else if (pc.isDarkelf()) { // 黑暗妖精
			charisma += 6;
		} else if (pc.isDragonknight()) { // 龍騎士
			charisma += 6;
		} else if (pc.isBlackwizard()) { // 幻術師
			charisma += 6;
		}
		charisma -= petcost;

		L1PcInventory inv = pc.getInventory();
		String npcname = target.getNpcTemplate().get_name();
//		if (charisma >= 6 && inv.getSize() < 180) {
		if (charisma >= 6 && inv.getSize() < 200) {
			if (isTamePet(target)) {
				L1ItemInstance petamu = inv.storeItem(40314, 1);
				if (petamu != null) {
					new L1PetInstance(target, pc, petamu.getId());
					pc.sendPackets(String.valueOf(new S_ItemName(petamu)));
					pc.sendPackets(String.valueOf(new S_SystemMessage(npcname + "的項鍊獲得了。")));
				}
			} else {
				pc.sendPackets(String.valueOf(new S_ServerMessage(324)));
			}
		}
	}

	private void evolvePet(L1PcInstance pc, L1NpcInstance target, int itemId) {
		if (!(target instanceof L1PetInstance)) {
			return;
		}
		L1PcInventory inv = pc.getInventory();
		L1PetInstance pet = (L1PetInstance) target;
		L1ItemInstance petamu = inv.getItem(pet.getItemObjId());
		String npcname = target.getNpcTemplate().get_name();
		if (pet.getLevel() >= 30 && pc == pet.getMaster() && petamu != null) {
			L1ItemInstance highpetamu = inv.storeItem(40316, 1);
			if (highpetamu != null) {
				pet.evolvePet(highpetamu.getId());
				pc.sendPackets(String.valueOf(new S_ItemName(highpetamu)));
				inv.removeItem(petamu, 1);
				pc.sendPackets(String.valueOf(new S_SystemMessage(npcname + "的進化成功了。")));
			}
		} else {
			pc.sendPackets(String.valueOf(new S_SystemMessage(npcname + "的進化條件不滿足。")));
		}
	}

	private boolean isTamePet(L1NpcInstance npc) {
		boolean isSuccess = false;
		int npcId = npc.getNpcTemplate().get_npcId();
		if (npcId == 45313 || npcId == 45711) {
			isSuccess = true;
		} else {
			if (npc.getMaxHp() / 2 > npc.getCurrentHp()) {
				isSuccess = true;
			}
		}

		if (npcId == 45313 || npcId == 45044 || npcId == 45711) {
			if (npc.isResurrect()) {
				isSuccess = false;
			}
		}
		return isSuccess;
	}

	@Override
	public String getType() {
		return C_GIVE_ITEM;
	}
}
