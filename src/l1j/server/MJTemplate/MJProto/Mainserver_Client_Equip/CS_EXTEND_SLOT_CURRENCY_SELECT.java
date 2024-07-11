package l1j.server.MJTemplate.MJProto.Mainserver_Client_Equip;

import l1j.server.server.model.L1Quest;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_EXTEND_SLOT_CURRENCY_SELECT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_EXTEND_SLOT_CURRENCY_SELECT newInstance() {
		return new CS_EXTEND_SLOT_CURRENCY_SELECT();
	}

	private int _slot_position;
	private boolean _isDiscount;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_EXTEND_SLOT_CURRENCY_SELECT() {
		set_isDiscount(false);
	}

	public int get_slot_position() {
		return _slot_position;
	}

	public void set_slot_position(int val) {
		_bit |= 0x1;
		_slot_position = val;
	}

	public boolean has_slot_position() {
		return (_bit & 0x1) == 0x1;
	}

	public boolean get_isDiscount() {
		return _isDiscount;
	}

	public void set_isDiscount(boolean val) {
		_bit |= 0x2;
		_isDiscount = val;
	}

	public boolean has_isDiscount() {
		return (_bit & 0x2) == 0x2;
	}

	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}

	@Override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}

	@Override
	public int getSerializedSize() {
		int size = 0;
		if (has_slot_position()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _slot_position);
		}
		if (has_isDiscount()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _isDiscount);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_slot_position()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_slot_position()) {
			output.wirteInt32(1, _slot_position);
		}
		if (has_isDiscount()) {
			output.writeBool(2, _isDiscount);
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
						message.toInt());
		try {
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				case 0x00000008: {
					set_slot_position(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_isDiscount(input.readBool());
					break;
				}
				default: {
					return this;
				}
			}
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}

			if (pc.getLevel() < 60) {
				pc.sendPackets("等級不足，無法開放插槽。");
				return this;
			}

			// System.out.println(get_slot_position());

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			int price = 0;
			boolean dicount = false;
			// 0 = 왼쪽 반지, 1 = 오른쪽 반지, 2 = 귀걸이, 4 = 견갑, 5 = 휘장
			switch (get_slot_position()) {
				case 0:
					if (!pc.getQuest().isEnd(L1Quest.QUEST_RING_LEFT_SLOT60)) {
						if (pc.getInventory().checkItem(3000400, 1)) {
							dicount = true;
						} else {
							if (pc.getInventory().checkItem(40308, 20000000)) {
								price = 20000000;
							} else {
								pc.sendPackets("金幣 不足。");
								return this;
							}
						}
					}
					break;
				case 1:
					if (!pc.getQuest().isEnd(L1Quest.QUEST_RING_RIGHT_SLOT60)
							&& pc.getQuest().isEnd(L1Quest.QUEST_RING_LEFT_SLOT60)) {
						if (pc.getInventory().checkItem(3000401, 1)) {
							dicount = true;
						} else {
							if (pc.getInventory().checkItem(40308, 20000000)) {
								price = 20000000;
							} else {
								pc.sendPackets("金幣 不足。");
								return this;
							}
						}
					} else {
						pc.sendPackets("請先開放左側戒指插槽後再使用。");
						return this;
					}
					break;
				case 2:
					if (!pc.getQuest().isEnd(L1Quest.QUEST_EARRING_SLOT60)) {
						if (pc.getInventory().checkItem(3000399, 1)) {
							dicount = true;
						} else {
							if (pc.getInventory().checkItem(40308, 2000000)) {
								price = 2000000;
							} else {
								pc.sendPackets("金幣 不足。");
								return this;
							}
						}
					}
					break;
				case 4:
					if (!pc.getQuest().isEnd(L1Quest.QUEST_SLOT_SHOULD)) {
						if (pc.getInventory().checkItem(3000402, 1)) {
							dicount = true;
						} else {
							if (pc.getInventory().checkItem(40308, 30000000)) {
								price = 30000000;
							} else {
								pc.sendPackets("金幣 不足。");
								return this;
							}
						}
					}
					break;
				case 5:
					if (!pc.getQuest().isEnd(L1Quest.QUEST_SLOT_BADGE)) {
						if (pc.getInventory().checkItem(3000403, 1)) {
							dicount = true;
						} else {
							if (pc.getInventory().checkItem(40308, 2000000)) {
								price = 2000000;
							} else {
								pc.sendPackets("金幣 不足。");
								return this;
							}
						}
					}
					break;
				case 6:
					if (!pc.getQuest().isEnd(L1Quest.QUEST_RING_LEFT_SLOT95)
							&& pc.getQuest().isEnd(L1Quest.QUEST_RING_RIGHT_SLOT60)) {
						if (pc.getInventory().checkItem(3000400, 1)) {
							dicount = true;
						} else {
							if (pc.getInventory().checkItem(40308, 50000000)) {
								price = 50000000;
							} else {
								pc.sendPackets("金幣 不足。");
								return this;
							}
						}
					} else {
						pc.sendPackets("請先開放右側戒指插槽後再使用。");
						return this;
					}
					break;
				case 7:
					if (!pc.getQuest().isEnd(L1Quest.QUEST_RING_RIGHT_SLOT100)
							&& pc.getQuest().isEnd(L1Quest.QUEST_RING_LEFT_SLOT95)) {
						if (pc.getInventory().checkItem(3000401, 1)) {
							dicount = true;
						} else {
							if (pc.getInventory().checkItem(40308, 70000000)) {
								price = 70000000;
							} else {
								pc.sendPackets("金幣 不足。");
								return this;
							}
						}
					} else {
						pc.sendPackets("請先開放95級戒指插槽後再使用。");
						return this;
					}
					break;
				case 8:
					if (!pc.getQuest().isEnd(L1Quest.QUEST_EARRING_LEFT_SLOT101)
							&& pc.getQuest().isEnd(L1Quest.QUEST_RING_RIGHT_SLOT100)) {
						if (pc.getInventory().checkItem(3000401, 1)) {
							dicount = true;
						} else {
							if (pc.getInventory().checkItem(40308, 100000000)) {
								price = 70000000;
							} else {
								pc.sendPackets("金幣 不足。");
								return this;
							}
						}
					} else {
						pc.sendPackets("請先開放100級戒指插槽後再使用。");
						return this;
					}
					break;

				case 9:
					if (!pc.getQuest().isEnd(L1Quest.QUEST_EARRING_RIGHT_SLOT103)
							&& pc.getQuest().isEnd(L1Quest.QUEST_EARRING_LEFT_SLOT101)) {
						if (pc.getInventory().checkItem(3000401, 1)) {
							dicount = true;
						} else {
							if (pc.getInventory().checkItem(40308, 100000000)) {
								price = 70000000;
							} else {
								pc.sendPackets("金幣 不足。");
								return this;
							}
						}
					} else {
						pc.sendPackets("請先解鎖100級戒指插槽後再使用。");
						return this;
					}
					break;
			}

			SC_EXTEND_SLOT_CURRENCY_NOTI.slot_currency_send(pc, price, get_slot_position(), dicount);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_EXTEND_SLOT_CURRENCY_SELECT();
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
