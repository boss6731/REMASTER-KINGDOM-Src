package l1j.server.MJTemplate.MJProto.MainServer_Client_PlaySupport;

import l1j.server.MJTemplate.MJProto.WireFormat;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJIProtoMessage;
import l1j.server.server.model.Instance.L1PcInstance;


// TODO : 자동으로 생성된 PROTO 코드입니다. made by Lenz.
public class SC_FORCE_FINISH_PLAY_SUPPORT_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage{
	public static SC_FORCE_FINISH_PLAY_SUPPORT_NOTI newInstance(){
		return new SC_FORCE_FINISH_PLAY_SUPPORT_NOTI();
	}
	private eReason _reason;
	private int _remain_time;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;
	private SC_FORCE_FINISH_PLAY_SUPPORT_NOTI(){
	}
	public eReason get_reason(){
		return _reason;
	}
	public void set_reason(eReason val){
		_bit |= 0x1;
		_reason = val;
	}
	public boolean has_reason(){
		return (_bit & 0x1) == 0x1;
	}
	public int get_remain_time(){
		return _remain_time;
	}
	public void set_remain_time(int val){
		_bit |= 0x2;
		_remain_time = val;
	}
	public boolean has_remain_time(){
		return (_bit & 0x2) == 0x2;
	}
	@Override
	public long getInitializeBit(){
		return (long)_bit;
	}
	@Override
	public int getMemorizedSerializeSizedSize(){
		return _memorizedSerializedSize;	}
	@Override
	public int getSerializedSize(){
		int size = 0;
		if (has_reason())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeEnumSize(1, _reason.toInt());
		if (has_remain_time())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeUInt32Size(2, _remain_time);
		_memorizedSerializedSize = size;
		return size;
	}
	@Override
	public boolean isInitialized(){
		if(_memorizedIsInitialized == 1)
			return true;
		if (!has_reason()){
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_remain_time()){
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}
	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException{
		if (has_reason()){
			output.writeEnum(1, _reason.toInt());
		}
		if (has_remain_time()){
			output.writeUInt32(2, _remain_time);
		}
	}
	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(l1j.server.MJTemplate.MJProto.MJEProtoMessages message){
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream =
			l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try{
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}
	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException{
		while(!input.isAtEnd()){
			int tag = input.readTag();
			switch(tag){

				case 0x00000008:{
					set_reason(eReason.fromInt(input.readEnum()));
					break;
				}
				case 0x00000010:{
					set_remain_time(input.readUInt32());
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
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes){
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE, ((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00)) + l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try{
			readFrom(is);

			if (!isInitialized())
				return this;
			L1PcInstance pc = clnt.getActiveChar();
			if (pc == null){
				return this;
			}

			// TODO : 아래부터 처리 코드를 삽입하십시오. made by Lenz.

		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	@Override
	public MJIProtoMessage copyInstance(){
		return new SC_FORCE_FINISH_PLAY_SUPPORT_NOTI();
	}
	@Override
	public MJIProtoMessage reloadInstance(){
		return newInstance();
	}
	@Override
	public void dispose(){
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
	public enum eReason{
		INVALID_MAP(1),
		INVALID_LEVEL(2),
		TIME_EXPIRE(3),
		USER_DEAD(4),
		MAP_TIME_EXPIRED(5);
		private int value;
		eReason(int val){
			value = val;
		}
		public int toInt(){
			return value;
		}
		public boolean equals(eReason v){
			return value == v.value;
		}
		public static eReason fromInt(int i){
			switch(i){
			case 1:
				return INVALID_MAP;
			case 2:
				return INVALID_LEVEL;
			case 3:
				return TIME_EXPIRE;
			case 4:
				return USER_DEAD;
			case 5:
				return MAP_TIME_EXPIRED;
			default:
				//return null;
				throw new IllegalArgumentException(String.format("invalid arguments eReason, %d", i));
			}
		}
	}
}
