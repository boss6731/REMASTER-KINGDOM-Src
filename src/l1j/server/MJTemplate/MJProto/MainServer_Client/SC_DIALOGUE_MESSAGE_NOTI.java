package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class SC_DIALOGUE_MESSAGE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void quest_send(L1PcInstance pc, int[] surf, int[] dialogs) {
		SC_DIALOGUE_MESSAGE_NOTI noti = newInstance();
		if (surf.length == dialogs.length) {
			for (int i = 0; i < surf.length; ++i) {
				Dialogue dialog = Dialogue.newInstance();
				dialog.set_talker_id(surf[i]);
				dialog.set_duration(10);
				dialog.set_dialogue_id(dialogs[i]);
				noti.add_dialogues(dialog);
			}
		} else {
			System.out.println("SC_DIALOGUE_MESSAGE_NOTI: 的 surf 長度與 dialogs 長度不匹配。");
		}
		pc.sendPackets(noti, MJEProtoMessages.SC_DIALOGUE_MESSAGE_NOTI);
	}

	public static SC_DIALOGUE_MESSAGE_NOTI newInstance() {
		return new SC_DIALOGUE_MESSAGE_NOTI();
	}

	private java.util.LinkedList<Dialogue> _dialogues;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_DIALOGUE_MESSAGE_NOTI() {
	}

	public java.util.LinkedList<Dialogue> get_dialogues() {
		return _dialogues;
	}

	public void add_dialogues(Dialogue val) {
		if (!has_dialogues()) {
			_dialogues = new java.util.LinkedList<Dialogue>();
			_bit |= 0x1;
		}
		_dialogues.add(val);
	}

	public boolean has_dialogues() {
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
		if (has_dialogues()) {
			for (Dialogue val : _dialogues)
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_dialogues()) {
			for (Dialogue val : _dialogues) {
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
		if (has_dialogues()) {
			for (Dialogue val : _dialogues) {
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
					add_dialogues((Dialogue) input.readMessage(Dialogue.newInstance()));
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
		return new SC_DIALOGUE_MESSAGE_NOTI();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		if (has_dialogues()) {
			for (Dialogue val : _dialogues)
				val.dispose();
			_dialogues.clear();
			_dialogues = null;
		}
		_bit = 0;
		_memorizedIsInitialized = -1;
	}

	public static class Dialogue implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
		public static Dialogue newInstance() {
			return new Dialogue();
		}

		private int _talker_id;
		private int _dialogue_id;
		private String _sound_file;
		private int _duration;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;

		private Dialogue() {
			set_talker_id(0);
			set_duration(0);
		}

		public int get_talker_id() {
			return _talker_id;
		}

		public void set_talker_id(int val) {
			_bit |= 0x1;
			_talker_id = val;
		}

		public boolean has_talker_id() {
			return (_bit & 0x1) == 0x1;
		}

		public int get_dialogue_id() {
			return _dialogue_id;
		}

		public void set_dialogue_id(int val) {
			_bit |= 0x2;
			_dialogue_id = val;
		}

		public boolean has_dialogue_id() {
			return (_bit & 0x2) == 0x2;
		}

		public String get_sound_file() {
			return _sound_file;
		}

		public void set_sound_file(String val) {
			_bit |= 0x4;
			_sound_file = val;
		}

		public boolean has_sound_file() {
			return (_bit & 0x4) == 0x4;
		}

		public int get_duration() {
			return _duration;
		}

		public void set_duration(int val) {
			_bit |= 0x8;
			_duration = val;
		}

		public boolean has_duration() {
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
			if (has_talker_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _talker_id);
			if (has_dialogue_id())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _dialogue_id);
			if (has_sound_file())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(3, _sound_file);
			if (has_duration())
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _duration);
			_memorizedSerializedSize = size;
			return size;
		}

		@Override
		public boolean isInitialized() {
			if (_memorizedIsInitialized == 1)
				return true;
			if (!has_dialogue_id()) {
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}

		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
			if (has_talker_id()) {
				output.writeUInt32(1, _talker_id);
			}
			if (has_dialogue_id()) {
				output.writeUInt32(2, _dialogue_id);
			}
			if (has_sound_file()) {
				output.writeString(3, _sound_file);
			}
			if (has_duration()) {
				output.writeUInt32(4, _duration);
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
						set_talker_id(input.readUInt32());
						break;
					}
					case 0x00000010: {
						set_dialogue_id(input.readUInt32());
						break;
					}
					case 0x0000001A: {
						set_sound_file(input.readString());
						break;
					}
					case 0x00000020: {
						set_duration(input.readUInt32());
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
			return new Dialogue();
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
