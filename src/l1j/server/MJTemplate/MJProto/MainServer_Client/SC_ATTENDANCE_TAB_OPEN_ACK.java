package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ATTENDANCE_TAB_OPEN_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static ProtoOutputStream make_stream_server(L1PcInstance pc, AttendanceGroupType group, int i) {
		SC_ATTENDANCE_TAB_OPEN_ACK noti = newInstance();
		noti.set_group_id(group.toInt());
		noti.set_open_step(SC_ATTENDANCE_TAB_OPEN_ACK.eOpenStep.fromInt(1));
		noti.set_season_num(i);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_ATTENDANCE_TAB_OPEN_ACK);
		noti.dispose();
		return stream;
	}

	public static ProtoOutputStream make_stream_db(L1PcInstance pc, AttendanceGroupType group, int i) {
		SC_ATTENDANCE_TAB_OPEN_ACK noti = newInstance();
		noti.set_group_id(group.toInt());
		noti.set_open_step(SC_ATTENDANCE_TAB_OPEN_ACK.eOpenStep.fromInt(2));
		noti.set_season_num(i);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_ATTENDANCE_TAB_OPEN_ACK);
		noti.dispose();
		return stream;
	}

	public static ProtoOutputStream make_stream_message(L1PcInstance pc, AttendanceGroupType group, eErrorType type) {
		SC_ATTENDANCE_TAB_OPEN_ACK noti = newInstance();
		noti.set_error(type);
		noti.set_group_id(group.toInt());
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_ATTENDANCE_TAB_OPEN_ACK);
		noti.dispose();
		return stream;
	}

	public static SC_ATTENDANCE_TAB_OPEN_ACK newInstance() {
		return new SC_ATTENDANCE_TAB_OPEN_ACK();
	}

	private SC_ATTENDANCE_TAB_OPEN_ACK.eErrorType _error;
	private int _group_id;
	private java.util.LinkedList<ATTENDANCE_INFO> _attendance_info;
	private SC_ATTENDANCE_TAB_OPEN_ACK.eOpenStep _open_step;
	private int _season_num;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_ATTENDANCE_TAB_OPEN_ACK() {
	}

	public SC_ATTENDANCE_TAB_OPEN_ACK.eErrorType get_error() {
		return _error;
	}

	public void set_error(SC_ATTENDANCE_TAB_OPEN_ACK.eErrorType val) {
		_bit |= 0x1;
		_error = val;
	}

	public boolean has_error() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_group_id() {
		return _group_id;
	}

	public void set_group_id(int val) {
		_bit |= 0x2;
		_group_id = val;
	}

	public boolean has_group_id() {
		return (_bit & 0x2) == 0x2;
	}

	public java.util.LinkedList<ATTENDANCE_INFO> get_attendance_info() {
		return _attendance_info;
	}

	public void add_attendance_info(ATTENDANCE_INFO val) {
		if (!has_attendance_info()) {
			_attendance_info = new java.util.LinkedList<ATTENDANCE_INFO>();
			_bit |= 0x4;
		}
		_attendance_info.add(val);
	}

	public boolean has_attendance_info() {
		return (_bit & 0x4) == 0x4;
	}

	public SC_ATTENDANCE_TAB_OPEN_ACK.eOpenStep get_open_step() {
		return _open_step;
	}

	public void set_open_step(SC_ATTENDANCE_TAB_OPEN_ACK.eOpenStep val) {
		_bit |= 0x8;
		_open_step = val;
	}

	public boolean has_open_step() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_season_num() {
		return _season_num;
	}

	public void set_season_num(int val) {
		_bit |= 0x10;
		_season_num = val;
	}

	public boolean has_season_num() {
		return (_bit & 0x10) == 0x10;
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
		if (has_error()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _error.toInt());
		}
		if (has_group_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _group_id);
		}
		if (has_attendance_info()) {
			for (ATTENDANCE_INFO val : _attendance_info) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		if (has_open_step()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(4, _open_step.toInt());
		}
		if (has_season_num()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _season_num);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_group_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_attendance_info()) {
			for (ATTENDANCE_INFO val : _attendance_info) {
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
		if (has_error()) {
			output.writeEnum(1, _error.toInt());
		}
		if (has_group_id()) {
			output.wirteInt32(2, _group_id);
		}
		if (has_attendance_info()) {
			for (ATTENDANCE_INFO val : _attendance_info) {
				output.writeMessage(3, val);
			}
		}
		if (has_open_step()) {
			output.writeEnum(4, _open_step.toInt());
		}
		if (has_season_num()) {
			output.writeUInt32(5, _season_num);
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
					set_error(SC_ATTENDANCE_TAB_OPEN_ACK.eErrorType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010: {
					set_group_id(input.readInt32());
					break;
				}
				case 0x0000001A: {
					add_attendance_info((ATTENDANCE_INFO) input.readMessage(ATTENDANCE_INFO.newInstance()));
					break;
				}
				case 0x00000020: {
					set_open_step(SC_ATTENDANCE_TAB_OPEN_ACK.eOpenStep.fromInt(input.readEnum()));
					break;
				}
				case 0x00000028: {
					set_season_num(input.readUInt32());
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
		return new SC_ATTENDANCE_TAB_OPEN_ACK();
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

	public enum eErrorType {
		error_block(1),
		error_fail(2),
		error_money(3),
		error_safepos(4),
		error_invalid(5),
		error_wait(6),
		error_wait_state(7),
		error_xml(8),
		error_system(9),
		error_unknown(10),
		error_fail_db(11),
		error_fail_group(12),
		;

		private int value;

		eErrorType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eErrorType v) {
			return value == v.value;
		}

		public static eErrorType fromInt(int i) {
			switch (i) {
				case 1:
					return error_block;
				case 2:
					return error_fail;
				case 3:
					return error_money;
				case 4:
					return error_safepos;
				case 5:
					return error_invalid;
				case 6:
					return error_wait;
				case 7:
					return error_wait_state;
				case 8:
					return error_xml;
				case 9:
					return error_system;
				case 10:
					return error_unknown;
				case 11:
					return error_fail_db;
				case 12:
					return error_fail_group;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eErrorType，%d", i));
			}
		}
	}

	public enum eOpenStep {
		open_server(1),
		open_db(2),
		;

		private int value;

		eOpenStep(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eOpenStep v) {
			return value == v.value;
		}

		public static eOpenStep fromInt(int i) {
			switch (i) {
				case 1:
					return open_server;
				case 2:
					return open_db;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eOpenStep，%d", i));
			}
		}
	}
}
