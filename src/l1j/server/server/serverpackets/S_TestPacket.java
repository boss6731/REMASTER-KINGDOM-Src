//13 2f 02 08 00 10 00 84 e9
package l1j.server.server.serverpackets;

import java.util.StringTokenizer;

import l1j.server.server.Opcodes;

public class S_TestPacket extends ServerBasePacket {

	private static final String S_TestPack = "S_TestPack";

	public static final int a = 103;
	public static final int b = 103;
	public static final int 伺服器的問候語 = 103;

	public S_TestPacket(int type, int gfxid, int messageCode, String color) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) {
			case a:
				writeC(0x00);
				writeC(0x08);
				writeBit(gfxid * 2);
				writeC(0x10);
				writeBit(messageCode * 2);
				writeC(0x1a);
				writeC(0x03);
				StringTokenizer st = new StringTokenizer(color);
				while (st.hasMoreTokens()) {
					writeC(Integer.parseInt(st.nextToken(), 16));
				}
				writeC(0x20);
				writeC(0x14);
				writeH(0x00);
				break;
		}
	}

	public S_TestPacket(int type) {
		// 此構造函數可以初始化一些默認值，如果有需要的話。
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		// 根據需求，可以添加更多的初始化邏輯。

		/*writeC(0x9c);
		writeH(0x006e);
		
		writeC(0x08);
		writeC(0x01);
		
		writeC(0x10);
		writeH(0x0e98);
		
		writeC(0x18);
		writeC(0xff);
		writeC(0xff);
		writeC(0xff);
		writeC(0xff);
		writeC(0x0f);
		
		writeC(0x20);
		writeC(0x0a);
		
		writeC(0x28);
		writeH(0x4bb5);
		
		writeC(0x30);
		writeC(0x00);
		
		writeC(0x38);
		writeC(0x04);
		
		writeC(0x40);
		writeH(0x10ec);
		
		writeC(0x48);
		writeC(0x00);
		
		writeC(0x50);
		writeC(0x00);
		
		writeC(0x58);
		writeC(0x01);
		
		writeC(0x60);
		writeC(0x00);
		
		writeC(0x68);
		writeC(0x00);
		
		writeC(0x70);
		writeC(0x00);
		
		writeC(0x78);
		writeC(0x1b);
		
		writeC(0x80);
		writeC(0x01);
		writeC(0x00);
		writeC(0x85);
		writeC(0x72);*/
		String message = "9c 43 03 08 01 12 00";
		StringTokenizer st = new StringTokenizer(message.toString());
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}

		// 7c 1a 0a 07 0d 10 09 00
		/* writeC(0xd8);
		writeC(0xff);
		writeC(0x5f);
		writeC(0x00); */

		/* writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) {
		    case b:
		        writeC(0x00);
		        writeC(0x08);
		        String message = "00 08 " + "b8 62 " // b4 62 = 中央領主臉, 01 = 無圖像
		                + "10 " // ?
		                + "c6 49 " // 訊息代碼
		                + "1a 03 " // ?
		                + "00 ff ff " // 字體顏色 (參考HTML字體顏色表)
		                + "20 14";
		        StringTokenizer st = new StringTokenizer(message.toString());
		        while (st.hasMoreTokens()) {
		            writeC(Integer.parseInt(st.nextToken(), 16));
		        }
		        writeH(0x00);
		        break;
		}
*/

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_TestPack;
	}
}


