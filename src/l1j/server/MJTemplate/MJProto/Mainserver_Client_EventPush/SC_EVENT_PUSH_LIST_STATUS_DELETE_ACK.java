package l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush;

import l1j.server.MJPushitem.dataloader.MJPushChaData;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static ProtoOutputStream make_stream(L1PcInstance pc, CS_EVENT_PUSH_LIST_STATUS_DELETE_REQ req) {
		SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK noti = newInstance(pc, req);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK);
		noti.dispose();
		return stream;
	}

	public static SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK newInstance(L1PcInstance pc,
			CS_EVENT_PUSH_LIST_STATUS_DELETE_REQ req) {
		SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK noti = newInstance();
		for (EventPushKey val : req.get_event_push_key_list()) {
			EventPushResult result = EventPushResult.newInstance();
			EventPushKey key = EventPushKey.newInstance();
			key.set_event_push_id(val.get_event_push_id());
			key.set_reset_num(val.get_reset_num());
			result.set_event_push_key(key);
			result.set_result(EventPushResultCode.EVENT_PUSH_SUCCESS);
			noti.add_event_push_result_list(result);
			for (int i = 0; i < pc.get_push_info().size(); i++) {
				if (pc.get_push_info().get(i).getPushId() == val.get_event_push_id()
						&& pc.get_push_info().get(i).getPushId() == val.get_reset_num()) {
					pc.get_push_info().remove(i);
					break;
				}
			}
			MJPushChaData.getInstance().DeleteInfo(pc, (int) val.get_event_push_id());
		}
		noti.set_result(EventPushResultCode.EVENT_PUSH_SUCCESS);
		return noti;
	}

	public static ProtoOutputStream make_stream_timover(L1PcInstance pc, int push_id, int reset_num) {
		SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK noti = newInstance_timover(pc, push_id, reset_num);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK);
		noti.dispose();
		return stream;
	}

	public static SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK newInstance_timover(L1PcInstance pc, int push_id,
			int reset_num) {
		SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK noti = newInstance();
		EventPushResult result = EventPushResult.newInstance();
		EventPushKey key = EventPushKey.newInstance();
		key.set_event_push_id(push_id);
		key.set_reset_num(reset_num);
		result.set_event_push_key(key);
		result.set_result(EventPushResultCode.EVENT_PUSH_SUCCESS);
		noti.add_event_push_result_list(result);
		noti.set_result(EventPushResultCode.EVENT_PUSH_SUCCESS);
		MJPushChaData.getInstance().DeleteInfo(pc, push_id);
		return noti;
	}

	public static SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK newInstance() {
		return new SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK();
	}

	private java.util.LinkedList<EventPushResult> _event_push_result_list;
	private EventPushResultCode _result;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK() {
	}

	public java.util.LinkedList<EventPushResult> get_event_push_result_list() {
		return _event_push_result_list;
	}

	public void add_event_push_result_list(EventPushResult val) {
		if (!has_event_push_result_list()) {
			_event_push_result_list = new java.util.LinkedList<EventPushResult>();
			_bit |= 0x1;
		}
		_event_push_result_list.add(val);
	}

	public boolean has_event_push_result_list() {
		return (_bit & 0x1) == 0x1;
	}

	public EventPushResultCode get_result() {
		return _result;
	}

	public void set_result(EventPushResultCode val) {
		_bit |= 0x2;
		_result = val;
	}

	public boolean has_result() {
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
		if (has_event_push_result_list()) {
			for (EventPushResult val : _event_push_result_list) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_result()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _result.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (has_event_push_result_list()) {
			for (EventPushResult val : _event_push_result_list) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_result()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_event_push_result_list()) {
			for (EventPushResult val : _event_push_result_list) {
				output.writeMessage(1, val);
			}
		}
		if (has_result()) {
			output.writeEnum(2, _result.toInt());
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
					add_event_push_result_list((EventPushResult) input.readMessage(EventPushResult.newInstance()));
					break;
				}
				case 0x00000010: {
					set_result(EventPushResultCode.fromInt(input.readEnum()));
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
		return new SC_EVENT_PUSH_LIST_STATUS_DELETE_ACK();
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
