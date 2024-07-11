package l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush;

import l1j.server.MJPushitem.dataloader.MJPushChaData;
import l1j.server.MJPushitem.dataloader.MJPushItemData;
import l1j.server.MJPushitem.model.MJChaPushModel;
import l1j.server.MJPushitem.model.MJItemPushModel;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_EVENT_PUSH_INFO_LIST_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static ProtoOutputStream make_stream(L1PcInstance pc) {
		SC_EVENT_PUSH_INFO_LIST_ACK noti = newInstance(pc);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_EVENT_PUSH_INFO_LIST_ACK);
		noti.dispose();
		return stream;
	}

	public static SC_EVENT_PUSH_INFO_LIST_ACK newInstance(L1PcInstance pc) {
		SC_EVENT_PUSH_INFO_LIST_ACK noti = newInstance();
		for (MJChaPushModel model : pc.get_push_info()) {
			MJItemPushModel pushinfo = MJPushItemData.getlist().get(model.getPushId());
			EventPushInfo einfo = EventPushInfo.newInstance();
			L1Item item = ItemTable.getInstance().getTemplate(pushinfo.getItemId());
			if (item == null) {
				continue;
			}
			einfo.set_event_push_id(pushinfo.getpushid());
			einfo.set_reset_num(pushinfo.getpushid());
			einfo.set_remain_time((int) (pushinfo.getEnabledate() - pushinfo.getExpiredate()));
			einfo.set_expire_date(pushinfo.getExpiredate());
			einfo.set_enable_date(pushinfo.getEnabledate());
			einfo.set_item_name_id(item.getItemDescId());
			einfo.set_item_amount(pushinfo.getItemCount());
			einfo.set_item_enchant(pushinfo.getEnchantlevel());
			einfo.set_bless_code(item.getBless());
			einfo.set_item_used_immediately(pushinfo.isUseitem_at_once());
			einfo.set_item_icon(
					MJPushItemData.getlist().get(model.getPushId()).isUseitem_at_once() ? 9904 : item.getGfxId());
			einfo.set_item_desc(
					MJPushItemData.getlist().get(model.getPushId()).isUseitem_at_once() ? "$31343" : item.getNameId());
			einfo.set_item_extra_desc(
					MJPushItemData.getlist().get(model.getPushId()).isUseitem_at_once() ? "艾因哈薩德祝福獎勵".getBytes()
							: item.getName().getBytes());
			einfo.set_subject(pushinfo.getSubject());
			einfo.set_text(pushinfo.getContents());
			einfo.set_event_push_status(model.getPushstate());
			einfo.set_web_url(pushinfo.getWeburl() != null ? pushinfo.getWeburl() : null);
			einfo.set_image_id(pushinfo.getImagenum() == null ? null : Integer.parseInt(pushinfo.getImagenum()));
			noti.add_event_push_info_list(einfo);
		}
		noti.set_result(EventPushResultCode.EVENT_PUSH_SUCCESS);
		noti.set_current_page(1);
		noti.set_total_page(pc.get_push_info().size() / 31 + 1);
		return noti;
	}

	public static SC_EVENT_PUSH_INFO_LIST_ACK newInstance() {
		return new SC_EVENT_PUSH_INFO_LIST_ACK();
	}

	private EventPushResultCode _result;
	private java.util.LinkedList<EventPushInfo> _event_push_info_list;
	private int _current_page;
	private int _total_page;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EVENT_PUSH_INFO_LIST_ACK() {
	}

	public EventPushResultCode get_result() {
		return _result;
	}

	public void set_result(EventPushResultCode val) {
		_bit |= 0x1;
		_result = val;
	}

	public boolean has_result() {
		return (_bit & 0x1) == 0x1;
	}

	public java.util.LinkedList<EventPushInfo> get_event_push_info_list() {
		return _event_push_info_list;
	}

	public void add_event_push_info_list(EventPushInfo val) {
		if (!has_event_push_info_list()) {
			_event_push_info_list = new java.util.LinkedList<EventPushInfo>();
			_bit |= 0x2;
		}
		_event_push_info_list.add(val);
	}

	public boolean has_event_push_info_list() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_current_page() {
		return _current_page;
	}

	public void set_current_page(int val) {
		_bit |= 0x4;
		_current_page = val;
	}

	public boolean has_current_page() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_total_page() {
		return _total_page;
	}

	public void set_total_page(int val) {
		_bit |= 0x8;
		_total_page = val;
	}

	public boolean has_total_page() {
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
		if (has_result()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result.toInt());
		}
		if (has_event_push_info_list()) {
			for (EventPushInfo val : _event_push_info_list) {
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
			}
		}
		if (has_current_page()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _current_page);
		}
		if (has_total_page()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _total_page);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_result()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_event_push_info_list()) {
			for (EventPushInfo val : _event_push_info_list) {
				if (!val.isInitialized()) {
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_current_page()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_total_page()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_result()) {
			output.writeEnum(1, _result.toInt());
		}
		if (has_event_push_info_list()) {
			for (EventPushInfo val : _event_push_info_list) {
				output.writeMessage(2, val);
			}
		}
		if (has_current_page()) {
			output.wirteInt32(3, _current_page);
		}
		if (has_total_page()) {
			output.wirteInt32(4, _total_page);
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
					set_result(EventPushResultCode.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012: {
					add_event_push_info_list((EventPushInfo) input.readMessage(EventPushInfo.newInstance()));
					break;
				}
				case 0x00000018: {
					set_current_page(input.readInt32());
					break;
				}
				case 0x00000020: {
					set_total_page(input.readInt32());
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
		return new SC_EVENT_PUSH_INFO_LIST_ACK();
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
