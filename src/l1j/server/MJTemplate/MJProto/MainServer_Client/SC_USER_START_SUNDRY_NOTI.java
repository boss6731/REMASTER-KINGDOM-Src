package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_USER_START_SUNDRY_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send(L1PcInstance pc, boolean isPC防禦增益) {
		SC_USER_START_SUNDRY_NOTI noti = newInstance();
		noti.set_is_pccafe(isPC防禦增益);
		noti.set_is_enable_ranking_system(true);
		noti.set_account_id(pc.getAccount().getAccountId());
		pc.sendPackets(noti, MJEProtoMessages.SC_USER_START_SUNDRY_NOTI, true);
	}

	public static void send2(L1PcInstance pc, boolean isPC防禦增益) {
		SC_USER_START_SUNDRY_NOTI noti = newInstance();
		noti.set_is_pccafe(isPC防禦增益);
		noti.set_is_enable_ranking_system(true);

		pc.sendPackets(noti, MJEProtoMessages.SC_USER_START_SUNDRY_NOTI, true);
	}

	public static SC_USER_START_SUNDRY_NOTI newInstance() {
		return new SC_USER_START_SUNDRY_NOTI();
	}

	private boolean _is_pccafe;
	private boolean _is_enable_ranking_system;
	private int _pc_move_delay_reduce_rate;
	private int _npc_move_delay_reduce;
	private SC_USER_START_SUNDRY_NOTI.eventHeroServer _eventInfo;
	private long _account_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_USER_START_SUNDRY_NOTI() {
	}

	public boolean get_is_pccafe() {
		return _is_pccafe;
	}

	public void set_is_pccafe(boolean val) {
		_bit |= 0x1;
		_is_pccafe = val;
	}

	public boolean has_is_pccafe() {
		return (_bit & 0x1) == 0x1;
	}

	public boolean get_is_enable_ranking_system() {
		return _is_enable_ranking_system;
	}

	public void set_is_enable_ranking_system(boolean val) {
		_bit |= 0x2;
		_is_enable_ranking_system = val;
	}

	public boolean has_is_enable_ranking_system() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_pc_move_delay_reduce_rate() {
		return _pc_move_delay_reduce_rate;
	}

	public void set_pc_move_delay_reduce_rate(int val) {
		_bit |= 0x4;
		_pc_move_delay_reduce_rate = val;
	}

	public boolean has_pc_move_delay_reduce_rate() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_npc_move_delay_reduce() {
		return _npc_move_delay_reduce;
	}

	public void set_npc_move_delay_reduce(int val) {
		_bit |= 0x8;
		_npc_move_delay_reduce = val;
	}

	public boolean has_npc_move_delay_reduce() {
		return (_bit & 0x8) == 0x8;
	}

	public SC_USER_START_SUNDRY_NOTI.eventHeroServer get_eventInfo() {
		return _eventInfo;
	}

	public void set_eventInfo(SC_USER_START_SUNDRY_NOTI.eventHeroServer val) {
		_bit |= 0x10;
		_eventInfo = val;
	}

	public boolean has_eventInfo() {
		return (_bit & 0x10) == 0x10;
	}

	public long get_account_id() {
		return _account_id;
	}

	public void set_account_id(long val) {
		_bit |= 0x20;
		_account_id = val;
	}

	public boolean has_account_id() {
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
		if (has_is_pccafe()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _is_pccafe);
		}
		if (has_is_enable_ranking_system()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _is_enable_ranking_system);
		}
		if (has_pc_move_delay_reduce_rate()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _pc_move_delay_reduce_rate);
		}
		if (has_npc_move_delay_reduce()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _npc_move_delay_reduce);
		}
		if (has_eventInfo()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, _eventInfo);
		}
		if (has_account_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(6, _account_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_is_pccafe()) {
			output.writeBool(1, _is_pccafe);
		}
		if (has_is_enable_ranking_system()) {
			output.writeBool(2, _is_enable_ranking_system);
		}
		if (has_pc_move_delay_reduce_rate()) {
			output.wirteInt32(3, _pc_move_delay_reduce_rate);
		}
		if (has_npc_move_delay_reduce()) {
			output.wirteInt32(4, _npc_move_delay_reduce);
		}
		if (has_eventInfo()) {
			output.writeMessage(5, _eventInfo);
		}
		if (has_account_id()) {
			output.writeInt64(6, _account_id);
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
					set_is_pccafe(input.readBool());
					break;
				}
				case 0x00000010: {
					set_is_enable_ranking_system(input.readBool());
					break;
				}
				case 0x00000018: {
					set_pc_move_delay_reduce_rate(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_npc_move_delay_reduce(input.readInt32());
					break;
				}
				case 0x0000002A: {
					set_eventInfo((SC_USER_START_SUNDRY_NOTI.eventHeroServer) input
							.readMessage(SC_USER_START_SUNDRY_NOTI.eventHeroServer.newInstance()));
					break;
				}
				case 0x00000030: {
					set_account_id(input.readInt64());
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
		return new SC_USER_START_SUNDRY_NOTI();
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

	public static class eventHeroServer implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static eventHeroServer newInstance() {
			return new eventHeroServer();
		}

		private int _allowCreateSlot;
		private java.util.LinkedList<Integer> _allowGameClass;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private eventHeroServer() {
		}

		public int get_allowCreateSlot() {
			return _allowCreateSlot;
		}

		public void set_allowCreateSlot(int val) {
			_bit |= 0x1;
			_allowCreateSlot = val;
		}

		public boolean has_allowCreateSlot() {
			return (_bit & 0x1) == 0x1;
		}

		public java.util.LinkedList<Integer> get_allowGameClass() {
			return _allowGameClass;
		}

		public void add_allowGameClass(int val) {
			if (!has_allowGameClass()) {
				_allowGameClass = new java.util.LinkedList<Integer>();
				_bit |= 0x2;
			}
			_allowGameClass.add(val);
		}

		public boolean has_allowGameClass() {
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
			if (has_allowCreateSlot()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _allowCreateSlot);
			}
			if (has_allowGameClass()) {
				for (int val : _allowGameClass) {
					size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, val);
				}
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (has_allowGameClass()) {
				// for(int val : _allowGameClass){
				// if (!val.isInitialized()){
				_memorizedIsInitialized = -1;
				return false;
				// }
				// }
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_allowCreateSlot()) {
				output.wirteInt32(1, _allowCreateSlot);
			}
			if (has_allowGameClass()) {
				for (int val : _allowGameClass) {
					output.wirteInt32(2, val);
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
						set_allowCreateSlot(input.readInt32());
						break;
					}
					case 0x00000010: {
						add_allowGameClass(input.readInt32());
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
			return new eventHeroServer();
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
