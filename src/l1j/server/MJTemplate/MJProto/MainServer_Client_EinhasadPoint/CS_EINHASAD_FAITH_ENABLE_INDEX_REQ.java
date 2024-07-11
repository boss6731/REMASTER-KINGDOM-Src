package l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Info;
import l1j.server.AinhasadSpecialStat2.AinhasadSpecialStat2Loader;
import l1j.server.AinhasadSpecialStat2.L1AinhasadFaithUserLoader;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MainServer_Client_EinhasadPoint.SC_EINHASAD_FAITH_BUFF_NOTI.eNotiType;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class CS_EINHASAD_FAITH_ENABLE_INDEX_REQ implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static CS_EINHASAD_FAITH_ENABLE_INDEX_REQ newInstance() {
		return new CS_EINHASAD_FAITH_ENABLE_INDEX_REQ();
	}

	private int _indexId;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private CS_EINHASAD_FAITH_ENABLE_INDEX_REQ() {
	}

	public int get_indexId() {
		return _indexId;
	}

	public void set_indexId(int val) {
		_bit |= 0x1;
		_indexId = val;
	}

	public boolean has_indexId() {
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
		if (has_indexId()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _indexId);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_indexId()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_indexId()) {
			output.wirteInt32(1, _indexId);
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
					set_indexId(input.readInt32());
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

			if (!Config.ServerAdSetting.EINHASAD_FAITH_SYSTEM) {
				pc.sendPackets("未公開：稍後將公開（請參考網站）");
				return this;
			}
			int resultcode = 1;
			int index = _indexId;
			// System.out.println(_indexId);

			AinhasadSpecialStat2Info info = AinhasadSpecialStat2Loader.getInstance().getSpecialStat(index);
			// System.out.println(info.get_point());
			int point = pc.getAccount().getBlessOfAinBonusPoint();
			if (info.get_point() > point) {
				resultcode = 2;
			}

			if (info != null) {

				SC_EINHASAD_FAITH_ENABLE_INDEX_ACK.send_index(pc, index, resultcode);
			}

			Timestamp endtime = new Timestamp(System.currentTimeMillis() + info.get_hours() * 60 * 60 * 1000);
			if (resultcode == 1) {
				SC_EINHASAD_FAITH_BUFF_NOTI.send_index(pc, eNotiType.NEW.toInt(), info.get_desc_id(), index);

				AinhasadSpecialStat2Info.einhasad_faith_option(pc, index, endtime, true);
				L1AinhasadFaithUserLoader.getInstance().addSpecialStat2(pc.getId(), pc.getName(), index);
				pc.getAccount().minusBlessOfAinBonusPoint(info.get_point());
				point = pc.getAccount().getBlessOfAinBonusPoint();
				SC_EINHASAD_POINT_POINT_NOTI.send_point(pc, point);
			}

			checkGroupComplete(pc);
			if (completeGroup1 || completeGroup2) {
				Iterator<Integer> iter = pc.getAinHasd_faith().keySet().iterator();
				int endTimeint = 0;
				SC_EINHASAD_FAITH_LIST_NOTI noti = SC_EINHASAD_FAITH_LIST_NOTI.newInstance();
				if (index >= 1 && index <= 4) {
					while (iter.hasNext()) {
						int number = iter.next();
						if (index >= 1 && index <= 4) {
							int comparetime = (Timestamp.valueOf(pc.getAinhasad_faith_EndTime(index).toString())
									.getDate());
							if (endTimeint == 0) {
								endTimeint = comparetime;
							}
							if (endTimeint < comparetime) {
								endTimeint = comparetime;
							}
						}
					}
					if (completeGroup1) {
						Timestamp groupEndTime = new Timestamp(endTimeint);
						SC_EINHASAD_FAITH_ENABLE_INDEX_ACK.send_group(pc, 1, 1, endTimeint / 1000);
						SC_EINHASAD_FAITH_BUFF_NOTI.send_group(pc, eNotiType.NEW.toInt(), info.get_desc_id(), 1);
						AinhasadSpecialStat2Info.einhasad_faith_option(pc, 101, groupEndTime, true);
						L1AinhasadFaithUserLoader.getInstance().addSpecialStat2(pc.getId(), pc.getName(), 101);
						// AinhasadSpecialStat2Loader.getInstance().addSpecialStat2(pc.getId(),
						// pc.getName(), 101);
						noti.send(pc, 1, 0, 1, null, true);
					} else {
						noti.send(pc, 1, 0, 1, null, false);
					}
				} else if (index >= 5 && index <= 8) {
					while (iter.hasNext()) {
						int number = iter.next();
						if (index >= 5 && index <= 8) {
							int comparetime = (Timestamp.valueOf(pc.getAinhasad_faith_EndTime(index).toString())
									.getDate());
							if (endTimeint == 0) {
								endTimeint = comparetime;
							}
							if (endTimeint < comparetime) {
								endTimeint = comparetime;
							}
						}
					}
					if (completeGroup2) {
						Timestamp groupEndTime = new Timestamp(endTimeint);
						SC_EINHASAD_FAITH_ENABLE_INDEX_ACK.send_group(pc, 2, 1, endTimeint / 1000);
						SC_EINHASAD_FAITH_BUFF_NOTI.send_group(pc, eNotiType.NEW.toInt(), info.get_desc_id(), 2);
						AinhasadSpecialStat2Info.einhasad_faith_option(pc, 102, groupEndTime, true);
						L1AinhasadFaithUserLoader.getInstance().addSpecialStat2(pc.getId(), pc.getName(), 102);
						// AinhasadSpecialStat2Loader.getInstance().addSpecialStat2(pc.getId(),
						// pc.getName(), 102);
						noti.send(pc, 1, 0, 2, null, true);
					} else {
						noti.send(pc, 1, 0, 2, null, false);
					}
				}

				for (int i = 1; i <= 8; i++) {
					if (pc.getAinHasd_faith().containsKey(i)) {
						noti.send(pc, 2, i, i >= 1 && i <= 4 ? 1 : 2, pc.getAinHasd_faith().get(i), true);
					} else {
						noti.send(pc, 2, i, i >= 1 && i <= 4 ? 1 : 2, null, false);
					}
				}
				pc.sendPackets(noti, MJEProtoMessages.SC_EINHASAD_FAITH_LIST_NOTI, true);
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	private boolean completeGroup1 = false;
	private boolean completeGroup2 = false;

	private void checkGroupComplete(L1PcInstance pc) {
		// System.out.println("1~4"+pc.isAinhasad_faith(1)+"+"+pc.isAinhasad_faith(2)+"+"+pc.isAinhasad_faith(3)+"+"+pc.isAinhasad_faith(4));
		// System.out.println("5~8"+pc.isAinhasad_faith(5)+"+"+pc.isAinhasad_faith(6)+"+"+pc.isAinhasad_faith(7)+"+"+pc.isAinhasad_faith(8));
		if (pc.isAinhasad_faith(1) && pc.isAinhasad_faith(2) && pc.isAinhasad_faith(3) && pc.isAinhasad_faith(4)) {
			completeGroup1 = true;
		}
		if (pc.isAinhasad_faith(5) && pc.isAinhasad_faith(6) && pc.isAinhasad_faith(7) && pc.isAinhasad_faith(8)) {
			completeGroup2 = true;
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance() {
		return new CS_EINHASAD_FAITH_ENABLE_INDEX_REQ();
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
