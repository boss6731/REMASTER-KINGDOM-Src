package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_ATTENDANCE_BONUS_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_ATTENDANCE_BONUS_INFO newInstance() {
		return new SC_ATTENDANCE_BONUS_INFO();
	}

	private int _checkInterval;
	private int _startTime;
	private int _resetPeriod;
	private int _dailyMaxCount;
	private int _weekendMaxCount;
	private java.util.LinkedList<SC_ATTENDANCE_BONUS_INFO.AttendanceBonus> _attendanceBonuses;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_ATTENDANCE_BONUS_INFO() {
	}

	public int get_checkInterval() {
		return _checkInterval;
	}

	public void set_checkInterval(int val) {
		_bit |= 0x1;
		_checkInterval = val;
	}

	public boolean has_checkInterval() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_startTime() {
		return _startTime;
	}

	public void set_startTime(int val) {
		_bit |= 0x2;
		_startTime = val;
	}

	public boolean has_startTime() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_resetPeriod() {
		return _resetPeriod;
	}

	public void set_resetPeriod(int val) {
		_bit |= 0x4;
		_resetPeriod = val;
	}

	public boolean has_resetPeriod() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_dailyMaxCount() {
		return _dailyMaxCount;
	}

	public void set_dailyMaxCount(int val) {
		_bit |= 0x8;
		_dailyMaxCount = val;
	}

	public boolean has_dailyMaxCount() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_weekendMaxCount() {
		return _weekendMaxCount;
	}

	public void set_weekendMaxCount(int val) {
		_bit |= 0x10;
		_weekendMaxCount = val;
	}

	public boolean has_weekendMaxCount() {
		return (_bit & 0x10) == 0x10;
	}

	public java.util.LinkedList<SC_ATTENDANCE_BONUS_INFO.AttendanceBonus> get_attendanceBonuses() {
		return _attendanceBonuses;
	}

	public void add_attendanceBonuses(SC_ATTENDANCE_BONUS_INFO.AttendanceBonus val) {
		if (!has_attendanceBonuses()) {
			_attendanceBonuses = new java.util.LinkedList<SC_ATTENDANCE_BONUS_INFO.AttendanceBonus>();
			_bit |= 0x20;
		}
		_attendanceBonuses.add(val);
	}

	public boolean has_attendanceBonuses() {
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
		if (has_checkInterval()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _checkInterval);
		}
		if (has_startTime()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _startTime);
		}
		if (has_resetPeriod()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _resetPeriod);
		}
		if (has_dailyMaxCount()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _dailyMaxCount);
		}
		if (has_weekendMaxCount()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _weekendMaxCount);
		}
		if (has_attendanceBonuses()) {
			for (SC_ATTENDANCE_BONUS_INFO.AttendanceBonus val : _attendanceBonuses) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_checkInterval()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_startTime()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_resetPeriod()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_dailyMaxCount()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_weekendMaxCount()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_attendanceBonuses()) {
			for (SC_ATTENDANCE_BONUS_INFO.AttendanceBonus val : _attendanceBonuses) {
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
		if (has_checkInterval()) {
			output.wirteInt32(1, _checkInterval);
		}
		if (has_startTime()) {
			output.wirteInt32(2, _startTime);
		}
		if (has_resetPeriod()) {
			output.wirteInt32(3, _resetPeriod);
		}
		if (has_dailyMaxCount()) {
			output.wirteInt32(4, _dailyMaxCount);
		}
		if (has_weekendMaxCount()) {
			output.wirteInt32(5, _weekendMaxCount);
		}
		if (has_attendanceBonuses()) {
			for (SC_ATTENDANCE_BONUS_INFO.AttendanceBonus val : _attendanceBonuses) {
				output.writeMessage(6, val);
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
					set_checkInterval(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_startTime(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_resetPeriod(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_dailyMaxCount(input.readInt32());
					break;
				}
				case 0x00000028: {
					set_weekendMaxCount(input.readInt32());
					break;
				}
				case 0x00000032: {
					add_attendanceBonuses((SC_ATTENDANCE_BONUS_INFO.AttendanceBonus) input
							.readMessage(SC_ATTENDANCE_BONUS_INFO.AttendanceBonus.newInstance()));
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
		return new SC_ATTENDANCE_BONUS_INFO();
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

	public static class BonusInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static BonusInfo newInstance() {
			return new BonusInfo();
		}

		private SC_ATTENDANCE_BONUS_INFO.BonusType _bonusType;
		private int _itemId;
		private int _itemCount;
		private String _itemName;
		private int _itemInteractType;
		private int _itemIcon;
		private int _itemBless;
		private String _itemDesc;
		private byte[] _itemExtraDesc;
		private int _itemAttr;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private BonusInfo() {
		}

		public SC_ATTENDANCE_BONUS_INFO.BonusType get_bonusType() {
			return _bonusType;
		}

		public void set_bonusType(SC_ATTENDANCE_BONUS_INFO.BonusType val) {
			_bit |= 0x1;
			_bonusType = val;
		}

		public boolean has_bonusType() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_itemId() {
			return _itemId;
		}

		public void set_itemId(int val) {
			_bit |= 0x2;
			_itemId = val;
		}

		public boolean has_itemId() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_itemCount() {
			return _itemCount;
		}

		public void set_itemCount(int val) {
			_bit |= 0x4;
			_itemCount = val;
		}

		public boolean has_itemCount() {
			return (_bit & 0x4) == 0x4;
		}

		public String get_itemName() {
			return _itemName;
		}

		public void set_itemName(String val) {
			_bit |= 0x8;
			_itemName = val;
		}

		public boolean has_itemName() {
			return (_bit & 0x8) == 0x8;
		}

		public int get_itemInteractType() {
			return _itemInteractType;
		}

		public void set_itemInteractType(int val) {
			_bit |= 0x10;
			_itemInteractType = val;
		}

		public boolean has_itemInteractType() {
			return (_bit & 0x10) == 0x10;
		}

		public int get_itemIcon() {
			return _itemIcon;
		}

		public void set_itemIcon(int val) {
			_bit |= 0x20;
			_itemIcon = val;
		}

		public boolean has_itemIcon() {
			return (_bit & 0x20) == 0x20;
		}

		public int get_itemBless() {
			return _itemBless;
		}

		public void set_itemBless(int val) {
			_bit |= 0x40;
			_itemBless = val;
		}

		public boolean has_itemBless() {
			return (_bit & 0x40) == 0x40;
		}

		public String get_itemDesc() {
			return _itemDesc;
		}

		public void set_itemDesc(String val) {
			_bit |= 0x80;
			_itemDesc = val;
		}

		public boolean has_itemDesc() {
			return (_bit & 0x80) == 0x80;
		}

		public byte[] get_itemExtraDesc() {
			return _itemExtraDesc;
		}

		public void set_itemExtraDesc(byte[] val) {
			_bit |= 0x100;
			_itemExtraDesc = val;
		}

		public boolean has_itemExtraDesc() {
			return (_bit & 0x100) == 0x100;
		}

		public int get_itemAttr() {
			return _itemAttr;
		}

		public void set_itemAttr(int val) {
			_bit |= 0x200;
			_itemAttr = val;
		}

		public boolean has_itemAttr() {
			return (_bit & 0x200) == 0x200;
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
			if (has_bonusType()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _bonusType.toInt());
			}
			if (has_itemId()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _itemId);
			}
			if (has_itemCount()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _itemCount);
			}
			if (has_itemName()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _itemName);
			}
			if (has_itemInteractType()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _itemInteractType);
			}
			if (has_itemIcon()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _itemIcon);
			}
			if (has_itemBless()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _itemBless);
			}
			if (has_itemDesc()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(8, _itemDesc);
			}
			if (has_itemExtraDesc()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(9, _itemExtraDesc);
			}
			if (has_itemAttr()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _itemAttr);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_bonusType()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_itemId()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_itemCount()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_itemName()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_itemInteractType()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_itemIcon()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_itemBless()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_itemDesc()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_itemExtraDesc()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_itemAttr()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_bonusType()) {
				output.writeEnum(1, _bonusType.toInt());
			}
			if (has_itemId()) {
				output.wirteInt32(2, _itemId);
			}
			if (has_itemCount()) {
				output.wirteInt32(3, _itemCount);
			}
			if (has_itemName()) {
				output.writeString(4, _itemName);
			}
			if (has_itemInteractType()) {
				output.wirteInt32(5, _itemInteractType);
			}
			if (has_itemIcon()) {
				output.wirteInt32(6, _itemIcon);
			}
			if (has_itemBless()) {
				output.wirteInt32(7, _itemBless);
			}
			if (has_itemDesc()) {
				output.writeString(8, _itemDesc);
			}
			if (has_itemExtraDesc()) {
				output.writeBytes(9, _itemExtraDesc);
			}
			if (has_itemAttr()) {
				output.wirteInt32(10, _itemAttr);
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
						set_bonusType(SC_ATTENDANCE_BONUS_INFO.BonusType.fromInt(input.readEnum()));
						break;
					}
					case 0x00000010: {
						set_itemId(input.readInt32());
						break;
					}
					case 0x00000018: {
						set_itemCount(input.readInt32());
						break;
					}
					case 0x00000022: {
						set_itemName(input.readString());
						break;
					}
					case 0x00000028: {
						set_itemInteractType(input.readInt32());
						break;
					}
					case 0x00000030: {
						set_itemIcon(input.readInt32());
						break;
					}
					case 0x00000038: {
						set_itemBless(input.readInt32());
						break;
					}
					case 0x00000042: {
						set_itemDesc(input.readString());
						break;
					}
					case 0x0000004A: {
						set_itemExtraDesc(input.readBytes());
						break;
					}
					case 0x00000050: {
						set_itemAttr(input.readInt32());
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
			return new BonusInfo();
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

	public static class AttendanceBonus implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static AttendanceBonus newInstance() {
			return new AttendanceBonus();
		}

		private int _attendaceIndex;
		private java.util.LinkedList<SC_ATTENDANCE_BONUS_INFO.BonusInfo> _bonuses;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private AttendanceBonus() {
		}

		public int get_attendaceIndex() {
			return _attendaceIndex;
		}

		public void set_attendaceIndex(int val) {
			_bit |= 0x1;
			_attendaceIndex = val;
		}

		public boolean has_attendaceIndex() {
			return (_bit & 0x1) == 0x1;
		}

		public java.util.LinkedList<SC_ATTENDANCE_BONUS_INFO.BonusInfo> get_bonuses() {
			return _bonuses;
		}

		public void add_bonuses(SC_ATTENDANCE_BONUS_INFO.BonusInfo val) {
			if (!has_bonuses()) {
				_bonuses = new java.util.LinkedList<SC_ATTENDANCE_BONUS_INFO.BonusInfo>();
				_bit |= 0x2;
			}
			_bonuses.add(val);
		}

		public boolean has_bonuses() {
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
			if (has_attendaceIndex()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _attendaceIndex);
			}
			if (has_bonuses()) {
				for (SC_ATTENDANCE_BONUS_INFO.BonusInfo val : _bonuses) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_attendaceIndex()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_bonuses()) {
				for (SC_ATTENDANCE_BONUS_INFO.BonusInfo val : _bonuses) {
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
			if (has_attendaceIndex()) {
				output.wirteInt32(1, _attendaceIndex);
			}
			if (has_bonuses()) {
				for (SC_ATTENDANCE_BONUS_INFO.BonusInfo val : _bonuses) {
					output.writeMessage(2, val);
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
						set_attendaceIndex(input.readInt32());
						break;
					}
					case 0x00000012: {
						add_bonuses((SC_ATTENDANCE_BONUS_INFO.BonusInfo) input
								.readMessage(SC_ATTENDANCE_BONUS_INFO.BonusInfo.newInstance()));
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
			return new AttendanceBonus();
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

	public enum BonusType {
		UseItem(1),
		GiveItem(2),
		;

		private int value;

		BonusType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(BonusType v) {
			return value == v.value;
		}

		public static BonusType fromInt(int i) {
			switch (i) {
				case 1:
					return UseItem;
				case 2:
					return GiveItem;
				default:
					throw new IllegalArgumentException(String.format("無效參數 BonusType，%d", i));
			}
		}
	}
}
