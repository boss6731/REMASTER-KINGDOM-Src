package l1j.server.MJTemplate.MJProto.MainServer_Client;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_AVAILABLE_SPELL_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public void appendNewSpell(int skill_id, boolean type) {
		SpellInfo data = SpellInfo.newInstance();
		data.set_spell_id(skill_id - 1);
		data.set_is_available(type);
		add_spell_info(data);

		// data.set_mastery_rank(0);
		// System.out.println(data.get_mastery_rank());
	}

	public static SC_AVAILABLE_SPELL_NOTI newInstance() {
		return new SC_AVAILABLE_SPELL_NOTI();
	}

	private java.util.LinkedList<SC_AVAILABLE_SPELL_NOTI.SpellInfo> _spell_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_AVAILABLE_SPELL_NOTI() {
	}

	public java.util.LinkedList<SC_AVAILABLE_SPELL_NOTI.SpellInfo> get_spell_info() {
		return _spell_info;
	}

	public void add_spell_info(SC_AVAILABLE_SPELL_NOTI.SpellInfo val) {
		if (!has_spell_info()) {
			_spell_info = new java.util.LinkedList<SC_AVAILABLE_SPELL_NOTI.SpellInfo>();
			_bit |= 0x1;
		}
		_spell_info.add(val);
	}

	public boolean has_spell_info() {
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
		if (has_spell_info()) {
			for (SC_AVAILABLE_SPELL_NOTI.SpellInfo val : _spell_info) {
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
		if (has_spell_info()) {
			for (SC_AVAILABLE_SPELL_NOTI.SpellInfo val : _spell_info) {
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
		if (has_spell_info()) {
			for (SC_AVAILABLE_SPELL_NOTI.SpellInfo val : _spell_info) {
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
					add_spell_info((SC_AVAILABLE_SPELL_NOTI.SpellInfo) input
							.readMessage(SC_AVAILABLE_SPELL_NOTI.SpellInfo.newInstance()));
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
		return new SC_AVAILABLE_SPELL_NOTI();
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

	public static class SpellInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static SpellInfo newInstance() {
			return new SpellInfo();
		}

		private int _spell_id;
		private boolean _is_available;
		private int _mastery_rank;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private SpellInfo() {
		}

		public int get_spell_id() {
			return _spell_id;
		}

		public void set_spell_id(int val) {
			_bit |= 0x1;
			_spell_id = val;
		}

		public boolean has_spell_id() {
			return (_bit & 0x1) == 0x1;
		}

		public boolean get_is_available() {
			return _is_available;
		}

		public void set_is_available(boolean val) {
			_bit |= 0x2;
			_is_available = val;
		}

		public boolean has_is_available() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_mastery_rank() {
			return _mastery_rank;
		}

		public void set_mastery_rank(int val) {
			_bit |= 0x4;
			_mastery_rank = val;
		}

		public boolean has_mastery_rank() {
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
			if (has_spell_id()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _spell_id);
			}
			if (has_is_available()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _is_available);
			}
			if (has_mastery_rank()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _mastery_rank);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_spell_id()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_is_available()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_spell_id()) {
				output.wirteInt32(1, _spell_id);
			}
			if (has_is_available()) {
				output.writeBool(2, _is_available);
			}
			if (has_mastery_rank()) {
				output.wirteInt32(3, _mastery_rank);
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
						set_spell_id(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_is_available(input.readBool());
						break;
					}
					case 0x00000018: {
						set_mastery_rank(input.readInt32());
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
			return new SpellInfo();
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
