package l1j.server.server.serverpackets;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_SlotChange extends ServerBasePacket {
	private static final String S_SlotChange = "S_SlotChange";

	public static final int SLOT_CHANGE = 32;

	public S_SlotChange(int type, L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) {
		case SLOT_CHANGE:
			List<Integer> one = pc.getSlotItems(0);
			List<Integer> two = pc.getSlotItems(1);
			List<Integer> three = pc.getSlotItems(2);
			List<Integer> four = pc.getSlotItems(3);
			int slotsize = 4;
			int size = 6;
			writeC(0x03);
			writeC(0x08);
			writeBit(pc.getSlotNumber());
			for (int slotnum = 0; slotnum < slotsize; slotnum++) {
				int namesize = (pc.get_slot_color() != null && pc.get_slot_info(slotnum) != null) ? pc.get_slot_info(slotnum).get_Slotname().getBytes().length : 0;
				writeC(0x12);
				switch (slotnum) {
				case 0:
					writeBit(one.size() > 0 ? (size * one.size()) + 6 + namesize : 6 + namesize);
					writeC(0x08);
					writeC(slotnum);
					if (one.size() > 0) {
						for (int i = 0; i < one.size(); i++) {
							writeC(0x10);
							writeBit(one.get(i));
						}
					}
					break;
				case 1:
					writeBit(two.size() > 0 ? (size * two.size()) + 6 + namesize : 6 + namesize);
					writeC(0x08);
					writeC(slotnum);
					if (two.size() > 0) {
						for (int i = 0; i < two.size(); i++) {
							writeC(0x10);
							writeBit(two.get(i));
						}
					}
					break;
				case 2:
					writeBit(three.size() > 0 ? (size * three.size()) + 6 + namesize : 6 + namesize);
					writeC(0x08);
					writeC(slotnum);
					if (three.size() > 0) {
						for (int i = 0; i < three.size(); i++) {
							writeC(0x10);
							writeBit(three.get(i));
						}
					}
					break;
				case 3:
					writeBit(four.size() > 0 ? (size * four.size()) + 6 + namesize : 6 + namesize);
					writeC(0x08);
					writeC(slotnum);
					if (four.size() > 0) {
						for (int i = 0; i < four.size(); i++) {
							writeC(0x10);
							writeBit(four.get(i));
						}
					}
					break;
				}
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

	public S_SlotChange(int type, int slot) { // 變更時調用
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