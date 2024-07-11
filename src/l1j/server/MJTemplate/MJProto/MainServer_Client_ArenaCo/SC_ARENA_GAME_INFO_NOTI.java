package l1j.server.MJTemplate.MJProto.MainServer_Client_ArenaCo;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eArenaMapKind;
import l1j.server.MJTemplate.MJProto.MainServer_Client.ArenaActorInfo;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class SC_ARENA_GAME_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send_info(L1PcInstance pc, int time) {
		SC_ARENA_GAME_INFO_NOTI noti = SC_ARENA_GAME_INFO_NOTI.newInstance();
		noti.set_round(0);
		noti.set_map_kind(eArenaMapKind.OrimLab_Minor);
		noti.set_result_display_time_sec(0);
		noti.set_round_time_sec(time);
		pc.sendPackets(noti, MJEProtoMessages.SC_ARENA_GAME_INFO_NOTI);
		SC_ARENA_PLAY_STATUS_NOTI.time_send(pc, time, false);
	}

	public static SC_ARENA_GAME_INFO_NOTI newInstance() {
		return new SC_ARENA_GAME_INFO_NOTI();
	}

	private java.util.LinkedList<ArenaActorInfo> _player_info;
	private eArenaMapKind _map_kind;
	private int _round;
	private int _round_time_sec;
	private int _result_display_time_sec;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_ARENA_GAME_INFO_NOTI() {
	}

	public java.util.LinkedList<ArenaActorInfo> get_player_info() {
		return _player_info;
	}

	public void add_player_info(ArenaActorInfo val) {
		if (!has_player_info()) {
			_player_info = new java.util.LinkedList<ArenaActorInfo>();
			_bit |= 0x1;
		}
		_player_info.add(val);
	}

	public boolean has_player_info() {
		return (_bit & 0x1) == 0x1;
	}

	public eArenaMapKind get_map_kind() {
		return _map_kind;
	}

	public void set_map_kind(eArenaMapKind val) {
		_bit |= 0x2;
		_map_kind = val;
	}

	public boolean has_map_kind() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_round() {
		return _round;
	}

	public void set_round(int val) {
		_bit |= 0x4;
		_round = val;
	}

	public boolean has_round() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_round_time_sec() {
		return _round_time_sec;
	}

	public void set_round_time_sec(int val) {
		_bit |= 0x8;
		_round_time_sec = val;
	}

	public boolean has_round_time_sec() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_result_display_time_sec() {
		return _result_display_time_sec;
	}

	public void set_result_display_time_sec(int val) {
		_bit |= 0x10;
		_result_display_time_sec = val;
	}

	public boolean has_result_display_time_sec() {
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
		if (has_player_info()) {
			for (ArenaActorInfo val : _player_info)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		if (has_map_kind())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _map_kind.toInt());
		if (has_round())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _round);
		if (has_round_time_sec())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _round_time_sec);
		if (has_result_display_time_sec())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _result_display_time_sec);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_player_info()) {
			for (ArenaActorInfo val : _player_info) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_map_kind()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_round()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_round_time_sec()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_result_display_time_sec()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_player_info()) {
			for (ArenaActorInfo val : _player_info) {
				output.writeMessage(1, val);
			}
		}
		if (has_map_kind()) {
			output.writeEnum(2, _map_kind.toInt());
		}
		if (has_round()) {
			output.writeUInt32(3, _round);
		}
		if (has_round_time_sec()) {
			output.writeUInt32(4, _round_time_sec);
		}
		if (has_result_display_time_sec()) {
			output.writeUInt32(5, _result_display_time_sec);
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
					add_player_info((ArenaActorInfo) input.readMessage(ArenaActorInfo.newInstance()));
					break;
				}
				case 0x00000010: {
					set_map_kind(eArenaMapKind.fromInt(input.readEnum()));
					break;
				}
				case 0x00000018: {
					set_round(input.readUInt32());
					break;
				}
				case 0x00000020: {
					set_round_time_sec(input.readUInt32());
					break;
				}
				case 0x00000028: {
					set_result_display_time_sec(input.readUInt32());
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
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Lenz.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_ARENA_GAME_INFO_NOTI();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_player_info()) {
			for (ArenaActorInfo val : _player_info)
				val.dispose();
			_player_info.clear();
			_player_info = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
