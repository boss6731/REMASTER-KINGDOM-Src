package l1j.server.MJTemplate.MJProto.MainServer_Client_GameGate;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

//TODO：自動產生 PROTO 程式碼。由 MJSoft 製作。
public class SC_GAMEGATE_PCCAFE_CHARGE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send(L1PcInstance pc, boolean isPC防禦增益) {
		SC_GAMEGATE_PCCAFE_CHARGE_NOTI noti = newInstance();
		int num = isPC防禦增益 ? 1 : 0;
		noti.set_chargeType(eChargeType.fromInt(num));
		// noti.set_msg_id(0);
		pc.sendPackets(noti, MJEProtoMessages.SC_GAMEGATE_PCCAFE_CHARGE_NOTI, true);
	}

	public static SC_GAMEGATE_PCCAFE_CHARGE_NOTI newInstance() {
		return new SC_GAMEGATE_PCCAFE_CHARGE_NOTI();
	}

	private SC_GAMEGATE_PCCAFE_CHARGE_NOTI.eChargeType _chargeType;
	private int _msg_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_GAMEGATE_PCCAFE_CHARGE_NOTI() {
	}

	public SC_GAMEGATE_PCCAFE_CHARGE_NOTI.eChargeType get_chargeType() {
		return _chargeType;
	}

	public void set_chargeType(SC_GAMEGATE_PCCAFE_CHARGE_NOTI.eChargeType val) {
		_bit |= 0x1;
		_chargeType = val;
	}

	public boolean has_chargeType() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_msg_id() {
		return _msg_id;
	}

	public void set_msg_id(int val) {
		_bit |= 0x2;
		_msg_id = val;
	}

	public boolean has_msg_id() {
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
		if (has_chargeType()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _chargeType.toInt());
		}
		if (has_msg_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _msg_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_chargeType()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_chargeType()) {
			output.writeEnum(1, _chargeType.toInt());
		}
		if (has_msg_id()) {
			output.wirteInt32(2, _msg_id);
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
					set_chargeType(SC_GAMEGATE_PCCAFE_CHARGE_NOTI.eChargeType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010: {
					set_msg_id(input.readInt32());
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
		return new SC_GAMEGATE_PCCAFE_CHARGE_NOTI();
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

	public enum eChargeType {
		STOP(0),
		START(1),
		DELAY(2),;

		private int value;

		eChargeType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eChargeType v) {
			return value == v.value;
		}

		public static eChargeType fromInt(int i) {
			switch (i) {
				case 0:
					return STOP;
				case 1:
					return START;
				case 2:
					return DELAY;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eChargeType，%d", i));
			}
		}
	}
}
