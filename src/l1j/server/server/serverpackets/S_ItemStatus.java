
package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

public class S_ItemStatus extends ServerBasePacket {





	private static final String S_ITEM_STATUS = "[S] S_ItemStatus";

	/**
	 * 更改物品的名稱、狀態、屬性和重量等顯示
	 */
	public S_ItemStatus(L1ItemInstance item) {
		writeC(Opcodes.S_CHANGE_ITEM_DESC_EX);
		writeD(item.getId());
		writeS(item.getViewName());
		writeD(item.getCount());
		if (!item.isIdentified()) {    // 未鑑定的情況下無需發送狀態
			writeC(0);
		} else {
			byte[] status = item.getStatusBytes();
			writeC(status.length);
			for (byte b : status) {
				writeC(b);
			}
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_ITEM_STATUS;
	}
}


