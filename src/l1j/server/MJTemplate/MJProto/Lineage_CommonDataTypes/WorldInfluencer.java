package l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes;

//TODO：自動產生 PROTO 程式碼。大自然創造的。
public class WorldInfluencer implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static WorldInfluencer newInstance() {
		return new WorldInfluencer();
	}

	private java.util.LinkedList<WorldInfluencer.InfluencerT> _Influencer;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private WorldInfluencer() {
	}

	public java.util.LinkedList<WorldInfluencer.InfluencerT> get_Influencer() {
		return _Influencer;
	}

	public void add_Influencer(WorldInfluencer.InfluencerT val) {
		if (!has_Influencer()) {
			_Influencer = new java.util.LinkedList<WorldInfluencer.InfluencerT>();
			_bit |= 0x1;
		}
		_Influencer.add(val);
	}

	public boolean has_Influencer() {
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
		if (has_Influencer()) {
			for (WorldInfluencer.InfluencerT val : _Influencer) {
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
		if (has_Influencer()) {
			for (WorldInfluencer.InfluencerT val : _Influencer) {
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
		if (has_Influencer()) {
			for (WorldInfluencer.InfluencerT val : _Influencer) {
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
					add_Influencer(
							(WorldInfluencer.InfluencerT) input.readMessage(WorldInfluencer.InfluencerT.newInstance()));
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
		return new WorldInfluencer();
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

	public static class InfluencerT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static InfluencerT newInstance() {
			return new InfluencerT();
		}

		private int _id;
		private int _intervalSec;
		private java.util.LinkedList<WorldInfluencer.InfluencerT.InfluenceT> _Influence;
		private java.util.LinkedList<WorldInfluencer.InfluencerT.DisplayT> _Display;
		private java.util.LinkedList<WorldInfluencer.InfluencerT.BoundT> _Include;
		private java.util.LinkedList<WorldInfluencer.InfluencerT.BoundT> _Exclude;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private InfluencerT() {
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

		public int get_intervalSec() {
			return _intervalSec;
		}

		public void set_intervalSec(int val) {
			_bit |= 0x2;
			_intervalSec = val;
		}

		public boolean has_intervalSec() {
			return (_bit & 0x2) == 0x2;
		}

		public java.util.LinkedList<WorldInfluencer.InfluencerT.InfluenceT> get_Influence() {
			return _Influence;
		}

		public void add_Influence(WorldInfluencer.InfluencerT.InfluenceT val) {
			if (!has_Influence()) {
				_Influence = new java.util.LinkedList<WorldInfluencer.InfluencerT.InfluenceT>();
				_bit |= 0x4;
			}
			_Influence.add(val);
		}

		public boolean has_Influence() {
			return (_bit & 0x4) == 0x4;
		}

		public java.util.LinkedList<WorldInfluencer.InfluencerT.DisplayT> get_Display() {
			return _Display;
		}

		public void add_Display(WorldInfluencer.InfluencerT.DisplayT val) {
			if (!has_Display()) {
				_Display = new java.util.LinkedList<WorldInfluencer.InfluencerT.DisplayT>();
				_bit |= 0x8;
			}
			_Display.add(val);
		}

		public boolean has_Display() {
			return (_bit & 0x8) == 0x8;
		}

		public java.util.LinkedList<WorldInfluencer.InfluencerT.BoundT> get_Include() {
			return _Include;
		}

		public void add_Include(WorldInfluencer.InfluencerT.BoundT val) {
			if (!has_Include()) {
				_Include = new java.util.LinkedList<WorldInfluencer.InfluencerT.BoundT>();
				_bit |= 0x10;
			}
			_Include.add(val);
		}

		public boolean has_Include() {
			return (_bit & 0x10) == 0x10;
		}

		public java.util.LinkedList<WorldInfluencer.InfluencerT.BoundT> get_Exclude() {
			return _Exclude;
		}

		public void add_Exclude(WorldInfluencer.InfluencerT.BoundT val) {
			if (!has_Exclude()) {
				_Exclude = new java.util.LinkedList<WorldInfluencer.InfluencerT.BoundT>();
				_bit |= 0x20;
			}
			_Exclude.add(val);
		}

		public boolean has_Exclude() {
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
			if (has_id()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _id);
			}
			if (has_intervalSec()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _intervalSec);
			}
			if (has_Influence()) {
				for (WorldInfluencer.InfluencerT.InfluenceT val : _Influence) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
				}
			}
			if (has_Display()) {
				for (WorldInfluencer.InfluencerT.DisplayT val : _Display) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
				}
			}
			if (has_Include()) {
				for (WorldInfluencer.InfluencerT.BoundT val : _Include) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, val);
				}
			}
			if (has_Exclude()) {
				for (WorldInfluencer.InfluencerT.BoundT val : _Exclude) {
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
			if (has_Influence()) {
				for (WorldInfluencer.InfluencerT.InfluenceT val : _Influence) {
					if (!val.isInitialized()) {
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_Display()) {
				for (WorldInfluencer.InfluencerT.DisplayT val : _Display) {
					if (!val.isInitialized()) {
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_Include()) {
				for (WorldInfluencer.InfluencerT.BoundT val : _Include) {
					if (!val.isInitialized()) {
						_memorizedIsInitialized = -1;
						return false;
					}
				}
			}
			if (has_Exclude()) {
				for (WorldInfluencer.InfluencerT.BoundT val : _Exclude) {
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
			if (has_id()) {
				output.writeUInt32(1, _id);
			}
			if (has_intervalSec()) {
				output.writeUInt32(2, _intervalSec);
			}
			if (has_Influence()) {
				for (WorldInfluencer.InfluencerT.InfluenceT val : _Influence) {
					output.writeMessage(3, val);
				}
			}
			if (has_Display()) {
				for (WorldInfluencer.InfluencerT.DisplayT val : _Display) {
					output.writeMessage(4, val);
				}
			}
			if (has_Include()) {
				for (WorldInfluencer.InfluencerT.BoundT val : _Include) {
					output.writeMessage(5, val);
				}
			}
			if (has_Exclude()) {
				for (WorldInfluencer.InfluencerT.BoundT val : _Exclude) {
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
						set_id(input.readUInt32());
						break;
					}
					case 0x00000010: {
						set_intervalSec(input.readUInt32());
						break;
					}
					case 0x0000001A: {
						add_Influence((WorldInfluencer.InfluencerT.InfluenceT) input
								.readMessage(WorldInfluencer.InfluencerT.InfluenceT.newInstance()));
						break;
					}
					case 0x00000022: {
						add_Display((WorldInfluencer.InfluencerT.DisplayT) input
								.readMessage(WorldInfluencer.InfluencerT.DisplayT.newInstance()));
						break;
					}
					case 0x0000002A: {
						add_Include((WorldInfluencer.InfluencerT.BoundT) input
								.readMessage(WorldInfluencer.InfluencerT.BoundT.newInstance()));
						break;
					}
					case 0x00000032: {
						add_Exclude((WorldInfluencer.InfluencerT.BoundT) input
								.readMessage(WorldInfluencer.InfluencerT.BoundT.newInstance()));
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
			return new InfluencerT();
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

		public static class InfluenceT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
			public static InfluenceT newInstance() {
				return new InfluenceT();
			}

			private WorldInfluencer.InfluencerT.InfluenceT.eType _type;
			private int _base;
			private java.util.LinkedList<Integer> _dice;
			private int _spellId;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;

			private InfluenceT() {
			}

			public WorldInfluencer.InfluencerT.InfluenceT.eType get_type() {
				return _type;
			}

			public void set_type(WorldInfluencer.InfluencerT.InfluenceT.eType val) {
				_bit |= 0x1;
				_type = val;
			}

			public boolean has_type() {
				return (_bit & 0x1) == 0x1;
			}

			public int get_base() {
				return _base;
			}

			public void set_base(int val) {
				_bit |= 0x2;
				_base = val;
			}

			public boolean has_base() {
				return (_bit & 0x2) == 0x2;
			}

			public java.util.LinkedList<Integer> get_dice() {
				return _dice;
			}

			public void add_dice(int val) {
				if (!has_dice()) {
					_dice = new java.util.LinkedList<Integer>();
					_bit |= 0x4;
				}
				_dice.add(val);
			}

			public boolean has_dice() {
				return (_bit & 0x4) == 0x4;
			}

			public int get_spellId() {
				return _spellId;
			}

			public void set_spellId(int val) {
				_bit |= 0x8;
				_spellId = val;
			}

			public boolean has_spellId() {
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
				if (has_type()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _type.toInt());
				}
				if (has_base()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _base);
				}
				if (has_dice()) {
					for (int val : _dice) {
						size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, val);
					}
				}
				if (has_spellId()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _spellId);
				}
				_memorizedSerializedSize = size;
				return size;
			}

			@Override
			public boolean isInitialized() {
				if (_memorizedIsInitialized == 1)
					return true;
				if (!has_type()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				if (has_dice()) {
					for (int val : _dice) {
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
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
				if (has_type()) {
					output.writeEnum(1, _type.toInt());
				}
				if (has_base()) {
					output.wirteInt32(2, _base);
				}
				if (has_dice()) {
					for (int val : _dice) {
						output.wirteInt32(3, val);
					}
				}
				if (has_spellId()) {
					output.wirteInt32(4, _spellId);
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
							set_type(WorldInfluencer.InfluencerT.InfluenceT.eType.fromInt(input.readEnum()));
							break;
						}
						case 0x00000010: {
							set_base(input.readInt32());
							break;
						}
						case 0x00000018: {
							add_dice(input.readInt32());
							break;
						}
						case 0x00000020: {
							set_spellId(input.readInt32());
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
				return new InfluenceT();
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

			public enum eType {
				Damage(1),
				CastSpell(2),
				;

				private int value;

				eType(int val) {
					value = val;
				}

				public int toInt() {
					return value;
				}

				public boolean equals(eType v) {
					return value == v.value;
				}

				public static eType fromInt(int i) {
					switch (i) {
						case 1:
							return Damage;
						case 2:
							return CastSpell;
						default:
							throw new IllegalArgumentException(String.format("無效參數 eType，%d", i));
					}
				}
			}
		}

		public static class DisplayT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
			public static DisplayT newInstance() {
				return new DisplayT();
			}

			private WorldInfluencer.InfluencerT.DisplayT.eType _type;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;

			private DisplayT() {
			}

			public WorldInfluencer.InfluencerT.DisplayT.eType get_type() {
				return _type;
			}

			public void set_type(WorldInfluencer.InfluencerT.DisplayT.eType val) {
				_bit |= 0x1;
				_type = val;
			}

			public boolean has_type() {
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
				if (has_type()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _type.toInt());
				}
				_memorizedSerializedSize = size;
				return size;
			}

			@Override
			public boolean isInitialized() {
				if (_memorizedIsInitialized == 1)
					return true;
				if (!has_type()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}

			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
				if (has_type()) {
					output.writeEnum(1, _type.toInt());
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
							set_type(WorldInfluencer.InfluencerT.DisplayT.eType.fromInt(input.readEnum()));
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
				return new DisplayT();
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

			public enum eType {
				Poison(1),
				Fire(2),
				;

				private int value;

				eType(int val) {
					value = val;
				}

				public int toInt() {
					return value;
				}

				public boolean equals(eType v) {
					return value == v.value;
				}

				public static eType fromInt(int i) {
					switch (i) {
						case 1:
							return Poison;
						case 2:
							return Fire;
						default:
							throw new IllegalArgumentException(String.format("無效參數 eType，%d", i));
					}
				}
			}
		}

		public static class BoundT implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
			public static BoundT newInstance() {
				return new BoundT();
			}

			private String _bound;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;

			private BoundT() {
			}

			public String get_bound() {
				return _bound;
			}

			public void set_bound(String val) {
				_bit |= 0x1;
				_bound = val;
			}

			public boolean has_bound() {
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
				if (has_bound()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _bound);
				}
				_memorizedSerializedSize = size;
				return size;
			}

			@Override
			public boolean isInitialized() {
				if (_memorizedIsInitialized == 1)
					return true;
				if (!has_bound()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}

			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
				if (has_bound()) {
					output.writeString(1, _bound);
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
							set_bound(input.readString());
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
				return new BoundT();
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
