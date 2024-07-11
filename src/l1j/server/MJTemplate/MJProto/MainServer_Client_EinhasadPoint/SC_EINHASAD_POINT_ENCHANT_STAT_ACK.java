package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_EINHASAD_POINT_ENCHANT_STAT_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send_point_enchant_stat(L1PcInstance pc, int value, int sequence, int enchant_level,
			int total_stat) {
		SC_EINHASAD_POINT_ENCHANT_STAT_ACK noti = SC_EINHASAD_POINT_ENCHANT_STAT_ACK.newInstance();
		noti.set_value(value);
		noti.set_sequence(sequence);
		noti.set_cur_enchant_level(enchant_level);
		noti.set_total_stat(total_stat);
		pc.sendPackets(noti, MJEProtoMessages.SC_EINHASAD_POINT_ENCHANT_STAT_ACK, true);
	}

	public static SC_EINHASAD_POINT_ENCHANT_STAT_ACK newInstance() {
		return new SC_EINHASAD_POINT_ENCHANT_STAT_ACK();
	}

	private int _value;
	private int _sequence;
	private int _cur_enchant_level;
	private int _total_stat;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EINHASAD_POINT_ENCHANT_STAT_ACK() {
	}

	public int get_value() {
		return _value;
	}

	public void set_value(int val) {
		_bit |= 0x1;
		_value = val;
	}

	public boolean has_value() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_sequence() {
		return _sequence;
	}

	public void set_sequence(int val) {
		_bit |= 0x2;
		_sequence = val;
	}

	public boolean has_sequence() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_cur_enchant_level() {
		return _cur_enchant_level;
	}

	public void set_cur_enchant_level(int val) {
		_bit |= 0x4;
		_cur_enchant_level = val;
	}

	public boolean has_cur_enchant_level() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_total_stat() {
		return _total_stat;
	}

	public void set_total_stat(int val) {
		_bit |= 0x8;
		_total_stat = val;
	}

	public boolean has_total_stat() {
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
		if (has_value()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _value);
		}
		if (has_sequence()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _sequence);
		}
		if (has_cur_enchant_level()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _cur_enchant_level);
		}
		if (has_total_stat()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _total_stat);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_value()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_sequence()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_cur_enchant_level()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_total_stat()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_value()) {
			output.wirteInt32(1, _value);
		}
		if (has_sequence()) {
			output.wirteInt32(2, _sequence);
		}
		if (has_cur_enchant_level()) {
			output.wirteInt32(3, _cur_enchant_level);
		}
		if (has_total_stat()) {
			output.wirteInt32(4, _total_stat);
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
					set_value(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_sequence(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_cur_enchant_level(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_total_stat(input.readInt32());
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
		return new SC_EINHASAD_POINT_ENCHANT_STAT_ACK();
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
