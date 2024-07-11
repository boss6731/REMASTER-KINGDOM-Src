package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_EINHASAD_FAITH_BUFF_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send_index(L1PcInstance pc, int i, int desc_id, int index_id) {
		SC_EINHASAD_FAITH_BUFF_NOTI noti = new SC_EINHASAD_FAITH_BUFF_NOTI();
		noti.set_noti_type(eNotiType.fromInt(i));
		noti.set_tooltip_str_id(desc_id);
		noti.add_enable_indexIds(index_id);

		pc.sendPackets(noti.writeTo(MJEProtoMessages.SC_EINHASAD_FAITH_BUFF_NOTI), true);
	}

	public static void send_group(L1PcInstance pc, int i, int desc_id, int group_id) {
		SC_EINHASAD_FAITH_BUFF_NOTI noti = new SC_EINHASAD_FAITH_BUFF_NOTI();
		noti.set_noti_type(eNotiType.fromInt(i));
		noti.set_tooltip_str_id(desc_id);
		noti.add_enable_groupIds(group_id);

		pc.sendPackets(noti.writeTo(MJEProtoMessages.SC_EINHASAD_FAITH_BUFF_NOTI), true);
	}

	public static SC_EINHASAD_FAITH_BUFF_NOTI newInstance() {
		return new SC_EINHASAD_FAITH_BUFF_NOTI();
	}

	private SC_EINHASAD_FAITH_BUFF_NOTI.eNotiType _noti_type;
	private int _tooltip_str_id;
	private java.util.LinkedList<Integer> _enable_indexIds;
	private java.util.LinkedList<Integer> _enable_groupIds;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EINHASAD_FAITH_BUFF_NOTI() {
	}

	public SC_EINHASAD_FAITH_BUFF_NOTI.eNotiType get_noti_type() {
		return _noti_type;
	}

	public void set_noti_type(SC_EINHASAD_FAITH_BUFF_NOTI.eNotiType val) {
		_bit |= 0x1;
		_noti_type = val;
	}

	public boolean has_noti_type() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_tooltip_str_id() {
		return _tooltip_str_id;
	}

	public void set_tooltip_str_id(int val) {
		_bit |= 0x2;
		_tooltip_str_id = val;
	}

	public boolean has_tooltip_str_id() {
		return (_bit & 0x2) == 0x2;
	}

	public java.util.LinkedList<Integer> get_enable_indexIds() {
		return _enable_indexIds;
	}

	public void add_enable_indexIds(int val) {
		if (!has_enable_indexIds()) {
			_enable_indexIds = new java.util.LinkedList<Integer>();
			_bit |= 0x4;
		}
		_enable_indexIds.add(val);
	}

	public boolean has_enable_indexIds() {
		return (_bit & 0x4) == 0x4;
	}

	public java.util.LinkedList<Integer> get_enable_groupIds() {
		return _enable_groupIds;
	}

	public void add_enable_groupIds(int val) {
		if (!has_enable_groupIds()) {
			_enable_groupIds = new java.util.LinkedList<Integer>();
			_bit |= 0x8;
		}
		_enable_groupIds.add(val);
	}

	public boolean has_enable_groupIds() {
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
		if (has_noti_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _noti_type.toInt());
		}
		if (has_tooltip_str_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _tooltip_str_id);
		}
		if (has_enable_indexIds()) {
			for (int val : _enable_indexIds) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, val);
			}
		}
		if (has_enable_groupIds()) {
			for (int val : _enable_groupIds) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_noti_type()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_tooltip_str_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_enable_indexIds()) {
			// for(int val : _enable_indexIds){
			// if (!val.isInitialized()){
			// _memorizedIsInitialized = -1;
			// return false;
			// }
			// }
		}
		if (has_enable_groupIds()) {
			// for(int val : _enable_groupIds){
			// if (!val.isInitialized()){
			// _memorizedIsInitialized = -1;
			// return false;
			// }
			// }
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_noti_type()) {
			output.writeEnum(1, _noti_type.toInt());
		}
		if (has_tooltip_str_id()) {
			output.writeUInt32(2, _tooltip_str_id);
		}
		if (has_enable_indexIds()) {
			for (int val : _enable_indexIds) {
				output.wirteInt32(3, val);
			}
		}
		if (has_enable_groupIds()) {
			for (int val : _enable_groupIds) {
				output.wirteInt32(4, val);
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
					set_noti_type(SC_EINHASAD_FAITH_BUFF_NOTI.eNotiType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010: {
					set_tooltip_str_id(input.readUInt32());
					break;
				}
				case 0x00000018: {
					add_enable_indexIds(input.readInt32());
					break;
				}
				case 0x00000020: {
					add_enable_groupIds(input.readInt32());
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
		return new SC_EINHASAD_FAITH_BUFF_NOTI();
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

	public enum eNotiType {
		NEW(1),
		RESTART(2),
		END(3),
		;

		private int value;

		eNotiType(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eNotiType v) {
			return value == v.value;
		}

		public static eNotiType fromInt(int i) {
			switch (i) {
				case 1:
					return NEW;
				case 2:
					return RESTART;
				case 3:
					return END;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eNotiType，%d", i));
			}
		}
	}
}
