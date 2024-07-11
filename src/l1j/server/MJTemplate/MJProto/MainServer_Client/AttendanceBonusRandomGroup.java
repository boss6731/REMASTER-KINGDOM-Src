package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class AttendanceBonusRandomGroup implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {

	public static AttendanceBonusRandomGroup opencostinfo(int descid, int count) {
		AttendanceBonusRandomGroup rinfo = newInstance();
		OpenTabCost openinfo = OpenTabCost.newInstance();
		openinfo.set_nameId(descid);
		openinfo.set_cost(count);
		rinfo.set_openCost(openinfo);
		return rinfo;
	}

	public static AttendanceBonusRandomGroup refreshinfo(int descid, int count) {
		AttendanceBonusRandomGroup rinfo = newInstance();
		RandomRefreshCost refreshinfo = RandomRefreshCost.newInstance();
		refreshinfo.set_nameId(descid);
		refreshinfo.set_cost(count);
		refreshinfo.set_refreshStep(0);
		rinfo.add_refreshCost(refreshinfo);
		return rinfo;
	}

	public static AttendanceBonusRandomGroup newInstance() {
		return new AttendanceBonusRandomGroup();
	}

	private java.util.LinkedList<AttendanceBonusRandomGroup.RandomRefreshCost> _refreshCost;
	private java.util.LinkedList<AttendanceBonusRandomGroup.RandomBonusInfo> _bonusInfo;
	private AttendanceBonusRandomGroup.OpenTabCost _openCost;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private AttendanceBonusRandomGroup() {
	}

	public java.util.LinkedList<AttendanceBonusRandomGroup.RandomRefreshCost> get_refreshCost() {
		return _refreshCost;
	}

	public void add_refreshCost(AttendanceBonusRandomGroup.RandomRefreshCost val) {
		if (!has_refreshCost()) {
			_refreshCost = new java.util.LinkedList<AttendanceBonusRandomGroup.RandomRefreshCost>();
			_bit |= 0x1;
		}
		_refreshCost.add(val);
	}

	public boolean has_refreshCost() {
		return (_bit & 0x1) == 0x1;
	}

	public java.util.LinkedList<AttendanceBonusRandomGroup.RandomBonusInfo> get_bonusInfo() {
		return _bonusInfo;
	}

	public void add_bonusInfo(AttendanceBonusRandomGroup.RandomBonusInfo val) {
		if (!has_bonusInfo()) {
			_bonusInfo = new java.util.LinkedList<AttendanceBonusRandomGroup.RandomBonusInfo>();
			_bit |= 0x2;
		}
		_bonusInfo.add(val);
	}

	public boolean has_bonusInfo() {
		return (_bit & 0x2) == 0x2;
	}

	public AttendanceBonusRandomGroup.OpenTabCost get_openCost() {
		return _openCost;
	}

	public void set_openCost(AttendanceBonusRandomGroup.OpenTabCost val) {
		_bit |= 0x4;
		_openCost = val;
	}

	public boolean has_openCost() {
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
		if (has_refreshCost()) {
			for (AttendanceBonusRandomGroup.RandomRefreshCost val : _refreshCost) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_bonusInfo()) {
			for (AttendanceBonusRandomGroup.RandomBonusInfo val : _bonusInfo) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
			}
		}
		if (has_openCost()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _openCost);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_refreshCost()) {
			for (AttendanceBonusRandomGroup.RandomRefreshCost val : _refreshCost) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_bonusInfo()) {
			for (AttendanceBonusRandomGroup.RandomBonusInfo val : _bonusInfo) {
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
		if (has_refreshCost()) {
			for (AttendanceBonusRandomGroup.RandomRefreshCost val : _refreshCost) {
				output.writeMessage(1, val);
			}
		}
		if (has_bonusInfo()) {
			for (AttendanceBonusRandomGroup.RandomBonusInfo val : _bonusInfo) {
				output.writeMessage(2, val);
			}
		}
		if (has_openCost()) {
			output.writeMessage(3, _openCost);
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
					add_refreshCost((AttendanceBonusRandomGroup.RandomRefreshCost) input
							.readMessage(AttendanceBonusRandomGroup.RandomRefreshCost.newInstance()));
					break;
				}
				case 0x00000012: {
					add_bonusInfo((AttendanceBonusRandomGroup.RandomBonusInfo) input
							.readMessage(AttendanceBonusRandomGroup.RandomBonusInfo.newInstance()));
					break;
				}
				case 0x0000001A: {
					set_openCost((AttendanceBonusRandomGroup.OpenTabCost) input
							.readMessage(AttendanceBonusRandomGroup.OpenTabCost.newInstance()));
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
		return new AttendanceBonusRandomGroup();
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

	public static class OpenTabCost implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static OpenTabCost newInstance() {
			return new OpenTabCost();
		}

		private int _nameId;
		private int _cost;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private OpenTabCost() {
		}

		public int get_nameId() {
			return _nameId;
		}

		public void set_nameId(int val) {
			_bit |= 0x1;
			_nameId = val;
		}

		public boolean has_nameId() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_cost() {
			return _cost;
		}

		public void set_cost(int val) {
			_bit |= 0x2;
			_cost = val;
		}

		public boolean has_cost() {
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
			if (has_nameId()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _nameId);
			}
			if (has_cost()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _cost);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_nameId()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_cost()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_nameId()) {
				output.wirteInt32(1, _nameId);
			}
			if (has_cost()) {
				output.wirteInt32(2, _cost);
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
						set_nameId(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_cost(input.readInt32());
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
			return new OpenTabCost();
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

	public static class RandomRefreshCost implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static RandomRefreshCost newInstance() {
			return new RandomRefreshCost();
		}

		private int _nameId;
		private int _cost;
		private int _refreshStep;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private RandomRefreshCost() {
		}

		public int get_nameId() {
			return _nameId;
		}

		public void set_nameId(int val) {
			_bit |= 0x1;
			_nameId = val;
		}

		public boolean has_nameId() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_cost() {
			return _cost;
		}

		public void set_cost(int val) {
			_bit |= 0x2;
			_cost = val;
		}

		public boolean has_cost() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_refreshStep() {
			return _refreshStep;
		}

		public void set_refreshStep(int val) {
			_bit |= 0x4;
			_refreshStep = val;
		}

		public boolean has_refreshStep() {
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
			if (has_nameId()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _nameId);
			}
			if (has_cost()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _cost);
			}
			if (has_refreshStep()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _refreshStep);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_nameId()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_cost()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_refreshStep()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_nameId()) {
				output.wirteInt32(1, _nameId);
			}
			if (has_cost()) {
				output.wirteInt32(2, _cost);
			}
			if (has_refreshStep()) {
				output.wirteInt32(3, _refreshStep);
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
						set_nameId(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_cost(input.readInt32());
						break;
					}
					case 0x00000018: {
						set_refreshStep(input.readInt32());
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
			return new RandomRefreshCost();
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

	public static class RandomBonusInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static RandomBonusInfo newInstance() {
			return new RandomBonusInfo();
		}

		private int _itemId;
		private int _itemCount;
		private String _itemName;
		private int _itemInteractType;
		private int _itemIcon;
		private int _itemBless;
		private String _itemDesc;
		private byte[] _itemExtraDesc;
		private int _itemAttr;
		private int _index;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private RandomBonusInfo() {
		}

		public int get_itemId() {
			return _itemId;
		}

		public void set_itemId(int val) {
			_bit |= 0x1;
			_itemId = val;
		}

		public boolean has_itemId() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_itemCount() {
			return _itemCount;
		}

		public void set_itemCount(int val) {
			_bit |= 0x2;
			_itemCount = val;
		}

		public boolean has_itemCount() {
			return (_bit & 0x2) == 0x2;
		}

		public String get_itemName() {
			return _itemName;
		}

		public void set_itemName(String val) {
			_bit |= 0x4;
			_itemName = val;
		}

		public boolean has_itemName() {
			return (_bit & 0x4) == 0x4;
		}

		public int get_itemInteractType() {
			return _itemInteractType;
		}

		public void set_itemInteractType(int val) {
			_bit |= 0x8;
			_itemInteractType = val;
		}

		public boolean has_itemInteractType() {
			return (_bit & 0x8) == 0x8;
		}

		public int get_itemIcon() {
			return _itemIcon;
		}

		public void set_itemIcon(int val) {
			_bit |= 0x10;
			_itemIcon = val;
		}

		public boolean has_itemIcon() {
			return (_bit & 0x10) == 0x10;
		}

		public int get_itemBless() {
			return _itemBless;
		}

		public void set_itemBless(int val) {
			_bit |= 0x20;
			_itemBless = val;
		}

		public boolean has_itemBless() {
			return (_bit & 0x20) == 0x20;
		}

		public String get_itemDesc() {
			return _itemDesc;
		}

		public void set_itemDesc(String val) {
			_bit |= 0x40;
			_itemDesc = val;
		}

		public boolean has_itemDesc() {
			return (_bit & 0x40) == 0x40;
		}

		public byte[] get_itemExtraDesc() {
			return _itemExtraDesc;
		}

		public void set_itemExtraDesc(byte[] val) {
			_bit |= 0x80;
			_itemExtraDesc = val;
		}

		public boolean has_itemExtraDesc() {
			return (_bit & 0x80) == 0x80;
		}

		public int get_itemAttr() {
			return _itemAttr;
		}

		public void set_itemAttr(int val) {
			_bit |= 0x100;
			_itemAttr = val;
		}

		public boolean has_itemAttr() {
			return (_bit & 0x100) == 0x100;
		}

		public int get_index() {
			return _index;
		}

		public void set_index(int val) {
			_bit |= 0x200;
			_index = val;
		}

		public boolean has_index() {
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
			if (has_itemId()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _itemId);
			}
			if (has_itemCount()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _itemCount);
			}
			if (has_itemName()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _itemName);
			}
			if (has_itemInteractType()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _itemInteractType);
			}
			if (has_itemIcon()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _itemIcon);
			}
			if (has_itemBless()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(6, _itemBless);
			}
			if (has_itemDesc()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(7, _itemDesc);
			}
			if (has_itemExtraDesc()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(8, _itemExtraDesc);
			}
			if (has_itemAttr()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(9, _itemAttr);
			}
			if (has_index()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _index);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
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
			if (!has_index()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_itemId()) {
				output.wirteInt32(1, _itemId);
			}
			if (has_itemCount()) {
				output.wirteInt32(2, _itemCount);
			}
			if (has_itemName()) {
				output.writeString(3, _itemName);
			}
			if (has_itemInteractType()) {
				output.wirteInt32(4, _itemInteractType);
			}
			if (has_itemIcon()) {
				output.wirteInt32(5, _itemIcon);
			}
			if (has_itemBless()) {
				output.wirteInt32(6, _itemBless);
			}
			if (has_itemDesc()) {
				output.writeString(7, _itemDesc);
			}
			if (has_itemExtraDesc()) {
				output.writeBytes(8, _itemExtraDesc);
			}
			if (has_itemAttr()) {
				output.wirteInt32(9, _itemAttr);
			}
			if (has_index()) {
				output.wirteInt32(10, _index);
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
						set_itemId(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_itemCount(input.readInt32());
						break;
					}
					case 0x0000001A: {
						set_itemName(input.readString());
						break;
					}
					case 0x00000020: {
						set_itemInteractType(input.readInt32());
						break;
					}
					case 0x00000028: {
						set_itemIcon(input.readInt32());
						break;
					}
					case 0x00000030: {
						set_itemBless(input.readInt32());
						break;
					}
					case 0x0000003A: {
						set_itemDesc(input.readString());
						break;
					}
					case 0x00000042: {
						set_itemExtraDesc(input.readBytes());
						break;
					}
					case 0x00000048: {
						set_itemAttr(input.readInt32());
						break;
					}
					case 0x00000050: {
						set_index(input.readInt32());
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
			return new RandomBonusInfo();
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
