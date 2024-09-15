package l1j.server.server.serverpackets;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

public class S_InvList extends ServerBasePacket {

	private static final String S_INV_LIST = "[S] S_InvList";

	/**
	 * 將多個物品整理後加入清單中。
	 */
	public S_InvList(List<L1ItemInstance> items) {
		super(items.size() * 128);
		writeC(Opcodes.S_ADD_INVENTORY_BATCH);
		writeC(items.size());
		byte[] status = null;
		for (L1ItemInstance item : items) {
			writeD(item.getId());
			writeH(item.getItem().getItemDescId());

			int type = item.getItem().getUseType();
			if (type < 0) {
				type = 0;
			}
			writeC(type);
			int count = item.getChargeCount();
			if (count < 0) {
				count = 0;
			}
			writeC(count);

			if (item.getItemId() == 4100135)
				writeH(5556);
			else
				writeH(item.get_gfxid());
			writeC(item.getBless());
			writeD(item.getCount());

			int bit = 0;
			if (!item.getItem().isTradable())
				bit += 2; // 無法交易
			if (item.getItem().isCantDelete())
				bit += 4; // 無法刪除
			if (item.getItem().get_safeenchant() < 0)
				bit += 8; // 無法附魔
// 			if(item.getItem().getWareHouse() > 0 && !item.getItem().isTradable()) bit += 16;
			// 可以放倉庫
			if (item.getBless() >= 128)
				bit = 46;
			if (item.isIdentified())
				bit += 1; // 已確認
			writeC(bit);
			writeS(item.getViewName());
			if (!item.isIdentified()) {
				// 未鑑定的情況下無需發送狀態
				writeC(0);
			} else {
				status = item.getStatusBytes();
				writeC(status.length);
				for (byte b : status) {
					writeC(b);
				}
			}
			writeC(24);
			writeC(0);
			writeH(0);
			writeH(0);
			if (item.getItem().getType2() == 0) {
				writeC(0);
			} else {
				writeC(item.getEnchantLevel());
			}
			writeD(item.getId());
			writeD(0);
			writeD(0);
			writeC(item.getBless() >= 128 ? 3 : item.getItem().isTradable() ? 7 : 2);// 1102製作封包修正附近
			writeD(0);
			int attrbit = item.getAttrEnchantBit(item.getAttrEnchantLevel());
			writeC(attrbit);
		}
		writeH(0);
	}

	public S_InvList(List<L1ItemInstance> items, List<L1ItemInstance> notsendList) {
		super(items.size() * 128);
		writeC(Opcodes.S_ADD_INVENTORY_BATCH);
		writeC(items.size() - notsendList.size());
		byte[] status = null;
		for (L1ItemInstance item : items) {
			if (notsendList.contains(item))
				continue;

			writeD(item.getId());
			writeH(item.getItem().getItemDescId());

			int type = item.getItem().getUseType();
			if (type < 0) {
				type = 0;
			}
			writeC(type);
			int count = item.getChargeCount();
			if (count < 0) {
				count = 0;
			}
			writeC(count);

			if (item.getItemId() == 4100135)
				writeH(5556);
			else
				writeH(item.get_gfxid());
			writeC(item.getBless());
			writeD(item.getCount());

			int bit = 0;
			if (!item.getItem().isTradable())
				bit += 2; // 無法交易
			if (item.getItem().isCantDelete())
				bit += 4; // 無法刪除
			if (item.getItem().get_safeenchant() < 0)
				bit += 8; // 無法附魔
// 			if (item.getItem().getWareHouse() > 0 && !item.getItem().isTradable()) bit += 16;
			// 可以放倉庫
			if (item.getBless() >= 128)
				bit = 46;
			if (item.isIdentified())
				bit += 1; // 已確認
			writeC(bit);
			writeS(item.getViewName());
			if (!item.isIdentified()) {
				// 未鑑定的情況下無需發送狀態
				writeC(0);
			} else {
				status = item.getStatusBytes();
				writeC(status.length);
				for (byte b : status) {
					writeC(b);
				}
			}
			writeC(24);
			writeC(0);
			writeH(0);
			writeH(0);
			if (item.getItem().getType2() == 0) {
				writeC(0);
			} else {
				writeC(item.getEnchantLevel());
			}
			writeD(item.getId());
			writeD(0);
			writeD(0);
			writeC(item.getBless() >= 128 ? 3 : item.getItem().isTradable() ? 7 : 2);// 1102製作封包修正附近
			writeD(0);
			int attrbit = item.getAttrEnchantBit(item.getAttrEnchantLevel());
			writeC(attrbit);
		}
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_INV_LIST;
	}
}
