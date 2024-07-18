package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;


public class S_EinhasadClanBuff extends ServerBasePacket {
    private static final String _S_EinhasadClanBuff = "[S] S_EinhasadClanBuff";

	public S_EinhasadClanBuff(L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF); // 寫入擴展的 Protobuf 操作碼
		writeC(0xfb); // 寫入固定值 0xfb
		writeC(0x03); // 寫入固定值 0x03

		L1Clan clan = L1World.getInstance().getClan(pc.getClanid()); // 取得玩家所在的公會
		if (clan == null) { // 如果公會為空
			writeC(0xfb); // 寫入固定值 0xfb
			writeC(0x03); // 寫入固定值 0x03
			writeC(0x0a); // 寫入固定值 0x0a
			writeC(0x00); // 寫入固定值 0x00
			writeH(0x00); // 寫入固定值 0x00
			return; // 結束方法
		}


		if (clan.getBuffFirst() != 0) { // 如果公會的第一個 Buff 不為 0
			writeC(0x0a); // 寫入標誌
			writeC(0x05); // 寫入尺寸
			writeC(0x08); // 寫入分隔符
			writeBit(clan.getBuffFirst()); // 寫入第一個 Buff 的值
			writeC(0x10); // 寫入分隔符
			if (clan.getEinhasadBlessBuff() == 0) {
				writeC(0x01); // 寫入開/關狀態 1
			} else if (clan.getEinhasadBlessBuff() == clan.getBuffFirst()) {
				writeC(0x02); // 寫入開/關狀態 2
			} else {
				writeC(0x03); // 寫入開/關狀態 3
			}
		}

			// 對第二個 Buff 進行相同處理
		if (clan.getBuffSecond() != 0) {
			writeC(0x0a); // 寫入標誌
			writeC(0x05); // 寫入尺寸
			writeC(0x08); // 寫入分隔符
			writeBit(clan.getBuffSecond()); // 寫入第二個 Buff 的值
			writeC(0x10); // 寫入分隔符
			if (clan.getEinhasadBlessBuff() == 0) {
				writeC(0x01); // 寫入開/關狀態 1
			} else if (clan.getEinhasadBlessBuff() == clan.getBuffSecond()) {
				writeC(0x02); // 寫入開/關狀態 2
			} else {
				writeC(0x03); // 寫入開/關狀態 3
			}
		}

		// 對第三個 Buff 進行相同處理
		if (clan.getBuffThird() != 0) {
			writeC(0x0a); // 寫入標誌
			writeC(0x05); // 寫入尺寸
			writeC(0x08); // 寫入分隔符
			writeBit(clan.getBuffThird()); // 寫入第三個 Buff 的值
			writeC(0x10); // 寫入分隔符
			if (clan.getEinhasadBlessBuff() == 0) {
				writeC(0x01); // 寫入開/關狀態 1
			} else if (clan.getEinhasadBlessBuff() == clan.getBuffThird()) {
				writeC(0x02); // 寫入開/關狀態 2
			} else {
				writeC(0x03); // 寫入開/關狀態 3
			}
		}

		writeC(0x10); // 寫入分隔符
		writeC(0x01); // 寫入整體變更狀態，0 為關閉，1 為啟動

		writeH(0x00); // 寫入固定值 0x00
	}
    
    @Override
    public byte[] getContent() {
    	return getBytes();
    }

    @Override
    public String getType() {
        return _S_EinhasadClanBuff;
    }
}


