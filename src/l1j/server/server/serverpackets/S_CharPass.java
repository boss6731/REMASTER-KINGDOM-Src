package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CharPass extends ServerBasePacket {
	public static final int _進入角色選擇界面 = 0x40;
	public static final int _進入角色選擇界面2 = 0x0a;
	public static final int _進入角色選擇界面3 = 0x16;
	public static final int _創建密碼界面 = 0x17;
	public static final int _密碼創建完成界面 = 0x11;
	public static final int _輸入密碼界面 = 0x14;
	public static final int _密碼輸入錯誤 = 0x15;
	public static final int _密碼更改回應 = 0x13;
	public static final int _密碼認證完成 = 0x3f;

	public static S_CharPass do_fail_password(int failure_count, int maximum_count) {
		S_CharPass packet = new S_CharPass(); // 創建一個新的 S_CharPass 封包實例
		packet.writeC(_密碼輸入錯誤); // 寫入操作碼，表示密碼輸入錯誤
		packet.writeD(0xa5); // 寫入固定值 0xa5
		packet.writeH(failure_count); // 寫入失敗次數
		packet.writeH(maximum_count); // 寫入最大允許失敗次數
		packet.writeD(0x00); // 寫入保留值 0x00
		return packet; // 返回封包
	}

	public S_CharPass(){
		writeC(Opcodes.S_VOICE_CHAT);
	}

	public S_CharPass(int val) {
		writeC(Opcodes.S_VOICE_CHAT); // 寫入操作碼，表示語音聊天（105）
		writeC(val); // 寫入傳入的值
		switch (val) {
			case _密碼輸入錯誤:
				writeD(0xa5); // 寫入固定值 0xa5
				writeH(0x02); // 寫入失敗次數
				writeH(0x05); // 寫入最大允許失敗次數
				writeD(0); // 寫入保留值 0
				break;
			case _密碼創建完成界面:
				writeD(0); // 寫入保留值 0
				break;
			case _進入角色選擇界面2:
				writeD(2); // 寫入值 2
				break;
			case _進入角色選擇界面3:
				writeD(170); // 寫入值 170
				writeD(0); // 寫入保留值 0
				writeD(0); // 寫入保留值 0
				writeH(0); // 寫入保留值 0
				writeC(1); // 寫入值 1
				writeC(0); // 寫入值 0
				break;
			default:
				break;
		}
	}



	public S_CharPass(int val, boolean ck) {
		writeC(Opcodes.S_VOICE_CHAT); // 寫入操作碼，表示語音聊天（105）
		writeC(val); // 寫入傳入的值 val
		switch (val) {
			case _密碼更改回應:
				// fe 13 00 00 00 00 00 00 05 00 00 00 00 00 ..............
				if (ck) {
					writeD(0); // 寫入 0
					writeH(0); // 寫入 0
					writeH(0x05); // 寫入 0x05
					writeD(0); // 寫入 0
				} else {
					writeD(0xa5); // 寫入 0xa5
					writeH(0x01); // 寫入 0x01
					writeH(0x05); // 寫入 0x05
					writeD(0); // 寫入 0
				}
				break;
			case _進入角色選擇界面3:
				if (ck) {
					writeD(0); // 寫入 0
					writeH(0); // 寫入 0
					writeD(0x05); // 寫入 0x05
					writeD(0); // 寫入 0
					writeH(0x01); // 寫入 0x01
				} else {
					writeD(170); // 寫入 170
					writeD(0); // 寫入 0
					writeD(0); // 寫入 0
					writeH(0); // 寫入 0
					writeH(1); // 寫入 1
				}
				break;
			default:
				break;
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return "[S] S_Test";


	}

	@SuppressWarnings("unused")
	private static final String _S__19_Test = "[S] S_Test";
}

