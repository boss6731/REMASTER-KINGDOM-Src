package l1j.server.MJTemplate.MJProto.MainServer_Client_DEATH_PENALTY;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_DEATH_PENALTY_RECOVERY_ITEM_LIST_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_DEATH_PENALTY_RECOVERY_ITEM_LIST_NOTI newInstance() {
		return new SC_DEATH_PENALTY_RECOVERY_ITEM_LIST_NOTI();
	}

	private java.util.LinkedList<DeathPenaltyItemListT> _death_penalty_item_list;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_DEATH_PENALTY_RECOVERY_ITEM_LIST_NOTI() {
	}

	public java.util.LinkedList<DeathPenaltyItemListT> get_death_penalty_item_list() {
		return _death_penalty_item_list;
	}

	public void add_death_penalty_item_list(DeathPenaltyItemListT val) {
		if (!has_death_penalty_item_list()) {
			_death_penalty_item_list = new java.util.LinkedList<DeathPenaltyItemListT>();
			_bit |= 0x1;
		}
		_death_penalty_item_list.add(val);
	}

	public boolean has_death_penalty_item_list() {
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
		if (has_death_penalty_item_list()) {
			for (DeathPenaltyItemListT val : _death_penalty_item_list) {
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
		if (has_death_penalty_item_list()) {
			for (DeathPenaltyItemListT val : _death_penalty_item_list) {
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
		if (has_death_penalty_item_list()) {
			for (DeathPenaltyItemListT val : _death_penalty_item_list) {
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
					add_death_penalty_item_list(
							(DeathPenaltyItemListT) input.readMessage(DeathPenaltyItemListT.newInstance()));
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
		return new SC_DEATH_PENALTY_RECOVERY_ITEM_LIST_NOTI();
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
