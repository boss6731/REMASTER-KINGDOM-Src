package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ACTIVE_SPELL_EX_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void appendNewSpell(L1PcInstance pc, int spellId, int spellType) {
		SC_ACTIVE_SPELL_EX_NOTI noti = SC_ACTIVE_SPELL_EX_NOTI.newInstance();
		noti.add_spells(newSpellEx(spellId, spellType));
		// pc.sendPackets(noti, MJEProtoMessages.SC_ACTIVE_SPELL_EX_NOTI, true);
	}

	public static SC_ACTIVE_SPELL_EX_NOTI.spellex newSpellEx(int spellId, int spellType) {
		SC_ACTIVE_SPELL_EX_NOTI.spellex spell = SC_ACTIVE_SPELL_EX_NOTI.spellex.newInstance();
		spell.set_spellid(spellId - 1);
		spell.set_spelltype(spellType);
		return spell;
	}

	public static SC_ACTIVE_SPELL_EX_NOTI newInstance() {
		return new SC_ACTIVE_SPELL_EX_NOTI();
	}

	private java.util.LinkedList<SC_ACTIVE_SPELL_EX_NOTI.spellex> _spells;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_ACTIVE_SPELL_EX_NOTI() {
	}

	public java.util.LinkedList<SC_ACTIVE_SPELL_EX_NOTI.spellex> get_spells() {
		return _spells;
	}

	public void add_spells(SC_ACTIVE_SPELL_EX_NOTI.spellex val) {
		if (!has_spells()) {
			_spells = new java.util.LinkedList<SC_ACTIVE_SPELL_EX_NOTI.spellex>();
			_bit |= 0x1;
		}
		_spells.add(val);
	}

	public boolean has_spells() {
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
		if (has_spells()) {
			for (SC_ACTIVE_SPELL_EX_NOTI.spellex val : _spells) {
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
		if (has_spells()) {
			for (SC_ACTIVE_SPELL_EX_NOTI.spellex val : _spells) {
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
		if (has_spells()) {
			for (SC_ACTIVE_SPELL_EX_NOTI.spellex val : _spells) {
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
					add_spells((SC_ACTIVE_SPELL_EX_NOTI.spellex) input
							.readMessage(SC_ACTIVE_SPELL_EX_NOTI.spellex.newInstance()));
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
		return new SC_ACTIVE_SPELL_EX_NOTI();
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

	public static class spellex implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static spellex newInstance() {
			return new spellex();
		}

		private int _spellid;
		private int _spelltype;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private spellex() {
		}

		public int get_spellid() {
			return _spellid;
		}

		public void set_spellid(int val) {
			_bit |= 0x1;
			_spellid = val;
		}

		public boolean has_spellid() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_spelltype() {
			return _spelltype;
		}

		public void set_spelltype(int val) {
			_bit |= 0x2;
			_spelltype = val;
		}

		public boolean has_spelltype() {
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
			if (has_spellid()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _spellid);
			}
			if (has_spelltype()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _spelltype);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_spellid()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_spelltype()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_spellid()) {
				output.wirteInt32(1, _spellid);
			}
			if (has_spelltype()) {
				output.wirteInt32(2, _spelltype);
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
						set_spellid(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_spelltype(input.readInt32());
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
			return new spellex();
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
