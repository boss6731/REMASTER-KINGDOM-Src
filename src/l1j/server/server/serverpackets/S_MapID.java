package l1j.server.server.serverpackets;

import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.MJRaidSystem.MJRaidSpace;
import l1j.server.server.Opcodes;

public class S_MapID extends ServerBasePacket {

	public S_MapID(int mapid, boolean isUnderwater) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(118);
		writeC(0);
		writeC(8);
		if (mapid > 6000 && mapid < 6499) {
			write4bit(1005);
		} // 安塔瑞斯
		else if (mapid > 6501 && mapid < 6999) {
			write4bit(1011);
		} // 巴風特
		else if (mapid > 1017 && mapid < 1023) {
			write4bit(1017);
		} else if (mapid > 9000 && mapid < 9099) {
			write4bit(9000);
		} // 瑪爾島
		else if (mapid > 2101 && mapid < 2151) {
			write4bit(2101);
		} // 冰女
		else if (mapid > 2151 && mapid < 2201) {
			write4bit(2151);
		} // 惡魔
		else if (mapid > 2699 && mapid < 2798) {
			write4bit(2699);
		} else if (mapid > 2600 && mapid < 2698) {
			write4bit(2600);
		}

		else if (mapid >= 7783 && mapid <= 7793) {
			write4bit(7783); // 克勞迪亞
		} else if (mapid >= 12152 && mapid <= 12252) {
			write4bit(12152); // 克勞迪亞攻城場
		} else if (mapid >= 12257 && mapid <= 12357) {
			write4bit(12257); // 克勞迪亞死亡騎士
		} else {
			// 團隊副本
			mapid = MJRaidSpace.getInstance().getIdenMap(mapid);

			/** 2016.11.26 MJ 應用中心 LFC **/
			mapid = MJInstanceSpace.getInstance().getIdenMap(mapid);
			/** 2016.11.26 MJ 應用中心 LFC **/
			write4bit(mapid);
		}
		writeC(0x10);
		writeC(0x0);
		writeC(0x18);
		writeC(isUnderwater ? 1 : 0);
		writeC(0x20);
		writeC(0);
		writeC(0x28);
		writeC(0x00);
		writeC(0x30);
		writeC(0x00);
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}


