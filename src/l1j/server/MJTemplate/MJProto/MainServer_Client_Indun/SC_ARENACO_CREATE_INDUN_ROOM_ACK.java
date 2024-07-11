package l1j.server.MJTemplate.MJProto.MainServer_Client_Indun;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_ARENACO_CREATE_INDUN_ROOM_ACK implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static void send_fail(L1PcInstance pc, eResult result) {
		SC_ARENACO_CREATE_INDUN_ROOM_ACK ack = newInstance();
		ack.set_result(result);
		pc.sendPackets(ack, MJEProtoMessages.SC_ARENACO_CREATE_INDUN_ROOM_ACK);
	}
	
	public static SC_ARENACO_CREATE_INDUN_ROOM_ACK newInstance(){
		return new SC_ARENACO_CREATE_INDUN_ROOM_ACK();
	}
	private SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult _result;
	private int _room_id;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_ARENACO_CREATE_INDUN_ROOM_ACK(){
	}
	public SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult get_result(){
		return _result;
	}
	public void set_result(SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult val){
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
					set_result(SC_ARENACO_CREATE_INDUN_ROOM_ACK.eResult.fromInt(input.readEnum()));
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
		return new SC_ARENACO_CREATE_INDUN_ROOM_ACK();
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
		ERROR_INTERNAL(2),
		ERROR_NOT_EXIST_USER(3),
		ERROR_EXCEED_MAX_ROOM_COUNT(4),
		ERROR_EXCEED_MAX_ARENA_COUNT(5),
		ERROR_CANNOT_CREATE_ARENA(6),
		ERROR_INVALID_TITLE(7),
		ERROR_PASSWORD(8),
		ERROR_TEAM_COUNT(9),
		ERROR_TEAM_MEMBER_COUNT(10),
		ERROR_ALLOCATE_ARENA(11),
		ERROR_JOIN_ROOM(12),
		ERROR_OTHER_ROOM_ENTERED(13),
		ERROR_LEVEL(14),
		ERROR_NOT_ENOUGH_KEY(15),
		ERROR_INVALID_FEE(16),
		ERROR_ENTER_LIMIT(17),
		ERROR_INDUN_BLOCK(18),
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
				return ERROR_INTERNAL;
			case 3:
				return ERROR_NOT_EXIST_USER;
			case 4:
				return ERROR_EXCEED_MAX_ROOM_COUNT;
			case 5:
				return ERROR_EXCEED_MAX_ARENA_COUNT;
			case 6:
				return ERROR_CANNOT_CREATE_ARENA;
			case 7:
				return ERROR_INVALID_TITLE;
			case 8:
				return ERROR_PASSWORD;
			case 9:
				return ERROR_TEAM_COUNT;
			case 10:
				return ERROR_TEAM_MEMBER_COUNT;
			case 11:
				return ERROR_ALLOCATE_ARENA;
			case 12:
				return ERROR_JOIN_ROOM;
			case 13:
				return ERROR_OTHER_ROOM_ENTERED;
			case 14:
				return ERROR_LEVEL;
			case 15:
				return ERROR_NOT_ENOUGH_KEY;
			case 16:
				return ERROR_INVALID_FEE;
			case 17:
				return ERROR_ENTER_LIMIT;
			case 18:
				return ERROR_INDUN_BLOCK;
			default:
				throw new IllegalArgumentException(String.format("無效的參數 eResult, %d", i));
			}
		}
	}
}
