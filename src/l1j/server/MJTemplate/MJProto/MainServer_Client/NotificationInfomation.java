package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eNotiAnimationType;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class NotificationInfomation implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static NotificationInfomation newInstance() {
		return new NotificationInfomation();
	}

	private int _notification_id;
	private int _objectid;
	private byte[] _hyperlink;
	private byte[] _displaydesc;
	private long _startdate;
	private long _enddate;
	private NotificationInfomation.TeleportData _teleport;
	private NotificationInfomation.EventNpcData _eventnpc;
	private boolean _rest_gauge_icon_display;
	private int _rest_gauge_bonus_display;
	private boolean _new;
	private eNotiAnimationType _animation_type;
	private java.util.LinkedList<Integer> _worlds;
	private int _NotiType;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private NotificationInfomation() {
	}

	public int get_notification_id() {
		return _notification_id;
	}

	public void set_notification_id(int val) {
		_bit |= 0x1;
		_notification_id = val;
	}

	public boolean has_notification_id() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_objectid() {
		return _objectid;
	}

	public void set_objectid(int val) {
		_bit |= 0x2;
		_objectid = val;
	}

	public boolean has_objectid() {
		return (_bit & 0x2) == 0x2;
	}

	public byte[] get_hyperlink() {
		return _hyperlink;
	}

	public void set_hyperlink(byte[] val) {
		_bit |= 0x4;
		_hyperlink = val;
	}

	public boolean has_hyperlink() {
		return (_bit & 0x4) == 0x4;
	}

	public byte[] get_displaydesc() {
		return _displaydesc;
	}

	public void set_displaydesc(byte[] val) {
		_bit |= 0x8;
		_displaydesc = val;
	}

	public boolean has_displaydesc() {
		return (_bit & 0x8) == 0x8;
	}

	public long get_startdate() {
		return _startdate;
	}

	public void set_startdate(long val) {
		_bit |= 0x10;
		_startdate = val;
	}

	public boolean has_startdate() {
		return (_bit & 0x10) == 0x10;
	}

	public long get_enddate() {
		return _enddate;
	}

	public void set_enddate(long val) {
		_bit |= 0x20;
		_enddate = val;
	}

	public boolean has_enddate() {
		return (_bit & 0x20) == 0x20;
	}

	public NotificationInfomation.TeleportData get_teleport() {
		return _teleport;
	}

	public void set_teleport(NotificationInfomation.TeleportData val) {
		_bit |= 0x40;
		_teleport = val;
	}

	public boolean has_teleport() {
		return (_bit & 0x40) == 0x40;
	}

	public NotificationInfomation.EventNpcData get_eventnpc() {
		return _eventnpc;
	}

	public void set_eventnpc(NotificationInfomation.EventNpcData val) {
		_bit |= 0x80;
		_eventnpc = val;
	}

	public boolean has_eventnpc() {
		return (_bit & 0x80) == 0x80;
	}

	public boolean get_rest_gauge_icon_display() {
		return _rest_gauge_icon_display;
	}

	public void set_rest_gauge_icon_display(boolean val) {
		_bit |= 0x100;
		_rest_gauge_icon_display = val;
	}

	public boolean has_rest_gauge_icon_display() {
		return (_bit & 0x100) == 0x100;
	}

	public int get_rest_gauge_bonus_display() {
		return _rest_gauge_bonus_display;
	}

	public void set_rest_gauge_bonus_display(int val) {
		_bit |= 0x200;
		_rest_gauge_bonus_display = val;
	}

	public boolean has_rest_gauge_bonus_display() {
		return (_bit & 0x200) == 0x200;
	}

	public boolean get_new() {
		return _new;
	}

	public void set_new(boolean val) {
		_bit |= 0x400;
		_new = val;
	}

	public boolean has_new() {
		return (_bit & 0x400) == 0x400;
	}

	public eNotiAnimationType get_animation_type() {
		return _animation_type;
	}

	public void set_animation_type(eNotiAnimationType val) {
		_bit |= 0x800;
		_animation_type = val;
	}

	public boolean has_animation_type() {
		return (_bit & 0x800) == 0x800;
	}

	public java.util.LinkedList<Integer> get_worlds() {
		return _worlds;
	}

	public void add_worlds(int val) {
		if (!has_worlds()) {
			_worlds = new java.util.LinkedList<Integer>();
			_bit |= 0x1000;
		}
		_worlds.add(val);
	}

	public boolean has_worlds() {
		return (_bit & 0x1000) == 0x1000;
	}

	public int get_NotiType() {
		return _NotiType;
	}

	public void set_NotiType(int val) {
		_bit |= 0x2000;
		_NotiType = val;
	}

	public boolean has_NotiType() {
		return (_bit & 0x2000) == 0x2000;
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
		if (has_notification_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _notification_id);
		}
		if (has_objectid()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _objectid);
		}
		if (has_hyperlink()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(3, _hyperlink);
		}
		if (has_displaydesc()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(4, _displaydesc);
		}
		if (has_startdate()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt64Size(5, _startdate);
		}
		if (has_enddate()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt64Size(6, _enddate);
		}
		if (has_teleport()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(7, _teleport);
		}
		if (has_eventnpc()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(8, _eventnpc);
		}
		if (has_rest_gauge_icon_display()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(9, _rest_gauge_icon_display);
		}
		if (has_rest_gauge_bonus_display()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(10, _rest_gauge_bonus_display);
		}
		if (has_new()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(11, _new);
		}
		if (has_animation_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(12, _animation_type.toInt());
		}
		if (has_worlds()) {
			for (int val : _worlds) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(13, val);
			}
		}
		if (has_NotiType()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(14, _NotiType);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_displaydesc()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		// if (has_worlds()){
		// for(int val : _worlds){
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
		if (has_notification_id()) {
			output.wirteInt32(1, _notification_id);
		}
		if (has_objectid()) {
			output.writeUInt32(2, _objectid);
		}
		if (has_hyperlink()) {
			output.writeBytes(3, _hyperlink);
		}
		if (has_displaydesc()) {
			output.writeBytes(4, _displaydesc);
		}
		if (has_startdate()) {
			output.wirteUInt64(5, _startdate);
		}
		if (has_enddate()) {
			output.wirteUInt64(6, _enddate);
		}
		if (has_teleport()) {
			output.writeMessage(7, _teleport);
		}
		if (has_eventnpc()) {
			output.writeMessage(8, _eventnpc);
		}
		if (has_rest_gauge_icon_display()) {
			output.writeBool(9, _rest_gauge_icon_display);
		}
		if (has_rest_gauge_bonus_display()) {
			output.writeUInt32(10, _rest_gauge_bonus_display);
		}
		if (has_new()) {
			output.writeBool(11, _new);
		}
		if (has_animation_type()) {
			output.writeEnum(12, _animation_type.toInt());
		}
		if (has_worlds()) {
			for (int val : _worlds) {
				output.wirteInt32(13, val);
			}
		}
		if (has_NotiType()) {
			output.wirteInt32(14, _NotiType);
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
					set_notification_id(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_objectid(input.readUInt32());
					break;
				}
				case 0x0000001A: {
					set_hyperlink(input.readBytes());
					break;
				}
				case 0x00000022: {
					set_displaydesc(input.readBytes());
					break;
				}
				case 0x00000028: {
					set_startdate(input.readUInt64());
					break;
				}
				case 0x00000030: {
					set_enddate(input.readUInt64());
					break;
				}
				case 0x0000003A: {
					set_teleport((NotificationInfomation.TeleportData) input
							.readMessage(NotificationInfomation.TeleportData.newInstance()));
					break;
				}
				case 0x00000042: {
					set_eventnpc((NotificationInfomation.EventNpcData) input
							.readMessage(NotificationInfomation.EventNpcData.newInstance()));
					break;
				}
				case 0x00000048: {
					set_rest_gauge_icon_display(input.readBool());
					break;
				}
				case 0x00000050: {
					set_rest_gauge_bonus_display(input.readUInt32());
					break;
				}
				case 0x00000058: {
					set_new(input.readBool());
					break;
				}
				case 0x00000060: {
					set_animation_type(eNotiAnimationType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000068: {
					add_worlds(input.readInt32());
					break;
				}
				case 0x00000070: {
					set_NotiType(input.readInt32());
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
		return new NotificationInfomation();
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

	public static class TeleportData implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static TeleportData newInstance() {
			return new TeleportData();
		}

		private byte[] _stringk;
		private int _adenacount;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private TeleportData() {
		}

		public byte[] get_stringk() {
			return _stringk;
		}

		public void set_stringk(byte[] val) {
			_bit |= 0x1;
			_stringk = val;
		}

		public boolean has_stringk() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_adenacount() {
			return _adenacount;
		}

		public void set_adenacount(int val) {
			_bit |= 0x2;
			_adenacount = val;
		}

		public boolean has_adenacount() {
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
			if (has_stringk()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(1, _stringk);
			}
			if (has_adenacount()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _adenacount);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_stringk()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_adenacount()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_stringk()) {
				output.writeBytes(1, _stringk);
			}
			if (has_adenacount()) {
				output.wirteInt32(2, _adenacount);
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
						set_stringk(input.readBytes());
						break;
					}
					case 0x00000010: {
						set_adenacount(input.readInt32());
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
			return new TeleportData();
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

	public static class EventNpcInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static EventNpcInfo newInstance() {
			return new EventNpcInfo();
		}

		private int _npc_id;
		private byte[] _displaydesc;
		private boolean _rest_gauge_icon_display;
		private int _rest_gauge_bonus_display;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private EventNpcInfo() {
		}

		public int get_npc_id() {
			return _npc_id;
		}

		public void set_npc_id(int val) {
			_bit |= 0x1;
			_npc_id = val;
		}

		public boolean has_npc_id() {
			return (_bit & 0x1) == 0x1;
		}

		public byte[] get_displaydesc() {
			return _displaydesc;
		}

		public void set_displaydesc(byte[] val) {
			_bit |= 0x2;
			_displaydesc = val;
		}

		public boolean has_displaydesc() {
			return (_bit & 0x2) == 0x2;
		}

		public boolean get_rest_gauge_icon_display() {
			return _rest_gauge_icon_display;
		}

		public void set_rest_gauge_icon_display(boolean val) {
			_bit |= 0x4;
			_rest_gauge_icon_display = val;
		}

		public boolean has_rest_gauge_icon_display() {
			return (_bit & 0x4) == 0x4;
		}

		public int get_rest_gauge_bonus_display() {
			return _rest_gauge_bonus_display;
		}

		public void set_rest_gauge_bonus_display(int val) {
			_bit |= 0x8;
			_rest_gauge_bonus_display = val;
		}

		public boolean has_rest_gauge_bonus_display() {
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
			if (has_npc_id()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _npc_id);
			}
			if (has_displaydesc()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(2, _displaydesc);
			}
			if (has_rest_gauge_icon_display()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _rest_gauge_icon_display);
			}
			if (has_rest_gauge_bonus_display()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4,
						_rest_gauge_bonus_display);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_npc_id()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_displaydesc()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_npc_id()) {
				output.writeUInt32(1, _npc_id);
			}
			if (has_displaydesc()) {
				output.writeBytes(2, _displaydesc);
			}
			if (has_rest_gauge_icon_display()) {
				output.writeBool(3, _rest_gauge_icon_display);
			}
			if (has_rest_gauge_bonus_display()) {
				output.writeUInt32(4, _rest_gauge_bonus_display);
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
						set_npc_id(input.readUInt32());
						break;
					}
					case 0x00000012: {
						set_displaydesc(input.readBytes());
						break;
					}
					case 0x00000018: {
						set_rest_gauge_icon_display(input.readBool());
						break;
					}
					case 0x00000020: {
						set_rest_gauge_bonus_display(input.readUInt32());
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
			return new EventNpcInfo();
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

	public static class EventNpcData implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static EventNpcData newInstance() {
			return new EventNpcData();
		}

		private java.util.LinkedList<NotificationInfomation.EventNpcInfo> _eventinfo;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private EventNpcData() {
		}

		public java.util.LinkedList<NotificationInfomation.EventNpcInfo> get_eventinfo() {
			return _eventinfo;
		}

		public void add_eventinfo(NotificationInfomation.EventNpcInfo val) {
			if (!has_eventinfo()) {
				_eventinfo = new java.util.LinkedList<NotificationInfomation.EventNpcInfo>();
				_bit |= 0x1;
			}
			_eventinfo.add(val);
		}

		public boolean has_eventinfo() {
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
			if (has_eventinfo()) {
				for (NotificationInfomation.EventNpcInfo val : _eventinfo) {
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
			if (has_eventinfo()) {
				for (NotificationInfomation.EventNpcInfo val : _eventinfo) {
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
			if (has_eventinfo()) {
				for (NotificationInfomation.EventNpcInfo val : _eventinfo) {
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
						add_eventinfo((NotificationInfomation.EventNpcInfo) input
								.readMessage(NotificationInfomation.EventNpcInfo.newInstance()));
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
			return new EventNpcData();
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
