package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.model.Instance.L1PcInstance;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

// TODO : 자동으로 생성된 PROTO 코드입니다.
public class SC_ADD_SPELL_PASSIVE_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_ADD_SPELL_PASSIVE_NOTI newInstance() {
		return new SC_ADD_SPELL_PASSIVE_NOTI();
	}

	public static void send(L1PcInstance pc, int passiveid) {
		SC_ADD_SPELL_PASSIVE_NOTI noti = SC_ADD_SPELL_PASSIVE_NOTI.newInstance();
		noti.set_passiveid(passiveid);
		pc.sendPackets(noti, MJEProtoMessages.SC_ADD_SPELL_PASSIVE_NOTI, true);
	}

	public static void send(L1PcInstance pc, int passiveid, int passiveType) {
		SC_ADD_SPELL_PASSIVE_NOTI noti = SC_ADD_SPELL_PASSIVE_NOTI.newInstance();
		noti.set_passiveid(passiveid);
		noti.set_param(passiveType);
		pc.sendPackets(noti, MJEProtoMessages.SC_ADD_SPELL_PASSIVE_NOTI, true);
	}

	public static void send_onoff(L1PcInstance pc, int passiveid, int passiveType, boolean onfooType) {
		SC_ADD_SPELL_PASSIVE_NOTI noti = SC_ADD_SPELL_PASSIVE_NOTI.newInstance();
		noti.set_passiveid(passiveid);
		noti.set_param(passiveType);
		noti.set_onoffType(onfooType);// 10-30일자로 사용함
		pc.sendPackets(noti, MJEProtoMessages.SC_ADD_SPELL_PASSIVE_NOTI, true);
	}

	private int _passiveid;
	private int _param;
	private boolean _onoffType; // 추가
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_ADD_SPELL_PASSIVE_NOTI() {
	}

	public int get_passiveid() {
		return _passiveid;
	}

	public void set_passiveid(int val) {
		_bit |= 0x1;
		_passiveid = val;
	}

	public boolean has_passiveid() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_param() {
		return _param;
	}

	public void set_param(int val) {
		_bit |= 0x2;
		_param = val;
	}

	public boolean has_param() {
		return (_bit & 0x2) == 0x2;
	}

	public boolean get_onoffType() {
		return _onoffType;
	}

	public void set_onoffType(boolean val) {
		_bit |= 0x4;
		_onoffType = val;
	}

	public boolean has_onoffType() {
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
		if (has_passiveid())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _passiveid);
		if (has_param())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _param);
		if (has_onoffType())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _onoffType);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_passiveid()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_passiveid()) {
			output.wirteInt32(1, _passiveid);
		}
		if (has_param()) {
			output.wirteInt32(2, _param);
		}
		if (has_onoffType()) {
			output.writeBool(3, _onoffType);
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
					set_passiveid(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_param(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_onoffType(input.readBool());
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
			// TODO : 아래부터 처리 코드를 삽입하십시오.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_ADD_SPELL_PASSIVE_NOTI();
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
