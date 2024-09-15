package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SystemMessage extends ServerBasePacket {
	private static final String S_SYSTEM_MESSAGE = "[S] S_SystemMessage";
	// TODO 解釋當發送訊息時圖標同時顯示
	public S_SystemMessage(int type, int time) {
		super(8);
		writeC(Opcodes.S_MESSAGE);
		writeH(type); // 訊息編號
		writeC(0x01);
		writeH(time); // 時間
	}
	/**
	 * 在客戶端顯示不存在於數據中的原始訊息。
	 * 如果訊息中包含 nameid($xxx)，則使用已重載的另一個方法。
	 *
	 * @param msg - 要顯示的字符行
	 */
	public S_SystemMessage(String msg) {
		// TODO 可能已被棄用 需重新檢查
		super(4 + (msg.length() * 2));
		writeC(Opcodes.S_MESSAGE);
		writeC(0x09);
		writeS(msg);

		// TODO 如果上面的封包被棄用，用下面的替代
		/*super(8 + (msg.length() * 2));
		writeC(Opcodes.S_SAY_CODE);
		writeC(2);
		writeD(0);
		writeS(msg);*/
	}
	/**
	 * 在客戶端顯示不存在於數據中的原始訊息。
	 *
	 * @param msg - 要顯示的字符行
	 * @param nameid - 如果字符行中包含 nameid($xxx)，則為 true。
	 */
	public S_SystemMessage(String msg, boolean nameid) {
		super(8 + (msg.length() * 2));
		writeC(Opcodes.S_SAY_CODE);
		writeC(2);
		writeD(0);
		writeS(msg);
		// NPC 聊天封包需要解析 nameid，因此使用此方法
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_SYSTEM_MESSAGE;
	}
}
