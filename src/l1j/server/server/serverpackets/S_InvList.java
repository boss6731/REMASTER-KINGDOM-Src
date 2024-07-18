package l1j.server.server.serverpackets;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

public class S_InvList extends ServerBasePacket {





	private static final String S_INV_LIST = "[S] S_InvList";

	/**
	 * 將多個物品添加到列表中。
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

			int bit = 0; // 初始化狀態位變數

			// 檢查物品是否可以交易
			if (!item.getItem().isTradable())
				bit += 2; // 不可交易

			// 檢查物品是否可以刪除
			if (item.getItem().isCantDelete())
				bit += 4; // 不可刪除

			// 檢查物品是否可安全強化
			if (item.getItem().get_safeenchant() < 0)
				bit += 8; // 不可強化

			// 檢查物品的祝福狀態
			if (item.getBless() >= 128)
				bit = 46; // 特殊祝福狀態

			// 檢查物品是否已鑑定
			if (item.isIdentified())
				bit += 1; // 已鑑定

			writeC(bit); // 寫入狀態位

			writeS(item.getViewName()); // 寫入物品顯示名稱

			// 如果物品未鑑定
			if (!item.isIdentified()) {
				writeC(0); // 未鑑定狀態，不需要發送狀態信息
			} else {
				byte[] status = item.getStatusBytes(); // 獲取物品狀態字節數組
				writeC(status.length); // 寫入狀態字節數組的長度
				for (byte b : status) {
					writeC(b); // 寫入每個狀態字節
				}
			}
			writeC(24); // 寫入固定值 24
			writeC(0); // 寫入固定值 0
			writeH(0); // 寫入固定值 0
			writeH(0); // 寫入固定值 0

			if (item.getItem().getType2() == 0) {
				writeC(0); // 如果物品類型是 0（其他物品），寫入 0
			} else {
				writeC(item.getEnchantLevel()); // 否則，寫入物品的強化等級
			}

			writeD(item.getId()); // 寫入物品 ID
			writeD(0); // 寫入固定值 0
			writeD(0); // 寫入固定值 0

			// 根據物品的屬性設置狀態
			writeC(item.getBless() >= 128 ? 3 : item.getItem().isTradable() ? 7 : 2);

			// 寫入固定值 0
			writeD(0);

			// 獲取並寫入物品的屬性強化位
			int attrbit = item.getAttrEnchantBit(item.getAttrEnchantLevel());
			writeC(attrbit);

			writeH(0); // 寫入固定值 0

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
				bit += 2; // 交換不可

			if (item.getItem().isCantDelete())
				bit += 4; // 刪除不可

			if (item.getItem().get_safeenchant() < 0)
				bit += 8; // 無法附魔

				// if(item.getItem().getWareHouse()>0&&!item.getItem().isTradable()) bit += 16;
				// // 可放入倉庫

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
				byte[] status = item.getStatusBytes();
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
			writeC(item.getBless() >= 128 ? 3 : item.getItem().isTradable() ? 7 : 2);// 1102臨近生產包修改
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


