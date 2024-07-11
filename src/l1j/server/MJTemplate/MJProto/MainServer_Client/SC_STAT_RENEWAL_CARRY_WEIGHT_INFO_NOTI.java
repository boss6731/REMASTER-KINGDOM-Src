package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_STAT_RENEWAL_CARRY_WEIGHT_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_STAT_RENEWAL_CARRY_WEIGHT_INFO_NOTI newInstance() {
		return new SC_STAT_RENEWAL_CARRY_WEIGHT_INFO_NOTI();
	}

	public static void send(L1PcInstance pc, int _carrypercent, int _carrycurrent, int _carrymax) {
		SC_STAT_RENEWAL_CARRY_WEIGHT_INFO_NOTI noti = SC_STAT_RENEWAL_CARRY_WEIGHT_INFO_NOTI.newInstance();
		noti.set_carrypercent(_carrypercent);
		noti.set_carrycurrent(_carrycurrent);
		noti.set_carrymax(_carrymax);
		pc.sendPackets(noti, MJEProtoMessages.SC_STAT_RENEWAL_CARRY_WEIGHT_INFO_NOTI, true);
	}

	private int _carrypercent;
	private int _carrycurrent;
	private int _carrymax;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_STAT_RENEWAL_CARRY_WEIGHT_INFO_NOTI() {
	}

	public int get_carrypercent() {
		return _carrypercent;
	}

	public void set_carrypercent(int val) {
		_bit |= 0x1;
		_carrypercent = val;
	}

	public boolean has_carrypercent() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_carrycurrent() {
		return _carrycurrent;
	}

	public void set_carrycurrent(int val) {
		_bit |= 0x2;
		_carrycurrent = val;
	}

	public boolean has_carrycurrent() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_carrymax() {
		return _carrymax;
	}

	public void set_carrymax(int val) {
		_bit |= 0x4;
		_carrymax = val;
	}

	public boolean has_carrymax() {
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
		if (has_carrypercent()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _carrypercent);
		}
		if (has_carrycurrent()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _carrycurrent);
		}
		if (has_carrymax()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _carrymax);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_carrypercent()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_carrycurrent()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_carrymax()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_carrypercent()) {
			output.wirteInt32(1, _carrypercent);
		}
		if (has_carrycurrent()) {
			output.wirteInt32(2, _carrycurrent);
		}
		if (has_carrymax()) {
			output.wirteInt32(3, _carrymax);
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
					set_carrypercent(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_carrycurrent(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_carrymax(input.readInt32());
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
		return new SC_STAT_RENEWAL_CARRY_WEIGHT_INFO_NOTI();
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
