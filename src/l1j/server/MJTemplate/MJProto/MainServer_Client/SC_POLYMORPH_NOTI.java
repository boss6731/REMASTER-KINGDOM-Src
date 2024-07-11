package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class SC_POLYMORPH_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send(L1PcInstance pc, int spriteId) {
		SC_POLYMORPH_NOTI noti = newInstance();
		noti.set_objId(pc.getId());
		noti.set_sprite(spriteId);
		noti.set_action(pc.getCurrentWeapon());

		pc.sendPackets(noti, MJEProtoMessages.SC_POLYMORPH_NOTI);
	}

	public static SC_POLYMORPH_NOTI newInstance() {
		return new SC_POLYMORPH_NOTI();
	}

	private int _objId;
	private int _sprite;
	private int _action;
	private byte[] _desc;
	private int _class_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_POLYMORPH_NOTI() {
	}

	public int get_objId() {
		return _objId;
	}

	public void set_objId(int val) {
		_bit |= 0x1;
		_objId = val;
	}

	public boolean has_objId() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_sprite() {
		return _sprite;
	}

	public void set_sprite(int val) {
		_bit |= 0x2;
		_sprite = val;
	}

	public boolean has_sprite() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_action() {
		return _action;
	}

	public void set_action(int val) {
		_bit |= 0x4;
		_action = val;
	}

	public boolean has_action() {
		return (_bit & 0x4) == 0x4;
	}

	public byte[] get_desc() {
		return _desc;
	}

	public void set_desc(byte[] val) {
		_bit |= 0x8;
		_desc = val;
	}

	public boolean has_desc() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_class_id() {
		return _class_id;
	}

	public void set_class_id(int val) {
		_bit |= 0x10;
		_class_id = val;
	}

	public boolean has_class_id() {
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
		if (has_objId())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _objId);
		if (has_sprite())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _sprite);
		if (has_action())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _action);
		if (has_desc())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(4, _desc);
		if (has_class_id())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(5, _class_id);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_objId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_sprite()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_action()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_objId()) {
			output.wirteInt32(1, _objId);
		}
		if (has_sprite()) {
			output.wirteInt32(2, _sprite);
		}
		if (has_action()) {
			output.wirteInt32(3, _action);
		}
		if (has_desc()) {
			output.writeBytes(4, _desc);
		}
		if (has_class_id()) {
			output.wirteInt32(5, _class_id);
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
					set_objId(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_sprite(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_action(input.readInt32());
					break;
				}
				case 0x00000022: {
					set_desc(input.readBytes());
					break;
				}
				case 0x00000028: {
					set_class_id(input.readInt32());
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
			// System.out.println(_desc);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_POLYMORPH_NOTI();
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
