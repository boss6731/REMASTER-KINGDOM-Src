package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

// TODO 20-01-13 之後未使用（已更改為 Protobuf）
//      SC_EXCHANGE_ITEM_LIST_NOTI

public class S_TradeAddItem extends ServerBasePacket {
	private static final String S_TRADE_ADD_ITEM = "[S] S_TradeAddItem";

//            ========================================================
//            [Server]  OPCODE :145   Size :11
//            ========================================================
//            0000: 91 00 05 00 24 34 00 03 00 07 00                   ....$4.....
//
//
//            ========================================================
//            [Server]  OPCODE :98   Size :5
//            ========================================================
//            0000: 62 8B 70 8F 09                                     b.p..
//

	public S_TradeAddItem(L1ItemInstance item, int count, int type) {
		writeC(Opcodes.S_ADD_XCHG);
		writeC(type); // 0:交易窗口上方 1:交易窗口下方
		writeH(item.getItem().getGfxId());
		writeS(item.getNumberedViewName(count));

		// 0:祝福 1:一般 2:詛咒 3:未鑑定
		if (!item.isIdentified()) { // 未鑑定
			//writeC(3);
			writeH(0x03);
		} else { // 交換已結束狀態
			byte[] status = null;
			int bless = item.getBless();
			writeC(bless);
			status = item.getStatusBytes();
			writeC(status.length);
			for (byte b : status) {
				writeC(b);
			}
		}
		writeH(0x07);
	}

	public S_TradeAddItem(L1ItemInstance item, String name, int count, int type) {
		writeC(Opcodes.S_ADD_XCHG);
		writeC(type); // 0:交易窗口上方 1:交易窗口下方
		writeH(item.getItem().getGfxId());
		writeS(name);

		// 0:祝福 1:一般 2:詛咒 3:未鑑定
		if (!item.isIdentified()) { // 未鑑定
			//writeC(3);
			writeH(0x03);
		} else { // 交換已結束狀態
			byte[] status = null;
			int bless = item.getBless();
			writeC(bless);
			status = item.getStatusBytes();
			writeC(status.length);
			for (byte b : status) {
				writeC(b);
			}
		}
		writeH(0x07);
	}

	@override
	public byte[] getContent() {
		return getBytes();
	}

	@override
	public String getType() {
		return S_TRADE_ADD_ITEM;
	}
}


