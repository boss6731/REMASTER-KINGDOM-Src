package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPEED_BONUS_NOTI.Bonus.Value;
import l1j.server.MJTemplate.MJProto.MainServer_Client.SC_SPEED_BONUS_NOTI.Bonus.eKind;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_SPEED_BONUS_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	// 랜서 추가
	public static ProtoOutputStream speed_send(L1PcInstance pc, eKind kind, int speed) {
		SC_SPEED_BONUS_NOTI noti = newInstance();
		Bonus bonus = Bonus.newInstance();
		Value value = Value.newInstance();
		noti.set_objectnumber(pc.getId());
		value.set_kind(kind);
		value.set_value(speed);
		bonus.add_speedBonus(value);
		noti.set_bonus(bonus);

		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_SPEED_BONUS_NOTI);
		noti.dispose();
		return stream;
	}

	public static SC_SPEED_BONUS_NOTI newInstance() {
		return new SC_SPEED_BONUS_NOTI();
	}

	private int _objectnumber;
	private SC_SPEED_BONUS_NOTI.Bonus _bonus;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_SPEED_BONUS_NOTI() {
	}

	public int get_objectnumber() {
		return _objectnumber;
	}

	public void set_objectnumber(int val) {
		_bit |= 0x1;
		_objectnumber = val;
	}

	public boolean has_objectnumber() {
		return (_bit & 0x1) == 0x1;
	}

	public SC_SPEED_BONUS_NOTI.Bonus get_bonus() {
		return _bonus;
	}

	public void set_bonus(SC_SPEED_BONUS_NOTI.Bonus val) {
		_bit |= 0x2;
		_bonus = val;
	}

	public boolean has_bonus() {
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
		if (has_objectnumber()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _objectnumber);
		}
		if (has_bonus()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _bonus);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_objectnumber()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_bonus()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_objectnumber()) {
			output.writeUInt32(1, _objectnumber);
		}
		if (has_bonus()) {
			output.writeMessage(2, _bonus);
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
					set_objectnumber(input.readUInt32());
					break;
				}
				case 0x00000012: {
					set_bonus((SC_SPEED_BONUS_NOTI.Bonus) input.readMessage(SC_SPEED_BONUS_NOTI.Bonus.newInstance()));
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
		return new SC_SPEED_BONUS_NOTI();
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

	public static class Bonus implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static Bonus newInstance() {
			return new Bonus();
		}

		private java.util.LinkedList<SC_SPEED_BONUS_NOTI.Bonus.Value> _speedBonus;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private Bonus() {
		}

		public java.util.LinkedList<SC_SPEED_BONUS_NOTI.Bonus.Value> get_speedBonus() {
			return _speedBonus;
		}

		public void add_speedBonus(SC_SPEED_BONUS_NOTI.Bonus.Value val) {
			if (!has_speedBonus()) {
				_speedBonus = new java.util.LinkedList<SC_SPEED_BONUS_NOTI.Bonus.Value>();
				_bit |= 0x1;
			}
			_speedBonus.add(val);
		}

		public boolean has_speedBonus() {
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
			if (has_speedBonus()) {
				for (SC_SPEED_BONUS_NOTI.Bonus.Value val : _speedBonus) {
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
			if (has_speedBonus()) {
				for (SC_SPEED_BONUS_NOTI.Bonus.Value val : _speedBonus) {
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
			if (has_speedBonus()) {
				for (SC_SPEED_BONUS_NOTI.Bonus.Value val : _speedBonus) {
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
						add_speedBonus((SC_SPEED_BONUS_NOTI.Bonus.Value) input
								.readMessage(SC_SPEED_BONUS_NOTI.Bonus.Value.newInstance()));
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
			return new Bonus();
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

		public static class Value implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
			public static Value newInstance() {
				return new Value();
			}

			private SC_SPEED_BONUS_NOTI.Bonus.eKind _kind;
			private int _value;
			private int _memorizedSerializedSize = -1;
			private byte _memorizedIsInitialized = -1;
			private int _bit;

			private Value() {
			}

			public SC_SPEED_BONUS_NOTI.Bonus.eKind get_kind() {
				return _kind;
			}

			public void set_kind(SC_SPEED_BONUS_NOTI.Bonus.eKind val) {
				_bit |= 0x1;
				_kind = val;
			}

			public boolean has_kind() {
				return (_bit & 0x1) == 0x1;
			}

			public int get_value() {
				return _value;
			}

			public void set_value(int val) {
				_bit |= 0x2;
				_value = val;
			}

			public boolean has_value() {
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
				if (has_kind()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _kind.toInt());
				}
				if (has_value()) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _value);
				}
				_memorizedSerializedSize = size;
				return size;
			}

			@Override
			public boolean isInitialized() {
				if (_memorizedIsInitialized == 1)
					return true;
				if (!has_kind()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				if (!has_value()) {
					_memorizedIsInitialized = -1;
					return false;
				}
				_memorizedIsInitialized = 1;
				return true;
			}

			@Override
			public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
				if (has_kind()) {
					output.writeEnum(1, _kind.toInt());
				}
				if (has_value()) {
					output.wirteInt32(2, _value);
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
							set_kind(SC_SPEED_BONUS_NOTI.Bonus.eKind.fromInt(input.readEnum()));
							break;
						}
						case 0x00000010: {
							set_value(input.readInt32());
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

					// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

				} catch (Exception e) {
					e.printStackTrace();
				}
				return this;
			}

			@Override
			public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
				return new Value();
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

		public enum eKind {
			MOVE_SPEED(0),
			ATTACK_SPEED(1),
			SPELL_SPEED(2),
			;

			private int value;

			eKind(int val) {
				value = val;
			}

			public int toInt() {
				return value;
			}

			public boolean equals(eKind v) {
				return value == v.value;
			}

			public static eKind fromInt(int i) {
				switch (i) {
					case 0:
						return MOVE_SPEED;
					case 1:
						return ATTACK_SPEED;
					case 2:
						return SPELL_SPEED;
					default:
						throw new IllegalArgumentException(String.format("無效參數 eKind，%d", i));
				}
			}
		}
	}
}
