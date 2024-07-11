package l1j.server.MJTemplate.MJProto.Mainserver_Client_EventPush;

import java.io.UnsupportedEncodingException;

import l1j.server.MJPushitem.model.MJItemPushModel;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_EVENT_PUSH_ADD_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static ProtoOutputStream make_stream(L1PcInstance pc, MJItemPushModel model, L1Item item) {
		SC_EVENT_PUSH_ADD_NOTI noti = newInstance(pc, model, item);
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_EVENT_PUSH_ADD_NOTI);
		noti.dispose();
		return stream;
	}

	public static SC_EVENT_PUSH_ADD_NOTI newInstance(L1PcInstance pc, MJItemPushModel model, L1Item item) {
		String subject = null;
		String contents = null;
		try {
			subject = new String(model.getSubject().getBytes("utf-8"), "MS949");
			contents = new String(model.getContents().getBytes("utf-8"), "MS949");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// String tmpDecode = null;
		// CharBuffer cbuffer;
		// try {
		// cbuffer = CharBuffer.wrap((new String(subject.getBytes(),
		// "EUC-KR")).toCharArray());
		// Charset utf8charset = Charset.forName("MS949");
		// ByteBuffer bbuffer = utf8charset.encode(cbuffer);
		// tmpDecode = new String(bbuffer.array());
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		SC_EVENT_PUSH_ADD_NOTI noti = newInstance();
		EventPushInfo einfo = EventPushInfo.newInstance();
		einfo.set_event_push_id(model.getpushid());
		einfo.set_reset_num(model.getpushid());
		einfo.set_remain_time((int) (model.getExpiredate() - model.getEnabledate()));
		einfo.set_expire_date(model.getExpiredate() / 1000);
		einfo.set_enable_date(model.getEnabledate() / 1000);
		einfo.set_item_name_id(item.getItemDescId());
		einfo.set_item_amount(model.getItemCount());
		einfo.set_item_enchant(model.getEnchantlevel());
		einfo.set_bless_code(item.getBless());
		einfo.set_item_used_immediately(model.isUseitem_at_once() ? true : false);
		einfo.set_item_icon(model.isUseitem_at_once() ? 9904 : item.getGfxId());
		einfo.set_item_desc(model.isUseitem_at_once() ? "$31343" : item.getNameId());
		einfo.set_item_extra_desc(model.isUseitem_at_once() ? "艾因哈薩德祝福獎勵".getBytes() : item.getName().getBytes());
		// try {0
		einfo.set_subject(subject);
		einfo.set_text(contents);
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		einfo.set_event_push_status(0);
		einfo.set_web_url(model.getWeburl() != null ? model.getWeburl() : null);
		einfo.set_image_id(model.getImagenum() != null ? Integer.parseInt(model.getImagenum()) : null);
		noti.set_event_push_info(einfo);
		return noti;
	}

	public static SC_EVENT_PUSH_ADD_NOTI newInstance() {
		return new SC_EVENT_PUSH_ADD_NOTI();
	}

	private EventPushInfo _event_push_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_EVENT_PUSH_ADD_NOTI() {
	}

	public EventPushInfo get_event_push_info() {
		return _event_push_info;
	}

	public void set_event_push_info(EventPushInfo val) {
		_bit |= 0x1;
		_event_push_info = val;
	}

	public boolean has_event_push_info() {
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
		if (has_event_push_info()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, _event_push_info);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_event_push_info()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_event_push_info()) {
			output.writeMessage(1, _event_push_info);
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
					set_event_push_info((EventPushInfo) input.readMessage(EventPushInfo.newInstance()));
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
		return new SC_EVENT_PUSH_ADD_NOTI();
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
