package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

//TODO：自動產生 PROTO 程式碼。倫茨製造。
public class RaceInfoT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static RaceInfoT newInstance() {
		return new RaceInfoT();
	}

	private int _worldNumber;
	private int _raceNumber;
	private int _raceKind;
	private int _trackId;
	private int _bookMakerNpcId;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private RaceInfoT() {
	}

	public int get_worldNumber() {
		return _worldNumber;
	}

	public void set_worldNumber(int val) {
		_bit |= 0x1;
		_worldNumber = val;
	}

	public boolean has_worldNumber() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_raceNumber() {
		return _raceNumber;
	}

	public void set_raceNumber(int val) {
		_bit |= 0x2;
		_raceNumber = val;
	}

	public boolean has_raceNumber() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_raceKind() {
		return _raceKind;
	}

	public void set_raceKind(int val) {
		_bit |= 0x4;
		_raceKind = val;
	}

	public boolean has_raceKind() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_trackId() {
		return _trackId;
	}

	public void set_trackId(int val) {
		_bit |= 0x8;
		_trackId = val;
	}

	public boolean has_trackId() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_bookMakerNpcId() {
		return _bookMakerNpcId;
	}

	public void set_bookMakerNpcId(int val) {
		_bit |= 0x10;
		_bookMakerNpcId = val;
	}

	public boolean has_bookMakerNpcId() {
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
		if (has_worldNumber())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _worldNumber);
		if (has_raceNumber())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _raceNumber);
		if (has_raceKind())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _raceKind);
		if (has_trackId())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _trackId);
		if (has_bookMakerNpcId())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _bookMakerNpcId);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_worldNumber()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_raceNumber()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_raceKind()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_trackId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_bookMakerNpcId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_worldNumber()) {
			output.wirteInt32(1, _worldNumber);
		}
		if (has_raceNumber()) {
			output.wirteInt32(2, _raceNumber);
		}
		if (has_raceKind()) {
			output.wirteInt32(3, _raceKind);
		}
		if (has_trackId()) {
			output.wirteInt32(4, _trackId);
		}
		if (has_bookMakerNpcId()) {
			output.wirteInt32(5, _bookMakerNpcId);
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
					set_worldNumber(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_raceNumber(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_raceKind(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_trackId(input.readInt32());
					break;
				}
				case 0x00000028: {
					set_bookMakerNpcId(input.readInt32());
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
		return new RaceInfoT();
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
