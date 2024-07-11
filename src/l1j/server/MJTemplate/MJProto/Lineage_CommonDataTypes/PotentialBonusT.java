package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO：自動產生 PROTO 程式碼。大自然創造的。
public class PotentialBonusT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static PotentialBonusT newInstance() {
		return new PotentialBonusT();
	}

	private int _bonus_grade;
	private int _bonus_id;
	private int _bonus_desc;
	private String _bonus_value;
	private byte[] _bonus_desc_str;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private PotentialBonusT() {
	}

	public int get_bonus_grade() {
		return _bonus_grade;
	}

	public void set_bonus_grade(int val) {
		_bit |= 0x1;
		_bonus_grade = val;
	}

	public boolean has_bonus_grade() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_bonus_id() {
		return _bonus_id;
	}

	public void set_bonus_id(int val) {
		_bit |= 0x2;
		_bonus_id = val;
	}

	public boolean has_bonus_id() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_bonus_desc() {
		return _bonus_desc;
	}

	public void set_bonus_desc(int val) {
		_bit |= 0x4;
		_bonus_desc = val;
	}

	public boolean has_bonus_desc() {
		return (_bit & 0x4) == 0x4;
	}

	public String get_bonus_value() {
		return _bonus_value;
	}

	public void set_bonus_value(String val) {
		_bit |= 0x8;
		_bonus_value = val;
	}

	public boolean has_bonus_value() {
		return (_bit & 0x8) == 0x8;
	}

	public byte[] get_bonus_desc_str() {
		return _bonus_desc_str;
	}

	public void set_bonus_desc_str(byte[] val) {
		_bit |= 0x10;
		_bonus_desc_str = val;
	}

	public boolean has_bonus_desc_str() {
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
		if (has_bonus_grade()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _bonus_grade);
		}
		if (has_bonus_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _bonus_id);
		}
		if (has_bonus_desc()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _bonus_desc);
		}
		if (has_bonus_value()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _bonus_value);
		}
		if (has_bonus_desc_str()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(5, _bonus_desc_str);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_bonus_grade()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_bonus_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_bonus_grade()) {
			output.wirteInt32(1, _bonus_grade);
		}
		if (has_bonus_id()) {
			output.wirteInt32(2, _bonus_id);
		}
		if (has_bonus_desc()) {
			output.wirteInt32(3, _bonus_desc);
		}
		if (has_bonus_value()) {
			output.writeString(4, _bonus_value);
		}
		if (has_bonus_desc_str()) {
			output.writeBytes(5, _bonus_desc_str);
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
					set_bonus_grade(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_bonus_id(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_bonus_desc(input.readInt32());
					break;
				}
				case 0x00000022: {
					set_bonus_value(input.readString());
					break;
				}
				case 0x0000002A: {
					set_bonus_desc_str(input.readBytes());
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

			// TODO：從下面插入處理程式碼。大自然創造的。

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new PotentialBonusT();
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
