package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.UnsupportedEncodingException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_EVENT_COUNTDOWN_NOTI_PACKET implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_EVENT_COUNTDOWN_NOTI_PACKET newInstance() {
		return new SC_EVENT_COUNTDOWN_NOTI_PACKET();
	}

	public static void send(L1PcInstance pc, int remainSeconds, String desc) {
		SC_EVENT_COUNTDOWN_NOTI_PACKET pck = SC_EVENT_COUNTDOWN_NOTI_PACKET.newInstance();
		pck.set_remain_time(remainSeconds);
		try {
			pck.set_event_desc(desc.getBytes("MS949"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		pc.sendPackets(pck, MJEProtoMessages.SC_EVENT_COUNTDOWN_NOTI_PACKET, true);
	}

	public static SC_EVENT_COUNTDOWN_NOTI_PACKET create(int remainSeconds, String desc) {
		SC_EVENT_COUNTDOWN_NOTI_PACKET pck = SC_EVENT_COUNTDOWN_NOTI_PACKET.newInstance();
		pck.set_remain_time(remainSeconds);
		try {
			pck.set_event_desc(desc.getBytes("MS949"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return pck;
	}

	private int _remain_time;
	private byte[] _event_desc;
	private SC_EVENT_COUNTDOWN_NOTI_PACKET.eType _timer_type;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EVENT_COUNTDOWN_NOTI_PACKET() {
	}

	public int get_remain_time() {
		return _remain_time;
	}

	public void set_remain_time(int val) {
		_bit |= 0x1;
		_remain_time = val;
	}

	public boolean has_remain_time() {
		return (_bit & 0x1) == 0x1;
	}

	public byte[] get_event_desc() {
		return _event_desc;
	}

	public void set_event_desc(byte[] val) {
		_bit |= 0x2;
		_event_desc = val;
	}

	public boolean has_event_desc() {
		return (_bit & 0x2) == 0x2;
	}

	public SC_EVENT_COUNTDOWN_NOTI_PACKET.eType get_timer_type() {
		return _timer_type;
	}

	public void set_timer_type(SC_EVENT_COUNTDOWN_NOTI_PACKET.eType val) {
		_bit |= 0x4;
		_timer_type = val;
	}

	public boolean has_timer_type() {
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
		if (has_remain_time()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _remain_time);
		}
		if (has_event_desc()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(2, _event_desc);
		}
		if (has_timer_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _timer_type.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_remain_time()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_remain_time()) {
			output.writeUInt32(1, _remain_time);
		}
		if (has_event_desc()) {
			output.writeBytes(2, _event_desc);
		}
		if (has_timer_type()) {
			output.writeEnum(3, _timer_type.toInt());
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
					set_remain_time(input.readUInt32());
					break;
				}
				case 0x00000012: {
					set_event_desc(input.readBytes());
					break;
				}
				case 0x00000018: {
					set_timer_type(SC_EVENT_COUNTDOWN_NOTI_PACKET.eType.fromInt(input.readEnum()));
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
		return new SC_EVENT_COUNTDOWN_NOTI_PACKET();
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
		MAP_TIMER(1),
		CONTENTS_TIMER(2),
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
				case 1:
					return MAP_TIMER;
				case 2:
					return CONTENTS_TIMER;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eType，%d", i));
			}
		}
	}
}
