package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class ATTENDANCE_RANDOM implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static ATTENDANCE_RANDOM newInstance() {
		return new ATTENDANCE_RANDOM();
	}

	private int _refresh_count;
	private java.util.LinkedList<Integer> _random_item;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private ATTENDANCE_RANDOM() {
	}

	public int get_refresh_count() {
		return _refresh_count;
	}

	public void set_refresh_count(int val) {
		_bit |= 0x1;
		_refresh_count = val;
	}

	public boolean has_refresh_count() {
		return (_bit & 0x1) == 0x1;
	}

	public java.util.LinkedList<Integer> get_random_item() {
		return _random_item;
	}

	public void add_random_item(int val) {
		if (!has_random_item()) {
			_random_item = new java.util.LinkedList<Integer>();
			_bit |= 0x2;
		}
		_random_item.add(val);
	}

	public boolean has_random_item() {
		return (_bit & 0x2) == 0x2;
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
		if (has_refresh_count()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _refresh_count);
		}
		if (has_random_item()) {
			for (int val : _random_item) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_refresh_count()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		// if (has_random_item()){
		// for(int val : _random_item){
		// if (!val.isInitialized()){
		// _memorizedIsInitialized = -1;
		// return false;
		// }
		// }
		// }
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_refresh_count()) {
			output.wirteInt32(1, _refresh_count);
		}
		if (has_random_item()) {
			for (int val : _random_item) {
				output.wirteInt32(2, val);
			}
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
					set_refresh_count(input.readInt32());
					break;
				}
				case 0x00000010: {
					add_random_item(input.readInt32());
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
		return new ATTENDANCE_RANDOM();
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
