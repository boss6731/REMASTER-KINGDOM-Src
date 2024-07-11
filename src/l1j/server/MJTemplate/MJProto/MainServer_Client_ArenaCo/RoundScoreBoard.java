package l1j.server.MJTemplate.MJProto.MainServer_Client_ArenaCo;

import l1j.server.MJTemplate.MJProto.WireFormat;
import java.io.IOException;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class RoundScoreBoard implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static RoundScoreBoard newInstance() {
		return new RoundScoreBoard();
	}

	private java.util.LinkedList<RoundScore> _team_a;
	private java.util.LinkedList<RoundScore> _team_b;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private RoundScoreBoard() {
	}

	public java.util.LinkedList<RoundScore> get_team_a() {
		return _team_a;
	}

	public void add_team_a(RoundScore val) {
		if (!has_team_a()) {
			_team_a = new java.util.LinkedList<RoundScore>();
			_bit |= 0x1;
		}
		_team_a.add(val);
	}

	public boolean has_team_a() {
		return (_bit & 0x1) == 0x1;
	}

	public java.util.LinkedList<RoundScore> get_team_b() {
		return _team_b;
	}

	public void add_team_b(RoundScore val) {
		if (!has_team_b()) {
			_team_b = new java.util.LinkedList<RoundScore>();
			_bit |= 0x2;
		}
		_team_b.add(val);
	}

	public boolean has_team_b() {
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
		if (has_team_a()) {
			for (RoundScore val : _team_a)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		if (has_team_b()) {
			for (RoundScore val : _team_b)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_team_a()) {
			for (RoundScore val : _team_a) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (has_team_b()) {
			for (RoundScore val : _team_b) {
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
		if (has_team_a()) {
			for (RoundScore val : _team_a) {
				output.writeMessage(1, val);
			}
		}
		if (has_team_b()) {
			for (RoundScore val : _team_b) {
				output.writeMessage(2, val);
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
					add_team_a((RoundScore) input.readMessage(RoundScore.newInstance()));
					break;
				}
				case 0x00000012: {
					add_team_b((RoundScore) input.readMessage(RoundScore.newInstance()));
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
		return new RoundScoreBoard();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_team_a()) {
			for (RoundScore val : _team_a)
				val.dispose();
			_team_a.clear();
			_team_a = null;
		}
		if (has_team_b()) {
			for (RoundScore val : _team_b)
				val.dispose();
			_team_b.clear();
			_team_b = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	public static class RoundScore implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static RoundScore newInstance() {
			return new RoundScore();
		}

		private boolean _is_win;
		private int _attack_amount;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private RoundScore() {
		}

		public boolean get_is_win() {
			return _is_win;
		}

		public void set_is_win(boolean val) {
			_bit |= 0x1;
			_is_win = val;
		}

		public boolean has_is_win() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_attack_amount() {
			return _attack_amount;
		}

		public void set_attack_amount(int val) {
			_bit |= 0x2;
			_attack_amount = val;
		}

		public boolean has_attack_amount() {
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
			if (has_is_win())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(1, _is_win);
			if (has_attack_amount())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _attack_amount);
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_is_win()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
			if (has_is_win()) {
				output.writeBool(1, _is_win);
			}
			if (has_attack_amount()) {
				output.writeUInt32(2, _attack_amount);
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
						set_is_win(input.readBool());
						break;
					}
					case 0x00000010: {
						set_attack_amount(input.readUInt32());
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
			return new RoundScore();
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
