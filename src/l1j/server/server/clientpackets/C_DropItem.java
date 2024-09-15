package l1j.server.server.server.clientpackets;

import java.util.Calendar;

import MJShiftObject.Battle.MJShiftBattlePlayManager;
import l1j.server.Config;
import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;

import l1j.server.server.server.datatables.ItemTable;
import l1j.server.server.server.datatables.NoDropItem;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.server.monitor.Logger.ItemActionType;
import l1j.server.server.server.monitor.LoggerInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

public class C_DropItem extends l1j.server.server.clientpackets.ClientBasePacket {

	private static final String C_DROP_ITEM = "[C] C_DropItem";
	/** 記錄日期和時間 **/
	Calendar rightNow = Calendar.getInstance();
	int day = rightNow.get(Calendar.DATE);
	int hour = rightNow.get(Calendar.HOUR);
	int min = rightNow.get(Calendar.MINUTE);
	int year = rightNow.get(Calendar.YEAR);
	int month = rightNow.get(Calendar.MONTH) + 1;
	String totime = "[" + year + ":" + month + ":" + day + ":" + hour + ":" + min + "]";

	public C_DropItem(byte[] decrypt, GameClient client) throws Exception {
		super(decrypt);
		int length = 1;
		length = readD();
		for (int i = 0; i < length; ++i) {
			int x = readH();
			int y = readH();
			int objectId = readD();
			int count = readD();

			L1PcInstance pc = client.getActiveChar();

			if (pc == null || pc.isGhost()) {
				return;
			}

			if (MJShiftBattlePlayManager.is_shift_battle(pc))
				return;

			if (pc.getOnlineStatus() != 1) {
				pc.sendPackets(new S_Disconnect());
				return;
			}

			L1ItemInstance item = pc.getInventory().getItem(objectId);
			if (item != null) {
				long nowtime = System.currentTimeMillis();
				if (item.getItemdelay3() >= nowtime) {
					return;
				}
				if (!pc.isGm() && !item.getItem().isTradable() || item.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE
						|| item.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
					// 1%0 無法丟棄或轉移給其他人。
					pc.sendPackets(String.valueOf(new S_ServerMessage(210, item.getItem().getName())));
					return;
				}

				/** 防止漏洞 **/
				int itemType = item.getItem().getType2();
				int itemId = item.getItem().getItemId();

				if ((itemType == 1 && count != 1) || (itemType == 2 && count != 1)) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (item.getCount() <= 0) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (!item.isStackable() && count != 1) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (item.getCount() < count || count <= 0 || count > 2000000000) {
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (count > item.getCount()) {
					count = item.getCount();
				}

				/** 防止丟棄外部化，從資料庫 nodropitem 表中添加列表 **/
				if (!pc.isGm() && NoDropItem.getInstance().isNoDropItem(itemId)) {
					String itemName = ItemTable.getInstance().findItemIdByName(itemId);
					pc.sendPackets(String.valueOf(new S_SystemMessage("[" + itemName + "] 無法丟棄此物品。")));
					return;
				}
				// 禁止丟棄附魔物品！
				if (!pc.isGm() && pc.getLevel() < Config.ServerAdSetting.ALTDROPLEVELLIMIT) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("等級 " + Config.ServerAdSetting.ALTDROPLEVELLIMIT + " 才能丟棄物品。")));
					return;
				}
				if (item.getId() >= 0 && (pc.getMapId() == 350 || pc.getMapId() == 340 || pc.getMapId() == 370
						|| pc.getMapId() == 800)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("在市場無法丟棄物品。")));
					return;
				}

				if (item.getId() >= 0 && (pc.getMapId() == 38 || pc.getMapId() == 34)) {
					pc.sendPackets(String.valueOf(new S_SystemMessage("在此區域無法丟棄物品。")));
					return;
				}

				/*if (item.getId() == 80500) {
				pc.sendPackets(new S_SystemMessage("訓練所鑰匙無法丟棄。"));
				return;
				}*/
				
				if (!pc.isGm() && item.getEndTime() != null) {
					pc.sendPackets(new S_SystemMessage("限時物品無法丟棄。"), true);
					return;
				}

				if (!pc.isGm() && item.get_Carving() != 0) {
					pc.sendPackets(new S_SystemMessage("刻印物品無法丟棄。"), true);
					return;
				}
				
				/*if (ItemMessageTable.getInstance().isItemMessage(item.getItemId())) {
				L1ItemMessage temp = ItemMessageTable.getInstance().getItemMessage(item.getItemId());
				if (temp != null) {
				if (temp.getType() == 1 && !pc.isGm()) {
				pc.sendPackets("" + item.getItem().getName() + " 無法放置在地面上。");
				return;
				}
				}
				}*/

				/*
				 * String[] NonDropItem = null; NonDropItem = Config.DROP_MENT_ITEM.split(",");
				 * 
				 * for (int j = 0; j < NonDropItem.length; j++) { int itemid = 0; itemid =
				 * Integer.parseInt(NonDropItem[j]); if (item.getItemId() == itemid) { if
				 * (!pc.isGm()) { pc.sendPackets("" + item.getItem().getName() +
				 * " 無法丟棄高價物品，但可以交換。");return;}]
				 */

				/** 防止漏洞 **/
				if (!pc.isGm() && item.getBless() >= 128) {
					pc.sendPackets(String.valueOf(new S_ServerMessage(210, item.getItem().getName()))); // 1%0은
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
						// 1%0 無法丟棄或轉讓給他人。
						pc.sendPackets(String.valueOf(new S_ServerMessage(210, item.getItem().getName())));
						return;
					}
				}

				if (item.isEquipped()) {
					// 1無法刪除的物品或裝備中的物品無法丟棄。
					pc.sendPackets(String.valueOf(new S_ServerMessage(125)));
					return;
				}
				if (x > pc.getX() + 1 || x < pc.getX() - 1 || y > pc.getY() + 1 || y < pc.getY() - 1) {
					return;
				}
				int delay_time = 2000;
				if (item != null) {
					if (item.isStackable()) {
						if (item.getItemdelay3() <= nowtime) {
							item.setItemdelay3(nowtime + delay_time);
						}
					}
				}
				//if (!pc.isGm())
				item.setGiveItem(true);
				pc.getInventory().tradeItem(item, count, L1World.getInstance().getInventory(x, y, pc.getMapId()));
				pc.getLight().turnOnOffLight();
				/** 文件日志保存 **/
				LoggerInstance.getInstance().addItemAction(ItemActionType.Drop, pc, item, count);
			}
		}
	}

	@Override
	public String getType() {
		return C_DROP_ITEM;
	}

	/*
	 * private boolean isTwoLogin(L1PcInstance c) { // 重複檢查更改 boolean bool = false;
	 * for (L1PcInstance target : L1World.getInstance().getAllPlayers()) { if
	 * (target.noPlayerCK || target.noPlayerck2) continue;
	 *
	 * if (target.getRobotAi() != null) continue;
	 *
	 * if (c.getId() != target.getId() && !target.isPrivateShop()) { if
	 * (c.getNetConnection().getAccountName().equalsIgnoreCase(target.
	 * getNetConnection().getAccountName())) { bool = true; break; } } } return
	 * bool; }
	 */
}
