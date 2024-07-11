package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.EinhasadFaithInfo.FaithInfoType;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send(L1PcInstance pc, int index) {
		SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI noti = SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI.newInstance();
		noti.set_resultCode(EinhasadFaithResultCode.fromInt(2));
		EinhasadFaithInfo info = EinhasadFaithInfo.newInstance();
		info.set_type(FaithInfoType.Index);
		int group = 0;
		if (index >= 1 && index <= 4) {
			group = 1;
		} else if (index >= 5 && index <= 8) {
			group = 2;
		}
		info.set_groupId(group);
		info.set_indexId(index);
		info.set_isEnable(false);
		info.set_expiredTime(0);
		noti.set_disableIndex(info);

		pc.sendPackets(noti, MJEProtoMessages.SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI, true);

	}

	public static void send_group(L1PcInstance pc, int group) {
		SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI noti = SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI.newInstance();
		noti.set_resultCode(EinhasadFaithResultCode.fromInt(2));

		EinhasadFaithInfo info = EinhasadFaithInfo.newInstance();
		info.set_type(FaithInfoType.Group);
		info.set_groupId(group);
		info.set_indexId(0);
		info.set_isEnable(false);
		info.set_expiredTime(0);
		noti.set_disableGroup(info);
		pc.sendPackets(noti, MJEProtoMessages.SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI, true);

	}

	public static SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI newInstance() {
		return new SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI();
	}

	private EinhasadFaithResultCode _resultCode;
	private EinhasadFaithInfo _disableIndex;
	private EinhasadFaithInfo _disableGroup;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI() {
	}

	public EinhasadFaithResultCode get_resultCode() {
		return _resultCode;
	}

	public void set_resultCode(EinhasadFaithResultCode val) {
		_bit |= 0x1;
		_resultCode = val;
	}

	public boolean has_resultCode() {
		return (_bit & 0x1) == 0x1;
	}

	public EinhasadFaithInfo get_disableIndex() {
		return _disableIndex;
	}

	public void set_disableIndex(EinhasadFaithInfo val) {
		_bit |= 0x2;
		_disableIndex = val;
	}

	public boolean has_disableIndex() {
		return (_bit & 0x2) == 0x2;
	}

	public EinhasadFaithInfo get_disableGroup() {
		return _disableGroup;
	}

	public void set_disableGroup(EinhasadFaithInfo val) {
		_bit |= 0x4;
		_disableGroup = val;
	}

	public boolean has_disableGroup() {
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
		if (has_resultCode()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _resultCode.toInt());
		}
		if (has_disableIndex()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _disableIndex);
		}
		if (has_disableGroup()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _disableGroup);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_resultCode()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_resultCode()) {
			output.writeEnum(1, _resultCode.toInt());
		}
		if (has_disableIndex()) {
			output.writeMessage(2, _disableIndex);
		}
		if (has_disableGroup()) {
			output.writeMessage(3, _disableGroup);
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
					set_resultCode(EinhasadFaithResultCode.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012: {
					set_disableIndex((EinhasadFaithInfo) input.readMessage(EinhasadFaithInfo.newInstance()));
					break;
				}
				case 0x0000001A: {
					set_disableGroup((EinhasadFaithInfo) input.readMessage(EinhasadFaithInfo.newInstance()));
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new SC_EINHASAD_FAITH_DISABLE_INDEX_NOTI();
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
