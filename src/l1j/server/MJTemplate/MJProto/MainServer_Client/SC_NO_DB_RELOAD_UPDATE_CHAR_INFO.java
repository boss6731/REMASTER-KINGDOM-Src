package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_NO_DB_RELOAD_UPDATE_CHAR_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_NO_DB_RELOAD_UPDATE_CHAR_INFO newInstance() {
		return new SC_NO_DB_RELOAD_UPDATE_CHAR_INFO();
	}

	private java.util.LinkedList<SC_NO_DB_RELOAD_UPDATE_CHAR_INFO.UpdateDeleteInfo> _updateDeleteInfo;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_NO_DB_RELOAD_UPDATE_CHAR_INFO() {
	}

	public java.util.LinkedList<SC_NO_DB_RELOAD_UPDATE_CHAR_INFO.UpdateDeleteInfo> get_updateDeleteInfo() {
		return _updateDeleteInfo;
	}

	public void add_updateDeleteInfo(SC_NO_DB_RELOAD_UPDATE_CHAR_INFO.UpdateDeleteInfo val) {
		if (!has_updateDeleteInfo()) {
			_updateDeleteInfo = new java.util.LinkedList<SC_NO_DB_RELOAD_UPDATE_CHAR_INFO.UpdateDeleteInfo>();
			_bit |= 0x1;
		}
		_updateDeleteInfo.add(val);
	}

	public boolean has_updateDeleteInfo() {
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
		if (has_updateDeleteInfo()) {
			for (SC_NO_DB_RELOAD_UPDATE_CHAR_INFO.UpdateDeleteInfo val : _updateDeleteInfo) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_updateDeleteInfo()) {
			for (SC_NO_DB_RELOAD_UPDATE_CHAR_INFO.UpdateDeleteInfo val : _updateDeleteInfo) {
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
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_updateDeleteInfo()) {
			for (SC_NO_DB_RELOAD_UPDATE_CHAR_INFO.UpdateDeleteInfo val : _updateDeleteInfo) {
				output.writeMessage(1, val);
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
				case 0x0000000A: {
					add_updateDeleteInfo((SC_NO_DB_RELOAD_UPDATE_CHAR_INFO.UpdateDeleteInfo) input
							.readMessage(SC_NO_DB_RELOAD_UPDATE_CHAR_INFO.UpdateDeleteInfo.newInstance()));
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
		return new SC_NO_DB_RELOAD_UPDATE_CHAR_INFO();
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

	public static class UpdateDeleteInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static UpdateDeleteInfo newInstance() {
			return new UpdateDeleteInfo();
		}

		private int _slot_id;
		private int _delete_time;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private UpdateDeleteInfo() {
		}

		public int get_slot_id() {
			return _slot_id;
		}

		public void set_slot_id(int val) {
			_bit |= 0x1;
			_slot_id = val;
		}

		public boolean has_slot_id() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_delete_time() {
			return _delete_time;
		}

		public void set_delete_time(int val) {
			_bit |= 0x2;
			_delete_time = val;
		}

		public boolean has_delete_time() {
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
			if (has_slot_id()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _slot_id);
			}
			if (has_delete_time()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _delete_time);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_slot_id()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_slot_id()) {
				output.writeUInt32(1, _slot_id);
			}
			if (has_delete_time()) {
				output.writeUInt32(2, _delete_time);
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
						set_slot_id(input.readUInt32());
						break;
					}
					case 0x00000010: {
						set_delete_time(input.readUInt32());
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
			return new UpdateDeleteInfo();
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
}
