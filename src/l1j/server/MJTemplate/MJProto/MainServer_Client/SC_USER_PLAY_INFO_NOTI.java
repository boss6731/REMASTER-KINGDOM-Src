package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.UnsupportedEncodingException;

import l1j.server.MJDungeonTimer.DungeonTimeInformation;
import l1j.server.MJDungeonTimer.Loader.DungeonTimeInformationLoader;
import l1j.server.MJDungeonTimer.Progress.DungeonTimeProgress;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_USER_PLAY_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send(L1PcInstance pc) {
		SC_USER_PLAY_INFO_NOTI noti = newInstance();
		for (DungeonTimeInformation dtInfo : DungeonTimeInformationLoader.getInstance().get_enumerator()) {
			if (!dtInfo.get_is_presentation())
				continue;
			MAP_TIME_LIMIT_INFO info = MAP_TIME_LIMIT_INFO.newInstance();
			info.set_time_limit_serial(dtInfo.get_serial_id());
			try {
				info.set_description(dtInfo.get_description().getBytes("MS949"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			info.set_time_limit_stay(dtInfo.get_amount_seconds());

			DungeonTimeProgress<?> progress = pc.get_progress(dtInfo);
			if (progress == null) {
				info.set_time_remained(dtInfo.get_amount_seconds());
			} else {
				info.set_time_remained(progress.get_remain_seconds());
			}
			noti.add_map_time_limit_info(info);

			if (dtInfo.get_charge_count() > 0) {
				CHARGED_TIME_MAP_INFO cinfo = CHARGED_TIME_MAP_INFO.newInstance();
				CHARGED_TIME_MAP_INFO ginfo = CHARGED_TIME_MAP_INFO.newInstance();
				if (progress == null) {
					cinfo.set_charged_time(0);
					cinfo.set_charged_count(0);
					cinfo.set_max_charge_count(0);
					cinfo.set_extra_charged_time(0);
					if (dtInfo.get_group_id() == 1) {// 숨사
						ginfo.set_charged_time(dtInfo.get_amount_seconds());
						ginfo.set_charged_count(dtInfo.get_charge_count());
						ginfo.set_max_charge_count(dtInfo.get_charge_count());
						ginfo.set_extra_charged_time(0);
						ginfo.set_group_id(eChargedTimeMapGroup.fromInt(1));
					} else if (dtInfo.get_group_id() == 2) {// 은기사 던전
						ginfo.set_charged_time(dtInfo.get_amount_seconds());
						ginfo.set_charged_count(dtInfo.get_charge_count());
						ginfo.set_max_charge_count(dtInfo.get_charge_count());
						ginfo.set_extra_charged_time(0);
						ginfo.set_group_id(eChargedTimeMapGroup.fromInt(2));
					} else if (dtInfo.get_group_id() == 3) {// 숨사 boost
						ginfo.set_charged_time(dtInfo.get_amount_seconds());
						ginfo.set_charged_count(dtInfo.get_charge_count());
						ginfo.set_max_charge_count(dtInfo.get_charge_count());
						ginfo.set_extra_charged_time(0);
						ginfo.set_group_id(eChargedTimeMapGroup.fromInt(3));
					}
				} else {
					cinfo.set_charged_time(progress.get_remain_seconds());
					cinfo.set_charged_count(progress.get_charge_count());
					cinfo.set_max_charge_count(dtInfo.get_charge_count());
					cinfo.set_extra_charged_time(0);
					if (dtInfo.get_group_id() == 1) {// 숨사
						ginfo.set_charged_time(progress.get_remain_seconds());
						ginfo.set_charged_count(progress.get_charge_count());
						ginfo.set_max_charge_count(dtInfo.get_charge_count());
						ginfo.set_extra_charged_time(0);
						ginfo.set_group_id(eChargedTimeMapGroup.fromInt(1));
					} else if (dtInfo.get_group_id() == 2) {// 은기사 던전
						ginfo.set_charged_time(progress.get_remain_seconds());
						ginfo.set_charged_count(progress.get_charge_count());
						ginfo.set_max_charge_count(dtInfo.get_charge_count());
						ginfo.set_extra_charged_time(0);
						ginfo.set_group_id(eChargedTimeMapGroup.fromInt(2));
					} else if (dtInfo.get_group_id() == 3) {// 숨사 boost
						ginfo.set_charged_time(progress.get_remain_seconds());
						ginfo.set_charged_count(progress.get_charge_count());
						ginfo.set_max_charge_count(dtInfo.get_charge_count());
						ginfo.set_extra_charged_time(0);
						ginfo.set_group_id(eChargedTimeMapGroup.fromInt(3));
					}

				}
				// noti.set_charged_time_map_info(cinfo);
				noti.add_charged_time_map_info_group(ginfo);

			}

		}
		pc.sendPackets(noti, MJEProtoMessages.SC_USER_PLAY_INFO_NOTI, true);
	}

	public static SC_USER_PLAY_INFO_NOTI newInstance() {
		return new SC_USER_PLAY_INFO_NOTI();
	}

	private java.util.LinkedList<SC_USER_PLAY_INFO_NOTI.MAP_TIME_LIMIT_INFO> _map_time_limit_info;
	private SC_USER_PLAY_INFO_NOTI.CHARGED_TIME_MAP_INFO _charged_time_map_info;
	private java.util.LinkedList<SC_USER_PLAY_INFO_NOTI.CHARGED_TIME_MAP_INFO> _charged_time_map_info_group;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_USER_PLAY_INFO_NOTI() {
	}

	public java.util.LinkedList<SC_USER_PLAY_INFO_NOTI.MAP_TIME_LIMIT_INFO> get_map_time_limit_info() {
		return _map_time_limit_info;
	}

	public void add_map_time_limit_info(SC_USER_PLAY_INFO_NOTI.MAP_TIME_LIMIT_INFO val) {
		if (!has_map_time_limit_info()) {
			_map_time_limit_info = new java.util.LinkedList<SC_USER_PLAY_INFO_NOTI.MAP_TIME_LIMIT_INFO>();
			_bit |= 0x1;
		}
		_map_time_limit_info.add(val);
	}

	public boolean has_map_time_limit_info() {
		return (_bit & 0x1) == 0x1;
	}

	public SC_USER_PLAY_INFO_NOTI.CHARGED_TIME_MAP_INFO get_charged_time_map_info() {
		return _charged_time_map_info;
	}

	public void set_charged_time_map_info(SC_USER_PLAY_INFO_NOTI.CHARGED_TIME_MAP_INFO val) {
		_bit |= 0x2;
		_charged_time_map_info = val;
	}

	public boolean has_charged_time_map_info() {
		return (_bit & 0x2) == 0x2;
	}

	public java.util.LinkedList<SC_USER_PLAY_INFO_NOTI.CHARGED_TIME_MAP_INFO> get_charged_time_map_info_group() {
		return _charged_time_map_info_group;
	}

	public void add_charged_time_map_info_group(SC_USER_PLAY_INFO_NOTI.CHARGED_TIME_MAP_INFO val) {
		if (!has_charged_time_map_info_group()) {
			_charged_time_map_info_group = new java.util.LinkedList<SC_USER_PLAY_INFO_NOTI.CHARGED_TIME_MAP_INFO>();
			_bit |= 0x4;
		}
		_charged_time_map_info_group.add(val);
	}

	public boolean has_charged_time_map_info_group() {
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
		if (has_map_time_limit_info()) {
			for (SC_USER_PLAY_INFO_NOTI.MAP_TIME_LIMIT_INFO val : _map_time_limit_info) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_charged_time_map_info()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _charged_time_map_info);
		}
		if (has_charged_time_map_info_group()) {
			for (SC_USER_PLAY_INFO_NOTI.CHARGED_TIME_MAP_INFO val : _charged_time_map_info_group) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_map_time_limit_info()) {
			for (SC_USER_PLAY_INFO_NOTI.MAP_TIME_LIMIT_INFO val : _map_time_limit_info) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_charged_time_map_info_group()) {
			for (SC_USER_PLAY_INFO_NOTI.CHARGED_TIME_MAP_INFO val : _charged_time_map_info_group) {
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
		if (has_map_time_limit_info()) {
			for (SC_USER_PLAY_INFO_NOTI.MAP_TIME_LIMIT_INFO val : _map_time_limit_info) {
				output.writeMessage(1, val);
			}
		}
		if (has_charged_time_map_info()) {
			output.writeMessage(2, _charged_time_map_info);
		}
		if (has_charged_time_map_info_group()) {
			for (SC_USER_PLAY_INFO_NOTI.CHARGED_TIME_MAP_INFO val : _charged_time_map_info_group) {
				output.writeMessage(3, val);
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
					add_map_time_limit_info((SC_USER_PLAY_INFO_NOTI.MAP_TIME_LIMIT_INFO) input
							.readMessage(SC_USER_PLAY_INFO_NOTI.MAP_TIME_LIMIT_INFO.newInstance()));
					break;
				}
				case 0x00000012: {
					set_charged_time_map_info((SC_USER_PLAY_INFO_NOTI.CHARGED_TIME_MAP_INFO) input
							.readMessage(SC_USER_PLAY_INFO_NOTI.CHARGED_TIME_MAP_INFO.newInstance()));
					break;
				}
				case 0x0000001A: {
					add_charged_time_map_info_group((SC_USER_PLAY_INFO_NOTI.CHARGED_TIME_MAP_INFO) input
							.readMessage(SC_USER_PLAY_INFO_NOTI.CHARGED_TIME_MAP_INFO.newInstance()));
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
		return new SC_USER_PLAY_INFO_NOTI();
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_map_time_limit_info()) {
			for (MAP_TIME_LIMIT_INFO val : _map_time_limit_info)
				val.dispose();
			_map_time_limit_info.clear();
			_map_time_limit_info = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	public static class MAP_TIME_LIMIT_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static MAP_TIME_LIMIT_INFO newInstance() {
			return new MAP_TIME_LIMIT_INFO();
		}

		private int _time_limit_serial;
		private byte[] _description;
		private int _time_remained;
		private int _time_limit_stay;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private MAP_TIME_LIMIT_INFO() {
		}

		public int get_time_limit_serial() {
			return _time_limit_serial;
		}

		public void set_time_limit_serial(int val) {
			_bit |= 0x1;
			_time_limit_serial = val;
		}

		public boolean has_time_limit_serial() {
			return (_bit & 0x1) == 0x1;
		}

		public byte[] get_description() {
			return _description;
		}

		public void set_description(byte[] val) {
			_bit |= 0x2;
			_description = val;
		}

		public boolean has_description() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_time_remained() {
			return _time_remained;
		}

		public void set_time_remained(int val) {
			_bit |= 0x4;
			_time_remained = val;
		}

		public boolean has_time_remained() {
			return (_bit & 0x4) == 0x4;
		}

		public int get_time_limit_stay() {
			return _time_limit_stay;
		}

		public void set_time_limit_stay(int val) {
			_bit |= 0x8;
			_time_limit_stay = val;
		}

		public boolean has_time_limit_stay() {
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
			if (has_time_limit_serial()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _time_limit_serial);
			}
			if (has_description()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(2, _description);
			}
			if (has_time_remained()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _time_remained);
			}
			if (has_time_limit_stay()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _time_limit_stay);
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_time_limit_serial()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_description()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_time_remained()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_time_limit_stay()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_time_limit_serial()) {
				output.writeUInt32(1, _time_limit_serial);
			}
			if (has_description()) {
				output.writeBytes(2, _description);
			}
			if (has_time_remained()) {
				output.writeUInt32(3, _time_remained);
			}
			if (has_time_limit_stay()) {
				output.writeUInt32(4, _time_limit_stay);
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
						set_time_limit_serial(input.readUInt32());
						break;
					}
					case 0x00000012: {
						set_description(input.readBytes());
						break;
					}
					case 0x00000018: {
						set_time_remained(input.readUInt32());
						break;
					}
					case 0x00000020: {
						set_time_limit_stay(input.readUInt32());
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
			return new MAP_TIME_LIMIT_INFO();
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

	public static class CHARGED_TIME_MAP_INFO implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static CHARGED_TIME_MAP_INFO newInstance() {
			return new CHARGED_TIME_MAP_INFO();
		}

		private int _charged_time;
		private int _charged_count;
		private int _max_charge_count;
		private int _extra_charged_time;
		private SC_USER_PLAY_INFO_NOTI.eChargedTimeMapGroup _group_id;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private CHARGED_TIME_MAP_INFO() {
		}

		public int get_charged_time() {
			return _charged_time;
		}

		public void set_charged_time(int val) {
			_bit |= 0x1;
			_charged_time = val;
		}

		public boolean has_charged_time() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_charged_count() {
			return _charged_count;
		}

		public void set_charged_count(int val) {
			_bit |= 0x2;
			_charged_count = val;
		}

		public boolean has_charged_count() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_max_charge_count() {
			return _max_charge_count;
		}

		public void set_max_charge_count(int val) {
			_bit |= 0x4;
			_max_charge_count = val;
		}

		public boolean has_max_charge_count() {
			return (_bit & 0x4) == 0x4;
		}

		public void set_extra_charged_time(int val) {
			_bit |= 0x8;
			_extra_charged_time = val;
		}

		public boolean has_extra_charged_time() {
			return (_bit & 0x8) == 0x8;
		}

		public SC_USER_PLAY_INFO_NOTI.eChargedTimeMapGroup get_group_id() {
			return _group_id;
		}

		public void set_group_id(SC_USER_PLAY_INFO_NOTI.eChargedTimeMapGroup val) {
			_bit |= 0x10;
			_group_id = val;
		}

		public boolean has_group_id() {
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
			if (has_charged_time()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _charged_time);
			}
			if (has_charged_count()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _charged_count);
			}
			if (has_max_charge_count()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _max_charge_count);
			}
			if (has_extra_charged_time()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _extra_charged_time);
			}
			if (has_group_id()) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(5, _group_id.toInt());
			}
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_charged_time()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_charged_count()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_max_charge_count()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
			if (has_charged_time()) {
				output.writeUInt32(1, _charged_time);
			}
			if (has_charged_count()) {
				output.wirteInt32(2, _charged_count);
			}
			if (has_max_charge_count()) {
				output.wirteInt32(3, _max_charge_count);
			}
			if (has_extra_charged_time()) {
				output.writeUInt32(4, _extra_charged_time);
			}
			if (has_group_id()) {
				output.writeEnum(5, _group_id.toInt());
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
						set_charged_time(input.readUInt32());
						break;
					}
					case 0x00000010: {
						set_charged_count(input.readInt32());
						break;
					}
					case 0x00000018: {
						set_max_charge_count(input.readInt32());
						break;
					}
					case 0x00000020: {
						set_extra_charged_time(input.readUInt32());
						break;
					}
					case 0x00000028: {
						set_group_id(SC_USER_PLAY_INFO_NOTI.eChargedTimeMapGroup.fromInt(input.readEnum()));
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
			return new CHARGED_TIME_MAP_INFO();
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

	public enum eChargedTimeMapGroup {
		// MAP_GROUP_START(1),
		HIDDEN_FIELD(1),
		SILVER_KNIGHT_DUNGEON(2),
		HIDDEN_FIELD_BOOST(3),
		// MAP_GROUP_END(2),
		;

		private int value;

		eChargedTimeMapGroup(int val) {
			value = val;
		}

		public int toInt() {
			return value;
		}

		public boolean equals(eChargedTimeMapGroup v) {
			return value == v.value;
		}

		public static eChargedTimeMapGroup fromInt(int i) {
			switch (i) {
				// case 1:
				// return MAP_GROUP_START;
				case 1:
					return HIDDEN_FIELD;
				case 2:
					return SILVER_KNIGHT_DUNGEON;
				case 3:
					return HIDDEN_FIELD_BOOST;
				// case 2:
				// return MAP_GROUP_END;
				default:
					throw new IllegalArgumentException(String.format("無效參數 eChargedTimeMapGroup，%d", i));
			}
		}
	}

}
