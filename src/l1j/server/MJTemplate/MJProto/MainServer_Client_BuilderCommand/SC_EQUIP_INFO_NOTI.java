package l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand;

import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_EQUIP_LIST_NOTI;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_EQUIP_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_EQUIP_INFO_NOTI newInstance() {
		return new SC_EQUIP_INFO_NOTI();
	}

	private byte[] _target_name;
	private int _target_gender;
	private int _target_class;
	private SC_EQUIP_LIST_NOTI _equip_list_noti;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EQUIP_INFO_NOTI() {
	}

	public byte[] get_target_name() {
		return _target_name;
	}

	public void set_target_name(byte[] val) {
		_bit |= 0x1;
		_target_name = val;
	}

	public boolean has_target_name() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_target_gender() {
		return _target_gender;
	}

	public void set_target_gender(int val) {
		_bit |= 0x2;
		_target_gender = val;
	}

	public boolean has_target_gender() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_target_class() {
		return _target_class;
	}

	public void set_target_class(int val) {
		_bit |= 0x4;
		_target_class = val;
	}

	public boolean has_target_class() {
		return (_bit & 0x4) == 0x4;
	}

	public SC_EQUIP_LIST_NOTI get_equip_list_noti() {
		return _equip_list_noti;
	}

	public void set_equip_list_noti(SC_EQUIP_LIST_NOTI val) {
		_bit |= 0x8;
		_equip_list_noti = val;
	}

	public boolean has_equip_list_noti() {
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
		if (has_target_name()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(1, _target_name);
		}
		if (has_target_gender()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _target_gender);
		}
		if (has_target_class()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _target_class);
		}
		if (has_equip_list_noti()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _equip_list_noti);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_target_name()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_target_gender()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_target_class()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_target_name()) {
			output.writeBytes(1, _target_name);
		}
		if (has_target_gender()) {
			output.wirteInt32(2, _target_gender);
		}
		if (has_target_class()) {
			output.wirteInt32(3, _target_class);
		}
		if (has_equip_list_noti()) {
			output.writeMessage(4, _equip_list_noti);
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
				case 0x0000000A: {
					set_target_name(input.readBytes());
					break;
				}
				case 0x00000010: {
					set_target_gender(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_target_class(input.readInt32());
					break;
				}
				case 0x00000022: {
					set_equip_list_noti((SC_EQUIP_LIST_NOTI) input.readMessage(SC_EQUIP_LIST_NOTI.newInstance()));
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
		return new SC_EQUIP_INFO_NOTI();
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
