package l1j.server.server.clientpackets;

import java.util.Calendar;

import l1j.server.MJCompanion.Instance.MJCompanionInstanceCache;
import l1j.server.server.GameClient;
import l1j.server.server.datatables.NoTradable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;

public class C_TradeAddItem extends ClientBasePacket {
	private static final String C_TRADE_ADD_ITEM = "[C] C_TradeAddItem";

	/** 날짜 , 시간 기록 **/
	Calendar rightNow = Calendar.getInstance();
	int day = rightNow.get(Calendar.DATE);
	int hour = rightNow.get(Calendar.HOUR);
	int min = rightNow.get(Calendar.MINUTE);
	int year = rightNow.get(Calendar.YEAR);
	int month = rightNow.get(Calendar.MONTH) + 1;
	String totime = "[" + year + ":" + month + ":" + day + ":" + hour + ":" + min + "]";

	public C_TradeAddItem(byte abyte0[], GameClient client) throws Exception {
		super(abyte0);

		int itemid = readD();
		int itemcount = readD();
		L1PcInstance pc = client.getActiveChar();
		if (pc == null)
			return;
		L1Trade trade = new L1Trade();
		L1ItemInstance item = pc.getInventory().getItem(itemid);
		if (item == null)
			return;

		/** 버그 방지 **/
		if (itemid != item.getId()) {
			return;
		}
		if (!item.isStackable() && itemcount != 1) {
			return;
		}
		if (itemcount <= 0 || item.getCount() <= 0) {
			return;
		}
		if (itemcount > item.getCount()) {
			itemcount = item.getCount();
		}
		if (itemcount > 2000000000) { // 防止複製漏洞
			System.out.println("防止複製漏洞");
			return;
		}
		//** 防止漏洞 **/
		if (item.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE || item.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
			if (pc.getLevel() >= 70 && item.getItemId() == L1ItemId.LOW_CHARACTER_TRADE) {
				pc.sendPackets(new S_ChatPacket(pc, "70級以上需使用高級角色交換憑證。"));
				return;
			} else if (pc.getLevel() < 70 && item.getItemId() == L1ItemId.HIGH_CHARACTER_TRADE) {
				pc.sendPackets(new S_ChatPacket(pc, "70級以下需使用低級角色交換憑證。"));
				return;
			}
		}

		// 交換不可物品資料庫連動 NoTradable
		int itemId = item.getItem().getItemId();
		if (!pc.isGm() && (NoTradable.getInstance().isNoTradable(itemId) || item.getEndTime() != null)) {
			pc.sendPackets(new S_SystemMessage("該物品無法進行交易。"));
			return;
		}

		if (!item.getItem().isTradable()) {
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); // \f1%0無法丟棄或交易給他人。
			return;
		}

		if (item.get_Carving() != 0) {
			pc.sendPackets(new S_SystemMessage("刻印的物品無法進行交易。"));
			return;
		}

		if (item.getBless() >= 128) {
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); // \f1%0無法丟棄或交易給他人。
			return;
		}
		if (item.isEquipped()) {
			pc.sendPackets(new S_ServerMessage(906)); //
			return;
		}

		if (!MJCompanionInstanceCache.is_companion_oblivion(item.getId())) {
			pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
			return;
		}

		Object[] petlist = pc.getPetList().values().toArray();
		L1PetInstance pet = null;
		for (Object petObject : petlist) {
			if (petObject instanceof L1PetInstance) {
				pet = (L1PetInstance) petObject;
				if (item.getId() == pet.getItemObjId()) {
					// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
					return;
				}
			}
		}

		L1DollInstance doll = pc.getMagicDoll();
		if (doll != null) {
			if (item.getId() == doll.getItemObjId()) {
				// \f1%0無法丟棄或交易給他人。
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
				return;
			}
		}

		L1PcInstance tradingPartner = (L1PcInstance) L1World.getInstance().findObject(pc.getTradeID());
		if (tradingPartner == null) {
			return;
		}
		if (pc.getTradeOk() || tradingPartner.getTradeOk()) {
			pc.sendPackets(new S_SystemMessage("無法添加：其中一方已按下完成鍵"));
			tradingPartner.sendPackets(new S_SystemMessage("無法添加：其中一方已按下完成鍵"));
			return;
		}
		if (tradingPartner.getInventory().checkAddItem(item, itemcount) != L1Inventory.OK) {
			tradingPartner.sendPackets(new S_ServerMessage(270));
			pc.sendPackets(new S_ServerMessage(271));
			return;
		}
		trade.TradeAddItem(pc, itemid, itemcount);
	}

	@Override
	public String getType() {
		return C_TRADE_ADD_ITEM;
	}
}
