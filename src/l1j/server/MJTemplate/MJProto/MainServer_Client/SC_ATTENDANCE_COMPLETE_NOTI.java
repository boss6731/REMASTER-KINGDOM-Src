package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.WireFormat;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ATTENDANCE_COMPLETE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_ATTENDANCE_COMPLETE_NOTI newInstance() {
		return new SC_ATTENDANCE_COMPLETE_NOTI();
	}

	public static void send(L1PcInstance pc, int index, int groupId) {
		SC_ATTENDANCE_COMPLETE_NOTI noti = newInstance();
		noti.set_attendance_id(index);
		noti.set_group_id(groupId);
		noti.set_status(2);
		noti.set_season_num(0);
		pc.sendPackets(noti.writeTo(MJEProtoMessages.SC_ATTENDANCE_COMPLETE_NOTI), true);
	}

	private int _attendance_id;
	private int _group_id;
	private int _status;
	private ATTENDANCE_RANDOM _reserved_random;
	private int _season_num;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_ATTENDANCE_COMPLETE_NOTI() {
	}

	public int get_attendance_id() {
		return _attendance_id;
	}

	public void set_attendance_id(int val) {
		_bit |= 0x1;
		_attendance_id = val;
	}

	public boolean has_attendance_id() {
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

	public int get_status() {
		return _status;
	}

	public void set_status(int val) {
		_bit |= 0x4;
		_status = val;
	}

	public boolean has_status() {
		return (_bit & 0x4) == 0x4;
	}

	public ATTENDANCE_RANDOM get_reserved_random() {
		return _reserved_random;
	}

	public void set_reserved_random(ATTENDANCE_RANDOM val) {
		_bit |= 0x8;
		_reserved_random = val;
	}

	public boolean has_reserved_random() {
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
		if (has_attendance_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _attendance_id);
		}
		if (has_group_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _group_id);
		}
		if (has_status()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _status);
		}
		if (has_reserved_random()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _reserved_random);
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
		if (!has_attendance_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_group_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_status()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_attendance_id()) {
			output.wirteInt32(1, _attendance_id);
		}
		if (has_group_id()) {
			output.wirteInt32(2, _group_id);
		}
		if (has_status()) {
			output.wirteInt32(3, _status);
		}
		if (has_reserved_random()) {
			output.writeMessage(4, _reserved_random);
		}
		if (has_season_num()) {
			output.writeUInt32(5, _season_num);
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
					set_attendance_id(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_group_id(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_status(input.readInt32());
					break;
				}
				case 0x00000022: {
					set_reserved_random((ATTENDANCE_RANDOM) input.readMessage(ATTENDANCE_RANDOM.newInstance()));
					break;
				}
				case 0x00000028: {
					set_season_num(input.readUInt32());
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
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_ATTENDANCE_COMPLETE_NOTI();
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
}
