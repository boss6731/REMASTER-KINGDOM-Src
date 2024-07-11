package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;

import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.CharacterClass;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Nature.
public class SC_INDUN_INVITE_LIST_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_INDUN_INVITE_LIST_ACK newInstance(){
		return new SC_INDUN_INVITE_LIST_ACK();
	}
	private java.util.LinkedList<SC_INDUN_INVITE_LIST_ACK.UserInfo> _user_info_list;
	private eInviteListType _type;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_INDUN_INVITE_LIST_ACK(){
	}
	public java.util.LinkedList<SC_INDUN_INVITE_LIST_ACK.UserInfo> get_user_info_list(){
		return _user_info_list;
	}
	public void add_user_info_list(SC_INDUN_INVITE_LIST_ACK.UserInfo val){
		if(!has_user_info_list()){
			_user_info_list = new java.util.LinkedList<SC_INDUN_INVITE_LIST_ACK.UserInfo>();
			_bit |= 0x1;
		}
		_user_info_list.add(val);
	}
	public boolean has_user_info_list(){
		return (_bit & 0x1) == 0x1;
	}
	public eInviteListType get_type(){
		return _type;
	}
	public void set_type(eInviteListType val){
		_bit |= 0x2;
		_type = val;
	}
	public boolean has_type(){
		return (_bit & 0x2) == 0x2;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;
	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_user_info_list()){
			for(SC_INDUN_INVITE_LIST_ACK.UserInfo val : _user_info_list){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(1, val);
			}
		}
		if (has_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(2, _type.toInt());
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (has_user_info_list()){
			for(SC_INDUN_INVITE_LIST_ACK.UserInfo val : _user_info_list){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		if (!has_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_user_info_list()){
			for (SC_INDUN_INVITE_LIST_ACK.UserInfo val : _user_info_list){
				output.writeMessage(1, val);
			}
		}
		if (has_type()){
			output.writeEnum(2, _type.toInt());
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = 
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){
				case 0x0000000A:{
					add_user_info_list((SC_INDUN_INVITE_LIST_ACK.UserInfo)input.readMessage(SC_INDUN_INVITE_LIST_ACK.UserInfo.newInstance()));
					break;
				}
				case 0x00000010:{
					set_type(eInviteListType.fromInt(input.readEnum()));
					break;
				}
				default:{
					return this;
				}
			}
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;

			l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
			if (pc == null){
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new SC_INDUN_INVITE_LIST_ACK();
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public static class UserInfo implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static UserInfo newInstance(){
			return new UserInfo();
		}
		private String _user_name;
		private int _server_num;
		private boolean _is_online;
		private CharacterClass _class;
		private boolean _invite_setting;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private UserInfo(){
		}
		public String get_user_name(){
			return _user_name;
		}
		public void set_user_name(String val){
			_bit |= 0x1;
			_user_name = val;
		}
		public boolean has_user_name(){
			return (_bit & 0x1) == 0x1;
		}
		public int get_server_num(){
			return _server_num;
		}
		public void set_server_num(int val){
			_bit |= 0x2;
			_server_num = val;
		}
		public boolean has_server_num(){
			return (_bit & 0x2) == 0x2;
		}
		public boolean get_is_online(){
			return _is_online;
		}
		public void set_is_online(boolean val){
			_bit |= 0x4;
			_is_online = val;
		}
		public boolean has_is_online(){
			return (_bit & 0x4) == 0x4;
		}
		public CharacterClass get_class(){
			return _class;
		}
		public void set_class(CharacterClass val){
			_bit |= 0x8;
			_class = val;
		}
		public boolean has_class(){
			return (_bit & 0x8) == 0x8;
		}
		public boolean get_invite_setting(){
			return _invite_setting;
		}
		public void set_invite_setting(boolean val){
			_bit |= 0x10;
			_invite_setting = val;
		}
		public boolean has_invite_setting(){
			return (_bit & 0x10) == 0x10;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_user_name()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(1, _user_name);
			}
			if (has_server_num()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _server_num);
			}
			if (has_is_online()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(3, _is_online);
			}
			if (has_class()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(4, _class.toInt());
			}
			if (has_invite_setting()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(5, _invite_setting);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_user_name()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_server_num()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_is_online()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_user_name()){
				output.writeString(1, _user_name);
			}
			if (has_server_num()){
				output.wirteInt32(2, _server_num);
			}
			if (has_is_online()){
				output.writeBool(3, _is_online);
			}
			if (has_class()){
				output.writeEnum(4, _class.toInt());
			}
			if (has_invite_setting()){
				output.writeBool(5, _invite_setting);
			}
		}
		@Override
		public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = 
				l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + l1j.server.MJTemplate.MJProto.WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
			try{
				writeTo(stream);
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
			return stream;
		}
		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws java.io.IOException{
			while(!input.isAtEnd()){
				int tag = input.readTag();
				switch(tag){
					case 0x0000000A:{
						set_user_name(input.readString());
						break;
					}
					case 0x00000010:{
						set_server_num(input.readInt32());
						break;
					}
					case 0x00000018:{
						set_is_online(input.readBool());
						break;
					}
					case 0x00000020:{
						set_class(CharacterClass.fromInt(input.readEnum()));
						break;
					}
					case 0x00000028:{
						set_invite_setting(input.readBool());
						break;
					}
					default:{
						return this;
					}
				}
			}
			return this;
		}
		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
			l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
			try{
				readFrom(is);

				if (!isInitialized())
					return this;

				l1j.server.server.model.Instance.L1PcInstance pc = clnt.getActiveChar();
				if (pc == null){
					return this;
				}

				// TODO : 아래부터 처리 코드를 삽입하십시오. made by Nature.

			} catch (Exception e){
				e.printStackTrace();
			}
			return this;
		}
		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
			return new UserInfo();
		}
		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage reloadInstance(){
			return newInstance();
		}
		@Override
		public void dispose(){
			_bit = 0;
			_memorizedIsInitialized = -1;
		}
	}
}
