package l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand;

import l1j.server.MJTemplate.MJSimpleRgb;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_MSG_ANNOUNCE implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static ProtoOutputStream AnnounceMessage(int pos, String message) {
		SC_MSG_ANNOUNCE noti = newInstance();
		noti.set_pos(eMsgPos.fromInt(pos));
		noti.set_message(message);
		noti.set_color(MJSimpleRgb.fromRgb(255, 255, 0));
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_MSG_ANNOUNCE);
		noti.dispose();
		return stream;
	}

	public static SC_MSG_ANNOUNCE newInstance() {
		return new SC_MSG_ANNOUNCE();
	}

	private SC_MSG_ANNOUNCE.eMsgPos _pos;
	private String _message;
	private byte[] _color;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_MSG_ANNOUNCE() {
	}

	public SC_MSG_ANNOUNCE.eMsgPos get_pos() {
		return _pos;
	}

	public void set_pos(SC_MSG_ANNOUNCE.eMsgPos val) {
		_bit |= 0x1;
		_pos = val;
	}

	public boolean has_pos() {
		return (_bit & 0x1) == 0x1;
	}

	public String get_message() {
		return _message;
	}

	public void set_message(String val) {
		_bit |= 0x2;
		_message = val;
	}

	public boolean has_message() {
		return (_bit & 0x2) == 0x2;
	}

	public byte[] get_color() {
		return _color;
	}

	public void set_color(byte[] val) {
		_bit |= 0x4;
		_color = val;
	}

	public void set_color(MJSimpleRgb rgb) {
		set_color(rgb.get_bytes());
	}

	public boolean has_color() {
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
		if (has_pos()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _pos.toInt());
		}
		if (has_message()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _message);
		}
		if (has_color()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(3, _color);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_pos()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_message()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_color()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_pos()) {
			output.writeEnum(1, _pos.toInt());
		}
		if (has_message()) {
			output.writeString(2, _message);
		}
		if (has_color()) {
			output.writeBytes(3, _color);
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
					set_pos(SC_MSG_ANNOUNCE.eMsgPos.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012: {
					set_message(input.readString());
					break;
				}
				case 0x0000001A: {
					set_color(input.readBytes());
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
		return new SC_MSG_ANNOUNCE();
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

	public enum eMsgPos {
		TOP_POS(0),
		TOP_CHAT_POS(1),
		;

		private int value;

		eMsgPos(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eMsgPos v) {
			return value == v.value;
		}

		public static eMsgPos fromInt(int i) {
			switch (i) {
				case 0:
					return TOP_POS;
				case 1:
					return TOP_CHAT_POS;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eMsgPos，%d", i));
			}
		}
	}
}
