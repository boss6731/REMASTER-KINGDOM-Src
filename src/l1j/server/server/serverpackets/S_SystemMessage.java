package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_SystemMessage extends ServerBasePacket {

	private static final String S_SYSTEM_MESSAGE = "[S] S_SystemMessage";

	// 構造函數，接受消息類型和時間
	public S_SystemMessage(int type, int time) {
		super(8);
		writeC(Opcodes.S_MESSAGE);
		writeH(type); // 消息類型
		writeC(0x01);
		writeH(time); // 顯示時間
	}

	/**
	 * 顯示一個不包含數據的原始消息。
	 * 如果消息包含 nameid($xxx)，則使用另一個重載版本。
	 *
	 * @param msg - 要顯示的消息
	 */
	public S_SystemMessage(String msg) {
		super(4 + (msg.length() * 2));
		writeC(Opcodes.S_MESSAGE);
		writeC(0x09);
		writeS(msg);

		// 如果上面的封包被廢棄，則用下面的代替
		/*super(8 + (msg.length() * 2));
		writeC(Opcodes.S_SAY_CODE);
		writeC(2);
		writeD(0);
		writeS(msg);*/
	}

	/**
	 * 顯示一個不包含數據的原始消息。
	 * 如果消息包含 nameid($xxx)，則設置 nameid 為 true。
	 *
	 * @param msg - 要顯示的消息
	 * @param nameid - 如果消息中包含 nameid($xxx)，則設置為 true
	 */
	public S_SystemMessage(String msg, boolean nameid) {
		super(8 + (msg.length() * 2));
		writeC(Opcodes.S_SAY_CODE);
		writeC(2);
		writeD(0);
		writeS(msg);
		// 如果是 NPC 聊天封包，解析 nameid
	}

	@override
	public byte[] getContent() {
		return getBytes();
	}

	@override
	public String getType() {
		return S_SYSTEM_MESSAGE;
	}
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
