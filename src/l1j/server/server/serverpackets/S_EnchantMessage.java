package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.MJTemplate.MJString;
import l1j.server.server.Opcodes;

/**
 * TODO:
 * 使用這裡的消息通知源時，應使用 iconid 來顯示圖像。
 **/
public class S_EnchantMessage extends ServerBasePacket {


	private S_EnchantMessage(int size) {
		super(size); // 調用父類的構造函數並設置大小
		writeC(Opcodes.S_MESSAGE_CODE); // 寫入消息代碼的操作碼
	}


	private static S_EnchantMessage newMessage(int stringId, int enchant, int iconId, String message) {

		S_EnchantMessage m = new S_EnchantMessage(10 + message.length());
		m.writeH(stringId);
		m.writeC(0x01);
		m.writeC(0x2b);
		m.writeS(MJString.concat(String.valueOf(enchant), " ", message));
		m.writeH(iconId);
		m.writeH(0x00);
		return m;
	}


	public static S_EnchantMessage newBlueMessage(int enchant, int iconId, String message) {
		return newMessage(4444, enchant, iconId, message + "  已被"); // 新增藍色消息
	}


	public static S_EnchantMessage newSilverMessage(int enchant, int iconId, String message) {
		return newMessage(4445, enchant, iconId, message + "  已被"); // 新增銀色消息
	}


	public static S_EnchantMessage newDollMessage(int enchant, int iconId, String message) {
		return newMessage(4433, enchant, iconId, message + "  已被"); // 新增娃娃消息
	}

	@override
	public byte[] getContent() throws IOException {
		return getBytes(); // 取得封包內容的字節數組
	}

