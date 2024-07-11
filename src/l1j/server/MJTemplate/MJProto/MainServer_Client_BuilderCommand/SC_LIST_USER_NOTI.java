package l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_LIST_USER_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_LIST_USER_NOTI newInstance() {
		return new SC_LIST_USER_NOTI();
	}

	private java.util.LinkedList<SC_LIST_USER_NOTI.Info> _infos;
	private boolean _begin;
	private boolean _last;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_LIST_USER_NOTI() {
	}

	public java.util.LinkedList<SC_LIST_USER_NOTI.Info> get_infos() {
		return _infos;
	}

	public void add_infos(SC_LIST_USER_NOTI.Info val) {
		if (!has_infos()) {
			_infos = new java.util.LinkedList<SC_LIST_USER_NOTI.Info>();
			_bit |= 0x1;
		}
		_infos.add(val);
	}

	public boolean has_infos() {
		return (_bit & 0x1) == 0x1;
	}

	public boolean get_begin() {
		return _begin;
	}

	public void set_begin(boolean val) {
		_bit |= 0x2;
		_begin = val;
	}

	public boolean has_begin() {
		return (_bit & 0x2) == 0x2;
	}

	public boolean get_last() {
		return _last;
	}

	public void set_last(boolean val) {
		_bit |= 0x4;
		_last = val;
	}

	public boolean has_last() {
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
		if (has_infos()) {
			for (SC_LIST_USER_NOTI.Info val : _infos) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_begin()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _begin);
		}
		if (has_last()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _last);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_infos()) {
			for (SC_LIST_USER_NOTI.Info val : _infos) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_begin()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_last()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_infos()) {
			for (SC_LIST_USER_NOTI.Info val : _infos) {
				output.writeMessage(1, val);
			}
		}
		if (has_begin()) {
			output.writeBool(2, _begin);
		}
		if (has_last()) {
			output.writeBool(3, _last);
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
					add_infos((SC_LIST_USER_NOTI.Info) input.readMessage(SC_LIST_USER_NOTI.Info.newInstance()));
					break;
				}
				case 0x00000010: {
					set_begin(input.readBool());
					break;
				}
				case 0x00000018: {
					set_last(input.readBool());
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new SC_LIST_USER_NOTI();
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

	public static class Info implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static Info newInstance() {
			return new Info();
		}

		private byte[] _name;
		private int _level;
		private int _game_class;
		private byte[] _account;
		private int _ip;
		private byte[] _pledge;
		private int _serverId;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private Info() {
		}

		public byte[] get_name() {
			return _name;
		}

		public void set_name(byte[] val) {
			_bit |= 0x1;
			_name = val;
		}

		public boolean has_name() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_level() {
			return _level;
		}

		public void set_level(int val) {
			_bit |= 0x2;
			_level = val;
		}

		public boolean has_level() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_game_class() {
			return _game_class;
		}

		public void set_game_class(int val) {
			_bit |= 0x4;
			_game_class = val;
		}

		public boolean has_game_class() {
			return (_bit & 0x4) == 0x4;
		}

		public byte[] get_account() {
			return _account;
		}

		public void set_account(byte[] val) {
			_bit |= 0x8;
			_account = val;
		}

		public boolean has_account() {
			return (_bit & 0x8) == 0x8;
		}

		public int get_ip() {
			return _ip;
		}

		public void set_ip(int val) {
			_bit |= 0x10;
			_ip = val;
		}

		public boolean has_ip() {
			return (_bit & 0x10) == 0x10;
		}

		public byte[] get_pledge() {
			return _pledge;
		}

		public void set_pledge(byte[] val) {
			_bit |= 0x20;
			_pledge = val;
		}

		public boolean has_pledge() {
			return (_bit & 0x20) == 0x20;
		}

		public int get_serverId() {
			return _serverId;
		}

		public void set_serverId(int val) {
			_bit |= 0x40;
			_serverId = val;
		}

		public boolean has_serverId() {
			return (_bit & 0x40) == 0x40;
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
			if (has_name()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(1, _name);
			}
			if (has_level()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _level);
			}
			if (has_game_class()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _game_class);
			}
			if (has_account()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(4, _account);
			}
			if (has_ip()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _ip);
			}
			if (has_pledge()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(6, _pledge);
			}
			if (has_serverId()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _serverId);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_name()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_level()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_game_class()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_account()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_ip()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_pledge()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_name()) {
				output.writeBytes(1, _name);
			}
			if (has_level()) {
				output.writeUInt32(2, _level);
			}
			if (has_game_class()) {
				output.writeUInt32(3, _game_class);
			}
			if (has_account()) {
				output.writeBytes(4, _account);
			}
			if (has_ip()) {
				output.writeUInt32(5, _ip);
			}
			if (has_pledge()) {
				output.writeBytes(6, _pledge);
			}
			if (has_serverId()) {
				output.wirteInt32(7, _serverId);
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
						set_name(input.readBytes());
						break;
					}
					case 0x00000010: {
						set_level(input.readUInt32());
						break;
					}
					case 0x00000018: {
						set_game_class(input.readUInt32());
						break;
					}
					case 0x00000022: {
						set_account(input.readBytes());
						break;
					}
					case 0x00000028: {
						set_ip(input.readUInt32());
						break;
					}
					case 0x00000032: {
						set_pledge(input.readBytes());
						break;
					}
					case 0x00000038: {
						set_serverId(input.readInt32());
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

				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new Info();
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
