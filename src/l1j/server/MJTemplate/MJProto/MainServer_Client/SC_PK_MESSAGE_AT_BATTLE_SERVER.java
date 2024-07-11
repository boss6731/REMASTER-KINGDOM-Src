package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_PK_MESSAGE_AT_BATTLE_SERVER implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_PK_MESSAGE_AT_BATTLE_SERVER newInstance() {
		return new SC_PK_MESSAGE_AT_BATTLE_SERVER();
	}

	public static void send(L1PcInstance pc, int _interkind, String _killer_name, int _killer_cache_no,
			String _die_name, int _die_cache_no, int _killer_name_str) {
		SC_PK_MESSAGE_AT_BATTLE_SERVER noti = SC_PK_MESSAGE_AT_BATTLE_SERVER.newInstance();
		noti.set_interkind(_interkind);
		noti.set_killer_name(_killer_name);
		noti.set_killer_cache_no(_killer_cache_no);
		noti.set_die_name(_die_name);
		noti.set_die_cache_no(_die_cache_no);
		noti.set_killer_name_str(_killer_name_str);
		pc.sendPackets(noti, MJEProtoMessages.SC_PK_MESSAGE_AT_BATTLE_SERVER, true);
	}

	private int _interkind;
	private String _killer_name;
	private int _killer_cache_no;
	private String _die_name;
	private int _die_cache_no;
	private int _killer_name_str;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_PK_MESSAGE_AT_BATTLE_SERVER() {
	}

	public int get_interkind() {
		return _interkind;
	}

	public void set_interkind(int val) {
		_bit |= 0x1;
		_interkind = val;
	}

	public boolean has_interkind() {
		return (_bit & 0x1) == 0x1;
	}

	public String get_killer_name() {
		return _killer_name;
	}

	public void set_killer_name(String val) {
		_bit |= 0x2;
		_killer_name = val;
	}

	public boolean has_killer_name() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_killer_cache_no() {
		return _killer_cache_no;
	}

	public void set_killer_cache_no(int val) {
		_bit |= 0x4;
		_killer_cache_no = val;
	}

	public boolean has_killer_cache_no() {
		return (_bit & 0x4) == 0x4;
	}

	public String get_die_name() {
		return _die_name;
	}

	public void set_die_name(String val) {
		_bit |= 0x8;
		_die_name = val;
	}

	public boolean has_die_name() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_die_cache_no() {
		return _die_cache_no;
	}

	public void set_die_cache_no(int val) {
		_bit |= 0x10;
		_die_cache_no = val;
	}

	public boolean has_die_cache_no() {
		return (_bit & 0x10) == 0x10;
	}

	public int get_killer_name_str() {
		return _killer_name_str;
	}

	public void set_killer_name_str(int val) {
		_bit |= 0x20;
		_killer_name_str = val;
	}

	public boolean has_killer_name_str() {
		return (_bit & 0x20) == 0x20;
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
		if (has_interkind()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _interkind);
		}
		if (has_killer_name()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _killer_name);
		}
		if (has_killer_cache_no()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _killer_cache_no);
		}
		if (has_die_name()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _die_name);
		}
		if (has_die_cache_no()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _die_cache_no);
		}
		if (has_killer_name_str()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _killer_name_str);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_interkind()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_killer_name()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_killer_cache_no()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_die_name()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_die_cache_no()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_interkind()) {
			output.wirteInt32(1, _interkind);
		}
		if (has_killer_name()) {
			output.writeString(2, _killer_name);
		}
		if (has_killer_cache_no()) {
			output.wirteInt32(3, _killer_cache_no);
		}
		if (has_die_name()) {
			output.writeString(4, _die_name);
		}
		if (has_die_cache_no()) {
			output.wirteInt32(5, _die_cache_no);
		}
		if (has_killer_name_str()) {
			output.wirteInt32(6, _killer_name_str);
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
					set_interkind(input.readInt32());
					break;
				}
				case 0x00000012: {
					set_killer_name(input.readString());
					break;
				}
				case 0x00000018: {
					set_killer_cache_no(input.readInt32());
					break;
				}
				case 0x00000022: {
					set_die_name(input.readString());
					break;
				}
				case 0x00000028: {
					set_die_cache_no(input.readInt32());
					break;
				}
				case 0x00000030: {
					set_killer_name_str(input.readInt32());
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
		return new SC_PK_MESSAGE_AT_BATTLE_SERVER();
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
