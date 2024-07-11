package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ARENACO_INVITE_INDUN_ROOM_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_ARENACO_INVITE_INDUN_ROOM_ACK newInstance(){
		return new SC_ARENACO_INVITE_INDUN_ROOM_ACK();
	}
	private SC_ARENACO_INVITE_INDUN_ROOM_ACK.eInviteResult _result;
	private int _room_id;
	private long _host_arena_char_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ARENACO_INVITE_INDUN_ROOM_ACK(){
	}
	public SC_ARENACO_INVITE_INDUN_ROOM_ACK.eInviteResult get_result(){
		return _result;
	}
	public void set_result(SC_ARENACO_INVITE_INDUN_ROOM_ACK.eInviteResult val){
		_bit |= 0x1;
		_result = val;
	}
	public boolean has_result(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_room_id(){
		return _room_id;
	}
	public void set_room_id(int val){
		_bit |= 0x2;
		_room_id = val;
	}
	public boolean has_room_id(){
		return (_bit & 0x2) == 0x2;
	}
	public long get_host_arena_char_id(){
		return _host_arena_char_id;
	}
	public void set_host_arena_char_id(long val){
		_bit |= 0x4;
		_host_arena_char_id = val;
	}
	public boolean has_host_arena_char_id(){
		return (_bit & 0x4) == 0x4;
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
		if (has_room_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _room_id);
		}
		if (has_host_arena_char_id()){
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt64Size(3, _host_arena_char_id);
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
		if (!has_room_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_host_arena_char_id()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws java.io.IOException{
		if (has_result()){
			output.writeEnum(1, _result.toInt());
		}
		if (has_room_id()){
			output.writeUInt32(2, _room_id);
		}
		if (has_host_arena_char_id()){
			output.writeInt64(3, _host_arena_char_id);
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
					set_result(SC_ARENACO_INVITE_INDUN_ROOM_ACK.eInviteResult.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_room_id(input.readUInt32());
					break;
				}
				case 0x00000018:{
					set_host_arena_char_id(input.readInt64());
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
		return new SC_ARENACO_INVITE_INDUN_ROOM_ACK();
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
	public enum eInviteResult{
		eRESULT_SUCCESS(1),
		eRESULT_ERROR_REJECT(2),
		eRESULT_ERROR_NOT_EXIST_USER(3),
		eRESULT_ERROR(4),
		eRESULT_ERROR_NOT_OPEN_TIME(5),
		;
		private int value;
		eInviteResult(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eInviteResult v){
			return value == v.value;
		}
		public static eInviteResult fromInt(int i){
			switch(i){
			case 1:
				return eRESULT_SUCCESS;
			case 2:
				return eRESULT_ERROR_REJECT;
			case 3:
				return eRESULT_ERROR_NOT_EXIST_USER;
			case 4:
				return eRESULT_ERROR;
			case 5:
				return eRESULT_ERROR_NOT_OPEN_TIME;
			default:
				throw new IllegalArgumentException(String.format("無效的參數 eResult, %d", i));
			}
		}
	}
}
