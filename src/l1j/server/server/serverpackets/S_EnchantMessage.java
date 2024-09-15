package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.MJTemplate.MJString;
import l1j.server.server.Opcodes;

/**
 * TODO：
 * 使用這裡的訊息通知源時，將 iconid 標記為圖片。
 **/
public class S_EnchantMessage extends ServerBasePacket{
	private S_EnchantMessage(int size) {
		super(size);
		writeC(Opcodes.S_MESSAGE_CODE);
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
		return newMessage(4444, enchant, iconId, message + " (這是)");
	}

	public static S_EnchantMessage newSilverMessage(int enchant, int iconId, String message) {
		return newMessage(4445, enchant, iconId, message + " (這是)");
	}

	public static S_EnchantMessage newDollMessage(int enchant, int iconId, String message) {
		return newMessage(4433, enchant, iconId, message + " (這是)");
	}
	
	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}

}
