package l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class EventPushInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static EventPushInfo newInstance() {
		return new EventPushInfo();
	}

	private long _event_push_id;
	private int _reset_num;
	private int _remain_time;
	private long _expire_date;
	private long _enable_date;
	private int _item_name_id;
	private int _item_amount;
	private int _item_enchant;
	private int _bless_code;
	private boolean _item_used_immediately;
	private int _item_icon;
	private String _item_desc;
	private byte[] _item_extra_desc;
	private String _subject;
	private String _text;
	private int _event_push_status;
	private String _web_url;
	private int _image_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private EventPushInfo() {
	}

	public long get_event_push_id() {
		return _event_push_id;
	}

	public void set_event_push_id(long val) {
		_bit |= 0x1;
		_event_push_id = val;
	}

	public boolean has_event_push_id() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_reset_num() {
		return _reset_num;
	}

	public void set_reset_num(int val) {
		_bit |= 0x2;
		_reset_num = val;
	}

	public boolean has_reset_num() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_remain_time() {
		return _remain_time;
	}

	public void set_remain_time(int val) {
		_bit |= 0x4;
		_remain_time = val;
	}

	public boolean has_remain_time() {
		return (_bit & 0x4) == 0x4;
	}

	public long get_expire_date() {
		return _expire_date;
	}

	public void set_expire_date(long val) {
		_bit |= 0x8;
		_expire_date = val;
	}

	public boolean has_expire_date() {
		return (_bit & 0x8) == 0x8;
	}

	public long get_enable_date() {
		return _enable_date;
	}

	public void set_enable_date(long val) {
		_bit |= 0x10;
		_enable_date = val;
	}

	public boolean has_enable_date() {
		return (_bit & 0x10) == 0x10;
	}

	public int get_item_name_id() {
		return _item_name_id;
	}

	public void set_item_name_id(int val) {
		_bit |= 0x20;
		_item_name_id = val;
	}

	public boolean has_item_name_id() {
		return (_bit & 0x20) == 0x20;
	}

	public int get_item_amount() {
		return _item_amount;
	}

	public void set_item_amount(int val) {
		_bit |= 0x40;
		_item_amount = val;
	}

	public boolean has_item_amount() {
		return (_bit & 0x40) == 0x40;
	}

	public int get_item_enchant() {
		return _item_enchant;
	}

	public void set_item_enchant(int val) {
		_bit |= 0x80;
		_item_enchant = val;
	}

	public boolean has_item_enchant() {
		return (_bit & 0x80) == 0x80;
	}

	public int get_bless_code() {
		return _bless_code;
	}

	public void set_bless_code(int val) {
		_bit |= 0x100;
		_bless_code = val;
	}

	public boolean has_bless_code() {
		return (_bit & 0x100) == 0x100;
	}

	public boolean get_item_used_immediately() {
		return _item_used_immediately;
	}

	public void set_item_used_immediately(boolean val) {
		_bit |= 0x200;
		_item_used_immediately = val;
	}

	public boolean has_item_used_immediately() {
		return (_bit & 0x200) == 0x200;
	}

	public int get_item_icon() {
		return _item_icon;
	}

	public void set_item_icon(int val) {
		_bit |= 0x400;
		_item_icon = val;
	}

	public boolean has_item_icon() {
		return (_bit & 0x400) == 0x400;
	}

	public String get_item_desc() {
		return _item_desc;
	}

	public void set_item_desc(String val) {
		_bit |= 0x800;
		_item_desc = val;
	}

	public boolean has_item_desc() {
		return (_bit & 0x800) == 0x800;
	}

	public byte[] get_item_extra_desc() {
		return _item_extra_desc;
	}

	public void set_item_extra_desc(byte[] val) {
		_bit |= 0x1000;
		_item_extra_desc = val;
	}

	public boolean has_item_extra_desc() {
		return (_bit & 0x1000) == 0x1000;
	}

	public String get_subject() {
		return _subject;
	}

	public void set_subject(String val) {
		_bit |= 0x2000;
		_subject = val;
	}

	public boolean has_subject() {
		return (_bit & 0x2000) == 0x2000;
	}

	public String get_text() {
		return _text;
	}

	public void set_text(String val) {
		_bit |= 0x4000;
		_text = val;
	}

	public boolean has_text() {
		return (_bit & 0x4000) == 0x4000;
	}

	public int get_event_push_status() {
		return _event_push_status;
	}

	public void set_event_push_status(int val) {
		_bit |= 0x8000;
		_event_push_status = val;
	}

	public boolean has_event_push_status() {
		return (_bit & 0x8000) == 0x8000;
	}

	public String get_web_url() {
		return _web_url;
	}

	public void set_web_url(String val) {
		_bit |= 0x10000;
		_web_url = val;
	}

	public boolean has_web_url() {
		return (_bit & 0x10000) == 0x10000;
	}

	public int get_image_id() {
		return _image_id;
	}

	public void set_image_id(int val) {
		_bit |= 0x20000;
		_image_id = val;
	}

	public boolean has_image_id() {
		return (_bit & 0x20000) == 0x20000;
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
		if (has_event_push_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(1, _event_push_id);
		}
		if (has_reset_num()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _reset_num);
		}
		if (has_remain_time()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _remain_time);
		}
		if (has_expire_date()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(4, _expire_date);
		}
		if (has_enable_date()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(5, _enable_date);
		}
		if (has_item_name_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _item_name_id);
		}
		if (has_item_amount()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _item_amount);
		}
		if (has_item_enchant()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _item_enchant);
		}
		if (has_bless_code()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _bless_code);
		}
		if (has_item_used_immediately()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(10, _item_used_immediately);
		}
		if (has_item_icon()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(11, _item_icon);
		}
		if (has_item_desc()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(12, _item_desc);
		}
		if (has_item_extra_desc()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(13, _item_extra_desc);
		}
		if (has_subject()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(14, _subject);
		}
		if (has_text()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(15, _text);
		}
		if (has_event_push_status()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(16, _event_push_status);
		}
		if (has_web_url()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(17, _web_url);
		}
		if (has_image_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(18, _image_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_event_push_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_reset_num()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_remain_time()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_expire_date()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_enable_date()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_item_name_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_item_amount()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_item_enchant()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_bless_code()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_item_used_immediately()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_item_icon()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_item_desc()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_item_extra_desc()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_subject()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_text()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_event_push_status()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_event_push_id()) {
			output.writeInt64(1, _event_push_id);
		}
		if (has_reset_num()) {
			output.wirteInt32(2, _reset_num);
		}
		if (has_remain_time()) {
			output.writeUInt32(3, _remain_time);
		}
		if (has_expire_date()) {
			output.writeInt64(4, _expire_date);
		}
		if (has_enable_date()) {
			output.writeInt64(5, _enable_date);
		}
		if (has_item_name_id()) {
			output.wirteInt32(6, _item_name_id);
		}
		if (has_item_amount()) {
			output.wirteInt32(7, _item_amount);
		}
		if (has_item_enchant()) {
			output.wirteInt32(8, _item_enchant);
		}
		if (has_bless_code()) {
			output.wirteInt32(9, _bless_code);
		}
		if (has_item_used_immediately()) {
			output.writeBool(10, _item_used_immediately);
		}
		if (has_item_icon()) {
			output.wirteInt32(11, _item_icon);
		}
		if (has_item_desc()) {
			output.writeString(12, _item_desc);
		}
		if (has_item_extra_desc()) {
			output.writeBytes(13, _item_extra_desc);
		}
		if (has_subject()) {
			output.writeString(14, _subject);
		}
		if (has_text()) {
			output.writeString(15, _text);
		}
		if (has_event_push_status()) {
			output.wirteInt32(16, _event_push_status);
		}
		if (has_web_url()) {
			output.writeString(17, _web_url);
		}
		if (has_image_id()) {
			output.wirteInt32(18, _image_id);
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
					set_event_push_id(input.readInt64());
					break;
				}
				case 0x00000010: {
					set_reset_num(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_remain_time(input.readUInt32());
					break;
				}
				case 0x00000020: {
					set_expire_date(input.readInt64());
					break;
				}
				case 0x00000028: {
					set_enable_date(input.readInt64());
					break;
				}
				case 0x00000030: {
					set_item_name_id(input.readInt32());
					break;
				}
				case 0x00000038: {
					set_item_amount(input.readInt32());
					break;
				}
				case 0x00000040: {
					set_item_enchant(input.readInt32());
					break;
				}
				case 0x00000048: {
					set_bless_code(input.readInt32());
					break;
				}
				case 0x00000050: {
					set_item_used_immediately(input.readBool());
					break;
				}
				case 0x00000058: {
					set_item_icon(input.readInt32());
					break;
				}
				case 0x00000062: {
					set_item_desc(input.readString());
					break;
				}
				case 0x0000006A: {
					set_item_extra_desc(input.readBytes());
					break;
				}
				case 0x00000072: {
					set_subject(input.readString());
					break;
				}
				case 0x0000007A: {
					set_text(input.readString());
					break;
				}
				case 0x00000080: {
					set_event_push_status(input.readInt32());
					break;
				}
				case 0x0000008A: {
					set_web_url(input.readString());
					break;
				}
				case 0x00000090: {
					set_image_id(input.readInt32());
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
		return new EventPushInfo();
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
