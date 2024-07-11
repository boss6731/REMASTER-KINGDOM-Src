package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class QuestProgress implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static QuestProgress newInstance() {
		return new QuestProgress();
	}

	private int _id;
	private long _start_time;
	private long _finish_time;
	private java.util.LinkedList<ObjectiveProgress> _objectives;
	private boolean _is_shown_in_quest_window;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private QuestProgress() {
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

	public long get_start_time() {
		return _start_time;
	}

	public void set_start_time(long val) {
		_bit |= 0x2;
		_start_time = val;
	}

	public boolean has_start_time() {
		return (_bit & 0x2) == 0x2;
	}

	public long get_finish_time() {
		return _finish_time;
	}

	public void set_finish_time(long val) {
		_bit |= 0x4;
		_finish_time = val;
	}

	public boolean has_finish_time() {
		return (_bit & 0x4) == 0x4;
	}

	public java.util.LinkedList<ObjectiveProgress> get_objectives() {
		return _objectives;
	}

	public void add_objectives(ObjectiveProgress val) {
		if (!has_objectives()) {
			_objectives = new java.util.LinkedList<ObjectiveProgress>();
			_bit |= 0x8;
		}
		_objectives.add(val);
	}

	public boolean has_objectives() {
		return (_bit & 0x8) == 0x8;
	}

	public boolean get_is_shown_in_quest_window() {
		return _is_shown_in_quest_window;
	}

	public void set_is_shown_in_quest_window(boolean val) {
		_bit |= 0x10;
		_is_shown_in_quest_window = val;
	}

	public boolean has_is_shown_in_quest_window() {
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
		if (has_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _id);
		if (has_start_time())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(2, _start_time);
		if (has_finish_time())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(3, _finish_time);
		if (has_objectives()) {
			for (ObjectiveProgress val : _objectives)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
		}
		if (has_is_shown_in_quest_window())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(5, _is_shown_in_quest_window);
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
		if (!has_start_time()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_objectives()) {
			for (ObjectiveProgress val : _objectives) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_id()) {
			output.writeUInt32(1, _id);
		}
		if (has_start_time()) {
			output.writeInt64(2, _start_time);
		}
		if (has_finish_time()) {
			output.writeInt64(3, _finish_time);
		}
		if (has_objectives()) {
			for (ObjectiveProgress val : _objectives) {
				output.writeMessage(4, val);
			}
		}
		if (has_is_shown_in_quest_window()) {
			output.writeBool(5, _is_shown_in_quest_window);
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
					set_start_time(input.readInt64());
					break;
				}
				case 0x00000018: {
					set_finish_time(input.readInt64());
					break;
				}
				case 0x00000022: {
					add_objectives((ObjectiveProgress) input.readMessage(ObjectiveProgress.newInstance()));
					break;
				}
				case 0x00000028: {
					set_is_shown_in_quest_window(input.readBool());
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
		return new QuestProgress();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_objectives()) {
			for (ObjectiveProgress val : _objectives)
				val.dispose();
			_objectives.clear();
			_objectives = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	public static class ObjectiveProgress implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static ObjectiveProgress newInstance() {
			return new ObjectiveProgress();
		}

		private int _id;
		private int _quantity;
		private int _required_quantity;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private ObjectiveProgress() {
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

		public int get_quantity() {
			return _quantity;
		}

		public void set_quantity(int val) {
			_bit |= 0x2;
			_quantity = val;
		}

		public boolean has_quantity() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_required_quantity() {
			return _required_quantity;
		}

		public void set_required_quantity(int val) {
			_bit |= 0x4;
			_required_quantity = val;
		}

		public boolean has_required_quantity() {
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
			if (has_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _id);
			if (has_quantity())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _quantity);
			if (has_required_quantity())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _required_quantity);
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
			if (has_quantity()) {
				output.writeUInt32(2, _quantity);
			}
			if (has_required_quantity()) {
				output.writeUInt32(3, _required_quantity);
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
						set_quantity(input.readUInt32());
						break;
					}
					case 0x00000018: {
						set_required_quantity(input.readUInt32());
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
			return new ObjectiveProgress();
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
}
