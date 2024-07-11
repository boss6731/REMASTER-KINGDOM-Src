package l1j.server.MJTemplate.MJProto.MainServer_Client_BuilderCommand;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_BUILDER_TELEPORT_USER_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_BUILDER_TELEPORT_USER_REQ newInstance() {
		return new CS_BUILDER_TELEPORT_USER_REQ();
	}

	private int _serverId;
	private byte[] _name;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_BUILDER_TELEPORT_USER_REQ() {
	}

	public int get_serverId() {
		return _serverId;
	}

	public void set_serverId(int val) {
		_bit |= 0x1;
		_serverId = val;
	}

	public boolean has_serverId() {
		return (_bit & 0x1) == 0x1;
	}

	public byte[] get_name() {
		return _name;
	}

	public void set_name(byte[] val) {
		_bit |= 0x2;
		_name = val;
	}

	public boolean has_name() {
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
		if (has_serverId()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _serverId);
		}
		if (has_name()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(2, _name);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_serverId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_name()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_serverId()) {
			output.wirteInt32(1, _serverId);
		}
		if (has_name()) {
			output.writeBytes(2, _name);
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
					set_serverId(input.readInt32());
					break;
				}
				case 0x00000012: {
					set_name(input.readBytes());
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

			if (_name.length > 13 || _name == null) {
				return this;
			}

			if (!pc.isGm()) {
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.
			set_serverId(_serverId);

			if (pc == null || pc.isPrivateShop() || pc.is무인상점()) {
				return this;
			}
			String targetname = new String(_name);
			L1PcInstance target = L1World.getInstance().getPlayer(targetname);
			if (target != null) {
				pc.start_teleport(target.getX(), target.getY(), target.getMapId(), target.getHeading(), 18339, false);
			}
			targetname = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_BUILDER_TELEPORT_USER_REQ();
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
