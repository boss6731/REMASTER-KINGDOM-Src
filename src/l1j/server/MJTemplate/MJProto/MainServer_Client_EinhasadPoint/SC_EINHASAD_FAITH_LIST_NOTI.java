package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;

import java.sql.Timestamp;

import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Info;
import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Loader;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.EinhasadFaithInfo.FaithInfoType;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_EINHASAD_FAITH_LIST_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public void send(L1PcInstance pc, int type, int index, int group, Timestamp time, boolean enable) {
		EinhasadFaithInfo data = EinhasadFaithInfo.newInstance();
		long currentTime = System.currentTimeMillis();
		long endTime;
		if (time != null) {
			endTime = Timestamp.valueOf(time.toString()).getTime();
		} else {
			endTime = 0;
		}
		int remainTime = Long.valueOf((endTime) / 1000).intValue();
		// System.out.println(index+"+"+endTime+"+"+time);
		int Index_id = index;
		if (type == 1) {
			Index_id = 0;
		}
		data.set_type(FaithInfoType.fromInt(type));
		data.set_indexId(Index_id);
		data.set_groupId(group);
		data.set_isEnable(enable);
		if (enable) {
			AinhasadSpecialStat2Info.einhasad_faith_option(pc, index, time, true);
			data.set_expiredTime(remainTime);
		} else {
			/*
			 * AinhasadSpecialStat2Info info =
			 * AinhasadSpecialStat2Loader.getInstance().getSpecialStat(index);
			 * if (info != null) {
			 * AinhasadSpecialStat2Info.einhasad_faith_option(pc, index, false);
			 * }
			 */
			data.set_expiredTime(0);
		}
		add_faithInfoList(data);
	}

	public static SC_EINHASAD_FAITH_LIST_NOTI newInstance() {
		return new SC_EINHASAD_FAITH_LIST_NOTI();
	}

	private java.util.LinkedList<EinhasadFaithInfo> _faithInfoList;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EINHASAD_FAITH_LIST_NOTI() {
	}

	public java.util.LinkedList<EinhasadFaithInfo> get_faithInfoList() {
		return _faithInfoList;
	}

	public void add_faithInfoList(EinhasadFaithInfo val) {
		if (!has_faithInfoList()) {
			_faithInfoList = new java.util.LinkedList<EinhasadFaithInfo>();
			_bit |= 0x1;
		}
		_faithInfoList.add(val);
	}

	public boolean has_faithInfoList() {
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
		if (has_faithInfoList()) {
			for (EinhasadFaithInfo val : _faithInfoList) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_faithInfoList()) {
			for (EinhasadFaithInfo val : _faithInfoList) {
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
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_faithInfoList()) {
			for (EinhasadFaithInfo val : _faithInfoList) {
				output.writeMessage(1, val);
			}
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
				case 0x0000000A: {
					add_faithInfoList((EinhasadFaithInfo) input.readMessage(EinhasadFaithInfo.newInstance()));
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
		return new SC_EINHASAD_FAITH_LIST_NOTI();
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
