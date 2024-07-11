package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_WHOUSER_NOTI_PACKET implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_WHOUSER_NOTI_PACKET newInstance() {
		return new SC_WHOUSER_NOTI_PACKET();
	}

	private java.util.LinkedList<SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO> _whouserinfo;
	private int _currentusercount;
	private java.util.LinkedList<SC_WHOUSER_NOTI_PACKET.HuntingMapInfo> _hunting_map_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_WHOUSER_NOTI_PACKET() {
	}

	public java.util.LinkedList<SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO> get_whouserinfo() {
		return _whouserinfo;
	}

	public void add_whouserinfo(SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO val) {
		if (!has_whouserinfo()) {
			_whouserinfo = new java.util.LinkedList<SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO>();
			_bit |= 0x1;
		}
		_whouserinfo.add(val);
	}

	public boolean has_whouserinfo() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_currentusercount() {
		return _currentusercount;
	}

	public void set_currentusercount(int val) {
		_bit |= 0x2;
		_currentusercount = val;
	}

	public boolean has_currentusercount() {
		return (_bit & 0x2) == 0x2;
	}

	public java.util.LinkedList<SC_WHOUSER_NOTI_PACKET.HuntingMapInfo> get_hunting_map_info() {
		return _hunting_map_info;
	}

	public void add_hunting_map_info(SC_WHOUSER_NOTI_PACKET.HuntingMapInfo val) {
		if (!has_hunting_map_info()) {
			_hunting_map_info = new java.util.LinkedList<SC_WHOUSER_NOTI_PACKET.HuntingMapInfo>();
			_bit |= 0x4;
		}
		_hunting_map_info.add(val);
	}

	public boolean has_hunting_map_info() {
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
		if (has_whouserinfo()) {
			for (SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO val : _whouserinfo) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_currentusercount()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _currentusercount);
		}
		if (has_hunting_map_info()) {
			for (SC_WHOUSER_NOTI_PACKET.HuntingMapInfo val : _hunting_map_info) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_whouserinfo()) {
			for (SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO val : _whouserinfo) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_hunting_map_info()) {
			for (SC_WHOUSER_NOTI_PACKET.HuntingMapInfo val : _hunting_map_info) {
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
		if (has_whouserinfo()) {
			for (SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO val : _whouserinfo) {
				output.writeMessage(1, val);
			}
		}
		if (has_currentusercount()) {
			output.wirteInt32(2, _currentusercount);
		}
		if (has_hunting_map_info()) {
			for (SC_WHOUSER_NOTI_PACKET.HuntingMapInfo val : _hunting_map_info) {
				output.writeMessage(3, val);
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
					add_whouserinfo((SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO) input
							.readMessage(SC_WHOUSER_NOTI_PACKET.WHOUSER_INFO.newInstance()));
					break;
				}
				case 0x00000010: {
					set_currentusercount(input.readInt32());
					break;
				}
				case 0x0000001A: {
					add_hunting_map_info((SC_WHOUSER_NOTI_PACKET.HuntingMapInfo) input
							.readMessage(SC_WHOUSER_NOTI_PACKET.HuntingMapInfo.newInstance()));
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
		return new SC_WHOUSER_NOTI_PACKET();
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

	public static class ACCOUNT_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static ACCOUNT_INFO newInstance() {
			return new ACCOUNT_INFO();
		}

		private String _accountname;
		private int _worldnumber;
		private int _location;
		private String _ip;
		private int _ipkind;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private ACCOUNT_INFO() {
		}

		public String get_accountname() {
			return _accountname;
		}

		public void set_accountname(String val) {
			_bit |= 0x1;
			_accountname = val;
		}

		public boolean has_accountname() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_worldnumber() {
			return _worldnumber;
		}

		public void set_worldnumber(int val) {
			_bit |= 0x2;
			_worldnumber = val;
		}

		public boolean has_worldnumber() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_location() {
			return _location;
		}

		public void set_location(int val) {
			_bit |= 0x4;
			_location = val;
		}

		public boolean has_location() {
			return (_bit & 0x4) == 0x4;
		}

		public String get_ip() {
			return _ip;
		}

		public void set_ip(String val) {
			_bit |= 0x8;
			_ip = val;
		}

		public boolean has_ip() {
			return (_bit & 0x8) == 0x8;
		}

		public int get_ipkind() {
			return _ipkind;
		}

		public void set_ipkind(int val) {
			_bit |= 0x10;
			_ipkind = val;
		}

		public boolean has_ipkind() {
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
			if (has_accountname()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _accountname);
			}
			if (has_worldnumber()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _worldnumber);
			}
			if (has_location()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _location);
			}
			if (has_ip()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _ip);
			}
			if (has_ipkind()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _ipkind);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_accountname()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_worldnumber()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_location()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_ip()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_ipkind()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_accountname()) {
				output.writeString(1, _accountname);
			}
			if (has_worldnumber()) {
				output.wirteInt32(2, _worldnumber);
			}
			if (has_location()) {
				output.wirteInt32(3, _location);
			}
			if (has_ip()) {
				output.writeString(4, _ip);
			}
			if (has_ipkind()) {
				output.wirteInt32(5, _ipkind);
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
						set_accountname(input.readString());
						break;
					}
					case 0x00000010: {
						set_worldnumber(input.readInt32());
						break;
					}
					case 0x00000018: {
						set_location(input.readInt32());
						break;
					}
					case 0x00000022: {
						set_ip(input.readString());
						break;
					}
					case 0x00000028: {
						set_ipkind(input.readInt32());
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
			return new ACCOUNT_INFO();
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

	public static class WHOUSER_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static WHOUSER_INFO newInstance() {
			return new WHOUSER_INFO();
		}

		private String _username;
		private String _alignstr;
		private int _serverno;
		private String _title;
		private String _pledge;
		private SC_WHOUSER_NOTI_PACKET.ACCOUNT_INFO _accountinfo;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private WHOUSER_INFO() {
		}

		public String get_username() {
			return _username;
		}

		public void set_username(String val) {
			_bit |= 0x1;
			_username = val;
		}

		public boolean has_username() {
			return (_bit & 0x1) == 0x1;
		}

		public String get_alignstr() {
			return _alignstr;
		}

		public void set_alignstr(String val) {
			_bit |= 0x2;
			_alignstr = val;
		}

		public boolean has_alignstr() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_serverno() {
			return _serverno;
		}

		public void set_serverno(int val) {
			_bit |= 0x4;
			_serverno = val;
		}

		public boolean has_serverno() {
			return (_bit & 0x4) == 0x4;
		}

		public String get_title() {
			return _title;
		}

		public void set_title(String val) {
			_bit |= 0x8;
			_title = val;
		}

		public boolean has_title() {
			return (_bit & 0x8) == 0x8;
		}

		public String get_pledge() {
			return _pledge;
		}

		public void set_pledge(String val) {
			_bit |= 0x10;
			_pledge = val;
		}

		public boolean has_pledge() {
			return (_bit & 0x10) == 0x10;
		}

		public SC_WHOUSER_NOTI_PACKET.ACCOUNT_INFO get_accountinfo() {
			return _accountinfo;
		}

		public void set_accountinfo(SC_WHOUSER_NOTI_PACKET.ACCOUNT_INFO val) {
			_bit |= 0x20;
			_accountinfo = val;
		}

		public boolean has_accountinfo() {
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
			if (has_username()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _username);
			}
			if (has_alignstr()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _alignstr);
			}
			if (has_serverno()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _serverno);
			}
			if (has_title()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _title);
			}
			if (has_pledge()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(5, _pledge);
			}
			if (has_accountinfo()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, _accountinfo);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_username()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_alignstr()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_serverno()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_username()) {
				output.writeString(1, _username);
			}
			if (has_alignstr()) {
				output.writeString(2, _alignstr);
			}
			if (has_serverno()) {
				output.wirteInt32(3, _serverno);
			}
			if (has_title()) {
				output.writeString(4, _title);
			}
			if (has_pledge()) {
				output.writeString(5, _pledge);
			}
			if (has_accountinfo()) {
				output.writeMessage(6, _accountinfo);
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
						set_username(input.readString());
						break;
					}
					case 0x00000012: {
						set_alignstr(input.readString());
						break;
					}
					case 0x00000018: {
						set_serverno(input.readInt32());
						break;
					}
					case 0x00000022: {
						set_title(input.readString());
						break;
					}
					case 0x0000002A: {
						set_pledge(input.readString());
						break;
					}
					case 0x00000032: {
						set_accountinfo((SC_WHOUSER_NOTI_PACKET.ACCOUNT_INFO) input
								.readMessage(SC_WHOUSER_NOTI_PACKET.ACCOUNT_INFO.newInstance()));
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
			return new WHOUSER_INFO();
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

	public static class HuntingMapInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static HuntingMapInfo newInstance() {
			return new HuntingMapInfo();
		}

		private int _map_desc;
		private int _user_count;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private HuntingMapInfo() {
		}

		public int get_map_desc() {
			return _map_desc;
		}

		public void set_map_desc(int val) {
			_bit |= 0x1;
			_map_desc = val;
		}

		public boolean has_map_desc() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_user_count() {
			return _user_count;
		}

		public void set_user_count(int val) {
			_bit |= 0x2;
			_user_count = val;
		}

		public boolean has_user_count() {
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
			if (has_map_desc()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _map_desc);
			}
			if (has_user_count()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _user_count);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_map_desc()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_user_count()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_map_desc()) {
				output.writeUInt32(1, _map_desc);
			}
			if (has_user_count()) {
				output.writeUInt32(2, _user_count);
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
						set_map_desc(input.readUInt32());
						break;
					}
					case 0x00000010: {
						set_user_count(input.readUInt32());
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
			return new HuntingMapInfo();
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
