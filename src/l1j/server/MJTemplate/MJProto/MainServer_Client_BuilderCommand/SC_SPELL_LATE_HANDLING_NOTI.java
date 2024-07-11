package l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class SC_SPELL_LATE_HANDLING_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send(L1PcInstance pc, boolean on_off, eLevel level) {
		SC_SPELL_LATE_HANDLING_NOTI noti = newInstance();
		noti.set_on_off(on_off);
		noti.set_correction_level(level);
		pc.sendPackets(noti, MJEProtoMessages.SC_SPELL_LATE_HANDLING_NOTI);
	}

	public static SC_SPELL_LATE_HANDLING_NOTI newInstance() {
		return new SC_SPELL_LATE_HANDLING_NOTI();
	}

	private boolean _on_off;
	private eLevel _correction_level;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_SPELL_LATE_HANDLING_NOTI() {
	}

	public boolean get_on_off() {
		return _on_off;
	}

	public void set_on_off(boolean val) {
		_bit |= 0x1;
		_on_off = val;
	}

	public boolean has_on_off() {
		return (_bit & 0x1) == 0x1;
	}

	public eLevel get_correction_level() {
		return _correction_level;
	}

	public void set_correction_level(eLevel val) {
		_bit |= 0x2;
		_correction_level = val;
	}

	public boolean has_correction_level() {
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
		if (has_on_off())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _on_off);
		if (has_correction_level())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _correction_level.toInt());
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_on_off()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_on_off()) {
			output.writeBool(1, _on_off);
		}
		if (has_correction_level()) {
			output.writeEnum(2, _correction_level.toInt());
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				default: {
					return this;
				}
				case 0x00000008: {
					set_on_off(input.readBool());
					break;
				}
				case 0x00000010: {
					set_correction_level(eLevel.fromInt(input.readEnum()));
					break;
				}
			}
		}
		return this;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Lenz.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_SPELL_LATE_HANDLING_NOTI();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	public enum eLevel {
		NOT_CORRECTION(0),
		LEVEL_1(1),
		LEVEL_2(2),
		LEVEL_3(3),
		LEVEL_4(4),
		LEVEL_5(5);

		private int value;

		eLevel(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eLevel v) {
			return value == v.value;
		}

		public static eLevel fromInt(int i) {
			switch (i) {
				case 0:
					return NOT_CORRECTION;
				case 1:
					return LEVEL_1;
				case 2:
					return LEVEL_2;
				case 3:
					return LEVEL_3;
				case 4:
					return LEVEL_4;
				case 5:
					return LEVEL_5;
				default:
					return null;
			}
		}
	}
}
