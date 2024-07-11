package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

//TODO：自動產生 PROTO 程式碼。倫茨製造。
public class RaceRankInfoT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static RaceRankInfoT newInstance() {
		return new RaceRankInfoT();
	}

	private int _rank;
	private int _serverNumber;
	private String _name;
	private int _weeklyPrize;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private RaceRankInfoT() {
	}

	public int get_rank() {
		return _rank;
	}

	public void set_rank(int val) {
		_bit |= 0x1;
		_rank = val;
	}

	public boolean has_rank() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_serverNumber() {
		return _serverNumber;
	}

	public void set_serverNumber(int val) {
		_bit |= 0x2;
		_serverNumber = val;
	}

	public boolean has_serverNumber() {
		return (_bit & 0x2) == 0x2;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String val) {
		_bit |= 0x4;
		_name = val;
	}

	public boolean has_name() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_weeklyPrize() {
		return _weeklyPrize;
	}

	public void set_weeklyPrize(int val) {
		_bit |= 0x8;
		_weeklyPrize = val;
	}

	public boolean has_weeklyPrize() {
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
		if (has_rank())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _rank);
		if (has_serverNumber())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _serverNumber);
		if (has_name())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _name);
		if (has_weeklyPrize())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _weeklyPrize);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_rank()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_serverNumber()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_name()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_weeklyPrize()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_rank()) {
			output.wirteInt32(1, _rank);
		}
		if (has_serverNumber()) {
			output.wirteInt32(2, _serverNumber);
		}
		if (has_name()) {
			output.writeString(3, _name);
		}
		if (has_weeklyPrize()) {
			output.writeUInt32(4, _weeklyPrize);
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
					set_rank(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_serverNumber(input.readInt32());
					break;
				}
				case 0x0000001A: {
					set_name(input.readString());
					break;
				}
				case 0x00000020: {
					set_weeklyPrize(input.readUInt32());
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
		return new RaceRankInfoT();
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
