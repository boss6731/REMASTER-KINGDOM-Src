package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

//TODO：自動產生 PROTO 程式碼。倫茨製造。
public class RacerTicketT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static RacerTicketT newInstance() {
		return new RacerTicketT();
	}

	private int _laneId;
	private String _name;
	private int _condition;
	private double _winRate;
	private int _price;
	private int _racerId;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private RacerTicketT() {
	}

	public int get_laneId() {
		return _laneId;
	}

	public void set_laneId(int val) {
		_bit |= 0x1;
		_laneId = val;
	}

	public boolean has_laneId() {
		return (_bit & 0x1) == 0x1;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String val) {
		_bit |= 0x2;
		_name = val;
	}

	public boolean has_name() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_condition() {
		return _condition;
	}

	public void set_condition(int val) {
		_bit |= 0x4;
		_condition = val;
	}

	public boolean has_condition() {
		return (_bit & 0x4) == 0x4;
	}

	public double get_winRate() {
		return _winRate;
	}

	public void set_winRate(double val) {
		_bit |= 0x8;
		_winRate = val;
	}

	public boolean has_winRate() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_price() {
		return _price;
	}

	public void set_price(int val) {
		_bit |= 0x10;
		_price = val;
	}

	public boolean has_price() {
		return (_bit & 0x10) == 0x10;
	}

	public int get_racerId() {
		return _racerId;
	}

	public void set_racerId(int val) {
		_bit |= 0x20;
		_racerId = val;
	}

	public boolean has_racerId() {
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
		if (has_laneId())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _laneId);
		if (has_name())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _name);
		if (has_condition())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _condition);
		if (has_winRate())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeDoubleSize(4, _winRate);
		if (has_price())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _price);
		if (has_racerId())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _racerId);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_laneId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_name()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_condition()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_winRate()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_price()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_racerId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_laneId()) {
			output.wirteInt32(1, _laneId);
		}
		if (has_name()) {
			output.writeString(2, _name);
		}
		if (has_condition()) {
			output.wirteInt32(3, _condition);
		}
		if (has_winRate()) {
			output.writeDouble(4, _winRate);
		}
		if (has_price()) {
			output.wirteInt32(5, _price);
		}
		if (has_racerId()) {
			output.wirteInt32(6, _racerId);
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
					set_laneId(input.readInt32());
					break;
				}
				case 0x00000012: {
					set_name(input.readString());
					break;
				}
				case 0x00000018: {
					set_condition(input.readInt32());
					break;
				}
				case 0x00000021: {
					set_winRate(input.readDouble());
					break;
				}
				case 0x00000028: {
					set_price(input.readInt32());
					break;
				}
				case 0x00000030: {
					set_racerId(input.readInt32());
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
		return new RacerTicketT();
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
