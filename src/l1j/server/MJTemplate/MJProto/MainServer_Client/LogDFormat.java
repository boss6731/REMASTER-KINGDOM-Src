package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class LogDFormat implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static LogDFormat newInstance() {
		return new LogDFormat();
	}

	private int _log_detail_code;
	private long _data1_num;
	private long _data2_num;
	private long _data3_num;
	private long _data4_num;
	private long _data5_num;
	private long _data6_num;
	private long _data7_num;
	private long _data8_num;
	private String _data1_str;
	private String _data2_str;
	private String _data3_str;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private LogDFormat() {
	}

	public int get_log_detail_code() {
		return _log_detail_code;
	}

	public void set_log_detail_code(int val) {
		_bit |= 0x1;
		_log_detail_code = val;
	}

	public boolean has_log_detail_code() {
		return (_bit & 0x1) == 0x1;
	}

	public long get_data1_num() {
		return _data1_num;
	}

	public void set_data1_num(long val) {
		_bit |= 0x2;
		_data1_num = val;
	}

	public boolean has_data1_num() {
		return (_bit & 0x2) == 0x2;
	}

	public long get_data2_num() {
		return _data2_num;
	}

	public void set_data2_num(long val) {
		_bit |= 0x4;
		_data2_num = val;
	}

	public boolean has_data2_num() {
		return (_bit & 0x4) == 0x4;
	}

	public long get_data3_num() {
		return _data3_num;
	}

	public void set_data3_num(long val) {
		_bit |= 0x8;
		_data3_num = val;
	}

	public boolean has_data3_num() {
		return (_bit & 0x8) == 0x8;
	}

	public long get_data4_num() {
		return _data4_num;
	}

	public void set_data4_num(long val) {
		_bit |= 0x10;
		_data4_num = val;
	}

	public boolean has_data4_num() {
		return (_bit & 0x10) == 0x10;
	}

	public long get_data5_num() {
		return _data5_num;
	}

	public void set_data5_num(long val) {
		_bit |= 0x20;
		_data5_num = val;
	}

	public boolean has_data5_num() {
		return (_bit & 0x20) == 0x20;
	}

	public long get_data6_num() {
		return _data6_num;
	}

	public void set_data6_num(long val) {
		_bit |= 0x40;
		_data6_num = val;
	}

	public boolean has_data6_num() {
		return (_bit & 0x40) == 0x40;
	}

	public long get_data7_num() {
		return _data7_num;
	}

	public void set_data7_num(long val) {
		_bit |= 0x80;
		_data7_num = val;
	}

	public boolean has_data7_num() {
		return (_bit & 0x80) == 0x80;
	}

	public long get_data8_num() {
		return _data8_num;
	}

	public void set_data8_num(long val) {
		_bit |= 0x100;
		_data8_num = val;
	}

	public boolean has_data8_num() {
		return (_bit & 0x100) == 0x100;
	}

	public String get_data1_str() {
		return _data1_str;
	}

	public void set_data1_str(String val) {
		_bit |= 0x200;
		_data1_str = val;
	}

	public boolean has_data1_str() {
		return (_bit & 0x200) == 0x200;
	}

	public String get_data2_str() {
		return _data2_str;
	}

	public void set_data2_str(String val) {
		_bit |= 0x400;
		_data2_str = val;
	}

	public boolean has_data2_str() {
		return (_bit & 0x400) == 0x400;
	}

	public String get_data3_str() {
		return _data3_str;
	}

	public void set_data3_str(String val) {
		_bit |= 0x800;
		_data3_str = val;
	}

	public boolean has_data3_str() {
		return (_bit & 0x800) == 0x800;
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
		if (has_log_detail_code())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _log_detail_code);
		if (has_data1_num())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(2, _data1_num);
		if (has_data2_num())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(3, _data2_num);
		if (has_data3_num())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(4, _data3_num);
		if (has_data4_num())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(5, _data4_num);
		if (has_data5_num())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(6, _data5_num);
		if (has_data6_num())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(7, _data6_num);
		if (has_data7_num())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(8, _data7_num);
		if (has_data8_num())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(9, _data8_num);
		if (has_data1_str())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(10, _data1_str);
		if (has_data2_str())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(11, _data2_str);
		if (has_data3_str())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(12, _data3_str);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_log_detail_code()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_log_detail_code()) {
			output.wirteInt32(1, _log_detail_code);
		}
		if (has_data1_num()) {
			output.writeInt64(2, _data1_num);
		}
		if (has_data2_num()) {
			output.writeInt64(3, _data2_num);
		}
		if (has_data3_num()) {
			output.writeInt64(4, _data3_num);
		}
		if (has_data4_num()) {
			output.writeInt64(5, _data4_num);
		}
		if (has_data5_num()) {
			output.writeInt64(6, _data5_num);
		}
		if (has_data6_num()) {
			output.writeInt64(7, _data6_num);
		}
		if (has_data7_num()) {
			output.writeInt64(8, _data7_num);
		}
		if (has_data8_num()) {
			output.writeInt64(9, _data8_num);
		}
		if (has_data1_str()) {
			output.writeString(10, _data1_str);
		}
		if (has_data2_str()) {
			output.writeString(11, _data2_str);
		}
		if (has_data3_str()) {
			output.writeString(12, _data3_str);
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
				case 0x00000008: {
					set_log_detail_code(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_data1_num(input.readInt64());
					break;
				}
				case 0x00000018: {
					set_data2_num(input.readInt64());
					break;
				}
				case 0x00000020: {
					set_data3_num(input.readInt64());
					break;
				}
				case 0x00000028: {
					set_data4_num(input.readInt64());
					break;
				}
				case 0x00000030: {
					set_data5_num(input.readInt64());
					break;
				}
				case 0x00000038: {
					set_data6_num(input.readInt64());
					break;
				}
				case 0x00000040: {
					set_data7_num(input.readInt64());
					break;
				}
				case 0x00000048: {
					set_data8_num(input.readInt64());
					break;
				}
				case 0x00000052: {
					set_data1_str(input.readString());
					break;
				}
				case 0x0000005A: {
					set_data2_str(input.readString());
					break;
				}
				case 0x00000062: {
					set_data3_str(input.readString());
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
		return new LogDFormat();
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
