package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eRevengeResult;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class PursuitTargetT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static PursuitTargetT newInstance() {
		return new PursuitTargetT();
	}

	private int _server_no;
	private String _user_name;
	private eRevengeResult _result;
	private int _world_number;
	private int _location;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private PursuitTargetT() {
	}

	public int get_server_no() {
		return _server_no;
	}

	public void set_server_no(int val) {
		_bit |= 0x1;
		_server_no = val;
	}

	public boolean has_server_no() {
		return (_bit & 0x1) == 0x1;
	}

	public String get_user_name() {
		return _user_name;
	}

	public void set_user_name(String val) {
		_bit |= 0x2;
		_user_name = val;
	}

	public boolean has_user_name() {
		return (_bit & 0x2) == 0x2;
	}

	public eRevengeResult get_result() {
		return _result;
	}

	public void set_result(eRevengeResult val) {
		_bit |= 0x4;
		_result = val;
	}

	public boolean has_result() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_world_number() {
		return _world_number;
	}

	public void set_world_number(int val) {
		_bit |= 0x8;
		_world_number = val;
	}

	public boolean has_world_number() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_location() {
		return _location;
	}

	public void set_location(int val) {
		_bit |= 0x10;
		_location = val;
	}

	public boolean has_location() {
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
		if (has_server_no()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _server_no);
		}
		if (has_user_name()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _user_name);
		}
		if (has_result()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(3, _result.toInt());
		}
		if (has_world_number()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _world_number);
		}
		if (has_location()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _location);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_server_no()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_user_name()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_server_no()) {
			output.writeUInt32(1, _server_no);
		}
		if (has_user_name()) {
			output.writeString(2, _user_name);
		}
		if (has_result()) {
			output.writeEnum(3, _result.toInt());
		}
		if (has_world_number()) {
			output.wirteInt32(4, _world_number);
		}
		if (has_location()) {
			output.wirteInt32(5, _location);
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
					set_server_no(input.readUInt32());
					break;
				}
				case 0x00000012: {
					set_user_name(input.readString());
					break;
				}
				case 0x00000018: {
					set_result(eRevengeResult.fromInt(input.readEnum()));
					break;
				}
				case 0x00000020: {
					set_world_number(input.readInt32());
					break;
				}
				case 0x00000028: {
					set_location(input.readInt32());
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
		return new PursuitTargetT();
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
