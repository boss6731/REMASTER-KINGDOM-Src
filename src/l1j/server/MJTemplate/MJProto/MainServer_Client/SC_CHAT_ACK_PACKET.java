package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.UnsupportedEncodingException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_CHAT_ACK_PACKET implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	public static ProtoOutputStream chat_send(ChatType type, byte[] message, byte[] target_name, int target_server,
			byte[] link_m) throws UnsupportedEncodingException {
		SC_CHAT_ACK_PACKET noti = SC_CHAT_ACK_PACKET.newInstance();
		String t_name = null;
		noti.set_transaction_id(0);
		noti.set_type(type);
		noti.set_message(message);

		if (target_name != null) {
			t_name = new String(target_name);
			L1PcInstance target = L1World.getInstance().getPlayer(t_name);
			if (target != null)
				noti.set_target_user_name(t_name.getBytes());
		} else {
			noti.set_target_user_name(null);
		}

		noti.set_target_user_server_no(target_server);
		noti.set_result(CHAT_RESULT.CHAT_RESULT_SUCCESS);

		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_CHAT_ACK_PACKET);
		noti.dispose();
		return stream;
	}

	public static SC_CHAT_ACK_PACKET newInstance() {
		return new SC_CHAT_ACK_PACKET();
	}

	private int _transaction_id;
	private ChatType _type;
	private byte[] _message;
	private byte[] _target_user_name;
	private int _target_user_server_no;
	private CHAT_RESULT _result;
	private int _chat_ban_duration;
	private byte[] _link_message;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_CHAT_ACK_PACKET() {
	}

	public int get_transaction_id() {
		return _transaction_id;
	}

	public void set_transaction_id(int val) {
		_bit |= 0x1;
		_transaction_id = val;
	}

	public boolean has_transaction_id() {
		return (_bit & 0x1) == 0x1;
	}

	public ChatType get_type() {
		return _type;
	}

	public void set_type(ChatType val) {
		_bit |= 0x2;
		_type = val;
	}

	public boolean has_type() {
		return (_bit & 0x2) == 0x2;
	}

	public byte[] get_message() {
		return _message;
	}

	public void set_message(byte[] val) {
		_bit |= 0x4;
		_message = val;
	}

	public boolean has_message() {
		return (_bit & 0x4) == 0x4;
	}

	public byte[] get_target_user_name() {
		return _target_user_name;
	}

	public void set_target_user_name(byte[] val) {
		_bit |= 0x8;
		_target_user_name = val;
	}

	public boolean has_target_user_name() {
		return (_bit & 0x8) == 0x8;
	}

	public int get_target_user_server_no() {
		return _target_user_server_no;
	}

	public void set_target_user_server_no(int val) {
		_bit |= 0x10;
		_target_user_server_no = val;
	}

	public boolean has_target_user_server_no() {
		return (_bit & 0x10) == 0x10;
	}

	public CHAT_RESULT get_result() {
		return _result;
	}

	public void set_result(CHAT_RESULT val) {
		_bit |= 0x20;
		_result = val;
	}

	public boolean has_result() {
		return (_bit & 0x20) == 0x20;
	}

	public int get_chat_ban_duration() {
		return _chat_ban_duration;
	}

	public void set_chat_ban_duration(int val) {
		_bit |= 0x40;
		_chat_ban_duration = val;
	}

	public boolean has_chat_ban_duration() {
		return (_bit & 0x40) == 0x40;
	}

	public byte[] get_link_message() {
		return _link_message;
	}

	public void set_link_message(byte[] val) {
		_bit |= 0x80;
		_link_message = val;
	}

	public boolean has_link_message() {
		return (_bit & 0x80) == 0x80;
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
		if (has_transaction_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _transaction_id);
		}
		if (has_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _type.toInt());
		}
		if (has_message()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(3, _message);
		}
		if (has_target_user_name()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(4, _target_user_name);
		}
		if (has_target_user_server_no()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(5, _target_user_server_no);
		}
		if (has_result()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(6, _result.toInt());
		}
		if (has_chat_ban_duration()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _chat_ban_duration);
		}
		if (has_link_message()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(8, _link_message);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_transaction_id()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_type()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_message()) {
			_memorizedIsInitialized = -1;
			return false;
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
		if (has_transaction_id()) {
			output.writeUInt32(1, _transaction_id);
		}
		if (has_type()) {
			output.writeEnum(2, _type.toInt());
		}
		if (has_message()) {
			output.writeBytes(3, _message);
		}
		if (has_target_user_name()) {
			output.writeBytes(4, _target_user_name);
		}
		if (has_target_user_server_no()) {
			output.writeUInt32(5, _target_user_server_no);
		}
		if (has_result()) {
			output.writeEnum(6, _result.toInt());
		}
		if (has_chat_ban_duration()) {
			output.wirteInt32(7, _chat_ban_duration);
		}
		if (has_link_message()) {
			output.writeBytes(8, _link_message);
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
					set_transaction_id(input.readUInt32());
					break;
				}
				case 0x00000010: {
					set_type(ChatType.fromInt(input.readEnum()));
					break;
				}
				case 0x0000001A: {
					set_message(input.readBytes());
					break;
				}
				case 0x00000022: {
					set_target_user_name(input.readBytes());
					break;
				}
				case 0x00000028: {
					set_target_user_server_no(input.readUInt32());
					break;
				}
				case 0x00000030: {
					set_result(CHAT_RESULT.fromInt(input.readEnum()));
					break;
				}
				case 0x00000038: {
					set_chat_ban_duration(input.readInt32());
					break;
				}
				case 0x00000042: {
					set_link_message(input.readBytes());
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
		return new SC_CHAT_ACK_PACKET();
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
