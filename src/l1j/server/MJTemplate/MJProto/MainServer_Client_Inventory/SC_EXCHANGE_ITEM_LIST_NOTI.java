package l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_EXCHANGE_ITEM_LIST_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void do_exchange(L1PcInstance pc, L1ItemInstance item, int count, int type) {
		SC_EXCHANGE_ITEM_LIST_NOTI noti = newInstance();
		noti.set_type(eExchangeType.fromInt(type));
		ItemInfo info = ItemInfo.newTradeInstance(pc, item, count);
		noti.add_item_list(info);
		pc.sendPackets(noti, MJEProtoMessages.SC_EXCHANGE_ITEM_LIST_NOTI);
	}

	public static SC_EXCHANGE_ITEM_LIST_NOTI newInstance() {
		return new SC_EXCHANGE_ITEM_LIST_NOTI();
	}

	private SC_EXCHANGE_ITEM_LIST_NOTI.eExchangeType _type;
	private java.util.LinkedList<ItemInfo> _item_list;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EXCHANGE_ITEM_LIST_NOTI() {
	}

	public SC_EXCHANGE_ITEM_LIST_NOTI.eExchangeType get_type() {
		return _type;
	}

	public void set_type(SC_EXCHANGE_ITEM_LIST_NOTI.eExchangeType val) {
		_bit |= 0x1;
		_type = val;
	}

	public boolean has_type() {
		return (_bit & 0x1) == 0x1;
	}

	public java.util.LinkedList<ItemInfo> get_item_list() {
		return _item_list;
	}

	public void add_item_list(ItemInfo val) {
		if (!has_item_list()) {
			_item_list = new java.util.LinkedList<ItemInfo>();
			_bit |= 0x2;
		}
		_item_list.add(val);
	}

	public boolean has_item_list() {
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
		if (has_item_list()) {
			for (ItemInfo val : _item_list) {
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
		if (has_item_list()) {
			for (ItemInfo val : _item_list) {
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
		if (has_item_list()) {
			for (ItemInfo val : _item_list) {
				output.writeMessage(2, val);
			}
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			case 0x00000008: {
				set_type(SC_EXCHANGE_ITEM_LIST_NOTI.eExchangeType.fromInt(input.readEnum()));
				break;
			}
			case 0x00000012: {
				add_item_list((ItemInfo) input.readMessage(ItemInfo.newInstance()));
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
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
				((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
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
		return new SC_EXCHANGE_ITEM_LIST_NOTI();
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

	public enum eExchangeType {
		EXC_SELF(0), EXC_OPPOSITE(1),;
		private int value;

		eExchangeType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eExchangeType v) {
			return value == v.value;
		}

		public static eExchangeType fromInt(int i) {
			switch (i) {
			case 0:
				return EXC_SELF;
			case 1:
				return EXC_OPPOSITE;
			default:
				throw new IllegalArgumentException(String.format("無效的參數 arguments eExchangeType, %d", i));
			}
		}
	}
}
