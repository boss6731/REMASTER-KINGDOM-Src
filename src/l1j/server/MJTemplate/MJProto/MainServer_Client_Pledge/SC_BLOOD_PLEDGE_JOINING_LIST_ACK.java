package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream;
import l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge.ePLEDGE_JOINING_LIST_TYPE;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_BLOOD_PLEDGE_JOINING_LIST_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static ProtoOutputStream sendJoinList(L1PcInstance pc){
		SC_BLOOD_PLEDGE_JOINING_LIST_ACK noti = SC_BLOOD_PLEDGE_JOINING_LIST_ACK.newInstance();
		
		if (pc.isCrown())
			noti.set_req_type(ePLEDGE_JOINING_LIST_TYPE.ePLEDGE_JOINING_LIST_TYPE_PLEDGE);
		else
			noti.set_req_type(ePLEDGE_JOINING_LIST_TYPE.ePLEDGE_JOINING_LIST_TYPE_USER);
		
		ProtoOutputStream stream = noti.writeTo(MJEProtoMessages.SC_BLOOD_PLEDGE_JOINING_LIST_ACK);
		noti.dispose();
		return stream;
	}
	
	public static SC_BLOOD_PLEDGE_JOINING_LIST_ACK newInstance(){
		return new SC_BLOOD_PLEDGE_JOINING_LIST_ACK();
	}
	private ePLEDGE_JOINING_LIST_TYPE _req_type;
	private java.util.LinkedList<SC_BLOOD_PLEDGE_JOINING_LIST_ACK.PLEDGE_JOINING_DATA> _data;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_BLOOD_PLEDGE_JOINING_LIST_ACK(){
	}
	public ePLEDGE_JOINING_LIST_TYPE get_req_type(){
		return _req_type;
	}
	public void set_req_type(ePLEDGE_JOINING_LIST_TYPE val){
		_bit |= 0x1;
		_req_type = val;
	}
	public boolean has_req_type(){
		return (_bit & 0x1) == 0x1;
	}
	public java.util.LinkedList<SC_BLOOD_PLEDGE_JOINING_LIST_ACK.PLEDGE_JOINING_DATA> get_data(){
		return _data;
	}
	public void add_data(SC_BLOOD_PLEDGE_JOINING_LIST_ACK.PLEDGE_JOINING_DATA val){
		if(!has_data()){
			_data = new java.util.LinkedList<SC_BLOOD_PLEDGE_JOINING_LIST_ACK.PLEDGE_JOINING_DATA>();
			_bit |= 0x2;
		}
		_data.add(val);
	}
	public boolean has_data(){
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
		if (has_req_type()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _req_type.toInt());
		}
		if (has_data()){
			for(SC_BLOOD_PLEDGE_JOINING_LIST_ACK.PLEDGE_JOINING_DATA val : _data){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(2, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_req_type()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_data()){
			for(SC_BLOOD_PLEDGE_JOINING_LIST_ACK.PLEDGE_JOINING_DATA val : _data){
				if (!val.isInitialized()){
					_memorizedIsInitialized = -1;
					return false;
				}
			}
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_req_type()){
			output.writeEnum(1, _req_type.toInt());
		}
		if (has_data()){
			for (SC_BLOOD_PLEDGE_JOINING_LIST_ACK.PLEDGE_JOINING_DATA val : _data){
				output.writeMessage(2, val);
			}
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
				case 0x00000008:{
					set_req_type(ePLEDGE_JOINING_LIST_TYPE.fromInt(input.readEnum()));
					break;
				}
				case 0x00000012:{
					add_data((SC_BLOOD_PLEDGE_JOINING_LIST_ACK.PLEDGE_JOINING_DATA)input.readMessage(SC_BLOOD_PLEDGE_JOINING_LIST_ACK.PLEDGE_JOINING_DATA.newInstance()));
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

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
		return new SC_BLOOD_PLEDGE_JOINING_LIST_ACK();
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
	public static class PLEDGE_JOINING_DATA implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
		public static PLEDGE_JOINING_DATA newInstance(){
			return new PLEDGE_JOINING_DATA();
		}
		private int _pledge_uid;
		private String _pledge_name;
		private long _user_uid;
		private String _user_name;
		private String _join_msg;
		private boolean _online;
		private int _char_class;
		private int _join_date;
		private int _memorizedSerializedSize = -1;
		private byte _memorizedIsInitialized = -1;
		private int _bit;
		private PLEDGE_JOINING_DATA(){
		}
		public int get_pledge_uid(){
			return _pledge_uid;
		}
		public void set_pledge_uid(int val){
			_bit |= 0x1;
			_pledge_uid = val;
		}
		public boolean has_pledge_uid(){
			return (_bit & 0x1) == 0x1;
		}
		public String get_pledge_name(){
			return _pledge_name;
		}
		public void set_pledge_name(String val){
			_bit |= 0x2;
			_pledge_name = val;
		}
		public boolean has_pledge_name(){
			return (_bit & 0x2) == 0x2;
		}
		public long get_user_uid(){
			return _user_uid;
		}
		public void set_user_uid(long val){
			_bit |= 0x4;
			_user_uid = val;
		}
		public boolean has_user_uid(){
			return (_bit & 0x4) == 0x4;
		}
		public String get_user_name(){
			return _user_name;
		}
		public void set_user_name(String val){
			_bit |= 0x8;
			_user_name = val;
		}
		public boolean has_user_name(){
			return (_bit & 0x8) == 0x8;
		}
		public String get_join_msg(){
			return _join_msg;
		}
		public void set_join_msg(String val){
			_bit |= 0x10;
			_join_msg = val;
		}
		public boolean has_join_msg(){
			return (_bit & 0x10) == 0x10;
		}
		public boolean get_online(){
			return _online;
		}
		public void set_online(boolean val){
			_bit |= 0x20;
			_online = val;
		}
		public boolean has_online(){
			return (_bit & 0x20) == 0x20;
		}
		public int get_char_class(){
			return _char_class;
		}
		public void set_char_class(int val){
			_bit |= 0x40;
			_char_class = val;
		}
		public boolean has_char_class(){
			return (_bit & 0x40) == 0x40;
		}
		@Override
		public long getInitializeBit(){
			return (long)_bit;
		}
		@Override
		public int getMemorizedSerializeSizedSize(){
			return _memorizedSerializedSize;
		}
		public void set_join_date(int val){
			_bit |= 0x80;
			_join_date = val;
		}
		public boolean has_join_date(){
			return (_bit & 0x80) == 0x80;
		}
		@Override
		public int getSerializedSize(){
			int size = 0;
			if (has_pledge_uid()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(1, _pledge_uid);
			}
			if (has_pledge_name()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(2, _pledge_name);
			}
			if (has_user_uid()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt64Size(3, _user_uid);
			}
			if (has_user_name()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(4, _user_name);
			}
			if (has_join_msg()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeStringSize(5, _join_msg);
			}
			if (has_online()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(6, _online);
			}
			if (has_char_class()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(7, _char_class);
			}
			if (has_join_date()){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(8, _join_date);
			}
			_memorizedSerializedSize = size;
			return size;
		}
		@Override
		public boolean isInitialized(){
			if(_memorizedIsInitialized == 1)
				return true;
			if (!has_pledge_uid()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_pledge_name()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_user_uid()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_user_name()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_join_msg()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_online()){
				_memorizedIsInitialized = -1;
				return false;
			}
			if (!has_char_class()){
				_memorizedIsInitialized = -1;
				return false;
			}
			_memorizedIsInitialized = 1;
			return true;
		}
		@Override
		public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
			if (has_pledge_uid()){
				output.writeUInt32(1, _pledge_uid);
			}
			if (has_pledge_name()){
				output.writeString(2, _pledge_name);
			}
			if (has_user_uid()){
				output.wirteUInt64(3, _user_uid);
			}
			if (has_user_name()){
				output.writeString(4, _user_name);
			}
			if (has_join_msg()){
				output.writeString(5, _join_msg);
			}
			if (has_online()){
				output.writeBool(6, _online);
			}
			if (has_char_class()){
				output.wirteInt32(7, _char_class);
			}
			if (has_join_date()){
				output.wirteInt32(8, _join_date);
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
					case 0x00000008:{
						set_pledge_uid(input.readUInt32());
						break;
					}
					case 0x00000012:{
						set_pledge_name(input.readString());
						break;
					}
					case 0x00000018:{
						set_user_uid(input.readUInt64());
						break;
					}
					case 0x00000022:{
						set_user_name(input.readString());
						break;
					}
					case 0x0000002A:{
						set_join_msg(input.readString());
						break;
					}
					case 0x00000030:{
						set_online(input.readBool());
						break;
					}
					case 0x00000038:{
						set_char_class(input.readInt32());
						break;
					}
					case 0x00000040:{
						set_join_date(input.readInt32());
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

				// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

			} catch (Exception e){
				e.printStackTrace();
			}
			return this;
		}
		@Override
		public l1j.server.MJTemplate.MJProto.MJIProtoMessage copyInstance(){
			return new PLEDGE_JOINING_DATA();
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
