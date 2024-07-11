package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Iterator;

import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Info;
import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Loader;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.EinhasadFaithInfo.FaithInfoType;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_EINHASAD_FAITH_ENABLE_INDEX_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static void send_index(L1PcInstance pc, int index, int resultcode) {
		SC_EINHASAD_FAITH_ENABLE_INDEX_ACK ack = new SC_EINHASAD_FAITH_ENABLE_INDEX_ACK();
		ack.set_resultCode(EinhasadFaithResultCode.fromInt(resultcode));

		EinhasadFaithInfo indexinfo = EinhasadFaithInfo.newInstance();
		AinhasadSpecialStat2Info info = AinhasadSpecialStat2Loader.getInstance().getSpecialStat(index);
		indexinfo.set_type(FaithInfoType.fromInt(2));
		indexinfo.set_groupId(info.get_group());
		indexinfo.set_indexId(info.get_index());

		switch (resultcode) {
			case 1: // EINHASAD_FAITH_SUCCESS(1),
				indexinfo.set_isEnable(true);
				int time = info.get_hours();
				int timemill = time * 60 * 60 * 1000;
				long currenttime = System.currentTimeMillis();
				long endtime = (currenttime + timemill) / 1000;
				int EndTime = Long.valueOf(endtime).intValue();
				// System.out.println(EndTime);
				indexinfo.set_expiredTime(EndTime);
				break;
			case 2:// EINHASAD_FAITH_FAIL_NEED_REFRESH(2),
				break;
			case 3:// EINHASAD_FAITH_FAIL_WRONG_REQUEST(3),
				indexinfo.set_isEnable(false);
				indexinfo.set_expiredTime(0);
				break;
			case 4:// EINHASAD_FAITH_FAIL_IS_NOT_GAMESERVER(4),
				indexinfo.set_isEnable(false);
				indexinfo.set_expiredTime(0);
				break;
		}

		ack.set_enableIndex(indexinfo);
		pc.sendPackets(ack.writeTo(MJEProtoMessages.SC_EINHASAD_FAITH_ENABLE_INDEX_ACK), true);
	}

	public static void send_group(L1PcInstance pc, int group, int resultcode, int EndTime) {
		SC_EINHASAD_FAITH_ENABLE_INDEX_ACK ack = new SC_EINHASAD_FAITH_ENABLE_INDEX_ACK();
		ack.set_resultCode(EinhasadFaithResultCode.fromInt(resultcode));
		EinhasadFaithInfo groupinfo = EinhasadFaithInfo.newInstance();
		if (group == 1) {
			pc.getAinhasad_faith_EndTime(1);
			pc.getAinhasad_faith_EndTime(2);
			pc.getAinhasad_faith_EndTime(3);
			pc.getAinhasad_faith_EndTime(4);

		}
		switch (resultcode) {
			case 1:
				groupinfo.set_type(FaithInfoType.fromInt(1));
				groupinfo.set_groupId(group);
				groupinfo.set_indexId(0);
				groupinfo.set_isEnable(true);
				groupinfo.set_expiredTime(EndTime);
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
		}
		ack.set_enableGroup(groupinfo);
		pc.sendPackets(ack.writeTo(MJEProtoMessages.SC_EINHASAD_FAITH_ENABLE_INDEX_ACK), true);
	}

	public static SC_EINHASAD_FAITH_ENABLE_INDEX_ACK newInstance() {
		return new SC_EINHASAD_FAITH_ENABLE_INDEX_ACK();
	}

	private EinhasadFaithResultCode _resultCode;
	private EinhasadFaithInfo _enableIndex;
	private EinhasadFaithInfo _enableGroup;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EINHASAD_FAITH_ENABLE_INDEX_ACK() {
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

	public EinhasadFaithInfo get_enableIndex() {
		return _enableIndex;
	}

	public void set_enableIndex(EinhasadFaithInfo val) {
		_bit |= 0x2;
		_enableIndex = val;
	}

	public boolean has_enableIndex() {
		return (_bit & 0x2) == 0x2;
	}

	public EinhasadFaithInfo get_enableGroup() {
		return _enableGroup;
	}

	public void set_enableGroup(EinhasadFaithInfo val) {
		_bit |= 0x4;
		_enableGroup = val;
	}

	public boolean has_enableGroup() {
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
		if (has_enableIndex()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, _enableIndex);
		}
		if (has_enableGroup()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(3, _enableGroup);
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
		if (has_enableIndex()) {
			output.writeMessage(2, _enableIndex);
		}
		if (has_enableGroup()) {
			output.writeMessage(3, _enableGroup);
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
					set_enableIndex((EinhasadFaithInfo) input.readMessage(EinhasadFaithInfo.newInstance()));
					break;
				}
				case 0x0000001A: {
					set_enableGroup((EinhasadFaithInfo) input.readMessage(EinhasadFaithInfo.newInstance()));
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
		return new SC_EINHASAD_FAITH_ENABLE_INDEX_ACK();
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
