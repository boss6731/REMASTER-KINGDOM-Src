package l1j.server.MJTemplate.MJProto.MainServer_Client;

import java.io.UnsupportedEncodingException;

import MJShiftObject.Battle.MJShiftBattleCharacterInfo;
import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.ePolymorphAnonymityType;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_CHAT_MESSAGE_NOTI_PACKET implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	private static byte[] parse_name(L1PcInstance pc, ChatType type, byte[] name) throws UnsupportedEncodingException {
		if (pc.isGm() && type == ChatType.CHAT_WORLD) {
			name = "******".getBytes("MS949");
		} else if (pc.is_shift_battle()) {
			String server_description = pc.get_server_description();
			MJShiftBattleCharacterInfo cInfo = pc.get_battle_info();
			if (l1j.server.MJTemplate.MJString.isNullOrEmpty(server_description) || cInfo == null) {
				name = "身份不明的人".getBytes("MS949");
			} else {
				name = cInfo.to_name_pair().getBytes("MS949");
			}
		} else if (type == ChatType.CHAT_WORLD) {
			String names = pc.getName();
			name = names.getBytes("MS949");
		} else if (pc.getAge() != 0 && type == ChatType.CHAT_PLEDGE) {
			String names = pc.getName() + "(" + pc.getAge() + ")";
			name = names.getBytes("MS949");
		} else if (pc.getMapId() == 13005 || pc.getMapId() == 13006) {
			name = pc.getClassName().getBytes("MS949");
		} else {
			name = pc.getName().getBytes("MS949");
		}
		return name;
	}

	public static ProtoOutputStream chat_send(L1PcInstance pc, ChatType type, byte[] message, byte[] target_name,
			int target_server, byte[] link_m) throws UnsupportedEncodingException {
		SC_CHAT_MESSAGE_NOTI_PACKET noti = SC_CHAT_MESSAGE_NOTI_PACKET.newInstance();
		String t_name = null;
		// ChatType 채팅 타입
		// CHAT_RESULT 채팅 성공 및 실패 사유
		noti.set_time_t64(System.currentTimeMillis() / 1000L);
		noti.set_type(type);
		noti.set_message(message);
		// noti.set_message(message);

		noti.set_message_color(0);
		noti.set_name(parse_name(pc, noti.get_type(), pc.getName().getBytes()));
		noti.set_server_no(0);
		noti.set_object_id(pc.getId());
		noti.set_loc_x(pc.getX());
		noti.set_loc_y(pc.getY());
		noti.set_ranker_rating(pc.getRankLevel());

		if (target_name != null) {
			t_name = new String(target_name);
			L1PcInstance target = L1World.getInstance().getPlayer(t_name);
			if (target != null)
				noti.set_target_user_name(t_name);
		} else {
			noti.set_target_user_name(t_name);
		}

		// 클래스 랭킹 앞에 이미지

		if (pc.getClassRankLevel() == 1) {
			noti.set_game_class(pc.getClassNumber());
			noti.set_total_class_ranker_rating(1);
		}

		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_CHAT_MESSAGE_NOTI_PACKET);
		noti.dispose();
		return stream;
	}

	public static SC_CHAT_MESSAGE_NOTI_PACKET newInstance() {
		return new SC_CHAT_MESSAGE_NOTI_PACKET();
	}

	private long _time_t64;
	private ChatType _type;
	private byte[] _message;
	private int _message_color;
	private byte[] _name;
	private int _server_no;
	private int _object_id;
	private int _loc_x;
	private int _loc_y;
	private int _ranker_rating;
	private String _target_user_name;
	private boolean _is_server_keeper;
	private ePolymorphAnonymityType _anonymity_type;
	private String _anonymity_name;
	private int _game_class;
	private byte[] _link_message;
	private int _total_class_ranker_rating;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_CHAT_MESSAGE_NOTI_PACKET() {
	}

	public long get_time_t64() {
		return _time_t64;
	}

	public void set_time_t64(long val) {
		_bit |= 0x1;
		_time_t64 = val;
	}

	public boolean has_time_t64() {
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

	public int get_message_color() {
		return _message_color;
	}

	public void set_message_color(int val) {
		_bit |= 0x8;
		_message_color = val;
	}

	public boolean has_message_color() {
		return (_bit & 0x8) == 0x8;
	}

	public byte[] get_name() {
		return _name;
	}

	public void set_name(byte[] val) {
		_bit |= 0x10;
		_name = val;
	}

	public boolean has_name() {
		return (_bit & 0x10) == 0x10;
	}

	public int get_server_no() {
		return _server_no;
	}

	public void set_server_no(int val) {
		_bit |= 0x20;
		_server_no = val;
	}

	public boolean has_server_no() {
		return (_bit & 0x20) == 0x20;
	}

	public int get_object_id() {
		return _object_id;
	}

	public void set_object_id(int val) {
		_bit |= 0x40;
		_object_id = val;
	}

	public boolean has_object_id() {
		return (_bit & 0x40) == 0x40;
	}

	public int get_loc_x() {
		return _loc_x;
	}

	public void set_loc_x(int val) {
		_bit |= 0x80;
		_loc_x = val;
	}

	public boolean has_loc_x() {
		return (_bit & 0x80) == 0x80;
	}

	public int get_loc_y() {
		return _loc_y;
	}

	public void set_loc_y(int val) {
		_bit |= 0x100;
		_loc_y = val;
	}

	public boolean has_loc_y() {
		return (_bit & 0x100) == 0x100;
	}

	public int get_ranker_rating() {
		return _ranker_rating;
	}

	public void set_ranker_rating(int val) {
		_bit |= 0x200;
		_ranker_rating = val;
	}

	public boolean has_ranker_rating() {
		return (_bit & 0x200) == 0x200;
	}

	public String get_target_user_name() {
		return _target_user_name;
	}

	public void set_target_user_name(String val) {
		_bit |= 0x400;
		_target_user_name = val;
	}

	public boolean has_target_user_name() {
		return (_bit & 0x400) == 0x400;
	}

	public boolean get_is_server_keeper() {
		return _is_server_keeper;
	}

	public void set_is_server_keeper(boolean val) {
		_bit |= 0x800;
		_is_server_keeper = val;
	}

	public boolean has_is_server_keeper() {
		return (_bit & 0x800) == 0x800;
	}

	public ePolymorphAnonymityType get_anonymity_type() {
		return _anonymity_type;
	}

	public void set_anonymity_type(ePolymorphAnonymityType val) {
		_bit |= 0x1000;
		_anonymity_type = val;
	}

	public boolean has_anonymity_type() {
		return (_bit & 0x1000) == 0x1000;
	}

	public String get_anonymity_name() {
		return _anonymity_name;
	}

	public void set_anonymity_name(String val) {
		_bit |= 0x2000;
		_anonymity_name = val;
	}

	public boolean has_anonymity_name() {
		return (_bit & 0x2000) == 0x2000;
	}

	public int get_game_class() {
		return _game_class;
	}

	public void set_game_class(int val) {
		_bit |= 0x4000;
		_game_class = val;
	}

	public boolean has_game_class() {
		return (_bit & 0x4000) == 0x4000;
	}

	public byte[] get_link_message() {
		return _link_message;
	}

	public void set_link_message(byte[] val) {
		_bit |= 0x8000;
		_link_message = val;
	}

	public boolean has_link_message() {
		return (_bit & 0x8000) == 0x8000;
	}

	public int get_total_class_ranker_rating() {
		return _total_class_ranker_rating;
	}

	public void set_total_class_ranker_rating(int val) {
		_bit |= 0x10000;
		_total_class_ranker_rating = val;
	}

	public boolean has_total_class_ranker_rating() {
		return (_bit & 0x10000) == 0x10000;
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
		if (has_time_t64()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(1, _time_t64);
		}
		if (has_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _type.toInt());
		}
		if (has_message()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(3, _message);
		}
		if (has_message_color()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(4, _message_color);
		}
		if (has_name()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(5, _name);
		}
		if (has_server_no()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(6, _server_no);
		}
		if (has_object_id()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(7, _object_id);
		}
		if (has_loc_x()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(8, _loc_x);
		}
		if (has_loc_y()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(9, _loc_y);
		}
		if (has_ranker_rating()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(10, _ranker_rating);
		}
		if (has_target_user_name()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(11, _target_user_name);
		}
		if (has_is_server_keeper()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(12, _is_server_keeper);
		}
		if (has_anonymity_type()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(13, _anonymity_type.toInt());
		}
		if (has_anonymity_name()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(14, _anonymity_name);
		}
		if (has_game_class()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(15, _game_class);
		}
		if (has_link_message()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBytesSize(16, _link_message);
		}
		if (has_total_class_ranker_rating()) {
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(17, _total_class_ranker_rating);
		}
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_time_t64()) {
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
		if (!has_name()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException {
		if (has_time_t64()) {
			output.writeInt64(1, _time_t64);
		}
		if (has_type()) {
			output.writeEnum(2, _type.toInt());
		}
		if (has_message()) {
			output.writeBytes(3, _message);
		}
		if (has_message_color()) {
			output.writeUInt32(4, _message_color);
		}
		if (has_name()) {
			output.writeBytes(5, _name);
		}
		if (has_server_no()) {
			output.writeUInt32(6, _server_no);
		}
		if (has_object_id()) {
			output.writeUInt32(7, _object_id);
		}
		if (has_loc_x()) {
			output.writeUInt32(8, _loc_x);
		}
		if (has_loc_y()) {
			output.writeUInt32(9, _loc_y);
		}
		if (has_ranker_rating()) {
			output.wirteInt32(10, _ranker_rating);
		}
		if (has_target_user_name()) {
			output.writeString(11, _target_user_name);
		}
		if (has_is_server_keeper()) {
			output.writeBool(12, _is_server_keeper);
		}
		if (has_anonymity_type()) {
			output.writeEnum(13, _anonymity_type.toInt());
		}
		if (has_anonymity_name()) {
			output.writeString(14, _anonymity_name);
		}
		if (has_game_class()) {
			output.wirteInt32(15, _game_class);
		}
		if (has_link_message()) {
			output.writeBytes(16, _link_message);
		}
		if (has_total_class_ranker_rating()) {
			output.wirteInt32(17, _total_class_ranker_rating);
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
					set_time_t64(input.readInt64());
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
				case 0x00000020: {
					set_message_color(input.readUInt32());
					break;
				}
				case 0x0000002A: {
					set_name(input.readBytes());
					break;
				}
				case 0x00000030: {
					set_server_no(input.readUInt32());
					break;
				}
				case 0x00000038: {
					set_object_id(input.readUInt32());
					break;
				}
				case 0x00000040: {
					set_loc_x(input.readUInt32());
					break;
				}
				case 0x00000048: {
					set_loc_y(input.readUInt32());
					break;
				}
				case 0x00000050: {
					set_ranker_rating(input.readInt32());
					break;
				}
				case 0x0000005A: {
					set_target_user_name(input.readString());
					break;
				}
				case 0x00000060: {
					set_is_server_keeper(input.readBool());
					break;
				}
				case 0x00000068: {
					set_anonymity_type(ePolymorphAnonymityType.fromInt(input.readEnum()));
					break;
				}
				case 0x00000072: {
					set_anonymity_name(input.readString());
					break;
				}
				case 0x00000078: {
					set_game_class(input.readInt32());
					break;
				}
				case 0x00000082: {
					set_link_message(input.readBytes());
					break;
				}
				case 0x00000088: {
					set_total_class_ranker_rating(input.readInt32());
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
		return new SC_CHAT_MESSAGE_NOTI_PACKET();
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
