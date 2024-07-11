package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.L1Character;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class SC_NPC_SPEED_VALUE_FLAG_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_NPC_SPEED_VALUE_FLAG_NOTI newInstance(L1Character npc) {
		SC_NPC_SPEED_VALUE_FLAG_NOTI noti = newInstance();
		double interval = npc.getCurrentSpriteInterval(EActionCodes.walk);
		interval -= interval * (npc.getMoveDelayRate() * 0.01);
		noti.set_object_id(npc.getId());
		noti.set_speed_value_flag((int) interval);
		noti.set_second_speed_type(npc.getMoveSpeed());
		return noti;
	}

	public static ProtoOutputStream speed_send(L1Character npc) {
		SC_NPC_SPEED_VALUE_FLAG_NOTI noti = newInstance(npc);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_NPC_SPEED_VALUE_FLAG_NOTI);
		noti.dispose();
		return stream;
	}

	public static SC_NPC_SPEED_VALUE_FLAG_NOTI newInstance() {
		return new SC_NPC_SPEED_VALUE_FLAG_NOTI();
	}

	private int _object_id;
	private int _speed_value_flag;
	private int _second_speed_type;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_NPC_SPEED_VALUE_FLAG_NOTI() {
	}

	public int get_object_id() {
		return _object_id;
	}

	public void set_object_id(int val) {
		_bit |= 0x1;
		_object_id = val;
	}

	public boolean has_object_id() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_speed_value_flag() {
		return _speed_value_flag;
	}

	public void set_speed_value_flag(int val) {
		_bit |= 0x2;
		_speed_value_flag = val;
	}

	public boolean has_speed_value_flag() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_second_speed_type() {
		return _second_speed_type;
	}

	public void set_second_speed_type(int val) {
		_bit |= 0x4;
		_second_speed_type = val;
	}

	public boolean has_second_speed_type() {
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
		if (has_object_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _object_id);
		if (has_speed_value_flag())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _speed_value_flag);
		if (has_second_speed_type())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _second_speed_type);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_object_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_speed_value_flag()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_object_id()) {
			output.writeUInt32(1, _object_id);
		}
		if (has_speed_value_flag()) {
			output.wirteInt32(2, _speed_value_flag);
		}
		if (has_second_speed_type()) {
			output.wirteInt32(3, _second_speed_type);
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
					set_object_id(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_speed_value_flag(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_second_speed_type(input.readInt32());
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
		return new SC_NPC_SPEED_VALUE_FLAG_NOTI();
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
