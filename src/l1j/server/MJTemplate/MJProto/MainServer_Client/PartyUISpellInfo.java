package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class PartyUISpellInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static PartyUISpellInfo newInstance() {
		return new PartyUISpellInfo();
	}

	private int _bufficon_id;
	private boolean _is_good;
	private int _spell_id;
	private int _duration;
	private int _tooltip_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private PartyUISpellInfo() {
	}

	public int get_bufficon_id() {
		return _bufficon_id;
	}

	public void set_bufficon_id(int val) {
		_bit |= 0x1;
		_bufficon_id = val;
	}

	public boolean has_bufficon_id() {
		return (_bit & 0x1) == 0x1;
	}

	public boolean get_is_good() {
		return _is_good;
	}

	public void set_is_good(boolean val) {
		_bit |= 0x2;
		_is_good = val;
	}

	public boolean has_is_good() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_spell_id() {
		return _spell_id;
	}

	public void set_spell_id(int val) {
		_bit |= 0x4;
		_spell_id = val;
	}

	public boolean has_spell_id() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_duration() {
		return _duration;
	}

	public void set_duration(int val) {
		_bit |= 0x8;
		_duration = val;
	}

	public boolean has_duration() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_tooltip_id() {
		return _tooltip_id;
	}

	public void set_tooltip_id(int val) {
		_bit |= 0x10;
		_tooltip_id = val;
	}

	public boolean has_tooltip_id() {
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
		if (has_bufficon_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _bufficon_id);
		if (has_is_good())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _is_good);
		if (has_spell_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _spell_id);
		if (has_duration())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _duration);
		if (has_tooltip_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _tooltip_id);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_bufficon_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_bufficon_id()) {
			output.writeUInt32(1, _bufficon_id);
		}
		if (has_is_good()) {
			output.writeBool(2, _is_good);
		}
		if (has_spell_id()) {
			output.writeUInt32(3, _spell_id);
		}
		if (has_duration()) {
			output.writeUInt32(4, _duration);
		}
		if (has_tooltip_id()) {
			output.writeUInt32(5, _tooltip_id);
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
					set_bufficon_id(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_is_good(input.readBool());
					break;
				}
				case 0x00000018: {
					set_spell_id(input.readUInt32());
					break;
				}
				case 0x00000020: {
					set_duration(input.readUInt32());
					break;
				}
				case 0x00000028: {
					set_tooltip_id(input.readUInt32());
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
		return new PartyUISpellInfo();
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
