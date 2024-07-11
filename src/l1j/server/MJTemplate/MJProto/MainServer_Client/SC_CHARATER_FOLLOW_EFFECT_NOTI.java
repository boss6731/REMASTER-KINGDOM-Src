package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_CHARATER_FOLLOW_EFFECT_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static ProtoOutputStream broad_follow_effect_send(L1Character pc, int sprid, boolean enable) {
		SC_CHARATER_FOLLOW_EFFECT_NOTI noti = SC_CHARATER_FOLLOW_EFFECT_NOTI.newInstance();
		noti.set_target_id(pc.getId());
		noti.set_enable(enable);
		noti.set_sprite_id(sprid);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_CHARATER_FOLLOW_EFFECT_NOTI);
		noti.dispose();
		return stream;
	}

	public static void follow_effect_send(L1PcInstance pc, int sprid, boolean enable) {
		SC_CHARATER_FOLLOW_EFFECT_NOTI noti = SC_CHARATER_FOLLOW_EFFECT_NOTI.newInstance();
		noti.set_target_id(pc.getId());
		noti.set_enable(enable);
		noti.set_sprite_id(sprid);
		pc.sendPackets(noti, MJEProtoMessages.SC_CHARATER_FOLLOW_EFFECT_NOTI);
	}

	public static SC_CHARATER_FOLLOW_EFFECT_NOTI newInstance() {
		return new SC_CHARATER_FOLLOW_EFFECT_NOTI();
	}

	private int _target_id;
	private boolean _enable;
	private int _sprite_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_CHARATER_FOLLOW_EFFECT_NOTI() {
	}

	public int get_target_id() {
		return _target_id;
	}

	public void set_target_id(int val) {
		_bit |= 0x1;
		_target_id = val;
	}

	public boolean has_target_id() {
		return (_bit & 0x1) == 0x1;
	}

	public boolean get_enable() {
		return _enable;
	}

	public void set_enable(boolean val) {
		_bit |= 0x2;
		_enable = val;
	}

	public boolean has_enable() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_sprite_id() {
		return _sprite_id;
	}

	public void set_sprite_id(int val) {
		_bit |= 0x4;
		_sprite_id = val;
	}

	public boolean has_sprite_id() {
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
		if (has_target_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _target_id);
		}
		if (has_enable()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _enable);
		}
		if (has_sprite_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _sprite_id);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_target_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_enable()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_sprite_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_target_id()) {
			output.writeUInt32(1, _target_id);
		}
		if (has_enable()) {
			output.writeBool(2, _enable);
		}
		if (has_sprite_id()) {
			output.wirteInt32(3, _sprite_id);
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
					set_target_id(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_enable(input.readBool());
					break;
				}
				case 0x00000018: {
					set_sprite_id(input.readInt32());
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
		return new SC_CHARATER_FOLLOW_EFFECT_NOTI();
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
