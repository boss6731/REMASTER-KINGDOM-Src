package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

//TODO：自動產生 PROTO 程式碼。倫茨製造。
public class RacerInfoT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static RacerInfoT newInstance() {
		return new RacerInfoT();
	}

	private int _racerId;
	private int _x;
	private int _y;
	private int _dir;
	private int _laneId;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private RacerInfoT() {
	}

	public int get_racerId() {
		return _racerId;
	}

	public void set_racerId(int val) {
		_bit |= 0x1;
		_racerId = val;
	}

	public boolean has_racerId() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_x() {
		return _x;
	}

	public void set_x(int val) {
		_bit |= 0x2;
		_x = val;
	}

	public boolean has_x() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_y() {
		return _y;
	}

	public void set_y(int val) {
		_bit |= 0x4;
		_y = val;
	}

	public boolean has_y() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_dir() {
		return _dir;
	}

	public void set_dir(int val) {
		_bit |= 0x8;
		_dir = val;
	}

	public boolean has_dir() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_laneId() {
		return _laneId;
	}

	public void set_laneId(int val) {
		_bit |= 0x10;
		_laneId = val;
	}

	public boolean has_laneId() {
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
		if (has_racerId())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _racerId);
		if (has_x())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _x);
		if (has_y())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _y);
		if (has_dir())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _dir);
		if (has_laneId())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _laneId);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_racerId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_x()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_y()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_dir()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_laneId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_racerId()) {
			output.wirteInt32(1, _racerId);
		}
		if (has_x()) {
			output.wirteInt32(2, _x);
		}
		if (has_y()) {
			output.wirteInt32(3, _y);
		}
		if (has_dir()) {
			output.wirteInt32(4, _dir);
		}
		if (has_laneId()) {
			output.wirteInt32(5, _laneId);
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
					set_racerId(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_x(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_y(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_dir(input.readInt32());
					break;
				}
				case 0x00000028: {
					set_laneId(input.readInt32());
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
		return new RacerInfoT();
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
