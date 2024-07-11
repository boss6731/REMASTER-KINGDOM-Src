package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ARENACO_ENTER_INDUN_ROOM_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_ARENACO_ENTER_INDUN_ROOM_ACK newInstance(){
		return new SC_ARENACO_ENTER_INDUN_ROOM_ACK();
	}
	private SC_ARENACO_ENTER_INDUN_ROOM_ACK.eResult _result;
	private int _room_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ARENACO_ENTER_INDUN_ROOM_ACK(){
	}
	public SC_ARENACO_ENTER_INDUN_ROOM_ACK.eResult get_result(){
		return _result;
	}
	public void set_result(SC_ARENACO_ENTER_INDUN_ROOM_ACK.eResult val){
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
					set_result(SC_ARENACO_ENTER_INDUN_ROOM_ACK.eResult.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_room_id(input.readUInt32());
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
		return new SC_ARENACO_ENTER_INDUN_ROOM_ACK();
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
		FAIL(2),
		FAIL_TO_ALREADY_PLAY(3),
		FAIL_NOT_ENOUGH_MONEY(4),
		FAIL_INVALID_PASSWORD(5),
		FAIL_INVALID_ROOM_STATUS(6),
		FAIL_NOT_FOUND_ROOM(7),
		FAIL_FULL(8),
		FAIL_ALREADY_ENTERED_PLAYER(9),
		FAIL_OTHER_ROOM_ENTERED(10),
		FAIL_LEVEL(11),
		FAIL_INVALID_SERVER(12),
		FAIL_ENTER_LIMIT(13),
		FAIL_INDUN_BLOCK(14),
		FAIL_NOT_ENOUGH_KEY(15),
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
				return FAIL;
			case 3:
				return FAIL_TO_ALREADY_PLAY;
			case 4:
				return FAIL_NOT_ENOUGH_MONEY;
			case 5:
				return FAIL_INVALID_PASSWORD;
			case 6:
				return FAIL_INVALID_ROOM_STATUS;
			case 7:
				return FAIL_NOT_FOUND_ROOM;
			case 8:
				return FAIL_FULL;
			case 9:
				return FAIL_ALREADY_ENTERED_PLAYER;
			case 10:
				return FAIL_OTHER_ROOM_ENTERED;
			case 11:
				return FAIL_LEVEL;
			case 12:
				return FAIL_INVALID_SERVER;
			case 13:
				return FAIL_ENTER_LIMIT;
			case 14:
				return FAIL_INDUN_BLOCK;
			case 15:
				return FAIL_NOT_ENOUGH_KEY;//08.31추가
			default:
				throw new IllegalArgumentException(String.format("無效的參數 eResult, %d", i));
			}
		}
	}
}
