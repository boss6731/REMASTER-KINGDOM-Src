package l1j.server.MJTemplate.MJProto.Mainserver_Client_Equip;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_EXTEND_SLOT_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static final int OPEN_SLOT_LRING = 0x04; // 左邊戒指. 4
	public static final int OPEN_SLOT_RRING = 0x08; // 右邊戒指. 8
	public static final int OPEN_SLOT_EARRING = 0x10; // 右邊耳環. 16
	public static final int OPEN_SLOT_SHOULD = 0x40; // 肩甲. 64
	public static final int OPEN_SLOT_BADGE = 0x80; // 徽章. 128
	public static final int OPEN_SLOT_LRING95 = 0x100; // 95級左邊戒指. 256
	public static final int OPEN_SLOT_RRING100 = 0x200; // 100級右邊戒指. 512
	public static final int OPEN_SLOT_LEARRING101 = 0x400; // 101級左邊耳環. 1024
	public static final int OPEN_SLOT_REARRING103 = 0x800; // 103級右邊耳環. 2048

	public static void slot_info_send(L1PcInstance pc, int loc, boolean success) {
		SC_EXTEND_SLOT_INFO noti = SC_EXTEND_SLOT_INFO.newInstance();
		slotInfoT info = slotInfoT.newInstance();
		noti.set_slot_kind(SC_EXTEND_SLOT_INFO.eSLOT_KIND.fromInt(1));
		info.set_slot_location(loc);
		info.set_is_open(success);
		info.set_slot_remaintime(0);
		noti.add_extend_slot_info(info);
		pc.sendPackets(noti, MJEProtoMessages.SC_EXTEND_SLOT_INFO, true);
	}

	public static void slot_info_send(L1PcInstance pc) {
		SC_EXTEND_SLOT_INFO noti = SC_EXTEND_SLOT_INFO.newInstance();
		noti.set_slot_kind(SC_EXTEND_SLOT_INFO.eSLOT_KIND.fromInt(1));

		if (pc.getQuest().isEnd(L1Quest.QUEST_RING_LEFT_SLOT60)) {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_LRING);
			info.set_is_open(true);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		} else {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_LRING);
			info.set_is_open(false);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		}

		if (pc.getQuest().isEnd(L1Quest.QUEST_RING_RIGHT_SLOT60)) {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_RRING);
			info.set_is_open(true);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		} else {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_RRING);
			info.set_is_open(false);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		}

		if (pc.getQuest().isEnd(L1Quest.QUEST_EARRING_SLOT60)) {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_EARRING);
			info.set_is_open(true);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		} else {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_EARRING);
			info.set_is_open(false);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		}

		if (pc.getQuest().isEnd(L1Quest.QUEST_SLOT_SHOULD)) {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_SHOULD);
			info.set_is_open(true);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		} else {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_SHOULD);
			info.set_is_open(false);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		}
		if (pc.getQuest().isEnd(L1Quest.QUEST_SLOT_BADGE)) {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_BADGE);
			info.set_is_open(true);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		} else {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_BADGE);
			info.set_is_open(false);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		}
		if (pc.getQuest().isEnd(L1Quest.QUEST_RING_LEFT_SLOT95)) {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_LRING95);
			info.set_is_open(true);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		} else {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_LRING95);
			info.set_is_open(false);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		}
		if (pc.getQuest().isEnd(L1Quest.QUEST_RING_RIGHT_SLOT100)) {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_RRING100);
			info.set_is_open(true);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		} else {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_RRING100);
			info.set_is_open(false);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		}
		if (pc.getQuest().isEnd(L1Quest.QUEST_EARRING_LEFT_SLOT101)) {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_LEARRING101);
			info.set_is_open(true);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		} else {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_LEARRING101);
			info.set_is_open(false);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		}
		if (pc.getQuest().isEnd(L1Quest.QUEST_EARRING_RIGHT_SLOT103)) {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_REARRING103);
			info.set_is_open(true);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		} else {
			slotInfoT info = slotInfoT.newInstance();
			info.set_slot_location(OPEN_SLOT_REARRING103);
			info.set_is_open(false);
			info.set_slot_remaintime(0);
			noti.add_extend_slot_info(info);
		}
		pc.sendPackets(noti, MJEProtoMessages.SC_EXTEND_SLOT_INFO, true);
	}

	public static SC_EXTEND_SLOT_INFO newInstance() {
		return new SC_EXTEND_SLOT_INFO();
	}

	private SC_EXTEND_SLOT_INFO.eSLOT_KIND _slot_kind;
	private java.util.LinkedList<SC_EXTEND_SLOT_INFO.slotInfoT> _extend_slot_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EXTEND_SLOT_INFO() {
	}

	public SC_EXTEND_SLOT_INFO.eSLOT_KIND get_slot_kind() {
		return _slot_kind;
	}

	public void set_slot_kind(SC_EXTEND_SLOT_INFO.eSLOT_KIND val) {
		_bit |= 0x1;
		_slot_kind = val;
	}

	public boolean has_slot_kind() {
		return (_bit & 0x1) == 0x1;
	}

	public java.util.LinkedList<SC_EXTEND_SLOT_INFO.slotInfoT> get_extend_slot_info() {
		return _extend_slot_info;
	}

	public void add_extend_slot_info(SC_EXTEND_SLOT_INFO.slotInfoT val) {
		if (!has_extend_slot_info()) {
			_extend_slot_info = new java.util.LinkedList<SC_EXTEND_SLOT_INFO.slotInfoT>();
			_bit |= 0x2;
		}
		_extend_slot_info.add(val);
	}

	public boolean has_extend_slot_info() {
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
		if (has_slot_kind()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _slot_kind.toInt());
		}
		if (has_extend_slot_info()) {
			for (SC_EXTEND_SLOT_INFO.slotInfoT val : _extend_slot_info) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_slot_kind()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_extend_slot_info()) {
			for (SC_EXTEND_SLOT_INFO.slotInfoT val : _extend_slot_info) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_slot_kind()) {
			output.writeEnum(1, _slot_kind.toInt());
		}
		if (has_extend_slot_info()) {
			for (SC_EXTEND_SLOT_INFO.slotInfoT val : _extend_slot_info) {
				output.writeMessage(2, val);
			}
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
					set_slot_kind(SC_EXTEND_SLOT_INFO.eSLOT_KIND.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012: {
					add_extend_slot_info((SC_EXTEND_SLOT_INFO.slotInfoT) input
							.readMessage(SC_EXTEND_SLOT_INFO.slotInfoT.newInstance()));
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new SC_EXTEND_SLOT_INFO();
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

	public static class slotInfoT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static slotInfoT newInstance() {
			return new slotInfoT();
		}

		private int _slot_location;
		private boolean _is_open;
		private int _slot_remaintime;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private slotInfoT() {
		}

		public int get_slot_location() {
			return _slot_location;
		}

		public void set_slot_location(int val) {
			_bit |= 0x1;
			_slot_location = val;
		}

		public boolean has_slot_location() {
			return (_bit & 0x1) == 0x1;
		}

		public boolean get_is_open() {
			return _is_open;
		}

		public void set_is_open(boolean val) {
			_bit |= 0x2;
			_is_open = val;
		}

		public boolean has_is_open() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_slot_remaintime() {
			return _slot_remaintime;
		}

		public void set_slot_remaintime(int val) {
			_bit |= 0x4;
			_slot_remaintime = val;
		}

		public boolean has_slot_remaintime() {
			return (_bit & 0x4) == 0x4;
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
			if (has_slot_location()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _slot_location);
			}
			if (has_is_open()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _is_open);
			}
			if (has_slot_remaintime()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _slot_remaintime);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_slot_location()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_is_open()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_slot_location()) {
				output.wirteInt32(1, _slot_location);
			}
			if (has_is_open()) {
				output.writeBool(2, _is_open);
			}
			if (has_slot_remaintime()) {
				output.wirteInt32(3, _slot_remaintime);
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
						set_slot_location(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_is_open(input.readBool());
						break;
					}
					case 0x00000018: {
						set_slot_remaintime(input.readInt32());
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

				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new slotInfoT();
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

	public enum eSLOT_KIND {
		SLOT_KIND_NULL(0),
		SLOT_KIND_RING(1),
		SLOT_KIND_RUNE(2),
		SLOT_KIND_MAX(3),
		;

		private int value;

		eSLOT_KIND(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eSLOT_KIND v) {
			return value == v.value;
		}

		public static eSLOT_KIND fromInt(int i) {
			switch (i) {
				case 0:
					return SLOT_KIND_NULL;
				case 1:
					return SLOT_KIND_RING;
				case 2:
					return SLOT_KIND_RUNE;
				case 3:
					return SLOT_KIND_MAX;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eSLOT_KIND，%d", i));
			}
		}
	}
}
