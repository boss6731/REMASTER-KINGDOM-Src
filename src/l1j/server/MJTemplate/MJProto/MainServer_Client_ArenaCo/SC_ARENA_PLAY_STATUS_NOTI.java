package l1j.server.MJTemplate.MJProto.MainServer_Client_ArenaCo;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ARENA_PLAY_STATUS_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void time_send(L1PcInstance pc, int time, boolean start) {
		SC_ARENA_PLAY_STATUS_NOTI noti = SC_ARENA_PLAY_STATUS_NOTI.newInstance();
		ArenaGameStatus statusinfo = ArenaGameStatus.newInstance();
		statusinfo.set_is_timer_run(start);
		statusinfo.set_play_time_msec(time);
		noti.set_game_status(statusinfo);
		pc.sendPackets(noti, MJEProtoMessages.SC_ARENA_PLAY_STATUS_NOTI);
	}

	public static void end_time_send(L1PcInstance pc) {
		SC_ARENA_PLAY_STATUS_NOTI noti = SC_ARENA_PLAY_STATUS_NOTI.newInstance();
		ArenaGameStatus statusinfo = ArenaGameStatus.newInstance();
		statusinfo.set_is_timer_run(false);
		statusinfo.set_play_time_msec(0);
		noti.set_game_status(statusinfo);
		pc.sendPackets(noti, MJEProtoMessages.SC_ARENA_PLAY_STATUS_NOTI);
	}

	public static SC_ARENA_PLAY_STATUS_NOTI newInstance() {
		return new SC_ARENA_PLAY_STATUS_NOTI();
	}

	private java.util.LinkedList<ArenaPlayerStatus> _player_status;
	private ArenaGameStatus _game_status;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_ARENA_PLAY_STATUS_NOTI() {
	}

	public java.util.LinkedList<ArenaPlayerStatus> get_player_status() {
		return _player_status;
	}

	public void add_player_status(ArenaPlayerStatus val) {
		if (!has_player_status()) {
			_player_status = new java.util.LinkedList<ArenaPlayerStatus>();
			_bit |= 0x1;
		}
		_player_status.add(val);
	}

	public boolean has_player_status() {
		return (_bit & 0x1) == 0x1;
	}

	public ArenaGameStatus get_game_status() {
		return _game_status;
	}

	public void set_game_status(ArenaGameStatus val) {
		_bit |= 0x2;
		_game_status = val;
	}

	public boolean has_game_status() {
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
		if (has_player_status()) {
			for (ArenaPlayerStatus val : _player_status)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		if (has_game_status())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _game_status);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_player_status()) {
			for (ArenaPlayerStatus val : _player_status) {
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
		if (has_player_status()) {
			for (ArenaPlayerStatus val : _player_status) {
				output.writeMessage(1, val);
			}
		}
		if (has_game_status()) {
			output.writeMessage(2, _game_status);
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
					add_player_status((ArenaPlayerStatus) input.readMessage(ArenaPlayerStatus.newInstance()));
					break;
				}
				case 0x00000012: {
					set_game_status((ArenaGameStatus) input.readMessage(ArenaGameStatus.newInstance()));
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
		return new SC_ARENA_PLAY_STATUS_NOTI();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_player_status()) {
			for (ArenaPlayerStatus val : _player_status)
				val.dispose();
			_player_status.clear();
			_player_status = null;
		}
		if (has_game_status() && _game_status != null) {
			_game_status.dispose();
			_game_status = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
