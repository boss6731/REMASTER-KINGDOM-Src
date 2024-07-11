package l1j.server.MJTemplate.MJProto.Mainserver_Client_Equip;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_EXTEND_SLOT_RESULT_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void slot_result_send(L1PcInstance pc, int position, boolean success, boolean discount) {
		SC_EXTEND_SLOT_RESULT_NOTI noti = SC_EXTEND_SLOT_RESULT_NOTI.newInstance();
		noti.set_slot_position(position);
		noti.set_is_sucess(success);
		noti.set_isDiscount(discount);
		pc.sendPackets(noti, MJEProtoMessages.SC_EXTEND_SLOT_RESULT_NOTI, true);
	}

	public static SC_EXTEND_SLOT_RESULT_NOTI newInstance() {
		return new SC_EXTEND_SLOT_RESULT_NOTI();
	}

	private int _slot_position;
	private boolean _is_sucess;
	private boolean _isDiscount;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EXTEND_SLOT_RESULT_NOTI() {
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

	public boolean get_is_sucess() {
		return _is_sucess;
	}

	public void set_is_sucess(boolean val) {
		_bit |= 0x2;
		_is_sucess = val;
	}

	public boolean has_is_sucess() {
		return (_bit & 0x2) == 0x2;
	}

	public boolean get_isDiscount() {
		return _isDiscount;
	}

	public void set_isDiscount(boolean val) {
		_bit |= 0x4;
		_isDiscount = val;
	}

	public boolean has_isDiscount() {
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
		if (has_slot_position()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _slot_position);
		}
		if (has_is_sucess()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _is_sucess);
		}
		if (has_isDiscount()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _isDiscount);
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
		if (!has_is_sucess()) {
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
		if (has_is_sucess()) {
			output.writeBool(2, _is_sucess);
		}
		if (has_isDiscount()) {
			output.writeBool(3, _isDiscount);
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
					set_is_sucess(input.readBool());
					break;
				}
				case 0x00000018: {
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new SC_EXTEND_SLOT_RESULT_NOTI();
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
