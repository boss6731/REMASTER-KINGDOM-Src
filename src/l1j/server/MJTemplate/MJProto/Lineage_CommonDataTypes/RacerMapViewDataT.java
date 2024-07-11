package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

//TODO：自動產生 PROTO 程式碼。倫茨製造。
public class RacerMapViewDataT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static RacerMapViewDataT newInstance() {
		return new RacerMapViewDataT();
	}

	private RacerInfoT _racerInfo;
	private String _name;
	private int _condition;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private RacerMapViewDataT() {
	}

	public RacerInfoT get_racerInfo() {
		return _racerInfo;
	}

	public void set_racerInfo(RacerInfoT val) {
		_bit |= 0x1;
		_racerInfo = val;
	}

	public boolean has_racerInfo() {
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
		if (has_racerInfo())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _racerInfo);
		if (has_name())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _name);
		if (has_condition())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _condition);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_racerInfo()) {
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
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_racerInfo()) {
			output.writeMessage(1, _racerInfo);
		}
		if (has_name()) {
			output.writeString(2, _name);
		}
		if (has_condition()) {
			output.wirteInt32(3, _condition);
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
				case 0x0000000A: {
					set_racerInfo((RacerInfoT) input.readMessage(RacerInfoT.newInstance()));
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
		return new RacerMapViewDataT();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_racerInfo() && _racerInfo != null) {
			_racerInfo.dispose();
			_racerInfo = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
