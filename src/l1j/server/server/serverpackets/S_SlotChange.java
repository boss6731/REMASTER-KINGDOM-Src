package l1j.server.server.serverpackets;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_SlotChange extends ServerBasePacket {
	private static final String S_SlotChange = "S_SlotChange";

	public static final int SLOT_CHANGE = 32;

	public class S_SlotChange extends ServerBasePacket {

		// 構造函數：改變槽位，並傳入玩家對象
		public S_SlotChange(int type, L1PcInstance pc) {
			writeC(Opcodes.S_EXTENDED_PROTOBUF); // 寫入操作碼，表示擴展的Protobuf操作
			writeC(type); // 寫入變更類型

			switch (type) {
				case SLOT_CHANGE:
					// 獲取玩家的槽位項目列表
					List<Integer> one = pc.getSlotItems(0);
					List<Integer> two = pc.getSlotItems(1);
					List<Integer> three = pc.getSlotItems(2);
					List<Integer> four = pc.getSlotItems(3);

					int slotsize = 4; // 槽位數量
					int size = 6; // 每個項目的大小

					writeC(0x03);
					writeC(0x08);
					writeBit(pc.getSlotNumber()); // 寫入玩家當前的槽位編號

					// 遍歷每個槽位
					for (int slotnum = 0; slotnum < slotsize; slotnum++) {
						// 獲取槽位名稱的字節長度
						int namesize = (pc.get_slot_color() != null && pc.get_slot_info(slotnum) != null) ? pc.get_slot_info(slotnum).get_Slotname().getBytes().length : 0;

						writeC(0x12); // 寫入槽位信息標識

						switch (slotnum) {
							case 0:
								// 計算並寫入槽位項目總大小
								writeBit(one.size() > 0 ? (size * one.size()) + 6 + namesize : 6 + namesize);
								writeC(0x08); // 寫入槽位標識
								writeC(slotnum); // 寫入槽位編號

								if (one.size() > 0) {
									// 遍歷並寫入槽位中的每個項目
									for (int i = 0; i < one.size(); i++) {
										writeC(0x10); // 寫入項目標識
										writeBit(one.get(i)); // 寫入項目ID
									}
								}
								break;
							case 1:
								// 計算並寫入槽位項目總大小
								writeBit(two.size() > 0 ? (size * two.size()) + 6 + namesize : 6 + namesize);
								writeC(0x08); // 寫入槽位標識
								writeC(slotnum); // 寫入槽位編號

								if (two.size() > 0) {
									// 遍歷並寫入槽位中的每個項目
									for (int i = 0; i < two.size(); i++) {
										writeC(0x10); // 寫入項目標識
										writeBit(two.get(i)); // 寫入項目ID
									}
								}
								break;
							case 2:
								// 計算並寫入槽位項目總大小
								writeBit(three.size() > 0 ? (size * three.size()) + 6 + namesize : 6 + namesize);
								writeC(0x08); // 寫入槽位標識
								writeC(slotnum); // 寫入槽位編號

								if (three.size() > 0) {
									// 遍歷並寫入槽位中的每個項目
									for (int i = 0; i < three.size(); i++) {
										writeC(0x10); // 寫入項目標識
										writeBit(three.get(i)); // 寫入項目ID
									}
								}
								break;
							case 3:
								// 計算並寫入槽位項目總大小
								writeBit(four.size() > 0 ? (size * four.size()) + 6 + namesize : 6 + namesize);
								writeC(0x08); // 寫入槽位標識
								writeC(slotnum); // 寫入槽位編號

								if (four.size() > 0) {
									// 遍歷並寫入槽位中的每個項目
									for (int i = 0; i < four.size(); i++) {
										writeC(0x10); // 寫入項目標識
										writeBit(four.get(i)); // 寫入項目ID
									}
								}
								break;
						}

						// 檢查槽位顏色和信息
						if (pc.get_slot_color() != null && pc.get_slot_info(slotnum) != null) {
							writeC(0x1a);
							if (!pc.get_slot_info(slotnum).get_Slotname().equalsIgnoreCase("") && pc.get_slot_info(slotnum).get_Slotname() != null) {
								writeC(pc.get_slot_info(slotnum).get_Slotname().getBytes().length);
								writeByte(pc.get_slot_info(slotnum).get_Slotname().getBytes());
							} else {
								writeS("");
							}
							writeC(0x20);
							writeC(pc.get_slot_info(slotnum).get_Color());
						} else {
							writeC(0x1a);
							writeS("");
							writeC(0x20);
							writeC(0);
						}
					}

					writeC(0x18);
					writeC(0x02);
					writeC(0x20);
					writeC(0x46);
					writeH(0);
					break;
			}
		}

	public S_SlotChange(int type, int slot) { // 更改時載入
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) {
		case SLOT_CHANGE:
			writeC(0x03);
			writeC(0x08);
			writeBit(slot);
			break;
		}

		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return S_SlotChange;
	}
}


