package l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush;

import l1j.server.MJPushitem.dataloader.MJPushItemData;
import l1j.server.MJPushitem.model.MJChaPushModel;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class EventPushUpdateInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static EventPushUpdateInfo newInstance(L1PcInstance pc, int push_id, int reset_num, int state) {
		EventPushUpdateInfo noti = newInstance();
		noti.set_event_push_id(push_id);
		noti.set_reset_num(reset_num);
		for (MJChaPushModel model : pc.get_push_info()) {
			if (MJPushItemData.getlist().get(model.getPushId()).getpushid() == push_id
					&& MJPushItemData.getlist().get(model.getPushId()).getpushid() == reset_num) {
				int remaintime = (int) (MJPushItemData.getlist().get(model.getPushId()).getExpiredate()
						- System.currentTimeMillis() / 1000);
				noti.set_remain_time(remaintime > 0 ? remaintime : 0);
			}
		}
		noti.set_event_push_status(state);
		return noti;
	}

	public static EventPushUpdateInfo newInstance() {
		return new EventPushUpdateInfo();
	}

	private long _event_push_id;
	private int _reset_num;
	private int _remain_time;
	private int _event_push_status;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private EventPushUpdateInfo() {
	}

	public long get_event_push_id() {
		return _event_push_id;
	}

	public void set_event_push_id(long val) {
		_bit |= 0x1;
		_event_push_id = val;
	}

	public boolean has_event_push_id() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_reset_num() {
		return _reset_num;
	}

	public void set_reset_num(int val) {
		_bit |= 0x2;
		_reset_num = val;
	}

	public boolean has_reset_num() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_remain_time() {
		return _remain_time;
	}

	public void set_remain_time(int val) {
		_bit |= 0x4;
		_remain_time = val;
	}

	public boolean has_remain_time() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_event_push_status() {
		return _event_push_status;
	}

	public void set_event_push_status(int val) {
		_bit |= 0x8;
		_event_push_status = val;
	}

	public boolean has_event_push_status() {
		return (_bit & 0x8) == 0x8;
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
		if (has_event_push_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(1, _event_push_id);
		}
		if (has_reset_num()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _reset_num);
		}
		if (has_remain_time()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _remain_time);
		}
		if (has_event_push_status()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _event_push_status);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_event_push_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_reset_num()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_remain_time()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_event_push_status()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_event_push_id()) {
			output.writeInt64(1, _event_push_id);
		}
		if (has_reset_num()) {
			output.wirteInt32(2, _reset_num);
		}
		if (has_remain_time()) {
			output.writeUInt32(3, _remain_time);
		}
		if (has_event_push_status()) {
			output.wirteInt32(4, _event_push_status);
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
					set_event_push_id(input.readInt64());
					break;
				}
				case 0x00000010: {
					set_reset_num(input.readInt32());
					break;
				}
				case 0x00000018: {
					set_remain_time(input.readUInt32());
					break;
				}
				case 0x00000020: {
					set_event_push_status(input.readInt32());
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
		return new EventPushUpdateInfo();
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
