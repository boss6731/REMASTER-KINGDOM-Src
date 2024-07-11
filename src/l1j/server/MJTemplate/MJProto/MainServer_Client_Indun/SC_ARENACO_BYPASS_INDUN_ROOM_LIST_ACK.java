package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;

import l1j.server.MJTemplate.MJProto.Lineage_CommonDataTypes.IndunRoomInfo;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK newInstance(){
		return new SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK();
	}
	private SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK.eResult _result;
	private int _page_id;
	private int _total_page;
	private java.util.LinkedList<IndunRoomInfo> _room_info;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK(){
	}
	public SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK.eResult get_result(){
		return _result;
	}
	public void set_result(SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK.eResult val){
		_bit |= 0x1;
		_result = val;
	}
	public boolean has_result(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_page_id(){
		return _page_id;
	}
	public void set_page_id(int val){
		_bit |= 0x2;
		_page_id = val;
	}
	public boolean has_page_id(){
		return (_bit & 0x2) == 0x2;
	}
	public int get_total_page(){
		return _total_page;
	}
	public void set_total_page(int val){
		_bit |= 0x4;
		_total_page = val;
	}
	public boolean has_total_page(){
		return (_bit & 0x4) == 0x4;
	}
	public java.util.LinkedList<IndunRoomInfo> get_room_info(){
		return _room_info;
	}
	public void add_room_info(IndunRoomInfo val){
		if(!has_room_info()){
			_room_info = new java.util.LinkedList<IndunRoomInfo>();
			_bit |= 0x8;
		}
		_room_info.add(val);
	}
	public boolean has_room_info(){
		return (_bit & 0x8) == 0x8;
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
		if (has_result()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _result.toInt());
		}
		if (has_page_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _page_id);
		}
		if (has_total_page()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(3, _total_page);
		}
		if (has_room_info()){
			for(IndunRoomInfo val : _room_info){
				size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeMessageSize(4, val);
			}
		}
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_result()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_page_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_total_page()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (has_room_info()){
			for(IndunRoomInfo val : _room_info){
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
		if (has_result()){
			output.writeEnum(1, _result.toInt());
		}
		if (has_page_id()){
			output.writeUInt32(2, _page_id);
		}
		if (has_total_page()){
			output.writeUInt32(3, _total_page);
		}
		if (has_room_info()){
			for (IndunRoomInfo val : _room_info){
				output.writeMessage(4, val);
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
					set_result(SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK.eResult.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_page_id(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_total_page(input.readUInt32());
					break;
				}
				case 0x00000022:{
					add_room_info((IndunRoomInfo)input.readMessage(IndunRoomInfo.newInstance()));
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
		return new SC_ARENACO_BYPASS_INDUN_ROOM_LIST_ACK();
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
	public enum eResult{
		SUCCESS(1),
		ERROR_INTENAL(2),
		ERROR_NOT_EXIST_USER(3),
		ERROR_NOT_EXIST_ROOM(4),
		ERROR_ARENACO_DISCONNECTED(5),
		;
		private int value;
		eResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eResult v){
			return value == v.value;
		}
		public static eResult fromInt(int i){
			switch(i){
			case 1:
				return SUCCESS;
			case 2:
				return ERROR_INTENAL;
			case 3:
				return ERROR_NOT_EXIST_USER;
			case 4:
				return ERROR_NOT_EXIST_ROOM;
			case 5:
				return ERROR_ARENACO_DISCONNECTED;
			default:
				throw new IllegalArgumentException(String.format("無效的參數 eResult, %d", i));
			}
		}
	}
}
