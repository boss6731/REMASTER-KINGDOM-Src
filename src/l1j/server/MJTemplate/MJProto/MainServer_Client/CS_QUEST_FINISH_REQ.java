package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.Beginner.Controller.MJBeginnerControllerProvider;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class CS_QUEST_FINISH_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_QUEST_FINISH_REQ newInstance() {
		return new CS_QUEST_FINISH_REQ();
	}

	private int _id;
	private java.util.LinkedList<Integer> _optional_reward_indexes;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_QUEST_FINISH_REQ() {
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

	public java.util.LinkedList<Integer> get_optional_reward_indexes() {
		return _optional_reward_indexes;
	}

	public void add_optional_reward_indexes(int val) {
		if (!has_optional_reward_indexes()) {
			_optional_reward_indexes = new java.util.LinkedList<Integer>();
			_bit |= 0x2;
		}
		_optional_reward_indexes.add(val);
	}

	public boolean has_optional_reward_indexes() {
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
		if (has_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _id);
		if (has_optional_reward_indexes()) {
			for (int val : _optional_reward_indexes)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, val);
		}
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
		if (has_optional_reward_indexes()) {
			for (int val : _optional_reward_indexes) {
				output.writeUInt32(2, val);
			}
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
				case 0x00000010: {
					add_optional_reward_indexes(input.readUInt32());
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
			L1PcInstance pc = clnt.getActiveChar();
			if (pc == null) {
				return this;
			}
			MJBeginnerControllerProvider.provider().clientController().onFinished(pc, get_id(),
					get_optional_reward_indexes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new CS_QUEST_FINISH_REQ();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_optional_reward_indexes()) {
			_optional_reward_indexes.clear();
			_optional_reward_indexes = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
