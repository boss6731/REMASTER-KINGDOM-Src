package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ATTENDANCE_REWARD_ITEM_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_ATTENDANCE_REWARD_ITEM_NOTI newInstance() {
		return new SC_ATTENDANCE_REWARD_ITEM_NOTI();
	}

	private java.util.LinkedList<ATTENDANCE_REWARD_ITEM> _reward_item_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_ATTENDANCE_REWARD_ITEM_NOTI() {
	}

	public java.util.LinkedList<ATTENDANCE_REWARD_ITEM> get_reward_item_info() {
		return _reward_item_info;
	}

	public void add_reward_item_info(ATTENDANCE_REWARD_ITEM val) {
		if (!has_reward_item_info()) {
			_reward_item_info = new java.util.LinkedList<ATTENDANCE_REWARD_ITEM>();
			_bit |= 0x1;
		}
		_reward_item_info.add(val);
	}

	public boolean has_reward_item_info() {
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
		if (has_reward_item_info()) {
			for (ATTENDANCE_REWARD_ITEM val : _reward_item_info)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_reward_item_info()) {
			for (ATTENDANCE_REWARD_ITEM val : _reward_item_info) {
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
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_reward_item_info()) {
			for (ATTENDANCE_REWARD_ITEM val : _reward_item_info) {
				output.writeMessage(1, val);
			}
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
				default: {
					return this;
				}
				case 0x0000000A: {
					add_reward_item_info(
							(ATTENDANCE_REWARD_ITEM) input.readMessage(ATTENDANCE_REWARD_ITEM.newInstance()));
					break;
				}
			}
		}
		return this;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_ATTENDANCE_REWARD_ITEM_NOTI();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_reward_item_info()) {
			for (ATTENDANCE_REWARD_ITEM val : _reward_item_info)
				val.dispose();
			_reward_item_info.clear();
			_reward_item_info = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	public static class ATTENDANCE_REWARD_ITEM implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static ATTENDANCE_REWARD_ITEM newInstance() {
			return new ATTENDANCE_REWARD_ITEM();
		}

		private int _attendance_id;
		private int _group_id;
		private int _item_name_id;
		private int _item_count;
		private int _season_num;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private ATTENDANCE_REWARD_ITEM() {
		}

		public int get_attendance_id() {
			return _attendance_id;
		}

		public void set_attendance_id(int val) {
			_bit |= 0x1;
			_attendance_id = val;
		}

		public boolean has_attendance_id() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_group_id() {
			return _group_id;
		}

		public void set_group_id(int val) {
			_bit |= 0x2;
			_group_id = val;
		}

		public boolean has_group_id() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_item_name_id() {
			return _item_name_id;
		}

		public void set_item_name_id(int val) {
			_bit |= 0x4;
			_item_name_id = val;
		}

		public boolean has_item_name_id() {
			return (_bit & 0x4) == 0x4;
		}

		public int get_item_count() {
			return _item_count;
		}

		public void set_item_count(int val) {
			_bit |= 0x8;
			_item_count = val;
		}

		public boolean has_item_count() {
			return (_bit & 0x8) == 0x8;
		}

		public int get_season_num() {
			return _season_num;
		}

		public void set_season_num(int val) {
			_bit |= 0x10;
			_season_num = val;
		}

		public boolean has_season_num() {
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
			if (has_attendance_id()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _attendance_id);
			}
			if (has_group_id()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _group_id);
			}
			if (has_item_name_id()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _item_name_id);
			}
			if (has_item_count()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _item_count);
			}
			if (has_season_num()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _season_num);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_attendance_id()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_group_id()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_item_name_id()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_item_count()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
			if (has_attendance_id()) {
				output.wirteInt32(1, _attendance_id);
			}
			if (has_group_id()) {
				output.wirteInt32(2, _group_id);
			}
			if (has_item_name_id()) {
				output.wirteInt32(3, _item_name_id);
			}
			if (has_item_count()) {
				output.wirteInt32(4, _item_count);
			}
			if (has_season_num()) {
				output.writeUInt32(5, _season_num);
			}
		}

		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
				l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
					.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
			try {
				writeTo(stream);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return stream;
		}

		@Override
		public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException {
			while (!input.isAtEnd()) {
				int tag = input.readTag();
				switch (tag) {
					default: {
						return this;
					}
					case 0x00000008: {
						set_attendance_id(input.readInt32());
						break;
					}
					case 0x00000010: {
						set_group_id(input.readInt32());
						break;
					}
					case 0x00000018: {
						set_item_name_id(input.readInt32());
						break;
					}
					case 0x00000020: {
						set_item_count(input.readInt32());
						break;
					}
					case 0x00000028: {
						set_season_num(input.readUInt32());
						break;
					}
				}
			}
			return this;
		}

		@Override
		public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
					.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
							((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
									+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try {
				readFrom(is);

				if (!isInitialized())
					return this;
				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this;
		}

		@Override
		public MJIProtoMessage copyInstance() {
			return new ATTENDANCE_REWARD_ITEM();
		}

		@Override
		public MJIProtoMessage reloadInstance() {
			return newInstance();
		}

		@Override
		public void dispose() {
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
}
