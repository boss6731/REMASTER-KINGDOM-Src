package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class SC_QUEST_FINISH_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_QUEST_FINISH_NOTI newInstance() {
		return new SC_QUEST_FINISH_NOTI();
	}

	private int _id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_QUEST_FINISH_NOTI() {
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int val) {
		_bit |= 0x1;
		_id = val;
	}

	public boolean has_id() {
		return (_bit & 0x1) == 0x1;
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
		if (has_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _id);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_id()) {
			output.writeUInt32(1, _id);
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
					set_id(input.readUInt32());
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
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Lenz.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_QUEST_FINISH_NOTI();
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
