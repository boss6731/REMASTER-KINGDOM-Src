package l1j.server.MJTemplate.MJProto.Mainserver_Client_Equip;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_EXTEND_SLOT_CURRENCY_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void slot_currency_send(L1PcInstance pc, int price, int position, boolean discount) {
		SC_EXTEND_SLOT_CURRENCY_NOTI noti = SC_EXTEND_SLOT_CURRENCY_NOTI.newInstance();
		noti.set_isDiscount(discount);
		noti.set_adena_count(price);
		noti.set_slot_position(position);
		pc.sendPackets(noti, MJEProtoMessages.SC_EXTEND_SLOT_CURRENCY_NOTI, true);
	}

	public static SC_EXTEND_SLOT_CURRENCY_NOTI newInstance() {
		return new SC_EXTEND_SLOT_CURRENCY_NOTI();
	}

	private boolean _isDiscount;
	private int _adena_count;
	private int _slot_position;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EXTEND_SLOT_CURRENCY_NOTI() {
	}

	public boolean get_isDiscount() {
		return _isDiscount;
	}

	public void set_isDiscount(boolean val) {
		_bit |= 0x1;
		_isDiscount = val;
	}

	public boolean has_isDiscount() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_adena_count() {
		return _adena_count;
	}

	public void set_adena_count(int val) {
		_bit |= 0x2;
		_adena_count = val;
	}

	public boolean has_adena_count() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_slot_position() {
		return _slot_position;
	}

	public void set_slot_position(int val) {
		_bit |= 0x4;
		_slot_position = val;
	}

	public boolean has_slot_position() {
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
		if (has_isDiscount()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _isDiscount);
		}
		if (has_adena_count()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _adena_count);
		}
		if (has_slot_position()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _slot_position);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_isDiscount()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_adena_count()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_slot_position()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_isDiscount()) {
			output.writeBool(1, _isDiscount);
		}
		if (has_adena_count()) {
			output.wirteInt32(2, _adena_count);
		}
		if (has_slot_position()) {
			output.wirteInt32(3, _slot_position);
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
					set_isDiscount(input.readBool());
					break;
				}
				case 0x00000010: {
					set_adena_count(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_slot_position(input.readInt32());
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
		return new SC_EXTEND_SLOT_CURRENCY_NOTI();
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
