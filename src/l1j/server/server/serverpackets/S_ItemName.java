package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

public class S_ItemName extends ServerBasePacket {





	private static final String S_ITEM_NAME = "[S] S_ItemName";

	/**
	 * 更改物品的名稱。當裝備或強化狀態發生變化時發送。
	 */

	public S_ItemName(int object_id, String name) {
		writeC(Opcodes.S_CHANGE_ITEM_DESC);
		writeD(object_id);
		writeS(name);
	}

	public S_ItemName(L1ItemInstance item) {
		if (item == null) {
			return;
		}
		// 根據觀察，這個操作碼是用於更新物品名稱的（專用於裝備後或強化後？）
		// 之後即使繼續發送數據也會被忽略
		writeC(Opcodes.S_CHANGE_ITEM_DESC);
		writeD(item.getId());
		writeS(item.getViewName());
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_ITEM_NAME;
	}
}


