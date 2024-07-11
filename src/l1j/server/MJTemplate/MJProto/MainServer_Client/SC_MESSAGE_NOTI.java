package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;

//TODO：自動產生 PROTO 程式碼。由 MJSoft 製作。
/**
 * 去做：
 * 此處使用訊息通知來源時，itemdesc（圖示）將顯示為圖像。
 ***/
public class SC_MESSAGE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static ProtoOutputStream newBlueMessage(int enchant, int iconId, String message) {
		SC_MESSAGE_NOTI noti = newInstance();
		noti.set_msg_code(4444);
		if (enchant > 0)
			noti.add_args(String.format("+%d %s", enchant, message));
		else
			noti.add_args(message);
		noti.set_option_a(iconId);// itemdesc
		noti.set_type(eType.fromInt(3));
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_MESSAGE_NOTI);
		noti.dispose();
		return stream;
	}

	public static ProtoOutputStream newSilverMessage(int enchant, int iconId, String message) {
		SC_MESSAGE_NOTI noti = newInstance();
		noti.set_msg_code(4445);
		if (enchant > 0)
			noti.add_args(String.format("+%d %s", enchant, message));
		else
			noti.add_args(message);
		noti.set_option_a(iconId);
		noti.set_type(eType.fromInt(3));
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_MESSAGE_NOTI);
		noti.dispose();
		return stream;
	}

	public static ProtoOutputStream newDollMessage(int iconId, String message) {
		SC_MESSAGE_NOTI noti = newInstance();
		noti.set_msg_code(4433);
		noti.add_args(message);
		noti.set_option_a(iconId);
		noti.set_type(eType.fromInt(3));
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_MESSAGE_NOTI);
		noti.dispose();
		return stream;
	}

	public static ProtoOutputStream newSmeltingMessage(int iconId, String message) {
		SC_MESSAGE_NOTI noti = newInstance();
		noti.set_msg_code(4433);
		noti.add_args(message);
		noti.set_option_a(iconId);
		noti.set_type(eType.fromInt(3));
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_MESSAGE_NOTI);
		noti.dispose();
		return stream;
	}

	public static ProtoOutputStream newDropMessage(int iconId, String message/* , int object_id */) {
		SC_MESSAGE_NOTI noti = newInstance();
		noti.set_msg_code(6008);
		// noti.set_object_id(object_id);

		noti.add_args(message);
		noti.set_option_a(iconId);
		noti.set_type(eType.fromInt(3));
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_MESSAGE_NOTI);
		noti.dispose();
		return stream;
	}

	public static SC_MESSAGE_NOTI newInstance() {
		return new SC_MESSAGE_NOTI();
	}

	private SC_MESSAGE_NOTI.eType _type;
	private int _msg_code;
	private String _msg_str;
	private java.util.LinkedList<String> _args;
	private int _option_a;
	private int _object_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_MESSAGE_NOTI() {
	}

	public SC_MESSAGE_NOTI.eType get_type() {
		return _type;
	}

	public void set_type(SC_MESSAGE_NOTI.eType val) {
		_bit |= 0x1;
		_type = val;
	}

	public boolean has_type() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_msg_code() {
		return _msg_code;
	}

	public void set_msg_code(int val) {
		_bit |= 0x2;
		_msg_code = val;
	}

	public boolean has_msg_code() {
		return (_bit & 0x2) == 0x2;
	}

	public String get_msg_str() {
		return _msg_str;
	}

	public void set_msg_str(String val) {
		_bit |= 0x4;
		_msg_str = val;
	}

	public boolean has_msg_str() {
		return (_bit & 0x4) == 0x4;
	}

	public java.util.LinkedList<String> get_args() {
		return _args;
	}

	public void add_args(String val) {
		if (!has_args()) {
			_args = new java.util.LinkedList<String>();
			_bit |= 0x8;
		}
		_args.add(val);
	}

	public boolean has_args() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_option_a() {
		return _option_a;
	}

	public void set_option_a(int val) {
		_bit |= 0x10;
		_option_a = val;
	}

	public boolean has_option_a() {
		return (_bit & 0x10) == 0x10;
	}

	public int get_object_id() {
		return _object_id;
	}

	public void set_object_id(int val) {
		_bit |= 0x20;
		_object_id = val;
	}

	public boolean has_object_id() {
		return (_bit & 0x20) == 0x20;
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
		if (has_msg_code()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _msg_code);
		}
		if (has_msg_str()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _msg_str);
		}
		if (has_args()) {
			for (String val : _args) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, val);
			}
		}
		if (has_option_a()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _option_a);
		}
		if (has_object_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(6, _object_id);
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
		if (!has_args()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_type()) {
			output.writeEnum(1, _type.toInt());
		}
		if (has_msg_code()) {
			output.wirteInt32(2, _msg_code);
		}
		if (has_msg_str()) {
			output.writeString(3, _msg_str);
		}
		if (has_args()) {
			for (String val : _args) {
				output.writeString(4, val);
			}
		}
		if (has_option_a()) {
			output.wirteInt32(5, _option_a);
		}
		if (has_object_id()) {
			output.writeUInt32(6, _object_id);
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
					set_type(SC_MESSAGE_NOTI.eType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010: {
					set_msg_code(input.readInt32());
					break;
				}
				case 0x0000001A: {
					set_msg_str(input.readString());
					break;
				}
				case 0x00000022: {
					add_args(input.readString());
					break;
				}
				case 0x00000028: {
					set_option_a(input.readInt32());
					break;
				}
				case 0x00000030: {
					set_object_id(input.readUInt32());
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
		return new SC_MESSAGE_NOTI();
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

	public enum eType {
		NORMAL(0),
		CRAFT_SUCCESS_BROADCAST(1),
		ALCHEMY_SUCCESS_BROADCAST(2),
		CENTER_BROADCAST_WITH_ITEM(3),
		REVENGE_BROADCAST(4),
		HIGH_LEVEL_INFINITY_BATTLE_BROADCAST(5),
		;

		private int value;

		eType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eType v) {
			return value == v.value;
		}

		public static eType fromInt(int i) {
			switch (i) {
				case 0:
					return NORMAL;
				case 1:
					return CRAFT_SUCCESS_BROADCAST;
				case 2:
					return ALCHEMY_SUCCESS_BROADCAST;
				case 3:
					return CENTER_BROADCAST_WITH_ITEM;
				case 4:
					return REVENGE_BROADCAST;
				case 5:
					return HIGH_LEVEL_INFINITY_BATTLE_BROADCAST;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eType，%d", i));
			}
		}
	}
}
