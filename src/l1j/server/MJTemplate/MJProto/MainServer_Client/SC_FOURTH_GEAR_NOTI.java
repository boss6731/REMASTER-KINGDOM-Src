package l1j.server.MJTemplate.MJProto.MainServer_Client;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_FOURTH_GEAR_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static SC_FOURTH_GEAR_NOTI newInstance(L1PcInstance pc) {
		SC_FOURTH_GEAR_NOTI noti = newInstance();
		noti.set_targetId(pc.getId());
		noti.set_enable(pc.isFourgear());
		return noti;
	}

	public static ProtoOutputStream Fourth_Gear(L1PcInstance pc) {
		SC_FOURTH_GEAR_NOTI noti = newInstance(pc);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_FOURTH_GEAR_NOTI);
		noti.dispose();
		return stream;
	}

	public static SC_FOURTH_GEAR_NOTI newInstance() {
		return new SC_FOURTH_GEAR_NOTI();
	}

	private int _targetId;
	private boolean _enable;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_FOURTH_GEAR_NOTI() {
	}

	public int get_targetId() {
		return _targetId;
	}

	public void set_targetId(int val) {
		_bit |= 0x1;
		_targetId = val;
	}

	public boolean has_targetId() {
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
		if (has_targetId()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _targetId);
		}
		if (has_enable()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(2, _enable);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_targetId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_enable()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_targetId()) {
			output.wirteInt32(1, _targetId);
		}
		if (has_enable()) {
			output.writeBool(2, _enable);
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
					set_targetId(input.readInt32());
					break;
				}
				case 0x00000010: {
					set_enable(input.readBool());
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
		return new SC_FOURTH_GEAR_NOTI();
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
