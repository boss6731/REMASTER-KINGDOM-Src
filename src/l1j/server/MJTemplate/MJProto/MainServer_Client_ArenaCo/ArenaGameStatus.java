package l1j.server.MJTemplate.MJProto.MainServer_Client_ArenaCo;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.eArenaTeam;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class ArenaGameStatus implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static ArenaGameStatus newInstance() {
		return new ArenaGameStatus();
	}

	private int _play_time_msec;
	private boolean _is_timer_run;
	private int _observer_count;
	private RoundScoreBoard _round_score_board;
	private java.util.LinkedList<TeamStatus> _team_status;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private ArenaGameStatus() {
	}

	public int get_play_time_msec() {
		return _play_time_msec;
	}

	public void set_play_time_msec(int val) {
		_bit |= 0x1;
		_play_time_msec = val;
	}

	public boolean has_play_time_msec() {
		return (_bit & 0x1) == 0x1;
	}

	public boolean get_is_timer_run() {
		return _is_timer_run;
	}

	public void set_is_timer_run(boolean val) {
		_bit |= 0x2;
		_is_timer_run = val;
	}

	public boolean has_is_timer_run() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_observer_count() {
		return _observer_count;
	}

	public void set_observer_count(int val) {
		_bit |= 0x4;
		_observer_count = val;
	}

	public boolean has_observer_count() {
		return (_bit & 0x4) == 0x4;
	}

	public RoundScoreBoard get_round_score_board() {
		return _round_score_board;
	}

	public void set_round_score_board(RoundScoreBoard val) {
		_bit |= 0x8;
		_round_score_board = val;
	}

	public boolean has_round_score_board() {
		return (_bit & 0x8) == 0x8;
	}

	public java.util.LinkedList<TeamStatus> get_team_status() {
		return _team_status;
	}

	public void add_team_status(TeamStatus val) {
		if (!has_team_status()) {
			_team_status = new java.util.LinkedList<TeamStatus>();
			_bit |= 0x10;
		}
		_team_status.add(val);
	}

	public boolean has_team_status() {
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
		if (has_play_time_msec())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _play_time_msec);
		if (has_is_timer_run())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _is_timer_run);
		if (has_observer_count())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _observer_count);
		if (has_round_score_board())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, _round_score_board);
		if (has_team_status()) {
			for (TeamStatus val : _team_status)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(5, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_team_status()) {
			for (TeamStatus val : _team_status) {
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
		if (has_play_time_msec()) {
			output.writeUInt32(1, _play_time_msec);
		}
		if (has_is_timer_run()) {
			output.writeBool(2, _is_timer_run);
		}
		if (has_observer_count()) {
			output.writeUInt32(3, _observer_count);
		}
		if (has_round_score_board()) {
			output.writeMessage(4, _round_score_board);
		}
		if (has_team_status()) {
			for (TeamStatus val : _team_status) {
				output.writeMessage(5, val);
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
				case 0x00000008: {
					set_play_time_msec(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_is_timer_run(input.readBool());
					break;
				}
				case 0x00000018: {
					set_observer_count(input.readUInt32());
					break;
				}
				case 0x00000022: {
					set_round_score_board((RoundScoreBoard) input.readMessage(RoundScoreBoard.newInstance()));
					break;
				}
				case 0x0000002A: {
					add_team_status((TeamStatus) input.readMessage(TeamStatus.newInstance()));
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
		return new ArenaGameStatus();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_round_score_board() && _round_score_board != null) {
			_round_score_board.dispose();
			_round_score_board = null;
		}
		if (has_team_status()) {
			for (TeamStatus val : _team_status)
				val.dispose();
			_team_status.clear();
			_team_status = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	public static class TeamStatus implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static TeamStatus newInstance() {
			return new TeamStatus();
		}

		private eArenaTeam _team_id;
		private int _cheer_msg_count;
		private int _team_kill_count;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private TeamStatus() {
		}

		public eArenaTeam get_team_id() {
			return _team_id;
		}

		public void set_team_id(eArenaTeam val) {
			_bit |= 0x1;
			_team_id = val;
		}

		public boolean has_team_id() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_cheer_msg_count() {
			return _cheer_msg_count;
		}

		public void set_cheer_msg_count(int val) {
			_bit |= 0x2;
			_cheer_msg_count = val;
		}

		public boolean has_cheer_msg_count() {
			return (_bit & 0x2) == 0x2;
		}

		public int get_team_kill_count() {
			return _team_kill_count;
		}

		public void set_team_kill_count(int val) {
			_bit |= 0x4;
			_team_kill_count = val;
		}

		public boolean has_team_kill_count() {
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
			if (has_team_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _team_id.toInt());
			if (has_cheer_msg_count())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _cheer_msg_count);
			if (has_team_kill_count())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _team_kill_count);
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_team_id()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
			if (has_team_id()) {
				output.writeEnum(1, _team_id.toInt());
			}
			if (has_cheer_msg_count()) {
				output.writeUInt32(2, _cheer_msg_count);
			}
			if (has_team_kill_count()) {
				output.writeUInt32(3, _team_kill_count);
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
						set_team_id(eArenaTeam.fromInt(input.readEnum()));
						break;
					}
					case 0x00000010: {
						set_cheer_msg_count(input.readUInt32());
						break;
					}
					case 0x00000018: {
						set_team_kill_count(input.readUInt32());
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
			return new TeamStatus();
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
