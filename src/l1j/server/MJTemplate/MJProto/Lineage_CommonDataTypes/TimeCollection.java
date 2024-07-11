package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO：自動產生 PROTO 程式碼。大自然創造的。
public class TimeCollection implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static TimeCollection newInstance() {
		return new TimeCollection();
	}

	private TimeCollection.BuffSelectT _BuffSelect;
	private TimeCollection.RewardListT _RewardList;
	private TimeCollection.EnchantSectionT _EnchantSection;
	private java.util.LinkedList<TimeCollection.GroupT> _Group;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private TimeCollection() {
	}

	public TimeCollection.BuffSelectT get_BuffSelect() {
		return _BuffSelect;
	}

	public void set_BuffSelect(TimeCollection.BuffSelectT val) {
		_bit |= 0x1;
		_BuffSelect = val;
	}

	public boolean has_BuffSelect() {
		return (_bit & 0x1) == 0x1;
	}

	public TimeCollection.RewardListT get_RewardList() {
		return _RewardList;
	}

	public void set_RewardList(TimeCollection.RewardListT val) {
		_bit |= 0x2;
		_RewardList = val;
	}

	public boolean has_RewardList() {
		return (_bit & 0x2) == 0x2;
	}

	public TimeCollection.EnchantSectionT get_EnchantSection() {
		return _EnchantSection;
	}

	public void set_EnchantSection(TimeCollection.EnchantSectionT val) {
		_bit |= 0x4;
		_EnchantSection = val;
	}

	public boolean has_EnchantSection() {
		return (_bit & 0x4) == 0x4;
	}

	public java.util.LinkedList<TimeCollection.GroupT> get_Group() {
		return _Group;
	}

	public void add_Group(TimeCollection.GroupT val) {
		if (!has_Group()) {
			_Group = new java.util.LinkedList<TimeCollection.GroupT>();
			_bit |= 0x8;
		}
		_Group.add(val);
	}

	public boolean has_Group() {
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
		if (has_BuffSelect()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _BuffSelect);
		}
		if (has_RewardList()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _RewardList);
		}
		if (has_EnchantSection()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _EnchantSection);
		}
		if (has_Group()) {
			for (TimeCollection.GroupT val : _Group) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_BuffSelect()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_RewardList()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_EnchantSection()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_Group()) {
			for (TimeCollection.GroupT val : _Group) {
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
		if (has_BuffSelect()) {
			output.writeMessage(1, _BuffSelect);
		}
		if (has_RewardList()) {
			output.writeMessage(2, _RewardList);
		}
		if (has_EnchantSection()) {
			output.writeMessage(3, _EnchantSection);
		}
		if (has_Group()) {
			for (TimeCollection.GroupT val : _Group) {
				output.writeMessage(4, val);
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
					set_BuffSelect(
							(TimeCollection.BuffSelectT) input.readMessage(TimeCollection.BuffSelectT.newInstance()));
					break;
				}
				case 0x00000012: {
					set_RewardList(
							(TimeCollection.RewardListT) input.readMessage(TimeCollection.RewardListT.newInstance()));
					break;
				}
				case 0x0000001A: {
					set_EnchantSection((TimeCollection.EnchantSectionT) input
							.readMessage(TimeCollection.EnchantSectionT.newInstance()));
					break;
				}
				case 0x00000022: {
					add_Group((TimeCollection.GroupT) input.readMessage(TimeCollection.GroupT.newInstance()));
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

			// TODO：從下面插入處理程式碼。大自然創造的。

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new TimeCollection();
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

	public static class BuffSelectT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static BuffSelectT newInstance() {
			return new BuffSelectT();
		}

		private java.util.LinkedList<TimeCollection.BuffSelectT.UserT> _User;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private BuffSelectT() {
		}

		public java.util.LinkedList<TimeCollection.BuffSelectT.UserT> get_User() {
			return _User;
		}

		public void add_User(TimeCollection.BuffSelectT.UserT val) {
			if (!has_User()) {
				_User = new java.util.LinkedList<TimeCollection.BuffSelectT.UserT>();
				_bit |= 0x1;
			}
			_User.add(val);
		}

		public boolean has_User() {
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
			if (has_User()) {
				for (TimeCollection.BuffSelectT.UserT val : _User) {
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
			if (has_User()) {
				for (TimeCollection.BuffSelectT.UserT val : _User) {
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
			if (has_User()) {
				for (TimeCollection.BuffSelectT.UserT val : _User) {
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
						add_User((TimeCollection.BuffSelectT.UserT) input
								.readMessage(TimeCollection.BuffSelectT.UserT.newInstance()));
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

				// TODO : 從下面開始插入您的處理程式碼。 made by Nature.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new BuffSelectT();
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

		public static class UserT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
			public static UserT newInstance() {
				return new UserT();
			}

			private int _GameClass;
			private int _BuffType;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;

			private UserT() {
			}

			public int get_GameClass() {
				return _GameClass;
			}

			public void set_GameClass(int val) {
				_bit |= 0x1;
				_GameClass = val;
			}

			public boolean has_GameClass() {
				return (_bit & 0x1) == 0x1;
			}

			public int get_BuffType() {
				return _BuffType;
			}

			public void set_BuffType(int val) {
				_bit |= 0x2;
				_BuffType = val;
			}

			public boolean has_BuffType() {
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
				if (has_GameClass()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _GameClass);
				}
				if (has_BuffType()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _BuffType);
				}
				_memorizedSerializedSize = size;
				return size;
			}

			@Override
			public boolean isInitialized() {
				if (_memorizedIsInitialized == 1)
					return true;
				if (!has_GameClass()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_BuffType()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}

			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
				if (has_GameClass()) {
					output.wirteInt32(1, _GameClass);
				}
				if (has_BuffType()) {
					output.wirteInt32(2, _BuffType);
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
							set_GameClass(input.readInt32());
							break;
						}
						case 0x00000010: {
							set_BuffType(input.readInt32());
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
			public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt,
					byte[] bytes) {
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

					// TODO：從下面插入處理程式碼。大自然創造的。

				} catch (Exception e) {
					e.printStackTrace();
				}
				return this;
			}

			@Override
			public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
				return new UserT();
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

	public static class RewardListT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static RewardListT newInstance() {
			return new RewardListT();
		}

		private java.util.LinkedList<TimeCollection.RewardListT.RewardT> _Reward;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private RewardListT() {
		}

		public java.util.LinkedList<TimeCollection.RewardListT.RewardT> get_Reward() {
			return _Reward;
		}

		public void add_Reward(TimeCollection.RewardListT.RewardT val) {
			if (!has_Reward()) {
				_Reward = new java.util.LinkedList<TimeCollection.RewardListT.RewardT>();
				_bit |= 0x1;
			}
			_Reward.add(val);
		}

		public boolean has_Reward() {
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
			if (has_Reward()) {
				for (TimeCollection.RewardListT.RewardT val : _Reward) {
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
			if (has_Reward()) {
				for (TimeCollection.RewardListT.RewardT val : _Reward) {
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
			if (has_Reward()) {
				for (TimeCollection.RewardListT.RewardT val : _Reward) {
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
						add_Reward((TimeCollection.RewardListT.RewardT) input
								.readMessage(TimeCollection.RewardListT.RewardT.newInstance()));
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

				// TODO：從下面插入處理程式碼。大自然創造的。

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new RewardListT();
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

		public static class RewardT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
			public static RewardT newInstance() {
				return new RewardT();
			}

			private int _Type;
			private int _Prob;
			private String _BonusTime;
			private int _BonusTimePercent;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;

			private RewardT() {
			}

			public int get_Type() {
				return _Type;
			}

			public void set_Type(int val) {
				_bit |= 0x1;
				_Type = val;
			}

			public boolean has_Type() {
				return (_bit & 0x1) == 0x1;
			}

			public int get_Prob() {
				return _Prob;
			}

			public void set_Prob(int val) {
				_bit |= 0x2;
				_Prob = val;
			}

			public boolean has_Prob() {
				return (_bit & 0x2) == 0x2;
			}

			public String get_BonusTime() {
				return _BonusTime;
			}

			public void set_BonusTime(String val) {
				_bit |= 0x4;
				_BonusTime = val;
			}

			public boolean has_BonusTime() {
				return (_bit & 0x4) == 0x4;
			}

			public int get_BonusTimePercent() {
				return _BonusTimePercent;
			}

			public void set_BonusTimePercent(int val) {
				_bit |= 0x8;
				_BonusTimePercent = val;
			}

			public boolean has_BonusTimePercent() {
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
				if (has_Type()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _Type);
				}
				if (has_Prob()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _Prob);
				}
				if (has_BonusTime()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _BonusTime);
				}
				if (has_BonusTimePercent()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _BonusTimePercent);
				}
				_memorizedSerializedSize = size;
				return size;
			}

			@Override
			public boolean isInitialized() {
				if (_memorizedIsInitialized == 1)
					return true;
				if (!has_Type()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_Prob()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}

			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
				if (has_Type()) {
					output.wirteInt32(1, _Type);
				}
				if (has_Prob()) {
					output.wirteInt32(2, _Prob);
				}
				if (has_BonusTime()) {
					output.writeString(3, _BonusTime);
				}
				if (has_BonusTimePercent()) {
					output.wirteInt32(4, _BonusTimePercent);
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
							set_Type(input.readInt32());
							break;
						}
						case 0x00000010: {
							set_Prob(input.readInt32());
							break;
						}
						case 0x0000001A: {
							set_BonusTime(input.readString());
							break;
						}
						case 0x00000020: {
							set_BonusTimePercent(input.readInt32());
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
			public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt,
					byte[] bytes) {
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

					// TODO：從下面插入處理程式碼。大自然創造的。

				} catch (Exception e) {
					e.printStackTrace();
				}
				return this;
			}

			@Override
			public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
				return new RewardT();
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

	public static class EnchantSectionT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static EnchantSectionT newInstance() {
			return new EnchantSectionT();
		}

		private java.util.LinkedList<TimeCollection.EnchantSectionT.EnchantIDT> _EnchantID;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private EnchantSectionT() {
		}

		public java.util.LinkedList<TimeCollection.EnchantSectionT.EnchantIDT> get_EnchantID() {
			return _EnchantID;
		}

		public void add_EnchantID(TimeCollection.EnchantSectionT.EnchantIDT val) {
			if (!has_EnchantID()) {
				_EnchantID = new java.util.LinkedList<TimeCollection.EnchantSectionT.EnchantIDT>();
				_bit |= 0x1;
			}
			_EnchantID.add(val);
		}

		public boolean has_EnchantID() {
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
			if (has_EnchantID()) {
				for (TimeCollection.EnchantSectionT.EnchantIDT val : _EnchantID) {
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
			if (has_EnchantID()) {
				for (TimeCollection.EnchantSectionT.EnchantIDT val : _EnchantID) {
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
			if (has_EnchantID()) {
				for (TimeCollection.EnchantSectionT.EnchantIDT val : _EnchantID) {
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
						add_EnchantID((TimeCollection.EnchantSectionT.EnchantIDT) input
								.readMessage(TimeCollection.EnchantSectionT.EnchantIDT.newInstance()));
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

				// TODO：從下面插入處理程式碼。大自然創造的。

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new EnchantSectionT();
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

		public static class EnchantIDT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
			public static EnchantIDT newInstance() {
				return new EnchantIDT();
			}

			private int _ID;
			private int _EnchantMin;
			private int _EnchantMax;
			private java.util.LinkedList<TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT> _EnchantBonus;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;

			private EnchantIDT() {
			}

			public int get_ID() {
				return _ID;
			}

			public void set_ID(int val) {
				_bit |= 0x1;
				_ID = val;
			}

			public boolean has_ID() {
				return (_bit & 0x1) == 0x1;
			}

			public int get_EnchantMin() {
				return _EnchantMin;
			}

			public void set_EnchantMin(int val) {
				_bit |= 0x2;
				_EnchantMin = val;
			}

			public boolean has_EnchantMin() {
				return (_bit & 0x2) == 0x2;
			}

			public int get_EnchantMax() {
				return _EnchantMax;
			}

			public void set_EnchantMax(int val) {
				_bit |= 0x4;
				_EnchantMax = val;
			}

			public boolean has_EnchantMax() {
				return (_bit & 0x4) == 0x4;
			}

			public java.util.LinkedList<TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT> get_EnchantBonus() {
				return _EnchantBonus;
			}

			public void add_EnchantBonus(TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT val) {
				if (!has_EnchantBonus()) {
					_EnchantBonus = new java.util.LinkedList<TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT>();
					_bit |= 0x8;
				}
				_EnchantBonus.add(val);
			}

			public boolean has_EnchantBonus() {
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
				if (has_ID()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _ID);
				}
				if (has_EnchantMin()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _EnchantMin);
				}
				if (has_EnchantMax()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _EnchantMax);
				}
				if (has_EnchantBonus()) {
					for (TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT val : _EnchantBonus) {
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
					}
				}
				_memorizedSerializedSize = size;
				return size;
			}

			@Override
			public boolean isInitialized() {
				if (_memorizedIsInitialized == 1)
					return true;
				if (!has_ID()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_EnchantMin()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_EnchantMax()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_EnchantBonus()) {
					for (TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT val : _EnchantBonus) {
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
				if (has_ID()) {
					output.wirteInt32(1, _ID);
				}
				if (has_EnchantMin()) {
					output.wirteInt32(2, _EnchantMin);
				}
				if (has_EnchantMax()) {
					output.wirteInt32(3, _EnchantMax);
				}
				if (has_EnchantBonus()) {
					for (TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT val : _EnchantBonus) {
						output.writeMessage(4, val);
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
							set_ID(input.readInt32());
							break;
						}
						case 0x00000010: {
							set_EnchantMin(input.readInt32());
							break;
						}
						case 0x00000018: {
							set_EnchantMax(input.readInt32());
							break;
						}
						case 0x00000022: {
							add_EnchantBonus(
									(TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT) input.readMessage(
											TimeCollection.EnchantSectionT.EnchantIDT.EnchantBonusT.newInstance()));
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
			public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt,
					byte[] bytes) {
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

					// TODO：從下面插入處理程式碼。大自然創造的。

				} catch (Exception e) {
					e.printStackTrace();
				}
				return this;
			}

			@Override
			public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
				return new EnchantIDT();
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

			public static class EnchantBonusT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
				public static EnchantBonusT newInstance() {
					return new EnchantBonusT();
				}

				private int _Enchant;
				private String _BonusTime;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;

				private EnchantBonusT() {
				}

				public int get_Enchant() {
					return _Enchant;
				}

				public void set_Enchant(int val) {
					_bit |= 0x1;
					_Enchant = val;
				}

				public boolean has_Enchant() {
					return (_bit & 0x1) == 0x1;
				}

				public String get_BonusTime() {
					return _BonusTime;
				}

				public void set_BonusTime(String val) {
					_bit |= 0x2;
					_BonusTime = val;
				}

				public boolean has_BonusTime() {
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
					if (has_Enchant()) {
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _Enchant);
					}
					if (has_BonusTime()) {
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _BonusTime);
					}
					_memorizedSerializedSize = size;
					return size;
				}

				@Override
				public boolean isInitialized() {
					if (_memorizedIsInitialized == 1)
						return true;
					if (!has_Enchant()) {
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}

				@Override
				public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output)
						throws java.io.IOException {
					if (has_Enchant()) {
						output.wirteInt32(1, _Enchant);
					}
					if (has_BonusTime()) {
						output.writeString(2, _BonusTime);
					}
				}

				@Override
				public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
						l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
					l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
							.newInstance(
									getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
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
								set_Enchant(input.readInt32());
								break;
							}
							case 0x00000012: {
								set_BonusTime(input.readString());
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
				public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt,
						byte[] bytes) {
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

						// TODO：從下面插入處理程式碼。大自然創造的。

					} catch (Exception e) {
						e.printStackTrace();
					}
					return this;
				}

				@Override
				public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
					return new EnchantBonusT();
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
	}

	public static class BonusT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static BonusT newInstance() {
			return new BonusT();
		}

		private String _Token;
		private String _Value;
		private byte[] _Desc;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private BonusT() {
		}

		public String get_Token() {
			return _Token;
		}

		public void set_Token(String val) {
			_bit |= 0x1;
			_Token = val;
		}

		public boolean has_Token() {
			return (_bit & 0x1) == 0x1;
		}

		public String get_Value() {
			return _Value;
		}

		public void set_Value(String val) {
			_bit |= 0x2;
			_Value = val;
		}

		public boolean has_Value() {
			return (_bit & 0x2) == 0x2;
		}

		public byte[] get_Desc() {
			return _Desc;
		}

		public void set_Desc(byte[] val) {
			_bit |= 0x4;
			_Desc = val;
		}

		public boolean has_Desc() {
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
			if (has_Token()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _Token);
			}
			if (has_Value()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _Value);
			}
			if (has_Desc()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(3, _Desc);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_Token()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Value()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_Token()) {
				output.writeString(1, _Token);
			}
			if (has_Value()) {
				output.writeString(2, _Value);
			}
			if (has_Desc()) {
				output.writeBytes(3, _Desc);
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
						set_Token(input.readString());
						break;
					}
					case 0x00000012: {
						set_Value(input.readString());
						break;
					}
					case 0x0000001A: {
						set_Desc(input.readBytes());
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

				// TODO：從下面插入處理程式碼。大自然創造的。

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new BonusT();
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

	public static class GroupT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static GroupT newInstance() {
			return new GroupT();
		}

		private int _ID;
		private int _Desc;
		private TimeCollection.GroupT.LevelT _Level;
		private TimeCollection.GroupT.PeriodT _Period;
		private java.util.LinkedList<TimeCollection.GroupT.SetT> _Set;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private GroupT() {
		}

		public int get_ID() {
			return _ID;
		}

		public void set_ID(int val) {
			_bit |= 0x1;
			_ID = val;
		}

		public boolean has_ID() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_Desc() {
			return _Desc;
		}

		public void set_Desc(int val) {
			_bit |= 0x2;
			_Desc = val;
		}

		public boolean has_Desc() {
			return (_bit & 0x2) == 0x2;
		}

		public TimeCollection.GroupT.LevelT get_Level() {
			return _Level;
		}

		public void set_Level(TimeCollection.GroupT.LevelT val) {
			_bit |= 0x4;
			_Level = val;
		}

		public boolean has_Level() {
			return (_bit & 0x4) == 0x4;
		}

		public TimeCollection.GroupT.PeriodT get_Period() {
			return _Period;
		}

		public void set_Period(TimeCollection.GroupT.PeriodT val) {
			_bit |= 0x8;
			_Period = val;
		}

		public boolean has_Period() {
			return (_bit & 0x8) == 0x8;
		}

		public java.util.LinkedList<TimeCollection.GroupT.SetT> get_Set() {
			return _Set;
		}

		public void add_Set(TimeCollection.GroupT.SetT val) {
			if (!has_Set()) {
				_Set = new java.util.LinkedList<TimeCollection.GroupT.SetT>();
				_bit |= 0x10;
			}
			_Set.add(val);
		}

		public boolean has_Set() {
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
			if (has_ID()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _ID);
			}
			if (has_Desc()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _Desc);
			}
			if (has_Level()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _Level);
			}
			if (has_Period()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _Period);
			}
			if (has_Set()) {
				for (TimeCollection.GroupT.SetT val : _Set) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_ID()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_Desc()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (has_Set()) {
				for (TimeCollection.GroupT.SetT val : _Set) {
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
			if (has_ID()) {
				output.wirteInt32(1, _ID);
			}
			if (has_Desc()) {
				output.wirteInt32(2, _Desc);
			}
			if (has_Level()) {
				output.writeMessage(3, _Level);
			}
			if (has_Period()) {
				output.writeMessage(4, _Period);
			}
			if (has_Set()) {
				for (TimeCollection.GroupT.SetT val : _Set) {
					output.writeMessage(5, val);
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
						set_ID(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_Desc(input.readInt32());
						break;
					}
					case 0x0000001A: {
						set_Level((TimeCollection.GroupT.LevelT) input
								.readMessage(TimeCollection.GroupT.LevelT.newInstance()));
						break;
					}
					case 0x00000022: {
						set_Period((TimeCollection.GroupT.PeriodT) input
								.readMessage(TimeCollection.GroupT.PeriodT.newInstance()));
						break;
					}
					case 0x0000002A: {
						add_Set((TimeCollection.GroupT.SetT) input
								.readMessage(TimeCollection.GroupT.SetT.newInstance()));
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

				// TODO：從下面插入處理程式碼。大自然創造的。

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
			return new GroupT();
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

		public static class LevelT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
			public static LevelT newInstance() {
				return new LevelT();
			}

			private int _LevelMin;
			private int _LevelMax;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;

			private LevelT() {
			}

			public int get_LevelMin() {
				return _LevelMin;
			}

			public void set_LevelMin(int val) {
				_bit |= 0x1;
				_LevelMin = val;
			}

			public boolean has_LevelMin() {
				return (_bit & 0x1) == 0x1;
			}

			public int get_LevelMax() {
				return _LevelMax;
			}

			public void set_LevelMax(int val) {
				_bit |= 0x2;
				_LevelMax = val;
			}

			public boolean has_LevelMax() {
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
				if (has_LevelMin()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _LevelMin);
				}
				if (has_LevelMax()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _LevelMax);
				}
				_memorizedSerializedSize = size;
				return size;
			}

			@Override
			public boolean isInitialized() {
				if (_memorizedIsInitialized == 1)
					return true;
				if (!has_LevelMin()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_LevelMax()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}

			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
				if (has_LevelMin()) {
					output.wirteInt32(1, _LevelMin);
				}
				if (has_LevelMax()) {
					output.wirteInt32(2, _LevelMax);
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
							set_LevelMin(input.readInt32());
							break;
						}
						case 0x00000010: {
							set_LevelMax(input.readInt32());
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
			public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt,
					byte[] bytes) {
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

					// TODO：從下面插入處理程式碼。大自然創造的。

				} catch (Exception e) {
					e.printStackTrace();
				}
				return this;
			}

			@Override
			public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
				return new LevelT();
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

		public static class PeriodT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
			public static PeriodT newInstance() {
				return new PeriodT();
			}

			private String _StartDate;
			private String _EndDate;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;

			private PeriodT() {
			}

			public String get_StartDate() {
				return _StartDate;
			}

			public void set_StartDate(String val) {
				_bit |= 0x1;
				_StartDate = val;
			}

			public boolean has_StartDate() {
				return (_bit & 0x1) == 0x1;
			}

			public String get_EndDate() {
				return _EndDate;
			}

			public void set_EndDate(String val) {
				_bit |= 0x2;
				_EndDate = val;
			}

			public boolean has_EndDate() {
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
				if (has_StartDate()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _StartDate);
				}
				if (has_EndDate()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _EndDate);
				}
				_memorizedSerializedSize = size;
				return size;
			}

			@Override
			public boolean isInitialized() {
				if (_memorizedIsInitialized == 1)
					return true;
				if (!has_StartDate()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_EndDate()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}

			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
				if (has_StartDate()) {
					output.writeString(1, _StartDate);
				}
				if (has_EndDate()) {
					output.writeString(2, _EndDate);
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
							set_StartDate(input.readString());
							break;
						}
						case 0x00000012: {
							set_EndDate(input.readString());
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
			public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt,
					byte[] bytes) {
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

					// TODO：從下面插入處理程式碼。大自然創造的。

				} catch (Exception e) {
					e.printStackTrace();
				}
				return this;
			}

			@Override
			public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
				return new PeriodT();
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

		public static class SetT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
			public static SetT newInstance() {
				return new SetT();
			}

			private int _ID;
			private int _Desc;
			private String _DefaultTime;
			private int _Recycle;
			private java.util.LinkedList<TimeCollection.GroupT.SetT.ItemSlotT> _ItemSlot;
			private java.util.LinkedList<TimeCollection.GroupT.SetT.BuffTypeT> _BuffType;
			private boolean _EndBonus;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;

			private SetT() {
			}

			public int get_ID() {
				return _ID;
			}

			public void set_ID(int val) {
				_bit |= 0x1;
				_ID = val;
			}

			public boolean has_ID() {
				return (_bit & 0x1) == 0x1;
			}

			public int get_Desc() {
				return _Desc;
			}

			public void set_Desc(int val) {
				_bit |= 0x2;
				_Desc = val;
			}

			public boolean has_Desc() {
				return (_bit & 0x2) == 0x2;
			}

			public String get_DefaultTime() {
				return _DefaultTime;
			}

			public void set_DefaultTime(String val) {
				_bit |= 0x4;
				_DefaultTime = val;
			}

			public boolean has_DefaultTime() {
				return (_bit & 0x4) == 0x4;
			}

			public int get_Recycle() {
				return _Recycle;
			}

			public void set_Recycle(int val) {
				_bit |= 0x8;
				_Recycle = val;
			}

			public boolean has_Recycle() {
				return (_bit & 0x8) == 0x8;
			}

			public java.util.LinkedList<TimeCollection.GroupT.SetT.ItemSlotT> get_ItemSlot() {
				return _ItemSlot;
			}

			public void add_ItemSlot(TimeCollection.GroupT.SetT.ItemSlotT val) {
				if (!has_ItemSlot()) {
					_ItemSlot = new java.util.LinkedList<TimeCollection.GroupT.SetT.ItemSlotT>();
					_bit |= 0x10;
				}
				_ItemSlot.add(val);
			}

			public boolean has_ItemSlot() {
				return (_bit & 0x10) == 0x10;
			}

			public java.util.LinkedList<TimeCollection.GroupT.SetT.BuffTypeT> get_BuffType() {
				return _BuffType;
			}

			public void add_BuffType(TimeCollection.GroupT.SetT.BuffTypeT val) {
				if (!has_BuffType()) {
					_BuffType = new java.util.LinkedList<TimeCollection.GroupT.SetT.BuffTypeT>();
					_bit |= 0x20;
				}
				_BuffType.add(val);
			}

			public boolean has_BuffType() {
				return (_bit & 0x20) == 0x20;
			}

			public boolean get_EndBonus() {
				return _EndBonus;
			}

			public void set_EndBonus(boolean val) {
				_bit |= 0x40;
				_EndBonus = val;
			}

			public boolean has_EndBonus() {
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
				if (has_ID()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _ID);
				}
				if (has_Desc()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _Desc);
				}
				if (has_DefaultTime()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _DefaultTime);
				}
				if (has_Recycle()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _Recycle);
				}
				if (has_ItemSlot()) {
					for (TimeCollection.GroupT.SetT.ItemSlotT val : _ItemSlot) {
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, val);
					}
				}
				if (has_BuffType()) {
					for (TimeCollection.GroupT.SetT.BuffTypeT val : _BuffType) {
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(6, val);
					}
				}
				if (has_EndBonus()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(7, _EndBonus);
				}
				_memorizedSerializedSize = size;
				return size;
			}

			@Override
			public boolean isInitialized() {
				if (_memorizedIsInitialized == 1)
					return true;
				if (!has_ID()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_Desc()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_DefaultTime()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_ItemSlot()) {
					for (TimeCollection.GroupT.SetT.ItemSlotT val : _ItemSlot) {
						if (!val.isInitialized()) {
							_memorizedIsInitialized = -1;
							return false;
						}
					}
				}
				if (has_BuffType()) {
					for (TimeCollection.GroupT.SetT.BuffTypeT val : _BuffType) {
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
				if (has_ID()) {
					output.wirteInt32(1, _ID);
				}
				if (has_Desc()) {
					output.wirteInt32(2, _Desc);
				}
				if (has_DefaultTime()) {
					output.writeString(3, _DefaultTime);
				}
				if (has_Recycle()) {
					output.wirteInt32(4, _Recycle);
				}
				if (has_ItemSlot()) {
					for (TimeCollection.GroupT.SetT.ItemSlotT val : _ItemSlot) {
						output.writeMessage(5, val);
					}
				}
				if (has_BuffType()) {
					for (TimeCollection.GroupT.SetT.BuffTypeT val : _BuffType) {
						output.writeMessage(6, val);
					}
				}
				if (has_EndBonus()) {
					output.writeBool(7, _EndBonus);
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
							set_ID(input.readInt32());
							break;
						}
						case 0x00000010: {
							set_Desc(input.readInt32());
							break;
						}
						case 0x0000001A: {
							set_DefaultTime(input.readString());
							break;
						}
						case 0x00000020: {
							set_Recycle(input.readInt32());
							break;
						}
						case 0x0000002A: {
							add_ItemSlot((TimeCollection.GroupT.SetT.ItemSlotT) input
									.readMessage(TimeCollection.GroupT.SetT.ItemSlotT.newInstance()));
							break;
						}
						case 0x00000032: {
							add_BuffType((TimeCollection.GroupT.SetT.BuffTypeT) input
									.readMessage(TimeCollection.GroupT.SetT.BuffTypeT.newInstance()));
							break;
						}
						case 0x00000038: {
							set_EndBonus(input.readBool());
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
			public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt,
					byte[] bytes) {
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

					// TODO：從下面插入處理程式碼。大自然創造的。

				} catch (Exception e) {
					e.printStackTrace();
				}
				return this;
			}

			@Override
			public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
				return new SetT();
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

			public static class ItemSlotT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
				public static ItemSlotT newInstance() {
					return new ItemSlotT();
				}

				private int _Slot;
				private java.util.LinkedList<Integer> _NameId;
				private int _Bless;
				private int _EnchantID;
				private int _Count;
				private java.util.LinkedList<byte[]> _extra_desc;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;

				private ItemSlotT() {
				}

				public int get_Slot() {
					return _Slot;
				}

				public void set_Slot(int val) {
					_bit |= 0x1;
					_Slot = val;
				}

				public boolean has_Slot() {
					return (_bit & 0x1) == 0x1;
				}

				public java.util.LinkedList<Integer> get_NameId() {
					return _NameId;
				}

				public void add_NameId(int val) {
					if (!has_NameId()) {
						_NameId = new java.util.LinkedList<Integer>();
						_bit |= 0x2;
					}
					_NameId.add(val);
				}

				public boolean has_NameId() {
					return (_bit & 0x2) == 0x2;
				}

				public int get_Bless() {
					return _Bless;
				}

				public void set_Bless(int val) {
					_bit |= 0x4;
					_Bless = val;
				}

				public boolean has_Bless() {
					return (_bit & 0x4) == 0x4;
				}

				public int get_EnchantID() {
					return _EnchantID;
				}

				public void set_EnchantID(int val) {
					_bit |= 0x8;
					_EnchantID = val;
				}

				public boolean has_EnchantID() {
					return (_bit & 0x8) == 0x8;
				}

				public int get_Count() {
					return _Count;
				}

				public void set_Count(int val) {
					_bit |= 0x10;
					_Count = val;
				}

				public boolean has_Count() {
					return (_bit & 0x10) == 0x10;
				}

				public java.util.LinkedList<byte[]> get_extra_desc() {
					return _extra_desc;
				}

				public void add_extra_desc(byte[] val) {
					if (!has_extra_desc()) {
						_extra_desc = new java.util.LinkedList<byte[]>();
						_bit |= 0x20;
					}
					_extra_desc.add(val);
				}

				public boolean has_extra_desc() {
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
					if (has_Slot()) {
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _Slot);
					}
					if (has_NameId()) {
						for (int val : _NameId) {
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, val);
						}
					}
					if (has_Bless()) {
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _Bless);
					}
					if (has_EnchantID()) {
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _EnchantID);
					}
					if (has_Count()) {
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _Count);
					}
					if (has_extra_desc()) {
						for (byte[] val : _extra_desc) {
							size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(6, val);
						}
					}
					_memorizedSerializedSize = size;
					return size;
				}

				@Override
				public boolean isInitialized() {
					if (_memorizedIsInitialized == 1)
						return true;
					if (!has_Slot()) {
						_memorizedIsInitialized = -1;
						return false;
					}
					if (has_NameId()) {
						for (int val : _NameId) {
							// if (!val.isInitialized()){
							_memorizedIsInitialized = -1;
							return false;
							// }
						}
					}
					if (!has_Bless()) {
						_memorizedIsInitialized = -1;
						return false;
					}
					if (has_extra_desc()) {
						for (byte[] val : _extra_desc) {
							// if (!val.isInitialized()){
							_memorizedIsInitialized = -1;
							return false;
							// }
						}
					}
					_memorizedIsInitialized = 1;
					return true;
				}

				@Override
				public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output)
						throws java.io.IOException {
					if (has_Slot()) {
						output.wirteInt32(1, _Slot);
					}
					if (has_NameId()) {
						for (int val : _NameId) {
							output.wirteInt32(2, val);
						}
					}
					if (has_Bless()) {
						output.wirteInt32(3, _Bless);
					}
					if (has_EnchantID()) {
						output.wirteInt32(4, _EnchantID);
					}
					if (has_Count()) {
						output.wirteInt32(5, _Count);
					}
					if (has_extra_desc()) {
						for (byte[] val : _extra_desc) {
							output.writeBytes(6, val);
						}
					}
				}

				@Override
				public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
						l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
					l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
							.newInstance(
									getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
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
								set_Slot(input.readInt32());
								break;
							}
							case 0x00000010: {
								add_NameId(input.readInt32());
								break;
							}
							case 0x00000018: {
								set_Bless(input.readInt32());
								break;
							}
							case 0x00000020: {
								set_EnchantID(input.readInt32());
								break;
							}
							case 0x00000028: {
								set_Count(input.readInt32());
								break;
							}
							case 0x00000032: {
								add_extra_desc(input.readBytes());
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
				public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt,
						byte[] bytes) {
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

						// TODO：從下面插入處理程式碼。大自然創造的。

					} catch (Exception e) {
						e.printStackTrace();
					}
					return this;
				}

				@Override
				public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
					return new ItemSlotT();
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

			public static class BuffTypeT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
				public static BuffTypeT newInstance() {
					return new BuffTypeT();
				}

				private int _Type;
				private TimeCollection.GroupT.SetT.BuffTypeT.SetBuffT _SetBuff;
				private TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT _EnchantBuffList;
				private int _memorizedSerializedSize = -1;
				private byte _memorizedIsInitialized = -1;
				private int _bit;

				private BuffTypeT() {
				}

				public int get_Type() {
					return _Type;
				}

				public void set_Type(int val) {
					_bit |= 0x1;
					_Type = val;
				}

				public boolean has_Type() {
					return (_bit & 0x1) == 0x1;
				}

				public TimeCollection.GroupT.SetT.BuffTypeT.SetBuffT get_SetBuff() {
					return _SetBuff;
				}

				public void set_SetBuff(TimeCollection.GroupT.SetT.BuffTypeT.SetBuffT val) {
					_bit |= 0x2;
					_SetBuff = val;
				}

				public boolean has_SetBuff() {
					return (_bit & 0x2) == 0x2;
				}

				public TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT get_EnchantBuffList() {
					return _EnchantBuffList;
				}

				public void set_EnchantBuffList(TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT val) {
					_bit |= 0x4;
					_EnchantBuffList = val;
				}

				public boolean has_EnchantBuffList() {
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
					if (has_Type()) {
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _Type);
					}
					if (has_SetBuff()) {
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _SetBuff);
					}
					if (has_EnchantBuffList()) {
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3,
								_EnchantBuffList);
					}
					_memorizedSerializedSize = size;
					return size;
				}

				@Override
				public boolean isInitialized() {
					if (_memorizedIsInitialized == 1)
						return true;
					if (!has_Type()) {
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_SetBuff()) {
						_memorizedIsInitialized = -1;
						return false;
					}
					if (!has_EnchantBuffList()) {
						_memorizedIsInitialized = -1;
						return false;
					}
					_memorizedIsInitialized = 1;
					return true;
				}

				@Override
				public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output)
						throws java.io.IOException {
					if (has_Type()) {
						output.wirteInt32(1, _Type);
					}
					if (has_SetBuff()) {
						output.writeMessage(2, _SetBuff);
					}
					if (has_EnchantBuffList()) {
						output.writeMessage(3, _EnchantBuffList);
					}
				}

				@Override
				public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
						l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
					l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
							.newInstance(
									getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
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
								set_Type(input.readInt32());
								break;
							}
							case 0x00000012: {
								set_SetBuff((TimeCollection.GroupT.SetT.BuffTypeT.SetBuffT) input
										.readMessage(TimeCollection.GroupT.SetT.BuffTypeT.SetBuffT.newInstance()));
								break;
							}
							case 0x0000001A: {
								set_EnchantBuffList(
										(TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT) input.readMessage(
												TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.newInstance()));
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
				public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt,
						byte[] bytes) {
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

						// TODO：從下面插入處理程式碼。大自然創造的。

					} catch (Exception e) {
						e.printStackTrace();
					}
					return this;
				}

				@Override
				public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
					return new BuffTypeT();
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

				public static class SetBuffT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
					public static SetBuffT newInstance() {
						return new SetBuffT();
					}

					private java.util.LinkedList<TimeCollection.BonusT> _Bonus;
					private int _memorizedSerializedSize = -1;
					private byte _memorizedIsInitialized = -1;
					private int _bit;

					private SetBuffT() {
					}

					public java.util.LinkedList<TimeCollection.BonusT> get_Bonus() {
						return _Bonus;
					}

					public void add_Bonus(TimeCollection.BonusT val) {
						if (!has_Bonus()) {
							_Bonus = new java.util.LinkedList<TimeCollection.BonusT>();
							_bit |= 0x1;
						}
						_Bonus.add(val);
					}

					public boolean has_Bonus() {
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
						if (has_Bonus()) {
							for (TimeCollection.BonusT val : _Bonus) {
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
						if (has_Bonus()) {
							for (TimeCollection.BonusT val : _Bonus) {
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
					public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output)
							throws java.io.IOException {
						if (has_Bonus()) {
							for (TimeCollection.BonusT val : _Bonus) {
								output.writeMessage(1, val);
							}
						}
					}

					@Override
					public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
							l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
						l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
								.newInstance(
										getSerializedSize()
												+ l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
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
									add_Bonus((TimeCollection.BonusT) input
											.readMessage(TimeCollection.BonusT.newInstance()));
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
					public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt,
							byte[] bytes) {
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

							// TODO：從下面插入處理程式碼。大自然創造的。

						} catch (Exception e) {
							e.printStackTrace();
						}
						return this;
					}

					@Override
					public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
						return new SetBuffT();
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

				public static class EnchantBuffListT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
					public static EnchantBuffListT newInstance() {
						return new EnchantBuffListT();
					}

					private java.util.LinkedList<TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT> _EnchantBuff;
					private int _memorizedSerializedSize = -1;
					private byte _memorizedIsInitialized = -1;
					private int _bit;

					private EnchantBuffListT() {
					}

					public java.util.LinkedList<TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT> get_EnchantBuff() {
						return _EnchantBuff;
					}

					public void add_EnchantBuff(
							TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT val) {
						if (!has_EnchantBuff()) {
							_EnchantBuff = new java.util.LinkedList<TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT>();
							_bit |= 0x1;
						}
						_EnchantBuff.add(val);
					}

					public boolean has_EnchantBuff() {
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
						if (has_EnchantBuff()) {
							for (TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT val : _EnchantBuff) {
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
						if (has_EnchantBuff()) {
							for (TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT val : _EnchantBuff) {
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
					public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output)
							throws java.io.IOException {
						if (has_EnchantBuff()) {
							for (TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT val : _EnchantBuff) {
								output.writeMessage(1, val);
							}
						}
					}

					@Override
					public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
							l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
						l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
								.newInstance(
										getSerializedSize()
												+ l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
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
									add_EnchantBuff(
											(TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT) input
													.readMessage(
															TimeCollection.GroupT.SetT.BuffTypeT.EnchantBuffListT.EnchantBuffT
																	.newInstance()));
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
					public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt,
							byte[] bytes) {
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

							// TODO：從下面插入處理程式碼。大自然創造的。

						} catch (Exception e) {
							e.printStackTrace();
						}
						return this;
					}

					@Override
					public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
						return new EnchantBuffListT();
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

					public static class EnchantBuffT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
						public static EnchantBuffT newInstance() {
							return new EnchantBuffT();
						}

						private int _TotalEnchant;
						private java.util.LinkedList<TimeCollection.BonusT> _Bonus;
						private int _memorizedSerializedSize = -1;
						private byte _memorizedIsInitialized = -1;
						private int _bit;

						private EnchantBuffT() {
						}

						public int get_TotalEnchant() {
							return _TotalEnchant;
						}

						public void set_TotalEnchant(int val) {
							_bit |= 0x1;
							_TotalEnchant = val;
						}

						public boolean has_TotalEnchant() {
							return (_bit & 0x1) == 0x1;
						}

						public java.util.LinkedList<TimeCollection.BonusT> get_Bonus() {
							return _Bonus;
						}

						public void add_Bonus(TimeCollection.BonusT val) {
							if (!has_Bonus()) {
								_Bonus = new java.util.LinkedList<TimeCollection.BonusT>();
								_bit |= 0x2;
							}
							_Bonus.add(val);
						}

						public boolean has_Bonus() {
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
							if (has_TotalEnchant()) {
								size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1,
										_TotalEnchant);
							}
							if (has_Bonus()) {
								for (TimeCollection.BonusT val : _Bonus) {
									size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2,
											val);
								}
							}
							_memorizedSerializedSize = size;
							return size;
						}

						@Override
						public boolean isInitialized() {
							if (_memorizedIsInitialized == 1)
								return true;
							if (!has_TotalEnchant()) {
								_memorizedIsInitialized = -1;
								return false;
							}
							if (has_Bonus()) {
								for (TimeCollection.BonusT val : _Bonus) {
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
						public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output)
								throws java.io.IOException {
							if (has_TotalEnchant()) {
								output.wirteInt32(1, _TotalEnchant);
							}
							if (has_Bonus()) {
								for (TimeCollection.BonusT val : _Bonus) {
									output.writeMessage(2, val);
								}
							}
						}

						@Override
						public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
								l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
							l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
									.newInstance(
											getSerializedSize()
													+ l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE,
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
										set_TotalEnchant(input.readInt32());
										break;
									}
									case 0x00000012: {
										add_Bonus((TimeCollection.BonusT) input
												.readMessage(TimeCollection.BonusT.newInstance()));
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
						public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt,
								byte[] bytes) {
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

								// TODO：從下面插入處理程式碼。大自然創造的。

							} catch (Exception e) {
								e.printStackTrace();
							}
							return this;
						}

						@Override
						public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
							return new EnchantBuffT();
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
			}
		}
	}
}
