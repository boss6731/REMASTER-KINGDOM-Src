package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

public class S_ItemName extends ServerBasePacket {

	private static final String S_ITEM_NAME = "[S] S_ItemName";

	/**
	 * ÌÚËÇÚªù¡Ù£öà¡£î¤íûİáûä?ûùßÒ÷¾Ü¨ûùãÁÛ¡áê¡£
	 */
	
	public S_ItemName(int object_id, String name){
		writeC(Opcodes.S_CHANGE_ITEM_DESC);
		writeD(object_id);
		writeS(name);
	}
	
	public S_ItemName(L1ItemInstance item) {
		if (item == null) {
			return;
		}
		// ğô jump ÕÎÊ×£¬îÏËÁ Opcode ŞÄûºãÀéÄåÚÌÚãæÚªù¡Ù£öà (ĞÁî¤íûİáûä?ûùı­ŞÅéÄ?)
		// ñıı­?ŞÅÍ©áÙÛ¡áêâ¦Ëàå¥üåù¬îïİ»ûìÕÔ
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
