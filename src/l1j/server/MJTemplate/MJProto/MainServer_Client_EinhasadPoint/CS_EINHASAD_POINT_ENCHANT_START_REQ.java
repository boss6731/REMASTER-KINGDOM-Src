package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;

import l1j.server.Config;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatInfo;
import l1j.server.AinhasadSpecialStat.AinhasadSpecialStatLoader;
import l1j.server.MJTemplate.ObjectEvent.MJObjectEventProvider;
import l1j.server.server.utils.CommonUtil;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class CS_EINHASAD_POINT_ENCHANT_START_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_EINHASAD_POINT_ENCHANT_START_REQ newInstance() {
		return new CS_EINHASAD_POINT_ENCHANT_START_REQ();
	}

	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_EINHASAD_POINT_ENCHANT_START_REQ() {
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
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
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

			if (!Config.ServerAdSetting.EINHASAD_POINT_SYSTEM) {
				pc.sendPackets("未公開：預計稍後發布（參見網站）");
				return this;
			}

			if (!pc.getSafetyZone()) {
				pc.sendPackets(6603);
				return this;
			}

			if (pc.getMapId() != 5167) {
				pc.isInsert_Tel_xym(32723 + CommonUtil.random(10), 32851 + CommonUtil.random(10), 5167, true);
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.
			AinhasadSpecialStatInfo Info = AinhasadSpecialStatLoader.getInstance().getSpecialStat(pc.getId());
			int point = pc.getAccount().getBlessOfAinBonusPoint();
			if (Info != null) {
				SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, point);
				SC_EINHASAD_POINT_ENCHANT_START_ACK.send_enchant_start(pc, Info.get_cur_enchant_level());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_EINHASAD_POINT_ENCHANT_START_REQ();
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
