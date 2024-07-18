package l1j.server.server.serverpackets;

import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_IconMessage extends ServerBasePacket {
	private static final int SC_NOTIFICATION_MESSAGE_NOTI 		= 0x40; 
	private static final int SC_NOTIFICATION_STRINGKINDEX_NOTI	= 0x67;

	public static S_IconMessage getGmMessage(String msg) {
		return getMessage(msg, new MJSimpleRgb(0, 255, 0), 38, 60); // 運營者 $ 公告事項 28 / 37 / 38 / 68 / 69 / 71~76
	}

	public static S_IconMessage getMessage(String msg, MJSimpleRgb rgb, int surfNum, int duration) {
		S_IconMessage s = new S_IconMessage(SC_NOTIFICATION_MESSAGE_NOTI);
		s.writeBit(0x08); // surf: 畫面號碼
		s.writeBit(surfNum * 2);
		s.writeBit(0x12); // message: 訊息
		s.writeS2(msg);
		s.writeBit(0x1A); // color: 顏色
		try {
			s.writeBytes(rgb.get_bytes());
		} catch (Exception e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
		s.writeBit(0x20); // duration: 持續時間
		s.writeBit(duration);
		s.writeH(0x00);
		return s;
	}
	
	public static S_IconMessage getMessage(int scode, MJSimpleRgb rgb, int surfNum, int duration){
		S_IconMessage s = new S_IconMessage(SC_NOTIFICATION_STRINGKINDEX_NOTI);
		s.writeC(0x08);
		s.writeBit(surfNum * 2);
		s.writeBit(0x10);
		s.writeBit(scode * 2);
		s.writeC(0x1A);
		try {
			s.writeBytes(rgb.get_bytes());
		} catch (Exception e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
		s.writeC(0x20);
		s.writeBit(duration);
		s.writeH(0x00);
		return s;
	}
	
	private S_IconMessage(int i){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(i);
	}
	
	public S_IconMessage(L1PcInstance gm, String p){
		int SC_UPDATE_INVENTORY_NOTI = 0x024D;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SC_UPDATE_INVENTORY_NOTI);
		writeC(0x0A);
		L1ItemInstance item = gm.getInventory().findItemObjId(269226741);
		item.setAttrEnchantLevel(3);
		byte[] data = item.serialize();
		writeBit(data.length);
		writeByte(data);
		writeH(0x00);
	}

	public S_IconMessage(boolean b) {
		int SC_BLOODPLEDGE_USER_INFO_NOTI = 0x219; // 血盟用戶信息通知
		writeC(Opcodes.S_EXTENDED_PROTOBUF); // 寫入擴展的 Protobuf 操作碼
		writeH(SC_BLOODPLEDGE_USER_INFO_NOTI); // 寫入血盟用戶信息通知碼

		try {
			byte[] bytes = "自由".getBytes("UTF-8"); // 將字符串 "프리" 轉換為 UTF-8 字節數組
			writeC(0x0A); // 寫入標記
			writeBit(bytes.length); // 寫入字節數組的長度
			writeBytes(bytes); // 寫入字節數組
			writeC(0x10); // 寫入標記
			writeC(10); // 寫入數值 10
		} catch (Exception e) {
			e.printStackTrace(); // 捕捉並打印異常
		}

		writeH(0x00); // 寫入 0
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}
}


