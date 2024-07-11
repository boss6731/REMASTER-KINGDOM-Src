package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.util.ArrayList;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.datatables.BuddyTable;
import l1j.server.server.model.L1Buddy;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_BUDDY_LIST_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_BUDDY_LIST_NOTI newInstance() {
		return new SC_BUDDY_LIST_NOTI();
	}

	public static void send(L1PcInstance pc) {
		SC_BUDDY_LIST_NOTI noti = newInstance();

		ArrayList<L1Buddy> buddy_list = BuddyTable.getInstance().getBuddyList(pc.getId());

		if (buddy_list != null) {
			for (L1Buddy buddy : buddy_list) {
				if (buddy != null) {
					BUDDY_DATA buddy_data = BUDDY_DATA.newInstance(buddy);
					noti.add_buddys(buddy_data);
				}
			}
		}

		eBUDDY_LIST_TYPE result = eBUDDY_LIST_TYPE.eBUDDY_LIST_TYPE_BUDDIES;
		noti.set_type(result);

		pc.sendPackets(noti, MJEProtoMessages.SC_BUDDY_LIST_NOTI);
	}

	private SC_BUDDY_LIST_NOTI.eBUDDY_LIST_TYPE _type;
	private java.util.LinkedList<SC_BUDDY_LIST_NOTI.BUDDY_DATA> _buddys;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_BUDDY_LIST_NOTI() {
	}

	public SC_BUDDY_LIST_NOTI.eBUDDY_LIST_TYPE get_type() {
		return _type;
	}

	public void set_type(SC_BUDDY_LIST_NOTI.eBUDDY_LIST_TYPE val) {
		_bit |= 0x1;
		_type = val;
	}

	public boolean has_type() {
		return (_bit & 0x1) == 0x1;
	}

	public java.util.LinkedList<SC_BUDDY_LIST_NOTI.BUDDY_DATA> get_buddys() {
		return _buddys;
	}

	public void add_buddys(SC_BUDDY_LIST_NOTI.BUDDY_DATA val) {
		if (!has_buddys()) {
			_buddys = new java.util.LinkedList<SC_BUDDY_LIST_NOTI.BUDDY_DATA>();
			_bit |= 0x2;
		}
		_buddys.add(val);
	}

	public boolean has_buddys() {
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
		if (has_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _type.toInt());
		}
		if (has_buddys()) {
			for (SC_BUDDY_LIST_NOTI.BUDDY_DATA val : _buddys) {
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
		if (!has_type()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_buddys()) {
			for (SC_BUDDY_LIST_NOTI.BUDDY_DATA val : _buddys) {
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
		if (has_type()) {
			output.writeEnum(1, _type.toInt());
		}
		if (has_buddys()) {
			for (SC_BUDDY_LIST_NOTI.BUDDY_DATA val : _buddys) {
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
					set_type(SC_BUDDY_LIST_NOTI.eBUDDY_LIST_TYPE.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012: {
					add_buddys((SC_BUDDY_LIST_NOTI.BUDDY_DATA) input
							.readMessage(SC_BUDDY_LIST_NOTI.BUDDY_DATA.newInstance()));
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new SC_BUDDY_LIST_NOTI();
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

	public static class BUDDY_DATA implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static BUDDY_DATA newInstance() {
			return new BUDDY_DATA();
		}

		public static BUDDY_DATA newInstance(L1Buddy buddy) {
			BUDDY_DATA noti = new BUDDY_DATA();

			L1PcInstance target = L1PcInstance.load(buddy.getCharName());

			noti.set_name(target.getName());
			noti.set_class(target.getType());
			noti.set_memo(buddy.getMemo() != null && buddy.getMemo().length() > 1 ? buddy.getMemo() : "");
			noti.set_online(target.getOnlineStatus() > 0 ? true : false);

			return noti;
		}

		private String _name;
		private boolean _online;
		private String _memo;
		private int _class;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private BUDDY_DATA() {
		}

		public String get_name() {
			return _name;
		}

		public void set_name(String val) {
			_bit |= 0x1;
			_name = val;
		}

		public boolean has_name() {
			return (_bit & 0x1) == 0x1;
		}

		public boolean get_online() {
			return _online;
		}

		public void set_online(boolean val) {
			_bit |= 0x2;
			_online = val;
		}

		public boolean has_online() {
			return (_bit & 0x2) == 0x2;
		}

		public String get_memo() {
			return _memo;
		}

		public void set_memo(String val) {
			_bit |= 0x4;
			_memo = val;
		}

		public boolean has_memo() {
			return (_bit & 0x4) == 0x4;
		}

		public int get_class() {
			return _class;
		}

		public void set_class(int val) {
			_bit |= 0x8;
			_class = val;
		}

		public boolean has_class() {
			return (_bit & 0x8) == 0x8;
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
			if (has_name()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _name);
			}
			if (has_online()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _online);
			}
			if (has_memo()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _memo);
			}
			if (has_class()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _class);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_name()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_online()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_memo()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_name()) {
				output.writeString(1, _name);
			}
			if (has_online()) {
				output.writeBool(2, _online);
			}
			if (has_memo()) {
				output.writeString(3, _memo);
			}
			if (has_class()) {
				output.wirteInt32(4, _class);
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
					case 0x0000000A: {
						set_name(input.readString());
						break;
					}
					case 0x00000010: {
						set_online(input.readBool());
						break;
					}
					case 0x0000001A: {
						set_memo(input.readString());
						break;
					}
					case 0x00000020: {
						set_class(input.readInt32());
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
				// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new BUDDY_DATA();
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

	public enum eBUDDY_LIST_TYPE {
		eBUDDY_LIST_TYPE_BUDDIES(1),;

		private int value;

		eBUDDY_LIST_TYPE(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eBUDDY_LIST_TYPE v) {
			return value == v.value;
		}

		public static eBUDDY_LIST_TYPE fromInt(int i) {
			switch (i) {
				case 1:
					return eBUDDY_LIST_TYPE_BUDDIES;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eBUDDY_LIST_TYPE，%d", i));
			}
		}
	}
}
