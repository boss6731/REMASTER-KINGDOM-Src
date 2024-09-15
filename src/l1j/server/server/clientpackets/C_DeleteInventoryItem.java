package l1j.server.server.server.clientpackets;

import java.util.Calendar;

import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;

import l1j.server.server.server.datatables.ItemTable;
import l1j.server.server.server.datatables.ResolventTable1;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.server.monitor.Logger.ItemActionType;
import l1j.server.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

public class C_DeleteInventoryItem extends l1j.server.server.clientpackets.ClientBasePacket {
	/** 日期 , 時間記錄 **/
	Calendar rightNow = Calendar.getInstance();
	int day = rightNow.get(Calendar.DATE);
	int hour = rightNow.get(Calendar.HOUR);
	int min = rightNow.get(Calendar.MINUTE);
	int year = rightNow.get(Calendar.YEAR);
	int month = rightNow.get(Calendar.MONTH) + 1;
	String totime = "[" + year + ":" + month + ":" + day + ":" + hour + ":" + min + "]";
	private static final String C_DELETE_INVENTORY_ITEM = "[C] C_DeleteInventoryItem";

	public C_DeleteInventoryItem(byte[] decrypt, GameClient client) {
		super(decrypt);

		L1PcInstance pc = client.getActiveChar();
		if (pc == null)
			return;

		int length = readD();
		for (int i = 0; i < length; ++i) {
			int itemObjectId = readD();
			int count = readD();

			L1ItemInstance item = pc.getInventory().getItem(itemObjectId);

			// 嘗試刪除的物品在服務器上不存在的情況
			if (item == null) {
				return;
			}

			if (!pc.isGm() && item.getItem().isCantDelete()) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(125)));
				return;
			}

			if (!pc.isGm() && item.getBless() >= 128) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(210, item.getItem().getName()))); // 1%0無法丟棄或轉讓給他人。
				return;
			}
			if (!MJCompanionInstanceCache.is_companion_oblivion(item.getId())) {
				pc.sendPackets(String.valueOf(new S_ServerMessage(210, item.getItem().getName())));
				return;
			}

			Object[] petlist = pc.getPetList().values().toArray();
			L1PetInstance pet = null;
			for (Object petObject : petlist) {
				if (petObject instanceof L1PetInstance) {
					pet = (L1PetInstance) petObject;
					if (item.getId() == pet.getItemObjId()) {
						// 1%0無法丟棄或轉讓給他人。
						pc.sendPackets(String.valueOf(new S_ServerMessage(210, item.getItem().getName())));
						return;
					}
				}
			}

			L1DollInstance doll = pc.getMagicDoll();
			if (doll != null) {
				if (item.getId() == doll.getItemObjId()) {
					// 1%0無法丟棄或轉讓給他人。
					pc.sendPackets(String.valueOf(new S_ServerMessage(210, item.getItem().getName())));
					return;
				}
			}

			if (item.isEquipped()) {
				// 1無法删除的物品或裝備中的物品無法丟棄。
				pc.sendPackets(String.valueOf(new S_ServerMessage(125)));
				return;
			}

			int crystalCount = ResolventTable1.getInstance().getCrystalCount(item.getItem().getItemId());
			L1ItemInstance crystal = ItemTable.getInstance().createItem(40308);

			if (crystalCount != 0) {
				if (count != 0) {
					crystal.setCount(crystalCount * count);
					pc.getInventory().storeItem(crystal);
					pc.sendPackets(String.valueOf(new S_SystemMessage("\\aB" + item.getName() + "(" + count + ")個刪除: " + crystal.getName() + "(" + crystal.getCount() + ")金幣獲得。")));
				} else {
					crystal.setCount(crystalCount * item.getCount());
					pc.getInventory().storeItem(crystal);
					pc.sendPackets(String.valueOf(new S_SystemMessage("\\aB" + item.getName() + "(" + item.getCount() + ")個刪除: " + crystal.getName() + "(" + crystal.getCount() + ")金幣獲得。")));
				}
			}

			/** 文件日誌保存 **/
			LoggerInstance.getInstance().addItemAction(ItemActionType.Delete, pc, item, count);
			if (count == 0)
				count = item.getCount();
			pc.getInventory().removeItem(item, count);
			pc.getLight().turnOnOffLight();
		}
	}

	@Override
	public String getType() {
		return C_DELETE_INVENTORY_ITEM;
	}
}
