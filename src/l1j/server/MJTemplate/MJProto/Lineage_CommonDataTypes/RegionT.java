package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

//TODO：自動產生 PROTO 程式碼。倫茨製造。
public class RegionT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static RegionT newInstance() {
		return new RegionT();
	}

	private int _OriginalMapID;
	private int _SX;
	private int _SY;
	private int _EX;
	private int _EY;
	private int _MapID;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private RegionT() {
		set_SX(0);
		set_SY(0);
		set_EX(0);
		set_EY(0);
	}

	public int get_OriginalMapID() {
		return _OriginalMapID;
	}

	public void set_OriginalMapID(int val) {
		_bit |= 0x1;
		_OriginalMapID = val;
	}

	public boolean has_OriginalMapID() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_SX() {
		return _SX;
	}

	public void set_SX(int val) {
		_bit |= 0x2;
		_SX = val;
	}

	public boolean has_SX() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_SY() {
		return _SY;
	}

	public void set_SY(int val) {
		_bit |= 0x4;
		_SY = val;
	}

	public boolean has_SY() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_EX() {
		return _EX;
	}

	public void set_EX(int val) {
		_bit |= 0x8;
		_EX = val;
	}

	public boolean has_EX() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_EY() {
		return _EY;
	}

	public void set_EY(int val) {
		_bit |= 0x10;
		_EY = val;
	}

	public boolean has_EY() {
		return (_bit & 0x10) == 0x10;
	}

	public int get_MapID() {
		return _MapID;
	}

	public void set_MapID(int val) {
		_bit |= 0x200;
		_MapID = val;
	}

	public boolean has_MapID() {
		return (_bit & 0x200) == 0x200;
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
		if (has_OriginalMapID())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _OriginalMapID);
		if (has_SX())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _SX);
		if (has_SY())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _SY);
		if (has_EX())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _EX);
		if (has_EY())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _EY);
		if (has_MapID())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(10, _MapID);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_OriginalMapID()) {
			output.writeUInt32(1, _OriginalMapID);
		}
		if (has_SX()) {
			output.writeUInt32(2, _SX);
		}
		if (has_SY()) {
			output.writeUInt32(3, _SY);
		}
		if (has_EX()) {
			output.writeUInt32(4, _EX);
		}
		if (has_EY()) {
			output.writeUInt32(5, _EY);
		}
		if (has_MapID()) {
			output.writeUInt32(10, _MapID);
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
					set_OriginalMapID(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_SX(input.readUInt32());
					break;
				}
				case 0x00000018: {
					set_SY(input.readUInt32());
					break;
				}
				case 0x00000020: {
					set_EX(input.readUInt32());
					break;
				}
				case 0x00000028: {
					set_EY(input.readUInt32());
					break;
				}
				case 0x00000050: {
					set_MapID(input.readUInt32());
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
			// TODO：從下面插入處理程式碼。倫茨製造。

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new RegionT();
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
